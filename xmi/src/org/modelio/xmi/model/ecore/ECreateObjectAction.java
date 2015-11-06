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

@objid ("1a63e4cb-d235-4b97-8310-e3593e234059")
public class ECreateObjectAction extends EActivityNode {
    @objid ("85b973bc-9297-48f1-ba60-29d0c7bc9b91")
    private org.eclipse.uml2.uml.CreateObjectAction ecoreElement = null;

    @objid ("955a999d-9636-4de9-b9a7-ef49a161fac0")
    @Override
    public Element createObjingElt() {
        IMModelServices mmServices = ReverseProperties.getInstance().getMModelServices();
        
        OpaqueAction element = mmServices.getModelFactory()
                .createOpaqueAction();
        
        try {
            element.getExtension().add(mmServices
                    .getStereotype( IModelerModuleStereotypes.UML2CREATEOBJECTACTION, element.getMClass()));
        } catch (ElementNotUniqueException e) {
            Xmi.LOG.warning(e);
        }
        return element;
    }

    @objid ("0ddd53e4-d5a3-470a-a41c-f96bfcc4b10f")
    public ECreateObjectAction(org.eclipse.uml2.uml.CreateObjectAction element) {
        super(element);
        this.ecoreElement = element;
    }

    @objid ("322cfcf7-5cbc-4939-bcc8-1706631130de")
    @Override
    public void setProperties(Element objingElt) {
        super.setProperties(objingElt);
        setClassifier((OpaqueAction) objingElt);
    }

    @objid ("de6d4e67-f627-408a-acf5-e1b0f8f2879e")
    private void setClassifier(OpaqueAction objingElt) {
        IModelingSession session = Modelio.getInstance().getModelingSession();
        org.eclipse.uml2.uml.Classifier classifier = this.ecoreElement.getClassifier();
        if (classifier != null){
            ModelElement obBehavior = (ModelElement)ReverseProperties.getInstance().getMappedElement(classifier);
        
            Dependency dependency = session.getModel().createDependency();
        
            dependency.getExtension().add(session.getMetamodelExtensions()
                    .getStereotype( IModelerModuleStereotypes.UML2CLASSIFIERREFERENCE, dependency.getMClass()));
        
        
            dependency.setDependsOn(obBehavior);
            dependency.setImpacted(objingElt);
        }
    }

}
