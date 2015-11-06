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
                                    

package org.modelio.property.ui.data.standard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.DefaultCellRenderer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.MetamodelLabels;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.core.ui.ktable.types.IPropertyType;
import org.modelio.core.ui.ktable.types.enumeration.EnumType;
import org.modelio.core.ui.ktable.types.header.HeaderType;
import org.modelio.core.ui.ktable.types.hybrid.HybridCellEditor;
import org.modelio.core.ui.ktable.types.label.LabelType;
import org.modelio.property.plugin.ModelProperty;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * This is the model of the KTable that display the elements properties.
 * It serves as a bridge between an {@link IPropertyModel} and a {@link KTable}.
 */
@objid ("8dd53ab6-c068-11e1-8c0a-002564c97630")
public class StandardKModel extends KTableDefaultModel {
    @objid ("8dd53ab8-c068-11e1-8c0a-002564c97630")
    private IPropertyModel data = null;

    @objid ("8dd53ac4-c068-11e1-8c0a-002564c97630")
    private Color oddColor = null;

    @objid ("8dd53ac5-c068-11e1-8c0a-002564c97630")
    private Color evenColor = null;

    @objid ("9f0e2b76-c5d6-11e1-8f21-002564c97630")
    private KTable table = null;

    @objid ("9f0e2b79-c5d6-11e1-8f21-002564c97630")
    private KTableCellEditor currentEditor = null;

    @objid ("86ac3ee1-cf24-11e1-80a9-002564c97630")
    private ICoreSession modelingSession;

    @objid ("06fc5b1b-16d1-11e2-aa0d-002564c97630")
    private IModelioPickingService pickingService;

    /**
     * Constructor initializing the KTable model.
     * @param modelingSession the currently opened core session. Might be <code>null</code>.
     * @param pickingService the picking service. Might be <code>null</code>.
     * @param table the KTable this model is given to.
     * @param data the property model to display in the KTable.
     */
    @objid ("8dd53acf-c068-11e1-8c0a-002564c97630")
    public StandardKModel(ICoreSession modelingSession, IModelioPickingService pickingService, KTable table, IPropertyModel data) {
        this.modelingSession = modelingSession;
        this.table = table;
        this.pickingService = pickingService;
        
        // Setup the data model
        this.data = data;
        
        // Initialize colors
        this.oddColor = UIColor.TABLE_ODDROW_BG;
        this.evenColor = UIColor.TABLE_EVENROW_BG;
    }

    @objid ("8dd53ad6-c068-11e1-8c0a-002564c97630")
    @Override
    public KTableCellEditor doGetCellEditor(int col, int row) {
        KTableCellEditor editor = null;
        
        // No session, no edition
        if (this.modelingSession == null) {
            return null;
        }
        
        // Row 0 is not editable
        if (row <= 0) {
            return null;
        }
        
        // Col 0 is not editable
        if (col <= 0) {
            return null;
        }
        
        if (!this.data.isEditable(row, col)) {
            return null;
        }
        
        final IPropertyType type = this.data.getTypeAt(row, col);
        
        // Other columns editor depends on data type
        if (col > 0) {
            editor = type.getEditor(this.pickingService);
        }
        
        if (this.currentEditor != null) {
            if (this.currentEditor instanceof HybridCellEditor) {
                final HybridCellEditor hybridCellEditor = (HybridCellEditor) this.currentEditor;
                hybridCellEditor.forceClose();
            }
        }
        this.currentEditor = editor;
        return editor;
    }

    @objid ("8dd53add-c068-11e1-8c0a-002564c97630")
    @Override
    public KTableCellRenderer doGetCellRenderer(int col, int row) {
        DefaultCellRenderer renderer = null;
        
        IPropertyType type;
        // Header row: return header renderer for all columns
        if (row == 0) {
            // First line always contains headers
            type = new HeaderType();
        } else if (col == 0) {
            // First column always contains non editable labels
            type = new LabelType();
        } else {
            type = this.data.getTypeAt(row, col);
        }
        
        renderer = type.getRenderer();
        if (renderer != null) {
            renderer.setBackground((row % 2 == 0) ? this.oddColor : this.evenColor);
        }
        return renderer;
    }

    @objid ("8dd6c148-c068-11e1-8c0a-002564c97630")
    @Override
    public int doGetColumnCount() {
        return this.data.getColumnNumber();
    }

    @objid ("8dd6c14d-c068-11e1-8c0a-002564c97630")
    @Override
    public Object doGetContentAt(int col, int row) {
        final Object value;
        
        try {
            value = this.data.getValueAt(row, col);
        } catch (RuntimeException e) {
            ModelProperty.LOG.error(e);
            return "<!"+e.getClass().getSimpleName()+"!>";
        }
        
        boolean i18n = false;
        String key = null;
        Object label = value;
        
        final IPropertyType type = this.data.getTypeAt(row, col);
        
        if (col == 0) {
            key = (String) value;
            if (key.startsWith("=")) {
                label = key.substring(1);
                i18n = false;
            } else {
                i18n = true;
            }
        } else if (row == 0) {
            key = (String) value;
            i18n = true;
        } else if (type instanceof EnumType) {
            key = ((Enum<?>) value).toString();
            i18n = true;
        }
        
        if (i18n) {
            label = MetamodelLabels.getString(key);
        }
        return label;
    }

