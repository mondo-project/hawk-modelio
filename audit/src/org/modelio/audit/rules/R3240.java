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
                                    

package org.modelio.audit.rules;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.core.IAuditEntry;
import org.modelio.audit.engine.core.IAuditPlan;
import org.modelio.audit.engine.core.IControl;
import org.modelio.audit.engine.core.IRule;
import org.modelio.audit.engine.impl.AbstractControl;
import org.modelio.audit.engine.impl.AbstractRule;
import org.modelio.audit.engine.impl.AuditEntry;
import org.modelio.audit.engine.impl.AuditTrigger;
import org.modelio.audit.engine.impl.IDiagnosticCollector;
import org.modelio.audit.plugin.Audit;
import org.modelio.audit.service.AuditSeverity;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.activities.BpmnAdHocSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnBusinessRuleTask;
import org.modelio.metamodel.bpmn.activities.BpmnCallActivity;
import org.modelio.metamodel.bpmn.activities.BpmnManualTask;
import org.modelio.metamodel.bpmn.activities.BpmnReceiveTask;
import org.modelio.metamodel.bpmn.activities.BpmnScriptTask;
import org.modelio.metamodel.bpmn.activities.BpmnSendTask;
import org.modelio.metamodel.bpmn.activities.BpmnServiceTask;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.activities.BpmnTask;
import org.modelio.metamodel.bpmn.activities.BpmnTransaction;
import org.modelio.metamodel.bpmn.activities.BpmnUserTask;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnProcess;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3240
 * 
 * Severity : error
 * 
 * Description : There can only be one sequence in a process or sub-process.
 */
@objid ("43b40e03-0f8b-4d52-a920-9dd5cc75ff91")
public class R3240 extends AbstractRule {
    @objid ("2be1fba8-43c9-46eb-9670-e6aa81524dff")
    private static final String RULEID = "R3240";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("30ea545c-3d49-4e3f-8a29-50d7c481d290")
    private CheckR3240 checkerInstance = null;

    @objid ("300dfa97-5be0-43c1-904b-5a9467f48eeb")
    private CheckR3240 flowDeletionCheckerInstance = null;

