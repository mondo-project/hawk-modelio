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
import org.modelio.metamodel.uml.behavior.activityModel.InitialNode;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E262:
 * <ul>
 * <li>desc = An InitialNode cannot have incoming edges.</li>
 * <li>what = The ''{0}'' initial node cannot have incoming edges.</li>
 * </ul>
 */
@objid ("004a2982-e20e-1f69-b3fb-001ec947cd2a")
public class E262Checker implements IChecker {
    @objid ("008ab3bc-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E262";

    /**
     * C++ reference: ActivityModelChecker::checkE262()
     */
    @objid ("00961e64-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        InitialNode fn = (InitialNode)object;
        
        EList<ActivityEdge> incoming = fn.getIncoming();
        if (incoming.size() > 0) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("00962030-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=InitialNode, feature=Incoming
        plan.registerChecker(this, Metamodel.getMClass(InitialNode.class), TriggerType.AnyTrigger, "Incoming");
    }

}
