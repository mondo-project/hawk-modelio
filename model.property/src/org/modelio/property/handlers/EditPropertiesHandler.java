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
                                    

package org.modelio.property.handlers;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.EditPropertiesDialog;

/**
 * Handler for the EditProperties command, using injection to execute on the
 * active selection. Opens a dialog box similar to the property view, but pinned
 * on a specific element.
 */
@objid ("8df6cc9c-c068-11e1-8c0a-002564c97630")
public class EditPropertiesHandler {
    @objid ("8df6cc9f-c068-11e1-8c0a-002564c97630")
    @Execute
    public void execute(IProjectService projectService, IMModelServices modelService, IModelioPickingService pickingService, @Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, IActivationService activationService) {
        // Edit in single element only
        if (selection.size() != 1 || !(selection.getFirstElement() instanceof Element)) {
            return;
        }
        
        // Modelio must be fully initialized
        if (projectService == null || modelService == null) {
            return;
        }
        
        // A project must be open
        if (projectService.getOpenedProject() == null) {
            return;
        }
        
        EditPropertiesDialog dlg = new EditPropertiesDialog(projectService, modelService, pickingService, activationService, shell);
        dlg.setEditedElement((Element) selection.getFirstElement());
        dlg.open();
    }

}
