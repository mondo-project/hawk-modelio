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
import org.eclipse.emf.common.util.EList;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.hybrid.HybridType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.TemplateBinding;
import org.modelio.metamodel.uml.statik.TemplateParameter;
import org.modelio.metamodel.uml.statik.TemplateParameterSubstitution;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>TemplateBinding</i> data model.
 * <p>
 * This class provides the list of properties for the <i>TemplateBinding</i> metaclass.
 */
@objid ("8f8b37a5-c068-11e1-8c0a-002564c97630")
public class TemplateBindingPropertyModel extends AbstractPropertyModel<TemplateBinding> {
    /**
     * Properties to display for <i>TemplateBinding</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a8630508-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "TemplateBinding", "InstanciatedTemplate" };

    @objid ("8f8b37b0-c068-11e1-8c0a-002564c97630")
    private boolean isOperation;

    @objid ("fb230e1f-c5d4-11e1-8f21-002564c97630")
    private StringType labelStringType;

    @objid ("fb230e20-c5d4-11e1-8f21-002564c97630")
    private SingleElementType operationType;

    @objid ("fb230e21-c5d4-11e1-8f21-002564c97630")
    private SingleElementType namespaceType;

    @objid ("fb230e22-c5d4-11e1-8f21-002564c97630")
    private SubstitutionValue substitutionValue;

    @objid ("22e25ec6-d00a-11e1-9020-002564c97630")
    private IMModelServices modelService;

    /**
     * Create a new <i>TemplateBinding</i> data model from an <i>TemplateBinding</i>.
     * @param theEditedElement the edited element.
     * @param modelService the model services
     */
    @objid ("8f8b37b1-c068-11e1-8c0a-002564c97630")
    public TemplateBindingPropertyModel(TemplateBinding theEditedElement, IMModelServices modelService) {
        super(theEditedElement);
        this.modelService = modelService;
        this.isOperation = (theEditedElement.getBoundOperation() != null);
        
        this.labelStringType = new StringType(false);
        ICoreSession session = CoreSession.getSession(this.theEditedElement);
        this.operationType = new SingleElementType(true, Operation.class, session);
        this.namespaceType = new SingleElementType(true, NameSpace.class, session);
        this.substitutionValue = new SubstitutionValue(session);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f8b37b7-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f8b37bc-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return TemplateBindingPropertyModel.PROPERTIES.length + getTemplateParameters().size();
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f8b37c1-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            if (row < 2)
                return TemplateBindingPropertyModel.PROPERTIES[row];
            // It is a template substitution row
            return "=" + getTemplateParameters().get(row - 2).getName() + " = ";
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                if (this.isOperation)
                    return this.theEditedElement.getInstanciatedTemplateOperation();
                return this.theEditedElement.getInstanciatedTemplate();
                
            default:
                // It is a template substitution row
                return SubstitutionValue.getValue(this.theEditedElement, getTemplateParameters().get(row - 2));
                
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
    @objid ("8f8b37c7-c068-11e1-8c0a-002564c97630")
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
                if (this.isOperation)
                    return this.operationType;
                // else
                return this.namespaceType;
                
            default:
                return this.substitutionValue;
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
    @objid ("8f8b37cf-c068-11e1-8c0a-002564c97630")
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
                if (this.isOperation)
                    this.theEditedElement.setInstanciatedTemplateOperation((Operation) value);
                else
                    this.theEditedElement.setInstanciatedTemplate((NameSpace) value);
                
                updateTemplateBinding(this.theEditedElement, this.modelService);
                
                break;
            default:
                // It is a template substitution
                SubstitutionValue.setValue(this.theEditedElement, getTemplateParameters().get(row - 2), value, this.modelService);
            }
            break;
        default:
            return;
        }
    }

    @objid ("8f8b37d5-c068-11e1-8c0a-002564c97630")
    private List<TemplateParameter> getTemplateParameters() {
        if (this.isOperation) {
            Operation op = this.theEditedElement.getInstanciatedTemplateOperation();
            if (op != null) {
                return op.getTemplate();
            }
            return new ArrayList<>();
        }
        NameSpace ns = this.theEditedElement.getInstanciatedTemplate();
        if (ns != null) {
            return ns.getTemplate();
        }
        return new ArrayList<>();
    }

    /**
     * Change the binding substitutions so as it reflects the template parameters
     * on the bound namespace or operations.
     * Deletes the obsolete substitutions and create the missing ones with theEditedElement default values.
     * @param aTemplateBinding the binding to update
     */
    @objid ("8f8b37dd-c068-11e1-8c0a-002564c97630")
    static void updateTemplateBinding(TemplateBinding aTemplateBinding, IMModelServices mmService) {
        List<TemplateParameter> parameters;
                
        Operation op = aTemplateBinding.getInstanciatedTemplateOperation();
        NameSpace ns = aTemplateBinding.getInstanciatedTemplate();
        if (op != null && aTemplateBinding.getBoundOperation() != null) {
            parameters = new ArrayList<>(op.getTemplate());
            aTemplateBinding.setName("->" + op.getName());
        } else if (ns != null) {
            parameters = new ArrayList<>(ns.getTemplate());
            aTemplateBinding.setName("->" + ns.getName());
        } else {
            parameters = new ArrayList<>();
            aTemplateBinding.setName("");
        }
                
        // Clear all obsolete TemplateParameterSubstitution
        for (TemplateParameterSubstitution sub : new ArrayList<>(aTemplateBinding.getParameterSubstitution())) {
            if (!parameters.contains(sub.getFormalParameter())) {
                sub.delete();
            }
        }
                
        // Create missing substitutions
        EList<TemplateParameterSubstitution> substitutions = aTemplateBinding.getParameterSubstitution();
        List<TemplateParameter> substituedParameters = new ArrayList<>(substitutions.size());
                
        for (TemplateParameterSubstitution sub : substitutions) {
            substituedParameters.add(sub.getFormalParameter());
        }
                
        for (TemplateParameter param : parameters) {
            if (!substituedParameters.contains(param)) {
                TemplateParameterSubstitution newSub = mmService.getModelFactory().createTemplateParameterSubstitution();
                newSub.setFormalParameter(param);
                newSub.setName(param.getName());
                newSub.setActual(param.getDefaultType());
                newSub.setValue(param.getDefaultValue());
                aTemplateBinding.getParameterSubstitution().add(newSub);
            }
        }
    }

    /**
     * Represents the 'Actual' and 'Value' properties on TemplateParameterSubstitution
     * @see TemplateParameterSubstitution#getActual()
     * @see TemplateParameterSubstitution#getValue()
     */
    @objid ("8f8b37e3-c068-11e1-8c0a-002564c97630")
    public static class SubstitutionValue extends HybridType {
        @objid ("8f8b37e6-c068-11e1-8c0a-002564c97630")
         final List<Class<? extends MObject>> stdTypes;

        /**
         * Default constructor.
         * @param session the core modeling session
         */
        @objid ("8f8b37ec-c068-11e1-8c0a-002564c97630")
        public SubstitutionValue(ICoreSession session) {
            super(session);
            this.stdTypes = new ArrayList<>();
            this.stdTypes.add(ModelElement.class);
        }

        @objid ("8f8b37ef-c068-11e1-8c0a-002564c97630")
        @Override
        public List<Class<? extends MObject>> getTypes() {
            return this.stdTypes;
        }

        @objid ("8f8b37fb-c068-11e1-8c0a-002564c97630")
        private List<Class<? extends MObject>> getTypes(TemplateParameterSubstitution el) {
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

        @objid ("8f8d9905-c068-11e1-8c0a-002564c97630")
        static Object getValue(TemplateParameterSubstitution el) {
            ModelElement r1 = el.getActual();
            if (r1 != null)
                return r1;
            return el.getValue();
        }

        @objid ("8f8d990c-c068-11e1-8c0a-002564c97630")
        static void setValue(TemplateParameterSubstitution el, Object value) {
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

        /**
         * Get the template parameter substitution for the given template parameter.
         * Looks for the corresponding TemplateParameterSubstitution and get its instantiated value.
         * If no corresponding TemplateParameterSubstitution is found, return the default value on theEditedElement TemplateParameter.
         * @param theEditedElement the template binding
         * @param param the template parameter
         * @return the template parameter substituted value/type, or the template parameter default value/type
         */
        @objid ("8f8d9912-c068-11e1-8c0a-002564c97630")
        public static Object getValue(TemplateBinding theEditedElement, TemplateParameter param) {
            for (TemplateParameterSubstitution sub : theEditedElement.getParameterSubstitution()) {
                if (param.equals(sub.getFormalParameter()))
                    return getValue(sub);
            }
                        
            ModelElement defaultType = param.getDefaultType();
            if (defaultType != null)
                return defaultType;
            return param.getDefaultValue();
        }

        /**
         * Set the template parameter substitution for the given template parameter.
         * Looks for the corresponding TemplateParameterSubstitution and sets its instantiated value.
         * If no corresponding TemplateParameterSubstitution is found, it is created.
         * @param theEditedElement the template biding to change
         * @param param the template parameter that must be instantiated
         * @param value the instantiated value. May be a String or an ObModelelement
         * @param mmService the model service
         */
        @objid ("8f8d991d-c068-11e1-8c0a-002564c97630")
        public static void setValue(TemplateBinding theEditedElement, TemplateParameter param, Object value, IMModelServices mmService) {
            // Ensure all substitutions are here
            updateTemplateBinding(theEditedElement, mmService);
                        
            // Find the right substitution and update it
            for (TemplateParameterSubstitution sub : theEditedElement.getParameterSubstitution()) {
                if (param.equals(sub.getFormalParameter())) {
                    setValue(sub, value);
                    return;
                }
            }
        }

        @objid ("9aa89c03-2412-410d-93b2-051a0f98ad55")
        @Override
        public boolean acceptStringValue() {
            return true;
        }

    }

}
