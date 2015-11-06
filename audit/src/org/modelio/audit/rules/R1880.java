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
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: LinkChecker checkContext
 */
@objid ("cc2f9158-ea3c-4e6a-9865-5092ac602112")
public class R1880 extends AbstractRule {
    @objid ("ab943995-ab35-46d9-bf8a-ae6c8ea99bc6")
    private static final String RULEID = "R1880";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(IElement)
     * @see AbstractRule#getUpdateControl(IElement)
     * @see AbstractRule#getMoveControl(IElementMovedEvent)
     */
    @objid ("eb2d1b89-3955-47b8-99c4-4bcc9afdf149")
    private CheckR1880 checkerInstance = null;

    @objid ("d9a4e910-9b2c-47b5-9250-6d5a6444e2a8")
    @Override
    public String getRuleId() {
        return R1880.RULEID;
    }

    @objid ("474372ba-aad9-4f6e-83b7-946327a5f4d6")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Link.class).getName(), this, AuditTrigger.CREATE |AuditTrigger.UPDATE | AuditTrigger.MOVE);
        plan.registerRule(Metamodel.getMClass(BindableInstance.class).getName(), this, AuditTrigger.UPDATE | AuditTrigger.MOVE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("5faa348f-14d3-4fb8-932d-0e810762cd29")
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
    @objid ("a783852c-9426-4e16-a72c-579516d7de70")
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
    @objid ("0df66520-8858-402c-b51c-7b11fdca15a7")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R1880
     */
    @objid ("d49e095b-c706-4b80-b2b8-5575a8458893")
    public R1880() {
        this.checkerInstance = new CheckR1880(this);
    }

    @objid ("ac6eceba-ced1-41f1-a20e-11a06d3c745c")
    static class CheckR1880 extends AbstractControl {
        @objid ("d06119bc-a2a6-436e-89b5-3c57e4db1227")
        public CheckR1880(IRule rule) {
            super(rule);
        }

        @objid ("fbc61ebd-568b-4783-8268-7e34a034e8b1")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if (element instanceof Link)
                diagnostic.addEntry(checkR1880((Link) element));
            if (element instanceof BindableInstance) {
                BindableInstance bi = (BindableInstance) element;
                for (LinkEnd le : bi.getOwnedEnd()) {
                    diagnostic.addEntry(checkR1880(le.getLink()));
                }
            }else
                Audit.LOG.warning(Audit.PLUGIN_ID, "R1000: unsupported element type '%s'", element.getMClass().getName());
            return diagnostic;
        }

        @objid ("db0b52bd-2d90-437c-9b19-5601af4becb3")
        private IAuditEntry checkR1880(final Link connector) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   connector,
                                                   null);
            
            Classifier reference = null;
            
            for (LinkEnd linkEnd : connector.getLinkEnd()) {
                Instance instance = linkEnd.getSource() != null ? linkEnd.getSource() : linkEnd.getOpposite().getTarget();
                if (instance instanceof BindableInstance) {
                    Classifier context = getTopContext((BindableInstance) instance);
                    if (reference == null)
                        reference = context;
                    else if (reference != context) {
            
                        // Rule failed
            
                        auditEntry.setSeverity(this.rule.getSeverity());
                        ArrayList<Object> linkedObjects = new ArrayList<>();
                        linkedObjects.add(connector);
                        auditEntry.setLinkedInfos(linkedObjects);
                    }
                }
            }
            return auditEntry;
        }

        @objid ("a990193d-9d37-4e11-8046-e1024243c5ef")
        private Classifier getTopContext(final BindableInstance bindableInstance) {
            Classifier owner = bindableInstance.getInternalOwner();
            ModelTree tmp = null;
            
            while ((tmp = owner.getOwner()) != null && tmp instanceof Classifier)
                owner = (Classifier) tmp;
            return owner;
        }

    }

}
