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
                                    

package org.modelio.core.ui.ktable.types.enumeration;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.MetamodelLabels;

/**
 * Enumeration cell editor.
 * <p>
 * Allows the user to type the literal name or to choose a literal in a combo.
 */
@objid ("8d5fbca4-c068-11e1-8c0a-002564c97630")
public class EnumCellEditor extends KTableCellEditor {
    @objid ("a470e34b-c068-11e1-8c0a-002564c97630")
    private String[] m_items = null;

    @objid ("8d5fbca6-c068-11e1-8c0a-002564c97630")
    private Object[] m_enumObjects;

    @objid ("8d5fbcac-c068-11e1-8c0a-002564c97630")
    private final TraverseListener travListener = new TraverseListener() {
        @Override
        public void keyTraversed(TraverseEvent e) {
            onTraverse(e);
        }
    };

    @objid ("8d5fbcae-c068-11e1-8c0a-002564c97630")
    private Combo m_Combo = null;

    @objid ("8d5fbcaf-c068-11e1-8c0a-002564c97630")
    private final KeyAdapter keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            try {
                onKeyPressed(e);
            } catch (final Exception ex) {
                // Do nothing
            }
            e.doit = false;
        }
    };

    /**
     * Creates an enumeration cell editor.
     * @param enumValue The eunm to display in the combo.
     */
    @objid ("8d5fbcb1-c068-11e1-8c0a-002564c97630")
    public EnumCellEditor(Object enumValue) {
        if (enumValue instanceof Class<?>) {
            final Class<?> enumClass = (Class<?>)enumValue;
            if (enumClass.isEnum()) {
                this.m_enumObjects = enumClass.getEnumConstants();
            } else {
                this.m_enumObjects = new Object[0];
            }
        } else {
            // TODO to remove when all property model return a PropertyType
            this.m_enumObjects = enumValue.getClass().getEnumConstants();
        }
        
        // Fill the items with translated labels
        this.m_items = new String[this.m_enumObjects.length];
        for (int i = 0; i < this.m_enumObjects.length; i++) {
            this.m_items[i] = getLabelFor(this.m_enumObjects[i].toString());
        }
    }

    @objid ("8d614326-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(boolean save) {
        // Nothing to do
    }

    @objid ("8d61432a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    @objid ("8d61432f-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(KTable table, int row, int col, Rectangle rect) {
        super.open(table, row, col, rect);
        
        // When opening, get the value from the model
        // and display the translated label
        final Object value = this.m_Model.getContentAt(this.m_Col, this.m_Row);
        this.m_Combo.setText(getLabelFor(value.toString()));
    }

    @objid ("8d614336-c068-11e1-8c0a-002564c97630")
    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(new Rectangle(rect.x, rect.y + 1, rect.width, rect.height - 2));
    }

    @objid ("8d61433a-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(Object content) {
        this.m_Combo.setData(content);
    }

    @objid ("8d61433e-c068-11e1-8c0a-002564c97630")
    @Override
    protected Control createControl() {
        this.m_Combo = new Combo(this.m_Table, SWT.READ_ONLY);
        
        this.m_Combo.addPaintListener(new PaintListener() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void paintControl(PaintEvent e) {
                final GC gc = e.gc;
                final Rectangle oldClip = gc.getClipping();
                final Rectangle clip = new Rectangle(e.x, e.y, e.width, e.height);
                final Rectangle textBounds = EnumCellEditor.this.m_Combo.getBounds();
                textBounds.x = 0;
                textBounds.y = 0;
                textBounds.height = textBounds.height - 1;
                textBounds.width = textBounds.width - 1;
        
                final Display display = Display.getCurrent();
                final Color color = display.getSystemColor(SWT.COLOR_BLUE);
                gc.setForeground(color);
                gc.setClipping(clip);
                gc.drawRectangle(textBounds);
                gc.setClipping(oldClip);
            }
        });
        
        if (this.m_items != null) {
            this.m_Combo.setItems(this.m_items);
        }
        
        this.m_Combo.addKeyListener(this.keyListener);
        this.m_Combo.addTraverseListener(this.travListener);
        
        this.m_Combo.addSelectionListener(new SelectionListener() {
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                validate(true);
            }
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        });
        return this.m_Combo;
    }

    /**
     * Overwrite the onTraverse method to ignore arrowup and arrowdown events so
     * that they get interpreted by the editor control.
     * <p>
     * Comment that out if you want the up and down keys move the editor.<br>
     * Hint by David Sciamma.
     */
    @objid ("8d614343-c068-11e1-8c0a-002564c97630")
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

    @objid ("8d614348-c068-11e1-8c0a-002564c97630")
    private static String getLabelFor(String value) {
        String label = null;
        try {
            label = MetamodelLabels.getString(value);
        } catch (final Exception e) {
            label = value;
        }
        return label;
    }

    @objid ("8d61434d-c068-11e1-8c0a-002564c97630")
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

    @objid ("8d61434f-c068-11e1-8c0a-002564c97630")
    @Override
    protected void onKeyPressed(final KeyEvent e) {
        if (e.keyCode == SWT.ARROW_UP) {
            if (this.m_Combo.getSelectionIndex() > 0) {
                this.m_Combo.select(this.m_Combo.getSelectionIndex() - 1);
                this.m_Combo.setSelection(new Point(0, 0));
            }
        } else if (e.keyCode == SWT.ARROW_DOWN) {
            this.m_Combo.select(this.m_Combo.getSelectionIndex() + 1);
            this.m_Combo.setSelection(new Point(0, 0));
        } else if (e.character == '\r') {
            validate(true);
            return;
        } else if (e.character == SWT.ESC) {
            validate(false);
        } else {
            super.onKeyPressed(e);
        }
    }

    @objid ("8d614354-c068-11e1-8c0a-002564c97630")
    protected void validate(final boolean save) {
        // If closing with save, set the model value
        // Get the enum value from the label currently selected in the combo
        if (save) {
            this.m_Model.setContentAt(this.m_Col, this.m_Row, this.m_enumObjects[this.m_Combo.getSelectionIndex()]);
        }
        if (this.m_Combo != null) {
            this.m_Combo.removeKeyListener(this.keyListener);
            this.m_Combo.removeTraverseListener(this.travListener);
        }
        this.m_Combo.removeKeyListener(this.keyListener);
        this.m_Combo.removeTraverseListener(this.travListener);
        super.close(save);
        selectNextField();
        this.m_Combo = null;
    }

}
