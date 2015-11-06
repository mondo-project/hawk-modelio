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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.gproject.model.IElementConfigurator;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Specific command to handle the creation of one (or more) partitions in a partition container.
 */
@objid ("61422fd6-55b6-11e2-877f-002564c97630")
public class CreateBpmnLaneSetContainerCommand extends Command {
    @objid ("61422fe2-55b6-11e2-877f-002564c97630")
    private MObject parentElement;

    @objid ("61422fd8-55b6-11e2-877f-002564c97630")
    private Object newConstraint;

    @objid ("71d22469-55c1-11e2-9337-002564c97630")
    private ModelioCreationContext context;

    @objid ("71d2246a-55c1-11e2-9337-002564c97630")
    private GmNodeModel insertAfter;

    @objid ("71d2246b-55c1-11e2-9337-002564c97630")
    private GmCompositeNode parentNode;

    /**
     * Creates a node creation command.
     * @param parentElement the element that lead to this command.
     * @param parentEditPart The parent editPart
     * @param context Details on the MObject and/or the node to create
     * @param requestConstraint Request Constraint
     */
    @objid ("61422fe5-55b6-11e2-877f-002564c97630")
    public CreateBpmnLaneSetContainerCommand(MObject parentElement, GmCompositeNode parentEditPart, ModelioCreationContext context, Object requestConstraint) {
        this.parentNode = parentEditPart;
        this.parentElement = parentElement;
        this.context = context;
        this.newConstraint = requestConstraint;
    }

    @objid ("61422ff2-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.parentNode.getDiagram();
        
        BpmnLaneSet newElement;
        
        if (this.context.getElementToUnmask() == null) {
            ModelManager modelManager = diagram.getModelManager();
        
            // Create the MObject...
            final IModelFactory modelFactory = modelManager.getModelFactory(this.parentElement);
            newElement = modelFactory.createElement(BpmnLaneSet.class);
        
            if (this.parentElement instanceof BpmnProcess) {
                newElement.setProcess((BpmnProcess) this.parentElement);
            } else if (this.parentElement instanceof BpmnLane) {
                newElement.setParentLane((BpmnLane) this.parentElement);
            }
        
            // Attach the stereotype if needed.
            if (this.context.getStereotype() != null) {
                ((ModelElement) newElement).getExtension().add(this.context.getStereotype());
            }
        
            // Configure element from properties
            final IElementConfigurator elementConfigurer = modelManager.getModelServices().getElementConfigurer();
            elementConfigurer.configure(modelManager.getModelFactory(newElement), newElement, this.context.getProperties());
        
            createAndUnmaskLane(diagram, newElement);
            if (this.parentElement instanceof BpmnLane) {
                createAndUnmaskLane(diagram, newElement);
            } else {
                // Set default name
                IElementNamer elementNamer = modelManager.getModelServices().getElementNamer();
                newElement.setName(elementNamer.getUniqueName(newElement));
            }
        } else {
            newElement = (BpmnLaneSet) this.context.getElementToUnmask();
            diagram.unmask(this.parentNode, newElement, this.newConstraint);
        }
    }

    /**
     * @param diagram
     * @param newElement
     */
    @objid ("61422ff5-55b6-11e2-877f-002564c97630")
    private BpmnLane createAndUnmaskLane(final GmAbstractDiagram diagram, final BpmnLaneSet owner) {
        ModelManager modelManager = diagram.getModelManager();
        final IModelFactory modelFactory = modelManager.getModelFactory(owner);
        BpmnLane newElement = modelFactory.createElement(BpmnLane.class);
        
        newElement.setLaneSet(owner);
        
        // Attach the stereotype if needed.
        if (this.context.getStereotype() != null) {
            ((ModelElement) newElement).getExtension().add(this.context.getStereotype());
        }
        
        // Configure element from properties
        final IElementConfigurator elementConfigurer = modelManager.getModelServices().getElementConfigurer();
        elementConfigurer.configure(modelManager.getModelFactory(newElement), newElement, this.context.getProperties());
        
        // Set default name
        newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
        
        // Show the new elements in the diagram (ie create their Gm )
        diagram.unmask(this.parentNode, newElement, this.newConstraint);
        return newElement;
    }

}
