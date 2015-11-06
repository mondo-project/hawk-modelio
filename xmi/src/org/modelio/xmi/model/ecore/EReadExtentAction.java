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
                                    

package org.modelio.xmi.model.ecore;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.modelio.Modelio;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.ElementNotUniqueException;
import org.modelio.metamodel.uml.behavior.activityModel.OpaqueAction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.xmi.plugin.Xmi;
import org.modelio.xmi.util.IModelerModuleStereotypes;
import org.modelio.xmi.util.ReverseProperties;

@objid ("bbeaca2d-aa3b-45bc-b1ec-d5b4ef4b1f07")
public class EReadExtentAction extends EActivityNode {
    @objid ("68f1cd8b-491e-4f6d-87b9-4841a4b184bf")
    private org.eclipse.uml2.uml.ReadExtentAction ecoreElement = null;

    @objid ("07cbcc71-b4ec-4e4c-8559-98f30192b8c8")
    @Override
    public Element createObjingElt() {
        IMModelServices mmServices = ReverseProperties.getInstance().getMModelServices();
        
        OpaqueAction element = mmServices.getModelFactory()
                .createOpaqueAction();
        
        try {
            element.getExtension().add(mmServices
                    .getStereotype(IModelerModuleStereotypes.UML2READEXTENTACTION, element.getMClass()));
        } catch (ElementNotUniqueException e) {
            Xmi.LOG.warning(e);
        }
        return element;
    }

    @objid ("6106fd33-b159-4dd7-b8f1-51bbb0833002")
    public EReadExtentAction(org.eclipse.uml2.uml.ReadExtentAction element) {
        super(element);
        this.ecoreElement = element;
    }

    @objid ("9313bd01-211c-423b-9585-42a45c370be6")
    @Override
    public void setProperties(Element objingElt) {
        super.setProperties(objingElt);
        setClassifier((OpaqueAction) objingElt);
    }

    @objid ("0910ad03-2bc3-464e-b816-9fba81e21655")
    private void setClassifier(OpaqueAction objingElt) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        org.eclipse.uml2.uml.Classifier classifier = this.ecoreElement.getClassifier();
        if (classifier != null){
            ModelElement obBehavior = (ModelElement)ReverseProperties.getInstance().getMappedElement(classifier);
        
            Dependency dependency = session.getModel().createDependency();
        
            dependency.getExtension().add(session.getMetamodelExtensions()
                    .getStereotype(IModelerModuleStereotypes.UML2CLASSIFIERREFERENCE, dependency.getMClass()));
        
            dependency.setDependsOn(obBehavior);
            dependency.setImpacted(objingElt);
        }
    }

}
