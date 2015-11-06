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
                                    

package org.modelio.property.ui.data.standard.uml.templateparameter;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>TemplateParameter</i> data model for template parameter that own a model element.
 * <p>
 * This class provides the list of properties for the <i>TemplateParameter</i> metaclass.
 * <p>
 * This data model has been manually created
 */
@objid ("8f8d9931-c068-11e1-8c0a-002564c97630")
class ElementTemplateParameterPropertyModel extends AbstractPropertyModel<TemplateParameter> {
    /**
     * Properties to display for <i>TemplateParameter</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a86798e8-c068-11e1-8c0a-002564c97630")
    private static final String[] properties = new String[] { "TemplateParameter", "Name", "DefaultType"};

    @objid ("fb2aaf36-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fb2aaf37-c5d4-11e1-8f21-002564c97630")
    private StringType stringType;

    @objid ("fb2aaf38-c5d4-11e1-8f21-002564c97630")
    private DefaultType defaultType;

    /**
     * Create a new <i>TemplateParameter</i> data model from an <i>TemplateParameter</i>.
     * @param theEditedElement
     */
    @objid ("8f8d993c-c068-11e1-8c0a-002564c97630")
    public ElementTemplateParameterPropertyModel(TemplateParameter theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.defaultType = new DefaultType(CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f8d9942-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f8ffa65-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ElementTemplateParameterPropertyModel.properties.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f8ffa6a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return ElementTemplateParameterPropertyModel.properties[row];
            case 1: // col 1 is the property value
                switch (row) {
                    case 0: // Header
                        return "Value";
                    case 1:
                        return this.theEditedElement.getName();
                    case 2:
                        Object ret = this.theEditedElement.getDefaultType();
                        if (ret != null)
                            return ret;
                        
                        return this.theEditedElement.getDefaultValue();
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
    @objid ("8f8ffa70-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key type
                return this.labelStringType;
            case 1: // col 1 is the property value type
                switch (row) {
                    case 0: // Header
                        return this.labelStringType;
                    case 1: // Name
                        return this.stringType;
                    case 2: // Default type
                        return this.defaultType;
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
    @objid ("8f8ffa78-c068-11e1-8c0a-002564c97630")
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
                        // Set the TemplateParameter and its owned parameter name.
                        this.theEditedElement.setName((String) value);
                        this.theEditedElement.getOwnedParameterElement().setName((String) value);
                        break;
                    case 2:
                        // Erase old value or exit if old value is new value
                        ModelElement old1 = this.theEditedElement.getDefaultType();
                        if (old1 != null) {
                            if (old1.equals(value))
                                return;
                            this.theEditedElement.setDefaultType(null);
                        } else {
                            String old2 = this.theEditedElement.getDefaultValue();
                            if (old2 != null && !old2.equals("")) {
                                if (old2.equals(value))
                                    return;
                                this.theEditedElement.setDefaultValue("");
                            }
                        }
        
                        if (value != null) {
                            // Set new value
                            if (ModelElement.class.isAssignableFrom(value.getClass()))
                                this.theEditedElement.setDefaultType((ModelElement) value);
                            else if (String.class.isAssignableFrom(value.getClass()))
                                this.theEditedElement.setDefaultValue((String) value);
                        }
                        break;
                    default:
                        return;
                }
                break;
            default:
                return;
        }
    }

    @objid ("8f8ffa7e-c068-11e1-8c0a-002564c97630")
    public static class DefaultType extends HybridType {
        @objid ("8f8ffa80-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> t;

        @objid ("8f8ffa85-c068-11e1-8c0a-002564c97630")
        public DefaultType(ICoreSession session) {
            super(session);
            this.t = new ArrayList<>();
            this.t.add(ModelElement.class);
        }

        @objid ("8f8ffa87-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.t;
        }

        @objid ("11e5aefa-d15a-4391-8cfb-8d8baef83a75")
        @Override
        public boolean acceptStringValue() {
            return true;
        }

    }

}