package org.modelio.diagram.browser.handlers;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.diagram.browser.model.core.DiagramRef;
import org.modelio.diagram.browser.model.core.VirtualFolder;
import org.modelio.diagram.browser.view.DiagramBrowserView;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Handler for the "select in explorer" menu item.
 */
@objid ("4f652525-2d96-445a-8e8a-4cab1d0cddb9")
public class SelectInExplorerHandler extends AbstractBrwModelHandler {
    @objid ("a321bd45-c59d-4f93-b4f9-c1d4aea8b911")
    @Inject
     IModelioNavigationService navigationService;

    @objid ("97634cfd-1566-4a0f-a570-52fb1af17d39")
    @Override
    protected void doExecute(DiagramBrowserView browserView, List<Object> selectedObjects, ICoreSession session) {
        this.navigationService.fireNavigate(getSelection(selectedObjects));
    }

    @objid ("cc098894-a8db-4123-8c04-2cef3e02257e")
    @CanExecute
    public boolean canExecute() {
        List<Object> selectedObjects = getSelected();
        
        if (selectedObjects.isEmpty())
            return false;
        
        for (Object selectedObject : selectedObjects) {
            if (selectedObject instanceof DiagramSet) {
                return false;
            }
            if (selectedObject instanceof DiagramRef) {
                return true;
            }
            if (selectedObject instanceof VirtualFolder && ((VirtualFolder) selectedObject).getRepresentedElement() != null) {
                return ((VirtualFolder) selectedObject).getRepresentedElement().isValid();
            }
            if (!(selectedObject instanceof MObject)) {
                return false;
            } else {
                if (!((MObject) selectedObject).isValid()) {
                    return false;
                }
            }
        }
        return true;
    }

    @objid ("f820bc34-b8e0-4e24-88bc-ae123cd874f6")
    public List<MObject> getSelection(List<Object> selectedObjects) {
        List<MObject> selectedElements = new ArrayList<>();
        for (Object selectedObject : selectedObjects) {
            if (selectedObject instanceof DiagramRef) {
                selectedElements.add(((DiagramRef) selectedObject).getReferencedDiagram());
            } else if (selectedObject instanceof VirtualFolder) {
                selectedElements.add(((VirtualFolder) selectedObject).getRepresentedElement());
            } else if (selectedObject instanceof MObject && !(selectedObject instanceof DiagramSet)) { //normally no need to test
                selectedElements.add((MObject) selectedObject);
            }
        }
        return selectedElements;
    }

}
