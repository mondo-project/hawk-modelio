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
import org.modelio.api.module.paramEdition.ParameterModel;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.ui.UIColor;

/**
 * Label provider for the parameter scope column.
 */
@objid ("c1396286-3bb4-49b4-bb1d-a1b1391b70d1")
class ScopeLabelProvider extends ColumnLabelProvider {
    @objid ("eb06b7c0-ff1f-487e-b03d-5fd25c27821e")
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

    @objid ("bb5c584a-f4af-4aa5-8233-950f88d23e62")
    @Override
    public String getText(Object element) {
        if (element instanceof ParameterModel) {
            ParameterModel property = (ParameterModel) element;
            return property.isLocked() ? AppProjectConf.I18N.getString("ParameterSection.Server") : AppProjectConf.I18N.getString("ParameterSection.Local");
        } else {
            return "";
        }
    }

}
