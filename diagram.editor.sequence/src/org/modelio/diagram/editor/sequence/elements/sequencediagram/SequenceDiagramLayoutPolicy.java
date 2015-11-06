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
                                    

package org.modelio.diagram.editor.sequence.elements.sequencediagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.sequence.elements.combinedfragment.CreateCombinedFragmentCommand;
import org.modelio.diagram.editor.sequence.elements.gate.CreateGateCommand;
import org.modelio.diagram.editor.sequence.elements.interactionuse.CreateInteractionUseCommand;
import org.modelio.diagram.editor.sequence.elements.lifeline.GmLifeline;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.commands.NodeChangeLayoutCommand;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.diagram.elements.core.policies.DiagramEditLayoutPolicy;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.CombinedFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.Gate;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionUse;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.PartDecomposition;

/**
 * LayoutPolicy that is specific to Sequence Diagram background.
 */
@objid ("d97d9b2b-55b6-11e2-877f-002564c97630")
public class SequenceDiagramLayoutPolicy extends DiagramEditLayoutPolicy {
    @objid ("d97d9b2f-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createChangeConstraintCommand(final ChangeBoundsRequest request, final EditPart child, final Object constraint) {
        if (((GmModel) child.getModel()).getRelatedElement() instanceof InteractionUse) {
            return createChangeConstraintCommandForInteractionUse(request,
                                                                  (GraphicalEditPart) child,
                                                                  constraint);
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof CombinedFragment) {
            return createChangeConstraintCommandForCombinedFragment(request,
                                                                    (GraphicalEditPart) child,
                                                                    constraint);
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof Lifeline) {
            return createChangeConstraintCommandForLifeline(child, constraint);
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof Gate) {
            return createChangeConstraintCommandForGate((GraphicalEditPart) child, constraint);
        } else {
            return super.createChangeConstraintCommand(request, child, constraint);
        }
    }

    @objid ("d97f2199-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPolicy createChildEditPolicy(final EditPart child) {
        if (child.getModel() instanceof GmLifeline) {
            DefaultNodeResizableEditPolicy policy = new DefaultNodeResizableEditPolicy();
            policy.setResizeDirections(PositionConstants.EAST_WEST);
            return policy;
        } else {
            return super.createChildEditPolicy(child);
        }
    }

    @objid ("d97f219f-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(final CreateRequest request) {
        ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
        if (Metamodel.getMClass(InteractionUse.class).getName().equals(ctx.getMetaclass())) {
            List<GmLifeline> coveredLifelines = new ArrayList<>();
            // Find the list of covered lifelines...
            Rectangle requestRect = (Rectangle) getConstraintFor(request);
            getCoveredLifelines(requestRect, coveredLifelines);
            return new CreateInteractionUseCommand((GmSequenceDiagram) getHostCompositeNode(),
                                                   ctx,
                                                   requestRect,
                                                   coveredLifelines);
        } else if (Metamodel.getMClass(CombinedFragment.class).getName().equals(ctx.getMetaclass())) {
            List<GmLifeline> coveredLifelines = new ArrayList<>();
            // Find the list of covered lifelines...
            Rectangle requestRect = (Rectangle) getConstraintFor(request);
            getCoveredLifelines(requestRect, coveredLifelines);
            return new CreateCombinedFragmentCommand((GmSequenceDiagram) getHostCompositeNode(),
                                                     ctx,
                                                     requestRect,
                                                     coveredLifelines);
        } else if (Metamodel.getMClass(Gate.class).getName().equals(ctx.getMetaclass())) {
            Rectangle requestRect = (Rectangle) getConstraintFor(request);
        
            return new CreateGateCommand((Interaction) getHostElement(),
                                         getHostCompositeNode(),
                                         ctx,
                                         requestRect,
                                         requestRect.getLocation().y);
        } else if (Metamodel.getMClass(PartDecomposition.class).getName().equals(ctx.getMetaclass())) {
            Rectangle requestRect = (Rectangle) getConstraintFor(request);
        
            return new CreatePartDecompositionCommand((Interaction) getHostElement(),
                                                      getHostCompositeNode(),
                                                      ctx,
                                                      requestRect);
        } else {
            return super.getCreateCommand(request);
        }
    }

    @objid ("d97f21a5-55b6-11e2-877f-002564c97630")
    @Override
    protected Rectangle getCurrentConstraintFor(final GraphicalEditPart child) {
        IFigure fig = child.getFigure();
        Object constraint = fig.getParent().getLayoutManager().getConstraint(fig);
        if (constraint instanceof PlacementConstraint) {
            return ((PlacementConstraint) constraint).getUpdatedBounds(child.getFigure());
        } else if (constraint instanceof Rectangle) {
            return (Rectangle) constraint;
        } else {
            return null;
        }
    }

    /**
     * Returns (0,0)
     * @see XYLayoutEditPolicy#getLayoutOrigin()
     */
    @objid ("d97f21ab-55b6-11e2-877f-002564c97630")
    @Override
    protected Point getLayoutOrigin() {
        return new Point();
    }

