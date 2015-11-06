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
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * A Assigned: Actor/Package/Process towards a Goal
 */
@objid ("c37ad5ca-8cc7-40f1-b421-f93c320bf4c4")
public class R3020 extends AbstractRule {
    @objid ("3a2a66db-72dd-43c3-a621-1683bc1f67b0")
    private static final String RULEID = "R3020";

    @objid ("4da45ae2-a60b-4c3c-bf5f-3c01d3d707e9")
    private static final String DependencyRelatedKind = "related";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("9c5d39e4-dda7-40cf-882f-5cc27c777387")
    private CheckR3020 checkerInstance = null;

    @objid ("8425e35a-d1ea-489a-ad74-50d8030cb970")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyRelatedKind, this, AuditTrigger.CREATE |
                                                                                   AuditTrigger.MOVE |
                                                                                   AuditTrigger.UPDATE);
    }

    @objid ("738892bd-2594-496c-8f88-9e79cd7e720b")
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
    @objid ("e38de0f2-6bb9-416d-96ce-1d1c0b8b3caa")
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
    @objid ("81999925-f8f7-4240-b673-f3cc9d42bde4")
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
    @objid ("32273506-a8c8-4ed5-9e08-cb681f98d55f")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3020
     */
    @objid ("6d30c1bb-1734-4ac5-9023-e6480d411d25")
    public R3020() {
        this.checkerInstance = new CheckR3020(this);
    }

    @objid ("16190d11-35c0-43ab-8b7a-7ff98bcd4055")
    public static class CheckR3020 extends AbstractControl {
        @objid ("23e756a7-53c2-4dfb-be19-c594d2d7e5e7")
        public CheckR3020(final IRule rule) {
            super(rule);
        }

        @objid ("0b09481d-f7d8-427e-bf01-eab098403228")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof Dependency) {
                diagnostic.addEntry(checkR3020((Dependency) element));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3020: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("3bd59b5d-dbae-42ca-a6fe-51270ba655b1")
        private IAuditEntry checkR3020(final Dependency dependency) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dependency,
                                                   null);
            
            ModelElement source = dependency.getImpacted();
            ModelElement target = dependency.getDependsOn();
            
            if (source != null &&
                target != null &&
                !((source instanceof BusinessRule &&
                   target instanceof BusinessRule) || (source instanceof Term && target instanceof Term))) {
            
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