    @objid ("1afdb461-dc87-493a-bada-b18e34cd81a0")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnLane.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnProcess.class).getName(), this, AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Activity.Activity
        plan.registerRule(Metamodel.getMClass(BpmnCallActivity.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Activity.Task
        plan.registerRule(Metamodel.getMClass(BpmnTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnSendTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnReceiveTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnServiceTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnUserTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnManualTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnScriptTask.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnBusinessRuleTask.class).getName(), this, AuditTrigger.CREATE |
                                                                     AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Activity.SubProcess
        plan.registerRule(Metamodel.getMClass(BpmnSubProcess.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnAdHocSubProcess.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnTransaction.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Event.CatchEvent
        //Except BoundaryEvent, which are not concerned by the rule.
        //Except ImplicitThrowEvent which is not implemented by Modelio.
        plan.registerRule(Metamodel.getMClass(BpmnStartEvent.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName(), this, AuditTrigger.CREATE |
                                                                           AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Event.ThrowEvent
        plan.registerRule(Metamodel.getMClass(BpmnEndEvent.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName(), this, AuditTrigger.CREATE |
                                                                           AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Gateway
        plan.registerRule(Metamodel.getMClass(BpmnParallelGateway.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnEventBasedGateway.class).getName(), this, AuditTrigger.CREATE |
                                                                      AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnComplexGateway.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnInclusiveGateway.class).getName(), this, AuditTrigger.CREATE |
                                                                     AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnExclusiveGateway.class).getName(), this, AuditTrigger.CREATE |
                                                                     AuditTrigger.UPDATE);
        
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                          AuditTrigger.MOVE |
                          AuditTrigger.UPDATE|
                          AuditTrigger.DELETE);
    }

    @objid ("72237671-d583-4d31-b0f7-2e726633e97f")
    @Override
    public String getRuleId() {
        return RULEID;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("39a2f4c3-53ff-4b1b-b435-e867b50f705b")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("6a76a147-e901-4e78-b070-bcf6ae0cff81")
    @Override
    public IControl getMoveControl(final IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("a2c1f269-26ca-4269-9809-4797518333dd")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3240
     */
    @objid ("1839d696-5a69-4630-bbc0-62c1e58cce6e")
    public R3240() {
        this.checkerInstance = new CheckR3240(this);
        this.flowDeletionCheckerInstance = new CheckR2340Delete(this);
    }

    @objid ("c8d57d59-a417-4184-be8f-e169a0fc2f12")
    @Override
    public IControl getDeleteControl(final MObject element) {
        return this.flowDeletionCheckerInstance;
    }

    /**
     * Actual checker for R3240: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("5d697a0f-c6dc-4727-8260-08a603b89184")
    public static class CheckR3240 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("a2a85748-8b15-4144-8887-34f6747415bb")
        public CheckR3240(final IRule rule) {
            super(rule);
        }

        @objid ("2f1a2fcc-65df-487b-b5e7-0799cae8ba4c")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnProcess) {
                diagnostic.addEntry(checkR3240((BpmnProcess) element));
            } else if (element instanceof BpmnLane)
                diagnostic.addEntry(checkR3240(getTopLane((BpmnLane) element)));
            else if (element instanceof BpmnSubProcess)
                diagnostic.addEntry(checkR3240((BpmnSubProcess) element));
            else if (element instanceof BpmnFlowElement) {
                BpmnFlowElement flowElement = null;
                if (element instanceof BpmnSequenceFlow){
                    BpmnSequenceFlow sequenceFlow = (BpmnSequenceFlow) element;
                    flowElement = sequenceFlow.getSourceRef();
                }else{
                    flowElement = (BpmnFlowElement) element;
                }
              
            
                BpmnSubProcess subProcess = flowElement.getSubProcess();
                List<BpmnLane> lanes = flowElement.getLane();
            
                // Case the FlowElement is in a non triggeredByEvent SubProcess
                if (subProcess != null)
                    diagnostic.addEntry(checkR3240(subProcess));
            
                // Case the FlowElement is in a Lane
                else if (lanes.size() > 0)
                    diagnostic.addEntry(checkR3240(getTopLane(lanes.get(0))));
                
                // Case the FlowElement is directly under the Process
                else
                    diagnostic.addEntry(checkR3240(flowElement.getContainer()));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3240: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("18e48eb6-81f3-4d3b-8440-e4575a562581")
        private IAuditEntry checkR3240(final BpmnProcess process) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   process,
                                                   null);
            
            List<BpmnFlowNode> foundNodes = new ArrayList<>();
            for(BpmnFlowNode flowNode : process.getFlowElement(BpmnFlowNode.class))
                if(flowNode.getLane().size() == 0)
                    foundNodes.add(flowNode);
            
            BpmnStartEvent start = null;
            
            // Fetching the first StartEvent we find.
            for (BpmnFlowNode node : foundNodes)
                if (node instanceof BpmnStartEvent) {
                    start = (BpmnStartEvent) node;
                    break;
                }
            
            if (start != null) {
                // Fetching all nodes of the sequence
                List<BpmnFlowNode> sequenceNodes = new ArrayList<>();
                fetchAllNodesFromStart(start, sequenceNodes);
            
                // Checking if all the nodes we found are part of the sequence.
                for (BpmnFlowNode node : foundNodes)
                    if (!sequenceNodes.contains(node)) {
            
                        // Rule failed
            
                        auditEntry.setSeverity(this.rule.getSeverity());
                        ArrayList<Object> linkedObjects = new ArrayList<>();
                        linkedObjects.add(process.getMClass().getName());
                        linkedObjects.add(process);
                        linkedObjects.add(node);
                        auditEntry.setLinkedInfos(linkedObjects);
                    }
            }
            return auditEntry;
        }

        @objid ("5f7915f3-3055-454d-8bae-52d3d70d09a2")
        private IAuditEntry checkR3240(final BpmnSubProcess subProcess) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   subProcess,
                                                   null);
            
            // Rule does not apply on this type of subProcess
            if(subProcess.isTriggeredByEvent())
                return auditEntry;
            
            List<BpmnFlowNode> foundNodes = subProcess.getFlowElement(BpmnFlowNode.class);
            BpmnStartEvent start = null;
            
            // Fetching the first StartEvent we find.
            for (BpmnFlowNode node : foundNodes)
                if (node instanceof BpmnStartEvent) {
                    start = (BpmnStartEvent) node;
                    break;
                }
            
            if (start != null) {
            
                // Fetching all nodes of the sequence
                List<BpmnFlowNode> sequenceNodes = new ArrayList<>();
                fetchAllNodesFromStart(start, sequenceNodes);
            
                // Checking if all the nodes we found are part of the sequence.
                for (BpmnFlowNode node : foundNodes)
                    if (!sequenceNodes.contains(node)) {
            
                        // Rule failed
            
                        auditEntry.setSeverity(this.rule.getSeverity());
                        ArrayList<Object> linkedObjects = new ArrayList<>();
                        linkedObjects.add(subProcess.getMClass().getName());
                        linkedObjects.add(subProcess);
                        linkedObjects.add(node);
                        auditEntry.setLinkedInfos(linkedObjects);
                    }
            }
            return auditEntry;
        }

        @objid ("f7138ff2-402a-4ff9-91c9-0941ed47c157")
        protected IAuditEntry checkR3240(final BpmnLane topLane) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   topLane,
                                                   null);
            
            List<BpmnFlowNode> foundNodes = new ArrayList<>();
            fetchAllNodesOfTopLane(topLane, foundNodes);
            
            BpmnStartEvent start = null;
            
            // Fetching the first StartEvent we find.
            for (BpmnFlowNode node : foundNodes)
                if (node instanceof BpmnStartEvent) {
                    start = (BpmnStartEvent) node;
                    break;
                }
            
            if (start != null) {
            
                // Fetching all nodes of the sequence
                List<BpmnFlowNode> sequenceNodes = new ArrayList<>();
                fetchAllNodesFromStart(start, sequenceNodes);
            
                // Checking if all the nodes we found are part of the sequence.
                for (BpmnFlowNode node : foundNodes)
                    if (!sequenceNodes.contains(node)) {
            
                        // Rule failed
            
                        auditEntry.setSeverity(this.rule.getSeverity());
                        ArrayList<Object> linkedObjects = new ArrayList<>();
                        linkedObjects.add(topLane.getMClass().getName());
                        linkedObjects.add(topLane);
                        linkedObjects.add(node);
                        auditEntry.setLinkedInfos(linkedObjects);
                    }
            }
            return auditEntry;
        }

        /**
         * This method fetches all node of a sequence, starting from a StartEvent.
         * @param flowNode The current node.
         * @param foundNodes The nodes of the sequence.
         */
        @objid ("f9157925-5f59-401d-a0a3-6ff3cbbf8f34")
        private void fetchAllNodesFromStart(final BpmnFlowNode flowNode, final List<BpmnFlowNode> foundNodes) {
            if (!foundNodes.contains(flowNode)) {
                foundNodes.add(flowNode);
            
                // Fetching downstream
                for (BpmnSequenceFlow flow : flowNode.getOutgoing())
                    fetchAllNodesFromStart(flow.getTargetRef(), foundNodes);
            
                // Fetching upstream
                for (BpmnSequenceFlow flow : flowNode.getIncoming())
                    fetchAllNodesFromStart(flow.getSourceRef(), foundNodes);
            }
        }

        /**
         * This method fetches all the nodes contains in sub-lanes and sub-processes, starting from a top-lane.
         * @param lane The current lane.
         * @param foundNodes The nodes contains in this top-lane and its sub-lanes and sub-processes.
         */
        @objid ("a3f6c9be-4c2c-429d-b8c6-418aade42ed1")
        private void fetchAllNodesOfTopLane(final BpmnLane lane, final List<BpmnFlowNode> foundNodes) {
            // Fetching all nodes of the lane
            for (BpmnFlowNode flowNode : lane.getFlowElementRef(BpmnFlowNode.class))
                foundNodes.add(flowNode);
            
            // Fetching all nodes of sub-lanes
            BpmnLaneSet laneSet = lane.getChildLaneSet();
            if (laneSet != null) {
                for (BpmnLane subLane : laneSet.getLane())
                    fetchAllNodesOfTopLane(subLane, foundNodes);
            }
        }

        /**
         * Return the top-lane, starting from a lane.
         * @param lane The current lane.
         * @return The top-lane.
         */
        @objid ("16c5b492-85c3-4d05-b174-213a0495e1fb")
        protected BpmnLane getTopLane(final BpmnLane lane) {
            BpmnLane parentLane = lane.getLaneSet().getParentLane();
            
            if (parentLane != null)
                return getTopLane(parentLane);
            else
                return lane;
        }

    }

    @objid ("46f57d8d-316b-4883-b2f0-2324eb43306b")
    public static class CheckR2340Delete extends CheckR3240 {
        @objid ("78094561-ebc7-4018-ad2a-da7ffd6edcc3")
        public CheckR2340Delete(final IRule rule) {
            super(rule);
        }

        @objid ("496b4831-6132-4a32-933c-d3ca27d02c83")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnProcess) {
            
                // A FlowElement was potentially deleted in a lane, so we check the rule on all the lanes of the Process.
                for (BpmnLaneSet laneSet : ((BpmnProcess) element).getLaneSet())
                    for (BpmnLane lane : laneSet.getLane())
                        diagnostic.addEntry(checkR3240(getTopLane(lane)));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3240: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

    }

}
