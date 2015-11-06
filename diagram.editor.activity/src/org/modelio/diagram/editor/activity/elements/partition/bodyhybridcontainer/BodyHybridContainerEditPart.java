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
                                    

package org.modelio.diagram.editor.activity.elements.partition.bodyhybridcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.GmPartitionContainer;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.PartitionContainerEditPart;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.PartitionContainerLayout;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramEditPart;
import org.modelio.diagram.elements.common.freezone.FreeZoneLayout;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;

/**
 * Specific edit part for the {@link GmBodyHybridContainer} model. Main specificity is to install the
 * {@link BodyHybridContainerLayoutEditPolicy}.
 * 
 * @author fpoyer
 */
@objid ("2af6f4c3-55b6-11e2-877f-002564c97630")
public class BodyHybridContainerEditPart extends PartitionContainerEditPart {
    @objid ("2af6f4c5-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        // Let the super class handle most of the job
        super.createEditPolicies();
        // Now just override the layout policy with our own.
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new BodyHybridContainerLayoutEditPolicy());
        // Now just override the drop policy with our own.
        installEditPolicy(ModelElementDropRequest.TYPE, new BodyHybridContainerDropEditPolicy());
    }

    @objid ("2af6f4c8-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshChildren() {
        // Additional step: update the current layout manager of the figure and
        // the state of the hybrid policies, based on the children in the Gm.
        if (this.getEditPolicy(EditPolicy.LAYOUT_ROLE) instanceof BodyHybridContainerLayoutEditPolicy) {
            Behaviour newBehaviour = computeBehaviourFromModel();
            if (!((BodyHybridContainerLayoutEditPolicy) this.getEditPolicy(EditPolicy.LAYOUT_ROLE)).getBehaviour()
                                                                                                   .equals(newBehaviour)) {
                updateBehaviour(newBehaviour);
            }
        }
        super.refreshChildren();
    }

    /**
     * This element is not meant to be an independent select-able node, but rather a child of a PartitionEditPart.
     */
    @objid ("2af6f4cb-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    /**
     * Overridden to use a FreeZoneLayout by default (rather than the PartitionContainerLayout) to prevent some
     * {@link NullPointerException} cases with the policies.
     */
    @objid ("2af6f4d1-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        IFigure fig = super.createFigure();
        // Add a negative margin border so that line borders of children overlap
        // the line border of parent (avoid multiple lines side by side)
        fig.setBorder(new MarginBorder(-1));
        // Default to FreeZoneLayout to prevent some problems with policies.
        fig.setLayoutManager(new FreeZoneLayout());
        return fig;
    }

    /**
     * Updates both the LayoutManager of the Figure and the behaviour state of the LayoutEditPolicy.
     * @param newBehaviour the new behaviour to adopt.
     */
    @objid ("2af87b3b-55b6-11e2-877f-002564c97630")
    private void updateBehaviour(Behaviour newBehaviour) {
        // Update the layout manager of the figure accordingly.
        switch (newBehaviour) {
            case HYBRID: {
                // Default to FreeZoneLayout to prevent some problems with
                // policies.
                FreeZoneLayout layoutManager = new FreeZoneLayout();
                getFigure().setLayoutManager(layoutManager);
                refreshFromStyle(getFigure(), getModelStyle());
                break;
            }
            case FREE_ZONE: {
                FreeZoneLayout layoutManager = new FreeZoneLayout();
                getFigure().setLayoutManager(layoutManager);
                refreshFromStyle(getFigure(), getModelStyle());
                break;
            }
            case PARTITION_CONTAINER: {
                PartitionContainerLayout layoutManager = new PartitionContainerLayout();
                layoutManager.setSpacing(-1);
                layoutManager.setStretchMinorAxis(true);
                getFigure().setLayoutManager(layoutManager);
                refreshFromStyle(getFigure(), getModelStyle());
                break;
            }
        }
        refreshVisuals();
        // Update state of hybrid policies
        ((BodyHybridContainerLayoutEditPolicy) this.getEditPolicy(EditPolicy.LAYOUT_ROLE)).setBehaviour(newBehaviour);
        ((BodyHybridContainerDropEditPolicy) this.getEditPolicy(ModelElementDropRequest.TYPE)).setBehaviour(newBehaviour);
    }

    /**
     * Computes the behaviour to have based on the model.
     * @return the behaviour to adopt.
     */
    @objid ("2af87b3f-55b6-11e2-877f-002564c97630")
    private Behaviour computeBehaviourFromModel() {
        GmBodyHybridContainer model = (GmBodyHybridContainer) this.getModel();
        if (!model.getChildren(GmPartitionContainer.SUB_PARTITION).isEmpty()) {
            // There are already some subpartitions: behave like a partition
            // container only (do not accept inner nodes anymore).
            return Behaviour.PARTITION_CONTAINER;
        }
        // else
        // There are no children yet or there are only inner nodes: behave like
        // hybrid (accept everything!).
        return Behaviour.HYBRID;
    }

    @objid ("2af87b44-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        // Do not read the layout data in the model, since this node is designed
        // to be a child node (so layoutData is usually null).
        // On the other hand, go read the "vertical" property to update the
        // layout if necessary.
        IFigure fig = getFigure();
        if (fig.getLayoutManager() instanceof ToolbarLayout) {
            final GmPartitionContainer gmModel = (GmPartitionContainer) getModel();
            ((ToolbarLayout) fig.getLayoutManager()).setVertical(gmModel.isVertical());
        }
    }

    /**
     * Visual hack: forward selection requests directly to the topmost partition container, thus "skipping" the
     * partition.
     */
    @objid ("2af87b47-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(final Request request) {
        if (REQ_SELECTION.equals(request.getType())) {
            // Walk up the composition stack until we meet the diagram, then apply selection to the last met PartitionContainer.
            EditPart parentPart = null;
            EditPart parentParentPart = this;
            do {
                parentPart = parentParentPart;
                parentParentPart = parentParentPart.getParent();
            } while (parentParentPart != null && !(parentParentPart instanceof AbstractDiagramEditPart));
        
            if (parentPart != null) {
                return parentPart.getTargetEditPart(request);
            }
        }
        return super.getTargetEditPart(request);
    }

    /**
     * An enumeration describing the 3 states we can be in: either behave totally like a partition container, behave
     * totally like a free zone, or behave like an hybrid because state is not yet defined.
     */
    @objid ("2af87b4f-55b6-11e2-877f-002564c97630")
    public enum Behaviour {
        /**
         * The behaviour to use is an hybrid (or more usually a combination) of both free zone and partition container
         * behaviours.
         */
        HYBRID,
        /**
         * The behaviour to use is the one of a "regular" free zone.
         */
        FREE_ZONE,
        /**
         * The behaviour to use is the one of a "regular" partition container.
         */
        PARTITION_CONTAINER;
    }

}