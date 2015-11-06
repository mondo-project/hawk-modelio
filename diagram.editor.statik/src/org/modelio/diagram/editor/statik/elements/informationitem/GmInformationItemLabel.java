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
                                    

package org.modelio.diagram.editor.statik.elements.informationitem;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ShowNameMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.informationFlow.InformationItem;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link InformationItem} label.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("35157b60-55b7-11e2-877f-002564c97630")
public class GmInformationItemLabel extends GmModelElementFlatHeader {
    @objid ("35157b64-55b7-11e2-877f-002564c97630")
    private InformationItem element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("35157b67-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("35157b6a-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Constructor for deserialization only.
     */
    @objid ("35157b6c-55b7-11e2-877f-002564c97630")
    public GmInformationItemLabel() {
    }

    /**
     * Create an attribute representation.
     * @param diagram The diagram
     * @param el The represented InformationItem, may be null.
     * @param ref The represented InformationItem reference, may not be null.
     */
    @objid ("35157b6f-55b7-11e2-877f-002564c97630")
    public GmInformationItemLabel(GmAbstractDiagram diagram, InformationItem el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        setShowMetaclassKeyword(false);
        setShowMetaclassIcon(true);
    }

    @objid ("35157b7b-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("351701d9-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("351701e7-55b7-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        return null;
    }

    @objid ("351701ee-55b7-11e2-877f-002564c97630")
    @Override
    public InformationItem getRelatedElement() {
        return this.element;
    }

    @objid ("351701f5-55b7-11e2-877f-002564c97630")
    @Override
    public InformationItem getRepresentedElement() {
        return this.element;
    }

    @objid ("351701fc-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (metakey == MetaKey.FONT) {
            return super.getStyleKey(MetaKey.InnerGroup.INNERFONT);
        } else if (metakey == MetaKey.SHOWSTEREOTYPES) {
            return super.getStyleKey(MetaKey.InnerGroup.INNERSHOWSTEREOTYPES);
        } else if (metakey == MetaKey.SHOWTAGS) {
            return super.getStyleKey(MetaKey.InnerGroup.INNERSHOWTAGS);
        } else if (metakey == MetaKey.TEXTCOLOR) {
            return super.getStyleKey(MetaKey.InnerGroup.INNERTEXTCOLOR);
        } else if (metakey == MetaKey.SHOWNAME) {
            return null;
        }
        return null;
    }

    /**
     * Attributes don't have own style key.
     * <p>
     * Everything is defined on the owner class.
     */
    @objid ("35170206-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("35170210-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmInformationItemLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("35170216-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("3517021b-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("3518887d-55b7-11e2-877f-002564c97630")
    private String computeSignature(InformationItem att) {
        switch (getNameMode()) {
            case NONE:
                return "";
            case SIMPLE:
            default:
                return att.getName();
            case QUALIFIED:
            case FULLQUALIFIED:
                StringBuilder s = new StringBuilder(100);
                s.append(att.getName());
        
                final List<Classifier> types = att.getRepresented();
                boolean first = true;
                for (Classifier c : types) {
                    if (first) {
                        s.append(":");
                        first = false;
                    } else {
                        s.append(", ");
                    }
                    s.append(c.getName());
                }
                return s.toString();
        }
    }

    @objid ("35188883-55b7-11e2-877f-002564c97630")
    private ShowNameMode getNameMode() {
        final StyleKey nameKey = getStyleKey(MetaKey.SHOWNAME);
        
        if (nameKey == null)
            return ShowNameMode.QUALIFIED;
        else
            return getStyle().getProperty(nameKey);
    }

    @objid ("35188888-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmInformationItemLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("3518888e-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (InformationItem) resolveRef(this.getRepresentedRef());
    }

    @objid ("35188893-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
