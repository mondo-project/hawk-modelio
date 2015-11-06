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
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.vaudit.modelshield.internal.ModelError;
import org.modelio.vaudit.modelshield.standard.TriggerType;
import org.modelio.vaudit.modelshield.standard.checkers.generic.DepCardinalityChecker;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * E217:
 * <ul>
 * <li>desc = A LinkEnd must be linked to a Link.</li>
 * <li>what = The ''{0}'' link end is not linked to a link.</li>
 * </ul>
 */
@objid ("008bccf2-e20d-1f69-b3fb-001ec947cd2a")
public class E217Checker extends DepCardinalityChecker {
    @objid ("004d4d56-6455-1f6c-bf9a-001ec947cd2a")
    private static final String ERRORID = "E217";

    @objid ("0056d6a0-9e33-1f6c-bf9a-001ec947cd2a")
    private static final String DEPNAME = "Linked";

    @objid ("0090719e-e472-1f69-b3fb-001ec947cd2a")
    @Override
    public void register(final Plan plan) {
        // trigger=create, metaclass=LinkEnd, feature=null
        plan.registerChecker(this, Metamodel.getMClass(LinkEnd.class), TriggerType.Create, null);
        
        // trigger=create, metaclass=LinkEnd, feature=Linked
        plan.registerChecker(this, Metamodel.getMClass(LinkEnd.class), TriggerType.Update, DEPNAME);
    }

    @objid ("0056e122-9e33-1f6c-bf9a-001ec947cd2a")
    public E217Checker() {
        super(ERRORID, DEPNAME);
    }

    @objid ("e4a97474-d8c2-4963-a042-83a596532f9f")
    @Override
    protected ModelError createError(MObject object, MDependency dep, int currentCard) {
        return createDefaultError(object, dep, currentCard);
    }

}
