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
                                    

package org.modelio.diagram.elements.common.portcontainer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.modelio.diagram.elements.core.node.IDragTrackerProvider;

/**
 * DragTrackerProvider specific to satellites. The returned DragTracker never allows reparenting of the concerned
 * EditPart.
 * 
 * @author fpoyer
 */
@objid ("7f05067c-1dec-11e2-8cad-001ec947c8cc")
public class SatelliteDragTrackerProvider implements IDragTrackerProvider {
    @objid ("64834c04-1e83-11e2-8cad-001ec947c8cc")
    private EditPart editPart;

    /**
     * Constructor.
     * @param editPart the EditPart for which DragTracker are to be returned.
     */
    @objid ("7f050681-1dec-11e2-8cad-001ec947c8cc")
    public SatelliteDragTrackerProvider(EditPart editPart) {
        this.editPart = editPart;
    }

    @objid ("7f050687-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public DragTracker getDragTracker(Request request) {
        return new SatelliteDragEditPartsTracker(this.editPart);
    }

}
