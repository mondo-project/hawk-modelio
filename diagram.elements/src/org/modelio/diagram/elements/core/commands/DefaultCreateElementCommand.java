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
                                    

package org.modelio.diagram.elements.core.commands;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * {@link GmNodeModel} creation command that:
 * <ul>
 * <li>creates and initialize the MObject if asked.
 * <li>creates the {@link GmNodeModel} and unmask it.
 * </ul>
 * according to the provided {@link ModelioCreationContext}.
 */
@objid ("7f34b5c0-1dec-11e2-8cad-001ec947c8cc")
public class DefaultCreateElementCommand extends Command {
    @objid ("7f34b5c4-1dec-11e2-8cad-001ec947c8cc")
    private ModelioCreationContext context;

    @objid ("7f34b5c5-1dec-11e2-8cad-001ec947c8cc")
    private Object constraint;

    @objid ("7f34b5c6-1dec-11e2-8cad-001ec947c8cc")
    private MObject parentElement;

    @objid ("7f34b5c7-1dec-11e2-8cad-001ec947c8cc")
    private GmCompositeNode parentNode;

    /**
     * Creates a node creation command.
     * @param parentNode The parent node
     * @param context Details on the MObject and/or the node to create
     * @param constraint The initial constraint of the created node.
     */
    @objid ("7f34b5c8-1dec-11e2-8cad-001ec947c8cc")
    public DefaultCreateElementCommand(GmCompositeNode parentNode, ModelioCreationContext context, Object constraint) {
        this.parentNode = parentNode;
        this.parentElement = parentNode.getRelatedElement();
        this.context = context;
        this.constraint = constraint;
    }

    /**
     * Creates a node creation command.
     * @param parentElement The parent MObject of the MObject to create
     * @param parentNode The parent node
     * @param context Details on the MObject and/or the node to create
     * @param constraint The initial constraint of the created node.
     */
    @objid ("7f3717dd-1dec-11e2-8cad-001ec947c8cc")
    public DefaultCreateElementCommand(MObject parentElement, GmCompositeNode parentNode, ModelioCreationContext context, Object constraint) {
        this.parentNode = parentNode;
        this.parentElement = parentElement;
        this.context = context;
        this.constraint = constraint;
    }

    @objid ("7f3717e4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.parentNode.getDiagram();
        
        MObject newElement = this.context.getElementToUnmask();
        
        if (newElement == null) {
            // Create the MObject...
            ModelManager modelManager = diagram.getModelManager();
            final IModelFactory modelFactory = modelManager.getModelFactory(this.parentElement);
            newElement = modelFactory.createElement(this.context.getMetaclass());
        
            // The new element must be attached to its parent using the composition dependency
            // provided by the context.
            // If the context provides a null dependency, use the default dependency recommended by the metamodel
            String effectiveDependencyName = this.context.getDependency();
            MDependency effectiveDependency = null;
            if (effectiveDependencyName != null) {
                effectiveDependency = this.parentElement.getMClass().getDependency(effectiveDependencyName);
            }
            if (effectiveDependency == null) {
                effectiveDependency = MTools.getMetaTool().getDefaultCompositionDep(this.parentElement, newElement);
            }
             
            // ... and attach it to its parent.
            try {
                this.parentElement.mGet(effectiveDependency).add(newElement);
            } catch (Exception e) {
                // FIXME: use a finer type of exception.
                // The dependency indicated in the context cannot be used: try
                // to find a valid one!
                MDependency compositionDep = MTools.getMetaTool().getDefaultCompositionDep(this.parentElement, newElement);
                if (compositionDep != null) {
                    this.parentElement.mGet(compositionDep).add(newElement);
                } else {
                    newElement.delete();
                    return;
                }
            }
        
            // Attach the stereotype if needed.
            if (this.context.getStereotype() != null && newElement instanceof ModelElement) {
                ((ModelElement) newElement).getExtension().add(this.context.getStereotype());
            }
        
            // Set default name
            newElement.setName(modelManager.getModelServices().getElementNamer().getUniqueName(newElement));
        
            // Configure element
            IMModelServices modelServices = modelManager.getModelServices();
            modelServices.getElementConfigurer().configure(modelFactory, newElement, this.context.getProperties());
        }
        
        // Show the new element in the diagram (ie create its Gm )
        diagram.unmask(this.parentNode, newElement, this.constraint);
    }

    /**
     * Get the creation context (parent element, parent dependency, stereotype).
     * @return the creation context.
     */
    @objid ("7f3717e7-1dec-11e2-8cad-001ec947c8cc")
    protected ModelioCreationContext getContext() {
        return this.context;
    }

    /**
     * Get the initial layout constraint.
     * @return the initial layout constraint.
     */
    @objid ("7f3717ec-1dec-11e2-8cad-001ec947c8cc")
    protected Object getConstraint() {
        return this.constraint;
    }

    /**
     * Get the parent model element.
     * @return the parent model element.
     */
    @objid ("7f3717f1-1dec-11e2-8cad-001ec947c8cc")
    protected MObject getParentElement() {
        return this.parentElement;
    }

    /**
     * Get the parent graphic node.
     * @return the parent graphic node.
     */
    @objid ("7f3717f6-1dec-11e2-8cad-001ec947c8cc")
    protected GmCompositeNode getParentNode() {
        return this.parentNode;
    }

    @objid ("7f3717fb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canExecute() {
        // The diagram must be valid and modifiable.
        final GmAbstractDiagram gmDiagram = this.parentNode.getDiagram();
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        
        // If it is an actual creation (and not a simple unmasking).
        if (this.context.getElementToUnmask() == null) {
            final MClass toCreate = Metamodel.getMClass(this.context.getMetaclass());
        
            // The parent element must be modifiable or
            // both must be CMS nodes.
            if (!MTools.getAuthTool().canAdd(this.parentElement, this.context.getMetaclass()))
                return false;
        
            // Ask metamodel experts
            return MTools.getMetaTool().canCompose(this.parentElement, toCreate, null);
        
        }
        return true;
    }

}
