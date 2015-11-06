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
import org.modelio.metamodel.bpmn.objects.BpmnDataInput;
import org.modelio.metamodel.bpmn.objects.BpmnItemDefinition;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Region;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;

/**
 * <i>BpmnDataInput</i> data model.
 * <p>
 * This class provides the list of properties for the <i>BpmnDataInput</i> metaclass.
 */
@objid ("8e1e78a5-c068-11e1-8c0a-002564c97630")
public class BpmnDataInputPropertyModel extends AbstractPropertyModel<BpmnDataInput> {
    /**
     * Properties to display for <i>BpmnDataInput</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a5bbc26e-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "DataInput", "Name","GeneralClass","State","Instance","AssociationEnd","Attribute","ItemDefinition","Parameter" };

    @objid ("1605a408-16da-11e2-aa0d-002564c97630")
    private IModel model;

    /**
     * Create a new <i>BpmnDataInput</i> data model from an <i>BpmnDataInput</i>.
     */
    @objid ("8e1e78b0-c068-11e1-8c0a-002564c97630")
    public BpmnDataInputPropertyModel(BpmnDataInput theEditedElement, IModel model) {
        super(theEditedElement);
        this.model = model;
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8e1e78b6-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8e1e78bb-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BpmnDataInputPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8e1e78c0-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return BpmnDataInputPropertyModel.properties[row];
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
                        return this.theEditedElement.getRepresentedInstance();
                    case 5:
                        return this.theEditedElement.getRepresentedAssociationEnd();
                    case 6:
                        return this.theEditedElement.getRepresentedAttribute();
                    case 7:
                        return this.theEditedElement.getItemSubjectRef();
                    case 8:
                        return this.theEditedElement.getRepresentedParameter();
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
    @objid ("8e1e78c6-c068-11e1-8c0a-002564c97630")
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
                        return new SingleElementType(true, Instance.class, CoreSession.getSession(this.theEditedElement));
                    case 5:
                        return new SingleElementType(true, AssociationEnd.class, CoreSession.getSession(this.theEditedElement));
                    case 6:
                        return new SingleElementType(true, Attribute.class, CoreSession.getSession(this.theEditedElement));
                    case 7:
                        return new SingleElementType(true, BpmnItemDefinition.class, CoreSession.getSession(this.theEditedElement));
                    case 8:
                        return new SingleElementType(true, Parameter.class, CoreSession.getSession(this.theEditedElement));
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
    @objid ("8e1e78cc-c068-11e1-8c0a-002564c97630")
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
                        this.theEditedElement.setType((GeneralClass)value);
                        this.theEditedElement.setRepresentedInstance(null);
                        this.theEditedElement.setRepresentedAssociationEnd(null);
                        this.theEditedElement.setRepresentedAttribute(null);
                        this.theEditedElement.setRepresentedParameter(null);
                        break;
                    case 3:
                        this.theEditedElement.setInState((State)value);
                        break;
                    case 4:
                        this.theEditedElement.setType(null);
                        this.theEditedElement.setRepresentedInstance((Instance) value);
                        this.theEditedElement.setRepresentedAssociationEnd(null);
                        this.theEditedElement.setRepresentedAttribute(null);
                        this.theEditedElement.setRepresentedParameter(null);
                        break;
                    case 5:
                        this.theEditedElement.setType(null);
                        this.theEditedElement.setRepresentedInstance(null);
                        this.theEditedElement.setRepresentedAssociationEnd((AssociationEnd) value);
                        this.theEditedElement.setRepresentedAttribute(null);
                        this.theEditedElement.setRepresentedParameter(null);
                        break;
                    case 6:
                        this.theEditedElement.setType(null);
                        this.theEditedElement.setRepresentedInstance(null);
                        this.theEditedElement.setRepresentedAssociationEnd(null);
                        this.theEditedElement.setRepresentedAttribute((Attribute) value);
                        this.theEditedElement.setRepresentedParameter(null);
                        break;
                    case 7:
                        this.theEditedElement.setType(null);
                        this.theEditedElement.setItemSubjectRef((BpmnItemDefinition)value);
                        this.theEditedElement.setRepresentedInstance(null);
                        this.theEditedElement.setRepresentedAssociationEnd(null);
                        this.theEditedElement.setRepresentedAttribute(null);
                        this.theEditedElement.setRepresentedParameter(null);
                        break;
                    case 8:
                        this.theEditedElement.setType(null);
                        this.theEditedElement.setRepresentedInstance(null);
                        this.theEditedElement.setRepresentedAssociationEnd(null);
                        this.theEditedElement.setRepresentedAttribute(null);
                        this.theEditedElement.setRepresentedParameter((Parameter) value);
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("2ebe9581-4fa4-45f9-a79b-2b9188a4e788")
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
    @objid ("484fddef-7e63-41f8-a356-2da79e641db5")
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
    @objid ("5f3c37a5-f3eb-4895-b415-a591deb83d5c")
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
