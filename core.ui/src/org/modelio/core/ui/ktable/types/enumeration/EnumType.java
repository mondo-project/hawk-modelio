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
                                    

package org.modelio.core.ui.ktable.types.enumeration;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTableCellEditor;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.ktable.types.PropertyType;

@objid ("8dea977b-c068-11e1-8c0a-002564c97630")
public class EnumType extends PropertyType {
    @objid ("8dea977c-c068-11e1-8c0a-002564c97630")
    private Class<? extends Enum<? >> theEnumeration = null;

    @objid ("8dea977f-c068-11e1-8c0a-002564c97630")
    public EnumType(Class<? extends Enum<? >> theEnumeration) {
        super(false);
        this.theEnumeration = theEnumeration;
    }

    @objid ("8dea9785-c068-11e1-8c0a-002564c97630")
    public Class<? extends Enum<? >> getTheEnumeration() {
        return this.theEnumeration;
    }

    @objid ("0a76f0ad-cb5b-11e1-9165-002564c97630")
    @Override
    public KTableCellEditor getEditor(IModelioPickingService pickingService) {
        return new EnumCellEditor(getTheEnumeration());
    }

}
