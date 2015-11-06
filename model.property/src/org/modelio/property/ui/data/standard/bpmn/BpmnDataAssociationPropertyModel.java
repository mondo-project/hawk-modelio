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
                                    

package org.modelio.property.ui.data.standard.bpmn;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.bpmn.objects.BpmnDataAssociation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>BpmnDataAssociation</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnDataAssociation</i> metaclass.
 */
@objid ("8e1cf219-c068-11e1-8c0a-002564c97630")
public class BpmnDataAssociationPropertyModel extends AbstractPropertyModel<BpmnDataAssociation> {
    /**
     * Properties to display for <i>BpmnDataAssociation</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a5b96112-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "DataAssociation", "Name", "Assignment", "Transfomation", "Language" };

    /**
     * Create a new <i>BpmnDataAssociation</i> data model from an <i>BpmnDataAssociation</i>.
     */
    @objid ("8e1cf224-c068-11e1-8c0a-002564c97630")
    public BpmnDataAssociationPropertyModel(BpmnDataAssociation theEditedElement) {
        super(theEditedElement);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e1cf22a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e1cf22f-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnDataAssociationPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e1cf234-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnDataAssociationPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        return this.theEditedElement.getAssignment();
                    case 3:
                        return this.theEditedElement.getTransfomation();
                    case 4:
                        return this.theEditedElement.getLanguage();
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
    @objid ("8e1cf23a-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return new StringType(false);
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return new StringType(false);
                    case 1:
                        return new StringType(true);
                    case 2:
                        return new StringType(true);
                    case 3:
                        return new StringType(true);
                    case 4:
                        return new StringType(true);
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
    @objid ("8e1cf240-c068-11e1-8c0a-002564c97630")
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
                    case 2:
                        this.theEditedElement.setAssignment((String) value);
                        break;
                    case 3:
                        this.theEditedElement.setTransfomation((String) value);
                        break;
                    case 4:
                        this.theEditedElement.setLanguage((String) value);
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
