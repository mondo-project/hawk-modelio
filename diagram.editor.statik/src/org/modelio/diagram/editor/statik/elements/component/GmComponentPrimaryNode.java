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
                                    

package org.modelio.diagram.editor.statik.elements.component;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.ElementImageService;
import org.modelio.diagram.editor.statik.elements.attributegroup.GmAttributeGroup;
import org.modelio.diagram.editor.statik.elements.classifier.GmClassifierResizableGroup;
import org.modelio.diagram.editor.statik.elements.innerclass.GmInnerClass;
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
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.diagram.styles.core.StyleKey.ShowStereotypeMode;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents an {@link Classifier}.
 * <p>
 * The GmClass is composed of many zones and groups that are shown depending on the style properties.
 */
@objid ("34a9251a-55b7-11e2-877f-002564c97630")
public class GmComponentPrimaryNode extends GmNoStyleCompositeNode implements IImageableNode {
    /**
     * Current version of this Gm.
     */
    @objid ("34aaab84-55b7-11e2-877f-002564c97630")
    private final int minorVersion = 3;

    @objid ("34aaab87-55b7-11e2-877f-002564c97630")
    private static final int MAJOR_VERSION = 0;

    @objid ("34aaab89-55b7-11e2-877f-002564c97630")
    private static final String HEADER = "Header";

    @objid ("34aaab8b-55b7-11e2-877f-002564c97630")
    private static final String ATTRIBUTE_GROUP = "AttributeGroup";

    @objid ("34aaab8d-55b7-11e2-877f-002564c97630")
    private static final String METHOD_GROUP = "MethodGroup";

    @objid ("34aaab8f-55b7-11e2-877f-002564c97630")
    private static final String INTERNAL_GROUP = "InternalGroup";

    @objid ("34aaab91-55b7-11e2-877f-002564c97630")
    private static final String INTERNAL_ZONE = "InternalZone";

    @objid ("34aaab93-55b7-11e2-877f-002564c97630")
    private static final String INNER = "Inner";

    @objid ("34aaab97-55b7-11e2-877f-002564c97630")
    private static final String INTERNAL = "Internal";

    @objid ("34aaab99-55b7-11e2-877f-002564c97630")
    @Deprecated
    private static final String IMAGE_HEADER = "ImageHeader";

    /**
     * Attributes list group
     */
    @objid ("34aaab7c-55b7-11e2-877f-002564c97630")
    private GmAttributeGroup attributeGroup;

    /**
     * Inner classes
     */
    @objid ("34aaab95-55b7-11e2-877f-002564c97630")
    private GmInnerClass innerElements;

    /**
     * Classifier header
     */
    @objid ("a64af40e-55c2-11e2-9337-002564c97630")
    private GmModelElementHeader header;

    /**
     * Operations list group
     */
    @objid ("a64b1b19-55c2-11e2-9337-002564c97630")
    private GmGroup methodGroup;

    /**
     * Internal structure
     */
    @objid ("5c050732-5bd5-11e2-9e33-00137282c51b")
    private GmInternalStructure internalStructure;

    /**
     * Constructor for deserialization only.
     */
    @objid ("34aaab9c-55b7-11e2-877f-002564c97630")
    public GmComponentPrimaryNode() {
        // Nothing to do.
    }

    /**
     * Creates a GmClass.
     * @param diagram The owner diagram.
     * @param ref a reference to the element this GmModel is related to, must not be null.
     */
    @objid ("34aaab9f-55b7-11e2-877f-002564c97630")
    public GmComponentPrimaryNode(GmAbstractDiagram diagram, final MRef ref) {
        super(diagram, ref);
        
        this.header = new GmNamespaceHeader(diagram, ref);
        this.header.setRoleInComposition(HEADER);
        
        GmClassifierResizableGroup group = new GmClassifierResizableGroup(diagram, ref);
        
        this.attributeGroup = new GmAttributeGroup(diagram, ref);
        this.attributeGroup.setRoleInComposition(ATTRIBUTE_GROUP);
        
        this.methodGroup = new GmOperationGroup(diagram, ref);
        this.methodGroup.setRoleInComposition(METHOD_GROUP);
        
        this.internalStructure = new GmInternalStructure(diagram, ref);
        this.internalStructure.setRoleInComposition(INTERNAL);
        
        this.innerElements = new GmInnerClass(diagram, ref);
        this.innerElements.setRoleInComposition(INNER);
        
        super.addChild(this.header);
        super.addChild(group);
        group.addChild(this.attributeGroup);
        group.addChild(this.methodGroup);
        group.addChild(this.internalStructure);
        group.addChild(this.innerElements);
        
        styleChanged(getStyle());
    }

