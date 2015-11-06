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
                                    

package org.modelio.diagram.editor.statik.elements.bpmnbehavior;

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
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.rootElements.BpmnBehavior;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the {@link GmPortContainer} class for {@link BpmnBehavior}.
 */
@objid ("3419b62a-55b7-11e2-877f-002564c97630")
public class GmBpmnBehavior extends GmPortContainer {
    @objid ("3419b62e-55b7-11e2-877f-002564c97630")
    private BpmnBehavior element;

    /**
     * Current version of this Gm.
     */
    @objid ("3419b637-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("3419b63a-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("3419b63c-55b7-11e2-877f-002564c97630")
    private static final String IMAGE_LABEL_ROLE = "ImageLabel";

    @objid ("5afec487-5bd5-11e2-9e33-00137282c51b")
     static final GmBpmnBehaviorStructuredStyleKeys STRUCTURED_KEYS = new GmBpmnBehaviorStructuredStyleKeys();

    @objid ("5afec489-5bd5-11e2-9e33-00137282c51b")
     static final GmBpmnBehaviorSimpleStyleKeys SIMPLE_KEYS = new GmBpmnBehaviorSimpleStyleKeys();

    @objid ("5afec48b-5bd5-11e2-9e33-00137282c51b")
     static final GmBpmnBehaviorImageStyleKeys IMAGE_KEYS = new GmBpmnBehaviorImageStyleKeys();

    /**
     * Constructor.
     * @param diagram the diagram in which the callBehavior is unmasked.
     * @param el the unmasked callBehavior.
     * @param ref a reference to the unmasked callBehavior.
     */
    @objid ("3419b63e-55b7-11e2-877f-002564c97630")
    public GmBpmnBehavior(final GmAbstractDiagram diagram, final BpmnBehavior el, final MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        GmBpmnBehaviorPrimaryNode mainNode = new GmBpmnBehaviorPrimaryNode(diagram, ref);
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
    @objid ("3419b64d-55b7-11e2-877f-002564c97630")
    public GmBpmnBehavior() {
        // Nothing specific to do.
    }

    @objid ("3419b650-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(final Class<? extends MObject> type) {
        return false;
    }

    @objid ("3419b659-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(final MObject el) {
        return false;
    }

    @objid ("341b3cbd-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("341b3cc4-55b7-11e2-877f-002564c97630")
    @Override
    public BpmnBehavior getRepresentedElement() {
        return this.element;
    }

    @objid ("341b3ccb-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(final MetaKey metakey) {
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

    @objid ("341b3cd6-55b7-11e2-877f-002564c97630")
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

    @objid ("341b3cdf-55b7-11e2-877f-002564c97630")
    @Override
    public void read(final IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnBehavior." + MINOR_VERSION_PROPERTY);
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

    @objid ("341b3ce6-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnBehavior." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("341b3cec-55b7-11e2-877f-002564c97630")
    private void read_0(final IDiagramReader in) {
        super.read(in);
        this.element = (BpmnBehavior) resolveRef(this.getRepresentedRef());
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(getDiagram(), getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(imageModeHeader, 1);
    }

    @objid ("341b3cf2-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("341b3cf7-55b7-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.element = (BpmnBehavior) resolveRef(this.getRepresentedRef());
    }

    @objid ("341cc359-55b7-11e2-877f-002564c97630")
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
    @objid ("341cc362-55b7-11e2-877f-002564c97630")
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
    @objid ("341cc36c-55b7-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("341cc374-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

}