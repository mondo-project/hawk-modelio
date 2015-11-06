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
                                    

package org.modelio.diagram.editor.usecase.elements.system;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.core.node.GmNodeModel;

@objid ("5e5664fe-55b7-11e2-877f-002564c97630")
public class TranslateChildrenCommand extends Command {
    @objid ("3f4cf6d8-ad77-482e-956f-eb6a829fb2a8")
    private EditPart container;

    @objid ("b6ebaee8-8337-4efa-91a4-97076ad8b7d1")
    private Point moveDelta;

    @objid ("5e566502-55b7-11e2-877f-002564c97630")
    public TranslateChildrenCommand(final EditPart container, final Point moveDelta) {
        this.container = container;
        this.moveDelta = moveDelta;
    }

    @objid ("5e566509-55b7-11e2-877f-002564c97630")
    @Override
    public void execute() {
        for (Object childObj : this.container.getChildren()) {
            EditPart childEditPart = (EditPart) childObj;
            GmNodeModel childModel = (GmNodeModel) childEditPart.getModel();
            if (this.container.getModel().equals(childModel.getParent())) {
                // There is a possibility (when the port container is deleting
                // a child) that the edit part has a child whose model is not a
                // child of the GmPortContainer.
                assert (childModel.getLayoutData() == null || childModel.getLayoutData() instanceof Rectangle) : "Unexpected type of constraint, this command can only work with Rectangle, found: " +
                                                                                                                 childModel.getLayoutData();
                if (childModel.getLayoutData() instanceof Rectangle) {
                    childModel.setLayoutData(((Rectangle) childModel.getLayoutData()).getTranslated(this.moveDelta));
                }
            }
        }
    }

}
