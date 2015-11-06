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
                                    

package org.modelio.diagram.elements.plugin;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

@objid ("810cc71f-1dec-11e2-8cad-001ec947c8cc")
public class DColorRegistry {
    @objid ("0d79bf91-6304-4b54-b597-0d32730ad269")
    public Map<String, Color> colors = new HashMap<>();

    @objid ("a002a021-7955-41e7-b25d-d86571379d04")
    private Display display;

    @objid ("810cc725-1dec-11e2-8cad-001ec947c8cc")
    public DColorRegistry(Display display) {
        this.display = display;
    }

    @objid ("810cc728-1dec-11e2-8cad-001ec947c8cc")
    public Color getColor(RGB rgb) {
        String key = rgb.toString();
        if (this.colors.get(key) == null) {
            this.colors.put(key, new Color(this.display, rgb));
        }
        return this.colors.get(key);
    }

    @objid ("810cc72d-1dec-11e2-8cad-001ec947c8cc")
    public synchronized void cleanCache() {
        for (String key : this.colors.keySet()) {
            this.colors.get(key).dispose();
        }
        this.colors.clear();
    }

    @objid ("810cc730-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void finalize() throws Throwable {
        cleanCache();
        super.finalize();
    }

}
