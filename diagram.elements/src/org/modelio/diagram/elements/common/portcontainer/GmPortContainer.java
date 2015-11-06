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
                                    

package org.modelio.diagram.elements.common.portcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Try 2 of Base class for all port containers.
 * 
 * @author fpoyer
 */
@objid ("7ee86a52-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmPortContainer extends GmCompositeNode {
    @objid ("7ee86a57-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7ee86a5f-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    /**
     * The Main Node of the assembly. this role should probably be used on only one node of an assembly at any time.
     */
    @objid ("8f477a66-1e83-11e2-8cad-001ec947c8cc")
    protected static final String MAIN_NODE_ROLE = "MainNode";

    /**
     * A Port which position is defined relatively to the Main Node's bounds.
     */
    @objid ("8f477a6c-1e83-11e2-8cad-001ec947c8cc")
    protected static final String PORT_ROLE = "Port";

    /**
     * A Satellite which position is defined relatively to the Main Node's bounds.
     */
    @objid ("8f477a72-1e83-11e2-8cad-001ec947c8cc")
    protected static final String SATELLITE_ROLE = "Satellite";

    /**
     * Empty constructor for deserialization.
     */
    @objid ("7ee86a69-1dec-11e2-8cad-001ec947c8cc")
    public GmPortContainer() {
        super();
    }

    /**
     * Constructor.
     * @param diagram The diagram in which this port container will be unmasked.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("7ee86a6c-1dec-11e2-8cad-001ec947c8cc")
    public GmPortContainer(final GmAbstractDiagram diagram, final MRef relatedRef) {
        super(diagram, relatedRef);
    }

    /**
     * Overridden so that any child added without a specific role is defined as a Port. Subclasses may override this
     * method so that some particular children are defined as Satellites rather than Ports.
     */
    @objid ("7ee86a73-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void addChild(GmNodeModel child) {
        // Default: all child are PORTS
        if (child.getRoleInComposition() == null || child.getRoleInComposition().equals("")) {
            child.setRoleInComposition(PORT_ROLE);
        } else if (child.getRoleInComposition().equals(MAIN_NODE_ROLE)) {
            // Main node should use the style of the container by default.
            child.getStyle().setCascadedStyle(this.getStyle());
        }
        super.addChild(child);
    }

    @objid ("7ee86a78-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean canUnmask(MObject el) {
        return el.getCompositionOwner().equals(this.getRelatedElement()) && canCreate(el.getClass());
    }

    @objid ("7ee86a7e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        if (canCreate(metaclass))
            return this;
        // else
        // Try delegating to main node.
        GmNodeModel mainNode = this.getFirstChild(MAIN_NODE_ROLE);
        if (mainNode != null && mainNode instanceof GmCompositeNode) {
            GmCompositeNode composite = ((GmCompositeNode) mainNode).getCompositeFor(metaclass);
            if (composite != null)
                return composite;
        }
        // else
        return null;
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("7eeacc9d-1dec-11e2-8cad-001ec947c8cc")
    public abstract GmNodeModel getMainNode();

    @objid ("7eeacca0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * A port container is always in structured mode.
     */
    @objid ("7eeacca5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("7eeaccab-1dec-11e2-8cad-001ec947c8cc")
    public abstract boolean isPort(final GmNodeModel childNode);

    /**
     * Is this node a Satellite, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Satellite.
     */
    @objid ("7eeaccb0-1dec-11e2-8cad-001ec947c8cc")
    public abstract boolean isSatellite(final GmNodeModel childNode);

    @objid ("7eeaccb5-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmPortContainer." + MINOR_VERSION_PROPERTY);
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

    @objid ("7eeaccb9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmPortContainer." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7eeaccbd-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

}
