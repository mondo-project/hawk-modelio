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
                                    

package org.modelio.vaudit.modelshield.standard.checkers.tofix;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionFragment;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E287:
 * <ul>
 * <li>desc = InteractionFragment.getLastLine() must be always >= the InteractionFragment.getFirstLine()</li>
 * <li>what = Incorrect first line and last line for the ''{0}'' interaction fragment: {1} >= {2}</li>
 * </ul>
 */
@objid ("00583e8c-e20e-1f69-b3fb-001ec947cd2a")
public class E287Checker implements IChecker {
    @objid ("00957d42-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E287";

    @objid ("00970b44-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        /* === BEGIN CXX CODE
            InteractionFragment* ifn = NULL; 
            
            if (!(
                (ifn = dynamic_cast<InteractionFragment*>(&object))
                ))
            return;
            
            int ifn_getFirstLine = ifn->getFirstLine();
            int ifn_getLastLine = ifn->getLastLine();
            
            if (! (
                (ifn_getLastLine >= ifn_getFirstLine)
                ))
            {
                CR_string msg;
                
                CR_string msg_ifn_getFirstLine ; msg_ifn_getFirstLine << ifn_getFirstLine ;
                CR_string msg_ifn_getLastLine  ; msg_ifn_getLastLine  << ifn_getLastLine  ;
                
                msg.pformat(rule.whatHappen.c_str()      ,
                    ifn->get_Name().c_str()      ,
                    msg_ifn_getLastLine.c_str() , 
                    msg_ifn_getFirstLine.c_str()   
                    );
                
                
                AuditDiagnosticEntry entry(session.getPlan(), rule, object, "", msg.c_str(), rule.severity);
                
                session.addDiagnosticEntry(entry);
            }
        === END CXX CODE */
    }

    @objid ("00970d06-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=InteractionFragment, feature=null
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.AnyTrigger, null);
    }

}
