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
                                    

package org.modelio.edition.notes.noteChooser;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.ui.UIImages;

/**
 * Default label provider for the note chooser dialog.
 */
@objid ("26e38593-186f-11e2-bc4e-002564c97630")
public class NoteChooserLabelProvider extends LabelProvider {
    @objid ("26e38594-186f-11e2-bc4e-002564c97630")
    @Override
    public Image getImage(Object element) {
        if (element instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent)element;
            return ModuleI18NService.getModuleImage(moduleComponent);
        } else if (element instanceof NoteType) {
            return MetamodelImageService.getIcon(Metamodel.getMClass(Note.class));
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

    @objid ("26e3859a-186f-11e2-bc4e-002564c97630")
    @Override
    public String getText(Object element) {
        if (element instanceof ModuleComponent) {
            ModuleComponent moduleComponent = (ModuleComponent) element;
            String label = ModuleI18NService.getLabel(moduleComponent);
            if (!"".equals(label)) {
                return label;
            } else {
                return moduleComponent.getName();
            }
        } else if (element instanceof NoteType) {
            StringBuffer noteTypeLabel = new StringBuffer();
            NoteType noteType = (NoteType) element;
        
            String label = ModuleI18NService.getLabel(noteType);
            if (!"".equals(label)) {
                noteTypeLabel.append(label);
            } else {
                noteTypeLabel.append(noteType.getName());
            }
        
            return noteTypeLabel.toString();
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
