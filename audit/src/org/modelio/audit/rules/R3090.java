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
import org.modelio.metamodel.bpmn.flows.BpmnSequenceFlow;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowNode;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3090
 * 
 * Severity : error
 * 
 * Description : A SequenceFlow cannot have its source or target in different Lane, except for sub-lanes.
 */
@objid ("187ccec3-225d-43fd-87f4-d4ce55a98d86")
public class R3090 extends AbstractRule {
    @objid ("a8cfc3dc-b4be-4b5a-86dd-92644eb66121")
    private static final String RULEID = "R3090";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("4afadb68-7325-4641-99fb-d87dcdf331dd")
    private CheckR3090 checkerInstance = null;

    @objid ("036ca4f9-6fac-4781-b378-34c9e1468569")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnSequenceFlow.class).getName(), this, AuditTrigger.CREATE |
                                                                 AuditTrigger.MOVE |
                                                                 AuditTrigger.UPDATE);
        
        // When a FlowElement is moved, we only get an update on the old and new Lanes
        // We also watch for Lane moves
        plan.registerRule(Metamodel.getMClass(BpmnLane.class).getName(), this, AuditTrigger.UPDATE | AuditTrigger.MOVE);
    }

    @objid ("43dc862d-3590-4420-a54a-5da4c84f89e8")
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
    @objid ("fe5f4e16-c6b5-423e-a8a7-f55bab46aff6")
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
    @objid ("fe13dd16-7841-4cce-9308-f5de27001ea3")
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
    @objid ("99535eff-5784-4387-b466-86c336ea09e3")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3090
     */
    @objid ("3aba8ab5-cac7-48ae-bc30-f352fe75fd30")
    public R3090() {
        this.checkerInstance = new CheckR3090(this);
    }

    /**
     * Actual checker for R3090: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("0feb3936-68e1-4ee2-868f-ca775bde2914")
    public static class CheckR3090 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("e307be6c-e30c-4c2c-b1b9-4cb7e5d9b739")
        public CheckR3090(final IRule rule) {
            super(rule);
        }

        @objid ("a372be33-8c54-4417-9f9b-33fb6c28c481")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnSequenceFlow)
                diagnostic.addEntry(checkR3090((BpmnSequenceFlow) element));
            else if (element instanceof BpmnLane)
                for(BpmnFlowNode flowNode : ((BpmnLane)element).getFlowElementRef(BpmnFlowNode.class)){
                    for(BpmnSequenceFlow incomingFlow : flowNode.getIncoming())
                        diagnostic.addEntry(checkR3090(incomingFlow));
                    for(BpmnSequenceFlow outgoingFlow : flowNode.getOutgoing())
                        diagnostic.addEntry(checkR3090(outgoingFlow));
                }
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3090: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("4f8ed408-892d-4933-9ae2-f6f16bae0a55")
        private IAuditEntry checkR3090(final BpmnSequenceFlow flow) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   flow,
                                                   null);
            
            BpmnFlowNode sourceRef = flow.getSourceRef();
            BpmnFlowNode targetRef = flow.getTargetRef();
            if (sourceRef == null || targetRef == null) {
                return auditEntry;
            }
            
            List<BpmnLane> sourceLanes = sourceRef.getLane();
            List<BpmnLane> targetLanes = targetRef.getLane();
            
            if (sourceLanes.size() > 0 && targetLanes.size() > 0) {
                BpmnLane sourceTopLane = getTopLane(sourceLanes.get(0));
                BpmnLane targetTopLane = getTopLane(targetLanes.get(0));
            
                if (sourceTopLane != null && !sourceTopLane.equals(targetTopLane)) {
            
                    // Rule failed
            
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(flow);
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
        @objid ("c510302c-8a04-4a31-8412-6a3e168620ed")
        private BpmnLane getTopLane(final BpmnLane lane) {
            BpmnLane parentLane = lane.getLaneSet().getParentLane();
            
            if (parentLane != null)
                return getTopLane(parentLane);
            else
                return lane;
        }

    }

}
