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
                                    

package org.modelio.diagram.styles.editingsupport.font;

import java.text.MessageFormat;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.modelio.core.ui.CoreFontRegistry;

@objid ("85a2342c-1926-11e2-92d2-001ec947c8cc")
public class FontDialogEditor extends CellEditor {
    /**
     * The value of this cell editor; initially <code>null</code>.
     */
    @objid ("85a2342d-1926-11e2-92d2-001ec947c8cc")
    private Font value = null;

    /**
     * The editor control.
     */
    @objid ("85a2342f-1926-11e2-92d2-001ec947c8cc")
    private Composite editor;

    /**
     * The current contents.
     */
    @objid ("85a23431-1926-11e2-92d2-001ec947c8cc")
     Control contents;

    /**
     * The label that gets reused by <code>updateLabel</code>.
     */
    @objid ("85a23433-1926-11e2-92d2-001ec947c8cc")
    private Label defaultLabel;

    /**
     * The button.
     */
    @objid ("85a23435-1926-11e2-92d2-001ec947c8cc")
     Button button;

    /**
     * Listens for 'focusLost' events and fires the 'apply' event as long as the focus wasn't lost because the dialog
     * was opened.
     */
    @objid ("85a23437-1926-11e2-92d2-001ec947c8cc")
    private FocusListener buttonFocusListener;

    /**
     * Creates a new dialog cell editor with no control
     * @param color
     * @param parent the parent control
     */
    @objid ("85a23439-1926-11e2-92d2-001ec947c8cc")
    protected FontDialogEditor(Composite parent) {
        this(parent, SWT.NONE);
    }

    /**
     * Creates a new dialog cell editor parented under the given control. The cell editor value is <code>null</code>
     * initially, and has no validator.
     * @param parent the parent control
     * @param style the style bits
     * @since 2.1
     */
    @objid ("85a2343d-1926-11e2-92d2-001ec947c8cc")
    public FontDialogEditor(Composite parent, int style) {
        super(parent, style);
    }

    /**
     * Creates the button for this cell editor under the given parent control.
     * <p>
     * The default implementation of this framework method creates the button display on the right hand side of the
     * dialog cell editor. Subclasses may extend or reimplement.
     * </p>
     * @param parent the parent control
     * @return the new button control
     */
    @objid ("85a23442-1926-11e2-92d2-001ec947c8cc")
    protected static Button createButton(Composite parent) {
        Button result = new Button(parent, SWT.DOWN);
        result.setText("..."); //$NON-NLS-1$
        return result;
    }

    /**
     * Creates the controls used to show the value of this cell editor.
     * <p>
     * The default implementation of this framework method creates a label widget, using the same font and background
     * color as the parent control.
     * </p>
     * <p>
     * Subclasses may reimplement. If you reimplement this method, you should also reimplement
     * <code>updateContents</code>.
     * </p>
     * @param cell the control for this cell editor
     * @return the underlying control
     */
    @objid ("85a23448-1926-11e2-92d2-001ec947c8cc")
    protected Control createContents(Composite cell) {
        this.defaultLabel = new Label(cell, SWT.LEFT);
        this.defaultLabel.setFont(cell.getFont());
        this.defaultLabel.setBackground(cell.getBackground());
        return this.defaultLabel;
    }

