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
                                    

package org.modelio.diagram.editor.handlers.drawings;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.diagram.elements.drawings.core.IGmDrawing;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLayer;
import org.modelio.diagram.elements.drawings.core.MoveToLayerCommand;

/**
 * Handler that moves free drawing to the foreground layer.
 */
@objid ("bab2ea51-519b-448e-8f77-4e4332693f7f")
public class MoveDrawingToForegroundHandler {
    @objid ("b25a403c-1919-4666-9e86-8a73402a3ca3")
    private CommandStack cmdStack;

    /**
     * Execute the handler
     * @param selection the Eclipse selection
     */
    @objid ("b15f1f77-3502-4c80-a56a-0bd7e5646332")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        MoveToLayerCommand cmd = createGefCommand(selection);
        
        if (cmd.canExecute()) {
            this.cmdStack.execute(cmd);
        }
    }

    @objid ("67d99f48-12c5-4434-b9fc-646b836af90e")
    private MoveToLayerCommand createGefCommand(ISelection selection) {
        List<IGmDrawing> selected = extractSelection(selection);
        
        MoveToLayerCommand cmd = new MoveToLayerCommand();
        
        cmd.addElements(selected);
        final List<IGmDrawingLayer> drawingLayers = selected.get(0).getDiagram().getDrawingLayers();
        IGmDrawingLayer fgl = drawingLayers.get(drawingLayers.size() - 1);
        
        cmd.setTarget(fgl);
        return cmd;
    }

    @objid ("89ab6b23-c44d-407b-8dcd-bb78544beb93")
    private List<IGmDrawing> extractSelection(ISelection selection) {
        List<IGmDrawing> selected = new ArrayList<>();
        this.cmdStack = null;
        if (selection instanceof IStructuredSelection) {
            for (Object selectedObject : ((IStructuredSelection) selection).toList()) {
                if (selectedObject instanceof IAdaptable) {
                    final IGmDrawing drawing = (IGmDrawing) ((IAdaptable) selectedObject).getAdapter(IGmDrawing.class);
                    if (drawing != null) {
                        selected.add(drawing);
                        
                        GraphicalEditPart part = (GraphicalEditPart) selectedObject;
                        this.cmdStack = part.getViewer().getEditDomain().getCommandStack();
                    }
                }
            }
        }
        return selected;
    }

    /**
     * @param selection the Eclipse selection
     * @return <code>true</code> to  enable the command, <code>false</code> to gray it.
     */
    @objid ("f0f959e9-63de-49a9-9415-53b9b26836bc")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        MoveToLayerCommand cmd = createGefCommand(selection);
        return  (cmd.canExecute());
    }

}
