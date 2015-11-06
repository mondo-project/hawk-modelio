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
                                    

package org.modelio.diagram.styles.viewer;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.IStyleChangeListener;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.diagram.styles.editingsupport.StyleCellLabelProvider;
import org.modelio.diagram.styles.editingsupport.StylePropertyEditingSupport;
import org.modelio.diagram.styles.editingsupport.key.KeyLabelProvider;
import org.modelio.diagram.styles.manager.StyleModelProvider;
import org.modelio.diagram.styles.plugin.DiagramStyles;

/**
 * This class allows to view and edit a Style.
 */
@objid ("85cf80da-1926-11e2-92d2-001ec947c8cc")
public class StyleViewer implements IStyleChangeListener {
    @objid ("85cf80dc-1926-11e2-92d2-001ec947c8cc")
    private StyleModelProvider model;

    @objid ("85cf80dd-1926-11e2-92d2-001ec947c8cc")
    private final StyleModelProvider emptyModel = new StyleModelProvider(null, null);

    @objid ("85cf80df-1926-11e2-92d2-001ec947c8cc")
    private TreeViewer treeViewer;

    @objid ("a9c6d983-bf26-48ce-bd9d-b2024a922e2e")
    private IModelioPickingService modelioPickingService;

    /**
     * C'tor.
     * @param parent the composite into which this view is fit.
     * @param model the edited style model
     * @param modelioPickingService the Modelio picking service.
     */
    @objid ("85cf80e0-1926-11e2-92d2-001ec947c8cc")
    public StyleViewer(Composite parent, StyleModelProvider model, IModelioPickingService modelioPickingService) {
        StyleModelProvider initialModel = (model != null) ? model : this.emptyModel;
        
        this.modelioPickingService = modelioPickingService;
        
        this.model = initialModel;
        this.treeViewer = new TreeViewer(parent, SWT.HIDE_SELECTION |
                                                 SWT.MULTI |
                                                 SWT.H_SCROLL |
                                                 SWT.V_SCROLL |
                                                 SWT.FULL_SELECTION |
                                                 SWT.BORDER);
        
        this.treeViewer.getTree().setHeaderVisible(true);
        this.treeViewer.getTree().setLinesVisible(true);
        
        this.treeViewer.setContentProvider(this.model);
        this.treeViewer.setInput(this.model);
        
        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        this.treeViewer.getControl().setLayoutData(gridData);
        
        // Create columns
        this.createColumns(this);
        
        this.treeViewer.refresh();
    }

    /**
     * Sets the layout data associated with the receiver to the argument.
     * @param layoutData the new layout data for the receiver.
     */
    @objid ("85cf80e5-1926-11e2-92d2-001ec947c8cc")
    public void setLayoutData(Object layoutData) {
        this.treeViewer.getTree().setLayoutData(layoutData);
    }

    /**
     * This will create the columns for the style table viewer
     */
    @objid ("85d1e333-1926-11e2-92d2-001ec947c8cc")
    private void createColumns(final StyleViewer viewer) {
        String[] columnTitles = { DiagramStyles.I18N.getString("StylesViewer.Property"),
                DiagramStyles.I18N.getString("StylesViewer.Value") };
        int[] columnInitialWidths = { 200, 100 };
        
        // First column is for the style key name
        TreeViewerColumn col1 = StyleViewer.createTreeViewerColumn(viewer, columnTitles[0], columnInitialWidths[0]);
        
        col1.setLabelProvider(new KeyLabelProvider(viewer));
        
        // Second column is for the style key type
        TreeViewerColumn col2 = StyleViewer.createTreeViewerColumn(viewer, columnTitles[1], columnInitialWidths[1]);
        
        col2.setLabelProvider(new StyleCellLabelProvider(viewer));
        col2.setEditingSupport(new StylePropertyEditingSupport(viewer));
        
        this.treeViewer.setContentProvider(this.model);
        //this.treeViewer.setLabelProvider(new StyleCellLabelProvider(this));
    }

    /**
     * Convenience to create and configure a column
     */
    @objid ("85d1e338-1926-11e2-92d2-001ec947c8cc")
    private static TreeViewerColumn createTreeViewerColumn(StyleViewer theViewer, String title, int bound) {
        final TreeViewerColumn column = new TreeViewerColumn(theViewer.treeViewer, SWT.NONE);
        
        column.getColumn().setText(title);
        column.getColumn().setWidth(bound);
        column.getColumn().setResizable(true);
        column.getColumn().setMoveable(true);
        return column;
    }

    /**
     * @return the currently edited style.
     */
    @objid ("85d1e340-1926-11e2-92d2-001ec947c8cc")
    public IStyle getEditedStyle() {
        return this.model.getStyleData();
    }

    /**
     * @return the style tree viewer.
     */
    @objid ("85d1e344-1926-11e2-92d2-001ec947c8cc")
    public TreeViewer getTreeViewer() {
        return this.treeViewer;
    }

    /**
     * Set the model for the viewer. The 'null' value means no model. The Viewer actually uses a fake empty model in
     * this case, displaying an empty table.
     * @param model the model used by this viewer.
     */
    @objid ("85d1e348-1926-11e2-92d2-001ec947c8cc")
    public void setModel(StyleModelProvider model) {
        // unregister listener from previous model
        if (this.model != null && this.model.getStyleData() != null) {
            this.model.getStyleData().removeListener(this);
        }
        // set model
        if (model == null) {
            this.model = this.emptyModel;
            this.treeViewer.setContentProvider(this.model);
            this.treeViewer.setInput(this.model);
            // do not register a listener
        } else {
            this.model = model;
            this.treeViewer.setContentProvider(this.model);
            this.treeViewer.setInput(this.model);
            // register as Style listener
            this.model.getStyleData().addListener(this);
        }
        
        this.treeViewer.refresh();
    }

    /**
     * Called when a property changed in the edited style. See IStyleChangeListener.
     */
    @objid ("85d1e34c-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void styleChanged(IStyle changedStyle) {
        this.treeViewer.getTree().deselectAll();
        this.treeViewer.refresh(true);
        this.treeViewer.getTree().redraw();
    }

    /**
     * Called when a property changed in the edited style See IStyleChangeListener.
     */
    @objid ("85d1e351-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void styleChanged(StyleKey property, Object newValue) {
        this.treeViewer.getTree().deselectAll();
        this.treeViewer.refresh(true);
        this.treeViewer.getTree().redraw();
    }

    /**
     * @return the edited style model.
     */
    @objid ("85d1e357-1926-11e2-92d2-001ec947c8cc")
    public StyleModelProvider getModel() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.model;
    }

    /**
     * @return the Modelio picking service
     */
    @objid ("955c6457-5101-40d2-9f7a-631cd2d37f68")
    public IModelioPickingService getPickingService() {
        return this.modelioPickingService;
    }

}
