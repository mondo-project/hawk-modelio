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
                                    

package org.modelio.property.ui.data.stereotype;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.coordinate.Range;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditBindings;
import org.eclipse.nebula.widgets.nattable.edit.config.DefaultEditConfiguration;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayerListener;
import org.eclipse.nebula.widgets.nattable.layer.event.ILayerEvent;
import org.eclipse.nebula.widgets.nattable.resize.command.ColumnResizeCommand;
import org.eclipse.nebula.widgets.nattable.resize.event.ColumnResizeEvent;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.config.DefaultSelectionStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.selection.event.CellSelectionEvent;
import org.eclipse.nebula.widgets.nattable.selection.event.RowSelectionEvent;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.property.ui.data.stereotype.body.BodyConfiguration;
import org.modelio.property.ui.data.stereotype.columnheader.ColumnHeaderConfiguration;
import org.modelio.property.ui.data.stereotype.data.TableDataModel;

/**
 * This method creates and configure a NatTable for property table editor. Also provides Selection management.
 */
@objid ("dbf6a6d0-f2f5-483c-8b95-6e67bbe01109")
public class PropertyTableViewer extends Viewer {
    @objid ("9a8b1ea0-d8f7-4fdd-bd5d-059eb0d07e4f")
    private TableDataModel dataModel;

    @objid ("436ae03c-eed3-4df4-9357-2047601b3031")
    private IProjectService projectService;

    @objid ("770fc3c7-ec01-453d-96ce-dd246d9436d1")
    private IModelioPickingService pickingService;

    @objid ("c51ee22a-577e-4902-8d27-93a566f3404b")
    protected final NatTable natTable;

    @objid ("c7975c4b-a7d5-4400-8199-c1205fccbba1")
    private SelectionLayer selectionLayer;

    @objid ("170f0d25-79b2-4b5d-8504-10889f1ba130")
    public PropertyTableViewer(Composite parent) {
        this.natTable = new NatTable(parent, false);
    }

    @objid ("3f7151b7-997b-4593-a3cc-cfcc7324232b")
    public void setPickingService(IModelioPickingService pickingService) {
        this.pickingService = pickingService;
    }

    @objid ("2766ef82-ab3a-4e7b-b64f-d35a3bc1b69b")
    public void setProjectService(IProjectService projectService) {
        this.projectService = projectService;
    }

    @objid ("a1e56ab5-b51d-4cce-856e-3627a0dc2277")
    private void setupTable(TableDataModel dataModel) {
        this.natTable.setData(dataModel);
        
        // Table Body
        final IDataProvider bodyDataProvider = dataModel.getBodyDataProvider();
        final DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        
        // Add table selection layer
        this.selectionLayer = new SelectionLayer(bodyDataLayer);
        
        // Scrollable port layer
        final ViewportLayer viewportLayer = new ViewportLayer(this.selectionLayer);
        
        // Column header
        final IDataProvider columnHeaderDataProvider = dataModel.getColumnHeaderDataProvider();
        final DefaultColumnHeaderDataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        final ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, this.selectionLayer, false);
        
        final ColumnHeaderConfiguration colConf = new ColumnHeaderConfiguration(dataModel, columnHeaderDataLayer);
        columnHeaderLayer.addConfiguration(colConf);
        
