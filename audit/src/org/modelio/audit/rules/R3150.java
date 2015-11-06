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
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.rootElements.BpmnBaseElement;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3150
 * 
 * Severity : error
 * 
 * Description : A MessageFlow cannot link two elements in the same top-level lane.
 */
@objid ("4c3edd03-c7eb-4e4b-be24-b7a4d6910062")
public class R3150 extends AbstractRule {
    @objid ("9d464752-6fe3-45dc-9834-a9c8f880d120")
    private static final String RULEID = "R3150";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("ec460460-d82f-4d4b-9360-6a136a3b1a6c")
    private CheckR3150 checkerInstance = null;

    @objid ("6221dce6-b9be-4c7a-80d2-46889cb31769")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnMessageFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                AuditTrigger.MOVE |
                                                                AuditTrigger.UPDATE);
    }

    @objid ("026ee44c-4751-46ff-8498-61634fe1acea")
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
    @objid ("852d1361-1821-48c4-bc6b-39e93a2eaf6c")
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
    @objid ("f44b671e-a69e-4831-af52-d33e3bcaa177")
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
    @objid ("14045d25-b013-44e2-bcd7-87b3c0ba19eb")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3150
     */
    @objid ("47bb035c-7d24-4c56-aa6c-3801a30da305")
    public R3150() {
        this.checkerInstance = new CheckR3150(this);
    }

    /**
     * Actual checker for R3150: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("0525b4fc-b48e-49b4-9b8c-19daa7b911af")
    public static class CheckR3150 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("df190b24-75bf-4cf9-bb15-fd800719f805")
        public CheckR3150(final IRule rule) {
            super(rule);
        }

        @objid ("c628706e-4951-4dea-be15-ee42fd27a04d")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnMessageFlow)
                diagnostic.addEntry(checkR3150((BpmnMessageFlow) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3150: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("0bde1f38-1ebd-4c2e-967c-50522d150366")
        private IAuditEntry checkR3150(final BpmnMessageFlow messageFlow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   messageFlow,
                                                   null);
            
            final BpmnBaseElement sourceRef = messageFlow.getSourceRef();
            final BpmnBaseElement targetRef = messageFlow.getTargetRef();
            
            if (sourceRef==null || targetRef==null)
                return auditEntry;
            
            final List<BpmnLane> sourceLanes = sourceRef.getBpmnLaneRefs();
            final List<BpmnLane> targetLanes = targetRef.getBpmnLaneRefs();
            
            if (sourceLanes.size() > 0 && targetLanes.size() > 0) {
            
                BpmnLane topSourceLane = null;
                BpmnLane topTargetLane = null;
            
                topSourceLane = getTopLane(sourceLanes.get(0));
                topTargetLane = getTopLane(targetLanes.get(0));
            
                if (topSourceLane.equals(topTargetLane)) {
            
                    // Rule failed
            
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(messageFlow);
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

        /**
         * Return the top-lane, starting from a lane.
         * @param lane The current lane.
         * @return The top-lane.
         */
        @objid ("3f4e6e23-1813-4db6-b82f-4ad9bd573a6d")
        private BpmnLane getTopLane(final BpmnLane lane) {
            BpmnLane parentLane = lane.getLaneSet().getParentLane();
            
            if (parentLane != null)
                return getTopLane(parentLane);
            else
                return lane;
        }

    }

}
