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
 * R3250
 * 
 * Severity : error
 * 
 * Description : If there is at least one element in a lane or a sub-process, there should be a StartEvent and a
 * EndEvent.
 */
@objid ("daaf99d8-5295-4755-94e6-4fe6eac3c18f")
public class R3250 extends AbstractRule {
    @objid ("330863bc-9ab6-4dac-a884-e9f994ad8465")
    private static final String RULEID = "R3250";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("c83d409b-8f20-4ead-81bb-030466d1dc93")
    private CheckR3250 checkerInstance = null;

    @objid ("20954c84-f624-445d-a8b8-0bb5b2397bd3")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnLane.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnProcess.class).getName(), this, AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Activity.Activity
        plan.registerRule(Metamodel.getMClass(BpmnCallActivity.class).getName(), this, AuditTrigger.CREATE);
        
        //BpmnFlowNode.Activity.Task
        plan.registerRule(Metamodel.getMClass(BpmnTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnSendTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnReceiveTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnServiceTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnUserTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnManualTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnScriptTask.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnBusinessRuleTask.class).getName(), this, AuditTrigger.CREATE);
        
        //BpmnFlowNode.Activity.SubProcess
        plan.registerRule(Metamodel.getMClass(BpmnSubProcess.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnAdHocSubProcess.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnTransaction.class).getName(), this, AuditTrigger.CREATE);
        
        //BpmnFlowNode.Event.CatchEvent
        //Except BoundaryEvent, which are not concerned by the rule.
        //Except ImplicitThrowEvent which is not implemented by Modelio.
        plan.registerRule(Metamodel.getMClass(BpmnStartEvent.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName(), this, AuditTrigger.CREATE);
        
        //BpmnFlowNode.Event.ThrowEvent
        plan.registerRule(Metamodel.getMClass(BpmnEndEvent.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName(), this, AuditTrigger.CREATE);
        
        //BpmnFlowNode.Gateway
        plan.registerRule(Metamodel.getMClass(BpmnParallelGateway.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnEventBasedGateway.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnComplexGateway.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnInclusiveGateway.class).getName(), this, AuditTrigger.CREATE);
        plan.registerRule(Metamodel.getMClass(BpmnExclusiveGateway.class).getName(), this, AuditTrigger.CREATE);
    }

    @objid ("83fe8140-e0c4-4147-8e57-6ce4fdb5baaf")
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
    @objid ("ba35f8b3-d2e5-41cb-8796-cb4ab12e3420")
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
    @objid ("c699f1ef-f296-4665-9f7a-40c4a38c66e7")
    @Override
    public IControl getMoveControl(final IElementMovedEvent moveEvent) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("3ecc3ed9-32b8-4583-891c-5b8d0f45092b")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3250
     */
    @objid ("e3819963-c261-4588-b901-1e5fa40bc0c3")
    public R3250() {
        this.checkerInstance = new CheckR3250(this);
    }

    /**
     * Actual checker for R3250: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("8e711418-175b-4f2b-a654-e85c36892385")
    public static class CheckR3250 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("2fefddaa-2cbc-4061-a2b4-233daa990bb4")
        public CheckR3250(final IRule rule) {
            super(rule);
        }

        @objid ("daceb01a-8c0b-4546-aa67-342ca40b4072")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnProcess) {
                diagnostic.addEntry(checkR3250((BpmnProcess) element));
            } else if (element instanceof BpmnLane)
                diagnostic.addEntry(checkR3250(getTopLane((BpmnLane) element)));
            else if (element instanceof BpmnSubProcess) {
                BpmnSubProcess subProcess = (BpmnSubProcess) element;
                diagnostic.addEntry(checkR3250(subProcess));
            } else if (element instanceof BpmnFlowElement) {
                BpmnFlowElement flowElement = (BpmnFlowElement) element;
            
                BpmnSubProcess subProcess = flowElement.getSubProcess();
                List<BpmnLane> lanes = flowElement.getBpmnLaneRefs();
            
                // Case the FlowElement is in a non triggeredByEvent SubProcess
                if (subProcess != null)
                    diagnostic.addEntry(checkR3250(subProcess));
            
                // Case the FlowElement is in a Lane
                else if (lanes.size() > 0)
                    diagnostic.addEntry(checkR3250(getTopLane(lanes.get(0))));
            
                // Case the FlowElement is directly under the Process
                else
                    diagnostic.addEntry(checkR3250(flowElement.getContainer()));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3250: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("3cd66b3a-ea7c-45a3-a6ae-c873995acbdf")
        private IAuditEntry checkR3250(final BpmnProcess process) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   process,
                                                   null);
            
            List<BpmnFlowNode> foundNodes = new ArrayList<>();
            for (BpmnFlowNode flowNode : process.getFlowElement(BpmnFlowNode.class))
                if (flowNode.getLane().size() == 0)
                    foundNodes.add(flowNode);
            
            // Process can escape to the rule if it is empty
            if (foundNodes.size() > 0) {
            
                BpmnStartEvent start = null;
                BpmnEndEvent end = null;
            
                for (BpmnFlowNode node : foundNodes)
                    if (node instanceof BpmnStartEvent)
                        start = (BpmnStartEvent) node;
                    else if (node instanceof BpmnEndEvent)
                        end = (BpmnEndEvent) node;
            
                if (start == null || end == null) {
            
                    // Rule failed
            
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(process.getMClass().getName());
                    linkedObjects.add(process);
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

        @objid ("c4dd8d72-53ca-4451-8a60-1b6bcd95dca7")
        private IAuditEntry checkR3250(final BpmnSubProcess subProcess) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   subProcess,
                                                   null);
            
            // Rule does not apply on this type of subProcess
            if (subProcess.isTriggeredByEvent())
                return auditEntry;
            
            List<BpmnFlowNode> nodes = subProcess.getFlowElement(BpmnFlowNode.class);
            
            // SubProcess can escape to the rule if it is empty
            if (nodes.size() > 0) {
            
                BpmnStartEvent start = null;
                BpmnEndEvent end = null;
            
                for (BpmnFlowNode node : nodes)
                    if (node instanceof BpmnStartEvent)
                        start = (BpmnStartEvent) node;
                    else if (node instanceof BpmnEndEvent)
                        end = (BpmnEndEvent) node;
            
                if (start == null || end == null) {
            
                    // Rule failed
            
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(subProcess.getMClass().getName());
                    linkedObjects.add(subProcess);
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

        @objid ("4ce824b5-5ed8-45c7-acc2-0ecda157f01c")
        private IAuditEntry checkR3250(final BpmnLane topLane) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   topLane,
                                                   null);
            
            List<BpmnFlowNode> foundNodes = new ArrayList<>();
            fetchAllNodesOfTopLane(topLane, foundNodes);
            
            // Lane can escape to the rule if it is empty
            if (foundNodes.size() > 0) {
            
                BpmnStartEvent start = null;
                BpmnEndEvent end = null;
            
                for (BpmnFlowNode node : foundNodes)
                    if (node instanceof BpmnStartEvent)
                        start = (BpmnStartEvent) node;
                    else if (node instanceof BpmnEndEvent)
                        end = (BpmnEndEvent) node;
            
                if (start == null || end == null) {
            
                    // Rule failed
            
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(topLane.getMClass().getName());
                    linkedObjects.add(topLane);
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

        /**
         * This method fetches all the nodes contains in sub-lanes and sub-processes, starting from a top-lane.
         * @param lane The current lane.
         * @param foundNodes The nodes contains in this top-lane and its sub-lanes and sub-processes.
         */
        @objid ("6a3babb6-8324-43f8-835d-e2d7a2fe4ccb")
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
        @objid ("45366be9-1c39-425d-930d-47b5ba0450df")
        protected BpmnLane getTopLane(final BpmnLane lane) {
            BpmnLane parentLane = lane.getLaneSet().getParentLane();
            
            if (parentLane != null)
                return getTopLane(parentLane);
            else
                return lane;
        }

    }

}
