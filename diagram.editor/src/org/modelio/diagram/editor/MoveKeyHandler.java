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
                                    

package org.modelio.diagram.editor;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagramStyleKeys;
import org.modelio.diagram.elements.core.model.IGmObject;

/**
 * Specialization of the {@link KeyHandler} to add handling of the arrow keys to move elements.
 * 
 * @author fpoyer
 */
@objid ("66674ada-33f7-11e2-95fe-001ec947c8cc")
public class MoveKeyHandler extends KeyHandler {
    /**
     * True if a not recorded move has been done by this handler.
     */
    @objid ("666c0f8b-33f7-11e2-95fe-001ec947c8cc")
    private boolean moveDone = false;

    @objid ("66674adc-33f7-11e2-95fe-001ec947c8cc")
    private final GraphicalViewer viewer;

    /**
     * Creates a MoveKeyHandler that will handle arrow keys as request to translate the selected edit parts.
     * @param viewer the viewer to act on.
     */
    @objid ("666c0f8d-33f7-11e2-95fe-001ec947c8cc")
    public MoveKeyHandler(final GraphicalViewer viewer) {
        this.viewer = viewer;
    }

    @objid ("666c0f92-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public boolean keyPressed(final KeyEvent event) {
        List<?> selectedEditParts = this.viewer.getSelectedEditParts();
        if ( !selectedEditParts.isEmpty()) {
            EditPart contents = this.viewer.getContents();
            SnapToHelper snapToHelper = (SnapToHelper) contents.getAdapter(SnapToHelper.class);
        
            int offset = 11;
            if (contents.getModel() instanceof IGmObject) {
                offset = ((IGmObject) contents.getModel()).getStyle()
                        .getInteger(GmAbstractDiagramStyleKeys.GRIDSPACING);
                offset = (offset / 2) + 1;
            }
            if ((event.stateMask & SWT.ALT) != 0) {
                offset = 10;
            }
            if ((event.stateMask & SWT.CTRL) != 0) {
                offset = 1;
            }
        
        
        
            Point moveDelta = new Point(0, 0);
            switch (event.keyCode) {
            case SWT.ARROW_UP: {
                moveDelta.setLocation(0, -offset);
                break;
            }
            case SWT.ARROW_LEFT: {
                moveDelta.setLocation(-offset, 0);
                break;
            }
            case SWT.ARROW_DOWN: {
                moveDelta.setLocation(0, offset);
                break;
            }
            case SWT.ARROW_RIGHT: {
                moveDelta.setLocation(offset, 0);
                break;
            }
            }
        
            if (! moveDelta.equals(0, 0)) {
                //viewer.getEditDomain().getActiveTool().
                ChangeBoundsRequest request = new ChangeBoundsRequest(RequestConstants.REQ_MOVE);
                request.setLocation(((GraphicalEditPart) this.viewer.getFocusEditPart()).getFigure()
                        .getBounds()
                        .getCenter());
                request.setMoveDelta(moveDelta);
        
                // TODO Read diagram style to know if snap is enabled
                if (snapToHelper != null &&
                        ((event.stateMask & SWT.ALT) == 0) &&
                        ((event.stateMask & SWT.CTRL) == 0)) {
                    snap(request, selectedEditParts, snapToHelper);
        
                }
        
                for (Object o : selectedEditParts) {
                    GraphicalEditPart editPart = (GraphicalEditPart) o;
                    Command command = editPart.getCommand(request);
                    if (command != null && command.canExecute()) {
                        // Directly running the command does not save the diagram nor record
                        // the move in a Modelio transaction.
                        // This is done in keyRelease().
                        command.execute();
                        this.moveDone = true;
                    }
                }
            }
        
        }
        return super.keyPressed(event);
    }

    @objid ("666c0f99-33f7-11e2-95fe-001ec947c8cc")
    private void snap(final ChangeBoundsRequest request, final List<?> selectedEditParts, final SnapToHelper snapToHelper) {
        PrecisionRectangle compoundRectangle = null;
        PrecisionRectangle sourceRectangle = null;
        for (Object o : selectedEditParts) {
            GraphicalEditPart editPart = (GraphicalEditPart) o;
            IFigure figure = editPart.getFigure();
            PrecisionRectangle bounds = null;
            if (figure instanceof HandleBounds) {
                bounds = new PrecisionRectangle(((HandleBounds) figure).getHandleBounds());
            } else {
                bounds = new PrecisionRectangle(figure.getBounds());
            }
            figure.translateToAbsolute(bounds);
        
            if (compoundRectangle == null) {
                compoundRectangle = new PrecisionRectangle(bounds);
            } else {
                compoundRectangle = compoundRectangle.union(bounds);
            }
            if (editPart.getSelected() == EditPart.SELECTED_PRIMARY) {
                sourceRectangle = bounds;
            }
        }
        if (compoundRectangle != null && sourceRectangle != null) {
            compoundRectangle.translate(request.getMoveDelta());
            sourceRectangle.translate(request.getMoveDelta());
            PrecisionPoint preciseDelta = new PrecisionPoint(request.getMoveDelta());
            snapToHelper.snapPoint(request,
                    PositionConstants.HORIZONTAL | PositionConstants.VERTICAL,
                    new PrecisionRectangle[] { sourceRectangle, compoundRectangle },
                    preciseDelta);
            request.setMoveDelta(preciseDelta);
        }
    }

    /**
     * Record the whole move in a Modelio transaction
     */
    @objid ("666c0fa3-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public boolean keyReleased(final KeyEvent event) {
        if (this.moveDone) {
            // Record the whole move in a Modelio transaction
            this.viewer.getEditDomain().getCommandStack().execute(new Command("Validate diagram elements move") {
                @Override
                public void execute() {
                    // Nothing to do
                } 
            });
        
            this.moveDone = false;
        }
        return super.keyReleased(event);
    }

}
