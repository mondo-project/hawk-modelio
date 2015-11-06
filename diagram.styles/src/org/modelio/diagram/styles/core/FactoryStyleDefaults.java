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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;
import org.modelio.diagram.styles.core.StyleKey.FillMode;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey.ShowNameMode;
import org.modelio.diagram.styles.core.StyleKey.ShowStereotypeMode;
import org.modelio.diagram.styles.plugin.DiagramStyles;

/**
 * An implementation of IDefaulValuesProvider used by FactoryStyle as its ultimate value provider.<br>
 * The implementation is based on initialized constants.
 * <p>
 * This implementation must be maintained when MetaKeys set of values is modified !
 * 
 * @author pvlaemyn
 */
@objid ("8551243a-1926-11e2-92d2-001ec947c8cc")
public class FactoryStyleDefaults {
    @objid ("85512446-1926-11e2-92d2-001ec947c8cc")
    private static Integer defaultLineWidth = null;

    @objid ("85512448-1926-11e2-92d2-001ec947c8cc")
    private static Boolean defaultShowTagsMode = null;

    @objid ("8551244a-1926-11e2-92d2-001ec947c8cc")
    private static Integer defaultLineRadius = null;

    @objid ("2769e52a-1927-11e2-92d2-001ec947c8cc")
    private static LinePattern defaultLinePattern = null;

    @objid ("2769e52c-1927-11e2-92d2-001ec947c8cc")
    private static FillMode defaultFillMode = null;

    @objid ("2769e52e-1927-11e2-92d2-001ec947c8cc")
    private static RepresentationMode defaultRepMode = null;

    @objid ("2769e530-1927-11e2-92d2-001ec947c8cc")
    private static ShowNameMode defaultShowNameMode = null;

    @objid ("2769e532-1927-11e2-92d2-001ec947c8cc")
    private static ShowStereotypeMode defaultShowStereotypesMode = null;

    @objid ("2769e534-1927-11e2-92d2-001ec947c8cc")
    private static ConnectionRouterId defaultRoutingMode = ConnectionRouterId.ORTHOGONAL;

    @objid ("8551243c-1926-11e2-92d2-001ec947c8cc")
    private static Color defaultFillColor = null;

    @objid ("8551243d-1926-11e2-92d2-001ec947c8cc")
    private static Color defaultPenColor = null;

    @objid ("8551243e-1926-11e2-92d2-001ec947c8cc")
    private static Color defaultTextColor = null;

    @objid ("8551243f-1926-11e2-92d2-001ec947c8cc")
    private static Font defaultMediumFont = null;

    /**
     * Initialize factory style default values.
     */
    @objid ("8551244c-1926-11e2-92d2-001ec947c8cc")
    public FactoryStyleDefaults() {
        // if static values are not yet initialized
        if (defaultPenColor == null) {
            defaultPenColor = org.eclipse.draw2d.ColorConstants.darkGray;
            defaultTextColor = org.eclipse.draw2d.ColorConstants.black;
            defaultFillColor = org.eclipse.draw2d.ColorConstants.white;
            // defaultSmallFont = new Font(Display.getDefault(), "Arial", 8, 0);
            defaultMediumFont = new Font(Display.getDefault(), "Arial", 10, 0);
            // defaultLargeFont = new Font(Display.getDefault(), "Arial", 12, 0);
            defaultFillMode = StyleKey.FillMode.SOLID;
            defaultLinePattern = LinePattern.LINE_SOLID;
            defaultLineWidth = 1;
            defaultLineRadius = 0;
            defaultRepMode = StyleKey.RepresentationMode.STRUCTURED;
            defaultShowNameMode = StyleKey.ShowNameMode.SIMPLE;
            defaultShowStereotypesMode = StyleKey.ShowStereotypeMode.NONE;
            defaultShowTagsMode = false;
        }
    }

    /**
     * Provide a default value for 'sKey'. The default value resolution is based on Metakey matching if sKey has a MetaKey,otherwise
     * it is based on sKey required type for the value. Note that in this latter case, only a few 'types' support default values
     * @param sKey @return
     */
    @objid ("8551244f-1926-11e2-92d2-001ec947c8cc")
    public static Object getDefaultValue(StyleKey sKey) {
        if (sKey.getMetakey() != null)
            return getDefaultValue(sKey.getMetakey());
        else
            return getDefaultValue(sKey.getType());
    }

    @objid ("85512454-1926-11e2-92d2-001ec947c8cc")
    private static Object getDefaultValue(Class<?> type) {
        if (type == Font.class) {
            return defaultMediumFont;
        }
        // Should not happen if factory.settings properly defined
        DiagramStyles.LOG.warning("FactoryStyleDefaults: cannot guess a default for type '%s'",
                type.getSimpleName());
        return null;
    }

    @objid ("8551245b-1926-11e2-92d2-001ec947c8cc")
    private static Object getDefaultValue(MetaKey metaKey) {
        if (MetaKey.FILLCOLOR.equals(metaKey)) {
            return defaultFillColor;
        }
        
        if (MetaKey.FILLMODE.equals(metaKey)) {
            return defaultFillMode;
        }
        
        if (MetaKey.FONT.equals(metaKey)) {
            return defaultMediumFont;
        }
        
        if (MetaKey.LINECOLOR.equals(metaKey)) {
            return defaultPenColor;
        }
        
        if (MetaKey.LINEWIDTH.equals(metaKey)) {
            return defaultLineWidth;
        }
        
        if (MetaKey.LINERADIUS.equals(metaKey)) {
            return defaultLineRadius;
        }
        
        if (MetaKey.REPMODE.equals(metaKey)) {
            return defaultRepMode;
        }
        
        if (MetaKey.SHOWNAME.equals(metaKey)) {
            return defaultShowNameMode;
        }
        
        if (MetaKey.SHOWSTEREOTYPES.equals(metaKey)) {
            return defaultShowStereotypesMode;
        }
        
        if (MetaKey.SHOWTAGS.equals(metaKey)) {
            return defaultShowTagsMode;
        }
        
        if (MetaKey.TEXTCOLOR.equals(metaKey)) {
            return defaultTextColor;
        }
        
        if (MetaKey.LINEPATTERN.equals(metaKey)) {
            return defaultLinePattern;
        }
        
        if (MetaKey.CONNECTIONROUTER.equals(metaKey)) {
            return defaultRoutingMode;
        }
        
        if (MetaKey.HYPERREFLINK.equals(metaKey)) {
            // No reference by default
            return null;
        }
        
        // Should not happen
        DiagramStyles.LOG.warning("FactoryStyleDefaults(): cannot guess a default value for unknown metakey '%s'", metaKey);
        return null;
    }

}
