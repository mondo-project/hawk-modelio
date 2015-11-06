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
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("61f94ae1-55b6-11e2-877f-002564c97630")
public class SmartPoolCommand extends Command {
    @objid ("61f94ae3-55b6-11e2-877f-002564c97630")
    private Actor element;

    @objid ("61f94ae7-55b6-11e2-877f-002564c97630")
    private BpmnProcess parentElement;

    @objid ("f78836fa-5a3f-11e2-9e33-00137282c51b")
    private EditPart editPart;

    @objid ("0cb33bce-d270-4df5-ad0d-1bc99eb2236b")
    private Point dropLocation;

    /**
     * Initialize the command.
     * @param dropLocation The location of the element in the diagram
     * @param toUnmask The operation to unmask
     * @param editPart The destination edit part that will own the call operation.
     * @param parentElement The element that will own the call operation action.
     */
    @objid ("61f94aea-55b6-11e2-877f-002564c97630")
    public SmartPoolCommand(final Point dropLocation, final Actor toUnmask, final EditPart editPart, final BpmnProcess parentElement) {
        this.element = toUnmask;
        this.dropLocation = dropLocation;
        this.editPart = editPart;
        this.parentElement = parentElement;
    }

    @objid ("61f94af9-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmModel gmModel = (GmModel) this.editPart.getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        ModelManager modelManager = gmDiagram.getModelManager();
        final IModelFactory factory = modelManager.getModelFactory(gmDiagram.getRelatedElement());
        
        BpmnLane newElement = factory.createElement(BpmnLane.class);
        BpmnLaneSet laneset = factory.createElement(BpmnLaneSet.class);
        laneset.setProcess(this.parentElement);
        newElement.setLaneSet(laneset);
        newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
        newElement.setPartitionElement(this.element);
        unmaskElement(newElement);
    }

    @objid ("61f94afc-55b6-11e2-877f-002564c97630")
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

    @objid ("61f94b02-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        return this.parentElement != null &&
                this.parentElement.isValid() &&
                this.parentElement.getStatus().isModifiable();
    }

}
