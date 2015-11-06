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
                                    

package org.modelio.core.ui.nsu;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.metamodel.uml.statik.NamespaceUse;

@objid ("1632175e-6c58-4bb7-91b8-ae6e1e7d8c5f")
class TreeContentProvider implements ITreeContentProvider {
    @objid ("398db14b-4e16-4cdb-8441-e331a7e54fda")
    @Override
    public void dispose() {
    }

    @objid ("245c7f16-5e5e-4684-9700-0d07750959c4")
    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    }

    @objid ("5082a9a5-eee8-4532-8aa7-de40afc30aa6")
    @Override
    public Object[] getElements(final Object inputElement) {
        //NamespaceUse use = (NamespaceUse) inputElement;
        return getChildren(inputElement);
    }

    @objid ("47b50189-6c07-4a88-a50f-7c16a3793e13")
    @Override
    public Object[] getChildren(final Object parentElement) {
        if (parentElement instanceof NamespaceUse) {
            NamespaceUse use = (NamespaceUse) parentElement;
            return use.getCause().toArray();
        } else {
            return new Object[0];
        }
    }

    @objid ("5ee7f303-593a-4098-ba2d-a9c4f0ab39e3")
    @Override
    public Object getParent(final Object element) {
        return null;
    }

    @objid ("98fcd5f4-2589-42a5-a2cc-bd806750d2c7")
    @Override
    public boolean hasChildren(final Object element) {
        if (element instanceof NamespaceUse) {
            return ! ((NamespaceUse) element).getCause().isEmpty();
        } else 
            return false;
    }

}
