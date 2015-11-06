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
                                    

package org.modelio.diagram.editor.activity.elements.partition.bodyhybridcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.activity.elements.partition.PartitionEditPart;
import org.modelio.diagram.editor.activity.elements.partition.PartitionToolKind;
import org.modelio.diagram.editor.activity.elements.partition.bodyhybridcontainer.BodyHybridContainerEditPart.Behaviour;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.PartitionContainerLayoutEditPolicy;
import org.modelio.diagram.elements.common.freezone.DefaultFreeZoneLayoutEditPolicy;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;

/**
 * Very specific hybrid edit policy that behave either like a default free zone layout edit policy OR like a partition
 * container layout edit policy, depending on the nature of the current children of the Gm.
 * 
 * @author fpoyer
 */
@objid ("2af87b5b-55b6-11e2-877f-002564c97630")
public class BodyHybridContainerLayoutEditPolicy extends AbstractEditPolicy {
    /**
     * Current behavioural state. Defaults to {@link Behavior.UNDEFINED}
     */
    @objid ("2af87b63-55b6-11e2-877f-002564c97630")
    private Behaviour behaviour = Behaviour.HYBRID;

    /**
     * This will be used to "get" the behaviour of a free zone policy.
     */
    @objid ("d25fa929-55c0-11e2-9337-002564c97630")
    private DefaultFreeZoneLayoutEditPolicy freeZonePolicy;

    /**
     * This will be used to "get" the behaviour of a partition container policy.
     */
    @objid ("3137942f-58a2-11e2-9574-002564c97630")
    private PartitionContainerLayoutEditPolicy partitionContainerPolicy;

    /**
     * C'tor.
     */
    @objid ("2af87b65-55b6-11e2-877f-002564c97630")
    public BodyHybridContainerLayoutEditPolicy() {
        super();
        // Create an instance of both DefaultFreeZoneLayoutEditPolicy and
        // PartitionContainerLayoutEditPolicy.
        this.freeZonePolicy = new DefaultFreeZoneLayoutEditPolicy();
        this.partitionContainerPolicy = new PartitionContainerLayoutEditPolicy() {
            @Override
            protected void getResizeContainerCommand(ChangeBoundsRequest request, CompoundCommand compound) {
                // Ask that container parent is resized (not container itself,
                // as it is only meant to be a child)
                ChangeBoundsRequest resizeContainerRequest = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
                resizeContainerRequest.setEditParts(getHost().getParent());
                resizeContainerRequest.setLocation(request.getLocation());
                resizeContainerRequest.setResizeDirection(request.getResizeDirection());
                Dimension sizeDelta = request.getSizeDelta().getCopy();
                // Only ask to be resized in the "major" axis.
                if (isHorizontal())
                    sizeDelta.height = 0;
                else
                    sizeDelta.width = 0;
                resizeContainerRequest.setSizeDelta(sizeDelta);
        
                Command parentCommand = getHost().getParent().getCommand(resizeContainerRequest);
                compound.add(parentCommand);
            }
        };
    }

    @objid ("2af87b68-55b6-11e2-877f-002564c97630")
    @Override
    public void activate() {
        super.activate();
        switch (this.behaviour) {
            case HYBRID: {
                this.freeZonePolicy.activate();
                this.partitionContainerPolicy.activate();
                break;
            }
            case FREE_ZONE: {
                this.freeZonePolicy.activate();
                this.partitionContainerPolicy.deactivate();
                break;
            }
            case PARTITION_CONTAINER: {
                this.freeZonePolicy.deactivate();
                this.partitionContainerPolicy.activate();
                break;
            }
        }
    }

    @objid ("2af87b6b-55b6-11e2-877f-002564c97630")
    @Override
    public void deactivate() {
        switch (this.behaviour) {
            case HYBRID: {
                this.freeZonePolicy.deactivate();
                this.partitionContainerPolicy.deactivate();
                break;
            }
            case FREE_ZONE: {
                this.freeZonePolicy.deactivate();
                break;
            }
            case PARTITION_CONTAINER: {
                this.partitionContainerPolicy.deactivate();
                break;
            }
        }
        super.deactivate();
    }

