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
                                    

package org.modelio.diagram.styles.manager;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.NamedStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.core.StyleLoader;
import org.modelio.diagram.styles.core.StyleWriter;
import org.modelio.diagram.styles.plugin.DiagramStyles;

@objid ("85cabc38-1926-11e2-92d2-001ec947c8cc")
public class StyleManager {
    @objid ("85cabc39-1926-11e2-92d2-001ec947c8cc")
    private HashMap<String, NamedStyle> styles;

    @objid ("85cabc3d-1926-11e2-92d2-001ec947c8cc")
    private HashMap<String, Path> files;

    @objid ("dce1a03b-1de4-11e2-8cad-001ec947c8cc")
    private Path projectStyleDir;

    /**
     * Get the list of available named styles
     * @return the names of the available named styles
     */
    @objid ("85cabc41-1926-11e2-92d2-001ec947c8cc")
    public List<String> getAvailableStyles() {
        return new ArrayList<>(this.styles.keySet());
    }

    /**
     * Get a style by name.
     * @param name the name of the named style to get
     * @return the 'name' NamedStyle or null if not found
     */
    @objid ("85cabc48-1926-11e2-92d2-001ec947c8cc")
    public NamedStyle getStyle(String name) {
        return this.styles.get(name);
    }

    /**
     * Create a new named style along with its persistence file.<br>
     * The new file is placed in $PROJECTSPACE/.config/styles directory and named 'name'.style.<br>
     * The cascaded style is FactoryStyle.
     * @param name then name of the style to create
     * @return then newly created NamedStyle or null if the style could not be created. When the style already exists it is simply
     * returned.
     */
    @objid ("85cabc4e-1926-11e2-92d2-001ec947c8cc")
    public NamedStyle createStyle(String name) {
        // check agains't already defined style
        if (getStyle(name) != null) {
            return getStyle(name);
        }
        
        // prepare new file storage
        if (!Files.exists(this.projectStyleDir)) {
            try {
                Files.createDirectories(this.projectStyleDir);
            } catch (IOException e) {
                DiagramStyles.LOG.error(e);
            }
        }
        final Path newStyleFile = this.projectStyleDir.resolve(name + ".style");
        
        // create the new style, based on factory style defaults
        final NamedStyle newStyle = new NamedStyle(name, FactoryStyle.getInstance());
        
        // register the style on the StyleManager
        registerStyle(name, newStyle, newStyleFile);
        
        // write initial storage contents
        save(newStyle);
        return newStyle;
    }

    @objid ("85cabc56-1926-11e2-92d2-001ec947c8cc")
    protected void registerStyle(String name, NamedStyle style, Path storage) {
        this.styles.put(name, style);
        this.files.put(name, storage);
    }

    /**
     * Loads the styles found in the specified directories. The style cache is cleared each time this method is called (ie no
     * accumulation)
     * @param stylesDir
     * the directories where to fetch the style files from.
     */
    @objid ("85cabc5b-1926-11e2-92d2-001ec947c8cc")
    public void reloadStylesIn(Path projectStyleDir) {
        // Clear existing data
        this.projectStyleDir = projectStyleDir;
        this.styles = new HashMap<>();
        this.files = new HashMap<>();
        
        // Load he styles from the given directories
        final HashMap<String, String> deferedBindings = new HashMap<>();
        try (DirectoryStream<Path> styleFiles = Files.newDirectoryStream(projectStyleDir, "*" + DiagramStyles.STYLE_FILE_EXTENSION)) {
        
            for (final Path f : styleFiles) {
                final StyleLoader loader = new StyleLoader();
        
                loader.load(f.toUri().toURL());
                String styleName = loader.getAdminProperties().get(NamedStyle.STYLENAME_ADMINKEY);
                if (styleName == null) {
                    styleName = f.getFileName().toString();
                }
        
                String cascadedStyleName = loader.getAdminProperties().get(NamedStyle.BASESTYLE_ADMINKEY);
        
                if (cascadedStyleName == null) {
                    cascadedStyleName = DiagramStyles.DEFAULT_STYLE_NAME;
                }
        
                if (getStyle(cascadedStyleName) != null) {
                    // cascaded style is already loaded, bind it
                    final NamedStyle namedStyle = new NamedStyle(styleName, loader.getStyleProperties(),
                            getStyle(cascadedStyleName));
                    registerStyle(styleName, namedStyle, f);
        
                } else {
                    // cascaded style is still not loaded,defer its binding, temporarily cascading the style to FactoryStyle
                    final NamedStyle namedStyle = new NamedStyle(styleName, loader.getStyleProperties(), FactoryStyle.getInstance());
                    registerStyle(styleName, namedStyle, f);
                    deferedBindings.put(styleName, cascadedStyleName);
                }
        
            }
        } catch (IOException e) {
            DiagramStyles.LOG.error(DiagramStyles.PLUGIN_ID, e);
        }
        
        // process the deferred cascading bindings
        // the rule is to cascade to the default style if no parent style is loaded (excepted for the default style itself)
        final NamedStyle defaultStyle = getStyle(DiagramStyles.DEFAULT_STYLE_NAME);
        for (final String styleName : deferedBindings.keySet()) {
            if (styleName.equals(DiagramStyles.DEFAULT_STYLE_NAME) == false) {
                final NamedStyle style = getStyle(styleName);
                final NamedStyle cascadeStyle = getStyle(deferedBindings.get(styleName));
                if (cascadeStyle != null) {
                    style.setCascadedStyle(cascadeStyle);
                } else {
                    style.setCascadedStyle(defaultStyle);
                    DiagramStyles.LOG.error(DiagramStyles.PLUGIN_ID,
                            "Cannot set cascade style for '%s' to '%s', replaced by 'default'", styleName, cascadeStyle);
                }
            }
        }
    }

