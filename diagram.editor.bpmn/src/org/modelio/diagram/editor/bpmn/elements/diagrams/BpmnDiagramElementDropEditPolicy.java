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
                                    

package org.modelio.diagram.editor.bpmn.elements.diagrams;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.modelio.diagram.editor.bpmn.elements.bpmnmessage.UnmaskBpmnMessageCommand;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramEditPart;
import org.modelio.diagram.elements.common.abstractdiagram.DiagramElementDropEditPolicy;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnParticipant;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnGroup;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
 * (eg: for pins).
 */
@objid ("61ed15ba-55b6-11e2-877f-002564c97630")
public class BpmnDiagramElementDropEditPolicy extends DiagramElementDropEditPolicy {
    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("61ed15be-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPart getDropTargetEditPart(ModelElementDropRequest request) {
        for (MObject toUnmask : request.getDroppedElements()) {
            if (toUnmask instanceof BpmnParticipant || toUnmask instanceof BpmnGroup) {
                return null;
            }
        
            if (toUnmask instanceof BpmnFlowElement ||
                    toUnmask instanceof BpmnLane ||
                    toUnmask instanceof BpmnProcess ||
                    toUnmask instanceof Behavior ||
                    toUnmask instanceof Operation ||
                    toUnmask instanceof BpmnDataAssociation ||
                    toUnmask instanceof BpmnMessageFlow ||
                    toUnmask instanceof BpmnMessage ||
                    toUnmask instanceof Note ||
                    toUnmask instanceof ExternDocument ||
                    toUnmask instanceof State ||
                    toUnmask instanceof GeneralClass ||
                    toUnmask instanceof Constraint ||
                    toUnmask instanceof Dependency ||
                    toUnmask instanceof AbstractDiagram) {
                return this.getHost();
            }
        }
        
        // Add exception for elements on partitions
        return null;
    }

    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("61ed15c7-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getDropCommand(ModelElementDropRequest request) {
        CompoundCommand command = new CompoundCommand();
        
        Point dropLocation = request.getDropLocation();
        
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final AbstractDiagram diag = (AbstractDiagram) gmDiagram.getRelatedElement();
        final MObject owner = diag.getOrigin();
        
        for (MObject toUnmask : request.getDroppedElements()) {
            if (toUnmask instanceof BpmnProcess ||
                    toUnmask instanceof Behavior ||
                    toUnmask instanceof Operation &&
                    request.isSmart()) {
                command.add(getProcessDropCommand(dropLocation, toUnmask));
            } else if (toUnmask instanceof Actor && owner instanceof BpmnProcess && request.isSmart()) {
                command.add(getPoolDropCommand(dropLocation, (Actor) toUnmask));
            } else if (toUnmask instanceof GeneralClass || toUnmask instanceof State && request.isSmart()) {
                command.add(getDataObjectDropCommand(dropLocation, toUnmask));
            } else if (toUnmask instanceof BpmnMessage && request.isSmart()) {
                command.add(getMessageDropCommand(dropLocation, toUnmask));
            } else if (toUnmask != null) {
                createSubRequest(request, command, dropLocation, toUnmask);
            }
        
            // Introduce some offset, so that all elements are not totally
            // on top of each other.
            dropLocation = dropLocation.getTranslated(20, 20);
        }
        return command;
    }

    @objid ("61ee9c5f-55b6-11e2-877f-002564c97630")
    private Command getProcessDropCommand(final Point dropLocation, final MObject toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final AbstractDiagram diag = (AbstractDiagram) gmDiagram.getRelatedElement();
        final MObject owner = diag.getOrigin();
        return new SmartCallActivityCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("61ee9c69-55b6-11e2-877f-002564c97630")
    private Command getMessageDropCommand(final Point dropLocation, final MObject toUnmask) {
        return new UnmaskBpmnMessageCommand((BpmnMessage) toUnmask,
                (AbstractDiagramEditPart) getHost(),
                new Rectangle(dropLocation, new Dimension(-1, -1)),
                dropLocation);
    }

    @objid ("61ee9c73-55b6-11e2-877f-002564c97630")
    private Command getDataObjectDropCommand(final Point dropLocation, final MObject toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final AbstractDiagram diag = (AbstractDiagram) gmDiagram.getRelatedElement();
        final MObject owner = diag.getOrigin();
        return new SmartDataObjectCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("61ee9c7d-55b6-11e2-877f-002564c97630")
    private Command getPoolDropCommand(final Point dropLocation, final Actor toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final AbstractDiagram diag = (AbstractDiagram) gmDiagram.getRelatedElement();
        final BpmnProcess owner = (BpmnProcess) diag.getOrigin();
        return new SmartPoolCommand(dropLocation, toUnmask, getHost(), owner);
    }

    @objid ("61ee9c87-55b6-11e2-877f-002564c97630")
    protected static MObject getComposition(final MObject element) {
        if (element instanceof BpmnBoundaryEvent) {
            BpmnBoundaryEvent event = (BpmnBoundaryEvent) element;
            return event.getAttachedToRef();
        }
        
        if (element instanceof BpmnFlowElement) {
            BpmnFlowElement flowelement = (BpmnFlowElement) element;
            if (flowelement.getLane().size() > 0) {
                return flowelement.getLane().get(0);
            }
        }
        if (element instanceof BpmnLane) {
            BpmnLane lane = (BpmnLane) element;
            BpmnLaneSet laneset = lane.getLaneSet();
            return laneset.getCompositionOwner();
        }
        return element.getCompositionOwner();
    }

    @objid ("61ee9c92-55b6-11e2-877f-002564c97630")
    private void createSubRequest(final ModelElementDropRequest request, final CompoundCommand command, final Point dropLocation, final MObject toUnmask) {
        ModelElementDropRequest subReq = new ModelElementDropRequest();
        subReq.setDroppedElements(new MObject[] { toUnmask });
        subReq.setExtendedData(request.getExtendedData());
        subReq.setLocation(dropLocation);
        subReq.isSmart(request.isSmart());
        command.add(super.getDropCommand(subReq));
    }

}
