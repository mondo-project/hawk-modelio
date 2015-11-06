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
                                    

package org.modelio.diagram.elements.common.label.modelelement;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Header label that shows the edge name, tags and stereotypes.
 */
@objid ("7e94f7f6-1dec-11e2-8cad-001ec947c8cc")
public class GmDefaultFlatHeader extends GmModelElementFlatHeader {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7e94f7f8-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("7e94f7fb-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Create an header label
     * @param diagram the diagram.
     * @param relatedRef reference to the diagram.
     */
    @objid ("7e94f7fd-1dec-11e2-8cad-001ec947c8cc")
    public GmDefaultFlatHeader(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
    }

    /**
     * For deserialization only.
     */
    @objid ("7e975a51-1dec-11e2-8cad-001ec947c8cc")
    public GmDefaultFlatHeader() {
        // serialization
    }

    @objid ("7e975a54-1dec-11e2-8cad-001ec947c8cc")
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

    @objid ("7e975a5e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<TaggedValue> filterTags(List<TaggedValue> taggedValues) {
        return taggedValues;
    }

    @objid ("7e975a68-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public IEditableText getEditableText() {
        if (getRelatedElement() == null || getRelatedElement().isDeleted())
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

    @objid ("7e975a6d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isVisible() {
        GmModel parent = getParent();
        if (parent == null) {
            return false;
        }
        
        final StyleKey key = parent.getStyleKey(MetaKey.SHOWLABEL);
        if (key == null) {
            return true;
        } else {
            return getStyle().getProperty(key);
        }
    }

    @objid ("7e975a71-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected String computeLabel() {
        if (getRelatedElement() != null)
            return (getRelatedElement().getName());
        else
            return "";
    }

    @objid ("7e975a75-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmDefaultFlatHeader." + MINOR_VERSION_PROPERTY);
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

    @objid ("7e975a79-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmDefaultFlatHeader." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7e975a7d-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("7e975a80-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
