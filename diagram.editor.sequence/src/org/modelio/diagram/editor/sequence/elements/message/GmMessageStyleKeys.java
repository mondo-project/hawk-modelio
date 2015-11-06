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
                                    

package org.modelio.diagram.editor.sequence.elements.message;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.sequence.style.SequenceAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * StyleKey provider for the GmMessage class
 */
@objid ("d955ef08-55b6-11e2-877f-002564c97630")
public class GmMessageStyleKeys extends SequenceAbstractStyleKeyProvider {
    /**
     * Line color
     */
    @objid ("4feb712a-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("MESSAGE_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Line width
     */
    @objid ("4feb712d-55c2-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("MESSAGE_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font
     */
    @objid ("4feb7130-55c2-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("MESSAGE_FONT", MetaKey.FONT);

    /**
     * Stereotype display mode: text / icon / text+icon
     */
    @objid ("4feb7133-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("MESSAGE_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values
     */
    @objid ("4feb7136-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("MESSAGE_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Show information flows
     */
    @objid ("4feb7139-55c2-11e2-9337-002564c97630")
    public static final StyleKey SHOWFLOWS = createStyleKey("MESSAGE_SHOWFLOWS", MetaKey.SHOWINFORMATIONFLOWS);

    /**
     * Information flows
     */
    @objid ("d9577578-55b6-11e2-877f-002564c97630")
    public static class InfoFlows extends SequenceAbstractStyleKeyProvider {
        /**
         * Text color.
         */
        @objid ("4feb713c-55c2-11e2-9337-002564c97630")
         static final StyleKey FLOWTEXTCOLOR = createStyleKey("MESSAGE_FLOWS_TEXTCOLOR",
                                                             MetaKey.InformationItemGroup.INFTEXTCOLOR);

        /**
         * Font
         */
        @objid ("4feb713f-55c2-11e2-9337-002564c97630")
         static final StyleKey FLOWFONT = createStyleKey("MESSAGE_FLOWS_FONT",
                                                        MetaKey.InformationItemGroup.INFFONT);

        /**
         * Stereotype display mode.
         */
        @objid ("4feb7142-55c2-11e2-9337-002564c97630")
         static final StyleKey FLOWSHOWSTEREOTYPES = createStyleKey("MESSAGE_FLOWS_SHOWSTEREOTYPES",
                                                                   MetaKey.InformationItemGroup.INFSHOWSTEREOTYPES);

        /**
         * Display tagged values.
         */
        @objid ("4feb7145-55c2-11e2-9337-002564c97630")
         static final StyleKey FLOWSHOWTAGS = createStyleKey("MESSAGE_FLOWS_SHOWTAGS",
                                                            MetaKey.InformationItemGroup.INFSHOWTAGS);

    }

}
