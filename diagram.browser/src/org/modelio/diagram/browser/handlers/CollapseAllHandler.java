package org.modelio.diagram.browser.handlers;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.modelio.diagram.browser.view.DiagramBrowserView;

@objid ("a9e805e2-54c7-11e2-ae63-002564c97630")
public class CollapseAllHandler {
    /**
     * Collapses all nodes of the DiagramBrowserView's tree, starting with the root.
     * @param part a {@link BrowserView} part.
     */
    @objid ("b1e97f07-54c7-11e2-ae63-002564c97630")
    @Execute
    public static final void execute(MPart part) {
        DiagramBrowserView view = (DiagramBrowserView) part.getObject();
        view.collapseAll();
    }

    @objid ("a36fc690-a43f-4fbe-9f8d-356c21e91acd")
    @CanExecute
    public boolean canExecute(MPart part) {
        if (part.getObject() == null) {
            return false;
        }
        return (part.getObject() instanceof DiagramBrowserView);
    }

}
