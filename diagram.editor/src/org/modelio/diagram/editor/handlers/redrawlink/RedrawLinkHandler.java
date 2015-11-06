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
                                    

package org.modelio.diagram.editor.handlers.redrawlink;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.metamodel.uml.statik.NaryAssociationEnd;
import org.modelio.metamodel.uml.statik.NaryLinkEnd;

/**
 * Handler for the "Redraw link" contextual command. Simply sets the current tool to a custom tool with a cutsom made
 * creation factory.
 * 
 * @author fpoyer
 */
@objid ("65c2c8df-33f7-11e2-95fe-001ec947c8cc")
public class RedrawLinkHandler {
    @objid ("65c2c8e1-33f7-11e2-95fe-001ec947c8cc")
    @Execute
    public Object execute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        // First get the current selection
        GmLinkEditPart primarySelection = null;
        for (GmLinkEditPart editPart : getLinksToProcess(selection)) {
            if (editPart.getSelected() == EditPart.SELECTED_PRIMARY) {
                primarySelection = editPart;
                break;
            }
        }
        
        if (primarySelection != null) {
            // Now instantiate the tool.
            EditDomain editDomain = primarySelection.getViewer().getEditDomain();
            RedrawConnectionTool redrawTool = new RedrawConnectionTool(primarySelection);
            // Set the tool as the active one.
            editDomain.setActiveTool(redrawTool);
        }
        return null;
    }

    @objid ("65c52af1-33f7-11e2-95fe-001ec947c8cc")
    @CanExecute
    public boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) ISelection selection) {
        final List<GmLinkEditPart> selectedLinks = getLinksToProcess(selection);
        // At least one link must be selected
        if (selectedLinks.isEmpty())
            return false;
        
        // At least one link must support changing router
        for (final GmLinkEditPart linkEditpart : selectedLinks) {
            final GmLink link = (GmLink) linkEditpart.getModel();
            // Deactivate command on Naries...
            if (link.getRelatedElement() instanceof NaryAssociationEnd || link.getRelatedElement() instanceof NaryLinkEnd || link.getRelatedElement() instanceof NamespaceUse) {
                return false;
            }
            final StyleKey styleKey = link.getStyleKey(MetaKey.CONNECTIONROUTER);
            if (styleKey != null)
                return true;
        }
        return false;
    }

    @objid ("65c52af6-33f7-11e2-95fe-001ec947c8cc")
    protected List<GmLinkEditPart> getLinksToProcess(ISelection selection) {
        final List<GmLinkEditPart> selectedLinks = new ArrayList<>();
        if (selection instanceof IStructuredSelection) {
            final List<?> selectedObjects = ((IStructuredSelection) selection).toList();
            for (final Object selectedObject : selectedObjects) {
                if (selectedObject instanceof GmLinkEditPart) {
                    selectedLinks.add((GmLinkEditPart) selectedObject);
                }
            }
        }
        return selectedLinks;
    }

}
