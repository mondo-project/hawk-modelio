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
                                    

package org.modelio.diagram.editor.state.elements.state;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint.Border;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.AbstractStyleKeyProvider;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ConnectionPointReference;
import org.modelio.metamodel.uml.behavior.stateMachineModel.EntryPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.ExitPointPseudoState;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the {@link GmPortContainer} class for {@link State}.
 * 
 * @author fpoyer
 */
@objid ("f57c5a99-55b6-11e2-877f-002564c97630")
public class GmState extends GmPortContainer {
    @objid ("f57c5a9d-55b6-11e2-877f-002564c97630")
    private State element;

    /**
     * Current version of this Gm.
     */
    @objid ("f57c5aac-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("f57c5aaf-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("f57c5ab1-55b6-11e2-877f-002564c97630")
    private static final String IMAGE_LABEL_ROLE = "ImageLabel";

    @objid ("818be8e9-55c2-11e2-9337-002564c97630")
     static final AbstractStyleKeyProvider STRUCTURED_KEYS = new GmStateStructuredStyleKeys();

    @objid ("818c0ffa-55c2-11e2-9337-002564c97630")
     static final AbstractStyleKeyProvider SIMPLE_KEYS = new GmStateSimpleStyleKeys();

    @objid ("818c0ffc-55c2-11e2-9337-002564c97630")
     static final AbstractStyleKeyProvider IMAGE_KEYS = new GmStateImageStyleKeys();

    /**
     * Constructor.
     * @param diagram the diagram in which the element is unmasked.
     * @param el the unmasked element, can be <i>null</i>.
     * @param ref the unmasked element reference, must not be <i>null</i>..
     */
    @objid ("f57c5ab3-55b6-11e2-877f-002564c97630")
    public GmState(GmAbstractDiagram diagram, State el, MRef ref) {
        super(diagram, ref);
        this.element = el;
        
        GmStatePrimaryNode mainNode = new GmStatePrimaryNode(diagram, ref);
        mainNode.setRoleInComposition(MAIN_NODE_ROLE);
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(diagram, ref);
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(mainNode);
        super.addChild(imageModeHeader);
    }

    /**
     * A state may have as port:
     * <ul>
     * <li>entry point and exit points.
     * <li>connection point reference if the state references a state machine.
     * </ul>
     */
    @objid ("f57c5abf-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        if (this.element == null)
            return false;
        
        if (ConnectionPointReference.class.isAssignableFrom(type))
            return this.element.getSubMachine() != null;
        
        if (EntryPointPseudoState.class.isAssignableFrom(type) ||
                ExitPointPseudoState.class.isAssignableFrom(type)) {
            return this.element.getSubMachine() == null;
        }
        return false;
    }

    @objid ("f57de120-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        if (!el.isValid() || !el.getCompositionOwner().equals(this.element))
            return false;
        
        Class<? extends MObject> type = el.getClass();
        return ConnectionPointReference.class.isAssignableFrom(type) ||
                EntryPointPseudoState.class.isAssignableFrom(type) ||
                ExitPointPseudoState.class.isAssignableFrom(type);
    }

    @objid ("f57de128-55b6-11e2-877f-002564c97630")
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

    @objid ("f57de132-55b6-11e2-877f-002564c97630")
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
     * Empty constructor needed for deserialisation.
     */
    @objid ("f57de13a-55b6-11e2-877f-002564c97630")
    public GmState() {
        // Nothing specific to do.
    }

    @objid ("f57de13d-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmState." + MINOR_VERSION_PROPERTY);
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

    @objid ("f57de143-55b6-11e2-877f-002564c97630")
    @Override
    public State getRepresentedElement() {
        return this.element;
    }

    @objid ("f57de14a-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return getRepresentedElement();
    }

    @objid ("f57de151-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        if (this.getRelatedElement() != null && this.getRelatedElement().isValid()) {
            refreshPointsFromObModel();
        }
    }

    @objid ("f57de154-55b6-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        super.styleChanged(changedStyle);
        
        if (this.getRelatedElement() != null && this.getRelatedElement().isValid()) {
            refreshPointsFromObModel();
        }
    }

    @objid ("f57f67ba-55b6-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        super.styleChanged(property, newValue);
        
        if (property.equals(GmStateStructuredStyleKeys.AUTOSHOWPOINTS)) {
            if (this.getRelatedElement() != null && this.getRelatedElement().isValid()) {
                refreshPointsFromObModel();
            }
        }
    }

    /**
     * Automatically unmask points if asked.
     */
    @objid ("f57f67c3-55b6-11e2-877f-002564c97630")
    private void refreshPointsFromObModel() {
        if (arePointsAutoDisplayed()) {
            final State node = getRepresentedElement();
        
            // Unmask entry points on the WEST border
            for (EntryPointPseudoState point : node.getEntryPoint()) {
                if (getChild(new MRef(point)) == null) {
                    GmNodeModel gmPoint = getDiagram().unmask(this, point, Border.West);
                    gmPoint.setRoleInComposition(GmPortContainer.PORT_ROLE);
                }
            }
        
            // Unmask exit points on the EAST border
            for (ExitPointPseudoState point : node.getExitPoint()) {
                if (getChild(new MRef(point)) == null) {
                    GmNodeModel gmPoint = getDiagram().unmask(this, point, Border.East);
                    gmPoint.setRoleInComposition(GmPortContainer.PORT_ROLE);
                }
            }
        
            // Unmask connection point references on the SOUTH border
            for (ConnectionPointReference point : node.getConnection()) {
                if (getChild(new MRef(point)) == null) {
                    GmNodeModel gmPoint = getDiagram().unmask(this, point, Border.South);
                    gmPoint.setRoleInComposition(GmPortContainer.PORT_ROLE);
                }
            }
        }
    }

    @objid ("f57f67c6-55b6-11e2-877f-002564c97630")
    private boolean arePointsAutoDisplayed() {
        return getStyle().getProperty(GmStateStructuredStyleKeys.AUTOSHOWPOINTS);
    }

    @objid ("f57f67ca-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmState." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("f57f67d0-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (State) resolveRef(this.getRepresentedRef());
        
        GmDefaultFlatHeader imageModeHeader = new GmDefaultFlatHeader(getDiagram(), getRepresentedRef());
        imageModeHeader.setRoleInComposition(IMAGE_LABEL_ROLE);
        imageModeHeader.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        super.addChild(imageModeHeader, 1);
    }

    @objid ("f57f67d5-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("f57f67da-55b6-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        this.element = (State) resolveRef(this.getRepresentedRef());
    }

    @objid ("f57f67e0-55b6-11e2-877f-002564c97630")
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
    @objid ("f57f67e9-55b6-11e2-877f-002564c97630")
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
    @objid ("f57f67f3-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("f580ee5e-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

}
