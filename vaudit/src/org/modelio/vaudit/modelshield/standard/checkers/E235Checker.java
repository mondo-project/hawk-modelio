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
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCaseDependency;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E235:
 * <ul>
 * <li>desc = A UseCaseDependency must be directed towards a UseCase.</li>
 * <li>what = A use case dependency belonging to the ''{1}'' element is not directed towards a use case.</li>
 * </ul>
 */
@objid ("000a0dde-e20e-1f69-b3fb-001ec947cd2a")
public class E235Checker extends DepCardinalityChecker {
    @objid ("005df836-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E235";

    @objid ("0073553c-9731-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "Target";

    @objid ("009201da-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=UseCaseDependency, feature=null
        plan.registerChecker(this, Metamodel.getMClass(UseCaseDependency.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=UseCaseDependency, feature=Target
        plan.registerChecker(this, Metamodel.getMClass(UseCaseDependency.class), TriggerType.Update, DEPNAME);
    }

    @objid ("007368c4-9731-1f6c-bf9a-001ec947cd2a")
    public E235Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("3499f2e7-0a64-4aa2-b96c-f27005a484cd")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        List<Object> objects = new ArrayList<>();
        objects.add(object.getCompositionOwner());
        return new ModelError(ERRORID, object, objects);
    }

}
