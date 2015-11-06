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
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.uml.statik.TemplateParameterSubstitution;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>TemplateParameterSubstitution</i> data model.
 * <p>
 * This class provides the list of properties for the <i>TemplateParameterSubstitution</i> metaclass.
 */
@objid ("8f94bd2f-c068-11e1-8c0a-002564c97630")
public class TemplateParameterSubstitutionPropertyModel extends AbstractPropertyModel<TemplateParameterSubstitution> {
    /**
     * Properties to display for <i>TemplateParameterSubstitution</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a873cde8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "TemplateParameterSubstitution", "Value" };

    @objid ("fb355d93-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fb355d94-c5d4-11e1-8f21-002564c97630")
    private SubstitutionValue substitutionValue;

    /**
     * Create a new <i>TemplateParameterSubstitution</i> data model from an <i>TemplateParameterSubstitution</i>.
     */
    @objid ("8f94bd3a-c068-11e1-8c0a-002564c97630")
    public TemplateParameterSubstitutionPropertyModel(TemplateParameterSubstitution theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.substitutionValue = new SubstitutionValue(CoreSession.getSession(this.theEditedElement));
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f94bd40-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f94bd45-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return TemplateParameterSubstitutionPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f94bd4a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return TemplateParameterSubstitutionPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return SubstitutionValue.getValue(this.theEditedElement);
                // case 2:
                // return theEditedElement.getFormalParameter();
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
    @objid ("8f94bd50-c068-11e1-8c0a-002564c97630")
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
                return this.substitutionValue;
                // case 2:
                // return TemplateParameter.class;
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
    @objid ("8f94bd58-c068-11e1-8c0a-002564c97630")
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
                SubstitutionValue.setValue(this.theEditedElement, value);
                break;
            // case 2:
            // theEditedElement.setFormalParameter((TemplateParameter) value);
            // break;
            default:
                return;
            }
              break;
        default:
            return;
        }
    }

    @objid ("8f94bd5e-c068-11e1-8c0a-002564c97630")
    public static String[] getPROPERTIES() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return PROPERTIES;
    }

    @objid ("8f94bd64-c068-11e1-8c0a-002564c97630")
    public TemplateParameterSubstitution gettheEditedElement() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.theEditedElement;
    }

    /**
     * Represents the 'Actual' and 'Value' properties on TemplateParameterSubstitution
     * @see com.modeliosoft.objecteering.joni.model.TemplateParameterSubstitution.Actual
     * @see com.modeliosoft.objecteering.joni.model.TemplateParameterSubstitution
     */
    @objid ("8f94bd6a-c068-11e1-8c0a-002564c97630")
    public static class SubstitutionValue extends HybridType {
        @objid ("8f94bd6d-c068-11e1-8c0a-002564c97630")
         final List<Class<? extends MObject>> stdTypes;

        @objid ("8f94bd73-c068-11e1-8c0a-002564c97630")
        public SubstitutionValue(ICoreSession session) {
            super(session);
            this.stdTypes = new ArrayList<>();
            this.stdTypes.add(ModelElement.class);
        }

        @objid ("8f94bd75-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.stdTypes;
        }

        @objid ("8f94bd81-c068-11e1-8c0a-002564c97630")
        public List<Class<? extends MObject>> getTypes(TemplateParameterSubstitution el) {
            TemplateParameter param = el.getFormalParameter();
            if (param == null)
                return this.stdTypes;
                        
            ModelElement paramType = param.getType();
            if (paramType == null)
                return this.stdTypes;
            List<Class<? extends MObject>> ret = new ArrayList<>();
            ret.add(paramType.getClass());
            return ret;
        }

        @objid ("8f94bd8c-c068-11e1-8c0a-002564c97630")
        public static Object getValue(TemplateParameterSubstitution el) {
            ModelElement r1 = el.getActual();
            if (r1 != null)
                return r1;
            return el.getValue();
        }

        @objid ("8f971e88-c068-11e1-8c0a-002564c97630")
        public static void setValue(TemplateParameterSubstitution el, Object value) {
            if (value != null && value.getClass() == String.class) {
                if (el.getActual() != null)
                    el.setActual(null);
                el.setValue((String) value);
            } else {
                if (!el.getValue().isEmpty())
                    el.setValue("");
                el.setActual((ModelElement) value);
                        
            }
        }

        @objid ("8f971e8e-c068-11e1-8c0a-002564c97630")
        public List<Class<? extends MObject>> getStdTypes() {
            // Automatically generated method. Please delete this comment before entering specific code.
            return this.stdTypes;
        }

        @objid ("b4842f3e-c24c-4851-b2d9-93fb135b4f95")
        @Override
        public boolean acceptStringValue() {
            return true;
        }

    }

}
