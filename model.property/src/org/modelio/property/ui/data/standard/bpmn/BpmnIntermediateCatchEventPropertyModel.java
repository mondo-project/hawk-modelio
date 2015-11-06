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
import org.modelio.metamodel.bpmn.events.BpmnConditionalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnIntermediateCatchEvent;
import org.modelio.metamodel.bpmn.events.BpmnLinkEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnMessageEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnSignalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnTimerEventDefinition;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>BpmnIntermediateCatchEvent</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnIntermediateCatchEvent</i> metaclass.
 */
@objid ("8e355c38-c068-11e1-8c0a-002564c97630")
public class BpmnIntermediateCatchEventPropertyModel extends AbstractPropertyModel<BpmnIntermediateCatchEvent> {
    @objid ("a5e69b2b-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "IntermediateCatchEvent", "Name","Type", "ParallelMultiple" };

    @objid ("aa80031c-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    /**
     * Create a new <i>BpmnIntermediateCatchEvent</i> data model from an <i>BpmnIntermediateCatchEvent</i>.
     */
    @objid ("8e355c42-c068-11e1-8c0a-002564c97630")
    public BpmnIntermediateCatchEventPropertyModel(BpmnIntermediateCatchEvent theEditedElement, IMModelServices modelService) {
        super(theEditedElement);
        this.modelService = modelService;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e36e2a9-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e36e2ae-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnIntermediateCatchEventPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e36e2b3-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnIntermediateCatchEventPropertyModel.properties[row];
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
    @objid ("8e36e2b9-c068-11e1-8c0a-002564c97630")
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
    @objid ("8e36e2bf-c068-11e1-8c0a-002564c97630")
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
                        setType(this.modelService, (EventType) value);
                        break;
                    case 3:
                        this.theEditedElement.setParallelMultiple((Boolean) value);
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("8e36e2c5-c068-11e1-8c0a-002564c97630")
    private Object getType() {
        List<BpmnEventDefinition> definition = this.theEditedElement.getEventDefinitions();
        if (definition.size() == 0) {
            return EventType.None;
        } else if (definition.size() == 1) {
            return EventType.getType(definition.get(0));
        } else {
            return EventType.Multiple;
        }
    }

    @objid ("8e36e2c8-c068-11e1-8c0a-002564c97630")
    private Class<? extends Enum<?>> getEnumeration() {
        return EventType.class;
    }

    @objid ("8e36e2cc-c068-11e1-8c0a-002564c97630")
    private void setType(IMModelServices mmService, EventType type) {
        if (type == EventType.None) {
            deleteEventType();
        } else if (type == EventType.Multiple) {
            createMultipleEventType(mmService);
        } else {
            deleteEventType();
            createEventType(mmService, EventType.getMetaclass(type));
        }
    }

    @objid ("8e36e2cf-c068-11e1-8c0a-002564c97630")
    private void createMultipleEventType(IMModelServices mmService) {
        List<BpmnEventDefinition> definitions = this.theEditedElement.getEventDefinitions();
        if (definitions.size() == 0) {
            createEventType(mmService, BpmnMessageEventDefinition.class);
            createEventType(mmService, BpmnSignalEventDefinition.class);
        } else if (definitions.size() == 1) {
            createEventType(mmService, BpmnMessageEventDefinition.class);
        }
    }

    @objid ("8e36e2d1-c068-11e1-8c0a-002564c97630")
    private void createEventType(IMModelServices mmService, Class<? extends Element> metaclass) {
        IModelFactory modelFactory = mmService.getModelFactory();
        BpmnEventDefinition event_definition = (BpmnEventDefinition) modelFactory.createElement(metaclass);
        event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
        event_definition.setDefined(this.theEditedElement);
    }

    @objid ("8e36e2d4-c068-11e1-8c0a-002564c97630")
    private void deleteEventType() {
        for (BpmnEventDefinition definition : new ArrayList<>(this.theEditedElement.getEventDefinitions())) {
            definition.delete();
        }
    }

    /**
     * Properties to display for <i>BpmnIntermediateCatchEvent</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     * 
     * Event Type
     */
    @objid ("8e36e2d6-c068-11e1-8c0a-002564c97630")
    private enum EventType {
        None,
        Message,
        Timer,
        Conditional,
        Link,
        Signal,
        Multiple;

        @objid ("8e36e2df-c068-11e1-8c0a-002564c97630")
        public static EventType getType(BpmnEventDefinition definition) {
            if (definition instanceof BpmnMessageEventDefinition) {
                return Message;
            } else if (definition instanceof BpmnTimerEventDefinition) {
                return Timer;
            } else if (definition instanceof BpmnConditionalEventDefinition) {
                return Conditional;
            } else if (definition instanceof BpmnLinkEventDefinition) {
                return Link;
            } else if (definition instanceof BpmnSignalEventDefinition) {
                return Signal;
            }
            return None;
        }

        @objid ("8e36e2e6-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(EventType event) {
            if (event == EventType.Message) {
                return BpmnMessageEventDefinition.class;
            } else if (event == EventType.Timer) {
                return BpmnTimerEventDefinition.class;
            } else if (event == EventType.Conditional) {
                return BpmnConditionalEventDefinition.class;
            } else if (event == EventType.Link) {
                return BpmnLinkEventDefinition.class;
            } else if (event == EventType.Signal) {
                return BpmnSignalEventDefinition.class;
            }
            return null;
        }

    }

}
