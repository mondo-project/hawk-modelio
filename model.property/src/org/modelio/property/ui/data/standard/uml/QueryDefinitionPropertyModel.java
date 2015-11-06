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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.matrix.QueryDefinition;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;

/**
 * <i>QueryDefinition</i> data model.
 * <p>
 * This class provides the list of properties for the <i>QueryDefinition</i> metaclass.
 */
@objid ("9bdb6ed9-d249-4c63-90b2-5ffa5f35dce9")
public class QueryDefinitionPropertyModel extends AbstractPropertyModel<QueryDefinition> {
    /**
     * Properties to display for <i>QueryDefinition</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for other rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("0d027a58-4773-455d-8791-b86dcbf3b07b")
    private static final String[] PROPERTIES = new String[] {"QueryDefinition", "Added"};

    @objid ("074d5805-68b8-4f55-81b2-ec99ff78117a")
    private StringType labelStringType;

    @objid ("2bd880b4-9ad0-4fe8-bfdb-bdc68d8d1bb5")
    private MultipleElementType elementType;

    /**
     * Create a new <i>QueryDefinition</i> data model from an <i>QueryDefinition</i>.
     */
    @objid ("fbbffbb1-92a6-4fbe-9361-77d10f949082")
    public QueryDefinitionPropertyModel(QueryDefinition theQueryDefinition, IModel model) {
        super(theQueryDefinition);
        
        this.labelStringType = new StringType(false);
        this.elementType = new MultipleElementType(true, this.theEditedElement, "Added", ModelElement.class, model);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("9ffad040-4f2a-40cd-9e20-f4fa14c760e5")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("dd77d5ee-a51f-4590-bec1-faa05d1f5226")
    @Override
    public int getRowsNumber() {
        return QueryDefinitionPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("d2d9ae99-cfd0-42af-bfcb-b45fc786f11e")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return QueryDefinitionPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getAdded();
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
    @objid ("deb40327-9fb4-4be9-ad35-df921164182c")
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
                    return this.elementType;
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
    @objid ("a00cbf66-a027-456b-9be2-bea83b5738ba")
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
                    this.theEditedElement.getAdded().clear();
                    this.theEditedElement.getAdded().addAll((List<Element>)value);
                    
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
