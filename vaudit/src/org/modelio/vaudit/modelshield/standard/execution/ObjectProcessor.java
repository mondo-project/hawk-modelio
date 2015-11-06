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
                                    

package org.modelio.vaudit.modelshield.standard.execution;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.vaudit.modelshield.internal.ShieldContext;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

/**
 * Used to check an object (outside any transaction)
 */
@objid ("00281324-0000-0007-0000-000000000000")
public class ObjectProcessor {
    @objid ("5729e35d-2e72-11de-b561-001ec947cd2a")
    private final PlanExecution planExecutor;

    @objid ("00281ea8-0000-1317-0000-000000000000")
    private final ShieldContext context;

    @objid ("00281ea8-0000-130b-0000-000000000000")
    public void check(final MObject obj) {
        MStatus status = obj.getStatus();
        if (! status.isDeleted() && ! status.isRamc() && ! status.isShell()) {
            this.planExecutor.process(this.context, obj, TriggerType.AnyTrigger, null);
        }
        
        // recursion
        for (MObject child : obj.getCompositionChildren()) {
            check(child);
        }
    }

    @objid ("00281ea8-0000-1310-0000-000000000000")
    public ObjectProcessor(final ShieldContext context, final Plan plan) {
        this.context = context;
        this.planExecutor = new PlanExecution(plan);
    }

}
