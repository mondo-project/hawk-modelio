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
                                    

package org.modelio.diagram.editor.object.elements.objectdiagram;

import java.util.ArrayList;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.helpers.UnmaskHelper;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Command used for smart unmask interactions: <br>
 * Creates a {@link BindableInstance} representing the dropped element.
 * 
 * @author cma
 */
@objid ("2579588f-1d79-42a0-8b33-45ee77af3c09")
public class SmartCreateInstanceCommand extends Command {
    @objid ("99d62d78-8cc0-4a1f-ae28-bfb5ee3afb5d")
    private MObject parentElement;

    @objid ("1300acc2-0186-42d7-899c-92fa684b3c55")
    private MObject toUnmask;

    @objid ("a84f8a70-8dd0-4991-9be2-38424c954e76")
    private EditPart parentEditPart;

    @objid ("bcfc42c2-2171-487d-806c-6b204ea3a0d6")
    private EditPartViewer viewer;

    @objid ("2c45c796-7d10-49d1-ba52-2d11018d19b8")
    private Point location;

    /**
     * @param dropLocation the location where the ObjectNode is to be unmasked.
     * @param toUnmask the element that the ObjectNode will represent.
     * @param parentEditPart the edit part handling the unmasking
     * @param parentElement the element that will own the new ObjectNode
     */
    @objid ("fa8f54ba-704a-481a-a102-0d6876d80782")
    public SmartCreateInstanceCommand(final Point dropLocation, final MObject toUnmask, final EditPart parentEditPart, final MObject parentElement) {
        this.location = dropLocation;
        this.toUnmask = toUnmask;
        this.parentEditPart = parentEditPart;
        this.parentElement = parentElement;
        this.viewer = parentEditPart.getViewer();
    }

    @objid ("2b9d4259-07e9-498a-87e1-8f9065947d9c")
    @Override
    public boolean canExecute() {
        final GmModel gmModel = (GmModel) this.parentEditPart.getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        if (!MTools.getAuthTool().canModify(gmDiagram.getRelatedElement()))
            return false;
        return (this.parentElement != null && this.parentElement.isValid() && this.parentElement.isModifiable());
    }

    @objid ("20b79399-3107-44a7-b5fd-2e9c610de595")
    @Override
    public void execute() {
        GmModel gmModel = (GmModel) this.parentEditPart.getModel();
        GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        IModelFactory factory = gmDiagram.getModelManager().getModelFactory(gmDiagram.getRelatedElement());
        
        // Create the smart node
        Instance instanceNode = factory.createInstance();
        
        // Attach to parent
        final MDependency effectiveDependency = MTools.getMetaTool().getDefaultCompositionDep(this.parentElement,instanceNode);
        
        if (effectiveDependency == null) {
            throw new IllegalStateException("Cannot find a composition dependency to attach " +
                    instanceNode.toString() +
                    " to " +
                    this.parentElement.toString());
        }
        this.parentElement.mGet(effectiveDependency).add(instanceNode);
        
        // Attach to dropped element
        Collection<Port> ports = null;
        if (this.toUnmask instanceof Classifier) {
            instanceNode.setBase((NameSpace) this.toUnmask);
            ports = createPorts(instanceNode);
        } else if (this.toUnmask instanceof NameSpace) {
            instanceNode.setBase((NameSpace) this.toUnmask);
        }
        
        instanceNode.setName(gmDiagram.getModelManager().getModelServices().getElementNamer().getUniqueName("Instance", instanceNode));
        
        // Unmask the node
        unmaskElement(instanceNode);
        
        // Unmask the part ports too
        if (ports != null && ports.size() > 0) {
            // Force graphical validation of parent to avoid some nasty side effects
            ((GraphicalEditPart) this.parentEditPart).getFigure().getUpdateManager().performValidation();
        
            // Translate point to unmask port on the right
            this.location.translate(100, 10);
            Command cmd = UnmaskHelper.getUnmaskCommand(this.viewer, ports, this.location);
            if (cmd != null && cmd.canExecute())
                cmd.execute();
        }
    }

    /**
     * Copy the Ports of the base class to the instance.
     * @param part the part where Ports are to be added.
     * @return the created ports.
     */
    @objid ("b19835f4-2d48-4818-a476-08afdde58902")
    private Collection<Port> createPorts(final Instance part) {
        final Classifier type = (Classifier) part.getBase();
        final Collection<Port> ret = new ArrayList<>(type.getInternalStructure().size());
        
        for (Instance typePart : type.getInternalStructure()) {
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

    @objid ("9c594154-0e47-4c25-add3-1e7b74a62421")
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
