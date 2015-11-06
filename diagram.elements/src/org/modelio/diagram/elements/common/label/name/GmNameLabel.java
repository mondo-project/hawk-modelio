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
                                    

package org.modelio.diagram.elements.common.label.name;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a label that displays the name, tags and stereotypes of an element.
 * <p>
 * The label visibility is controlled by the {@link MetaKey#SHOWLABEL} meta key.
 */
@objid ("7ea5a86c-1dec-11e2-8cad-001ec947c8cc")
public final class GmNameLabel extends GmModelElementFlatHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7ea5a86e-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("7ea5a871-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Create a model element label
     * @param diagram the diagram.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("7ea5a873-1dec-11e2-8cad-001ec947c8cc")
    public GmNameLabel(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
    }

    /**
     * For deserialization only.
     */
    @objid ("7ea5a878-1dec-11e2-8cad-001ec947c8cc")
    public GmNameLabel() {
        // serialization
    }

    @objid ("7ea5a87b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<Stereotype> filterStereotypes(List<Stereotype> stereotypes) {
        // Check the current representation mode
        final StyleKey key = getStyleKey(MetaKey.REPMODE);
        if (key != null) {
            // For image mode, filter the first image stereotype
            if (getStyle().getProperty(key) == RepresentationMode.IMAGE) {
                for (Stereotype stereo : stereotypes) {
                    if (!stereo.getIcon().isEmpty()) {
                        List<Stereotype> ret = new ArrayList<>(stereotypes);
                        ret.remove(stereo);
                        return ret;
                    }
                }
            }
        }
        return stereotypes;
    }

    @objid ("7ea5a885-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("7ea5a88f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public IEditableText getEditableText() {
        if (getRelatedElement() == null)
            return null;
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

    @objid ("7ea5a894-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isVisible() {
        final StyleKey key = getParent().getStyleKey(MetaKey.SHOWLABEL);
        if (key == null)
            return true;
        else
            return getStyle().getProperty(key);
    }

    @objid ("7ea80ac5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected String computeLabel() {
        if (getRelatedElement() != null)
            return (getRelatedElement().getName());
        else
            return "";
    }

    @objid ("7ea80ac9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void styleChanged(IStyle changedStyle) {
        fireVisibilityChanged();
        super.styleChanged(changedStyle);
    }

    @objid ("7ea80acd-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void styleChanged(StyleKey property, Object newValue) {
        final StyleKey key = getParent().getStyleKey(MetaKey.SHOWLABEL);
        if (key != null && key.equals(property))
            fireVisibilityChanged();
        else
            super.styleChanged(property, newValue);
    }

    @objid ("7ea80ad2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmNameLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("7ea80ad6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNameLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7ea80ada-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("7ea80add-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
