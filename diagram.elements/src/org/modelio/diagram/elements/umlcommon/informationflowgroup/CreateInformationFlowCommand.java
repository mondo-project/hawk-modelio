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
                                    

package org.modelio.diagram.elements.umlcommon.informationflowgroup;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command that creates and unmask an information flow as label in a {@link GmInfoFlowsGroup}.
 */
@objid ("815dd714-1dec-11e2-8cad-001ec947c8cc")
class CreateInformationFlowCommand extends DefaultCreateElementCommand {
    /**
     * Creates the command
     * @param gmGroup The group where the flow must be created
     * @param ctx The creation context
     * @param index an index in the group
     */
    @objid ("815dd716-1dec-11e2-8cad-001ec947c8cc")
    public CreateInformationFlowCommand(final GmCompositeNode gmGroup, final ModelioCreationContext ctx, final Integer index) {
        super(gmGroup, ctx, index);
    }

    @objid ("815dd720-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.getParentNode().getDiagram();
        
        MObject newElement = this.getContext().getElementToUnmask();
        
        if (newElement == null) {
            // Create the Element...
            final IModelFactory modelFactory = diagram.getModelManager().getModelFactory(getParentElement());
            newElement = new InformationFlowFactory(modelFactory).createInformationFlow(getParentElement());
        
            // Attach the stereotype if needed.
            if (getContext().getStereotype() != null && newElement instanceof ModelElement) {
                ((ModelElement) newElement).getExtension().add(getContext().getStereotype());
            }
        }
        
        // Show the new element in the diagram (ie create its Gm )
        diagram.unmask(getParentNode(), newElement, this.getConstraint());
    }

    @objid ("815dd723-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canExecute() {
        // The diagram must be valid and modifiable.
        final GmAbstractDiagram gmDiagram = getParentNode().getDiagram();
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        
        // If it is an actual creation (and not a simple unmasking).
        if (getContext().getElementToUnmask() == null) {
            final IModelFactory modelFactory = gmDiagram.getModelManager().getModelFactory(getParentElement());
            return new InformationFlowFactory(modelFactory).canCreateInformationFlow(getParentElement());
        }
        return true;
    }

}
