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
                                    

package org.modelio.diagram.editor.state.elements.statediagramview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.state.style.StateAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Style keys for the stereotype image representation mode.
 * 
 * @author cmarin
 */
@objid ("f59adf06-55b6-11e2-877f-002564c97630")
public class StateDiagramViewImageStyleKeys extends StateAbstractStyleKeyProvider {
    @objid ("81aba5ed-55c2-11e2-9337-002564c97630")
     static final StyleKey FONT = StateDiagramViewStructuredStyleKeys.FONT;

    @objid ("81aba5ef-55c2-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = StateDiagramViewStructuredStyleKeys.TEXTCOLOR;

    @objid ("81aba5f1-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWNAME = StateDiagramViewStructuredStyleKeys.SHOWNAME;

    @objid ("81aba5f3-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = StateDiagramViewStructuredStyleKeys.SHOWSTEREOTYPES;

    @objid ("81aba5f5-55c2-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = StateDiagramViewStructuredStyleKeys.SHOWTAGS;

}
