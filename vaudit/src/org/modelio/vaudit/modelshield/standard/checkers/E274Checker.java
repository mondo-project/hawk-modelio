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
                                    

package org.modelio.vaudit.modelshield.standard.checkers;

import java.util.Arrays;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.CombinedFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionOccurenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionOperand;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionUse;
import org.modelio.metamodel.uml.behavior.interactionModel.Lifeline;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.metamodel.uml.behavior.interactionModel.MessageSort;
import org.modelio.metamodel.uml.behavior.interactionModel.OccurrenceSpecification;
import org.modelio.metamodel.uml.behavior.interactionModel.PartDecomposition;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E274:
 * <ul>
 * <li>desc = The creation message end must be the first fragment on the lifeline it covers.</li>
 * <li>what = The ''{0}'' creation message has at least one fragment before it.</li>
 * </ul>
 */
@objid ("007bfc5a-e20e-1f69-b3fb-001ec947cd2a")
public class E274Checker implements IChecker {
    @objid ("0017508e-6456-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E274";

    @objid ("0000d750-e473-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        if (object == null)
            return;
        else if (object instanceof Lifeline)
            checkForLifeline((Lifeline) object, report);
        else if (object instanceof InteractionFragment)
            checkForFragment((InteractionFragment) object, report);
    }

    @objid ("0000d926-e473-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=InteractionFragment, feature=null
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(CombinedFragment.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(InteractionUse.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(OccurrenceSpecification.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(PartDecomposition.class), TriggerType.Create, null);
        
        // trigger=update, metaclass=InteractionFragment, feature=Covered
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Update, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(CombinedFragment.class), TriggerType.Update, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Update, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.Update, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(InteractionUse.class), TriggerType.Update, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(OccurrenceSpecification.class), TriggerType.Update, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(PartDecomposition.class), TriggerType.Update, "Covered");
        
        // trigger=update, metaclass=InteractionFragment, feature=LineNumber
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Update, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(CombinedFragment.class), TriggerType.Update, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Update, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.Update, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(InteractionUse.class), TriggerType.Update, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(OccurrenceSpecification.class), TriggerType.Update, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(PartDecomposition.class), TriggerType.Update, "LineNumber");
        
        // trigger=move, metaclass=InteractionFragment, feature=Covered
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Move, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(CombinedFragment.class), TriggerType.Move, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Move, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.Move, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(InteractionUse.class), TriggerType.Move, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(OccurrenceSpecification.class), TriggerType.Move, "Covered");
        plan.registerChecker(this, Metamodel.getMClass(PartDecomposition.class), TriggerType.Move, "Covered");
        
        // trigger=move, metaclass=InteractionFragment, feature=LineNumber
        plan.registerChecker(this, Metamodel.getMClass(InteractionFragment.class), TriggerType.Move, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(CombinedFragment.class), TriggerType.Move, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Move, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(InteractionOperand.class), TriggerType.Move, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(InteractionUse.class), TriggerType.Move, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(OccurrenceSpecification.class), TriggerType.Move, "LineNumber");
        plan.registerChecker(this, Metamodel.getMClass(PartDecomposition.class), TriggerType.Move, "LineNumber");
    }

    @objid ("00684fa2-edf7-1f71-90c1-001ec947cd2a")
    private static void checkForFragment(final InteractionFragment fragmentToCheck, final IErrorReport report) {
        // at an IF creation/update, we check all the covered lifelines
        // to see if it breaks the destroy message position rule.
        for (Lifeline lifelineToCheck : fragmentToCheck.getCovered()) {
            checkForLifeline(lifelineToCheck, report);
        }
    }

    @objid ("0068a90c-edf7-1f71-90c1-001ec947cd2a")
    private static void checkForLifeline(final Lifeline lifeline, final IErrorReport report) {
        // For the whole lifeline, compare its fragments to the creation message
        // end on itself, if any
        ExecutionOccurenceSpecification createMessageEnd = null;
        
        for (InteractionFragment fragment : lifeline.getCoveredBy()) {
            if (fragment instanceof ExecutionOccurenceSpecification) {
                ExecutionOccurenceSpecification eos = (ExecutionOccurenceSpecification) fragment;
                if (eos.getReceivedMessage() != null) {
                    Message receivedMessage = eos.getReceivedMessage();
                    if (receivedMessage.getSortOfMessage() == MessageSort.CREATEMESSAGE) {
                        // we found a creation message end on the lifeline
                        createMessageEnd = eos;
                        break;
                    }
                }
            }
        }
        
        if (createMessageEnd != null) {
            int createLine = createMessageEnd.getLineNumber();
            for (InteractionFragment fragment : lifeline.getCoveredBy()) {
                if ((fragment.getLineNumber() < createLine)
                        || ((fragment instanceof InteractionUse) && (((InteractionUse) fragment).getEndLineNumber() < createLine))) {
                    Message destroyMessage = createMessageEnd.getReceivedMessage();
                    report.addEntry(new ModelError(ERRORID, destroyMessage, Arrays.asList((Object) fragment)));
                }
            }
        }
    }

}
