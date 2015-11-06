package org.modelio.diagram.browser.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.diagram.browser.plugin.DiagramBrowser;
import org.modelio.metamodel.uml.infrastructure.Element;

/**
 * This class handles picking. When picking starts it adds itself as mouse listener (mouse, drack and move) to the tree.
 * When picking is done, it removes itself from mouse listening.
 */
@objid ("0011b91c-0d4f-10c6-842f-001ec947cd2a")
class DiagramBrowserPickingManager implements MouseListener, MouseTrackListener, MouseMoveListener {
    @objid ("0011ed9c-0d4f-10c6-842f-001ec947cd2a")
    private TreeViewer view;

    @objid ("0011fb5c-0d4f-10c6-842f-001ec947cd2a")
    private Cursor defaultCursor;

    @objid ("0012030e-0d4f-10c6-842f-001ec947cd2a")
    private Cursor noCursor;

    @objid ("00120aac-0d4f-10c6-842f-001ec947cd2a")
    private Cursor yesCursor;

    @objid ("285c3244-4ab5-11e2-a4d3-002564c97630")
    private Cursor cursor;

    @objid ("285db8e3-4ab5-11e2-a4d3-002564c97630")
    private IPickingSession pickingSession;

    @objid ("285db8e4-4ab5-11e2-a4d3-002564c97630")
    private ISelection pickingStartSelection;

    @objid ("001210d8-0d4f-10c6-842f-001ec947cd2a")
    public DiagramBrowserPickingManager(TreeViewer view, IPickingSession pickingSession) {
        this.view = view;
        this.pickingSession = pickingSession;
        
        // picking cursor:
        ImageDescriptor pickingDesc = DiagramBrowser.getImageDescriptor("icons/picking_cursor.png");
        
        if (this.cursor == null) {
            this.cursor = new Cursor(Display.getCurrent(), pickingDesc.getImageData(), 10, 9);
        }
        
        // picking cursor no:
        ImageDescriptor pickingNoDesc = DiagramBrowser.getImageDescriptor("icons/picking_cursor_no.png");
        
        if (this.noCursor == null) {
            this.noCursor = new Cursor(Display.getCurrent(), pickingNoDesc.getImageData(), 10, 9);
        }
        
        // picking cursor yes:
        ImageDescriptor pickingYesDesc = DiagramBrowser.getImageDescriptor("icons/picking_cursor_yes.png");
        
        if (this.yesCursor == null) {
            this.yesCursor = new Cursor(Display.getCurrent(), pickingYesDesc.getImageData(), 10, 9);
        }
        
        this.defaultCursor = view.getTree().getCursor();
    }

    @objid ("001225f0-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseDoubleClick(MouseEvent e) {
        // Nothing to do
    }

    @objid ("0012417a-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseDown(MouseEvent e) {
        // Nothing to do
    }

    @objid ("00125db8-0d4f-10c6-842f-001ec947cd2a")
    private static Element getPickedElement(MouseEvent e) {
        Object source = e.getSource();
        
        if (source instanceof Tree) {
            Tree tree = (Tree) source;
            TreeItem item = tree.getItem(new Point(e.x, e.y));
        
            if (item != null) {
                Object data = item.getData();
                if (data instanceof Element) {
                    return (Element) data;
                }
            }
        }
        return null;
    }

    @objid ("00128c0c-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseUp(MouseEvent e) {
        if (e.button == 1) {
            Element selectedElement = getPickedElement(e);
            if (selectedElement != null && this.pickingSession.hover(selectedElement)) {
                this.pickingSession.pick(selectedElement);
            }
        }
    }

    @objid ("0012a8ea-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseEnter(MouseEvent e) {
        this.view.getTree().setCursor(this.cursor);
    }

    @objid ("0012c5b4-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseExit(MouseEvent e) {
        this.view.getTree().setCursor(this.defaultCursor);
    }

    @objid ("0012e3e6-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseHover(MouseEvent e) {
        // Nothing to do
    }

    @objid ("00130128-0d4f-10c6-842f-001ec947cd2a")
    public void dispose() {
        if (this.cursor != null) {
            this.cursor.dispose();
        }
        
        if (this.noCursor != null) {
            this.noCursor.dispose();
        }
        
        if (this.yesCursor != null) {
            this.yesCursor.dispose();
        }
    }

    @objid ("0013106e-0d4f-10c6-842f-001ec947cd2a")
    public void beginPicking() {
        this.view.getTree().addMouseListener(this);
        this.view.getTree().addMouseTrackListener(this);
        this.view.getTree().addMouseMoveListener(this);
        
        // Store the current selection to restore it when the picking ends
        this.pickingStartSelection = this.view.getSelection();
    }

    @objid ("00132a22-0d4f-10c6-842f-001ec947cd2a")
    public void endPicking() {
        // Deactivate picking:
        this.view.getTree().removeMouseListener(this);
        this.view.getTree().removeMouseTrackListener(this);
        this.view.getTree().removeMouseMoveListener(this);
        
        this.view.getTree().setCursor(this.defaultCursor);
        
        // Restore old selection
        this.view.setSelection(this.pickingStartSelection);
        this.pickingStartSelection = null;
    }

    @objid ("00134368-0d4f-10c6-842f-001ec947cd2a")
    @Override
    public void mouseMove(MouseEvent e) {
        Element element = null;
        
        Object source = e.getSource();
        
        if (source instanceof Tree) {
            Tree tree = (Tree) source;
            TreeItem item = tree.getItem(new Point(e.x, e.y));
        
            if (item != null) {
                Object data = item.getData();
                if (data instanceof Element) {
                    element = (Element) data;
                }
            }
        }
        
        if (element != null) {
            if (this.pickingSession.hover(element)) {
                this.view.getTree().setCursor(this.yesCursor);
            } else {
                this.view.getTree().setCursor(this.noCursor);
            }
        } else {
            this.view.getTree().setCursor(this.cursor);
        }
    }

}
