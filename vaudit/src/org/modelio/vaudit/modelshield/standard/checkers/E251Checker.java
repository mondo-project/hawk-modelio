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
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E251:
 * <ul>
 * <li>desc = A diagram cannot belongs to another diagram.</li>
 * <li>what = The ''{1}'' Diagram must not belong to a %0.</li>
 * </ul>
 */
@objid ("003f8036-e20e-1f69-b3fb-001ec947cd2a")
public class E251Checker implements IChecker {
    @objid ("0083fba8-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E251";

    /**
     * C++ reference: OwnershipChecker::checkDiagram()
     */
    @objid ("009562b2-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        AbstractDiagram c = (AbstractDiagram) object;
        ModelElement owner = c.getOrigin();
        
        if (owner instanceof AbstractDiagram) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("0095647e-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=AbstractDiagram, feature=Origin
        plan.registerChecker(this, Metamodel.getMClass(AbstractDiagram.class), TriggerType.AnyTrigger, "Origin");
        
        // trigger=create, metaclass=AbstractDiagram, feature=Origin
        plan.registerChecker(this, Metamodel.getMClass(AbstractDiagram.class), TriggerType.Create, null);
    }

}
