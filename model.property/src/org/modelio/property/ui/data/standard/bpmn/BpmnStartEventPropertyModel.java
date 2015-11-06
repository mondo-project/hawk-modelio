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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.bpmn.activities.BpmnSubProcess;
import org.modelio.metamodel.bpmn.events.BpmnConditionalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnErrorEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEscalationEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnMessageEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnSignalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;
import org.modelio.metamodel.bpmn.events.BpmnTimerEventDefinition;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.smkernel.SmObjectImpl;

/**
 * <i>BpmnStartEvent</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnStartEvent</i> metaclass.
 */
@objid ("8e5b81cf-c068-11e1-8c0a-002564c97630")
public class BpmnStartEventPropertyModel extends AbstractPropertyModel<BpmnStartEvent> {
    /**
     * Properties to display for <i>BpmnStartEvent</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a62941b1-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "StartEvent", "Name","Type", "ParallelMultiple", "Interrupting" };

    @objid ("aa2781f4-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    /**
     * Create a new <i>BpmnStartEvent</i> data model from an <i>BpmnStartEvent</i>.
     */
    @objid ("8e5b81d9-c068-11e1-8c0a-002564c97630")
    public BpmnStartEventPropertyModel(BpmnStartEvent theEditedElement, IMModelServices modelService) {
        super(theEditedElement);
        this.modelService = modelService;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e5b81df-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e5b81e4-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnStartEventPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e5d0847-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnStartEventPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();                     
                    case 2:                     
                      return getType();
                    case 3:
                        return this.theEditedElement.isParallelMultiple() ? Boolean.TRUE : Boolean.FALSE;
                    case 4:
                        return this.theEditedElement.isIsInterrupting() ? Boolean.TRUE : Boolean.FALSE;
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
    @objid ("8e5d084d-c068-11e1-8c0a-002564c97630")
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
                        return new EnumType(getEnumeration()); 
                    case 3:
                        return new BooleanType();
                    case 4:
                        return new BooleanType();
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
    @objid ("8e5d0853-c068-11e1-8c0a-002564c97630")
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
                        setType(this.modelService, value);
                    break;
                    case 3:
                        this.theEditedElement.setParallelMultiple((Boolean) value);
                        break;
                    case 4:
                        this.theEditedElement.setIsInterrupting((Boolean) value);
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("8e5d0859-c068-11e1-8c0a-002564c97630")
    private Object getType() {
        if(getEnumeration() == EventType.class){            
            List<BpmnEventDefinition> definition = this.theEditedElement.getEventDefinitions();    
            if(definition.size() == 0){
                return EventType.None;
            }else  if(definition.size() == 1){
                return EventType.getType(definition.get(0));                  
            }else{
                return EventType.Multiple;
            }
        }else if(getEnumeration() == SubEventType.class){            
            List<BpmnEventDefinition> definition = this.theEditedElement.getEventDefinitions();    
            if(definition.size() == 0){
                return SubEventType.None;
            }else  if(definition.size() == 1){
                return SubEventType.getType(definition.get(0));                  
            }else{
                return SubEventType.Multiple;
            }
        }else if(getEnumeration() == InterSubEventType.class){            
            List<BpmnEventDefinition> definition = this.theEditedElement.getEventDefinitions();    
            if(definition.size() == 0){
                return InterSubEventType.None;
            }else  if(definition.size() == 1){
                return InterSubEventType.getType(definition.get(0));                  
            }else{
                return InterSubEventType.Multiple;
            }
        }
        return null;
    }

    @objid ("8e5d085d-c068-11e1-8c0a-002564c97630")
    private Class<? extends Enum<?>> getEnumeration() {
        if(((SmObjectImpl)this.theEditedElement).getCompositionOwner() instanceof BpmnSubProcess){
            if(this.theEditedElement.isIsInterrupting()){
                return InterSubEventType.class;
            }else{
                return SubEventType.class;
            }
        }else{
            return EventType.class ;
        }
    }

    @objid ("8e5d0860-c068-11e1-8c0a-002564c97630")
    private void setType(IMModelServices modelService, Object type) {
        if(getEnumeration() == EventType.class){ 
            EventType event_type = (EventType) type;
            if(type == EventType.None){
                deleteEventType();
            }else if(type == EventType.Multiple){
                createMultipleEventType(modelService);
            }else{
                deleteEventType();
                createEventType(modelService, EventType.getMetaclass(event_type));
            }
        }else if(getEnumeration() == SubEventType.class){            
            SubEventType event_type = (SubEventType) type;
            if(type == SubEventType.None){
                deleteEventType();
            }else if(type == SubEventType.Multiple){
                createMultipleEventType(modelService);
            }else{
                deleteEventType();
                createEventType(modelService, SubEventType.getMetaclass(event_type));
            }
        }else if(getEnumeration() == InterSubEventType.class){            
            InterSubEventType event_type = (InterSubEventType) type;
            if(type == InterSubEventType.None){
                deleteEventType();
            }else if(type == InterSubEventType.Multiple){
                createMultipleEventType(modelService);
            }else{
                deleteEventType();
                createEventType(modelService, InterSubEventType.getMetaclass(event_type));
            }
        }
    }

    @objid ("8e5d0863-c068-11e1-8c0a-002564c97630")
    private void createMultipleEventType(IMModelServices mmService) {
        List<BpmnEventDefinition> definitions = this.theEditedElement.getEventDefinitions();
        if(definitions.size() == 0){
            createEventType(mmService, BpmnMessageEventDefinition.class);
            createEventType(mmService, BpmnSignalEventDefinition.class);
        }else if(definitions.size() == 1){
            createEventType(mmService, BpmnMessageEventDefinition.class);
        }
    }

    @objid ("8e5d0865-c068-11e1-8c0a-002564c97630")
    private void createEventType(IMModelServices mmService, Class<? extends Element> metaclass) {
        IModelFactory modelFactory = mmService.getModelFactory();
        BpmnEventDefinition event_definition = (BpmnEventDefinition) modelFactory.createElement(metaclass);
        event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
        event_definition.setDefined(this.theEditedElement);
    }

    @objid ("8e5d0868-c068-11e1-8c0a-002564c97630")
    private void deleteEventType() {
        for (BpmnEventDefinition definition : new ArrayList<>(this.theEditedElement.getEventDefinitions())) {
            definition.delete();
        }
    }

    @objid ("8e5d086a-c068-11e1-8c0a-002564c97630")
    private enum EventType {
        None,
        Message,
        Timer,
        Conditional,
        Signal,
        Multiple;

        @objid ("8e5d0872-c068-11e1-8c0a-002564c97630")
        public static EventType getType(BpmnEventDefinition definition) {
            if(definition instanceof BpmnMessageEventDefinition){
                return Message;
            }else if(definition instanceof BpmnTimerEventDefinition){
                return Timer;
            }else if(definition instanceof BpmnConditionalEventDefinition){
                return Conditional;
            }else if(definition instanceof BpmnSignalEventDefinition){
                return Signal; 
            }
            return None;
        }

        @objid ("8e5d0879-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(EventType event) {
            if(event == EventType.Message){
                return BpmnMessageEventDefinition.class;
            }else if(event == EventType.Timer ){
                return BpmnTimerEventDefinition.class;
            }else if(event == EventType.Conditional ){
                return BpmnConditionalEventDefinition.class;
            }else if(event == EventType.Signal ){
                return BpmnSignalEventDefinition.class; 
            }
            return null;
        }

    }

    /**
     * Event Sub Process Interruption Type
     */
    @objid ("8e5d087e-c068-11e1-8c0a-002564c97630")
    private enum SubEventType {
        None,
        Message,
        Timer,
        Escalation,
        Conditional,
        Signal,
        Multiple;

        @objid ("8e5d0887-c068-11e1-8c0a-002564c97630")
        public static SubEventType getType(BpmnEventDefinition definition) {
            if(definition instanceof BpmnMessageEventDefinition){
                return Message;
            }else if(definition instanceof BpmnTimerEventDefinition){
                return Timer;
            }else if(definition instanceof BpmnEscalationEventDefinition){
                return Escalation;
            }else if(definition instanceof BpmnConditionalEventDefinition){
                return Conditional;
            }else if(definition instanceof BpmnSignalEventDefinition){
                return Signal; 
            }
            return None;
        }

        @objid ("8e5d088e-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(SubEventType event) {
            if(event == SubEventType.Message){
                return BpmnMessageEventDefinition.class;
            }else if(event == SubEventType.Timer ){
                return BpmnTimerEventDefinition.class;
            }else if(event == SubEventType.Escalation){
                return BpmnEscalationEventDefinition.class;
            }else if(event == SubEventType.Conditional ){
                return BpmnConditionalEventDefinition.class;
            }else if(event == SubEventType.Signal ){
                return BpmnSignalEventDefinition.class; 
            }
            return null;
        }

    }

    /**
     * Event Sub Process Interruption Type
     */
    @objid ("8e5e8ee6-c068-11e1-8c0a-002564c97630")
    private enum InterSubEventType {
        None,
        Message,
        Error,
        Timer,
        Escalation,
        Conditional,
        Signal,
        Multiple;

        @objid ("8e5e8ef0-c068-11e1-8c0a-002564c97630")
        public static InterSubEventType getType(BpmnEventDefinition definition) {
            if(definition instanceof BpmnMessageEventDefinition){
                return Message;
            }else if(definition instanceof BpmnTimerEventDefinition){
                return Error;
            }else if(definition instanceof BpmnErrorEventDefinition){
                return Timer;
            }else if(definition instanceof BpmnEscalationEventDefinition){
                return Escalation;
            }else if(definition instanceof BpmnConditionalEventDefinition){
                return Conditional;
            }else if(definition instanceof BpmnSignalEventDefinition){
                return Signal; 
            }
            return None;
        }

        @objid ("8e5e8ef7-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(InterSubEventType event) {
            if(event == InterSubEventType.Message){
                return BpmnMessageEventDefinition.class;
            }else if(event == InterSubEventType.Error){
                return BpmnTimerEventDefinition.class;
            }else if(event == InterSubEventType.Timer ){
                return BpmnTimerEventDefinition.class;
            }else if(event == InterSubEventType.Escalation){
                return BpmnEscalationEventDefinition.class;
            }else if(event == InterSubEventType.Conditional ){
                return BpmnConditionalEventDefinition.class;
            }else if(event == InterSubEventType.Signal ){
                return BpmnSignalEventDefinition.class;
            }
            return null;
        }

    }

}
