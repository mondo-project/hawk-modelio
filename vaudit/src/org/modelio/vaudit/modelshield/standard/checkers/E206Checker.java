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
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E206:
 * <ul>
 * <li>desc = An Operation cannot redefine itself.</li>
 * <li>what = The ''{0}'' operation must not redefine itself.</li>
 * </ul>
 */
@objid ("007b31c6-e20d-1f69-b3fb-001ec947cd2a")
public class E206Checker implements IChecker {
    @objid ("004103a2-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E206";

    @objid ("008f4c24-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        final Operation me = (Operation) object;
        final Operation redefined = me.getRedefines();
        if (redefined != null && redefined.equals(me)) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("008f4df0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(Operation.class), TriggerType.Update, "Redefines");
    }

}
