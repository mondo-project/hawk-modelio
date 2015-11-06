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
                                    

package org.modelio.core.ui.ktable.types;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.modelio.app.core.picking.IModelioPickingService;

@objid ("8deda4a5-c068-11e1-8c0a-002564c97630")
public interface IPropertyType {
    @objid ("8deda4a6-c068-11e1-8c0a-002564c97630")
    boolean acceptNullValue();

    @objid ("0a8a521c-cb5b-11e1-9165-002564c97630")
    DefaultCellRenderer getRenderer();

    @objid ("0a8a521e-cb5b-11e1-9165-002564c97630")
    KTableCellEditor getEditor(IModelioPickingService pickingService);

}
