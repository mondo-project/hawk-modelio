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
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.list.EditableListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.activityModel.ExpansionNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNodeOrderingKind;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>ExpansionNode</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ExpansionNode</i> metaclass.
 */
@objid ("8f15aa0d-c068-11e1-8c0a-002564c97630")
public class ExpansionNodePropertyModel extends AbstractPropertyModel<ExpansionNode> {
    /**
     * Properties to display for <i>ExpansionNode</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a70dc5a6-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ExpansionNode", "Name", "TypeRepresented", "UpperBound",
			"IsControlType", "Ordering", "SelectionBehavior", "InState" };

    @objid ("fa64527c-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fa64527d-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fa64527e-c5d4-11e1-8f21-002564c97630")
    private BooleanType booleanType;

    @objid ("fa64527f-c5d4-11e1-8f21-002564c97630")
    private EditableListType cardinalityMaxType;

    @objid ("fa645280-c5d4-11e1-8f21-002564c97630")
    private SingleElementType stateType;

    @objid ("fa645281-c5d4-11e1-8f21-002564c97630")
    private EnumType objectNodeOrderingKindType;

    @objid ("fa645282-c5d4-11e1-8f21-002564c97630")
    private RepresentedType representedType;

    /**
     * Create a new <i>ExpansionNode</i> data model from an <i>ExpansionNode</i>.
     * @param model
     */
    @objid ("8f15aa18-c068-11e1-8c0a-002564c97630")
    public ExpansionNodePropertyModel(ExpansionNode theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        
        List<String> cardinalityMaxValues = new ArrayList<>();
        cardinalityMaxValues.add("1");
        cardinalityMaxValues.add("*");
        this.cardinalityMaxType = new EditableListType(true, cardinalityMaxValues);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.stateType = new SingleElementType(false, State.class, session );
        this.objectNodeOrderingKindType = new EnumType(ObjectNodeOrderingKind.class);
        this.representedType = new RepresentedType(session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f15aa1e-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f15aa23-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ExpansionNodePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f173085-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ExpansionNodePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                return this.theEditedElement.getName();
            case 2:
                return getRepresented();
            case 3:
                return this.theEditedElement.getUpperBound();
            case 4:
                return this.theEditedElement.isIsControlType()?Boolean.TRUE:Boolean.FALSE;
            case 5:
                return this.theEditedElement.getOrdering();
            case 6:
                return this.theEditedElement.getSelectionBehavior();
            case 7:
                return this.theEditedElement.getInState();
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
    @objid ("8f17308b-c068-11e1-8c0a-002564c97630")
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
                return this.representedType;
            case 3:
                return this.cardinalityMaxType;
            case 4:
                return this.booleanType;
            case 5:
                return this.objectNodeOrderingKindType;
            case 6:
                return this.stringType;
            case 7:
                return this.stateType;
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
    @objid ("8f173093-c068-11e1-8c0a-002564c97630")
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
                setRepresented(this.theEditedElement, value);
                break;
            case 3:
                this.theEditedElement.setUpperBound((String) value);
                break;
            case 4:
                this.theEditedElement.setIsControlType(((Boolean) value).booleanValue());
                break;
            case 5:
                this.theEditedElement.setOrdering((ObjectNodeOrderingKind) value);
                break;
            case 6:
                this.theEditedElement.setSelectionBehavior((String) value);
                break;
            case 7:
                this.theEditedElement.setInState((State) value);
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
     * Returns the element represented by the given instance node.
     * @return the represented element
     */
    @objid ("8f173099-c068-11e1-8c0a-002564c97630")
    private ModelElement getRepresented() {
        ModelElement ret = this.theEditedElement.getRepresented();
        if (ret != null)
            return ret;
        ret = this.theEditedElement.getRepresentedAttribute();
        if (ret != null)
            return ret;
        ret = this.theEditedElement.getRepresentedRole();
        if (ret != null)
            return ret;
        ret = this.theEditedElement.getRepresentedRealParameter();
        if (ret != null)
            return ret;
        ret = this.theEditedElement.getType();
        return ret;
    }

    /**
     * Set the InstanceNode represented elements.
     * This method set the right dependency and clears the otheEditedElement.
     * @param theEditedElement the instance node
     * @param value the new represented element
     */
    @objid ("8f1730a0-c068-11e1-8c0a-002564c97630")
    private void setRepresented(final ExpansionNode theEditedElement, final Object value) {
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
                    } else {
                        GeneralClass old5 = theEditedElement.getType();
                        if (old5 != null) {
                            if (old5.equals(value))
                                return;
                            theEditedElement.setType(null);
                        }
                    }
                }
            }
        }
                
        // Set new value
        if (value != null) {
            if (Instance.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresented((Instance) value);
            else if (Attribute.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresentedAttribute((Attribute) value);
            else if (AssociationEnd.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresentedRole((AssociationEnd) value);
            else if (BehaviorParameter.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRepresentedRealParameter((BehaviorParameter) value);
            else if (GeneralClass.class.isAssignableFrom(value.getClass()))
                theEditedElement.setType((GeneralClass) value);
        }
    }

    @objid ("8f1730a9-c068-11e1-8c0a-002564c97630")
    public static class RepresentedType extends HybridType {
        @objid ("8f1730ab-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8f1730b0-c068-11e1-8c0a-002564c97630")
        public RepresentedType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(Instance.class);
            this.t.add(Attribute.class);
            this.t.add(AssociationEnd.class);
            this.t.add(BehaviorParameter.class);
            this.t.add(GeneralClass.class);
        }

        @objid ("8f1730b2-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        @objid ("39f4676a-77d8-4d95-be4b-5d6b3c19b91d")
        @Override
        public boolean acceptStringValue() {
            return false;
        }

    }

}