    @objid ("2af87b6e-55b6-11e2-877f-002564c97630")
    @Override
    public void eraseSourceFeedback(Request request) {
        super.eraseSourceFeedback(request);
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    editPolicy.eraseSourceFeedback(request);
                break;
            }
            case FREE_ZONE: {
                this.freeZonePolicy.eraseSourceFeedback(request);
                break;
            }
            case PARTITION_CONTAINER: {
                this.partitionContainerPolicy.eraseSourceFeedback(request);
                break;
            }
        }
    }

    @objid ("2af87b72-55b6-11e2-877f-002564c97630")
    @Override
    public void eraseTargetFeedback(Request request) {
        super.eraseTargetFeedback(request);
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    editPolicy.eraseTargetFeedback(request);
                break;
            }
            case FREE_ZONE: {
                this.freeZonePolicy.eraseTargetFeedback(request);
                break;
            }
            case PARTITION_CONTAINER: {
                this.partitionContainerPolicy.eraseTargetFeedback(request);
                break;
            }
        }
    }

    @objid ("2af87b76-55b6-11e2-877f-002564c97630")
    @Override
    public Command getCommand(Request request) {
        Command command = null;
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    command = editPolicy.getCommand(request);
                break;
            }
            case FREE_ZONE: {
                command = this.freeZonePolicy.getCommand(request);
                break;
            }
            case PARTITION_CONTAINER: {
                command = this.partitionContainerPolicy.getCommand(request);
                break;
            }
        }
        return command;
    }

    @objid ("2afa01dd-55b6-11e2-877f-002564c97630")
    @Override
    public EditPart getTargetEditPart(Request request) {
        EditPart targetEditPart = null;
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    targetEditPart = editPolicy.getTargetEditPart(request);
                break;
            }
            case FREE_ZONE: {
                targetEditPart = this.freeZonePolicy.getTargetEditPart(request);
                break;
            }
            case PARTITION_CONTAINER: {
                targetEditPart = this.partitionContainerPolicy.getTargetEditPart(request);
                break;
            }
        }
        return targetEditPart;
    }

    @objid ("2afa01e3-55b6-11e2-877f-002564c97630")
    @Override
    public void setHost(EditPart editpart) {
        super.setHost(editpart);
        this.freeZonePolicy.setHost(editpart);
        this.partitionContainerPolicy.setHost(editpart);
    }

    @objid ("2afa01e7-55b6-11e2-877f-002564c97630")
    @Override
    public void showSourceFeedback(Request request) {
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    editPolicy.showSourceFeedback(request);
                break;
            }
            case FREE_ZONE: {
                this.freeZonePolicy.showSourceFeedback(request);
                break;
            }
            case PARTITION_CONTAINER: {
                this.partitionContainerPolicy.showSourceFeedback(request);
                break;
            }
        }
    }

    @objid ("2afa01eb-55b6-11e2-877f-002564c97630")
    @Override
    public void showTargetFeedback(Request request) {
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    editPolicy.showTargetFeedback(request);
                break;
            }
            case FREE_ZONE: {
                this.freeZonePolicy.showTargetFeedback(request);
                break;
            }
            case PARTITION_CONTAINER: {
                this.partitionContainerPolicy.showTargetFeedback(request);
                break;
            }
        }
    }

    @objid ("2afa01ef-55b6-11e2-877f-002564c97630")
    @Override
    public boolean understandsRequest(Request request) {
        switch (this.behaviour) {
            case HYBRID: {
                LayoutEditPolicy editPolicy = getPolicyInvolvedWith(request);
                if (editPolicy != null)
                    return editPolicy.understandsRequest(request);
                // else
                return false;
            }
            case FREE_ZONE: {
                return this.freeZonePolicy.understandsRequest(request);
            }
            case PARTITION_CONTAINER: {
                return this.partitionContainerPolicy.understandsRequest(request);
            }
        }
        return false;
    }

    /**
     * Sets the behaviour to adopt.
     * @param value the new behaviour.
     */
    @objid ("2afa01f5-55b6-11e2-877f-002564c97630")
    public void setBehaviour(Behaviour value) {
        // If state actually changes, update he behaviour.
        if (!this.behaviour.equals(value)) {
            Behaviour previousBehaviour = this.behaviour;
            this.behaviour = value;
            switch (this.behaviour) {
                case HYBRID: {
                    if (previousBehaviour.equals(Behaviour.PARTITION_CONTAINER))
                        this.freeZonePolicy.activate();
                    if (previousBehaviour.equals(Behaviour.FREE_ZONE))
                        this.partitionContainerPolicy.activate();
                    break;
                }
                case FREE_ZONE: {
                    if (previousBehaviour.equals(Behaviour.PARTITION_CONTAINER)) {
                        this.partitionContainerPolicy.deactivate();
                        this.freeZonePolicy.activate();
                    }
                    if (previousBehaviour.equals(Behaviour.HYBRID)) {
                        this.partitionContainerPolicy.deactivate();
                    }
                    break;
                }
                case PARTITION_CONTAINER: {
                    if (previousBehaviour.equals(Behaviour.FREE_ZONE)) {
                        this.freeZonePolicy.deactivate();
                        this.partitionContainerPolicy.activate();
                    }
                    if (previousBehaviour.equals(Behaviour.HYBRID)) {
                        this.freeZonePolicy.deactivate();
                    }
                    break;
                }
            }
        }
    }

    @objid ("2afa01f9-55b6-11e2-877f-002564c97630")
    private LayoutEditPolicy getPolicyInvolvedWith(Request request) {
        if (RequestConstants.REQ_CREATE.equals(request.getType())) {
            return getPolicyInvolvedWith((CreateRequest) request);
        } else if (RequestConstants.REQ_ADD.equals(request.getType()) ||
                   RequestConstants.REQ_RESIZE_CHILDREN.equals(request.getType()) ||
                   RequestConstants.REQ_MOVE_CHILDREN.equals(request.getType()) ||
                   RequestConstants.REQ_RESIZE.equals(request.getType()) ||
                   RequestConstants.REQ_MOVE.equals(request.getType()) ||
                   RequestConstants.REQ_CLONE.equals(request.getType())) {
            return getPolicyInvolvedWith((ChangeBoundsRequest) request);
        }
        return null;
    }

    /**
     * <p>
     * This Request analysis method is used when in hybrid mode, to determine which policy should handle a specific
     * request.
     * </p>
     * <p>
     * Basically, if the request concerns the creation of sub partitions, then the partition container policy should
     * handle it. Otherwise, the free zone policy is concerned.
     * </p>
     * @param request @return
     */
    @objid ("2afa01fe-55b6-11e2-877f-002564c97630")
    private LayoutEditPolicy getPolicyInvolvedWith(CreateRequest request) {
        final ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
        if (ctx.getMetaclass().equals(Metamodel.getMClass(ActivityPartition.class).getName())) {
            // Anything concerning IActiviPartition may only be handled by
            // partitionContainer policy.
            // Get the specific property "kind" from the tool, to know exactly
            // what is requested: a partition container, a sibling partition, or
            // an inner partition.
            PartitionToolKind kind = PartitionToolKind.valueOf((String) ctx.getProperties().get("kind"));
            switch (kind) {
                case INNER: {
                    // Inner partition we can handle
                    return this.partitionContainerPolicy;
                }
                case SIBLING:
                case HORIZONTAL_CONTAINER:
                case VERTICAL_CONTAINER: {
                    // Sibling partitions we should not handle, cause if we
                    // where to handle it, that would mean that this partition
                    // would already have sub partitions, and then we wouldn't
                    // end in this method, since we would already be in
                    // partition
                    // container mode.
                    // Partition container creation is only processed by the
                    // diagram background.
                    return null;
                }
            }
        } else {
            return this.freeZonePolicy;
        }
        return null;
    }

    /**
     * <p>
     * This Request analysis method is used when in hybrid mode, to determine which policy should handle a specific
     * request.
     * </p>
     * <p>
     * Basically, if the request concerns the addition/cloning of sub partitions, then the partition container policy
     * should handle it. Otherwise, the free zone policy is concerned.
     * </p>
     * @param request @return
     */
    @objid ("2afa0204-55b6-11e2-877f-002564c97630")
    private LayoutEditPolicy getPolicyInvolvedWith(ChangeBoundsRequest request) {
        // Involved Edit Parts must be of a consistant type (either all
        // partitions or none)
        boolean allPartitions = (request.getEditParts().get(0) instanceof PartitionEditPart);
        for (Object editPartObj : request.getEditParts()) {
            // Check for inconsistency
            if (allPartitions && !(editPartObj instanceof PartitionEditPart))
                return null;
            else if (!allPartitions && (editPartObj instanceof PartitionEditPart))
                return null;
        }
        // Everything of the same type, return corresponding policy.
        return allPartitions ? this.partitionContainerPolicy : this.freeZonePolicy;
    }

    /**
     * Returns the current behaviour.
     * @return the current behaviour.
     */
    @objid ("2afa020a-55b6-11e2-877f-002564c97630")
    public Behaviour getBehaviour() {
        return this.behaviour;
    }

}
