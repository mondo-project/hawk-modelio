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
                                    

package org.modelio.core.ui.ktable.types.text;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.editors.KTableCellEditorText2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.modelio.ui.UIColor;

/**
 * Our text cell editor.
 */
@objid ("8dd3b412-c068-11e1-8c0a-002564c97630")
public class TextCellEditor extends KTableCellEditorText2 {
    @objid ("8dd3b414-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(boolean save) {
        super.close(save);
        selectNextField();
    }

    /**
     * @return Returns a value indicating on which actions
     * this editor should be activated.
     */
    @objid ("8dd3b418-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    @objid ("8dd3b41e-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(KTable table, int col, int row, Rectangle rect) {
        super.open(table, col, row, rect);
    }

    @objid ("8dd3b425-c068-11e1-8c0a-002564c97630")
    @Override
    protected Control createControl() {
        super.createControl();
        
        this.m_Text.setBackground(UIColor.TEXT_WRITABLE_BG);
        this.m_Text.setForeground(UIColor.BLACK);
        
        this.m_Text.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent event) {
                // Nothing to do
            }
        
            @SuppressWarnings("synthetic-access")
            @Override
            public void keyReleased(KeyEvent event) {
                if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == 'a') {
                    TextCellEditor.this.m_Text.selectAll();
                }
            }
        });
        return this.m_Text;
    }

    @objid ("8dd3b42a-c068-11e1-8c0a-002564c97630")
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
