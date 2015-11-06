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

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

@objid ("2f28a6ed-186d-11e2-92d2-001ec947c8cc")
public class CoreFontRegistry {
    @objid ("3bc99bac-186d-11e2-92d2-001ec947c8cc")
     static Map<String, Font> fonts = new HashMap<>();

    /**
     * Get a font given a device and font data which describes the desired font's appearance.
     * <p>
     * {@link #getFont(FontData[])} should better be used to be fully compatible on Unix.
     * @param fontdata describes the desired font (must not be null)
     * @return the matching font
     */
    @objid ("3bc99bad-186d-11e2-92d2-001ec947c8cc")
    public static Font getFont(FontData fontdata) {
        String key = fontdata.toString();
        if (fonts.get(key) == null) {
            fonts.put(key, new Font(Display.getDefault(), fontdata));
        }
        return fonts.get(key);
    }

    /**
     * Get a font given an array of font data which describes the desired font's appearance.
     * @param fontdatas the array of FontData that describes the desired font (must not be null)
     * @return the matching font
     */
    @objid ("3bc99bae-186d-11e2-92d2-001ec947c8cc")
    public static Font getFont(final FontData[] fontdatas) {
        StringBuilder keyBuilder = new StringBuilder(50);
        for (FontData f : fontdatas)
            keyBuilder.append(f.toString());
        
        final String key = keyBuilder.toString();
        if (fonts.get(key) == null) {
            fonts.put(key, new Font(Display.getDefault(), fontdatas));
        }
        return fonts.get(key);
    }

    /**
     * Get a font with the same appearance as the given one but with the given style flags to add.
     * @param font the base font
     * @param styleToAdd the style flags to add
     * @return the matching font.
     */
    @objid ("3bc99baf-186d-11e2-92d2-001ec947c8cc")
    public static Font getModifiedFont(final Font font, final int styleToAdd) {
        FontData[] fontdatas = font.getFontData();
        for (FontData d : fontdatas) {
            d.setStyle(d.getStyle() | styleToAdd);
        }
        return getFont(fontdatas);
    }

    /**
     * Get a font with the same appearance as the given one but with a scaled size.
     * @param font the base font
     * @param scaleFactor the scale factor to apply.
     * @return the matching font.
     */
    @objid ("3bc99bb0-186d-11e2-92d2-001ec947c8cc")
    public static Font getScaledFont(final Font font, final float scaleFactor) {
        FontData[] fontdatas = font.getFontData();
        for (FontData d : fontdatas) {
            d.setHeight((int) (d.getHeight() * scaleFactor));
        }
        return getFont(fontdatas);
    }

}
