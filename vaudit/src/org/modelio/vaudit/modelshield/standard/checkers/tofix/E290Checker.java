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
                                    

package org.modelio.vaudit.modelshield.standard.checkers.tofix;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.vaudit.modelshield.IErrorReport;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.IChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E290:
 * <ul>
 * <li>desc = If the sending and the receiving MessageEnds are on the same same Lifeline, the sending MessageEnd must have a LineNumber lesser than or equal to the receiving MessageEnd.</li>
 * <li>what = The ''{0}'' message must have SendEvent.LineNumber <= ReceiveEvent.LineNumber but {1} <= {2}.</li>
 * </ul>
 */
@objid ("00505a82-e20e-1f69-b3fb-001ec947cd2a")
public class E290Checker implements IChecker {
    @objid ("008f1f06-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E290";

    @objid ("009687a0-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void check(final MObject object, final IErrorReport report) {
        /* === BEGIN CXX CODE
        
        === END CXX CODE */
    }

    @objid ("0096896c-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=*, metaclass=Message, feature=ReceiveEvent
        plan.registerChecker(this, Metamodel.getMClass(Message.class), TriggerType.AnyTrigger, "ReceiveEvent");
        
        // trigger=*, metaclass=Message, feature=SendEvent
        plan.registerChecker(this, Metamodel.getMClass(Message.class), TriggerType.AnyTrigger, "SendEvent");
    }

}
