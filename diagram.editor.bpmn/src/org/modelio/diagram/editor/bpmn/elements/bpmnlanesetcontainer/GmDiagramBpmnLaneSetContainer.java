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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLaneSet;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specialization of the Partition Container that delete itself when last child is removed.
 */
@objid ("6149d0de-55b6-11e2-877f-002564c97630")
public class GmDiagramBpmnLaneSetContainer extends GmBpmnLaneSetContainer {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("6149d0e0-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("6149d0e3-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("6149d0e5-55b6-11e2-877f-002564c97630")
    @Override
    public void removeChild(GmNodeModel child) {
        super.removeChild(child);
        // If removed child was the last, delete self.
        if (!this.hasChildren())
            delete();
    }

    /**
     * Empty c'tor for deserialisation.
     */
    @objid ("6149d0eb-55b6-11e2-877f-002564c97630")
    public GmDiagramBpmnLaneSetContainer() {
        // Nothing to do.
    }

    /**
     * Default C'tor.
     * @param diagram the diagram.
     */
    @objid ("6149d0ee-55b6-11e2-877f-002564c97630")
    public GmDiagramBpmnLaneSetContainer(GmAbstractDiagram diagram, BpmnLaneSet theLaneSet, MRef relatedRef) {
        super(diagram, theLaneSet, relatedRef);
    }

    @objid ("6149d0fa-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmDiagramBpmnLaneSetContainer." + MINOR_VERSION_PROPERTY);
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

    @objid ("6149d100-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmDiagramBpmnLaneSetContainer." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("6149d106-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("6149d10b-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
