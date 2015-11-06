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
                                    

package org.modelio.core.ui.treetable.combo;

import java.text.MessageFormat;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * This class is a modified copy of the original ComboBoxCellEditor class. The modification provides direct validation
 * of the editor once a value is selected (otherwise the user has to click to select a value and to validate the editor
 * separately)
 * <p>
 * The original ComboBoxCellEditor class is not intended to be subclassed.
 * </p>
 */
@objid ("6b306d51-1eba-11e2-9382-bc305ba4815c")
class ComboBoxCellEditor2 extends CellEditor {
    /**
     * The list is dropped down when the activation is done through the mouse
     */
    @objid ("6b306d53-1eba-11e2-9382-bc305ba4815c")
    public static final int DROP_DOWN_ON_MOUSE_ACTIVATION = 1;

    /**
     * The list is dropped down when the activation is done through the keyboard
     */
    @objid ("6b309462-1eba-11e2-9382-bc305ba4815c")
    public static final int DROP_DOWN_ON_KEY_ACTIVATION = 1 << 1;

    /**
     * The list is dropped down when the activation is done without ui-interaction
     */
    @objid ("6b30bb70-1eba-11e2-9382-bc305ba4815c")
    public static final int DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION = 1 << 2;

    /**
     * The list is dropped down when the activation is done by traversing from cell to cell
     */
    @objid ("6b30bb73-1eba-11e2-9382-bc305ba4815c")
    public static final int DROP_DOWN_ON_TRAVERSE_ACTIVATION = 1 << 3;

    @objid ("6b30e281-1eba-11e2-9382-bc305ba4815c")
    private int activationStyle = SWT.NONE;

    /**
     * The zero-based index of the selected item.
     */
    @objid ("6b310992-1eba-11e2-9382-bc305ba4815c")
     int selection;

    /**
     * Default ComboBoxCellEditor style
     */
    @objid ("6b3130a0-1eba-11e2-9382-bc305ba4815c")
    private static final int defaultStyle = SWT.NONE;

    /**
     * The list of items to present in the combo box.
     */
    @objid ("8b58d953-1eba-11e2-9382-bc305ba4815c")
    private String[] items;

    /**
     * The custom combo box control.
     */
    @objid ("6b310994-1eba-11e2-9382-bc305ba4815c")
     CCombo comboBox;

    /**
     * Creates a new cell editor with no control and no st of choices. Initially, the cell editor has no cell validator.
     * @since 2.1
     * @see CellEditor#setStyle
     * @see CellEditor#create
     * @see ComboBoxCellEditor#setItems
     * @see CellEditor#dispose
     */
    @objid ("6b3130a3-1eba-11e2-9382-bc305ba4815c")
    public ComboBoxCellEditor2() {
        setStyle(defaultStyle);
    }

    /**
     * Creates a new cell editor with a combo containing the given list of choices and parented under the given control.
     * The cell editor value is the zero-based index of the selected item. Initially, the cell editor has no cell
     * validator and the first item in the list is selected.
     * @param parent the parent control
     * @param items the list of strings for the combo box
     */
    @objid ("6b3157b1-1eba-11e2-9382-bc305ba4815c")
    public ComboBoxCellEditor2(final Composite parent, final String[] items) {
        this(parent, items, defaultStyle);
    }

    /**
     * Creates a new cell editor with a combo containing the given list of choices and parented under the given control.
     * The cell editor value is the zero-based index of the selected item. Initially, the cell editor has no cell
     * validator and the first item in the list is selected.
     * @param parent the parent control
     * @param items the list of strings for the combo box
     * @param style the style bits
     * @since 2.1
     */
    @objid ("6b317ec5-1eba-11e2-9382-bc305ba4815c")
    public ComboBoxCellEditor2(final Composite parent, final String[] items, final int style) {
        super(parent, style);
        setItems(items);
    }

    /**
     * Returns the list of choices for the combo box
     * @return the list of choices for the combo box
     */
    @objid ("6b31cce5-1eba-11e2-9382-bc305ba4815c")
    public String[] getItems() {
        return this.items;
    }

    /**
     * Sets the list of choices for the combo box
     * @param items the list of choices for the combo box
     */
    @objid ("6b321b00-1eba-11e2-9382-bc305ba4815c")
    public void setItems(final String[] items) {
        Assert.isNotNull(items);
        this.items = items;
        populateComboBoxItems();
    }

    @objid ("6b324211-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void activate(final ColumnViewerEditorActivationEvent activationEvent) {
        super.activate(activationEvent);
        if (activationStyle != SWT.NONE) {
            boolean dropDown = false;
            if ((activationEvent.eventType == ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION || activationEvent.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) &&
                (activationStyle & DROP_DOWN_ON_MOUSE_ACTIVATION) != 0) {
                dropDown = true;
            } else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED &&
                       (activationStyle & DROP_DOWN_ON_KEY_ACTIVATION) != 0) {
                dropDown = true;
            } else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC &&
                       (activationStyle & DROP_DOWN_ON_PROGRAMMATIC_ACTIVATION) != 0) {
                dropDown = true;
            } else if (activationEvent.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL &&
                       (activationStyle & DROP_DOWN_ON_TRAVERSE_ACTIVATION) != 0) {
                dropDown = true;
            }
        
