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
                                    

package org.modelio.core.ui.ktable.types.modelelement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.core.picking.IPickingClient;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Feature;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Model element cell editor.
 * <p>
 * Allows the user to type the element name or to choose an element in any supported view by clicking on it.
 */
@objid ("8d645073-c068-11e1-8c0a-002564c97630")
public class ModelElementListCellEditor extends KTableCellEditor implements IPickingClient {
    @objid ("a475a616-c068-11e1-8c0a-002564c97630")
    private String[] m_items = null;

    @objid ("8d645083-c068-11e1-8c0a-002564c97630")
    private boolean acceptNullValue = false;

// TODO CHM implements IEditorDropClient {
    @objid ("8d645075-c068-11e1-8c0a-002564c97630")
    private Combo m_Combo = null;

    @objid ("8d645077-c068-11e1-8c0a-002564c97630")
    private final Cursor m_ArrowCursor = new Cursor(Display.getDefault(), SWT.CURSOR_ARROW);

    @objid ("8d645079-c068-11e1-8c0a-002564c97630")
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

    @objid ("8d64507b-c068-11e1-8c0a-002564c97630")
    private final TraverseListener travListener = new TraverseListener() {
        @Override
        public void keyTraversed(TraverseEvent e) {
            onTraverse(e);
        }
    };

    @objid ("8d645080-c068-11e1-8c0a-002564c97630")
    private List<ElementLabel> m_elements = null;

// TODO CHM drop listener
// //private EditorDropListener dropListener;
    @objid ("8d645084-c068-11e1-8c0a-002564c97630")
    private List<Class<? extends MObject>> allowedClasses = null;

    @objid ("163df214-16da-11e2-aa0d-002564c97630")
    private IPickingSession pickingSession;

    @objid ("163e6749-16da-11e2-aa0d-002564c97630")
    private final IModelioPickingService pickingService;

    /**
     * Creates a model element cell editor.
     * @param allowedClasses The allowed metaclasses. The children classes will be allowed too.
     * @param elements The initial value.
     * @param acceptNullValue true to accept empty selection, false to forbid it.
     * @param pickingService the modelio picking service. Optional.
     */
    @objid ("8d64508a-c068-11e1-8c0a-002564c97630")
    public ModelElementListCellEditor(List<Class<? extends MObject>> allowedClasses, List<ModelElement> elements, boolean acceptNullValue, IModelioPickingService pickingService) {
        this.allowedClasses = allowedClasses;
        this.m_elements = new ArrayList<>();
        this.pickingService = pickingService;
        
        for (final ModelElement element : elements) {
            this.m_elements.add(new ElementLabel(element));
        }
        
        Collections.sort(this.m_elements);
        
        this.acceptNullValue = acceptNullValue;
        
        // Fill the items with translated labels
        if (acceptNullValue) {
            this.m_items = new String[this.m_elements.size() + 1];
            String elementName = null;
            ElementLabel elementLabel = null;
            this.m_items[0] = CoreUi.I18N.getString("KTable.None");
            for (int i = 0; i < this.m_elements.size(); i++) {
                elementLabel = this.m_elements.get(i);
                elementName = elementLabel.getLabel();
                this.m_items[i + 1] = elementName;
            }
        } else {
            this.m_items = new String[this.m_elements.size()];
            String elementName = null;
            ElementLabel elementLabel = null;
            for (int i = 0; i < this.m_elements.size(); i++) {
                elementLabel = this.m_elements.get(i);
                elementName = elementLabel.getLabel();
                this.m_items[i] = elementName;
            }
        }
    }

    @objid ("8d645098-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(boolean save) {
        // Nothing to do
    }

