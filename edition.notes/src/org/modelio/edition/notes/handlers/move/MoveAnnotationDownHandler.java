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
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.edition.notes.handlers.AbstractAnnotationHandler;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
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
@objid ("cbc2e599-b002-4640-9ec8-5a3528af42a8")
public class MoveAnnotationDownHandler extends AbstractAnnotationHandler {
    @objid ("eefe0754-5ea9-4903-8f14-63a8b9336d6e")
    private static int computeNewIndex(ModelElement element, List<MObject> listToReorder) {
        int index = listToReorder.indexOf(element) + 1;
        
        // Iterate until we find an element of the same metaclass or until we find the end of the list.
        while (index < listToReorder.size() && listToReorder.get(index).getClass() != element.getClass()) {
            index++;
        }
        
        // If that would move outside of the list, that means element is already the last one.
        if (index >= listToReorder.size()) {
            return -1;
        }
        return index;
    }

    @objid ("5c199e15-0b24-461d-87d9-b5f943a0d0fc")
    static void reportException(Exception e) {
        // Show an error box
        String title = EditionNotes.I18N.getMessage("CannotPasteClipboard");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        EditionNotes.LOG.error(e);
    }

    @objid ("50c749fe-2988-471c-84df-937fdf10a6e2")
    @Override
    protected void doExecute(Shell parentShell, IMModelServices modelServices, ICoreSession session, NotesPanelProvider notesPanel, String noteType) {
        ModelElement parentElement = notesPanel.getInput();
        List<ModelElement> selectedElements = notesPanel.getSelectedNoteItems();
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Move annotation down")) {
            int nbToMove = 0;
        
            List listToReorder = new ArrayList<>();
            if (selectedElements.get(0) instanceof Note) {
                listToReorder = parentElement.getDescriptor(); // list of notes
            } else if (selectedElements.get(0) instanceof Constraint) {                
                listToReorder = parentElement.getConstraintDefinition(); // list of constraints
            } else if (selectedElements.get(0) instanceof ExternDocument) {
                listToReorder = parentElement.getDocument();                
            }
            // We first move down the Last selected element of the list; This way the positions of other
            // selected elements are not affected by the move of the current element.
            for (int i = selectedElements.size() - 1; i >= 0; --i) {
                ModelElement element = selectedElements.get(i);   
        
                // Retrieve the new index of the element
                int index = computeNewIndex(element, listToReorder);
        
                if (index == -1) {
                    // Invalid case, just exit
                    transaction.rollback();
                    return;
                }
        
                // Move the element in the list 
                nbToMove++;
                listToReorder.remove(element);
                listToReorder.add(index, element);        
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

    @objid ("958f304b-34f2-4209-9504-2a46a575d0b1")
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
