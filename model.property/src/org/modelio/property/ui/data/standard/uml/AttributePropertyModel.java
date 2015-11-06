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
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.KindOfAccess;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>Attribute</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Attribute</i> metaclass.
 */
@objid ("8ed40d2f-c068-11e1-8c0a-002564c97630")
public class AttributePropertyModel extends AbstractPropertyModel<Attribute> {
    /**
     * Properties to display for <i>Attribute</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a67ef328-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Attribute", "Name", "Type", "Visibility", "MultiplicityMin", "MultiplicityMax", "Value", "Changeable", "TypeConstraint", "IsAbstract", "IsClass", "IsDerived", "IsOrdered", "IsUnique", "TargetIsClass"};

    @objid ("8ed40d3a-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ed40d3b-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8ed40d3c-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8ed40d3d-c068-11e1-8c0a-002564c97630")
    private SingleElementType generalClassType = null;

    @objid ("8ed40d3e-c068-11e1-8c0a-002564c97630")
    private EnumType visibilityType = null;

    @objid ("8ed40d3f-c068-11e1-8c0a-002564c97630")
    private EnumType kindOfAccessType = null;

    @objid ("8ed40d40-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8ed40d41-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    /**
     * Create a new <i>Attribute</i> data model from an <i>Attribute</i>.
     */
    @objid ("8ed40d42-c068-11e1-8c0a-002564c97630")
    public AttributePropertyModel(Attribute theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        this.generalClassType = new SingleElementType(true, GeneralClass.class, CoreSession.getSession(this.theEditedElement));
        
        this.visibilityType = new EnumType(VisibilityMode.class);
        this.kindOfAccessType = new EnumType(KindOfAccess.class);
        
        List<String> cardinalityMinValues = new ArrayList<>();
        cardinalityMinValues.add("0");
        cardinalityMinValues.add("1");
        this.cardinalityMinType = new EditableListType(true, cardinalityMinValues);
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ed40d48-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return AttributePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getType();
            case 3:
                return this.theEditedElement.getVisibility();
            case 4:
                return this.theEditedElement.getMultiplicityMin();
            case 5:
                return this.theEditedElement.getMultiplicityMax();
            case 6:
                return this.theEditedElement.getValue();
            case 7:
                return this.theEditedElement.getChangeable();
            case 8:
                return this.theEditedElement.getTypeConstraint();
            case 9:
                return this.theEditedElement.isIsAbstract()?Boolean.TRUE:Boolean.FALSE;
            case 10:
                return this.theEditedElement.isIsClass()?Boolean.TRUE:Boolean.FALSE;
            case 11:
                return this.theEditedElement.isIsDerived()?Boolean.TRUE:Boolean.FALSE;
            case 12:
                return this.theEditedElement.isIsOrdered()?Boolean.TRUE:Boolean.FALSE;
            case 13:
                return this.theEditedElement.isIsUnique()?Boolean.TRUE:Boolean.FALSE;
            case 14:
                return this.theEditedElement.isTargetIsClass()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8ed40d4e-c068-11e1-8c0a-002564c97630")
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
                return this.generalClassType;
            case 3:
                return this.visibilityType;
            case 4:
                return this.cardinalityMinType;
            case 5:
                return this.cardinalityMaxType;
            case 6:
                return this.stringType;
            case 7:
                return this.kindOfAccessType;
            case 8:
                return this.stringType;
            case 9:
                return this.booleanType;
            case 10:
                return this.booleanType;
            case 11:
                return this.booleanType;
            case 12:
                return this.booleanType;
            case 13:
                return this.booleanType;
            case 14:
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
    @objid ("8ed593a8-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setType((GeneralClass) value);
                break;
            case 3:
                this.theEditedElement.setVisibility((VisibilityMode) value);
                break;
            case 4:
                this.theEditedElement.setMultiplicityMin((String) value);
                break;
            case 5:
                this.theEditedElement.setMultiplicityMax((String) value);
                break;
            case 6:
                this.theEditedElement.setValue((String) value);
                break;
            case 7:
                this.theEditedElement.setChangeable((KindOfAccess) value);
                break;
            case 8:
                this.theEditedElement.setTypeConstraint((String) value);
                break;
            case 9:
                this.theEditedElement.setIsAbstract(((Boolean) value).booleanValue());
                break;
            case 10:
                this.theEditedElement.setIsClass(((Boolean) value).booleanValue());
                break;
            case 11:
                this.theEditedElement.setIsDerived(((Boolean) value).booleanValue());
                break;
            case 12:
                this.theEditedElement.setIsOrdered(((Boolean) value).booleanValue());
                break;
            case 13:
                this.theEditedElement.setIsUnique(((Boolean) value).booleanValue());
                break;
            case 14:
                this.theEditedElement.setTargetIsClass(((Boolean) value).booleanValue());
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ed593ae-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ed593b3-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return AttributePropertyModel.PROPERTIES.length;
    }

}
