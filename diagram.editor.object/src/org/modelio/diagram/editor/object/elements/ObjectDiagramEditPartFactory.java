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
                                    

package org.modelio.diagram.editor.object.elements;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.diagram.editor.object.elements.objectdiagram.GmObjectDiagram;
import org.modelio.diagram.editor.object.elements.objectdiagram.ObjectDiagramEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;

/**
 * The State diagram EditPart factory.
 * <p>
 * This factory only processes the state specific edit parts. It is intended to be used only as a cascaded factory in
 * order to dynamically enriching the Modelio standard factory so that this latter ends by being able to process the
 * complete UML. The StateDiagramEditPartFactory does not provide edit part for simple and image mode.
 */
@objid ("9d664e13-55b6-11e2-877f-002564c97630")
public class ObjectDiagramEditPartFactory implements EditPartFactory {
    /**
     * the default factory to use when structured mode is requested.
     */
    @objid ("9d664e15-55b6-11e2-877f-002564c97630")
    private StructuredModeEditPartFactory structuredModeEditPartFactory = new StructuredModeEditPartFactory();

    @objid ("9d664e17-55b6-11e2-877f-002564c97630")
    private static final ObjectDiagramEditPartFactory INSTANCE = new ObjectDiagramEditPartFactory();

    @objid ("9d664e19-55b6-11e2-877f-002564c97630")
    private ObjectDiagramEditPartFactory() {
        // private default constructor
    }

    /**
     * @return the instance.
     */
    @objid ("9d664e1b-55b6-11e2-877f-002564c97630")
    public static EditPartFactory getInstance() {
        return INSTANCE;
    }

    @objid ("9d664e20-55b6-11e2-877f-002564c97630")
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
    @objid ("9d664e26-55b6-11e2-877f-002564c97630")
    public class StructuredModeEditPartFactory implements EditPartFactory {
        @objid ("9d664e28-55b6-11e2-877f-002564c97630")
        @Override
        public EditPart createEditPart(EditPart context, Object model) {
            EditPart editPart = null;
            
            if (model.getClass() == GmObjectDiagram.class) {
                editPart = new ObjectDiagramEditPart();
                editPart.setModel(model);
                return editPart;
            }
            
            // not found
            return null;
        }

    }

}
