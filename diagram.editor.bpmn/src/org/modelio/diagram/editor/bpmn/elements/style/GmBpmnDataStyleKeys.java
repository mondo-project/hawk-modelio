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
                                    

package org.modelio.diagram.editor.bpmn.elements.style;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.datainput.GmBpmnDataInput;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * Style key provider for {@link GmBpmnDataInput}.
 */
@objid ("6220f70c-55b6-11e2-877f-002564c97630")
public class GmBpmnDataStyleKeys extends BpmnAbstractStyleKeyProvider {
    /**
     * Connection routing mode
     */
    @objid ("72aad12b-55c1-11e2-9337-002564c97630")
    public static final StyleKey CONNECTIONROUTER = createStyleKey("DATA_CONNECTIONROUTER",
                                                                   MetaKey.CONNECTIONROUTER);

    /**
     * Fill color
     */
    @objid ("72aad12e-55c1-11e2-9337-002564c97630")
    public static final StyleKey FILLCOLOR = createStyleKey("DATA_FILLCOLOR", MetaKey.FILLCOLOR);

    /**
     * Fill mode
     */
    @objid ("72aad131-55c1-11e2-9337-002564c97630")
    public static final StyleKey FILLMODE = createStyleKey("DATA_FILLMODE", MetaKey.FILLMODE);

    /**
     * Line color
     */
    @objid ("72aad134-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINECOLOR = createStyleKey("DATA_LINECOLOR", MetaKey.LINECOLOR);

    /**
     * Line width
     */
    @objid ("72aad137-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINEWIDTH = createStyleKey("DATA_LINEWIDTH", MetaKey.LINEWIDTH);

    /**
     * Text font
     */
    @objid ("72aad13a-55c1-11e2-9337-002564c97630")
    public static final StyleKey FONT = createStyleKey("DATA_FONT", MetaKey.FONT);

    /**
     * Text color
     */
    @objid ("72aad13d-55c1-11e2-9337-002564c97630")
    public static final StyleKey TEXTCOLOR = createStyleKey("DATA_TEXTCOLOR", MetaKey.TEXTCOLOR);

    @objid ("72ac57cb-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWSTEREOTYPES = createStyleKey("DATA_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    @objid ("72ac57cd-55c1-11e2-9337-002564c97630")
    public static final StyleKey SHOWTAGS = createStyleKey("DATA_SHOWTAGS", MetaKey.SHOWTAGS);

    /**
     * Line style
     */
    @objid ("72ac57cf-55c1-11e2-9337-002564c97630")
    public static final StyleKey LINEPATTERN = createStyleKey("DATA_LINEPATTERN", MetaKey.LINEPATTERN);

}
