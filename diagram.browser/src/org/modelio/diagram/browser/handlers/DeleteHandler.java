package org.modelio.diagram.browser.handlers;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.modelio.diagram.browser.model.core.DiagramRef;
import org.modelio.diagram.browser.view.DiagramBrowserView;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("001a35a6-0d4f-10c6-842f-001ec947cd2a")
public class DeleteHandler extends AbstractBrwModelHandler {
    /**
     * Selected object are either:
     * <ul>
     * <li>ModelElementAdapter for diagram and diagram set
     * <li>BrowserDiagramAdapter for diagram reference
     * </ul>
     * 
     * Deleting a diagram or a diagram set is straight forward => treat the delete at the model level. Diagram reference
     * a a little bit more tricky. If the reference has a non-null getReferenceOwner() it actually belongs to a diagram
     * set => the delete can be done.
     */
    @objid ("001a3e02-0d4f-10c6-842f-001ec947cd2a")
    @Override
    protected void doExecute(DiagramBrowserView browserView, List<Object> selectedObjects, ICoreSession session) {
        // DiagramRef are treated for deletion by only removing the reference
        for (Object obj : selectedObjects) {
            if (obj instanceof DiagramRef) {
                DiagramRef ref = (DiagramRef) obj;
                if (ref.getReferenceOwner() != null) {
                    ref.getReferenceOwner().getReferencedDiagram().remove(ref.getReferencedDiagram());
                }
            } else if (obj instanceof MObject) {
                ((MObject) obj).delete();
            }
        }
    }

    /**
     * Rule for enabling a multiple selection => all selected elements are modifiable
     */
    @objid ("001a886c-0d4f-10c6-842f-001ec947cd2a")
    @CanExecute
    public boolean isEnabled() {
        List<Object> selectedObjects = getSelected();
        
        if (selectedObjects.isEmpty())
            return false;
        
        Boolean valid = true; 
        for (Object obj : selectedObjects) {                        
            if (obj instanceof DiagramRef) {
                // diagram ref that has no reference diagram set owner cannot be deleted
                valid = valid && ((DiagramRef) obj).getReferenceOwner() != null;
            } else if (obj instanceof DiagramSet) {
                DiagramSet set = (DiagramSet) obj;
                if (set.getParent() == null) {
                    // Root DiagramSet can't be deleted
                    return false;
                } else {
                    // Standard deletion rules
                    valid = valid && ((Element) obj).getStatus().isModifiable();
                }
            } else if (obj instanceof MObject) {
                // Element must be modifiable
                valid = valid && ((MObject) obj).getStatus().isModifiable();
            } else {
                return false;
            }
            if (!valid) {
                break;
            }
        }
        return valid;
    }

}
