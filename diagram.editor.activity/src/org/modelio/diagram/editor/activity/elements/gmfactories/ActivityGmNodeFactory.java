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
                                    

package org.modelio.diagram.editor.activity.elements.gmfactories;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.IParameter;
import org.modelio.diagram.editor.activity.elements.acceptsignal.GmAcceptSignal;
import org.modelio.diagram.editor.activity.elements.action.GmAction;
import org.modelio.diagram.editor.activity.elements.activitydiagram.GmActivityDiagram;
import org.modelio.diagram.editor.activity.elements.activitydiagramview.GmActivityDiagramView;
import org.modelio.diagram.editor.activity.elements.activityfinal.GmActivityFinal;
import org.modelio.diagram.editor.activity.elements.callbehavior.GmCallBehavior;
import org.modelio.diagram.editor.activity.elements.callevent.GmCallEvent;
import org.modelio.diagram.editor.activity.elements.calloperation.GmCallOperation;
import org.modelio.diagram.editor.activity.elements.centralbuffer.GmCentralBuffer;
import org.modelio.diagram.editor.activity.elements.changeevent.GmChangeEvent;
import org.modelio.diagram.editor.activity.elements.clause.GmClause;
import org.modelio.diagram.editor.activity.elements.conditional.GmConditional;
import org.modelio.diagram.editor.activity.elements.datastore.GmDataStore;
import org.modelio.diagram.editor.activity.elements.decisionmerge.GmDecisionMerge;
import org.modelio.diagram.editor.activity.elements.expansionnode.GmExpansionNode;
import org.modelio.diagram.editor.activity.elements.expansionregion.GmExpansionRegion;
import org.modelio.diagram.editor.activity.elements.flowfinal.GmFlowFinal;
import org.modelio.diagram.editor.activity.elements.forkjoin.GmForkJoin;
import org.modelio.diagram.editor.activity.elements.initial.GmInitial;
import org.modelio.diagram.editor.activity.elements.inputpin.GmInputPin;
import org.modelio.diagram.editor.activity.elements.interruptible.GmInterruptible;
import org.modelio.diagram.editor.activity.elements.loopnode.GmLoopNode;
import org.modelio.diagram.editor.activity.elements.objectnode.GmObjectNode;
import org.modelio.diagram.editor.activity.elements.outputpin.GmOutputPin;
import org.modelio.diagram.editor.activity.elements.partition.GmPartition;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.GmDiagramPartitionContainer;
import org.modelio.diagram.editor.activity.elements.partitioncontainer.GmPartitionContainer;
import org.modelio.diagram.editor.activity.elements.sendsignal.GmSendSignal;
import org.modelio.diagram.editor.activity.elements.structuredactivity.GmStructuredActivity;
import org.modelio.diagram.editor.activity.elements.timeevent.GmTimeEvent;
import org.modelio.diagram.editor.activity.elements.valuepin.GmValuePin;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.diagrams.ActivityDiagram;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptCallEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptChangeEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptTimeEventAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.CallOperationAction;
import org.modelio.metamodel.uml.behavior.activityModel.CentralBufferNode;
import org.modelio.metamodel.uml.behavior.activityModel.Clause;
import org.modelio.metamodel.uml.behavior.activityModel.ConditionalNode;
import org.modelio.metamodel.uml.behavior.activityModel.DataStoreNode;
import org.modelio.metamodel.uml.behavior.activityModel.DecisionMergeNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionRegion;
import org.modelio.metamodel.uml.behavior.activityModel.FlowFinalNode;
import org.modelio.metamodel.uml.behavior.activityModel.ForkJoinNode;
import org.modelio.metamodel.uml.behavior.activityModel.InitialNode;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.InstanceNode;
import org.modelio.metamodel.uml.behavior.activityModel.InterruptibleActivityRegion;
import org.modelio.metamodel.uml.behavior.activityModel.LoopNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.activityModel.OpaqueAction;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.metamodel.uml.behavior.activityModel.SendSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.StructuredActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ValuePin;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.visitors.DefaultModelVisitor;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * State diagram specific implementation of {@link IGmNodeFactory}.
 * <p>
 * This particular implementation:
 * <ul>
 * <li>does not support cascading</li>
 * <li>only processes state diagram specific elements</li>
 * </ul>
 */
