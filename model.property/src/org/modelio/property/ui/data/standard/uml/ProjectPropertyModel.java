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
import org.modelio.metamodel.mda.Project;
import org.modelio.property.ui.data.standard.common.AbstractPropertyModel;

@objid ("8f62c052-c068-11e1-8c0a-002564c97630")
public class ProjectPropertyModel extends AbstractPropertyModel<Project> {
    @objid ("a7e45f28-c068-11e1-8c0a-002564c97630")
    private static final String[] PROPERTIES = new String[] {"Project", "Name"};

    @objid ("8f62c05a-c068-11e1-8c0a-002564c97630")
    private StringType labelStringType = null;

    @objid ("8f62c05b-c068-11e1-8c0a-002564c97630")
    private StringType stringType = null;

    /**
     * Instantiate the project properties view.
     * @param theEditedElement the current project.
     */
    @objid ("8f62c05c-c068-11e1-8c0a-002564c97630")
    public ProjectPropertyModel(Project theEditedElement) {
        super(theEditedElement);
        
        this.labelStringType = new StringType(false);
        this.stringType = new StringType(true);
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getColumnNumber()
     */
    @objid ("8f62c062-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnNumber() {
        return 2;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getRowsNumber()
     */
    @objid ("8f62c068-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowsNumber() {
        return ProjectPropertyModel.PROPERTIES.length;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getTypeAt(int, int)
     */
    @objid ("8f62c06e-c068-11e1-8c0a-002564c97630")
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

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#getValueAt(int, int)
     */
    @objid ("8f62c075-c068-11e1-8c0a-002564c97630")
    @Override
    public Object getValueAt(int row, int col) {
        switch (col) {
        case 0: // col 0 is the property key
            return ProjectPropertyModel.PROPERTIES[row];
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

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#isEditable(int, int)
     */
    @objid ("8f62c07c-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        return false;
    }

    /**
     * (non-Javadoc)
     * @see com.modeliosoft.modelio.edition.property.model.uml.PropertyModel#setValueAt(int, int, java.lang.Object)
     */
    @objid ("8f62c084-c068-11e1-8c0a-002564c97630")
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
