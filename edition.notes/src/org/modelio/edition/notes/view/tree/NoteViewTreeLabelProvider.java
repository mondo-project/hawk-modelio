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
                                    

package org.modelio.edition.notes.view.tree;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.editors.richnote.helper.RichNoteLabelProvider;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

/**
 * Label provider of the annotation tree.
 */
@objid ("26fb5302-186f-11e2-bc4e-002564c97630")
public class NoteViewTreeLabelProvider extends LabelProvider {
    @objid ("26fb5304-186f-11e2-bc4e-002564c97630")
    @Override
    public Image getImage(Object element) {
        if (element instanceof ExternDocument) {
            return RichNoteLabelProvider.getIcon((ExternDocument) element);
        } else if (element instanceof Element) {
            return ElementImageService.getIcon((Element) element);
        }
        return null;
    }

    @objid ("26fb530a-186f-11e2-bc4e-002564c97630")
    @Override
    public String getText(Object element) {
        if (element instanceof Note) {
            Note note = (Note) element;
            NoteType type = note.getModel();
            if (type != null) {
                return ModuleI18NService.getLabel(type);
            } else {
                return EditionNotes.I18N.getString("Note");
            }
        } else if (element instanceof ExternDocument) {
            final ExternDocument document = (ExternDocument) element;
            final ExternDocumentType type = document.getType();
            if (type != null) {
                String name = document.getName();
                if (!name.isEmpty()) {
                    name += " ";
                }
                
                final String label = ModuleI18NService.getLabel(type);
                return label.isEmpty() ? name + "[" + type.getName() + "]" : name + "[" + label + "]";
                
            } else {
                return EditionNotes.I18N.getString("ExternDocument");
            }
        } else if (element instanceof Constraint) {
            final Constraint constraint = (Constraint)element;
        
            // If the name is filled we use it for the constraint label
            final String name = constraint.getName();
            if (name != null && !name.isEmpty()) {
                return name;
            }
        
            // If the name is not filled and if there is at least on stereotype on the constraint
            // we use the first stereotype name as a label.
            final List<Stereotype> stereotypes = constraint.getExtension();
            if (stereotypes.size() > 0) {
                final Stereotype stereotype = getFirstSelected(stereotypes);
        
                if (stereotype != null) {
                    return ModuleI18NService.getLabel(stereotype);
                }
            }
        
            // If there is no name and no stereotype we set "Constraint" as a label
            return EditionNotes.I18N.getString("Constraint");
        }
        return element.toString();
    }

    @objid ("26fb5310-186f-11e2-bc4e-002564c97630")
    private static Stereotype getFirstSelected(List<Stereotype> stereotypes) {
        for (Stereotype stereotype : stereotypes) {
            ModuleComponent module = stereotype.getOwner().getOwnerModule();
            if (module != null) {
                return stereotype;
            }
        }
        return null;
    }

}
