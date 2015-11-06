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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>NamespaceUse</i> data model.
 * <p>
 * This class provides the list of properties for the <i>NamespaceUse</i> metaclass.
 */
@objid ("8f4af2e5-c068-11e1-8c0a-002564c97630")
public class NamespaceUsePropertyModel extends AbstractPropertyModel<NamespaceUse> {
    /**
     * Properties to display for <i>NamespaceUse</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a78bee88-c068-11e1-8c0a-002564c97630")
    private List<String> properties = new ArrayList<>();

    @objid ("8f4d53e7-c068-11e1-8c0a-002564c97630")
    private static final StringType labelStringType = new StringType(false);

    /**
     * Create a new <i>NamespaceUse</i> data model from an <i>NamespaceUse</i>.
     */
    @objid ("8f4d53ed-c068-11e1-8c0a-002564c97630")
    public NamespaceUsePropertyModel(NamespaceUse theEditedElement) {
        super(theEditedElement);
        this.properties.add("NamespaceUse");
        for (Element cause : this.theEditedElement.getCause()) {
            this.properties.add(cause.getClass().getName());
        }
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f4d53f3-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f4d53f9-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return this.properties.size();
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f4d53ff-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return this.properties.get(row);
        }
        
        // else
        if (col == 1) // col 1 is the property value
        {
            if (row == 0) {
                return "Value";
            }
            // else
            Element cause = this.theEditedElement.getCause().get(row - 1);
            if ( cause instanceof ModelElement )
            {
                ModelElement modelElement = (ModelElement)cause;
                return modelElement.getName();
            }
            //else: Not a ModelElement, so no name to show, display an empty string.
            return "";
        }
        // else
        return null;
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
    @objid ("8f4d5407-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        return NamespaceUsePropertyModel.labelStringType;
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("8f4d540f-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        return;
    }

    @objid ("8f4d5417-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        return false;
    }

}
