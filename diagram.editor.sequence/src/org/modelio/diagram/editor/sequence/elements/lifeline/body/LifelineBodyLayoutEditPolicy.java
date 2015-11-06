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
                                    

package org.modelio.diagram.editor.sequence.elements.lifeline.body;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.GmExecutionOccurenceSpecification;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.MoveExecutionOccurenceSpecificationCommand;
import org.modelio.diagram.editor.sequence.elements.executionoccurencespecification.MoveExecutionOccurrenceSpecificationEditPolicy;
import org.modelio.diagram.editor.sequence.elements.executionspecification.CreateExecutionSpecificationCommand;
import org.modelio.diagram.editor.sequence.elements.executionspecification.ExecutionSpecificationEditPart;
import org.modelio.diagram.editor.sequence.elements.executionspecification.GmExecutionSpecification;
import org.modelio.diagram.editor.sequence.elements.executionspecification.MoveResizeExecutionSpecificationCommand;
import org.modelio.diagram.editor.sequence.elements.executionspecification.MoveResizeExecutionSpecificationEditPolicy;
import org.modelio.diagram.editor.sequence.elements.executionspecification.ReparentExecutionSpecificationCommand;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.IPlacementConstraintProvider;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.PlacementConstraint;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.CreateStateInvariantCommand;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.GmStateInvariant;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.MoveResizeStateInvariantCommand;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.ReparentStateInvariantCommand;
import org.modelio.diagram.editor.sequence.elements.stateinvariant.StateInvariantEditPart;
import org.modelio.diagram.elements.common.freezone.DefaultFreeZoneLayoutEditPolicy;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.policies.DefaultNodeNonResizableEditPolicy;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionOccurenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.StateInvariant;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Specialisation of the default freezone edit policy to handle the specific constraints of execution specifications on
 * a lifeline (centered, etc).
 * 
 * @author fpoyer
 */
@objid ("d9345d06-55b6-11e2-877f-002564c97630")
public class LifelineBodyLayoutEditPolicy extends DefaultFreeZoneLayoutEditPolicy {
    @objid ("d9345d0a-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createAddCommand(final EditPart child, final Object constraint) {
        if (((GmModel) child.getModel()).getRelatedElement() instanceof ExecutionSpecification) {
            Rectangle requestConstraint = (Rectangle) constraint;
            Rectangle tmp = requestConstraint.getCopy();
            tmp.translate(getLayoutOrigin());
            int newTime = tmp.y;
            
            ExecutionSpecification spec = (ExecutionSpecification) ((GmModel) child.getModel()).getRelatedElement();
            if (spec.getLineNumber() == -1 || newTime == spec.getLineNumber()) {
                return createAddCommandForExecution(child, constraint);
            } else {
                return null;
            }
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof StateInvariant) {
            return createAddCommandForStateInvariant(child, constraint);
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof ExecutionOccurenceSpecification) {
            return createAddCommandForBlueSquare(child, constraint);
        } else {
            return super.createAddCommand(child, constraint);
        }
    }

    @objid ("d9345d12-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createChangeConstraintCommand(final ChangeBoundsRequest request, final EditPart child, final Object constraint) {
        if (((GmModel) child.getModel()).getRelatedElement() instanceof ExecutionSpecification) {
            return createChangeConstraintCommandForExecutionSpecification(child, constraint);
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof ExecutionOccurenceSpecification) {
            return createChangeConstraintCommandForExecutionOccurenceSpecification(child, constraint);
        } else if (((GmModel) child.getModel()).getRelatedElement() instanceof StateInvariant) {
            return createChangeConstraintCommandForStateInvariant(child, constraint);
        } else {
            return super.createChangeConstraintCommand(request, child, constraint);
        }
    }

