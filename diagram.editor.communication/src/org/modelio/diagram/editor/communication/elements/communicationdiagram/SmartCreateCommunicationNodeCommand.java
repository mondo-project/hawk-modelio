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
                                    

package org.modelio.diagram.editor.communication.elements.communicationdiagram;

import java.util.ArrayList;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.helpers.UnmaskHelper;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command used for smart unmask interactions: <br>
 * Creates a {@link BindableInstance} representing the dropped element.
 * 
 * @author cma
 */
@objid ("7a2e30ff-55b6-11e2-877f-002564c97630")
public class SmartCreateCommunicationNodeCommand extends Command {
    @objid ("7a2e3101-55b6-11e2-877f-002564c97630")
    private MObject parentElement;

    @objid ("7a2e3104-55b6-11e2-877f-002564c97630")
    private MObject toUnmask;

    @objid ("05691e82-599a-11e2-ae45-002564c97630")
    private EditPart parentEditPart;

    @objid ("db9bef70-6b13-4259-a9c2-4d5ad23e44de")
    private Point location;

    /**
     * @param dropLocation the location where the ObjectNode is to be unmasked.
     * @param toUnmask the element that the ObjectNode will represent.
     * @param parentEditPart the edit part handling the unmasking
     * @param parentElement the element that will own the new ObjectNode
     */
    @objid ("7a2e3109-55b6-11e2-877f-002564c97630")
    public SmartCreateCommunicationNodeCommand(final Point dropLocation, final MObject toUnmask, final EditPart parentEditPart, final MObject parentElement) {
        this.location = dropLocation;
        this.toUnmask = toUnmask;
        this.parentEditPart = parentEditPart;
        this.parentElement = parentElement;
    }

    @objid ("7a2e3118-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canExecute() {
        final GmModel gmModel = (GmModel) this.parentEditPart.getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        return (this.parentElement != null && this.parentElement.isValid() && this.parentElement.getStatus()
                .isModifiable());
    }

    @objid ("7a2e311d-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        GmModel gmModel = (GmModel) this.parentEditPart.getModel();
        GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        ModelManager modelManager = gmDiagram.getModelManager();
        IModelFactory factory = modelManager.getModelFactory(gmDiagram.getRelatedElement());
        
        CommunicationInteraction interaction = (CommunicationInteraction) this.parentElement;
        
        // Create the communication node
        CommunicationNode commNode = factory.createCommunicationNode();
        commNode.setOwner(interaction);
        
        // Unmask the node
        unmaskElement(commNode);
        
        Instance instanceNode;
        if (this.toUnmask instanceof Instance) {
            instanceNode = (Instance) this.toUnmask;
        } else {
            instanceNode = createInstance(factory, modelManager.getModelServices().getElementNamer(), interaction);
        
            // Attach to dropped element
            Collection<Port> ports = null;
            if (this.toUnmask instanceof Instance) {
                ((BindableInstance) instanceNode).setRepresentedFeature((Instance) this.toUnmask);
            } else if (this.toUnmask instanceof Attribute) {
                ((BindableInstance) instanceNode).setRepresentedFeature((Attribute) this.toUnmask);
                instanceNode.setBase(((Attribute) this.toUnmask).getType());
            } else if (this.toUnmask instanceof AssociationEnd) {
                ((BindableInstance) instanceNode).setRepresentedFeature((AssociationEnd) this.toUnmask);
            } else if (this.toUnmask instanceof GeneralClass) {
                instanceNode.setBase((NameSpace) this.toUnmask);
                ports = createPorts((BindableInstance) instanceNode);
            } else if (this.toUnmask instanceof ModelElement) {
                ((BindableInstance) instanceNode).setRepresentedFeature((ModelElement) this.toUnmask);
            }
        
            // Unmask the part ports too
            if (ports != null) {
                // Translate point to unmask port on the right
                this.location.translate(100, 10);
                Command cmd = UnmaskHelper.getUnmaskCommand(this.parentEditPart.getViewer(),
                        ports,
                        this.location);
                if (cmd != null && cmd.canExecute())
                    cmd.execute();
            }
        }
        
        // Link the node to the instance
        commNode.setRepresented(instanceNode);
        commNode.setName(instanceNode.getName());
    }

    @objid ("7a2e3120-55b6-11e2-877f-002564c97630")
    private BindableInstance createInstance(final IModelFactory factory, IElementNamer elementNamer, final CommunicationInteraction interaction) {
        // Get the "locals" collaboration
        Collaboration localsCollaboration = getLocalsCollaboration(interaction);
        // Create the "locals" collaboration if none was found
        if (localsCollaboration == null) {
            localsCollaboration = createLocalsCollaboration(factory, interaction);
        }
        
        // Create the new instance
        BindableInstance instanceNode = factory.createBindableInstance();
        localsCollaboration.getDeclared().add(instanceNode);
        
        instanceNode.setName(elementNamer.getUniqueName("r", instanceNode));
        return instanceNode;
    }

    @objid ("7a2e312e-55b6-11e2-877f-002564c97630")
    private Collaboration createLocalsCollaboration(final IModelFactory factory, final CommunicationInteraction interaction) {
        Collaboration localsCollaboration;
        localsCollaboration = factory.createCollaboration();
        localsCollaboration.setName("locals");
        interaction.getOwnedCollaboration().add(localsCollaboration);
        return localsCollaboration;
    }

    @objid ("7a2e313c-55b6-11e2-877f-002564c97630")
    private Collaboration getLocalsCollaboration(final CommunicationInteraction interaction) {
        for (Collaboration collaboration : interaction.getOwnedCollaboration()) {
            if (collaboration.getName().equals("locals")) {
                return collaboration;
            }
        }
        return null;
    }

    /**
     * Copy the Ports of the base class to the instance.
     * @param part the part where Ports are to be added.
     * @return the created ports.
     */
    @objid ("7a2fb7a1-55b6-11e2-877f-002564c97630")
    private Collection<Port> createPorts(final BindableInstance part) {
        final Classifier type = (Classifier) part.getBase();
        final Collection<Port> ret = new ArrayList<>();
        
        for (BindableInstance typePart : type.getInternalStructure()) {
            if (typePart instanceof Port) {
        
                final Port partPort = (Port) MTools.getModelTool().cloneElement(typePart);
                partPort.setInternalOwner(null);
                partPort.setCluster(part);
                partPort.setRepresentedFeature(typePart);
        
                ret.add(partPort);
            }
        }
        return ret;
    }

    @objid ("7a2fb7ae-55b6-11e2-877f-002564c97630")
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
