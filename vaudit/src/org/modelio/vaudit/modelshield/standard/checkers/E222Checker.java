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
 * E222:
 * <ul>
 * <li>desc = An AnalystProject must have one property container.</li>
 * <li>what = The ''{0}'' analyst project has no property container.</li>
 * </ul>
 */
@objid ("0014c454-e20e-1f69-b3fb-001ec947cd2a")
public class E222Checker extends DepCardinalityChecker {
    @objid ("006770aa-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E222";

    @objid ("0059c9f0-9e33-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "Properties";

    @objid ("0092bd32-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=AnalystProject, feature=null
        plan.registerChecker(this, Metamodel.getMClass(AnalystProject.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=AnalystProject, feature=Properties
        plan.registerChecker(this, Metamodel.getMClass(AnalystProject.class), TriggerType.Update, DEPNAME);
    }

    @objid ("0059d2ec-9e33-1f6c-bf9a-001ec947cd2a")
    public E222Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("7446080b-e333-48d5-9fcd-572e7619cf17")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        return createDefaultError(object, dep, currentCard);
    }

}
