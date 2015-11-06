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
import org.eclipse.emf.common.util.EList;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ConditionalNode;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E265:
 * <ul>
 * <li>desc = ConditionalNodes must not directly own ActivityNodes (contrary to other StructuredActivityNodes such as LoopNodes).</li>
 * <li>what = The ''{0}'' conditional node cannot contain activity nodes.</li>
 * </ul>
 */
@objid ("004d1458-e20e-1f69-b3fb-001ec947cd2a")
public class E265Checker implements IChecker {
    @objid ("008cbdb0-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E265";

    /**
     * C++ reference: ActivityModelChecker::checkE265()
     */
    @objid ("00965334-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        ConditionalNode cn = (ConditionalNode)object;
        EList<ActivityNode> body = cn.getBody();
        if (body.size() > 0) {
            List<Object> objects = new ArrayList<>();
            objects.addAll(body);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("00965500-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=ConditionalNode, feature=Body
        plan.registerChecker(this, Metamodel.getMClass(ConditionalNode.class), TriggerType.AnyTrigger, "Body");
    }

}
