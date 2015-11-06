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
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>ConnectorEnd</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ConnectorEnd</i> metaclass.
 */
@objid ("8ef72572-c068-11e1-8c0a-002564c97630")
public class ConnectorEndPropertyModel extends AbstractPropertyModel<ConnectorEnd> {
    /**
     * Properties to display for <i>ConnectorEnd</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6c8bdc8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ConnectorEnd", "Name", "MultiplicityMax", "MultiplicityMin", "IsNavigable", "IsOrdered", "IsUnique", "RepresentedFeature"};

    @objid ("fa28821e-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa28821f-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa288220-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("fa288221-c5d4-11e1-8f21-002564c97630")
    private SingleElementType modelElementType;

    @objid ("bb778a80-19f2-11e2-ad19-002564c97630")
    private BooleanType navigabilityType;

    /**
     * Create a new <i>ConnectorEnd</i> data model from an <i>ConnectorEnd</i>.
     * @param model
     */
    @objid ("8ef7257d-c068-11e1-8c0a-002564c97630")
    public ConnectorEndPropertyModel(ConnectorEnd theConnectorEnd) {
        super(theConnectorEnd);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.modelElementType = new SingleElementType(false, ModelElement.class, CoreSession.getSession(this.theEditedElement));
        this.navigabilityType = new BooleanType();
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ef72583-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ef72588-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ConnectorEndPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ef7258d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ConnectorEndPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getMultiplicityMax();
                case 3:
                    return this.theEditedElement.getMultiplicityMin();
                case 4:
                    return this.theEditedElement.isNavigable();
                case 5:
                    return this.theEditedElement.isIsOrdered()?Boolean.TRUE:Boolean.FALSE;
                case 6:
                    return this.theEditedElement.isIsUnique()?Boolean.TRUE:Boolean.FALSE;
                case 7:
                    return this.theEditedElement.getRepresentedFeature();
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
    @objid ("8ef72593-c068-11e1-8c0a-002564c97630")
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
                    return this.stringType;
                case 3:
                    return this.stringType;
                case 4:
                    return this.navigabilityType;
                case 5:
                    return this.booleanType;
                case 6:
                    return this.booleanType;
                case 7:
                    return this.modelElementType;
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
    @objid ("8ef7259b-c068-11e1-8c0a-002564c97630")
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
                    this.theEditedElement.setMultiplicityMax((String) value);
                    break;
                case 3:
                    this.theEditedElement.setMultiplicityMin((String) value);
                    break;
                case 4:
                    this.theEditedElement.setNavigable((Boolean) value);
                    break;
                case 5:
                    this.theEditedElement.setIsOrdered(((Boolean) value).booleanValue());
                    break;
                case 6:
                    this.theEditedElement.setIsUnique(((Boolean) value).booleanValue());
                    break;
                case 7:
                    this.theEditedElement.setRepresentedFeature((ModelElement) value);
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
