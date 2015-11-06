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
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E250:
 * <ul>
 * <li>desc = An artifact stereotyped <<ModelComponentArchive>> cannot manifest elements that belong to a deployed model component.</li>
 * <li>what = The ''{0}'' Artifact cannot manifest the ''{1}'' element because it already belongs to a deployed model component.</li>
 * </ul>
 */
@objid ("0030cc62-e20e-1f69-b3fb-001ec947cd2a")
public class E250Checker implements IChecker {
    @objid ("007bda90-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E250";

    /**
     * C++ reference: ManifestationChecker::checkRamcManifestation()
     */
    @objid ("0094a688-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        Manifestation currentManifestation = (Manifestation) object;
        ModelElement utilizedElement = currentManifestation.getUtilizedElement();
        Artifact owner = currentManifestation.getOwner();
        
        if (owner != null && owner.isStereotyped("ModelerModule", "ModelComponentArchive") && !((MObject)owner).getStatus().isRamc()) {
            if (utilizedElement != null && ((MObject)utilizedElement).getStatus().isRamc()) {
                List<Object> objects = new ArrayList<>();
                objects.add(owner);
                objects.add(utilizedElement);
        
                report.addEntry(new ModelError(ERRORID, object, objects));
            }
        }
    }

    @objid ("0094a854-e472-1f69-b3fb-001ec947cd2a")
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
