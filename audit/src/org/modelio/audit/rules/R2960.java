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
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * - A Synonym dependency can be created from a Term towards a Term. - A Antonym
 * dependency can be created from a Term towards a Term. - A Homonym dependency
 * can be created from a Term towards a Term. - A Related dependency can be
 * created from a Term towards a Term. - A Context dependency can be created
 * from a Term towards a Term. - A Kind-of dependency can be created from a Term
 * towards a Term or from a Dictionary towards a Dictionary.
 */
@objid ("1d3d5daa-ffd2-4986-bb09-99675c32af85")
public class R2960 extends AbstractRule {
    @objid ("ba23c829-e928-4069-9553-7490393506da")
    private static final String RULEID = "R2960";

    @objid ("b2beada1-357e-4f1c-9781-d7fbb9015fbd")
    private static final String DependencySynonymKind = "synonym";

    @objid ("cf5a3809-b863-479f-b567-24d0452a1000")
    private static final String DependencyAntonymKind = "antonym";

    @objid ("eb8486d1-f55e-4400-9e73-35f289f0376a")
    private static final String DependencyHomonymKind = "homonym";

    @objid ("2726c55c-df1e-4a7f-9934-1ccd4daca72a")
    private static final String DependencyContextind = "context";

    @objid ("337cc0f7-ddd4-4c44-a222-84b3379bb262")
    private static final String DependencyKindOfKind = "kind-of";

    /**
     * The checker unique instance. Remove it if you are not using a unique
     * checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("16fc04fe-55a7-4410-a906-d6f1c07cff59")
    private CheckR2960 checkerInstance = null;

    @objid ("6e97729f-fc8a-41f3-a1e1-79e882696ed7")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencySynonymKind, this, AuditTrigger.CREATE
                | AuditTrigger.MOVE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyAntonymKind, this, AuditTrigger.CREATE
                | AuditTrigger.MOVE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyHomonymKind, this, AuditTrigger.CREATE
                | AuditTrigger.MOVE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyContextind, this, AuditTrigger.CREATE
                | AuditTrigger.MOVE | AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyKindOfKind, this, AuditTrigger.CREATE
                | AuditTrigger.MOVE | AuditTrigger.UPDATE);
    }

    @objid ("ac44e121-6219-446e-a0c3-52ef2aa0b3b8")
    @Override
    public String getRuleId() {
        return RULEID;
    }

    /**
     * Default implementation is using a unique instance for the checker. An
     * alternative implementation consists in creating a new instance of the
     * checker for each element to check. This allows for fine tuning of the
     * check depending on the element status or on some external conditions. Use
     * the 'new instance' strategy only if fine tuning of the check is required
     * for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("2f917a81-37be-406b-9e40-a4c0f28e39cb")
    @Override
    public IControl getCreationControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An
     * alternative implementation consists in creating a new instance of the
     * checker for each element to check. This allows for fine tuning of the
     * check depending on the element status or on some external conditions. Use
     * the 'new instance' strategy only if fine tuning of the check is required
     * for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("fd4f8d31-2846-4156-9800-a4d22d8d8006")
    @Override
    public IControl getMoveControl(final IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An
     * alternative implementation consists in creating a new instance of the
     * checker for each element to check. This allows for fine tuning of the
     * check depending on the element status or on some external conditions. Use
     * the 'new instance' strategy only if fine tuning of the check is required
     * for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("28abaa8f-c23a-44cf-bb44-c2f521b4bdfa")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2960
     */
    @objid ("38d779d3-1793-4108-ad24-fc42762454e2")
    public R2960() {
        this.checkerInstance = new CheckR2960(this);
    }

    @objid ("df9651f8-5adc-4ec4-98de-94099129b9ac")
    public static class CheckR2960 extends AbstractControl {
        @objid ("197dd442-d069-444a-86b0-6b5ff01138d1")
        public CheckR2960(final IRule rule) {
            super(rule);
        }

        @objid ("cb1145cd-cdda-459e-8d54-f41e63a8d707")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof Dependency) {
                diagnostic.addEntry(checkR2960((Dependency) element));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID, "R2960: unsupported element type '%s'", element.getMClass().getName());
            return diagnostic;
        }

        @objid ("3e4ad676-b3d3-4bb6-b9b9-9b2973c5e0e4")
        private IAuditEntry checkR2960(final Dependency dependency) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(), AuditSeverity.AuditSuccess, dependency, null);
            
            ModelElement source = dependency.getImpacted();
            ModelElement target = dependency.getDependsOn();
            
            boolean failed = false;
            if (!(source instanceof Term && target instanceof Term)
                    && !(dependency.isStereotyped("ModelerModule", DependencyKindOfKind) && source instanceof Dictionary && target instanceof Dictionary)) {
                // Rule failed
                failed = true;
            
            }
            
            if (failed) {
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
