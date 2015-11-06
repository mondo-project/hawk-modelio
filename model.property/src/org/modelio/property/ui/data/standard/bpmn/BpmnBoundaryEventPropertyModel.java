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
import org.modelio.metamodel.bpmn.events.BpmnBoundaryEvent;
import org.modelio.metamodel.bpmn.events.BpmnCancelEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnCompensateEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnConditionalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnErrorEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEscalationEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnMessageEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnSignalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnTimerEventDefinition;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>BpmnBoundaryEvent</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnBoundaryEvent</i> metaclass.
 */
@objid ("8e10bd07-c068-11e1-8c0a-002564c97630")
public class BpmnBoundaryEventPropertyModel extends AbstractPropertyModel<BpmnBoundaryEvent> {
    /**
     * Properties to display for <i>BpmnBoundaryEvent</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a5a3f4b1-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "BoundaryEvent", "Name","Type", "ParallelMultiple", "CancelActivity" };

    @objid ("aa15324a-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    /**
     * Create a new <i>BpmnBoundaryEvent</i> data model from an <i>BpmnBoundaryEvent</i>.
     */
    @objid ("8e10bd12-c068-11e1-8c0a-002564c97630")
    public BpmnBoundaryEventPropertyModel(BpmnBoundaryEvent theEditedElement, IMModelServices modelService) {
        super(theEditedElement);
        this.modelService = modelService;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e10bd18-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e10bd1d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnBoundaryEventPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e10bd22-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return BpmnBoundaryEventPropertyModel.properties[row];
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
                return !this.theEditedElement.isCancelActivity() ? Boolean.TRUE : Boolean.FALSE;
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
    @objid ("8e10bd28-c068-11e1-8c0a-002564c97630")
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
    @objid ("8e10bd2e-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setCancelActivity(!(Boolean) value);
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    @objid ("8e10bd34-c068-11e1-8c0a-002564c97630")
    private Object getType() {
        if (getEnumeration() == EventType.class) {
            List<BpmnEventDefinition> definition = this.theEditedElement.getEventDefinitions();
            if (definition.size() == 0) {
                return EventType.None;
            } else if (definition.size() == 1) {
                return EventType.getType(definition.get(0));
            } else {
                return EventType.Multiple;
            }
        } else if (getEnumeration() == InterEventType.class) {
            List<BpmnEventDefinition> definition = this.theEditedElement.getEventDefinitions();
            if (definition.size() == 0) {
                return InterEventType.None;
            } else if (definition.size() == 1) {
                return InterEventType.getType(definition.get(0));
            } else {
                return InterEventType.Multiple;
            }
        }
        return null;
    }

    @objid ("8e10bd38-c068-11e1-8c0a-002564c97630")
    private Class<? extends Enum<?>> getEnumeration() {
        if (this.theEditedElement.isCancelActivity()) {
            return InterEventType.class;
        } else {
            return EventType.class;
        }
    }

    @objid ("8e10bd3b-c068-11e1-8c0a-002564c97630")
    private void setType(IMModelServices mmService, Object type) {
        if (getEnumeration() == EventType.class) {
            EventType event_type = (EventType) type;
            if (type == EventType.None) {
                deleteEventType();
            } else if (type == EventType.Multiple) {
                createMultipleEventType(mmService);
            } else {
                deleteEventType();
                createEventType(mmService, EventType.getMetaclass(event_type));
            }
        } else if (getEnumeration() == InterEventType.class) {
            InterEventType event_type = (InterEventType) type;
            if (type == InterEventType.None) {
                deleteEventType();
            } else if (type == InterEventType.Multiple) {
                createMultipleEventType(mmService);
            } else {
                deleteEventType();
                createEventType(mmService, InterEventType.getMetaclass(event_type));
            }
        }
    }

    @objid ("8e10bd3e-c068-11e1-8c0a-002564c97630")
    private void createMultipleEventType(IMModelServices mmService) {
        IModelFactory modelFactory = mmService.getModelFactory();
        
        List<BpmnEventDefinition> definitions = this.theEditedElement.getEventDefinitions();
        if (definitions.size() == 0) {
            BpmnEventDefinition event_definition = modelFactory.createBpmnMessageEventDefinition();
            event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
            event_definition.setDefined(this.theEditedElement);
            
            event_definition = modelFactory.createBpmnSignalEventDefinition();
            event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
            event_definition.setDefined(this.theEditedElement);
        } else if (definitions.size() == 1) {
            BpmnEventDefinition event_definition = modelFactory.createBpmnMessageEventDefinition();
            event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
            event_definition.setDefined(this.theEditedElement);
        }
    }

    @objid ("8e10bd40-c068-11e1-8c0a-002564c97630")
    private void createEventType(IMModelServices mmService, Class<? extends Element> metaclass) {
        IModelFactory modelFactory = mmService.getModelFactory();
        BpmnEventDefinition event_definition = (BpmnEventDefinition) modelFactory.createElement(metaclass);
        event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
        event_definition.setDefined(this.theEditedElement);
    }

    @objid ("8e10bd43-c068-11e1-8c0a-002564c97630")
    private void deleteEventType() {
        for (BpmnEventDefinition definition : new ArrayList<>(this.theEditedElement.getEventDefinitions())) {
            definition.delete();
        }
    }

    @objid ("8e10bd45-c068-11e1-8c0a-002564c97630")
    private enum InterEventType {
        None,
        Message,
        Timer,
        Error,
        Escalation,
        Cancel,
        Compensate,
        Conditional,
        Signal,
        Multiple;

        @objid ("8e1243ab-c068-11e1-8c0a-002564c97630")
        public static InterEventType getType(BpmnEventDefinition definition) {
            if (definition instanceof BpmnMessageEventDefinition) {
                return Message;
            } else if (definition instanceof BpmnTimerEventDefinition) {
                return Timer;
            } else if (definition instanceof BpmnErrorEventDefinition) {
                return Error;
            } else if (definition instanceof BpmnEscalationEventDefinition) {
                return Escalation;
            } else if (definition instanceof BpmnCancelEventDefinition) {
                return Cancel;
            } else if (definition instanceof BpmnCompensateEventDefinition) {
                return Compensate;
            } else if (definition instanceof BpmnConditionalEventDefinition) {
                return Conditional;
            } else if (definition instanceof BpmnSignalEventDefinition) {
                return Signal;
            }
            return None;
        }

        @objid ("8e1243b2-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(InterEventType event) {
            if (event == InterEventType.Message) {
                return BpmnMessageEventDefinition.class;
            } else if (event == InterEventType.Timer) {
                return BpmnTimerEventDefinition.class;
            } else if (event == InterEventType.Error) {
                return BpmnErrorEventDefinition.class;
            } else if (event == InterEventType.Escalation) {
                return BpmnEscalationEventDefinition.class;
            } else if (event == InterEventType.Cancel) {
                return BpmnCancelEventDefinition.class;
            } else if (event == InterEventType.Compensate) {
                return BpmnCompensateEventDefinition.class;
            } else if (event == InterEventType.Conditional) {
                return BpmnConditionalEventDefinition.class;
            } else if (event == InterEventType.Signal) {
                return BpmnSignalEventDefinition.class;
            }
            return null;
        }

    }

    @objid ("8e1243b7-c068-11e1-8c0a-002564c97630")
    private enum EventType {
        None,
        Message,
        Timer,
        Escalation,
        Conditional,
        Signal,
        Multiple;

        @objid ("8e1243bf-c068-11e1-8c0a-002564c97630")
        public static EventType getType(BpmnEventDefinition definition) {
            if (definition instanceof BpmnMessageEventDefinition) {
                return Message;
            } else if (definition instanceof BpmnTimerEventDefinition) {
                return Timer;
            } else if (definition instanceof BpmnEscalationEventDefinition) {
                return Escalation;
            } else if (definition instanceof BpmnConditionalEventDefinition) {
                return Conditional;
            } else if (definition instanceof BpmnSignalEventDefinition) {
                return Signal;
            }
            return None;
        }

        @objid ("8e1243c6-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(EventType event) {
            if (event == EventType.Message) {
                return BpmnMessageEventDefinition.class;
            } else if (event == EventType.Timer) {
                return BpmnTimerEventDefinition.class;
            } else if (event == EventType.Escalation) {
                return BpmnEscalationEventDefinition.class;
            } else if (event == EventType.Conditional) {
                return BpmnConditionalEventDefinition.class;
            } else if (event == EventType.Signal) {
                return BpmnSignalEventDefinition.class;
            }
            return null;
        }

    }

}
