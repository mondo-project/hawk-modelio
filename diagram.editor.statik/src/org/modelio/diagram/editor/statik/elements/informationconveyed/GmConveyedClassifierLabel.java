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
                                    

package org.modelio.diagram.editor.statik.elements.informationconveyed;

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
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents the label of a {@link Classifier} conveyed by an information flow.
 * <p>
 * Contrary to GmNameSpacelabel, GmConveyedClassifierLabel does <b>not</b> represents the classifier, it only relates
 * it.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("34fb8aa2-55b7-11e2-877f-002564c97630")
public class GmConveyedClassifierLabel extends GmModelElementFlatHeader {
    @objid ("34fb8aa6-55b7-11e2-877f-002564c97630")
    private Classifier element = null;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("34fb8aa9-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("34fb8aac-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Empty constructor needed for (de-)serialization.
     */
    @objid ("34fb8aae-55b7-11e2-877f-002564c97630")
    public GmConveyedClassifierLabel() {
        // Empty constructor needed for (de-)serialization.
    }

    /**
     * Default constructor.
     * @param diagram the diagram in which this gm is unmasked.
     * @param el the represented element, may be <i>null</i>.
     * @param ref a reference to the represented element.
     */
    @objid ("34fb8ab1-55b7-11e2-877f-002564c97630")
    public GmConveyedClassifierLabel(GmAbstractDiagram diagram, Classifier el, MRef ref) {
        super(diagram, ref);
        this.element = el;
    }

    @objid ("34fb8abd-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("34fb8acb-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("34fb8ad9-55b7-11e2-877f-002564c97630")
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

    @objid ("34fd113a-55b7-11e2-877f-002564c97630")
    @Override
    public Classifier getRelatedElement() {
        return this.element;
    }

    @objid ("34fd1141-55b7-11e2-877f-002564c97630")
    @Override
    public Classifier getRepresentedElement() {
        return null;
    }

    @objid ("34fd1148-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (metakey == MetaKey.FONT) {
            return super.getStyleKey(MetaKey.InformationItemGroup.INFFONT);
        } else if (metakey == MetaKey.SHOWSTEREOTYPES) {
            return super.getStyleKey(MetaKey.InformationItemGroup.INFSHOWSTEREOTYPES);
        } else if (metakey == MetaKey.SHOWTAGS) {
            return super.getStyleKey(MetaKey.InformationItemGroup.INFSHOWTAGS);
        } else if (metakey == MetaKey.TEXTCOLOR) {
            return super.getStyleKey(MetaKey.InformationItemGroup.INFTEXTCOLOR);
        }
        return null;
    }

    /**
     * Attributes don't have own style key.
     * <p>
     * Everything is defined on the owner class.
     */
    @objid ("34fd1152-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("34fd115c-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmConveyedClassifierLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("34fd1162-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return getRelatedElement().getName();
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("34fd1167-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("34fd116e-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmConveyedClassifierLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("34fd1174-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (Classifier) resolveRef(this.getRepresentedRef());
    }

    @objid ("34fd1179-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
