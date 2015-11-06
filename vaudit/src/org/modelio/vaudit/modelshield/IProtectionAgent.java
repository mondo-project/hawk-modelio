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
                                    

package org.modelio.vaudit.modelshield;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.vaudit.modelshield.internal.ErrorReport;
import org.modelio.vaudit.modelshield.standard.plan.Plan;
import org.modelio.vcore.session.impl.transactions.Transaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * An IProtectionAgent is a agent able to check a {@link Transaction} or an {@link MObject} and to fill
 * an {@link ErrorReport}. IProtectionAgents have to be registered on the {@link ModelShield} to be active.
 * 
 * @author phv
 */
@objid ("d9ed9fe1-3ef9-11de-8be5-001ec947cd2a")
public interface IProtectionAgent {
    /**
     * @param object the object to evaluate.
     * @return true if the object is in the scope of this agent responsibility.
     */
    @objid ("f4014263-3ef9-11de-8be5-001ec947cd2a")
    boolean isInScope(final MObject object);

    @objid ("f4014264-3ef9-11de-8be5-001ec947cd2a")
    CheckStatus check(final Transaction theTransaction, final IErrorReport report);

    @objid ("f4014265-3ef9-11de-8be5-001ec947cd2a")
    CheckStatus check(final MObject obj, final IErrorReport report);

    @objid ("0060d9b6-1ace-1f4e-b2b8-001ec947cd2a")
    String getId();

    @objid ("00114d7e-0abf-1f62-8473-001ec947cd2a")
    Plan getPlan();

}
