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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnactivity;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.bpmn.elements.policies.BpmnBoundaryEventReparentElementCommand;
import org.modelio.diagram.editor.bpmn.elements.policies.BpmnFlowElementReparentElementCommand;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPolicy;
import org.modelio.diagram.elements.core.commands.DefaultReparentElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("6078c55f-55b6-11e2-877f-002564c97630")
public class BpmnPortContainerEditPolicy extends PortContainerEditPolicy {
    @objid ("6078c562-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        MObject hostElement = getHostElement();
        
        ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
        
        MObject elementToUnmask = ctx.getElementToUnmask();
        if (elementToUnmask != null) {
            if (getHostCompositeNode().canUnmask(elementToUnmask)) {
                Object requestConstraint = getConstraintFor(request);
                return new BpmnPortContainerCreateElementCommand(hostElement,
                                                                 getHostCompositeNode(),
                                                                 ctx,
                                                                 requestConstraint);
            } else {
                return null;
            }
        }
        
        Object requestConstraint = getConstraintFor(request);
        return new BpmnPortContainerCreateElementCommand(hostElement,
                                                         getHostCompositeNode(),
                                                         ctx,
                                                         requestConstraint);
    }

    @objid ("6078c568-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            CreateRequest createRequest = (CreateRequest) request;
            return getTargetEditPart(createRequest);
        }
        return super.getTargetEditPart(request);
    }

    @objid ("6078c56e-55b6-11e2-877f-002564c97630")
    private EditPart getTargetEditPart(CreateRequest createRequest) {
        if (createRequest.getNewObject() instanceof ModelioCreationContext) {
            final ModelioCreationContext ctx = (ModelioCreationContext) createRequest.getNewObject();
        
            if (ctx.getElementToUnmask() != null) {
                if (getHostCompositeNode().canUnmask(ctx.getElementToUnmask())) {
                    return getHost();
                } else {
                    return null;
                }
            }
        
            if (canHandle(Metamodel.getJavaInterface(Metamodel.getMClass(ctx.getMetaclass()))))
                return getHost();
        
        }
        return null;
    }

    @objid ("6078c573-55b6-11e2-877f-002564c97630")
    @Override
    protected boolean canHandle(Class<? extends MObject> metaclass) {
        return ((GmCompositeNode) getHost().getModel()).canCreate(metaclass);
    }

    @objid ("6078c57b-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createAddCommand(final EditPart child, final Object constraint) {
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

}
