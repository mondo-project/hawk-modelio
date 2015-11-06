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
                                    

package org.modelio.diagram.editor.sequence.elements.message;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.diagram.editor.sequence.elements.modelmanipulation.SequenceModelManipulationServices;
import org.modelio.diagram.editor.sequence.plugin.DiagramEditorSequence;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.CreateBendedConnectionRequest;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.GmPath;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionOccurenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.Gate;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionUse;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageEnd;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageKind;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageSort;

/**
 * Helper class used to create and unmask Messages in a sequence diagram. As source and target, currently handles:
 * <ul>
 * <li>Execution</li>
 * <li>Gate</li>
 * <li>InteractionUse</li>
 * <li>Lifeline</li>
 * </ul>
 * 
 * @author fpoyer
 */
@objid ("d9515afd-55b6-11e2-877f-002564c97630")
public class CreateMessageHelper {
    @objid ("d9515b02-55b6-11e2-877f-002564c97630")
    private static final int DEFAULT_EXECUTION_DURATION = 20;

    @objid ("504b928a-55c2-11e2-9337-002564c97630")
    private CreateBendedConnectionRequest request;

    @objid ("22811fc0-385c-4aba-a0a6-54352ae29ea7")
    private IModelFactory modelFactory;

    /**
     * C'tor.
     * @param modelFactory model factory that will be used to create all the necessary model org.modelio.diagram.editor.sequence.elements.
     */
    @objid ("d9515b07-55b6-11e2-877f-002564c97630")
    public CreateMessageHelper(IModelFactory modelFactory) {
        this.modelFactory = modelFactory;
    }

    /**
     * Main method. Handles the model creation and the unmaskings.
     * @param sourceModel the model that is the source of the link creation interaction
     * @param sourceTime the "time" of the source event
     * @param targetModel the model that is the target of the link creation interaction
     * @param targetTime the "time" of the target event
     * @param type the exact type of requested creation
     */
    @objid ("d9515b0d-55b6-11e2-877f-002564c97630")
    public void createMessage(IGmLinkable sourceModel, int sourceTime, IGmLinkable targetModel, int targetTime, MessageType type) {
        // Manage source
        SourceStructure source = null;
        if (sourceModel.getRelatedElement() instanceof Lifeline) {
            source = startOnLifeline((GmCompositeNode) sourceModel, sourceTime);
        } else if (sourceModel.getRelatedElement() instanceof ExecutionSpecification) {
            source = startOnEx((GmNodeModel) sourceModel, sourceTime);
        } else if (sourceModel.getRelatedElement() instanceof MessageEnd) {
            source = startOnMessageEnd((GmNodeModel) sourceModel);
        } else if (sourceModel.getRelatedElement() instanceof InteractionUse) {
            source = startOnInteractionUse((GmCompositeNode) sourceModel, sourceTime);
        } else {
            // Case not covered yet!
            throw new IllegalArgumentException("Message creation case not covered from:" + sourceModel.getRelatedElement().getMClass().getName());
        }
        
        // Manage target
        if (targetModel.getRelatedElement() instanceof Lifeline) {
            endOnLifeline(source, (GmCompositeNode) targetModel, targetTime, type);
        } else if (targetModel.getRelatedElement() instanceof ExecutionSpecification) {
            endOnEx(source, (GmCompositeNode) targetModel, targetTime, type);
        } else if (targetModel.getRelatedElement() instanceof InteractionUse) {
            endOnInteractionUse(source, (GmCompositeNode) targetModel, targetTime, type);
        } else if (targetModel.getRelatedElement() instanceof MessageEnd) {
            endOnMessageEnd(source, targetModel, type);
        } else {
            // Case not covered yet!
            throw new IllegalArgumentException("Message creation case not covered from:" +
                    sourceModel.getRelatedElement().getMClass().getName() +
                    " to " +
                    targetModel.getRelatedElement().getMClass().getName());
        
        }
    }

