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
import org.modelio.metamodel.uml.statik.ElementImport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E213:
 * <ul>
 * <li>desc = An ElementImport must be directed towards a NameSpace.</li>
 * <li>what = An element import belonging to ''{1}'' is not directed towards a name space.</li>
 * </ul>
 */
@objid ("008589fa-e20d-1f69-b3fb-001ec947cd2a")
public class E213Checker extends DepCardinalityChecker {
    @objid ("0048ef5e-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E213";

    @objid ("0000c2d8-80ec-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "ImportedElement";

    @objid ("00900790-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=ElementImport, feature=null
        plan.registerChecker(this, Metamodel.getMClass(ElementImport.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=ElementImport, feature=ImportedElement
        plan.registerChecker(this, Metamodel.getMClass(ElementImport.class), TriggerType.Update, DEPNAME);
    }

    @objid ("0000d67e-80ec-1f6c-bf9a-001ec947cd2a")
    public E213Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("035ce4cf-2d72-4a91-8981-b4b7a4a35933")
    @Override
    protected ModelError createError(final MObject object, MDependency dep, int currentCard) {
        List<Object> objects = new ArrayList<>();
        objects.add(object.getCompositionOwner());
        return new ModelError(ERRORID, object, objects);
    }

}
