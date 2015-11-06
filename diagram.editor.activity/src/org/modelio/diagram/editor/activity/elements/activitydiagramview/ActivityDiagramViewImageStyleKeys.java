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
 * Style keys for the stereotype image representation mode.
 * 
 * @author cmarin
 */
@objid ("299b0e1a-55b6-11e2-877f-002564c97630")
public class ActivityDiagramViewImageStyleKeys extends ActivityAbstractStyleKeyProvider {
    @objid ("d15453aa-55c0-11e2-9337-002564c97630")
     static final StyleKey FONT = ActivityDiagramViewStructuredStyleKeys.FONT;

    @objid ("d15453ac-55c0-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = ActivityDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("d15453ae-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = ActivityDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("d15453b0-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = ActivityDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("d15453b2-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = ActivityDiagramViewStructuredStyleKeys.SHOWTAGS;

}
