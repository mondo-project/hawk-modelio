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
                                    

package org.modelio.diagram.elements.umlcommon.namespaceuse;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphic model for {@link INamespaceUse}.
 */
@objid ("817a734c-1dec-11e2-8cad-001ec947c8cc")
public class GmNamespaceUse extends GmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("817a7351-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("817a7354-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("817a734e-1dec-11e2-8cad-001ec947c8cc")
    private NamespaceUse nsu;

    @objid ("817a734f-1dec-11e2-8cad-001ec947c8cc")
    private static final GmNamespaceUseStyleKeys styleKeyProvider = new GmNamespaceUseStyleKeys();

    /**
     * Initialize a namespaceuse graphic model.
     * @param diagram The owning diagram
     * @param nsu The namespaceuse, may be null
     * @param ref The namespaceuse reference, may not be null
     */
    @objid ("817a7356-1dec-11e2-8cad-001ec947c8cc")
    public GmNamespaceUse(GmAbstractDiagram diagram, final NamespaceUse nsu, MRef ref) {
        super(diagram, ref);
        this.nsu = nsu;
        
        addExtension(ExtensionLocation.MiddleNW, new GmNamespaceUseLabel(diagram, ref));
    }

    @objid ("817a735d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return styleKeyProvider.getStyleKey(metakey);
    }

    @objid ("817a7363-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<StyleKey> getStyleKeys() {
        return styleKeyProvider.getStyleKeys();
    }

    /**
     * For deserialization only.
     */
    @objid ("817a736a-1dec-11e2-8cad-001ec947c8cc")
    public GmNamespaceUse() {
    }

    @objid ("817a736d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void readLink(IDiagramReader in) {
        super.readLink(in);
        this.nsu = (NamespaceUse) resolveRef(this.getRepresentedRef());
        if (this.nsu == null) {
            // NSU may have been re-identified, try finding a new one with the same source and target
            if (this.from != null && this.to != null) {
                MObject fromElt = this.from.getRelatedElement();
                MObject toElt = this.to.getRelatedElement();
                
                if (fromElt != null) {
                    for (NamespaceUse link : ((NameSpace)fromElt).getUsedNsu()) {
                        if (link.getUsed() == toElt) {
                            this.nsu = link;
                            break;
                        }
                    }
                }
            }
        }
    }

    @objid ("817a7371-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getFromElement() {
        return this.nsu.getUser();
    }

    @objid ("817a7376-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getToElement() {
        return this.nsu.getUsed();
    }

    @objid ("817a737b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRepresentedElement() {
        return this.nsu;
    }

    @objid ("817a7380-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("817a7385-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNamespaceUse." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("817a7389-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
