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
                                    

package org.modelio.mda.infra.service.dynamic;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramService;
import org.modelio.api.diagram.style.IStyleHandle;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.ui.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.factory.ModelFactory;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MStatus;

@objid ("da1043d3-d7e1-47ea-9329-be797a702b59")
class GenericWizardContributor implements IDiagramWizardContributor {
    @objid ("ee824b6e-96af-4e79-b4f6-88e0a8597fbc")
    private final String label;

    @objid ("97f9a63f-3f47-4be7-a406-578a1be40bcd")
    private final String helpUrl;

    @objid ("c47dc6a0-db7a-43cb-be58-7abb84334cac")
    private final String information;

    @objid ("9f91ea0b-c158-4ca9-a33f-3c22b57b90ed")
    private final String details;

    @objid ("a0c16379-093b-4d0d-b593-6479fa75e277")
    private String style;

    @objid ("a3465474-004c-4f74-beab-ae6add6526b8")
    private final List<MClass> allowedMetaclasses;

    @objid ("be8239cc-da88-4ad5-b009-fccea551e50a")
    private final List<Stereotype> allowedStereotypes;

    @objid ("e5b43ab5-75d8-4cb6-9f01-6966150b76a9")
    private Image icon;

    @objid ("872cc7c3-0daf-441e-905a-fe2cd534d5cd")
    private MClass metaclass;

    @objid ("39c24346-5ada-4ef4-bd51-0ed2032abb7f")
    private Stereotype stereotype;

    @objid ("fd9739f2-1848-4702-af11-f558079146a1")
    public GenericWizardContributor(MClass metaclass, Stereotype stereotypeClass, String style, String label, String helpUrl, String information, ImageDescriptor iconDescriptor, String details, List<MClass> allowedMetaclasses, List<Stereotype> allowedStereotypes) {
        // Jxbv2Handler
        this.metaclass = metaclass;
        this.stereotype = stereotypeClass;
        this.style = style;
        
        // Jxbv2Scope
        this.allowedMetaclasses = allowedMetaclasses;
        this.allowedStereotypes = allowedStereotypes;
        
        // Display
        this.label = label;
        this.helpUrl = helpUrl;
        this.information = information;
        this.details = details;
        if (iconDescriptor != null) {
            this.icon = iconDescriptor.createImage();
        }
    }

    @objid ("80bcc6e5-5a87-4a23-959b-c01222fc4a54")
    @Override
    public boolean accept(MObject owner) {
        if (owner == null) {
            return false;
        }
        
        for (Stereotype allowedStereotype : this.allowedStereotypes) {
            if (owner instanceof ModelElement) {
                ModelElement modelElement = (ModelElement) owner;
                for (Stereotype extention : modelElement.getExtension()) {
                    if (compareStereotype(extention, allowedStereotype)) {
                        MStatus elementStatus = owner.getStatus();
                        if (owner.getMClass().isCmsNode() && elementStatus.isCmsManaged()) {
                            return !elementStatus.isRamc();
                        } else {
                            return owner.isModifiable();
                        }
                    }
                }
            }
        }
        
        for (MClass allowedMetaclass : this.allowedMetaclasses) {
            if (owner.getMClass().hasBase(allowedMetaclass)) {
                MStatus elementStatus = owner.getStatus();
                if (owner.getMClass().isCmsNode() && elementStatus.isCmsManaged()) {
                    return !elementStatus.isRamc();
                } else {
                    return owner.isModifiable();
                }
            }
        }
        return false;
    }

    @objid ("f1088037-c988-4685-92cb-edcdf8d9d306")
    private Boolean compareStereotype(Stereotype aStereotype, Stereotype type) {
        if (aStereotype.equals(type)) {
            return true;
        } else {
            if (aStereotype.getParent() != null) {
                return compareStereotype(aStereotype.getParent(), type);
            }
        }
        return false;
    }

    @objid ("41c3c450-3bb4-4f95-b76c-c6c826736ddb")
    @Override
    public String getLabel() {
        return this.label;
    }

    @objid ("014d4af5-545a-42ed-94e3-724a2f7221d8")
    @Override
    public String getInformation() {
        return this.information;
    }

    @objid ("94546f1d-13db-4bef-977b-ece9c3e1944d")
    @Override
    public Image getIcon() {
        return this.icon;
    }

    @objid ("182268d7-2bdb-498e-bb49-52b2bf14088c")
    @Override
    public String getHelpUrl() {
        return this.helpUrl;
    }

    @objid ("600a47b5-21aa-4a39-8c3d-1fbe1ca78170")
    @Override
    public String getDetails() {
        return this.details;
    }

    @objid ("011f0ec3-6ff7-4002-947b-087592c4711e")
    @Override
    public List<MClass> getAcceptedMetaclasses() {
        return this.allowedMetaclasses;
    }

    @objid ("23d9d4aa-326e-4393-8468-0aa99e41446b")
    @Override
    public AbstractDiagram actionPerformed(ModelElement diagramContext, String diagramName, String diagramDescription) {
        IModelFactory modelFactory = ModelFactory.getFactory(diagramContext);
        AbstractDiagram diagram = (AbstractDiagram) modelFactory.createElement(this.metaclass, diagramContext, diagramContext.getMClass().getDependency("Product"));
        if (this.stereotype != null) {
            diagram.getExtension().add(this.stereotype);
        }
        
        diagram.setName(diagramName);
        try {
            diagram.putNoteContent("ModelerModule", "description", diagramDescription);
        } catch (ExtensionNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Set new diagram style
        final IDiagramService diagramService = Modelio.getInstance().getDiagramService();
        final IStyleHandle styleHandle = diagramService.getStyle(this.style);
        if (styleHandle != null) {
            try (IDiagramHandle handle = diagramService.getDiagramHandle(diagram)) {
                handle.getDiagramNode().setStyle(styleHandle);
                handle.save();
            }
        }
        return diagram;
    }

}
