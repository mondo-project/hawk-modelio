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
                                    

package org.modelio.diagram.editor.statik.elements.clazz;

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
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the {@link GmPortContainer} class for {@link Class}.
 * 
 * @author fpoyer
 */
@objid ("343b47da-55b7-11e2-877f-002564c97630")
public class GmClass extends GmTemplateContainer {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("343b47e5-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("343b47e8-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("343b47e3-55b7-11e2-877f-002564c97630")
    private static final GmClassImageStyleKeys IMAGE_KEYS = new GmClassImageStyleKeys();

    @objid ("a510512f-55c2-11e2-9337-002564c97630")
    private org.modelio.metamodel.uml.statik.Class element;

    @objid ("5b6c70b3-5bd5-11e2-9e33-00137282c51b")
     static final GmClassStructuredStyleKeys STRUCTURED_KEYS = new GmClassStructuredStyleKeys();

    @objid ("5b6c70b5-5bd5-11e2-9e33-00137282c51b")
    private static final GmClassSimpleStyleKeys SIMPLE_KEYS = new GmClassSimpleStyleKeys();

    /**
     * Empty constructor needed for deserialisation.
     */
    @objid ("343b47ea-55b7-11e2-877f-002564c97630")
    public GmClass() {
        // Nothing specific to do.
    }

    /**
     * Constructor.
     * @param diagram the diagram in which the class is unmasked.
     * @param el the unmasked class.
     * @param ref a reference to the unmasked class.
     */
    @objid ("343b47ed-55b7-11e2-877f-002564c97630")
    public GmClass(GmAbstractDiagram diagram, org.modelio.metamodel.uml.statik.Class el, MRef ref) {
        super(diagram, new GmClassPrimaryNode(diagram, el, ref), ref);
        this.element = el;
        
        final GmImageNameSpaceLabel interfaceLabel = new GmImageNameSpaceLabel(diagram, getRepresentedElement(), ref);
        interfaceLabel.setRoleInComposition(SATELLITE_ROLE);
        interfaceLabel.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        
        addChild(interfaceLabel);
    }

    @objid ("343b47f9-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return (Port.class.isAssignableFrom(type));
    }

    @objid ("343b4801-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return (Port.class.isAssignableFrom(el.getClass()) && el.getCompositionOwner().equals(this.element));
    }

    @objid ("343b4809-55b7-11e2-877f-002564c97630")
    @Override
    public org.modelio.metamodel.uml.statik.Class getRepresentedElement() {
        return this.element;
    }

    @objid ("343b4810-55b7-11e2-877f-002564c97630")
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

    @objid ("343cce81-55b7-11e2-877f-002564c97630")
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

    @objid ("343cce89-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmClass." + MINOR_VERSION_PROPERTY);
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

    @objid ("343cce8f-55b7-11e2-877f-002564c97630")
    @Override
    public org.modelio.metamodel.uml.statik.Class getRelatedElement() {
        return getRepresentedElement();
    }

    /**
     * Automatically unmask ports if asked.
     */
    @objid ("343cce96-55b7-11e2-877f-002564c97630")
    private void refreshPortsFromObModel() {
        if (arePortsAutoDisplayed()) {
            final org.modelio.metamodel.uml.statik.Class node = getRepresentedElement();
            if (node == null || !node.isValid()) {
                return;
            }
            for (BindableInstance part : node.getInternalStructure()) {
                if (part instanceof Port && getChild(new MRef(part)) == null) {
                    GmNodeModel gmPort = getDiagram().unmask(this, part, Border.East);
                    gmPort.setRoleInComposition(GmPortContainer.PORT_ROLE);
                }
            }
        
        }
    }

    @objid ("343cce99-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        refreshPortsFromObModel();
    }

    /**
     * @return true if ports are to be unmasked automatically.
     */
    @objid ("343cce9c-55b7-11e2-877f-002564c97630")
    protected Boolean arePortsAutoDisplayed() {
        return getStyle().getProperty(GmClassStructuredStyleKeys.SHOWPORTS);
    }

    @objid ("343ccea2-55b7-11e2-877f-002564c97630")
    private GmModel getPrimaryNode() {
        List<GmNodeModel> model = getChildren("MainNode");
        if (model.size() > 0)
            return model.get(0);
        return this;
    }

    @objid ("343ccea8-55b7-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        if (getPrimaryNode().getRepresentationMode().equals(RepresentationMode.SIMPLE)
                || getPrimaryNode().getRepresentationMode().equals(RepresentationMode.STRUCTURED)) {
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

    @objid ("343cceb1-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmClass." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("343cceb7-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (org.modelio.metamodel.uml.statik.Class) resolveRef(this.getRepresentedRef());
    }

    @objid ("343ccebc-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("343e551d-55b7-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("343e5525-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

    /**
     * Is this node a Satellite, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Satellite.
     */
    @objid ("343e552f-55b7-11e2-877f-002564c97630")
    @Override
    public boolean isSatellite(final GmNodeModel childNode) {
        return "body content as satellite".equals(childNode.getRoleInComposition())
                || GmPortContainer.SATELLITE_ROLE.equals(childNode.getRoleInComposition());
    }

}
