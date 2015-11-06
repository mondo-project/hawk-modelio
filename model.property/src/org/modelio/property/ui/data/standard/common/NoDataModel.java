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
                                    

package org.modelio.property.ui.data.standard.common;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableDefaultModel;
import de.kupzog.ktable.SWTX;
import de.kupzog.ktable.renderers.FixedCellRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

@objid ("8ebba3eb-c068-11e1-8c0a-002564c97630")
public class NoDataModel extends KTableDefaultModel {
    @objid ("f9ccf4d3-c5d4-11e1-8f21-002564c97630")
    private FixedCellRenderer headerRenderer = null;

    @objid ("f9ccf4d4-c5d4-11e1-8f21-002564c97630")
    private KTable table = null;

    @objid ("8ebba3ef-c068-11e1-8c0a-002564c97630")
    public NoDataModel(KTable table) {
        this.table = table;
        
        this.headerRenderer = new FixedCellRenderer(SWT.NONE);
        this.headerRenderer.setForeground(Display.getCurrent().getSystemColor(
                SWT.COLOR_DARK_GRAY));
        this.headerRenderer.setBackground(Display.getCurrent().getSystemColor(
                SWT.COLOR_WIDGET_BACKGROUND));
        this.headerRenderer.setAlignment(SWTX.ALIGN_HORIZONTAL_CENTER
                | SWTX.ALIGN_VERTICAL_CENTER);
    }

    @objid ("8ebba3f2-c068-11e1-8c0a-002564c97630")
    @Override
    public KTableCellEditor doGetCellEditor(int col, int row) {
        return null;
    }

    @objid ("8ebba3f9-c068-11e1-8c0a-002564c97630")
    @Override
    public KTableCellRenderer doGetCellRenderer(int col, int row) {
        if (row == 0)
            return this.headerRenderer;
        else
            return null;
    }

    @objid ("8ebba3ff-c068-11e1-8c0a-002564c97630")
    @Override
    public int doGetColumnCount() {
        return 0;
    }

    @objid ("8ebd29a6-c068-11e1-8c0a-002564c97630")
    @Override
    public Object doGetContentAt(int col, int row) {
        return null;
    }

    @objid ("8ebd29ad-c068-11e1-8c0a-002564c97630")
    @Override
    public int doGetRowCount() {
        return 0;
    }

    @objid ("8ebd29b2-c068-11e1-8c0a-002564c97630")
    @Override
    public void doSetContentAt(int col, int row, Object value) {
        // Nothing to do.
    }

    @objid ("8ebd29b8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getInitialColumnWidth(int column) {
        return 40;
    }

    @objid ("8ebd29be-c068-11e1-8c0a-002564c97630")
    @Override
    public int getInitialRowHeight(int row) {
        return 20;
    }

    @objid ("8ebd29c4-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedHeaderColumnCount() {
        return 0;
    }

    @objid ("8ebd29c9-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedHeaderRowCount() {
        return 0;
    }

    @objid ("8ebd29ce-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedSelectableColumnCount() {
        return 0;
    }

    @objid ("8ebd29d3-c068-11e1-8c0a-002564c97630")
    @Override
    public int getFixedSelectableRowCount() {
        return 0;
    }

    @objid ("8ebd29d8-c068-11e1-8c0a-002564c97630")
    @Override
    public int getRowHeightMinimum() {
        return 10;
    }

    @objid ("8ebd29dd-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isColumnResizable(int col) {
        return (col == 0);
    }

    @objid ("8ebd29e3-c068-11e1-8c0a-002564c97630")
    @Override
    public void setColumnWidth(int col, int value) {
        super.setColumnWidth(col, value);
    }

    @objid ("8ebeb045-c068-11e1-8c0a-002564c97630")
    @Override
    public boolean isRowResizable(int row) {
        return false;
    }

    @objid ("8ebeb04b-c068-11e1-8c0a-002564c97630")
    @Override
    public int getColumnWidth(int col) {
        return super.getColumnWidth(col);
    }

}
