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
 * E218:
 * <ul>
 * <li>desc = An AnalystProject must have one root dictionary.</li>
 * <li>what = The ''{0}'' analyst project has no root dictionary.</li>
 * </ul>
 */
@objid ("000e7ba8-e20e-1f69-b3fb-001ec947cd2a")
public class E218Checker extends DepCardinalityChecker {
    @objid ("006212ae-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E218";

    @objid ("00576fc0-9e33-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "DictionaryRoot";

    @objid ("00925306-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=AnalystProject, feature=null
        plan.registerChecker(this, Metamodel.getMClass(AnalystProject.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=AnalystProject, feature=DictionaryRoot
        plan.registerChecker(this, Metamodel.getMClass(AnalystProject.class), TriggerType.Update, DEPNAME);
    }

    @objid ("005778da-9e33-1f6c-bf9a-001ec947cd2a")
    public E218Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("53d8c3a7-db57-4da1-a0c1-e2b2441136f0")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        return createDefaultError(object, dep, currentCard);
    }

}
