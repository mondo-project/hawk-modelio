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
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.informationFlow.DataFlow;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>DataFlow</i> data model.
 * <p>
 * This class provides the list of properties for the <i>DataFlow</i> metaclass.
 */
@objid ("8efa32be-c068-11e1-8c0a-002564c97630")
public class DataFlowPropertyModel extends AbstractPropertyModel<DataFlow> {
    /**
     * Properties to display for <i>DataFlow</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6cfe1e9-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"DataFlow", "Name", "Destination", "SModel"};

    @objid ("fa2d1601-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa2e9c93-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa2e9c94-c5d4-11e1-8f21-002564c97630")
    private SingleElementType namespaceType;

    @objid ("fa2e9c95-c5d4-11e1-8f21-002564c97630")
    private SingleElementType signalType;

    /**
     * Create a new <i>DataFlow</i> data model from an <i>DataFlow</i>.
     * @param model
     */
    @objid ("8efa32c9-c068-11e1-8c0a-002564c97630")
    public DataFlowPropertyModel(DataFlow theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.signalType = new SingleElementType(false, Signal.class, session);
        this.namespaceType = new SingleElementType(false, NameSpace.class, session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8efa32cf-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8efa32d4-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return DataFlowPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8efa32d9-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return DataFlowPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getDestination();
                case 3:
                    return this.theEditedElement.getSModel();
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
    @objid ("8efa32df-c068-11e1-8c0a-002564c97630")
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
                    return this.namespaceType;
                case 3:
                    return this.signalType;
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
    @objid ("8efa32e7-c068-11e1-8c0a-002564c97630")
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
                    this.theEditedElement.setDestination((NameSpace) value);
                    break;
                case 3:
                    this.theEditedElement.setSModel((Signal) value);
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