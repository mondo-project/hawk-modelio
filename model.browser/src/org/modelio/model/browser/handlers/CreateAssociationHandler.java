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
                                    

package org.modelio.model.browser.handlers;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;

@objid ("8c5e219f-c9d8-11e1-b479-001ec947c8cc")
public class CreateAssociationHandler extends CreateElementHandler {
    @objid ("53a5486f-ccff-11e1-97e5-001ec947c8cc")
    @Override
    protected Element doCreate(Element owner, MClass metaclass, MDependency dependency, Stereotype stereotype) {
        IModelFactory mmFactory = this.mmServices.getModelFactory(owner);
        IElementNamer mmNamer = this.mmServices.getElementNamer();
        
        Association newElement = mmFactory.createAssociation((Classifier) owner, (Classifier) owner, "");
        for (AssociationEnd end : newElement.getEnd()) {
            if (end.isNavigable()) {
                end.setName(mmNamer.getUniqueName(end));                
            }
        }
        
        if (stereotype != null) {
            newElement.getExtension().add(stereotype);
        }
        return newElement;
    }

}
