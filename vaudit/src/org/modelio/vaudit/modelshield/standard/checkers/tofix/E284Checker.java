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
 * E284:
 * <ul>
 * <li>desc = An ExecutionSpecification, ExecutionOccurrenceSpecification and StateInvariant must cover exactly one Lifeline.</li>
 * <li>what = The ''{0}'' interaction fragment covers {1} lifelines.</li>
 * </ul>
 */
@objid ("005ce720-e20e-1f69-b3fb-001ec947cd2a")
public class E284Checker implements IChecker {
    @objid ("00005262-6456-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E284";

    @objid ("00975a5e-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        /*
         * === BEGIN CXX CODE InteractionFragment *interactionFragment = NULL;
         * 
         * if (!((interactionFragment = dynamic_cast < InteractionFragment
         * *>(&object)) && (object.get_ClassOf() ==
         * ExecutionSpecificationClass() || object.get_ClassOf() ==
         * ExecutionOccurenceSpecificationClass() || object.get_ClassOf() ==
         * MessageEndClass() || object.get_ClassOf() == StateInvariantClass())))
         * return;
         * 
         * if (interactionFragment->card_Covered() != 1) { // EOS that can be
         * ends of a lost or found message are not required to cover a lifeline
         * bool canBeLostFoundEnd = false; ExecutionOccurenceSpecification *eos
         * = dynamic_cast < ExecutionOccurenceSpecification *
         * >(interactionFragment); if (eos) { // oci: I consider here that
         * whether the message has the correct attribute or cardinality does not
         * concern this rule. if ((eos->card_SentMessage() > 0) ||
         * (eos->card_ReceivedMessage() > 0)) { // it can be a lost or found
         * message end canBeLostFoundEnd = true; } }
         * 
         * if (!canBeLostFoundEnd) { CR_string msg; CR_string cardCovered;
         * cardCovered << interactionFragment->card_Covered();
         * 
         * msg.pformat(rule.whatHappen.c_str(),
         * interactionFragment->get_Name().c_str(), cardCovered.c_str() );
         * 
         * AuditDiagnosticEntry entry(session.getPlan(), rule, object, "",
         * msg.c_str(), rule.severity);
         * 
         * session.addDiagnosticEntry(entry); } }
         * 
         * === END CXX CODE
         */
    }

    @objid ("00975c2a-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=InteractionFragment, feature=null
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=InteractionFragment, feature=Covered
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Create, "Covered");
    }

}
