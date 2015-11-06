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
import org.modelio.metamodel.uml.behavior.interactionModel.ExecutionSpecification;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E273:
 * <ul>
 * <li>desc = An ExecutionSpecification must have Start and Finish occurrences.</li>
 * <li>what = The ''{0}'' execution specification does not have a start or a finish occurrence.</li>
 * </ul>
 */
@objid ("00632cac-e20e-1f69-b3fb-001ec947cd2a")
public class E273Checker implements IChecker {
    @objid ("0004faba-6456-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E273";

    @objid ("0097c3cc-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        if (object == null || !(object instanceof ExecutionSpecification))
            return;
        
        ExecutionSpecification es = (ExecutionSpecification) object;
        
        if ((es.getStart() == null) || (es.getFinish() == null)) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("0097c58e-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=ExecutionSpecification, feature=null
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=ExecutionSpecification, feature=Start
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Update, "Start");
        
        // trigger=create, metaclass=ExecutionSpecification, feature=Finish
        plan.registerChecker(this, Metamodel.getMClass(ExecutionSpecification.class), TriggerType.Update, "Finish");
    }

}
