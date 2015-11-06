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
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationChannel;
import org.modelio.metamodel.uml.statik.Link;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>CommunicationChannel</i> data model.
 * <p>
 * This class provides the list of properties for the <i>CommunicationChannel</i> metaclass.
 * <p>
 * WARNING: This data model has been automaticaly generated.
 * <p>
 * MODIFICATION POLICY: If this data model needs to be manualy modified,
 * change the warning message by "This data model has been manually updated"
 */
@objid ("4a113a1d-5367-4413-b89b-45d93feb5c1e")
public class CommunicationChannelPropertyModel extends AbstractPropertyModel<CommunicationChannel> {
    /**
     * Properties to display for <i>CommunicationChannel</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("90991dd1-3ae7-4608-9af8-1ec6507eef15")
    private static final String[] PROPERTIES = new String[] {"CommunicationChannel", "Name", "Channel"};

    @objid ("193e6504-3d69-458a-9f55-c32424d0d403")
    private StringType labelStringType;

    @objid ("2324fe37-4c85-4ee9-a833-8eba8ad46981")
    private StringType stringType;

    @objid ("2089d087-3b1c-4dda-bb1f-9c34bbc5c182")
    private SingleElementType channelType;

    /**
     * Create a new <i>CommunicationChannel</i> data model from an <i>CommunicationChannel</i>.
     */
    @objid ("aebfdc09-805b-4af9-b434-d9a71d29c719")
    public CommunicationChannelPropertyModel(CommunicationChannel theCommunicationChannel) {
        super(theCommunicationChannel);
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.channelType = new SingleElementType(true, Link.class, CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("47604521-5d00-4ed1-87be-3b656e67a7d3")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("89cc91eb-05b6-4b5d-8060-d7c6ef7651ba")
    @Override
    public int getRowsNumber() {
        return CommunicationChannelPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("0cf02127-260c-4611-8094-869100cb2b63")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return CommunicationChannelPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getChannel();
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
    @objid ("cfb70236-8915-4a59-ae09-07e1a5527600")
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
                    return this.channelType;
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
    @objid ("6a06756f-e6c9-498c-9a45-aa76b9c8e8b0")
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
                    this.theEditedElement.setChannel((Link) value);
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
