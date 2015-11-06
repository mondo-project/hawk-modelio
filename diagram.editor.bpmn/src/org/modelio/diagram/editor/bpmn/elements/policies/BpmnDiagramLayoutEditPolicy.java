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
                                    

package org.modelio.diagram.editor.bpmn.elements.policies;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.BpmnLaneEditPart;
import org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer.CreateBpmnLaneSetContainerCommand;
import org.modelio.diagram.elements.core.commands.DefaultReparentElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.commands.NodeChangeLayoutCommand;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DiagramEditLayoutPolicy;
import org.modelio.diagram.elements.drawings.core.GmDrawing;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("6219560b-55b6-11e2-877f-002564c97630")
public class BpmnDiagramLayoutEditPolicy extends DiagramEditLayoutPolicy {
    @objid ("84c70758-37c8-462c-990c-5f904ba25a08")
    private static final int POOL_Y_MIN_MARGING = 40;

    @objid ("6219560e-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType().equals("BpmnLaneSet")) {
            MObject hostElement = getHostElement();
        
            ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
        
            MObject elementToUnmask = ctx.getElementToUnmask();
        
            if (elementToUnmask != null) {
                if (getHostCompositeNode().canUnmask(elementToUnmask)) {
                    Object requestConstraint = getConstraintFor(request);
                    return new CreateBpmnLaneSetContainerCommand(hostElement, getHostCompositeNode(), ctx, requestConstraint);
                } else {
                    return null;
                }
            }
            CompoundCommand compCommand = new CompoundCommand();
                    
            Object requestConstraint = getConstraintFor(request);                
            List<BpmnLaneEditPart> childrens =  findMovableChildrens();
            for (Command command : createMoveCommands(childrens, null,(Rectangle)requestConstraint)) {
                compCommand.add(command);
            }
            compCommand.add( new CreateBpmnLaneSetContainerCommand(hostElement, getHostCompositeNode(), ctx, requestConstraint));
            return compCommand;
        }
        return super.getCreateCommand(request);
    }

    @objid ("62195614-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            CreateRequest createRequest = (CreateRequest) request;
            if (createRequest.getNewObject() instanceof ModelioCreationContext) {
                final ModelioCreationContext ctx = (ModelioCreationContext) createRequest.getNewObject();
                MObject element = ctx.getElementToUnmask();
                if (element instanceof BpmnLaneSet) {
                    if (canHandle(Metamodel.getJavaInterface(Metamodel.getMClass(ctx.getMetaclass()))))
                        return getHost();
                }
        
            }
        }
        return super.getTargetEditPart(request);
    }

    @objid ("6219561a-55b6-11e2-877f-002564c97630")
    @Override
    protected boolean canHandle(Class<? extends MObject> metaclass) {
        return ((GmCompositeNode) getHost().getModel()).canCreate(metaclass);
    }

    @objid ("621adc7a-55b6-11e2-877f-002564c97630")
    @Override
    protected MObject getHostElement() {
        // AbstractDiagram diagram = (AbstractDiagram) super.getHostElement();
        // return diagram.getOrigin();
        return getRoot(getHostCompositeNode().getRelatedElement());
    }

    @objid ("621adc81-55b6-11e2-877f-002564c97630")
    public MObject getRoot(MObject element) {
        if (element instanceof AbstractDiagram) {
            return ((AbstractDiagram) element).getOrigin();
        } else if (element instanceof BpmnProcess || element instanceof BpmnSubProcess) {
            return element;
        }
        return getRoot(element.getCompositionOwner());
    }

    @objid ("621adc8a-55b6-11e2-877f-002564c97630")
    @Override
    protected Command createAddCommand(final EditPart child, final Object constraint) {
        GmNodeModel gmmodel = (GmNodeModel) child.getModel();
        MObject element = gmmodel.getRelatedElement();
        if (element instanceof BpmnBoundaryEvent) {
        
            if (getHostElement() instanceof BpmnActivity) {
                return new BpmnBoundaryEventReparentElementCommand(getHostElement(), getHostCompositeNode(), (GmNodeModel) child.getModel(), constraint);
            }
        } else if (element instanceof BpmnLane) {
            return new BpmnLaneReparentElementCommand(getHostElement(), getHostCompositeNode(), (GmNodeModel) child.getModel(), constraint);
        } else if (element instanceof BpmnFlowElement) {
            return new BpmnFlowElementReparentElementCommand(getHostElement(), getHostCompositeNode(), (GmNodeModel) child.getModel(), constraint);
        } else {
            return new DefaultReparentElementCommand(getHostElement(), getHostCompositeNode(), (GmNodeModel) child.getModel(), constraint);
        }
        return null;
    }

    @objid ("c570ee6b-3811-45ee-aaeb-bffc7c0e11e4")
    @Override
    protected Command createChangeConstraintCommand(EditPart movedEditPart, Object constraint) {
        // if child is a 'node' it usually can be resized and/or moved
        if (movedEditPart instanceof GmNodeEditPart || movedEditPart.getModel() instanceof GmDrawing) {
            Rectangle constrRect = (Rectangle) constraint;
            // If it's a BPMNLane, we move also the others BPMNLane of the
            // diagram
            if (movedEditPart instanceof BpmnLaneEditPart) {
                CompoundCommand compCommand = new CompoundCommand();
                BpmnLaneEditPart movedLane = (BpmnLaneEditPart) movedEditPart;
                Rectangle delta = calculateDelta(movedLane.getFigure().getBounds(), constrRect);
                
                Rectangle newPos = null;
        
                List<BpmnLaneEditPart> childrens =  findMovableChildrens();
                if (delta.height != 0 || delta.width != 0) {
                    // Resize                    
                    for (Command command : createResizeCommands(childrens, movedLane, delta)) {
                        compCommand.add(command);
                    }
                    
                } else if (delta.x != 0 || delta.y != 0) {
                    // Move
                    for (Command command : createMoveCommands(childrens, movedLane,constrRect)) {
                        compCommand.add(command);
                    }
                }
                return compCommand.unwrap();
        
            } else {
                final NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
                command.setModel(movedEditPart.getModel());
                command.setConstraint(constraint);
                return command;
            }
        }
        return null;
    }

    @objid ("b813fada-ae37-45fa-aefd-e17130f44b88")
    private List<BpmnLaneEditPart> findMovableChildrens() {
        List<BpmnLaneEditPart>  result = new ArrayList<>();
        for (EditPart children : new ArrayList<EditPart>(getHost().getChildren())) {
            if (children instanceof BpmnLaneEditPart) {
                result.add((BpmnLaneEditPart)children);
            }
        }
        return result;
    }

    @objid ("bd14ee46-4a95-4af4-a3d0-ab69c22d5db9")
    private Rectangle calculateDelta(Rectangle actualPos, Rectangle newPos) {
        if(newPos.height == -1 || newPos.height == -1){
             return new Rectangle(newPos.x - actualPos.x, newPos.y - actualPos.y,0,0);
        }
        return new Rectangle(newPos.x - actualPos.x, newPos.y - actualPos.y, newPos.width - actualPos.width, newPos.height - actualPos.height);
    }

    @objid ("e18d55f0-3908-4cb9-8da9-b4422c0d8ef0")
    private List<Command> createResizeCommands(List<BpmnLaneEditPart> impactedElements, BpmnLaneEditPart movedElement, Rectangle delta) {
        List<Command> result = new ArrayList<>();
        Rectangle refPos = movedElement.getFigure().getBounds();
        
        for(BpmnLaneEditPart editpart : impactedElements){
            
            Rectangle actualPos = editpart.getFigure().getBounds();    
            Rectangle newPos = null;
            
            if (editpart.equals(movedElement)) {
                 newPos = new Rectangle(actualPos.x + delta.x, actualPos.y + delta.y, actualPos.width + delta.width, actualPos.height + delta.height);
            } else {
               if (refPos.y < actualPos.y && delta.y == 0) {
                     newPos = new Rectangle(actualPos.x + delta.x, actualPos.y + delta.height, actualPos.width + delta.width, actualPos.height);
                } else if (refPos.y > actualPos.y && delta.y != 0) {
                     newPos = new Rectangle(actualPos.x + delta.x, actualPos.y - delta.height, actualPos.width  + delta.width, actualPos.height);
                } else {
                     newPos = new Rectangle(actualPos.x + delta.x, actualPos.y, actualPos.width + delta.width, actualPos.height);
                }
            }
            
            // Create Moved Command
            NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
            command.setModel(editpart.getModel());
            command.setConstraint(newPos);
            result.add(command);
        }
        return result;
    }

    @objid ("ae0c5254-a3d9-495a-b182-2f7b31b1b05e")
    private List<Command> createMoveCommands(List<BpmnLaneEditPart> impactedElements, BpmnLaneEditPart movedElement, Rectangle destination) {
        List<Command> result = new ArrayList<>();
        
        int aboveDelta = 0;
        int belowDelta = 0;
        // Calculate Delta for pool above and below moved pool
        for(BpmnLaneEditPart editpart : impactedElements){
            if(!editpart.equals(movedElement)){
                Rectangle actualPos = editpart.getFigure().getBounds();    
                
                //Pool Above
                if(actualPos.y + actualPos.height + POOL_Y_MIN_MARGING > destination.y && actualPos.y  < destination.y){            
                    aboveDelta = destination.y - (actualPos.height + POOL_Y_MIN_MARGING)  - actualPos.y;
                }
                
                //Pool Below
                if(actualPos.y - POOL_Y_MIN_MARGING < destination.y  +  destination.height && actualPos.y > destination.y ){            
                    belowDelta = destination.y + (destination.height + POOL_Y_MIN_MARGING)  - actualPos.y;
                }
            }    
        }
        
        Rectangle newPos = null;
        for(BpmnLaneEditPart editpart : impactedElements){
            Rectangle actualPos = editpart.getFigure().getBounds();            
            if (editpart.equals(movedElement)) {
                 newPos = destination;
            } else {
                if (actualPos.y  < destination.y){
                     newPos = new Rectangle(destination.x, actualPos.y + aboveDelta, actualPos.width, actualPos.height);        
                }else if (actualPos.y > destination.y ){
                     newPos = new Rectangle(destination.x, actualPos.y + belowDelta, actualPos.width, actualPos.height);
                }
            }    
            NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
            command.setModel(editpart.getModel());
            command.setConstraint(newPos);
            result.add(command);        
        }
        return result;
    }

    @objid ("56514315-29ad-4282-b64e-48afdf4efa8c")
    @Override
    protected Object getConstraintFor(CreateRequest request) {
        Object constraint = super.getConstraintFor(request);
        if(constraint instanceof Rectangle &&((( ModelioCreationContext) request.getNewObject()).getMetaclass().equals("BpmnLane") || (( ModelioCreationContext) request.getNewObject()).getMetaclass().equals("BpmnLaneSet"))){
            
            if(((Rectangle)constraint).height == -1){
                ((Rectangle)constraint).height = 200;
            }
            
            for (EditPart children : new ArrayList<EditPart>(getHost().getChildren())) {
                   if (children instanceof BpmnLaneEditPart) {
                       Rectangle childrenPos = ((BpmnLaneEditPart) children).getFigure().getBounds();
                       ((Rectangle)constraint).x = childrenPos.x;
                       ((Rectangle)constraint).width = childrenPos.width; 
                   }
               }
                
        }
        return constraint;
    }

}
