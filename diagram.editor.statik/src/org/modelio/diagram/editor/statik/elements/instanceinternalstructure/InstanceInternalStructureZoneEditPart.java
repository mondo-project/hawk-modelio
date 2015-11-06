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
                                    

package org.modelio.diagram.editor.statik.elements.instanceinternalstructure;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.elements.common.freezone.GmFreeZoneEditPart;

/**
 * {@link GmInstanceInternalStructureZone} edit part.
 * <p>
 * Installs a custom layout policy.
 */
@objid ("355591b2-55b7-11e2-877f-002564c97630")
public class InstanceInternalStructureZoneEditPart extends GmFreeZoneEditPart {
    @objid ("355591b6-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new InstanceZoneLayoutEditPolicy());
    }

}
