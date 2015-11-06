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
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.edition.notes.view.NotesPanelProvider;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("26fb5317-186f-11e2-bc4e-002564c97630")
public class NoteViewTreePanel extends Composite {
    @objid ("3500b75b-186f-11e2-bc4e-002564c97630")
    private String lastSelectedType = "";

    @objid ("26fb5318-186f-11e2-bc4e-002564c97630")
    private NotesPanelProvider view = null;

    @objid ("26fb5319-186f-11e2-bc4e-002564c97630")
    private ElementNameModifier constraintNameModifier = null;

    @objid ("26fb531a-186f-11e2-bc4e-002564c97630")
    private ModelElement element;

    @objid ("26fb531b-186f-11e2-bc4e-002564c97630")
    private final NoteViewTreeLabelProvider labelProvider;

    @objid ("26fb531d-186f-11e2-bc4e-002564c97630")
    private TreeViewer treeViewer = null;

    @objid ("26fb531e-186f-11e2-bc4e-002564c97630")
    private final ICoreSession session;

    @objid ("26fb531f-186f-11e2-bc4e-002564c97630")
    public NoteViewTreePanel(SashForm sash, int style, NotesPanelProvider view, ICoreSession session) {
        super(sash, style);
        
        this.session = session;
        this.view = view;
        this.labelProvider = new NoteViewTreeLabelProvider();
        
        createGUI();
    }

