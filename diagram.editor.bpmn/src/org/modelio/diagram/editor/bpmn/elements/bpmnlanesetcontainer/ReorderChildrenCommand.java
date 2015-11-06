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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;

/**
 * A command that reorders children of a composite node.
 */
@objid ("6149d117-55b6-11e2-877f-002564c97630")
public class ReorderChildrenCommand extends Command {
    @objid ("71d9c589-55c1-11e2-9337-002564c97630")
    private GmCompositeNode container;

    @objid ("71d9c58a-55c1-11e2-9337-002564c97630")
    private GmNodeModel childToMove;

    @objid ("71d9c58b-55c1-11e2-9337-002564c97630")
    private GmNodeModel reference;

    @objid ("614b577d-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        // this.container.removeChild(this.childToMove);
        this.container.moveChild(this.childToMove, this.container.getChildren().indexOf(this.reference));
    }

    /**
     * C'tor.
     * @param container the container in which children will be moved.
     * @param childToMove the child to move.
     * @param reference the reference: moved child will be moved just before this reference. If it is null, child will be
     * moved at the end of the container.
     */
    @objid ("614b5780-55b6-11e2-877f-002564c97630")
    public ReorderChildrenCommand(GmCompositeNode container, GmNodeModel childToMove, GmNodeModel reference) {
        this.container = container;
        this.childToMove = childToMove;
        this.reference = reference;
    }

}
