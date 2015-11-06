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
import org.modelio.metamodel.uml.behavior.commonBehaviors.ParameterEffectKind;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>Parameter</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Parameter</i> metaclass.
 */
@objid ("8f782cbe-c068-11e1-8c0a-002564c97630")
public class ReturnParameterPropertyModel extends AbstractPropertyModel<Parameter> {
    /**
     * Properties to display for <i>Parameter</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a8290948-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Parameter", "Type", "MultiplicityMin", "MultiplicityMax", "TypeConstraint", "Effect", "IsException", "IsStream","IsOrdered","IsUnique"};

    @objid ("fb030314-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fb030315-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fb030316-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("fb0489ac-c5d4-11e1-8f21-002564c97630")
    private EditableListType cardinalityMaxType;

    @objid ("fb0489ad-c5d4-11e1-8f21-002564c97630")
    private EditableListType cardinalityMinType;

    @objid ("fb0489ae-c5d4-11e1-8f21-002564c97630")
    private SingleElementType generalClassType;

    @objid ("fb0489af-c5d4-11e1-8f21-002564c97630")
    private EnumType parameterEffectKindType;

    /**
     * Create a new <i>Parameter</i> data model from an <i>Parameter</i>.
     * @param model
     */
    @objid ("8f782cc9-c068-11e1-8c0a-002564c97630")
    public ReturnParameterPropertyModel(Parameter theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
        
        List<String> cardinalityMinValues = new ArrayList<>();
        cardinalityMinValues.add("0");
        cardinalityMinValues.add("1");
        this.cardinalityMinType = new EditableListType(true, cardinalityMinValues);
        
        this.generalClassType = new SingleElementType(false, GeneralClass.class, CoreSession.getSession(this.theEditedElement));
        this.parameterEffectKindType = new EnumType(ParameterEffectKind.class);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f782ccf-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f782cd5-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f782cdb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return PROPERTIES[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getType();
                    case 2:
                        return this.theEditedElement.getMultiplicityMin();
                    case 3:
                        return this.theEditedElement.getMultiplicityMax();
                    case 4:
                        return this.theEditedElement.getTypeConstraint();
                    case 5:
                        return this.theEditedElement.getEffect();
                    case 6:
                        return this.theEditedElement.isIsException()?Boolean.TRUE:Boolean.FALSE;
                    case 7:
                        return this.theEditedElement.isIsStream()?Boolean.TRUE:Boolean.FALSE;
                    case 8:
                        return this.theEditedElement.isIsOrdered()?Boolean.TRUE:Boolean.FALSE;
                    case 9:
                        return this.theEditedElement.isIsUnique()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8f782ce2-c068-11e1-8c0a-002564c97630")
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
                        return this.generalClassType;
                    case 2:
                        return this.cardinalityMinType;
                    case 3:
                        return this.cardinalityMaxType;
                    case 4:
                        return this.stringType;
                    case 5:
                        return this.parameterEffectKindType;
                    case 6:
                        return this.booleanType;
                    case 7:
                        return this.booleanType;
                    case 8:
                        return this.booleanType;
                    case 9:
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
    @objid ("8f782ceb-c068-11e1-8c0a-002564c97630")
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
                        this.theEditedElement.setType((GeneralClass) value);
                        break;
                    case 2:
                        this.theEditedElement.setMultiplicityMin((String) value);
                        break;
                    case 3:
                        this.theEditedElement.setMultiplicityMax((String) value);
                        break;
                    case 4:
                        this.theEditedElement.setTypeConstraint((String) value);
                        break;
                    case 5:
                        this.theEditedElement.setEffect((ParameterEffectKind) value);
                        break;
                    case 6:
                        this.theEditedElement.setIsException(((Boolean) value).booleanValue());
                        break;
                    case 7:
                        this.theEditedElement.setIsStream(((Boolean) value).booleanValue());
                        break;
                    case 8:
                        this.theEditedElement.setIsOrdered(((Boolean) value).booleanValue());
                        break;
                    case 9:
                        this.theEditedElement.setIsUnique(((Boolean) value).booleanValue());
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