    @objid ("d9515b19-55b6-11e2-877f-002564c97630")
    private void endOnEx(final SourceStructure source, final GmCompositeNode targetModel, final int targetTime, final MessageType type) {
        GmCompositeNode targetLifelineBody = targetModel.getParentNode();
        ExecutionSpecification targetExecution = (ExecutionSpecification) targetModel.getRelatedElement();
        boolean createInnerExecution = (type == MessageType.InnerExecutionAsynchronous) ||
                (type == MessageType.InnerExecutionSynchronous) ||
                (type == MessageType.InnerExecutionToSelf);
        
        Interaction interaction = SequenceModelManipulationServices.getInteraction(targetExecution);
        
        // Create the objects, binding them together
        // The message is created from meA to meB where meA and meB are
        // MessageEnd instances
        // create meB the target MessageEnd
        ExecutionOccurenceSpecification meB = this.modelFactory.createExecutionOccurenceSpecification();
        meB.setEnclosingInteraction(interaction);
        Lifeline lifeline = targetExecution.getCovered().get(0);
        meB.getCovered().add(lifeline);
        
        // If InnerExecution was requested, create it
        ExecutionOccurenceSpecification exB = null;
        ExecutionSpecification innerExecution = null;
        if (createInnerExecution) {
            exB = this.modelFactory.createExecutionOccurenceSpecification();
            exB.setEnclosingInteraction(interaction);
            exB.getCovered().add(lifeline);
            innerExecution = this.modelFactory.createExecutionSpecification();
            innerExecution.setEnclosingInteraction(interaction);
            innerExecution.getCovered().add(lifeline);
            innerExecution.setStart(meB);
            innerExecution.setFinish(exB);
        }
        
        // create the message itself
        Message theMessage = this.modelFactory.createMessage();
        source.inOb.setSentMessage(theMessage);
        meB.setReceivedMessage(theMessage);
        setMessageSortAndKind(type, theMessage);
        
        // Unmask objects
        final GmAbstractDiagram gmDiagram = targetModel.getDiagram();
        if (createInnerExecution) {
            gmDiagram.unmask(targetLifelineBody, innerExecution, new Rectangle(0, 0, -1, -1));
            gmDiagram.unmask(targetLifelineBody, exB, new Rectangle(0, 0, -1, -1));
        }
        GmNodeModel targetExecutionOccurrenceSpecificationModel = gmDiagram.unmask(targetLifelineBody,
                meB,
                new Rectangle(0, 0, -1, -1));
        GmLink unmaskedLink = gmDiagram.unmaskLink(theMessage,
                source.inGm,
                targetExecutionOccurrenceSpecificationModel,
                new GmPath());
        if (this.request != null && unmaskedLink != null) {
            this.request.getCreatedObjectsToSelect().add(unmaskedLink);
        }
        
        // Set timing data
        meB.setLineNumber(targetTime);
        if (createInnerExecution && innerExecution != null && exB != null) {
            innerExecution.setLineNumber(targetTime);
            exB.setLineNumber(targetTime + DEFAULT_EXECUTION_DURATION);
        }
        
        if (source.inOb instanceof ExecutionOccurenceSpecification &&
                (type == MessageType.InnerExecutionSynchronous) &&
                exB != null) {
            // synchronous message starting on an Execution start: automagically create the reply message.
            ExecutionOccurenceSpecification replyEnd;
            if (((ExecutionOccurenceSpecification) source.inOb).getStarted() != null) {
                replyEnd = ((ExecutionOccurenceSpecification) source.inOb).getStarted().getFinish();
            } else {
                replyEnd = this.modelFactory.createExecutionOccurenceSpecification();
                replyEnd.getCovered().add(((ExecutionOccurenceSpecification) source.inOb).getCovered().get(0));
                replyEnd.setEnclosingInteraction(interaction);
            }
        
            Message theReplyMessage = this.modelFactory.createMessage();
            exB.setSentMessage(theReplyMessage);
            replyEnd.setReceivedMessage(theReplyMessage);
            setMessageSortAndKind(MessageType.Reply, theReplyMessage);
        
            replyEnd.setLineNumber(targetTime + DEFAULT_EXECUTION_DURATION);
        }
    }

