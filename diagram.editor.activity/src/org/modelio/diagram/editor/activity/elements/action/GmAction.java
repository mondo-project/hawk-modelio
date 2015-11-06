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
                                    

package org.modelio.diagram.editor.activity.elements.action;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.editor.activity.elements.pincontainer.GmPinContainer;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityAction;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.OpaqueAction;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.metamodel.uml.behavior.activityModel.ValuePin;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * This class represents the graphic model of an {@link OpaqueAction action} .
 * <p>
 * Specialization of the GmPortContainer class for OpaqueAction.
 * 
 * @author fpoyer
 */
@objid ("29811d91-55b6-11e2-877f-002564c97630")
public class GmAction extends GmPinContainer {
    @objid ("29811d93-55b6-11e2-877f-002564c97630")
    private OpaqueAction element;

    /**
     * Current version of this Gm.
     */
    @objid ("29811d9c-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("29811d9f-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("29811da1-55b6-11e2-877f-002564c97630")
    private static final String IMAGE_LABEL_ROLE = "ImageLabel";

    @objid ("29811d98-55b6-11e2-877f-002564c97630")
     static final GmActionSimpleStyleKeys SIMPLE_KEYS = new GmActionSimpleStyleKeys();

    @objid ("2f43af53-58a2-11e2-9574-002564c97630")
     static final GmActionStructuredStyleKeys STRUCTURED_KEYS = new GmActionStructuredStyleKeys();

    @objid ("2f43af55-58a2-11e2-9574-002564c97630")
     static final GmActionImageStyleKeys IMAGE_KEYS = new GmActionImageStyleKeys();

    /**
     * Constructor.
     * @param diagram the diagram in which the action is unmasked.
     * @param el the unmasked action.
     * @param ref a reference to the unmasked action.
     */
    @objid ("29811da3-55b6-11e2-877f-002564c97630")
    public GmAction(GmAbstractDiagram diagram, OpaqueAction el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        GmActionPrimaryNode mainNode = new GmActionPrimaryNode(diagram, ref);
        mainNode.setRoleInComposition(MAIN_NODE_ROLE);
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(diagram, ref);
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(mainNode);
        super.addChild(imageModeHeader);
    }

    @objid ("29811daf-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return (InputPin.class.isAssignableFrom(type) || OutputPin.class.isAssignableFrom(type) || ValuePin.class.isAssignableFrom(type));
    }

    @objid ("2982a419-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return ((InputPin.class.isAssignableFrom(el.getClass()) ||
                 OutputPin.class.isAssignableFrom(el.getClass()) || ValuePin.class.isAssignableFrom(el.getClass())) &&
                el.isValid() && el.getCompositionOwner().equals(this.element));
    }

    @objid ("2982a421-55b6-11e2-877f-002564c97630")
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

    @objid ("2982a42b-55b6-11e2-877f-002564c97630")
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

    /**
     * Empty constructor needed for deserialization.
     */
    @objid ("2982a434-55b6-11e2-877f-002564c97630")
    public GmAction() {
        // Nothing specific to do.
    }

    @objid ("2982a437-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmAction." + MINOR_VERSION_PROPERTY);
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

    @objid ("2982a43d-55b6-11e2-877f-002564c97630")
    @Override
    public ActivityAction getRepresentedElement() {
        return this.element;
    }

    @objid ("2982a444-55b6-11e2-877f-002564c97630")
    @Override
    public ActivityAction getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("2982a44b-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmAction." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("2982a451-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (OpaqueAction) resolveRef(this.getRepresentedRef());
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(getDiagram(), getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(imageModeHeader, 1);
    }

    @objid ("2982a456-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("29842abc-55b6-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.element = (OpaqueAction) resolveRef(this.getRepresentedRef());
    }

    @objid ("29842ac2-55b6-11e2-877f-002564c97630")
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
    @objid ("29842acb-55b6-11e2-877f-002564c97630")
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
    @objid ("29842ad5-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("29842add-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

}
