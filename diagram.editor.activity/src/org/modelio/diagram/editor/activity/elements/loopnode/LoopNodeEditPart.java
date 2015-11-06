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
                                    

package org.modelio.diagram.editor.activity.elements.loopnode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.modelio.diagram.editor.activity.elements.policies.CreateFlowEditPolicy;
import org.modelio.diagram.editor.activity.elements.policies.SmartDropEditPolicy;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.figures.RoundedBoxFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;
import org.modelio.diagram.elements.core.figures.borders.TLBRBorder;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DelegatingEditPolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;

/**
 * EditPart for a {@link GmLoopNodePrimaryNode} Node.
 * 
 * @author fpoyer
 */
@objid ("2ac620b6-55b6-11e2-877f-002564c97630")
public class LoopNodeEditPart extends GmNodeEditPart {
    @objid ("2ac620ba-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    @objid ("2ac620bf-55b6-11e2-877f-002564c97630")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        // First child: the header!
        // Children 1, 2, and 3: respectively the setup, the test, and the inner zone.
        // See GmAction constructor for detail
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        
        if (index > 0) {
            final RoundedBoxFigure roundedBoxFigure = (RoundedBoxFigure) this.getFigure();
            final TLBRBorder upperBorder = new TLBRBorder(roundedBoxFigure.getLineColor(),
                                                          roundedBoxFigure.getLineWidth(),
                                                          true,
                                                          false,
                                                          false,
                                                          false);
            child.setBorder(upperBorder);
        }
        
        getContentPane().add(child, index);
    }

    @objid ("2ac620c4-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.NODE_ROLE, new CreateFlowEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DelegatingEditPolicy());
        installEditPolicy(ModelElementDropRequest.TYPE, new SmartDropEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("2ac620c7-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        // create the figure
        RoundedBoxFigure fig = new RoundedBoxFigure();
        
        ToolbarLayoutWithGrab layout = new ToolbarLayoutWithGrab(false);
        layout.setStretchMinorAxis(true);
        fig.setLayoutManager(layout);
        
        // set style independent properties
        fig.setPreferredSize(250, 200);
        
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // return the figure
        return fig;
    }

    /**
     * Refresh this EditPart's visuals.
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    @objid ("2ac620cc-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        GmLoopNodePrimaryNode loopnodeModel = (GmLoopNodePrimaryNode) this.getModel();
        this.getFigure().getParent().setConstraint(this.getFigure(), loopnodeModel.getLayoutData());
    }

    @objid ("2ac620d0-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (aFigure instanceof RoundedBoxFigure) {
            if (!switchRepresentationMode()) {
                super.refreshFromStyle(aFigure, style);
            }
        }
    }

}
