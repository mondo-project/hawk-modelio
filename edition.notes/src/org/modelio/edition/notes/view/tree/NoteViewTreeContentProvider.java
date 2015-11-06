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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

/**
 * Content provider for the annotation view.
 */
@objid ("26fb52dc-186f-11e2-bc4e-002564c97630")
public class NoteViewTreeContentProvider implements ITreeContentProvider {
    @objid ("26fb52de-186f-11e2-bc4e-002564c97630")
    @Override
    public Object[] getElements(Object object) {
        List<Object> objects = new ArrayList<>();
        
        if (object instanceof ModelElement) {
            ModelElement me = (ModelElement) object;
            // Extern Notes
            for (ExternDocument ex : me.getDocument()) {
                if (! ex.getType().isIsHidden()) {
                    objects.add(ex);
                }
            }
        
        
        
            // Notes
            for (Note note : me.getDescriptor()) {
                if (!note.getModel().isIsHidden()) {
                    objects.add(note);
                }
            }
        
            // Constraints
            for (Constraint constraint : me.getConstraintDefinition()) {
                // if there are stereotypes and only hidden ones, the constraint is hidden
                if (constraint.getExtension().size() > 0) {
                    for (Stereotype stereotype : constraint.getExtension()) {
                        if (!stereotype.isIsHidden()) {
                            objects.add(constraint);
                            break;
                        }
        
                    }
                } else {
                    // not stereotype => visible constraint
                    objects.add(constraint);
                }
            }
        }
        return objects.toArray();
    }

    @objid ("26fb52e6-186f-11e2-bc4e-002564c97630")
    @Override
    public void dispose() {
        // Nothing to dispose
    }

    @objid ("26fb52e9-186f-11e2-bc4e-002564c97630")
    @Override
    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        // Nothing to change
    }

    @objid ("26fb52ef-186f-11e2-bc4e-002564c97630")
    @Override
    public Object[] getChildren(Object parent) {
        return Collections.EMPTY_LIST.toArray();
    }

    @objid ("26fb52f7-186f-11e2-bc4e-002564c97630")
    @Override
    public Object getParent(Object child) {
        if (child instanceof ModelElement && !((ModelElement) child).isDeleted()) {
            return ((ModelElement) child).getCompositionOwner();
        } else {
            return null;
        }
    }

    @objid ("26fb52fc-186f-11e2-bc4e-002564c97630")
    @Override
    public boolean hasChildren(Object parent) {
        if (parent instanceof ModelElement) {
            ModelElement me = (ModelElement) parent;
            return (me.getDocument().size() + me.getDescriptor().size() + me.getConstraintDefinition().size()) > 0;
        }
        return false;
    }

}
