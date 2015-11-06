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

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.copy.PasteElementObject.PasteType;
import org.modelio.core.ui.copy.PasteElementObject;
import org.modelio.core.ui.copy.PasteElementTransfer;
import org.modelio.core.ui.copy.TransferItem;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.edition.notes.view.NotesView;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Pastes the annotation(s) from the clipboard.
 */
@objid ("6a94ce26-686e-452d-8025-ca16be0183f9")
public class PasteAnnotationHandler {
    @objid ("72af845a-3fd8-4159-80f0-a10086bb8081")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selection contains only one modifiable element.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("604025a2-ef00-4d64-b9a9-4e97bb7ee6b7")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
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
        
        ModelElement destElement = notesPanel.getInput();
        if (destElement == null || !destElement.getStatus().isModifiable()) { // cannot paste to the unmodifiable element
            return false;
        }
        
        Clipboard clipboard = new Clipboard(currentDisplay);
        final PasteElementObject pastedObject = (PasteElementObject) clipboard.getContents(PasteElementTransfer.getInstance());
        // There is no data corresponding to PasteElementTransfer
        if (pastedObject == null) {
            return false;
        }
            
        final ICoreSession session = this.projectService.getSession();
        
        final List<TransferItem> items = pastedObject.getTransferedItems();
        final List<MObject> pastedElements = getElementsToCopy(items, session);
        
        for (MObject pasted : pastedElements) {
            switch(pastedObject.getPasteType()) {
            case CUT:
                if (!MTools.getAuthTool().canAddTo(pasted, destElement)) {                    
                    return false;
                }
                if (!MTools.getMetaTool().canCompose(destElement, pasted, null)) {
                    return false;
                }
                break;
            case COPY:
            default:
                if (!MTools.getAuthTool().canAdd(destElement, pasted.getMClass().getName()))  {                    
                    return false;
                }
                if (!MTools.getMetaTool().canCompose(destElement, pasted, null)) {
                    return false;
                }
                break;
            }
        }
        return true;
    }

    /**
     * Cut the currently selected elements.
     * @param selection the current modelio selection.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("be2f6280-9d4f-4e3e-b24f-877ff3dec5e2")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
        ICoreSession session = this.projectService.getSession();
        
        NotesPanelProvider notesPanel = ((NotesView) part.getObject()).getNotesPanel();
        
        final MObject targetElement = notesPanel.getInput();
        
        Clipboard clipboard = new Clipboard(currentDisplay);
        final PasteElementObject pastedObject = (PasteElementObject) clipboard.getContents(PasteElementTransfer.getInstance());
        
        final List<TransferItem> items = pastedObject.getTransferedItems();
        final List<MObject> pastedElements = getElementsToCopy(items, session);       
        
        for (MObject element : pastedElements) {
            if (!canBeParentOf(targetElement, element)) {
                return;
            }
        }
        
        if (pastedObject.getPasteType() == PasteType.COPY) {
            try (ITransaction transaction = session.getTransactionSupport().createTransaction("Paste")) {
                List<MObject> copyResult = new ArrayList<>();
        
                if (pastedElements.size() > 0) {
                    copyResult.addAll(MTools.getModelTool().copyElements(pastedElements, targetElement));
                }        
                transaction.commit();
            } catch (Exception e) {
                // Should catch InvalidModelManipulationException to display a popup box, but it
                // is not a RuntimeException.
                reportException(e);
            }
        } else if (pastedObject.getPasteType() == PasteType.CUT) {
            // cannot cut/paste an element onto itself or a child
            for (MObject element : pastedElements) {
                if (element.equals(targetElement) || isParentOf(element, targetElement)) {
                    return;
                }
            }
        
            try (ITransaction transaction = session.getTransactionSupport().createTransaction("Cut")) {
                for (TransferItem item : items) {
                    MRef oldParentRef = item.getOldParentRef();
                    MObject oldParent = session.getModel().findByRef(oldParentRef);
                    if (!pastedElements.isEmpty()) {
                        MTools.getModelTool().moveElements(pastedElements, targetElement, oldParent);
                    }
                }
        
                transaction.commit();
        
                // Keep the elements in the clipboard, but as a copy
                pastedObject.setPasteType(PasteType.COPY);
                clipboard.setContents(new Object[] { pastedObject }, new Transfer[] { PasteElementTransfer.getInstance() });
            } catch (Exception e) {
                reportException(e);
            }
        }
    }

    @objid ("ad096c20-9fca-4b7d-af5d-cae6b237acbc")
    static void reportException(Exception e) {
        // Show an error box
        String title = EditionNotes.I18N.getMessage("CannotPasteClipboard");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        EditionNotes.LOG.error(e);
    }

    @objid ("9b22049c-7cce-4a32-a5ce-af22951273f9")
    private static List<MObject> getElementsToCopy(List<TransferItem> items, ICoreSession session) {
        List<MObject> elementsToCopy = new ArrayList<>();
        for (TransferItem item : items) {
            MRef transferedElementRef = item.getTransferedElementRef();
        
            MObject transferedElement = session.getModel().findByRef(transferedElementRef);
            elementsToCopy.add(transferedElement);
        }
        return elementsToCopy;
    }

    @objid ("fd661e9e-8f63-4b68-bffa-1e3ff878952c")
    private boolean isParentOf(MObject parentCandidate, MObject element) {
        MObject parent = element.getCompositionOwner();
        
        if (parent == null) {
            return false;
        }
        
        if (parentCandidate.equals(parent)) {
            return true;
        }
        return isParentOf(parentCandidate, parent);
    }

    /**
     * Tells whether 'child' can be owned by 'parent'.
     * @param owner The future parent element
     * @param composed a child element
     * @return true only if parent can contain the child.
     */
    @objid ("f5c03ebd-ae4b-45fb-bd5f-7218df9d2f12")
    private static boolean canBeParentOf(final MObject owner, final MObject composed) {
        return MTools.getMetaTool().canCompose(owner, composed, null);
    }

}
