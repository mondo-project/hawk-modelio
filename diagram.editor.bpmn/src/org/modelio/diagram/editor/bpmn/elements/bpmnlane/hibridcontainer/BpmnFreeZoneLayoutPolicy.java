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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlane.hibridcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.bpmn.elements.bpmnboundaryevent.GmBpmnBoundaryEvent;
import org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer.BpmnLaneSetContainerEditPart;
import org.modelio.diagram.editor.bpmn.elements.policies.BpmnBoundaryEventReparentElementCommand;
import org.modelio.diagram.editor.bpmn.elements.policies.BpmnFlowElementReparentElementCommand;
import org.modelio.diagram.editor.bpmn.elements.policies.CreateBpmnFlowElementCommand;
import org.modelio.diagram.elements.common.freezone.DefaultFreeZoneLayoutEditPolicy;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.DefaultReparentElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.link.CreateBendedConnectionRequest;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * LayoutPolicy of Bpmn Free Zone
 */
@objid ("61347421-55b6-11e2-877f-002564c97630")
public class BpmnFreeZoneLayoutPolicy extends DefaultFreeZoneLayoutEditPolicy {
    @objid ("61347425-55b6-11e2-877f-002564c97630")
    @Override
    protected boolean canHandle(Class<? extends MObject> metaclass) {
        if (BpmnFlowElement.class.isAssignableFrom(metaclass) || BpmnLane.class.isAssignableFrom(metaclass)) {
            return true;
        }
        return false;
    }

    @objid ("6134742d-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType().equals("BpmnLane")) {
            MObject hostElement = getHostElement();
            ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
            MObject elementToUnmask = ctx.getElementToUnmask();
            if (elementToUnmask != null) {
                if (getHostCompositeNode().canUnmask(elementToUnmask)) {
                    Object requestConstraint = getConstraintFor(request);
                    return new CreateBpmnFreeZoneLaneCommand(hostElement,
                            getHostCompositeNode(),
                            ctx,
                            requestConstraint);
                } else {
                    return null;
                }
            }
        
            Object requestConstraint = getConstraintFor(request);
            return new CreateBpmnFreeZoneLaneCommand(hostElement,
                    getHostCompositeNode(),
                    ctx,
                    requestConstraint);
        } else {
            MObject hostElement = getHostElement();
            ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
            MClass metaclassToCreate = Metamodel.getMClass(ctx.getMetaclass());
        
            while (hostElement instanceof BpmnLane) {
                hostElement = hostElement.getCompositionOwner().getCompositionOwner();
            }
            MObject elementToUnmask = ctx.getElementToUnmask();
            if (elementToUnmask != null) {
                if (getHostCompositeNode().canUnmask(elementToUnmask)) {
                    Object requestConstraint = getConstraintFor(request);
                    return new DefaultCreateElementCommand(hostElement,
                            getHostCompositeNode(),
                            ctx,
                            requestConstraint);
                } else {
                    return null;
                }
            }
        
            boolean returnCommand = MTools.getMetaTool().canCompose(hostElement.getMClass(), metaclassToCreate, null);
        
            if (returnCommand) {
                Object requestConstraint = getConstraintFor(request);
                return new CreateBpmnFlowElementCommand(hostElement,
                        getHostCompositeNode(),
                        ctx,
                        requestConstraint);
            }
        }
        return null;
    }

    @objid ("61347433-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createAddCommand(EditPart child, Object constraint) {
        GmNodeModel gmmodel = (GmNodeModel) child.getModel();
        MObject element = gmmodel.getRelatedElement();
        if (element instanceof BpmnBoundaryEvent) {
            return new BpmnBoundaryEventReparentElementCommand(getHostElement(),
                    getHostCompositeNode(),
                    (GmNodeModel) child.getModel(),
                    constraint);
        } else if (element instanceof BpmnFlowElement) {
            return new BpmnFlowElementReparentElementCommand(getHostElement(),
                    getHostCompositeNode(),
                    (GmNodeModel) child.getModel(),
                    constraint);
        } else {
            return new DefaultReparentElementCommand(getHostElement(),
                    getHostCompositeNode(),
                    (GmNodeModel) child.getModel(),
                    constraint);
        }
    }

    @objid ("61347439-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (RequestConstants.REQ_CONNECTION_START.equals(request.getType())) {
            CreateBendedConnectionRequest crequest = (CreateBendedConnectionRequest) request;
            if (crequest.getTargetEditPart() instanceof BpmnLaneSetContainerEditPart) {
                return null;
            }
        } else if (RequestConstants.REQ_CONNECTION_END.equals(request.getType())) {
            CreateConnectionRequest crequest = (CreateConnectionRequest) request;
        
            if (crequest.getTargetEditPart() instanceof BpmnLaneSetContainerEditPart) {
                return null;
            }
        } else if (RequestConstants.REQ_CREATE.equals(request.getType())) {
            CreateRequest crequest = (CreateRequest) request;
            if (crequest.getNewObjectType().equals(Metamodel.getMClass(BpmnBoundaryEvent.class).getName()) ||
                    crequest.getNewObjectType().equals(Metamodel.getMClass(BpmnLaneSet.class).getName())) {
                return null;
            }
        } else if (RequestConstants.REQ_ADD.equals(request.getType())) {
            ChangeBoundsRequest add_request = (ChangeBoundsRequest) request;
            for (Object element : add_request.getEditParts()) {
                EditPart editpart = (EditPart) element;
                if (editpart.getModel() instanceof GmBpmnBoundaryEvent)
                    return null;
            }
        
        }
        return super.getTargetEditPart(request);
    }

}
