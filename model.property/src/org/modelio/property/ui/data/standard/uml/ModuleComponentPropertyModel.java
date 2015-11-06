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
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * The properties model of the Profile element.
 */
@objid ("8f4af2ae-c068-11e1-8c0a-002564c97630")
public class ModuleComponentPropertyModel extends AbstractPropertyModel<ModuleComponent> {
    @objid ("a7872bd4-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "Module", "Name" };

    @objid ("8f4af2b7-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f4af2b8-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    /**
     * Instantiate the project properties view.
     * @param theEditedElement the current project.
     */
    @objid ("8f4af2ba-c068-11e1-8c0a-002564c97630")
    public ModuleComponentPropertyModel(ModuleComponent theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    @objid ("8f4af2c0-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("8f4af2c5-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ModuleComponentPropertyModel.PROPERTIES.length;
    }

    @objid ("8f4af2ca-c068-11e1-8c0a-002564c97630")
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

    @objid ("8f4af2d0-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
            case 0: // col 0 is the property key
                return ModuleComponentPropertyModel.PROPERTIES[row];
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

    @objid ("8f4af2d6-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            return false;
        }
        String moduleName = this.theEditedElement.getName();
        
        if (row == 1) {
            if (moduleName != null && moduleName.equals("LocalModule")) {
                return false;
            }
            return super.isEditable(row, col);
        }
        // else
        return false;
    }

    @objid ("8f4af2dd-c068-11e1-8c0a-002564c97630")
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
                    default:
                        return;
                }
            default:
                return;
        }
    }

}
