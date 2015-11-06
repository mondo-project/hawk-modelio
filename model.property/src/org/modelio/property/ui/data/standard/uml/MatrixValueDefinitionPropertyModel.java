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
import org.modelio.metamodel.uml.infrastructure.matrix.MatrixValueDefinition;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>MatrixValueDefinition</i> data model.
 * <p>
 * This class provides the list of properties for the <i>MatrixValueDefinition</i> metaclass.
 */
@objid ("81532832-9e83-4eb5-b1a0-ffb4b0f71c34")
public class MatrixValueDefinitionPropertyModel extends AbstractPropertyModel<MatrixValueDefinition> {
    /**
     * Properties to display for <i>MatrixValueDefinition</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("f034429e-8371-4b01-b9a1-9066f7b985b7")
    private static final String[] PROPERTIES = new String[] {"MatrixValueDefinition"};

    @objid ("96d97f15-684b-4141-b18e-03d58d834b91")
    private StringType labelStringType;

    /**
     * Create a new <i>MatrixValueDefinition</i> data model from an <i>MatrixValueDefinition</i>.
     */
    @objid ("336df0ff-4e46-4a59-a7f4-ae5cb99b8de0")
    public MatrixValueDefinitionPropertyModel(MatrixValueDefinition theMatrixValueDefinition) {
        super(theMatrixValueDefinition);
        
        this.labelStringType = new StringType(false);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("90396145-c9ed-4712-8654-746e081b44e9")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("96be6176-685d-4580-9de1-aac0ed55b12a")
    @Override
    public int getRowsNumber() {
        return MatrixValueDefinitionPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("9eff4c6b-7e7e-4ee0-9618-d6570b1bee2a")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return MatrixValueDefinitionPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
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
    @objid ("721f43bf-bbb5-46ab-87ff-bf78afff9373")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
                case 0: // Header
                    return this.labelStringType;
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
    @objid ("15b3a332-af0c-46da-86ca-d67492df3abe")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
                case 0:
                    return; // Header cannot be modified
                default:
                    return;
            }
        default:
            return;
        }
    }

}
