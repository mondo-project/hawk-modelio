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
                                    

package org.modelio.model.browser.handlers.state;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.model.browser.handlers.CreateElementHandler;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;

@objid ("8b0b93b3-c9d8-11e1-b479-001ec947c8cc")
public class CreateConnectionPointReferenceHandler extends CreateElementHandler {
    @objid ("0056f432-d035-1006-9c1d-001ec947cd2a")
    @Override
    protected boolean doCanExecute(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype) {
        if ((owner instanceof State) && ((State) owner).getSubMachine() == null) {
            return false;
        } else {
            return super.doCanExecute(owner, metaclass, dependency, stereotype);
        }
    }

}
