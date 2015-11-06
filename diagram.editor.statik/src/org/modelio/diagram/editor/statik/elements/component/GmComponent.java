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
                                    

package org.modelio.diagram.editor.statik.elements.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.editor.statik.elements.imagenamespacelabel.GmImageNameSpaceLabel;
import org.modelio.diagram.editor.statik.elements.templatecontainer.GmTemplateContainer;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint.Border;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the {@link GmPortContainer} class for {@link Class}.
 * 
 * @author fpoyer
 */
@objid ("34a617ab-55b7-11e2-877f-002564c97630")
public class GmComponent extends GmTemplateContainer {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("34a617b6-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("34a617b9-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("34a617b0-55b7-11e2-877f-002564c97630")
     static final ComponentStructuredStyleKeys STRUCTURED_KEYS = new ComponentStructuredStyleKeys();

    @objid ("34a617b2-55b7-11e2-877f-002564c97630")
    private static final ComponentSimpleStyleKeys SIMPLE_KEYS = new ComponentSimpleStyleKeys();

    @objid ("34a617b4-55b7-11e2-877f-002564c97630")
    private static final ComponentImageStyleKeys IMAGE_KEYS = new ComponentImageStyleKeys();

    @objid ("a647e6ed-55c2-11e2-9337-002564c97630")
    private Class element;

    /**
     * Empty constructor needed for deserialisation.
     */
    @objid ("34a617bb-55b7-11e2-877f-002564c97630")
    public GmComponent() {
        // Nothing specific to do.
    }

    /**
     * Constructor.
     * @param diagram the diagram in which the class is unmasked.
     * @param el the unmasked class.
     * @param ref a reference to the unmasked class.
     */
    @objid ("34a617be-55b7-11e2-877f-002564c97630")
    public GmComponent(GmAbstractDiagram diagram, Class el, MRef ref) {
        super(diagram, new GmComponentPrimaryNode(diagram, ref), ref);
        this.element = el;
        
        final GmImageNameSpaceLabel interfaceLabel = new GmImageNameSpaceLabel(diagram,
                                                                               getRepresentedElement(),
                                                                               ref);
        interfaceLabel.setRoleInComposition(SATELLITE_ROLE);
        interfaceLabel.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        addChild(interfaceLabel);
    }

    @objid ("34a617ca-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(java.lang.Class<? extends MObject> type) {
        return (Port.class.isAssignableFrom(type));
    }

    @objid ("34a79e39-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return (Port.class.isAssignableFrom(el.getClass()) && el.getCompositionOwner().equals(this.element));
    }

    @objid ("34a79e41-55b7-11e2-877f-002564c97630")
    @Override
    public Class getRepresentedElement() {
        return this.element;
    }

    @objid ("34a79e48-55b7-11e2-877f-002564c97630")
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

    @objid ("34a79e52-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        switch (getMainNode().getRepresentationMode()) {
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

    @objid ("34a79e5a-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmComponent." + MINOR_VERSION_PROPERTY);
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

    @objid ("34a79e60-55b7-11e2-877f-002564c97630")
    @Override
    public Class getRelatedElement() {
        return getRepresentedElement();
    }

    /**
     * Automatically unmask ports if asked.
     */
    @objid ("34a79e67-55b7-11e2-877f-002564c97630")
    private void refreshPortsFromObModel() {
        if (arePortsAutoDisplayed()) {
            final Class node = getRepresentedElement();
            if (node != null && node.isValid()) {
                for (BindableInstance part : node.getInternalStructure()) {
                    if (part instanceof Port && getChild(new MRef(part)) == null) {
                        GmNodeModel gmPort = getDiagram().unmask(this, part, Border.East);
                        gmPort.setRoleInComposition(GmPortContainer.PORT_ROLE);
                    }
                }
            }
        }
    }

    @objid ("34a79e6a-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        refreshPortsFromObModel();
    }

    /**
     * @return true if ports are to be unmasked automatically.
     */
    @objid ("34a79e6d-55b7-11e2-877f-002564c97630")
    protected Boolean arePortsAutoDisplayed() {
        return getStyle().getProperty(ComponentStructuredStyleKeys.SHOWPORTS);
    }

    @objid ("34a79e73-55b7-11e2-877f-002564c97630")
    private GmModel getPrimaryNode() {
        List<GmNodeModel> model = getChildren("MainNode");
        if (model.size() > 0)
            return model.get(0);
        return this;
    }

    @objid ("34a79e79-55b7-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        if (getPrimaryNode().getRepresentationMode().equals(RepresentationMode.SIMPLE) ||
                getPrimaryNode().getRepresentationMode().equals(RepresentationMode.STRUCTURED)) {
            List<GmNodeModel> childrens = super.getVisibleChildren();
            List<GmNodeModel> labels = new ArrayList<>();
            for (GmNodeModel children : childrens) {
                if (children instanceof GmImageNameSpaceLabel) {
                    labels.add(children);
                }
            }
            childrens.removeAll(labels);
            return childrens;
        }
        return super.getVisibleChildren();
    }

    @objid ("34a924de-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmComponent." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("34a924e4-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (Class) resolveRef(this.getRepresentedRef());
    }

    @objid ("34a924e9-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("34a924ee-55b7-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("34a924f6-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

    /**
     * Is this node a Satellite, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Satellite.
     */
    @objid ("34a92500-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSatellite(final GmNodeModel childNode) {
        return "body content as satellite".equals(childNode.getRoleInComposition()) ||
                GmPortContainer.SATELLITE_ROLE.equals(childNode.getRoleInComposition());
    }

}