    @objid ("85cabc61-1926-11e2-92d2-001ec947c8cc")
    public StyleManager() {
        this.styles = new HashMap<>();
        this.files = new HashMap<>();
    }

    /**
     * Write the current named style contents to the disk.
     * @param style
     */
    @objid ("85cabc63-1926-11e2-92d2-001ec947c8cc")
    public void save(NamedStyle style) {
        if (this.styles.containsValue(style)) {
            final NamedStyle namedStyle = style;
            final Path file = this.files.get(namedStyle.getName());
        
            final StyleWriter sw = new StyleWriter(file);
            sw.save(namedStyle);
        
        }
    }

    /**
     * Normalizing a style consists in removing from its local definitions the values that are currently the same as the value in
     * cascaded style.
     * @param editedStyle
     */
    @objid ("85cabc67-1926-11e2-92d2-001ec947c8cc")
    public void normalize(NamedStyle style) {
        if (this.styles.containsValue(style)) {
            final NamedStyle s = style;
        
            for (final StyleKey skey : s.getLocalKeys()) {
                final Object localValue = s.getProperty(skey);
                final Object cascadedValue = s.getCascadedStyle().getProperty(skey);
                if (localValue.equals(cascadedValue)) {
                    s.removeProperty(skey);
                }
        
            }
        }
    }

    @objid ("85cabc6b-1926-11e2-92d2-001ec947c8cc")
    public NamedStyle getDefaultStyle() {
        return getStyle(DiagramStyles.DEFAULT_STYLE_NAME);
    }

    /**
     * Create a new named style along with its persistence file.<br>
     * The new file is placed in $PROJECTSPACE/.config/styles directory and named 'name'.style.<br>
     * The cascaded style is the named style "parentStyleName" or default style if "parentStyleName" cannot be resolved.
     * @param name then name of the style to create
     * @param parentStyleName then name of the parent style of the created style. May be null, in which case 'default' style is used as parent
     * for the newly created style.
     * @return then newly created NamedStyle or null if the style could not be created or if it already exists
     */
    @objid ("85cabc6f-1926-11e2-92d2-001ec947c8cc")
    public NamedStyle createStyle(final String name, final String parentStyleName) {
        final NamedStyle newStyle = createStyle(name);
        if (newStyle != null && parentStyleName != null) {
            final IStyle parentStyle = getStyle(parentStyleName);
            newStyle.setCascadedStyle((parentStyle != null) ? parentStyle : getDefaultStyle());
            save(newStyle);
        }
        return newStyle;
    }

    @objid ("85cabc7a-1926-11e2-92d2-001ec947c8cc")
    public NamedStyle createStyle(final String name, final String parentStyleName, final URL styleData) {
        final NamedStyle newStyle = createStyle(name, parentStyleName);
        final StyleLoader loader = new StyleLoader();
        loader.load(styleData);
        
        for (final StyleKey key : loader.getStyleProperties().keySet()) {
            newStyle.setProperty(key, loader.getStyleProperties().get(key));
        }
        
        save(newStyle);
        return newStyle;
    }

}
