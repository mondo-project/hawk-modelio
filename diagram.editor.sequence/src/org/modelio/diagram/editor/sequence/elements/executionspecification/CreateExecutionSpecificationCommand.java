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
                                    

package org.modelio.diagram.editor.sequence.elements.executionspecification;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionOccurenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Creation Command for Execution. Creates (and Unmask) Execution on a Lifeline or on another Execution.
 */
@objid ("d8e37dda-55b6-11e2-877f-002564c97630")
public class CreateExecutionSpecificationCommand extends Command {
    @objid ("50086f09-55c2-11e2-9337-002564c97630")
    private GmCompositeNode parentNode;

    @objid ("2d8fd2ea-f9c6-472f-a794-5d179a5465d7")
    private Rectangle initialLayoutData;

    /**
     * C'tor.
     * @param parentNode the node into which the created execution should be unmasked.
     * @param initialLayoutData the initial layout data to use. X coordinate will be ignored, since it will be updated in the
     * container's layout.
     */
    @objid ("d8e37de0-55b6-11e2-877f-002564c97630")
    public CreateExecutionSpecificationCommand(final GmCompositeNode parentNode, final Rectangle initialLayoutData) {
        this.initialLayoutData = initialLayoutData;
        this.parentNode = parentNode;
    }

    @objid ("d8e37de9-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        // Create the MObject
        MObject parentElement = this.parentNode.getRelatedElement();
        final GmAbstractDiagram diagram = this.parentNode.getDiagram();
        ModelManager modelManager = diagram.getModelManager();
        final IModelFactory modelFactory = modelManager.getModelFactory(parentElement);
        
        Interaction interaction = null;
        Lifeline lifeline = null;
        
        if (parentElement instanceof Lifeline) {
            lifeline = ((Lifeline) parentElement);
            interaction = lifeline.getOwner();
        } else if (parentElement instanceof ExecutionSpecification) {
            lifeline = ((ExecutionSpecification) parentElement).getCovered().get(0);
            interaction = lifeline.getOwner();
        }
        
        // Use the collected elements to create and initialise the execution.
        ExecutionOccurenceSpecification startOccurence = modelFactory.createExecutionOccurenceSpecification();
        startOccurence.setEnclosingInteraction(interaction);
        startOccurence.getCovered().add(lifeline);
        startOccurence.setLineNumber(this.initialLayoutData.y);
        
        ExecutionOccurenceSpecification finishOccurence = modelFactory.createExecutionOccurenceSpecification();
        finishOccurence.setEnclosingInteraction(interaction);
        finishOccurence.getCovered().add(lifeline);
        finishOccurence.setLineNumber(this.initialLayoutData.bottom());
        
        ExecutionSpecification newExecution = modelFactory.createExecutionSpecification();
        newExecution.setEnclosingInteraction(interaction);
        newExecution.getCovered().add(lifeline);
        newExecution.setStart(startOccurence);
        newExecution.setFinish(finishOccurence);
        
        // Show the new element in the diagram (ie create its Gm )
        diagram.unmask(this.parentNode, startOccurence, this.initialLayoutData);
        diagram.unmask(this.parentNode, finishOccurence, this.initialLayoutData);
        diagram.unmask(this.parentNode, newExecution, this.initialLayoutData);
    }

}
