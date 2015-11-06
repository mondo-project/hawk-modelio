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
                                    

package org.modelio.diagram.editor.communication.elements.communicationnode;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.editor.communication.elements.communicationnode.v0._GmCommunicationNode;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmModelElementFlatHeader;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationNode;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the Gm of a CommunicationNode.
 * 
 * @author chm
 */
@objid ("7a52d01d-55b6-11e2-877f-002564c97630")
public class GmCommunicationNode extends GmPortContainer {
    @objid ("7a52d021-55b6-11e2-877f-002564c97630")
    private ModelElement communicationNode;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7a52d02a-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("7a52d02d-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 1;

    @objid ("7a52d02f-55b6-11e2-877f-002564c97630")
    private static final String IMAGE_MODE_HEADER = "image mode header";

    @objid ("7a52d026-55b6-11e2-877f-002564c97630")
    private static final GmCommunicationNodeStructuredStyleKeys STRUCTKEYS = new GmCommunicationNodeStructuredStyleKeys();

    @objid ("7a52d028-55b6-11e2-877f-002564c97630")
    private static final GmCommunicationNodeImageStyleKeys IMAGEKEYS = new GmCommunicationNodeImageStyleKeys();

    @objid ("0561fa7d-599a-11e2-ae45-002564c97630")
     static final GmCommunicationNodeSimpleStyleKeys SIMPLEKEYS = new GmCommunicationNodeSimpleStyleKeys();

    /**
     * Default constructor.
     * @param diagram the diagram in which this gm is unmasked.
     * @param theCommunicationNode the represented object node, may be null.
     * @param ref a reference to the represented object node.
     */
    @objid ("7a52d031-55b6-11e2-877f-002564c97630")
    public GmCommunicationNode(GmAbstractDiagram diagram, ModelElement theCommunicationNode, MRef ref) {
        super(diagram, ref);
        this.communicationNode = theCommunicationNode;
        
        GmCommunicationNodePrimaryNode primary = new GmCommunicationNodePrimaryNode(diagram, ref);
        primary.setRoleInComposition(GmPortContainer.MAIN_NODE_ROLE);
        addChild(primary);
        GmModelElementFlatHeader imageModeHeader = new GmCommunicationNodeFlatHeader(diagram, ref);
        imageModeHeader.setRoleInComposition(IMAGE_MODE_HEADER);
        imageModeHeader.setLayoutData(PositionConstants.SOUTH);
        addChild(imageModeHeader);
    }

    /**
     * Empty constructor, needed for serialization.
     */
    @objid ("7a54569c-55b6-11e2-877f-002564c97630")
    public GmCommunicationNode() {
        // empty constructor for the serialization
    }

    @objid ("7a54569f-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    @objid ("7a5456a7-55b6-11e2-877f-002564c97630")
    @Override
    public ModelElement getRepresentedElement() {
        return this.communicationNode;
    }

    @objid ("7a5456ae-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        StyleKey ret = STRUCTKEYS.getStyleKey(metakey);
        if (ret != null)
            return ret;
        
        ret = SIMPLEKEYS.getStyleKey(metakey);
        if (ret != null)
            return ret;
        
        ret = IMAGEKEYS.getStyleKey(metakey);
        return ret;
    }

    @objid ("7a5456b8-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        if (getMainNode() != null) {
            switch (getMainNode().getRepresentationMode()) {
                case SIMPLE:
                    return SIMPLEKEYS.getStyleKeys();
                case STRUCTURED:
                    return STRUCTKEYS.getStyleKeys();
                case IMAGE:
                    return IMAGEKEYS.getStyleKeys();
                default:
                    return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    @objid ("7a5456c0-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmCommunicationNode." + MINOR_VERSION_PROPERTY);
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

    @objid ("7a5456c6-55b6-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        // Returned result depends on current representation mode:
        List<GmNodeModel> ret = super.getVisibleChildren();
        if (getMainNode() != null) {
            switch (getMainNode().getRepresentationMode()) {
                case STRUCTURED:
                case SIMPLE:
                    GmNodeModel imageModeHeader = getFirstChild(IMAGE_MODE_HEADER);
                    ret.remove(imageModeHeader);
                    break;
                case IMAGE:
                default:
                    break;
        
            }
        }
        return ret;
    }

    @objid ("7a5456cf-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("7a5456d6-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmCommunicationNode." + MINOR_VERSION_PROPERTY,
                              Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7a55dd39-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.communicationNode = (CommunicationNode) resolveRef(getRepresentedRef());
    }

    @objid ("7a55dd3e-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("7a55dd43-55b6-11e2-877f-002564c97630")
    @Override
    public GmCommunicationNodePrimaryNode getMainNode() {
        return (GmCommunicationNodePrimaryNode) getFirstChild(GmPortContainer.MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Satellite, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Satellite.
     */
    @objid ("7a55dd49-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSatellite(final GmNodeModel childNode) {
        return IMAGE_MODE_HEADER.equals(childNode.getRoleInComposition());
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("7a55dd53-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return false;
    }

    @objid ("7a55dd5d-55b6-11e2-877f-002564c97630")
    @Override
    public void addStartingLink(final IGmLink link) {
        if (getMainNode() != null) {
            getMainNode().addStartingLink(link);
        } else {
            super.addStartingLink(link);
        }
    }

    @objid ("7a55dd64-55b6-11e2-877f-002564c97630")
    @Override
    public void addEndingLink(final IGmLink link) {
        if (getMainNode() != null) {
            getMainNode().addEndingLink(link);
        } else {
            super.addEndingLink(link);
        }
    }

    /**
     * Migration constructor from major version 0, should only be called by migrator.
     * @param oldVersionGm the instance to migrate from.
     */
    @objid ("7a55dd6b-55b6-11e2-877f-002564c97630")
    GmCommunicationNode(final _GmCommunicationNode oldVersionGm) {
        super(oldVersionGm.getDiagram(), oldVersionGm.getRepresentedRef());
        this.communicationNode = oldVersionGm.getRepresentedElement();
        
        GmCommunicationNodePrimaryNode primary = new GmCommunicationNodePrimaryNode(oldVersionGm);
        primary.setRoleInComposition(GmPortContainer.MAIN_NODE_ROLE);
        addChild(primary);
        GmModelElementFlatHeader imageModeHeader = new GmCommunicationNodeFlatHeader(oldVersionGm.getDiagram(), oldVersionGm.getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_MODE_HEADER);
        imageModeHeader.setLayoutData(PositionConstants.SOUTH);
        addChild(imageModeHeader);
    }

}
