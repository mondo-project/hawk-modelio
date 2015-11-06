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
                                    

package org.modelio.app.project.conf.dialog.modules.parameters;

import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IParameterEditionModel;
import org.modelio.api.module.IParameterGroupModel;

@objid ("e7a0ee67-3a39-11e2-90eb-002564c97630")
class ParametersContentProvider implements ITreeContentProvider {
    @objid ("e7a0ee68-3a39-11e2-90eb-002564c97630")
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // Nothing to do
    }

    @objid ("e7a0ee6e-3a39-11e2-90eb-002564c97630")
    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof Collection<?>) {
            return ((Collection<?>) inputElement).toArray();
        }
        return null;
    }

    @objid ("e7a0ee76-3a39-11e2-90eb-002564c97630")
    @Override
    public Object[] getChildren(Object element) {
        if (element instanceof IModule) {
            IParameterEditionModel parametersEditionModel = ((IModule) element).getParametersEditionModel();
            if (parametersEditionModel != null) {
                return parametersEditionModel.getGroups().toArray();
            }
        } else if (element instanceof IParameterGroupModel) {
            return ((IParameterGroupModel) element).getParameters().toArray();
        }
        return null;
    }

    @objid ("e7a0ee7e-3a39-11e2-90eb-002564c97630")
    @Override
    public Object getParent(Object element) {
        return null;
    }

    @objid ("e7a34fb6-3a39-11e2-90eb-002564c97630")
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof IModule) {
            return ((IModule) element).getParametersEditionModel().getGroups().size() > 0;
        } else if (element instanceof IParameterGroupModel) {
            return ((IParameterGroupModel) element).getParameters().size() > 0;
        }
        return false;
    }

    @objid ("e7a34fbc-3a39-11e2-90eb-002564c97630")
    @Override
    public void dispose() {
        // Nothing to dispose
    }

}
