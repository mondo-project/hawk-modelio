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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Display;
import org.modelio.vcore.session.api.model.change.IElementDeletedEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Manage the Selection history in the Explorer.
 */
@objid ("a8f3725b-4603-11e2-960d-002564c97630")
public class SelectionHistory implements IModelChangeListener {
    @objid ("ad8afbe4-4603-11e2-960d-002564c97630")
    protected boolean isForward;

    @objid ("ad89753c-4603-11e2-960d-002564c97630")
    private static final SelectionHistory INSTANCE = new SelectionHistory ();

    @objid ("ad8afbdc-4603-11e2-960d-002564c97630")
    protected LinkedList<MObject> navigationHistory = new LinkedList<>();

    @objid ("ad8afbdf-4603-11e2-960d-002564c97630")
    protected ListIterator<MObject> currentSelection;

    @objid ("ad8afbe2-4603-11e2-960d-002564c97630")
    private SelectionHistory() {
        // Private c'tor
    }

    @objid ("ad8afbe5-4603-11e2-960d-002564c97630")
    public static SelectionHistory getInstance() {
        return INSTANCE;
    }

    /**
     * On selection we add the selected element in the History list. If the current selection is not the last element of
     * the history all the elements that are after the current element are removed from the history.
     */
    @objid ("ad8afbe9-4603-11e2-960d-002564c97630")
    public void update(List<MObject> selectedElements) {
        // We consider the selection only if there is one element selected.
        if (selectedElements.size() != 1) {
            return;
        }
        
        MObject element = selectedElements.get(0);
        
        // Compute index for the current element
        int nextIndex = this.currentSelection != null ? this.currentSelection.nextIndex() : -1;
        if (this.isForward) {
            nextIndex --;
        }
        
        // Ignore update if the selected element is already the current one
        if (nextIndex >= 0 && nextIndex < this.navigationHistory.size() && element.equals(this.navigationHistory.get(nextIndex))) {
            return;
        }
        
        // Remove elements that are after the current selection in the history:
        if (this.currentSelection != null) {
            int pos = nextIndex;
            this.removeAfter(pos);
        }
        
        // Add the selected element to the history:
        
        // We add it only if it is not already the last element of the history.
        if (this.navigationHistory.size() == 0 ||
                this.navigationHistory.size() > 0 &&
                !this.navigationHistory.peekLast().equals(element)) {
            addElement(element);
        }
        
        // Set the iterator to the added element.
        int index = this.navigationHistory.lastIndexOf(element);
        this.currentSelection = this.navigationHistory.listIterator(index);
        
        this.isForward = false;
    }

    @objid ("ad8afbef-4603-11e2-960d-002564c97630")
    private void addElement(MObject element) {
        if (this.navigationHistory.size() == 250) {
            this.navigationHistory.removeFirst();
        }
        this.navigationHistory.offerLast(element);
    }

    @objid ("ad8c827c-4603-11e2-960d-002564c97630")
    public void selectPreviousSelection(BrowserView view) {
        if (this.isForward && this.currentSelection.hasPrevious()) {
            this.currentSelection.previous();
            this.isForward = false;
        }
        
        if (this.currentSelection.hasPrevious()) {
            MObject element = this.currentSelection.previous();
            if (element != null) {
                view.selectElement(element);
            }
        }
    }

    @objid ("ad8c827f-4603-11e2-960d-002564c97630")
    public void selectNextSelection(BrowserView view) {
        if (!this.isForward  && this.currentSelection.hasNext()) {
            this.currentSelection.next();
            this.isForward = true;
        }
        
        if (this.currentSelection.hasNext()) {
            MObject element = this.currentSelection.next();
            if (element != null) {
                view.selectElement(element);
            }
        }
    }

    @objid ("ad8c8282-4603-11e2-960d-002564c97630")
    private void removeAfter(int index) {
        while (this.navigationHistory.size() > index + 1) {
            this.navigationHistory.removeLast();
        }
    }

    @objid ("ad8c8285-4603-11e2-960d-002564c97630")
    @Override
    public void modelChanged(IModelChangeEvent event) {
        final List<IElementDeletedEvent> deletedEvents = event.getDeleteEvents();
        
        // Re enter the UI thread
        Display display = Display.getDefault();
        if (display != null) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    if (deletedEvents.size() > 0) {
                        for (IElementDeletedEvent deletedEvent : deletedEvents) {
                            MObject element = deletedEvent.getDeletedElement();
                            SelectionHistory.this.navigationHistory.remove(element);
                        }
        
                        // Set the iterator to the last element.
                        int index = SelectionHistory.this.navigationHistory.size();
                        SelectionHistory.this.currentSelection = SelectionHistory.this.navigationHistory.listIterator(index);
                        SelectionHistory.this.isForward = false;
                    }
                }
            });
        }
    }

    @objid ("ad8c8289-4603-11e2-960d-002564c97630")
    public void clear() {
        this.navigationHistory.clear();
        this.currentSelection = null;
        this.isForward = false;
    }

    @objid ("ad8c828b-4603-11e2-960d-002564c97630")
    public boolean hasNext() {
        if (this.currentSelection == null) {
            return false;
        }
        
        int nextIndex = this.currentSelection.nextIndex();
        if (this.isForward) {
            nextIndex --;
        }
        return nextIndex >= 0 && nextIndex < this.navigationHistory.size() - 1;
    }

    @objid ("ad8c828f-4603-11e2-960d-002564c97630")
    public boolean hasPrevious() {
        if (this.currentSelection == null) {
            return false;
        }
        return this.currentSelection.hasPrevious();
    }

}