    @objid ("8d64509d-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    @objid ("8d65d706-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(KTable table, int row, int col, Rectangle rect) {
        super.open(table, row, col, rect);
        
        // When opening, get the value from the model
        // and display the translated label
        final ModelElement value = (ModelElement) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        if (this.acceptNullValue) {
            if (value != null) {
                final ElementLabel elementLabel = getElementLabel(value.getName());
                if (elementLabel != null) {
                    this.m_Combo.setText(elementLabel.getLabel());
                } else {
                    this.m_Combo.setText(CoreUi.I18N.getString("KTable.None"));
                }
            } else {
                this.m_Combo.setText(CoreUi.I18N.getString("KTable.None"));
            }
        } else {
            if (value != null) {
                final ElementLabel elementLabel = getElementLabel(value.getName());
                if (elementLabel != null) {
                    this.m_Combo.setText(elementLabel.getLabel());
                } else {
                    this.m_Combo.setText("");
                }
            } else {
                this.m_Combo.setText("");
            }
        }
        // m_Combo.setSelection(new Point(0, content.length()));
        
        if (this.pickingService != null) {
            this.pickingSession = this.pickingService.startPicking(this);
        }
    }

    @objid ("8d65d70d-c068-11e1-8c0a-002564c97630")
    @Override
    public void setBounds(Rectangle rect) {
        super.setBounds(new Rectangle(rect.x, rect.y + 1, rect.width, rect.height - 2));
    }

    @objid ("8d65d712-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(Object content) {
        this.m_Combo.setData(content);
    }

    @objid ("8d65d716-c068-11e1-8c0a-002564c97630")
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
                final Rectangle textBounds = ModelElementListCellEditor.this.m_Combo.getBounds();
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
        
        this.m_Combo.setCursor(this.m_ArrowCursor);
        
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
        
        // initDropTarget();
        return this.m_Combo;
    }

