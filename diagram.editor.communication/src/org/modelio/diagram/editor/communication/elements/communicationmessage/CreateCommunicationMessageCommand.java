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
                                    

package org.modelio.diagram.editor.communication.elements.communicationmessage;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.gproject.model.IElementConfigurator;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationMessage;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * Command that creates and unmask an information flow as label in a {@link GmCommunicationSentMessageGroup}.
 */
@objid ("7a3d7361-55b6-11e2-877f-002564c97630")
class CreateCommunicationMessageCommand extends DefaultCreateElementCommand {
    /**
     * Creates the command
     * @param gmGroup The group where the flow must be created
     * @param ctx The creation context
     * @param index an index in the group
     */
    @objid ("7a3d7365-55b6-11e2-877f-002564c97630")
    public CreateCommunicationMessageCommand(final GmCompositeNode gmGroup, final ModelioCreationContext ctx, final Integer index) {
        super(gmGroup, ctx, index);
    }

    @objid ("7a3d7373-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.getParentNode().getDiagram();
        
        CommunicationMessage newElement = (CommunicationMessage) this.getContext().getElementToUnmask();
        
        if (newElement == null) {
            ModelManager modelManager = diagram.getModelManager();
        
            // Create the Element...
            final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
            newElement = modelFactory.createCommunicationMessage();
        
            if (getParentNode() instanceof GmCommunicationSentMessageGroup) {
                ((CommunicationChannel) getParentElement()).getStartToEndMessage().add(newElement);
            } else {
                ((CommunicationChannel) getParentElement()).getEndToStartMessage().add(newElement);
            }
        
            // Attach the stereotype if needed.
            if (getContext().getStereotype() != null) {
                ((ModelElement) newElement).getExtension().add(getContext().getStereotype());
            }
        
            // Configure element from properties
            final IElementConfigurator elementConfigurer = modelManager.getModelServices().getElementConfigurer();
            elementConfigurer.configure(modelManager.getModelFactory(newElement), newElement, getContext().getProperties());
        
            // Set default name
            IElementNamer elementNamer = modelManager.getModelServices().getElementNamer();
            newElement.setName(elementNamer.getUniqueName(newElement));
        
        }
        
        // Show the new element in the diagram (ie create its Gm )
        diagram.unmask(getParentNode(), newElement, this.getConstraint());
    }

}
