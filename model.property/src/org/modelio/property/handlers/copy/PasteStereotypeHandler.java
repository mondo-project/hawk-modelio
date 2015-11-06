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
                                    

package org.modelio.property.handlers.copy;

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
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.PropertyView;
import org.modelio.property.plugin.ModelProperty;
import org.modelio.property.ui.ModelPropertyPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Pastes the stereotype(s) from the clipboard.
 */
@objid ("e92154f6-7cd2-4f74-ad51-8d8099e84cfc")
public class PasteStereotypeHandler {
    @objid ("43bce7c8-7dbd-4fa7-bb6d-9af4628c2f70")
    @Inject
    protected IProjectService projectService;

    /**
     * @param part the current active part.
     * @return true if the handler can be executed.
     */
    @objid ("41188712-06d3-403f-9152-67e73478745d")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
        if (!(part.getObject() instanceof PropertyView)) {
            return false;
        }
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        if (propertyPanel == null) {
            return false;
        }
        Element destElement = propertyPanel.getInput();
        if (destElement == null || !destElement.isModifiable() || !(destElement instanceof ModelElement)) {
            return false;
        }
        
        Clipboard clipboard = new Clipboard(currentDisplay);
        final PasteElementObject pastedObject = (PasteElementObject) clipboard.getContents(PasteElementTransfer.getInstance());
        // There is no data corresponding to PasteElementTransfer
        if (pastedObject == null) {
            return false;
        }
        
        final List<TransferItem> items = pastedObject.getTransferedItems();
        final List<TransferItem> pastedStereotypeItems = getStereotypesItemsToCopy(items);
        
        if (pastedStereotypeItems.isEmpty()) {
            return false;
        }
        for (TransferItem item : pastedStereotypeItems) {
            if (((ModelElement)destElement).isStereotyped(null, item.getTransferedElementRef().name)) {
                return false;
            }
            Stereotype stereotype = (Stereotype) this.projectService.getSession().getModel().findByRef(item.getTransferedElementRef());
            if (!(destElement.getMClass()).hasBase(Metamodel.getMClass(stereotype.getBaseClassName()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Paste the currently selected stereotype(s).
     * @param part the current active part.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("170d3810-ce6d-4908-a5b7-f0ec70a92e91")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_PART) final MPart part, Display currentDisplay) {
        ICoreSession session = this.projectService.getSession();
        ModelPropertyPanelProvider propertyPanel = ((PropertyView) part.getObject()).getPanel();
        final MObject targetElement = propertyPanel.getInput();
        
        Clipboard clipboard = new Clipboard(currentDisplay);
        final PasteElementObject pastedObject = (PasteElementObject) clipboard.getContents(PasteElementTransfer.getInstance());
        
        final List<TransferItem> items = pastedObject.getTransferedItems();
        final List<TransferItem> pastedStereotypeItems = getStereotypesItemsToCopy(items);
        
        if (pastedObject.getPasteType() == PasteType.COPY) {
            try (ITransaction transaction = session.getTransactionSupport().createTransaction("Paste stereotypes")) {
                // paste stereotypes
                ModelElement selectedModelElement = (ModelElement) targetElement;
                for (TransferItem item : pastedStereotypeItems) {
                    Stereotype stereotype = (Stereotype) session.getModel().findByRef(item.getTransferedElementRef());
                    selectedModelElement.getExtension().add(stereotype);
                }
                transaction.commit();
            } catch (Exception e) {
                // Should catch InvalidModelManipulationException to display a popup box, but it
                // is not a RuntimeException.
                reportException(e);
            }
        } else if (pastedObject.getPasteType() == PasteType.CUT) {
            // cannot cut/paste an element onto itself or a child
            try (ITransaction transaction = session.getTransactionSupport().createTransaction("Cut stereotypes")) {
                // paste stereotypes
                ModelElement selectedModelElement = (ModelElement) targetElement;
                for (TransferItem item : pastedStereotypeItems) {
                    Stereotype stereotype = (Stereotype) session.getModel().findByRef(item.getTransferedElementRef());
                    MRef oldParentRef = item.getOldParentRef();
                    ModelElement oldParent = (ModelElement) session.getModel().findByRef(oldParentRef);
                    oldParent.getExtension().remove(stereotype);
                    selectedModelElement.getExtension().add(stereotype);
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

    @objid ("2c1bbf15-9014-41a4-8277-75f4292a27d8")
    static void reportException(Exception e) {
        // Show an error box
        String title = ModelProperty.I18N.getMessage("CannotPasteClipboard");
        
        MessageDialog.openError(null, title, e.getLocalizedMessage());
        
        ModelProperty.LOG.error(e);
    }

    @objid ("28924db8-511a-4a64-bde8-5633a9cbe660")
    private static List<TransferItem> getStereotypesItemsToCopy(List<TransferItem> items) {
        List<TransferItem> stereotypeItemsToCopy = new ArrayList<>();
        for (TransferItem item : items) {
            if (item.getTransferedElementRef().mc.equals("Stereotype")) {
                stereotypeItemsToCopy.add(item);
            }
        }
        return stereotypeItemsToCopy;
    }

}
