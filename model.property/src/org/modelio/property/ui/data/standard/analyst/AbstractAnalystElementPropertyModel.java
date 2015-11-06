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
import org.modelio.metamodel.analyst.AnalystElement;
import org.modelio.metamodel.analyst.AnalystPropertyTable;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("01f89e83-fde5-4e61-8378-fbec71cb2c3c")
abstract class AbstractAnalystElementPropertyModel<T extends AnalystElement> extends AbstractPropertyModel<T> {
    /**
     * Properties to display for <i>AnalystElement</i>.
     * <p>
     * This array contains the first column values: <ul>
     * <li>for the first row the value is the table header label (usually the
     * metaclass name)
     * <li>for other EditedElement rows the values usually match the
     * meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("cc89f690-4b34-4d61-b973-fad5e040fa4a")
    private List<String> properties = new ArrayList<>();

    @objid ("34b01ff5-7000-4740-b050-951fb4f22987")
    private StringType labelStringType = new StringType(false);

    @objid ("e52b6b31-b199-4a35-927a-cf193a2f7ed1")
    private StringType stringType = new StringType(true);

    @objid ("0c3d4491-555a-49d4-bc8a-304eea37c88b")
    private BooleanType booleanType = new BooleanType();

    @objid ("b0faf59e-369a-4960-ae2b-9d82c66a207b")
    private Map<EnumeratedPropertyType, ListType> listType = new HashMap<>();

    @objid ("ff428c55-1493-421c-a55d-9b775d401365")
    protected IMModelServices modelService;

    @objid ("4a3fcd06-57e7-4b13-85dd-16204f0c0cdc")
    private IActivationService activationService;

    @objid ("bbedbc57-0551-4de1-be25-32908e036670")
    private IProjectService projectService;

    @objid ("cd99e0c5-08d6-47d1-85cb-d584094a30bd")
    private SingleElementType elementType;

    @objid ("920ba5a8-b649-4339-b059-c83a2dd16b7b")
    public AbstractAnalystElementPropertyModel(T theEditedElement, IMModelServices modelService, IProjectService projectService, IActivationService activationService) {
        super(theEditedElement);
        this.elementType = new SingleElementType(true, Element.class, projectService.getSession());
        
        this.modelService = modelService;
        this.activationService = activationService;
        this.projectService = projectService;
        
        buildDisplayedProperties();
    }

    @objid ("e95eb318-a34b-4194-be64-dd289c0eb29c")
    private void buildDisplayedProperties() {
        this.properties.add(this.theEditedElement.getMClass().getName());
        this.properties.add("Name");
        this.properties.add("Text");
        
        // Get the propertySet from the containing AnalystContainer.
        PropertyTableDefinition propertySet = this.theEditedElement.getDefaultProperties().getType();
        if (propertySet != null) {
            for (PropertyDefinition property : propertySet.getOwned()) {
                this.properties.add(property.getName());
            }
        }
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("f0d0a1f6-d273-4e9f-a178-7d8ab53d0deb")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("cc3348f4-93c0-4880-ac88-0528c36b909c")
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
    @objid ("bad5ab44-7e63-4dce-a3f0-47b8615525a2")
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            // col 0 is the title column
            return this.properties.get(row);
        } else if (col == 1) {
            // col 1 is the property value
            if (row == 0) {
                // Header line
                return "Value";
            } else if (row == 1) {
                // name
                return this.theEditedElement.getName();
            } else if (row == 2) {
                // Text definition
                return this.theEditedElement.getDefinition();
            } else if (3 <= row && row < this.properties.size()) {
                // properties
                return getPropertyValue(row - 3);
            }
            // else
            return null;
        }
        // else
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
    @objid ("a890989d-7b72-4d92-9fb6-3c46025dd552")
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
                return new MultilineStringType(this.theEditedElement, this.properties.get(row), true);
            }
            else if (3 <= row && row < this.properties.size()) {
                return getPropertyType(row - 3);
            }
            return null;
        }
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
    @objid ("348489b1-c93d-4650-9e4b-6e5fac24da46")
    @Override
    public void setValueAt(int row, int col, Object value) {
        if (col == 0) {
            return;
        }
        else if (col == 1) // col 1 is the property value
        {
            if (row == 0) {
                return; // Header cannot be modified
            }
            else if (row == 1) {
                this.theEditedElement.setName(value.toString());
                return;
            }
            else if (row == 2) {
                this.theEditedElement.setDefinition(value.toString());
            }
            else {
                setPropertyValue(row - 3, value.toString());
                return;
            }
        }
        return;
    }

    @objid ("b6826c14-ea2d-4ed1-810d-3dbda7b31f85")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            return false;
        } else if (3 <= row && row < this.properties.size()) {
            int propertyIndex = row - 3;
            
            PropertyDefinition property = this.theEditedElement.getDefaultProperties().getType().getOwned().get(propertyIndex);
            if (!property.isIsEditable()) {
                return false;
            }
            
            // Rich text properties are always considered "editable", the editor itself will be read only if necessary.
            if (property.getType().getName().equals("RichText")) {
                return true;
            } 
        }
        return this.theEditedElement.isModifiable();
    }

    /**
     * Returns the value of the PropertyDefinition at propertyIndex. Exact PropertyDefinition to
     * search a value for is defined in the PropertyTableDefinition of the containing AnalystContainer.
     * @param propertyIndex @return
     */
    @objid ("5e049920-e805-4883-aaa2-1c5855fdc527")
    private Object getPropertyValue(int propertyIndex) {
        // Get the correct PropertyDefinition:
        PropertyDefinition property = this.theEditedElement.getDefaultProperties().getType().getOwned().get(propertyIndex);
        boolean isBooleanProperty = (property.getType().getName().equals("Boolean")); //$NON-NLS-1$
        boolean isRichTextProperty = (property.getType().getName().equals("RichText")); //$NON-NLS-1$
        boolean isElement = (property.getType().getName().equals("Element")); //$NON-NLS-1$
        
        // If property values are defined on this AnalystElement
        // go through theEditedElement and look for the one corresponding
        // to this property
        AnalystPropertyTable propertyTable = this.theEditedElement.getAnalystProperties();
        if (propertyTable != null) {
            String stringValue = propertyTable.getProperty(property);
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
                } else if (isRichTextProperty) {
                    String docTypeName = getRichTextType();
                    for (ExternDocument doc : this.theEditedElement.getDocument()) {
                        if (doc.getType() != null && doc.getType().getName().equals(docTypeName)) {
                            return doc;
                        }
                    }
                    return null;
                }
                return stringValue;
            }
        
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