        // Assemble layers
        CompositeLayer compositeLayer = new CompositeLayer(1, 2);
        compositeLayer.setChildLayer(GridRegion.BODY, viewportLayer, 0, 1);
        compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER, columnHeaderLayer, 0, 0);
        compositeLayer.addConfiguration(new DefaultEditBindings());
        compositeLayer.addConfiguration(new DefaultEditConfiguration());
        
        // Setup the global main layer on the table, here the assembly grid.
        this.natTable.setLayer(compositeLayer);
        
        // Configuration for edition
        this.natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        
        this.natTable.addConfiguration(new DefaultSelectionStyleConfiguration());
        
        this.natTable.addConfiguration(new BodyConfiguration(bodyDataLayer, dataModel, this.projectService, this.pickingService));
        
        this.natTable.configure();
        
        this.natTable.addLayerListener(new ILayerListener() {
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void handleLayerEvent(ILayerEvent event) {
                if (event instanceof CellSelectionEvent || event instanceof RowSelectionEvent) {
                    final ISelection selection = getSelection();
                    final SelectionChangedEvent e = new SelectionChangedEvent(PropertyTableViewer.this, selection);
                    fireSelectionChanged(e);
                }
            }
        });
        
        this.natTable.addLayerListener(new ILayerListener() {
            
            @Override
            public void handleLayerEvent(ILayerEvent event) {
                if (event instanceof ColumnResizeEvent) {
                    ColumnResizeEvent resizeEvent = (ColumnResizeEvent) event;
                    if (resizeEvent.getColumnPositionRanges().contains(new Range(0, 1))) {
                        final int width = getSecondColumnWidth(PropertyTableViewer.this.natTable.getParent().getParent(), resizeEvent.getLayer().getColumnWidthByPosition(0), PropertyTableViewer.this.natTable.getVerticalBar());
                        if (width > 0) {
                            bodyDataLayer.setColumnWidthByPosition(1, width);
                        }
                    }
                }
            }
        });
        
        bodyDataLayer.setColumnWidthByPosition(0, 150);
        final int width = getSecondColumnWidth(this.natTable.getParent().getParent(), 150, this.natTable.getVerticalBar());
        if (width > 0) {
            bodyDataLayer.setColumnWidthByPosition(1, width);
        }
        this.natTable.getParent().addControlListener(new LayoutChangeListener(columnHeaderDataLayer));
    }

    @objid ("e6e380c3-3a61-4b5a-b831-e172f4bf7107")
    @Override
    public Control getControl() {
        return this.natTable;
    }

    @objid ("b8fe5501-ccaa-442a-be0f-f73394add4ee")
    @Override
    public Object getInput() {
        return this.dataModel;
    }

    @objid ("d4370744-183e-4230-a50c-48b45e63d684")
    @Override
    public void setInput(Object input) {
        this.dataModel = (TableDataModel) input;
        setupTable(this.dataModel);
    }

    @objid ("422557d6-1b59-4a61-9b03-6022f73084be")
    @Override
    public void refresh() {
        this.natTable.refresh();
    }

    @objid ("da371abc-2ac4-4886-acd0-a602a2a82f37")
    @Override
    public ISelection getSelection() {
        final List<Object> objects = new ArrayList<>();
        
        final Set<Range> rowRanges = this.selectionLayer.getSelectedRowPositions();
        for (final Range rowRange : rowRanges) {
            for (int rowPosition = rowRange.start; rowPosition < rowRange.end; rowPosition++) {
                objects.add(this.dataModel.getObjectAtRow(rowPosition));
            }
        }
        return new StructuredSelection(objects);
    }

    @objid ("257a66be-a0a6-4641-b574-8bd1d6465326")
    @Override
    public void setSelection(ISelection selection, boolean reveal) {
        IStructuredSelection s = (IStructuredSelection)selection;
        
        this.selectionLayer.clear();
        for (Object o : s.toList()) {
            int row = this.dataModel.getRowIndex(o);
            if (row != -1) {
                this.selectionLayer.selectRow(0, row, false, true);
            }
        }
    }

    @objid ("6be20d20-87f1-4529-ab97-c848daf10725")
    public boolean isEditionActive() {
        return !this.natTable.isFocusControl();
    }

    @objid ("906dfd22-025a-4103-9185-fcefce959cef")
    protected int getSecondColumnWidth(Composite parentComposite, int firstColWidth, ScrollBar verticalScrollbar) {
        int parentWidth = parentComposite.getSize().x;
        int scollbarWidth = verticalScrollbar != null && verticalScrollbar.isVisible() ? verticalScrollbar.getSize().x : 0;
        int margin = 5;
        
        // We have two columns, let the second one take all the available width
        final int secondColumnWidth = parentWidth - scollbarWidth - firstColWidth - margin;
        return secondColumnWidth;
    }

    @objid ("aca09338-5938-4afe-a959-1785989f2469")
    private class LayoutChangeListener implements ControlListener {
        @objid ("6e21ab4d-2cea-4a29-80f9-0bf8069ef5af")
        private DefaultColumnHeaderDataLayer columnHeaderDataLayer;

        @objid ("307a2ab6-b950-4a77-a75f-37e94d3183f7")
        protected LayoutChangeListener(DefaultColumnHeaderDataLayer columnHeaderDataLayer) {
            super();
            this.columnHeaderDataLayer = columnHeaderDataLayer;
        }

        @objid ("ff6902d6-ea36-4b54-90c4-66e886cfd1d5")
        @Override
        public void controlResized(ControlEvent theEvent) {
            changeLayout(theEvent);
        }

        @objid ("c47b073e-0010-4c62-87c5-c52e89795353")
        @Override
        public void controlMoved(ControlEvent theEvent) {
            // Nothing to do
        }

        @objid ("46eb4442-93ce-41c3-a7a2-1bf7b05af0e8")
        private void changeLayout(ControlEvent theEvent) {
            Composite parentComposite = (Composite) theEvent.widget;
            final ScrollBar verticalScrollbar = PropertyTableViewer.this.natTable.getVerticalBar();
            
            final int secondColumnWidth = getSecondColumnWidth(parentComposite, PropertyTableViewer.this.natTable.getColumnWidthByPosition(0), verticalScrollbar);
            if (secondColumnWidth > 0) {
                ColumnResizeCommand crc = new ColumnResizeCommand(PropertyTableViewer.this.natTable, 1, secondColumnWidth);
                PropertyTableViewer.this.natTable.doCommand(crc);
            }
        }

    }

}
