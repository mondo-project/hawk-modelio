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

import java.util.Collections;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.properties.LocalPropertyTable;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E205:
 * <ul>
 * <li>desc = An element must belong to another element, with the exception of UML projects, NamespaceUse, AnalystProject, ModuleComponent and LocalpropertyTable.</li>
 * <li>what = The ''{1}'' {0} is orphan: it has no container. (identifier: {3})</li>
 * </ul>
 * 
 * where {0} is the orphan element
 */
@objid ("00799e24-e20d-1f69-b3fb-001ec947cd2a")
public class E205Checker implements IChecker {
    @objid ("003fd0ae-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E205";

    @objid ("008f31a8-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        if (object.isValid() && object.getCompositionOwner() == null) {
            if (!(object instanceof Project) &&
                    !(object instanceof AnalystProject) && 
                    !(object instanceof NamespaceUse) && 
                    !(object instanceof ModuleComponent) && 
                    !(object instanceof LocalPropertyTable)) {
                report.addEntry(new ModelError(ERRORID, object, Collections.emptyList()));
            }
        }
    }

    @objid ("008f3374-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(Element.class), TriggerType.Move, null);
        plan.registerChecker(this, Metamodel.getMClass(Element.class), TriggerType.Create, null);
    }

}
