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
                                    

package org.modelio.property.stereotype.chooser;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.ui.UIImages;

/**
 * Default label provider for the note chooser dialog.
 */
@objid ("a4375516-7c8e-42ce-b70f-198f1ddfddab")
public class StereotypeChooserLabelProvider extends LabelProvider {
    @objid ("ca5efc3a-fad2-4e37-9d7c-6c04a195bcd8")
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
            return UIImages.DOT;
        }
        return null;
    }

    @objid ("b2d7dece-82e3-4ee4-b1ff-ac73c2396a9d")
    @Override
    public String getText(Object element) {
        if (element instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent) element;
            return ModuleI18NService.getLabel(moduleComponent);
        } else if (element instanceof Stereotype) {
            StringBuffer noteTypeLabel = new StringBuffer();
            Stereotype stereotype = (Stereotype) element;
        
            noteTypeLabel.append("<<");
            String label = ModuleI18NService.getLabel(stereotype);
            if (!"".equals(label)) {
                noteTypeLabel.append(label);
            } else {
                noteTypeLabel.append(stereotype.getName());
            }
            noteTypeLabel.append(">>");
            
            return noteTypeLabel.toString();
        } else {
            return element.toString();
        }
    }

}
