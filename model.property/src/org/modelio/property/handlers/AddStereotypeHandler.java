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
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.elementChooser.ElementChooserDlg;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.stereotype.chooser.StereotypeChooserDriver;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Handler for the 'Add stereotype' button.
 */
@objid ("5be6572b-7630-46df-a78d-56e8edebf345")
public class AddStereotypeHandler {
    @objid ("d9d7ce4c-9b6c-48a4-aed7-269f630a4d3f")
    @Execute
    public void execute(Shell parentShell, IProjectService projectService, IMModelServices modelServices, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        ICoreSession session = projectService.getSession();
        createStereotype(parentShell, getSelectedElement(selection), session, modelServices);
    }

    @objid ("30786f06-0b02-4b8c-b424-f7a1cf66b10d")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        ModelElement element = getSelectedElement(selection);
        return element != null && element.getStatus().isModifiable();
    }

    @objid ("383f86bf-6b87-400b-b0ff-fecaa8598768")
    private ModelElement getSelectedElement(IStructuredSelection selection) {
        if (selection.size() == 1) {
            Object first = selection.getFirstElement();
            if (first instanceof ModelElement) {
                return (ModelElement) first;
            } else if (first instanceof IAdaptable) {
                return (ModelElement) ((IAdaptable) first).getAdapter(ModelElement.class);
            }
        }
        return null;
    }

    @objid ("1c6c7280-850f-4452-b9a4-f193de5ffeb4")
    private Stereotype createStereotype(final Shell parentShell, final ModelElement parent, ICoreSession session, IMModelServices modelServices) {
        StereotypeChooserDriver driver = new StereotypeChooserDriver(session, modelServices, null);
        ElementChooserDlg dialog = new ElementChooserDlg(parentShell, driver, parent);
        
        // Don't return from open() until window closes
        dialog.setBlockOnOpen(true);
        
        // Open the main window
        dialog.open();
        return driver.getCreatedStereotype();
    }

}