    @objid ("d952e17d-55b6-11e2-877f-002564c97630")
    private void endOnMessageEnd(final SourceStructure source, final IGmLinkable targetModel, final MessageType type) {
        MessageEnd messageEnd = (MessageEnd) targetModel.getRelatedElement();
        // Create the objects, binding them together
        // create the message itself
        Message theMessage = this.modelFactory.createMessage();
        source.inOb.setSentMessage(theMessage);
        messageEnd.setReceivedMessage(theMessage);
        setMessageSortAndKind(type, theMessage);
        
        // Unmask objects
        final GmAbstractDiagram gmDiagram = targetModel.getDiagram();
        GmLink unmaskedLink = gmDiagram.unmaskLink(theMessage, source.inGm, targetModel, new GmPath());
        if (this.request != null && unmaskedLink != null) {
            this.request.getCreatedObjectsToSelect().add(unmaskedLink);
        }
    }

    @objid ("d952e187-55b6-11e2-877f-002564c97630")
    private void endOnLifeline(final SourceStructure source, final GmCompositeNode targetModel, final int targetTime, final MessageType type) {
        Lifeline targetLifeline = (Lifeline) targetModel.getRelatedElement();
        Interaction interaction = SequenceModelManipulationServices.getInteraction(targetLifeline);
        
        // targetExecutionStart and targetExecutionEnd are the start and end of
        // the Execution on the target lifeline.
        // targetExecutionStart is also the end of the Message on the target
        // lifeline.
        ExecutionOccurenceSpecification targetExecutionStart = this.modelFactory.createExecutionOccurenceSpecification();
        targetExecutionStart.getCovered().add(targetLifeline);
        targetExecutionStart.setEnclosingInteraction(interaction);
        Message theMessage = this.modelFactory.createMessage();
        source.inOb.setSentMessage(theMessage);
        targetExecutionStart.setReceivedMessage(theMessage);
        setMessageSortAndKind(type, theMessage);
        
        // Unmask all this stuff
        final GmAbstractDiagram gmDiagram = targetModel.getDiagram();
        GmNodeModel targetNode = gmDiagram.unmask(targetModel, targetExecutionStart, new Rectangle(0,
                0,
                -1,
                -1));
        GmLink unmaskedLink = gmDiagram.unmaskLink(theMessage, source.inGm, targetNode, new GmPath());
        if (this.request != null && unmaskedLink != null) {
            this.request.getCreatedObjectsToSelect().add(unmaskedLink);
        }
        targetExecutionStart.setLineNumber(targetTime);
        if (type == MessageType.Creation) {
            theMessage.setName(DiagramEditorSequence.I18N.getString("Labels.Message.Create"));
        } else if (type == MessageType.Destruction) {
            theMessage.setName(DiagramEditorSequence. I18N.getString("Labels.Message.Destroy"));
        } else {
            ExecutionOccurenceSpecification targetExecutionEnd = this.modelFactory.createExecutionOccurenceSpecification();
            targetExecutionEnd.getCovered().add(targetLifeline);
            targetExecutionEnd.setEnclosingInteraction(interaction);
        
            ExecutionSpecification targetExecution = this.modelFactory.createExecutionSpecification();
            targetExecution.getCovered().add(targetLifeline);
            targetExecution.setEnclosingInteraction(interaction);
            targetExecution.setStart(targetExecutionStart);
            targetExecution.setFinish(targetExecutionEnd);
        
            gmDiagram.unmask(targetModel, targetExecution, new Rectangle(0, 0, -1, -1));
            gmDiagram.unmask(targetModel, targetExecutionEnd, new Rectangle(0, 0, -1, -1));
            targetExecution.setLineNumber(targetTime);
            targetExecutionEnd.setLineNumber(targetTime + DEFAULT_EXECUTION_DURATION);
        
            if (source.inOb instanceof ExecutionOccurenceSpecification &&
                    (type == MessageType.InnerExecutionSynchronous || type == MessageType.SimpleSynchronous)) {
                // synchronous message starting on an Execution start: automagically create the reply message.
                ExecutionOccurenceSpecification replyEnd;
                if (source.inOb instanceof ExecutionOccurenceSpecification &&
                        ((ExecutionOccurenceSpecification) source.inOb).getStarted() != null) {
                    replyEnd = ((ExecutionOccurenceSpecification) source.inOb).getStarted().getFinish();
                } else {
                    replyEnd = this.modelFactory.createExecutionOccurenceSpecification();
                    replyEnd.getCovered().add(((ExecutionOccurenceSpecification) source.inOb).getCovered().get(0));
                    replyEnd.setEnclosingInteraction(interaction);
                }
        
                Message theReplyMessage = this.modelFactory.createMessage();
                targetExecutionEnd.setSentMessage(theReplyMessage);
                replyEnd.setReceivedMessage(theReplyMessage);
                setMessageSortAndKind(MessageType.Reply, theReplyMessage);
        
                replyEnd.setLineNumber(targetTime + 2 * DEFAULT_EXECUTION_DURATION);
            }
        }
    }

