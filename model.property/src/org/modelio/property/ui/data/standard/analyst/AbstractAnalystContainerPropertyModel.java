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
                                    

package org.modelio.property.ui.data.standard.analyst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.list.ListType;
import org.modelio.core.ui.ktable.types.text.MultilineStringType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.editors.richnote.gui.ktable.ScopeRichTextType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.facilities.AnalystPropertiesHelper;
import org.modelio.metamodel.analyst.AnalystContainer;
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.AnalystPropertyTable;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyType;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("7fab0858-d842-42bf-a3df-28fd657ee612")
abstract class AbstractAnalystContainerPropertyModel<T extends AnalystContainer> extends AbstractPropertyModel<T> {
    /**
     * Properties to display for <i>AnalystContainer</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the
     * metaclass name)
     * <li>for otheEditedElement rows the values usually match the
     * meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("c0a489d7-01b3-49a9-90c5-1f773ef29dab")
    private List<String> properties = new ArrayList<>();

    @objid ("0f50849f-58aa-4947-a48a-de08dc00893a")
    private StringType labelStringType = new StringType(false);

    @objid ("f7aad23e-5022-4cd7-b3ac-4a7807fe3c0f")
    private StringType stringType = new StringType(true);

    @objid ("1d470ef1-bcdd-4b1f-a008-b23af285ebcd")
    private BooleanType booleanType = new BooleanType();

    @objid ("e97e7fe6-e2e9-47f7-b647-90e22fac2b55")
    private Map<EnumeratedPropertyType, ListType> listType = new HashMap<>();

    @objid ("0a282ac8-f270-4d98-8e9f-469c4b708e98")
    protected IMModelServices modelService;

    @objid ("e342e29e-bc3c-476b-8cd5-a7708ad2dbcb")
    protected final IModel model;

    /**
     * The Modelio activation service to use to open the rich note.
     */
    @objid ("308df6d0-79ff-46e3-a12b-247df204acfc")
    private IActivationService activationService;

    @objid ("fc570747-81ad-4378-a903-b42282c78f21")
    private IProjectService projectService;

    @objid ("d55922bd-aeac-47a9-ba1e-8f7616de37cb")
    private SingleElementType elementType;

    @objid ("06007e66-93e6-4afe-96b2-18786f2bde82")
    protected AbstractAnalystContainerPropertyModel(T editedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService, IModel model) {
        super(editedElement);
        this.elementType = new SingleElementType(true, Element.class, projectService.getSession());
        
        this.modelService = modelService;
        this.model = model;
        this.activationService = activationService;
        this.projectService = projectService;
        
        buildDisplayedProperties();
    }

    /**
     * Rebuild the displayed properties
     */
    @objid ("de2c45d7-c396-43fc-a9e2-ca137fbb7589")
    private void buildDisplayedProperties() {
        this.properties.clear();
        this.properties.add(this.theEditedElement.getMClass().getName());
        this.properties.add("Name");
        this.properties.add("Type");
        this.properties.add("Text");
        
        // Get the propertySet from the containing AnalystContainer.
        for (PropertyDefinition property : getDisplayedProperties()) {
            this.properties.add(property.getName());
        }
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("e69fe5c8-bccc-407c-9bd0-4a1933b58400")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("0c87a772-9722-4337-a4c5-5c31007b527e")
    @Override
    public int getRowsNumber() {
        return this.properties.size();
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("2bc3d72d-61a6-4ae4-911b-143a6fd45542")
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return this.properties.get(row);
        }
        else if (col == 1) // col 1 is the property value
        {
            if (row == 0) {
                return "Value";
            }
            else if (row == 1) {
                return this.theEditedElement.getName();
            }
            else if (row == 2) {
                return this.theEditedElement.getAnalystProperties().getType();
            }
            else if (row == 3) {
                return this.theEditedElement.getDefinition();
            }
            else if (isPropertyRow(row)) {
                return getPropertyValue(row - 4);
            }
            else
                return null;
        }
        else
            return null;
    }

