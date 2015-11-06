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

import java.util.ArrayList;
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
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link Instance} label for the image mode.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("352de550-55b7-11e2-877f-002564c97630")
public class GmImageInstanceLabel extends GmModelElementFlatHeader {
    @objid ("352de554-55b7-11e2-877f-002564c97630")
    private MObject element = null;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("352de557-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("352de55a-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * constructor to be used only for deserialization
     */
    @objid ("352de55c-55b7-11e2-877f-002564c97630")
    public GmImageInstanceLabel() {
    }

    /**
     * Creates an instance label.
     * @param diagram the owning graphic diagram, may not be <tt>null</tt>.
     * @param el the represented instance, may be <tt>null</tt>.
     * @param ref the represented instance reference, may not be <tt>null</tt>.
     */
    @objid ("352de55f-55b7-11e2-877f-002564c97630")
    public GmImageInstanceLabel(final GmAbstractDiagram diagram, final Instance el, final MRef ref) {
        super(diagram, ref);
        
        this.element = el;
    }

    @objid ("352de56e-55b7-11e2-877f-002564c97630")
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

    @objid ("352de572-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(final List<Stereotype> stereotypes) {
        return new ArrayList<>();
    }

    @objid ("352f6be1-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(final List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("352f6bf0-55b7-11e2-877f-002564c97630")
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

    @objid ("352f6bf7-55b7-11e2-877f-002564c97630")
    @Override
    public Instance getRelatedElement() {
        return (Instance) super.getRelatedElement();
    }

    @objid ("352f6bfe-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.element;
    }

    @objid ("352f6c05-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(final MetaKey metakey) {
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
    @objid ("352f6c10-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("352f6c1a-55b7-11e2-877f-002564c97630")
    @Override
    public void read(final IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmImageInstanceLabel." + MINOR_VERSION_PROPERTY);
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
    @objid ("3530f27d-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(final GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("3530f285-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        if (property == getStyleKey(MetaKey.SHOWNAME))
            if (updateMainLabelFromObModel())
                firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
        
        super.styleChanged(property, newValue);
    }

    @objid ("3530f28e-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        if (updateMainLabelFromObModel())
            firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
        
        super.styleChanged(changedStyle);
    }

    @objid ("3530f295-55b7-11e2-877f-002564c97630")
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

    @objid ("3530f299-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmImageInstanceLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("3530f29f-55b7-11e2-877f-002564c97630")
    private void read_0(final IDiagramReader in) {
        super.read(in);
        this.element = resolveRef(this.getRepresentedRef());
    }

    @objid ("3530f2a5-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