    @objid ("34aaaba9-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canCreate(Class<? extends MObject> type) {
        // Must be of the proper metaclass
        if ((NameSpace.class.isAssignableFrom(type) && !TemplateParameter.class.isAssignableFrom(type)) ||
                (Instance.class.isAssignableFrom(type) && !Port.class.isAssignableFrom(type)) ||
                (Behavior.class.isAssignableFrom(type)))
            return true;
        return false;
    }

    @objid ("34aaabb1-55b7-11e2-877f-002564c97630")
    @Override
    public boolean canUnmask(MObject el) {
        return false;
    }

    /**
     * Get the group where <tt>GmAttributes</tt> are unmasked.
     * @return the attributes group.
     */
    @objid ("34ac321d-55b7-11e2-877f-002564c97630")
    public GmCompositeNode getAttributesGroup() {
        return this.attributeGroup;
    }

    @objid ("34ac3224-55b7-11e2-877f-002564c97630")
    @Override
    public GmCompositeNode getCompositeFor(Class<? extends MObject> metaclass) {
        GmCompositeNode ret = null;
        if (TemplateParameter.class.isAssignableFrom(metaclass)) {
            return null;
        } else if (NameSpace.class.isAssignableFrom(metaclass)) {
            // Namespaces are unmasked in the inner classes gm
            ret = getInnerElements().getCompositeFor(metaclass);
        } else if (Instance.class.isAssignableFrom(metaclass) && !(Port.class.isAssignableFrom(metaclass))) {
            // Instances are unmasked in the internal structure gm
            ret = getInternalStructure().getCompositeFor(metaclass);
        } else if (CollaborationUse.class.isAssignableFrom(metaclass)) {
            // Collaboration uses are unmasked in the internal structure gm
            ret = getInternalStructure().getCompositeFor(metaclass);
        } else if (Attribute.class.isAssignableFrom(metaclass)) {
            // Attributes are unmasked in the attributes group
            ret = getAttributesGroup();
        } else if (Operation.class.isAssignableFrom(metaclass)) {
            // Operations are unmasked in the operations group
            ret = getOperationsGroup();
        }
        return ret;
    }

    @objid ("34ac322e-55b7-11e2-877f-002564c97630")
    @Override
    public Image getImage() {
        return ElementImageService.getImage(getRelatedElement());
    }

    /**
     * Get the internal structure.
     * @return the internal structure.
     */
    @objid ("34ac3233-55b7-11e2-877f-002564c97630")
    public GmInternalStructure getInternalStructure() {
        return this.internalStructure;
    }

    /**
     * Get the group where {@link Operation} are unmasked.
     * @return the operations group.
     */
    @objid ("34ac3238-55b7-11e2-877f-002564c97630")
    public GmCompositeNode getOperationsGroup() {
        return this.methodGroup;
    }

    @objid ("34ac323f-55b7-11e2-877f-002564c97630")
    @Override
    public Classifier getRelatedElement() {
        return (Classifier) super.getRelatedElement();
    }

    @objid ("34ac3246-55b7-11e2-877f-002564c97630")
    @Override
    public RepresentationMode getRepresentationMode() {
        return getStyle().getProperty(GmComponent.STRUCTURED_KEYS.getStyleKey(MetaKey.REPMODE));
    }

    @objid ("34ac324d-55b7-11e2-877f-002564c97630")
    @Override
    public List<GmNodeModel> getVisibleChildren() {
        // Returned result depends on current representation mode:
        List<GmNodeModel> ret;
        switch (this.getRepresentationMode()) {
        case IMAGE: {
            return Collections.emptyList();
        }
        case SIMPLE:
        case STRUCTURED:
        default: {
            ret = super.getVisibleChildren();
            break;
        }
        }
        return ret;
    }

