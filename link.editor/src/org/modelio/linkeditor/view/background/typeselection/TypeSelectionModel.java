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
                                    

package org.modelio.linkeditor.view.background.typeselection;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("1b9865ad-5e33-11e2-b81d-002564c97630")
public class TypeSelectionModel {
    /**
     * A list of all the types that are possible to create.
     */
    @objid ("77143107-35f5-40f8-9f4d-2dcc987d055c")
    private List<Object> allowedTypes;

    @objid ("2d2b4b5d-62da-483b-8d6a-77b4ce7984f1")
    private Object selectedType;

    @objid ("1b9865b3-5e33-11e2-b81d-002564c97630")
    public TypeSelectionModel(final List<Object> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    @objid ("1b9865b9-5e33-11e2-b81d-002564c97630")
    public List<Object> getAllowedTypes() {
        return this.allowedTypes;
    }

    @objid ("1b9865bf-5e33-11e2-b81d-002564c97630")
    public void setAllowedTypes(final List<Object> allowedTypes) {
        this.allowedTypes = allowedTypes;
    }

    @objid ("1b9865c5-5e33-11e2-b81d-002564c97630")
    public Object getSelectedType() {
        return this.selectedType;
    }

    @objid ("1b9865c9-5e33-11e2-b81d-002564c97630")
    public void setSelectedType(final Object selectedType) {
        this.selectedType = selectedType;
    }

}
