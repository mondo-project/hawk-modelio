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
                                    

package org.modelio.diagram.editor.usecase.elements.actor;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.editor.statik.elements.attributegroup.GmAttributeGroup;
import org.modelio.diagram.editor.statik.elements.internalstructure.GmInternalStructure;
import org.modelio.diagram.editor.statik.elements.namespaceheader.GmNamespaceHeader;
import org.modelio.diagram.editor.statik.elements.operationgroup.GmOperationGroup;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.freezone.GmFreeZone;
import org.modelio.diagram.elements.common.group.GmGroup;
import org.modelio.diagram.elements.common.header.GmModelElementHeader;
import org.modelio.diagram.elements.common.label.modelelement.GmDefaultFlatHeader;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNoStyleCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.node.IImageableNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.metamodel.uml.behavior.usecaseModel.Actor;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("5e396759-55b7-11e2-877f-002564c97630")
public class GmActorPrimaryNode extends GmNoStyleCompositeNode implements IImageableNode {
    @objid ("5e3aedc9-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 1;

    @objid ("5e3aedcc-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("5e3aedce-55b7-11e2-877f-002564c97630")
    private static final String HEADER = "Header";

    @objid ("5e3aedd0-55b7-11e2-877f-002564c97630")
    private static final String ATTRIBUTE_GROUP = "AttributeGroup";

    @objid ("5e3aedd2-55b7-11e2-877f-002564c97630")
    private static final String METHOD_GROUP = "MethodGroup";

    @objid ("5e3aedd4-55b7-11e2-877f-002564c97630")
    private static final String INTERNAL = "Internal";

    @objid ("d9aa32db-55c2-11e2-9337-002564c97630")
    private GmModelElementHeader header;

    @objid ("d9aa32dd-55c2-11e2-9337-002564c97630")
    private GmGroup attributeGroup;

    @objid ("d9aa32df-55c2-11e2-9337-002564c97630")
    private GmGroup methodGroup;

    @objid ("d9aa32e1-55c2-11e2-9337-002564c97630")
    private GmDefaultFlatHeader imageModeHeader;

    @objid ("7b83e747-5eff-11e2-b9cc-001ec947c8cc")
    private GmInternalStructure internalStructure;

    @objid ("5e3aedd6-55b7-11e2-877f-002564c97630")
    public GmActorPrimaryNode(GmAbstractDiagram diagram, MRef ref) {
        super(diagram, ref);
        
        this.header = new GmNamespaceHeader(diagram, ref);
        this.header.setRoleInComposition(HEADER);
        this.header.setShowMetaclassIcon(true);
        this.header.setShowMetaclassKeyword(true);
        
        this.attributeGroup = new GmAttributeGroup(diagram, ref);
        this.attributeGroup.setRoleInComposition(ATTRIBUTE_GROUP);
        
        this.methodGroup = new GmOperationGroup(diagram, ref);
        this.methodGroup.setRoleInComposition(METHOD_GROUP);
        
        this.internalStructure = new GmInternalStructure(diagram, ref);
        this.internalStructure .setRoleInComposition(INTERNAL);
        
        
        super.addChild(this.header);
        super.addChild(this.attributeGroup);
        super.addChild(this.methodGroup);
        super.addChild(this.internalStructure);
        this.imageModeHeader = new GmDefaultFlatHeader(diagram, ref);
        addChild(this.imageModeHeader);
    }

    @objid ("5e3aeddf-55b7-11e2-877f-002564c97630")
    public GmActorPrimaryNode() {
        // Nothing to do.
    }

    @objid ("5e3aede2-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        if (Attribute.class.isAssignableFrom(type)) {
            return this.attributeGroup.canCreate(type);
        }
        
        if (Operation.class.isAssignableFrom(type)) {
            return this.methodGroup.canCreate(type);
        }
        
        if (Instance.class.isAssignableFrom(type) && !(Port.class.isAssignableFrom(type))) {
            return this.internalStructure.canCreate(type);
        }
        
        if (CollaborationUse.class.isAssignableFrom(type)) {
            return this.internalStructure.canCreate(type);
        }
        return false;
    }

    @objid ("5e3aedea-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        if (el instanceof Attribute) {
            return this.attributeGroup.canUnmask(el);
        }
        
        if (el instanceof Operation) {
            return this.methodGroup.canUnmask(el);
        }
        
        if (el instanceof Instance && !(el instanceof Port)) {
            return this.internalStructure.canUnmask(el);
        }
        
        if (el instanceof CollaborationUse) {
            return this.internalStructure.canUnmask(el);
        }
        return false;
    }

