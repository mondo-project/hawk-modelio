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
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E249:
 * <ul>
 * <li>desc = A BindableInstance can represent an Attribute, an AssociationEnd or a BindableInstance.</li>
 * <li>what = The ''{0}'' part cannot represent the ''{1}'' %2.</li>
 * </ul>
 */
@objid ("002ab322-e20e-1f69-b3fb-001ec947cd2a")
public class E249Checker implements IChecker {
    @objid ("007747e6-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E249";

    /**
     * C++ reference: BindableInstanceChecker::checkRepresentedFeatureOnBindableInstance()
     */
    @objid ("00943662-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        BindableInstance currentBindableInstance = (BindableInstance) object;
        if (currentBindableInstance instanceof Port) {
            // Do nothing: bind checking on Port is done by another rule
            return;
        } else {
            ModelElement feature = currentBindableInstance.getRepresentedFeature();
            if (feature != null && ! ((feature instanceof Attribute) || (feature instanceof AssociationEnd) || feature instanceof BindableInstance)) {
                List<Object> objects = new ArrayList<>();
                objects.add(feature);
                report.addEntry(new ModelError(ERRORID, object, objects));
            }
        }
    }

    @objid ("0094382e-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=BindableInstance, feature=RepresentedFeature
        plan.registerChecker(this, Metamodel.getMClass(BindableInstance.class), TriggerType.AnyTrigger, "RepresentedFeature");
    }

}
