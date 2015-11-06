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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.mda.Project;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Usage;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>Usage</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Usage</i> metaclass.
 */
@objid ("8f9e42b9-c068-11e1-8c0a-002564c97630")
public class UsagePropertyModel extends AbstractPropertyModel<Usage> {
    /**
     * Properties to display for <i>Usage</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a8818988-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Usage", "DependsOn"};

    @objid ("8f9e42c4-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f9e42c5-c068-11e1-8c0a-002564c97630")
    private SingleElementType dependsOnType = null;

    /**
     * Create a new <i>Usage</i> data model from an <i>Usage</i>.
     * @param model
     */
    @objid ("8f9e42c6-c068-11e1-8c0a-002564c97630")
    public UsagePropertyModel(Usage theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        
        this.dependsOnType = new SingleElementType(false, ModelElement.class, CoreSession.getSession(this.theEditedElement));
        this.dependsOnType.setElementFilter(new DependsOnFilter());
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f9e42cc-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f9e42d1-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return UsagePropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f9e42d6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return UsagePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getDependsOn();
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
    @objid ("8f9e42dc-c068-11e1-8c0a-002564c97630")
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
                return this.dependsOnType;
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
    @objid ("8f9e42e2-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setDependsOn((ModelElement) value);
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    @objid ("8f9e42e8-c068-11e1-8c0a-002564c97630")
    protected static class DependsOnFilter implements IMObjectFilter {
////private PredefinedTypes predefinedTypes = null;
        @objid ("8f9e42e9-c068-11e1-8c0a-002564c97630")
        public DependsOnFilter() {
            /*CoreSession modelingSession = ModelProperty.getInstance().getModelingSession();
            
            if (modelingSession != null) {
                this.predefinedTypes = modelingSession.getModel().getPredefinedTypes();
            }*/
        }

        @objid ("8f9e42ec-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean accept(final MObject element) {
            // TODO CHM predefined types
            if (element instanceof ModelElement) {
                ModelElement type = (ModelElement) element;
            
                if (type instanceof Project) {
                    return false;
                } else if (type instanceof Package && (type.getName().equals("_predefinedTypes") 
                                                  || type.getName().equals("_S_PredefinedTypes"))) {
                    return false;
                } else if (type.getName().equals("undefined")) {
                    return false;
                } else {
                    return true;
                }
            }
            return false;
        }

    }

}
