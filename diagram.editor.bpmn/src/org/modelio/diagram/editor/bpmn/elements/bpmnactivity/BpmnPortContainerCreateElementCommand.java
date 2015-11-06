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
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.gproject.model.IElementConfigurator;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command dedicated to the creation of BpmnBoundaryEvent
 */
@objid ("60773e9b-55b6-11e2-877f-002564c97630")
public class BpmnPortContainerCreateElementCommand extends DefaultCreateElementCommand {
    @objid ("60773e9f-55b6-11e2-877f-002564c97630")
    public BpmnPortContainerCreateElementCommand(GmCompositeNode parentNode, ModelioCreationContext context, Object constraint) {
        super(parentNode, context, constraint);
    }

    @objid ("60773ea8-55b6-11e2-877f-002564c97630")
    public BpmnPortContainerCreateElementCommand(MObject parentElement, GmCompositeNode parentNode, ModelioCreationContext context, Object constraint) {
        super(parentElement, parentNode, context, constraint);
    }

    @objid ("6078c541-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.getParentNode().getDiagram();
        
        MObject newElement = this.getContext().getElementToUnmask();
        
        if (newElement == null) {
            ModelManager modelManager = diagram.getModelManager();
            
            // Create the Element...
            final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
            newElement = modelFactory.createElement(this.getContext().getMetaclass());
        
            // Add Port to Task : Relation boundaryEventRef        
            assert (getParentElement() instanceof BpmnActivity);
            assert (newElement instanceof BpmnBoundaryEvent);
            BpmnBoundaryEvent boundaryElement = (BpmnBoundaryEvent) newElement;
            BpmnActivity activity = (BpmnActivity) getParentElement();
        
            if (activity.getContainer() != null) {
                boundaryElement.setContainer(activity.getContainer());
            } else if (activity.getSubProcess() != null) {
                boundaryElement.setSubProcess(activity.getSubProcess());
            }
        
            activity.getBoundaryEventRef().add((BpmnBoundaryEvent) newElement);
        
            // Attach the stereotype if needed.
            if (this.getContext().getStereotype() != null && newElement instanceof ModelElement) {
                ((ModelElement) newElement).getExtension().add(this.getContext().getStereotype());
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

    @objid ("6078c544-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        final GmAbstractDiagram gmDiagram = getParentNode().getDiagram();
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        return true;
    }

}
