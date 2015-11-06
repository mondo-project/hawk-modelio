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
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

@objid ("8f097506-c068-11e1-8c0a-002564c97630")
public class EmptyDataModel extends AbstractPropertyModel<Element> {
    @objid ("8f09750a-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f09750b-c068-11e1-8c0a-002564c97630")
    public EmptyDataModel(Element theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
    }

    @objid ("8f097510-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("8f097515-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return 1;
    }

    @objid ("8f09751a-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            if (this.theEditedElement != null) {
                return this.theEditedElement.getMClass().getName();
            }
            return "Null";
        case 1: // col 1 is the property value
            return "Value";
        default:
            return null;
        }
    }

    @objid ("8f097520-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        // Nothing to do.
    }

    @objid ("8f097526-c068-11e1-8c0a-002564c97630")
    @Override
    public IPropertyType getTypeAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key type
            return this.labelStringType;
        case 1: // col 1 is the property value type
            return this.labelStringType;
        default:
            return null;
        }
    }

    @objid ("8f0afb86-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        return false;
    }

}
