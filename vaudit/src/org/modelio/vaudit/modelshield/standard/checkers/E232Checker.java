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
import org.modelio.metamodel.uml.behavior.interactionModel.Message;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E232:
 * <ul>
 * <li>desc = A Message must be directed towards a MessageEnd.</li>
 * <li>what = A message belonging to the ''{1}'' element is not directed towards an message end.</li>
 * </ul>
 */
@objid ("0003e5f8-e20e-1f69-b3fb-001ec947cd2a")
public class E232Checker extends DepCardinalityChecker {
    @objid ("00591f28-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E232";

    @objid ("00610ea4-9e33-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "ReceiveEvent";

    @objid ("009197ea-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=Message, feature=null
        plan.registerChecker(this, Metamodel.getMClass(Message.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=Message, feature=ReceiveEvent
        plan.registerChecker(this, Metamodel.getMClass(Message.class), TriggerType.Update, DEPNAME);
    }

    @objid ("006117a0-9e33-1f6c-bf9a-001ec947cd2a")
    public E232Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("d707b4f5-ef75-437e-b215-5014f2629c9a")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        List<Object> objects = new ArrayList<>();
        objects.add(object.getCompositionOwner());
        return new ModelError(ERRORID, object, objects);
    }

}
