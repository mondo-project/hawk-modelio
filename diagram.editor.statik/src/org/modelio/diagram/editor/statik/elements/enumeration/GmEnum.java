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
                                    

package org.modelio.diagram.editor.statik.elements.enumeration;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.editor.statik.elements.templatecontainer.GmTemplateContainer;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Enumeration;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the {@link GmPortContainer} class for {@link Class}.
 * 
 * @author fpoyer
 */
@objid ("34d8724a-55b7-11e2-877f-002564c97630")
public class GmEnum extends GmTemplateContainer {
    @objid ("34d8724c-55b7-11e2-877f-002564c97630")
    private Enumeration element;

    /**
     * Current version of this Gm.
     */
    @objid ("34d87255-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("34d87258-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("34d8725a-55b7-11e2-877f-002564c97630")
    private static final String IMAGE_LABEL_ROLE = "ImageLabel";

    @objid ("5c91b1f1-5bd5-11e2-9e33-00137282c51b")
     static final EnumStructuredStyleKeys STRUCTURED_KEYS = new EnumStructuredStyleKeys();

    @objid ("5c91b1f3-5bd5-11e2-9e33-00137282c51b")
     static final EnumSimpleStyleKeys SIMPLE_KEYS = new EnumSimpleStyleKeys();

    @objid ("5c94144b-5bd5-11e2-9e33-00137282c51b")
     static final EnumImageStyleKeys IMAGE_KEYS = new EnumImageStyleKeys();

    /**
     * Constructor.
     * @param diagram the diagram in which the class is unmasked.
     * @param el the unmasked class.
     * @param ref a reference to the unmasked class.
     */
    @objid ("34d8725c-55b7-11e2-877f-002564c97630")
    public GmEnum(GmAbstractDiagram diagram, Enumeration el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        GmEnumPrimaryNode mainNode = new GmEnumPrimaryNode(diagram, ref);
        mainNode.setRoleInComposition(MAIN_NODE_ROLE);
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(diagram, ref);
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(mainNode);
        super.addChild(imageModeHeader);
    }

    @objid ("34d87268-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(java.lang.Class<? extends MObject> type) {
        return (Port.class.isAssignableFrom(type));
    }

    @objid ("34d87270-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return (Port.class.isAssignableFrom(el.getClass()) && el.getCompositionOwner().equals(this.element));
    }

    @objid ("34d87278-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        StyleKey ret = STRUCTURED_KEYS.getStyleKey(metakey);
        if (ret != null)
            return ret;
        
        ret = SIMPLE_KEYS.getStyleKey(metakey);
        if (ret != null)
            return ret;
        
        ret = IMAGE_KEYS.getStyleKey(metakey);
        return ret;
    }

    @objid ("34d9f8e0-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        switch (getRepresentationMode()) {
            case IMAGE:
                return IMAGE_KEYS.getStyleKeys();
            case SIMPLE:
                return SIMPLE_KEYS.getStyleKeys();
            case STRUCTURED:
                return STRUCTURED_KEYS.getStyleKeys();
            default:
                return Collections.emptyList();
        }
    }

    /**
     * Empty constructor needed for deserialization.
     */
    @objid ("34d9f8e8-55b7-11e2-877f-002564c97630")
    public GmEnum() {
        // Nothing specific to do.
    }

    @objid ("34d9f8eb-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmEnum." + MINOR_VERSION_PROPERTY);
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

    @objid ("34d9f8f1-55b7-11e2-877f-002564c97630")
    @Override
    public Enumeration getRepresentedElement() {
        return this.element;
    }

    @objid ("34d9f8f8-55b7-11e2-877f-002564c97630")
    @Override
    public Enumeration getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("34d9f8ff-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmEnum." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("34d9f905-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (Enumeration) resolveRef(this.getRepresentedRef());
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(getDiagram(), getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(imageModeHeader, 1);
    }

    @objid ("34d9f90a-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("34d9f90f-55b7-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.element = (Enumeration) resolveRef(this.getRepresentedRef());
    }

    @objid ("34db7f7b-55b7-11e2-877f-002564c97630")
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
    @objid ("34db7f84-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSatellite(final GmNodeModel childNode) {
        String role = childNode.getRoleInComposition();
        return "body content as satellite".equals(childNode.getRoleInComposition())
                || GmPortContainer.SATELLITE_ROLE.equals(role)
                || IMAGE_LABEL_ROLE.equals(role);
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("34db7f8e-55b7-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("34db7f96-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

}