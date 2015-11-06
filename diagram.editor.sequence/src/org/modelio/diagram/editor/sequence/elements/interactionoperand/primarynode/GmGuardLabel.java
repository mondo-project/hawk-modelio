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
                                    

package org.modelio.diagram.editor.sequence.elements.interactionoperand.primarynode;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.metamodel.uml.behavior.interactionModel.InteractionOperand;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Label for the guard of an InteractionOperand.
 * 
 * @author fpoyer
 */
@objid ("d9081cda-55b6-11e2-877f-002564c97630")
public class GmGuardLabel extends GmElementLabel {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("d9081cde-55b6-11e2-877f-002564c97630")
    private final int minorVersion = 0;

    @objid ("d9081ce1-55b6-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("d9081ce3-55b6-11e2-877f-002564c97630")
    @Override
    protected String computeLabel() {
        InteractionOperand interactionOperand = (InteractionOperand) getRelatedElement();
        if (interactionOperand != null && interactionOperand.isValid()) {
            return "[" + interactionOperand.getGuard() + "]";
        }
        return "[]";
    }

    /**
     * Empty c'tor for deserialisation.
     */
    @objid ("d9081ce8-55b6-11e2-877f-002564c97630")
    public GmGuardLabel() {
        super();
    }

    /**
     * C'tor.
     * @param diagram the diagram in which this gm is created.
     * @param relatedRef a reference to the represented element.
     */
    @objid ("d9081ceb-55b6-11e2-877f-002564c97630")
    public GmGuardLabel(final GmAbstractDiagram diagram, final MRef relatedRef) {
        super(diagram, relatedRef);
    }

    @objid ("d909a383-55b6-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmGuardLabel." + MINOR_VERSION_PROPERTY);
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

    @objid ("d909a389-55b6-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmGuardLabel." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("d909a38f-55b6-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
    }

    @objid ("d909a394-55b6-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
