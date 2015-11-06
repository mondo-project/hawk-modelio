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
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.modelio.core.ui.CoreColorRegistry;
import org.modelio.core.ui.CoreFontRegistry;
import org.modelio.diagram.styles.plugin.DiagramStyles;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class loads the properties of a Style from a textual property file.<br>
 * It loads two kinds of properties:
 * <ul>
 * <li>style properties ie known StyleKeys defined in the property file.</li>
 * <li>admin properties ie properties whose key does not appear to be a StyleKey</li>
 * </ul>
 * Properties, either style or admin properties, are only available after a load() operation. Each load() call resets
 * the loaded properties, meaning that there is no accumulation of loaded properties when calling load() several times. <br>
 * <p>
 * Color and Font allocation:<br>
 * The loader will allocate Colors and Fonts from the global CoreColorRegistry and CoreFontRegistry.
 */
@objid ("85859844-1926-11e2-92d2-001ec947c8cc")
public class StyleLoader {
    @objid ("2765207e-1927-11e2-92d2-001ec947c8cc")
    private HashMap<String, String> adminProperties;

    @objid ("85859846-1926-11e2-92d2-001ec947c8cc")
    private Map<StyleKey, Object> styleProperties;

    /**
     * A Map of already loaded fonts.
     */
    @objid ("8585984a-1926-11e2-92d2-001ec947c8cc")
     Map<String, Font> fontMap;

    /**
     * A map of already loaded colors.
     */
    @objid ("8585984f-1926-11e2-92d2-001ec947c8cc")
     Map<String, Color> colorMap;

    /**
     * Constructor.
     */
    @objid ("85859858-1926-11e2-92d2-001ec947c8cc")
    public StyleLoader() {
        this.fontMap = new HashMap<>();
        this.colorMap = new HashMap<>();
    }

    /**
     * Load property values from the default settings resource file.
     * @param url the url of the file to load the style from.
     */
    @objid ("8587fa4f-1926-11e2-92d2-001ec947c8cc")
    public void load(URL url) {
        this.styleProperties = new HashMap<>();
        this.adminProperties = new HashMap<>();
        
        final Properties loadedValues = new Properties();
        try (InputStream inputStream = url.openStream()){
            loadedValues.load(inputStream);
        } catch (IOException e) {
            DiagramStyles.LOG.error(e);
        }
        
        // Process raw properties to dispatch StyleKey versus non-StyleKey values
        for (Object entry : loadedValues.keySet()) {
            String k = (String) entry;
            StyleKey sKey = StyleKey.getInstance(k);
            if (sKey != null) {
                try {
                    Object value = StyleLoader.loadValue(loadedValues, sKey);
                    if (value != null) {
                        this.styleProperties.put(sKey, value);
                    }
                } catch (IOException e) {
                    DiagramStyles.LOG.error(e);
                }
            } else {
                this.adminProperties.put(k, loadedValues.getProperty(k));
            }
        }
    }

    /**
     * This method tries to extract a value for the StyleKey 'sKey' from the raw properties 'loadedValues' that have
     * been read from a property file.<br>
     * When no value can directly be extracted from 'loadedValue' the method tries to analyze 'sKey' as a MetaKey to
     * guess a reasonable default value (asking the defaults provider if some). If nothing work, it returns null.
     * @param loadedValues the raw loaded values from the property file
     * @param sKey the StyleKey for which the method is expected to fetch a value
     * @return the value for 'sKey' or null if none.
     * @throws java.io.IOException If the parsing of the value failed.
     */
    @objid ("8587fa53-1926-11e2-92d2-001ec947c8cc")
    private static Object loadValue(Properties loadedValues, StyleKey sKey) throws IOException {
        String data = loadedValues.getProperty(sKey.getId(), null);
        
        // if the fetched value is a variable, resolve it
        if (data != null && data.startsWith("$")) {
            data = loadedValues.getProperty(data.trim());
        }
        
        MetaKey metaKey = sKey.getMetakey();
        
        // When there is no data value, we try to use a default value resolved
        // by analyzing the metakey as the metakey holds the key semantic
        // allowing for a guess of the default value.
        // However, if there is no metakey, no guess is possible and an
        // exception is thrown.
        
        if (data != null) {
            Class<?> type;
            if (metaKey != null) {
                // Parse data using the metakey type
                type = metaKey.getType();
            } else {
                // Parse data using the style key type.
                type = sKey.getType();
            }
        
            try {
                return StyleLoader.parseData(data, type);
            } catch (RuntimeException e) {
                throw new IOException("Parsing of '" +
                                      data +
                                      "' default value of '" +
                                      type.getSimpleName() +
                                      "' typed '" +
                                      sKey.getId() +
                                      "' style key failed: " +
                                      e.toString(), e);
            }
        } else {
        
            // May happen when loading a 'complement' property file, 
            // ie a property file that do not define all possible values
            return null;
        }
    }

    @objid ("8587fa59-1926-11e2-92d2-001ec947c8cc")
    private static Color makeColor(String data) {
        final String[] vals = data.split(" ");
        final int red = Integer.valueOf(vals[0]).intValue();
        final int green = Integer.valueOf(vals[1]).intValue();
        final int blue = Integer.valueOf(vals[2]).intValue();
        
        RGB rgb = new RGB(red, green, blue);
        return CoreColorRegistry.getColor(rgb);
    }

    @objid ("8587fa5e-1926-11e2-92d2-001ec947c8cc")
    private static Object makeFont(String data) {
        final String[] vals = data.split(",");
        
        if (vals.length == 3) {
            final String name = vals[0].trim();
            final int height = Integer.valueOf(vals[1].trim()).intValue();
            final int style = Integer.valueOf(vals[2].trim()).intValue();
        
            FontData fd = new FontData(name, height, style);
            return CoreFontRegistry.getFont(fd);
        } else {
            return null;
        }
    }

    @objid ("8587fa62-1926-11e2-92d2-001ec947c8cc")
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object parseData(String data, Class<?> type) {
        if (type == Color.class) {
            return StyleLoader.makeColor(data);
        }
        if (type == Font.class) {
            return StyleLoader.makeFont(data.trim());
        }
        if (type == Boolean.class) {
            return new Boolean(data);
        }
        
        if (type == Integer.class) {
            return new Integer(data);
        }
        
        if (type == String.class) {
            return data;
        }
        
        if (type == MRef.class) {
            if (data.trim().isEmpty())
                return null;
            else
                return new MRef(data);
        }
        
        if (type.isEnum()) {
            return Enum.valueOf((Class<? extends Enum>) type, data.trim());
        }
        
        
        
        DiagramStyles.LOG.warning( "StyleLoader.parseData()  missing converter for '%s'", type.getName());
        return null;
    }

    @objid ("8587fa74-1926-11e2-92d2-001ec947c8cc")
    public Map<StyleKey, Object> getStyleProperties() {
        return this.styleProperties;
    }

    @objid ("8587fa7b-1926-11e2-92d2-001ec947c8cc")
    public Map<String, String> getAdminProperties() {
        return this.adminProperties;
    }

}
