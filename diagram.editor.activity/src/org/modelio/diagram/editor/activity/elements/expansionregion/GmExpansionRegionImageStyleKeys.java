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
                                    

package org.modelio.diagram.editor.activity.elements.expansionregion;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.style.ActivityAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmStructuredactivity when its representation mode is
 * RepresentationMode.IMAGE
 */
@objid ("2a62f205-55b6-11e2-877f-002564c97630")
public class GmExpansionRegionImageStyleKeys extends ActivityAbstractStyleKeyProvider {
    @objid ("d083480a-55c0-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("EXPANSIONREGION_REPMODE", MetaKey.REPMODE);

    @objid ("d083480c-55c0-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("EXPANSIONREGION_FONT", MetaKey.FONT);

    @objid ("d083480e-55c0-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("EXPANSIONREGION_TEXTCOLOR", MetaKey.TEXTCOLOR);

    @objid ("d0834810-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("EXPANSIONREGION_SHOWSTEREOTYPES",
                                                                  MetaKey.SHOWSTEREOTYPES);

    @objid ("d0834812-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("EXPANSIONREGION_SHOWTAGS", MetaKey.SHOWTAGS);

}
