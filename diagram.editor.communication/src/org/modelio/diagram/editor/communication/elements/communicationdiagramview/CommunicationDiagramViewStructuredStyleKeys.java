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
                                    

package org.modelio.diagram.editor.communication.elements.communicationdiagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.communication.style.CommunicationAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmCommunicationDiagramView} style keys for the standard structured mode.
 * 
 * @author cmarin
 */
@objid ("7a3758ca-55b6-11e2-877f-002564c97630")
public final class CommunicationDiagramViewStructuredStyleKeys extends CommunicationAbstractStyleKeyProvider {
    /**
     * Fill color.
     */
    @objid ("9cb0b89b-55c1-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("COMMUNICATIONDIAGRAMVIEW_FILLCOLOR",
                                                            MetaKey.FILLCOLOR);

    /**
     * Lines color.
     */
    @objid ("9cb0b89e-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("COMMUNICATIONDIAGRAMVIEW_LINECOLOR",
                                                            MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("9cb0dfab-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("COMMUNICATIONDIAGRAMVIEW_LINEWIDTH",
                                                            MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("9cb0dfae-55c1-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("COMMUNICATIONDIAGRAMVIEW_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("9cb106bb-55c1-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("COMMUNICATIONDIAGRAMVIEW_TEXTCOLOR",
                                                            MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("9cb106be-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("COMMUNICATIONDIAGRAMVIEW_SHOWNAME",
                                                           MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("9cb12dcb-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("COMMUNICATIONDIAGRAMVIEW_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("9cb154d9-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("COMMUNICATIONDIAGRAMVIEW_SHOWTAGS",
                                                           MetaKey.SHOWTAGS);

}
