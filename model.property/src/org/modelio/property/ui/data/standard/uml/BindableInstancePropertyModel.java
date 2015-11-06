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
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>BindableInstance</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BindableInstance</i> metaclass.
 */
@objid ("8ed71a57-c068-11e1-8c0a-002564c97630")
public class BindableInstancePropertyModel extends AbstractPropertyModel<BindableInstance> {
    /**
     * Properties to display for <i>BindableInstance</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6861748-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"BindableInstance", "Name", "Base", "MultiplicityMin", "MultiplicityMax", "Value", "IsConstant", "RepresentedFeature"};

    @objid ("f9f7ae45-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("f9f7ae46-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("f9f7ae47-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("f9f934d6-c5d4-11e1-8f21-002564c97630")
    private SingleElementType modelElementType;

    @objid ("f9f934d7-c5d4-11e1-8f21-002564c97630")
    private SingleElementType namespaceType;

    /**
     * Create a new <i>BindableInstance</i> data model from an <i>BindableInstance</i>.
     */
    @objid ("8ed71a62-c068-11e1-8c0a-002564c97630")
    public BindableInstancePropertyModel(BindableInstance theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.modelElementType = new SingleElementType(false, ModelElement.class, CoreSession.getSession(this.theEditedElement));
        this.namespaceType = new SingleElementType(false, NameSpace.class, CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ed71a68-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ed71a6d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BindableInstancePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ed71a72-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return BindableInstancePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getBase();
                case 3:
                    return this.theEditedElement.getMultiplicityMin();
                case 4:
                    return this.theEditedElement.getMultiplicityMax();
                case 5:
                    return this.theEditedElement.getValue();
                case 6:
                    return this.theEditedElement.isIsConstant()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8ed71a78-c068-11e1-8c0a-002564c97630")
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
                    return this.stringType;
                case 4:
                    return this.stringType;
                case 5:
                    return this.stringType;
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
    @objid ("8ed71a80-c068-11e1-8c0a-002564c97630")
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
                    this.theEditedElement.setBase((NameSpace) value);
                    break;
                case 3:
                    this.theEditedElement.setMultiplicityMin((String) value);
                    break;
                case 4:
                    this.theEditedElement.setMultiplicityMax((String) value);
                    break;
                case 5:
                    this.theEditedElement.setValue((String) value);
                    break;
                case 6:
                    this.theEditedElement.setIsConstant(((Boolean) value).booleanValue());
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
