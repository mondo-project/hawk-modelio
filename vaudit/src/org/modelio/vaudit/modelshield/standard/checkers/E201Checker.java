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
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E201:
 * <ul>
 * <li>desc = A Parameter cannot simultaneously be an in/out Parameter and a return Parameter.</li>
 * <li>what = The ''{0}'' parameter cannot simultaneously be an in/out parameter and a return parameter.</li>
 * </ul>
 */
@objid ("006a59d2-e20d-1f69-b3fb-001ec947cd2a")
public class E201Checker implements IChecker {
    @objid ("003477c2-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E201";

    @objid ("008e27a4-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        Parameter currentParameter = (Parameter) object;
        
        Operation composed = currentParameter.getComposed();
        Operation returned = currentParameter.getReturned();
        if (composed != null && returned != null) {
            List<Object> objects = new ArrayList<>();
            objects.add(composed);
            objects.add(returned);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("008e2966-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(Parameter.class), TriggerType.AnyTrigger, "Composed");
        plan.registerChecker(this, Metamodel.getMClass(Parameter.class), TriggerType.AnyTrigger, "Returned");
    }

}
