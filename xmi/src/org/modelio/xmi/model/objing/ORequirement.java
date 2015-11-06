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
                                    

package org.modelio.xmi.model.objing;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLFactory;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.xmi.util.GenerationProperties;
import org.modelio.xmi.util.IModelerModuleStereotypes;

@objid ("5239dc77-9628-4c39-bcc2-596bb58d6a21")
public class ORequirement extends OElement implements IOElement {
    @objid ("8ee0a227-ba94-46c7-9c4d-f508ec9e440a")
    private GenerationProperties genProp = GenerationProperties.getInstance();

    @objid ("5a22a2b3-d28b-44e2-bbca-4f3b77f13276")
    public org.eclipse.uml2.uml.Element createEcoreElt() {
        return  UMLFactory.eINSTANCE.createClass();
    }

    @objid ("b8bf3d14-6283-4cc6-93a5-5264c5837e91")
    public ORequirement(Requirement param) {
        super(param);
    }

    @objid ("ce91cfe3-75be-49cd-aa85-2d431baff034")
    public void attach(org.eclipse.uml2.uml.Element ecoreElt) {
        if (ecoreElt != null){
            GenerationProperties genProp = GenerationProperties.getInstance();
            if (isRequirementPart()){
                genProp.getEcoreModel().getPackagedElements().remove((PackageableElement)ecoreElt);
                ( (org.eclipse.uml2.uml.Class)genProp.getMappedElement(getRequirementOwner())).getNestedClassifiers().add( (org.eclipse.uml2.uml.Class) ecoreElt);
            }else
                genProp.getEcoreModel().getPackagedElements().add((PackageableElement)ecoreElt);
        }
    }

    @objid ("fcfb50a5-2e03-420a-b511-48b60481481b")
    public void setProperties(org.eclipse.uml2.uml.Element ecoreElt) {
        if (ecoreElt != null){
            genProp.addSysMLExported((Requirement) getObjingElement());
            ( (org.eclipse.uml2.uml.Class)ecoreElt).setName(((Requirement)getObjingElement()).getName());
            ( (org.eclipse.uml2.uml.Class)ecoreElt).setVisibility(org.eclipse.uml2.uml.VisibilityKind.PUBLIC_LITERAL);
            ( (org.eclipse.uml2.uml.Class)ecoreElt).setIsAbstract(false);
        }
    }

    @objid ("edac69ea-d7db-4661-9ce3-ba1397daec2f")
    private boolean isRequirementPart() {
        for (Dependency dependency : ((ModelElement)getObjingElement()).getDependsOnDependency()){
            if (dependency.isStereotyped("ModelerModule", IModelerModuleStereotypes.PART)){
                return true;
            }
        }
        return false;
    }

    @objid ("b90bc7e9-38ef-486c-a07e-f6cfdb622cae")
    private Requirement getRequirementOwner() {
        for (Dependency dependency : ((ModelElement)getObjingElement()).getDependsOnDependency()){
            if (dependency.isStereotyped("ModelerModule", IModelerModuleStereotypes.PART)){
        
                return (Requirement) dependency.getDependsOn();
            }
        }
        return null;
    }

}
