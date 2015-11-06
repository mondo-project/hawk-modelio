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
                                    

package org.modelio.diagram.elements.core.link;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.model.IGmPath;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.obfactory.IModelLinkFactory;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command that creates a relationship element between the 2 node model elements represented by the given EditPart.
 */
@objid ("7fe523ca-1dec-11e2-8cad-001ec947c8cc")
public class DefaultCreateLinkCommand extends Command {
    @objid ("7fe523ce-1dec-11e2-8cad-001ec947c8cc")
    private IGmPath path;

    @objid ("7fe523cf-1dec-11e2-8cad-001ec947c8cc")
    protected ModelioLinkCreationContext context;

    @objid ("7fe523d0-1dec-11e2-8cad-001ec947c8cc")
    protected IGmLinkable sourceNode;

    @objid ("7fe785e1-1dec-11e2-8cad-001ec947c8cc")
    protected IGmLinkable targetNode;

    /**
     * Command constructor
     * @param context Informations on the model element to create and or unmask.
     */
    @objid ("7fe785e2-1dec-11e2-8cad-001ec947c8cc")
    public DefaultCreateLinkCommand(ModelioLinkCreationContext context) {
        this.context = context;
    }

    @objid ("7fe785e6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canExecute() {
        // The diagram must be valid and modifiable.
        final GmAbstractDiagram gmDiagram = this.sourceNode.getDiagram();
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        
        // If it is an actual creation (and not a simple unmasking).
        if (this.context.getElementToUnmask() == null) {
            final MObject srcElement = this.sourceNode.getRelatedElement();
            MClass toCreateMetaclass = Metamodel.getMClass(this.context.getMetaclass());
            Stereotype toCreateStereotype = this.context.getStereotype();
            Class<? extends MObject> toCreateInterface = Metamodel.getJavaInterface(toCreateMetaclass);
            
            if (this.targetNode == null) {
                // The creation experts must allow starting the link
                if (!MTools.getLinkTool().canSource(toCreateStereotype, toCreateMetaclass, srcElement.getMClass()))
                    return false;
        
                // The access right expert must allow the command
                if (!MTools.getAuthTool().canCreateLinkFrom(toCreateInterface, srcElement))
                    return false;
        
            } else {
                // The creation experts must allow the link
                final MObject targetEl = this.targetNode.getRelatedElement();
                if (targetEl == null || targetEl.isShell() || targetEl.isDeleted())
                    return false;
                if (!MTools.getLinkTool().canLink(toCreateStereotype, toCreateMetaclass, srcElement, targetEl))
                    return false;
                
                // The access right expert must allow the command
                if (!MTools.getAuthTool().canCreateLink(toCreateInterface, srcElement, targetEl))
                    return false;
            }
        }
        
        // All conditions are fulfilled
        return true;
    }

    @objid ("7fe785eb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        final GmAbstractDiagram gmDiagram = this.sourceNode.getDiagram();
        
        MObject linkElement = this.context.getElementToUnmask();
        if (linkElement == null) {
            linkElement = createElement();
        }
        
        // Unmask the link
        unmaskElement(gmDiagram, linkElement);
    }

    /**
     * Sets the context
     * @param newContext the link creation context.
     */
    @objid ("7fe785ee-1dec-11e2-8cad-001ec947c8cc")
    public void setContext(ModelioLinkCreationContext newContext) {
        this.context = newContext;
    }

    /**
     * Set the link source.
     * @param sourceNode the link source.
     */
    @objid ("7fe785f2-1dec-11e2-8cad-001ec947c8cc")
    public void setSource(final IGmLinkable sourceNode) {
        this.sourceNode = sourceNode;
    }

    /**
     * Set the link destination.
     * @param targetNode the link destination.
     */
    @objid ("7fe785f7-1dec-11e2-8cad-001ec947c8cc")
    public void setTarget(final IGmLinkable targetNode) {
        this.targetNode = targetNode;
    }

    /**
     * Set the path of the link.
     * @param path the link path.
     */
    @objid ("7fe785fc-1dec-11e2-8cad-001ec947c8cc")
    public void setPath(final IGmPath path) {
        this.path = path;
    }

    /**
     * Unmask the link element.
     * <p>
     * May be redefined to do more work.
     * @param gmDiagram the diagram where the element must be unmasked
     * @param linkElement the link element to unmask
     * @return The unmasked link graphic model.
     */
    @objid ("7fe78601-1dec-11e2-8cad-001ec947c8cc")
    protected GmLink unmaskElement(final GmAbstractDiagram gmDiagram, final MObject linkElement) {
        return gmDiagram.unmaskLink(linkElement, this.sourceNode, this.targetNode, this.path);
    }

    /**
     * Create the model element specified by the context.
     * <p>
     * May be redefined to do more work.
     * @return The created link model element.
     */
    @objid ("7fe7860a-1dec-11e2-8cad-001ec947c8cc")
    protected MObject createElement() {
        final GmAbstractDiagram gmDiagram = this.sourceNode.getDiagram();
        MObject linkElement;
        // Create the link model element
        final ModelManager modelManager = gmDiagram.getModelManager();
        final IModelLinkFactory modelFactory = modelManager.getModelLinkFactory();
        final MObject srcElement = this.sourceNode.getRelatedElement();
        final MObject targetElement = this.targetNode.getRelatedElement();
        linkElement = modelFactory.createLink(this.context.getMetaclass(), srcElement, targetElement);
        
        // Attach the stereotype if needed.
        if (this.context.getStereotype() != null && linkElement instanceof ModelElement) {
            ((ModelElement) linkElement).getExtension().add(this.context.getStereotype());
        }
        
        // Configure element
        IMModelServices modelServices = modelManager.getModelServices();
        modelServices.getElementConfigurer().configure(modelManager.getModelFactory(srcElement), linkElement, this.context.getProperties());
        
        // Set default name
        linkElement.setName(modelServices.getElementNamer().getUniqueName(linkElement));
        return linkElement;
    }

}
