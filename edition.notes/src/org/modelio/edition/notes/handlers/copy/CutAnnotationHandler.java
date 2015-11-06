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
 * Cuts selected annotation(s) into the clipboard.
 */
@objid ("65c0aea4-4ece-408b-bbb4-1cd600ead190")
public class CutAnnotationHandler {
    @objid ("05ac3c5d-b435-4a6c-8db1-981e7adf2451")
     boolean ctrlFlag;

    @objid ("4842c180-e06a-450c-8aef-bc1d47b5145e")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selected elements are modifiable.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("7ffd9d53-937d-4303-ac08-25ca37564575")
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
     * Cut the currently selected elements.
     * @param selection the current modelio selection.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("daaeef67-3284-4439-9d48-77bc3f51add8")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
        NotesPanelProvider notesPanel = ((NotesView) part.getObject()).getNotesPanel();
        
        List<ModelElement> selectedElements = notesPanel.getSelectedNoteItems();
        
        PasteElementObject toCopy = new PasteElementObject(PasteType.CUT);
        
        for (MObject element : selectedElements) {
            toCopy.addTransferedItems(new TransferItem(element, element.getCompositionOwner()));
        }
        
        Clipboard clipboard= new Clipboard(currentDisplay);
        clipboard.setContents(new Object[] { toCopy }, new Transfer[] { PasteElementTransfer.getInstance() });
    }

}
