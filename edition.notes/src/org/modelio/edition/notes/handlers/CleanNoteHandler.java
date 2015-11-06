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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Handler for the 'clean' button.
 * <p>
 * Empties the selected note or constraint body.
 */
@objid ("26dc61aa-186f-11e2-bc4e-002564c97630")
public class CleanNoteHandler extends AbstractAnnotationHandler {
    @objid ("82080419-a545-4bca-986c-0ab7f71a87f2")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        notesPanel.cleanCurrentNoteItemContent();
    }

    @objid ("2045f208-f5bd-4a5d-a105-ce8156e68502")
    @Override
    protected boolean doCanExecute(IStructuredSelection selection, IProjectService projectService) {
        List<ModelElement> noteItems = getSelectedNoteItems();
        
        if (noteItems.isEmpty()) return false;
        for (ModelElement me : noteItems) {
            if (!me.getStatus().isModifiable()) {
                return false;
            }
        }
        return true;
    }

}
