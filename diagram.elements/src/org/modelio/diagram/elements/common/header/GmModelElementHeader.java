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
                                    

package org.modelio.diagram.elements.common.header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.node.GmSimpleNode;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Represents a ModelElement header.
 * <p>
 * Displays:
 * <li>the ModelElement main label, computed with <tt>computeMainLabel()</tt>
 * <li>tagged values<br>
 * <li>the stereotypes<br>
 * <li>the stereotype icons<br>
 * <li>the metaclass icon
 * <li>the metaclass keyword
 */
@objid ("7e62e6b4-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmModelElementHeader extends GmSimpleNode implements IEditableText {
    @objid ("7e6548f1-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * The cached main label.
     */
    @objid ("8e6c220c-1e83-11e2-8cad-001ec947c8cc")
    private String label;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7e6548ee-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("7e6548ed-1dec-11e2-8cad-001ec947c8cc")
    private boolean showLabel = true;

    @objid ("7e62e6b8-1dec-11e2-8cad-001ec947c8cc")
    private boolean showMetaclassIcon = false;

    @objid ("7e62e6b9-1dec-11e2-8cad-001ec947c8cc")
    private boolean showMetaclassKeyword = false;

    @objid ("7e62e6ba-1dec-11e2-8cad-001ec947c8cc")
    private boolean stackedStereotypes = false;

    /**
     * Empty constructor to use only for deserialization.
     */
    @objid ("7e6548f3-1dec-11e2-8cad-001ec947c8cc")
    public GmModelElementHeader() {
        // Empty constructor to use only for deserialization.
    }

    /**
     * Initializes a model element header.
     * @param diagram the owning diagram.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("7e6548f6-1dec-11e2-8cad-001ec947c8cc")
    public GmModelElementHeader(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
        init();
    }

    /**
     * This method can be used to filter the stereotypes that must be actually displayed. Return an empty list when no stereotypes
     * are to be displayed. Return the passed parameter to implement a nop filter.
     * @param stereotypes the stereotypes that can be displayed
     * @return the stereotypes to display
     */
    @objid ("7e6548fb-1dec-11e2-8cad-001ec947c8cc")
    public abstract List<Stereotype> filterStereotypes(List<Stereotype> stereotypes);

    /**
     * This method can be used to filter the tags that must be actually displayed.
     * <p>
     * Return an empty list when no tag are to be displayed. Return the passed parameter to implement a nop filter.
     * @param taggedValues the tags that can be displayed. Do <b>not</b> modify this list.
     * @return the tags to display
     */
    @objid ("7e654903-1dec-11e2-8cad-001ec947c8cc")
    public abstract List<TaggedValue> filterTags(List<TaggedValue> taggedValues);

    @objid ("7e65490b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public IEditableText getEditableText() {
        return this;
    }

    /**
     * Get the main label.
     * <p>
     * The main label usually contains the element name with possibly its signature.
     * @return The main label.
     */
    @objid ("7e67ab90-1dec-11e2-8cad-001ec947c8cc")
    public String getMainLabel() {
        if (this.label == null) {
            updateMainLabelFromObModel();
        }
        return this.label;
    }

    @objid ("7e6a0db9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @objid ("7e6a0da4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ModelElement getRelatedElement() {
        return (ModelElement) super.getRelatedElement();
    }

    /**
     * Get the stereotype icons to display.
     * <p>
     * Look for the element stereotypes and filter them by calling {@link #filterStereotypes(List)}.
     * @return the stereotype icons to display.
     */
    @objid ("7e654910-1dec-11e2-8cad-001ec947c8cc")
    public List<Image> getStereotypeIcons() {
        if (this.getRelatedElement() != null) {
            final List<Stereotype> stereotypes = filterStereotypes(this.getRelatedElement().getExtension());
            final List<Image> ret = new ArrayList<>();
        
            for (Stereotype s : stereotypes) {
                final Profile ownerProfile = s.getOwner();
                if (ownerProfile != null) {
                    ModuleComponent ownerModule = ownerProfile.getOwnerModule();
                    final Image im = ModuleI18NService.getIcon(ownerModule, s);
                    if (im != null) {
                        ret.add(im);
                    }
                }
            }
        
            return ret;
        }
        return Collections.emptyList();
    }

    /**
     * Get all stereotypes labels to display.
     * <p>
     * May return one label or many depending on the value passed to {@link #setStackedStereotypes(boolean)} .
     * @return the tagged stereotypes to display.
     */
    @objid ("7e654917-1dec-11e2-8cad-001ec947c8cc")
    public List<String> getStereotypeLabels() {
        if (this.getRelatedElement() != null) {
            List<Stereotype> stereotypes = filterStereotypes(this.getRelatedElement().getExtension());
            if (this.stackedStereotypes)
                return getStackedStereotypeLabels(stereotypes);
            return getLinedStereotypeLabels(stereotypes);
        }
        return Collections.emptyList();
    }

    /**
     * Get all tagged value labels to display.
     * <p>
     * Look for the element tagged values and filter them by calling {@link #filterTags(List)}.
     * @return the tagged value labels to display.
     */
    @objid ("7e65491e-1dec-11e2-8cad-001ec947c8cc")
    public List<String> getTaggedValueLabels() {
        List<String> labels = new ArrayList<>();
        if (this.getRelatedElement() != null) {
            for (TaggedValue tag : filterTags(this.getRelatedElement().getTag())) {
                // Ignore hidden tags 
                final TagType tagType = tag.getDefinition();
                if (tagType == null || ! tagType.isIsHidden())
                    labels.add(makeTagLabel(tag));
            }
        }
        return labels;
    }

    /**
     * Return the represented classifier name.
     * @return the represented classifier name.
     */
    @objid ("7e654925-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public String getText() {
        ModelElement relatedElement = this.getRelatedElement();
        if (relatedElement == null)
            return "?";
        return relatedElement.getName();
    }

    /**
     * Tells whether the element's name is shown.
     * @return true if the element's name is shown.
     */
    @objid ("7e6a0da9-1dec-11e2-8cad-001ec947c8cc")
    public boolean isShowLabel() {
        return this.showLabel;
    }

    /**
     * Tells whether the metaclass icon is shown.
     * @return true if the metaclass icon is shown.
     */
    @objid ("7e65492b-1dec-11e2-8cad-001ec947c8cc")
    public boolean isShowMetaclassIcon() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.showMetaclassIcon;
    }

    /**
     * Tells whether the metaclass keyword is shown.
     * @return true if the metaclass keyword is shown.
     */
    @objid ("7e654930-1dec-11e2-8cad-001ec947c8cc")
    public boolean isShowMetaclassKeyword() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.showMetaclassKeyword;
    }

    @objid ("7e67ab4b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void obElementAdded(MObject addedEl) {
        super.obElementAdded(addedEl);
        
        if (addedEl instanceof TaggedValue || addedEl instanceof Stereotype) {
            refreshFromObModel();
        }
    }

    @objid ("7e67ab4f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmModelElementHeader." + MINOR_VERSION_PROPERTY);
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

    @objid ("7e67ab53-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void refreshFromObModel() {
        updateMainLabelFromObModel();
        
        // whatever has changed, fire a label change, the header will update everything.
        firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
    }

    /**
     * Set whether the element's name must be shown.
     * @param value whether the element's name must be shown.
     */
    @objid ("7e6a0dae-1dec-11e2-8cad-001ec947c8cc")
    public void setShowLabel(boolean value) {
        this.showLabel = value;
    }

    /**
     * Set whether the metaclass icon must be shown.
     * @param value whether the metaclass icon must be shown.
     */
    @objid ("7e67ab56-1dec-11e2-8cad-001ec947c8cc")
    public void setShowMetaclassIcon(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.showMetaclassIcon = value;
    }

    /**
     * Set whether the metaclass keyword must be shown .
     * @param value whether the metaclass keyword must be shown.
     */
    @objid ("7e67ab5a-1dec-11e2-8cad-001ec947c8cc")
    public void setShowMetaclassKeyword(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.showMetaclassKeyword = value;
    }

    /**
     * Set whether all stereotypes must be displayed on a one line label or many stacked labels.
     * @param value if true, stereotypes will be displayed one per line. If false all on one line.
     */
    @objid ("7e67ab5e-1dec-11e2-8cad-001ec947c8cc")
    public void setStackedStereotypes(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.stackedStereotypes = value;
    }

    /**
     * Set the represented classifier name in the model.
     * <p>
     * This method directly and only modifies the model. The label will then update at then end of the transaction, when the header
     * will be notified of the change event.
     * @param newName the new represented classifier name.
     */
    @objid ("7e67ab62-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setText(String newName) {
        this.getRelatedElement().setName(newName);
    }

    @objid ("7e67ab8c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        out.writeProperty("show_icon", this.showMetaclassIcon);
        out.writeProperty("show_keyword", this.showMetaclassKeyword);
        out.writeProperty("stack_stereo", this.stackedStereotypes);
        out.writeProperty("show_label", this.showLabel);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmModelElementHeader." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    /**
     * Computes the main label of the header.
     * <p>
     * The main label is often the element name and the default implementation returns it.
     * <p>
     * This method may be redefined by subclasses.
     * @return The main label of the header.
     */
    @objid ("7e67ab67-1dec-11e2-8cad-001ec947c8cc")
    protected String computeMainLabel() {
        return this.getRelatedElement().getName();
    }

    @objid ("7e6a0db2-1dec-11e2-8cad-001ec947c8cc")
    protected Image getMetaclassIcon() {
        return MetamodelImageService.getIcon(getRelatedElement().getMClass());
    }

    @objid ("daf6ae16-f5ef-4356-85a2-0c22d8cf1c82")
    protected String getMetaclassKeyword() {
        return getRelatedElement().getMClass().getName();
    }

    /**
     * Updates the cached main label.
     * <p>
     * To be called from {@link #refreshFromObModel()} or <tt>styleChanged(*)</tt> methods when the label needs to be updated.
     * <p>
     * Fires no property change notification. It is to the caller to decide depending on the returned value and other conditions
     * whether a property change event must be fired.
     * @return true if the label was changed, false if it is still the same.
     */
    @objid ("7e67ab6c-1dec-11e2-8cad-001ec947c8cc")
    protected boolean updateMainLabelFromObModel() {
        if (this.getRelatedElement() != null) {
            final String newName = computeMainLabel();
            if (this.label == null || !this.label.equals(newName)) {
                this.label = newName;
                return true;
            }
        }
        return false;
    }

    @objid ("7e67ab71-1dec-11e2-8cad-001ec947c8cc")
    private static List<String> getLinedStereotypeLabels(List<Stereotype> stereotypes) {
        final List<String> labels = new ArrayList<>();
        final StringBuffer line = new StringBuffer();
        
        if (stereotypes.isEmpty())
            return labels;
        
        line.append("<<");
        for (Stereotype s : stereotypes) {
            // if there is no label, use the name
            final String aLabel = ModuleI18NService.getLabel(s);
            line.append(aLabel);
            line.append(", ");
        }
        line.delete(line.length() - 2, line.length());
        line.append(">>");
        labels.add(line.toString());
        return labels;
    }

    @objid ("7e67ab7a-1dec-11e2-8cad-001ec947c8cc")
    private static List<String> getStackedStereotypeLabels(List<Stereotype> stereotypes) {
        final List<String> labels = new ArrayList<>();
        for (Stereotype s : stereotypes) {
            // if there is no label, use the name
            labels.add("<<" + ModuleI18NService.getLabel(s) + ">>");
        }
        return labels;
    }

    /**
     * To be called by the constructor and the {@link #read(IDiagramReader)} methods.
     */
    @objid ("7e67ab83-1dec-11e2-8cad-001ec947c8cc")
    private void init() {
        if (this.getRelatedElement() != null) {
            this.label = computeMainLabel();
        }
    }

    /**
     * Build the label for a tagged value.
     * @param tag a tagged value
     * @return the tag label
     */
    @objid ("7e67ab86-1dec-11e2-8cad-001ec947c8cc")
    private static String makeTagLabel(TaggedValue tag) {
        final StringBuilder buf = new StringBuilder();
        final TagType tagType = tag.getDefinition();
        
        String tagLabel = ModuleI18NService.getLabel(tagType);
        if (tagLabel == null || tagLabel.isEmpty()) {
            tagLabel = tagType.getLabelKey();
            if (tagLabel == null || tagLabel.isEmpty())
                tagLabel = tagType.getName();
        }
        
        Stereotype typeStereotype = tagType.getOwnerStereotype();
        
        buf.append("{");
        if (typeStereotype != null) {
            buf.append(typeStereotype.getName());
            buf.append(".");
        }
        buf.append(tagLabel);
        
        final EList<TagParameter> params = tag.getActual();
        if (params.size() > 0) {
            buf.append("(");
            boolean first = true;
            for (TagParameter param : params) {
                if (first)
                    first = false;
                else
                    buf.append(", ");
                
                buf.append(param.getValue());
            }
            buf.append(")");
        }
        
        TagParameter qual = tag.getQualifier();
        if (qual != null) {
            buf.append(":");
            buf.append(qual.getValue());
        }
        
        buf.append("}");
        return buf.toString();
    }

    @objid ("7e6a0db6-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
        
        this.showMetaclassIcon = (Boolean) in.readProperty("show_icon");
        this.showMetaclassKeyword = (Boolean) in.readProperty("show_keyword");
        this.stackedStereotypes = (Boolean) in.readProperty("stack_stereo");
        this.showLabel = (Boolean) in.readProperty("show_label");
        
        init();
    }

}
