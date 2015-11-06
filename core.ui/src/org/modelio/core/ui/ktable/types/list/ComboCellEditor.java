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
                                    

package org.modelio.core.ui.ktable.types.list;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.editors.KTableCellEditorCombo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * Our combo cell editor.
 */
@objid ("8d59a212-c068-11e1-8c0a-002564c97630")
public class ComboCellEditor extends KTableCellEditorCombo {
    @objid ("8d59a214-c068-11e1-8c0a-002564c97630")
    private CCombo m_Combo = null;

    @objid ("8d59a215-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(boolean save) {
        super.close(save);
        selectNextField();
    }

    /**
     * @return Returns a value indicating on which actions
     * this editor should be activated.
     */
    @objid ("8d59a219-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY | KEY_RETURN_AND_SPACE;
    }

    @objid ("8d59a21f-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(KTable table, int col, int row, Rectangle rect) {
        super.open(table, col, row, rect);
    }

    @objid ("8d59a226-c068-11e1-8c0a-002564c97630")
    @Override
    protected Control createControl() {
        this.m_Combo = (CCombo)super.createControl();
        return this.m_Combo;
    }

    @objid ("8d59a22b-c068-11e1-8c0a-002564c97630")
    @Override
    protected void onKeyPressed(KeyEvent e) {
        if (!this.m_Combo.getListVisible() && e.keyCode == SWT.ARROW_UP) {
            if (this.m_Combo.getSelectionIndex() > 0) {
                this.m_Combo.select(this.m_Combo.getSelectionIndex() - 1);
                this.m_Combo.setSelection(new Point(0, 0));
            }
        } else if (!this.m_Combo.getListVisible() && e.keyCode == SWT.ARROW_DOWN) {
            this.m_Combo.select(this.m_Combo.getSelectionIndex() + 1);
            this.m_Combo.setSelection(new Point(0, 0));
        }
        super.onKeyPressed(e);
    }

    @objid ("8d59a22f-c068-11e1-8c0a-002564c97630")
    @Override
    protected void onTraverse(TraverseEvent e) {
        // set selection to the appropriate next element:
        switch (e.keyCode) {
            case SWT.ARROW_UP: // Go to previous item
            case SWT.ARROW_DOWN: // Go to next item
            {
                // Just don't treat the event
                break;
            }
            default: {
                super.onTraverse(e);
                break;
            }
        }
    }

    @objid ("8d59a233-c068-11e1-8c0a-002564c97630")
    private void selectNextField() {
        int nextCol = this.m_Col + 1;
        int nextRow = this.m_Row;
        
        if (nextCol > this.m_Model.getColumnCount() - 1) {
            nextCol = 1;
            nextRow ++;
        }
        
        if (nextRow > this.m_Model.getRowCount() - 1) {
            nextRow = 1;
        }
        
        this.m_Table.setSelection(nextCol, nextRow, true);
    }

}
