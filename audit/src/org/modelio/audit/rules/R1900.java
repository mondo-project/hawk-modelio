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
 * Rule implementation origin:  LinkChecker checkLinkEndOrigin
 */
@objid ("e222c0f6-89d7-462c-a457-59bcbc7723f1")
public class R1900 extends AbstractRule {
    @objid ("a9003bde-78ff-467b-ba32-4d2811120462")
    private static final String RULEID = "R1900";

    /**
     * The checker unique instance. Remove it if you are not using a unique checker strategy.<br>
     * @see AbstractRule#getCreationControl(MObject)
     * @see AbstractRule#getUpdateControl(MObject)
     * @see AbstractRule#getMoveControl(ElementMovedEvent)
     */
    @objid ("c18ae737-44a3-4bc9-ab51-a9fd4a0281dc")
    private CheckR1900 checkerInstance = null;

    @objid ("4ea2972b-c38b-4312-9fe3-4be630a37682")
    @Override
    public String getRuleId() {
        return R1900.RULEID;
    }

    @objid ("ef877d69-5e50-469c-bcc4-5a377e778ea8")
    @Override
    public void autoRegister(IAuditPlan plan) {
        // plan.registerRule(Metamodel.getMClass(Link.class).getName(), this, AuditTrigger.CREATE | AuditTrigger.UPDATE);
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("bb6a4bad-aa84-4d95-949b-7f7a9444bc02")
    @Override
    public IControl getCreationControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("77f21021-d87b-4d79-8db3-2e3d4344ffec")
    @Override
    public IControl getMoveControl(IElementMovedEvent moveEvent) {
        return this.checkerInstance;
    }

    /**
     * Default implementation is using a unique instance for the checker. An alternative implementation consists in creating a new instance of the checker for each element to check. This allows for fine tuning of the check depending on the element status or on some external conditions. Use the 'new instance' strategy only if fine tuning of the check is required for each element, because this strategy creates many objects (performance issues).
     */
    @objid ("3bed35c4-dbab-44dc-8b87-0934ee37df42")
    @Override
    public IControl getUpdateControl(MObject element) {
        return this.checkerInstance;
    }

    /**
     * Default constructor for R1900
     */
    @objid ("d47f00dd-08ee-4204-a791-ab07425992c1")
    public R1900() {
        this.checkerInstance = new CheckR1900(this);
    }

    @objid ("16fc012b-db72-4cc9-b453-aacad2e29088")
    public static class CheckR1900 extends AbstractControl {
        @objid ("67160566-1a73-42ec-be48-c2986ec3bb61")
        public CheckR1900(IRule rule) {
            super(rule);
        }

        @objid ("e0690d1a-95b5-4dd0-9341-030f7c5d45c5")
        @Override
        public IDiagnosticCollector doRun(IDiagnosticCollector diagnostic, MObject element) {
            diagnostic.addEntry(checkR1900(element));
            return diagnostic;
        }

        @objid ("a9b7493e-5c76-4e9b-8d01-d2fc7c86ffa3")
        private IAuditEntry checkR1900(MObject element) {
            AuditEntry auditEntry = new AuditEntry(this.rule.getRuleId(), AuditSeverity.AuditSuccess, element, null);
            // Link link = (Link)element;
            
            // Code the test here and update 'auditentry' according to the test results
            //---------- BEGIN Cxx original code -----------------
            /*
                LinkEnd *currentLinkEnd = dynamic_cast < LinkEnd * >(&object);
                bool found = false;
            
                if (!currentLinkEnd)
                {
                    return;
                }
            
                NameSpace *originClass = 0;
                if (currentLinkEnd->get_Linked())
                {
                    originClass = currentLinkEnd->get_Linked()->get_Base();
                }
            
                if (!originClass)
                {
                    return;
                }
            
                AssociationEnd *baseAssocEnd = currentLinkEnd->get_Model();
                std::set < NameSpace * >parentList;
            
                if (baseAssocEnd)
                {
                    NameSpace *owner = (NameSpace *) baseAssocEnd->get_Owner();
            
                    AuditServices::getAllParentNameSpaces(*owner, parentList);
                    std::set < NameSpace * >::iterator it;
            
                    for (it = parentList.begin(); it != parentList.end(); it++)
                    {
                        if (*it == originClass)
                        {
                            found = true;
                        }
                    }
            
                    if (!found && owner != originClass)
                    {
                        CR_string msg;
                        msg.pformat(rule.whatHappen.c_str(), currentLinkEnd->get_Name().c_str(),
                                    currentLinkEnd->get_LinkNode()->get_Name().c_str(), originClass->get_Name().c_str(),
                                    RC2::getRC("classof.properties").getString(originClass->ClassOf->Name).c_str());
            
                        session.addDiagnosticEntry(AuditDiagnosticEntry(session.getPlan(), rule, object, "R102-LinkEnd", msg.c_str(), rule.severity));
                    }
                }
            */
            
            // --------- END Cxx original code --------------------
            return auditEntry;
        }

    }

}
