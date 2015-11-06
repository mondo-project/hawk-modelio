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
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3320
 * 
 * Severity : error
 * 
 * Description : A SequenceFlow outgoing from an EventBasedGateway must target an IntermediaryCatchEvent.
 */
@objid ("bbcadfb4-62a8-43c1-9d7a-9ef8037ab794")
public class R3220 extends AbstractRule {
    @objid ("691641cd-6100-4978-936f-573290aeaa8f")
    private static final String RULEID = "R3220";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("e10c170b-8375-4f62-8e05-e8e41152f638")
    private CheckR3220 checkerInstance = null;

    @objid ("35217cb9-d2b8-4a02-ac8b-ed359505ca39")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
    }

    @objid ("605affb6-cf1d-4a37-aebf-3b3ac6ee2ec1")
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
    @objid ("1c083d73-84ca-43b4-9fb9-fd26d9789bab")
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
    @objid ("df42339e-52e8-4abc-a2b7-114230a2417e")
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
    @objid ("42095680-804d-48cb-870a-d5bd6a078c07")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3220
     */
    @objid ("e4c3bd56-4d42-4f16-bc87-978ca2c32ad2")
    public R3220() {
        this.checkerInstance = new CheckR3220(this);
    }

    /**
     * Actual checker for R3220: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("71ab8a37-7cf4-4702-a6e9-f663e1d8379e")
    public static class CheckR3220 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("e9b44ea7-0454-4708-ac6d-8c5db149d769")
        public CheckR3220(final IRule rule) {
            super(rule);
        }

        @objid ("4dca1677-60bb-4eb1-83fc-7f4a15876334")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow)
                diagnostic.addEntry(checkR3220((BpmnSequenceFlow) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3220: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("b033ecdc-1ed3-42a5-9e75-6a72627b62e1")
        private IAuditEntry checkR3220(final BpmnSequenceFlow seqFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   seqFlow,
                                                   null);
            
            if (seqFlow.getSourceRef() instanceof BpmnEventBasedGateway &&
                !(seqFlow.getTargetRef() instanceof BpmnIntermediateCatchEvent)) {
            
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
