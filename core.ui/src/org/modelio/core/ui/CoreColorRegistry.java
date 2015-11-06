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
                                    

package org.modelio.core.ui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

@objid ("34bf29dd-186d-11e2-92d2-001ec947c8cc")
public class CoreColorRegistry {
    @objid ("26ee07a5-186f-11e2-92d2-001ec947c8cc")
     static ColorRegistry colors = null;

    @objid ("26ee07a6-186f-11e2-92d2-001ec947c8cc")
    public static Color getColor(RGB rgb) {
        if (colors == null) {
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    colors = new ColorRegistry();
                }
            });
        }
        
        String key = rgb.toString();
        if (colors.get(key) == null) {
            colors.put(key, rgb);
            return colors.get(key);
        }
        return colors.get(key);
    }

}
