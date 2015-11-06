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
                                    

package org.modelio.property.ui.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagType;

/**
 * Content provider for the annotation view.
 */
@objid ("8fac8b30-c068-11e1-8c0a-002564c97630")
public class TreeContentProvider implements ITreeContentProvider {
    @objid ("52b8f9d4-86aa-4848-8db5-d524a6aa3fc6")
    private boolean displayHiddenTags = false;

    @objid ("078cffa8-179f-11e2-aa0d-002564c97630")
    private IMModelServices modelService;

    @objid ("33c2aabd-297b-4230-b767-f3700ab8c06c")
    private ModelElement inputElement;

    @objid ("8fac8b32-c068-11e1-8c0a-002564c97630")
    @Override
    public Object[] getElements(Object object) {
        if (object == null) {
            return Collections.EMPTY_LIST.toArray();
        }
        
        List<Object> elements = new ArrayList<>();
        
        if (object instanceof Element) {
            // first child is the element itself
            elements.add(((Element)object).getMClass());
        }
        
        // for ModelElement add stereotypes and modules
        if (object instanceof ModelElement) {
            // then comes the modules
            elements.addAll(getContributingModules((ModelElement) object));
        }
        return elements.toArray();
    }

    @objid ("8faeec49-c068-11e1-8c0a-002564c97630")
    @Override
    public void dispose() {
        // Nothing to dispose
    }

    @objid ("8faeec4c-c068-11e1-8c0a-002564c97630")
    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
        // Nothing to do
        this.inputElement = (ModelElement) (newInput instanceof ModelElement ? newInput : null);
    }

    @objid ("8faeec55-c068-11e1-8c0a-002564c97630")
    @Override
    public Object[] getChildren(Object parent) {
        if (parent instanceof ModuleComponent && this.inputElement != null) {
            List<Stereotype> ret = new ArrayList<>();
            for (Stereotype stereotype : this.inputElement.getExtension()) {
                if (parent.equals(stereotype.getModule())) {
                    ret.add(stereotype);
                }
            }
            return ret.toArray(); 
        }
        return Collections.EMPTY_LIST.toArray();
    }

    @objid ("8faeec5d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getParent(Object child) {
        return null;
    }

    @objid ("8faeec63-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean hasChildren(Object parent) {
        if (parent instanceof ModuleComponent && this.inputElement != null) {
            return getChildren(parent).length > 0;
        } else if (parent instanceof Stereotype) {
            return getChildren(parent).length > 0;
        }
        return false;
    }

    /**
     * Returns the list of the modules that 'contribute' to the model element annotations (ie tagged values based on
     * metaclass reference instead of stereotype).<br/> A module is a 'contributor' when:
     * <ul>
     * <li>it defines at least one tagtype applicable on the model element</li>
     * <li>it is started</li>
     * </ul>
     * @param element
     * @return a collection of Modules
     */
    @objid ("8faeec69-c068-11e1-8c0a-002564c97630")
    private Collection<ModuleComponent> getContributingModules(final ModelElement element) {
        Set<ModuleComponent> modules = new TreeSet<>(new Comparator<ModuleComponent>() {
        
            @Override
            public int compare(ModuleComponent o1, ModuleComponent o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        
        if (this.modelService != null) {
            for (TagType tagType : this.modelService.findTagTypes(".*", ".*", element.getMClass())) {
                MetaclassReference ownerReference = tagType.getOwnerReference();
                if (ownerReference != null && displayTagType(tagType)) {
                    modules.add(ownerReference.getOwnerProfile().getOwnerModule());
                }
            }
        }
        
        for (Stereotype stereotype : element.getExtension()) {
            modules.add(stereotype.getModule());
        }
        return modules;
    }

    /**
     * @param modelService the Modelio model services.
     */
    @objid ("89f044c9-e712-4816-898c-9b865c4c2838")
    public void setModelService(IMModelServices modelService) {
        this.modelService = modelService;
    }

    @objid ("44a42f81-5bb3-445d-b07c-b62345961fa6")
    protected boolean displayTagType(TagType tagType) {
        return this.displayHiddenTags || !tagType.isIsHidden();
    }

    @objid ("a0e9b073-9554-45c6-9b6c-47185d233745")
    void setShowHiddenMdaElements(boolean show) {
        this.displayHiddenTags = show;
    }

}
