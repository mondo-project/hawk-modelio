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
                                    

package org.modelio.model.browser.handlers.copy;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.copy.PasteElementObject.PasteType;
import org.modelio.core.ui.copy.PasteElementObject;
import org.modelio.core.ui.copy.PasteElementTransfer;
import org.modelio.core.ui.copy.TransferItem;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Operation;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.model.browser.plugin.ModelBrowser;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Pastes the elements from the clipboard.
 */
@objid ("c87aa77a-2500-11e2-ba1c-002564c97630")
public class PasteElementHandler {
    @objid ("c87aa77c-2500-11e2-ba1c-002564c97630")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selection contains only one modifiable element.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("c87aa77e-2500-11e2-ba1c-002564c97630")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, Display currentDisplay) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        // Must and only one element
        List<MObject> selectedElements = getSelectedElements(selection);
        if (selectedElements.size() != 1) {
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
        final List<TransferItem> pastedStereotypeItems = getStereotypesItemsToCopy(items);
        final List<MObject> pastedElements = getElementsToCopy(items, session);
        
        MObject destElement = selectedElements.get(0);
        if (!destElement.getStatus().isModifiable()) {  // cannot paste to the unmodifiable element
            return false;
        }
        for (MObject pasted : pastedElements) {
            switch(pastedObject.getPasteType()) {
            case CUT:
                if (!MTools.getAuthTool().canAddTo(pasted, destElement)) 
                    return false;
                if (!MTools.getMetaTool().canCompose(destElement, pasted, null)) {
                    return false;
                }
                break;
            case COPY:
            default:
                if (!MTools.getAuthTool().canAdd(destElement, pasted.getMClass().getName())) 
                    return false;
                if (!MTools.getMetaTool().canCompose(destElement, pasted, null)) {
                    return false;
                }
                break;
            }
        }
        for (TransferItem item : pastedStereotypeItems) {
            Stereotype stereotype = (Stereotype) this.projectService.getSession().getModel().findByRef(item.getTransferedElementRef(), IModel.NODELETED);
            if (((ModelElement)destElement).getExtension().contains(stereotype)) {
                return false;
            }
            if (!(destElement.getMClass()).hasBase(Metamodel.getMClass(stereotype.getBaseClassName()))) {
                return false;
            }
        }
        return true;
    }

    @objid ("c87aa787-2500-11e2-ba1c-002564c97630")
    private static List<MObject> getSelectedElements(final Object selection) {
        List<MObject> selectedElements = new ArrayList<>();
        if (selection instanceof MObject) {
            selectedElements.add((MObject) selection);
        } else if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() >= 1) {
            Object[] elements = ((IStructuredSelection) selection).toArray();
            for (Object element : elements) {
                if (element instanceof MObject) {
                    selectedElements.add((MObject) element);
                } else if (element instanceof IAdaptable) {
                    final MObject adapter = (MObject) ((IAdaptable) element).getAdapter(MObject.class);
                    if (adapter != null) {
                        selectedElements.add(adapter);
                    }
                }
            }
        }
        return selectedElements;
    }

    /**
     * Cut the currently selected elements.
     * @param selection the current modelio selection.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("c87aa78f-2500-11e2-ba1c-002564c97630")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, Display currentDisplay) {
        ICoreSession session = this.projectService.getSession();
        
        // Must have one element
        List<MObject> selectedElements = getSelectedElements(selection);
        if (selectedElements.size() == 1) {
            final MObject targetElement = selectedElements.get(0);
        
            Clipboard clipboard = new Clipboard(currentDisplay);
            final PasteElementObject pastedObject = (PasteElementObject) clipboard.getContents(PasteElementTransfer.getInstance());
        
            final List<TransferItem> items = pastedObject.getTransferedItems();
            final List<TransferItem> pastedStereotypeItems = getStereotypesItemsToCopy(items);
            final List<MObject> pastedElements = getElementsToCopy(items, session);
            // No elements to paste
            if (pastedElements.isEmpty() && pastedStereotypeItems.isEmpty()) {
                return;
            }
        
            for (MObject element : pastedElements) {
                if (!canBeParentOf(targetElement, element)) {
                    return;
                }
        
                if (element instanceof Parameter) {
                    Parameter parameter = (Parameter) element;
                    if (targetElement instanceof Operation && parameter.getReturned() != null) {
                        Operation targetOperation = (Operation) targetElement;
                        if (targetOperation.getReturn() != null) {
                            return;
                        }
                    }
                }
            }
        
            if (pastedObject.getPasteType() == PasteType.COPY) {
                try (ITransaction transaction = session.getTransactionSupport().createTransaction("Paste")) {
                    List<MObject> copyResult = new ArrayList<>();
        
                    if (pastedElements.size() > 0) {
                        copyResult.addAll(MTools.getModelTool().copyElements(pastedElements, targetElement));
                    }
        
                    // paste stereotypes
                    if (targetElement instanceof ModelElement) {
                        ModelElement selectedModelElement = (ModelElement) targetElement;
                        for (TransferItem item : pastedStereotypeItems) {
                            Stereotype stereotype = (Stereotype) session.getModel().findByRef(item.getTransferedElementRef(), IModel.NODELETED);
                            selectedModelElement.getExtension().add(stereotype);
                        }
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
                        if (!item.getTransferedElementRef().mc.equals("Stereotype")) {                            
                            MRef oldParentRef = item.getOldParentRef();
                            MObject oldParent = session.getModel().findByRef(oldParentRef);
                            if (!pastedElements.isEmpty()) {
                                MTools.getModelTool().moveElements(pastedElements, targetElement, oldParent);
                            }
                        }
                    }
        
                    // paste stereotypes
                    if (targetElement instanceof ModelElement) {
                        ModelElement selectedModelElement = (ModelElement) targetElement;
                        for (TransferItem item : pastedStereotypeItems) {
                            Stereotype stereotype = (Stereotype) session.getModel().findByRef(item.getTransferedElementRef(), IModel.NODELETED);
                            MRef oldParentRef = item.getOldParentRef();
                            ModelElement oldParent = (ModelElement) session.getModel().findByRef(oldParentRef, IModel.NODELETED);
                            oldParent.getExtension().remove(stereotype);
                            selectedModelElement.getExtension().add(stereotype);
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
    }

    @objid ("c87aa797-2500-11e2-ba1c-002564c97630")
    static void reportException(Exception e) {
        // Show an error box
        String title = ModelBrowser.I18N.getMessage("CannotPasteClipboard");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        ModelBrowser.LOG.error(e);
    }

    @objid ("c87aa79a-2500-11e2-ba1c-002564c97630")
    private static List<MObject> getElementsToCopy(List<TransferItem> items, ICoreSession session) {
        List<MObject> elementsToCopy = new ArrayList<>();
        for (TransferItem item : items) {
            MRef transferedElementRef = item.getTransferedElementRef();
        
            MObject transferedElement = session.getModel().findByRef(transferedElementRef, IModel.NODELETED);
            if (!item.getTransferedElementRef().mc.equals("Stereotype")) {
                elementsToCopy.add(transferedElement);
            }
        }
        return elementsToCopy;
    }

    @objid ("c87aa7a4-2500-11e2-ba1c-002564c97630")
    private static List<TransferItem> getStereotypesItemsToCopy(List<TransferItem> items) {
        List<TransferItem> stereotypeItemsToCopy = new ArrayList<>();
        for (TransferItem item : items) {
            if (item.getTransferedElementRef().mc.equals("Stereotype")) {
                stereotypeItemsToCopy.add(item);
            }
        }
        return stereotypeItemsToCopy;
    }

    @objid ("c87aa7ad-2500-11e2-ba1c-002564c97630")
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
    @objid ("c87c2e12-2500-11e2-ba1c-002564c97630")
    private static boolean canBeParentOf(final MObject owner, final MObject composed) {
        return MTools.getMetaTool().canCompose(owner, composed, null);
    }

}
