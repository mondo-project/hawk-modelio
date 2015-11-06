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

import java.util.Collections;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.activityModel.AcceptSignalAction;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityAction;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E256:
 * <ul>
 * <li>desc = An AcceptSignalAction cannot have InputPins.</li>
 * <li>what = The element ''{0}'' cannot have input pins.</li>
 * </ul>
 */
@objid ("004584f4-e20e-1f69-b3fb-001ec947cd2a")
public class E256Checker implements IChecker {
    @objid ("00871608-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E256";

    /**
     * C++ reference: ActivityModelChecker::checkHasNoOutputPins()
     */
    @objid ("0095cf54-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        ActivityAction activityAction = (ActivityAction)object;
        
        if (activityAction.getInput().size() > 0) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("0095d116-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=AcceptSignalAction, feature=Input
        plan.registerChecker(this, Metamodel.getMClass(AcceptSignalAction.class), TriggerType.AnyTrigger, "Input");
    }

}
