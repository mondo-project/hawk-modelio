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
                                    

package org.modelio.linkeditor.view.background.typeselection;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Label and image provider for the type selection dialog.
 * 
 * @author fpoyer
 */
@objid ("1b960462-5e33-11e2-b81d-002564c97630")
class TypeSelectionLabelProvider extends LabelProvider {
    @objid ("1b98658f-5e33-11e2-b81d-002564c97630")
    @Override
    public Image getImage(final Object element) {
        // Stereotype
        if (element instanceof Stereotype) {
            return this.getStereotypeImage((Stereotype) element);
        } else if (element instanceof ModuleComponent) {
            return this.getModuleImage((ModuleComponent) element);
        } else if (element instanceof MClass) {
            return MetamodelImageService.getIcon((MClass) element, null);
        }
        return null;
    }

    @objid ("1b986595-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("unchecked")
    @Override
    public String getText(final Object element) {
        if (element instanceof Stereotype) {
            final StringBuffer stereotypeLabel = new StringBuffer();
            stereotypeLabel.append("\u00AB "); // "? " : '<<'
        
            final String label = ModuleI18NService.getLabel((Stereotype) element);
            if (label != null && !label.isEmpty()) {
                stereotypeLabel.append(label);
            } else {
                stereotypeLabel.append(((Stereotype) element).getName());
            }
        
            stereotypeLabel.append(" \u00BB"); // "? " : '>>'
            return stereotypeLabel.toString();
        } else if (element instanceof Class && MObject.class.isAssignableFrom((Class<?>) element)) {
            return Metamodel.getMClass((Class<? extends MObject>) element).getName();
        } else if (element instanceof ModuleComponent) {
            return ModuleI18NService.getLabel((ModuleComponent) element);
        }
        return element.toString();
    }

    @objid ("1b98659c-5e33-11e2-b81d-002564c97630")
    private Image getModuleImage(final ModuleComponent moduleComponent) {
        Image icon = null;
        
        // If it is valid, get the module image
        if (moduleComponent.isValid()) {
            icon = ModuleI18NService.getModuleImage(moduleComponent);
        }
        return icon;
    }

    @objid ("1b9865a2-5e33-11e2-b81d-002564c97630")
    private Image getStereotypeImage(final Stereotype stereotype) {
        Image image = null;
        
        // If it is valid, get the stereotype image
        if (stereotype.isValid()) {
            image = ModuleI18NService.getIcon(stereotype.getOwner().getOwnerModule(), stereotype);
        }
        return image;
    }

}
