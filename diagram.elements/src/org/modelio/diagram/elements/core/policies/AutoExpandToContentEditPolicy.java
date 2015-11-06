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
                                    

package org.modelio.diagram.elements.core.policies;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.modelio.diagram.elements.core.commands.ExpandToContentCommand;

/**
 * The host will try to expands itself to fits its content when a child node is added, removed or changed.
 * <p>
 * This policy should be installed with the {@link AutoFitToContentEditPolicy#ROLE} role.
 * 
 * @author cmarin
 */
@objid ("80b22dc4-1dec-11e2-8cad-001ec947c8cc")
public class AutoExpandToContentEditPolicy extends GraphicalEditPolicy {
    @objid ("80b22dc8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Command getCommand(final Request request) {
        final Object reqType = request.getType();
        
        if (REQ_ADD.equals(reqType))
            return getFitCommand();
        
        if (REQ_ORPHAN_CHILDREN.equals(reqType))
            return getFitCommand();
        
        if (REQ_MOVE_CHILDREN.equals(reqType))
            return getFitCommand();
        
        if (REQ_CREATE.equals(reqType))
            return getFitCommand();
        return null;
    }

    @objid ("80b22dd3-1dec-11e2-8cad-001ec947c8cc")
    private Command getFitCommand() {
        return new ExpandToContentCommand((GraphicalEditPart) getHost());
    }

}
