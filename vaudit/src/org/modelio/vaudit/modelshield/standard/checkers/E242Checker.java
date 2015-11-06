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
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E242:
 * <ul>
 * <li>desc = A Manifestation cannot manifest its Artifact.</li>
 * <li>what = The ''{1}'' artifact must not manifest itself.</li>
 * </ul>
 */
@objid ("001b5ff8-e20e-1f69-b3fb-001ec947cd2a")
public class E242Checker implements IChecker {
    @objid ("006c0e76-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E242";

    /**
     * C++ reference: ManifestationChecker::checkUtilizedArtifact()
     */
    @objid ("009326f0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        Manifestation currentManifestation = (Manifestation) object;
        
        Artifact owner = currentManifestation.getOwner();
        if (owner != null && owner.equals(currentManifestation.getUtilizedElement())) {
            List<Object> objects = new ArrayList<>();
            objects.add(owner);
            report.addEntry(new ModelError(ERRORID, object, objects));
        }
    }

    @objid ("009328bc-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=Manifestation, feature=null
        plan.registerChecker(this, Metamodel.getMClass(Manifestation.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=Manifestation, feature=UtilizedElement
        plan.registerChecker(this, Metamodel.getMClass(Manifestation.class), TriggerType.Create, "UtilizedElement");
        
        // trigger=create, metaclass=Manifestation, feature=Owner
        plan.registerChecker(this, Metamodel.getMClass(Manifestation.class), TriggerType.Create, "Owner");
    }

}
