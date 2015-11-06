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
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.TagType;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

@objid ("8f88d674-c068-11e1-8c0a-002564c97630")
public class TagTypePropertyModel extends AbstractPropertyModel<TagType> {
    @objid ("a8617e6b-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"TagType", "Name", "Label", "ParamNumber", "IsHidden", "BelongToPrototype"};

    @objid ("8f88d675-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    @objid ("8f88d67d-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f88d67e-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    /**
     * Instantiate the tag type properties view.
     * @param theEditedElement the current tag type.
     */
    @objid ("8f88d67f-c068-11e1-8c0a-002564c97630")
    public TagTypePropertyModel(TagType theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        this.booleanType = new BooleanType();
    }

    @objid ("8f88d685-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    @objid ("8f88d68a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return TagTypePropertyModel.PROPERTIES.length;
    }

    @objid ("8f88d68f-c068-11e1-8c0a-002564c97630")
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
                return this.stringType;
            case 3:
                return this.stringType;
            case 4:
                return this.booleanType;
            case 5:
                return this.booleanType;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8f88d695-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return TagTypePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getLabelKey();
            case 3:
                return this.theEditedElement.getParamNumber();
            case 4:
                return this.theEditedElement.isIsHidden()?Boolean.TRUE:Boolean.FALSE;
            case 5:
                return this.theEditedElement.isBelongToPrototype()?Boolean.TRUE:Boolean.FALSE;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    @objid ("8f88d69b-c068-11e1-8c0a-002564c97630")
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
            case 2:
                this.theEditedElement.setLabelKey((String)value);
                return;
            case 3:
                this.theEditedElement.setParamNumber((String)value);
                return;
            case 4:
                this.theEditedElement.setIsHidden(((Boolean) value).booleanValue());
                return;
            case 5:
                this.theEditedElement.setBelongToPrototype(((Boolean) value).booleanValue());
                return;
            default:
                return;
            }
        default:
            return;
        }
    }

}
