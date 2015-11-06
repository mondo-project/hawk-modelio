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
                                    

package org.modelio.diagram.editor.activity.elements.callbehavior;

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
import org.modelio.metamodel.uml.behavior.activityModel.CallBehaviorAction;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.OutputPin;
import org.modelio.metamodel.uml.behavior.activityModel.ValuePin;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the {@link GmPinContainer} class for {@link CallBehaviorAction}.
 * 
 * @author fpoyer
 */
@objid ("29b80c2e-55b6-11e2-877f-002564c97630")
public class GmCallBehavior extends GmPinContainer {
    @objid ("29b80c30-55b6-11e2-877f-002564c97630")
    private CallBehaviorAction element;

    /**
     * Current version of this Gm.
     */
    @objid ("29b80c39-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("29b9929b-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("29b9929d-55b6-11e2-877f-002564c97630")
    private static final String IMAGE_LABEL_ROLE = "ImageLabel";

    @objid ("29b80c35-55b6-11e2-877f-002564c97630")
     static final GmCallBehaviorSimpleStyleKeys SIMPLE_KEYS = new GmCallBehaviorSimpleStyleKeys();

    @objid ("2faece15-58a2-11e2-9574-002564c97630")
     static final GmCallBehaviorStructuredStyleKeys STRUCTURED_KEYS = new GmCallBehaviorStructuredStyleKeys();

    @objid ("2faece17-58a2-11e2-9574-002564c97630")
     static final GmCallBehaviorImageStyleKeys IMAGE_KEYS = new GmCallBehaviorImageStyleKeys();

    /**
     * Constructor.
     * @param diagram the diagram in which the callBehavior is unmasked.
     * @param el the unmasked callBehavior.
     * @param ref a reference to the unmasked callBehavior.
     */
    @objid ("29b9929f-55b6-11e2-877f-002564c97630")
    public GmCallBehavior(GmAbstractDiagram diagram, CallBehaviorAction el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        GmCallBehaviorPrimaryNode mainNode = new GmCallBehaviorPrimaryNode(diagram, ref);
        mainNode.setRoleInComposition(MAIN_NODE_ROLE);
        
        GmDefaultFlatHeader imageModeHeader = new GmBehaviorFlatLabel(diagram, ref);
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(mainNode);
        super.addChild(imageModeHeader);
    }

    /**
     * Empty constructor needed for deserialisation.
     */
    @objid ("29b992ab-55b6-11e2-877f-002564c97630")
    public GmCallBehavior() {
        // Nothing specific to do.
    }

    @objid ("29b992ae-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return (InputPin.class.isAssignableFrom(type) || ValuePin.class.isAssignableFrom(type) || OutputPin.class.isAssignableFrom(type));
    }

    @objid ("29b992b6-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return ((InputPin.class.isAssignableFrom(el.getClass()) ||
                 ValuePin.class.isAssignableFrom(el.getClass()) || OutputPin.class.isAssignableFrom(el.getClass())) && el.getCompositionOwner()
                                                                                                                           .equals(this.element));
    }

    @objid ("29b992be-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("29b992c5-55b6-11e2-877f-002564c97630")
    @Override
    public ActivityAction getRepresentedElement() {
        return this.element;
    }

    @objid ("29b992cc-55b6-11e2-877f-002564c97630")
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

    @objid ("29b992d6-55b6-11e2-877f-002564c97630")
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

    @objid ("29bb193d-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmCallBehavior." + MINOR_VERSION_PROPERTY);
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

    @objid ("29bb1943-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmCallBehavior." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("29bb1949-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (CallBehaviorAction) resolveRef(this.getRepresentedRef());
        
        GmDefaultFlatHeader imageModeHeader = new GmBehaviorFlatLabel(getDiagram(), getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(imageModeHeader, 1);
    }

    @objid ("29bb194e-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("29bb1953-55b6-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.element = (CallBehaviorAction) resolveRef(this.getRepresentedRef());
    }

    @objid ("29bb1959-55b6-11e2-877f-002564c97630")
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
    @objid ("29bb1962-55b6-11e2-877f-002564c97630")
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
    @objid ("29bb196c-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("29bb1974-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

}