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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.BorderLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.hibridcontainer.GmBodyHybridContainer;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.styles.core.IStyle;

/**
 * Base class for edit part of {@link GmBpmnLaneSetContainer}.
 */
@objid ("613a8e9a-55b6-11e2-877f-002564c97630")
public class BpmnLaneSetContainerEditPart extends GmNodeEditPart {
    @objid ("613a8e9e-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        Figure fig = new GradientFigure();
        fig.setOpaque(true);
        BpmnLaneSetContainerLayout layoutManager = new BpmnLaneSetContainerLayout();
        layoutManager.setStretchMinorAxis(true);
        layoutManager.setVertical(true);
        fig.setLayoutManager(layoutManager);
        fig.setPreferredSize(700, 200);
        fig.setMinimumSize(new Dimension(700, 200));
        // Define properties specific to style
        refreshFromStyle(fig, getModelStyle());
        return fig;
    }

    @objid ("613a8ea3-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new BpmnLaneSetContainerLayoutEditPolicy());
        //        // Remove the default DIRECT_EDIT policy: we don't want the container to
        //        // delegate direct edit requests.
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, null);
        //        // Override the default DROP policy to add one that can only understand Partitions
        installEditPolicy(ModelElementDropRequest.TYPE, new BpmnLaneSetDropEditPolicy());
    }

    @objid ("613a8ea6-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        super.refreshFromStyle(aFigure, style);
    }

    @objid ("613a8ead-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        // Update figure constraint in parent from model
        final IFigure fig = getFigure();
        final GmBpmnLaneSetContainer partitionContainerModel = (GmBpmnLaneSetContainer) this.getModel();
        if (!GmBodyHybridContainer.SUB_PARTITION.equals(partitionContainerModel.getRoleInComposition())) {
            fig.getParent().setConstraint(this.getFigure(), partitionContainerModel.getLayoutData());
        } else {
            fig.getParent().setConstraint(this.getFigure(), BorderLayout.CENTER);
        }
    }

    @objid ("613a8eb0-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        // Is selectable if is not a sub partition
        GmBpmnLaneSetContainer model = (GmBpmnLaneSetContainer) getModel();
        if (model.getRepresentedElement().getParentLane() == null) {
            return true;
        }
        return false;
    }

    @objid ("613c153c-55b6-11e2-877f-002564c97630")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        // If child doesn't have a layout data, compute one for it.
        GmNodeModel childModel = (GmNodeModel) childEditPart.getModel();
        if (childModel.getLayoutData() == null) {
            if (GmBodyHybridContainer.SUB_PARTITION.equals(childModel.getRoleInComposition())) {
                childModel.setLayoutData(Integer.valueOf(-1));
            }
        }
        super.addChildVisual(childEditPart, index);
    }

}
