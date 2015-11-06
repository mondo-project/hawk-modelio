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
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.rootElements.BpmnBaseElement;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3180
 * 
 * Severity : error
 * 
 * Description : A FlowElement cannot have a SequenceFlow or a MessageFlow towards itself.
 */
@objid ("915497a6-9e8b-4075-b489-5acb8ccd674d")
public class R3180 extends AbstractRule {
    @objid ("137983ad-a60b-492d-b1d0-f8e86f3b3093")
    private static final String RULEID = "R3180";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("677739fd-9eb5-40d4-924f-723be228e1e1")
    private CheckR3180 checkerInstance = null;

    @objid ("a3763518-e750-4fb7-be26-f14b66750166")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnMessageFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                AuditTrigger.MOVE |
                                                                AuditTrigger.UPDATE);
    }

    @objid ("fc862359-153a-4392-b268-affe91dcc27a")
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
    @objid ("ac24379c-132e-4aab-82e4-b8ab82f6fea8")
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
    @objid ("c0043a83-4f93-4ed8-bdfd-4a114fc64071")
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
    @objid ("c1c29fcd-6425-43ca-ae3d-7def59fd416e")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3180
     */
    @objid ("98cf2da5-854c-416b-9ae2-da571dac6dc5")
    public R3180() {
        this.checkerInstance = new CheckR3180(this);
    }

    /**
     * Actual checker for R3180: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("717860a3-7a9f-4f2c-a9e0-2a4b990f1a42")
    public static class CheckR3180 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("0b023d6b-375f-4a7e-8591-f47d2b48d108")
        public CheckR3180(final IRule rule) {
            super(rule);
        }

        @objid ("4242e283-ddb7-472c-a483-df12d185372a")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow)
                diagnostic.addEntry(checkR3180((BpmnSequenceFlow) element));
            else if (element instanceof BpmnMessageFlow)
                diagnostic.addEntry(checkR3180((BpmnMessageFlow) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3180: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("cfb2d8b1-a4ef-4892-9744-ae5810b3e1bd")
        private IAuditEntry checkR3180(final BpmnSequenceFlow seqFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   seqFlow,
                                                   null);
            
            final BpmnFlowNode sourceRef = seqFlow.getSourceRef();
            if (sourceRef!=null && sourceRef.equals(seqFlow.getTargetRef())) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(sourceRef.getMClass().getName());
                linkedObjects.add(sourceRef);
                linkedObjects.add(seqFlow.getMClass().getName());
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

        @objid ("09d1e805-ba2b-48fb-b6e2-3b7eec210320")
        private IAuditEntry checkR3180(final BpmnMessageFlow msgFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   msgFlow,
                                                   null);
            
            final BpmnBaseElement sourceRef = msgFlow.getSourceRef();
            if (sourceRef!= null && sourceRef.equals(msgFlow.getTargetRef())) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(sourceRef.getMClass().getName());
                linkedObjects.add(sourceRef);
                linkedObjects.add(msgFlow.getMClass().getName());
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
