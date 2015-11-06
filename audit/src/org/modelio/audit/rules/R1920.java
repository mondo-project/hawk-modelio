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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.audit.engine.core.IAuditEntry;
import org.modelio.audit.engine.core.IAuditPlan;
import org.modelio.audit.engine.core.IControl;
import org.modelio.audit.engine.core.IRule;
import org.modelio.audit.engine.impl.AbstractControl;
import org.modelio.audit.engine.impl.AbstractRule;
import org.modelio.audit.engine.impl.AuditEntry;
import org.modelio.audit.engine.impl.IDiagnosticCollector;
import org.modelio.audit.service.AuditSeverity;
import org.modelio.vcore.session.api.model.change.IElementMovedEvent;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Rule implementation origin:  LinkChecker checkLinkOrigin
 */
@objid ("0b757782-7d0b-4a9c-94f4-a3b3de623d03")
public class R1920 extends AbstractRule {
    @objid ("cd4ed637-43cf-4785-b34b-b3eef2454988")
    private static final String RULEID = "R1920";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * @see AbstractRule#getCreationControl(MObject)
     * @see AbstractRule#getUpdateControl(MObject)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("47e16425-2e9a-4b1e-a834-bc4efa03933b")
    private CheckR1920 checkerInstance = null;

    @objid ("db2fec12-2259-4b20-9580-5af415958516")
    @Override
    public String getRuleId() {
        return R1920.RULEID;
    }

    @objid ("2b31c209-88a2-4a6d-a90d-6d2cfab3d1e8")
    @Override
    public void autoRegister(IAuditPlan plan) {
        // plan.registerRule(Metamodel.getMClass(Link.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("a48426ab-c30e-49ea-ab16-598558aa1189")
    @Override
    public IControl getCreationControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("99dedf71-564a-4864-a07f-9681c3a09e40")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("aedc51d2-660a-4440-a985-021069d1f6c8")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R1920
     */
    @objid ("59784a5a-f99a-42f3-8d53-1d5028f3965f")
    public R1920() {
        this.checkerInstance = new CheckR1920(this);
    }

    @objid ("ed9321c1-1c1c-4945-af9e-021abf8d95a9")
    public static class CheckR1920 extends AbstractControl {
        @objid ("7167ce40-adad-4b14-bb30-a0fa2bb0fde0")
        public CheckR1920(IRule rule) {
            super(rule);
        }

        @objid ("d3206c20-cc3f-450d-8341-0b32c450b5f3")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            diagnostic.addEntry(checkR1920(element));
            return diagnostic;
        }

        @objid ("54bf29d3-fd6d-4482-988c-c15eb8be1954")
        private IAuditEntry checkR1920(MObject element) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(), AuditSeverity.AuditSuccess, element, null);
            // Link link = (Link)element;
            
            // Code the test here and update 'auditentry' according to the test results
            //---------- BEGIN Cxx original code -----------------
            /*
                Link *currentLink = dynamic_cast < Link * >(&object);
                bool found = false;
            
                if (!currentLink)
                {
                    return;
                }
            
                Association *baseAssoc = currentLink->get_Base();
            
                std::set < AssociationEnd * >assocEndList;
                std::set < AssociationEnd * >instancedAssocEndList;
            
                AssociationEnd *assocEnd = 0;
                LinkEnd *linkEnd = 0;
            
                int i = 0;
            
                if (baseAssoc)
                {
                    CR_string msg;
                    msg.pformat(rule.whatHappen.c_str(), currentLink->get_Name().c_str(), baseAssoc->get_Name().c_str());
                    AuditDiagnosticEntry entry(session.getPlan(), rule, object, "R102-Link", msg.c_str(), rule.severity);
                    for (i = 0; i < baseAssoc->card_Connection(); i++)
                    {
                        assocEnd = AssociationEnd::downCast(baseAssoc->get_Connection(i));
            
                        if (assocEnd)
                        {
                            assocEndList.insert(assocEnd);
                        }
                    }
            
                    for (i = 0; i < currentLink->card_Connection(); i++)
                    {
                        linkEnd = currentLink->get_Connection(i);
            
                        if (linkEnd)
                        {
                            entry.addLinkEntry(*linkEnd);
                            assocEnd = linkEnd->get_Model();
                            if (assocEnd)
                            {
                                instancedAssocEndList.insert(assocEnd);
                            }
                        }
                    }
            
                    if ( instancedAssocEndList.size() > 0 && assocEndList != instancedAssocEndList)
                    {
                        session.addDiagnosticEntry(entry);
                    }
                }
            */
            
            // --------- END Cxx original code --------------------
            
            if (false) {
                    auditEntry.setSeverity(this.rule.getSeverity());
            }
            return auditEntry;
        }

    }

}