@objid ("2a8f324a-55b6-11e2-877f-002564c97630")
public class ActivityGmNodeFactory implements IGmNodeFactory {
    @objid ("2a8f324e-55b6-11e2-877f-002564c97630")
    private static final ActivityGmNodeFactory _instance = new ActivityGmNodeFactory();

    @objid ("2a8f3250-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel create(GmAbstractDiagram diagram, GmCompositeNode parent, MObject newElement, Object initialLayoutData) {
        if (parent instanceof GmGroup) {
            // Use the group element factory visitor
            final GroupElementFactoryVisitor v = new GroupElementFactoryVisitor(diagram);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        } else {
            // Use the node factory visitor
            final NodeFactoryVisitor v = new NodeFactoryVisitor(diagram, parent, initialLayoutData);
        
            final GmNodeModel child = (GmNodeModel) newElement.accept(v);
            if (child != null)
                parent.addChild(child);
            return child;
        }
    }

    @objid ("2a8f3260-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isRepresentable(Class<? extends MObject> metaclass) {
        if (metaclass == TaggedValue.class ||
                metaclass == TagParameter.class ||
                metaclass == IParameter.class)
            return false;
        return true;
    }

    /**
     * Register an GmNode factory extension.
     * <p>
     * Extension GmNode factories are called first when looking for an GmNode.
     * @param id id for the extension factory
     * @param factory the edit part factory.
     */
    @objid ("2a90b8bf-55b6-11e2-877f-002564c97630")
    @Override
    public void registerFactory(String id, IGmNodeFactory factory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("2a90b8c6-55b6-11e2-877f-002564c97630")
    @Override
    public void unregisterFactory(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Get the singleton instance of the node factory.
     * @return The graphic node factory.
     */
    @objid ("2a90b8ca-55b6-11e2-877f-002564c97630")
    public static IGmNodeFactory getInstance() {
        return _instance;
    }

    @objid ("2a90b8d1-55b6-11e2-877f-002564c97630")
    @Override
    public Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * Constructor.
     * @param gmDiagram
     * The diagram all nodes created by this factory will be unmasked in.
     */
    @objid ("2a90b8d8-55b6-11e2-877f-002564c97630")
    private ActivityGmNodeFactory() {
        // Nothing to do.
    }

    /**
     * Factory visitor that creates standard GmNodes.
     */
    @objid ("2a90b8db-55b6-11e2-877f-002564c97630")
    private class NodeFactoryVisitor extends DefaultModelVisitor {
        @objid ("2a90b8df-55b6-11e2-877f-002564c97630")
        private Object initialLayoutData;

        @objid ("d2135dea-55c0-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("d2135deb-55c0-11e2-9337-002564c97630")
        private GmCompositeNode parent;

        @objid ("2a90b8e6-55b6-11e2-877f-002564c97630")
        public NodeFactoryVisitor(GmAbstractDiagram diagram, GmCompositeNode parent, Object initialLayoutData) {
            this.diagram = diagram;
            this.parent = parent;
            this.initialLayoutData = initialLayoutData;
        }

        @objid ("2a90b8ef-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("f90583e4-58ca-11e2-8539-00137282c51b")
        @Override
        public Object visitOpaqueAction(OpaqueAction theOpaqueAction) {
            final GmAction opaqueAction = new GmAction(this.diagram,
                    theOpaqueAction,
                    new MRef(theOpaqueAction));
            opaqueAction.setLayoutData(this.initialLayoutData);
            return opaqueAction;
        }

        @objid ("41deba36-58d0-11e2-8539-00137282c51b")
        @Override
        public Object visitCallBehaviorAction(CallBehaviorAction theCallBehaviorAction) {
            GmCallBehavior callBehavior = new GmCallBehavior(this.diagram,
                    theCallBehaviorAction,
                    new MRef(theCallBehaviorAction));
            callBehavior.setLayoutData(this.initialLayoutData);
            return callBehavior;
        }

        @objid ("41e11c94-58d0-11e2-8539-00137282c51b")
        @Override
        public Object visitCallOperationAction(CallOperationAction theCallOperationAction) {
            GmCallOperation callOperation = new GmCallOperation(this.diagram,
                    theCallOperationAction,
                    new MRef(theCallOperationAction));
            callOperation.setLayoutData(this.initialLayoutData);
            return callOperation;
        }

        @objid ("2b37005d-58d2-11e2-8539-00137282c51b")
        @Override
        public Object visitConditionalNode(ConditionalNode theConditionalNode) {
            final GmConditional conditional = new GmConditional(this.diagram,
                    theConditionalNode,
                    new MRef(theConditionalNode));
            conditional.setLayoutData(this.initialLayoutData);
            return conditional;
        }

        @objid ("00d6adcc-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitExpansionRegion(ExpansionRegion theExpansionRegion) {
            final GmExpansionRegion expansionRegion = new GmExpansionRegion(this.diagram,
                    theExpansionRegion,
                    new MRef(theExpansionRegion));
            expansionRegion.setLayoutData(this.initialLayoutData);
            return expansionRegion;
        }

        @objid ("00d6add3-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitStructuredActivityNode(StructuredActivityNode theStructuredActivityNode) {
            final GmStructuredActivity structured = new GmStructuredActivity(this.diagram,
                    theStructuredActivityNode,
                    new MRef(theStructuredActivityNode));
            structured.setLayoutData(this.initialLayoutData);
            return structured;
        }

        @objid ("00d6adda-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitInterruptibleActivityRegion(InterruptibleActivityRegion theInterruptibleActivityRegion) {
            GmInterruptible interruptibleRegion = new GmInterruptible(this.diagram,
                    theInterruptibleActivityRegion,
                    new MRef(theInterruptibleActivityRegion));
            interruptibleRegion.setLayoutData(this.initialLayoutData);
            return interruptibleRegion;
        }

        @objid ("00d6ade0-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitLoopNode(LoopNode theLoopNode) {
            final GmLoopNode loopNode = new GmLoopNode(this.diagram, theLoopNode, new MRef(theLoopNode));
            loopNode.setLayoutData(this.initialLayoutData);
            return loopNode;
        }

        @objid ("00d91027-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitFlowFinalNode(FlowFinalNode theFlowFinalNode) {
            final GmFlowFinal flowfinal = new GmFlowFinal(this.diagram,
                    theFlowFinalNode,
                    new MRef(theFlowFinalNode));
            flowfinal.setLayoutData(this.initialLayoutData);
            return flowfinal;
        }

        @objid ("00d9102e-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitForkJoinNode(ForkJoinNode theForkJoinNode) {
            final GmForkJoin forkJoin = new GmForkJoin(this.diagram,
                    theForkJoinNode,
                    new MRef(theForkJoinNode));
            forkJoin.setLayoutData(this.initialLayoutData);
            return forkJoin;
        }

        @objid ("00d91034-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitInitialNode(InitialNode theInitialNode) {
            final GmInitial timeEvent = new GmInitial(this.diagram, theInitialNode, new MRef(theInitialNode));
            timeEvent.setLayoutData(this.initialLayoutData);
            return timeEvent;
        }

        @objid ("00d9103a-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitDecisionMergeNode(DecisionMergeNode theDecisionMergeNode) {
            final GmDecisionMerge decisionMerge = new GmDecisionMerge(this.diagram,
                    theDecisionMergeNode,
                    new MRef(theDecisionMergeNode));
            decisionMerge.setLayoutData(this.initialLayoutData);
            return decisionMerge;
        }

        @objid ("00d91041-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitActivityFinalNode(ActivityFinalNode theActivityFinalNode) {
            final GmActivityFinal activityFinal = new GmActivityFinal(this.diagram,
                    theActivityFinalNode,
                    new MRef(theActivityFinalNode));
            activityFinal.setLayoutData(this.initialLayoutData);
            return activityFinal;
        }

        @objid ("2bd4dba8-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitObjectNode(ObjectNode theObjectNode) {
            final GmObjectNode instanceNode = new GmObjectNode(this.diagram,
                    theObjectNode,
                    new MRef(theObjectNode));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("2bd4dbae-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitValuePin(ValuePin theValuePin) {
            GmValuePin valuePin = new GmValuePin(this.diagram, theValuePin, new MRef(theValuePin));
            valuePin.setLayoutData(this.initialLayoutData);
            return valuePin;
        }

        @objid ("2bd4dbb4-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitExpansionNode(ExpansionNode theExpansionNode) {
            GmExpansionNode expansionNode = new GmExpansionNode(this.diagram,
                    theExpansionNode,
                    new MRef(theExpansionNode));
            expansionNode.setLayoutData(this.initialLayoutData);
            return expansionNode;
        }

        @objid ("2bd4dbba-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitSendSignalAction(SendSignalAction theSendSignalAction) {
            GmSendSignal sendSignal = new GmSendSignal(this.diagram,
                    theSendSignalAction,
                    new MRef(theSendSignalAction));
            sendSignal.setLayoutData(this.initialLayoutData);
            return sendSignal;
        }

        @objid ("2bd73e04-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitOutputPin(OutputPin theOutputPin) {
            GmOutputPin outputPin = new GmOutputPin(this.diagram, theOutputPin, new MRef(theOutputPin));
            outputPin.setLayoutData(this.initialLayoutData);
            return outputPin;
        }

        @objid ("2bd73e0a-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitInstanceNode(InstanceNode theInstanceNode) {
            GmObjectNode instanceNode = new GmObjectNode(this.diagram,
                    theInstanceNode,
                    new MRef(theInstanceNode));
            instanceNode.setLayoutData(this.initialLayoutData);
            return instanceNode;
        }

        @objid ("2bd9a05f-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitInputPin(InputPin theInputPin) {
            GmInputPin inputPin = new GmInputPin(this.diagram, theInputPin, new MRef(theInputPin));
            inputPin.setLayoutData(this.initialLayoutData);
            return inputPin;
        }

        @objid ("2bd9a065-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitClause(Clause theClause) {
            final GmClause clause = new GmClause(this.diagram, theClause, new MRef(theClause));
            clause.setLayoutData(this.initialLayoutData);
            return clause;
        }

        @objid ("2bdc02b6-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitDataStoreNode(DataStoreNode theDataStoreNode) {
            GmDataStore dataStore = new GmDataStore(this.diagram,
                    theDataStoreNode,
                    new MRef(theDataStoreNode));
            dataStore.setLayoutData(this.initialLayoutData);
            return dataStore;
        }

        @objid ("2bdc02bc-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitAcceptCallEventAction(AcceptCallEventAction theAcceptCallEventAction) {
            GmCallEvent callEvent = new GmCallEvent(this.diagram,
                    theAcceptCallEventAction,
                    new MRef(theAcceptCallEventAction));
            callEvent.setLayoutData(this.initialLayoutData);
            return callEvent;
        }

        @objid ("2bdc02c2-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitAcceptChangeEventAction(AcceptChangeEventAction theAcceptChangeEventAction) {
            final GmChangeEvent changeEvent = new GmChangeEvent(this.diagram,
                    theAcceptChangeEventAction,
                    new MRef(theAcceptChangeEventAction));
            changeEvent.setLayoutData(this.initialLayoutData);
            return changeEvent;
        }

        @objid ("2bdc02c8-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitAcceptSignalAction(AcceptSignalAction theAcceptSignalAction) {
            final GmAcceptSignal acceptSignal = new GmAcceptSignal(this.diagram,
                    theAcceptSignalAction,
                    new MRef(theAcceptSignalAction));
            acceptSignal.setLayoutData(this.initialLayoutData);
            return acceptSignal;
        }

        @objid ("2bdc02ce-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitAcceptTimeEventAction(AcceptTimeEventAction theAcceptTimeEventAction) {
            final GmTimeEvent timeEvent = new GmTimeEvent(this.diagram,
                    theAcceptTimeEventAction,
                    new MRef(theAcceptTimeEventAction));
            timeEvent.setLayoutData(this.initialLayoutData);
            return timeEvent;
        }

        @objid ("2bde6514-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitActivityPartition(ActivityPartition theActivityPartition) {
            if (this.parent instanceof GmActivityDiagram) {
                // Create the partition container
                GmDiagramPartitionContainer partitionContainer = new GmDiagramPartitionContainer(this.diagram,
                        new MRef(this.diagram.getRepresentedElement()));
                partitionContainer.setLayoutData(this.initialLayoutData);
                // Create the partition and add it to the partition container. 
                GmPartition partition = new GmPartition(this.diagram,
                        theActivityPartition,
                        new MRef(theActivityPartition));
                partitionContainer.addChild(partition);
                // Return the partition container.
                return partitionContainer;
            } else if (this.parent instanceof GmPartitionContainer) {
                GmPartition partition = new GmPartition(this.diagram,
                        theActivityPartition,
                        new MRef(theActivityPartition));
                partition.setLayoutData(this.initialLayoutData);
                return partition;
            }
            // Error case
            throw new IllegalArgumentException("Unhandled type of parent node while trying to create a Partition. Parent node must be GmaActivityDiagram or GmPartitionContainer. Given parent node is of type: " +
                    this.parent.getClass().getName());
        }

        @objid ("2bde6519-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitCentralBufferNode(CentralBufferNode theCentralBufferNode) {
            GmCentralBuffer centralBuffer = new GmCentralBuffer(this.diagram,
                    theCentralBufferNode,
                    new MRef(theCentralBufferNode));
            centralBuffer.setLayoutData(this.initialLayoutData);
            return centralBuffer;
        }

        @objid ("2be0c76a-597f-11e2-8539-00137282c51b")
        @Override
        public Object visitActivityDiagram(final ActivityDiagram el) {
            final GmNodeModel node = new GmActivityDiagramView(this.diagram, el, new MRef(el));
            node.setLayoutData(this.initialLayoutData);
            return node;
        }

    }

    /**
     * Factory visitor that creates instances to put into {@link GmGroup}.
     * 
     * @author cmarin
     */
    @objid ("2a96d350-55b6-11e2-877f-002564c97630")
    private class GroupElementFactoryVisitor extends DefaultModelVisitor {
        @objid ("d2197869-55c0-11e2-9337-002564c97630")
        private GmAbstractDiagram diagram;

        @objid ("2a96d357-55b6-11e2-877f-002564c97630")
        public GroupElementFactoryVisitor(GmAbstractDiagram diagram) {
            this.diagram = diagram;
        }

        @objid ("2a96d35c-55b6-11e2-877f-002564c97630")
        @Override
        public Object visitElement(Element theElement) {
            // We don't know what to do with that element.
            return null;
        }

        @objid ("00db7284-58db-11e2-8539-00137282c51b")
        @Override
        public Object visitClause(Clause theClause) {
            GmClause clause = new GmClause(this.diagram, theClause, new MRef(theClause));
            return clause;
        }

    }

}
