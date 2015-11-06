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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmndataobject.dataobject;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataImageStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataLabel;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataObjectStyleKeys;
import org.modelio.diagram.editor.bpmn.elements.bpmndataobject.GmBpmnDataSimpleStyleKeys;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.bpmn.objects.BpmnDataObject;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Graphical model for a {@link Note}.
 */
@objid ("60bbe8c3-55b6-11e2-877f-002564c97630")
public class GmBpmnDataObject extends GmPortContainer {
    @objid ("60bbe8c7-55b6-11e2-877f-002564c97630")
    private BpmnDataObject theElement;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("60bbe8d0-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("60bbe8d3-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("c4da03af-59a6-11e2-ae45-002564c97630")
    public static final GmBpmnDataObjectStyleKeys STRUCTURED_KEYS = new GmBpmnDataObjectStyleKeys();

    @objid ("c4da03b1-59a6-11e2-ae45-002564c97630")
    public static final GmBpmnDataImageStyleKeys IMAGE_KEYS = new GmBpmnDataImageStyleKeys();

    @objid ("c4dc650e-59a6-11e2-ae45-002564c97630")
    public static final GmBpmnDataSimpleStyleKeys SIMPLE_KEYS = new GmBpmnDataSimpleStyleKeys();

    /**
     * Constructor to use only for deserialization.
     */
    @objid ("60bbe8d5-55b6-11e2-877f-002564c97630")
    public GmBpmnDataObject() {
        super();
    }

    /**
     * Creates a GmNote.
     * @param diagram The diagram owning the node
     * @param theElement The represented note element
     * @param ref The represented note reference
     */
    @objid ("60bbe8d8-55b6-11e2-877f-002564c97630")
    public GmBpmnDataObject(GmAbstractDiagram diagram, BpmnDataObject theElement, MRef ref) {
        super(diagram,ref);
        
        GmBpmnDataObjectPrimaryNode mainNode = new GmBpmnDataObjectPrimaryNode(diagram, ref);
        mainNode.setRoleInComposition(MAIN_NODE_ROLE);
        this.addChild(mainNode);
        
        this.theElement = theElement;
        GmBpmnDataLabel label = new GmBpmnDataLabel(diagram, ref);
        label.setRoleInComposition(GmPortContainer.SATELLITE_ROLE);
        label.setLayoutData(Integer.valueOf(PositionConstants.SOUTH));
        this.addChild(label);
    }

    @objid ("60bbe8e4-55b6-11e2-877f-002564c97630")
    @Override
    public BpmnDataObject getRepresentedElement() {
        return this.theElement;
    }

    @objid ("60bbe8eb-55b6-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        switch (getPrimaryNode().getRepresentationMode()) {
            case IMAGE:
                return IMAGE_KEYS.getStyleKey(metakey);
            case SIMPLE:
                return SIMPLE_KEYS.getStyleKey(metakey);
            case STRUCTURED:
                return STRUCTURED_KEYS.getStyleKey(metakey);
            default:
                return null;
        }
    }

    @objid ("60bbe8f3-55b6-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        switch (getPrimaryNode().getRepresentationMode()) {
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

    @objid ("60bbe8fb-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnDataObject." + MINOR_VERSION_PROPERTY);
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

    @objid ("60bbe901-55b6-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        if (this.theElement != null) {
            firePropertyChange(PROPERTY_LABEL, null, this.theElement.getName());
        }
    }

    @objid ("60bbe904-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return false;
    }

    @objid ("60bd6f60-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    @objid ("60bd6f68-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.theElement;
    }

    @objid ("60bd6f6f-55b6-11e2-877f-002564c97630")
    private GmModel getPrimaryNode() {
        List<GmNodeModel> model = getChildren("MainNode");
        if (model.size() > 0)
            return model.get(0);
        return this;
    }

    @objid ("60bd6f75-55b6-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        if (getPrimaryNode().getRepresentationMode().equals(RepresentationMode.SIMPLE)) {
            return (getChildren("MainNode"));
        }
        return super.getVisibleChildren();
    }

    @objid ("60bd6f7e-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnDataObject." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("60bd6f84-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.theElement = (BpmnDataObject) resolveRef(this.getRepresentedRef());
    }

    @objid ("60bd6f89-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * Get the main node that is decorated with ports and satellites.
     * @return a GmNodeModel, can't be <code>null</code>.
     */
    @objid ("60bd6f8e-55b6-11e2-877f-002564c97630")
    @Override
    public GmNodeModel getMainNode() {
        return getFirstChild(MAIN_NODE_ROLE);
    }

    /**
     * Is this node a Port, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Port.
     */
    @objid ("60bd6f96-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isPort(final GmNodeModel childNode) {
        return GmPortContainer.PORT_ROLE.equals(childNode.getRoleInComposition());
    }

    /**
     * Is this node a Satellite, which position is defined relatively to the Main Node's bounds.
     * @param childNode the node to check.
     * @return <code>true</code> if the node is a Satellite.
     */
    @objid ("60bef5fc-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSatellite(final GmNodeModel childNode) {
        return GmPortContainer.SATELLITE_ROLE.equals(childNode.getRoleInComposition());
    }

}
