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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.commonBehaviors.ParameterEffectKind;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.metamodel.uml.statik.PassingMode;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>BehaviorParameter</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BehaviorParameter</i> metaclass.
 */
@objid ("8ed593ca-c068-11e1-8c0a-002564c97630")
public class BehaviorParameterPropertyModel extends AbstractPropertyModel<BehaviorParameter> {
    /**
     * Properties to display for <i>BehaviorParameter</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a683b5e8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"BehaviorParameter", "Name", "Type", "MultiplicityMin", "MultiplicityMax", "ParameterPassing", "DefaultValue", "TypeConstraint", "Effect", "IsException", "IsStream", "Mapped","IsOrdered","IsUnique"};

    @objid ("f9f627a3-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("f9f627a4-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("f9f627a5-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("f9f627a6-c5d4-11e1-8f21-002564c97630")
    private SingleElementType generalClassType;

    @objid ("f9f627a7-c5d4-11e1-8f21-002564c97630")
    private EnumType parameterEffectKindType;

    @objid ("f9f627a8-c5d4-11e1-8f21-002564c97630")
    private EnumType passingModeType;

    @objid ("f9f627a9-c5d4-11e1-8f21-002564c97630")
    private SingleElementType parameterType;

    /**
     * Create a new <i>BehaviorParameter</i> data model from an <i>BehaviorParameter</i>.
     */
    @objid ("8ed593d5-c068-11e1-8c0a-002564c97630")
    public BehaviorParameterPropertyModel(BehaviorParameter theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        this.generalClassType = new SingleElementType(false, GeneralClass.class, CoreSession.getSession(this.theEditedElement));
        this.parameterType = new SingleElementType(false, Parameter.class, CoreSession.getSession(this.theEditedElement));
        this.parameterEffectKindType = new EnumType(ParameterEffectKind.class);
        this.passingModeType = new EnumType(PassingMode.class);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ed593db-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ed593e0-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BehaviorParameterPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ed593e5-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return BehaviorParameterPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return this.theEditedElement.getName();
                case 2:
                    return this.theEditedElement.getType();
                case 3:
                    return this.theEditedElement.getMultiplicityMin();
                case 4:
                    return this.theEditedElement.getMultiplicityMax();
                case 5:
                    return this.theEditedElement.getParameterPassing();
                case 6:
                    return this.theEditedElement.getDefaultValue();
                case 7:
                    return this.theEditedElement.getTypeConstraint();
                case 8:
                    return this.theEditedElement.getEffect();
                case 9:
                    return this.theEditedElement.isIsException()?Boolean.TRUE:Boolean.FALSE;
                case 10:
                    return this.theEditedElement.isIsStream()?Boolean.TRUE:Boolean.FALSE;
                case 11:
                    return this.theEditedElement.getMapped();
                case 12:
                    return this.theEditedElement.isIsOrdered()?Boolean.TRUE:Boolean.FALSE;
                case 13:
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
    @objid ("8ed593eb-c068-11e1-8c0a-002564c97630")
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
                    return this.stringType;
                case 4:
                    return this.stringType;
                case 5:
                    return this.passingModeType;
                case 6:
                    return this.stringType;
                case 7:
                    return this.stringType;
                case 8:
                    return this.parameterEffectKindType;
                case 9:
                    return this.booleanType;
                case 10:
                    return this.booleanType;
                case 11:
                    return this.parameterType;
                case 12:
                    return this.booleanType;
                case 13:
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
    @objid ("8ed71a4b-c068-11e1-8c0a-002564c97630")
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
                    this.theEditedElement.setMultiplicityMin((String) value);
                    break;
                case 4:
                    this.theEditedElement.setMultiplicityMax((String) value);
                    break;
                case 5:
                    this.theEditedElement.setParameterPassing((PassingMode) value);
                    break;
                case 6:
                    this.theEditedElement.setDefaultValue((String) value);
                    break;
                case 7:
                    this.theEditedElement.setTypeConstraint((String) value);
                    break;
                case 8:
                    this.theEditedElement.setEffect((ParameterEffectKind) value);
                    break;
                case 9:
                    this.theEditedElement.setIsException(((Boolean) value).booleanValue());
                    break;
                case 10:
                    this.theEditedElement.setIsStream(((Boolean) value).booleanValue());
                    break;
                case 11:
                    this.theEditedElement.setMapped((Parameter) value);
                    break;
                case 12:
                    this.theEditedElement.setIsOrdered(((Boolean) value).booleanValue());
                    break;
                case 13:
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
