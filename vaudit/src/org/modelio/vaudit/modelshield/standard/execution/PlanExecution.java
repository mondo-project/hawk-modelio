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

import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.vaudit.modelshield.internal.ShieldContext;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("00280c2c-0000-02b3-0000-000000000000")
public class PlanExecution {
    @objid ("004069a6-f904-1f61-8473-001ec947cd2a")
    private final Plan plan;

    @objid ("00280c2c-0000-02cd-0000-000000000000")
    public PlanExecution(final Plan plan) {
        this.plan = plan;
    }

    @objid ("003957b0-544f-1f4e-b2b8-001ec947cd2a")
    public void process(final ShieldContext context, final MObject obj, final TriggerType trigger, final String feature) {
        Collection<IChecker> rulesToApply = this.plan.getApplicableCheckers(trigger, obj, feature);
        for (IChecker checker : rulesToApply) {
            context.applyChecker(checker, obj);
        }
    }

}
