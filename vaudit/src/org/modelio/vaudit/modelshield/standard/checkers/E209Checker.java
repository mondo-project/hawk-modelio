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
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateVertex;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E209:
 * <ul>
 * <li>desc = The start relationship must not be defined inside an InternalTransition.</li>
 * <li>what = The ''{0}'' internal transition's start relationship must not be defined.</li>
 * </ul>
 */
@objid ("007fa62a-e20d-1f69-b3fb-001ec947cd2a")
public class E209Checker implements IChecker {
    @objid ("00446786-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E209";

    @objid ("008f9bca-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        InternalTransition currentTransition = (InternalTransition) object;
        
        StateVertex source = currentTransition.getSource();
        if (source != null) {
            List<Object> objects = new ArrayList<>();
            objects.add(source);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("008f9d96-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(InternalTransition.class), TriggerType.AnyTrigger, "Source");
    }

}
