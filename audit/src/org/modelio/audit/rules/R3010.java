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
@objid ("0a1b30bd-adf2-4133-9adb-4f51dd95e021")
public class R3010 extends AbstractRule {
    @objid ("8a542564-eb42-4bc3-9f56-156ba4254e86")
    private static final String RULEID = "R3010";

    @objid ("33ebff9d-278a-4639-9115-04e2a0bd0999")
    private static final String DependencyRefersKind = "refers";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("6226b666-73aa-4eb0-b7f2-b9484cced60e")
    private CheckR3010 checkerInstance = null;

    @objid ("d2f8afee-7403-46e2-99c8-d556ed2bd691")
    @Override
    public void autoRegister(final IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dependency.class).getName() + DependencyRefersKind,
                          this,
                          AuditTrigger.CREATE | AuditTrigger.MOVE | AuditTrigger.UPDATE);
    }

    @objid ("a1b85aba-6e6d-46e4-9d11-728cfe7ad5c1")
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
    @objid ("9216b27e-b99a-40bc-83a6-e23358022d96")
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
    @objid ("dc660476-b8c4-4f6e-9699-335c913e636e")
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
    @objid ("88bc6fc9-caa6-424d-a65d-ff4442070774")
    @Override
    public IControl getUpdateControl(final MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R3010
     */
    @objid ("d10141d7-100f-4df2-931c-7842054cf890")
    public R3010() {
        this.checkerInstance = new CheckR3010(this);
    }

    @objid ("10287ea1-cea7-4224-b98e-c1c11ad13b67")
    public static class CheckR3010 extends AbstractControl {
        @objid ("694348b5-92af-4328-9984-4db808c65b75")
        public CheckR3010(final IRule rule) {
            super(rule);
        }

        @objid ("9018c357-fb38-401e-ac41-8635ce821d55")
        @Override
        public IDiagnosticCollector doRun(final IDiagnosticCollector diagnostic, final MObject element) {
            if (element instanceof Dependency) {
                diagnostic.addEntry(checkR3010((Dependency) element));
            } else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R3010: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("3378b841-ec14-4f64-aeca-dfa7ae36f1e7")
        private IAuditEntry checkR3010(final Dependency dependency) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dependency,
                                                   null);
            
            ModelElement source = dependency.getImpacted();
            ModelElement target = dependency.getDependsOn();
            
            if (!(source instanceof BusinessRule && target instanceof Term)) {
            
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
