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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.gproject.model.IElementConfigurator;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("621c6360-55b6-11e2-877f-002564c97630")
public class CreateBpmnFlowElementCommand extends DefaultCreateElementCommand {
    @objid ("621de9bb-55b6-11e2-877f-002564c97630")
    public CreateBpmnFlowElementCommand(GmCompositeNode parentNode, ModelioCreationContext context, Object constraint) {
        super(parentNode, context, constraint);
    }

    @objid ("621de9c4-55b6-11e2-877f-002564c97630")
    public CreateBpmnFlowElementCommand(MObject parentNode, GmCompositeNode parentElement, ModelioCreationContext context, Object constraint) {
        super(parentNode, parentElement, context, constraint);
    }

    @objid ("621de9d0-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.getParentNode().getDiagram();
        
        MObject newElement = this.getContext().getElementToUnmask();
        
        if (newElement == null) {
            ModelManager modelManager = diagram.getModelManager();
        
            // Create the Element...
            final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
            newElement = modelFactory.createElement(this.getContext().getMetaclass());
        
            // The new element must be attached to its parent using the composition dependency
            // provided by the context.
            // If the context provides a null dependency, use the default dependency recommended by the metamodel
            MDependency effectiveDependency = MTools.getMetaTool().getDefaultCompositionDep(this.getParentElement(), newElement);
            // Attach to parent
            if (effectiveDependency == null)
                throw new IllegalStateException("Cannot find a composition dependency to attach " +
                        newElement.toString() +
                        " to " +
                        this.getParentElement().toString());
            // ... and attach it to its parent.
        
            this.getParentElement().mGet(effectiveDependency).add(newElement);
        
            // Attach the stereotype if needed.
            if (this.getContext().getStereotype() != null && newElement instanceof ModelElement) {
                ((ModelElement) newElement).getExtension().add(this.getContext().getStereotype());
            }
        
            if (newElement instanceof BpmnFlowElement &&
                    getParentNode().getRelatedElement() instanceof BpmnLane) {
                BpmnLane lane = (BpmnLane) getParentNode().getRelatedElement();
                BpmnFlowElement flowElement = (BpmnFlowElement) newElement;
                //flowElement.addBpmnLaneRefs(lane);
                lane.getFlowElementRef().add(flowElement);
            }
        
            // Configure element from properties
            final IElementConfigurator elementConfigurer = modelManager.getModelServices().getElementConfigurer();
            elementConfigurer.configure(modelManager.getModelFactory(newElement), newElement, getContext().getProperties());
        
            // Set default name
            IElementNamer elementNamer = modelManager.getModelServices().getElementNamer();
            newElement.setName(elementNamer.getUniqueName(newElement));
        
        }
        
        // Show the new element in the diagram (ie create its Gm )
        diagram.unmask(this.getParentNode(), newElement, this.getConstraint());
    }

}
