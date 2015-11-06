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
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.IFigure;
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
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.modelio.diagram.elements.core.figures.FigureUtilities2;

/**
 * A local specialisation of {@link ResizableEditPolicy} is used by default for children. <br>
 * <br>
 * This specialisation adds a behaviour on ORPHAN requests similar to what is done in
 * {@link NonResizableEditPolicy#getMoveCommand}, meaning that it actually dispatches the request to the host's parent
 * edit part as a {@link RequestConstants#REQ_ORPHAN_CHILDREN} request. The parent's contribution is returned.<br>
 * <br>
 * Subclasses may override this method to supply a different EditPolicy.
 * 
 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy(EditPart)
 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getMoveCommand
 */
@objid ("80be19de-1dec-11e2-8cad-001ec947c8cc")
public class DefaultNodeNonResizableEditPolicy extends NonResizableEditPolicy {
    @objid ("80be19e2-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> createSelectionHandles() {
        List<?> selectionHandles = doCreateSelectionHandles();
        MoveHandle mh = null;
        ResizeHandle rh = null;
        
        for (Object o : selectionHandles) {
            if (o instanceof MoveHandle) {
                // the MoveHandle draws a rectangle we do not need, remove it
                mh = (MoveHandle) o;
                mh.setSize(10, 10);
                mh.setPreferredSize(9, 9);
                mh.setMaximumSize(new Dimension(9, 9));
                mh.setMinimumSize(new Dimension(9, 9));
        
            }
            if (o instanceof ResizeHandle) {
        
                rh = (ResizeHandle) o;
                rh.setSize(10, 10);
                rh.setPreferredSize(9, 9);
                rh.setMaximumSize(new Dimension(9, 9));
                rh.setMinimumSize(new Dimension(9, 9));
            }
        }
        selectionHandles.remove(mh);
        return selectionHandles;
    }

    @objid ("80c07be1-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createDragSourceFeedbackFigure() {
        // Use a ghost rectangle for feedback
        RectangleFigure r = new RectangleFigure();
        FigureUtilities2.makeGhostShape(r, getHostFigure());
        addFeedback(r);
        return r;
    }

    @objid ("80c07be8-1dec-11e2-8cad-001ec947c8cc")
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

    @objid ("80c07bf2-1dec-11e2-8cad-001ec947c8cc")
    private List<AbstractHandle> doCreateSelectionHandles() {
        List<AbstractHandle> list = new ArrayList<>();
        if (isDragAllowed())
            NonResizableHandleKit.addHandles((GraphicalEditPart) getHost(),
                                             list,
                                             getHost().getDragTracker(null),
                                             getHostFigure().getCursor());
        else
            NonResizableHandleKit.addHandles((GraphicalEditPart) getHost(),
                                             list,
                                             getHost().getDragTracker(null),
                                             Cursors.ARROW);
        return list;
    }

}
