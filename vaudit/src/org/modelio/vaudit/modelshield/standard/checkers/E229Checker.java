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
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E229:
 * <ul>
 * <li>desc = A Property must have a PropertyType.</li>
 * <li>what = The ''{0}'' property has no property type.</li>
 * </ul>
 */
@objid ("009681c4-e20d-1f69-b3fb-001ec947cd2a")
public class E229Checker extends DepCardinalityChecker {
    @objid ("0054c20c-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E229";

    @objid ("005e5042-9e33-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "Type";

    @objid ("00912e54-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=Property, feature=null
        plan.registerChecker(this, Metamodel.getMClass(PropertyDefinition.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=Property, feature=Type
        plan.registerChecker(this, Metamodel.getMClass(PropertyDefinition.class), TriggerType.Update, DEPNAME);
    }

    @objid ("005e5934-9e33-1f6c-bf9a-001ec947cd2a")
    public E229Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("174fb21b-6345-4013-8727-862a471c33a1")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        // TODO Auto-generated method stub
        return createDefaultError(object, dep, currentCard);
    }

}
