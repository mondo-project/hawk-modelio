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
                                    

package org.modelio.property.ui.data.standard.common;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.metamodel.uml.infrastructure.Element;

/**
 * Generic abstract data model for the property view.
 * <p>
 * This class provides a field for the edited element, as well as a default implementation for {@link IPropertyModel#isEditable(int, int)}.
 */
@objid ("8d581b67-c068-11e1-8c0a-002564c97630")
public abstract class AbstractPropertyModel<T extends Element> implements IPropertyModel {
    /**
     * The <i>Element</i> that corresponds to this data model.
     */
    @objid ("235defcd-cb5b-11e1-9165-002564c97630")
    protected final T theEditedElement;

    /**
     * Basic implementation of isEditable.
     * @param row the row index from the table.
     * @param col the column index from the table.
     * @return <code>false</code> when col = 0 or the edited element is not modifiable.
     */
    @objid ("8d581b68-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isEditable(int row, int col) {
        if (col == 0) {
            return false;
        }
        return this.theEditedElement.isModifiable();
    }

    @objid ("0a5a4033-cb5b-11e1-9165-002564c97630")
    protected AbstractPropertyModel(T editedElement) {
        this.theEditedElement = editedElement;
    }

}
