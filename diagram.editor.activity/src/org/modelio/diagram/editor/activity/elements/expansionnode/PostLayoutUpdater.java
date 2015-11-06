/*
 * Copyright 2013 Modeliosoft
 *
 * This file is part of Modelio.
 *
 * Modelio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Modelio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Modelio.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */  
                                    

package org.modelio.diagram.editor.activity.elements.expansionnode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener.Stub;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transform;
import org.eclipse.gef.EditPart;
import org.modelio.diagram.editor.activity.elements.figures.ExpansionNodeFigure;
import org.modelio.diagram.elements.common.portcontainer.LastMinuteContainerAutoResizeCommand;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint.Border;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint;
import org.modelio.diagram.elements.common.portcontainer.PortContainerFigure;
import org.modelio.diagram.elements.common.portcontainer.PortContainerLayout;
import org.modelio.diagram.elements.core.figures.IOrientableShaper.Orientation;

/**
 * A small listener used to update the orientation of the node and if necessary (== it changed) force the parent port
 * container to self update AFTER the layout has been applied.
 * 
 * @author fpoyer
 */
@objid ("2a5b5108-55b6-11e2-877f-002564c97630")
public class PostLayoutUpdater extends Stub {
    @objid ("2a5b510a-55b6-11e2-877f-002564c97630")
    private ExpansionNodeEditPart expansionNodePrimaryNodeEditPart;

    /**
     * C'tor.
     * @param expansionNodePrimaryNodeEditPart the edit part to update.
     */
    @objid ("2a5b510b-55b6-11e2-877f-002564c97630")
    public PostLayoutUpdater(ExpansionNodeEditPart expansionNodePrimaryNodeEditPart) {
        this.expansionNodePrimaryNodeEditPart = expansionNodePrimaryNodeEditPart;
    }

    @objid ("2a5b510f-55b6-11e2-877f-002564c97630")
    @Override
    public void postLayout(IFigure container) {
        GmExpansionNodePrimaryNode expansionnodeModel = (GmExpansionNodePrimaryNode) this.expansionNodePrimaryNodeEditPart.getModel();
        ExpansionNodeFigure fig = (ExpansionNodeFigure) this.expansionNodePrimaryNodeEditPart.getFigure();
        if (this.expansionNodePrimaryNodeEditPart.getFigure().getParent().getParent() instanceof PortContainerFigure) {
            // we are sure that parent's parent figure is a PortContainerFigure and have
            // a PortContainerLayout: get the current constraint for our parent figure
            // and analyse it.
            PortContainerFigure portContainerFigure = (PortContainerFigure) fig.getParent().getParent();
            PortContainerLayout portLayout = (PortContainerLayout) portContainerFigure.getLayoutManager();
            PortConstraint constraint = (PortConstraint) portLayout.getConstraint(fig.getParent());
            Border border = constraint.getReferenceBorder();
        
            // update the figure orientation accordingly.
            Orientation newOrientation = fig.setReferenceBorder(border);
            if (newOrientation != expansionnodeModel.getOrientation()) {
                expansionnodeModel.setOrientation(newOrientation);
                // Update the requested bounds by rotating them of -PI/2
                Transform transform = new Transform();
                transform.setRotation(-Math.PI / 2.0);
                Rectangle bounds = fig.getBounds().getCopy();
                fig.translateToAbsolute(bounds);
                Point newLocation = bounds.getTopRight();
                newLocation.translate(bounds.getCenter().getNegated());
                newLocation = transform.getTransformed(newLocation);
                newLocation.translate(bounds.getCenter());
                newLocation.translate(bounds.getLocation().getNegated());
        
                fig.translateToRelative(newLocation);
                fig.translateToRelative(bounds);
                Rectangle currentConstraint = (Rectangle) expansionnodeModel.getLayoutData();
                if (currentConstraint == null) {
                    expansionnodeModel.setLayoutData(new Rectangle(newLocation, bounds.getSize()
                                                                                      .getTransposed()));
                    currentConstraint = (Rectangle) expansionnodeModel.getLayoutData();
                } else {
                    currentConstraint.translate(newLocation);
                    currentConstraint.resize(bounds.getSize()
                                                   .getTransposed()
                                                   .getDifference(currentConstraint.getSize()));
                }
                this.expansionNodePrimaryNodeEditPart.getFigure()
                                                     .getParent()
                                                     .setConstraint(this.expansionNodePrimaryNodeEditPart.getFigure(),
                                                                    currentConstraint);
                EditPart parentEditPart = this.expansionNodePrimaryNodeEditPart.getParent();
                parentEditPart.refresh();
                new LastMinuteContainerAutoResizeCommand(parentEditPart).execute();
            }
            // In all case, remove this listener
            container.removeLayoutListener(this);
        }
    }

}
