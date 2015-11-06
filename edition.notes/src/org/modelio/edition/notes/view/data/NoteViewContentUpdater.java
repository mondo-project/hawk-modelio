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
                                    

package org.modelio.edition.notes.view.data;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

/**
 * This class update the AnnotationContentPanel
 */
@objid ("26f1cdc1-186f-11e2-bc4e-002564c97630")
public class NoteViewContentUpdater implements ISelectionChangedListener {
    @objid ("26f1cdc3-186f-11e2-bc4e-002564c97630")
    private NoteViewContentPanel contentPanel = null;

    @objid ("26f1cdc4-186f-11e2-bc4e-002564c97630")
    private ModelElement element;

    @objid ("26f1cdc5-186f-11e2-bc4e-002564c97630")
    public NoteViewContentUpdater(final NoteViewContentPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    @objid ("26f1cdc9-186f-11e2-bc4e-002564c97630")
    @Override
    public void selectionChanged(final SelectionChangedEvent event) {
        ISelection selection = event.getSelection();
        
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        
            Object[] selectedElements = structuredSelection.toArray();
        
            if (selectedElements.length == 1) {
                Object selectedElement = selectedElements[0];
                if (selectedElement instanceof ModelElement) {
                    this.element = (ModelElement) selectedElement;
                    this.contentPanel.setInput(this.element);
                    return;
                } else {
                    this.element = null;
                    this.contentPanel.setInput(null);
                    return;
                }
            }
        }
        this.contentPanel.setInput(null);
    }

}
