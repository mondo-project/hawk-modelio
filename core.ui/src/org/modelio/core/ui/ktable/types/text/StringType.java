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
                                    

package org.modelio.core.ui.ktable.types.text;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import de.kupzog.ktable.renderers.TextCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.types.PropertyType;

@objid ("8df3bf32-c068-11e1-8c0a-002564c97630")
public class StringType extends PropertyType {
    @objid ("8df3bf33-c068-11e1-8c0a-002564c97630")
    public StringType(boolean acceptNullValue) {
        super(acceptNullValue);
    }

    @objid ("0abf9449-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return new TextCellEditor();
    }

    /**
     * Renderer used to display editable text fields
     */
    @objid ("0abfbb5b-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        TextCellRenderer textRenderer = new TextCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        textRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        
        textRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return textRenderer;
    }

}
