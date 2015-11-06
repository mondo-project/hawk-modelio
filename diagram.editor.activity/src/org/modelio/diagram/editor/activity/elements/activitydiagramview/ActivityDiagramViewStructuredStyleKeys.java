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
                                    

package org.modelio.diagram.editor.activity.elements.activitydiagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.style.ActivityAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmActivityDiagramView} style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("299c94d5-55b6-11e2-877f-002564c97630")
public final class ActivityDiagramViewStructuredStyleKeys extends ActivityAbstractStyleKeyProvider {
    /**
     * Fill color.
     */
    @objid ("d155da58-55c0-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("ACTIVITYDIAGRAMVIEW_FILLCOLOR",
                                                            MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("d155da5b-55c0-11e2-9337-002564c97630")
    public static final StyleKey FILLMODE = createStyleKey("ACTIVITYDIAGRAMVIEW_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("d155da5e-55c0-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("ACTIVITYDIAGRAMVIEW_LINECOLOR",
                                                            MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("d155da61-55c0-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("ACTIVITYDIAGRAMVIEW_LINEWIDTH",
                                                            MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("d155da64-55c0-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("ACTIVITYDIAGRAMVIEW_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("d155da67-55c0-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("ACTIVITYDIAGRAMVIEW_TEXTCOLOR",
                                                            MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("d155da6a-55c0-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("ACTIVITYDIAGRAMVIEW_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("d155da6d-55c0-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("ACTIVITYDIAGRAMVIEW_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("d155da70-55c0-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("ACTIVITYDIAGRAMVIEW_SHOWTAGS", MetaKey.SHOWTAGS);

}
