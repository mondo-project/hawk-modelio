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
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3100
 * 
 * Severity : error
 * 
 * Description : A SequcneFlow in a SubProcess must have its origin and target in the same SubProcess.
 */
@objid ("9d8b6d37-fd81-4f02-b112-ad165076944a")
public class R3100 extends AbstractRule {
    @objid ("97970df3-bfdd-41c1-ab76-cdb9ee125638")
    private static final String RULEID = "R3100";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("d19c31e6-cb96-400e-9492-4889dfa12d7e")
    private CheckR3100 checkerInstance = null;

    @objid ("66a35d0d-827b-46ac-afaf-367accaf71ac")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
        
        //BpmnFlowNode.Activity.Activity
        plan.registerRule(Metamodel.getMClass(BpmnCallActivity.class).getName(), this, AuditTrigger.MOVE);
        
        //BpmnFlowNode.Activity.Task
        plan.registerRule(Metamodel.getMClass(BpmnTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnSendTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnReceiveTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnServiceTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnUserTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnManualTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnScriptTask.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnBusinessRuleTask.class).getName(), this, AuditTrigger.MOVE);
        
        //BpmnFlowNode.Activity.SubProcess
        plan.registerRule(Metamodel.getMClass(BpmnSubProcess.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnAdHocSubProcess.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnTransaction.class).getName(), this, AuditTrigger.MOVE);
        
        //BpmnFlowNode.Event.CatchEvent
        //Except BoundaryEvent, which are not concerned by the rule.
        //Except ImplicitThrowEvent which is not implemented by Modelio.
        plan.registerRule(Metamodel.getMClass(BpmnStartEvent.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnIntermediateCatchEvent.class).getName(), this, AuditTrigger.MOVE);
        
        //BpmnFlowNode.Event.ThrowEvent
        plan.registerRule(Metamodel.getMClass(BpmnEndEvent.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnIntermediateThrowEvent.class).getName(), this, AuditTrigger.MOVE);
        
        //BpmnFlowNode.Gateway
        plan.registerRule(Metamodel.getMClass(BpmnParallelGateway.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnEventBasedGateway.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnComplexGateway.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnInclusiveGateway.class).getName(), this, AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BpmnExclusiveGateway.class).getName(), this, AuditTrigger.MOVE);
    }

    @objid ("ebef8eb7-1210-475d-bceb-b1bfe244b0b4")
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
    @objid ("7f1f58d3-33f5-4ba4-b611-8f32092636ce")
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
    @objid ("122c653e-ad5f-4ae1-ae1c-0a949f079a64")
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
    @objid ("c6f64de7-c402-4539-8868-a5772a436520")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3100
     */
    @objid ("d795ca9d-5b39-49e6-a2df-16be89273aa4")
    public R3100() {
        this.checkerInstance = new CheckR3100(this);
    }

    /**
     * Actual checker for R3100: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("cda5fff3-896e-41f7-9f90-02ed093d551b")
    public static class CheckR3100 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("a926cf7b-fa35-49a8-a0d2-eeea02159caf")
        public CheckR3100(final IRule rule) {
            super(rule);
        }

        @objid ("a379bfca-aebc-4728-a1ac-a306c373e0c7")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow)
                diagnostic.addEntry(checkR3100((BpmnSequenceFlow) element));
            else if (element instanceof BpmnFlowNode) {
                for (BpmnSequenceFlow incomingFlow : ((BpmnFlowNode) element).getIncoming())
                    diagnostic.addEntry(checkR3100(incomingFlow));
                for (BpmnSequenceFlow outgoingFlow : ((BpmnFlowNode) element).getOutgoing())
                    diagnostic.addEntry(checkR3100(outgoingFlow));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3100: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("33b84dae-4522-4b88-a4b5-21c98079f465")
        private IAuditEntry checkR3100(final BpmnSequenceFlow seqFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   seqFlow,
                                                   null);
            
            BpmnFlowNode sourceRef = seqFlow.getSourceRef();
            BpmnFlowNode targetRef = seqFlow.getTargetRef();
            if (sourceRef == null || targetRef == null) {
                return auditEntry;
            }
            
            BpmnSubProcess sourceSubPorcess = sourceRef.getSubProcess();
            BpmnSubProcess targetSubPorcess = targetRef.getSubProcess();
            
            if (sourceSubPorcess != null &&
                targetSubPorcess != null &&
                !sourceSubPorcess.equals(targetSubPorcess)) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(seqFlow);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
