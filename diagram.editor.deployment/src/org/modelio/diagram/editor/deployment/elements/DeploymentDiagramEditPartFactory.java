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
                                    

package org.modelio.diagram.editor.deployment.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.diagram.editor.deployment.elements.artifact.ArtifactEditPart;
import org.modelio.diagram.editor.deployment.elements.artifact.GmArtifact;
import org.modelio.diagram.editor.deployment.elements.artifact.GmArtifactPrimaryNode;
import org.modelio.diagram.editor.deployment.elements.deploymentdiagram.DeploymentDiagramEditPart;
import org.modelio.diagram.editor.deployment.elements.deploymentdiagram.GmDeploymentDiagram;
import org.modelio.diagram.editor.deployment.elements.manifestation.GmManifestation;
import org.modelio.diagram.editor.deployment.elements.manifestation.ManifestationEditPart;
import org.modelio.diagram.editor.deployment.elements.node.GmNode;
import org.modelio.diagram.editor.deployment.elements.node.GmNodePrimaryNode;
import org.modelio.diagram.editor.deployment.elements.node.NodeEditPart;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;

/**
 * The State diagram EditPart factory.
 * <p>
 * This factory only processes the state specific edit parts. It is intended to be used only as a cascaded factory in
 * order to dynamically enriching the Modelio standard factory so that this latter ends by being able to process the
 * complete UML. The StateDiagramEditPartFactory does not provide edit part for simple and image mode.
 */
@objid ("9729a01f-55b6-11e2-877f-002564c97630")
public final class DeploymentDiagramEditPartFactory implements EditPartFactory {
    /**
     * the default factory to use when structured mode is requested.
     */
    @objid ("9729a021-55b6-11e2-877f-002564c97630")
    private StructuredModeEditPartFactory structuredModeEditPartFactory = new StructuredModeEditPartFactory();

    @objid ("9729a023-55b6-11e2-877f-002564c97630")
    private static final DeploymentDiagramEditPartFactory INSTANCE = new DeploymentDiagramEditPartFactory();

    @objid ("9729a025-55b6-11e2-877f-002564c97630")
    private DeploymentDiagramEditPartFactory() {
        // private default constructor
    }

    /**
     * @return the instance.
     */
    @objid ("9729a027-55b6-11e2-877f-002564c97630")
    public static EditPartFactory getInstance() {
        return INSTANCE;
    }

    @objid ("9729a02c-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart;
        if (model instanceof GmNodeModel) {
            // For node models, delegates according the representation model.
            GmNodeModel node = (GmNodeModel) model;
            switch (node.getRepresentationMode()) {
                case STRUCTURED:
                    editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
                    break;
                default:
                    editPart = null; // generically supported by standard factory
            }
        
            return editPart;
        } else {
            // Link models are always in structured mode.
            editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
            return editPart;
        }
    }

    /**
     * EditPart factory for node models in standard structured mode.
     * <p>
     * This is the default mode so the default factory.
     */
    @objid ("9729a032-55b6-11e2-877f-002564c97630")
    public class StructuredModeEditPartFactory implements EditPartFactory {
        @objid ("9729a034-55b6-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmArtifact.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmArtifactPrimaryNode.class) {
                editPart = new ArtifactEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmDeploymentDiagram.class) {
                editPart = new DeploymentDiagramEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmManifestation.class) {
                editPart = new ManifestationEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNode.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmNodePrimaryNode.class) {
                editPart = new NodeEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            // not found
            return null;
        }

    }

}
