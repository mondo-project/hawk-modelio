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
                                    

package org.modelio.diagram.styles.core;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class writes the properties of a NamedStyle to a textual property file.<br>
 * It writes two kinds of properties:
 * <ul>
 * <li>style properties ie known {@link StyleKey StyleKeys} defined in the {@link NamedStyle Style} .</li>
 * <li>admin properties about the cascading scheme</li>
 * </ul>
 */
@objid ("8587fa82-1926-11e2-92d2-001ec947c8cc")
public class StyleWriter {
    @objid ("276782d6-1927-11e2-92d2-001ec947c8cc")
    private HashMap<String, String> adminProperties;

    @objid ("8587fa84-1926-11e2-92d2-001ec947c8cc")
    private Map<StyleKey, Object> styleProperties;

    @objid ("8587fa88-1926-11e2-92d2-001ec947c8cc")
    private Path file;

    /**
     * Constructor.
     * @param file the file to save into.
     */
    @objid ("8587fa8d-1926-11e2-92d2-001ec947c8cc")
    public StyleWriter(Path file) {
        this.file = file;
    }

    /**
     * Save a style to the file.
     * @param style the style to save.
     */
    @objid ("8587fa91-1926-11e2-92d2-001ec947c8cc")
    public void save(NamedStyle style) {
        final Properties properties = new Properties();
        
        // Write admin properties
        properties.put("stylename", style.getName());
        if (style.getCascadedStyle() instanceof NamedStyle) {
            properties.put("basestyle", ((NamedStyle) style.getCascadedStyle()).getName());
        }
        
        // Write style properties
        for (StyleKey skey : style.getLocalKeys()) {
            String key = skey.getId();
            String value = StyleWriter.formatValue(skey, style.getProperty(skey));
            properties.put(key, value);
        }
        
        // Write the output file
        try {
            Files.createDirectories(this.file.getParent());
            try (OutputStream outputStream = Files.newOutputStream(this.file)) {
                properties.store(outputStream, "");
            }
        } catch (IOException e) {
            DiagramStyles.LOG.error(e);
        }
    }

    /**
     * Serializes a property value to a string.
     */
    @objid ("8587fa95-1926-11e2-92d2-001ec947c8cc")
    private static String formatValue(StyleKey sKey, Object value) {
        Class<?> type = sKey.getType();
        
        if (type == Color.class) {
            RGB rgb = ((Color) value).getRGB();
            return rgb.red + " " + rgb.green + " " + rgb.blue;
        }
        if (type == Font.class) {
            FontData fd = ((Font) value).getFontData()[0];
            return fd.getName() + ", " + fd.getHeight() + ", " + fd.getStyle();
        }
        if (type == Boolean.class) {
            return ((Boolean) value).toString();
        }
        if (type == Integer.class) {
            return ((Integer) value).toString();
        }
        if (type == String.class) {
            return (String) value;
        }
        if (type == MRef.class) {
            if (value == null)
                return "";
            else
                return ((MRef)value).toString();
        }
        
        if (type.isEnum()) {
            return value.toString();
        }
        
        DiagramStyles.LOG.warning("StyleWriter.formatValue(): missing converter for type '%s'",
                type.getName());
        return "not supported type " + type;
    }

}
