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
                                    

package org.modelio.diagram.styles.editingsupport;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.editingsupport.checkbox.CheckboxLabelProvider;
import org.modelio.diagram.styles.editingsupport.color.ColorLabelProvider;
import org.modelio.diagram.styles.editingsupport.combo.EnumComboBoxLabelProvider;
import org.modelio.diagram.styles.editingsupport.element.ElementLabelProvider;
import org.modelio.diagram.styles.editingsupport.font.FontLabelProvider;
import org.modelio.diagram.styles.editingsupport.text.TextLabelProvider;
import org.modelio.diagram.styles.viewer.StyleViewer;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Style key value label provider.
 */
@objid ("85b0826d-1926-11e2-92d2-001ec947c8cc")
public class StyleCellLabelProvider extends ColumnLabelProvider {
    @objid ("85b0826e-1926-11e2-92d2-001ec947c8cc")
    private Map<Class<?>, ColumnLabelProvider> providers = new HashMap<>();

    /**
     * C'tor.
     * @param viewer the viewer.
     */
    @objid ("85b2e4a5-1926-11e2-92d2-001ec947c8cc")
    public StyleCellLabelProvider(StyleViewer viewer) {
        this.providers.put(Boolean.class, new CheckboxLabelProvider(viewer));
        this.providers.put(String.class, new TextLabelProvider(viewer));
        this.providers.put(Color.class, new ColorLabelProvider(viewer));
        this.providers.put(Integer.class, new TextLabelProvider(viewer));
        this.providers.put(Font.class, new FontLabelProvider(viewer));
        this.providers.put(Enum.class, new EnumComboBoxLabelProvider(viewer));
        this.providers.put(MRef.class, new ElementLabelProvider(viewer));
    }

    @objid ("85b2e4a8-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void update(ViewerCell cell) {
        Object element = cell.getElement();
        
        if (element instanceof StyleKey) {
            Class<?> stype = ((StyleKey) element).getType();
            ColumnLabelProvider provider = this.providers.get(stype);
            if (provider != null) {
                provider.update(cell);
            } else if (stype.isEnum()) {
                this.providers.get(Enum.class).update(cell);
            } else
                super.update(cell);
        }
    }

    @objid ("85b2e4ac-1926-11e2-92d2-001ec947c8cc")
    @Override
    public String getToolTipText(Object element) {
        return super.getToolTipText(element);
    }

}
