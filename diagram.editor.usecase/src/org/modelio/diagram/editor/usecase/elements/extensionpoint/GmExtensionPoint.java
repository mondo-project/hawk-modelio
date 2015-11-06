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
                                    

package org.modelio.diagram.editor.usecase.elements.extensionpoint;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCaseStructuredStyleKeys;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("5e4722ba-55b7-11e2-877f-002564c97630")
public class GmExtensionPoint extends GmModelElementFlatHeader {
    @objid ("5e4722be-55b7-11e2-877f-002564c97630")
    private ExtensionPoint theExtensionPoint;

    @objid ("5e4722c1-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("5e4722c4-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("5e4722c6-55b7-11e2-877f-002564c97630")
    public GmExtensionPoint() {
    }

    @objid ("5e4722c9-55b7-11e2-877f-002564c97630")
    public GmExtensionPoint(GmAbstractDiagram diagram, ExtensionPoint el, MRef ref) {
        super(diagram, ref);
        this.theExtensionPoint = el;
        init();
    }

    @objid ("5e4722d5-55b7-11e2-877f-002564c97630")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("5e48a966-55b7-11e2-877f-002564c97630")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("5e48a974-55b7-11e2-877f-002564c97630")
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

    @objid ("5e48a97b-55b7-11e2-877f-002564c97630")
    @Override
    public ExtensionPoint getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("5e48a982-55b7-11e2-877f-002564c97630")
    @Override
    public ExtensionPoint getRepresentedElement() {
        return this.theExtensionPoint;
    }

    @objid ("5e48a989-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        if (metakey == MetaKey.FONT) {
            return GmUseCaseStructuredStyleKeys.ExtensionPoint.FONT;
        } else if (metakey == MetaKey.SHOWSTEREOTYPES) {
            return GmUseCaseStructuredStyleKeys.ExtensionPoint.SHOWSTEREOTYPES;
        } else if (metakey == MetaKey.SHOWTAGS) {
            return GmUseCaseStructuredStyleKeys.ExtensionPoint.SHOWTAGS;
        } else if (metakey == MetaKey.TEXTCOLOR) {
            return GmUseCaseStructuredStyleKeys.ExtensionPoint.TEXTCOLOR;
        }
        return null;
    }

    @objid ("5e48a993-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("5e48a99d-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmExtensionPoint." + MINOR_VERSION_PROPERTY);
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

    @objid ("5e4a2ffc-55b7-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    @objid ("5e4a3001-55b7-11e2-877f-002564c97630")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("5e4a3008-55b7-11e2-877f-002564c97630")
    private String computeSignature(ExtensionPoint att) {
        String svis;
        switch (att.getVisibility()) {
            case PUBLIC:
                svis = "+";
                break;
            case PROTECTED:
                svis = "#";
                break;
            case PRIVATE:
                svis = "-";
                break;
            case PACKAGEVISIBILITY:
                svis = "~";
                break;
            default:
                svis = " ";
        }
        return svis + " " + att.getName();
    }

    @objid ("5e4a300f-55b7-11e2-877f-002564c97630")
    private void init() {
        setShowMetaclassKeyword(false);
        setShowMetaclassIcon(false);
    }

    @objid ("5e4a3011-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmExtensionPoint." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("5e4a3017-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.theExtensionPoint = (ExtensionPoint) resolveRef(this.getRepresentedRef());
    }

    @objid ("5e4a301c-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
