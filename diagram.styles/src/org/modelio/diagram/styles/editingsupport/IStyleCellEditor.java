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
                                    

package org.modelio.diagram.styles.editingsupport;

import javax.swing.CellEditor;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("85a496dd-1926-11e2-92d2-001ec947c8cc")
public interface IStyleCellEditor {
    @objid ("85a496de-1926-11e2-92d2-001ec947c8cc")
    boolean canEdit(Object element);

    @objid ("85a6f8e0-1926-11e2-92d2-001ec947c8cc")
    CellEditor getCellEditor(Object element);

    @objid ("85a6f8e3-1926-11e2-92d2-001ec947c8cc")
    Object getValue(Object element);

    @objid ("85a6f8e6-1926-11e2-92d2-001ec947c8cc")
    void setValue(Object element, Object value);

}
