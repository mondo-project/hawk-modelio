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
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.metamodel.bpmn.objects.BpmnDataInput;
import org.modelio.metamodel.bpmn.objects.BpmnDataOutput;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3190
 * 
 * Severity : error
 * 
 * Description : A DataAssociation cannot target a DataInput or have a DataOutput as its source.
 */
@objid ("1f5db2fe-fb3b-462d-ba0a-e627e61dd51e")
public class R3190 extends AbstractRule {
    @objid ("cb3d1abe-6255-4786-85a9-3627383669b8")
    private static final String RULEID = "R3190";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("0bce628c-18cd-4102-bda4-4f5452566a29")
    private CheckR3190 checkerInstance = null;

    @objid ("94314eb6-783f-47d9-9a68-2cb628741f19")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnDataAssociation.class).getName(), this, AuditTrigger.CREATE |
                                                                    AuditTrigger.MOVE |
                                                                    AuditTrigger.UPDATE);
        //        plan.registerRule(Metamodel.getMClass(BpmnDataInput.class).getName(), this, AuditTrigger.UPDATE);
        //        plan.registerRule(Metamodel.getMClass(BpmnDataOutput.class).getName(), this, AuditTrigger.UPDATE);
    }

    @objid ("2eccecdd-e6ea-4fbb-b674-d318524eff43")
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
    @objid ("87342803-ebe7-4e91-a79b-a9a2d292c96b")
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
    @objid ("5e7b25b1-f198-433d-a965-a2fd45461a0f")
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
    @objid ("0a10479c-e2ee-4b52-a5f9-edc4d6ddb8af")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3190
     */
    @objid ("2cded801-f355-4981-9107-5be010db6030")
    public R3190() {
        this.checkerInstance = new CheckR3190(this);
    }

    /**
     * Actual checker for R3190: Checks that an ActivityParameterNode doesn't have both incoming and outgoing edges at
     * the same time.
     */
    @objid ("ea3f47da-97c6-4b31-b290-6da5a930518c")
    public static class CheckR3190 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("c8c254a1-1d46-4397-8cd3-c86c995259c3")
        public CheckR3190(final IRule rule) {
            super(rule);
        }

        @objid ("b20f948d-777e-4d27-8bfe-b612945a0a0a")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof BpmnDataAssociation)
                diagnostic.addEntry(checkR3190((BpmnDataAssociation) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3190: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("c48d7bd5-7279-4f8a-aff0-9c9101643752")
        private IAuditEntry checkR3190(final BpmnDataAssociation dataAssoc) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dataAssoc,
                                                   null);
            
            if((dataAssoc.getSourceRef().size() > 0 && dataAssoc.getSourceRef().get(0) instanceof BpmnDataOutput) || dataAssoc.getTargetRef() instanceof BpmnDataInput){
                
                // Rule failed
             
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(dataAssoc);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
