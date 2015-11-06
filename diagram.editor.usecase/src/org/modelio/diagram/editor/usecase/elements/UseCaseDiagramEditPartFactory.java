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
                                    

package org.modelio.diagram.editor.usecase.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.diagram.editor.statik.elements.association.AssociationEditPart;
import org.modelio.diagram.editor.usecase.elements.actor.ActorEditPart;
import org.modelio.diagram.editor.usecase.elements.actor.GmActor;
import org.modelio.diagram.editor.usecase.elements.actor.GmActorPrimaryNode;
import org.modelio.diagram.editor.usecase.elements.actor.SimpleActorEditPart;
import org.modelio.diagram.editor.usecase.elements.communicationlink.GmCommunicationLink;
import org.modelio.diagram.editor.usecase.elements.extensionpoint.GmExtensionPoint;
import org.modelio.diagram.editor.usecase.elements.system.GmSystem;
import org.modelio.diagram.editor.usecase.elements.system.GmSystemFreeZone;
import org.modelio.diagram.editor.usecase.elements.system.SystemEditPart;
import org.modelio.diagram.editor.usecase.elements.system.SystemFreeZoneEditPart;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCase;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCasePrimaryNode;
import org.modelio.diagram.editor.usecase.elements.usecase.UseCaseEditPart;
import org.modelio.diagram.editor.usecase.elements.usecasedependency.GmExtensionPointLabel;
import org.modelio.diagram.editor.usecase.elements.usecasedependency.GmUseCaseDependency;
import org.modelio.diagram.editor.usecase.elements.usecasedependency.UseCaseDependencyEditPart;
import org.modelio.diagram.editor.usecase.elements.usecasediagram.GmUseCaseDiagram;
import org.modelio.diagram.editor.usecase.elements.usecasediagram.UseCaseDiagramEditPart;
import org.modelio.diagram.elements.common.groupitem.GroupItemEditPart;
import org.modelio.diagram.elements.common.label.base.GmElementLabelEditPart;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;

@objid ("5e8a464d-55b7-11e2-877f-002564c97630")
public final class UseCaseDiagramEditPartFactory implements EditPartFactory {
    @objid ("5e8a464f-55b7-11e2-877f-002564c97630")
    private StructuredModeEditPartFactory structuredModeEditPartFactory = new StructuredModeEditPartFactory();

    @objid ("5e8a4651-55b7-11e2-877f-002564c97630")
    private static final UseCaseDiagramEditPartFactory INSTANCE = new UseCaseDiagramEditPartFactory();

    @objid ("5e8a4653-55b7-11e2-877f-002564c97630")
    private SimpleModeEditPartFactory simpleModeEditPartFactory = new SimpleModeEditPartFactory();

    @objid ("5e8a4655-55b7-11e2-877f-002564c97630")
    private UseCaseDiagramEditPartFactory() {
        // Nothing to do
    }

    @objid ("5e8a4658-55b7-11e2-877f-002564c97630")
    public static UseCaseDiagramEditPartFactory getInstance() {
        return INSTANCE;
    }

    @objid ("5e8a465d-55b7-11e2-877f-002564c97630")
    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart = null;
        
        if (model instanceof GmNodeModel) {
            // For node models, delegates according the representation model.
            GmNodeModel node = (GmNodeModel) model;
            switch (node.getRepresentationMode()) {
                case IMAGE:
                    break;
                case SIMPLE:
                    editPart = this.simpleModeEditPartFactory.createEditPart(context, model);
                    break;
                case STRUCTURED:
                    editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
            }
        
            if (editPart != null)
                return editPart;
        
            return null;
        }
        // Link models are always in structured mode.
        editPart = this.structuredModeEditPartFactory.createEditPart(context, model);
        
        if (editPart != null)
            return editPart;
        return null;
    }

    @objid ("5e8a4664-55b7-11e2-877f-002564c97630")
    public class StructuredModeEditPartFactory implements EditPartFactory {
        @objid ("5e8a4666-55b7-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmUseCaseDiagram.class) {
                editPart = new UseCaseDiagramEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmActor.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmActorPrimaryNode.class) {
                editPart = new ActorEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmUseCase.class) {
                editPart = new PortContainerEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmUseCasePrimaryNode.class) {
                editPart = new UseCaseEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmExtensionPoint.class) {
                editPart = new GroupItemEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmExtensionPointLabel.class) {
                editPart = new GmElementLabelEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmUseCaseDependency.class) {
                editPart = new UseCaseDependencyEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmCommunicationLink.class) {
                editPart = new AssociationEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmSystem.class) {
                editPart = new SystemEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            if (model.getClass() == GmSystemFreeZone.class) {
                editPart = new SystemFreeZoneEditPart();
                editPart.setModel(model);
                return editPart;
            }
            return null;
        }

    }

    @objid ("5e8a466d-55b7-11e2-877f-002564c97630")
    public class SimpleModeEditPartFactory implements EditPartFactory {
        @objid ("5e8a466f-55b7-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmActorPrimaryNode.class) {
                editPart = new SimpleActorEditPart();
                editPart.setModel(model);
                return editPart;
            }
            return null;
        }

    }

}
