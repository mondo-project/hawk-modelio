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
import org.modelio.metamodel.uml.statik.Connector;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>Connector</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Connector</i> metaclass.
 */
@objid ("31656e6b-f825-456d-93b2-bfa38cc2d2a5")
public class ConnectorPropertyModel extends AbstractPropertyModel<Connector> {
    /**
     * Properties to display for <i>Connector</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("1b3c9275-52b7-4035-a6de-bf5eba003de9")
    private static final String[] PROPERTIES = new String[] {"Connector", "Name"};

    @objid ("373d1770-d069-42bd-bda2-1c4eed42f494")
    private StringType labelStringType;

    @objid ("9b80cdeb-95b3-454a-bfe3-88908993d001")
    private StringType stringType;

    /**
     * Create a new <i>Connector</i> data model from an <i>Connector</i>.
     */
    @objid ("f80f1a44-2e90-4f05-8e1b-c6e0f4a8bf9f")
    public ConnectorPropertyModel(Connector theConnector) {
        super(theConnector);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("169e4f03-8dc2-474c-a977-efb813434e21")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("84c01853-ad81-49c2-8d39-26e9cb72c7ee")
    @Override
    public int getRowsNumber() {
        return ConnectorPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("e0b1615d-779c-437b-8172-390d932d41cc")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ConnectorPropertyModel.PROPERTIES[row];
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
    @objid ("497a0e98-7141-4b33-b001-7d1e0727117f")
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
    @objid ("1ef2df74-8b9d-4c8b-8fda-73b1fdfe0ee7")
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
