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
                                    

package org.modelio.diagram.editor.statik.elements.instancelink;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.header.GmDefaultModelElementHeader;
import org.modelio.diagram.elements.core.link.ExtensionLocation;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.extensions.GmFractionalConnectionLocator;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInfoFlowsGroup;
import org.modelio.diagram.elements.umlcommon.informationflowgroup.GmInformationFlowArrow;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a Link.
 */
@objid ("35589ed3-55b7-11e2-877f-002564c97630")
public class GmInstanceLink extends GmLink {
    @objid ("35589edc-55b7-11e2-877f-002564c97630")
    private boolean fromNavigable;

    @objid ("355a2559-55b7-11e2-877f-002564c97630")
    private boolean toNavigable;

    /**
     * Current version of this Gm.
     */
    @objid ("355a2561-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("355a2564-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    /**
     * Represented element reference.
     */
    @objid ("58a85648-5bd5-11e2-9e33-00137282c51b")
    private MRef roleRef = null;

    @objid ("58a8564a-5bd5-11e2-9e33-00137282c51b")
    private static final InstanceLinkStructuredStyleKeys STRUCTURED_KEYS = new InstanceLinkStructuredStyleKeys();

    @objid ("981ef922-7b10-4ccd-95b4-0a79cfec67f2")
    private LinkEnd sourceRole;

    @objid ("daac7fa5-83dc-444c-a92d-7e9bb17d970c")
    private LinkEnd oppositeRole;

    /**
     * Constructor for deserialization.
     */
    @objid ("355a2566-55b7-11e2-877f-002564c97630")
    public GmInstanceLink() {
        // Nothing to do.
    }

    /**
     * Creates a GmLink.
     * @param diagram The diagram containing the link.
     * @param sourceRole The represented element.
     * @param roleRef The represented role reference. May not be null.
     * @param linkRef The represented link reference. May not be null.
     */
    @objid ("355a2569-55b7-11e2-877f-002564c97630")
    public GmInstanceLink(GmAbstractDiagram diagram, LinkEnd sourceRole, MRef roleRef, MRef linkRef) {
        super(diagram, linkRef);
        
        this.sourceRole = sourceRole;
        this.roleRef = roleRef;
        
        if (sourceRole != null) {
            this.oppositeRole = sourceRole.getOpposite();
        
            // initialize fields
            updateNavigability();
        
            // Create extensions
            GmFractionalConnectionLocator constraint;
            final MRef oppositeRoleRef = new MRef(this.oppositeRole);
        
           // source side extensions
            addExtension(ExtensionLocation.TargetNW, new GmLinkRoleNameLabel(diagram, sourceRole, roleRef));
            addExtension(ExtensionLocation.TargetSE, new GmLinkRoleCardinalityLabel(diagram, sourceRole, roleRef));
            constraint = new GmFractionalConnectionLocator(0.25, 0, -10);
            addExtension(new GmInfoFlowsGroup(diagram, oppositeRoleRef), constraint);
            constraint = new GmFractionalConnectionLocator(0.25, 0, 0, false);
            addExtension(new GmInformationFlowArrow(diagram, oppositeRoleRef), constraint);
        
            // Target side extensions
            addExtension(ExtensionLocation.SourceNW, new GmLinkRoleNameLabel(diagram, this.oppositeRole, oppositeRoleRef));
            addExtension(ExtensionLocation.SourceSE, new GmLinkRoleCardinalityLabel(diagram, this.oppositeRole, oppositeRoleRef));
            constraint = new GmFractionalConnectionLocator(0.75, 0, -10);
            addExtension(new GmInfoFlowsGroup(diagram, roleRef), constraint);
            constraint = new GmFractionalConnectionLocator(0.75, 0, 0, true);
            addExtension(new GmInformationFlowArrow(diagram, roleRef), constraint);
        
            // Middle extensions
            addExtension(ExtensionLocation.MiddleSE, new GmLinkLabel(diagram, linkRef));
        
        }
    }

    @objid ("355a2578-55b7-11e2-877f-002564c97630")
    @Override
    public LinkEnd getRelatedElement() {
        return this.sourceRole;
    }

    @objid ("355a257f-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STRUCTURED_KEYS.getStyleKey(metakey);
    }

    @objid ("355a2589-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STRUCTURED_KEYS.getStyleKeys();
    }

    @objid ("355a2592-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        updateNavigability();
        
        // post change event
        firePropertyChange(PROPERTY_LAYOUTDATA, null, this);
    }

    /**
     * @return the opposite role of the represented role, or null if none (model broken)
     */
    @objid ("355a2595-55b7-11e2-877f-002564c97630")
    LinkEnd getOppositeRole() {
        return this.oppositeRole;
    }

    /**
     * Get the source side navigability.
     * @return true if the source side is navigable.
     */
    @objid ("355babf9-55b7-11e2-877f-002564c97630")
    public boolean isFromNavigable() {
        return this.fromNavigable;
    }

    /**
     * Get the target side navigability.
     * @return true if the target side of the link is navigable.
     */
    @objid ("355babfe-55b7-11e2-877f-002564c97630")
    public boolean isToNavigable() {
        return this.toNavigable;
    }

    @objid ("355bac03-55b7-11e2-877f-002564c97630")
    @Override
    public Instance getFromElement() {
        return this.sourceRole.getSource() != null ? this.sourceRole.getSource() : this.oppositeRole.getTarget();
    }

    @objid ("355bac0a-55b7-11e2-877f-002564c97630")
    @Override
    public Instance getToElement() {
        return this.sourceRole.getTarget() != null ? this.sourceRole.getTarget() : this.oppositeRole.getSource();
    }

    @objid ("355bac11-55b7-11e2-877f-002564c97630")
    protected void read_1(IDiagramReader in) {
        this.roleRef = (MRef) in.readProperty("representedRole");
        this.sourceRole = (LinkEnd) resolveRef(this.roleRef);
        
        if (this.sourceRole != null) {
            this.oppositeRole = this.sourceRole.getOpposite();
            updateNavigability();
        } else {
            this.fromNavigable = false;
            this.toNavigable = true;
        }
    }

    @objid ("355bac17-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        out.writeProperty("representedRole", this.roleRef);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmInstanceLink." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * Get the represented link role.
     * @return the link role.
     */
    @objid ("355bac28-55b7-11e2-877f-002564c97630")
    public LinkEnd getRepresentedRole() {
        return this.sourceRole;
    }

    @objid ("355bac2f-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRepresentedElement() {
        return this.sourceRole;
    }

    @objid ("355bac36-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("de485a13-e009-4578-ba08-4df9f63ae825")
    private void updateNavigability() {
        if (this.sourceRole != null && this.sourceRole.isValid() && this.oppositeRole != null && this.oppositeRole.isValid()) {
            this.fromNavigable = this.oppositeRole.isNavigable();
            this.toNavigable = this.sourceRole.isNavigable();
        } else {
            this.fromNavigable = false;
            this.toNavigable = false;
        }
    }

    @objid ("d4f28602-c0d9-4cd1-9898-f16627290e01")
    private void read_0(IDiagramReader in) {
        read_1(in);
        
        // Look for an Association lable to migrate... there should be one
        GmDefaultModelElementHeader oldLabel = null;
        for (GmNodeModel extension : this.getExtensions()) {
            if (extension.getRepresentedRef().mc.equals("ModelElement") && extension.getClass() == GmDefaultModelElementHeader.class) {
                    oldLabel = (GmDefaultModelElementHeader) extension;
            }
        }
        
        if (oldLabel != null) {
            // Create a new label, with the appropriate Gm
            final GmLinkLabel newLabel = new GmLinkLabel(getDiagram(), getRepresentedRef());
            addExtension(ExtensionLocation.MiddleSE, newLabel);
            newLabel.setLayoutData(oldLabel.getLayoutData());
            
            // Delete the old association label
            removeExtension(oldLabel);
            oldLabel.delete();
        }
    }

    @objid ("eee8ce21-1a32-451e-a590-2ded2bc75706")
    @Override
    protected void readLink(IDiagramReader in) {
        Object versionProperty = in.readProperty("GmInstanceLink." + MINOR_VERSION_PROPERTY);
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

}
