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
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * A Assigned: Actor/Package/Process towards a Goal
 */
@objid ("6cd1fb59-d528-4595-8ab9-77cec008929e")
public class R3060 extends AbstractRule {
    @objid ("25a57d6a-ca77-438c-ada1-9ee1f7f9e048")
    private static final String RULEID = "R3060";

    @objid ("0a191c52-abc5-4d84-860e-a999446ecd99")
    private static final String DependencySatisfyKind = "satisfy";

    @objid ("1a408a88-205c-4a9b-bb08-087604643d3a")
    private static final String DependencyVerifyKind = "verify";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("c4469b1f-5b33-4d38-9ead-c6d5d919b8ff")
    private CheckR3060 checkerInstance = null;

    @objid ("90731018-ecbd-4256-b049-0a24573174c2")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencySatisfyKind, this, AuditTrigger.CREATE |
                                                                                  AuditTrigger.MOVE |
                                                                                  AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyVerifyKind, this, AuditTrigger.CREATE |
                          AuditTrigger.MOVE |
                          AuditTrigger.UPDATE);
    }

    @objid ("2d58413a-e52d-4b5a-b462-64092fba9113")
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
    @objid ("6f41a414-44e8-48ad-9569-4bf0f5556fa7")
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
    @objid ("98eb22cc-1f5b-4571-ad7e-f6cbdc95579c")
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
    @objid ("b4b7c07f-7539-42e6-b9b0-796d35304838")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3060
     */
    @objid ("521b8017-717f-4523-a6ed-3bec24af0fe9")
    public R3060() {
        this.checkerInstance = new CheckR3060(this);
    }

    @objid ("a0e28762-b60c-4975-804c-8e78ecb2b46c")
    public static class CheckR3060 extends AbstractControl {
        @objid ("f51223cc-5c38-4fae-a35c-b0b146ff76c2")
        public CheckR3060(final IRule rule) {
            super(rule);
        }

        @objid ("676d6b9e-493b-4104-9c28-db938522900c")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof Dependency) {
                diagnostic.addEntry(checkR3060((Dependency) element));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3060: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("59e40afc-34f7-40f6-a682-139f20a871be")
        private IAuditEntry checkR3060(final Dependency dependency) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dependency,
                                                   null);
            
            ModelElement source = dependency.getImpacted();
            ModelElement target = dependency.getDependsOn();
            
            if (!(target instanceof Requirement)) {
            
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
