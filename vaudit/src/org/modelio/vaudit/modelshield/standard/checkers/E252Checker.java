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
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Component;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.Node;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.TypeChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E252:
 * <ul>
 * <li>desc = An Attribute can be created in:
 * - a Class
 * - a Enumeration
 * - an Actor
 * - an Interface
 * - a DataType
 * - a UseCase
 * - a Component
 * - a Node
 * - an AssociationEnd
 * - a Template Parameter</li>
 * <li>what = The ''{0}'' attribute cannot be created in the ''{1}'' element (%2).</li>
 * </ul>
 */
@objid ("00587ba4-e20d-1f69-b3fb-001ec947cd2a")
public class E252Checker extends TypeChecker {
    @objid ("0026c2c6-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E252";

    @objid ("008ce9d4-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=Attribute, feature=null
        plan.registerChecker(this, Metamodel.getMClass(Attribute.class), TriggerType.Create, null);
        
        // trigger=move, metaclass=Attribute, feature=
        plan.registerChecker(this, Metamodel.getMClass(Attribute.class), TriggerType.Move, null);
    }

    @objid ("0086f056-8c25-1f70-90c1-001ec947cd2a")
    public E252Checker() {
        super(ERRORID);
        
        addRequiredType(Metamodel.getMClass(Class.class));
        addRequiredType(Metamodel.getMClass(Actor.class));
        addRequiredType(Metamodel.getMClass(Interface.class));
        addRequiredType(Metamodel.getMClass(UseCase.class));
        addRequiredType(Metamodel.getMClass(Component.class));
        addRequiredType(Metamodel.getMClass(Node.class));
        addRequiredType(Metamodel.getMClass(DataType.class));
        addRequiredType(Metamodel.getMClass(Artifact.class));
        addRequiredType(Metamodel.getMClass(Signal.class));
        addRequiredType(Metamodel.getMClass(AssociationEnd.class));
        addRequiredType(Metamodel.getMClass(Enumeration.class));
    }

    @objid ("00872ab2-8c25-1f70-90c1-001ec947cd2a")
    @Override
    protected MObject getCheckedObject(final MObject obj) {
        return obj.getCompositionOwner();
    }

}
