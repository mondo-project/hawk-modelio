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
                                    

package org.modelio.property.ui.data.standard.uml;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyTableDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.TypedPropertyTable;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>TypedPropertyTable</i> data model.
 * <p>
 * This class provides the list of properties for the <i>TypedPropertyTable</i> metaclass.
 */
@objid ("8f6c45f1-c068-11e1-8c0a-002564c97630")
public class TypedPropertyTablePropertyModel extends AbstractPropertyModel<TypedPropertyTable> {
    /**
     * Properties to display for <i>TypedPropertyTable</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a81225e8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"TypedPropertyTable", "Type", "Value"};

    @objid ("faf0b3a0-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("faf0b3a1-c5d4-11e1-8f21-002564c97630")
    private SingleElementType propertySetType;

    @objid ("faf0b3a2-c5d4-11e1-8f21-002564c97630")
    private IPropertyType propertyValuesType;

    /**
     * Create a new <i>TypedPropertyTable</i> data model from an <i>TypedPropertyTable</i>.
     */
    @objid ("8f6c45fc-c068-11e1-8c0a-002564c97630")
    public TypedPropertyTablePropertyModel(TypedPropertyTable theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.propertySetType = new SingleElementType(false, PropertyTableDefinition.class, CoreSession.getSession(this.theEditedElement));
        //TODO this.propertyValuesType = new MultipleElementType(true, theEditedElement, "Value", PropertyValue.class, model);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f6c4602-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f6c4607-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return TypedPropertyTablePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f6c460c-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return TypedPropertyTablePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getType();
                //case 2:
                //    return this.theEditedElement.getValue();
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
     * This type will be used to choose an editor and a renderer for each cell
     * of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8f6c4612-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
                case 0: // Header
                    return this.labelStringType;
                case 1:
                    return this.propertySetType;
                case 2:
                    return this.propertyValuesType;
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
    @objid ("8f6c461a-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
                case 0:
                    return; // Header cannot be modified
                case 1:
                    this.theEditedElement.setType((PropertyTableDefinition) value);
                    break;
                case 2:
                    // TODO update data model generator when we know wath to do.
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