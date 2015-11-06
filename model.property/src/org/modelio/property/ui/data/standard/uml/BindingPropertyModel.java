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
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.BindableInstance;
import org.modelio.metamodel.uml.statik.Binding;
import org.modelio.metamodel.uml.statik.ConnectorEnd;
import org.modelio.metamodel.uml.statik.LinkEnd;
import org.modelio.metamodel.uml.statik.NaryAssociation;
import org.modelio.metamodel.uml.statik.NaryConnector;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>Binding</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Binding</i> metaclass.
 */
@objid ("8ed71a8a-c068-11e1-8c0a-002564c97630")
public class BindingPropertyModel extends AbstractPropertyModel<Binding> {
    /**
     * Properties to display for <i>Binding</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a68878a8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Binding", "Role", "RepresentedFeature"};

    @objid ("8ed8a0ee-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8ed8a0ef-c068-11e1-8c0a-002564c97630")
    private SingleElementType roleType = null;

    @objid ("8ed8a0f0-c068-11e1-8c0a-002564c97630")
    private SingleElementType representingFeatureType = null;

    /**
     * Create a new <i>Binding</i> data model from an <i>Binding</i>.
     */
    @objid ("8ed8a0f1-c068-11e1-8c0a-002564c97630")
    public BindingPropertyModel(Binding theEditedElement) {
        super(theEditedElement);
                    
        this.labelStringType = new StringType(false);
        
        List<java.lang.Class<? extends MObject>> roleTypes = new ArrayList<>();
        roleTypes.add(BindableInstance.class);
        roleTypes.add(ConnectorEnd.class);
        this.roleType = new SingleElementType(true, roleTypes);
        
        List<java.lang.Class<? extends MObject>> representingTypes = new ArrayList<>();
        representingTypes.add(BindableInstance.class);
        representingTypes.add(Attribute.class);
        representingTypes.add(Parameter.class);
        representingTypes.add(AssociationEnd.class);
        representingTypes.add(NaryAssociation.class);
        representingTypes.add(LinkEnd.class);
        this.representingFeatureType = new SingleElementType(false, representingTypes);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8ed8a0f7-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8ed8a0fc-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return BindingPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8ed8a101-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return BindingPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
                case 0: // Header
                    return "Value";
                case 1:
                    return getRole(this.theEditedElement);
                case 2:
                    return this.theEditedElement.getRepresentedFeature();
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
    @objid ("8ed8a107-c068-11e1-8c0a-002564c97630")
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
                    return this.roleType;
                case 2:
                    return this.representingFeatureType;
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
    @objid ("8ed8a10d-c068-11e1-8c0a-002564c97630")
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
                    setRole(this.theEditedElement, value);
                    break;
                case 2:
                    this.theEditedElement.setRepresentedFeature((ModelElement) value);
                    break;
                default:
                    return;
            }
              break;
        default:
            return;
        }
    }

    @objid ("8ed8a113-c068-11e1-8c0a-002564c97630")
    public static ModelElement getRole(Binding el) {
        ModelElement ret = el.getConnectorEndRole();
        if (ret != null)
            return ret;
        
        ret = el.getConnectorRole();
        if (ret != null)
            return ret;
        
        ret = el.getRole();
        return ret;
    }

    @objid ("8ed8a11c-c068-11e1-8c0a-002564c97630")
    public static void setRole(Binding theEditedElement, Object value) {
        // Erase old value or exit if old value is new value
        ConnectorEnd old1 = theEditedElement.getConnectorEndRole();
        if (old1 != null) {
            if (old1.equals(value)) return;
            theEditedElement.setConnectorEndRole(null);
        } else {
            NaryConnector old2 = theEditedElement.getConnectorRole();
            if (old2 != null) {
                if (old2.equals(value)) return;
                theEditedElement.setConnectorRole(null);
            } else {
                BindableInstance old3 = theEditedElement.getRole();
                if (old3 != null) {
                    if (old3.equals(value)) return;
                    theEditedElement.setRole(null);
                }
            }
        }
        
        if (value != null) {
            // Set new value
            if (ConnectorEnd.class.isAssignableFrom(value.getClass()))
                theEditedElement.setConnectorEndRole((ConnectorEnd) value);
            else if (NaryConnector.class.isAssignableFrom(value.getClass()))
                theEditedElement.setConnectorRole((NaryConnector) value);
            else if (BindableInstance.class.isAssignableFrom(value.getClass()))
                theEditedElement.setRole((BindableInstance) value);
        }
    }

}