    @objid ("34ac3256-55b7-11e2-877f-002564c97630")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmComponentPrimaryNode." + MINOR_VERSION_PROPERTY);
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
        case 2: {
            read_2(in);
            break;
        }
        case 3: {
            read_3(in);
            break;
        }
        default: {
            assert (false) : "version number not covered!";
            // reading as last handled version: 3
            read_3(in);
            break;
        }
        }
    }

    @objid ("34adb8bb-55b7-11e2-877f-002564c97630")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        String oldLabel = this.header.getMainLabel();
        this.header.refreshFromObModel();
        firePropertyChange(PROPERTY_LABEL, oldLabel, this.header.getMainLabel());
        // forcing visual refresh in case Image changed 
        firePropertyChange(PROPERTY_LAYOUTDATA, null, getLayoutData());
    }

    /**
     * Show or not the metaclass keyword and the metaclass icon depending on the stereotype show mode.
     */
    @objid ("34adb8be-55b7-11e2-877f-002564c97630")
    private void refreshHeaderFromStyle(final IStyle changedStyle) {
        final ShowStereotypeMode mode = changedStyle.getProperty(ComponentStructuredStyleKeys.SHOWSTEREOTYPES);
        switch (mode) {
        case NONE:
            this.header.setShowMetaclassKeyword(false);
            this.header.setShowMetaclassIcon(true);
            break;
        case ICON:
            this.header.setShowMetaclassKeyword(false);
            this.header.setShowMetaclassIcon(true);
            break;
        case TEXT:
            this.header.setShowMetaclassKeyword(true);
            this.header.setShowMetaclassIcon(false);
            break;
        case TEXTICON:
            this.header.setShowMetaclassKeyword(true);
            this.header.setShowMetaclassIcon(true);
        }
    }

    @objid ("34adb8c5-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        super.styleChanged(changedStyle);
        
        refreshHeaderFromStyle(changedStyle);
    }

    @objid ("34adb8cc-55b7-11e2-877f-002564c97630")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        super.styleChanged(property, newValue);
        
        if (property.equals(ComponentStructuredStyleKeys.SHOWSTEREOTYPES)) {
            refreshHeaderFromStyle(getStyle());
        }
    }

    @objid ("34adb8d5-55b7-11e2-877f-002564c97630")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmComponentPrimaryNode." + MINOR_VERSION_PROPERTY,
                    Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("34adb8db-55b7-11e2-877f-002564c97630")
    private void read_0(IDiagramReader in) {
        super.read(in);
        
        final List<GmNodeModel> children = getChildren();
        
        this.header = (GmModelElementHeader) children.get(0);
        this.attributeGroup = (GmAttributeGroup) children.get(1);
        this.methodGroup = (GmGroup) children.get(2);
        GmGroup internalStructureGroup = (GmGroup) children.get(3);
        GmFreeZone internalStructureZone = (GmFreeZone) children.get(4);
        GmGroup innerGroup = (GmGroup) children.get(5);
        GmFreeZone innerZone = (GmFreeZone) children.get(6);
        
        // Delete the old image mode header
        GmDefaultFlatHeader imageHeader = (GmDefaultFlatHeader) children.get(7);
        if (imageHeader != null) {
            imageHeader.delete();
        }
        
        // Migrate inner group/zone
        removeChild(innerGroup);
        removeChild(innerZone);
        
        // Migrate internal structure group/zone
        removeChild(internalStructureGroup);
        removeChild(internalStructureZone);
        
        this.internalStructure = new GmInternalStructure(getDiagram(), getRepresentedRef(), internalStructureZone, internalStructureGroup);
        this.innerElements = new GmInnerClass(getDiagram(), getRepresentedRef(), innerZone, innerGroup);
        
        // Add roles
        this.header.setRoleInComposition(HEADER);
        this.attributeGroup.setRoleInComposition(ATTRIBUTE_GROUP);
        this.methodGroup.setRoleInComposition(METHOD_GROUP);
        this.internalStructure.setRoleInComposition(INTERNAL);
        this.innerElements.setRoleInComposition(INNER);
        
        GmClassifierResizableGroup group = new GmClassifierResizableGroup(getDiagram(), getRepresentedRef());
        removeChild(this.attributeGroup);
        group.addChild(this.attributeGroup);
        removeChild(this.methodGroup);
        group.addChild(this.methodGroup);
        group.addChild(this.internalStructure);
        group.addChild(this.innerElements);
        super.addChild(group, 1);
    }

    @objid ("34adb8e0-55b7-11e2-877f-002564c97630")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("34adb8e5-55b7-11e2-877f-002564c97630")
    private void read_1(final IDiagramReader in) {
        super.read(in);
        
        this.header = (GmModelElementHeader) getFirstChild(HEADER);
        this.attributeGroup = (GmAttributeGroup) getFirstChild(ATTRIBUTE_GROUP);
        this.methodGroup = (GmGroup) getFirstChild(METHOD_GROUP);
        GmGroup internalStructureGroup = (GmGroup) getFirstChild(INTERNAL_GROUP);
        GmFreeZone internalStructureZone = (GmFreeZone) getFirstChild(INTERNAL_ZONE);
        this.innerElements = (GmInnerClass) getFirstChild(INNER);
        
        // Delete the old image mode header
        GmNodeModel imageHeader = getFirstChild(IMAGE_HEADER);
        if (imageHeader != null) {
            imageHeader.delete();
        }
        
        // Migrate internal structure group/zone
        removeChild(internalStructureGroup);
        removeChild(internalStructureZone);
        
        this.internalStructure = new GmInternalStructure(getDiagram(), getRepresentedRef(), internalStructureZone, internalStructureGroup);
        this.internalStructure.setRoleInComposition(INTERNAL);
        
        GmClassifierResizableGroup group = new GmClassifierResizableGroup(getDiagram(), getRepresentedRef());
        removeChild(this.attributeGroup);
        group.addChild(this.attributeGroup);
        removeChild(this.methodGroup);
        group.addChild(this.methodGroup);
        group.addChild(this.internalStructure);
        removeChild(this.innerElements);
        group.addChild(this.innerElements);
        super.addChild(group, 1);
    }

    @objid ("34adb8eb-55b7-11e2-877f-002564c97630")
    private GmInnerClass getInnerElements() {
        return this.innerElements;
    }

    @objid ("34adb8ef-55b7-11e2-877f-002564c97630")
    private void read_2(final IDiagramReader in) {
        super.read(in);
        
        this.header = (GmModelElementHeader) getFirstChild(HEADER);
        GmClassifierResizableGroup group = (GmClassifierResizableGroup) getChildren().get(1);
        this.attributeGroup = (GmAttributeGroup) group.getFirstChild(ATTRIBUTE_GROUP);
        this.methodGroup = (GmGroup) group.getFirstChild(METHOD_GROUP);
        GmGroup internalStructureGroup = (GmGroup) group.getFirstChild(INTERNAL_GROUP);
        GmFreeZone internalStructureZone = (GmFreeZone) group.getFirstChild(INTERNAL_ZONE);
        this.innerElements = (GmInnerClass) group.getFirstChild(INNER);
        
        // Delete the old image mode header
        GmNodeModel imageHeader = getFirstChild(IMAGE_HEADER);
        if (imageHeader != null) {
            imageHeader.delete();
        }
        
        // Migrate internal structure group/zone
        removeChild(internalStructureGroup);
        removeChild(internalStructureZone);
        
        this.internalStructure = new GmInternalStructure(getDiagram(), getRepresentedRef(), internalStructureZone, internalStructureGroup);
        this.internalStructure.setRoleInComposition(INTERNAL);
        group.addChild(this.internalStructure);
    }

    @objid ("34af3f5c-55b7-11e2-877f-002564c97630")
    private void read_3(final IDiagramReader in) {
        super.read(in);
        
        this.header = (GmModelElementHeader) getFirstChild(HEADER);
        GmClassifierResizableGroup group = (GmClassifierResizableGroup) getChildren().get(1);
        this.attributeGroup = (GmAttributeGroup) group.getFirstChild(ATTRIBUTE_GROUP);
        this.methodGroup = (GmGroup) group.getFirstChild(METHOD_GROUP);
        this.internalStructure = (GmInternalStructure) group.getFirstChild(INTERNAL);
        this.innerElements = (GmInnerClass) group.getFirstChild(INNER);
    }

}
