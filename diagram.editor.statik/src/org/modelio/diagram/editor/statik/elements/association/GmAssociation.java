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
                                    

package org.modelio.diagram.editor.statik.elements.association;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.statik.elements.assocqualifier.GmQualifierGroup;
import org.modelio.diagram.editor.statik.elements.rolecardinalitylabel.GmRoleCardinalityLabel;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
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
import org.modelio.metamodel.uml.statik.AggregationKind;
import org.modelio.metamodel.uml.statik.Association;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link Association} link.
 * 
 * @author cmarin
 */
@objid ("33e8e24a-55b7-11e2-877f-002564c97630")
public class GmAssociation extends GmLink {
    @objid ("33e8e24e-55b7-11e2-877f-002564c97630")
    private AssociationEnd sourceRole;

    @objid ("33e8e253-55b7-11e2-877f-002564c97630")
    private boolean fromNavigable;

    @objid ("33e8e254-55b7-11e2-877f-002564c97630")
    private boolean toNavigable;

    @objid ("33e8e258-55b7-11e2-877f-002564c97630")
    private AssociationEnd targetRole;

    @objid ("33e8e25b-55b7-11e2-877f-002564c97630")
    private AggregationKind fromAggregation;

    @objid ("33e8e25e-55b7-11e2-877f-002564c97630")
    private AggregationKind toAggregation;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("33ea68ba-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("33ea68bd-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("5a6d5515-5bd5-11e2-9e33-00137282c51b")
    private MRef roleRef = null;

    @objid ("5a6d5518-5bd5-11e2-9e33-00137282c51b")
    private static final GmAssocStructuredStyleKeys STRUCTURED_KEYS = new GmAssocStructuredStyleKeys();

    @objid ("bede0e49-984c-40fd-801a-45332989d2c9")
    private AssociationEnd oppositeRole;

    /**
     * Constructor for deserialization only.
     */
    @objid ("33ea68bf-55b7-11e2-877f-002564c97630")
    public GmAssociation() {
        // Nothing to do.
    }

    /**
     * Creates a GmAssociation.
     * @param role The represented association role, may be null
     * @param diagram The diagram
     * @param roleRef The represented association role reference, must not be null
     * @param associationRef The represented association reference, must not be null
     */
    @objid ("33ea68c2-55b7-11e2-877f-002564c97630")
    public GmAssociation(GmAbstractDiagram diagram, AssociationEnd sourceRole, MRef roleRef, MRef associationRef) {
        super(diagram, associationRef);
        this.roleRef = roleRef;
        this.sourceRole = sourceRole;
        
        if (sourceRole != null) {
            this.oppositeRole = sourceRole.getOpposite();
        
            // initialize fields
            updateNavigability();
        
            this.toAggregation = this.oppositeRole!=null ? this.oppositeRole.getAggregation() : AggregationKind.KINDISASSOCIATION;
            this.fromAggregation = sourceRole.getAggregation();
        
            // Create extensions
            final MRef oppositeRoleRef = new MRef(this.oppositeRole);
            GmFractionalConnectionLocator constraint;
        
            // Qualifier extensions
            // The location is not given here, the edit part handles it directly.
            addExtension(new GmQualifierGroup(diagram, roleRef, sourceRole), null);
            addExtension(new GmQualifierGroup(diagram, oppositeRoleRef, this.oppositeRole), null);
        
            // source side extensions
            addExtension(ExtensionLocation.TargetNW, new GmRoleNameLabel(diagram, sourceRole, roleRef));
            addExtension(ExtensionLocation.TargetSE, new GmRoleCardinalityLabel(diagram, sourceRole, roleRef));
            constraint = new GmFractionalConnectionLocator(0.25, 0, -10);
            addExtension(new GmInfoFlowsGroup(diagram, oppositeRoleRef), constraint);
            constraint = new GmFractionalConnectionLocator(0.25, 0, 0, false);
            addExtension(new GmInformationFlowArrow(diagram, oppositeRoleRef), constraint);
        
            // Target side extensions
            addExtension(ExtensionLocation.SourceNW, new GmRoleNameLabel(diagram, this.oppositeRole, oppositeRoleRef));
            addExtension(ExtensionLocation.SourceSE, new GmRoleCardinalityLabel(diagram, this.oppositeRole, oppositeRoleRef));
            constraint = new GmFractionalConnectionLocator(0.75, 0, -10);
            addExtension(new GmInfoFlowsGroup(diagram, roleRef), constraint);
            constraint = new GmFractionalConnectionLocator(0.75, 0, 0, true);
            addExtension(new GmInformationFlowArrow(diagram, roleRef), constraint);
        
            // Middle extensions
            addExtension(ExtensionLocation.MiddleSE, new GmAssociationLabel(diagram, associationRef));
        
        }
    }

    /**
     * @return the source side aggregation kind.
     */
    @objid ("33ea68d1-55b7-11e2-877f-002564c97630")
    public AggregationKind getFromAggregation() {
        return this.fromAggregation;
    }

    @objid ("33ea68d8-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getFromElement() {
        final Classifier sourceClass = this.sourceRole!=null ? this.sourceRole.getSource() : null;
        if (sourceClass != null)
            return sourceClass;
        
        if (this.oppositeRole == null)
            return null;
        return this.oppositeRole.getTarget();
    }

    @objid ("33ea68df-55b7-11e2-877f-002564c97630")
    @Override
    public AssociationEnd getRelatedElement() {
        return this.sourceRole;
    }

    @objid ("33ea68e6-55b7-11e2-877f-002564c97630")
    @Override
    public AssociationEnd getRepresentedElement() {
        return this.sourceRole;
    }

    /**
     * Get the represented association role.
     * <p>
     * Returns null if the represented role is not in the model.
     * @return the represented association role.
     */
    @objid ("33ea68ed-55b7-11e2-877f-002564c97630")
    public AssociationEnd getRepresentedRole() {
        return this.sourceRole;
    }

    @objid ("33ea68f4-55b7-11e2-877f-002564c97630")
    @Override
    public StyleKey getStyleKey(MetaKey metakey) {
        return STRUCTURED_KEYS.getStyleKey(metakey);
    }

    @objid ("33ebef59-55b7-11e2-877f-002564c97630")
    @Override
    public List<StyleKey> getStyleKeys() {
        return STRUCTURED_KEYS.getStyleKeys();
    }

    /**
     * @return the target side aggregation kind.
     */
    @objid ("33ebef62-55b7-11e2-877f-002564c97630")
    public AggregationKind getToAggregation() {
        return this.toAggregation;
    }

    @objid ("33ebef69-55b7-11e2-877f-002564c97630")
    @Override
    public MObject getToElement() {
        final Classifier oppSource = this.oppositeRole!=null ? this.oppositeRole.getSource() : null;
        if (oppSource != null )
            return oppSource;
        return this.sourceRole!=null ? this.sourceRole.getTarget() : null;
    }

    /**
     * @return the source side navigability.
     */
    @objid ("33ebef70-55b7-11e2-877f-002564c97630")
    public boolean isFromNavigable() {
        return this.fromNavigable;
    }

    /**
     * @return the target side navigability.
     */
    @objid ("33ebef75-55b7-11e2-877f-002564c97630")
    public boolean isToNavigable() {
        return this.toNavigable;
    }

    @objid ("33ebef7a-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        if (this.oppositeRole != null && this.oppositeRole.isValid()) {
            this.toAggregation = this.oppositeRole.getAggregation();
        }
        
        if (this.sourceRole != null && this.sourceRole.isValid()) {
            this.fromAggregation = this.sourceRole.getAggregation();
        }
        
        updateNavigability();
        
        // post change event
        firePropertyChange(PROPERTY_LAYOUTDATA, null, this);
    }

