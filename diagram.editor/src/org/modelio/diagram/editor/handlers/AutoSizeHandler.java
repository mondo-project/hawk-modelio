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
                                    

package org.modelio.diagram.editor.handlers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.diagram.editor.plugin.DiagramEditor;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramEditPart;
import org.modelio.diagram.elements.common.portcontainer.GmPortContainer;
import org.modelio.diagram.elements.core.model.IGmModelRelated;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Handler for the autosize command that will reduce a selection of node edit parts to their minimum size.
 * 
 * @author phv
 */
@objid ("65ad5386-33f7-11e2-95fe-001ec947c8cc")
public class AutoSizeHandler {
    /**
     * @param selection the Eclipse selection
     */
    @objid ("65ad5399-33f7-11e2-95fe-001ec947c8cc")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        GraphicalEditPart primarySelection = null;
        List<GraphicalEditPart> otherSelections = new ArrayList<>();
        if (selection instanceof IStructuredSelection) {
            List<?> selectedObjects = ((IStructuredSelection) selection).toList();
            for (Object selectedObject : selectedObjects) {
                if (selectedObject instanceof GraphicalEditPart) {
                    GraphicalEditPart editPart = (GraphicalEditPart) selectedObject;
                    boolean isPrimary = editPart.getSelected() == EditPart.SELECTED_PRIMARY;
        
                    Object model = editPart.getModel();
                    if (model instanceof IGmModelRelated) {
                        // Only keep 'main' gms
                        MObject representedElement = ((IGmModelRelated)model).getRepresentedElement();
                        while (representedElement == null) {
                            editPart = (GraphicalEditPart) editPart.getParent();
                            model = editPart.getModel();
                            representedElement = ((IGmModelRelated)model).getRepresentedElement();
                        }
                    }
        
                    if (isPrimary) {
                        primarySelection = editPart;
        
                        // Avoid keeping the same edit part more than once
                        otherSelections.remove(editPart);
                    } else {
                        // Avoid keeping the same edit part more than once
                        if (editPart != primarySelection && !otherSelections.contains(editPart)) {
                            otherSelections.add(editPart);
                        }
                    }
                }
            }
        }
        filterSelection(primarySelection, otherSelections);
        
        // Resize the elements
        execute(primarySelection, otherSelections);
    }

    @objid ("65ad538e-33f7-11e2-95fe-001ec947c8cc")
    private ChangeBoundsRequest buildAutoSizeRequest(GraphicalEditPart editPart) {
        // does not apply on diagram
        if (editPart instanceof AbstractDiagramEditPart) {
            return null;
        }
        
        final IFigure fig = editPart.getFigure();
        
        final Dimension oldSize = getEffectiveBounds(fig).getSize();
        
        Dimension newSize = getMinimumSize(editPart);
        
        if (oldSize.equals(newSize))
            return null;
        
        final ChangeBoundsRequest req = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
        req.setEditParts(editPart);
        req.setSizeDelta(newSize.getCopy().getShrinked(oldSize));
        req.setMoveDelta(new Point(0, 0));
        return req;
    }

    @objid ("65ad53a0-33f7-11e2-95fe-001ec947c8cc")
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

    @objid ("65ad5393-33f7-11e2-95fe-001ec947c8cc")
    private Dimension getMinimumSize(final GraphicalEditPart editPart) {
        // For PortContainers, we must take the main node minimum size to avoid side effects during the setSize
        if (editPart.getModel() instanceof GmPortContainer) {
            GmPortContainer gpc = (GmPortContainer) editPart.getModel();
            GmNodeModel mainNode = gpc.getMainNode();
        
            if (mainNode != null) {
                GraphicalEditPart mainNodeEditPart = (GraphicalEditPart) editPart.getViewer()
                        .getEditPartRegistry()
                        .get(mainNode);
        
                if (mainNodeEditPart != null) {
                    IFigure mainFig = mainNodeEditPart.getFigure();
                    return mainFig.getMinimumSize();
                }
            }
        }
        return editPart.getFigure().getMinimumSize();
    }

    /**
     * This method returns the effective bounds (those seen by the end user) of a figure
     * @param figure the figure which bounds are to be returned.
     * @return a copy of the effective bounds of the figure
     */
    @objid ("65ad53a8-33f7-11e2-95fe-001ec947c8cc")
    protected Rectangle getEffectiveBounds(final IFigure figure) {
        return (figure instanceof HandleBounds) ? ((HandleBounds) figure).getHandleBounds().getCopy()
                : figure.getBounds().getCopy();
    }

    @objid ("65ad5388-33f7-11e2-95fe-001ec947c8cc")
    protected void execute(GraphicalEditPart primarySelection, List<GraphicalEditPart> otherSelections) {
        CompoundCommand compound = new CompoundCommand("Auto size");
        EditPartViewer viewer = null;
        
        if (primarySelection != null) {
            final ChangeBoundsRequest req = buildAutoSizeRequest(primarySelection);
            if (req != null) {
                compound.add(primarySelection.getCommand(req));
            }
            viewer = primarySelection.getViewer();
        }
        
        for (GraphicalEditPart editPart : otherSelections) {
            final ChangeBoundsRequest req = buildAutoSizeRequest(editPart);
            if (req != null) {
                compound.add(editPart.getCommand(req));
            }
        
            if (viewer == null)
                viewer = editPart.getViewer();
        }
        
        if (viewer != null && compound.canExecute()) {
            viewer.getEditDomain().getCommandStack().execute(compound);
        } else if (viewer == null) {
            DiagramEditor.LOG.warning("AutosizeHandler#align : could not reach a valid EditPartViewer");
        }
    }

}
