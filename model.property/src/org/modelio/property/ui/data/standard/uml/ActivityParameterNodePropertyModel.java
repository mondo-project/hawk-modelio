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
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.behavior.activityModel.ActivityParameterNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNode;
import org.modelio.metamodel.uml.behavior.activityModel.ObjectNodeOrderingKind;
import org.modelio.metamodel.uml.behavior.commonBehaviors.BehaviorParameter;
import org.modelio.metamodel.uml.behavior.stateMachineModel.State;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>ActivityParameterNode</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ActivityParameterNode</i> metaclass.
 */
@objid ("8ec7d807-c068-11e1-8c0a-002564c97630")
public class ActivityParameterNodePropertyModel extends AbstractPropertyModel<ActivityParameterNode> {
    /**
     * Properties to display for <i>ActivityParameterNode</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a664c40e-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ActivityParameterNode", "Name", "Type", "UpperBound", "IsControlType", "Ordering", "SelectionBehavior", "InState", "Represented"};

    @objid ("8ec7d812-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ec7d813-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8ec7d814-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8ec7d815-c068-11e1-8c0a-002564c97630")
    private SingleElementType generalClassType = null;

    @objid ("8ec7d816-c068-11e1-8c0a-002564c97630")
    private SingleElementType stateType = null;

    @objid ("8ec7d817-c068-11e1-8c0a-002564c97630")
    private EnumType orderingKindType = null;

    @objid ("8ec7d818-c068-11e1-8c0a-002564c97630")
    private SingleElementType representedType = null;

    /**
     * Create a new <i>ActivityParameterNode</i> data model from an <i>ActivityParameterNode</i>.
     */
    @objid ("8ec7d819-c068-11e1-8c0a-002564c97630")
    public ActivityParameterNodePropertyModel(ActivityParameterNode theEditedElement) {
        super(theEditedElement);
                    
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
        this.generalClassType = new SingleElementType(true, GeneralClass.class, CoreSession.getSession(this.theEditedElement));
        this.stateType = new SingleElementType(true, State.class, CoreSession.getSession(this.theEditedElement));
        this.orderingKindType = new EnumType(ObjectNodeOrderingKind.class);
                
        List<java.lang.Class<? extends MObject>> allowedClasses = new ArrayList<>();
        allowedClasses.add(Instance.class);
        allowedClasses.add(Attribute.class);
        allowedClasses.add(AssociationEnd.class);
        allowedClasses.add(BehaviorParameter.class);
        this.representedType = new SingleElementType(true, allowedClasses);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ec7d81f-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ec7d824-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ActivityParameterNodePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ec7d829-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ActivityParameterNodePropertyModel.PROPERTIES[row];
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
                    return new Boolean(this.theEditedElement.isIsControlType());
                case 5:
                    return this.theEditedElement.getOrdering();
                case 6:
                    return this.theEditedElement.getSelectionBehavior();
                case 7:
                    return this.theEditedElement.getInState();
                case 8:
                    return getRepresented(this.theEditedElement);
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
    @objid ("8ec7d82f-c068-11e1-8c0a-002564c97630")
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
                    return this.stringType;
                case 4:
                    return this.booleanType;
                case 5:
                    return this.orderingKindType;
                case 6:
                    return this.stringType;
                case 7:
                    return this.stateType;
                case 8:
                    return this.representedType;
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
    @objid ("8ec7d835-c068-11e1-8c0a-002564c97630")
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
                    this.theEditedElement.setType((GeneralClass) value);
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
                case 8:
                    setRepresented(this.theEditedElement, value);
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
    @objid ("8ec7d83b-c068-11e1-8c0a-002564c97630")
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
     * Set the InstanceNode represented elements.
     * This method set the right dependency and clears the otheEditedElement.
     * @param theEditedElement the instance node
     * @param value the new represented element
     */
    @objid ("8ec7d845-c068-11e1-8c0a-002564c97630")
    private void setRepresented(ObjectNode theEditedElement, Object value) {
        // Erase old value or exit if old value is new value
        Instance old1 = theEditedElement.getRepresented();
        if (old1 != null) {
            if (old1.equals(value)) return;
            theEditedElement.setRepresented(null);
        } else {
            Attribute old2 = theEditedElement.getRepresentedAttribute();
            if (old2 != null) {
                if (old2.equals(value)) return;
                theEditedElement.setRepresentedAttribute(null);
            } else {
                AssociationEnd old3 = theEditedElement.getRepresentedRole();
                if (old3 != null) {
                    if (old3.equals(value)) return;
                    theEditedElement.setRepresentedRole(null);
                } else {
                    BehaviorParameter old4 = theEditedElement.getRepresentedRealParameter();                    
                    if (old4 != null) {
                        if (old4.equals(value)) return;
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

}
