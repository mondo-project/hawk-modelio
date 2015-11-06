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
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

@objid ("26dec2d1-186f-11e2-bc4e-002564c97630")
public class RemoveAnnotationHandler extends AbstractAnnotationHandler {
    @objid ("2130107d-a7a1-41f2-bbb9-aab3bbe0d33c")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        List<ModelElement> noteItems = notesPanel.getSelectedNoteItems();
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("NoteDeletion")) {
            for (ModelElement noteItem : noteItems) {
                noteItem.delete();
            }
            transaction.commit();
        } catch (Exception e) {
            EditionNotes.LOG.error(EditionNotes.PLUGIN_ID, e);
        }
    }

    @objid ("ad5afe5d-bda6-46d7-9bd3-06a7167357e7")
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
