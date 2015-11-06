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
                                    

package org.modelio.core.ui.ktable.types;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;

@objid ("8df0b204-c068-11e1-8c0a-002564c97630")
public abstract class PropertyType implements IPropertyType {
    @objid ("8df0b205-c068-11e1-8c0a-002564c97630")
    protected boolean acceptNullValue = true;

    @objid ("8df0b207-c068-11e1-8c0a-002564c97630")
    public PropertyType(boolean acceptNullValue) {
        super();
        this.acceptNullValue = acceptNullValue;
    }

    @objid ("8df0b20a-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean acceptNullValue() {
        return this.acceptNullValue;
    }

    /**
     * Renderer used to display 'anything'
     */
    @objid ("0aaaac31-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        TextCellRenderer objectRenderer = null;
        
        objectRenderer = new TextCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        objectRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        objectRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return objectRenderer;
    }

    /**
     * Default type is not editable
     */
    @objid ("6ff196b3-cb5e-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return null;
    }

}
