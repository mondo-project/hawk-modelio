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
                                    

package org.modelio.diagram.editor.bpmn.elements.diagrams.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.style.BpmnAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * GmClass style keys for the stereotype image representation mode.
 */
@objid ("6202727a-55b6-11e2-877f-002564c97630")
public class BpmnDiagramViewImageStyleKeys extends BpmnAbstractStyleKeyProvider {
    @objid ("72819e4b-55c1-11e2-9337-002564c97630")
     static final StyleKey FONT = BpmnDiagramViewStructuredStyleKeys.FONT;

    @objid ("72819e4d-55c1-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = BpmnDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("72819e4f-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = BpmnDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("72819e51-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = BpmnDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("72819e53-55c1-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = BpmnDiagramViewStructuredStyleKeys.SHOWTAGS;

}
