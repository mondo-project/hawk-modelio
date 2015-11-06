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
@objid ("1a4c9f4c-fe3c-4627-aaa1-da81bd4e8b90")
public class R2980 extends AbstractRule {
    @objid ("77c0985f-132f-4337-8e81-d9d69d0986f5")
    private static final String RULEID = "R2980";

    @objid ("91384c1b-f52e-47b6-b96c-69e72e23f94b")
    private static final String DependencyMeasureKind = "measure";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("a5ad607e-e847-47a9-a8ad-a9d35e0de949")
    private CheckR2980 checkerInstance = null;

    @objid ("4e014548-5712-48dc-8417-c96fb7f59061")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyMeasureKind, this, AuditTrigger.CREATE |
                                                                                   AuditTrigger.MOVE |
                                                                                   AuditTrigger.UPDATE);
    }

    @objid ("f7b318a1-912d-4955-b66d-46f32a35491f")
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
    @objid ("1c479d9f-e06f-42de-9013-5a5620858887")
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
    @objid ("bd0d1c97-c1bc-4497-933f-7b8dc4845525")
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
    @objid ("a7c46182-12ef-435d-8be2-d18bb92aa315")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2980
     */
    @objid ("50563a08-ca1f-4b20-a288-40fab531591f")
    public R2980() {
        this.checkerInstance = new CheckR2980(this);
    }

    @objid ("4be89116-c5f9-4a7c-abba-472f2a73acf2")
    public static class CheckR2980 extends AbstractControl {
        @objid ("cba59d41-b164-47a2-8a97-e6627c6925bc")
        public CheckR2980(final IRule rule) {
            super(rule);
        }

        @objid ("1ef372b4-1aca-40bf-b65c-832cd606ac57")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof Dependency) {
                diagnostic.addEntry(checkR2980((Dependency) element));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R2980: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("fb9d0320-9ad7-4588-b0c5-4b7a6e3b1314")
        private IAuditEntry checkR2980(final Dependency dependency) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dependency,
                                                   null);
            
            ModelElement source = dependency.getImpacted();
            ModelElement target = dependency.getDependsOn();
            
            if (!(target instanceof Goal)) {
            
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
