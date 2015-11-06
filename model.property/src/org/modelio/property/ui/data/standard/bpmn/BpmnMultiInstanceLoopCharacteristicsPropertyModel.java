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
                                    

package org.modelio.property.ui.data.standard.bpmn;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.bpmn.activities.BpmnMultiInstanceLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.MultiInstanceBehavior;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>BpmnMultiInstanceLoopCharacteristics</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnMultiInstanceLoopCharacteristics</i> metaclass.
 */
@objid ("8e449e76-c068-11e1-8c0a-002564c97630")
public class BpmnMultiInstanceLoopCharacteristicsPropertyModel extends AbstractPropertyModel<BpmnMultiInstanceLoopCharacteristics> {
    /**
     * Properties to display for <i>BpmnMultiInstanceLoopCharacteristics</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a600ca48-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "MultiInstanceLoopCharacteristics", "Name", "Sequencial", "Behavior",
            "LoopCardinality", "CompletionCondition" ,"EventDefinition"};

    @objid ("166a5948-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>BpmnMultiInstanceLoopCharacteristics</i> data model from an
     * <i>BpmnMultiInstanceLoopCharacteristics</i>.
     */
    @objid ("8e449e81-c068-11e1-8c0a-002564c97630")
    public BpmnMultiInstanceLoopCharacteristicsPropertyModel(BpmnMultiInstanceLoopCharacteristics theEditedElement, IModel model) {
        super(theEditedElement);
        this.model = model;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e449e87-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e4624e8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnMultiInstanceLoopCharacteristicsPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e4624ed-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnMultiInstanceLoopCharacteristicsPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        return this.theEditedElement.isIsSequencial() ? Boolean.TRUE : Boolean.FALSE;
                    case 3:
                        return this.theEditedElement.getBehavior();
                    case 4:
                        return this.theEditedElement.getLoopCardinality();
                    case 5:
                        return this.theEditedElement.getCompletionCondition();
                    case 6:
                        return this.theEditedElement.getCompletionEventRef();
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
    @objid ("8e4624f3-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return new StringType(false);
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return new StringType(false);
                    case 1:
                        return new StringType(true);
                    case 2:
                        return new BooleanType();
                    case 3:
                        return new EnumType(MultiInstanceBehavior.class);
                    case 4:
                        return new StringType(true);
                    case 5:
                        return new StringType(true);
                    case 6:
                        return new SingleElementType(true, BpmnEventDefinition.class, CoreSession.getSession(this.theEditedElement));
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
    @objid ("8e4624f9-c068-11e1-8c0a-002564c97630")
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
                        this.theEditedElement.setIsSequencial((Boolean) value);
                        break;
                    case 3:
                        this.theEditedElement.setBehavior((MultiInstanceBehavior) value);
                        break;
                    case 4:
                        this.theEditedElement.setLoopCardinality((String) value);
                        break;
                    case 5:
                        this.theEditedElement.setCompletionCondition((String) value);
                        break;
                    case 6:
                        this.theEditedElement.setCompletionEventRef((BpmnEventDefinition) value);
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
