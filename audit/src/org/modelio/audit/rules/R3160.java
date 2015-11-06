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
import org.modelio.metamodel.bpmn.gateways.BpmnGateway;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3160
 * 
 * Severity : error
 * 
 * Description : A MessageFlow cannot have a Gateway as its source or target.
 */
@objid ("852f2f4d-7b70-48ac-8c71-e788bebb068c")
public class R3160 extends AbstractRule {
    @objid ("76360485-59a5-4ff0-b786-e9468c2be723")
    private static final String RULEID = "R3160";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("61a8bbc4-aed9-464e-b62c-19de3abab3c0")
    private CheckR3160 checkerInstance = null;

    @objid ("7ce6bc78-a947-4fa4-bdf5-76a20564e647")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnMessageFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                AuditTrigger.MOVE |
                                                                AuditTrigger.UPDATE);
    }

    @objid ("36b05bbd-daa8-4601-852d-a7e9855206b7")
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
    @objid ("8e3f52e4-cde3-4a63-9c3e-87ae4dd1dda2")
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
    @objid ("8fa413e4-d8c3-4b96-b4c8-5ddb078e59ac")
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
    @objid ("98392f32-9671-4b72-97fb-852ef586c6c3")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3160
     */
    @objid ("6bcce76e-a9ca-465f-8e1f-d3c7e9e20ec0")
    public R3160() {
        this.checkerInstance = new CheckR3160(this);
    }

    /**
     * Actual checker for R3160: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("9b5281f2-702d-484e-9e8b-1e308578c32e")
    public static class CheckR3160 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("20734708-f778-43dd-bb1d-0e259d878b21")
        public CheckR3160(final IRule rule) {
            super(rule);
        }

        @objid ("3008afcd-ccde-4531-a653-92c722459aec")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnMessageFlow)
                diagnostic.addEntry(checkR1050((BpmnMessageFlow) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3160: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("bdd89f48-f712-4aa1-9656-6c1b51376b27")
        private IAuditEntry checkR1050(final BpmnMessageFlow messageFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   messageFlow,
                                                   null);
            
            if (messageFlow.getSourceRef() instanceof BpmnGateway ||
                messageFlow.getTargetRef() instanceof BpmnGateway) {
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(messageFlow);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
