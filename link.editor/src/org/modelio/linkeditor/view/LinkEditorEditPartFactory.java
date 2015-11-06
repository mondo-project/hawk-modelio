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
                                    

package org.modelio.linkeditor.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.linkeditor.view.background.BackgroundEditPart;
import org.modelio.linkeditor.view.background.BackgroundModel;
import org.modelio.linkeditor.view.edge.EdgeEditPart;
import org.modelio.linkeditor.view.node.BusEditPart;
import org.modelio.linkeditor.view.node.EdgeBus;
import org.modelio.linkeditor.view.node.GraphNode;
import org.modelio.linkeditor.view.node.NodeEditPart;

/**
 * Edit part factory for the Link Editor.
 * 
 * @author fpoyer
 */
@objid ("1ba1eb56-5e33-11e2-b81d-002564c97630")
class LinkEditorEditPartFactory implements EditPartFactory {
    @objid ("d4995d77-5efd-11e2-a8be-00137282c51b")
    private final IMModelServices modelServices;

    @objid ("1ba1eb5a-5e33-11e2-b81d-002564c97630")
    @Override
    public EditPart createEditPart(final EditPart context, final Object model) {
        EditPart editPart;
        if (model instanceof BackgroundModel) {
            editPart = new BackgroundEditPart(this.modelServices);
            editPart.setModel(model);
            return editPart;
        }
        
        if (model instanceof GraphNode) {
            editPart = new NodeEditPart();
            editPart.setModel(model);
            return editPart;
        }
        
        if (model instanceof Edge) {
            editPart = new EdgeEditPart();
            editPart.setModel(model);
            return editPart;
        }
        
        if (model instanceof EdgeBus) {
            editPart = new BusEditPart();
            editPart.setModel(model);
            return editPart;
        }
        return null;
    }

    @objid ("d4995d79-5efd-11e2-a8be-00137282c51b")
    public LinkEditorEditPartFactory(IMModelServices modelServices) {
        this.modelServices = modelServices;
    }

}
