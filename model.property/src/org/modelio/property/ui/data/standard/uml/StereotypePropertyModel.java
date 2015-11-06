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
import org.modelio.core.ui.ktable.types.bool.BooleanType;
import org.modelio.core.ui.ktable.types.element.SingleElementType;
import org.modelio.core.ui.ktable.types.list.ListType;
import org.modelio.core.ui.ktable.types.text.StringType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * PropertyDefinition model for {@link Stereotype}.
 */
@objid ("8f8413d3-c068-11e1-8c0a-002564c97630")
public class StereotypePropertyModel extends AbstractPropertyModel<Stereotype> {
    @objid ("a842f9e8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Stereotype", "Name", "Label", "BaseClass", "IsHidden", "ParentStereotype", "Icon", "DiagramImage"};

    @objid ("8f8413dc-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f8413dd-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    @objid ("8f8413de-c068-11e1-8c0a-002564c97630")
    private ListType baseClassType = null;

    @objid ("8f8413df-c068-11e1-8c0a-002564c97630")
    private SingleElementType parentStereotypeType = null;

    @objid ("8f8413e0-c068-11e1-8c0a-002564c97630")
    private BooleanType booleanType = null;

    /**
     * Instantiate the stereotype properties view.
     * @param model element finder structure.
     * @param theEditedElement the current stereotype.
     */
    @objid ("8f8413e1-c068-11e1-8c0a-002564c97630")
    public StereotypePropertyModel(Stereotype theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
        
        List<String> metaclasses = new ArrayList<>();
        metaclasses.add("");
        
        // Get all metaclasses inheriting ModelElement
        for (MClass metaclass : SmClass.getClass(ModelElement.class).getSub(true)) {
            metaclasses.add(metaclass.getName());
        }
        
        // Remove a few specific metaclass we do not want to appear
        metaclasses.remove("Module");
        metaclasses.remove("Profile");
        metaclasses.remove("Project");
        metaclasses.remove("TagType");
        metaclasses.remove("NoteType");
        metaclasses.remove("Stereotype");
        metaclasses.remove("ConfigParam");
        
        this.baseClassType = new ListType(true, metaclasses);
        
        this.parentStereotypeType = new SingleElementType(true, Stereotype.class, CoreSession.getSession(this.theEditedElement));
        
        this.booleanType = new BooleanType();
    }

    /**
     * (non-Javadoc)
     * @see AbstractPropertyModel#getColumnNumber()
     */
    @objid ("8f8674e8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * (non-Javadoc)
     * @see AbstractPropertyModel#getRowsNumber()
     */
    @objid ("8f8674ee-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return StereotypePropertyModel.PROPERTIES.length;
    }

    /**
     * (non-Javadoc)
     * @see AbstractPropertyModel#getTypeAt(int, int)
     */
    @objid ("8f8674f4-c068-11e1-8c0a-002564c97630")
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
                return this.baseClassType;
            case 4:
                return this.booleanType;
            case 5:
                return this.parentStereotypeType;
            case 6:
                return this.stringType;
            case 7:
                return this.stringType;
            default:
                return null;
            }
        default:
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * @see AbstractPropertyModel#getValueAt(int, int)
     */
    @objid ("8f8674fb-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return StereotypePropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1:
                return this.theEditedElement.getName();
            case 2:
                return this.theEditedElement.getLabelKey();
            case 3:
                return this.theEditedElement.getBaseClassName();
            case 4:
                return this.theEditedElement.isIsHidden()?Boolean.TRUE:Boolean.FALSE;
            case 5:
                return this.theEditedElement.getParent();
            case 6:
                return this.theEditedElement.getIcon();
            case 7:
                return this.theEditedElement.getImage();
            default:
                return null;
            }
        default:
            return null;
        }
    }

    /**
     * (non-Javadoc)
     * @see AbstractPropertyModel#setValueAt(int, int, java.lang.Object)
     */
    @objid ("8f867502-c068-11e1-8c0a-002564c97630")
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
                this.theEditedElement.setBaseClassName((String)value);
                return;
            case 4:
                this.theEditedElement.setIsHidden(((Boolean) value).booleanValue());
                return;
            case 5:
                this.theEditedElement.setParent((Stereotype)value);
                return;
            case 6:
                this.theEditedElement.setIcon((String)value);
                return;
            case 7:
                this.theEditedElement.setImage((String)value);
                return;
            default:
                return;
            }
        default:
            return;
        }
    }

}
