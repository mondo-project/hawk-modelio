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
import org.modelio.metamodel.uml.behavior.commonBehaviors.Behavior;
import org.modelio.metamodel.uml.behavior.commonBehaviors.Signal;
import org.modelio.metamodel.uml.behavior.stateMachineModel.InternalTransition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Transition;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>InternalTransition</i> data model.
 * <p>
 * This class provides the list of properties for the <i>InternalTransition</i> metaclass.
 */
@objid ("8f342e94-c068-11e1-8c0a-002564c97630")
public class InternalTransitionPropertyModel extends AbstractPropertyModel<InternalTransition> {
    /**
     * Properties to display for <i>InternalTransition</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a76115d1-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "InternalTransition", "ReceivedEvents", "Guard",
			"Effect", "SentEvents", "Post" };

    @objid ("fa939fbc-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa939fbd-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa939fbe-c5d4-11e1-8f21-002564c97630")
    private EnumType receivedEventTypeType;

    @objid ("fa939fbf-c5d4-11e1-8f21-002564c97630")
    private TransitionEffectType transitionEffectType;

    @objid ("fa939fc0-c5d4-11e1-8f21-002564c97630")
    private TransitionSentType transitionSentType;

    /**
     * Create a new <i>InternalTransition</i> data model from an <i>InternalTransition</i>.
     */
    @objid ("8f342e9f-c068-11e1-8c0a-002564c97630")
    public InternalTransitionPropertyModel(InternalTransition theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.receivedEventTypeType = new EnumType(ReceivedEventType.class);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.transitionEffectType = new TransitionEffectType(session);
        this.transitionSentType = new TransitionSentType(session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f342ea5-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f35b509-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return InternalTransitionPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f35b50e-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return InternalTransitionPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                // Received event
                String s = this.theEditedElement.getReceivedEvents();
                if (s.equals("Entry"))
                    return ReceivedEventType.ENTRY;
                else if (s.equals("Do"))
                    return ReceivedEventType.DO;
                else if (s.equals("Exit"))
                    return ReceivedEventType.EXIT;
                else {
                    return ReceivedEventType.DO;
                }
            case 2:
                return this.theEditedElement.getGuard();
            case 3:
                return TransitionEffectType.getValue(this.theEditedElement);
            case 4:
                return TransitionSentType.getValue(this.theEditedElement);
            case 5:
                return this.theEditedElement.getPostCondition();
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
    @objid ("8f35b514-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            switch (row) {
            case 0:
                return this.stringType; // ** Header
            case 1:
                return this.receivedEventTypeType; // ReceivedEvents
            case 2:
                return this.stringType; // Guard
            case 3:
                return this.transitionEffectType; // Effect
            case 4:
                return this.transitionSentType; // SentEvents
            case 5:
                return this.stringType; // Post
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
    @objid ("8f35b51c-c068-11e1-8c0a-002564c97630")
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
                ReceivedEventType v = (ReceivedEventType) value;
                switch (v) {
                case ENTRY:
                    this.theEditedElement.setReceivedEvents("Entry");
                    break;
                case DO:
                    this.theEditedElement.setReceivedEvents("Do");
                    break;
                case EXIT:
                    this.theEditedElement.setReceivedEvents("Exit");
                    break;
                default:
                    break;
                }
                
                break;
            case 2:
                this.theEditedElement.setGuard((String) value);
                break;
            case 3:
                TransitionEffectType.setValue(this.theEditedElement, value);
                break;
            case 4:
                TransitionSentType.setValue(this.theEditedElement, value);
                break;
            case 5:
                this.theEditedElement.setPostCondition((String) value);
                break;
            default:
                return;
            }
              break;
        default:
            return;
        }
    }

    /**
     * Represents the Transition effects.
     * 
     * Merges the following Transition features:
     * - Effect : string
     * - Processed : Operation
     * - BehaviorEffect : Behavior
     * 
     * @author cmarin
     */
    @objid ("8f35b522-c068-11e1-8c0a-002564c97630")
    protected static class TransitionEffectType extends HybridType {
        @objid ("8f35b525-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8f35b52a-c068-11e1-8c0a-002564c97630")
        public TransitionEffectType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(Operation.class);
            this.t.add(Behavior.class);
        }

