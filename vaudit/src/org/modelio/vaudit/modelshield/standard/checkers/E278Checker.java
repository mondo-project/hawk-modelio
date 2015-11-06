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

import java.util.Arrays;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E278:
 * <ul>
 * <li>desc = A communication diagram can only be defined on a communication interaction.</li>
 * <li>what = The ''{0}'' communication diagram is defined on %1, which is a(n) %2.</li>
 * </ul>
 */
@objid ("0067fcdc-e20e-1f69-b3fb-001ec947cd2a")
public class E278Checker implements IChecker {
    @objid ("0008246a-6456-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E278";

    @objid ("009812dc-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        if (object == null)
            return;
        
        CommunicationDiagram diagram = (CommunicationDiagram) object;
        if (diagram.getOrigin() == null || (diagram.getOrigin() instanceof CommunicationInteraction))
            return;
        
        report.addEntry(new ModelError(ERRORID, object, Arrays.asList((Object) diagram.getOrigin())));
    }

    @objid ("00981598-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=ModelElement, feature=product
        plan.registerChecker(this, Metamodel.getMClass(CommunicationDiagram.class), TriggerType.Update, "origin");
    }

}
