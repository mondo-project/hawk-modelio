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
                                    

package org.modelio.diagram.elements.common.label.modelelement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.common.label.base.GmElementLabel;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IEditableText;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IDiagramWriter;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
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
 * Represents a ModelElement label.
 * <p>
 * Displays:
 * <li>the ModelElement name<br>
 * <li>its visibility<br>
 * <li>tagged values<br>
 * <li>the stereotypes<br>
 * <li>the stereotype icons<br>
 * <li>the metaclass icon
 * <li>the metaclass keyword
 */
@objid ("7e975a85-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmModelElementFlatHeader extends GmElementLabel {
    @objid ("7e975a87-1dec-11e2-8cad-001ec947c8cc")
    private boolean displayStereotypesAsStack = false;

    @objid ("7e975a88-1dec-11e2-8cad-001ec947c8cc")
    private boolean showLabel = true;

    @objid ("7e975a89-1dec-11e2-8cad-001ec947c8cc")
    private boolean showMetaclassIcon = false;

    @objid ("7e975a8a-1dec-11e2-8cad-001ec947c8cc")
    private boolean showMetaclassKeyword = false;

    /**
     * Current version of this Gm. Defaults to 0.
     */
    @objid ("7e975a8b-1dec-11e2-8cad-001ec947c8cc")
    private final int minorVersion = 0;

    @objid ("7e975a8e-1dec-11e2-8cad-001ec947c8cc")
    private static final int MAJOR_VERSION = 0;

    /**
     * Initializes a model element header.
     * @param diagram the owning diagram.
     * @param relatedRef a reference to the element this GmModel is related to.
     */
    @objid ("7e975a90-1dec-11e2-8cad-001ec947c8cc")
    public GmModelElementFlatHeader(GmAbstractDiagram diagram, MRef relatedRef) {
        super(diagram, relatedRef);
        init();
    }

    /**
     * Empty constructor to use only for deserialization.
     * <p>
     * Use {@link #GmModelElementFlatHeader(GmAbstractDiagram, MRef)} for regular instantiation.
     */
    @objid ("7e99bcac-1dec-11e2-8cad-001ec947c8cc")
    public GmModelElementFlatHeader() {
        // Empty constructor to use only for deserialization.
    }

    /**
     * This method can be used to filter the stereotypes that must be actually displayed. Return an empty list when no stereotypes
     * are to be displayed. Return the passed parameter to implement a nop filter.
     * @param stereotypes the stereotypes that can be displayed
     * @return the stereotypes to display
     */
    @objid ("7e99bcaf-1dec-11e2-8cad-001ec947c8cc")
    public abstract List<Stereotype> filterStereotypes(List<Stereotype> stereotypes);

    /**
     * This method can be used to filter the tags that must be actullay displayed. Return an empty list when no tag are to be
     * displayed. Return the passed parameter to implement a nop filter.
     * @param taggedValues the tags that can be displayed
     * @return the tags to display
     */
    @objid ("7e99bcb7-1dec-11e2-8cad-001ec947c8cc")
    public abstract List<TaggedValue> filterTags(List<TaggedValue> taggedValues);

    @objid ("7e99bcbf-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public abstract IEditableText getEditableText();

    @objid ("7e99bcc2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ModelElement getRelatedElement() {
        return (ModelElement) super.getRelatedElement();
    }

    /**
     * Get the stereotype icons.
     * @return the stereotype icons.
     */
    @objid ("7e99bcc7-1dec-11e2-8cad-001ec947c8cc")
    public List<Image> getStereotypeIcons() {
        if (getRelatedElement() != null) {
            final List<Stereotype> stereotypes = filterStereotypes(getRelatedElement().getExtension());
            final List<Image> ret = new ArrayList<>(stereotypes.size());
        
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
     * Get the stereotypes label.
     * @return an array of labels.
     */
    @objid ("7e99bcce-1dec-11e2-8cad-001ec947c8cc")
    public String getStereotypesLabel() {
        if (getRelatedElement() != null) {
            List<Stereotype> stereotypes = filterStereotypes(getRelatedElement().getExtension());
            if (this.displayStereotypesAsStack)
                return getStackedStereotypesLabel(stereotypes);
            else
                return getLinedStereotypesLabel(stereotypes);
        }
        return "";
    }

    /**
     * Get the tagged values labels.
     * @return the tagged values labels.
     */
    @objid ("7e99bcd3-1dec-11e2-8cad-001ec947c8cc")
    public String getTaggedValuesLabel() {
        final StringBuilder labels = new StringBuilder();
        if (getRelatedElement() != null) {
            for (TaggedValue tag : filterTags(getRelatedElement().getTag())) {
                makeTagLabel(tag, labels);
            }
        }
        return labels.toString();
    }

    /**
     * Tells whether the element's name is shown.
     * @return true if the element's name is shown.
     */
    @objid ("7e99bcd8-1dec-11e2-8cad-001ec947c8cc")
    public boolean isShowLabel() {
        return this.showLabel;
    }

    /**
     * Tells whether the metaclass icon will be displayed.
     * @return whether the metaclass icon will be displayed.
     */
    @objid ("7e99bcdd-1dec-11e2-8cad-001ec947c8cc")
    public boolean isShowMetaclassIcon() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.showMetaclassIcon;
    }

    /**
     * Tells whether the metaclass keyword will be displayed.
     * @return whether the metaclass keyword will be displayed.
     */
    @objid ("7e99bce2-1dec-11e2-8cad-001ec947c8cc")
    public boolean isShowMetaclassKeyword() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.showMetaclassKeyword;
    }

    @objid ("7e99bce7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void obElementAdded(MObject addedEl) {
        super.obElementAdded(addedEl);
        
        if (addedEl instanceof TaggedValue || addedEl instanceof Stereotype) {
            refreshFromObModel();
        }
    }

    @objid ("7e99bceb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void read(IDiagramReader in) {
        // Read version, defaults to 0 if not found
        Object versionProperty = in.readProperty("GmModelElementFlatHeader." + MINOR_VERSION_PROPERTY);
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

    @objid ("7e99bcef-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void refreshFromObModel() {
        super.refreshFromObModel();
        
        // Whatever has changed, fire a label change so that the edit part
        // updates tags and stereotypes labels.
        firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
    }

    /**
     * Set whether each stereotype is in its own &lt;&lt; >> or all are in the same &lt;&lt; >>.
     * @param value if <i>true</i>, each stereotype is in its &lt;&lt; >>.<br>
     * If <i>false</i> all stereotypes will be in a single &lt;&lt;a, b, c ...>> label
     */
    @objid ("7e99bcf2-1dec-11e2-8cad-001ec947c8cc")
    public void setDisplayStereotypesAsStack(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.displayStereotypesAsStack = value;
    }

    /**
     * Set whether the element's name must be shown.
     * @param value whether the element's name must be shown.
     */
    @objid ("7e99bcf6-1dec-11e2-8cad-001ec947c8cc")
    public void setShowLabel(boolean value) {
        this.showLabel = value;
    }

    /**
     * Set whether the metaclass icon must be displayed.
     * @param value whether the metaclass icon must be displayed.
     */
    @objid ("7e99bcfa-1dec-11e2-8cad-001ec947c8cc")
    public void setShowMetaclassIcon(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.showMetaclassIcon = value;
    }

    /**
     * Set whether the metaclass keyword must be displayed.
     * @param value whether the metaclass keyword must be displayed.
     */
    @objid ("7e9c1f05-1dec-11e2-8cad-001ec947c8cc")
    public void setShowMetaclassKeyword(boolean value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.showMetaclassKeyword = value;
    }

    @objid ("7e9c1f09-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void styleChanged(final IStyle changedStyle) {
        super.styleChanged(changedStyle);
        // Some keys might change the content of the label (mainly show tags and show stereotypes)
        firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
    }

    @objid ("7e9c1f0e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void styleChanged(final StyleKey property, final Object newValue) {
        super.styleChanged(property, newValue);
        // Some keys might change the content of the label
        GmModel parent = getParent();
        if (parent != null) {
            final StyleKey showStereotypesKey = parent.getStyleKey(MetaKey.SHOWSTEREOTYPES);
            final StyleKey showTagKey = parent.getStyleKey(MetaKey.SHOWTAGS);
            if ((showStereotypesKey != null && showStereotypesKey.equals(property))
                    || (showTagKey != null && showTagKey.equals(property))) {
                firePropertyChange(IGmObject.PROPERTY_LABEL, this, null);
            }
        }
    }

    @objid ("7e9c1f15-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void write(IDiagramWriter out) {
        super.write(out);
        
        out.writeProperty("show_icon", this.showMetaclassIcon);
        out.writeProperty("show_keyword", this.showMetaclassKeyword);
        out.writeProperty("stack_stereo", this.displayStereotypesAsStack);
        out.writeProperty("show_label", this.showLabel);
        
        // Write version of this Gm if different of 0.
        if (this.minorVersion != 0) {
            out.writeProperty("GmModelElementFlatHeader." + MINOR_VERSION_PROPERTY, Integer.valueOf(this.minorVersion));
        }
    }

    @objid ("7e9c1f19-1dec-11e2-8cad-001ec947c8cc")
    private String getLinedStereotypesLabel(List<Stereotype> stereotypes) {
        final StringBuffer line = new StringBuffer();
        
        if (stereotypes.isEmpty())
            return "";
        
        line.append("<<");
        for (Stereotype s : stereotypes) {
            // if there is no label, use the name
            final String aLabel = ModuleI18NService.getLabel(s);
            line.append(aLabel);
            line.append(", ");
        }
        line.delete(line.length() - 2, line.length());
        line.append(">>");
        return line.toString();
    }

    /**
     * Get the stereotype labels in the form [&lt;&lt;a>>, &lt;&lt;b>>, &lt;&lt;c>> ...]
     * @param stereotypes the stereotypes to show
     * @return a list of stereotype labels.
     */
    @objid ("7e9c1f20-1dec-11e2-8cad-001ec947c8cc")
    private String getStackedStereotypesLabel(List<Stereotype> stereotypes) {
        final StringBuilder labels = new StringBuilder();
        for (Stereotype s : stereotypes) {
            // if there is no label, use the name
            labels.append("<<");
            labels.append(ModuleI18NService.getLabel(s));
            labels.append(">>");
        }
        return labels.toString();
    }

    /**
     * To be called by the constructor and the {@link #read(IDiagramReader)} methods.
     */
    @objid ("7e9c1f28-1dec-11e2-8cad-001ec947c8cc")
    private void init() {
        if (getRelatedElement() != null) {
            this.label = computeLabel();
        }
    }

    @objid ("7e9c1f2b-1dec-11e2-8cad-001ec947c8cc")
    private void makeTagLabel(TaggedValue tag, StringBuilder buf) {
        final TagType tagType = tag.getDefinition();
        
        buf.append("{");
        
        final Stereotype s = tagType.getOwnerStereotype();
        if (s != null) {
            buf.append(s.getName());
            buf.append(".");
        }
        
        buf.append(ModuleI18NService.getLabel(tagType));
        
        final List<TagParameter> tagParameters = tag.getActual();
        if (tagParameters.size() > 0) {
            buf.append("(");
        
            for (TagParameter param : tagParameters) {
                buf.append(param.getValue().toString() + ", ");
            }
            buf.delete(buf.length() - 2, buf.length());
        
            buf.append(")");
        }
        
        buf.append("}");
    }

    @objid ("7e9c1f2f-1dec-11e2-8cad-001ec947c8cc")
    protected Image getMetaclassIcon() {
        return MetamodelImageService.getIcon(getRelatedElement().getMClass());
    }

    @objid ("7e9c1f33-1dec-11e2-8cad-001ec947c8cc")
    private void read_0(IDiagramReader in) {
        super.read(in);
        
        this.showMetaclassIcon = (Boolean) in.readProperty("show_icon");
        this.showMetaclassKeyword = (Boolean) in.readProperty("show_keyword");
        this.displayStereotypesAsStack = (Boolean) in.readProperty("stack_stereo");
        this.showLabel = (Boolean) in.readProperty("show_label");
        
        init();
    }

    @objid ("7e9c1f36-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

}
