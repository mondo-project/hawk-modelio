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
import java.util.Collection;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.modelelement.ModelElementListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.objects.BpmnItemDefinition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Region;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>BpmnMessage</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnMessage</i> metaclass.
 */
@objid ("8e4317e0-c068-11e1-8c0a-002564c97630")
public class BpmnMessagePropertyModel extends AbstractPropertyModel<BpmnMessage> {
    /**
     * Properties to display for <i>BpmnMessage</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a5fe68eb-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "Message", "Name","GeneralClass","State","ItemDefinition" };

    @objid ("172ff337-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>BpmnMessage</i> data model from an <i>BpmnMessage</i>.
     */
    @objid ("8e449e4f-c068-11e1-8c0a-002564c97630")
    public BpmnMessagePropertyModel(BpmnMessage theEditedElement, IModel model) {
        super(theEditedElement);
        this.model = model;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e449e55-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e449e5a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnMessagePropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e449e5f-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnMessagePropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        return this.theEditedElement.getType();
                    case 3:
                         return this.theEditedElement.getInState();
                    case 4:              
                        return this.theEditedElement.getItemRef();
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
    @objid ("8e449e65-c068-11e1-8c0a-002564c97630")
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
                        return new SingleElementType(true, GeneralClass.class, CoreSession.getSession(this.theEditedElement));
                    case 3:    
                        return getInStateType();
                    case 4:
                        return new SingleElementType(true, BpmnItemDefinition.class, CoreSession.getSession(this.theEditedElement));
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
    @objid ("8e449e6b-c068-11e1-8c0a-002564c97630")
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
                    case 2 :
                        this.theEditedElement.setType((GeneralClass)value);
                        this.theEditedElement.setItemRef(null);
                        break;
                    case 3:
                        this.theEditedElement.setInState((State)value);
                        break;
                    case 4:
                        this.theEditedElement.setType(null);
                        this.theEditedElement.setItemRef((BpmnItemDefinition)value);
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("39dd43e1-3462-4109-bbcc-46eb32a7b68b")
    private IPropertyType getInStateType() {
        GeneralClass referencedClass = this.theEditedElement.getType();
        if (referencedClass == null) {
            return new SingleElementType(true, State.class, CoreSession.getSession(this.theEditedElement));
        }
        
        List<ModelElement> availableStates = getAvailableStates(referencedClass);
        
        ModelElementListType type = new ModelElementListType(true, State.class, availableStates, CoreSession.getSession(this.theEditedElement));
        return type;
    }

    /**
     * Get all states defined in a class.
     */
    @objid ("47aa7ded-62bf-4182-be1f-36a979f07ffc")
    private List<ModelElement> getAvailableStates(GeneralClass referencedClass) {
        List<ModelElement> states = new ArrayList<>();
        
        // Add states from owned state machines
        for (StateMachine sm : referencedClass.getOwnedBehavior(StateMachine.class)) {
            final Region topRegion = sm.getTop();
            states.addAll(getAvailableStates(topRegion));
        }
        
        // Add states from owned operations
        for (Operation op : referencedClass.getOwnedOperation()) {
            for (StateMachine sm : op.getOwnedBehavior(StateMachine.class)) {
                final Region topRegion = sm.getTop();
                states.addAll(getAvailableStates(topRegion));
            }
        }
        
        // Add states from owned classes
        for (GeneralClass sub : referencedClass.getOwnedElement(GeneralClass.class)) {
           states.addAll(getAvailableStates(sub));
            }
        return states;
    }

    /**
     * Get all states defined in a region.
     */
    @objid ("3f98f4ba-53ca-4d2e-88f2-4d29aae74e47")
    private Collection<? extends ModelElement> getAvailableStates(Region region) {
        List<ModelElement> states = new ArrayList<>();
        for (State s : region.getSub(State.class)) {
            states.add(s);
            
            for (Region subRegion : s.getOwnedRegion()) {
                states.addAll(getAvailableStates(subRegion));
            }
        }
        return states;
    }

}
