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
                                    

package org.modelio.model.browser.handlers;

import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.IModelioService;
import org.modelio.app.core.events.ModelioEvent;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8c29ade3-c9d8-11e1-b479-001ec947c8cc")
public class CreateCommunicationInteractionHandler extends CreateElementHandler {
    @objid ("67d5e460-750d-4656-b7c1-15eac4eb9207")
    @Inject
     IModelioEventService eventService;

    @objid ("4e4bf6cd-ccde-11e1-97e5-001ec947c8cc")
    @Override
    protected void postCreationStep(Element createdElement) {
        CommunicationInteraction interaction = (CommunicationInteraction) createdElement;
        // Create the diagram
        MObject owner = interaction.getCompositionOwner();
        if ((owner instanceof Classifier) && !(owner instanceof UseCase)) {
            smartCreateForClassifier(interaction, (Classifier) owner);
        } else if (owner instanceof Operation) {
            smartCreateForOperation(interaction, (Operation) owner);
        } else {
            smartCreateForNameSpace(interaction);
        }
    }

    @objid ("4e4bf6d1-ccde-11e1-97e5-001ec947c8cc")
    @Override
    protected void postCommit(MPart part, Element element) {
        final CommunicationDiagram param = ((CommunicationInteraction) element).getProduct(CommunicationDiagram.class).get(0);
        
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                CreateCommunicationInteractionHandler.this.eventService.postAsyncEvent(new IModelioService() {
                    @Override
                    public String getName() {
                        return "openEditor : AbstractDiagram";
                    }
                }, ModelioEvent.EDIT_ELEMENT, param);
            }
        });
    }

    @objid ("4e4bf6d6-ccde-11e1-97e5-001ec947c8cc")
    private void smartCreateForOperation(CommunicationInteraction interaction, Operation parentOperation) {
        IModelFactory mmFactory = this.mmServices.getModelFactory(interaction);
        
        // Create the sequence diagram:
        CommunicationDiagram diagram = mmFactory.createCommunicationDiagram();
        interaction.getProduct().add(diagram);
        diagram.setName(this.mmServices.getElementNamer().getUniqueName(diagram));
        
        // Create the locals Collaboration:
        Collaboration locals = mmFactory.createCollaboration();
        interaction.getOwnedCollaboration().add(locals);
        locals.setName("locals");
        
        // Create the instance:
        BindableInstance instance = mmFactory.createBindableInstance();
        locals.getDeclared().add(instance);
        instance.setName("this");
        instance.setBase(parentOperation.getOwner());
        
        // Create the corresponding InstanceNode:
        CommunicationNode node = mmFactory.createCommunicationNode();
        interaction.getOwned().add(node);
        node.setName("this");
        node.setRepresented(instance);
    }

    @objid ("4e4bf6da-ccde-11e1-97e5-001ec947c8cc")
    private void smartCreateForNameSpace(CommunicationInteraction interaction) {
        IModelFactory mmFactory = this.mmServices.getModelFactory(interaction);
        
        // Create the sequence diagram:
        CommunicationDiagram diagram = mmFactory.createCommunicationDiagram();
        interaction.getProduct().add(diagram);
        diagram.setName(this.mmServices.getElementNamer().getUniqueName(diagram));
        
        // Create the locals Collaboration:
        Collaboration locals = mmFactory.createCollaboration();
        interaction.getOwnedCollaboration().add(locals);
        locals.setName("locals");
    }

    @objid ("4e4bf6dd-ccde-11e1-97e5-001ec947c8cc")
    private void smartCreateForClassifier(CommunicationInteraction interaction, Classifier parentClassifier) {
        IModelFactory mmFactory = this.mmServices.getModelFactory(interaction);
        
        // Create the sequence diagram:
        CommunicationDiagram diagram = mmFactory.createCommunicationDiagram();
        interaction.getProduct().add(diagram);
        diagram.setName(this.mmServices.getElementNamer().getUniqueName(diagram));
        
        // Create the locals Collaboration:
        Collaboration locals = mmFactory.createCollaboration();
        interaction.getOwnedCollaboration().add(locals);
        locals.setName("locals");
        
        // Create the instance representing the classifier:
        BindableInstance instance = mmFactory.createBindableInstance();
        locals.getDeclared().add(instance);
        instance.setName("this");
        instance.setBase(parentClassifier);
        
        // Create the corresponding InstanceNode:
        CommunicationNode node = mmFactory.createCommunicationNode();
        interaction.getOwned().add(node);
        node.setName("this");
        node.setRepresented(instance);
    }

}
