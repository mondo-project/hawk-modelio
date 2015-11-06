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
                                    

package org.modelio.diagram.editor.activity.elements.activitydiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.editor.activity.elements.commands.CreateActivityParameterNodeCommand;
import org.modelio.diagram.editor.activity.elements.commands.CreateCallBehaviorCommand;
import org.modelio.diagram.editor.activity.elements.commands.CreateCallOperationCommand;
import org.modelio.diagram.editor.activity.elements.partition.PartitionToolKind;
import org.modelio.diagram.elements.common.abstractdiagram.DiagramElementDropEditPolicy;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
 * (eg: for pins).
 * 
 * @author fpoyer
 */
@objid ("298d5295-55b6-11e2-877f-002564c97630")
public class ActivityDiagramElementDropEditPolicy extends DiagramElementDropEditPolicy {
    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("298ed91b-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPart getDropTargetEditPart(ModelElementDropRequest request) {
        final GmActivityDiagram gmDiagram = (GmActivityDiagram) this.getHost().getModel();
        
        // If either of the dropped elements cannot be unmasked, return null.
        for (MObject droppedElement : request.getDroppedElements()) {
            if (!gmDiagram.canUnmask(droppedElement)) {
                // Gm doesn't know how to handle this element directly, look if
                // it is an element for which we can do something "smarter".
                if (!(droppedElement instanceof Operation && request.isSmart()) &&
                    !(droppedElement instanceof Behavior && request.isSmart()) &&
                    !(droppedElement instanceof BehaviorParameter && request.isSmart()) &&
                    !isSmartObjectNodeTarget(droppedElement, request)) {
                    // It is not a smart interaction
                    //
                    // The Gm cannot unmask this element directly, and we don't know
                    // what to do with it... return null
                    return null;
                }
        
            }
        }
        
        // All dropped elements understood: return host!
        return this.getHost();
    }

    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("298ed924-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getDropCommand(ModelElementDropRequest request) {
        CompoundCommand command = new CompoundCommand();
        
        Point dropLocation = request.getDropLocation();
        
        for (MObject toUnmask : request.getDroppedElements()) {
        
            if (toUnmask instanceof ActivityPartition) {
                command.add(getPartitionDropCommand(dropLocation, (ActivityPartition) toUnmask));
            } else if (toUnmask instanceof Operation && request.isSmart()) {
                command.add(getOperationDropCommand(dropLocation, (Operation) toUnmask));
            } else if (toUnmask instanceof Behavior && request.isSmart()) {
                command.add(getBehaviorDropCommand(dropLocation, (Behavior) toUnmask));
            } else if (toUnmask instanceof BehaviorParameter) {
                command.add(getBehaviorParameterDropCommand(dropLocation, (BehaviorParameter) toUnmask));
            } else if (isSmartObjectNodeTarget(toUnmask, request)) {
                command.add(getSmartObjectNodeDropCommand(dropLocation, toUnmask));
            } else if (toUnmask != null) {
                createSubRequest(request, command, dropLocation, toUnmask);
            }
        
            // Introduce some offset, so that all elements are not totally
            // on top of each other.
            dropLocation = dropLocation.getTranslated(20, 20);
        }
        return command;
    }

    /**
     * Returns the command to unmask a partition.
     * @param toUnmask
     * the partition to unmask
     * @param dropLocation the dropLocation in absolute coordinates.
     */
    @objid ("298ed92d-55b6-11e2-877f-002564c97630")
    private Command getPartitionDropCommand(Point dropLocation, ActivityPartition partitionToUnmask) {
        // If we ended here, that means that at the time of the drop, no super
        // partition has been unmasked yet: we need to create a partition
        // container on diagram background first, then unmask the whole hierarchy.
        // return new UnmaskPartitionCommand((GmCompositeNode)
        // getHost().getModel(), (ActivityPartition) partitionToUnmask,null));
        final CreateRequest creationRequest = new CreateRequest();
        creationRequest.setLocation(dropLocation);
        creationRequest.setSize(new Dimension(-1, -1));
        
        final ModelioCreationContext gmCreationContext = new ModelioCreationContext(partitionToUnmask);
        if (((ActivityDiagram) ((GmActivityDiagram) getHost().getModel()).getRelatedElement()).isIsVertical()) {
            gmCreationContext.setProperty("kind", PartitionToolKind.VERTICAL_CONTAINER.toString());
        } else {
            gmCreationContext.setProperty("kind", PartitionToolKind.HORIZONTAL_CONTAINER.toString());
        }
        
        creationRequest.setFactory(gmCreationContext);
        return getHost().getCommand(creationRequest);
    }

    @objid ("298ed936-55b6-11e2-877f-002564c97630")
    private Command getBehaviorParameterDropCommand(Point dropLocation, BehaviorParameter toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final ActivityDiagram diag = (ActivityDiagram) gmDiagram.getRelatedElement();
        final Activity owner = (Activity) diag.getOrigin();
        return new CreateActivityParameterNodeCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("298ed93e-55b6-11e2-877f-002564c97630")
    private Command getOperationDropCommand(Point dropLocation, Operation toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final ActivityDiagram diag = (ActivityDiagram) gmDiagram.getRelatedElement();
        final Activity owner = (Activity) diag.getOrigin();
        return new CreateCallOperationCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("298ed946-55b6-11e2-877f-002564c97630")
    private Command getBehaviorDropCommand(Point dropLocation, Behavior toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final ActivityDiagram diag = (ActivityDiagram) gmDiagram.getRelatedElement();
        final Activity owner = (Activity) diag.getOrigin();
        return new CreateCallBehaviorCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("298ed94e-55b6-11e2-877f-002564c97630")
    private Command getSmartObjectNodeDropCommand(final Point dropLocation, final MObject toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final ActivityDiagram diag = (ActivityDiagram) gmDiagram.getRelatedElement();
        final Activity owner = (Activity) diag.getOrigin();
        return new SmartObjectNodeCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("298ed958-55b6-11e2-877f-002564c97630")
    private boolean isSmartObjectNodeTarget(final MObject element, final ModelElementDropRequest request) {
        if (!request.isSmart())
            return false;
        return (element instanceof AssociationEnd ||
                element instanceof Attribute ||
                element instanceof Instance ||
                element instanceof GeneralClass || element instanceof Parameter);
    }

    @objid ("29905fc1-55b6-11e2-877f-002564c97630")
    private void createSubRequest(final ModelElementDropRequest request, final CompoundCommand command, final Point dropLocation, final MObject toUnmask) {
        ModelElementDropRequest subReq = new ModelElementDropRequest();
        subReq.setDroppedElements(new MObject[] { toUnmask });
        subReq.setExtendedData(request.getExtendedData());
        subReq.setLocation(dropLocation);
        subReq.isSmart(request.isSmart());
        command.add(super.getDropCommand(subReq));
    }

}
