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
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.modelio.diagram.elements.common.ghostlink.GhostLinkEditPart;
import org.modelio.diagram.elements.common.ghostnode.GhostNodeEditPart;
import org.modelio.diagram.elements.core.commands.DeleteInDiagramCommand;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.drawings.core.IGmDrawing;

/**
 * Default edit policy for the {@link EditPolicy#COMPONENT_ROLE} role that can delete a graphic element from the
 * diagram.
 * <p>
 * A model-based EditPolicy for <i>components within a </i>container</i>. A model-based EditPolicy only knows about the
 * host's model and the basic operations it supports. A <i>component</i> is anything that is inside a container. By
 * default, DefaultDeleteEditPolicy understands being DELETEd from its container. Subclasses can add support to handle
 * additional behavior specific to the model.
 * <P>
 * DELETE is forwarded to the <i>parent</i> EditPart, but subclasses may also contribute to the delete by overriding
 * {@link #getDeleteCommand(GroupRequest)}.
 * <P>
 * This EditPolicy is not a {@link org.eclipse.gef.editpolicies.GraphicalEditPolicy}, and should not be used to show
 * feedback or interact with the host's visuals in any way.
 * <P>
 * This EditPolicy should not be used with {@link org.eclipse.gef.ConnectionEditPart}. Connections do not really have a
 * parent; use {@link ConnectionEditPolicy}.
 * 
 * @author cmarin
 * @see org.eclipse.gef.editpolicies.ComponentEditPolicy
 */
@objid ("80bbb745-1dec-11e2-8cad-001ec947c8cc")
public class DefaultDeleteNodeEditPolicy extends AbstractEditPolicy {
    /**
     * Factors the incoming Request into ORPHANs and DELETEs.
     * @see org.eclipse.gef.EditPolicy#getCommand(Request)
     */
    @objid ("80bbb749-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Command getCommand(Request request) {
        if (REQ_DELETE.equals(request.getType()))
            return getDeleteCommand((GroupRequest) request);
        return null;
    }

    /**
     * Create a DeleteInDiagramCommand if the edit part is selectable.
     * <p>
     * Forwards the request to its parent if the edit part is not selectable.
     * @param request the DeleteRequest
     * @return a delete command
     */
    @objid ("80bbb754-1dec-11e2-8cad-001ec947c8cc")
    protected Command getDeleteCommand(GroupRequest request) {
        if (getHost().isSelectable()) {
            final Object model = getHost().getModel();
            if (model instanceof GmModel) {
                final GmModel gmModel = (GmModel)model;
        
                // Allow deletion only if the graphic is a main node/link
                if (((gmModel.getRepresentedElement() != null)
                       
                        || getHost() instanceof GhostNodeEditPart
                        || getHost() instanceof GhostLinkEditPart)) {
                    DeleteInDiagramCommand ret = new DeleteInDiagramCommand();
                    ret.setNodetoDelete((IGmObject) model);
                    return ret;
                }
            } else if(model instanceof IGmDrawing) {
                DeleteInDiagramCommand ret = new DeleteInDiagramCommand();
                ret.setNodetoDelete((IGmObject) model);
                return ret;
            } 
        }
        return getHost().getParent().getCommand(request);
    }

}