    @objid ("d9345d1c-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPolicy createChildEditPolicy(final EditPart child) {
        if (child.getModel() instanceof GmExecutionSpecification) {
            DefaultNodeResizableEditPolicy policy = new MoveResizeExecutionSpecificationEditPolicy();
            policy.setResizeDirections(PositionConstants.NORTH_SOUTH);
            return policy;
        } else if (child.getModel() instanceof GmStateInvariant) {
            DefaultNodeResizableEditPolicy policy = new DefaultNodeResizableEditPolicy();
            return policy;
        } else if (child.getModel() instanceof GmExecutionOccurenceSpecification) {
            DefaultNodeNonResizableEditPolicy policy = new MoveExecutionOccurrenceSpecificationEditPolicy();
            return policy;
        } else {
            return super.createChildEditPolicy(child);
        }
    }

    @objid ("d9345d22-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(CreateRequest req) {
        if (req.getNewObject() instanceof ModelioCreationContext) {
            MObject hostElement = getHostElement();
            ModelioCreationContext ctx = (ModelioCreationContext) req.getNewObject();
            MObject elementToUnmask = ctx.getElementToUnmask();
            if (elementToUnmask != null) {
                return getUnmaskCommand(req, hostElement, ctx, elementToUnmask);
            }
        
            MClass metaclassToCreate = Metamodel.getMClass(ctx.getMetaclass());
            if (MTools.getMetaTool().canCompose( hostElement.getMClass(),metaclassToCreate, ctx.getDependency())) {
                Rectangle requestConstraint = (Rectangle) getConstraintFor(req);
                if (ExecutionSpecification.class.isAssignableFrom(Metamodel.getJavaInterface(metaclassToCreate))) {
                    int newHeight = requestConstraint.height == -1
                            ? ExecutionSpecificationEditPart.DEFAULT_EXECUTION_HEIGHT
                            : requestConstraint.height;
                    requestConstraint.setSize(ExecutionSpecificationEditPart.EXECUTION_WIDTH, newHeight);
                    return new CreateExecutionSpecificationCommand(getHostCompositeNode(), requestConstraint);
                } else if (StateInvariant.class.isAssignableFrom(Metamodel.getJavaInterface(metaclassToCreate))) {
                    int newHeight = requestConstraint.height == -1
                            ? StateInvariantEditPart.DEFAULT_STATEINVARIANT_HEIGHT : requestConstraint.height;
                    int newWidth = requestConstraint.width == -1
                            ? StateInvariantEditPart.DEFAULT_STATEINVARIANT_WIDTH : requestConstraint.width;
                    requestConstraint.setSize(newWidth, newHeight);
                    return new CreateStateInvariantCommand(getHostCompositeNode(), requestConstraint);
                } else {
                    return new DefaultCreateElementCommand(hostElement,
                                                           getHostCompositeNode(),
                                                           ctx,
                                                           requestConstraint);
                }
            }
        
        }
        return null;
    }

    @objid ("d9345d28-55b6-11e2-877f-002564c97630")
    @Override
    protected Rectangle getCurrentConstraintFor(final GraphicalEditPart child) {
        IFigure childFigure = child.getFigure();
        return ((PlacementConstraint) childFigure.getParent().getLayoutManager().getConstraint(childFigure)).getUpdatedBounds(childFigure);
    }

    @objid ("d9345d2f-55b6-11e2-877f-002564c97630")
    @Override
    protected Point getLayoutOrigin() {
        return getHostFigure().getClientArea().getLocation();
    }

    @objid ("d9345d34-55b6-11e2-877f-002564c97630")
    private Command createAddCommandForExecution(final EditPart child, final Object constraint) {
        Rectangle requestConstraint = (Rectangle) constraint;
        Rectangle tmp = requestConstraint.getCopy();
        tmp.translate(getLayoutOrigin());
        int startTime = tmp.y;
        int finishTime = tmp.bottom();
        return new ReparentExecutionSpecificationCommand(getHostCompositeNode(),
                                                         (GmExecutionSpecification) child.getModel(),
                                                         startTime,
                                                         finishTime);
    }

