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
                                    

package org.modelio.edition.notes.handlers.move;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.edition.notes.handlers.AbstractAnnotationHandler;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.edition.notes.view.NotesView;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Pastes the elements from the clipboard.
 */
@objid ("b827992c-66c8-47c4-8575-de1d43221527")
public class MoveAnnotationUpHandler extends AbstractAnnotationHandler {
    @objid ("7a98c1d6-77e2-4ecb-9cd7-738c58ae67dd")
    private static int getIndexUp(ModelElement element, List<MObject> listToReorder) {
        int index = listToReorder.indexOf(element);
        
        if (index < 1) {
            return -1;
        }
        
        index--;
        
        // Iterate until we find an element of the same metaclass or until we find the begining of the list.
        while (index != -1 && listToReorder.get(index).getClass() != element.getClass()) {
            index--;
        }
        return index;
    }

    @objid ("c6280aeb-92b0-487e-b220-f8265902c9da")
    static void reportException(Exception e) {
        // Show an error box
        String title = EditionNotes.I18N.getMessage("CannotPasteClipboard");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        EditionNotes.LOG.error(e);
    }

    @objid ("ba96a268-6d8f-4e66-abd0-34cfefd1817f")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        ModelElement parentElement = notesPanel.getInput();
        
        List<ModelElement> selectedElements = notesPanel.getSelectedNoteItems();
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Move annotation up")) {
            int nbToMove = 0;
            List listToReorder = new ArrayList<>();
            if (selectedElements.get(0) instanceof Note) {
                listToReorder = parentElement.getDescriptor(); // list of notes
            } else if (selectedElements.get(0) instanceof Constraint) {                
                listToReorder = parentElement.getConstraintDefinition(); // list of constraints
            } else if (selectedElements.get(0) instanceof ExternDocument) {
                listToReorder = parentElement.getDocument();                
            }
            for (ModelElement element : selectedElements) {
                
                int index = getIndexUp(element, listToReorder);
        
                if (index != -1) {
                    nbToMove++;
                    listToReorder.remove(element);
                    listToReorder.add(index, element);
                } else {
                    break;
                }
            }
        
            if (nbToMove > 0) {
                transaction.commit();
            } else {
                transaction.rollback();        
            }
        } catch (Exception e) {
            // Should catch InvalidModelManipulationException to display a popup box, but it
            // is not a RuntimeException.
            reportException(e);
        }
    }

    @objid ("e26335a1-08dd-4b51-9bad-d560d6cba08d")
    @Override
    protected boolean doCanExecute(IStructuredSelection selection, IProjectService projectService) {
        List<ModelElement> selectedItems = getSelectedNoteItems();
        Class<?> selectType = null;
        for (ModelElement me : selectedItems) {
            if (!me.getStatus().isModifiable()) {
                return false;
            }
            if (selectType == null) {          
                selectType = me.getClass();
            } else {                
                if (me.getClass() != selectType) {  //cannot move elements with different types
                    return false;
                }
            }
        }
        return (selectedItems.size() > 0);
    }

}
