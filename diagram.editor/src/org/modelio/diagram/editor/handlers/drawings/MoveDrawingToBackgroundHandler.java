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
 * Handler that moves free drawing to the background layer.
 */
@objid ("8c27607d-2438-43b1-a455-7130e370e81d")
public class MoveDrawingToBackgroundHandler {
    @objid ("5b3a4c99-54a6-40b8-9fca-958b9f19358e")
    private CommandStack cmdStack;

    /**
     * Execute the handler
     * @param selection the Eclipse selection
     */
    @objid ("8b0961b6-de6a-4e13-a6d5-9656fef0c497")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        MoveToLayerCommand cmd = createGefCommand(selection);
        
        if (cmd.canExecute()) {
            this.cmdStack.execute(cmd);
        }
    }

    /**
     * @param selection the Eclipse selection
     * @return <code>true</code> to  enable the command, <code>false</code> to gray it.
     */
    @objid ("0152f901-ea6e-40a9-a0d8-c38b166308bd")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        MoveToLayerCommand cmd = createGefCommand(selection);
        return  (cmd.canExecute());
    }

    @objid ("653aafdd-40e8-4f11-889d-e75aa662ebf9")
    private MoveToLayerCommand createGefCommand(ISelection selection) {
        List<IGmDrawing> selected = extractSelection(selection);
        
        MoveToLayerCommand cmd = new MoveToLayerCommand();
        
        cmd.addElements(selected);
        
        IGmDrawingLayer bgl = selected.get(0).getDiagram().getBackgroundDrawingLayer();
        cmd.setTarget(bgl);
        return cmd;
    }

    @objid ("7a64f662-467d-45fa-ab20-e4deb44672c5")
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

}