    @objid ("26fb5325-186f-11e2-bc4e-002564c97630")
    private void createGUI() {
        Composite treeArea = new Composite(this, SWT.NONE);
        treeArea.setLayout(new FillLayout());
        
        this.treeViewer = new TreeViewer(treeArea, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        this.treeViewer.setLabelProvider(this.labelProvider);
        this.treeViewer.setContentProvider(new NoteViewTreeContentProvider());
        this.treeViewer.setAutoExpandLevel(3);
        
        initEditor();
        initDragSupport();
    }

    @objid ("26fb5327-186f-11e2-bc4e-002564c97630")
    @Override
    public boolean setFocus() {
        return this.treeViewer.getControl().setFocus();
    }

    @objid ("26fb532c-186f-11e2-bc4e-002564c97630")
    public void setInput(final ModelElement element, final ModelElement preselect) {
        // Avoid errors before unregistering the listener, when closing the project
        Control control = this.treeViewer.getControl();
        if (control == null || control.isDisposed()) {
            return;
        }
        
        if (element != null && element.equals(this.element)) {
            this.treeViewer.refresh();
            return;
        }
        
        this.element = element;
        this.treeViewer.setInput(this.element);
        
        if (this.element != null) {
        
            // if a concrete note element is asked for pre-selection, select it
            if (preselect != null) {
                select(preselect, true);
            } else {
                // try to find an optimal default selection.
                // rules are:
                // - try to find a note of the same type as the last selected note
                // - if not, select the first available note if some
                ModelElement lastNote = findNoteByType(element, this.lastSelectedType);
                ModelElement firstNote = getFirstNote(element);
        
                if (lastNote != null) {
                    select(lastNote, true);
                } else if (firstNote != null) {
                    select(firstNote, false);
                } else {
                    // nothing to preselect
                }
            }
        }
    }

    @objid ("26fb5332-186f-11e2-bc4e-002564c97630")
    private void initEditor() {
        // Define cell editor:
        TextCellEditor[] cellEditors = new TextCellEditor[1];
        cellEditors[0] = new TextCellEditor(this.treeViewer.getTree(), SWT.NONE);
        this.treeViewer.setCellEditors(cellEditors);
        
        // Define ICellModifier:
        String[] properties = new String[1];
        properties[0] = "name";
        this.treeViewer.setColumnProperties(properties);
        
        this.constraintNameModifier = new ElementNameModifier(this.view);
        this.treeViewer.setCellModifier(this.constraintNameModifier);
        this.treeViewer.getTree().addKeyListener(this.constraintNameModifier);
        
        // Define editor activation strategy:
        ElementEditorActivationStrategy actSupport = new ElementEditorActivationStrategy(this.treeViewer);
        TreeViewerEditor.create(this.treeViewer, null, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
                | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
                | ColumnViewerEditor.KEYBOARD_ACTIVATION);
    }

    @objid ("26fb5334-186f-11e2-bc4e-002564c97630")
    private void initDragSupport() {
        /*
         * TODO d&d // Allow data to be copied or moved from the drag source int operations = DND.DROP_MOVE | DND.DROP_COPY;
         * 
         * DragSource dragSource = new DragSource(this.treeViewer.getControl(), operations); // Provide data in Text format
         * Transfer[] types = new Transfer[] { PasteElementTransfer.getInstance(), ModelElementTransfer.getInstance() };
         * dragSource.setTransfer(types); dragSource.addDragListener(new NoteDragListener(this.treeViewer));
         */
    }

    @objid ("26fdb431-186f-11e2-bc4e-002564c97630")
    public TreeViewer getTreeViewer() {
        return this.treeViewer;
    }

    /**
     * Select the given model element in the annotation view.
     * @param target the model element to select.
     * @param updateLast wheter or not store the target as 'last selected'.
     */
    @objid ("26fdb435-186f-11e2-bc4e-002564c97630")
    public void select(Element target, final boolean updateLast) {
        if (this.element == null) {
            return;
        }
        this.treeViewer.refresh();
        
        String previousLast = this.lastSelectedType;
        
        if (target instanceof ModelElement) {
            this.treeViewer.setSelection(new StructuredSelection(target), true);
        
            if (!updateLast) {
                this.lastSelectedType = previousLast;
            } else {
                setLastType((ModelElement) target);
            }
        }
        
        // as the user chose a new note type in the tree, register this type as a preference for next navigation in the model
        // if (noteItem instanceof INote)
        // this.treePanel.setLastType(((INote) noteItem).getModel().getName());
        // else if (noteItem instanceof ExternDocument)
        // this.treePanel.setLastType(((ExternDocument) noteItem).getType().getName());
    }

    @objid ("26fdb43b-186f-11e2-bc4e-002564c97630")
    public void setInput(final ModelElement element) {
        this.setInput(element, null);
    }

    @objid ("26fdb43f-186f-11e2-bc4e-002564c97630")
    public void setLastType(final ModelElement elt) {
        if (elt instanceof Note) {
            this.lastSelectedType = ((Note) elt).getModel().getName();
        } else if (elt instanceof ExternDocument) {
            this.lastSelectedType = ((ExternDocument) elt).getType().getName();
        } else if (elt instanceof Constraint) {
            this.lastSelectedType = getConstraintName((Constraint) elt);
        }
    }

    @objid ("26fdb443-186f-11e2-bc4e-002564c97630")
    private static ModelElement getFirstNote(final ModelElement element) {
        // Extern Notes
        for (ExternDocument doc : element.getDocument()) {
            return doc;
        }
        
        // Notes
        for (Note note : element.getDescriptor()) {
            return note;
        }
        
        // Constraints
        for (Constraint constraint : element.getConstraintDefinition()) {
            return constraint;
        }
        return null;
    }

    @objid ("26fdb449-186f-11e2-bc4e-002564c97630")
    private static ModelElement findNoteByType(final ModelElement element, final String lastType) {
        // Extern Notes
        for (ExternDocument doc : element.getDocument()) {
            if (lastType.equals(doc.getType().getName())) {
                return doc;
            }
        }
        
        // Notes
        for (Note note : element.getDescriptor()) {
            if (lastType.equals(note.getModel().getName())) {
                return note;
            }
        }
        
        // Constraints
        for (Constraint constraint : element.getConstraintDefinition()) {
            if (!constraint.getExtension().isEmpty() && lastType.equals(constraint.getExtension().get(0).getName())) {
                return constraint;
            }
        }
        return null;
    }

    @objid ("26fdb451-186f-11e2-bc4e-002564c97630")
    private static String getConstraintName(final Constraint constraint) {
        // If the name is filled we use it for the constraint label
        final String name = constraint.getName();
        if (name != null && !name.isEmpty()) {
            return name;
        }
        
        // If the name is not filled and if there is at least on stereotype on the constraint
        // we use the first stereotype name as a label.
        final List<Stereotype> stereotypes = constraint.getExtension();
        if (stereotypes.size() > 0) {
            final Stereotype stereotype = stereotypes.get(0);
            if (stereotype != null) {
                return (ModuleI18NService.getLabel(stereotype));
            }
        }
        return "Constraint";
    }

}
