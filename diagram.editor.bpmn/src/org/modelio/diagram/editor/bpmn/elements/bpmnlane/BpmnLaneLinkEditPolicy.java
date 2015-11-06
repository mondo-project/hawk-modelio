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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlane;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.modelio.diagram.elements.core.link.CreateBendedConnectionRequest;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.link.ModelioLinkCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Edit Policy who manage BpmnSequenceFLow, BpmnMessageFlow and BpmnDataAssociation
 */
@objid ("61177651-55b6-11e2-877f-002564c97630")
public class BpmnLaneLinkEditPolicy extends DefaultCreateLinkEditPolicy {
    @objid ("61177655-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(final Request request) {
        if (REQ_CONNECTION_START.equals(request.getType())) {
            GmModel model = (GmModel) getHost().getModel();
            MObject element = model.getRelatedElement();
            if (element instanceof BpmnLane) {
                if (!isFirstLane((BpmnLane) element)) {
                    return null;
                }
            }
        } else if (REQ_CONNECTION_END.equals(request.getType())) {
            GmModel model = (GmModel) getHost().getModel();
        
            if (request instanceof CreateBendedConnectionRequest) {
                CreateBendedConnectionRequest bendedrequest = (CreateBendedConnectionRequest) request;
                ModelioLinkCreationContext newobject = (ModelioLinkCreationContext) bendedrequest.getNewObject();
                if (newobject.getMetaclass().equals("BpmnSequenceFlow") ||
                    newobject.getMetaclass().equals("BpmnDataAssociation")) {
                    return null;
                }
            }
            
            if (request instanceof CreateConnectionRequest) {
                MObject element = model.getRelatedElement();
                    return getHost();           
            }
        
            MObject element = model.getRelatedElement();
            if (element instanceof BpmnLane) {
                if (!isFirstLane((BpmnLane) element))
                    return null;
            }
        }
        return super.getTargetEditPart(request);
    }

    @objid ("6117765c-55b6-11e2-877f-002564c97630")
    private boolean isFirstLane(final BpmnLane element) {
        BpmnLaneSet owner = (BpmnLaneSet) element.getCompositionOwner();
        if (owner.getParentLane() != null) {
            return false;
        }
        return true;
    }

}
