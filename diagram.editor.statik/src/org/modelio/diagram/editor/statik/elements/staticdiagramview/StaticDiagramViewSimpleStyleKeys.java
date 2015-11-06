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
import org.modelio.diagram.styles.core.StyleKey;

/**
 * GmClass style keys for the simple representation mode.
 * 
 * @author cmarin
 */
@objid ("36d1831b-55b7-11e2-877f-002564c97630")
public class StaticDiagramViewSimpleStyleKeys extends StaticAbstractStyleKeyProvider {
    @objid ("a7ed5978-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = StaticDiagramViewStructuredStyleKeys.FILLCOLOR;

    @objid ("a7ed597a-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = StaticDiagramViewStructuredStyleKeys.LINECOLOR;

    @objid ("a7ed597c-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = StaticDiagramViewStructuredStyleKeys.LINEWIDTH;

    @objid ("a7ed597e-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = StaticDiagramViewStructuredStyleKeys.FONT;

    @objid ("a7ed5980-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = StaticDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("a7ed5982-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = StaticDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("a7ed5984-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = StaticDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("a7ed5986-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = StaticDiagramViewStructuredStyleKeys.SHOWTAGS;

}
