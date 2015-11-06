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
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3140
 * 
 * Severity : warning
 * 
 * Description : All outgoing SequenceFlow from an EventBasedGateway or a ParallelGateway must have its default and
 * guard properties empty.
 */
@objid ("17792212-96ba-4291-b86d-f17726953327")
public class R3140 extends AbstractRule {
    @objid ("4409f573-1533-4f21-b5e8-abb9a4a8ab4c")
    private static final String RULEID = "R3140";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("27e2931a-f020-4a5e-a2a6-261da4be5e60")
    private CheckR3140 checkerInstance = null;

    @objid ("d983848b-b301-41c1-9000-23356671ccf8")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnFlowElement.class).getName(), this, AuditTrigger.UPDATE);
    }

    @objid ("8826392b-159d-4617-9583-ba6877c723d6")
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
    @objid ("e6269299-38cf-4790-9d0c-2de3aa237479")
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
    @objid ("a601a992-ca4f-4544-be49-1e32b6c2416a")
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
    @objid ("d5fee9a6-aa68-4d7b-9e8b-5bf58ee1ee31")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3140
     */
    @objid ("f4659e9f-f4bb-466c-a8c9-7fc659f50587")
    public R3140() {
        this.checkerInstance = new CheckR3140(this);
    }

    /**
     * Actual checker for R3140: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("3dbfa142-ec25-473b-ac7a-1a4fef8fdb48")
    public static class CheckR3140 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("37e11048-64e6-40bf-818f-536adec85a34")
        public CheckR3140(final IRule rule) {
            super(rule);
        }

        @objid ("f25e4853-6960-4634-8054-a2ab975ded61")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow)
                diagnostic.addEntry(checkR1050((BpmnSequenceFlow) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3140: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("03661f21-d64b-4677-b0cf-cde2b012a92d")
        private IAuditEntry checkR1050(final BpmnSequenceFlow seqFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   seqFlow,
                                                   null);
            
            BpmnFlowNode source = seqFlow.getSourceRef();
            
            if (source instanceof BpmnEventBasedGateway || source instanceof BpmnParallelGateway)
                if (!seqFlow.getConditionExpression().isEmpty()) {
            
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
