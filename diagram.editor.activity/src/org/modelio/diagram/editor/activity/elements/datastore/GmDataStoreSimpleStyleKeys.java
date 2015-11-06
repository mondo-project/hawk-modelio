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
                                    

package org.modelio.diagram.editor.activity.elements.datastore;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.activity.style.ActivityAbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;

/**
 * This class provides the StyleKey constants for a GmDataStore when its representation mode is
 * RepresentationMode.SIMPLE
 */
@objid ("2a2f10cc-55b6-11e2-877f-002564c97630")
public class GmDataStoreSimpleStyleKeys extends ActivityAbstractStyleKeyProvider {
    @objid ("d1cb586c-55c0-11e2-9337-002564c97630")
     static final StyleKey REPMODE = createStyleKey("DATASTORE_REPMODE", MetaKey.REPMODE);

    @objid ("d1cb586e-55c0-11e2-9337-002564c97630")
     static final StyleKey FILLCOLOR = createStyleKey("DATASTORE_FILLCOLOR", MetaKey.FILLCOLOR);

    @objid ("d1cb5870-55c0-11e2-9337-002564c97630")
     static final StyleKey FILLMODE = createStyleKey("DATASTORE_FILLMODE", MetaKey.FILLMODE);

    @objid ("d1cb5872-55c0-11e2-9337-002564c97630")
     static final StyleKey LINECOLOR = createStyleKey("DATASTORE_LINECOLOR", MetaKey.LINECOLOR);

    @objid ("d1cb5874-55c0-11e2-9337-002564c97630")
     static final StyleKey LINEWIDTH = createStyleKey("DATASTORE_LINEWIDTH", MetaKey.LINEWIDTH);

    @objid ("d1cb5876-55c0-11e2-9337-002564c97630")
     static final StyleKey FONT = createStyleKey("DATASTORE_FONT", MetaKey.FONT);

    @objid ("d1cb5878-55c0-11e2-9337-002564c97630")
     static final StyleKey TEXTCOLOR = createStyleKey("DATASTORE_TEXTCOLOR", MetaKey.TEXTCOLOR);

    @objid ("d1cb587a-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWSTEREOTYPES = createStyleKey("DATASTORE_SHOWSTEREOTYPES",
                                                           MetaKey.SHOWSTEREOTYPES);

    @objid ("d1cb587c-55c0-11e2-9337-002564c97630")
     static final StyleKey SHOWTAGS = createStyleKey("DATASTORE_SHOWTAGS", MetaKey.SHOWTAGS);

}
