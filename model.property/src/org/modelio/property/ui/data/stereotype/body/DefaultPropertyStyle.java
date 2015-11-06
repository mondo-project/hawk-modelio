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
                                    

package org.modelio.property.ui.data.stereotype.body;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.swt.graphics.Color;

@objid ("a92f5865-69f9-45f4-bc08-6b034f52c398")
public class DefaultPropertyStyle implements IPropertyStyle {
    @objid ("76f1b7ea-4f89-4b85-8a1a-ec222b66efce")
    private Color backgroundColor;

    @objid ("fa115331-76c0-4ee5-b4b3-c4ddd7565f23")
    private Color foregroundColor;

    @objid ("b18fe7ba-f6ef-4057-821d-304b2bcc6a89")
    public DefaultPropertyStyle() {
        this.backgroundColor = GUIHelper.COLOR_WHITE;
        this.foregroundColor = GUIHelper.COLOR_BLACK;
    }

    @objid ("9ae4917d-7618-4924-99a1-e0d4ee8792ef")
    @Override
    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    @objid ("6c893929-b0cd-4328-bb27-73428cce3a18")
    @Override
    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }

    @objid ("4e494634-cb01-487d-9680-8da0c7128b4d")
    @Override
    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    @objid ("a86195f3-1d26-4e04-a6e8-c476f7ce4271")
    @Override
    public void setForegroundColor(Color color) {
        this.foregroundColor = color;
    }

}
