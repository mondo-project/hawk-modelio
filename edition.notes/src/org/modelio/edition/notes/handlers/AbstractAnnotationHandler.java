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

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.edition.notes.view.NotesView;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("08501fd0-6f9f-446b-b7bf-ec24fee611f4")
public abstract class AbstractAnnotationHandler {
    @objid ("c4ec46e0-c475-48a9-84ab-0eb058f5cd43")
    @Inject
     EPartService partService;

    @objid ("ca7723e8-3f18-4723-9ca8-b4211efeea56")
    @Execute
    public void execute(Shell parentShell, IProjectService projectService, IMModelServices modelServices, @Optional
@Named("noteType") final String noteType) {
        NotesPanelProvider notesPanel = getNotesPanel();        
        if (notesPanel != null) {
            ICoreSession session = projectService.getSession();
            doExecute(parentShell, modelServices, session, notesPanel, noteType);
        } else  {
            EditionNotes.LOG.debug("No notes view !");
        }
    }

    @objid ("c9b3e75d-4f9b-45f8-92f3-0dbfe71e1a52")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection, IProjectService projectService) {
        // Sanity checks
        if (projectService.getSession() == null) {
            return false;
        }
        // Must have and only one selected element
        MObject element = getSelectedElement(selection);
        
        if (element == null || !element.getStatus().isModifiable()) {
            return false;
        }
        return doCanExecute(selection, projectService);
    }

    @objid ("6bc31180-4507-401e-a77e-f50ea64992c7")
    protected ModelElement getSelectedElement(IStructuredSelection selection) {
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

    @objid ("7964e4ba-3268-4fe8-b449-7a42309dc225")
    protected List<ModelElement> getSelectedNoteItems() {
        List<ModelElement> noteItems = new ArrayList<>();
        NotesPanelProvider notesPanel = getNotesPanel();        
        if (notesPanel != null) {
            noteItems = notesPanel.getSelectedNoteItems();
        }
        return noteItems;
    }

    @objid ("bf95de67-4afb-4550-9787-688b5f457746")
    protected NotesPanelProvider getNotesPanel() {
        NotesPanelProvider notesPanel = null;
        MPart part = this.partService.findPart(NotesView.VIEW_ID);
        if (part != null) {
            notesPanel = ((NotesView) part.getObject()).getNotesPanel();
        }
        return notesPanel;
    }

    @objid ("9c755c7a-62b9-4547-9228-e3786d452d4c")
    protected abstract void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType);

    @objid ("4a2aa7b2-70ee-4100-98b0-f9ac5dade69a")
    protected abstract boolean doCanExecute(IStructuredSelection selection, IProjectService projectService);

}
