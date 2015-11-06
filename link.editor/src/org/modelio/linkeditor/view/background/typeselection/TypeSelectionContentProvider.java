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

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

@objid ("1b960430-5e33-11e2-b81d-002564c97630")
class TypeSelectionContentProvider implements ITreeContentProvider {
    @objid ("1b960431-5e33-11e2-b81d-002564c97630")
    private final TypeSelectionModel model;

    @objid ("1b960432-5e33-11e2-b81d-002564c97630")
    public TypeSelectionContentProvider(final TypeSelectionModel model) {
        this.model = model;
    }

    @objid ("1b960436-5e33-11e2-b81d-002564c97630")
    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        // Nothing to do.
    }

    @objid ("1b96043f-5e33-11e2-b81d-002564c97630")
    @Override
    public void dispose() {
        // Nothing to do.
    }

    @objid ("1b960442-5e33-11e2-b81d-002564c97630")
    @Override
    public boolean hasChildren(final Object element) {
        return (element == Dependency.class || element instanceof ModuleComponent);
    }

    @objid ("1b960449-5e33-11e2-b81d-002564c97630")
    @Override
    public Object getParent(final Object element) {
        return null;
    }

    @objid ("1b960450-5e33-11e2-b81d-002564c97630")
    @Override
    public Object[] getElements(final Object inputElement) {
        List<Object> allTypes = this.model.getAllowedTypes();
        Set<Object> firstLevelTypes = new TreeSet<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                TypeSelectionLabelProvider labelProvider = new TypeSelectionLabelProvider();
                String label1 = labelProvider.getText(o1);
                String label2 = labelProvider.getText(o2);
                return label1.compareTo(label2);
            }
        });
        for (Object obj : allTypes) {
            if (obj instanceof Class) {
                firstLevelTypes.add(obj);
            } else if (obj instanceof Stereotype) {
                firstLevelTypes.add(Dependency.class);
            }
        }
        return firstLevelTypes.toArray();
    }

    @objid ("1b960459-5e33-11e2-b81d-002564c97630")
    @Override
    public Object[] getChildren(final Object parentElement) {
        List<Object> allTypes = this.model.getAllowedTypes();
        if (parentElement == Dependency.class) {
            Set<ModuleComponent> modules = new TreeSet<>(new Comparator<ModuleComponent>() {
                @Override
                public int compare(ModuleComponent o1, ModuleComponent o2) {
                    TypeSelectionLabelProvider labelProvider = new TypeSelectionLabelProvider();
                    String label1 = labelProvider.getText(o1);
                    String label2 = labelProvider.getText(o2);
                    return label1.compareTo(label2);
                }
            });
            // list modules
            for (Object obj : allTypes) {
                if (obj instanceof Stereotype) {
                    Stereotype stereotype = (Stereotype) obj;
                    modules.add(stereotype.getOwner().getOwnerModule());
                }
            }
            return modules.toArray();
        } else if (parentElement instanceof ModuleComponent) {
            Set<Stereotype> stereotypes = new TreeSet<>(new Comparator<Stereotype>() {
                @Override
                public int compare(Stereotype o1, Stereotype o2) {
                    TypeSelectionLabelProvider labelProvider = new TypeSelectionLabelProvider();
                    String label1 = labelProvider.getText(o1);
                    String label2 = labelProvider.getText(o2);
                    return label1.compareTo(label2);
                }
            });
            // list stereotypes
            for (Object obj : allTypes) {
                if (obj instanceof Stereotype) {
                    Stereotype stereotype = (Stereotype) obj;
                    if (stereotype.getOwner().getOwnerModule().equals(parentElement)) {
                        stereotypes.add(stereotype);
                    }
                }
            }
            // sort the result.
            return stereotypes.toArray();
        }
        return null;
    }

}
