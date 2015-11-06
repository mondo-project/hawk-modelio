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
                                    

package org.modelio.diagram.editor.activity.elements.expansionregion;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.AbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionRegion;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialisation of the GmPortContainer class for ExpansionRegion.
 * 
 * @author fpoyer
 */
@objid ("2a5e5e3f-55b6-11e2-877f-002564c97630")
public class GmExpansionRegion extends GmPortContainer {
    @objid ("2a5e5e43-55b6-11e2-877f-002564c97630")
    private ExpansionRegion element;

    /**
     * Current version of this Gm.
     */
    @objid ("2a5e5e52-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("2a5e5e55-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("2a5e5e57-55b6-11e2-877f-002564c97630")
    private static final String IMAGE_LABEL_ROLE = "ImageLabel";

    @objid ("d0803aca-55c0-11e2-9337-002564c97630")
     static final AbstractStyleKeyProvider STRUCTURED_KEYS = new GmExpansionRegionStructuredStyleKeys();

    @objid ("d0803acc-55c0-11e2-9337-002564c97630")
     static final AbstractStyleKeyProvider SIMPLE_KEYS = new GmExpansionRegionSimpleStyleKeys();

    @objid ("d0803ace-55c0-11e2-9337-002564c97630")
     static final AbstractStyleKeyProvider IMAGE_KEYS = new GmExpansionRegionImageStyleKeys();

    /**
     * Constructor.
     * @param diagram the diagram in which the expansionRegion is unmasked.
     * @param el the unmasked expansionRegion.
     * @param ref a reference to the unmasked expansionRegion.
     */
    @objid ("2a5e5e59-55b6-11e2-877f-002564c97630")
    public GmExpansionRegion(GmAbstractDiagram diagram, ExpansionRegion el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        GmExpansionRegionPrimaryNode mainNode = new GmExpansionRegionPrimaryNode(diagram, ref);
        mainNode.setRoleInComposition(MAIN_NODE_ROLE);
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(diagram, ref);
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(mainNode);
        super.addChild(imageModeHeader);
    }

    /**
     * Empty constructor needed for deserialisation.
     */
    @objid ("2a5fe4bf-55b6-11e2-877f-002564c97630")
    public GmExpansionRegion() {
        // Nothing specific to do.
    }

    @objid ("2a5fe4c2-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return (ExpansionNode.class.isAssignableFrom(type));
    }

    @objid ("2a5fe4ca-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return ((ExpansionNode.class.isAssignableFrom(el.getClass())) && el.getCompositionOwner()
                                                                            .equals(this.element));
    }

    @objid ("2a5fe4d2-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        StyleKey styleKey = STRUCTURED_KEYS.getStyleKey(MetaKey.REPMODE);
        if (styleKey != null) {
            RepresentationMode mode = getStyle().getProperty(styleKey);
            switch (mode) {
                case IMAGE:
                    return IMAGE_KEYS.getStyleKey(metakey);
                case SIMPLE:
                    return SIMPLE_KEYS.getStyleKey(metakey);
                case STRUCTURED:
                    return STRUCTURED_KEYS.getStyleKey(metakey);
            }
        }
        return null;
    }

    @objid ("2a5fe4dc-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        StyleKey styleKey = STRUCTURED_KEYS.getStyleKey(MetaKey.REPMODE);
        if (styleKey != null) {
            RepresentationMode mode = getStyle().getProperty(styleKey);
            switch (mode) {
                case IMAGE:
                    return IMAGE_KEYS.getStyleKeys();
                case SIMPLE:
                    return SIMPLE_KEYS.getStyleKeys();
                case STRUCTURED:
                    return STRUCTURED_KEYS.getStyleKeys();
            }
        }
        return Collections.emptyList();
    }

    @objid ("2a5fe4e5-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmExpansionRegion." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
            case 0: {
                read_0(in);
                break;
            }
            case 1: {
                read_1(in);
                break;
            }
            default: {
                assert (false) : "version number not covered!";
                // reading as last handled version: 1
                read_1(in);
                break;
            }
        }
    }

    @objid ("2a5fe4eb-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.element;
    }

    @objid ("2a5fe4f2-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("2a616b5a-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmExpansionRegion." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("2a616b60-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (ExpansionRegion) resolveRef(this.getRepresentedRef());
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(getDiagram(), getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(imageModeHeader, 1);
    }

    @objid ("2a616b65-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("2a616b6a-55b6-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.element = (ExpansionRegion) resolveRef(this.getRepresentedRef());
    }

    @objid ("2a616b70-55b6-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        List<GmNodeModel> ret = super.getVisibleChildren();
        
        // Returned result depends on current representation mode of the primary node
        GmNodeModel firstChild = getMainNode();
        if (firstChild == null || (firstChild.getRepresentationMode() != RepresentationMode.IMAGE)) {
            // Remove the header used for image mode.
            ret.remove(getFirstChild(IMAGE_LABEL_ROLE));
        }
        return ret;
    }

    /**
     * Is this node a Satellite, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Satellite.
     */
    @objid ("2a616b79-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSatellite(final GmNodeModel childNode) {
        String role = childNode.getRoleInComposition();
        return GmPortContainer.SATELLITE_ROLE.equals(role)
                || IMAGE_LABEL_ROLE.equals(role);
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("2a616b83-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("2a616b8b-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

}
