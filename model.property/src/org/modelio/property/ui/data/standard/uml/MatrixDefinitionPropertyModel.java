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
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.matrix.MatrixDefinition;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>MatrixDefinition</i> data model.
 * <p>
 * This class provides the list of properties for the <i>MatrixDefinition</i> metaclass.
 */
@objid ("a3453bb4-8d43-4f95-8acd-4ed5a6f188ec")
public class MatrixDefinitionPropertyModel extends AbstractPropertyModel<MatrixDefinition> {
    /**
     * Properties to display for <i>MatrixDefinition</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("215680a9-13b4-4fb3-8a05-66c0e018f74f")
    private static final String[] PROPERTIES = new String[] {"MatrixDefinition", "Name"};

    @objid ("0761f48f-b234-4ffe-bf55-e7876633c2e0")
    private StringType labelStringType;

    @objid ("5dcac16f-c4bc-4071-b9ac-a57d10a35fe8")
    private StringType stringType;

    /**
     * Create a new <i>MatrixDefinition</i> data model from an <i>MatrixDefinition</i>.
     */
    @objid ("a3ad97ba-80b1-445e-9d58-466c809eb3b7")
    public MatrixDefinitionPropertyModel(MatrixDefinition theMatrixDefinition) {
        super(theMatrixDefinition);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("ce0ae7f7-d84e-4f0e-9ab8-4e2abe9ddaaf")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("cef8996b-a1cf-446a-abf0-169e0c238168")
    @Override
    public int getRowsNumber() {
        return MatrixDefinitionPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("c9344a12-df90-44cc-9c4f-bcd2990b4b1d")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return MatrixDefinitionPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
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
    @objid ("6473a4d4-bd60-4aae-9f92-595affad42c0")
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
    @objid ("4997377e-b189-4ec0-885b-4ab65d6f35d9")
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
                    this.theEditedElement.setName((String) value);
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
