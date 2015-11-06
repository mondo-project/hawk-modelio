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
                                    

package org.modelio.diagram.editor.bpmn.elements.diagrams;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("61f7c44d-55b6-11e2-877f-002564c97630")
public class SmartDataObjectCommand extends Command {
    @objid ("61f7c453-55b6-11e2-877f-002564c97630")
    private MObject parentElement;

    @objid ("f7837247-5a3f-11e2-9e33-00137282c51b")
    private EditPart editPart;

    @objid ("f783724a-5a3f-11e2-9e33-00137282c51b")
    private MObject element;

    @objid ("76589ad6-ce7a-4f85-866c-53ea3060c004")
    private Point dropLocation;

    /**
     * Initialize the command.
     * @param dropLocation The location of the element in the diagram
     * @param toUnmask The operation to unmask
     * @param editPart The destination edit part that will own the call operation.
     * @param parentElement The element that will own the call operation action.
     */
    @objid ("61f7c456-55b6-11e2-877f-002564c97630")
    public SmartDataObjectCommand(final Point dropLocation, final MObject toUnmask, final EditPart editPart, final MObject parentElement) {
        this.element = toUnmask;
        this.dropLocation = dropLocation;
        this.editPart = editPart;
        this.parentElement = parentElement;
    }

    @objid ("61f7c465-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmModel gmModel = (GmModel) this.editPart.getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final IModelFactory factory = gmDiagram.getModelManager().getModelFactory(gmDiagram.getRelatedElement());
        
        // Create the smart node
        final BpmnDataObject el = factory.createElement(BpmnDataObject.class);
        
        // Set default name
        el.setName(gmDiagram.getModelManager().getModelServices().getElementNamer().getUniqueName(el));
        
        // The new element must be attached to its parent using the composition dependency
        // provided by the context.
        // If the context provides a null dependency, use the default dependency recommended by the metamodel
        MDependency effectiveDependency = MTools.getMetaTool().getDefaultCompositionDep(this.parentElement, el);
        // Attach to parent
        if (effectiveDependency == null)
            throw new IllegalStateException("Cannot find a composition dependency to attach " +
                    el.toString() +
                    " to " +
                    this.parentElement.toString());
        // ... and attach it to its parent.
        
        this.parentElement.mGet(effectiveDependency).add(el);
        
        if (this.parentElement instanceof BpmnLane) {
            BpmnLane lane = (BpmnLane) this.parentElement;
            el.getLane().add((BpmnLane) this.parentElement);
            if (lane.getLaneSet().getProcess() != null) {
                this.parentElement = lane.getLaneSet().getProcess();
            } else {
                this.parentElement = lane.getLaneSet().getSubProcess();
            }
        }
        
        if (this.parentElement instanceof BpmnProcess) {
            el.setContainer((BpmnProcess) this.parentElement);
        } else if (this.parentElement instanceof BpmnSubProcess) {
            el.setSubProcess((BpmnSubProcess) this.parentElement);
        }
        
        if (this.element instanceof GeneralClass) {
            el.setType((GeneralClass) this.element);
        } else if (this.element instanceof State) {
            el.setInState((State) this.element);
        }
        
        unmaskElement(el);
    }

    /**
     * Unmask the given element in the destination edit part.
     * @param el The element to unmask
     */
    @objid ("61f94abb-55b6-11e2-877f-002564c97630")
    private void unmaskElement(final MObject el) {
        final ModelioCreationContext gmCreationContext = new ModelioCreationContext(el);
        
        final CreateRequest creationRequest = new CreateRequest();
        creationRequest.setLocation(this.dropLocation);
        creationRequest.setSize(new Dimension(-1, -1));
        creationRequest.setFactory(gmCreationContext);
        
        final Command cmd = this.editPart.getTargetEditPart(creationRequest).getCommand(creationRequest);
        if (cmd != null && cmd.canExecute())
            cmd.execute();
    }

    @objid ("61f94ac2-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        return this.parentElement != null &&
                this.parentElement.isValid() &&
                this.parentElement.getStatus().isModifiable();
    }

    @objid ("61f94ac7-55b6-11e2-877f-002564c97630")
    private MObject getBpmnBehavior(final MObject mobjectElement) {
        if (mobjectElement instanceof BpmnBehavior) {
            return mobjectElement;
        }
        return getBpmnBehavior(mobjectElement.getCompositionOwner());
    }

}
