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
                                    

package org.modelio.diagram.editor.statik.elements.namespacelabel;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.namespaceheader.NamespaceSymbolProvider;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ShowNameMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link NameSpace} label.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("7a12b9bf-55b6-11e2-877f-002564c97630")
public class GmNameSpaceLabel extends GmModelElementFlatHeader {
    @objid ("35a0561c-55b7-11e2-877f-002564c97630")
    private NameSpace element = null;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("35a0561f-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("35a05622-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Empty constructor needed for (de-)serialization.
     */
    @objid ("35a05624-55b7-11e2-877f-002564c97630")
    public GmNameSpaceLabel() {
        // Empty constructor needed for (de-)serialization.
    }

    /**
     * Default constructor.
     * @param diagram the diagram in which this gm is unmasked.
     * @param el the represented element, may be <i>null</i>.
     * @param ref a reference to the represented element.
     */
    @objid ("35a05627-55b7-11e2-877f-002564c97630")
    public GmNameSpaceLabel(GmAbstractDiagram diagram, NameSpace el, MRef ref) {
        super(diagram, ref);
        this.element = el;
    }

    @objid ("35a1dcc4-55b7-11e2-877f-002564c97630")
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

    @objid ("35a1dccb-55b7-11e2-877f-002564c97630")
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
        } else if (metakey == MetaKey.SHOWVISIBILITY) {
            return super.getStyleKey(MetaKey.InnerGroup.INNERSHOWVISIBILITY);
        } else if (metakey == MetaKey.SHOWNAME) {
            return super.getStyleKey(MetaKey.InnerGroup.INNERSHOWNAME);
        }
        return null;
    }

    /**
     * Attributes don't have own style key.
     * <p>
     * Everything is defined on the owner class.
     */
    @objid ("35a1dcd5-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("35a1dcdf-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("35a1dce6-55b7-11e2-877f-002564c97630")
    private String computeSignature(NameSpace att) {
        StyleKey key = getStyleKey(MetaKey.SHOWNAME);
        if (key != null) {
            final ShowNameMode nameMode = getStyle().getProperty(key);
        
            switch (nameMode) {
                case FULLQUALIFIED:
                    return NamespaceSymbolProvider.computeFullQualifiedLabel(getRelatedElement(),
                                                                             showVisibility());
                case NONE:
                    return "";
                case QUALIFIED:
                    return NamespaceSymbolProvider.computeQualifiedLabel(getRelatedElement(),
                                                                         showVisibility());
                case SIMPLE:
                default:
                    return NamespaceSymbolProvider.computeSimpleLabel(getRelatedElement(), showVisibility());
        
            }
        } else {
            return getRelatedElement().getName();
        }
    }

    @objid ("35a1dcec-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    @objid ("35a1dcf1-55b7-11e2-877f-002564c97630")
    @Override
    public NameSpace getRelatedElement() {
        return this.element;
    }

    @objid ("35a1dcf8-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("35a3635e-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("35a3636c-55b7-11e2-877f-002564c97630")
    @Override
    public NameSpace getRepresentedElement() {
        return this.element;
    }

    @objid ("35a36373-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmNameSpaceLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("35a36379-55b7-11e2-877f-002564c97630")
    private boolean showVisibility() {
        final StyleKey showVisibilityKey = getStyleKey(MetaKey.SHOWVISIBILITY);
        return (showVisibilityKey != null && (Boolean) getStyle().getProperty(showVisibilityKey));
    }

    @objid ("35a3637d-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNameSpaceLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("35a36383-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (NameSpace) resolveRef(this.getRepresentedRef());
    }

    @objid ("35a36388-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
