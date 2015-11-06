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
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageSort;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: MessageChecker checkInvoked
 */
@objid ("bcb80d37-5e38-4064-9197-98789cf84810")
public class R1950 extends AbstractRule {
    @objid ("915ece95-bb5c-4266-ae56-3cc2185f2f16")
    private static final String RULEID = "R1950";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("5a341df5-4970-4269-af39-967496490c2e")
    private CheckR1950 checkerInstance = null;

    @objid ("1a843786-d251-424c-be37-50fb069cfe1b")
    @Override
    public String getRuleId() {
        return R1950.RULEID;
    }

    @objid ("c30df644-29d8-4d9e-8535-0c872d6292e8")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Message.class).getName(), this, AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("0fb8f74e-5797-4b63-a369-a94d4b1e2098")
    @Override
    public IControl getCreationControl(MObject element) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("5da7d4a7-f443-4083-991c-9db723c20964")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return null;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("3c0606ab-7787-4247-88b3-e299dd54a697")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R1950
     */
    @objid ("e206e01f-c951-4615-bbf0-395461ddb5b7")
    public R1950() {
        this.checkerInstance = new CheckR1950(this);
    }

    @objid ("28753f7d-be8d-410f-993b-efc515ac2ed2")
    public static class CheckR1950 extends AbstractControl {
        @objid ("683b8b40-5dbe-41af-937f-2a2a880f287d")
        public CheckR1950(IRule rule) {
            super(rule);
        }

        @objid ("dc5ebb00-366e-4598-8fe2-37a728a7e756")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if (element instanceof Message)
                diagnostic.addEntry(checkR1950((Message) element));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R1950: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("c60e9980-f6f9-4b9c-9a1c-b29af33463b0")
        private IAuditEntry checkR1950(final Message message) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   message,
                                                   null);
            
            Operation operation = message.getInvoked();
            MessageSort sort = message.getSortOfMessage();
            
            if (operation != null &&
                !sort.equals(MessageSort.ASYNCCALL) &&
                !sort.equals(MessageSort.SYNCCALL) &&
                !sort.equals(MessageSort.CREATEMESSAGE) &&
                !sort.equals(MessageSort.DESTROYMESSAGE)) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(message);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
