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
                                    

package org.modelio.diagram.editor.deployment.elements.manifestation;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.Manifestation;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link Manifestation}.
 * 
 * @author chm
 */
@objid ("972fba7a-55b6-11e2-877f-002564c97630")
public class GmManifestation extends GmLink {
    @objid ("972fba7e-55b6-11e2-877f-002564c97630")
    private Manifestation theManifestation;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("972fba83-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("972fba86-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("43644c69-5beb-11e2-9e33-00137282c51b")
    private static final GmManifestationStyleKeys styleKeyProvider = new GmManifestationStyleKeys();

    /**
     * For deserialization only.
     */
    @objid ("972fba88-55b6-11e2-877f-002564c97630")
    public GmManifestation() {
    }

    /**
     * Initialize a control flow graphic model.
     * @param diagram The owning diagram
     * @param theManifestation The reference flow, may be null
     * @param ref The referenced flow reference, may not be null
     */
    @objid ("972fba8b-55b6-11e2-877f-002564c97630")
    public GmManifestation(GmAbstractDiagram diagram, MObject theManifestation, MRef ref) {
        super(diagram, ref);
        this.theManifestation = (Manifestation) theManifestation;
        
        GmDefaultFlatHeader extension = new GmDefaultFlatHeader(diagram, ref);
        extension.setShowLabel(false);
        extension.setShowMetaclassKeyword(true);
        addExtension(ExtensionLocation.MiddleNW, extension);
    }

    @objid ("972fba97-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.theManifestation;
    }

    @objid ("972fba9e-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("972fbaa8-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    @objid ("97314121-55b6-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        
        this.theManifestation = (Manifestation) resolveRef(getRepresentedRef());
    }

    @objid ("97314127-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.theManifestation.getOwner();
    }

    @objid ("9731412e-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.theManifestation.getUtilizedElement();
    }

    @objid ("97314135-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("9731413c-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmManifestation." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("97314142-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