    @objid ("d952e193-55b6-11e2-877f-002564c97630")
    private void setMessageSortAndKind(MessageType type, Message theMessage) {
        MessageKind messageKind;
        switch (type) {
        case Found:
            messageKind = MessageKind.FOUNDKIND;
            break;
        case Lost:
            messageKind = MessageKind.LOSTKIND;
            break;
        default:
            messageKind = MessageKind.COMPLETEKIND;
            break;
        }
        theMessage.setKindOfMessage(messageKind);
        MessageSort messageSort;
        switch (type) {
        case Lost:
        case Found:
        case SimpleAsynchronous:
        case InnerExecutionAsynchronous:
            messageSort = MessageSort.ASYNCCALL;
            break;
        case Reply:
            messageSort = MessageSort.RETURNMESSAGE;
            break;
        case Creation:
            messageSort = MessageSort.CREATEMESSAGE;
            break;
        case Destruction:
            messageSort = MessageSort.DESTROYMESSAGE;
            break;
        case SimpleSynchronous:
        case InnerExecutionSynchronous:
        case ToSelf:
        case InnerExecutionToSelf:
        default:
            messageSort = MessageSort.SYNCCALL;
            break;
        
        }
        theMessage.setSortOfMessage(messageSort);
    }

    @objid ("d952e199-55b6-11e2-877f-002564c97630")
    private SourceStructure startOnEx(final GmNodeModel sourceModel, final int sourceTime) {
        ExecutionSpecification sourceExecution = (ExecutionSpecification) sourceModel.getRelatedElement();
        Interaction interaction = SequenceModelManipulationServices.getInteraction(sourceExecution);
        
        // Create the objects, binding them together
        // The message is created from meA to meB where meA and meB are
        // MessageEnd instances
        // create meA the origin MessageEnd
        ExecutionOccurenceSpecification meA = this.modelFactory.createExecutionOccurenceSpecification();
        meA.setEnclosingInteraction(interaction);
        meA.getCovered().add(sourceExecution.getCovered().get(0));
        
        // Unmask it
        final GmAbstractDiagram gmDiagram = sourceModel.getDiagram();
        GmCompositeNode sourceLifelineBody = sourceModel.getParentNode();
        GmNodeModel sourceMessageEndModel = gmDiagram.unmask(sourceLifelineBody, meA, new Rectangle(0,
                0,
                -1,
                -1));
        
        // Set timing data
        meA.setLineNumber(sourceTime);
        return new SourceStructure(meA, sourceMessageEndModel);
    }

    @objid ("d952e1a3-55b6-11e2-877f-002564c97630")
    private SourceStructure startOnMessageEnd(final GmNodeModel sourceModel) {
        MessageEnd messageEnd = (MessageEnd) sourceModel.getRelatedElement();
        return new SourceStructure(messageEnd, sourceModel);
    }

