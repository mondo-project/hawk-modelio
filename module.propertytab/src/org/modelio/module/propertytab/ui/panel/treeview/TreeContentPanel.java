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
                                    

package org.modelio.module.propertytab.ui.panel.treeview;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.module.propertytab.model.ModulePropertyModel;
import org.modelio.module.propertytab.plugin.ModulePropertyTab;
import org.modelio.module.propertytab.ui.panel.IModulePropertyPanel;
import org.modelio.module.propertytab.ui.panel.treeview.editingsupport.ModulePropertyEditingSupport;
import org.modelio.module.propertytab.ui.panel.treeview.editingsupport.key.KeyLabelProvider;
import org.modelio.vcore.session.api.ICoreSession;

@objid ("c89a7583-1eba-11e2-9382-bc305ba4815c")
public class TreeContentPanel implements IModulePropertyPanel {
    @objid ("c89a9c90-1eba-11e2-9382-bc305ba4815c")
    private TreeViewer treeViewer;

    @objid ("c89a9c92-1eba-11e2-9382-bc305ba4815c")
    public TreeContentPanel(Composite parent, ModelElement element) {
        createGUI(parent);
    }

    @objid ("c89ac3a0-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void stop() {
        setInput(null, null);
        disableGUI();
    }

    @objid ("c89ac3a3-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void start(ICoreSession session) {
        // Nothing to do
    }

    @objid ("c89aeab1-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void disableGUI() {
        // Nothing to do
    }

    @objid ("c89aeab4-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void enableGUI() {
        // Nothing to do
    }

    @objid ("c89b11c1-1eba-11e2-9382-bc305ba4815c")
    private void createGUI(Composite parent) {
        this.treeViewer = new TreeViewer(parent, SWT.HIDE_SELECTION | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
                | SWT.FLAT | SWT.BORDER | SWT.NO_REDRAW_RESIZE);
        this.treeViewer.getTree().setHeaderVisible(true);
        this.treeViewer.getTree().setLinesVisible(true);
        
        // Layout the viewer
        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.horizontalSpan = 2;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        this.treeViewer.getControl().setLayoutData(gridData);
        
        // Create columns
        String[] columnTitles = { ModulePropertyTab.I18N.getString("TreeView.Property"),  ModulePropertyTab.I18N.getString("TreeView.Value")};
        int[] columnInitialWidths = { 200, 100 };
        TreeViewerColumn col1 = this.createTreeViewerColumn(columnTitles[0], columnInitialWidths[0]);
        col1.setLabelProvider(new KeyLabelProvider());
        
        TreeViewerColumn col2 = createTreeViewerColumn(columnTitles[1], columnInitialWidths[1]);
        col2.setLabelProvider(new ModulePropertyCellLabelProvider());
        col2.setEditingSupport(new ModulePropertyEditingSupport(this.treeViewer));
        
        ModulePropertyContentProvider content = new ModulePropertyContentProvider();
        this.treeViewer.setContentProvider(content);
    }

    @objid ("c89b11c4-1eba-11e2-9382-bc305ba4815c")
    private TreeViewerColumn createTreeViewerColumn(String title, int bound) {
        final TreeViewerColumn column = new TreeViewerColumn(this.treeViewer, SWT.LEFT);
        column.getColumn().setText(title);
        column.getColumn().setWidth(bound);
        column.getColumn().setResizable(true);
        column.getColumn().setMoveable(true);
        return column;
    }

    @objid ("c89b38d4-1eba-11e2-9382-bc305ba4815c")
    @Override
    public Composite getComposite() {
        return this.treeViewer.getTree();
    }

    @objid ("c89b5fe3-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void refresh() {
        this.treeViewer.refresh();
    }

    @objid ("c89b86f0-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void setFocus() {
        this.treeViewer.getTree().setFocus();
    }

    @objid ("c89b86f3-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void setInput(IModelioPickingService pickingService, ModulePropertyModel moduleProperty) {
        if (System.getProperty("os.name").startsWith("Mac")) {
            // On Mac OS, we have to 
            Composite parent = this.treeViewer.getTree().getParent();
            this.treeViewer.getTree().dispose();
        
            createGUI(parent);
            parent.layout();
        }
        
        if (moduleProperty == null) {
            this.treeViewer.setInput(null);
        } else {         
            this.treeViewer.setInput(moduleProperty);
        }
    }

    @objid ("c89bae03-1eba-11e2-9382-bc305ba4815c")
    public ColumnViewer getTreeViewer() {
        return this.treeViewer;
    }

}
