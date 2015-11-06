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
import de.kupzog.ktable.KTableCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.modelio.metamodel.uml.infrastructure.Element;

/**
 * Our KTable cell editor for multi-line texts.
 * <p>
 * Opens a big dialog box that allows the user to enter multi-line text.
 */
@objid ("8db9c37f-c068-11e1-8c0a-002564c97630")
public class MultilineTextCellEditor extends KTableCellEditor {
    @objid ("8db9c380-c068-11e1-8c0a-002564c97630")
    private boolean active = false;

    @objid ("8db9c382-c068-11e1-8c0a-002564c97630")
    private Element editedElement;

    @objid ("a48fd532-c068-11e1-8c0a-002564c97630")
    private String fieldName;

    @objid ("8db9c381-c068-11e1-8c0a-002564c97630")
     EditionDialog dialog = null;

    @objid ("8db9c386-c068-11e1-8c0a-002564c97630")
    protected KeyAdapter keyListener = new KeyAdapter() {
        @SuppressWarnings("synthetic-access")
        @Override
        public void keyPressed(KeyEvent e) {
            try {
                onKeyPressed(e);
            } catch (final Exception ex) {
                ex.printStackTrace();
                // Do nothing
            }
        }
    };

    @objid ("8db9c387-c068-11e1-8c0a-002564c97630")
    protected TraverseListener travListener = new TraverseListener() {
        @Override
        public void keyTraversed(TraverseEvent e) {
            // onTraverse(e);
        }
    };

    @objid ("8db9c388-c068-11e1-8c0a-002564c97630")
    public MultilineTextCellEditor(final Element element, final String name) {
        this.editedElement = element;
        this.fieldName = name;
    }

    @objid ("8db9c390-c068-11e1-8c0a-002564c97630")
    public void closeEditor(final boolean save) {
        this.active = false;
        super.close(save);
        selectNextField();
    }

    @objid ("8db9c394-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(final boolean save) {
        // Nothing to do.
    }

    @objid ("8db9c399-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    @objid ("8db9c39e-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(final KTable table, final int col, final int row, final Rectangle rect) {
        super.open(table, col, row, rect);
        
        if (!this.active) {
            this.active = true;
        
            final String initialContent = (String) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        
            this.dialog = EditionDialog.getInstance(table.getShell(),  this, initialContent);
        
            // Open dialog in modal mode
            this.dialog.setBlockOnOpen(false);
            this.dialog.open();
        }
    }

    @objid ("8dbb4a0d-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(final Object content) {
        this.m_Model.setContentAt(this.m_Col, this.m_Row, content);
        closeEditor(true);
    }

    @objid ("8dbb4a12-c068-11e1-8c0a-002564c97630")
    @Override
    protected Control createControl() {
        final Text m_Text2 = new Text(this.m_Table, SWT.MULTI);
        m_Text2.setData(null);
        m_Text2.setEnabled(false);
        m_Text2.setEditable(false);
        
        final String mcontent = (String) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        
        m_Text2.setText(mcontent);
        return m_Text2;
    }

    @objid ("8dbb4a17-c068-11e1-8c0a-002564c97630")
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

    @objid ("8dbb4a19-c068-11e1-8c0a-002564c97630")
    public Element getEditedElement() {
        return this.editedElement;
    }

    @objid ("8dbb4a1f-c068-11e1-8c0a-002564c97630")
    public String getFieldName() {
        return this.fieldName;
    }

    @objid ("c133313e-639b-4800-a7bb-42728df075fc")
    @Override
    public void dispose() {
        this.dialog.close();
        super.dispose();
    }

}
