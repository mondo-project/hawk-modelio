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
                                    

package org.modelio.diagram.editor.statik.elements.requiredinterface;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.header.GmModelElementHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Label representing an {@link RequiredInterface}.
 * 
 * @author cmarin
 */
@objid ("36746f2a-55b7-11e2-877f-002564c97630")
public class GmRequiredInterfaceLabel extends GmModelElementHeader {
    @objid ("36746f2e-55b7-11e2-877f-002564c97630")
    private RequiredInterface element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("36746f31-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("36746f34-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Constructor.
     * @param diagram The diagram
     * @param el The represented element, may be null.
     * @param ref The represented element reference, may not be null.
     */
    @objid ("36746f36-55b7-11e2-877f-002564c97630")
    public GmRequiredInterfaceLabel(GmAbstractDiagram diagram, RequiredInterface el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        init();
    }

    /**
     * Constructor for deserialization only.
     */
    @objid ("3675f599-55b7-11e2-877f-002564c97630")
    public GmRequiredInterfaceLabel() {
    }

    @objid ("3675f59c-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("3675f5aa-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("3675f5b8-55b7-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        return null;
    }

    @objid ("3675f5bf-55b7-11e2-877f-002564c97630")
    @Override
    public RequiredInterface getRelatedElement() {
        return this.element;
    }

    @objid ("3675f5c6-55b7-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("3675f5cd-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (getParent() != null)
            return getParent().getStyleKey(metakey);
        else
            return null;
    }

    @objid ("3675f5d6-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("3675f5df-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmRequiredInterfaceLabel." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
            case 0: {
                read_0(in);
                break;
            }
            default: {
                assert (false) : "version number not covered!";
                // reading as last handled version: 0
                read_0(in);
                break;
            }
        }
    }

    @objid ("36777c3d-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeMainLabel() {
        return computeSignature(getRelatedElement());
    }

    @objid ("36777c42-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("36777c48-55b7-11e2-877f-002564c97630")
    private String computeSignature(RequiredInterface att) {
        final List<Interface> types = att.getRequiredElement();
        
        String typename = ""; //"<none>";
        
        if (!types.isEmpty()) {
            final StringBuilder s = new StringBuilder();
            for (Interface t : types) {
                if (s.length() > 0)
                    s.append(", ");
                s.append(t.getName());
            }
            typename = s.toString();
        }
        return typename;
    }

    @objid ("36777c4f-55b7-11e2-877f-002564c97630")
    private void init() {
        setShowMetaclassKeyword(false);
        setShowMetaclassIcon(false);
    }

    @objid ("36777c51-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmRequiredInterfaceLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("36777c57-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (RequiredInterface) resolveRef(this.getRepresentedRef());
    }

    @objid ("36777c5c-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
