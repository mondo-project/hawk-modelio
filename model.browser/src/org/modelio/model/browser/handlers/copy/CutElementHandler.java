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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.copy.PasteElementObject.PasteType;
import org.modelio.core.ui.copy.PasteElementObject;
import org.modelio.core.ui.copy.PasteElementTransfer;
import org.modelio.core.ui.copy.TransferItem;
import org.modelio.metamodel.analyst.PropertyContainer;
import org.modelio.metamodel.mda.Project;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Cuts selected elements into the clipboard.
 */
@objid ("c87920e1-2500-11e2-ba1c-002564c97630")
public class CutElementHandler {
    @objid ("c87920e2-2500-11e2-ba1c-002564c97630")
     boolean ctrlFlag;

    @objid ("c87920e3-2500-11e2-ba1c-002564c97630")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selected elements are modifiable.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("c87920e5-2500-11e2-ba1c-002564c97630")
    @CanExecute
    public final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return false;
        }
        
        // Must have at least an element
        List<MObject> selectedElements = getSelectedElements(selection);
        if (selectedElements.isEmpty()) {
            return false;
        }
        
        for (MObject element : selectedElements) {
            MObject owner = element.getCompositionOwner();            
            if (owner == null || owner instanceof Project) {
                return false;   // cannot delete if its owner is the root which means it is created by default
            }
            if (element instanceof PropertyContainer) {
                return false;
            }
            if (!element.getStatus().isModifiable()) {
                return false;
            }                    
            //final MObject owner = element.getCompositionOwner();
            // TODO if (!AuthExpert.canRemoveFrom(element, owner))
        }
        return true;
    }

    /**
     * Cut the currently selected elements.
     * @param selection the current modelio selection.
     * @param currentDisplay the display Modelio runs into.
     */
    @objid ("c87920ee-2500-11e2-ba1c-002564c97630")
    @Execute
    public final void execute(@Named(IServiceConstants.ACTIVE_SELECTION) final Object selection, Display currentDisplay) {
        // Sanity checks
        if (this.projectService.getSession() == null) {
            return;
        }
        
        // Must have at least an element
        List<MObject> selectedElements = getSelectedElements(selection);
        if (selectedElements.isEmpty()) {
            return;
        }
        
        PasteElementObject toCopy = new PasteElementObject(PasteType.CUT);
        
        for (MObject element : selectedElements) {
            toCopy.addTransferedItems(new TransferItem(element, element.getCompositionOwner()));
        }
        
        Clipboard clipboard= new Clipboard(currentDisplay);
        clipboard.setContents(new Object[] { toCopy }, new Transfer[] { PasteElementTransfer.getInstance() });
    }

    @objid ("c87aa772-2500-11e2-ba1c-002564c97630")
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

}
