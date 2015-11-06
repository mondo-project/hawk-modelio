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
                                    

package org.modelio.model.browser.views;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
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
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.model.browser.plugin.ModelBrowser;
import org.modelio.model.browser.views.treeview.ModelBrowserPanelProvider;

/**
 * This class handles picking. When picking starts it adds itself as mouse listener (mouse, drack and move) to the tree.
 * When picking is done, it removes itself from mouse listening.
 */
@objid ("7f07402b-16a3-11e2-aa0d-002564c97630")
class BrowserPickingManager implements MouseListener, MouseTrackListener, MouseMoveListener {
    @objid ("840a454c-16a3-11e2-aa0d-002564c97630")
    private ModelBrowserPanelProvider view;

    @objid ("840a454d-16a3-11e2-aa0d-002564c97630")
    private Cursor cursor;

    @objid ("840a454e-16a3-11e2-aa0d-002564c97630")
    private Cursor defaultCursor;

    @objid ("840a454f-16a3-11e2-aa0d-002564c97630")
    private Cursor noCursor;

    @objid ("840a4550-16a3-11e2-aa0d-002564c97630")
    private Cursor yesCursor;

    @objid ("840a4551-16a3-11e2-aa0d-002564c97630")
    private IPickingSession pickingSession;

    @objid ("16657744-16da-11e2-aa0d-002564c97630")
    private ISelection pickingStartSelection;

    @objid ("840a4552-16a3-11e2-aa0d-002564c97630")
    public BrowserPickingManager(ModelBrowserPanelProvider view, IPickingSession pickingSession) {
        this.view = view;
        this.pickingSession = pickingSession;
        
        // picking cursor:
        ImageDescriptor pickingDesc = ModelBrowser.getImageDescriptor("icons/picking_cursor.png");
        
        if (this.cursor == null) {
            this.cursor = new Cursor(Display.getCurrent(), pickingDesc.getImageData(), 10, 9);
        }
        
        // picking cursor no:
        ImageDescriptor pickingNoDesc = ModelBrowser.getImageDescriptor("icons/picking_cursor_no.png");
        
        if (this.noCursor == null) {
            this.noCursor = new Cursor(Display.getCurrent(), pickingNoDesc.getImageData(), 10, 9);
        }
        
        // picking cursor yes:
        ImageDescriptor pickingYesDesc = ModelBrowser.getImageDescriptor("icons/picking_cursor_yes.png");
        
        if (this.yesCursor == null) {
            this.yesCursor = new Cursor(Display.getCurrent(), pickingYesDesc.getImageData(), 10, 9);
        }
        
        this.defaultCursor = view.getPanel().getTree().getCursor();
    }

    @objid ("840a4556-16a3-11e2-aa0d-002564c97630")
    @Override
    public void mouseDoubleClick(MouseEvent e) {
        // Nothing to do
    }

    @objid ("840a455a-16a3-11e2-aa0d-002564c97630")
    @Override
    public void mouseDown(MouseEvent e) {
        // Nothing to do
    }

    @objid ("840a455e-16a3-11e2-aa0d-002564c97630")
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

    @objid ("840a4563-16a3-11e2-aa0d-002564c97630")
    @Override
    public void mouseUp(MouseEvent e) {
        if (e.button == 1) {
            Element selectedElement = getPickedElement(e);
            if (selectedElement != null && this.pickingSession.hover(selectedElement)) {
                this.pickingSession.pick(selectedElement);
            }
        }
    }

    @objid ("840a4567-16a3-11e2-aa0d-002564c97630")
    @Override
    public void mouseEnter(MouseEvent e) {
        this.view.getPanel().getTree().setCursor(this.cursor);
    }

    @objid ("840a456b-16a3-11e2-aa0d-002564c97630")
    @Override
    public void mouseExit(MouseEvent e) {
        this.view.getPanel().getTree().setCursor(this.defaultCursor);
    }

    @objid ("840a456f-16a3-11e2-aa0d-002564c97630")
    @Override
    public void mouseHover(MouseEvent e) {
        // Nothing to do
    }

    @objid ("840a4573-16a3-11e2-aa0d-002564c97630")
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

    @objid ("840a4575-16a3-11e2-aa0d-002564c97630")
    public void beginPicking() {
        this.view.getPanel().getTree().addMouseListener(this);
        this.view.getPanel().getTree().addMouseTrackListener(this);
        this.view.getPanel().getTree().addMouseMoveListener(this);
        
        // Store the current selection to restore it when the picking ends
        this.pickingStartSelection = this.view.getPanel().getSelection();
    }

    @objid ("840a4577-16a3-11e2-aa0d-002564c97630")
    public void endPicking() {
        // Deactivate picking:
        this.view.getPanel().getTree().removeMouseListener(this);
        this.view.getPanel().getTree().removeMouseTrackListener(this);
        this.view.getPanel().getTree().removeMouseMoveListener(this);
        
        this.view.getPanel().getTree().setCursor(this.defaultCursor);
        
        // Restore old selection
        this.view.getPanel().setSelection(this.pickingStartSelection);
        this.pickingStartSelection = null;
    }

    @objid ("840a4579-16a3-11e2-aa0d-002564c97630")
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
                this.view.getPanel().getTree().setCursor(this.yesCursor);
            } else {
                this.view.getPanel().getTree().setCursor(this.noCursor);
            }
        } else {
            this.view.getPanel().getTree().setCursor(this.cursor);
        }
    }

}
