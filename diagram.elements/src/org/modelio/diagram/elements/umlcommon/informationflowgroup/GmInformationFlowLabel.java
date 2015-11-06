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
                                    

package org.modelio.diagram.elements.umlcommon.informationflowgroup;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.informationFlow.InformationFlow;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Label representing an {@link IInformationFlow}.
 * 
 * @author cmarin
 */
@objid ("816c2534-1dec-11e2-8cad-001ec947c8cc")
public class GmInformationFlowLabel extends GmModelElementFlatHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("816c2537-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("816c253a-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    @objid ("816c2536-1dec-11e2-8cad-001ec947c8cc")
    private InformationFlow element;

    /**
     * Constructor for deserialization only.
     */
    @objid ("816c253c-1dec-11e2-8cad-001ec947c8cc")
    public GmInformationFlowLabel() {
    }

    /**
     * Constructor.
     * @param diagram The diagram
     * @param el The represented element, may be null.
     * @param ref The represented element reference, may not be null.
     */
    @objid ("816c253f-1dec-11e2-8cad-001ec947c8cc")
    public GmInformationFlowLabel(GmAbstractDiagram diagram, InformationFlow el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        init();
    }

    @objid ("816c2545-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        return stereotypes;
    }

    @objid ("816c254f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("816c2559-1dec-11e2-8cad-001ec947c8cc")
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

    @objid ("816c255e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public InformationFlow getRelatedElement() {
        return (InformationFlow) super.getRelatedElement();
    }

    @objid ("816c2563-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public InformationFlow getRepresentedElement() {
        return this.element;
    }

    @objid ("816c2568-1dec-11e2-8cad-001ec947c8cc")
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

    @objid ("816e878b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<StyleKey> getStyleKeys() {
        return Collections.emptyList();
    }

    @objid ("816e8792-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmInformationFlowLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("816e8796-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected String computeLabel() {
        return computeSignature(getRelatedElement());
    }

    @objid ("816e879b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void setParent(GmCompositeNode parent) {
        if (getParent() != parent) {
            super.setParent(parent);
        
            if (parent != null)
                getStyle().setCascadedStyle(parent.getStyle());
        }
    }

    @objid ("816e879f-1dec-11e2-8cad-001ec947c8cc")
    private static String computeSignature(InformationFlow att) {
        final EList<Classifier> types = att.getConveyed();
        
        String typename = "<no item>";
        if (!types.isEmpty()) {
            StringBuilder s = new StringBuilder();
            for (Classifier t : types) {
                if (s.length() > 0)
                    s.append(", ");
                s.append(t.getName());
            }
            typename = s.toString();
        }
        return att.getName() + " : " + typename;
    }

    @objid ("816e87a4-1dec-11e2-8cad-001ec947c8cc")
    private void init() {
        setShowMetaclassKeyword(false);
        setShowMetaclassIcon(false);
    }

    @objid ("816e87a6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmInformationFlowLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("816e87aa-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (InformationFlow) resolveRef(this.getRepresentedRef());
    }

    @objid ("816e87ad-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
