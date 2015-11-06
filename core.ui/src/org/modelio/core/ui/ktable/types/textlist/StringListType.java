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
                                    

package org.modelio.core.ui.ktable.types.textlist;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.types.PropertyType;

@objid ("8df238aa-c068-11e1-8c0a-002564c97630")
public class StringListType extends PropertyType {
    @objid ("a57ddead-c068-11e1-8c0a-002564c97630")
    private String title = "";

    @objid ("a57ddeb1-c068-11e1-8c0a-002564c97630")
    private String message = "";

    @objid ("a57ddeb5-c068-11e1-8c0a-002564c97630")
    private String detailedMessage = "";

    @objid ("8df238ae-c068-11e1-8c0a-002564c97630")
    private int size = 0;

    @objid ("8df238af-c068-11e1-8c0a-002564c97630")
    public StringListType(boolean acceptNullValue, String title, String message, int size, String detailedMessage) {
        super(acceptNullValue);
        this.title = title;
        this.message = message;
        this.size = size;
        this.detailedMessage = detailedMessage;
    }

    @objid ("8df238bb-c068-11e1-8c0a-002564c97630")
    public String getTitle() {
        return this.title;
    }

    @objid ("8df238bf-c068-11e1-8c0a-002564c97630")
    public String getDetailedMessage() {
        return this.detailedMessage;
    }

    @objid ("8df238c3-c068-11e1-8c0a-002564c97630")
    public String getMessage() {
        return this.message;
    }

    @objid ("8df3bf25-c068-11e1-8c0a-002564c97630")
    public int getSize() {
        return this.size;
    }

    @objid ("0adae52c-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return new StringListCellEditor(getTitle(), getMessage(), getSize(), getDetailedMessage());
    }

    /**
     * Renderer used to display editable text list fields
     */
    @objid ("0adae531-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        DefaultCellRenderer textListRenderer = new TextListCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        textListRenderer.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
        textListRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return textListRenderer;
    }

}
