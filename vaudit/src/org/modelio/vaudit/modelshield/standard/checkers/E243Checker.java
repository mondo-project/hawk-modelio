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
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.behavior.usecaseModel.UseCase;
import org.modelio.metamodel.uml.statik.Artifact;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.TypeChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E243:
 * <ul>
 * <li>desc = An AssociationEnd can belong to a Class, an Interface, a Component, an Actor, a DataType, a Node or a Signal.</li>
 * <li>what = The ''{0}'' role cannot belong to the ''{1}'' %2.</li>
 * </ul>
 */
@objid ("00550bb8-e20d-1f69-b3fb-001ec947cd2a")
public class E243Checker extends TypeChecker {
    @objid ("00244d66-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E243";

    @objid ("008ca5f0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        plan.registerChecker(this, Metamodel.getMClass(AssociationEnd.class), TriggerType.Update, "Source");
        plan.registerChecker(this, Metamodel.getMClass(AssociationEnd.class), TriggerType.Update, "Target");
        
        plan.registerChecker(this, Metamodel.getMClass(AssociationEnd.class), TriggerType.Update, "Opposite");
        
        plan.registerChecker(this, Metamodel.getMClass(AssociationEnd.class), TriggerType.Create, null);
    }

    @objid ("002ca718-38c1-1f6b-b3fb-001ec947cd2a")
    public E243Checker() {
        super(ERRORID);
        
        addRequiredType(Metamodel.getMClass(AssociationEnd.class));
        addRequiredType(Metamodel.getMClass(Class.class));
        addRequiredType(Metamodel.getMClass(Actor.class));
        addRequiredType(Metamodel.getMClass(UseCase.class));
        addRequiredType(Metamodel.getMClass(DataType.class));
        addRequiredType(Metamodel.getMClass(Interface.class));
        addRequiredType(Metamodel.getMClass(Node.class));
        addRequiredType(Metamodel.getMClass(Signal.class));
        addRequiredType(Metamodel.getMClass(Artifact.class));
        addRequiredType(Metamodel.getMClass(Component.class));
        addRequiredType(Metamodel.getMClass(TemplateParameter.class));
        addRequiredType(Metamodel.getMClass(Enumeration.class));
    }

    @objid ("002cbbe0-38c1-1f6b-b3fb-001ec947cd2a")
    @Override
    protected MObject getCheckedObject(final MObject obj) {
        return obj.getCompositionOwner();
    }

}
