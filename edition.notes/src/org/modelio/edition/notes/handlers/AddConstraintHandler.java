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
                                    

package org.modelio.edition.notes.handlers;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.elementChooser.ElementChooserDlg;
import org.modelio.edition.notes.constraintChooser.ConstraintChooserDriver;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Handler for the 'Create constraint' button.
 */
@objid ("26dc6170-186f-11e2-bc4e-002564c97630")
public class AddConstraintHandler extends AbstractAnnotationHandler {
    @objid ("0f826388-89a5-4f95-99fd-788dbec19860")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        ConstraintChooserDriver driver = new ConstraintChooserDriver(session, modelServices, null);
        ModelElement currentElement = notesPanel.getInput();
        ElementChooserDlg dialog = new ElementChooserDlg(parentShell, driver, currentElement);
            
        // Don't return from open() until window closes
        dialog.setBlockOnOpen(true);
            
        // Open the main window
        dialog.open();
            
        Constraint constraint = driver.getCreatedConstraint();
        if (constraint != null) {
            notesPanel.setInput(constraint);
        }
    }

    @objid ("e7c6a49e-2d31-4511-a0b9-940614b3b3b7")
    @Override
    protected boolean doCanExecute(IStructuredSelection selection, IProjectService projectService) {
        return true;
    }

}
