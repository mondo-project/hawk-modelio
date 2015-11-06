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
import org.modelio.metamodel.uml.behavior.activityModel.ActivityNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectFlow;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectFlowEffectKind;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>ObjectFlow</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ObjectFlow</i> metaclass.
 */
@objid ("8f521702-c068-11e1-8c0a-002564c97630")
public class ObjectFlowPropertyModel extends AbstractPropertyModel<ObjectFlow> {
    /**
     * Properties to display for <i>ObjectFlow</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a7c5daa8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ObjectFlow", "Name", "Target", "Guard", "Weight", "TransformationBehavior", "Effect", "SelectionBehavior", "IsMultiCast", "IsMultiReceive"};

    @objid ("8f547809-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f54780a-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f54780b-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8f54780c-c068-11e1-8c0a-002564c97630")
    private SingleElementType targetType = null;

    @objid ("8f54780d-c068-11e1-8c0a-002564c97630")
    private EnumType effectKindType = null;

    /**
     * Create a new <i>ObjectFlow</i> data model from an <i>ObjectFlow</i>.
     */
    @objid ("8f54780e-c068-11e1-8c0a-002564c97630")
    public ObjectFlowPropertyModel(ObjectFlow theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.targetType = new SingleElementType(false, ActivityNode.class, CoreSession.getSession(this.theEditedElement));
        this.effectKindType = new EnumType(ObjectFlowEffectKind.class);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f547814-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f547819-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ObjectFlowPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f54781e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ObjectFlowPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getTarget();
            case 3:
                return this.theEditedElement.getGuard();
            case 4:
                return this.theEditedElement.getWeight();
            case 5:
                return this.theEditedElement.getTransformationBehavior();
            case 6:
                return this.theEditedElement.getEffect();
            case 7:
                return this.theEditedElement.getSelectionBehavior();
            case 8:
                return this.theEditedElement.isIsMultiCast()?Boolean.TRUE:Boolean.FALSE;
            case 9:
                return this.theEditedElement.isIsMultiReceive()?Boolean.TRUE:Boolean.FALSE;
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
    @objid ("8f547824-c068-11e1-8c0a-002564c97630")
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
                return this.targetType;
            case 3:
                return this.stringType;
            case 4:
                return this.stringType;
            case 5:
                return this.stringType;
            case 6:
                return this.effectKindType;
            case 7:
                return this.stringType;
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
    @objid ("8f54782a-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setTarget((ActivityNode) value);
                break;
            case 3:
                this.theEditedElement.setGuard((String) value);
                break;
            case 4:
                this.theEditedElement.setWeight((String) value);
                break;
            case 5:
                this.theEditedElement.setTransformationBehavior((String) value);
                break;
            case 6:
                this.theEditedElement.setEffect((ObjectFlowEffectKind) value);
                break;
            case 7:
                this.theEditedElement.setSelectionBehavior((String) value);
                break;
            case 8:
                this.theEditedElement.setIsMultiCast(((Boolean) value).booleanValue());
                break;
            case 9:
                this.theEditedElement.setIsMultiReceive(((Boolean) value).booleanValue());
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
