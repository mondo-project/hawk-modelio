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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E207:
 * <ul>
 * <li>desc = In a Transition, the 'Effects' relationship and the 'SentEvents' attribute must not be defined simultaneously.</li>
 * <li>what = The ''{0}'' transition must not simultaneouly define the 'Effects' relationship to the ''{1}'' signal and the 'SentEvents' attribute with the ''{2}'' value.</li>
 * </ul>
 */
@objid ("007caf88-e20d-1f69-b3fb-001ec947cd2a")
public class E207Checker implements IChecker {
    @objid ("00423ba0-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E207";

    @objid ("008f6664-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        Transition currentTransition = (Transition) object;
        
        Signal effects = currentTransition.getEffects();
        String sentEvents = currentTransition.getSentEvents();
        if (effects != null && sentEvents.length() > 0) {
            List<Object> objects = new ArrayList<>();
            objects.add(effects);
            objects.add(sentEvents);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("008f6830-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(Transition.class), TriggerType.AnyTrigger, "Effects");
        plan.registerChecker(this, Metamodel.getMClass(Transition.class), TriggerType.AnyTrigger, "Effects");
    }

}
