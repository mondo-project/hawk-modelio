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

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * Specific Command that make the port container try to resize itself so that it contains all children. This has to be
 * done at the very last moment, since some children might change their mind between the moment the command is emitted
 * and the moment it is actually executed (think ExpansionNode which is moved from a vertical border to an horizontal
 * border).
 */
@objid ("7eeaccc0-1dec-11e2-8cad-001ec947c8cc")
public class LastMinuteContainerAutoResizeCommand extends Command {
    @objid ("0dd1d23d-f9ee-4ce5-9ee6-a76bca956bc1")
    private EditPart host;

    /**
     * C'tor.
     * @param host the host on which to apply the autoresize.
     */
    @objid ("7eeaccc7-1dec-11e2-8cad-001ec947c8cc")
    public LastMinuteContainerAutoResizeCommand(EditPart host) {
        this.host = host;
    }

    @objid ("7eeacccd-1dec-11e2-8cad-001ec947c8cc")
    private EditPart getHost() {
        return this.host;
    }

    @objid ("7eeaccd3-1dec-11e2-8cad-001ec947c8cc")
    private IFigure getHostFigure() {
        return ((GraphicalEditPart) this.host).getFigure();
    }

    @objid ("7eeaccd9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void execute() {
        // Container might need to resize itself to contain the new bounds
        // of the child:
        // 0 - container may need to be layouted itslef
        if (((PortContainerEditPart) this.host).dirty) {
            getHostFigure().getUpdateManager().performValidation();
        }
        // 1 - Get current bounds of container in absolute coordinates.
        IFigure containerFigure = getHostFigure();
        Rectangle oldContainerBounds = containerFigure.getBounds().getCopy();
        containerFigure.translateToAbsolute(oldContainerBounds);
        
        // 2 - given the child new constraint, compute the "updated" bounds of
        // the container, in absolute coordinates
        PortContainerLayout portContainerLayout = (PortContainerLayout) containerFigure.getLayoutManager();
        Rectangle updatedContainerBounds = portContainerLayout.getPreferredBounds(containerFigure);
        containerFigure.translateToAbsolute(updatedContainerBounds);
        
        // 3 - Compute the difference between old and updated bounds, and
        // request a resize of container to its parent. Return corresponding
        // command.
        // Note: let's just hope said parent does accept RESIZE_CHILDREN
        // requests... :/
        ChangeBoundsRequest req = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE_CHILDREN);
        req.setEditParts(getHost());
        final Point moveDelta = new Point(updatedContainerBounds.x - oldContainerBounds.x,
                                          updatedContainerBounds.y - oldContainerBounds.y);
        req.setMoveDelta(moveDelta);
        req.setSizeDelta(new Dimension(updatedContainerBounds.width - oldContainerBounds.width,
                                       updatedContainerBounds.height - oldContainerBounds.height));
        // In case parent is a port container itself, it might need to know the modification of the handle bounds
        Map<String, Object> map = new HashMap<>();
        Rectangle newHandleBounds = ((PortContainerLayout) containerFigure.getLayoutManager()).getMainNodeConstraint();
        if (newHandleBounds != null) {
            newHandleBounds = newHandleBounds.getTranslated(((PortContainerLayout) containerFigure.getLayoutManager()).getOrigin(containerFigure));
        } else {
            newHandleBounds = ((PortContainerFigure) containerFigure).getHandleBounds().getCopy();
        }
        //containerFigure.translateToAbsolute(newHandleBounds);
        map.put("newHandleBounds", newHandleBounds);
        req.setExtendedData(map);
        Command resizeContainerCommand = getHost().getParent().getCommand(req);
        
        // 4 - If container is about to be translated (moveDelta != (0,0)), all
        // children need to be translated oppositely so they stay visually at
        // the same place.
        Command translateChildrenCommand = null;
        if (moveDelta.x != 0 || moveDelta.y != 0) {
            translateChildrenCommand = new TranslateChildrenCommand((GraphicalEditPart) getHost(), moveDelta.getNegated());
        }
        if (resizeContainerCommand != null && resizeContainerCommand.canExecute()) {
            resizeContainerCommand.execute();
            if (translateChildrenCommand != null && translateChildrenCommand.canExecute())
                translateChildrenCommand.execute();
        }
    }

}
