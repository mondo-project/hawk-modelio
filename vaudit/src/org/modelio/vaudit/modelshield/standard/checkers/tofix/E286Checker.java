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
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionOperand;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E286:
 * <ul>
 * <li>desc = InteractionOperand.EndLineNumber must be >= to all InteractionOperand.Fragment.getLastLine()</li>
 * <li>what = The ''{0}'' interaction operand must have getLastLine() >= getFirstLine() of all its fragments.</li>
 * </ul>
 */
@objid ("0059c36a-e20e-1f69-b3fb-001ec947cd2a")
public class E286Checker implements IChecker {
    @objid ("00967f08-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E286";

    @objid ("00972570-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        /* === BEGIN CXX CODE
            InteractionOperand* io = NULL; 
            
            if (!(
                (io = dynamic_cast<InteractionOperand*>(&object))
                ))
            return;
            
            int eln = io->get_EndLineNumber();
            int card_Fragment = io->card_Fragment();
            set<InteractionFragment*> bad_fragments;
            for (int i = 0; i < card_Fragment; i++)
            {
                InteractionFragment* frag = io->get_Fragment(i);
                int ll = frag->getLastLine();
                if (!(eln >= ll))
                    bad_fragments.insert(frag);
            }
            
            if (! (bad_fragments.empty()))
            {
                CR_string msg;
                msg.pformat(rule.whatHappen.c_str(),
                    io->get_Name().c_str()
                    );
                
                AuditDiagnosticEntry entry(session.getPlan(), rule, object, "", msg.c_str(), rule.severity);
                
                set<InteractionFragment*>::iterator it;
                for (it = bad_fragments.begin(); it != bad_fragments.end(); ++it)
                    entry.addLinkEntry(**it);
                
                session.addDiagnosticEntry(entry);
            }
        === END CXX CODE */
    }

    @objid ("0097273c-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=InteractionOperand, feature=null
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.AnyTrigger, null);
        
        // trigger=*, metaclass=InteractionOperand, feature=Fragment
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.AnyTrigger, "Fragment");
    }

}
