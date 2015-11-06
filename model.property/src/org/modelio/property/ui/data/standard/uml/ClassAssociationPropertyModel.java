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
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.ClassAssociation;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * <i>ClassAssociation</i> data model.
 * <p>
 * This class provides the list of properties for the <i>ClassAssociation</i> metaclass.
 */
@objid ("8edebb84-c068-11e1-8c0a-002564c97630")
public class ClassAssociationPropertyModel extends AbstractPropertyModel<ClassAssociation> {
    /**
     * Properties to display for <i>ClassAssociation</i>.
     * <p>
     * This array contains the first column values:
     * <ul>
     * <li>for the first row the value is the table header label (usually the metaclass name)
     * <li>for otheEditedElement rows the values usually match the meta-attributes and roles names of the metaclass
     * </ul>
     */
    @objid ("a696c0e8-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] { "ClassAssociation", "ClassPart" };

    @objid ("8edebb8f-c068-11e1-8c0a-002564c97630")
    private SingleElementType classType = null;

    @objid ("fa03e338-c5d4-11e1-8f21-002564c97630")
    private final StringType labelStringType;

    /**
     * Create a new <i>ClassAssociation</i> data model from an <i>ClassAssociation</i>.
     * @param model
     */
    @objid ("8edebb90-c068-11e1-8c0a-002564c97630")
    public ClassAssociationPropertyModel(ClassAssociation theEditedElement, IModel model) {
        super(theEditedElement);
        
        this.classType = new SingleElementType(false, Class.class, CoreSession.getSession(this.theEditedElement));
        this.classType.setElementFilter(new AssociationClassTypeFilter());
        
        this.labelStringType = new StringType(false);
    }

    /**
     * The number of columns that the properties table must display.
     * @return the number of columns
     */
    @objid ("8edebb96-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * The number of rows that the properties table must display.
     * @return the number of rows
     */
    @objid ("8edebb9c-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ClassAssociationPropertyModel.PROPERTIES.length;
    }

    /**
     * Return the value that will be displayed at the specified row and column.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the value corresponding to the row and column
     */
    @objid ("8edebba2-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ClassAssociationPropertyModel.PROPERTIES[row];
        case 1: // col 1 is the property value
            switch (row) {
            case 0: // Header
                return "Value";
            case 1: // Class part
                return this.theEditedElement.getClassPart();
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
     * This type will be used to choose an editor and a renderer for each cell of the properties table.
     * <p>
     * The first column contains the properties names.
     * @param row the row number
     * @param col the column number
     * @return the type of the element corresponding to the row and column
     */
    @objid ("8ee04206-c068-11e1-8c0a-002564c97630")
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
                return this.classType;
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
    @objid ("8ee0420d-c068-11e1-8c0a-002564c97630")
    @Override
    public void setValueAt(int row, int col, Object value) {
        switch (col) {
        case 0: // Keys cannot be modified
            return;
        case 1: // col 1 is the property value
            switch (row) {
            case 0:
                return; // Header cannot be modified
            case 1: // Class part
                this.theEditedElement.setClassPart((Class) value);
                break;
            default:
                return;
            }
            break;
        default:
            return;
        }
    }

    @objid ("8ee04214-c068-11e1-8c0a-002564c97630")
    protected class AssociationClassTypeFilter implements IMObjectFilter {
        @objid ("8ee04215-c068-11e1-8c0a-002564c97630")
        @Override
        public boolean accept(final MObject element) {
            Class type = (Class) element;
            
            for (AssociationEnd end : ClassAssociationPropertyModel.this.theEditedElement.getAssociationPart().getEnd()) {
                if (type.equals(end.getSource())) {
                    return false;
                }
            }
            return true;
        }

    }

}
