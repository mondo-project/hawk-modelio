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
                                    

package org.modelio.property.ui.data.stereotype.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.factory.ModelFactory;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTable;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.TypedPropertyTable;
import org.modelio.property.plugin.ModelProperty;
import org.modelio.property.ui.data.stereotype.body.IPropertyStyle;
import org.modelio.property.ui.data.stereotype.body.PropertyAdapter;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("76906006-5f6e-4408-85f3-32853c9da8a3")
public class PropertyTableModel implements IPropertyModel2 {
    @objid ("8952a0c5-3126-4986-b091-b65cb04c048b")
    private static final String TAG_MARKER = "{} ";

    @objid ("07da42c7-bb8f-4a99-80f4-bc65448aca7c")
    private PropertyTableHelper tableModel;

    @objid ("6a369e5b-8ed2-440b-ab60-a431d5cd8ede")
    private ModelElement editedElement;

    @objid ("eaa7b162-cf7d-4dc0-b824-3f045ef82389")
    private TagDataHelper tagModel;

    @objid ("b0fde2e2-0548-4394-98c5-59fdb458483a")
    public PropertyTableModel(ModelElement editedElement, Stereotype stereotype, IMModelServices modelService) {
        this.editedElement = editedElement;
        this.tagModel = new TagDataHelper(editedElement, stereotype, modelService);
        this.tableModel = new PropertyTableHelper(stereotype.getDefinedTable());
    }

    @objid ("f38a1545-175c-4f9f-b50d-9176530979c6")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("a9751b2c-76ab-4dca-98cc-00086c3a182c")
    @Override
    public int getRowsNumber() {
        return this.tagModel.getTagTypes().size() + this.tableModel.getProperties().size() + 1;
    }

