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
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>Dependency</i> data model.
 * <p>
 * This class provides the list of properties for the <i>Dependency</i> metaclass.
 */
@objid ("8f004d4f-c068-11e1-8c0a-002564c97630")
public class DependencyPropertyModel extends AbstractPropertyModel<Dependency> {
    /**
     * Properties to display for <i>Dependency</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li> for the first row the value is the table header label (usually the metaclass name)
     * <li> for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a6dbc8d5-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Dependency", "Name", "DependsOn"};

    @objid ("8f004d5a-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f004d5b-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f004d5c-c068-11e1-8c0a-002564c97630")
    private SingleElementType dependsOnType = null;

    /**
     * Create a new <i>Dependency</i> data model from an <i>Dependency</i>.
     * @param model
     */
    @objid ("8f004d5d-c068-11e1-8c0a-002564c97630")
    public DependencyPropertyModel(Dependency theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        
        this.dependsOnType = new SingleElementType(true, ModelElement.class, CoreSession.getSession(this.theEditedElement));
        this.dependsOnType.setElementFilter(new DependsOnTypeFilter());
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8f004d63-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8f01d3c5-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return DependencyPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8f01d3ca-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return DependencyPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
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
    @objid ("8f01d3d0-c068-11e1-8c0a-002564c97630")
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
    @objid ("8f01d3d6-c068-11e1-8c0a-002564c97630")
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

    @objid ("8f01d3dc-c068-11e1-8c0a-002564c97630")
    protected static class DependsOnTypeFilter implements IMObjectFilter {
        @objid ("8f01d3dd-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean accept(final MObject element) {
            if (element instanceof ModelElement) {
                ModelElement type = (ModelElement) element;
            
                if (type instanceof Project) {
                    return false;
                } else if (type instanceof Package && (type.getName().equals("_predefinedTypes") 
                                                  || type.getName().equals("_S_PredefinedTypes"))) {
                    return false;
                    // TODO CHM use predefined types
                } else if (type.getName().equals("undefined")) {
                    return false;
                } else {
                    return true;
                }
            } 
            // else
            return false;
        }

    }

}
