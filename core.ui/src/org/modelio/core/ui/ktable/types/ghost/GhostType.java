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
                                    

package org.modelio.core.ui.ktable.types.ghost;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.ktable.types.PropertyType;

@objid ("8dea9790-c068-11e1-8c0a-002564c97630")
public class GhostType extends PropertyType {
    @objid ("8dea9791-c068-11e1-8c0a-002564c97630")
    public GhostType() {
        super(true);
    }

    /**
     * Ghost renderer
     */
    @objid ("0a7a7334-cb5b-11e1-9165-002564c97630")
    @Override
    public DefaultCellRenderer getRenderer() {
        GhostCellRenderer ghostRenderer = new GhostCellRenderer(DefaultCellRenderer.INDICATION_FOCUS);
        ghostRenderer.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        ghostRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER | SWTX.ALIGN_VERTICAL_CENTER);
        return ghostRenderer;
    }

}