    @objid ("8d65d71c-c068-11e1-8c0a-002564c97630")
    @Override
    protected void onKeyPressed(KeyEvent e) {
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

    /**
     * Overwrite the onTraverse method to ignore arrowup and arrowdown events so that they get interpreted by the editor control.
     * <p>
     * Comment that out if you want the up and down keys move the editor.<br>
     * Hint by David Sciamma.
     */
    @objid ("8d65d720-c068-11e1-8c0a-002564c97630")
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

    @objid ("8d65d725-c068-11e1-8c0a-002564c97630")
    private ElementLabel getElementLabel(String elementName) {
        for (final ElementLabel elementLabel : this.m_elements) {
            if (elementName.equals(elementLabel.getElement().getName())) {
                return elementLabel;
            }
        }
        return null;
    }

/*
     * private void initDropTarget() { this.dropListener = new EditorDropListener(this); final int operations = DND.DROP_MOVE |
     * DND.DROP_COPY; final Transfer[] types = new Transfer[] {ModelElementTransfer.getInstance(), PluginTransfer.getInstance() };
     * final DropTarget target = new DropTarget(this.m_Combo, operations); target.setTransfer(types);
     * target.addDropListener(this.dropListener); }
     */
    @objid ("8d65d72a-c068-11e1-8c0a-002564c97630")
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

    @objid ("8d65d72d-c068-11e1-8c0a-002564c97630")
    protected void validate(boolean save) {
        // If closing with save, set the model value
        // Get the enum value from the label currently selected in the combo
        if (save) {
            final Object pickingContent = this.m_Combo.getData();
            if (pickingContent != null) {
                this.m_Model.setContentAt(this.m_Col, this.m_Row, pickingContent);
            } else if (this.acceptNullValue) {
                final int selectedIndex = this.m_Combo.getSelectionIndex();
                if (selectedIndex == 0) {
                    this.m_Model.setContentAt(this.m_Col, this.m_Row, null);
                } else if (selectedIndex > 0) {
                    this.m_Model.setContentAt(this.m_Col, this.m_Row, this.m_elements.get(selectedIndex - 1).getElement());
                }
            } else {
                this.m_Model.setContentAt(this.m_Col, this.m_Row, this.m_elements.get(this.m_Combo.getSelectionIndex())
                        .getElement());
            }
        }
        this.m_Combo.removeKeyListener(this.keyListener);
        this.m_Combo.removeTraverseListener(this.travListener);
        super.close(save);
        selectNextField();
        this.m_Combo = null;
        this.m_ArrowCursor.dispose();
        
        // Stop the picking session
        if (this.pickingSession != null) {
            this.pickingService.stopPicking(this.pickingSession);
            this.pickingSession = null;
        }
    }

/*
     * @Override public boolean acceptDroppedElements(MObject[] target) { if (target.length != 1) { return false; } for (final
     * Class<?> c : this.allowedClasses) { if (c.isAssignableFrom(target[0].getClass())) { return true; } } return false; }
     */
    @objid ("163e4035-16da-11e2-aa0d-002564c97630")
    @Override
    public boolean hover(MObject target) {
        for (final Class<?> c : this.allowedClasses) {
            if (c.isAssignableFrom(target.getClass())) {
                return true;
            }
        }
        return false;
    }

    @objid ("163f0384-16da-11e2-aa0d-002564c97630")
    @Override
    public void abort() {
        // Stop the picking session
        if (this.pickingSession != null) {
            this.pickingService.stopPicking(this.pickingSession);
            this.pickingSession = null;
        }
        
        if (this.m_Combo != null && !this.m_Combo.isDisposed()) {
            this.m_Combo.setData(null);
            this.m_Combo.removeKeyListener(this.keyListener);
            this.m_Combo.removeTraverseListener(this.travListener);
            super.close(false);
            this.m_Combo = null;
        }
    }

/*
     * @Override public void setDroppedElements(MObject[] target) { if (acceptElement (target[0])) { setContent (target[0]);
     * validate (true); } }
     */
    @objid ("163f51a4-16da-11e2-aa0d-002564c97630")
    @Override
    public boolean pick(MObject target) {
        if (hover(target)) {
            setContent(target);
            validate(true);
        
            // Stop the picking session
            if (this.pickingSession != null) {
                this.pickingService.stopPicking(this.pickingSession);
                this.pickingSession = null;
            }
        
            return true;
        } else {
            return false;
        }
    }

    @objid ("8d65d730-c068-11e1-8c0a-002564c97630")
    protected static class ElementLabel implements Comparable<ElementLabel> {
        @objid ("a478076a-c068-11e1-8c0a-002564c97630")
        private final String label;

        @objid ("8d65d733-c068-11e1-8c0a-002564c97630")
        private final ModelElement element;

        @objid ("8d65d737-c068-11e1-8c0a-002564c97630")
        public String getLabel() {
            return this.label;
        }

        @objid ("8d65d73b-c068-11e1-8c0a-002564c97630")
        public ModelElement getElement() {
            return this.element;
        }

        @objid ("8d65d741-c068-11e1-8c0a-002564c97630")
        public ElementLabel(ModelElement element) {
            super();
            this.element = element;
            
            final ModelElementLabelService labelService = new ModelElementLabelService();
            this.label = labelService.getLabel(element);
        }

        @objid ("8d675da8-c068-11e1-8c0a-002564c97630")
        @Override
        public int compareTo(ElementLabel o) {
            if (getElement() instanceof Feature && o.getElement() instanceof Feature) {
                final Feature thisFeature = (Feature) getElement();
                final Feature oFeature = (Feature) o.getElement();
            
                final VisibilityMode thisVisibility = thisFeature.getVisibility();
                final VisibilityMode oVisibility = oFeature.getVisibility();
            
                if (thisVisibility != oVisibility) {
                    if (thisVisibility == VisibilityMode.PUBLIC) {
                        return -1;
                    } else if (thisVisibility == VisibilityMode.PACKAGEVISIBILITY && oVisibility == VisibilityMode.PUBLIC) {
                        return 1;
                    } else if (thisVisibility == VisibilityMode.PROTECTED
                            && (oVisibility == VisibilityMode.PUBLIC || oVisibility == VisibilityMode.PACKAGEVISIBILITY)) {
                        return 1;
                    } else if (thisVisibility == VisibilityMode.PRIVATE) {
                        return 1;
                    }
            
                    return -1;
                }
            }
            return getLabel().compareTo(o.getLabel());
        }

    }

}
