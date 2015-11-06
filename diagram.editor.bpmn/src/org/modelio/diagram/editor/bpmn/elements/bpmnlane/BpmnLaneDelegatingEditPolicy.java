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
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.policies.DelegatingEditPolicy;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;

/**
 * Specialisation of the default delegating policy to handle the Lane specific tools like "create sibling partition",
 * "create inner partition" and "create partition container".
 */
@objid ("6115ef99-55b6-11e2-877f-002564c97630")
public class BpmnLaneDelegatingEditPolicy extends DelegatingEditPolicy {
    /**
     * If the request is to create a sibling partition, then delegate to the partition container currently holding the
     * host. If the request is to create an inner partition, then delegate to the partition container currently held by
     * the host. Request for creation of partition containers are ignored completely.
     */
    @objid ("6115ef9d-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(final Request request) {
        if (RequestConstants.REQ_ADD.equals(request.getType()) ||
                RequestConstants.REQ_MOVE.equals(request.getType())) {
            // If the moved edit parts are partitions, delegate to the
            // container holding the host.
            if (((ChangeBoundsRequest) request).getEditParts().get(0) instanceof BpmnLaneEditPart) {
                return getHost().getParent().getTargetEditPart(request);
            }
            // Otherwise call super (Is it a bird? Is it a plane?)
            return super.getTargetEditPart(request);
        } else if (!RequestConstants.REQ_CREATE.equals(request.getType())) {
            // Only care about specific CREATE and ADD request, super can handle
            // the rest. Since ADD was handled above, ignore everything that is
            // not CREATE.
            return super.getTargetEditPart(request);
        }
        // Only care about request for partitions, super can handle the rest.
        final ModelioCreationContext ctx = (ModelioCreationContext) ((CreateRequest) request).getNewObject();
        if (!ctx.getMetaclass().equals(Metamodel.getMClass(ActivityPartition.class).getName())) {
            return super.getTargetEditPart(request);
        }
        // Get the specific property "kind" from the tool, to know exactly what
        // is requested: a partition container, a sibling partition, or an inner
        // partition.
        String kindProperty = (String) ctx.getProperties().get("kind");
        if (kindProperty == null || kindProperty.isEmpty()) {
            // No kind defined, this means that it is probably a drop, not
            // handled by this policy.
            return null;
        }
        // else
        LaneToolKind kind = LaneToolKind.valueOf(kindProperty);
        switch (kind) {
        case SIBLING: {
            // A sibling of this partition can only be created in the
            // partition container currently holding this partition.
            // Delegate to it.
            return getHost().getParent();
        }
        case INNER: {
            // Inner partition creation request will be treated by the
            // partition container of this partition. Since we shouldn't
            // make assertions about the exact structure of the Gm and of
            // the associated edit parts, lets ask the one who knows: the
            // Gm.
            GmCompositeNode gmPartitionContainer = ((GmCompositeNode) getHost().getModel()).getCompositeFor(ActivityPartition.class);
            if (gmPartitionContainer != null) {
                // We have the model, lets find the corresponding edit part
                EditPart partitionContainerEditPart = (EditPart) getHost().getViewer()
                        .getEditPartRegistry()
                        .get(gmPartitionContainer);
                if (partitionContainerEditPart != null) {
                    return partitionContainerEditPart;
                }
            }
            // Something along the way went wrong: we don't know how to
            // handle this request.
            return null;
        }
        case HORIZONTAL_CONTAINER:
            return null;
        }
        return null;
    }

}
