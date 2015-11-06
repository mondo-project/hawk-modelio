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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.bpmn.activities.BpmnMultiInstanceLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.BpmnSendTask;
import org.modelio.metamodel.bpmn.activities.BpmnStandardLoopCharacteristics;
import org.modelio.metamodel.bpmn.activities.MultiInstanceBehavior;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>BpmnSendTask</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnSendTask</i>
 * metaclass.
 */
@objid ("8e53e0a7-c068-11e1-8c0a-002564c97630")
public class BpmnSendTaskPropertyModel extends AbstractPropertyModel<BpmnSendTask> {
    @objid ("a61af97a-c068-11e1-8c0a-002564c97630")
    private List<String> properties = new ArrayList<>();

    @objid ("aa30a9d2-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    @objid ("16192c07-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>BpmnSendTask</i> data model from an <i>BpmnSendTask</i>.
     */
    @objid ("8e53e0b0-c068-11e1-8c0a-002564c97630")
    public BpmnSendTaskPropertyModel(BpmnSendTask theEditedElement, IMModelServices modelService, IModel model) {
        super(theEditedElement);
        this.modelService = modelService;
        this.model = model;
        initPropertyModel();
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e53e0b6-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e53e0bb-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        // Init properties here to avoid problems with UNDO
        initPropertyModel();
        return this.properties.size();
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e53e0c0-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        LoopType type = LoopType.getType(this.theEditedElement);
        if (col == 0) // col 0 is the property key
            return this.properties.get(row);
        
        // else
        if (col == 1) // col 1 is the property value
        {
            if (row == 0)
            return "Value";
            else if (row == 1)
            return this.theEditedElement.getName();
            else if (row == 2)
            return this.theEditedElement.isIsForCompensation() ? Boolean.TRUE : Boolean.FALSE;
            else if (row == 3)
            return this.theEditedElement.getStartQuantity();
            else if (row == 4)
            return this.theEditedElement.getCompletionQuantity();
            else if (row == 5)
            return this.theEditedElement.isIsGlobal() ? Boolean.TRUE : Boolean.FALSE;
            else if (row == 6)
            return this.theEditedElement.getImplementation();
            else if (row == 7)
            return this.theEditedElement.getCalledOperation();
            else if (row == 8)
            return this.theEditedElement.getMessageRef();
            else if (row == 9)
            return LoopType.getType(this.theEditedElement);
            else if (row > 9) {
            if (type == LoopType.Standard) {
                BpmnStandardLoopCharacteristics caracteristic = (BpmnStandardLoopCharacteristics) this.theEditedElement.getLoopCharacteristics();
                if (row == 10)
                return caracteristic.isTestBefore() ? Boolean.TRUE : Boolean.FALSE;
                else if (row == 11)
                return caracteristic.getLoopCondition();
                else if (row == 12)
                return caracteristic.getLoopMaximum();
            } else if (type == LoopType.MultiInstanceParallel || type == LoopType.MultiInstanceSequential) {
                BpmnMultiInstanceLoopCharacteristics caracteristic = (BpmnMultiInstanceLoopCharacteristics) this.theEditedElement.getLoopCharacteristics();
                if (row == 10)
                return caracteristic.getBehavior();
                else if (row == 11)
                return caracteristic.getLoopCardinality();
                else if (row == 12)
                return caracteristic.getCompletionCondition();
                else if (row == 13)
                return caracteristic.getCompletionEventRef();
            }
            }
        }
        return null;
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
    @objid ("8e53e0c7-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        LoopType type = LoopType.getType(this.theEditedElement);
        if (col == 0) // col 0 is the property key
            return new StringType(false);
        
        // else
        if (col == 1) // col 1 is the property value
        {
            if (row == 0)
            return new StringType(false);
            else if (row == 1)
            return new StringType(true);
            else if (row == 2)
            return new BooleanType();
            else if (row == 3)
            return new StringType(true);
            else if (row == 4)
            return new StringType(true);
            else if (row == 5)
            return new BooleanType();
            else if (row == 6)
            return new StringType(true);
            else if (row == 7)
            return new SingleElementType(true, Operation.class, CoreSession.getSession(this.theEditedElement));
            else if (row == 8)
            return new SingleElementType(true, BpmnMessage.class, CoreSession.getSession(this.theEditedElement));
            else if (row == 9)
            return new EnumType(LoopType.class);
            else if (row > 9) {
            if (type == LoopType.Standard) {
                if (row == 10)
                return new BooleanType();
                else if (row == 11)
                return new StringType(true);
                else if (row == 12)
                return new StringType(true);
            } else if (type == LoopType.MultiInstanceParallel || type == LoopType.MultiInstanceSequential) {
                if (row == 10)
                return new EnumType(MultiInstanceBehavior.class);
                else if (row == 11)
                return new StringType(true);
                else if (row == 12)
                return new StringType(true);
                else if (row == 13)
                return new SingleElementType(true, BpmnEventDefinition.class, CoreSession.getSession(this.theEditedElement));
            }
            }
        }
        return null;
    }

    /**
     * Set value in the model for the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number.
     * @param col the column number.
     * @param value the value specified by the user.
     */
    @objid ("8e55672a-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        LoopType type = LoopType.getType(this.theEditedElement);
        if (col == 0) // col 0 is the property key
            return;
        
        if (col == 1) // col 1 is the property value
        {
            if (row == 0)
            return;
            else if (row == 1)
            this.theEditedElement.setName((String) value);
            else if (row == 2)
            this.theEditedElement.setIsForCompensation((Boolean) value);
            else if (row == 3)
            this.theEditedElement.setStartQuantity(Integer.valueOf((String) value));
            else if (row == 4)
            this.theEditedElement.setCompletionQuantity(Integer.valueOf((String) value));
            else if (row == 5)
            this.theEditedElement.setIsGlobal((Boolean) value);
            else if (row == 6)
            this.theEditedElement.setImplementation((String) value);
            else if (row == 7)
            this.theEditedElement.setCalledOperation((Operation) value);
            else if (row == 8)
            this.theEditedElement.setMessageRef((BpmnMessage) value);
            else if (row == 9) {
            LoopType.setType(this.modelService, (LoopType)value, this.theEditedElement);
            initPropertyModel();
            } else if (row > 9) {
            if (type == LoopType.Standard) {
                BpmnStandardLoopCharacteristics caracteristic = (BpmnStandardLoopCharacteristics) this.theEditedElement.getLoopCharacteristics();
                if (row == 10)
                caracteristic.setTestBefore((Boolean) value);
                else if (row == 11)
                caracteristic.setLoopCondition((String) value);
                else if (row == 12)
                caracteristic.setLoopMaximum((String) value);
            } else if (type == LoopType.MultiInstanceParallel || type == LoopType.MultiInstanceSequential) {
                BpmnMultiInstanceLoopCharacteristics caracteristic = (BpmnMultiInstanceLoopCharacteristics) this.theEditedElement.getLoopCharacteristics();
                if (row == 10)
                caracteristic.setBehavior((MultiInstanceBehavior) value);
                else if (row == 11)
                caracteristic.setLoopCardinality((String) value);
                else if (row == 12)
                caracteristic.setCompletionCondition((String) value);
                else if (row == 13)
                caracteristic.setCompletionEventRef((BpmnEventDefinition) value);
            }
            }
        }
    }

    @objid ("8e556730-c068-11e1-8c0a-002564c97630")
    private void initPropertyModel() {
        this.properties.clear();
        this.properties.add("SendTask");
        this.properties.add("Name");
        this.properties.add("ForCompensation");
        this.properties.add("StartQuantity");
        this.properties.add("CompletionQuanitity");
        this.properties.add("Global");
        this.properties.add("Implementation");
        this.properties.add("Operation");
        this.properties.add("Message");
        this.properties.add("LoopCharacteristics");
        
        LoopType type = LoopType.getType(this.theEditedElement);
        if (type == LoopType.Standard) {
            this.properties.add("TestBefore");
            this.properties.add("LoopCondition");
            this.properties.add("LoopMaximum");
        } else if (type == LoopType.MultiInstanceParallel || type == LoopType.MultiInstanceSequential) {
            this.properties.add("Behavior");
            this.properties.add("LoopCardinality");
            this.properties.add("CompletionCondition");
            this.properties.add("EventDefinition");
        }
    }

}