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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.CreateBpmnLaneCommand;
import org.modelio.diagram.editor.bpmn.elements.policies.BpmnLaneReparentElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DiagramEditLayoutPolicy;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("6134744d-55b6-11e2-877f-002564c97630")
public class BpmnPartitionContainerEditPolicy extends DiagramEditLayoutPolicy {
    @objid ("61347450-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(final CreateRequest request) {
        if (request.getNewObjectType().equals("BpmnLane")) {
            //Element hostElement = getHostElement();
            Object requestConstraint = getConstraintFor(request);
            ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
        
            MObject elementToUnmask = ctx.getElementToUnmask();
        
            if (elementToUnmask != null) {
                if (getHostCompositeNode().canUnmask(elementToUnmask)) {
                   
                    return new CreateBpmnLaneCommand(getHostCompositeNode(),requestConstraint, ctx);
                } else {
                    return null;
                }
            }
        
            CompoundCommand compound = new CompoundCommand();
            ChangeBoundsRequest resizeContainerRequest = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
            resizeContainerRequest.setEditParts(getHost().getParent());
            resizeContainerRequest.setLocation(request.getLocation());
            resizeContainerRequest.setSizeDelta(new Dimension(0,100));    
            Command parentCommand = getHost().getParent().getCommand(resizeContainerRequest);
            compound.add(parentCommand);
            compound.add(new CreateBpmnLaneCommand(getHostCompositeNode(),requestConstraint, ctx));
        
            return compound;
        }
        return super.getCreateCommand(request);
    }

    @objid ("61347457-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(final Request request) {
        if (REQ_CREATE.equals(request.getType())) {
        
            CreateRequest createRequest = (CreateRequest) request;
            final ModelioCreationContext ctx = (ModelioCreationContext) createRequest.getNewObject();
            if (createRequest.getNewObject() instanceof ModelioCreationContext) {
                if (BpmnLaneSet.class.isAssignableFrom(Metamodel.getJavaInterface(Metamodel.getMClass(ctx.getMetaclass())))) {
                    if (ctx.getElementToUnmask() != null) {
                        if (getHostCompositeNode().canUnmask(ctx.getElementToUnmask())) {
                            return getHost();
                        } else {
                            return null;
                        }
                    }
                    return null;
                }
            }
        }
        return super.getTargetEditPart(request);
    }

    @objid ("6134745e-55b6-11e2-877f-002564c97630")
    @Override
    protected boolean canHandle(final Class<? extends MObject> metaclass) {
        return ((GmCompositeNode) getHost().getModel()).canCreate(metaclass);
    }

    @objid ("6135faba-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createAddCommand(final EditPart child, final Object constraint) {
        return new BpmnLaneReparentElementCommand(((GmModel) (getHost()).getModel()).getRelatedElement(),
                getHostCompositeNode(),
                (GmNodeModel) child.getModel(),
                constraint);
    }

    @objid ("251ba9dd-de8d-449c-8cdd-a6355569d6c8")
    @Override
    protected MObject getHostElement() {
        return getHostCompositeNode().getRelatedElement();
    }

    @objid ("5c53f914-d963-4278-9a30-fda2288bba41")
    @Override
    protected Object getConstraintFor(final CreateRequest request) {
        // Only care about request for partitions, super can handle the rest.
                final ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
                if (!ctx.getMetaclass().equals(Metamodel.getMClass(BpmnLane.class).getName())) {
           return super.getConstraintFor(request);
                }  
                
                IFigure figure = getLayoutContainer();
        return figure.getBounds();
    }

}
