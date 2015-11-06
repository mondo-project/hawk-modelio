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
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

/**
 * The properties model of the Profile element.
 */
@objid ("8f605f20-c068-11e1-8c0a-002564c97630")
public class ProfilePropertyModel extends AbstractPropertyModel<Profile> {
    @objid ("a7e2d886-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Profile", "Name"};

    @objid ("8f605f29-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f605f2a-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    /**
     * Instantiate the profile properties view.
     * @param theEditedElement the current profile.
     */
    @objid ("8f605f2b-c068-11e1-8c0a-002564c97630")
    public ProfilePropertyModel(Profile theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    @objid ("8f605f31-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("8f605f36-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ProfilePropertyModel.PROPERTIES.length;
    }

    @objid ("8f605f3b-c068-11e1-8c0a-002564c97630")
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

    @objid ("8f605f41-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ProfilePropertyModel.PROPERTIES[row];
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

    @objid ("8f605f47-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            return false;
        }
        String profileName = this.theEditedElement.getName();
        if (profileName != null && profileName.equals("LocalProfile")) {
            return false;
        }
        return super.isEditable(row, col);
    }

    @objid ("8f62c04a-c068-11e1-8c0a-002564c97630")
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