    @objid ("d97f21b1-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForGate(final GraphicalEditPart childEditPart, final Object constraint) {
        Rectangle rect = (Rectangle) constraint;
        if (childEditPart instanceof IPlacementConstraintProvider) {
            PlacementConstraint placementConstraint = ((IPlacementConstraintProvider) childEditPart).createPlacementConstraint((GmModel) childEditPart.getModel(),
                    rect.x,
                    rect.y,
                    rect.width,
                    rect.height);
            NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
            command.setModel(childEditPart.getModel());
            command.setConstraint(placementConstraint);
            return command;
        }
        return null;
    }

    @objid ("d97f21b9-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForInteractionUse(final ChangeBoundsRequest request, final GraphicalEditPart childEditPart, final Object constraint) {
        Rectangle rect = (Rectangle) constraint;
        PlacementConstraint placementConstraint = ((IPlacementConstraintProvider) childEditPart).createPlacementConstraint((GmModel) childEditPart.getModel(),
                                                                                                                           rect.x,
                                                                                                                           rect.y,
                                                                                                                           rect.width,
                                                                                                                           rect.height);
        NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
        command.setModel(childEditPart.getModel());
        command.setConstraint(placementConstraint);
        return command;
        //        Rectangle rect = (Rectangle) constraint;
        //        GmInteractionUse model = (GmInteractionUse) childEditPart.getModel();
        //        InteractionUse interactionUse = model.getRelatedElement();
        //        InteractionUsePlacementConstraint placementConstraint = (InteractionUsePlacementConstraint) ((IPlacementConstraintProvider) childEditPart).createPlacementConstraint((GmModel) childEditPart.getModel(),
        //                                                                                                                                                                             rect.x,
        //                                                                                                                                                                             rect.y,
        //                                                                                                                                                                             rect.width,
        //                                                                                                                                                                             rect.height);
        //        int startTime = interactionUse.getLineNumber() + request.getMoveDelta().y;
        //        int finishTime = interactionUse.getEndLineNumber() + request.getMoveDelta().y + request.getSizeDelta().height;
        //        //        NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
        //        //        command.setModel(childEditPart.getModel());
        //        //        command.setConstraint(placementConstraint);
        //        MoveResizeInteractionUseCommand command = new MoveResizeInteractionUseCommand();
        //        command.setGmInteractionUse(model);
        //        command.setNewConstraint(placementConstraint);
        //        command.setStartTime(startTime);
        //        command.setFinishTime(finishTime);
        //        return command;
    }

    @objid ("d97f21c3-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForLifeline(final EditPart childEditPart, final Object constraint) {
        Rectangle rect = (Rectangle) constraint;
        PlacementConstraint placementConstraint = ((IPlacementConstraintProvider) childEditPart).createPlacementConstraint((GmModel) childEditPart.getModel(),
                                                                                                                           rect.x,
                                                                                                                           rect.y,
                                                                                                                           rect.width,
                                                                                                                           rect.height);
        NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
        command.setModel(childEditPart.getModel());
        command.setConstraint(placementConstraint);
        return command;
    }

    @objid ("d97f21cb-55b6-11e2-877f-002564c97630")
    private void getAllChildren(final EditPart editPart, final Set<GraphicalEditPart> allChildren) {
        List<?> children = editPart.getChildren();
        for (int i = 0; i < children.size(); i++) {
            GraphicalEditPart child = (GraphicalEditPart) children.get(i);
            allChildren.add(child);
            getAllChildren(child, allChildren);
        }
    }

    @objid ("d97f21d3-55b6-11e2-877f-002564c97630")
    private void getCoveredLifelines(final Rectangle requestRect, final List<GmLifeline> coveredLifelines) {
        Set<GraphicalEditPart> allChildren = new HashSet<>();
        getAllChildren(getHost(), allChildren);
        for (GraphicalEditPart childEditPart : allChildren) {
            if (childEditPart.getModel() instanceof GmLifeline) {
                if (requestRect.intersects(childEditPart.getFigure().getBounds())) {
                    coveredLifelines.add((GmLifeline) childEditPart.getModel());
                }
            }
        }
    }

    @objid ("d97f21db-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForCombinedFragment(final ChangeBoundsRequest request, final GraphicalEditPart childEditPart, final Object constraint) {
        Rectangle rect = (Rectangle) constraint;
        PlacementConstraint placementConstraint = ((IPlacementConstraintProvider) childEditPart).createPlacementConstraint((GmModel) childEditPart.getModel(),
                                                                                                                           rect.x,
                                                                                                                           rect.y,
                                                                                                                           rect.width,
                                                                                                                           rect.height);
        NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
        command.setModel(childEditPart.getModel());
        command.setConstraint(placementConstraint);
        return command;
    }

    @objid ("d980a83f-55b6-11e2-877f-002564c97630")
    @Override
    protected Dimension getMinimumSizeFor(final GraphicalEditPart child) {
        return new Dimension(8, 0);
    }

}
