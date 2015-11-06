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
                                    

package org.modelio.diagram.editor.statik.elements.imagenamespacelabel;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
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
@objid ("34f0dc3a-55b7-11e2-877f-002564c97630")
public class GmImageNameSpaceLabel extends GmModelElementFlatHeader {
    @objid ("34f0dc3e-55b7-11e2-877f-002564c97630")
    private NameSpace element = null;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("34f0dc41-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("34f0dc44-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Empty constructor needed for (de-)serialization.
     */
    @objid ("34f0dc46-55b7-11e2-877f-002564c97630")
    public GmImageNameSpaceLabel() {
        // Empty constructor needed for (de-)serialization.
    }

    /**
     * Default constructor.
     * @param diagram the diagram in which this gm is unmasked.
     * @param el the represented element, may be <i>null</i>.
     * @param ref a reference to the represented element.
     */
    @objid ("34f0dc49-55b7-11e2-877f-002564c97630")
    public GmImageNameSpaceLabel(final GmAbstractDiagram diagram, final NameSpace el, final MRef ref) {
        super(diagram, ref);
        this.element = el;
    }

    @objid ("34f262e7-55b7-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        if (getRelatedElement() == null || !getRelatedElement().isValid()) {
            return null;
        }
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

    /**
     * Attributes don't have own style key.
     * <p>
     * Everything is defined on the owner class.
     */
    @objid ("34f262ee-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("34f262f8-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(final GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("34f26300-55b7-11e2-877f-002564c97630")
    private String computeSignature(final NameSpace att) {
        StyleKey showNameKey = getStyleKey(MetaKey.SHOWNAME);
        final ShowNameMode nameMode = (ShowNameMode) (showNameKey == null ? ShowNameMode.NONE
                : getStyle().getProperty(showNameKey));
        
        switch (nameMode) {
            case FULLQUALIFIED:
                return NamespaceSymbolProvider.computeFullQualifiedLabel(att, showVisibility());
            case NONE:
                return "";
            case QUALIFIED:
                return NamespaceSymbolProvider.computeQualifiedLabel(att, showVisibility());
            case SIMPLE:
            default:
                return NamespaceSymbolProvider.computeSimpleLabel(att, showVisibility());
        
        }
    }

    @objid ("34f26307-55b7-11e2-877f-002564c97630")
    private boolean showVisibility() {
        final StyleKey showVisibilityKey = getStyleKey(MetaKey.SHOWVISIBILITY);
        return (showVisibilityKey != null && (Boolean) getStyle().getProperty(showVisibilityKey));
    }

    @objid ("34f2630b-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    @objid ("34f26310-55b7-11e2-877f-002564c97630")
    @Override
    public NameSpace getRelatedElement() {
        return this.element;
    }

    @objid ("34f26317-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(final List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("34f3e985-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(final List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    /**
     * @Override
     * @objid ("61598727-d475-11e0-818b-bc305ba4815c") public NameSpace getRepresentedElement() { return this.element;
     * }
     */
    @objid ("34f3e994-55b7-11e2-877f-002564c97630")
    @Override
    public void read(final IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmImageNameSpaceLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("34f3e99c-55b7-11e2-877f-002564c97630")
    @Override
    public List<Image> getStereotypeIcons() {
        return Collections.emptyList();
    }

    @objid ("34f3e9a3-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmImageNameSpaceLabel." + MINOR_VERSION_PROPERTY,
                              Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * @Override
     * @objid ("61598727-d475-11e0-818b-bc305ba4815c") public NameSpace getRepresentedElement() { return this.element;
     * }
     */
    @objid ("34f3e9a9-55b7-11e2-877f-002564c97630")
    private void read_0(final IDiagramReader in) {
        super.read(in);
        this.element = (NameSpace) resolveRef(this.getRepresentedRef());
    }

    @objid ("34f3e9b0-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
