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
                                    

package org.modelio.diagram.editor.statik.elements.binding;

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
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link Binding} label.
 * <p>
 * Extends {@link GmModelElementFlatHeader}.
 */
@objid ("3408ed3a-55b7-11e2-877f-002564c97630")
public class GmBindingLabel extends GmModelElementFlatHeader {
    @objid ("3408ed3e-55b7-11e2-877f-002564c97630")
    private Binding element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("3408ed41-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("3408ed44-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Constructor for deserialization only.
     */
    @objid ("3408ed46-55b7-11e2-877f-002564c97630")
    public GmBindingLabel() {
    }

    /**
     * Create an attribute representation.
     * @param diagram The diagram
     * @param el The represented attribute, may be null.
     * @param ref The represented attribute reference, may not be null.
     */
    @objid ("3408ed49-55b7-11e2-877f-002564c97630")
    public GmBindingLabel(GmAbstractDiagram diagram, Binding el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        init();
    }

    @objid ("3408ed55-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("3408ed63-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("3408ed71-55b7-11e2-877f-002564c97630")
    @Override
    public IEditableText getEditableText() {
        return null;
    }

    @objid ("3408ed78-55b7-11e2-877f-002564c97630")
    @Override
    public Binding getRelatedElement() {
        return this.element;
    }

    @objid ("340a73da-55b7-11e2-877f-002564c97630")
    @Override
    public Binding getRepresentedElement() {
        return this.element;
    }

    @objid ("340a73e1-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (metakey == MetaKey.FONT) {
            return super.getStyleKey(MetaKey.AttGroup.ATTFONT);
        } else if (metakey == MetaKey.SHOWSTEREOTYPES) {
            return super.getStyleKey(MetaKey.AttGroup.ATTSHOWSTEREOTYPES);
        } else if (metakey == MetaKey.SHOWTAGS) {
            return super.getStyleKey(MetaKey.AttGroup.ATTSHOWTAGS);
        } else if (metakey == MetaKey.TEXTCOLOR) {
            return super.getStyleKey(MetaKey.AttGroup.ATTTEXTCOLOR);
        }
        return null;
    }

    /**
     * Attributes don't have own style key.
     * <p>
     * Everything is defined on the owner class.
     */
    @objid ("340a73eb-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("340a73f5-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBindingLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("340a73fb-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    /**
     * Redefined to set its own style cascading from the new parent node style.
     */
    @objid ("340a7400-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("340a7407-55b7-11e2-877f-002564c97630")
    private String computeSignature(Binding att) {
        final ModelElement feature = att.getRepresentedFeature();
        final ModelElement collabRole = getBindingRole(att);
        
        String featureName = "<not bound>";
        if (feature != null)
            featureName = feature.getName();
        
        String roleName = "<none>";
        if (collabRole != null)
            roleName = collabRole.getName();
        return roleName + " -> " + featureName;
    }

    /**
     * Get the collaboration role the given binding binds.
     * @param el a collaboration use binding
     * @return The bound collaboration role.
     */
    @objid ("340a740e-55b7-11e2-877f-002564c97630")
    private ModelElement getBindingRole(Binding el) {
        ModelElement role = el.getRole();
        if (role == null)
            role = el.getConnectorRole();
        if (role == null)
            role = el.getConnectorEndRole();
        return role;
    }

    @objid ("340a7418-55b7-11e2-877f-002564c97630")
    private void init() {
        setShowMetaclassKeyword(false);
        setShowMetaclassIcon(false);
    }

    @objid ("340a741a-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBindingLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("340bfa7e-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (Binding) resolveRef(this.getRepresentedRef());
    }

    @objid ("340bfa83-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
