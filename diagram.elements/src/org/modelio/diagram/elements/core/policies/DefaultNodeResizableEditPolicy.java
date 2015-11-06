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
                                    

package org.modelio.diagram.elements.core.policies;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.modelio.diagram.elements.core.figures.FigureUtilities2;

/**
 * A local specialisation of {@link ResizableEditPolicy} is used by default for children. <br>
 * <br>
 * This specialisation adds a behaviour on ORPHAN requests similar to what is done in {@link NonResizableEditPolicy#getMoveCommand},
 * meaning that it actually dispatches the request to the host's parent edit part as a {@link RequestConstants#REQ_ORPHAN_CHILDREN}
 * request. The parent's contribution is returned.<br>
 * <br>
 * Subclasses may override this method to supply a different EditPolicy.
 * 
 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy(EditPart)
 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getMoveCommand
 */
@objid ("80c07c0b-1dec-11e2-8cad-001ec947c8cc")
public class DefaultNodeResizableEditPolicy extends ResizableEditPolicy {
    @objid ("80c07c0f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> createSelectionHandles() {
        List<?> selectionHandles = doCreateSelectionHandles();
        MoveHandle mh = null;
        ResizeHandle rh = null;
        
        for (Object o : selectionHandles) {
            // System.out.println("o=" + o.toString());
            if (o instanceof MoveHandle) {
                // the MoveHandle draws a rectangle we do not need, remove it
                mh = (MoveHandle) o;
                mh.setForegroundColor(ColorConstants.blue);
        
            }
            if (o instanceof ResizeHandle) {
        
                rh = (ResizeHandle) o;
                rh.setSize(10, 10);
                rh.setPreferredSize(9, 9);
                rh.setMaximumSize(new Dimension(9, 9));
                rh.setMinimumSize(new Dimension(9, 9));
                // changing colors does not work as colors are forced to black
                // or white
                // in the ResizeHandle code
                // rh.setBackgroundColor(ColorConstants.red);
                // rh.setForegroundColor(ColorConstants.red);
        
            }
        }
        selectionHandles.remove(mh);
        return selectionHandles;
    }

    @objid ("80c07c16-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createDragSourceFeedbackFigure() {
        // Use a ghost rectangle for feedback
        RectangleFigure r = new RectangleFigure();
        FigureUtilities2.makeGhostShape(r, getHostFigure());
        addFeedback(r);
        return r;
    }

    @objid ("80c07c1d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getOrphanCommand(Request request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_ORPHAN_CHILDREN);
        req.setEditParts(getHost());
        
        req.setMoveDelta(((ChangeBoundsRequest) request).getMoveDelta());
        req.setSizeDelta(((ChangeBoundsRequest) request).getSizeDelta());
        req.setLocation(((ChangeBoundsRequest) request).getLocation());
        req.setExtendedData(request.getExtendedData());
        return getHost().getParent().getCommand(req);
    }

    @objid ("80c07c27-1dec-11e2-8cad-001ec947c8cc")
    private List<AbstractHandle> doCreateSelectionHandles() {
        List<AbstractHandle> list = new ArrayList<>();
        int directions = getResizeDirections();
        if (directions == 0)
            NonResizableHandleKit.addHandles((GraphicalEditPart) getHost(), list, getHost().getDragTracker(null), getHostFigure()
                    .getCursor());
        else if (directions != -1) {
            ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), list, getHost().getDragTracker(null), getHostFigure()
                    .getCursor());
            if ((directions & PositionConstants.EAST) != 0)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.EAST);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.EAST, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.SOUTH_EAST) == PositionConstants.SOUTH_EAST)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_EAST);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_EAST, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.SOUTH) != 0)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.SOUTH_WEST) == PositionConstants.SOUTH_WEST)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_WEST);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.SOUTH_WEST, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.WEST) != 0)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.WEST);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.WEST, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.NORTH_WEST) == PositionConstants.NORTH_WEST)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_WEST);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_WEST, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.NORTH) != 0)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
            if ((directions & PositionConstants.NORTH_EAST) == PositionConstants.NORTH_EAST)
                ResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_EAST);
            else
                NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), list, PositionConstants.NORTH_EAST, getHost()
                        .getDragTracker(null), getHostFigure().getCursor());
        } else
            ResizableHandleKit.addHandles((GraphicalEditPart) getHost(), list);
        return list;
    }

}