    /**
     * Return the type of the element displayed at the specified row and column.
     * <p>
     * This type will be used to choose an editor and a renderer for each cell
     * of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("d4ac2860-b9ff-4b41-85b9-a02d7e777013")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        if (col == 0) {
            return this.labelStringType;
        }
        else if (col == 1) // col 1 is the property value
        {
            if (row == 0) {
                return this.labelStringType;
            }
            else if (row == 1) {
                return this.stringType;
            }
            else if (row == 2) {
                return getAvailableSets();
            }
            else if (row == 3) {
                return new MultilineStringType(this.theEditedElement, this.properties.get(row), true);
            }
            else if (isPropertyRow(row)) {
                return getPropertyType(row - 4);
            }
            else 
                return null;
        }
        else 
            return null;
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("0642562d-af83-4681-83fa-4c43fd810636")
    @Override
    public void setValueAt(int row, int col, Object value) {
        if (col == 0) {
            return;
        }
        if (col == 1) // col 1 is the property value
        {
            if (row == 0) {
                return; // Header cannot be modified
            }
            if (row == 1) {
                this.theEditedElement.setName(value.toString());
                return;
            }
            if (row == 2) {
                setType((PropertyTableDefinition) value);
                return;
            }
            if (row == 3) {
                this.theEditedElement.setDefinition(value.toString());
            }
            if (isPropertyRow(row) ) {
                setPropertyValue(row - 4, value.toString());
            }
            return;
        }
        return;
    }

    @objid ("384457dd-c566-426b-9658-d010883848c0")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            return false;
        } else if (isPropertyRow(row)) {
            int propertyIndex = row - 4;
            PropertyDefinition property = getDisplayedProperties().get(propertyIndex);
            if (!property.isIsEditable()) {
                return false;
            }
        }
        return this.theEditedElement.isModifiable();
    }

    @objid ("47600e10-aca2-4b4d-bf73-3888b88dfe2c")
    private Object getPropertyValue(int propertyIndex) {
        // Get the correct PropertyDefinition:
        PropertyDefinition property = getDisplayedProperties().get(propertyIndex);
        boolean isBooleanProperty = (property.getType().getName().equals("Boolean")); //$NON-NLS-1$
        boolean isElement = (property.getType().getName().equals("Element")); //$NON-NLS-1$
        // If property values are defined on this AnalystContainer
        // go through theEditedElement and look for the one corresponding
        // to this property
        String stringValue = this.theEditedElement.getAnalystProperties().getProperty(property);
        
        if (stringValue != null) {
            if (isBooleanProperty) {
                return new Boolean(stringValue);
            } else if (isElement) {
                try {
                    return this.projectService.getSession().getModel().findByRef(new MRef(stringValue));
                } catch (Exception e) {
                    // Ignore invalid values
                    return null;
                }
            }
                return stringValue;
        }
        
        // No value is already defined for this property, return the default
        // value.
        if (isElement) {
            try {
                return this.projectService.getSession().getModel().findByRef(new MRef(property.getDefaultValue()));
            } catch (Exception e) {
                return null;
            }
        } else if (isBooleanProperty) {
            return new Boolean(property.getDefaultValue());
        } else {
            return property.getDefaultValue();
        }
    }

    @objid ("a7ef7ca4-823a-469f-9076-668d4451e345")
    private IPropertyType getPropertyType(int propertyIndex) {
        // Get the correct PropertyDefinition:
        PropertyDefinition property = getDisplayedProperties().get(propertyIndex);
        PropertyType propertyType = property.getType();
        if (propertyType instanceof EnumeratedPropertyType) {
            EnumeratedPropertyType enumeratedPropertyType = (EnumeratedPropertyType) propertyType;
            ListType type = this.listType.get(enumeratedPropertyType);
            if (type != null) {
                return type;
            } else {
                // Type not found yet, create it.
                List<String> values = new ArrayList<>();
                for (PropertyEnumerationLitteral enumLitteral : enumeratedPropertyType.getLitteral()) {
                    values.add(enumLitteral.getName());
                }
                type = new ListType(false, values);
                this.listType.put(enumeratedPropertyType, type);
                return type;
            }
        } else {
            // property is not an enumerated, try the known property types
            String propertyName = propertyType.getName();
            if (propertyName.equals("Boolean")) {
                return this.booleanType;
            } else if (propertyName.equals("Element")) {
                return this.elementType;
            } else if (propertyName.equals("MultiText")) {
                return new MultilineStringType(this.theEditedElement, "Text", true);
            } else if (propertyName.equals("Text") || propertyName.equals("Integer") || propertyName.equals("Real") || propertyName.equals("Date")) {
                return this.stringType;
            } else if (propertyName.equals("RichText")) { //$NON-NLS-1$
                return new ScopeRichTextType(this.theEditedElement, true, this.projectService, this.activationService);
            } else {
                // Unknown property type, treat as a text
                return this.stringType;
            }
        }
    }

    @objid ("cf088907-8fe0-4f2e-9f77-fda975cffb31")
    private void setPropertyValue(int propertyIndex, String value) {
        // Get the correct PropertyDefinition:
        PropertyDefinition property = getDisplayedProperties().get(propertyIndex);
        
        // If property values are defined on this AnalystContainer
        // go through theEditedElement and look for the one corresponding
        // to this property
        AnalystPropertyTable propertyValueSet = this.theEditedElement.getAnalystProperties();
        if (propertyValueSet == null) {
            // No AnalystPropertyTable defined for this AnalystContainer yet, create one
        
            propertyValueSet = this.modelService.getModelFactory().createAnalystPropertyTable();
            propertyValueSet.setType(this.theEditedElement.getAnalystProperties().getType());
            this.theEditedElement.setAnalystProperties(propertyValueSet);
        }
        
        propertyValueSet.setProperty(property, value);
    }

    /**
     * Change the edited analyst container type.
     * @param referenceProperties the new type, must be a {@link PropertyTableDefinition}.
     */
    @objid ("7819c9c8-ee93-4b7a-b542-695751236c0f")
    private void setType(PropertyTableDefinition referenceProperties) {
        AnalystPropertyTable outdatedPropertyTable = this.theEditedElement.getAnalystProperties();
        if (outdatedPropertyTable == null) {
            // Create default values for new properties.
            outdatedPropertyTable = this.modelService.getModelFactory().createAnalystPropertyTable();
            this.theEditedElement.setAnalystProperties(outdatedPropertyTable);
        }
        
        // Set the new type
        outdatedPropertyTable.setType(referenceProperties);
        
        // If setting to type to null, delete all values and the value set.
        if (referenceProperties == null) {
            // If setting type to null, empty the values.
            outdatedPropertyTable.setContent(new Properties());
        } else {
            // Run through the default values and check if theEditedElement still apply to a property.
            keepValidValues(referenceProperties, outdatedPropertyTable);
        
            // Add all missing values
            addMissingValues(referenceProperties, outdatedPropertyTable);
        }
        
        // Finally, apply this to all children.
        AnalystPropertiesHelper.synchronizeAnalystProperties(this.theEditedElement, getOwnedAnalystElements());
        
        buildDisplayedProperties();
    }

