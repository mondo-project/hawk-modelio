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
 * Style keys for the stereotype image representation mode.
 * 
 * @author cmarin
 */
@objid ("7a35d21a-55b6-11e2-877f-002564c97630")
public class CommunicationDiagramViewImageStyleKeys extends CommunicationAbstractStyleKeyProvider {
    @objid ("9cc4409b-55c1-11e2-9337-002564c97630")
     static final StyleKey FONT = CommunicationDiagramViewStructuredStyleKeys.FONT;

    @objid ("9cc4409d-55c1-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = CommunicationDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("9cc4409f-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = CommunicationDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("9cc467aa-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = CommunicationDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("9cc467ac-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = CommunicationDiagramViewStructuredStyleKeys.SHOWTAGS;

}
