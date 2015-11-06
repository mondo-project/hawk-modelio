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
                                    

package org.modelio.diagram.editor.statik.elements.narylink;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.helpers.UnmaskHelper;
import org.modelio.diagram.elements.core.link.ModelioLinkCreationContext;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.gproject.model.IElementConfigurator;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NaryLink;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;

/**
 * Command that create a n-ary Association in Ob model, links it to several elements and then unmask it.
 * 
 * @author cmarin
 */
@objid ("35e379a0-55b7-11e2-877f-002564c97630")
public class CreateNLinkCommand extends Command {
    @objid ("a6fc42ab-55c2-11e2-9337-002564c97630")
    private ModelioLinkCreationContext context;

    @objid ("a6fc42af-55c2-11e2-9337-002564c97630")
    private GmCompositeNode parentNode;

    @objid ("b72c91c2-e034-4a44-9f5a-ade5d5c71911")
    private List<IGmLinkable> sourceModels;

    /**
     * Initial layout constraint
     */
    @objid ("db0e4917-6636-4d76-aa86-8263082d93f5")
    private Rectangle layoutConstraint;

    @objid ("bde297a8-8e13-42e3-947d-41ac9094c93a")
    private EditPart editPart;

    /**
     * Creates a n-ary association creation command.
     * @param editPart the edit part producing this command
     * @param sourceModels The models that are to be linked to the created association.
     * @param parentNode The parent node unmasking the "diamond" of the association.
     * @param context Details on the MObject and/or the node to create
     * @param constraint The initial layout constraint of the association diamond.
     */
    @objid ("35e379b0-55b7-11e2-877f-002564c97630")
    public CreateNLinkCommand(final EditPart editPart, final List<IGmLinkable> sourceModels, final GmCompositeNode parentNode, final ModelioLinkCreationContext context, final Rectangle constraint) {
        this.editPart = editPart;
        this.sourceModels = sourceModels;
        this.parentNode = parentNode;
        this.context = context;
        this.layoutConstraint = constraint;
    }

    @objid ("35e379c5-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        // the diagram must be modifiable 
        if (!MTools.getAuthTool().canModify(this.parentNode.getDiagram().getRelatedElement()))
            return false;
        
        // Must have at least 3 ends
        if (this.sourceModels.size() < 3) {
            return false;
        }
        
        // All sourceNodes must be modifiable.
        for (IGmLinkable sourceModel : this.sourceModels) {
            if (!MTools.getAuthTool().canModify(sourceModel.getRelatedElement())) {
                return false;
            }
        }
        return true;
    }

    @objid ("35e379ca-55b7-11e2-877f-002564c97630")
    @Override
    public void execute() {
        final GmAbstractDiagram diagram = this.parentNode.getDiagram();
        
        NaryLink newAssoc = (NaryLink) this.context.getElementToUnmask();
        ArrayList<NaryLinkEnd> createdAssocs = new ArrayList<>(this.sourceModels.size());
        
        if (newAssoc == null) {
            // Create the association node...
            ModelManager modelManager = diagram.getModelManager();
            final IModelFactory modelFactory = modelManager.getModelFactory(diagram.getRelatedElement());
            newAssoc = modelFactory.createNaryLink();
        
            // ... and create all roles.
            IElementConfigurator configurer = modelManager.getModelServices().getElementConfigurer();
            for (IGmLinkable sourceModel : this.sourceModels) {
                final Instance el = (Instance) sourceModel.getRelatedElement();
                final NaryLinkEnd role = modelFactory.createNaryLinkEnd();
                role.setSource(el);
                role.setNaryLink(newAssoc);
                createdAssocs.add(role);
        
                configurer.configure(modelFactory, role, this.context.getProperties());
            }
        
            // Attach the stereotype if needed.
            if (this.context.getStereotype() != null) {
                newAssoc.getExtension().add(this.context.getStereotype());
            }
        
            // Some additional initializing steps might be needed.
            configurer.configure(modelFactory, newAssoc, this.context.getProperties());
        
        }
        
        // Show the new element in the diagram (ie create its Gm )
        diagram.unmask(this.parentNode, newAssoc, this.layoutConstraint);
        
        // Unmask all roles
        EditPartViewer viewer = this.editPart.getViewer();
        for (NaryLinkEnd r : createdAssocs) {
            Command cmd = UnmaskHelper.getUnmaskCommand(viewer, r, this.layoutConstraint.getCenter());
            cmd.execute();
        }
    }

}