            if (dropDown) {
                getControl().getDisplay().asyncExec(new Runnable() {
        
                    public void run() {
                        ((CCombo) getControl()).setListVisible(true);
                    }
        
                });
        
            }
        }
    }

    @objid ("6b326920-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected Control createControl(final Composite parent) {
        comboBox = new CCombo(parent, getStyle());
        comboBox.setFont(parent.getFont());
        
        populateComboBoxItems();
        
        comboBox.addKeyListener(new KeyAdapter() {
            // hook key pressed - see PR 14201
            @Override
            public void keyPressed(KeyEvent e) {
                keyReleaseOccured(e);
            }
        });
        
        comboBox.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                applyEditorValueAndDeactivate();
            }
        
            @Override
            public void widgetSelected(SelectionEvent event) {
                selection = comboBox.getSelectionIndex();
                applyEditorValueAndDeactivate();
            }
        });
        
        comboBox.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });
        
        comboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                ComboBoxCellEditor2.this.focusLost();
            }
        });
        return comboBox;
    }

    /**
     * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method returns the
     * zero-based index of the current selection.
     * @return the zero-based index of the current selection wrapped as an <code>Integer</code>
     */
    @objid ("6b329031-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected Object doGetValue() {
        return new Integer(selection);
    }

    @objid ("6b32b742-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected void doSetFocus() {
        comboBox.setFocus();
    }

    /**
     * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method sets the
     * minimum width of the cell. The minimum width is 10 characters if <code>comboBox</code> is not <code>null</code>
     * or <code>disposed</code> else it is 60 pixels to make sure the arrow button and some text is visible. The list of
     * CCombo will be wide enough to show its longest item.
     */
    @objid ("6b32de50-1eba-11e2-9382-bc305ba4815c")
    @Override
    public LayoutData getLayoutData() {
        LayoutData layoutData = super.getLayoutData();
        if ((comboBox == null) || comboBox.isDisposed()) {
            layoutData.minimumWidth = 60;
        } else {
            // make the comboBox 10 characters wide
            GC gc = new GC(comboBox);
            layoutData.minimumWidth = (gc.getFontMetrics().getAverageCharWidth() * 10) + 10;
            gc.dispose();
        }
        return layoutData;
    }

    /**
     * This method allows to control how the combo reacts when activated
     * @param activationStyle the style used
     */
    @objid ("6b330562-1eba-11e2-9382-bc305ba4815c")
    public void setActivationStyle(final int activationStyle) {
        this.activationStyle = activationStyle;
    }

    /**
     * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method accepts a
     * zero-based index of a selection.
     * @param value the zero-based index of the selection wrapped as an <code>Integer</code>
     */
    @objid ("6b332c72-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected void doSetValue(final Object value) {
        Assert.isTrue(comboBox != null && (value instanceof Integer));
        selection = ((Integer) value).intValue();
        comboBox.select(selection);
    }

    /**
     * Updates the list of choices for the combo box for the current control.
     */
    @objid ("6b335383-1eba-11e2-9382-bc305ba4815c")
    private void populateComboBoxItems() {
        if (comboBox != null && items != null) {
            comboBox.removeAll();
            for (int i = 0; i < items.length; i++) {
                comboBox.add(items[i], i);
            }
        
            setValueValid(true);
            selection = 0;
        }
    }

    /**
     * Applies the currently selected value and deactivates the cell editor
     */
    @objid ("6b337a91-1eba-11e2-9382-bc305ba4815c")
    void applyEditorValueAndDeactivate() {
        // must set the selection before getting value
        selection = comboBox.getSelectionIndex();
        Object newValue = doGetValue();
        markDirty();
        boolean isValid = isCorrect(newValue);
        setValueValid(isValid);
        
        if (!isValid) {
            // Only format if the 'index' is valid
            if (items.length > 0 && selection >= 0 && selection < items.length) {
                // try to insert the current value into the error message.
                setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { items[selection] }));
            } else {
                // Since we don't have a valid index, assume we're using an
                // 'edit'
                // combo so format using its text value
                setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { comboBox.getText() }));
            }
        }
        
        fireApplyEditorValue();
        deactivate();
    }

    @objid ("6b337a94-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected void focusLost() {
        if (isActivated()) {
            applyEditorValueAndDeactivate();
        }
    }

    @objid ("6b33a1a2-1eba-11e2-9382-bc305ba4815c")
    @Override
    protected void keyReleaseOccured(final KeyEvent keyEvent) {
        if (keyEvent.character == '\u001b') { // Escape character
            fireCancelEditor();
        } else if (keyEvent.character == '\t') { // tab key
            applyEditorValueAndDeactivate();
        }
    }

}
