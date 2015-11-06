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
                                    

package org.modelio.diagram.editor.usecase.elements.actor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.editor.statik.elements.policies.SmartGeneralizationEditPolicy;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.figures.ChildFigureLineSeparator;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;
import org.modelio.diagram.elements.core.figures.borders.ShadowBorder;
import org.modelio.diagram.elements.core.figures.borders.TLBRBorder;
import org.modelio.diagram.elements.core.figures.borders.ZoomableLineBorder;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultElementDropEditPolicy;
import org.modelio.diagram.elements.core.policies.DeferringCreateNodePolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;

@objid ("5e34d33a-55b7-11e2-877f-002564c97630")
public class ActorEditPart extends GmNodeEditPart {
    @objid ("5e34d33e-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        final GradientFigure actorFigure = new GradientFigure();
        
        // Set style independent properties
        actorFigure.setOpaque(true);
        
        final ToolbarLayoutWithGrab layout = new ToolbarLayoutWithGrab();
        layout.setVertical(true);
        layout.setStretchMinorAxis(true);
        
        actorFigure.setLayoutManager(layout);
        
        // Set style dependent properties
        refreshFromStyle(actorFigure, getModelStyle());
        return actorFigure;
    }

    @objid ("5e34d344-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(ModelElementDropRequest.TYPE, new DefaultElementDropEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DeferringCreateNodePolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new SmartGeneralizationEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("5e34d348-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (aFigure instanceof GradientFigure) {
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
        
                updateSeparations(aFigure);
                updateFigureBorder(aFigure);
            }
        }
    }

    @objid ("5e34d350-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        GmActorPrimaryNode model = (GmActorPrimaryNode) this.getModel();
        this.getFigure().getParent().setConstraint(this.getFigure(), model.getLayoutData());
    }

    @objid ("5e34d354-55b7-11e2-877f-002564c97630")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        super.addChildVisual(childEditPart, index);
        
        updateSeparations(getFigure());
        updateFigureBorder(getFigure());
    }

    @objid ("5e34d359-55b7-11e2-877f-002564c97630")
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        super.removeChildVisual(childEditPart);
        
        updateSeparations(getFigure());
        updateFigureBorder(getFigure());
    }

    @objid ("5e34d35d-55b7-11e2-877f-002564c97630")
    protected void updateSeparations(IFigure aFigure) {
        final GradientFigure stateFig = (GradientFigure) aFigure;
        // Update the zone separation lines
        final TLBRBorder zoneBorder = new TLBRBorder(stateFig.getLineColor(),
                                                     stateFig.getLineWidth(),
                                                     false,
                                                     false,
                                                     true,
                                                     false);
        
        ChildFigureLineSeparator.updateSeparation(stateFig, zoneBorder);
    }

    @objid ("5e34d361-55b7-11e2-877f-002564c97630")
    private void updateFigureBorder(IFigure aFigure) {
        final GradientFigure classFig = (GradientFigure) aFigure;
        final Border inner = new ZoomableLineBorder(classFig.getLineColor(), classFig.getLineWidth());
        final Border outer = new ShadowBorder(classFig.getLineColor(), classFig.getLineWidth());
        final CompoundBorder b = new CompoundBorder(outer, inner);
        
        classFig.setBorder(b);
    }

    @objid ("5e34d364-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

}
