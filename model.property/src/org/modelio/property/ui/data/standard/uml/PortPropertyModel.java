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
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.metamodel.uml.statik.PortOrientation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>Port</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Port</i> metaclass.
 */
@objid ("8f605ee5-c068-11e1-8c0a-002564c97630")
public class PortPropertyModel extends AbstractPropertyModel<Port> {
    /**
     * Properties to display for <i>Port</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a7de44a9-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "Port", "Name", "Base", "Value",
            "MultiplicityMin", "MultiplicityMax", "IsBehavior", "IsService", "IsConstant",
            "RepresentedFeature", "IsConjugated", "Direction" };

    @objid ("8f605ef0-c068-11e1-8c0a-002564c97630")
    private StringType stringType = new StringType(false);

    @objid ("8f605ef1-c068-11e1-8c0a-002564c97630")
    private EnumType directionType = null;

    @objid ("8f605ef2-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMinType = null;

    @objid ("8f605ef3-c068-11e1-8c0a-002564c97630")
    private EditableListType cardinalityMaxType = null;

    @objid ("8f605ef4-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("fadb56ee-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fadb56ef-c5d4-11e1-8f21-002564c97630")
    private SingleElementType modelElementType;

    @objid ("fadb56f0-c5d4-11e1-8f21-002564c97630")
    private SingleElementType namespaceType;

    /**
     * Create a new <i>Port</i> data model from an <i>Port</i>.
     * @param model
     * @param theEditedElement the port to build a model for
     */
    @objid ("8f605ef5-c068-11e1-8c0a-002564c97630")
    public PortPropertyModel(Port theEditedElement) {
        super(theEditedElement);
        this.stringType = new StringType(false);
        this.directionType = new EnumType(PortOrientation.class);
        List<String> cardinalityValues = new ArrayList<>();
        cardinalityValues.add("0");
        cardinalityValues.add("1");
        cardinalityValues.add("*");
        this.cardinalityMinType = new EditableListType(true, cardinalityValues);
        cardinalityValues = new ArrayList<>();
        cardinalityValues.add("0");
        cardinalityValues.add("1");
        cardinalityValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityValues);
        this.booleanType = new BooleanType();
        
        this.labelStringType = new StringType(false);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.modelElementType = new SingleElementType(false, ModelElement.class, session);
        this.namespaceType = new SingleElementType(false, NameSpace.class, session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f605efb-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f605f01-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return PortPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f605f07-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return PortPropertyModel.PROPERTIES[row];
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
                        return this.theEditedElement.isIsBehavior() ? Boolean.TRUE : Boolean.FALSE;
                    case 7:
                        return this.theEditedElement.isIsService() ? Boolean.TRUE : Boolean.FALSE;
                    case 8:
                        return this.theEditedElement.isIsConstant() ? Boolean.TRUE : Boolean.FALSE;
                    case 9:
                        return this.theEditedElement.getRepresentedFeature();
                    case 10:
                        return this.theEditedElement.isIsConjugated() ? Boolean.TRUE : Boolean.FALSE;
                    case 11:
                        return this.theEditedElement.getDirection();
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
     * This type will be used to choose an editor and a renderer for each cell of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8f605f0e-c068-11e1-8c0a-002564c97630")
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
                        return this.cardinalityMinType;
                    case 5:
                        return this.cardinalityMaxType;
                    case 6:
                        return this.booleanType;
                    case 7:
                        return this.booleanType;
                    case 8:
                        return this.booleanType;
                    case 9:
                        return this.modelElementType;
                    case 10:
                        return this.booleanType;
                    case 11:
                        return this.directionType;
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
    @objid ("8f605f15-c068-11e1-8c0a-002564c97630")
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
                        this.theEditedElement.setIsBehavior(((Boolean) value).booleanValue());
                        break;
                    case 7:
                        this.theEditedElement.setIsService(((Boolean) value).booleanValue());
                        break;
                    case 8:
                        this.theEditedElement.setIsConstant(((Boolean) value).booleanValue());
                        break;
                    case 9:
                        this.theEditedElement.setRepresentedFeature((ModelElement) value);
                        break;
                    case 10:
                        this.theEditedElement.setIsConjugated(((Boolean) value).booleanValue());
                        break;
                    case 11:
                        this.theEditedElement.setDirection((PortOrientation) value);
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
