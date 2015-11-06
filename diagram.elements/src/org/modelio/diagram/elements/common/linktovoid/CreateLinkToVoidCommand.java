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
                                    

package org.modelio.diagram.elements.common.linktovoid;

import java.util.ArrayList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.GmPath;
import org.modelio.diagram.elements.core.link.anchors.GmSourceSatelliteAnchor;
import org.modelio.diagram.elements.core.model.IGmPath;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
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
@objid ("7ec96bd9-1dec-11e2-8cad-001ec947c8cc")
public class CreateLinkToVoidCommand extends Command {
    @objid ("96083f7e-10e7-4cd3-8a8c-e8d09d15a768")
    private Dimension destinationLocation;

    @objid ("7ec96bdd-1dec-11e2-8cad-001ec947c8cc")
    private final ModelioCreationContext context;

    @objid ("7ec96bdf-1dec-11e2-8cad-001ec947c8cc")
    private MObject parentElement;

    @objid ("7ec96be0-1dec-11e2-8cad-001ec947c8cc")
    private GmNodeModel sourceNode;

    @objid ("7ec96be1-1dec-11e2-8cad-001ec947c8cc")
    private GmCompositeNode destNode;

    @objid ("7ec96be5-1dec-11e2-8cad-001ec947c8cc")
    private MObject createdElement;

    /**
     * Source anchor model
     */
    @objid ("7ec96be6-1dec-11e2-8cad-001ec947c8cc")
    private Object srcAnchorModel;

    /**
     * Creates a node creation command.
     * @param context Details on the MObject and/or the node to create
     */
    @objid ("7ec96be8-1dec-11e2-8cad-001ec947c8cc")
    public CreateLinkToVoidCommand(ModelioCreationContext context) {
        this.context = context;
    }

    @objid ("7ec96bec-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.sourceNode.getDiagram();
        
        MObject newElement = this.context.getElementToUnmask();
        
        if (newElement == null) {
            newElement = createElement(diagram.getModelManager().getModelFactory(this.parentElement), diagram.getModelManager().getModelServices().getElementNamer());
        }
        
        // Show the link between the source node
        if (this.sourceNode != this.destNode) {
            final GmLink gmlink = diagram.unmaskLink(newElement);
        
            final IGmPath path = new GmPath();
            path.setSourceAnchor(this.srcAnchorModel);
            path.setTargetAnchor(new GmSourceSatelliteAnchor(this.destinationLocation));
            path.setPathData(new ArrayList<>());
        
            gmlink.setLayoutData(path);
        
            this.sourceNode.addStartingLink(gmlink);
            diagram.addEndingLink(gmlink);
        }
    }

    /**
     * Set the node inside which the node will be created.
     * @param destNode The node in which the node will be created.
     */
    @objid ("7ec96bef-1dec-11e2-8cad-001ec947c8cc")
    public void setDestinationNode(GmCompositeNode destNode) {
        this.destNode = destNode;
    }

    /**
     * Set the parent element independently from the parent node.
     * @param parentElement the parent element.
     */
    @objid ("7ec96bf3-1dec-11e2-8cad-001ec947c8cc")
    public void setParentElement(MObject parentElement) {
        this.parentElement = parentElement;
    }

    /**
     * Set the node on which the created node will be linked.
     * <p>
     * Set the parent element to be the represented element of the source node.
     * @param sourceNode the source node.
     */
    @objid ("7ecbce0b-1dec-11e2-8cad-001ec947c8cc")
    public void setSourceNode(GmNodeModel sourceNode) {
        this.sourceNode = sourceNode;
        this.parentElement = sourceNode.getRelatedElement();
    }

    /**
     * Create and initialize the model element.
     * @param modelFactory the model factory.
     * @return the created model element.
     */
    @objid ("7ecbce0f-1dec-11e2-8cad-001ec947c8cc")
    protected MObject createElement(final IModelFactory modelFactory, IElementNamer elementNamer) {
        // Create the MObject...
        this.createdElement = modelFactory.createElement(this.context.getMetaclass());
        
        // ... and attach it to its parent.
        try {
            this.parentElement.mGet(this.parentElement.getMClass().getDependency(this.context.getDependency())).add(
                    this.createdElement);
        } catch (Exception e) {
            // FIXME: use a finer type of exception.
            // The dependency indicated in the context cannot be used: try
            // to find a valid one.
            final MDependency compositionDep = MTools.getMetaTool().getDefaultCompositionDep(this.parentElement, this.createdElement);
            if (compositionDep != null) {
                this.parentElement.mGet(compositionDep).add(this.createdElement);
            } else {
                this.createdElement.delete();
                throw e;
            }
        }
        
        // Attach the stereotype if needed.
        if (this.context.getStereotype() != null && this.createdElement instanceof ModelElement) {
            ((ModelElement) this.createdElement).getExtension().add(this.context.getStereotype());
        }
        
        // Set default name
        this.createdElement.setName(elementNamer.getUniqueName(this.createdElement));
        return this.createdElement;
    }

    /**
     * Set the destination location.
     * @param dimension destination location in absolute coordiantes.
     */
    @objid ("7ecbce16-1dec-11e2-8cad-001ec947c8cc")
    public void setDestinationLocation(final Dimension dimension) {
        this.destinationLocation = dimension;
    }

    @objid ("7ecbce1d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canExecute() {
        if (!MTools.getAuthTool().canModify(this.sourceNode.getDiagram().getRelatedElement()))
            return false;
        
        final MObject newElement = this.context.getElementToUnmask();
        
        if (newElement == null) {
            return MTools.getAuthTool().canAdd(this.parentElement, this.context.getMetaclass());
        } else {
            return true;
        }
    }

    @objid ("7ecbce21-1dec-11e2-8cad-001ec947c8cc")
    protected GmCompositeNode getDestNode() {
        return this.destNode;
    }

    @objid ("7ecbce25-1dec-11e2-8cad-001ec947c8cc")
    protected Dimension getDestinationLocation() {
        return this.destinationLocation;
    }

    @objid ("7ecbce2b-1dec-11e2-8cad-001ec947c8cc")
    protected GmNodeModel getSourceNode() {
        return this.sourceNode;
    }

    @objid ("7ecbce2f-1dec-11e2-8cad-001ec947c8cc")
    protected ModelioCreationContext getContext() {
        return this.context;
    }

    /**
     * Get the parent element of the element to create.
     * @return the parent element.
     */
    @objid ("7ecbce33-1dec-11e2-8cad-001ec947c8cc")
    public MObject getParentElement() {
        return this.parentElement;
    }

    /**
     * Get the created model element.
     * <p>
     * Returns <code>null</code> until {@link #execute()} has been called.
     * @return the created element.
     */
    @objid ("7ecbce38-1dec-11e2-8cad-001ec947c8cc")
    public MObject getCreatedElement() {
        return this.createdElement;
    }

    /**
     * Set the source anchor model.
     * @param srcAnchorModel the source anchor model.
     */
    @objid ("7ecbce3d-1dec-11e2-8cad-001ec947c8cc")
    public void setSourceAnchor(final Object srcAnchorModel) {
        this.srcAnchorModel = srcAnchorModel;
    }

}
