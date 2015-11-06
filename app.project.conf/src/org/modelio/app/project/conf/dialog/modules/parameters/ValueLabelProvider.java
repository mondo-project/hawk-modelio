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
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.modelio.api.module.paramEdition.BoolParameterModel;
import org.modelio.api.module.paramEdition.EnumParameterModel;
import org.modelio.api.module.paramEdition.ParameterModel;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.ui.UIColor;

/**
 * Label provider for the parameter value column.
 */
@objid ("e7aa73dd-3a39-11e2-90eb-002564c97630")
class ValueLabelProvider extends ColumnLabelProvider {
    @objid ("f87ebfc4-33bf-4037-a6e8-faaadb9dfa14")
    private static final Image CHECKED = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectConf.PLUGIN_ID, "icons/checked.gif").createImage();

    @objid ("0b8a012c-c57f-465c-937a-4436ccb1fd56")
    private static final Image UNCHECKED = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectConf.PLUGIN_ID, "icons/unchecked.gif").createImage();

    @objid ("e7aa73e3-3a39-11e2-90eb-002564c97630")
    @Override
    public String getToolTipText(Object element) {
        return super.getToolTipText(element);
    }

    @objid ("e7aa73e9-3a39-11e2-90eb-002564c97630")
    @Override
    public String getText(Object element) {
        if (element instanceof BoolParameterModel) {
            return ""; //$NON-NLS-1$
        } else if (element instanceof EnumParameterModel) {
            return ((EnumParameterModel) element).getLabel(((ParameterModel) element).getStringValue());
        } else if (element instanceof ParameterModel) {
            ParameterModel property = (ParameterModel) element;
            return property.getStringValue();
        } else {
            return ""; //$NON-NLS-1$
        }
    }

    @objid ("e7aa73ee-3a39-11e2-90eb-002564c97630")
    @Override
    public Image getImage(Object element) {
        if (element instanceof BoolParameterModel) {
            BoolParameterModel property = (BoolParameterModel) element;
            if (Boolean.parseBoolean(property.getStringValue()))
                return CHECKED;
            else
                return UNCHECKED;
        } else {
            // Default case
            return null;
        }
    }

    @objid ("b60132f1-9f85-4721-a1a0-a89f42d0c5a1")
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
