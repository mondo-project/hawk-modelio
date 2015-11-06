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
import org.modelio.audit.service.AuditSeverity;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.objects.BpmnDataInput;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.bpmn.objects.BpmnDataOutput;
import org.modelio.metamodel.bpmn.objects.BpmnDataStore;
import org.modelio.metamodel.bpmn.objects.BpmnItemAwareElement;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * R3270
 * 
 * Severity : warning
 * 
 * Description : If a BpmnItemAwareElement has a type GeneralClass, then its State must also be part of that GeneralClass.
 */
@objid ("81846efd-a0ff-42ca-b7a7-873e8315f27d")
public class R3270 extends AbstractRule {
    @objid ("997594ad-36d0-4e01-bcb3-7f9c32a771ce")
    private static final String RULEID = "R3270";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("b5a2f959-0010-486d-9a1f-b6a5368cb916")
    private CheckR3270 checkerInstance = null;

    @objid ("38d80f1a-5ca8-4ef2-9ff8-d9b171ae6efb")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(BpmnDataInput.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnDataObject.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnDataOutput.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(BpmnDataStore.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
    }

    @objid ("5aaa22dd-6fbb-4b44-b41d-f9d67fbcf292")
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
    @objid ("454b159c-b1bf-4b6d-94e2-1cf29b28b5ce")
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
    @objid ("bb7f7cac-4304-41c9-9b52-f958e6a303eb")
    @Override
    public IControl getMoveControl(final IElementMovedEvent moveEvent) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("776f77f5-d72f-4e1d-8612-69726bb6b482")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3250
     */
    @objid ("c1a6ea31-bde1-4cb4-8359-582e4e60e3da")
    public R3270() {
        this.checkerInstance = new CheckR3270(this);
    }

    /**
     * Actual checker for R3270: Checks that the type and inState relationships are coherent.
     */
    @objid ("690d8ee3-ff3c-4078-8b4f-a5682808e7fc")
    public static class CheckR3270 extends AbstractControl {
        /**
         * C'tor.
         * @param rule the rule to check.
         */
        @objid ("72b87415-9b95-4b32-9f49-70c0e5e89d4d")
        public CheckR3270(final IRule rule) {
            super(rule);
        }

        @objid ("c6f4622d-d846-4a79-8206-be8f1dfd6625")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if(element instanceof BpmnItemAwareElement)
                diagnostic.addEntry(checkR3270((BpmnItemAwareElement) element));
            return diagnostic;
        }

        @objid ("ac5160a2-ce00-4634-bc9a-b230d2df2b71")
        private IAuditEntry checkR3270(final BpmnItemAwareElement element) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                    AuditSeverity.AuditSuccess,
                    element,
                    null);
            
            final GeneralClass type = element.getType();
            final State state = element.getInState();
            if (type != null && state != null) {
                if (!isOwningClass(state, type)) {
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(element);
                    linkedObjects.add(state);
                    linkedObjects.add(type);
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            }
            return auditEntry;
        }

        @objid ("ad6dc510-679e-4bdf-9840-a48714750cda")
        private boolean isOwningClass(State state, GeneralClass type) {
            MObject parent = state;
            while (parent != null && !parent.equals(type)) {
                parent = parent.getCompositionOwner();
            }
            return parent != null && parent.equals(type);
        }

    }

}
