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
import org.modelio.metamodel.uml.behavior.activityModel.Activity;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityGroup;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityPartition;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E258:
 * <ul>
 * <li>desc = An activityGroup cannot belong to more than one Activity or group.</li>
 * <li>what = The ''{0}'' group cannot belong to more than one Activity or group.</li>
 * </ul>
 */
@objid ("00759446-e20e-1f69-b3fb-001ec947cd2a")
public class E258Checker implements IChecker {
    @objid ("0012b51a-6456-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E258";

    @objid ("00006c02-e473-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        if (object instanceof ActivityPartition) {
            ActivityPartition ap = (ActivityPartition)object;
                
            Activity inActivity = ap.getInActivity();
            ActivityPartition superPartition = ap.getSuperPartition();
            if (inActivity != null && superPartition != null) {
                List<Object> objects = new ArrayList<>();
                objects.add(inActivity);
                objects.add(superPartition);
                
                report.addEntry(new ModelError(ERRORID, object, objects));
            }
        }
    }

    @objid ("00006de2-e473-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=ActivityPartition, feature=SuperPartition
        plan.registerChecker(this, Metamodel.getMClass(ActivityPartition.class), TriggerType.AnyTrigger, "SuperPartition");
        
        // trigger=*, metaclass=ActivityGroup, feature=InActivity
        plan.registerChecker(this, Metamodel.getMClass(ActivityGroup.class), TriggerType.AnyTrigger, "InActivity");
    }

}
