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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnlanesetcontainer;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.bpmn.elements.bpmnlane.GmBpmnLane;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNoStyleCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * The node (doesn't have anything corresponding in the ObModel) that contains partitions. Used on the diagram
 * background to hold top-level partitions AND in partitions to hold sub-partitions.
 */
@objid ("6143b668-55b6-11e2-877f-002564c97630")
public class GmBpmnLaneSetContainer extends GmNoStyleCompositeNode {
    /**
     * The orientation of this container. Constant used to describe role of subpartitions. * public static final String
     * SUB_PARTITION = "Lane";
     */
    @objid ("6143b66c-55b6-11e2-877f-002564c97630")
    private BpmnLaneSet element;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("6143b670-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("6143b673-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("6143b675-55b6-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        if (BpmnLaneSet.class.isAssignableFrom(metaclass) || BpmnLane.class.isAssignableFrom(metaclass)) {
            return this;
        }
        return null;
    }

    @objid ("6143b67f-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        return BpmnLane.class.isAssignableFrom(type);
    }

    @objid ("6143b687-55b6-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return (BpmnLane.class.isAssignableFrom(el.getClass()) && el.getCompositionOwner()
                .equals(this.getRelatedElement()));
    }

    @objid ("6143b68f-55b6-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return RepresentationMode.STRUCTURED;
    }

    @objid ("6143b696-55b6-11e2-877f-002564c97630")
    @Override
    public BpmnLaneSet getRepresentedElement() {
        return this.element;
    }

    /**
     * @param diagram the diagram in which this partition container is used.
     */
    @objid ("6143b69d-55b6-11e2-877f-002564c97630")
    public GmBpmnLaneSetContainer(GmAbstractDiagram diagram, BpmnLaneSet theLaneSet, MRef relatedRef) {
        super(diagram, relatedRef);
        this.element = theLaneSet;
    }

    /**
     * Empty constructor needed for serialisation.
     */
    @objid ("61453cfc-55b6-11e2-877f-002564c97630")
    public GmBpmnLaneSetContainer() {
        // Nothing to do.
    }

    @objid ("61453cff-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmBpmnLaneSetContainer." + MINOR_VERSION_PROPERTY);
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

    @objid ("61453d05-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmBpmnLaneSetContainer." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * Returns a list of the contained GmPartition nodes.
     * @return a list of the contained GmPartition nodes.
     */
    @objid ("61453d0b-55b6-11e2-877f-002564c97630")
    public List<GmBpmnLane> getPartitions() {
        List<GmBpmnLane> partitions = new ArrayList<>();
        for (GmNodeModel p : getChildren()) {
            partitions.add((GmBpmnLane) p);
        }
        return partitions;
    }

    @objid ("61453d12-55b6-11e2-877f-002564c97630")
    @Override
    public void removeChild(GmNodeModel child) {
        super.removeChild(child);
        // If removed child was the last, delete self.
        if (!this.hasChildren() && child instanceof GmBpmnLane)
            delete();
    }

    @objid ("61453d18-55b6-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.element;
    }

    @objid ("61453d1f-55b6-11e2-877f-002564c97630")
    @Override
    public void addChild(final GmNodeModel child) {
        if (child instanceof GmBpmnLane) {
        
            GmBpmnLane lane = (GmBpmnLane) child;
        
            for (GmModel ownedNode : getChildren()) {
                if (ownedNode.getRelatedElement() instanceof BpmnFlowElement) {
                    // GM side
                    removeChild((GmNodeModel) ownedNode);
                    lane.getCompositeFor(ownedNode.getRelatedElement().getClass()).addChild((GmNodeModel) ownedNode);
        
                    // OB Side
                    BpmnFlowElement flowElement = (BpmnFlowElement) ownedNode.getRelatedElement();
                    
                    for (BpmnLane elane : new ArrayList<>(flowElement.getLane())) {
                        flowElement.getLane().remove(elane);
                    }
                    
                    flowElement.getLane().add((BpmnLane)lane.getRelatedElement());
                }
            }
        
        }
        
        super.addChild(child);
    }

    @objid ("61453d26-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        this.element = (BpmnLaneSet) resolveRef(this.getRepresentedRef());
    }

    @objid ("61453d2b-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
