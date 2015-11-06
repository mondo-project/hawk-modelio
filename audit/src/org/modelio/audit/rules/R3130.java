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
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateThrowEvent;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.rootElements.BpmnBaseElement;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3130
 * 
 * Severity : error
 * 
 * Description : A MessageFlow cannot target a StartEvent or an IntermediateCatchEvent, or have an EndEvent or an
 * IntermediateCatchEvent as its source.
 */
@objid ("aaa9cc58-9c2c-4240-9719-7e9e977475ea")
public class R3130 extends AbstractRule {
    @objid ("9641f822-df51-4b67-b816-600ac1b07295")
    private static final String RULEID = "R3130";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("fbb51671-0814-4ccb-aaa1-de16ee5111c0")
    private CheckR3130 checkerInstance = null;

    @objid ("6a560c34-e425-4672-bfdc-b6e82eb50342")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnMessageFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                AuditTrigger.MOVE |
                                                                AuditTrigger.UPDATE);
    }

    @objid ("5e795016-438e-4a7a-a7dc-f9bc3c016714")
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
    @objid ("578b0791-28f4-4316-b7dd-6b5050c4681a")
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
    @objid ("af6fcb73-b668-4027-9928-336b30428fbf")
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
    @objid ("2125dac1-c5b0-4be3-a68a-9b7235ec8780")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3130
     */
    @objid ("218e8347-39da-4912-a3cf-e136c6fd9248")
    public R3130() {
        this.checkerInstance = new CheckR3130(this);
    }

    /**
     * Actual checker for R3130: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("a5f04405-9ca3-44a5-a812-dabc0feae331")
    public static class CheckR3130 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("97078b60-3503-4994-b226-db1f2d8f574f")
        public CheckR3130(final IRule rule) {
            super(rule);
        }

        @objid ("a250f962-f20e-4a49-b38a-0f928a5d6fb3")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnMessageFlow)
                diagnostic.addEntry(checkR1050((BpmnMessageFlow) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3130: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("8824a056-25c0-453c-80c0-77dbfaef8adc")
        private IAuditEntry checkR1050(final BpmnMessageFlow messageFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   messageFlow,
                                                   null);
            
            BpmnBaseElement source = messageFlow.getSourceRef();
            BpmnBaseElement target = messageFlow.getTargetRef();
            
            if (target  instanceof BpmnEndEvent ||
                source instanceof BpmnIntermediateCatchEvent ||
                source instanceof BpmnStartEvent ||
                target instanceof BpmnIntermediateThrowEvent) {
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(messageFlow);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
