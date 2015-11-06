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
import org.modelio.metamodel.uml.behavior.stateMachineModel.InitialPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateVertex;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: TransitionChecker checkR63006 error
 */
@objid ("2b10cf3f-cf76-464f-ba6f-7cb986a6da60")
public class R2800 extends AbstractRule {
    @objid ("5ebb8bdf-eaf6-4411-bd00-f90fc1c43d10")
    private static final String RULEID = "R2800";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("db20ff81-0668-4372-a94a-2451342f3163")
    private CheckR2800 checkerInstance = null;

    @objid ("3fd36595-384b-40e9-990c-8d05957e861f")
    @Override
    public String getRuleId() {
        return R2800.RULEID;
    }

    @objid ("2e129309-8f6e-4f08-845a-0387fc52f097")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Transition.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE | AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(InitialPseudoState.class).getName(), this, AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("10ec0a5e-6acd-4537-b6dd-8e92ca67d45b")
    @Override
    public IControl getCreationControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("6223d304-421e-4820-b6d1-74a034bb1208")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("53911fd8-9182-4380-8633-7999fd22da37")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2800
     */
    @objid ("5fe52bbb-bd75-4b37-a373-1f41abac0677")
    public R2800() {
        this.checkerInstance = new CheckR2800(this);
    }

    @objid ("c008a17a-c305-493d-8706-32f2131fed53")
    public static class CheckR2800 extends AbstractControl {
        @objid ("ab0611be-71fa-4546-908b-89c4dc71c701")
        public CheckR2800(IRule rule) {
            super(rule);
        }

        @objid ("4c11a0ce-1459-48cb-aa61-4079c07b271e")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if (element instanceof Transition){
                StateVertex source = ((Transition)element).getSource();
                if (source instanceof InitialPseudoState) {
                    diagnostic.addEntry(checkR2800((InitialPseudoState) source));
                }
            }
            else if(element instanceof InitialPseudoState)
                diagnostic.addEntry(checkR2800((InitialPseudoState) element));
            else
                Audit.LOG.warning("R2800: unsupported element type '%s'", element.getMClass().getName());
            return diagnostic;
        }

        @objid ("117c14ce-33d1-4f44-8ef8-ef81cade03cb")
        private IAuditEntry checkR2800(InitialPseudoState state) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(), AuditSeverity.AuditSuccess, state, null);
            
            List<Transition> outgoings = state.getOutGoing();
            if (outgoings.size() > 1) {
                auditEntry.setSeverity(this.rule.getSeverity());
                List<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(state);
                linkedObjects.addAll(outgoings);
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
