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
                                    

package org.modelio.diagram.editor.state.elements.statediagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ConnectionPointReference;
import org.modelio.metamodel.uml.behavior.stateMachineModel.EntryPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ExitPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Create a {@link State} linked to the given {@link StateMachine} and unmask it in the diagram.
 */
@objid ("f58b9cba-55b6-11e2-877f-002564c97630")
class CreateCallBehaviorCommand extends Command {
    @objid ("f58b9cbd-55b6-11e2-877f-002564c97630")
    private StateMachine stateMachine;

    @objid ("f58b9cc1-55b6-11e2-877f-002564c97630")
    private StateMachine parentElement;

    @objid ("fe4d2f74-5a5b-11e2-9e33-00137282c51b")
    private EditPart editPart;

    @objid ("8dcc2736-3b3c-428e-8fcb-4dc36126c9b4")
    private Point dropLocation;

    /**
     * Initialize the command.
     * @param dropLocation The location of the element in the diagram
     * @param toUnmask The operation to unmask
     * @param editPart The destination edit part that will own the call operation.
     * @param parentElement The element that will own the call operation action.
     */
    @objid ("f58b9cc4-55b6-11e2-877f-002564c97630")
    public CreateCallBehaviorCommand(final Point dropLocation, final StateMachine toUnmask, final EditPart editPart, final StateMachine parentElement) {
        this.stateMachine = toUnmask;
        this.dropLocation = dropLocation;
        this.editPart = editPart;
        this.parentElement = parentElement;
    }

    @objid ("f58d235f-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmModel gmModel = (GmModel) this.editPart.getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final IModelFactory factory = gmDiagram.getModelManager().getModelFactory(gmDiagram.getRelatedElement());
        
        // Create the node
        final State el = factory.createState();
        
        // Attach to its parent
        el.setParent(this.parentElement.getTop());
        
        // Attach to the dropped element
        el.setName(this.stateMachine.getName());
        el.setSubMachine(this.stateMachine);
        
        // Also create ConnectionPointReference for each EntryPoint and ExitPoint on the StateMachine.
        for (EntryPointPseudoState entry : this.stateMachine.getEntryPoint()) {
            ConnectionPointReference reference = factory.createConnectionPointReference();
            reference.setName(entry.getName());
            reference.setEntry(entry);
            reference.setOwnerState(el);
        }
        for (ExitPointPseudoState exit : this.stateMachine.getExitPoint()) {
            ConnectionPointReference reference = factory.createConnectionPointReference();
            reference.setName(exit.getName());
            reference.setExit(exit);
            reference.setOwnerState(el);
        }
        
        // Unmask the created node
        unmaskElement(el);
    }

    /**
     * Unmask the given element in the destination edit part.
     * @param el The element to unmask
     */
    @objid ("f58d2362-55b6-11e2-877f-002564c97630")
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

    @objid ("f58d2369-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        return this.parentElement != null &&
               this.parentElement.isValid() &&
               this.parentElement.isModifiable();
    }

}
