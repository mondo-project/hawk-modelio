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
 * Style keys for the stereotype image representation mode.
 * 
 * @author cmarin
 */
@objid ("d98e6422-55b6-11e2-877f-002564c97630")
public class SequenceDiagramViewImageStyleKeys extends SequenceAbstractStyleKeyProvider {
    @objid ("506a170b-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = SequenceDiagramViewStructuredStyleKeys.FONT;

    @objid ("506a170d-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = SequenceDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("506a170f-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = SequenceDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("506a1711-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = SequenceDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("506a1713-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = SequenceDiagramViewStructuredStyleKeys.SHOWTAGS;

}
