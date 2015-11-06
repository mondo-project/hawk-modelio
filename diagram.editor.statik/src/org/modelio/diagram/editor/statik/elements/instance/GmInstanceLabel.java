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
                                    

package org.modelio.diagram.editor.statik.elements.instance;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ShowNameMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link Instance} label.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("35358699-55b7-11e2-877f-002564c97630")
public class GmInstanceLabel extends GmModelElementFlatHeader {
    @objid ("3535869d-55b7-11e2-877f-002564c97630")
    private Instance element = null;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("353586a0-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("35370cfb-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * constructor to be used only for deserialization
     */
    @objid ("35370cfd-55b7-11e2-877f-002564c97630")
    public GmInstanceLabel() {
    }

    /**
     * Creates an instance label.
     * @param diagram the owning graphic diagram, may not be <tt>null</tt>.
     * @param el the represented instance, may be <tt>null</tt>.
     * @param ref the represented instance reference, may not be <tt>null</tt>.
     */
    @objid ("35370d00-55b7-11e2-877f-002564c97630")
    public GmInstanceLabel(GmAbstractDiagram diagram, Instance el, MRef ref) {
        super(diagram, ref);
        
        this.element = el;
    }

    @objid ("35370d0c-55b7-11e2-877f-002564c97630")
    @Override
    public String computeLabel() {
        final Instance inst = getRelatedElement();
        
        final ShowNameMode nameMode = getStyle().getProperty(GmInstanceStructuredStyleKeys.SHOWNAME);
        
        switch (nameMode) {
        case FULLQUALIFIED:
            return InstanceSymbolProvider.computeFullQualifiedLabel(inst);
        case NONE:
            return "";
        case QUALIFIED:
            return InstanceSymbolProvider.computeQualifiedLabel(inst);
        case SIMPLE:
        default:
            return InstanceSymbolProvider.computeSimpleLabel(inst);
        
        }
    }

    @objid ("35370d10-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("35370d1e-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("35370d2c-55b7-11e2-877f-002564c97630")
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

    @objid ("35370d33-55b7-11e2-877f-002564c97630")
    @Override
    public Instance getRelatedElement() {
        return this.element;
    }

    @objid ("35370d3a-55b7-11e2-877f-002564c97630")
    @Override
    public Instance getRepresentedElement() {
        return this.element;
    }

    @objid ("3538939a-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (metakey == MetaKey.FONT) {
            return super.getStyleKey(MetaKey.InternalGroup.INTFONT);
        } else if (metakey == MetaKey.SHOWSTEREOTYPES) {
            return super.getStyleKey(MetaKey.InternalGroup.INTSHOWSTEREOTYPES);
        } else if (metakey == MetaKey.SHOWTAGS) {
            return super.getStyleKey(MetaKey.InternalGroup.INTSHOWTAGS);
        } else if (metakey == MetaKey.TEXTCOLOR) {
            return super.getStyleKey(MetaKey.InternalGroup.INTTEXTCOLOR);
        }
        return null;
    }

    /**
     * Instance labels don't have own style key.
     * <p>
     * Everything is defined on the owner class.
     */
    @objid ("353893a4-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("353893ae-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmInstanceLabel." + MINOR_VERSION_PROPERTY);
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

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("353893b4-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("353893bb-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        if (property == getStyleKey(MetaKey.SHOWNAME))
            if (updateMainLabelFromObModel())
                firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
        
        super.styleChanged(property, newValue);
    }

    @objid ("353893c4-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        if (updateMainLabelFromObModel())
            firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
        
        super.styleChanged(changedStyle);
    }

    @objid ("353893cb-55b7-11e2-877f-002564c97630")
    protected boolean updateMainLabelFromObModel() {
        if (this.getRelatedElement() != null && this.getRelatedElement().isValid()) {
            final String newName = computeLabel();
            if (this.label == null || !this.label.equals(newName)) {
                this.label = newName;
                return true;
            }
        }
        return false;
    }

    @objid ("353893cf-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmInstanceLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("353893d5-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (Instance) resolveRef(this.getRepresentedRef());
    }

    @objid ("353893da-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
