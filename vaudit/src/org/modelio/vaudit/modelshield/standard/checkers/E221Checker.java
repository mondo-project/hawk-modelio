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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E221:
 * <ul>
 * <li>desc = An AnalystProject must have one root business rules container.</li>
 * <li>what = The ''{0}'' analyst project has no root business rules container.</li>
 * </ul>
 */
@objid ("00132220-e20e-1f69-b3fb-001ec947cd2a")
public class E221Checker extends DepCardinalityChecker {
    @objid ("0066724a-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E221";

    @objid ("005934cc-9e33-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "BusinessRuleRoot";

    @objid ("0092a2de-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=AnalystProject, feature=null
        plan.registerChecker(this, Metamodel.getMClass(AnalystProject.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=AnalystProject, feature=BusinessRuleRoot
        plan.registerChecker(this, Metamodel.getMClass(AnalystProject.class), TriggerType.Update, DEPNAME);
    }

    @objid ("00593daa-9e33-1f6c-bf9a-001ec947cd2a")
    public E221Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("77208d18-de2c-4ff3-a2de-3b323312ac2d")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        return createDefaultError(object, dep, currentCard);
    }

}
