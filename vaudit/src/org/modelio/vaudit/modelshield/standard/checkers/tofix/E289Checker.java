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
                                    

package org.modelio.vaudit.modelshield.standard.checkers.tofix;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.CombinedFragment;
import org.modelio.metamodel.uml.behavior.interactionModel.Gate;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E289:
 * <ul>
 * <li>desc = A Gate on CombinedFragments must satisfy these inequalities: Gate.OwnerFragment.getFirstLine() < Gate.getFirstLine() < Gate.OwnerFragment.getLastLine()</li>
 * <li>what = Incorrect first line or last line for the  ''{0}'' gate on the ''{1}'' combined fragment : {2}  <  {3}  <  {4}.</li>
 * </ul>
 */
@objid ("00539094-e20e-1f69-b3fb-001ec947cd2a")
public class E289Checker implements IChecker {
    @objid ("00916eaa-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E289";

    @objid ("0096bc48-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        Gate g = (Gate) object; 
        CombinedFragment cf  = g.getOwnerFragment();
        
        if (cf == null) {
            return;
        }
        
        // TODO getFirstLine and getLastLine are not available on Java elements for now...
        int cf_getFirstLine = 0; //cf.getFirstLine();
        int g_getFirstLine  = 0; //g .getFirstLine();
        int cf_getLastLine = 0; //cf.getLastLine();
        if ((cf_getFirstLine >= g_getFirstLine) || (g_getFirstLine >= cf_getLastLine)) {
            List<Object> objects = new ArrayList<>();
            objects.add(cf);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("0096be14-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=Gate, feature=OwnerFragment
        plan.registerChecker(this, Metamodel.getMClass(Gate.class), TriggerType.AnyTrigger, "OwnerFragment");
        
        // trigger=*, metaclass=Gate, feature=OwnerFragment
        plan.registerChecker(this, Metamodel.getMClass(Gate.class), TriggerType.AnyTrigger, "OwnerFragment");
    }

}
