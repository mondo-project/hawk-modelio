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
                                    

package org.modelio.linkeditor.showcause;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.core.ui.nsu.CauseAnalyser;
import org.modelio.linkeditor.view.background.BackgroundEditPart;
import org.modelio.linkeditor.view.edge.EdgeEditPart;
import org.modelio.linkeditor.view.node.GraphNode;
import org.modelio.linkeditor.view.node.NodeEditPart;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Handler for the "show cause" contextual command on NamespaceUse.
 */
@objid ("1b7712a0-5e33-11e2-b81d-002564c97630")
public class ShowCauseHandler {
    @objid ("1b7712a2-5e33-11e2-b81d-002564c97630")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell, IModelioNavigationService navigationService) {
        for (MObject element : getSelection(selection)) {
            if (element instanceof NamespaceUse) {
        
               CauseAnalyser.showCauses((NamespaceUse)element, navigationService);
            }
        }
        return null;
    }

    @objid ("1b7712a9-5e33-11e2-b81d-002564c97630")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection iSelection) {
        List<MObject> selection = getSelection(iSelection);
        return selection.size() == 1 && (selection.get(0) instanceof NamespaceUse);
    }

    @objid ("1b7712ae-5e33-11e2-b81d-002564c97630")
    protected List<MObject> getSelection(IStructuredSelection selection) {
        List<MObject> selectedElements = new ArrayList<>();
        
        for (Object selectedObject : selection.toList()) {
            if (selectedObject instanceof EdgeEditPart) {
                Edge edge = (Edge) ((EdgeEditPart) selectedObject).getModel();
                if (edge.data != null && edge.data instanceof MObject) {
                    selectedElements.add((MObject) edge.data);
                }
            } else if (selectedObject instanceof NodeEditPart) {
                GraphNode node = ((NodeEditPart) selectedObject).getModel();
                if (node.getData() != null) {
                    selectedElements.add(node.getData());
                }
            } else if (selectedObject instanceof BackgroundEditPart) {
                GraphNode node = ((BackgroundEditPart) selectedObject).getModel().getCenter();
                if (node.getData() != null) {
                    selectedElements.add(node.getData());
                }
            }
        }
        return selectedElements;
    }

}
