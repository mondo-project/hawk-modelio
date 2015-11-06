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
                                    

package org.modelio.diagram.editor.state.elements.fork;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.modelio.diagram.editor.state.elements.ForkJoinOrientation;
import org.modelio.diagram.elements.common.portcontainer.AutoSizeEditPolicy;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.common.portcontainer.PortContainerEditPart;

/**
 * Edit part for {@link GmForkState}.
 * 
 * @author fpoyer
 */
@objid ("f5300f68-55b6-11e2-877f-002564c97630")
public class ForkStateSatelliteContainerEditPart extends PortContainerEditPart {
    @objid ("f5300f6c-55b6-11e2-877f-002564c97630")
    @Override
    public SelectionEditPolicy getPreferredDragRolePolicy(String requestType) {
        if (requestType.equals(REQ_RESIZE)) {
            GmPortContainer pc = (GmPortContainer) this.getModel();
            GmForkStatePrimaryNode fork = (GmForkStatePrimaryNode) pc.getMainNode();
            ForkJoinOrientation orientation = (ForkJoinOrientation) fork.getStyle()
                                                                        .getProperty(GmForkStateStructuredStyleKeys.ORIENTATION);
            AutoSizeEditPolicy resizablePolicy = new AutoSizeEditPolicy();
            if (orientation.equals(ForkJoinOrientation.VERTICAL))
                resizablePolicy.setResizeDirections(PositionConstants.NORTH_SOUTH);
            else
                resizablePolicy.setResizeDirections(PositionConstants.EAST_WEST);
            return resizablePolicy;
        }
        return super.getPreferredDragRolePolicy(requestType);
    }

}
