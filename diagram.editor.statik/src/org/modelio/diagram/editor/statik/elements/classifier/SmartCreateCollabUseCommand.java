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
                                    

package org.modelio.diagram.editor.statik.elements.classifier;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command used for smart unmask interactions: <br>
 * Creates a {@link CollaborationUse} typed by the dropped element.
 * 
 * @author cma
 */
@objid ("3436b42a-55b7-11e2-877f-002564c97630")
public class SmartCreateCollabUseCommand extends Command {
    @objid ("3436b42c-55b7-11e2-877f-002564c97630")
    private MObject parentElement;

    @objid ("3436b42f-55b7-11e2-877f-002564c97630")
    private MObject toUnmask;

    @objid ("5b56fb91-5bd5-11e2-9e33-00137282c51b")
    private EditPart parentEditPart;

    @objid ("13b8a296-eb6a-41d2-a813-aef4f3dc36ca")
    private Point location;

    /**
     * @param dropLocation the location where the ObjectNode is to be unmasked.
     * @param toUnmask the element that the ObjectNode will represent.
     * @param parentEditPart the edit part handling the unmasking
     * @param parentElement the element that will own the new ObjectNode
     */
    @objid ("3436b434-55b7-11e2-877f-002564c97630")
    public SmartCreateCollabUseCommand(final Point dropLocation, final MObject toUnmask, final EditPart parentEditPart, final MObject parentElement) {
        this.location = dropLocation;
        this.toUnmask = toUnmask;
        this.parentEditPart = parentEditPart;
        this.parentElement = parentElement;
    }

    @objid ("3436b443-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        final GmModel gmModel = (GmModel) this.parentEditPart.getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        return (this.parentElement != null && this.parentElement.isValid() && this.parentElement.isModifiable());
    }

    @objid ("34383a9a-55b7-11e2-877f-002564c97630")
    @Override
    public void execute() {
        GmModel gmModel = (GmModel) this.parentEditPart.getModel();
        GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        ModelManager modelManager = gmDiagram.getModelManager();
        IModelFactory factory = modelManager.getModelFactory(gmDiagram.getRelatedElement());
        
        // Create the smart node
        CollaborationUse collabUseNode = factory.createCollaborationUse();
        
        // Set type
        if (this.toUnmask instanceof Collaboration) {
            collabUseNode.setType((Collaboration) this.toUnmask);
        }
        
        // Attach to parent
        
        final MDependency effectiveDependency = MTools.getMetaTool().getDefaultCompositionDep(this.parentElement,
                collabUseNode);
        
        if (effectiveDependency == null) {
            throw new IllegalStateException("Cannot find a composition dependency to attach " +
                    collabUseNode.toString() +
                    " to " +
                    this.parentElement.toString());
        }
        
        this.parentElement.mGet(effectiveDependency).add(collabUseNode);
        
        // Unmask the node
        unmaskElement(collabUseNode);
    }

    @objid ("34383a9d-55b7-11e2-877f-002564c97630")
    private void unmaskElement(final MObject el) {
        final ModelioCreationContext gmCreationContext = new ModelioCreationContext(el);
        
        final CreateRequest creationRequest = new CreateRequest();
        creationRequest.setLocation(this.location);
        creationRequest.setSize(new Dimension(-1, -1));
        creationRequest.setFactory(gmCreationContext);
        
        final Command cmd = this.parentEditPart.getTargetEditPart(creationRequest)
                .getCommand(creationRequest);
        if (cmd != null && cmd.canExecute())
            cmd.execute();
    }

}