    @objid ("d952e1ab-55b6-11e2-877f-002564c97630")
    private SourceStructure startOnLifeline(final GmCompositeNode sourceModel, final int sourceTime) {
        Lifeline sourceLifeline = (Lifeline) sourceModel.getRelatedElement();
        Interaction interaction = SequenceModelManipulationServices.getInteraction(sourceLifeline);
        
        // messageStart is the start of the message 
        ExecutionOccurenceSpecification messageStart = this.modelFactory.createExecutionOccurenceSpecification();
        messageStart.getCovered().add(sourceLifeline);
        messageStart.setEnclosingInteraction(interaction);
        
        // Unmask all this stuff
        final GmAbstractDiagram gmDiagram = sourceModel.getDiagram();
        GmNodeModel sourceNode = gmDiagram.unmask(sourceModel, messageStart, new Rectangle(0, 0, -1, -1));
        messageStart.setLineNumber(sourceTime);
        return new SourceStructure(messageStart, sourceNode);
    }

    @objid ("d952e1b5-55b6-11e2-877f-002564c97630")
    public void setRequest(CreateBendedConnectionRequest request) {
        this.request = request;
    }

    @objid ("f0bce288-e7f3-456f-ab29-0a0f75393194")
    private void endOnInteractionUse(final SourceStructure source, final GmCompositeNode targetModel, final int targetTime, final MessageType type) {
        InteractionUse targetInteractionUse = (InteractionUse) targetModel.getRelatedElement();
        Interaction interaction = SequenceModelManipulationServices.getInteraction(targetInteractionUse);
        
        // create gate
        Gate targetGate = this.modelFactory.createGate();
        targetGate.setOwnerUse(targetInteractionUse);
        targetGate.setEnclosingInteraction(interaction);
        targetGate.setLineNumber(targetTime);
        
        // create the message itself
        Message theMessage = this.modelFactory.createMessage();
        source.inOb.setSentMessage(theMessage);
        targetGate.setReceivedMessage(theMessage);
        setMessageSortAndKind(type, theMessage);
        
        // Unmask objects
        final GmAbstractDiagram gmDiagram = targetModel.getDiagram();
        GmLink unmaskedLink = gmDiagram.unmaskLink(theMessage, source.inGm, targetModel, new GmPath());
        if (this.request != null && unmaskedLink != null) {
            this.request.getCreatedObjectsToSelect().add(unmaskedLink);
        }
    }

    @objid ("b8cdb540-c718-48f2-99fb-5c2d6a678b4c")
    private SourceStructure startOnInteractionUse(final GmCompositeNode sourceModel, final int sourceTime) {
        InteractionUse sourceInteractionUse = (InteractionUse) sourceModel.getRelatedElement();
        Interaction interaction = SequenceModelManipulationServices.getInteraction(sourceInteractionUse);
        
        // create gate
        Gate sourceGate = this.modelFactory.createGate();
        sourceGate.setOwnerUse(sourceInteractionUse);
        sourceGate.setEnclosingInteraction(interaction);
        sourceGate.setLineNumber(sourceTime);
        return new SourceStructure(sourceGate, sourceModel);
    }

    /**
     * Very simple structure that is the pair of an IExecutionOccurrenceSpecification (that is the source event of the
     * message to create in the Ob model) and a GmNodeModel (that is the source of the GmMessage to create in the Gm
     * model).<br>
     * This structure is used to pass the minimal required information from one half of the command (source) to the
     * other half (target).
     * 
     * @author fpoyer
     */
    @objid ("d952e1ba-55b6-11e2-877f-002564c97630")
    private class SourceStructure {
        @objid ("5498f5a4-016e-431d-b89b-c95bb817bdb8")
        public IGmLinkable inGm;

        @objid ("86d96181-ca61-405a-90de-7562db4e0ecb")
        public MessageEnd inOb;

        @objid ("d954681e-55b6-11e2-877f-002564c97630")
        SourceStructure(final MessageEnd inOb, final IGmLinkable inGm) {
            this.inOb = inOb;
            this.inGm = inGm;
        }

    }

}
