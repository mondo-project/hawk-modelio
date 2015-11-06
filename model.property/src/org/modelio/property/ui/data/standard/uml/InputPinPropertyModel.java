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
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.activityModel.InputPin;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>InputPin</i> data model.
 * <p>
 * This class provides the list of properties for the <i>InputPin</i> metaclass.
 */
@objid ("8f298015-c068-11e1-8c0a-002564c97630")
public class InputPinPropertyModel extends AbstractPropertyModel<InputPin> {
    /**
     * Properties to display for <i>InputPin</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the
     * metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles
     * names of the metaclass
     * </ul>
     */
    @objid ("a74ba968-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "InputPin", "Name", "Type", "UpperBound",
            "IsSelf", "IsExpansion", "Matched", "IsControlType", "InState", "Represented" };

    @objid ("fa81504a-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("fa81504b-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa81504c-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa81504d-c5d4-11e1-8f21-002564c97630")
    private EditableListType cardinalityMaxType;

    @objid ("fa81504e-c5d4-11e1-8f21-002564c97630")
    private SingleElementType generalClassType;

    @objid ("fa81504f-c5d4-11e1-8f21-002564c97630")
    private SingleElementType parameterType;

    @objid ("fa815050-c5d4-11e1-8f21-002564c97630")
    private SingleElementType stateType;

    @objid ("fa815051-c5d4-11e1-8f21-002564c97630")
    private RepresentedType representedType;

    /**
     * Create a new <i>InputPin</i> data model from an <i>InputPin</i>.
     * @param model
     */
    @objid ("8f298020-c068-11e1-8c0a-002564c97630")
    public InputPinPropertyModel(InputPin theEditedElement, IModel model) {
        // Removed properties: "Ordering", "SelectionBehavior",
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.generalClassType = new SingleElementType(false, GeneralClass.class, session);
        this.parameterType = new SingleElementType(false, Parameter.class, session);
        this.stateType = new SingleElementType(false, State.class, session);
        this.representedType = new RepresentedType(session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f298026-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f29802b-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return InputPinPropertyModel.PROPERTIES.length;
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
    @objid ("8f298030-c068-11e1-8c0a-002564c97630")
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
                        return this.generalClassType;
                    case 3:
                        return this.cardinalityMaxType;
                    case 4:
                        return this.booleanType;
                    case 5:
                        return this.booleanType;
                    case 6:
                        return this.parameterType;
                    case 7:
                        return this.booleanType;
                    case 8:
                        return this.stateType;
                    case 9:
                        return this.representedType;
                    default:
                        return null;
                }
            default:
                return null;
        }
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f298038-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return InputPinPropertyModel.PROPERTIES[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        return this.theEditedElement.getType();
                    case 3:
                        return this.theEditedElement.getUpperBound();
                    case 4:
                        return this.theEditedElement.isIsSelf() ? Boolean.TRUE : Boolean.FALSE;
                    case 5:
                        return this.theEditedElement.isIsExpansion() ? Boolean.TRUE : Boolean.FALSE;
                    case 6:
                        return this.theEditedElement.getMatched();
                    case 7:
                        return this.theEditedElement.isIsControlType() ? Boolean.TRUE : Boolean.FALSE;
                    case 8:
                        return this.theEditedElement.getInState();
                    case 9:
                        return getRepresented(this.theEditedElement);
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
    @objid ("8f29803e-c068-11e1-8c0a-002564c97630")
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
                        return;
                    case 2:
                        this.theEditedElement.setType((GeneralClass) value);
                        return;
                    case 3:
                        this.theEditedElement.setUpperBound((String) value);
                        return;
                    case 4:
                        this.theEditedElement.setIsSelf(((Boolean) value).booleanValue());
                        return;
                    case 5:
                        this.theEditedElement.setIsExpansion(((Boolean) value).booleanValue());
                        return;
                    case 6:
                        this.theEditedElement.setMatched((Parameter) value);
                        return;
                    case 7:
                        this.theEditedElement.setIsControlType(((Boolean) value).booleanValue());
                        return;
                    case 8:
                        this.theEditedElement.setInState((State) value);
                        return;
                    case 9:
                        setRepresented(this.theEditedElement, value);
                        return;
                    default:
                        return;
                }
            default:
                return;
        }
    }

    /**
     * Returns the element represented by the given instance node.
     * @return the represented element
     */
    @objid ("8f298044-c068-11e1-8c0a-002564c97630")
    private ModelElement getRepresented(ObjectNode elt) {
        ModelElement ret = elt.getRepresented();
        if (ret != null)
            return ret;
        ret = elt.getRepresentedAttribute();
        if (ret != null)
            return ret;
        ret = elt.getRepresentedRole();
        if (ret != null)
            return ret;
        ret = elt.getRepresentedRealParameter();
        return ret;
    }

    /**
     * Set the InstanceNode represented elements. This method set the right
     * dependency and clears the otheEditedElement.
     * @param theEditedElement the instance node
     * @param value the new represented element
     */
    @objid ("8f2b06ab-c068-11e1-8c0a-002564c97630")
    private void setRepresented(ObjectNode theEditedElement, Object value) {
        // Erase old value or exit if old value is new value
        Instance old1 = theEditedElement.getRepresented();
        if (old1 != null) {
            if (old1.equals(value))
                return;
            theEditedElement.setRepresented(null);
        } else {
            Attribute old2 = theEditedElement.getRepresentedAttribute();
            if (old2 != null) {
                if (old2.equals(value))
                    return;
                theEditedElement.setRepresentedAttribute(null);
            } else {
                AssociationEnd old3 = theEditedElement.getRepresentedRole();
                if (old3 != null) {
                    if (old3.equals(value))
                        return;
                    theEditedElement.setRepresentedRole(null);
                } else {
                    BehaviorParameter old4 = theEditedElement.getRepresentedRealParameter();
                    if (old4 != null) {
                        if (old4.equals(value))
                            return;
                        theEditedElement.setRepresentedRealParameter(null);
                    }
                }
            }
        }
        
        if (value != null) {
            // Set new value
            if (Instance.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresented((Instance) value);
            else if (Attribute.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresentedAttribute((Attribute) value);
            else if (AssociationEnd.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresentedRole((AssociationEnd) value);
            else if (BehaviorParameter.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresentedRealParameter((BehaviorParameter) value);
        }
    }

    @objid ("8f2b06b2-c068-11e1-8c0a-002564c97630")
    public static class RepresentedType extends HybridType {
        @objid ("8f2b06b4-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8f2b06b9-c068-11e1-8c0a-002564c97630")
        public RepresentedType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(Instance.class);
            this.t.add(Attribute.class);
            this.t.add(AssociationEnd.class);
            this.t.add(BehaviorParameter.class);
        }

        @objid ("8f2b06bb-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        @objid ("d11d8e0d-9afa-46ed-aff1-ebacd2e15377")
        @Override
        public boolean acceptStringValue() {
            return false;
        }

    }

}
