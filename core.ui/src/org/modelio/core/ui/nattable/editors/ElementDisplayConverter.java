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
                                    

package org.modelio.core.ui.nattable.editors;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.nattable.data.convert.DisplayConverter;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("f388e6c6-3e1b-42f9-8254-bf56166694bd")
public class ElementDisplayConverter extends DisplayConverter {
    @objid ("799215ce-1bd5-4229-ba35-c29bdee98dbd")
    @Override
    public Object canonicalToDisplayValue(Object canonicalValue) {
        final MRef mRef = (MRef) canonicalValue;
        return (mRef != null) ? mRef.name : "";
    }

    @objid ("c399e14f-6955-40a6-ac07-79e151b599d3")
    @Override
    public Object displayToCanonicalValue(Object displayValue) {
        return displayValue;
    }

}
