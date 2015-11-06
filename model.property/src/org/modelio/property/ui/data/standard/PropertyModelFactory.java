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
                                    

package org.modelio.property.ui.data.standard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableModel;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.property.ui.data.standard.common.NoDataModel;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("8ebeb051-c068-11e1-8c0a-002564c97630")
public class PropertyModelFactory {
    @objid ("8ebeb052-c068-11e1-8c0a-002564c97630")
    public KTableModel getIPropertyModel(ICoreSession session, IModelioPickingService pickingService, KTable table, IPropertyModel data) {
        if (data == null) {
            return new NoDataModel(table);
        } else {
            return new StandardKModel(session, pickingService, table, data);
        }
    }

}
