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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.BusinessRuleContainer;
import org.modelio.metamodel.analyst.Dictionary;
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.analyst.GoalContainer;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.RequirementContainer;
import org.modelio.metamodel.analyst.Term;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E292:
 * <ul>
 * <li>desc = An AnalystItem must have an AnalystPropertyTable.</li>
 * <li>what = An AnalystItem has no property set.</li>
 * </ul>
 */
@objid ("06b2e1a1-64f3-4f2d-bce8-bf104e95402c")
public class E292Checker extends DepCardinalityChecker {
    @objid ("ffeaaab4-3483-4af7-a4ed-c79ff305e84b")
    private static final String ERRORID = "E292";

    @objid ("93f711cf-02c2-4f1a-aa67-2dee1035a730")
    private static final String DEPNAME = "AnalystProperties";

    @objid ("d2d5e789-e527-457b-bb30-8f3311a63561")
    @Override
    public void check(MObject object, IErrorReport report) {
        super.check(object, report);
    }

    @objid ("cdeae854-bd7f-49c0-ba65-52a3592c7332")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(Requirement.class), TriggerType.AnyTrigger, "AnalystProperties");
        plan.registerChecker(this, Metamodel.getMClass(RequirementContainer.class), TriggerType.AnyTrigger, "AnalystProperties");
        
        plan.registerChecker(this, Metamodel.getMClass(Term.class), TriggerType.AnyTrigger, "AnalystProperties");
        plan.registerChecker(this, Metamodel.getMClass(Dictionary.class), TriggerType.AnyTrigger, "AnalystProperties");
        
        plan.registerChecker(this, Metamodel.getMClass(Goal.class), TriggerType.AnyTrigger, "AnalystProperties");
        plan.registerChecker(this, Metamodel.getMClass(GoalContainer.class), TriggerType.AnyTrigger, "AnalystProperties");
        
        plan.registerChecker(this, Metamodel.getMClass(BusinessRule.class), TriggerType.AnyTrigger, "AnalystProperties");
        plan.registerChecker(this, Metamodel.getMClass(BusinessRuleContainer.class), TriggerType.AnyTrigger, "AnalystProperties");
    }

    @objid ("e63ac28b-1d8c-4579-bf52-992878192f3e")
    public E292Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("a43813e3-8445-4463-bdd0-68698d0bb25d")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        return createDefaultError(object, dep, currentCard);
    }

}
