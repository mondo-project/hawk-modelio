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
                                    

package org.modelio.diagram.editor.statik.elements.collabuselink;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.binding.GmBindingLabelGroup;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a {@linkplain CollaborationUse collaboration use} link.
 */
@objid ("348dada1-55b7-11e2-877f-002564c97630")
public class GmCollabUseLink extends GmLink {
    @objid ("348dada5-55b7-11e2-877f-002564c97630")
    private CollaborationUse element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("348dadaa-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("348dadad-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("348dada8-55b7-11e2-877f-002564c97630")
    private static final CollabUseLinkStructuredStyleKeys STRUCTURED_KEYS = new CollabUseLinkStructuredStyleKeys();

    /**
     * Constructor for deserialization.
     */
    @objid ("348dadaf-55b7-11e2-877f-002564c97630")
    public GmCollabUseLink() {
        // Nothing to do.
    }

    /**
     * Creates a GmElementImport.
     * @param diagram The diagram containing the link.
     * @param role The represented element.
     * @param ref The represented element reference. May not be null.
     */
    @objid ("348dadb2-55b7-11e2-877f-002564c97630")
    public GmCollabUseLink(GmAbstractDiagram diagram, CollaborationUse role, MRef ref) {
        super(diagram, ref);
        
        this.element = role;
        
        if (role != null) {
            // Create extensions
        
            addExtension(ExtensionLocation.MiddleSE, new GmCollabUseLinkHeader(diagram, ref));
            addExtension(ExtensionLocation.SourceNW, new GmBindingLabelGroup(diagram, ref));
        }
    }

    @objid ("348dadbe-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.element.getCompositionOwner();
    }

    @objid ("348dadc5-55b7-11e2-877f-002564c97630")
    @Override
    public CollaborationUse getRelatedElement() {
        return this.element;
    }

    @objid ("348dadcc-55b7-11e2-877f-002564c97630")
    @Override
    public CollaborationUse getRepresentedElement() {
        return this.element;
    }

    @objid ("348dadd3-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STRUCTURED_KEYS.getStyleKey(metakey);
    }

    @objid ("348f3439-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STRUCTURED_KEYS.getStyleKeys();
    }

    @objid ("348f3442-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.element.getType();
    }

    @objid ("348f3449-55b7-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        this.element = (CollaborationUse) resolveRef(this.getRepresentedRef());
    }

    @objid ("348f344f-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmCollabUseLink." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("348f3455-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
