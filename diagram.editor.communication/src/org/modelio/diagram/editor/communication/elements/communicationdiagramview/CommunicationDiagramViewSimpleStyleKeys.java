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
import org.modelio.diagram.styles.core.StyleKey;

/**
 * {@link GmCommunicationDiagramView} style keys for the simple representation mode.
 * 
 * @author cmarin
 */
@objid ("7a35d232-55b6-11e2-877f-002564c97630")
public class CommunicationDiagramViewSimpleStyleKeys extends CommunicationAbstractStyleKeyProvider {
    @objid ("9cc4b5ca-55c1-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = CommunicationDiagramViewStructuredStyleKeys.FILLCOLOR;

    @objid ("9cc4b5cc-55c1-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = CommunicationDiagramViewStructuredStyleKeys.LINECOLOR;

    @objid ("9cc4b5ce-55c1-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = CommunicationDiagramViewStructuredStyleKeys.LINEWIDTH;

    @objid ("9cc4dcda-55c1-11e2-9337-002564c97630")
     static final StyleKey FONT = CommunicationDiagramViewStructuredStyleKeys.FONT;

    @objid ("9cc4dcdc-55c1-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = CommunicationDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("9cc503ea-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = CommunicationDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("9cc503ec-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = CommunicationDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("9cc52afa-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = CommunicationDiagramViewStructuredStyleKeys.SHOWTAGS;

}
