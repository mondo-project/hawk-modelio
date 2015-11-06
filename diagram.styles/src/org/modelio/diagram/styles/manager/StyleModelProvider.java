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
                                    

package org.modelio.diagram.styles.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.mdl;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * The StyleModelProvider is a IContentProvider suited for the StyleViewer.<br>
 * It returns the subset of StyleKeys obtained by filtering the full set of existing keys with the 'keyfilter' object. If
 * 'keyfilter' is null, all the available keys are returned. (TableLabelProvider, IColorProvider)
 */
@objid ("85cd1e88-1926-11e2-92d2-001ec947c8cc")
public class StyleModelProvider implements ITreeContentProvider {
    @objid ("85cd1ea7-1926-11e2-92d2-001ec947c8cc")
    private boolean isEditable;

    @mdl.prop
    @objid ("85cd1e8a-1926-11e2-92d2-001ec947c8cc")
    private IStyle styleData;

    @mdl.propgetter
    public IStyle getStyleData() {
        // Automatically generated method. Please do not modify this code.
        return this.styleData;
    }

    @mdl.prop
    @objid ("85cd1e91-1926-11e2-92d2-001ec947c8cc")
    private Collection<StyleKey> keyfilter;

    @mdl.propgetter
    public Collection<StyleKey> getKeyfilter() {
        // Automatically generated method. Please do not modify this code.
        return this.keyfilter;
    }

    @objid ("85cd1e9c-1926-11e2-92d2-001ec947c8cc")
    private final Map<String, List<StyleKey>> cache = new HashMap<>();

    @objid ("85cd1ea3-1926-11e2-92d2-001ec947c8cc")
    private static final Object[] NO_OBJECTS = new Object[0];

    @objid ("cbb91b9b-718d-4485-8208-cdabaf23b067")
    private ICoreSession coreSession;

    @objid ("85cd1ea8-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Object[] getElements(Object inputElement) {
        // If there is no styledata, return 'no objects'
        if (this.styleData == null) {
            return NO_OBJECTS;
        }
        
        // If there is a filter, the filter defines the set of keys
        // otherwise let use the whole set of known StyleKeys instances
        List<StyleKey> displayedKeys;
        if (this.keyfilter != null) {
            displayedKeys = new ArrayList<>(this.keyfilter);
        } else {
            displayedKeys = new ArrayList<>(StyleKey.getInstances());
        }
        
        // Build the category cache
        this.cache.clear();
        for (StyleKey skey : displayedKeys) {
            List<StyleKey> keys = this.cache.get(skey.getCategory());
            if (keys == null) {
                this.cache.put(skey.getCategory(), keys = new ArrayList<>());
            }
            keys.add(skey);
        }
        
        // Depending on the existence of several categories, return either the categories or the stylekey.
        // This results in a tree structure when there are several categories and a flat structure when there is only one or no
        // category
        if (this.cache.keySet().size() == 1) {
            return this.cache.get(this.cache.keySet().iterator().next()).toArray();
        } else {
            ArrayList<String> categories = new ArrayList<>(this.cache.keySet());
            Collections.sort(categories);
            return categories.toArray();
        }
    }

    @objid ("85cd1eaf-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void dispose() {
        this.cache.clear();
        this.styleData = null;
        this.keyfilter = null;
    }

    @objid ("85cd1eb2-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        if (viewer != null && !viewer.getControl().isDisposed()) {
            viewer.refresh();
        }
    }

    /**
     * C'tor.
     * @param styleData the edited style data
     * @param session the modeling session
     * @param keyfilter the keys subset to edit
     * @param isEditable whether or not this style can be edited
     */
    @objid ("85cd1eb8-1926-11e2-92d2-001ec947c8cc")
    public StyleModelProvider(IStyle styleData, ICoreSession session, Collection<StyleKey> keyfilter, boolean isEditable) {
        this.styleData = styleData;
        this.coreSession = session;
        this.keyfilter = keyfilter;
        this.isEditable = isEditable;
    }

    /**
     * C'tor.
     * @param editedStyle the edited style data
     * @param session the modeling session
     */
    @objid ("85cd1ec0-1926-11e2-92d2-001ec947c8cc")
    public StyleModelProvider(IStyle editedStyle, ICoreSession session) {
        this(editedStyle, session, null, false);
    }

    @objid ("85cd1ec4-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Object[] getChildren(Object parentElement) {
        // A StyleKey has no children
        if (parentElement instanceof StyleKey) {
            return null;
        }
        
        // A String represents a category
        if (parentElement instanceof String) {
            return this.cache.get(parentElement).toArray();
        }
        return null;
    }

    @objid ("85cd1ecc-1926-11e2-92d2-001ec947c8cc")
    @Override
    public Object getParent(Object element) {
        if (element instanceof StyleKey && this.cache.keySet().size() > 1) {
            return ((StyleKey) element).getCategory();
        } else {
            return null;
        }
    }

    @objid ("85cd1ed1-1926-11e2-92d2-001ec947c8cc")
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof StyleKey) {
            return false;
        } else if (element instanceof String && this.cache.containsKey(element)) {
            return true;
        }
        return false;
    }

    /**
     * @return <code>true</code> if the style is editable else <code>false</code>.
     */
    @objid ("85cd1ed7-1926-11e2-92d2-001ec947c8cc")
    public boolean isEditable() {
        return this.isEditable;
    }

    /**
     * @return the modeling session
     */
    @objid ("564d9cff-8ddd-44c4-bead-7a9a53b3bfe4")
    public ICoreSession getSession() {
        return this.coreSession;
    }

}
