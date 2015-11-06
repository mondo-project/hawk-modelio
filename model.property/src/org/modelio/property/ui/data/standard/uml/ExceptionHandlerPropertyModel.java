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
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.activityModel.ExceptionHandler;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>ExceptionHandler</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ExceptionHandler</i> metaclass.
 */
@objid ("8f111631-c068-11e1-8c0a-002564c97630")
public class ExceptionHandlerPropertyModel extends AbstractPropertyModel<ExceptionHandler> {
    /**
     * Properties to display for <i>ExceptionHandler</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a701decb-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ExceptionHandler", "Name", "ExceptionType", "ExceptionInput"};

    @objid ("8f11163c-c068-11e1-8c0a-002564c97630")
    private MultipleElementType exceptionTypes;

    @objid ("fa5cb161-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa5cb162-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa5cb163-c5d4-11e1-8f21-002564c97630")
    private SingleElementType inputPinType;

    /**
     * Create a new <i>ExceptionHandler</i> data model from an <i>ExceptionHandler</i>.
     * @param theEditedElement
     */
    @objid ("8f11163d-c068-11e1-8c0a-002564c97630")
    public ExceptionHandlerPropertyModel(ExceptionHandler theEditedElement, IModel model) {
        super(theEditedElement);
        this.exceptionTypes = new MultipleElementType(true, theEditedElement, "ExceptionType", GeneralClass.class, model);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.inputPinType = new SingleElementType(false, InputPin.class, CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f111643-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f111648-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ExceptionHandlerPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f129ca5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ExceptionHandlerPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1: // Name
                    return this.theEditedElement.getName();
                case 2: // Type
                    return this.theEditedElement.getExceptionType();
                case 3: // Target
                    return this.theEditedElement.getExceptionInput();
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
    @objid ("8f129cab-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
                case 0: // Header
                    return this.labelStringType;
                case 1: // Name
                    return this.stringType;
                case 2: // Exception types
                    return this.exceptionTypes;
                case 3:
                    return this.inputPinType;
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
    @objid ("8f129cb1-c068-11e1-8c0a-002564c97630")
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
                    this.theEditedElement.setName((String) value);
                    break;
                case 2:
                    for (GeneralClass s : this.theEditedElement.getExceptionType())
                        this.theEditedElement.getExceptionType().remove(s);
                    
                    List<GeneralClass> newcontent = (List<GeneralClass>)value;
                    for (GeneralClass s : newcontent)
                        this.theEditedElement.getExceptionType().add(s);
                    break;
                case 3:
                    this.theEditedElement.setExceptionInput((InputPin) value);
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
