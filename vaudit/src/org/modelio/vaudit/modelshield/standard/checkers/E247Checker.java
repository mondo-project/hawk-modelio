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
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E247:
 * <ul>
 * <li>desc = A NameSpace cannot import itself.</li>
 * <li>what = The ''{0}'' %1 cannot have an element import link towards itself.</li>
 * </ul>
 */
@objid ("00247ca0-e20e-1f69-b3fb-001ec947cd2a")
public class E247Checker implements IChecker {
    @objid ("0072b816-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E247";

    /**
     * C++ reference: ElementImportChecker::checkSelfImport()
     */
    @objid ("0093c8da-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        ElementImport currentElementImport = (ElementImport) object;
        
        NameSpace ns = currentElementImport.getImportingNameSpace();
        NameSpace dest = currentElementImport.getImportedElement();
        
        if (ns != null && dest != null && ns.equals(dest)) {
            List<Object> objects = new ArrayList<>();
            objects.add(ns);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("0093cab0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=ElementImport, feature=null
        plan.registerChecker(this, Metamodel.getMClass(ElementImport.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=ElementImport, feature=ImportingNameSpace
        plan.registerChecker(this, Metamodel.getMClass(ElementImport.class), TriggerType.Create, "ImportingNameSpace");
        
        // trigger=create, metaclass=ElementImport, feature=ImportedElement
        plan.registerChecker(this, Metamodel.getMClass(ElementImport.class), TriggerType.Create, "ImportedElement");
    }

}
