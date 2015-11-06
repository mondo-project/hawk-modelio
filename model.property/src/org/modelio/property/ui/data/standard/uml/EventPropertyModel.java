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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Event;
import org.modelio.metamodel.uml.behavior.commonBehaviors.EventType;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>Event</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Event</i> metaclass.
 */
@objid ("8f0f8f6d-c068-11e1-8c0a-002564c97630")
public class EventPropertyModel extends AbstractPropertyModel<Event> {
    /**
     * Properties to display for <i>Event</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6ff7d6b-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "Event", "Name", "Kind" };

    @objid ("fa57cf61-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa57cf62-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa57cf63-c5d4-11e1-8f21-002564c97630")
    private EnumType eventTypeType;

    @objid ("fa57f66d-c5d4-11e1-8f21-002564c97630")
    private EventKindType eventKindType;

    @objid ("984a6861-cb47-11e1-9165-002564c97630")
     static Event staticEvent;

    /**
     * Create a new <i>Event</i> data model from an <i>Event</i>.
     */
    @objid ("8f0f8f78-c068-11e1-8c0a-002564c97630")
    public EventPropertyModel(Event theEditedElement) {
        super(theEditedElement);
        
        staticEvent = theEditedElement;
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.eventTypeType = new EnumType(EventType.class);
        this.eventKindType = new EventKindType(CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f0f8f7e-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f0f8f83-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return EventPropertyModel.PROPERTIES.length + 1;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f0f8f88-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            switch (row) {
            case 3:
                return EventKindType.getLabel(this.theEditedElement);
            default:
                return EventPropertyModel.PROPERTIES[row];
            }
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getKind();
            case 3:
                return EventKindType.getValue(this.theEditedElement);
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
    @objid ("8f0f8f8e-c068-11e1-8c0a-002564c97630")
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
                return this.eventTypeType;
            case 3:
                return this.eventKindType;
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
    @objid ("8f0f8f96-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setKind((EventType) value);
                EventKindType.resetValue(this.theEditedElement);
                break;
            case 3:
                EventKindType.setValue(this.theEditedElement, value);
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    @objid ("8f0f8f9c-c068-11e1-8c0a-002564c97630")
    protected static class EventKindType extends HybridType {
        @objid ("8f111606-c068-11e1-8c0a-002564c97630")
        public EventKindType(ICoreSession session) {
            super(session);
        }

        @objid ("8f111608-c068-11e1-8c0a-002564c97630")
        public List<Class<? extends MObject>> getTypes(Element element) {
            List<Class<? extends MObject>> types = new ArrayList<>();
            
            if (element instanceof Event) {
                Event theEditedElement = (Event) element;
                if (theEditedElement.getKind() == EventType.CALLEVENT) {
                    types.add(Operation.class);
                } else if (theEditedElement.getKind() == EventType.CHANGEEVENT) {
                    //types[0] = String.class;
                } else if (theEditedElement.getKind() == EventType.SIGNALEVENT) {
                    types.add(Signal.class);
                } else if (theEditedElement.getKind() == EventType.TIMEEVENT) {
                    //types[0] = String.class;
                }
            }
            return types;
        }

        @objid ("8f111614-c068-11e1-8c0a-002564c97630")
        public static Object getValue(Event e) {
            if (e.getKind() == EventType.CALLEVENT) {
                return e.getCalled();
            } else if (e.getKind() == EventType.CHANGEEVENT) {
                return e.getExpression();
            } else if (e.getKind() == EventType.SIGNALEVENT) {
                return e.getModel();
            } else if (e.getKind() == EventType.TIMEEVENT) {
                return e.getExpression();
            } else {
                return null;
            }
        }

        @objid ("8f11161a-c068-11e1-8c0a-002564c97630")
        public static Object getLabel(Event e) {
            if (e.getKind() == EventType.CALLEVENT) {
                return "Called";
            } else if (e.getKind() == EventType.CHANGEEVENT) {
                return "Expression";
            } else if (e.getKind() == EventType.SIGNALEVENT) {
                return "Model";
            } else if (e.getKind() == EventType.TIMEEVENT) {
                return "Expression";
            } else {
                return null;
            }
        }

        @objid ("8f111620-c068-11e1-8c0a-002564c97630")
        public static void setValue(Event e, Object value) {
            // Erase old value or exit if old value is new value
            e.setCalled(null);
            e.setExpression("");
            e.setModel(null);
            
            if (e.getKind() == EventType.CALLEVENT) {
                e.setCalled((Operation) value);
            } else if (e.getKind() == EventType.CHANGEEVENT) {
                e.setExpression((String) value);
            } else if (e.getKind() == EventType.SIGNALEVENT) {
                e.setModel((Signal) value);
            } else if (e.getKind() == EventType.TIMEEVENT) {
                e.setExpression((String) value);
            }
        }

        @objid ("8f111626-c068-11e1-8c0a-002564c97630")
        public static void resetValue(Event e) {
            if (e.getKind() == EventType.CALLEVENT) {
                e.setExpression("");
                e.setModel(null);
            } else if (e.getKind() == EventType.CHANGEEVENT) {
                e.setCalled(null);
                e.setModel(null);
            } else if (e.getKind() == EventType.SIGNALEVENT) {
                e.setExpression("");
                e.setCalled(null);
            } else if (e.getKind() == EventType.TIMEEVENT) {
                e.setCalled(null);
                e.setModel(null);
            }
        }

        @objid ("fa5b2abf-c5d4-11e1-8f21-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return getTypes(staticEvent);
        }

        @objid ("abf62483-e487-476f-908e-fd91331e1910")
        @Override
        public boolean acceptStringValue() {
            if (staticEvent.getKind() == EventType.CALLEVENT) {
                return false;
            } else if (staticEvent.getKind() == EventType.CHANGEEVENT) {
                return true;
            } else if (staticEvent.getKind() == EventType.SIGNALEVENT) {
                return false;
            } else if (staticEvent.getKind() == EventType.TIMEEVENT) {
                return true;
            }
            return false;
        }

    }

}
