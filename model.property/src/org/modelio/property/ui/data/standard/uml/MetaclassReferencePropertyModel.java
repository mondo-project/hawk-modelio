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
import org.modelio.metamodel.uml.infrastructure.MetaclassReference;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

@objid ("8f489184-c068-11e1-8c0a-002564c97630")
public class MetaclassReferencePropertyModel extends AbstractPropertyModel<MetaclassReference> {
    @objid ("a7872bc6-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"MetaclassReference", "ReferencedClass"};

    @objid ("8f48918c-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f48918d-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    /**
     * Instantiate the profile properties view.
     * @param theEditedElement the current profile.
     */
    @objid ("8f48918e-c068-11e1-8c0a-002564c97630")
    public MetaclassReferencePropertyModel(MetaclassReference theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    @objid ("8f4af288-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("8f4af28d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return MetaclassReferencePropertyModel.PROPERTIES.length;
    }

    @objid ("8f4af292-c068-11e1-8c0a-002564c97630")
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

    @objid ("8f4af298-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return MetaclassReferencePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getReferencedClassName();
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8f4af29e-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        return false;
    }

    @objid ("8f4af2a5-c068-11e1-8c0a-002564c97630")
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
                return;
            default:
                return;
            }
        default:
            return;
        }
    }

}
