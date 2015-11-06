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
                                    

package org.modelio.edition.notes.handlers.copy;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.copy.PasteElementObject.PasteType;
import org.modelio.core.ui.copy.PasteElementObject;
import org.modelio.core.ui.copy.PasteElementTransfer;
import org.modelio.core.ui.copy.TransferItem;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.edition.notes.view.NotesView;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Copies selected elements into the clipboard.
 */
@objid ("28109be5-70e7-4767-9f6e-1de540602928")
public class CopyAnnotationHandler {
    @objid ("34878d76-645c-4962-84d2-59325c79d360")
     boolean ctrlFlag;

    @objid ("72df8357-313e-4e84-a22e-95ab20fa4246")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selected elements is not empty.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("a57c1fa7-6084-449a-afa3-e04145bd6302")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) final MPart part) {
        if (!(part.getObject() instanceof NotesView)) {
            return false;
        }
        
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        NotesPanelProvider notesPanel = ((NotesView) part.getObject()).getNotesPanel();
        if (notesPanel == null) {
            return false;
        }
        
        ModelElement parentElement = notesPanel.getInput();
        if (parentElement == null) {
            return false;
        }
        
        if (!parentElement.getStatus().isModifiable()) {
            return false;
        }
        
        List<ModelElement> selectedItems = notesPanel.getSelectedNoteItems();
        for (ModelElement me : selectedItems) {
            if (!(me instanceof Note) && !(me instanceof Constraint) && (!(me instanceof ExternDocument))) {
                return false;
            }
        }
        return (selectedItems.size() > 0);
    }

    /**
     * Copy the currently selected elements.
     * @param selection the current modelio selection.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("45a51303-bfe0-4176-a4d7-adcd1c0ffb0a")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
        NotesPanelProvider notesPanel = ((NotesView) part.getObject()).getNotesPanel();
        
        List<ModelElement> selectedElements = notesPanel.getSelectedNoteItems();
        
        PasteElementObject toCopy = new PasteElementObject(PasteType.COPY);
        
        for (MObject element : selectedElements) {
            toCopy.addTransferedItems(new TransferItem(element, element.getCompositionOwner()));
        }
        
        Clipboard clipboard= new Clipboard(currentDisplay);
        clipboard.setContents(new Object[] { toCopy }, new Transfer[] { PasteElementTransfer.getInstance() });
    }

}