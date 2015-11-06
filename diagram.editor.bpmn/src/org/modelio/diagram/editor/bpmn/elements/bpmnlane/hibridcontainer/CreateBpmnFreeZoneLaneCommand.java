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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlane.hibridcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
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
@objid ("6135facc-55b6-11e2-877f-002564c97630")
public class CreateBpmnFreeZoneLaneCommand extends Command {
    @objid ("6135fad5-55b6-11e2-877f-002564c97630")
    private MObject parentElement;

    @objid ("71c5ef69-55c1-11e2-9337-002564c97630")
    private ModelioCreationContext context;

    @objid ("71c5ef6a-55c1-11e2-9337-002564c97630")
    private GmCompositeNode parentNode;

    @objid ("8c61eeab-71fc-40d8-9e43-182716ba8631")
    private Object newConstraint;

    /**
     * Creates a node creation command.
     * @param parentElement the element that lead to this command.
     * @param parentEditPart The parent editPart
     * @param context Details on the MObject and/or the node to create
     * @param requestConstraint Request Constraint
     */
    @objid ("6135fad8-55b6-11e2-877f-002564c97630")
    public CreateBpmnFreeZoneLaneCommand(MObject parentElement, GmCompositeNode parentEditPart, ModelioCreationContext context, Object requestConstraint) {
        this.parentNode = parentEditPart;
        this.parentElement = parentElement;
        this.context = context;
        this.newConstraint = requestConstraint;
    }

    @objid ("6135fae5-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.parentNode.getDiagram();
        
        ModelManager modelManager = diagram.getModelManager();
        
        BpmnLaneSet newElement;
        // Create the Element...
        final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
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
        
        // Set default name
        IElementNamer elementNamer = modelManager.getModelServices().getElementNamer();
        newElement.setName(elementNamer.getUniqueName(newElement));
        
        GmModel lansetgm = diagram.unmask(this.parentNode, newElement, this.newConstraint);
        
        createAndUnmaskLane(diagram, lansetgm);
        if (this.parentElement instanceof BpmnLane) {
            createAndUnmaskLane(diagram, lansetgm);
        }
    }

    /**
     * @param diagram
     * @param newElement
     */
    @objid ("6135fae8-55b6-11e2-877f-002564c97630")
    private void createAndUnmaskLane(final GmAbstractDiagram diagram, GmModel ownergm) {
        ModelManager modelManager = diagram.getModelManager();
        final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
        BpmnLane newElement = modelFactory.createElement(BpmnLane.class);
        
        newElement.setLaneSet((BpmnLaneSet) ownergm.getRelatedElement());
        
        // Set default name
        IElementNamer elementNamer = modelManager.getModelServices().getElementNamer();
        newElement.setName(elementNamer.getUniqueName(newElement));
        
        // Attach the stereotype if needed.
        if (this.context.getStereotype() != null) {
            ((ModelElement) newElement).getExtension().add(this.context.getStereotype());
        }
        
        // Configure element from properties
        final IElementConfigurator elementConfigurer = modelManager.getModelServices().getElementConfigurer();
        elementConfigurer.configure(modelManager.getModelFactory(newElement), newElement, this.context.getProperties());
        
        // Show the new elements in the diagram (ie create their Gm )
        diagram.unmask((GmCompositeNode) ownergm, newElement, -1);
    }

}
