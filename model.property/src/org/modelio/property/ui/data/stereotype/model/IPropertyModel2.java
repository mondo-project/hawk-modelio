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
                                    

package org.modelio.property.ui.data.stereotype.model;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.property.ui.data.stereotype.body.IPropertyStyle;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("5304e2ff-10d4-4506-b8f1-472ba79de1bb")
public interface IPropertyModel2 {
    @objid ("281e0fbc-610b-4790-a18c-510315e9ffbf")
    MObject getEditedElement();

    @objid ("4cc4b4db-3029-495d-ab4d-4ae829dfa5ae")
    void updateStyle(int row, int col, IPropertyStyle newStyle);

    @objid ("4f9ce9df-8aed-4ace-b58f-09a695f9dac0")
    Class<?> getValueTypeAt(int row, int col);

    @objid ("05d79835-2758-4554-a12a-aead50736ede")
    List<String> getPossibleValues(int row, int col);

    @objid ("6e8cff52-e42b-43da-b8d3-b6298c720861")
    int getColumnNumber();

    @objid ("931d0872-38ad-4db7-b1d6-26bbe28e2c30")
    int getRowsNumber();

    @objid ("4a99eda5-66d6-48c7-aa00-157d8cf2a9e2")
    Object getValueAt(int row, int col);

    @objid ("c87f229a-9a60-4221-a1b7-99da82d29b73")
    void setValueAt(int row, int col, Object value);

    @objid ("07df8a08-a0d3-4536-bc46-27ef0dfabbab")
    boolean isEditable(int row, int col);

}
