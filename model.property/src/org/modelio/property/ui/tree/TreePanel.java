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
                                    

package org.modelio.property.ui.tree;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

@objid ("8faeec99-c068-11e1-8c0a-002564c97630")
public class TreePanel extends Composite {
    @objid ("8faeec9c-c068-11e1-8c0a-002564c97630")
    private Element currentElement = null;

    @objid ("8faeec9b-c068-11e1-8c0a-002564c97630")
    private TreeLabelProvider annotationLabelProvider = null;

    @objid ("25276b51-cf5d-11e1-80a9-002564c97630")
    private Object lastSelectedType = null;

    @objid ("29c3d158-164b-4b7e-a92b-60c0cb974667")
    private TreeContentProvider contentProvider;

    @objid ("48b85d08-560b-41dd-bdc5-d7b0c04625a8")
    private TreeViewer viewer = null;

    @objid ("8faeeca3-c068-11e1-8c0a-002564c97630")
    public TreePanel(SashForm sash, int style) {
        super(sash, style);
        
        this.annotationLabelProvider = new TreeLabelProvider();
        
        createGUI();
    }

    @objid ("8faeeca8-c068-11e1-8c0a-002564c97630")
    private void createGUI() {
        Composite treeArea = new Composite(this, SWT.NONE);
        treeArea.setLayout(new FillLayout());
        
        this.viewer = new TreeViewer(treeArea, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        this.viewer.setLabelProvider(this.annotationLabelProvider);
        this.contentProvider = new TreeContentProvider();
        this.viewer.setContentProvider(this.contentProvider);
        this.viewer.setAutoExpandLevel(3);
        
        initEditor();
        initDragSupport();
        
        hookContextMenu();
    }

    @objid ("8faeecaa-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean setFocus() {
        return this.viewer.getControl().setFocus();
    }

    @objid ("8faeecaf-c068-11e1-8c0a-002564c97630")
    private void hookContextMenu() {
        /* TODO CHM context menu
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(IMenuManager manager) {
                TreePanel.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(this.viewer.getControl());
        this.viewer.getControl().setMenu(menu);
        this.view.getSite().registerContextMenu(menuMgr, this.viewer);
        */
    }

    @objid ("8fb14da6-c068-11e1-8c0a-002564c97630")
    private void initEditor() {
        // Define cell editor:
        TextCellEditor[] cellEditors = new TextCellEditor[1];
        cellEditors[0] = new TextCellEditor(this.viewer.getTree(), SWT.NONE);
        this.viewer.setCellEditors(cellEditors);
        
        // Define ICellModifier:
        String[] properties = new String[1];
        properties[0] = "name";
        this.viewer.setColumnProperties(properties);
    }

    @objid ("8fb14da8-c068-11e1-8c0a-002564c97630")
    private void initDragSupport() {
        /* TODO CHM drag & drop
        // Allow data to be copied or moved from the drag source
        int operations = DND.DROP_MOVE | DND.DROP_COPY;
        
        DragSource dragSource = new DragSource(this.viewer.getControl(), operations);
        // Provide data in Text format
        Transfer[] types = new Transfer[] { PasteElementTransfer.getInstance(),
                ModelElementTransfer.getInstance() };
        dragSource.setTransfer(types);
        dragSource.addDragListener(new NoteDragListener(this.viewer));
        */
    }

    /**
     * @return the tree viewer.
     */
    @objid ("8fb14daa-c068-11e1-8c0a-002564c97630")
    public TreeViewer getTreeViewer() {
        return this.viewer;
    }

    /**
     * Select the given model element in the annotation view.
     * @param target the model element to select.
     */
    @objid ("8fb14db1-c068-11e1-8c0a-002564c97630")
    private void select(final Object target, final boolean updateLast) {
        if (this.currentElement == null || target == null) {
            this.viewer.setSelection(new TreeSelection((TreePath) null), false);
        } else {
            Object previousLast = this.lastSelectedType;
        
            TreePath treePath = new TreePath(new Object[] { target });
            this.viewer.setSelection(new TreeSelection(treePath), true);
        
            if (!updateLast) {
                this.lastSelectedType = previousLast;
            } else {
                setLastType(target);
            }
        }
    }

    @objid ("8fb14dba-c068-11e1-8c0a-002564c97630")
    public void setLastType(final Object typeItem) {
        if (typeItem instanceof Stereotype) {
            this.lastSelectedType = typeItem;
        } else if (typeItem instanceof ModuleComponent) {
            this.lastSelectedType = typeItem;
        } else {
            // Don't keep the selection if it is nor a module, nor stereotype
            this.lastSelectedType = null;
        }
    }

    /**
     * @param newInput the new input
     */
    @objid ("8fb14dbe-c068-11e1-8c0a-002564c97630")
    public void setInput(TreePanelInput newInput) {
        // IMModelServices modelService, final Element typedElement, final Element preselectedTypingElement
        this.contentProvider.setModelService(newInput.getModelService());
        this.contentProvider.setShowHiddenMdaElements(newInput.isDisplayHiddenAnnotations());
        
        Element typedElement = newInput.getTypedElement();
        
        if (typedElement != null && typedElement.equals(this.currentElement)) {
            this.viewer.refresh();
        } else {
            this.currentElement = typedElement;
            this.viewer.setInput(this.currentElement);
        }
        
        if (typedElement != null) {
            Object preselectedTypingElement = newInput.getPreselectedTypingElement();
            if (preselectedTypingElement != null) {
                select(preselectedTypingElement, true);
            } else {
                // try to find an optimal default selection.
                // rules are:
                // - try to find a stereotype or module of the same type as the last selected one
                // - if not, select the model element's class itself
                select(this.lastSelectedType, true);
                if (this.viewer.getSelection().isEmpty()) {
                    select(typedElement.getClass(), false);
                }
            }
        } else {
            select(typedElement, false);
        }
    }

    /**
     * @return the last selected annotation set
     */
    @objid ("2df98193-c68e-44aa-94ab-4c3b02901c21")
    public Object getLastSelectedType() {
        return this.lastSelectedType;
    }

}
