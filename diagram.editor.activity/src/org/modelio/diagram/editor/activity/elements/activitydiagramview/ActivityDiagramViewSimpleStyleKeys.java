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
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmActivityDiagramView} style keys for the simple representation mode.
 * 
 * @author cmarin
 */
@objid ("299b0e32-55b6-11e2-877f-002564c97630")
public class ActivityDiagramViewSimpleStyleKeys extends ActivityAbstractStyleKeyProvider {
    @objid ("d15453b4-55c0-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = ActivityDiagramViewStructuredStyleKeys.FILLCOLOR;

    @objid ("d15453b6-55c0-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = ActivityDiagramViewStructuredStyleKeys.FILLMODE;

    @objid ("d155da4a-55c0-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = ActivityDiagramViewStructuredStyleKeys.LINECOLOR;

    @objid ("d155da4c-55c0-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = ActivityDiagramViewStructuredStyleKeys.LINEWIDTH;

    @objid ("d155da4e-55c0-11e2-9337-002564c97630")
     static final StyleKey FONT = ActivityDiagramViewStructuredStyleKeys.FONT;

    @objid ("d155da50-55c0-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = ActivityDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("d155da52-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = ActivityDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("d155da54-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = ActivityDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("d155da56-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = ActivityDiagramViewStructuredStyleKeys.SHOWTAGS;

}
