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
                                    

package org.modelio.diagram.editor.statik.elements.naryassoc;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.association.GmRoleNameLabel;
import org.modelio.diagram.editor.statik.elements.rolecardinalitylabel.GmRoleCardinalityLabel;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.extensions.GmFractionalConnectionLocator;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInfoFlowsGroup;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInformationFlowArrow;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.ProxyStyle;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link NaryAssociationEnd} brach of n-ary association.
 * <p>
 * The source is the association node.<br>
 * The target the {@link NaryAssociationEnd#getOwner()} class.
 * <p>
 * The link style is a proxy on the association node style.
 * 
 * @author cmarin
 */
@objid ("35c06160-55b7-11e2-877f-002564c97630")
public class GmNAssocEndLink extends GmLink {
    @objid ("35c06164-55b7-11e2-877f-002564c97630")
    private NaryAssociationEnd role;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("35c0616d-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("35c06170-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("60b4460c-5bd5-11e2-9e33-00137282c51b")
     static final NAssocStructuredStyleKeys STRUCTURED_KEYS = new NAssocStructuredStyleKeys();

    /**
     * Constructor for deserialization only.
     */
    @objid ("35c06172-55b7-11e2-877f-002564c97630")
    public GmNAssocEndLink() {
        // Nothing to do.
    }

    /**
     * Creates a GmAssociation.
     * @param diagram The diagram
     * @param role The represented association role, may be null
     * @param roleRef The represented association role reference, must not be null
     */
    @objid ("35c06175-55b7-11e2-877f-002564c97630")
    public GmNAssocEndLink(GmAbstractDiagram diagram, NaryAssociationEnd role, MRef roleRef) {
        super(diagram, roleRef);
        this.role = role;
        
        if (role != null) {
            // Create extensions
            GmFractionalConnectionLocator constraint;
        
            // Target side extensions
            addExtension(ExtensionLocation.TargetNW, new GmNaryRoleNameLabel(diagram, role, roleRef));
            addExtension(ExtensionLocation.TargetSE, new GmNaryRoleCardinalityLabel(diagram, role, roleRef));
            constraint = new GmFractionalConnectionLocator(0.75, 0, -10);
            addExtension(new GmInfoFlowsGroup(diagram, roleRef), constraint);
            constraint = new GmFractionalConnectionLocator(0.75, 0, 0, true);
            addExtension(new GmInformationFlowArrow(diagram, roleRef), constraint);
        
        }
    }

    @objid ("35c1e7e0-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.role.getNaryAssociation();
    }

    @objid ("35c1e7e7-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.role;
    }

    @objid ("35c1e7ee-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.role;
    }

    /**
     * Get the represented association role.
     * <p>
     * Returns null if the represented role is not in the model.
     * @return the represented association role.
     */
    @objid ("35c1e7f5-55b7-11e2-877f-002564c97630")
    public NaryAssociationEnd getRepresentedRole() {
        return this.role;
    }

    @objid ("35c1e7fc-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STRUCTURED_KEYS.getStyleKey(metakey);
    }

    @objid ("35c1e806-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STRUCTURED_KEYS.getStyleKeys();
    }

    @objid ("35c1e80f-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.role.getOwner();
    }

    @objid ("35c1e81b-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        // post change event
        firePropertyChange(PROPERTY_LAYOUTDATA, null, this);
    }

    @objid ("35c36e79-55b7-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        Object versionProperty = in.readProperty("GmAssociation." + MINOR_VERSION_PROPERTY);
        int readVersion = versionProperty == null ? 0 : ((Integer) versionProperty).intValue();
        switch (readVersion) {
        case 0: {
            read_0();
            break;
        }
        case 1: {
            read_1();
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 1
            read_1();
            break;
        }
        }
    }

    @objid ("35c36e7f-55b7-11e2-877f-002564c97630")
    @Override
    protected IStyle createStyle(final GmAbstractDiagram aDiagram) {
        return new ProxyStyle(aDiagram.getStyle());
    }

    @objid ("35c36e8a-55b7-11e2-877f-002564c97630")
    @Override
    public void setFrom(final IGmLinkable from) {
        super.setFrom(from);
        if (from != null)
            getStyle().setCascadedStyle(from.getStyle());
    }

    @objid ("35c36e91-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNAssocEndLink." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("35c36e97-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("494293bc-bdac-4a16-8b7e-7d2e872102d0")
    protected void read_0() {
        this.role = (NaryAssociationEnd) resolveRef(getRepresentedRef());
             
        // Look for labels to migrate... there should be two
        GmRoleNameLabel oldRoleLabel = null;
        GmRoleCardinalityLabel oldCardinalityLabel = null;
        for (GmNodeModel extension : this.getExtensions()) {
            if (extension instanceof GmRoleNameLabel) {
                oldRoleLabel = (GmRoleNameLabel) extension;
            } else if (extension instanceof GmRoleCardinalityLabel) {
                oldCardinalityLabel = (GmRoleCardinalityLabel) extension;
            }
        }
        
        if (oldRoleLabel != null) {
            // Create a new label, with the appropriate Gm
            final GmNaryRoleNameLabel newRoleLabel = new GmNaryRoleNameLabel(getDiagram(), this.role, oldRoleLabel.getRepresentedRef());
            addExtension(ExtensionLocation.TargetNW, newRoleLabel);
            newRoleLabel.setLayoutData(oldRoleLabel.getLayoutData());
            
            // Delete the old association label
            removeExtension(oldRoleLabel);
            oldRoleLabel.delete();
        }
        
        if (oldCardinalityLabel != null) {
            // Create a new label, with the appropriate Gm
            final GmNaryRoleCardinalityLabel newCardinalityLabel = new GmNaryRoleCardinalityLabel(getDiagram(), this.role, oldCardinalityLabel.getRepresentedRef());
            addExtension(ExtensionLocation.TargetSE, newCardinalityLabel);
            newCardinalityLabel.setLayoutData(oldCardinalityLabel.getLayoutData());
            
            // Delete the old association label
            removeExtension(oldCardinalityLabel);
            oldCardinalityLabel.delete();
        }
    }

    @objid ("6fc79db6-0b5c-451f-92b0-8e8b63eed547")
    protected void read_1() {
        this.role = (NaryAssociationEnd) resolveRef(getRepresentedRef());
    }

}
