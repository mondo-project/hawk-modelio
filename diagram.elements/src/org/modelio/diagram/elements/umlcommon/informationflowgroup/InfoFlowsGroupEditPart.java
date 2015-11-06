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
                                    

package org.modelio.diagram.elements.umlcommon.informationflowgroup;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.elements.common.group.GmGroupEditPart;

/**
 * Abstract edit policy for {@link GmInfoFlowsGroup}.
 * 
 * @author cmarin
 */
@objid ("81734c3d-1dec-11e2-8cad-001ec947c8cc")
public class InfoFlowsGroupEditPart extends GmGroupEditPart {
    /**
     * The group is moveable.
     */
    @objid ("81734c3f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean isSelectable() {
        return true;
    }

    @objid ("81734c45-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected final void createEditPolicies() {
        super.createEditPolicies();
        
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new InfoFlowsGroupCreatePolicy());
    }

}
