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
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.modelio.diagram.elements.core.policies.DefaultElementDropEditPolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;

/**
 * Drag & drop edit policy for smart interactions.
 * <p>
 * Allow drag and drop of Operation, Behaviors, BehaviorParameter and more...
 */
@objid ("614b57a8-55b6-11e2-877f-002564c97630")
public class SmartDropEditPolicy extends DefaultElementDropEditPolicy {
    @objid ("614b57ac-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPart getDropTargetEditPart(final ModelElementDropRequest request) {
        return super.getDropTargetEditPart(request);
    }

    @objid ("614b57b5-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getSmartDropCommand(final ModelElementDropRequest request) {
        CompoundCommand command = new CompoundCommand();
        
        if (command.isEmpty())
            return null;
        return command;
    }

}
