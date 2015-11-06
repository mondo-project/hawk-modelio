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
import org.eclipse.emf.common.util.EList;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityEdge;
import org.modelio.metamodel.uml.behavior.activityModel.FinalNode;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E261:
 * <ul>
 * <li>desc = A FinalNode cannot have outgoing edges.</li>
 * <li>what = The ''{0}'' final node cannot have outgoing edges.</li>
 * </ul>
 */
@objid ("00486796-e20e-1f69-b3fb-001ec947cd2a")
public class E261Checker implements IChecker {
    @objid ("00899b3a-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E261";

    /**
     * C++ reference: ActivityModelChecker::checkE261()
     */
    @objid ("009603fc-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        FinalNode fn = (FinalNode)object;
        
        EList<ActivityEdge> outgoing = fn.getOutgoing();
        if (outgoing.size() > 0) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("009605be-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=FinalNode, feature=Outgoing
        plan.registerChecker(this, Metamodel.getMClass(FinalNode.class), TriggerType.AnyTrigger, "Outgoing");
    }

}
