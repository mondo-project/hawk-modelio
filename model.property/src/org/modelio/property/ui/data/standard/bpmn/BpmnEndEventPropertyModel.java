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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.bpmn.events.BpmnCancelEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnCompensateEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEndEvent;
import org.modelio.metamodel.bpmn.events.BpmnErrorEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEscalationEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnMessageEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnSignalEventDefinition;
import org.modelio.metamodel.bpmn.events.BpmnTerminateEventDefinition;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * <i>BpmnEndEvent</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnEndEvent</i> metaclass.
 */
@objid ("8e230cba-c068-11e1-8c0a-002564c97630")
public class BpmnEndEventPropertyModel extends AbstractPropertyModel<BpmnEndEvent> {
    /**
     * Properties to display for <i>BpmnEndEvent</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a5c7a948-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "EndEvent", "Name","Type"};

    @objid ("aacdd5c8-d004-11e1-9020-002564c97630")
    private IMModelServices modelService;

    /**
     * Create a new <i>BpmnEndEvent</i> data model from an <i>BpmnEndEvent</i>.
     */
    @objid ("8e230cc5-c068-11e1-8c0a-002564c97630")
    public BpmnEndEventPropertyModel(BpmnEndEvent theEditedElement, IMModelServices modelService) {
        super(theEditedElement);
        this.modelService = modelService;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e249325-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e24932a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnEndEventPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e24932f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnEndEventPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        return getType();
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
    @objid ("8e249335-c068-11e1-8c0a-002564c97630")
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
    @objid ("8e24933b-c068-11e1-8c0a-002564c97630")
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
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("8e249341-c068-11e1-8c0a-002564c97630")
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

    @objid ("8e249344-c068-11e1-8c0a-002564c97630")
    private Class<? extends Enum<?>> getEnumeration() {
        return EventType.class;
    }

    @objid ("8e249348-c068-11e1-8c0a-002564c97630")
    private void setType(IMModelServices modelService, EventType type) {
        if (type == EventType.None) {
            deleteEventType();
        } else if (type == EventType.Multiple) {
            createMultipleEventType(modelService);
        } else {
            deleteEventType();
            createEventType(modelService, EventType.getMetaclass(type));
        }
    }

    @objid ("8e24934b-c068-11e1-8c0a-002564c97630")
    private void createMultipleEventType(IMModelServices mmService) {
        List<BpmnEventDefinition> definitions = this.theEditedElement.getEventDefinitions();
        if (definitions.size() == 0) {
            createEventType(mmService, BpmnMessageEventDefinition.class);
            createEventType(mmService, BpmnSignalEventDefinition.class);
        } else if (definitions.size() == 1) {
            createEventType(mmService, BpmnMessageEventDefinition.class);
        }
    }

    @objid ("8e24934d-c068-11e1-8c0a-002564c97630")
    private void createEventType(IMModelServices mmService, Class<? extends Element> metaclass) {
        IModelFactory modelFactory = mmService.getModelFactory();
        BpmnEventDefinition event_definition = (BpmnEventDefinition) modelFactory.createElement(metaclass);
        event_definition.setName(mmService.getElementNamer().getBaseName(event_definition.getMClass()));
        event_definition.setDefined(this.theEditedElement);
    }

    @objid ("8e249350-c068-11e1-8c0a-002564c97630")
    private void deleteEventType() {
        for (BpmnEventDefinition definition : new ArrayList<>(this.theEditedElement.getEventDefinitions())) {
            definition.delete();
        }
    }

    @objid ("8e249352-c068-11e1-8c0a-002564c97630")
    private enum EventType {
        None,
        Message,
        Error,
        Escalation,
        Cancel,
        Compensate,
        Signal,
        Terminate,
        Multiple;

        @objid ("8e24935c-c068-11e1-8c0a-002564c97630")
        public static EventType getType(BpmnEventDefinition definition) {
            if (definition instanceof BpmnMessageEventDefinition) {
                return Message;
            } else if (definition instanceof BpmnErrorEventDefinition) {
                return Error;
            } else if (definition instanceof BpmnEscalationEventDefinition) {
                return Escalation;
            } else if (definition instanceof BpmnCancelEventDefinition) {
                return Cancel;
            } else if (definition instanceof BpmnCompensateEventDefinition) {
                return Compensate;
            } else if (definition instanceof BpmnSignalEventDefinition) {
                return Signal;
            }else if (definition instanceof BpmnTerminateEventDefinition) {
                return Terminate;
            }
            return None;
        }

        @objid ("8e249363-c068-11e1-8c0a-002564c97630")
        public static Class<? extends Element> getMetaclass(EventType event) {
            if (event == EventType.Message) {
                return BpmnMessageEventDefinition.class;
            } else if (event == EventType.Error) {
                return BpmnErrorEventDefinition.class;
            } else if (event == EventType.Escalation) {
                return  BpmnEscalationEventDefinition.class;
            } else if (event == EventType.Cancel) {
                return BpmnCancelEventDefinition.class;
            } else if (event == EventType.Compensate) {
                return BpmnCompensateEventDefinition.class;
            } else if (event == EventType.Signal) {
                return BpmnSignalEventDefinition.class;
            }else if (event == EventType.Terminate) {
                return BpmnTerminateEventDefinition.class;
            }
            return null;
        }

    }

}
