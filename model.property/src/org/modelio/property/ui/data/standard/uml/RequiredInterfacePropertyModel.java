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
import org.eclipse.emf.common.util.EList;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.MultipleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.statik.Interface;
import org.modelio.metamodel.uml.statik.RequiredInterface;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;

/**
 * <i>RequiredInterface</i> data model.
 * <p>
 * This class provides the list of properties for the <i>RequiredInterface</i> metaclass.
 */
@objid ("8f710885-c068-11e1-8c0a-002564c97630")
public class RequiredInterfacePropertyModel extends AbstractPropertyModel<RequiredInterface> {
    /**
     * Properties to display for <i>RequiredInterface</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a81b4da8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"RequiredInterface", "RequiredElement"};

    @objid ("8f710890-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f710891-c068-11e1-8c0a-002564c97630")
    private MultipleElementType requiredInterfaces = null;

    /**
     * Create a new <i>RequiredInterface</i> data model from an <i>RequiredInterface</i>.
     * @param theEditedElement the edited element.
     * @param model access to the model
     */
    @objid ("8f710892-c068-11e1-8c0a-002564c97630")
    public RequiredInterfacePropertyModel(RequiredInterface theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.requiredInterfaces = new MultipleElementType(true, theEditedElement, "RequiredElement", Interface.class, model);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f710898-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f71089d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return RequiredInterfacePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f7108a2-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return RequiredInterfacePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getRequiredElement();
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
    @objid ("8f7108a8-c068-11e1-8c0a-002564c97630")
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
                    return this.requiredInterfaces;
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
    @objid ("8f7108ae-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
                case 0:
                    return; // Header cannot be modified
                case 1:
                    EList<Interface> currentContent = this.theEditedElement.getRequiredElement();
                    List<Interface> newcontent = (List<Interface>)value;
                    if (! newcontent.equals(currentContent)) {
                        currentContent.clear();
                        currentContent.addAll(newcontent);
                    }
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
