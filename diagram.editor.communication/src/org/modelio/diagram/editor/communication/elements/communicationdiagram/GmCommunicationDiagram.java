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
                                    

package org.modelio.diagram.editor.communication.elements.communicationdiagram;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the Gm of a CommunicationDiagram.
 */
@objid ("7a299d30-55b6-11e2-877f-002564c97630")
public class GmCommunicationDiagram extends GmAbstractDiagram {
    @objid ("7a299d36-55b6-11e2-877f-002564c97630")
    private CommunicationDiagram element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7a299d39-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("7a299d3c-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("0551512c-599a-11e2-ae45-002564c97630")
    private static final GmCommunicationDiagramStyleKeys STYLEKEYS = new GmCommunicationDiagramStyleKeys();

    /**
     * Initialize the diagram.
     * @param manager The model manager
     * @param theCommunicationDiagram the displayed diagram.
     * @param diagramRef the reference of the displayed diagram. Must reference a {@link CommunicationDiagram}.
     */
    @objid ("7a299d3e-55b6-11e2-877f-002564c97630")
    public GmCommunicationDiagram(ModelManager manager, CommunicationDiagram theCommunicationDiagram, MRef diagramRef) {
        super(manager, diagramRef);
        this.element = theCommunicationDiagram;
    }

    /**
     * Empty constructor needed for the (de-)serialization.
     */
    @objid ("7a299d4a-55b6-11e2-877f-002564c97630")
    public GmCommunicationDiagram() {
        // empty constructor for the serialization
    }

    @objid ("7a299d4d-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return CommunicationNode.class.isAssignableFrom(type);
    }

    @objid ("7a299d55-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        if ((el instanceof Dependency) ||
            (el instanceof Note) ||
            (el instanceof Constraint) ||
            (el instanceof ExternDocument)) {
            return true;
        }
        
        if ((el instanceof CommunicationChannel)) {
            return canUnmask(el.getCompositionOwner());
        }
        return (el instanceof CommunicationNode) &&
               el.getCompositionOwner()
                 .equals(((CommunicationDiagram) this.getRelatedElement()).getOrigin());
    }

    @objid ("7a299d5d-55b6-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        if (canCreate(metaclass))
            return this;
        //else
        return null;
    }

    @objid ("7a2b23c2-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("7a2b23c9-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STYLEKEYS.getStyleKey(metakey);
    }

    @objid ("7a2b23d3-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STYLEKEYS.getStyleKeys();
    }

    @objid ("7a2b23dc-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmCommunicationDiagram." + MINOR_VERSION_PROPERTY);
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

    @objid ("7a2b23e2-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        // Nothing to do.
    }

    @objid ("7a2b23e5-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.element;
    }

    @objid ("7a2b23ec-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("7a2b23f3-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmCommunicationDiagram." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7a2b23f9-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (CommunicationDiagram) this.resolveRef(this.getRepresentedRef());
    }

    @objid ("7a2caa59-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