    /**
     * Returns the type of the PropertyDefinition at propertyIndex. Exact PropertyDefinition to
     * search a type for is defined in the PropertyTableDefinition of the containing AnalystContainer.
     * @param propertyIndex @return
     */
    @objid ("2e04edf6-38f7-40f3-ae7a-dd28535780ee")
    private IPropertyType getPropertyType(int propertyIndex) {
        // Get the correct PropertyDefinition:
        PropertyDefinition property = this.theEditedElement.getDefaultProperties().getType().getOwned().get(propertyIndex);
        org.modelio.metamodel.uml.infrastructure.properties.PropertyType propertyType = property.getType();
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
            } else if (propertyName.equals("RichText")) {//$NON-NLS-1$
                return new ScopeRichTextType(this.theEditedElement, true, this.projectService, this.activationService);
            } else {
                // Unknown property type, treat as a text
                return this.stringType;
            }
        }
    }

    @objid ("6c897a34-e501-4082-89d2-8d17f946512a")
    private void setPropertyValue(int propertyIndex, String value) {
        // Get the correct PropertyDefinition:
        PropertyDefinition property = this.theEditedElement.getDefaultProperties().getType().getOwned().get(propertyIndex);
        
        // If property values are defined on this AnalystElement
        // go through theEditedElement and look for the one corresponding
        // to this property
        AnalystPropertyTable propertyTable = this.theEditedElement.getAnalystProperties();
        if (propertyTable == null) {
            // No AnalystPropertyTable defined for this AnalystElement yet, create one
            propertyTable = this.modelService.getModelFactory().createAnalystPropertyTable();
            propertyTable.setType(this.theEditedElement.getDefaultProperties().getType());
            this.theEditedElement.setAnalystProperties(propertyTable);
        }
        
        if (propertyTable.getStatus().isModifiable()) {
            propertyTable.setProperty(property, value);
        }
    }

    @objid ("d6d3e72f-2d0e-4525-a738-db654e03e7c5")
    protected abstract List<? extends AnalystElement> getOwnedAnalystElements();

    @objid ("7186f838-9b81-465d-895a-260b7acaab1a")
    protected abstract String getRichTextType();

}
