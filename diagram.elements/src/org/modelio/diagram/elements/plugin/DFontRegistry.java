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
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

@objid ("810cc733-1dec-11e2-8cad-001ec947c8cc")
public class DFontRegistry {
    @objid ("f35cf63f-b556-49b5-8f89-22be77e9e03b")
    public Map<String, Font> fonts = new HashMap<>();

    @objid ("ce208a24-7184-4ee8-a412-7d0aa705f6a6")
    private Display display;

    @objid ("810cc739-1dec-11e2-8cad-001ec947c8cc")
    public DFontRegistry(Display display) {
        this.display = display;
    }

    @objid ("810cc73c-1dec-11e2-8cad-001ec947c8cc")
    public Font getFont(FontData fontdata) {
        String key = fontdata.toString();
        if (this.fonts.get(key) == null) {
            this.fonts.put(key, new Font(this.display, fontdata));
        }
        return this.fonts.get(key);
    }

    @objid ("810cc741-1dec-11e2-8cad-001ec947c8cc")
    public synchronized void cleanCache() {
        for (String key : this.fonts.keySet()) {
            this.fonts.get(key).dispose();
        }
        this.fonts.clear();
    }

    @objid ("810cc744-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void finalize() throws Throwable {
        cleanCache();
        super.finalize();
    }

}
