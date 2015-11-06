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
                                    

package org.modelio.property.ui.data.standard.uml.templateparameter;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>TemplateParameter</i> data model for "value" template parameters.
 * <p>
 * This class provides the list of properties for the <i>TemplateParameter</i> metaclass.
 * <p>
 * This data model has been manually created
 */
@objid ("8f925c0c-c068-11e1-8c0a-002564c97630")
class ValueTemplateParameterPropertyModel extends AbstractPropertyModel<TemplateParameter> {
    /**
     * Properties to display for <i>TemplateParameter</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a870c0a8-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "TemplateParameter",
            "IsValueParameter", "Name", "Type", "DefaultValue" };

    @objid ("fb325056-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fb325057-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fb325058-c5d4-11e1-8f21-002564c97630")
    private SingleElementType generalClassType;

    @objid ("fb325059-c5d4-11e1-8f21-002564c97630")
    private EnumType templateKindEnumType;

    /**
     * Create a new <i>TemplateParameter</i> data model from an <i>TemplateParameter</i>.
     * @param theEditedElement
     * @param model
     */
    @objid ("8f925c17-c068-11e1-8c0a-002564c97630")
    public ValueTemplateParameterPropertyModel(TemplateParameter theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.generalClassType = new SingleElementType(false, GeneralClass.class, CoreSession.getSession(this.theEditedElement));
        this.templateKindEnumType = new EnumType(TemplateKind.class);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f925c1d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f925c22-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ValueTemplateParameterPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f925c27-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return ValueTemplateParameterPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return TemplateKind.ValueTemplate;
                    case 2:
                        return this.theEditedElement.getName();
                    case 3:
                        return this.theEditedElement.getType();
                    case 4:
                        ModelElement  ret = this.theEditedElement.getDefaultType();
                        if (ret != null)
                            return ret.getName();
        
                        return this.theEditedElement.getDefaultValue();
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * Return the type of the element displayed at the specified row and column.
     * <p>
     * This type will be used to choose an editor and a renderer for each cell of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8f925c2d-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return this.labelStringType;
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return this.labelStringType;
                    case 1: // is value parameter
                        return this.templateKindEnumType;
                    case 2: // Name
                        return this.stringType;
                    case 3: // type
                        return this.generalClassType;
                    case 4: // default value
                        return this.stringType;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("8f94bd25-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
            case 0: // Keys cannot be modified
                return;
            case 1: // col 1 is the property value
                switch (row) {
                    case 0:
                        return; // Header cannot be modified
                    case 1: // Template kind : value parameter/type parameter
                        if (value == TemplateKind.TypeTemplate)
                            this.theEditedElement.setIsValueParameter(false);
                        break;
                    case 2: // Name
                        this.theEditedElement.setName((String) value);
                        break;
                    case 3: // Type
                        this.theEditedElement.setType((ModelElement) value);
                        break;
                    case 4: // Default value
                        // Erase old model element value 
                        ModelElement old1 = this.theEditedElement.getDefaultType();
                        if (old1 != null) {
                            this.theEditedElement.setDefaultType(null);
                        } 
                        
                        // Set new value
                        if (value != null) {
                            if (String.class.isAssignableFrom(value.getClass()))
                                this.theEditedElement.setDefaultValue((String) value);
                        }
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

}
