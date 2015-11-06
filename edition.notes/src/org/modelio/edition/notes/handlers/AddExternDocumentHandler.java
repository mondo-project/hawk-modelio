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
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Handler for the 'Create ExternDocument' button.
 */
@objid ("092ef4bf-f132-4564-998b-231a154018e9")
public class AddExternDocumentHandler extends AbstractAnnotationHandler {
    @objid ("6d147381-5862-40c2-add3-a98b1440528f")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        //TODO Add ExternDocument
    }

    @objid ("e457c934-4969-4feb-9ed5-3ac7bb4deed1")
    @Override
    protected boolean doCanExecute(IStructuredSelection selection, IProjectService projectService) {
        return true;
    }

}
