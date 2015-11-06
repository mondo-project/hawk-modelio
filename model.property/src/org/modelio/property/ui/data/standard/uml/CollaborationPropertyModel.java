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
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.statik.Collaboration;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>Collaboration</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Collaboration</i> metaclass.
 */
@objid ("8ee34f72-c068-11e1-8c0a-002564c97630")
public class CollaborationPropertyModel extends AbstractPropertyModel<Collaboration> {
    /**
     * Properties to display for <i>Collaboration</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6a0466b-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Collaboration", "Name", "Visibility", "IsAbstract", "IsConcurrent", "IsLeaf", "IsRoot"};

    @objid ("fa0d0af3-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa0d0af4-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa0d0af5-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("fa0d0af6-c5d4-11e1-8f21-002564c97630")
    private EnumType visibilityEnumType;

    /**
     * Create a new <i>Collaboration</i> data model from an <i>Collaboration</i>.
     */
    @objid ("8ee34f7d-c068-11e1-8c0a-002564c97630")
    public CollaborationPropertyModel(Collaboration theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.visibilityEnumType = new EnumType(VisibilityMode.class);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ee34f83-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ee34f88-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return CollaborationPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ee4d5e6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return CollaborationPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getVisibility();
            case 3:
                return this.theEditedElement.isIsAbstract()?Boolean.TRUE:Boolean.FALSE;
            case 4:
                return this.theEditedElement.isIsConcurrent()?Boolean.TRUE:Boolean.FALSE;
            case 5:
                // The logic here has been inverted to allow a positive logic: The displayed field is now can be inherited.
                return (!this.theEditedElement.isIsLeaf())?Boolean.TRUE:Boolean.FALSE;
            case 6:
                return this.theEditedElement.isIsRoot()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8ee4d5ec-c068-11e1-8c0a-002564c97630")
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
            case 2:
                return this.visibilityEnumType;
            case 3:
                return this.booleanType;
            case 4:
                return this.booleanType;
            case 5:
                return this.booleanType;
            case 6:
                return this.booleanType;
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
    @objid ("8ee4d5f4-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setVisibility((VisibilityMode) value);
                break;
            case 3:
                this.theEditedElement.setIsAbstract(((Boolean) value).booleanValue());
                if (((Boolean) value).booleanValue() == true) {
                    this.theEditedElement.setIsLeaf(false);
                }
                break;
            case 4:
                this.theEditedElement.setIsConcurrent(((Boolean) value).booleanValue());
                break;
            case 5:
                // The logic here has been inverted to allow a positive logic: The displayed field is now can be inherited.
                this.theEditedElement.setIsLeaf(!((Boolean) value).booleanValue());
                if (((Boolean) value).booleanValue() == false) {
                    this.theEditedElement.setIsAbstract(false);
                }
                break;
            case 6:
                this.theEditedElement.setIsRoot(((Boolean) value).booleanValue());
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
