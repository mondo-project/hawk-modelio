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
                                    

package org.modelio.diagram.editor.activity.elements.centralbuffer.v0;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.style.ActivityAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmCentralBuffer when its representation mode is
 * RepresentationMode.SIMPLE
 */
@objid ("29ed73f3-55b6-11e2-877f-002564c97630")
class GmCentralBufferSimpleStyleKeys extends ActivityAbstractStyleKeyProvider {
    @objid ("d1a3ac4d-55c0-11e2-9337-002564c97630")
    public static final StyleKey REPMODE = createStyleKey("CENTRALBUFFER_REPMODE", MetaKey.REPMODE);

    @objid ("d1a532c9-55c0-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("CENTRALBUFFER_FILLCOLOR", MetaKey.FILLCOLOR);

    @objid ("d1a532cb-55c0-11e2-9337-002564c97630")
    public static final StyleKey FILLMODE = createStyleKey("CENTRALBUFFER_FILLMODE", MetaKey.FILLMODE);

    @objid ("d1a532cd-55c0-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("CENTRALBUFFER_LINECOLOR", MetaKey.LINECOLOR);

    @objid ("d1a532cf-55c0-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("CENTRALBUFFER_LINEWIDTH", MetaKey.LINEWIDTH);

    @objid ("d1a532d1-55c0-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("CENTRALBUFFER_FONT", MetaKey.FONT);

    @objid ("d1a532d3-55c0-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("CENTRALBUFFER_TEXTCOLOR", MetaKey.TEXTCOLOR);

    @objid ("d1a532d5-55c0-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("CENTRALBUFFER_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    @objid ("d1a532d7-55c0-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("CENTRALBUFFER_SHOWTAGS", MetaKey.SHOWTAGS);

}
