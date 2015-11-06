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
                                    

package org.modelio.diagram.editor.state.elements.statediagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.state.style.StateAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmStateDiagramView} style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("f59c659f-55b6-11e2-877f-002564c97630")
public final class StateDiagramViewStructuredStyleKeys extends StateAbstractStyleKeyProvider {
    /**
     * Fill color.
     */
    @objid ("81ad2c9c-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("STATEDIAGRAMVIEW_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Lines color.
     */
    @objid ("81ad2c9f-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("STATEDIAGRAMVIEW_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("81ad2ca2-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("STATEDIAGRAMVIEW_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("81ad2ca5-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("STATEDIAGRAMVIEW_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("81ad2ca8-55c2-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("STATEDIAGRAMVIEW_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("81ad2cab-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("STATEDIAGRAMVIEW_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("81ad2cae-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("STATEDIAGRAMVIEW_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("81ad2cb1-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("STATEDIAGRAMVIEW_SHOWTAGS", MetaKey.SHOWTAGS);

}
