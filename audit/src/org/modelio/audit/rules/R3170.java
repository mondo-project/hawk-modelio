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
import org.modelio.metamodel.bpmn.gateways.BpmnComplexGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnEventBasedGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnInclusiveGateway;
import org.modelio.metamodel.bpmn.gateways.BpmnParallelGateway;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3170
 * 
 * Severity : warning
 * 
 * Description : An Inclusive Gateway,Complex Gateway and  Event Based Gateway must have at least two outgoing SequcneFlows.
 */
@objid ("91b99d8e-d82d-4a8a-9daa-5d9c15db8f4b")
public class R3170 extends AbstractRule {
    @objid ("5c4e911f-b9cb-421c-a6ae-5fbed676bf25")
    private static final String RULEID = "R3170";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("d7ec717b-5176-407d-9c0a-08ca5e533560")
    private CheckR3170 checkerInstance = null;

    @objid ("d838af7c-9a38-43f9-9427-f505e0a11fc4")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
         
        //BPMN.Gateway
        plan.registerRule(Metamodel.getMClass(BpmnInclusiveGateway.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnComplexGateway.class).getName(), this, AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnEventBasedGateway.class).getName(), this, AuditTrigger.UPDATE);
    }

    @objid ("2361baeb-f998-4105-9869-30563a997cd6")
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
    @objid ("fed4c760-950c-4923-8635-4d3ff789a5a5")
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
    @objid ("bb4d90c3-466d-475c-96ea-7d3f2d8502cf")
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
    @objid ("23a4fff1-faf9-42c1-8087-294b1cf78636")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3170
     */
    @objid ("b739d674-1b0f-4e1d-acaf-dccf7b197043")
    public R3170() {
        this.checkerInstance = new CheckR3170(this);
    }

    /**
     * Actual checker for R3170: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("78797811-82d2-40b5-81d8-bc9dc5e8737e")
    public static class CheckR3170 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("abff8eab-2a38-4443-936d-9c1c3111f043")
        public CheckR3170(final IRule rule) {
            super(rule);
        }

        @objid ("db03dd8d-1988-4427-97a9-7f34d79b139a")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow) {
                BpmnFlowNode flowNode = ((BpmnSequenceFlow) element).getSourceRef();
                if (flowNode instanceof BpmnInclusiveGateway || flowNode instanceof BpmnComplexGateway || flowNode instanceof BpmnEventBasedGateway)
                    diagnostic.addEntry(checkR3170((BpmnGateway) flowNode));
            } else if (element instanceof BpmnGateway && !(element instanceof BpmnParallelGateway))
                diagnostic.addEntry(checkR3170((BpmnGateway) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3170: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("3f1dcb49-3200-477e-bbb8-2a101643ab17")
        private IAuditEntry checkR3170(final BpmnGateway gateway) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   gateway,
                                                   null);
            
            if (gateway.getOutgoing().size() < 2) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(gateway);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
