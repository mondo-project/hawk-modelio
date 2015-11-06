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
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.TypeChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E204:
 * <ul>
 * <li>desc = A StateMachine can belong to an Operation or any other NameSpace, except a DataType, a primitive Class or an Enumeration.</li>
 * <li>what = The ''{0}'' state machine cannot belong to the ''{1}'' element. </li>
 * </ul>
 */
@objid ("0076ab60-e20d-1f69-b3fb-001ec947cd2a")
public class E204Checker extends TypeChecker {
    @objid ("003db698-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E204";

    @objid ("008efbc0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(MObject object, IErrorReport report) {
        MObject checkedObject = getCheckedObject(object);
        if (checkedObject == null) {
            // the rule cannot complain about type when there is no object
            // the responsibility of checking null object is not here
            return;
        }
        
        if (checkedObject instanceof GeneralClass && !(checkedObject instanceof Interface)) {
            GeneralClass checkedClass = (GeneralClass) checkedObject;
            if (checkedClass.isIsElementary()) {
                List<Object> objects = new ArrayList<>();
                objects.add(object);
                objects.add(checkedClass);
                report.addEntry(new ModelError(ERRORID, object, objects));
                return;
            }
        }
        super.check(object, report);
    }

    @objid ("008efd96-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(StateMachine.class), TriggerType.Create, null);
        plan.registerChecker(this, Metamodel.getMClass(StateMachine.class), TriggerType.Create, "Owner");
        plan.registerChecker(this, Metamodel.getMClass(StateMachine.class), TriggerType.Create, "OwnerOperation");
    }

    @objid ("d7428979-d7f6-11e1-a4a6-002564c97630")
    public E204Checker() {
        super(ERRORID);
        addRequiredType(Metamodel.getMClass(Operation.class));
        addRequiredType(Metamodel.getMClass(Artifact.class));
        addRequiredType(Metamodel.getMClass(Actor.class));
        addRequiredType(Metamodel.getMClass(Class.class));
        addRequiredType(Metamodel.getMClass(Collaboration.class));
        addRequiredType(Metamodel.getMClass(Component.class));
        addRequiredType(Metamodel.getMClass(InformationItem.class));
        addRequiredType(Metamodel.getMClass(Interface.class));
        addRequiredType(Metamodel.getMClass(Node.class));
        addRequiredType(Metamodel.getMClass(Operation.class));
        addRequiredType(Metamodel.getMClass(Package.class));
        addRequiredType(Metamodel.getMClass(Signal.class));
        addRequiredType(Metamodel.getMClass(UseCase.class));
        
        addForbiddenType(Metamodel.getMClass(DataType.class));
        addForbiddenType(Metamodel.getMClass(Enumeration.class));
        addForbiddenType(Metamodel.getMClass(TemplateParameter.class));
    }

    @objid ("d742d799-d7f6-11e1-a4a6-002564c97630")
    @Override
    protected MObject getCheckedObject(final MObject obj) {
        return obj.getCompositionOwner();
    }

}
