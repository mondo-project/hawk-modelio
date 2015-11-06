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
                                    

package org.modelio.diagram.editor.activity.elements.objectnode.v0;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.style.ActivityAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmObjectNode when its representation mode is
 * RepresentationMode.SIMPLE
 */
@objid ("2ae197e5-55b6-11e2-877f-002564c97630")
class GmObjectNodeSimpleStyleKeys extends ActivityAbstractStyleKeyProvider {
    @objid ("d24d59b3-55c0-11e2-9337-002564c97630")
     static final StyleKey REPMODE = GmObjectNodeStructuredStyleKeys.REPMODE;

    @objid ("d24d59b5-55c0-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = GmObjectNodeStructuredStyleKeys.FILLCOLOR;

    @objid ("d24d59b7-55c0-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = GmObjectNodeStructuredStyleKeys.FILLMODE;

    @objid ("d24d59b9-55c0-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = GmObjectNodeStructuredStyleKeys.LINECOLOR;

    @objid ("d24d59bb-55c0-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = GmObjectNodeStructuredStyleKeys.LINEWIDTH;

    @objid ("d24d59bd-55c0-11e2-9337-002564c97630")
     static final StyleKey FONT = GmObjectNodeStructuredStyleKeys.FONT;

    @objid ("d24d59bf-55c0-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = GmObjectNodeStructuredStyleKeys.TEXTCOLOR;

    @objid ("d24d59c1-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = GmObjectNodeStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("d24d59c3-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = GmObjectNodeStructuredStyleKeys.SHOWTAGS;

}
