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
                                    

package org.modelio.edition.notes.view.tree;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("26f8f18c-186f-11e2-bc4e-002564c97630")
public class ElementEditorActivationStrategy extends ColumnViewerEditorActivationStrategy {
    @objid ("26f8f18d-186f-11e2-bc4e-002564c97630")
     int time = 0;

    @objid ("26f8f18e-186f-11e2-bc4e-002564c97630")
     Element selectedElement = null;

    @objid ("26f8f18f-186f-11e2-bc4e-002564c97630")
    public ElementEditorActivationStrategy(ColumnViewer viewer) {
        super(viewer);
    }

    @objid ("26f8f192-186f-11e2-bc4e-002564c97630")
    @Override
    protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
        if (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2) {
            return true;
        }
        
        if (event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC) {
            return true;
        }
        
        if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION) {
            // Retrieve selected element:
            ModelElement sourceElement = null;
        
            Object eventSource = event.getSource();
            if (eventSource instanceof ViewerCell) {
                ViewerCell sourceCell = (ViewerCell) eventSource;
                Object sourceObject = sourceCell.getElement();
                if (sourceObject instanceof Constraint || sourceObject instanceof ExternDocument) {
                    sourceElement = (ModelElement)sourceObject;
                } else {
                    return false;
                }
            }
        
            if (this.time == 0) {
                this.time = event.time;
                this.selectedElement = sourceElement;
                return false;
            } else {
                int delta = event.time - this.time;
        
                if (delta > 300 && delta < 1000 && this.selectedElement == sourceElement) {
                    this.time = 0;
                    this.selectedElement = sourceElement;
                    return true;
                } else {
                    this.time = event.time;
                    this.selectedElement = sourceElement;
                    return false;
                }
            }
        }
        return false;
    }

}
