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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
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
import org.modelio.metamodel.analyst.AnalystItem;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin: NameChecker checkDictionaryElements
 */
@objid ("9d3579f0-fa76-4abc-8a2e-2ed8a2079fc0")
public class R2010 extends AbstractRule {
    @objid ("027b32b1-c53f-4697-9758-a54cc46b5e50")
    private static final String RULEID = "R2010";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * 
     * @see AbstractRule#getCreationControl(Element)
     * @see AbstractRule#getUpdateControl(Element)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("adfc4308-c2aa-4fa8-baeb-2130f54132d4")
    private CheckR2010 checkerInstance = null;

    @objid ("64ccdca3-34f5-4ba5-802e-e37db813114f")
    @Override
    public String getRuleId() {
        return R2010.RULEID;
    }

    @objid ("b7134d2d-3220-45cd-b101-bf6b2cada27a")
    @Override
    public void autoRegister(IAuditPlan plan) {
        plan.registerRule(Metamodel.getMClass(Dictionary.class).getName(), this, AuditTrigger.CREATE |
                                                           AuditTrigger.MOVE |
                                                           AuditTrigger.UPDATE);
        plan.registerRule(Metamodel.getMClass(Term.class).getName(), this, AuditTrigger.CREATE |
                                                     AuditTrigger.MOVE |
                                                     AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in
     * creating a new instance of the checker for each element to check. This allows for fine tuning of the check
     * depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine
     * tuning of the check is required for each element, because this strategy creates many objects (performance
     * issues).
     */
    @objid ("dd01e4af-7129-4876-a810-98a73c1be6b8")
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
    @objid ("6e23c196-e1d0-4b93-8e3d-23d83feca354")
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
    @objid ("53dcf82f-7412-466e-b049-039365b92402")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R2010
     */
    @objid ("323e3f87-1958-4fb0-a14a-ef894feaaba5")
    public R2010() {
        this.checkerInstance = new CheckR2010(this);
    }

    @objid ("a092791c-3c51-4a72-85e2-5a69b9873362")
    public static class CheckR2010 extends AbstractControl {
        @objid ("0d43cd8a-99d1-4566-9282-715de2094407")
        public CheckR2010(IRule rule) {
            super(rule);
        }

        @objid ("1d05eec8-3fc2-44e2-b498-e7de36af4328")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            if (element instanceof Dictionary) {
                
                // Checking the Dictionary itself, in case it was updated from a move or a delete.
                Dictionary dictionary = (Dictionary) element;
                diagnostic.addEntry(checkR2010(dictionary));
                
                // Checking the parent Dictionary, in case the element was created or moved in a new parent
                Dictionary owner = dictionary.getOwnerDictionary();
                if (owner != null)
                    diagnostic.addEntry(checkR2010(owner));
            } else if (element instanceof Term)
                diagnostic.addEntry(checkR2010(((Term) element).getOwnerDictionary()));
            else
                Audit.LOG.warning(Audit.PLUGIN_ID,
                            "R2010: unsupported element type '%s'",
                            element.getMClass().getName());
            return diagnostic;
        }

        @objid ("f63e7510-a0cc-4a1f-85a1-5054d5e89d15")
        private IAuditEntry checkR2010(final Dictionary dictionary) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(),
                                                   AuditSeverity.AuditSuccess,
                                                   dictionary,
                                                   null);
            
            Map<String, List<AnalystItem>> duplicates = new HashMap<>();
            
            for (Dictionary de : dictionary.getOwnedDictionary()) {
                String name = de.getName();
                if (!duplicates.containsKey(name))
                    duplicates.put(name, new ArrayList<AnalystItem>());
                duplicates.get(de.getName()).add(de);
            }
            
            for (Term de : dictionary.getOwnedTerm()) {
                String name = de.getName();
                if (!duplicates.containsKey(name))
                    duplicates.put(name, new ArrayList<AnalystItem>());
                duplicates.get(de.getName()).add(de);
            }
            
            for (Entry<String, List<AnalystItem>> entry : duplicates.entrySet())
                if (entry.getValue().size() > 1) {
            
                    //Rule failed
            
                    auditEntry.setSeverity(this.rule.getSeverity());
                    ArrayList<Object> linkedObjects = new ArrayList<>();
                    linkedObjects.add(dictionary);
                    linkedObjects.add(entry.getKey());
                    auditEntry.setLinkedInfos(linkedObjects);
                }
            return auditEntry;
        }

    }

}
