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
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>Instance</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Instance</i> metaclass.
 */
@objid ("8f2c8d88-c068-11e1-8c0a-002564c97630")
public class InstancePropertyModel extends AbstractPropertyModel<Instance> {
    /**
     * Properties to display for <i>Instance</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a7506c31-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Instance", "Name", "Base", "Value", "MultiplicityMin", "MultiplicityMax", "IsConstant"};

    @objid ("8f2e13e9-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f2e13ea-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f2e13eb-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8f2e13ec-c068-11e1-8c0a-002564c97630")
    private SingleElementType baseType = null;

    @objid ("8f2e13ed-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8f2e13ee-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    /**
     * Create a new <i>Instance</i> data model from an <i>Instance</i>.
     * @param theEditedElement
     * @param model
     */
    @objid ("8f2e13ef-c068-11e1-8c0a-002564c97630")
    public InstancePropertyModel(Instance theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        this.baseType = new SingleElementType(true, NameSpace.class, CoreSession.getSession(this.theEditedElement));
        this.baseType.setElementFilter(new BaseTypeFilter());
        
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
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f2e13f5-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f2e13fa-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return InstancePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f2e13ff-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return InstancePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getBase();
            case 3:
                return this.theEditedElement.getValue();
            case 4:
                return this.theEditedElement.getMultiplicityMin();
            case 5:
                return this.theEditedElement.getMultiplicityMax();
            case 6:
                return this.theEditedElement.isIsConstant()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8f2e1405-c068-11e1-8c0a-002564c97630")
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
                return this.baseType;
            case 3:
                return this.stringType;
            case 4:
                return this.cardinalityMinType;
            case 5:
                return this.cardinalityMaxType;
            case 6:
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
    @objid ("8f2e140b-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setValue((String) value);
                break;
            case 4:
                this.theEditedElement.setMultiplicityMin((String) value);
                break;
            case 5:
                this.theEditedElement.setMultiplicityMax((String) value);
                break;
            case 6:
                this.theEditedElement.setIsConstant(((Boolean) value).booleanValue());
                break;
            default:
                return;
            }
            return;
        default:
            return;
        }
    }

    @objid ("8f2e1411-c068-11e1-8c0a-002564c97630")
    protected static class BaseTypeFilter implements IMObjectFilter {
        @objid ("8f2e1412-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean accept(final MObject element) {
            NameSpace type = (NameSpace) element;
                
            // TODO CHM check predefined types
            if (type.getName().equals("undefined")) {
                return false;
            } else if (type instanceof Package && (type.getName().equals("_predefinedTypes") 
                    || type.getName().equals("_S_PredefinedTypes"))) {
                return false;
            } else if (type instanceof Profile) {
                return false;
            } else if (type instanceof ModuleComponent) {
                return false;
            } else {
                return true;
            }
        }

    }

}
