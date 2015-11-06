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
                                    

package org.modelio.diagram.editor.handlers.align;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Base abstract class for all alignment command handlers.
 * 
 * @author pvlaemynck
 */
@objid ("65ad536b-33f7-11e2-95fe-001ec947c8cc")
public abstract class AbstractAlignHandler {
    @objid ("65ad536d-33f7-11e2-95fe-001ec947c8cc")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        GraphicalEditPart primarySelection = null;
        List<GraphicalEditPart> otherSelections = new ArrayList<>();
               
        if (selection instanceof IStructuredSelection) {
            List<?> selectedObjects = ((IStructuredSelection) selection).toList();
            for (Object selectedObject : selectedObjects) {
                if (selectedObject instanceof GraphicalEditPart) {
                    GraphicalEditPart editPart = (GraphicalEditPart) selectedObject;
                    if (editPart.getSelected() == EditPart.SELECTED_PRIMARY)
                        primarySelection = editPart;
                    else
                        otherSelections.add(editPart);
                }
            }
        }
        filterSelection(primarySelection, otherSelections);
        
        // Align the elements
        if (primarySelection != null && otherSelections.size() > 0) {            
            align(primarySelection, otherSelections);
        }
        return null;
    }

    @objid ("65ad5373-33f7-11e2-95fe-001ec947c8cc")
    protected abstract void align(GraphicalEditPart primarySelection, List<GraphicalEditPart> otherSelections);

    /**
     * This method returns the effective bounds (those seen by the end user) of a figure
     * @param figure the figure which bounds are to be returned.
     * @return a copy of the effective bounds of the figure
     */
    @objid ("65ad5378-33f7-11e2-95fe-001ec947c8cc")
    protected Rectangle getEffectiveBounds(IFigure figure) {
        return (figure instanceof HandleBounds) ? ((HandleBounds) figure).getHandleBounds().getCopy()
                : figure.getBounds().getCopy();
    }

    @objid ("65ad537e-33f7-11e2-95fe-001ec947c8cc")
    private void filterSelection(final GraphicalEditPart primarySelection, final List<GraphicalEditPart> otherSelections) {
        // Filter the selection: when an ancestor is also in selection, remove the child.
        // That is done because any translation/resizing applied to the ancestor will already have an
        // impact on the child.
        List<GraphicalEditPart> otherSelectionsCopy = new ArrayList<>(otherSelections);
        otherSelectionsCopy.add(primarySelection);
        for (EditPart editPart : otherSelectionsCopy) {
            boolean ancestorFound = false;
            while (editPart != null && !ancestorFound) {
                if (otherSelectionsCopy.contains(editPart.getParent())) {
                    otherSelections.remove(editPart);
                    ancestorFound = true;
                }
                editPart = editPart.getParent();
            }
        }
    }

}