    @objid ("caa7b57a-7274-4f92-8c1d-0008d963b7e4")
    private static void keepValidValues(PropertyTableDefinition referenceProperties, AnalystPropertyTable outdatedPropertyTable) {
        Properties toUpdate = outdatedPropertyTable.toProperties();
        
        Properties newProps = new Properties();
        for (PropertyDefinition  referenceProperty: referenceProperties.getOwned()) {
            String name = referenceProperty.getName();
            String value = toUpdate.getProperty(name);
        
            // Keep values corresponding to a reference property
            if (value != null) {
                newProps.setProperty(name, value);
            }
        }
        outdatedPropertyTable.setContent(newProps);
    }

    @objid ("01c5da16-ae5b-41c8-8bfd-9b69b8665c8a")
    private static void addMissingValues(PropertyTableDefinition referenceProperties, AnalystPropertyTable outdatedPropertyTable) {
        Properties toUpdate = outdatedPropertyTable.toProperties();
        
        boolean diff = false;
        for (PropertyDefinition referenceProperty : referenceProperties.getOwned()) {
            String key = referenceProperty.getName();
        
            // Set a value for each missing property
            if (! toUpdate.containsKey(key)) {
                toUpdate.setProperty(key, referenceProperty.getDefaultValue());
                diff = true;
            }
        }
        
        // Optimization: store the content only if a property was missing
        if (diff) {
            outdatedPropertyTable.setContent(toUpdate);
        }
    }

    @objid ("69832ff5-7069-414c-ba8d-4403a4f95bf1")
    private List<PropertyDefinition> getDisplayedProperties() {
        List<PropertyDefinition> displayedProperties = new ArrayList<>();
        
        PropertyTableDefinition propertySet = this.theEditedElement.getAnalystProperties().getType();
        if (propertySet != null) {
            for (PropertyDefinition property : propertySet.getOwned()) {
                if (!property.getType().getName().equals("RichText")) {
                    displayedProperties.add(property);
                }
            }
        }
        return displayedProperties;
    }

    @objid ("6713cf19-6e8c-4143-be7d-d1e2d4b38b1d")
    protected abstract List<? extends AnalystElement> getOwnedAnalystElements();

    @objid ("097c9e87-7329-4fad-b099-ca3732490f34")
    protected abstract IPropertyType getAvailableSets();

    @objid ("0f73fb35-8610-4724-afc8-d4c00c258e7d")
    protected boolean isPropertyRow(int row) {
        return 4 <= row && row < this.properties.size();
    }

}