    @objid ("85a49689-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected Control createControl(final Composite parent) {
        Font font = parent.getFont();
        Color bg = parent.getBackground();
        
        this.editor = new Composite(parent, this.getStyle());
        this.editor.setFont(font);
        this.editor.setBackground(bg);
        this.editor.setLayout(new DialogCellLayout());
        
        this.contents = this.createContents(this.editor);
        this.updateContents(this.value);
        
        this.button = FontDialogEditor.createButton(this.editor);
        this.button.setFont(font);
        
        this.button.addKeyListener(new KeyAdapter() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.character == '\u001b') { // Escape
                    FontDialogEditor.this.fireCancelEditor();
                }
            }
        });
        
        this.button.addFocusListener(this.getButtonFocusListener());
        
        this.button.addSelectionListener(new SelectionAdapter() {
            /* (non-Javadoc)
             * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
             */
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetSelected(SelectionEvent event) {
                // Remove the button's focus listener since it's guaranteed
                // to lose focus when the dialog opens
                FontDialogEditor.this.button.removeFocusListener(FontDialogEditor.this.getButtonFocusListener());
                Object newValue = FontDialogEditor.this.openDialogBox(FontDialogEditor.this.editor);
                // Re-add the listener once the dialog closes
                FontDialogEditor.this.button.addFocusListener(FontDialogEditor.this.getButtonFocusListener());
                if (newValue != null) {
                    boolean newValidState = FontDialogEditor.this.isCorrect(newValue);
                    if (newValidState) {
                        FontDialogEditor.this.markDirty();
                        FontDialogEditor.this.doSetValue(CoreFontRegistry.getFont((FontData) newValue));
                    } else {
                        // try to insert the current value into the error message.
                        FontDialogEditor.this.setErrorMessage(MessageFormat.format(FontDialogEditor.this.getErrorMessage(),
                                                                                   new Object[] { newValue.toString() }));
                    }
                    FontDialogEditor.this.fireApplyEditorValue();
                }
            }
        });
        
        this.setValueValid(true);
        
        this.defaultLabel.addMouseListener(new MouseAdapter() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void mouseDoubleClick(MouseEvent e) {
                Object newValue = FontDialogEditor.this.openDialogBox(FontDialogEditor.this.editor);
                if (newValue != null) {
                    boolean newValidState = FontDialogEditor.this.isCorrect(newValue);
                    if (newValidState) {
                        FontDialogEditor.this.markDirty();
                        FontDialogEditor.this.doSetValue(newValue);
                    } else {
                        // try to insert the current value into the error message.
                        FontDialogEditor.this.setErrorMessage(MessageFormat.format(FontDialogEditor.this.getErrorMessage(),
                                                                                   new Object[] { newValue.toString() }));
                    }
                    FontDialogEditor.this.fireApplyEditorValue();
                }
            }
        });
        return this.editor;
    }

    @objid ("85a49690-1926-11e2-92d2-001ec947c8cc")
    @Override
    public void deactivate() {
        if (this.button != null && !this.button.isDisposed()) {
            this.button.removeFocusListener(this.getButtonFocusListener());
        }
        
        super.deactivate();
    }

    @objid ("85a49693-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected Object doGetValue() {
        return this.value;
    }

    @objid ("85a49698-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected void doSetFocus() {
        this.button.setFocus();
        // add a FocusListener to the button
        this.button.addFocusListener(this.getButtonFocusListener());
    }

    /**
     * Return a listener for button focus.
     * @return FocusListener
     */
    @objid ("85a4969b-1926-11e2-92d2-001ec947c8cc")
    FocusListener getButtonFocusListener() {
        if (this.buttonFocusListener == null) {
            this.buttonFocusListener = new FocusListener() {
        
                @Override
                public void focusGained(FocusEvent e) {
                    // Do nothing
                }
        
                @SuppressWarnings("synthetic-access")
                @Override
                public void focusLost(FocusEvent e) {
                    FontDialogEditor.this.focusLost();
                }
            };
        }
        return this.buttonFocusListener;
    }

    @objid ("85a496a0-1926-11e2-92d2-001ec947c8cc")
    @Override
    protected void doSetValue(Object newValue) {
        this.value = (Font) newValue;
        this.updateContents(newValue);
    }

    /**
     * Returns the default label widget created by <code>createContents</code>.
     * @return the default label widget
     */
    @objid ("85a496a4-1926-11e2-92d2-001ec947c8cc")
    protected Label getDefaultLabel() {
        return this.defaultLabel;
    }

    /**
     * Opens a dialog box under the given parent control and returns the dialog's value when it closes, or
     * <code>null</code> if the dialog was canceled or no selection was made in the dialog.
     * <p>
     * This framework method must be implemented by concrete subclasses. It is called when the user has pressed the
     * button and the dialog box must pop up.
     * </p>
     * @param cellEditorWindow the parent control cell editor's window so that a subclass can adjust the dialog box accordingly
     * @return the selected value, or <code>null</code> if the dialog was canceled or no selection was made in the
     * dialog
     */
    @objid ("85a496a9-1926-11e2-92d2-001ec947c8cc")
    protected Object openDialogBox(Control cellEditorWindow) {
        final Display display = cellEditorWindow.getDisplay();
        final Shell centerShell = new Shell(cellEditorWindow.getShell(), SWT.NO_TRIM);
        centerShell.setLocation(display.getCursorLocation());
        
        FontDialog ftDialog = new FontDialog(centerShell, SWT.NONE);
        if (this.value != null) {
            ftDialog.setFontList(this.value.getFontData());
        }
        FontData fData = ftDialog.open();
        
        if (fData != null) {
            return fData;
        }
        return null;
    }

    /**
     * Updates the controls showing the value of this cell editor.
     * <p>
     * The default implementation of this framework method just converts the passed object to a string using
     * <code>toString</code> and sets this as the text of the label widget.
     * </p>
     * <p>
     * Subclasses may reimplement. If you reimplement this method, you should also reimplement
     * <code>createContents</code>.
     * </p>
     * @param newValue the new value of this cell editor
     */
    @objid ("85a496af-1926-11e2-92d2-001ec947c8cc")
    protected void updateContents(Object newValue) {
        if (this.defaultLabel == null) {
            return;
        }
        
        String text = "";//$NON-NLS-1$
        if (newValue != null) {
            if (newValue instanceof Font)
                text = FontService.getFontLabel((Font) newValue);
            else
                text = newValue.toString();
        }
        this.defaultLabel.setText(text);
    }

    /**
     * Internal class for laying out the dialog.
     */
    @objid ("85a496b3-1926-11e2-92d2-001ec947c8cc")
    private class DialogCellLayout extends Layout {
        @objid ("85a496b5-1926-11e2-92d2-001ec947c8cc")
        public DialogCellLayout() {
            super();
        }

        @objid ("85a496b7-1926-11e2-92d2-001ec947c8cc")
        @Override
        public void layout(Composite editorToLayout, boolean force) {
            Rectangle bounds = editorToLayout.getClientArea();
            Point size = FontDialogEditor.this.button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            if (FontDialogEditor.this.contents != null) {
                FontDialogEditor.this.contents.setBounds(0, 0, bounds.width - size.x, bounds.height);
            }
            FontDialogEditor.this.button.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
        }

        @objid ("85a496bc-1926-11e2-92d2-001ec947c8cc")
        @Override
        public Point computeSize(Composite editorToCompute, int wHint, int hHint, boolean force) {
            if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
                return new Point(wHint, hHint);
            }
            Point contentsSize = FontDialogEditor.this.contents.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            Point buttonSize = FontDialogEditor.this.button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
            // Just return the button width to ensure the button is not clipped
            // if the label is long.
            // The label will just use whatever extra width there is
            Point result = new Point(buttonSize.x, Math.max(contentsSize.y, buttonSize.y));
            return result;
        }

    }

}
