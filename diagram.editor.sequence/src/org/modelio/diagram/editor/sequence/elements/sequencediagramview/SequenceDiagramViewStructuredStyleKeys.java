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
                                    

package org.modelio.diagram.editor.sequence.elements.sequencediagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.sequence.style.SequenceAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmSequenceDiagramView} style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("d9917136-55b6-11e2-877f-002564c97630")
public final class SequenceDiagramViewStructuredStyleKeys extends SequenceAbstractStyleKeyProvider {
    /**
     * Fill color.
     */
    @objid ("4fee7e69-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("SEQUENCEDIAGRAMVIEW_FILLCOLOR",
                                                            MetaKey.FILLCOLOR);

    /**
     * Fill mode.
     */
    @objid ("4fee7e6c-55c2-11e2-9337-002564c97630")
    public static final StyleKey FILLMODE = createStyleKey("SEQUENCEDIAGRAMVIEW_FILLMODE", MetaKey.FILLMODE);

    /**
     * Lines color.
     */
    @objid ("4fee7e6f-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("SEQUENCEDIAGRAMVIEW_LINECOLOR",
                                                            MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("4fee7e72-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("SEQUENCEDIAGRAMVIEW_LINEWIDTH",
                                                            MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("4fee7e75-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("SEQUENCEDIAGRAMVIEW_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("4fee7e78-55c2-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("SEQUENCEDIAGRAMVIEW_TEXTCOLOR",
                                                            MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("4fee7e7b-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("SEQUENCEDIAGRAMVIEW_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("4fee7e7e-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("SEQUENCEDIAGRAMVIEW_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("4fee7e81-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("SEQUENCEDIAGRAMVIEW_SHOWTAGS", MetaKey.SHOWTAGS);

}
