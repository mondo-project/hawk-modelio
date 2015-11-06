package org.modelio.diagram.browser.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * This class handles special mouse "clicks" events for the browser.
 * 
 * <li>double-click is handled by firing an "Activate" application event. <li>alt-click (at mouse up) is handled by firing a
 * "Navigate" application event.
 */
@objid ("000a98a8-0d4f-10c6-842f-001ec947cd2a")
public class DiagramBrowserActivateSender implements MouseListener {
    @objid ("000acf3a-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseDoubleClick(MouseEvent e) {
        Object source = e.getSource();
        if (source instanceof Tree) {
            Tree tree = (Tree) source;
            TreeItem[] items = tree.getSelection();
            if (items.length == 1) {
                fireActivate(items[0].getData());
            }
        }
    }

    @objid ("000aeda8-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseDown(MouseEvent e) {
        // Nothing to do
    }

    @objid ("000b3f10-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseUp(MouseEvent e) {
        if (e.getSource() instanceof Tree) {
            if ((e.stateMask & SWT.ALT) != 0) {
                TreeItem[] items = ((Tree) e.getSource()).getSelection();
                if (items.length == 1) {
                    fireNavigate(items[0].getData());
                }
            }
        }
    }

    @objid ("000b5b30-0d4f-10c6-842f-001ec947cd2a")
    public DiagramBrowserActivateSender() {
    }

    @objid ("000b69d6-0d4f-10c6-842f-001ec947cd2a")
    private void fireActivate(Object objectToActivate) {
        // TODO
    }

    @objid ("000b7cd2-0d4f-10c6-842f-001ec947cd2a")
    private void fireNavigate(Object targetObject) {
        /* TODO
        if (targetObject instanceof Element) {
            O.getDefault().getNavigateService().fireNavigate((Element) targetObject);
        }
        
        if (targetObject instanceof VirtualFolder) {
            if (((VirtualFolder) targetObject).getRepresentedElement() != null) {
                O.getDefault().getNavigateService().fireNavigate(((VirtualFolder) targetObject).getRepresentedElement());
            }
        }
        
        if (targetObject instanceof DiagramRef) {
            O.getDefault().getNavigateService().fireNavigate(((DiagramRef) targetObject).getReferencedDiagram());
        }
        */
    }

}
