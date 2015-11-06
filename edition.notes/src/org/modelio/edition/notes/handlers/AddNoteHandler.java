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
import org.modelio.edition.notes.noteChooser.NoteChooserDriver;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * Handler for the 'Create note' button.
 */
@objid ("26dc6183-186f-11e2-bc4e-002564c97630")
public class AddNoteHandler extends AbstractAnnotationHandler {
    @objid ("26dc6196-186f-11e2-bc4e-002564c97630")
    private Note createNoteFromType(final ModelElement noteParent, final String noteType, ICoreSession session, IMModelServices modelServices) {
        Note newNote = null;
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction(EditionNotes.I18N.getString("AddNote"))) {
            try {
                newNote = modelServices.getModelFactory(noteParent).createNote("ModelerModule", noteType, noteParent, EditionNotes.I18N.getString("EnterNoteBody"));
            } catch (ExtensionNotFoundException e) {
                EditionNotes.I18N.equals(e);
            }
            transaction.commit();
        }
        return newNote;
    }

    @objid ("26dc61a0-186f-11e2-bc4e-002564c97630")
    private Note createNoteFromUserChoice(final Shell parentShell, final ModelElement noteParent, ICoreSession session, IMModelServices modelServices) {
        NoteChooserDriver driver = new NoteChooserDriver(session, modelServices);
        ModelElement currentElement = noteParent;
        ElementChooserDlg dialog = new ElementChooserDlg(parentShell, driver, currentElement);
        
        // Don't return from open() until window closes
        dialog.setBlockOnOpen(true);
        
        // Open the main window
        dialog.open();
        return driver.getCreatedNote();
    }

    @objid ("887279b5-520c-49cf-a067-5a219dfa6572")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        Note newNote = null;
        if (noteType != null && !noteType.isEmpty()) {
            newNote = createNoteFromType(notesPanel.getInput(), noteType, session, modelServices);
        } else {
            newNote = createNoteFromUserChoice(parentShell, notesPanel.getInput(), session, modelServices);
        }
        
        if (newNote != null) {
            notesPanel.setInput(newNote);
        }
    }

    @objid ("edae63d8-5ef1-4631-9686-0ddb434bd6ed")
    @Override
    protected boolean doCanExecute(IStructuredSelection selection, IProjectService projectService) {
        return true;
    }

}
