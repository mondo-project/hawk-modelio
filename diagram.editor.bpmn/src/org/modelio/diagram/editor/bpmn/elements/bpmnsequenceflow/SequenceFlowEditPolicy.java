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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnsequenceflow;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.CreateBpmnDataObjectCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.link.GmLinkLayoutEditPolicy;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;

@objid ("61a25153-55b6-11e2-877f-002564c97630")
public class SequenceFlowEditPolicy extends GmLinkLayoutEditPolicy {
    @objid ("61a3d7b9-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            CreateRequest createRequest = (CreateRequest) request;
            if (createRequest.getNewObject() instanceof ModelioCreationContext) {
                final ModelioCreationContext ctx = (ModelioCreationContext) createRequest.getNewObject();
                if (ctx.getElementToUnmask() instanceof BpmnMessage) {
                    return getHost();
                }
            }
        
        }
        return super.getTargetEditPart(request);
    }

    @objid ("61a3d7bf-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType().equals("BpmnDataObject")) {
            GmBpmnSequenceFlow gm = (GmBpmnSequenceFlow) getHost().getModel();
            ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
            return new CreateBpmnDataObjectCommand((BpmnSequenceFlow) gm.getRelatedElement(), gm, ctx);
        }
        return super.getCreateCommand(request);
    }

}
