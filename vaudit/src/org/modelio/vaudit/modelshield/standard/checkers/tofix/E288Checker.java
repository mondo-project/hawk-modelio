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
import org.modelio.metamodel.uml.behavior.interactionModel.Gate;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E288:
 * <ul>
 * <li>desc = A Gate on an InteractionUse must satisfy these inequalities:
 * Gate.OwnerUse.getFirstLine() < Gate.getFirstLine() <
 * Gate.OwnerUse.getLastLine()</li>
 * <li>what = Incorrect first line or last line for the ''{0}'' gate on the
 * ''{1}'' interaction use : {2} < {3} < {4}.</li>
 * </ul>
 */
@objid ("0055163a-e20e-1f69-b3fb-001ec947cd2a")
public class E288Checker implements IChecker {
    @objid ("00935d96-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E288";

    @objid ("0096d6b0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        /*
         * === BEGIN CXX CODE Gate* g = NULL; InteractionUse* iu = NULL;
         * 
         * if (!( (g = dynamic_cast<Gate*>(&object)) && (g->card_OwnerUse() > 0
         * ) && (iu = g->get_OwnerUse()) )) return;
         * 
         * if (iu->get_ClassOf()->get_Name() == "PartDecomposition") return;
         * 
         * int iu_getFirstLine = iu->getFirstLine(); int g_getFirstLine = g
         * ->getFirstLine(); int iu_getLastLine = iu->getLastLine();
         * 
         * if (! ( (iu_getFirstLine < g_getFirstLine) && (g_getFirstLine <
         * iu_getLastLine) )) { CR_string msg;
         * 
         * CR_string msg_iu_getFirstLine ; msg_iu_getFirstLine <<
         * iu_getFirstLine ; CR_string msg_g_getFirstLine ; msg_g_getFirstLine
         * << g_getFirstLine ; CR_string msg_iu_getLastLine ; msg_iu_getLastLine
         * << iu_getLastLine ;
         * 
         * msg.pformat(rule.whatHappen.c_str() , g->get_Name().c_str() ,
         * iu->get_Name().c_str() , msg_iu_getFirstLine.c_str() ,
         * msg_g_getFirstLine.c_str() , msg_iu_getLastLine.c_str() );
         * 
         * AuditDiagnosticEntry entry(session.getPlan(), rule, object, "",
         * msg.c_str(), rule.severity);
         * 
         * entry.addLinkEntry(*iu);
         * 
         * session.addDiagnosticEntry(entry); } === END CXX CODE
         */
    }

    @objid ("0096d87c-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=Gate, feature=OwnerUse
        plan.registerChecker(this, Metamodel.getMClass(Gate.class), TriggerType.AnyTrigger, "OwnerUse");
        
        // trigger=*, metaclass=Gate, feature=OwnerUse
        plan.registerChecker(this, Metamodel.getMClass(Gate.class), TriggerType.AnyTrigger, "OwnerUse");
    }

}
