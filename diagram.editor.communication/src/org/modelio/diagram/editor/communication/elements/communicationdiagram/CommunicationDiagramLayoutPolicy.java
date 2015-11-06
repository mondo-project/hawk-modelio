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
                                    

package org.modelio.diagram.editor.communication.elements.communicationdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.diagram.elements.core.policies.DiagramEditLayoutPolicy;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * LayoutPolicy that is specific to Communication Diagram background
 */
@objid ("7a299d1b-55b6-11e2-877f-002564c97630")
public class CommunicationDiagramLayoutPolicy extends DiagramEditLayoutPolicy {
    @objid ("7a299d1f-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        return new DefaultNodeResizableEditPolicy();
    }

    @objid ("7a299d25-55b6-11e2-877f-002564c97630")
    @Override
    protected MObject getHostElement() {
        return super.getHostElement();
    }

}