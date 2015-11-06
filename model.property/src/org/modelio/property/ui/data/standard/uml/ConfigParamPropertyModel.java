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
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.mda.ModuleParameter;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * This class provide the data to display config parameter properties.
 */
@objid ("8eef8456-c068-11e1-8c0a-002564c97630")
public class ConfigParamPropertyModel extends AbstractPropertyModel<ModuleParameter> {
    @objid ("a6ba758d-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"ConfigParam", "Name"};

    @objid ("8eef845f-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8eef8460-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    /**
     * Instantiate the profile properties view.
     * @param theEditedElement the current profile.
     */
    @objid ("8eef8461-c068-11e1-8c0a-002564c97630")
    public ConfigParamPropertyModel(ModuleParameter theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    @objid ("8eef8467-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("8eef846c-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ConfigParamPropertyModel.PROPERTIES.length;
    }

    @objid ("8eef8471-c068-11e1-8c0a-002564c97630")
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
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8eef8477-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ConfigParamPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8eef847d-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setName((String)value);
                return;
            default:
                return;
            }
        default:
            return;
        }
    }

}
