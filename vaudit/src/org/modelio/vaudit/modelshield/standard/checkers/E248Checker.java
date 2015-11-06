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
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E248:
 * <ul>
 * <li>desc = A Usage link must have a destination.</li>
 * <li>what = A Usage link from the ''{0}'' element (%1) has no destination.</li>
 * </ul>
 */
@objid ("002912a6-e20e-1f69-b3fb-001ec947cd2a")
public class E248Checker implements IChecker {
    @objid ("00764062-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E248";

    /**
     * C++ reference: UsageChecker::checkDestination()
     */
    @objid ("00941a1a-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        Usage currentUsage = (Usage) object;
        ModelElement impacted = currentUsage.getImpacted();
        ModelElement dependsOn = currentUsage.getDependsOn();
        
        if (impacted != null && dependsOn == null) {
            report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
        }
    }

    @objid ("00941be6-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=Usage, feature=null
        plan.registerChecker(this, Metamodel.getMClass(Usage.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=Usage, feature=DependsOn
        plan.registerChecker(this, Metamodel.getMClass(Usage.class), TriggerType.Create, "DependsOn");
    }

}