    @objid ("1c1f95cc-0ee6-42fb-a3ce-5b141d05405c")
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            if (row == 0) {
                // Corner
                return "Properties";
            } else if (isTaggedValue(row)) {
                TagType type = this.tagModel.getTagTypes().get(row - 1);
                String label = ModuleI18NService.getLabel(type);
                if (label==null || label.isEmpty()) {
                    label = "!"+type.getName()+"!";
                }
                
                if (type.isIsHidden()) {
                    final String hiddenString = ModelProperty.I18N.getString("AnnotationView.PropertyPanel.Hidden");
                    return TAG_MARKER + hiddenString + label;
                } else {
                    return TAG_MARKER + label;
                }
            } else if (isProperty(row)) {
                // Row Header
                final PropertyDefinition pdef = this.tableModel.getProperties().get(row - this.tagModel.getTagTypes().size() -1);
                return pdef.getName();
            } else {
                throw new InvalidParameterException("Invalid row number");
            }
        } else {
            if (row == 0) {
                // Col Header
                return "Value";
            } else if (isTaggedValue(row)) {
                return this.tagModel.getPropertyValue(row - 1);
            } else if (isProperty(row)) {
                // Value
                final PropertyDefinition pdef = this.tableModel.getProperties().get(row - this.tagModel.getTagTypes().size() -1);
        
                // Get property table
                TypedPropertyTable typedTable = null;
                for (PropertyTable table : this.editedElement.getProperties()) {
                    if (table instanceof TypedPropertyTable && this.tableModel.getPropertyTable().equals(((TypedPropertyTable)table).getType())) {
                        typedTable = (TypedPropertyTable) table;
                        break;
                    }
                }
                
                String value = null;
                if (typedTable != null) {
                    value = typedTable.getProperty(pdef);
                }
                
                return PropertyAdapter.convertToObject(pdef, value);
            } else {
                throw new InvalidParameterException("Invalid row number");
            }
        }
    }

    @objid ("db9197aa-9625-413b-9d72-f60600ffc801")
    @Override
    public void setValueAt(int row, int col, Object value) {
        if (col == 0) {
            // RowHeader
            throw new InvalidParameterException("Unable to edit header");
        } else {
            if (row == 0) {
                // Col Header
                throw new InvalidParameterException("Unable to edit header");
            } else if (isTaggedValue(row)) {
                this.tagModel.setPropertyValue(row - 1, value);
            } else if (isProperty(row)) {
                // Value
                final PropertyDefinition pdef = this.tableModel.getProperties().get(row - this.tagModel.getTagTypes().size() -1);
                final String svalue = PropertyAdapter.convertToString(pdef, value);
        
                // Get property table
                TypedPropertyTable typedTable = null;
                for (PropertyTable table : this.editedElement.getProperties()) {
                    if ((table instanceof TypedPropertyTable) && this.tableModel.getPropertyTable().equals(((TypedPropertyTable)table).getType())) {
                        typedTable = (TypedPropertyTable) table;
                        break;
                    }
                }
                if (typedTable == null) {
                    // No table, create one
                    typedTable = ModelFactory.getFactory(this.editedElement).createTypedPropertyTable();
                    typedTable.setOwner(this.editedElement);
                    typedTable.setType(this.tableModel.getPropertyTable());
                }
                typedTable.setProperty(pdef, svalue);
            }
        }
    }

    @objid ("a12651a5-eacc-4155-8241-fa7842f3663e")
    @Override
    public boolean isEditable(int row, int col) {
        return (col == 0) ? false : this.editedElement.isModifiable();
    }

    @objid ("43168280-8425-40e8-9328-2b29e9240808")
    @Override
    public MObject getEditedElement() {
        return this.editedElement;
    }

    @objid ("b3d335fc-0633-4c5a-8d7c-543203d68007")
    @Override
    public void updateStyle(int row, int col, IPropertyStyle newStyle) {
        // Nothing to do
    }

    @objid ("fea17249-d633-4fb3-85d4-576a068b00e8")
    @Override
    public Class<?> getValueTypeAt(int row, int col) {
        if (col == 0) {
            if (row == 0) {
                // Corner
                return String.class;
            } else {
                // Row Header
                return String.class;
            }
        } else {
            if (row == 0) {
                // Col Header
                return String.class;
            } else if (isTaggedValue(row)) {
                return this.tagModel.getPropertyType(row - 1);
            } else if (isProperty(row)) {
                // Value
                final PropertyDefinition pdef = this.tableModel.getProperties().get(row - this.tagModel.getTagTypes().size() -1);
                return PropertyAdapter.getType(pdef);
            } else {
                throw new InvalidParameterException("Invalid row number");
            }
        }
    }

    @objid ("3e3944c9-ca93-4765-9730-e32c206663b2")
    @Override
    public List<String> getPossibleValues(int row, int col) {
        // Nothing to do
        return null;
    }

    @objid ("01eb3d5d-cb33-451d-a3b9-682e4487831a")
    public PropertyTableModel(ModelElement editedElement, ModuleComponent module, IMModelServices modelService) {
        this.editedElement = editedElement;
        this.tagModel = new TagDataHelper(editedElement, module, modelService);
        this.tableModel = new PropertyTableHelper(null);
    }

    /**
     * Indicates whether or not this row corresponds to a Property edition.
     */
    @objid ("11911abc-56ac-4c38-b2ff-95062875ed9b")
    public boolean isProperty(int row) {
        return row > this.tagModel.getTagTypes().size();
    }

    /**
     * Indicates whether or not this row corresponds to a TaggedValue edition.
     */
    @objid ("3b436fa5-74a5-499b-a7ad-0f78cf9cacf7")
    public boolean isTaggedValue(int row) {
        return row > 0 && row <= this.tagModel.getTagTypes().size();
    }

    @objid ("37f76f25-b120-4a80-b113-24dd50fc2a8f")
    private static class PropertyTableHelper {
        @objid ("b555f26a-dfb9-4ff1-961f-a042a524d089")
        private PropertyTableDefinition propertyTable;

        @objid ("265ee32f-0da6-452f-9209-02ed694510b9")
        public PropertyTableHelper(PropertyTableDefinition propertyTable) {
            this.propertyTable = propertyTable;
        }

        @objid ("05dfdc3b-8e29-4f11-bd0e-a8ccfe66a1ee")
        public List<PropertyDefinition> getProperties() {
            if (this.propertyTable == null) {
                return Collections.emptyList();
            }
            return this.propertyTable.getOwned();
        }

        @objid ("a823b072-a8ce-4587-8ca6-da1ea6c4b43c")
        public PropertyTableDefinition getPropertyTable() {
            return this.propertyTable;
        }

    }

    @objid ("5c9365a6-b4a6-4ba9-ac3f-c1e45ba2122f")
    private static class TagDataHelper {
        @objid ("6d4b8f5c-a04d-4d51-90c3-43bee92f34ef")
        private List<TagType> tagTypes = null;

        @objid ("1db6aa83-b219-47d6-b246-4734c95e2d55")
        private IMModelServices modelService;

        @objid ("7cbd46bc-3d5f-401c-bc55-d8d014c1be44")
        private ModelElement typedElement;

        @objid ("547ea2f6-e89a-4d29-8c91-694203e1b4d4")
        public TagDataHelper(ModelElement typedElement, ModelElement typingElement, IMModelServices modelService) {
            this.typedElement = typedElement;
            this.modelService = modelService;
            
            // Compute the tag type list
            if (typingElement == null) {
                this.tagTypes = Collections.emptyList();
            
            } else if (typingElement instanceof Stereotype) {
                Stereotype s = (Stereotype) typingElement;
                this.tagTypes = new ArrayList<>();
                while (s != null) {
                    List<TagType> currentLevelTypes = new ArrayList<>();
                    for (TagType tagType : s.getDefinedTagType()) {
                        if (!tagType.isIsHidden()) {
                            currentLevelTypes.add(tagType);
                        }
                    }
                    // Put tag types from parent stereotypes first...
                    this.tagTypes.addAll(0, currentLevelTypes);
                    s = s.getParent();
                }
            } else if (typingElement instanceof ModuleComponent) {
                this.tagTypes = getModuleTagTypesForMetaclass((ModuleComponent) typingElement, this.typedElement.getMClass());
            }
        }

        @objid ("50d02cca-20a7-4220-b2aa-cca280622b9d")
        public List<TagType> getTagTypes() {
            return this.tagTypes;
        }

        @objid ("955ad4f4-c8ef-4916-b240-49351ee9786f")
        public Object getPropertyValue(final int index) {
            TagType tagType = this.tagTypes.get(index);
            int paramNumber = getParamNumber(tagType);
            
            TaggedValue taggedValue = null;
            for (TaggedValue v : this.typedElement.getTag()) {
                if (v.getDefinition().equals(tagType)) {
                    taggedValue = v;
                    break;
                }
            }
            
            switch (paramNumber) {
            case 0:
                return (taggedValue != null);
            case 1:
                if (taggedValue != null) {
                    List<TagParameter> parameters = taggedValue.getActual();
                    if (parameters.isEmpty()) {
                        return "";
                    } else {
                        return parameters.get(0).getValue();
                    }
                } else {
                    return "";
                }
            
            default:
                if (taggedValue != null) {
                    List<TagParameter> parameters = taggedValue.getActual();
                    List<String> values = new ArrayList<>();
                    for (TagParameter parameter : parameters) {
                        values.add(parameter.getValue());
                    }
                    return values;
                } else {
                    return new ArrayList<>();
                }
            }
        }

        @objid ("b67259ab-e4dd-492a-b007-478f8b138779")
        @SuppressWarnings("unchecked")
        public void setPropertyValue(final int index, final Object value) {
            TagType tagType = this.tagTypes.get(index);
            
            int paramNumber = getParamNumber(tagType);
            
            if (paramNumber == 0) { // Boolean type
                updateBooleanTaggedValue(this.typedElement, tagType, (Boolean) value);
            } else if (paramNumber == 1) {
                updateStringTaggedValue(this.typedElement, tagType, (String) value);
            } else { 
                // paramNumber > 1 and paramNumber == -1 (param number no limit)
                updateStringlistTaggedValue(this.typedElement, tagType, (List<String>) value);
            }
        }

        @objid ("5d437cb3-2dd8-40ad-a32c-f692a44181df")
        public Class<?> getPropertyType(final int index) {
            TagType tagType = this.tagTypes.get(index);
            int paramNumber = getParamNumber(tagType);
            if (paramNumber == 0) {
                return Boolean.class;
            } else if (paramNumber == 1) {
                return String.class;
            } else {
                return List.class;
            }
        }

        @objid ("0127b049-dab8-4551-9a0c-fd8163ee5631")
        private List<TagType> getModuleTagTypesForMetaclass(final ModuleComponent module, final MClass metaclass) {
            // Compute the tag types that are defined by 'module' and applicable
            // on 'element'
            List<TagType> ret = new ArrayList<>();
            
            if (module != null) {
                for (TagType tagType : this.modelService.findTagTypes(module.getName(), ".*", metaclass)) {
                    if (tagType.getOwnerStereotype() == null && displayTagType(tagType)) {
                        ret.add(tagType);
                    }
                }
            }
            return ret;
        }

        @objid ("507a8cf1-d27c-4e1a-b3d5-391dac99bf53")
        private int getParamNumber(final TagType type) {
            String paramNumberStr = type.getParamNumber();
            int paramNumber = -1;
            
            try {
                paramNumber = Integer.parseInt(paramNumberStr);
            } catch (NumberFormatException e) {
                paramNumber = -1;
            }
            return paramNumber;
        }

        @objid ("8a5caeb3-7972-4e32-8de2-a36b4d0db721")
        private void updateBooleanTaggedValue(final ModelElement element, final TagType tagType, final Boolean value) {
            if (value) {
                for (TaggedValue tag : element.getTag()) {
                    if (tag.getDefinition() == tagType) {
                        return;
                    }
                }
                ModelFactory.getFactory(element).createTaggedValue(tagType, element);
            } else {
                removeTag(element, tagType);
            }
        }

        @objid ("0896a1c0-a908-4681-ad3f-ebe84c8bacc6")
        private void updateStringTaggedValue(final ModelElement element, final TagType tagType, final String value) {
            try {
                putTagValue(element, tagType, "".equals(value) ? null : value);
            } catch (Exception e) {
                ModelProperty.LOG.error(e);
            }
        }

        @objid ("3eb4a124-5da8-451c-bdf4-50061e9b235a")
        private void updateStringlistTaggedValue(final ModelElement element, final TagType tagType, final List<String> values) {
            try {
                putTagValues(element, tagType, values.isEmpty() ? null : values);
            } catch (Exception e) {
                ModelProperty.LOG.error(e);
            }
        }

        /**
         * This operation returns the tagged value with the corresponding type.
         * @param element IModelElement on which the tagged value is search for.
         * @param type The tagged value type name
         * @return The tag or null if it can't be found
         */
        @objid ("cd607a47-8a40-4b46-89f7-01b60fac1aa1")
        private TaggedValue getTag(ModelElement element, TagType type) {
            for (TaggedValue currentTag : element.getTag()) {
                TagType tagDef = currentTag.getDefinition();
                if (tagDef != null && tagDef == type) {
                    return currentTag;
                }
            }
            return null;
        }

        /**
         * This operation deletes the tagged value having this type from the given element.
         * @param element IModelElement on which the tagged value is removed.
         * @param type The tagged value type name
         */
        @objid ("6754f68c-a371-4f5d-9786-4ad0ed058524")
        private void removeTag(ModelElement element, TagType type) {
            TaggedValue tag = getTag(element, type);
            if (tag != null) {
                tag.delete();
            }
        }

        /**
         * This operation sets the parameters of the tagged value with the given type on the &lt;element&gt; IModelElement.<br/>
         * The tagged value and the parameter are created if they don't exist.<br/>
         * If values is <tt>null</tt> or empty the existing tag is deleted.
         * @param element IModelElement on which the tagged value is created or updated.
         * @param tagType The tagged value type name.
         * @param value The values to store on the first tag parameter. If values is <tt>null</tt> the tag is deleted.
         */
        @objid ("b2e9b45c-5e9b-499e-a403-e9014db81b2f")
        private void putTagValue(ModelElement element, TagType tagType, String value) {
            IModelFactory factory = ModelFactory.getFactory(element);
            
            TaggedValue tag = getTag(element, tagType);
            
            if (value == null) {
                // Delete the tag if no more value
                if (tag != null)
                    tag.delete();
                return;
            } else {
                // Create the tagged value if necessary
                if (tag == null) {
                    tag = factory.createTaggedValue(tagType, element);
                }
            
                final List<TagParameter> oldParameters = tag.getActual();
                int cpt = 0;
            
                // Replace existing parameter values and delete spare ones
                for (int i = 0; i < oldParameters.size() && i < 1; i++) {
                    oldParameters.get(i).setValue(value);
                    cpt++;
                }
            
                // Delete spare parameter                
                while (oldParameters.size() > 1) {
                    oldParameters.get(oldParameters.size() - 1).delete();
                }
            
                // Add missing parameter
                if (cpt < 1) {
                    factory.createTagParameter(value, tag);
                    cpt++;
                }
            }
        }

        /**
         * This operation sets the parameters of the tagged value with the given type on the &lt;element&gt; IModelElement.<br/>
         * The tagged value and the parameter are created if they don't exist.<br/>
         * If values is <tt>null</tt> or empty the existing tag is deleted.
         * @param element IModelElement on which the tagged value is created or updated.
         * @param type The tagged value type name.
         * @param values The values to store on the tag parameters. If values is <tt>null</tt> or empty the tag is deleted.
         */
        @objid ("9c724510-7ede-417c-a93e-703c901bf66f")
        private void putTagValues(ModelElement element, TagType type, List<String> values) {
            IModelFactory factory = ModelFactory.getFactory(element);
            
            TaggedValue tag = getTag(element, type);
            
            if (values == null || values.isEmpty()) {
                // Delete the tag if no more value
                if (tag != null)
                    tag.delete();
                return;
            } else {
                // Create the tagged value if necessary
                if (tag == null) {
                    tag = factory.createTaggedValue(type, element);
                }
            
                final int newSize = values.size();
                final List<TagParameter> oldParameters = tag.getActual();
                int cpt = 0;
            
                // Replace existing parameter values and delete spare ones
                for (int i = 0; i < oldParameters.size() && i < newSize; i++) {
                    oldParameters.get(i).setValue(values.get(i));
                    cpt++;
                }
            
                // Delete spare parameter                
                while (oldParameters.size() > newSize) {
                    oldParameters.get(oldParameters.size() - 1).delete();
                }
            
                // Add missing parameters
                while (cpt < newSize) {
                    factory.createTagParameter(values.get(cpt), tag);
                    cpt++;
                }
            }
        }

        @objid ("2c6eebda-2b89-44d3-a6e5-b059f23e6710")
        protected boolean displayTagType(TagType tagType) {
            return isShowHiddenAnnotations() || !tagType.isIsHidden();
        }

        @objid ("c7c423dc-b871-4e45-a930-03538b2c369b")
        private boolean isShowHiddenAnnotations() {
            // TODO Auto-generated method stub
            return false;
        }

    }

}
