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
                                    

package org.modelio.core.ui.ktable;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.IPropertyType;

@objid ("8dd3b437-c068-11e1-8c0a-002564c97630")
public interface IPropertyModel {
    @objid ("8dd3b438-c068-11e1-8c0a-002564c97630")
    int getColumnNumber();

    @objid ("8dd3b43a-c068-11e1-8c0a-002564c97630")
    int getRowsNumber();

    @objid ("8dd3b43c-c068-11e1-8c0a-002564c97630")
    Object getValueAt(int row, int col);

    @objid ("8dd3b440-c068-11e1-8c0a-002564c97630")
    void setValueAt(int row, int col, Object value);

    @objid ("8dd3b444-c068-11e1-8c0a-002564c97630")
    IPropertyType getTypeAt(int row, int col);

    @objid ("8dd53aa8-c068-11e1-8c0a-002564c97630")
    boolean isEditable(int row, int col);

}
