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
                                    

package org.modelio.diagram.editor.statik.elements.narylink;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.instancelink.GmLinkRoleCardinalityLabel;
import org.modelio.diagram.editor.statik.elements.instancelink.GmLinkRoleNameLabel;
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
import org.modelio.metamodel.uml.statik.NaryLinkEnd;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link NaryLinkEnd} brach of n-ary association.
 * <p>
 * The source is the link node.<br>
 * The target the {@link NaryLinkEnd#getLinked()} instance.
 * <p>
 * The link style is a proxy on the link node style.
 * 
 * @author cmarin
 */
@objid ("35e379d9-55b7-11e2-877f-002564c97630")
public class GmNLinkEndLink extends GmLink {
    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("35e5003a-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("35e5003d-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("6164b40f-5bd5-11e2-9e33-00137282c51b")
     static final NLinkStructuredStyleKeys STRUCTURED_KEYS = new NLinkStructuredStyleKeys();

    @objid ("b024fe96-585d-4a59-b0d0-53f910b6ede0")
    private NaryLinkEnd role;

    /**
     * Constructor for deserialization only.
     */
    @objid ("35e5003f-55b7-11e2-877f-002564c97630")
    public GmNLinkEndLink() {
        // Nothing to do.
    }

    /**
     * Creates a GmAssociation.
     * @param diagram The diagram
     * @param role The represented association role, may be null
     * @param roleRef The represented association role reference, must not be null
     */
    @objid ("35e50042-55b7-11e2-877f-002564c97630")
    public GmNLinkEndLink(GmAbstractDiagram diagram, NaryLinkEnd role, MRef roleRef) {
        super(diagram, roleRef);
        this.role = role;
        
        if (role != null) {
            // Create extensions
            GmFractionalConnectionLocator constraint;
        
            // Target side extensions
            addExtension(ExtensionLocation.TargetNW, new GmNaryLinkRoleNameLabel(diagram, this.role, roleRef));
            addExtension(ExtensionLocation.TargetSE, new GmNaryLinkRoleCardinalityLabel(diagram, this.role, roleRef));
            constraint = new GmFractionalConnectionLocator(0.75, 0, -10);
            addExtension(new GmInfoFlowsGroup(diagram, roleRef), constraint);
            constraint = new GmFractionalConnectionLocator(0.75, 0, 0, true);
            addExtension(new GmInformationFlowArrow(diagram, roleRef), constraint);
        
        }
    }

    @objid ("35e5004e-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        return this.role.getNaryLink();
    }

    @objid ("35e50055-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getRelatedElement() {
        return this.role;
    }

    @objid ("35e5005c-55b7-11e2-877f-002564c97630")
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
    @objid ("35e50063-55b7-11e2-877f-002564c97630")
    public NaryLinkEnd getRepresentedRole() {
        return this.role;
    }

    @objid ("35e5006a-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STRUCTURED_KEYS.getStyleKey(metakey);
    }

    @objid ("35e686da-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STRUCTURED_KEYS.getStyleKeys();
    }

    @objid ("35e686e3-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        return this.role.getSource();
    }

    @objid ("35e686ef-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        // post change event
        firePropertyChange(PROPERTY_LAYOUTDATA, null, this);
    }

    @objid ("35e686f2-55b7-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        Object versionProperty = in.readProperty("GmNLinkEndLink." + MINOR_VERSION_PROPERTY);
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

    @objid ("35e686f8-55b7-11e2-877f-002564c97630")
    @Override
    protected IStyle createStyle(final GmAbstractDiagram aDiagram) {
        return new ProxyStyle(aDiagram.getStyle());
    }

    @objid ("35e68703-55b7-11e2-877f-002564c97630")
    @Override
    public void setFrom(final IGmLinkable from) {
        super.setFrom(from);
        if (from != null)
            getStyle().setCascadedStyle(from.getStyle());
    }

    @objid ("35e6870a-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmNLinkEndLink." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("35e68710-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    /**
     * @return the target side navigability.
     */
    @objid ("ed20daeb-dd22-4475-a5c1-c2f1631ee3c1")
    public boolean isToNavigable() {
        // Nary links are always navigable
        return true;
    }

    @objid ("00ba8920-44da-4f02-ae60-11e92c98f174")
    protected void read_0() {
        this.role = (NaryLinkEnd) resolveRef(getRepresentedRef());
             
        // Look for labels to migrate... there should be two
        GmLinkRoleNameLabel oldRoleLabel = null;
        GmLinkRoleCardinalityLabel oldCardinalityLabel = null;
        for (GmNodeModel extension : this.getExtensions()) {
            if (extension instanceof GmLinkRoleNameLabel) {
                oldRoleLabel = (GmLinkRoleNameLabel) extension;
            } else if (extension instanceof GmLinkRoleCardinalityLabel) {
                oldCardinalityLabel = (GmLinkRoleCardinalityLabel) extension;
            }
        }
        
        if (oldRoleLabel != null) {
            // Create a new label, with the appropriate Gm
            final GmNaryLinkRoleNameLabel newRoleLabel = new GmNaryLinkRoleNameLabel(getDiagram(), this.role, oldRoleLabel.getRepresentedRef());
            addExtension(ExtensionLocation.TargetNW, newRoleLabel);
            newRoleLabel.setLayoutData(oldRoleLabel.getLayoutData());
            
            // Delete the old association label
            removeExtension(oldRoleLabel);
            oldRoleLabel.delete();
        }
        
        if (oldCardinalityLabel != null) {
            // Create a new label, with the appropriate Gm
            final GmNaryLinkRoleCardinalityLabel newCardinalityLabel = new GmNaryLinkRoleCardinalityLabel(getDiagram(), this.role, oldCardinalityLabel.getRepresentedRef());
            addExtension(ExtensionLocation.TargetSE, newCardinalityLabel);
            newCardinalityLabel.setLayoutData(oldCardinalityLabel.getLayoutData());
            
            // Delete the old association label
            removeExtension(oldCardinalityLabel);
            oldCardinalityLabel.delete();
        }
    }

    @objid ("d24d2a8b-08b9-4a61-af13-56c01dfdb717")
    protected void read_1() {
        this.role = (NaryLinkEnd) resolveRef(getRepresentedRef());
    }

}
