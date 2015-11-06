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
                                    

package org.modelio.diagram.editor.statik.elements.staticdiagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.style.StaticAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmStaticDiagramView} style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("36d1833f-55b7-11e2-877f-002564c97630")
public final class StaticDiagramViewStructuredStyleKeys extends StaticAbstractStyleKeyProvider {
    /**
     * Fill color.
     */
    @objid ("a7ed598a-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("STATICDIAGRAMVIEW_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Lines color.
     */
    @objid ("a7eee00b-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("STATICDIAGRAMVIEW_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("a7eee00e-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("STATICDIAGRAMVIEW_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("a7eee011-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("STATICDIAGRAMVIEW_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("a7eee014-55c2-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("STATICDIAGRAMVIEW_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("a7eee017-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("STATICDIAGRAMVIEW_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("a7eee01a-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("STATICDIAGRAMVIEW_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("a7eee01d-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("STATICDIAGRAMVIEW_SHOWTAGS", MetaKey.SHOWTAGS);

}
