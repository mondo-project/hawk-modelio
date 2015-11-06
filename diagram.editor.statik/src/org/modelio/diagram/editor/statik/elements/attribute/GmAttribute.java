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
                                    

package org.modelio.diagram.editor.statik.elements.attribute;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link Attribute} label.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("33fe3ef9-55b7-11e2-877f-002564c97630")
public class GmAttribute extends GmModelElementFlatHeader {
    @objid ("33fe3efd-55b7-11e2-877f-002564c97630")
    private Attribute element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("33fe3f00-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("33fe3f03-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("6c546a5d-d7d0-4c24-b1b9-81e4d31209f3")
    public static GmAttributeStyleKeys ATT_KEYS = new GmAttributeStyleKeys();

    /**
     * Constructor for deserialization only.
     */
    @objid ("33fe3f05-55b7-11e2-877f-002564c97630")
    public GmAttribute() {
    }

    /**
     * Create an attribute representation.
     * @param diagram The diagram
     * @param el The represented attribute, may be null.
     * @param ref The represented attribute reference, may not be null.
     */
    @objid ("33ffc579-55b7-11e2-877f-002564c97630")
    public GmAttribute(GmAbstractDiagram diagram, Attribute el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        init();
    }

    @objid ("33ffc585-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("33ffc593-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("33ffc5a1-55b7-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        return new IEditableText() {
        
            @Override
            public String getText() {
        return getRelatedElement().getName();
                    }
        
                    @Override
                    public void setText(String text) {
        getRelatedElement().setName(text);
                    }
        
                };
    }

    @objid ("33ffc5a8-55b7-11e2-877f-002564c97630")
    @Override
    public Attribute getRelatedElement() {
        return this.element;
    }

    @objid ("33ffc5af-55b7-11e2-877f-002564c97630")
    @Override
    public Attribute getRepresentedElement() {
        return this.element;
    }

    @objid ("33ffc5b6-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (metakey == MetaKey.SHOWSTEREOTYPES) {
            return super.getStyleKey(MetaKey.AttGroup.ATTSHOWSTEREOTYPES);
        } else if (metakey == MetaKey.SHOWTAGS) {
            return super.getStyleKey(MetaKey.AttGroup.ATTSHOWTAGS);
        } else if (metakey == MetaKey.SHOWVISIBILITY) {
            return super.getStyleKey(MetaKey.AttGroup.ATTSHOWVISIBILITY);
        } else
            return ATT_KEYS.getStyleKey(metakey);
    }

    @objid ("33ffc5c0-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return ATT_KEYS.getStyleKeys();
    }

    @objid ("34014c21-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmAttribute." + MINOR_VERSION_PROPERTY);
        int readVersion = (versionProperty == null) ? 0 : ((Integer) versionProperty).intValue();
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

    @objid ("34014c27-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("34014c2c-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("34014c33-55b7-11e2-877f-002564c97630")
    private String computeSignature(Attribute att) {
        final GeneralClass type = att.getType();
        
        String typename = "<no type>";
        if (type != null)
            typename = type.getName();
        
        StringBuffer symbolBuf = new StringBuffer(30);
        
        StyleKey styleKey = getStyleKey(MetaKey.SHOWVISIBILITY);
        if (styleKey != null && (Boolean) getStyle().getProperty(styleKey)) {
            switch (att.getVisibility()) {
            case PUBLIC:
                symbolBuf.append("+");
                break;
            case PROTECTED:
                symbolBuf.append("#");
                break;
            case PRIVATE:
                symbolBuf.append("-");
                break;
            case PACKAGEVISIBILITY:
                symbolBuf.append("~");
                break;
            default:
                symbolBuf.append(" ");
            }
        }
        
        symbolBuf.append(" ");
        if (att.isIsDerived())
            symbolBuf.append("/");
        
        symbolBuf.append(att.getName());
        symbolBuf.append(" : ");
        symbolBuf.append(typename);
        getAttributeMultiplicity(att, symbolBuf);
        return symbolBuf.toString();
    }

    /**
     * Compute the multiplicity symbol of the attribute and append it to the given buffer.
     * @param theAttribute the attribute to compute the visibility.
     * @param output the output buffer
     * @return
     */
    @objid ("34014c3a-55b7-11e2-877f-002564c97630")
    private StringBuffer getAttributeMultiplicity(final Attribute theAttribute, final StringBuffer output) {
        String multiplicityMinStr = theAttribute.getMultiplicityMin();
        String multiplicityMaxStr = theAttribute.getMultiplicityMax();
        String separator = "";
        
        if (multiplicityMinStr.equals("1") && multiplicityMaxStr.equals("1")) {
            return output;
        }
        
        if (!multiplicityMinStr.equals("") || !multiplicityMaxStr.equals("")) {
            output.append(" [");
        
            if (multiplicityMinStr.equals(multiplicityMaxStr)) {
                output.append(multiplicityMinStr);
            } else if (multiplicityMinStr.equals("0") && multiplicityMaxStr.equals("*")) {
                output.append("*");
            } else {
                if (!multiplicityMinStr.equals("") && !multiplicityMaxStr.equals("")) {
                    separator = "..";
                }
        
                output.append(multiplicityMinStr);
                output.append(separator);
                output.append(multiplicityMaxStr);
            }
            output.append("]");
        }
        return output;
    }

    @objid ("34014c45-55b7-11e2-877f-002564c97630")
    private void init() {
        setShowMetaclassKeyword(false);
        setShowMetaclassIcon(false);
    }

    @objid ("34014c47-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmAttribute." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("34014c4d-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (Attribute) resolveRef(this.getRepresentedRef());
    }

    @objid ("34014c52-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