    @objid ("8dd6c154-c068-11e1-8c0a-002564c97630")
    @Override
    public int doGetRowCount() {
        return this.data.getRowsNumber();
    }

    @objid ("8dd6c159-c068-11e1-8c0a-002564c97630")
    @Override
    public void doSetContentAt(int col, int row, Object value) {
        // Row 0 is not editable
        if (row == 0) {
            return;
        }
        
        // Col 0 is not editable
        if (col == 0) {
            return;
        }
        
        // Col 1 editor depends on data type
        
        try (ITransaction t = this.modelingSession.getTransactionSupport().createTransaction("doSetContentAt")) {
            this.data.setValueAt(row, col, value);
            t.commit();
        } 
        
        if (!this.table.isDisposed()) {
            this.table.redraw();
        }
    }

    @objid ("8dd6c15f-c068-11e1-8c0a-002564c97630")
    @Override
    public int getInitialColumnWidth(int column) {
        final int colCount = this.data.getColumnNumber();
        final int availableWidth = this.table.getClientArea().width - 18;
        
        if (column == 0) {
            return getOptimalColumnWidth(0);
        } else {
            final int firstColumnWidth = getOptimalColumnWidth(0);
            return (availableWidth - firstColumnWidth) / (colCount - 1);
        }
    }

    @objid ("8dd6c165-c068-11e1-8c0a-002564c97630")
    @Override
    public int getInitialRowHeight(int row) {
        if (row == 0) 
            return 22;
        
        int ret = 18;
        
        // Ask each String cells in the row their required height
        // and return the biggest one with 18 pix minimum
        GC gc = null;
        try {
            for (int col = getFixedColumnCount(); col<getColumnCount(); col++) {
                Object val = getContentAt(1, row);
                if (val instanceof String) {
                    if (gc==null) 
                        gc = new GC(this.table);
                    // Assumes the cell renderer uses the default font...
                    int cell = SWTX.getCachedStringExtent(gc, val.toString()).y + 2;
                    ret  = Math.max(cell, ret);
                } 
            }
        } finally {
            if (gc != null) gc.dispose();
        }
        return ret;
    }

    @objid ("8dd6c16b-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedHeaderColumnCount() {
        return 0;
    }

    @objid ("8dd6c170-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedHeaderRowCount() {
        return 1;
    }

    @objid ("8dd6c175-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedSelectableColumnCount() {
        return 0;
    }

    @objid ("8dd6c17a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedSelectableRowCount() {
        return 0;
    }

    @objid ("8dd6c17f-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowHeightMinimum() {
        return 10;
    }

    @objid ("8dd847e5-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isColumnResizable(int col) {
        return true;
    }

    @objid ("8dd847eb-c068-11e1-8c0a-002564c97630")
    @Override
    public void setColumnWidth(int col, int value) {
        super.setColumnWidth(col, value);
    }

    @objid ("8dd847f0-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isRowResizable(int row) {
        return false;
    }

    @objid ("8dd847f6-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnWidth(int col) {
        return super.getColumnWidth(col);
    }

    /**
     * Get the property type of a cell.
     * @param col the column index of the cell.
     * @param row the row index of the cell.
     * @return an {@link IPropertyType}
     */
    @objid ("8dd84800-c068-11e1-8c0a-002564c97630")
    public IPropertyType doGetTypeAt(int col, int row) {
        return this.data.getTypeAt(row, col);
    }

    @objid ("8dd84806-c068-11e1-8c0a-002564c97630")
    private int getOptimalColumnWidth(int column) {
        if (column >= 0 && column < getColumnCount()) {
            int optWidth = 5;
            final GC gc = new GC(this.table);
            for (int i = 0; i < getFixedHeaderRowCount(); i++) {
                final int width = getCellRenderer(column, i).getOptimalWidth(gc, column, i, getContentAt(column, i), true, this);
                if (width > optWidth) {
                    optWidth = width;
                }
            }
            for (int i = 1; i < 1 + getRowCount() - 1; i++) {
                final int width = getCellRenderer(column, i).getOptimalWidth(gc, column, i, getContentAt(column, i), true, this);
                if (width > optWidth) {
                    optWidth = width;
                }
            }
            gc.dispose();
            return optWidth + 10;
        }
        return -1;
    }

    @objid ("8dd8480b-c068-11e1-8c0a-002564c97630")
    @Override
    public String getTooltipAt(int col, int row) {
        return String.valueOf(doGetContentAt(col, row));
    }

}