    @objid ("d935e39c-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForExecutionSpecification(final EditPart child, final Object constraint) {
        Rectangle requestConstraint = (Rectangle) constraint;
        Rectangle tmp = requestConstraint.getCopy();
        tmp.translate(getLayoutOrigin());
        int startTime = tmp.y;
        int finishtime = tmp.bottom();
        MoveResizeExecutionSpecificationCommand moveResizeExecutionCommand = new MoveResizeExecutionSpecificationCommand();
        moveResizeExecutionCommand.setGmExecution((GmExecutionSpecification) child.getModel());
        moveResizeExecutionCommand.setStartTime(startTime);
        moveResizeExecutionCommand.setFinishTime(finishtime);
        return moveResizeExecutionCommand;
    }

    @objid ("d935e3a4-55b6-11e2-877f-002564c97630")
    private Command getUnmaskCommand(final CreateRequest req, final MObject hostElement, final ModelioCreationContext ctx, final MObject elementToUnmask) {
        if (getHostCompositeNode().canUnmask(elementToUnmask)) {
            return new DefaultCreateElementCommand(hostElement,
                                                   getHostCompositeNode(),
                                                   ctx,
                                                   getConstraintFor(req));
        } else {
            return null;
        }
    }

    @objid ("d935e3b5-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForExecutionOccurenceSpecification(final EditPart child, final Object constraint) {
        Rectangle requestConstraint = (Rectangle) constraint;
        Rectangle tmp = requestConstraint.getCopy();
        tmp.translate(getLayoutOrigin());
        int newTime = tmp.getCenter().y;
        MoveExecutionOccurenceSpecificationCommand moveExecutionOccurenceSpecificationCommand = new MoveExecutionOccurenceSpecificationCommand();
        moveExecutionOccurenceSpecificationCommand.setGmExecutionOccurenceSpecification((GmExecutionOccurenceSpecification) child.getModel());
        moveExecutionOccurenceSpecificationCommand.setNewTime(newTime);
        return moveExecutionOccurenceSpecificationCommand;
    }

    @objid ("d935e3bd-55b6-11e2-877f-002564c97630")
    private Command createAddCommandForStateInvariant(final EditPart child, final Object constraint) {
        Rectangle requestConstraint = (Rectangle) constraint;
        Rectangle tmp = requestConstraint.getCopy();
        tmp.translate(getLayoutOrigin());
        int startTime = tmp.y;
        int finishTime = tmp.bottom();
        return new ReparentStateInvariantCommand(getHostCompositeNode(),
                                                 (GmStateInvariant) child.getModel(),
                                                 startTime,
                                                 finishTime);
        //        return null;
    }

    @objid ("d935e3c5-55b6-11e2-877f-002564c97630")
    private Command createChangeConstraintCommandForStateInvariant(final EditPart child, final Object constraint) {
        Rectangle requestConstraint = (Rectangle) constraint;
        Rectangle tmp = requestConstraint.getCopy();
        tmp.translate(getLayoutOrigin());
        int startTime = tmp.y;
        int finishtime = tmp.bottom();
        MoveResizeStateInvariantCommand moveResizeStateInvariant = new MoveResizeStateInvariantCommand();
        moveResizeStateInvariant.setGmStateInvariant((GmStateInvariant) child.getModel());
        moveResizeStateInvariant.setStartTime(startTime);
        moveResizeStateInvariant.setFinishTime(finishtime);
        PlacementConstraint placementConstraint = ((IPlacementConstraintProvider) child).createPlacementConstraint((GmModel) child.getModel(),
                                                                                                                   tmp.x,
                                                                                                                   tmp.y,
                                                                                                                   tmp.width,
                                                                                                                   tmp.height);
        moveResizeStateInvariant.setNewLayoutData(placementConstraint);
        return moveResizeStateInvariant;
    }

    @objid ("d935e3cd-55b6-11e2-877f-002564c97630")
    private Command createAddCommandForBlueSquare(final EditPart child, final Object constraint) {
        Rectangle requestConstraint = (Rectangle) constraint;
        Rectangle tmp = requestConstraint.getCopy();
        tmp.translate(getLayoutOrigin());
        int startTime = tmp.getCenter().y();
        return new ReparentBlueSquareCommand(getHostCompositeNode(),
                                             (GmExecutionOccurenceSpecification) child.getModel(),
                                             startTime);
    }

}
