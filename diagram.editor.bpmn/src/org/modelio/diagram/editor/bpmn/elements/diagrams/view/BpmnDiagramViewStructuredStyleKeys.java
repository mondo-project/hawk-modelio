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
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * style keys for the standard structured mode.
 */
@objid ("6203f932-55b6-11e2-877f-002564c97630")
public final class BpmnDiagramViewStructuredStyleKeys extends BpmnAbstractStyleKeyProvider {
    /**
     * Fill color.
     */
    @objid ("72819e69-55c1-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("BPMNDIAGRAMVIEW_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Lines color.
     */
    @objid ("72819e6c-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("BPMNDIAGRAMVIEW_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Lines width.
     */
    @objid ("72819e6f-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("BPMNDIAGRAMVIEW_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font.
     */
    @objid ("728324eb-55c1-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("BPMNDIAGRAMVIEW_FONT", MetaKey.FONT);

    /**
     * Text color.
     */
    @objid ("728324ee-55c1-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("BPMNDIAGRAMVIEW_TEXTCOLOR", MetaKey.TEXTCOLOR);

    /**
     * Name display mode: none, simple, qualified, ...
     */
    @objid ("728324f1-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWNAME = createStyleKey("BPMNDIAGRAMVIEW_SHOWNAME", MetaKey.SHOWNAME);

    /**
     * Stereotype display mode.
     */
    @objid ("728324f4-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("BPMNDIAGRAMVIEW_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    /**
     * Show tagged values.
     */
    @objid ("728324f7-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("BPMNDIAGRAMVIEW_SHOWTAGS", MetaKey.SHOWTAGS);

}
