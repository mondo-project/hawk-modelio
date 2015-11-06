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
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmSequenceDiagramView} style keys for the simple representation mode.
 * 
 * @author cmarin
 */
@objid ("d98fea8d-55b6-11e2-877f-002564c97630")
public class SequenceDiagramViewSimpleStyleKeys extends SequenceAbstractStyleKeyProvider {
    @objid ("506a1715-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = SequenceDiagramViewStructuredStyleKeys.FILLCOLOR;

    @objid ("506a1717-55c2-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = SequenceDiagramViewStructuredStyleKeys.FILLMODE;

    @objid ("506a1719-55c2-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = SequenceDiagramViewStructuredStyleKeys.LINECOLOR;

    @objid ("506b9daa-55c2-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = SequenceDiagramViewStructuredStyleKeys.LINEWIDTH;

    @objid ("506b9dac-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = SequenceDiagramViewStructuredStyleKeys.FONT;

    @objid ("506b9dae-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = SequenceDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("506b9db0-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = SequenceDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("506b9db2-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = SequenceDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("506b9db4-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = SequenceDiagramViewStructuredStyleKeys.SHOWTAGS;

}
