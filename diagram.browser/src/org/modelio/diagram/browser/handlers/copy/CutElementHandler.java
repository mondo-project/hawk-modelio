package org.modelio.diagram.browser.handlers.copy;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
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
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Cuts selected elements into the clipboard.
 */
@objid ("b1ee41de-54c7-11e2-ae63-002564c97630")
public class CutElementHandler {
    @objid ("b1ee41e0-54c7-11e2-ae63-002564c97630")
     boolean ctrlFlag;

    @objid ("b1ee41e1-54c7-11e2-ae63-002564c97630")
    @Inject
    protected IProjectService projectService;

    /**
     * Available only when the selected elements are modifiable.
     * @param selection the current modelio selection.
     * @return true if the handler can be executed.
     */
    @objid ("b1ee41e3-54c7-11e2-ae63-002564c97630")
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
    @objid ("b1ee41ec-54c7-11e2-ae63-002564c97630")
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

    @objid ("b1f0a322-54c7-11e2-ae63-002564c97630")
    private static List<MObject> getSelectedElements(final Object selection) {
        List<MObject> selectedElements = new ArrayList<>();
        if (selection instanceof DiagramSet) {
            selectedElements.add((MObject) selection);
        } else if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() >= 1) {
            Object[] elements = ((IStructuredSelection) selection).toArray();
            for (Object element : elements) {
                if (element instanceof DiagramSet) {
                    selectedElements.add((MObject) element);
                }
            }
        }
        return selectedElements;
    }

}
