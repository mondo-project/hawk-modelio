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
import org.modelio.metamodel.bpmn.activities.BpmnActivity;
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
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnImplicitThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3080
 * 
 * Severity: error
 * 
 * Description : All FlowNodes should be part of a sequence starting with a StartEvent and finishing with an EndEvent.
 */
@objid ("9bd6b507-46f1-4275-bb80-e256f952f63b")
public class R3080 extends AbstractRule {
    @objid ("5356c1d0-cee3-41af-8f92-e26bcc7f7bb3")
    private static final String RULEID = "R3080";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("013a0e9f-6fc2-415a-89d7-bba4de64c483")
    private CheckR3080 checkerInstance = null;

    @objid ("2fae90a6-0b34-4ed4-8409-7352bcdfa09b")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                          AuditTrigger.MOVE |
                          AuditTrigger.UPDATE);
        
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
    }

    @objid ("148d9ef9-c7ce-43c6-af78-fabaf3eacda6")
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
    @objid ("18fca21c-35b8-4834-83c5-1754e35a21db")
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
    @objid ("b298d0eb-dba1-491a-96c8-9e4740d0d2ea")
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
    @objid ("67f537af-11ee-416c-85d0-f1f028b53e63")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3080
     */
    @objid ("92fd17ea-e814-42ba-b7a6-652b3903da92")
    public R3080() {
        this.checkerInstance = new CheckR3080(this);
    }

    /**
     * Actual checker for R3080: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("37c390b0-dff1-4580-a771-78fb54b10dfc")
    public static class CheckR3080 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("494108e9-3008-43ac-add6-1f11aa5abffb")
        public CheckR3080(final IRule rule) {
            super(rule);
        }

        @objid ("7757f4f7-dbee-41e6-9daf-48478a17959e")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnFlowNode)
                diagnostic.addEntry(checkR3080((BpmnFlowNode) element));
            else if (element instanceof BpmnSequenceFlow) {
                BpmnFlowNode sourceRef = ((BpmnSequenceFlow) element).getSourceRef();
                if (sourceRef != null) {
                    diagnostic.addEntry(checkR3080(sourceRef));
                }
            
                BpmnFlowNode targetRef = ((BpmnSequenceFlow) element).getTargetRef();
                if (targetRef != null) {
                    diagnostic.addEntry(checkR3080(targetRef));
                }
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3080: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("5e38353a-cc22-4d20-bcd8-bcd73b16979f")
        private IAuditEntry checkR3080(final BpmnFlowNode flowNode) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   flowNode,
                                                   null);
            
            if (flowNode instanceof BpmnBoundaryEvent || flowNode instanceof BpmnImplicitThrowEvent)
                return auditEntry;
            
            int incoming = flowNode.getIncoming().size();
            int outgoing = flowNode.getOutgoing().size();
            
            if ((flowNode instanceof BpmnActivity && ((BpmnActivity) flowNode).getBoundaryEventRef().size() > 0) ||
                    (flowNode instanceof BpmnStartEvent && outgoing > 0) ||
                    (flowNode instanceof BpmnEndEvent && incoming > 0) ||
                    (incoming > 0 && outgoing > 0))
                return auditEntry;
            
            // At this point the rule failed
            
            auditEntry.setSeverity(this.rule.getSeverity());
            ArrayList<Object> linkedObjects = new ArrayList<>();
            linkedObjects.add(flowNode);
            auditEntry.setLinkedInfos(linkedObjects);
            return auditEntry;
        }

    }

}