    @objid ("5e3aedf2-55b7-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        GmCompositeNode ret = null;
        
        if (Instance.class.isAssignableFrom(metaclass) && !(Port.class.isAssignableFrom(metaclass))) {
            // Instances are unmasked in the internal structure zone or group
            ret = getInternalStructure().getCompositeFor(metaclass);
        } else if (CollaborationUse.class.isAssignableFrom(metaclass)) {
            // Collaboration uses are unmasked in the internal structure zone or group
            ret = getInternalStructure().getCompositeFor(metaclass);
        } else if (Attribute.class.isAssignableFrom(metaclass)) {
            // Attributes are unmasked in the attributes group
            ret = this.attributeGroup;
        } else if (Operation.class.isAssignableFrom(metaclass)) {
            // Operations are unmasked in the operations group
            ret = this.methodGroup;
        }
        return ret;
    }

    @objid ("5e3aedfc-55b7-11e2-877f-002564c97630")
    @Override
    public Image getImage() {
        return ElementImageService.getImage(getRelatedElement());
    }

    @objid ("5e3c745c-55b7-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return getStyle().getProperty(GmActor.STRUCTURED_KEYS.getStyleKey(MetaKey.REPMODE));
    }

    @objid ("5e3c7463-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmActorPrimaryNode." + MINOR_VERSION_PROPERTY);
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

    @objid ("5e3c7469-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        // forcing visual refresh in case Image changed 
        firePropertyChange(PROPERTY_LAYOUTDATA, null, getLayoutData());
    }

    @objid ("5e3c746c-55b7-11e2-877f-002564c97630")
    public GmGroup getAttributeGroup() {
        return this.attributeGroup;
    }

    @objid ("5e3c7473-55b7-11e2-877f-002564c97630")
    public GmGroup getOperationGroup() {
        return this.methodGroup;
    }

    @objid ("5e3c747a-55b7-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        // Returned result depends on current representation mode:
        List<GmNodeModel> ret;
        switch (this.getRepresentationMode()) {
            case IMAGE: {
                ret = Collections.emptyList();
                break;
            }
            default: {
                ret = super.getVisibleChildren();
                // Remove the header used for image mode.
                ret.remove(this.imageModeHeader);
                break;
            }
        }
        return ret;
    }

    @objid ("5e3c7483-55b7-11e2-877f-002564c97630")
    @Override
    public Actor getRelatedElement() {
        return (Actor) super.getRelatedElement();
    }

    @objid ("5e3c748a-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmActorPrimaryNode." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("5e3c7490-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        
        final List<GmNodeModel> children = getChildren();
        
        this.header = (GmModelElementHeader) children.get(0);
        this.attributeGroup = (GmGroup) children.get(1);
        this.methodGroup = (GmGroup) children.get(2);
        
        GmGroup internalStructureGroup = (GmGroup) children.get(3);
        GmFreeZone internalStructureZone = (GmFreeZone) children.get(4);
        
        this.imageModeHeader = (GmDefaultFlatHeader) this.getChildren().get(5);
        
        // Migrate internal structure group/zone
        removeChild(internalStructureGroup);
        removeChild(internalStructureZone);
        
        this.internalStructure = new GmInternalStructure(getDiagram(), getRepresentedRef(), internalStructureZone, internalStructureGroup);
        addChild(this.internalStructure);
        
        // Add roles
        this.header.setRoleInComposition(HEADER);
        this.attributeGroup.setRoleInComposition(ATTRIBUTE_GROUP);
        this.methodGroup.setRoleInComposition(METHOD_GROUP);
        this.internalStructure.setRoleInComposition(INTERNAL);
    }

    @objid ("5e3c7495-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("5e3dfaf9-55b7-11e2-877f-002564c97630")
    public GmInternalStructure getInternalStructure() {
        return this.internalStructure;
    }

    @objid ("5e3dfafe-55b7-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        
        this.header = (GmModelElementHeader) getFirstChild(HEADER);
        this.attributeGroup = (GmAttributeGroup) getFirstChild(ATTRIBUTE_GROUP);
        this.methodGroup = (GmGroup) getFirstChild(METHOD_GROUP);
        this.internalStructure = (GmInternalStructure) getFirstChild(INTERNAL);
    }

}
