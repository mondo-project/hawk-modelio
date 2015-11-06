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
                                    

package org.modelio.property.ui.data;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * Fixes a size computing bug in KTable, as the library is dead on source forge...
 */
@objid ("511b70c5-e21b-406c-8e3c-e61896005d0b")
public class ModelioKTable extends KTable {
    @objid ("bcb4c009-da69-4a7d-bea5-902e82ae16a7")
    public ModelioKTable(Composite parent, int style) {
        super(parent, style);
        getHorizontalBar().setVisible(false);
        getVerticalBar().setVisible(false);
    }

    /**
     * Calculates the preferred size of the KTable. The preferred size of a KTable is calculated such that there is
     * enough space available to show all header columns and rows, as well as a configurable number of data columns and
     * rows.
     * @see #setNumColsVisibleInPreferredSize(int)
     * @see #setNumRowsVisibleInPreferredSize(int)
     * @param wHint The width hint (can be <code>SWT.DEFAULT</code>)
     * @param hHint The height hint (can be <code>SWT.DEFAULT</code>)
     * @param changed <code>true</code> if the control's contents have changed, and <code>false</code> otherwise
     * @return the preferred size of the control.
     */
    @objid ("3289b8ae-f9f6-4d63-93ee-9c1ae4159570")
    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        // start with margins
        int height = 1;
        int width = 1;
        
        if (this.m_Model != null) {
            // Determine height of header rows
            for (int i = 0; i < this.m_Model.getFixedHeaderRowCount(); i++) {
                height += this.m_Model.getRowHeight(i);
            }
        
            // Add height of data rows to display
            int rowsVisible = 0;
            for (int i = this.m_Model.getFixedHeaderRowCount(); i < this.m_Model.getFixedHeaderRowCount()
                    + this.m_numRowsVisibleInPreferredSize
                    && i < this.m_Model.getRowCount(); i++) {
                height += this.m_Model.getRowHeight(i);
                rowsVisible++;
            }
        
            // Make sure that there is room for m_numRowsVisibleInPreferredSize rows, even if there are not that
            // many data rows currently available
            for (int i = rowsVisible; i < this.m_numRowsVisibleInPreferredSize; i++) {
                height += this.m_preferredSizeDefaultRowHeight;
            }
        
            // Determine width of header columns
            for (int i = 0; i < this.m_Model.getFixedHeaderColumnCount(); i++) {
                width += this.m_Model.getColumnWidth(i);
            }
        
            // Add width of data columns to display
            for (int i = this.m_Model.getFixedHeaderColumnCount(); i < this.m_Model.getFixedHeaderColumnCount()
                    + this.m_numColsVisibleInPreferredSize
                    && i < this.m_Model.getColumnCount(); i++) {
                // was width += this.m_Model.getColumnWidth(row); 
                width += this.m_Model.getColumnWidth(i);
            }
        }
        
        // Take scrollbars into account
        if (getHorizontalBar() != null && getHorizontalBar().isVisible()) {
            height += getHorizontalBar().getSize().y;
        }
        if (getVerticalBar() != null && getVerticalBar().isVisible()) {
            width += getVerticalBar().getSize().x;
        }
        return new Point(width, height);
    }

    @objid ("80a11d9d-0d0e-4f7e-9492-1f6d1186b000")
    @Override
    @SuppressWarnings("synthetic-access")
    protected void createListeners() {
        super.createListeners();
        
        addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (ModelioKTable.this.m_defaultCursor != null)
                    ModelioKTable.this.m_defaultCursor.dispose();
        
                if (ModelioKTable.this.m_defaultRowResizeCursor != null)
                    ModelioKTable.this.m_defaultRowResizeCursor.dispose();
        
                if (ModelioKTable.this.m_defaultColumnResizeCursor != null)
                    ModelioKTable.this.m_defaultColumnResizeCursor.dispose();
            }
        });
    }

    @objid ("b3fef658-4397-4023-b939-8ab113d5fbe2")
    @Override
    protected void updateScrollbarVisibility() {
        // No need to compute things if the table is not layouted yet...
        if (!getClientArea().isEmpty()) {
            super.updateScrollbarVisibility();
        }
    }

}
