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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.TreeItem;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.impl.CoreSession;

@objid ("26f8f198-186f-11e2-bc4e-002564c97630")
public class ElementNameModifier implements ICellModifier, KeyListener {
    @objid ("26f8f199-186f-11e2-bc4e-002564c97630")
    private final NotesPanelProvider noteView;

    @objid ("26f8f19b-186f-11e2-bc4e-002564c97630")
    public ElementNameModifier(NotesPanelProvider noteView) {
        this.noteView = noteView;
    }

    @objid ("26f8f19f-186f-11e2-bc4e-002564c97630")
    @Override
    public boolean canModify(Object object, String property) {
        return (this.noteView.getSelectedNoteItems().size() == 1 && isEditable(object));
    }

    @objid ("26f8f1a6-186f-11e2-bc4e-002564c97630")
    @Override
    public Object getValue(Object object, String property) {
        if (object instanceof ModelElement) {
            return ((ModelElement) object).getName();
        }
        return "";
    }

    @objid ("26f8f1ad-186f-11e2-bc4e-002564c97630")
    @Override
    public void modify(Object object, String property, Object value) {
        if (object instanceof TreeItem) {
            TreeItem item = (TreeItem) object;
            Object data = item.getData();
        
            if (data instanceof ModelElement) {
                ModelElement element = (ModelElement) data;
                if (!element.getName().equals(value)) {
                    final CoreSession session = CoreSession.getSession(element);
                    try (ITransaction transaction = session.getTransactionSupport().createTransaction("Rename")) {
                        element.setName((String) value);
                        transaction.commit();
                    } catch (Exception e) {
                        EditionNotes.LOG.error(e);
                    }
                }
            }
        
        }
        return;
    }

    @objid ("26f8f1b4-186f-11e2-bc4e-002564c97630")
    private boolean isEditable(Object object) {
        if ((object instanceof Constraint) || (object instanceof ExternDocument)) {
            return ((ModelElement) object).getStatus().isModifiable();
        } else {
            return false;
        }
    }

    @objid ("26f8f1b8-186f-11e2-bc4e-002564c97630")
    @Override
    public void keyPressed(KeyEvent e) {
        // Nothing to do
    }

    @objid ("26f8f1bc-186f-11e2-bc4e-002564c97630")
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.keyCode == SWT.F2) {
            List<ModelElement> selectedItems = this.noteView.getSelectedNoteItems();
        
            if (selectedItems.size() == 1) {
                Object selectedObject = selectedItems.get(0);
        
                this.noteView.getTreeViewer().editElement(selectedObject, 0);
            }
        }
    }

}