        @objid ("8f35b52c-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        /**
         * Get the effect of a transition
         * @param t a Transition
         * @return a String, an Operation or a Behavior
         */
        @objid ("8f35b538-c068-11e1-8c0a-002564c97630")
        public static Object getValue(Transition t) {
            String sEffect = t.getEffect();
            if (sEffect != null && !sEffect.isEmpty())
                return sEffect;
                        
            Operation op = t.getProcessed();
            if (op != null)
                return op;
                        
            Behavior b = t.getBehaviorEffect();
            return b;
        }

        /**
         * Set the effect of a Transition
         * @param t a Transition
         * @param value a String, an Operation or a Behavior
         */
        @objid ("8f35b540-c068-11e1-8c0a-002564c97630")
        public static void setValue(Transition t, Object value) {
            // Erase old value or exit if old value is new value
            String old1 = t.getEffect();
            if (old1 != null && !old1.isEmpty()) {
                if (old1.equals(value))
                    return;
                t.setEffect("");
            } else {
                Operation old2 = t.getProcessed();
                if (old2 != null) {
                    if (old2.equals(value))
                        return;
                    t.setProcessed(null);
                } else {
                    Behavior old3 = t.getBehaviorEffect();
                    if (old3 != null) {
                        if (old3.equals(value))
                            return;
                        t.setBehaviorEffect(null);
                    }
                }
            }
                        
            if (value != null) {
                // Set new value
                if (String.class.isAssignableFrom(value.getClass()))
                    t.setEffect((String) value);
                else if (Operation.class.isAssignableFrom(value.getClass()))
                    t.setProcessed((Operation) value);
                else if (Behavior.class.isAssignableFrom(value.getClass()))
                    t.setBehaviorEffect((Behavior) value);
                else
                    throw new IllegalArgumentException("value must be a String, an Operation or a Behavior but not a "
                            + value.getClass().getCanonicalName());
            }
        }

        @objid ("94645b78-af92-4897-88dc-60297a248fb9")
        @Override
        public boolean acceptStringValue() {
            return true;
        }

    }

    /**
     * Represents the Transition sent signals.
     * 
     * Merges the following Transition features:
     * - SentEvents : string
     * - Effects : Signal
     * 
     * @author cmarin
     */
    @objid ("8f35b547-c068-11e1-8c0a-002564c97630")
    protected static class TransitionSentType extends HybridType {
        @objid ("8f35b54a-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8f373ba7-c068-11e1-8c0a-002564c97630")
        public TransitionSentType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(Signal.class);
        }

        @objid ("8f373ba9-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        /**
         * Get the sent signal of a transition
         * @param t a Transition
         * @return a String, or a Signal
         */
        @objid ("8f373bb5-c068-11e1-8c0a-002564c97630")
        public static Object getValue(Transition t) {
            String sEffect = t.getSentEvents();
            if (sEffect != null && !sEffect.isEmpty())
                return sEffect;
                        
            Signal op = t.getEffects();
            return op;
        }

        /**
         * Set the signal sent from a Transition
         * @param t a Transition
         * @param value a String or a Signal
         */
        @objid ("8f373bbd-c068-11e1-8c0a-002564c97630")
        public static void setValue(Transition t, Object value) {
            // Erase old value or exit if old value is new value
            String old1 = t.getSentEvents();
            if (old1 != null && !old1.isEmpty()) {
                if (old1.equals(value))
                    return;
                t.setSentEvents("");
            } else {
                Signal old2 = t.getEffects();
                if (old2 != null) {
                    if (old2.equals(value))
                        return;
                    t.setEffects(null);
                }
            }
                        
            if (value != null) {
                // Set new value
                if (String.class.isAssignableFrom(value.getClass()))
                    t.setSentEvents((String) value);
                else if (Signal.class.isAssignableFrom(value.getClass()))
                    t.setEffects((Signal) value);
                else
                    throw new IllegalArgumentException("value must be a String or a Signal but not a "
                            + value.getClass().getCanonicalName());
            }
        }

        @objid ("431f93c4-f2b6-4103-bcd6-55bd6fdcf73a")
        @Override
        public boolean acceptStringValue() {
            return true;
        }

    }

    /**
     * Predefined events for InternalTransition.ReceivedEvents
     * @author cmarin
     */
    @objid ("8f373bc4-c068-11e1-8c0a-002564c97630")
    protected enum ReceivedEventType {
        ENTRY,
        DO,
        EXIT;
    }

}
