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
import org.modelio.metamodel.bpmn.gateways.BpmnExclusiveGateway;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3230
 * 
 * Severity : warning
 * 
 * Dscription : All SequenceFLow outgoing from an ExclusiveGateway must have a guard, except for the default
 * SequenceFlow.
 */
@objid ("e0fc9f35-99f2-46a5-8bd7-5dabd01655b6")
public class R3230 extends AbstractRule {
    @objid ("457a7d15-6b07-4315-aafb-cfd6792f6f85")
    private static final String RULEID = "R3230";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("12c168ef-a50c-438d-9013-563e9373bb11")
    private CheckR3230 checkerInstance = null;

    @objid ("57ade3de-98c1-4964-8486-39cc5f534a62")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnExclusiveGateway.class).getName(), this, AuditTrigger.UPDATE);
    }

    @objid ("448cb4bf-be15-4025-a43e-c1be272b9a60")
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
    @objid ("8baa7586-e75c-464c-8f08-54dd026e9da3")
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
    @objid ("f8ada20e-c4d2-40cd-9acc-8227b702ecd9")
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
    @objid ("2c3f9faf-3416-4083-ad44-31adeb4c1caa")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3230
     */
    @objid ("4d1778e6-aa9b-4027-8a84-1d651d738d81")
    public R3230() {
        this.checkerInstance = new CheckR3230(this);
    }

    /**
     * Actual checker for R3230: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("3f8d8235-a165-4c97-98cf-1290ec0f2008")
    public static class CheckR3230 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("6152b68c-41cc-44fd-ba11-2b93284c0c51")
        public CheckR3230(final IRule rule) {
            super(rule);
        }

        @objid ("19b80e12-d501-4798-a84f-a01e86bc4b74")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow) {
                BpmnFlowNode flowNode = ((BpmnSequenceFlow) element).getSourceRef();
                if (flowNode instanceof BpmnExclusiveGateway)
                    diagnostic.addEntry(checkR1050((BpmnExclusiveGateway) flowNode));
            } else if (element instanceof BpmnExclusiveGateway)
                diagnostic.addEntry(checkR1050((BpmnExclusiveGateway) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3230: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("300d11ef-cde8-4e40-b128-861b36d98145")
        private IAuditEntry checkR1050(final BpmnExclusiveGateway excluGateway) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   excluGateway,
                                                   null);
            
            if(excluGateway.getOutgoing().size() > 1){
                for (BpmnSequenceFlow seqFlow : excluGateway.getOutgoing()){
                    if (seqFlow.getDefaultOfExclusive() == null){
                        if (seqFlow.getConditionExpression().isEmpty()) {
                
                            // Rule failed
                
                            auditEntry.setSeverity(this.rule.getSeverity());
                            ArrayList<Object> linkedObjects = new ArrayList<>();
                            linkedObjects.add(excluGateway);
                            auditEntry.setLinkedInfos(linkedObjects);
                        }
                    }
                }
            }
            return auditEntry;
        }

    }

}
