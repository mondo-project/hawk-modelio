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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IParameterGroupModel;
import org.modelio.api.module.paramEdition.ParameterModel;
import org.modelio.ui.UIColor;

/**
 * Label provider for the parameter name column.
 */
@objid ("e7a0ee55-3a39-11e2-90eb-002564c97630")
class NameLabelProvider extends ColumnLabelProvider {
    @objid ("e7a0ee5d-3a39-11e2-90eb-002564c97630")
    @Override
    public String getText(Object element) {
        if (element instanceof IModule) {
            IModule module = (IModule) element;
            return module.getLabel();
        } else if (element instanceof IParameterGroupModel) {
            IParameterGroupModel groupModel = (IParameterGroupModel) element;
            return groupModel.getLabel();
        } else if (element instanceof ParameterModel) {
            ParameterModel property = (ParameterModel) element;
            return property.getLabel();
        } else {
            return super.getText(element);
        }
    }

    @objid ("e7a0ee62-3a39-11e2-90eb-002564c97630")
    @Override
    public Image getImage(Object element) {
        if (element instanceof IModule) {
            IModule module = (IModule) element;
            return module.getModuleImage();
        } else {
            return super.getImage(element);
        }
    }

    @objid ("44311194-aed7-4bf3-a800-a8291120753b")
    @Override
    public Color getForeground(Object element) {
        if (element instanceof ParameterModel) {
            ParameterModel property = (ParameterModel) element;
            if (property.isLocked()) {
                return UIColor.NONMODIFIABLE_ELEMENT;
            } else {
                return UIColor.MODIFIABLE_ELEMENT;
            }
        }
        return super.getForeground(element);
    }

}
