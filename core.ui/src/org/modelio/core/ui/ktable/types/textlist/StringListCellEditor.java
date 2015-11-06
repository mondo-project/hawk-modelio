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
                                    

package org.modelio.core.ui.ktable.types.textlist;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * This Editor is used to edit a ModelElement multiple dependency field in a dialog box.
 * The editor is opened on a SINGLECLICK.
 * When closing the edition dialog by OK the input is validated otherwise it is canceled.
 */
@objid ("8dcc12fa-c068-11e1-8c0a-002564c97630")
public class StringListCellEditor extends KTableCellEditor {
    @objid ("8dcc12fc-c068-11e1-8c0a-002564c97630")
    private boolean active = false;

    @objid ("a4e586a8-c068-11e1-8c0a-002564c97630")
    private String title = "";

    @objid ("a4e586ac-c068-11e1-8c0a-002564c97630")
    private String message = "";

    @objid ("a4e586b0-c068-11e1-8c0a-002564c97630")
    private String detailedMessage = "";

    @objid ("8dcc1300-c068-11e1-8c0a-002564c97630")
    private int size = 0;

    @objid ("8dcc1301-c068-11e1-8c0a-002564c97630")
    protected KeyAdapter keyListener = new KeyAdapter() {
        @SuppressWarnings("synthetic-access")
        @Override
        public void keyPressed(KeyEvent e) {
            try {
                onKeyPressed(e);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Do nothing
            }
        }
    };

    @objid ("8dcc1302-c068-11e1-8c0a-002564c97630")
    protected TraverseListener travListener = new TraverseListener() {
        @Override
        public void keyTraversed(TraverseEvent e) {
            // onTraverse(e);
        }
    };

    /**
     * Creates a string list cell editor.
     * @param title The editor title.
     * @param message A short message for the user.
     * @param size The size of the editor.
     * @param detailedMessage A detailed message for the user.
     */
    @objid ("8dcc1303-c068-11e1-8c0a-002564c97630")
    public StringListCellEditor(String title, String message, int size, String detailedMessage) {
        this.title = title;
        this.message = message;
        this.size = size;
        this.detailedMessage = detailedMessage;
    }

    /**
     * Close the editor.
     * @param save true to save the content to the data model.
     */
    @objid ("8dcc130a-c068-11e1-8c0a-002564c97630")
    public void closeEditor(boolean save) {
        this.active = false;
        super.close(save);
        selectNextField();
    }

    @objid ("8dcc130e-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(boolean save) {
        // Nothing to do.
    }

    @objid ("8dcc1312-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    /**
     * (non-Javadoc)
     * @see de.kupzog.ktable.KTableCellEditor#open(de.kupzog.ktable.KTable, int, int, org.eclipse.swt.graphics.Rectangle)
     */
    @objid ("8dcc1317-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
    public void open(KTable table, int col, int row, Rectangle rect) {
        super.open(table, col, row, rect);
        
        if (!this.active) {
            this.active = true;
            List<String> initialContent = (ArrayList<String>) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        
            StringListEditionDialog dialog = StringListEditionDialog.getInstance(
                    table.getShell(), 
                    this.title, 
                    this.message, 
                    this.size,
                    this.detailedMessage,
                    this, 
                    initialContent, 
                    new StringListValidator(this.m_Model, this.m_Col, this.m_Row));
        
            // Open dialog in modal mode
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
    }

    @objid ("8dcc131f-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(Object content) {
        // EList<Element> me = (EList<Element>) content;
        // this.dialog.setContent(me);
    }

    @objid ("8dcd9987-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
    protected Control createControl() {
        Text m_Text2 = new Text(this.m_Table, SWT.NONE);
        m_Text2.setData(null);
        m_Text2.setEnabled(false);
        m_Text2.setEditable(false);
        
        List<String> mcontent = (List<String>) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        
        StringBuffer text = new StringBuffer();
        
        if (mcontent != null) {
            for (int i = 0; i < mcontent.size(); i++) {
                if (i > 0) {
                    text.append(", ");
                }
                text.append(mcontent.get(i));
            }
        }
        
        m_Text2.setText(text.toString());
        return m_Text2;
    }

    @objid ("8dcd998c-c068-11e1-8c0a-002564c97630")
    private void selectNextField() {
        int nextCol = this.m_Col + 1;
        int nextRow = this.m_Row;
        
        if (nextCol > this.m_Model.getColumnCount() - 1) {
            nextCol = 1;
            nextRow++;
        }
        
        if (nextRow > this.m_Model.getRowCount() - 1) {
            nextRow = 1;
        }
        
        this.m_Table.setSelection(nextCol, nextRow, true);
    }

    @objid ("8dcd998e-c068-11e1-8c0a-002564c97630")
    private static class StringListValidator implements IStringListValidator {
        @objid ("8dcd9990-c068-11e1-8c0a-002564c97630")
        private int col;

        @objid ("8dcd9991-c068-11e1-8c0a-002564c97630")
        private int row;

        @objid ("9e10db25-c5d6-11e1-8f21-002564c97630")
        private KTableModel model;

        @objid ("8dcd9993-c068-11e1-8c0a-002564c97630")
        StringListValidator(KTableModel model, int col, int row) {
            this.model = model;
            this.col = col;
            this.row = row;
        }

        @objid ("8dcd9998-c068-11e1-8c0a-002564c97630")
        @Override
        public void validate(List<String> newContent) {
            if (newContent != null) {
                this.model.setContentAt(this.col, this.row, newContent);
            }
        }

    }

}
