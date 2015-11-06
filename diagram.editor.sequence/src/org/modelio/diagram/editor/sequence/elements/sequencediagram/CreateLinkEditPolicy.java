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
                                    

package org.modelio.diagram.editor.sequence.elements.sequencediagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.ReconnectRequest;
import org.modelio.diagram.editor.sequence.elements.message.CreateMessageCommand;
import org.modelio.diagram.elements.core.link.CreateBendedConnectionRequest;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.link.ModelioLinkCreationContext;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;

/**
 * Edit policy that allow creating Links to/from an Execution
 */
@objid ("d97165fa-55b6-11e2-877f-002564c97630")
public class CreateLinkEditPolicy extends DefaultCreateLinkEditPolicy {
    @objid ("d97165fe-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest req) {
        CreateMessageCommand startCommand = (CreateMessageCommand) super.getConnectionCompleteCommand(req);
        int sourceTime = startCommand.getSourceTime();
        int targetTime = req.getLocation().y;
        // Do not allow a message to get back in time.
        if (targetTime < sourceTime) {
            startCommand.setTargetTime(sourceTime);
        } else {
            startCommand.setTargetTime(targetTime);
        }
        return startCommand;
    }

    @objid ("d9716604-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest req) {
        if (req.getNewObject() instanceof ModelioLinkCreationContext) {
            ModelioLinkCreationContext context = (ModelioLinkCreationContext) req.getNewObject();
            if (Metamodel.getMClass(Message.class).getName().equals(context.getMetaclass())) {
        
                CreateMessageCommand cmd = new CreateMessageCommand(context);
        
                cmd.setSource((GmNodeModel) getHost().getModel());
                req.setStartCommand(cmd);
                cmd.setSourceTime(req.getLocation().y);
                
                if (req instanceof CreateBendedConnectionRequest) {
                    cmd.setRequest((CreateBendedConnectionRequest) req);
                }
                return cmd;
            }
        }
        return null;
    }

    @objid ("d971660a-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest req) {
        return null;
    }

    @objid ("d9716610-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest req) {
        return null;
    }

    @objid ("d972ec9d-55b6-11e2-877f-002564c97630")
    @Override
    protected void showCreationFeedback(CreateConnectionRequest request) {
        // Copy and hack the request to make sure the "destination" Y is never
        // less than the source. Copy is needed cause we don't want to modify
        // the original request (to avoid some nasty side effects)
        if (request instanceof CreateBendedConnectionRequest) {
            CreateBendedConnectionRequest request_origin = (CreateBendedConnectionRequest) request;
            CreateBendedConnectionRequest request_copy = new CreateBendedConnectionRequest();
            request_copy.setFactory((CreationFactory) request_origin.getNewObject()); //< hack: we know that we stored the factory as the new object.
            request_copy.setLocation(request_origin.getLocation().getCopy());
            request_copy.setSize(null);
            request_copy.setSourceEditPart(request_origin.getSourceEditPart());
            request_copy.setStartCommand(request_origin.getStartCommand());
            request_copy.setTargetEditPart(request_origin.getTargetEditPart());
            request_copy.setType(request_origin.getType());
            request_copy.getData().setRoutingMode(request_origin.getData().getRoutingMode());
            request_copy.getData().setSrcPoint(request_origin.getData().getSrcPoint());
            request_copy.getData().setLastPoint(request_origin.getData().getLastPoint());
            // Fix the "Y" coordinate.
            request_copy.getLocation().y = Math.max(((CreateMessageCommand) request_origin.getStartCommand()).getSourceTime(),
                                                    request_origin.getLocation().y);
            super.showCreationFeedback(request_copy);
        } else {
            super.showCreationFeedback(request);
        }
    }

}
