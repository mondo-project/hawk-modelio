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
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>Signal</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Signal</i> metaclass.
 */
@objid ("8f7cef8b-c068-11e1-8c0a-002564c97630")
public class SignalPropertyModel extends AbstractPropertyModel<Signal> {
    /**
     * Properties to display for <i>Signal</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a8323108-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "Signal", "Name", "Base", "IsEvent", "IsException" };

    @objid ("fb0c2acf-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fb0c2ad0-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fb0c2ad1-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("fb0c2ad2-c5d4-11e1-8f21-002564c97630")
    private SignalBaseType signalBaseType;

    /**
     * Create a new <i>Signal</i> data model from an <i>Signal</i>.
     */
    @objid ("8f7cef96-c068-11e1-8c0a-002564c97630")
    public SignalPropertyModel(Signal theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.signalBaseType = new SignalBaseType(CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f7cef9c-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f7cefa1-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return SignalPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f7cefa6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return SignalPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return SignalBaseType.getValue(this.theEditedElement);
            case 3:
                return this.theEditedElement.isIsEvent()?Boolean.TRUE:Boolean.FALSE;
            case 4:
                return this.theEditedElement.isIsException()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8f7cefac-c068-11e1-8c0a-002564c97630")
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
                return this.signalBaseType;
            case 3:
                return this.booleanType;
            case 4:
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
    @objid ("8f7cefb4-c068-11e1-8c0a-002564c97630")
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
                SignalBaseType.setValue(this.theEditedElement, value);
                break;
            case 3:
                this.theEditedElement.setIsEvent(((Boolean) value).booleanValue());
                break;
            case 4:
                this.theEditedElement.setIsException(((Boolean) value).booleanValue());
                break;
            default:
                return;
            }
              break;
        default:
            return;
        }
    }

    @objid ("8f7cefba-c068-11e1-8c0a-002564c97630")
    public static class SignalBaseType extends HybridType {
        @objid ("8f7cefbc-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8f7cefc1-c068-11e1-8c0a-002564c97630")
        public SignalBaseType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(Parameter.class);
            this.t.add(GeneralClass.class);
            this.t.add(Operation.class);
        }

        @objid ("8f7cefc3-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        /**
         * Returns the element represented by the given instance node.
         * @return the represented element
         */
        @objid ("8f7f50c6-c068-11e1-8c0a-002564c97630")
        public static ModelElement getValue(Signal theEditedElement) {
            ModelElement ret = theEditedElement.getPBase();
            if (ret != null)
                return ret;
                        
            ret = theEditedElement.getOBase();
            if (ret != null)
                return ret;
                        
            ret = theEditedElement.getBase();
            return ret;
        }

        /**
         * Set the ObjectNode represented elements.
         * This method set the right dependency and clears the otheEditedElement.
         * @param theEditedElement the instance node
         * @param value the new represented element
         */
        @objid ("8f7f50d0-c068-11e1-8c0a-002564c97630")
        public static void setValue(Signal theEditedElement, Object value) {
            // Erase old value or exit if old value is new value
            Parameter old1 = theEditedElement.getPBase();
            if (old1 != null) {
                if (old1.equals(value))
                    return;
                theEditedElement.setPBase(null);
            } else {
                Operation old2 = theEditedElement.getOBase();
                if (old2 != null) {
                    if (old2.equals(value))
                        return;
                    theEditedElement.setOBase(null);
                } else {
                    GeneralClass old3 = theEditedElement.getBase();
                    if (old3 != null) {
                        if (old3.equals(value))
                            return;
                        theEditedElement.setBase(null);
                    }
                }
            }
                        
            if (value != null) {
                // Set new value
                if (Parameter.class.isAssignableFrom(value.getClass()))
                    theEditedElement.setPBase((Parameter) value);
                else if (Operation.class.isAssignableFrom(value.getClass()))
                    theEditedElement.setOBase((Operation) value);
                else if (GeneralClass.class.isAssignableFrom(value.getClass()))
                    theEditedElement.setBase((GeneralClass) value);
            }
        }

        @objid ("b1b06f1c-153b-4e98-9d82-99824721b167")
        @Override
        public boolean acceptStringValue() {
            return false;
        }

    }

}
