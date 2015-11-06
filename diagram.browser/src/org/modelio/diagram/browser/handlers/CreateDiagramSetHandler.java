package org.modelio.diagram.browser.handlers;

import java.util.List;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.browser.view.DiagramBrowserView;
import org.modelio.gproject.model.IElementNamer;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.diagrams.DiagramSet;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Handler creating a DiagramSet under another DiagramSet.
 */
@objid ("001965b8-0d4f-10c6-842f-001ec947cd2a")
public class CreateDiagramSetHandler extends AbstractBrwModelHandler {
    @objid ("c1e259d6-4ac6-11e2-a4d3-002564c97630")
    @Inject
    private IMModelServices modelService;

    @objid ("00196fae-0d4f-10c6-842f-001ec947cd2a")
    @Override
    protected void doExecute(DiagramBrowserView browserView, List<Object> selectedObjects, ICoreSession session) {
        final Object object = (selectedObjects.isEmpty()) ? null : selectedObjects.get(0);
        
        if (object instanceof DiagramSet) {
            DiagramSet parentSet = (DiagramSet) object;
            DiagramSet newSet = this.modelService.getModelFactory().createDiagramSet();
            newSet.setParent(parentSet);
            IElementNamer namer = this.modelService.getElementNamer();
            newSet.setName(namer.getUniqueName(newSet));
            postExpandAndSelect(browserView, newSet, parentSet);
        }
    }

    @objid ("0019b446-0d4f-10c6-842f-001ec947cd2a")
    @CanExecute
    public boolean isEnabled() {
        List<Object> selectedObject = getSelected();
        
        if (selectedObject.size() != 1 ) {
            return false;
        }
        return selectedObject.get(0) instanceof DiagramSet;
    }

    @objid ("0019d994-0d4f-10c6-842f-001ec947cd2a")
    private void postExpandAndSelect(final DiagramBrowserView browserView, final DiagramSet diagramSet, final Element parent) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run()
            {
                browserView.getComposite().getPanel().expandToLevel(parent, 1);
                browserView.getComposite().getPanel().refresh(parent, true);
                browserView.getComposite().getPanel().setSelection(new StructuredSelection(diagramSet), true);
                browserView.edit(diagramSet);
            }
        });
    }

    @objid ("ca3eb80c-4b58-11e2-a4d3-002564c97630")
    public CreateDiagramSetHandler() {
        super();
    }

}
