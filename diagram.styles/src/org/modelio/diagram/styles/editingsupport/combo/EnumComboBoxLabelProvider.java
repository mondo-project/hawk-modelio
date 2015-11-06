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
                                    

package org.modelio.diagram.styles.editingsupport.combo;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.diagram.styles.viewer.StyleViewer;

@objid ("859fd1d1-1926-11e2-92d2-001ec947c8cc")
public class EnumComboBoxLabelProvider extends ColumnLabelProvider {
    @objid ("859fd1d2-1926-11e2-92d2-001ec947c8cc")
     StyleViewer viewer;

    @objid ("859fd1d3-1926-11e2-92d2-001ec947c8cc")
    public EnumComboBoxLabelProvider(StyleViewer viewer) {
        this.viewer = viewer;
    }

    @objid ("859fd1d6-1926-11e2-92d2-001ec947c8cc")
    @Override
    public String getText(Object element) {
        StyleKey skey = (StyleKey) element;
        Enum<?> value = this.viewer.getEditedStyle().getProperty(skey);
        if (value != null)
            return DiagramStyles.I18N.getString(value.getClass().getSimpleName() + "." + value.toString());
        else
            return super.getText(element);
    }

}
