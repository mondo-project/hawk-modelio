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
                                    

package org.modelio.edition.notes.constraintChooser;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

/**
 * Default label provider for the constraint chooser dialog.
 */
@objid ("26d79eee-186f-11e2-bc4e-002564c97630")
public class ConstraintChooserLabelProvider extends LabelProvider {
    @objid ("26d79eef-186f-11e2-bc4e-002564c97630")
    @Override
    public Image getImage(Object element) {
        if (element instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent)element;
            return ModuleI18NService.getModuleImage(moduleComponent);
        } else if (element instanceof Stereotype) {
            Stereotype stereotype = (Stereotype) element;
            Image image = ModuleI18NService.getIcon(stereotype.getOwner().getOwnerModule(), stereotype);
            if (image != null) {
                return image;
            }
        }
        return MetamodelImageService.getIcon(Metamodel.getMClass(Constraint.class));
    }

    @objid ("26d79ef5-186f-11e2-bc4e-002564c97630")
    @Override
    public String getText(Object element) {
        if (element instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent) element;
            return moduleComponent.getName(); // TODO get label
        } else if (element instanceof Stereotype) {
            StringBuffer constraintTypeLabel = new StringBuffer();
            Stereotype stereotype = (Stereotype) element;
            String label = ModuleI18NService.getLabel(stereotype);
            if (!"".equals(label)) {
                constraintTypeLabel.append(label);
            } else {
                constraintTypeLabel.append(stereotype.getName());
            }
            return constraintTypeLabel.toString();
        } else {
            return EditionNotes.I18N.getString("Constraint");
        }
    }

}