    @objid ("33ebef7d-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        out.writeProperty("representedRole", this.roleRef);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmAssociation." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * @return the opposite association role.
     */
    @objid ("33ebef83-55b7-11e2-877f-002564c97630")
    public AssociationEnd getOppositeRole() {
        return this.oppositeRole;
    }

    @objid ("33ebef89-55b7-11e2-877f-002564c97630")
    @Override
    protected void readLink(IDiagramReader in) {
        Object versionProperty = in.readProperty("GmAssociation." + MINOR_VERSION_PROPERTY);
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

    /**
     * Get the represented association role reference.
     * @return the represented association role reference.
     */
    @objid ("33ebef8f-55b7-11e2-877f-002564c97630")
    public MRef getRepresentedRoleRef() {
        return this.roleRef;
    }

    @objid ("33ebef96-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("b2288c2e-6e20-48d1-ad3c-0557821bf16c")
    private void updateNavigability() {
        if (this.sourceRole != null && this.sourceRole.isValid() && this.oppositeRole != null && this.oppositeRole.isValid()) {
            this.fromNavigable = this.oppositeRole.isNavigable();
            this.toNavigable = this.sourceRole.isNavigable();
        } else {
            this.fromNavigable = false;
            this.toNavigable = false;
        }
    }

    /**
     * Modelio 2.2.1 -> Modelio 3.0 migration.
     * Replace the Association label with a GmAssociationLabel.
     */
    @objid ("90d6879f-3fca-4073-830e-cd8449644688")
    private void read_0(IDiagramReader in) {
        read_1(in);
        
        // Look for an Association lable to migrate... there should be one
        GmDefaultFlatHeader oldLabel = null;
        for (GmNodeModel extension : this.getExtensions()) {
            if (extension.getRepresentedRef().mc.equals("ModelElement") && extension instanceof GmDefaultFlatHeader) {
                oldLabel = (GmDefaultFlatHeader) extension;
                break;
            }
        }
        
        if (oldLabel != null) {
            // Create a new label, with the appropriate Gm
            final GmAssociationLabel newLabel = new GmAssociationLabel(getDiagram(), oldLabel.getRepresentedRef());
            addExtension(ExtensionLocation.MiddleSE, newLabel);
            newLabel.setLayoutData(oldLabel.getLayoutData());
            
            // Delete the old association label
            removeExtension(oldLabel);
            oldLabel.delete();
        }
    }

    @objid ("d4a4f2e1-631c-4582-b525-38f412e71997")
    private void read_1(IDiagramReader in) {
        this.roleRef = (MRef) in.readProperty("representedRole");
        this.sourceRole = (AssociationEnd) resolveRef(this.roleRef);
        
        if (this.sourceRole != null) {
            this.oppositeRole = this.sourceRole.getOpposite();
            if (this.oppositeRole != null) {
                this.fromAggregation = this.oppositeRole.getAggregation();
            } else {
                this.fromAggregation = AggregationKind.KINDISASSOCIATION;
            }
        
            this.toAggregation = this.sourceRole.getAggregation();
        
        } else {
            this.oppositeRole = null;
            this.toAggregation = AggregationKind.KINDISASSOCIATION;
            this.fromAggregation = AggregationKind.KINDISASSOCIATION;
        }
        
        updateNavigability();
    }

}
