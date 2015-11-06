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
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * A Assigned: Actor/Package/Process towards a Goal
 */
@objid ("48a99d22-5873-4601-a673-9ce5caede92c")
public class R3000 extends AbstractRule {
    @objid ("4650136d-d808-4c75-b3bb-0245e7a438fc")
    private static final String RULEID = "R3000";

    @objid ("eadb0e30-3c87-4a27-86b0-bab8e758c859")
    private static final String DependencyPositiveInfluenceKind = "+influence";

    @objid ("c83cedb9-e057-4cc1-9ece-9b46f540d984")
    private static final String DependencyNegativeInfluenceKind = "-influence";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("8afecc67-e078-4049-ba89-7ec4b8536351")
    private CheckR3000 checkerInstance = null;

    @objid ("822b82a6-3161-4363-853f-fedf9bc4f78c")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyPositiveInfluenceKind,
                          this,
                          AuditTrigger.CREATE | AuditTrigger.MOVE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyNegativeInfluenceKind,
                          this,
                          AuditTrigger.CREATE | AuditTrigger.MOVE | AuditTrigger.UPDATE);
    }

    @objid ("e0e73a08-89c4-4c54-aea1-ca96e363359c")
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
    @objid ("4bac472f-e3d1-4f2a-aa07-78f2af0c6e19")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("09e21962-5798-4f5d-9318-72d5be58a3c3")
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
    @objid ("985e848b-7c7f-4cea-bcd0-c64964b560d3")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3000
     */
    @objid ("61f13c52-7ca4-43ad-8a28-22c0f59fc50a")
    public R3000() {
        this.checkerInstance = new CheckR3000(this);
    }

    @objid ("b8f99148-8823-4b3e-a279-106be2146a24")
    public static class CheckR3000 extends AbstractControl {
        @objid ("4615a9c4-e88c-42af-a23c-701961bb9c80")
        public CheckR3000(final IRule rule) {
            super(rule);
        }

        @objid ("3ee573e6-4c90-44f5-83b5-c7db10309ba2")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof Dependency) {
                diagnostic.addEntry(checkR3000((Dependency) element));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3000: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("eb12b953-b3b6-432a-9f52-490533252c90")
        private IAuditEntry checkR3000(final Dependency dependency) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dependency,
                                                   null);
            
            ModelElement source = dependency.getImpacted();
            ModelElement target = dependency.getDependsOn();
            
            if (!(source instanceof Goal && target instanceof Goal)) {
            
                // Rule failed
            
                auditEntry.setSeverity(this.rule.getSeverity());
                ArrayList<Object> linkedObjects = new ArrayList<>();
                linkedObjects.add(dependency);
                linkedObjects.add(source);
                linkedObjects.add(source.getMClass().getName());
                linkedObjects.add(target);
                linkedObjects.add(target.getMClass().getName());
                auditEntry.setLinkedInfos(linkedObjects);
            }
            return auditEntry;
        }

    }

}
