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
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>DiagramSet</i> data model.
 * <p>
 * This class provides the list of properties for the <i>DiagramSet</i> metaclass.
 */
@objid ("498e2769-06e5-4dc9-9578-e35f8bc2e8ec")
public class DiagramSetPropertyModel extends AbstractPropertyModel<DiagramSet> {
    /**
     * Properties to display for <i>DiagramSet</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("5d4932ba-7a57-4989-ae11-de1fe171b030")
    private static final String[] PROPERTIES = new String[] {"DiagramSet", "Name"};

    @objid ("6e68663b-68e3-40f4-b80f-857c405317d8")
    private StringType stringType = null;

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("1315a214-f93f-48a4-accb-2fefa9ee2b9d")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("37669274-b2e5-452d-9ab8-12c1fb6170c1")
    @Override
    public int getRowsNumber() {
        return DiagramSetPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("1cb090ca-fdd4-49ff-8c00-71344bc8b129")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return DiagramSetPropertyModel.PROPERTIES[row];
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
    @objid ("b33141de-1e01-4519-bb07-dd03a4479a59")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return this.stringType;
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return this.stringType;
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
    @objid ("afff8f8d-b812-4aae-8aab-a23fa7ba7ee2")
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

    /**
     * Create a new <i>DiagramSet</i> data model from an <i>DiagramSet</i>.
     */
    @objid ("b5c697cf-30cc-4edb-a220-565f4d275d9b")
    public DiagramSetPropertyModel(final DiagramSet theEditedElement) {
        super(theEditedElement);
        
        this.stringType = new StringType(false);
    }

}
