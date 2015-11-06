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
                                    

package org.modelio.core.ui.ktable.types.element;

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
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This Editor is used to edit a ModelElement multiple dependency field in a dialog box.
 * The editor is opened on a SINGLECLICK.
 * When closing the edition dialog by OK the input is validated otherwise it is canceled.
 */
@objid ("8dc164ba-c068-11e1-8c0a-002564c97630")
public class MultipleElementCellEditor extends KTableCellEditor {
    @objid ("a4c4336d-c068-11e1-8c0a-002564c97630")
    private String fieldName;

    @objid ("8dc164c0-c068-11e1-8c0a-002564c97630")
    private MObject editedElement;

    @objid ("8dc164c3-c068-11e1-8c0a-002564c97630")
    private boolean active = false;

    @objid ("8dc164bc-c068-11e1-8c0a-002564c97630")
    private Class<? extends MObject> targetClass;

    @objid ("8dc164c4-c068-11e1-8c0a-002564c97630")
    private IMObjectFilter elementFilter = null;

    @objid ("8dc164c5-c068-11e1-8c0a-002564c97630")
     ElementEditionDialog dialog = null;

    @objid ("8dc164c6-c068-11e1-8c0a-002564c97630")
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

    @objid ("8dc164c7-c068-11e1-8c0a-002564c97630")
    protected TraverseListener travListener = new TraverseListener() {
        @Override
        public void keyTraversed(TraverseEvent e) {
            // onTraverse(e);
        }
    };

    @objid ("15bdeca4-16da-11e2-aa0d-002564c97630")
    private IModel session;

    @objid ("15be13b4-16da-11e2-aa0d-002564c97630")
    private IModelioPickingService pickingService;

    /**
     * Close the editor.
     * @param save true to save the content in the data model.
     */
    @objid ("8dc164c8-c068-11e1-8c0a-002564c97630")
    public void closeEditor(boolean save) {
        this.active = false;
        super.close(save);
        selectNextField();
    }

    @objid ("8dc2eb26-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(boolean save) {
        // Nothing to do.
    }

    @objid ("8dc2eb2a-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    /**
     * @return the edited element.
     */
    @objid ("8dc2eb2f-c068-11e1-8c0a-002564c97630")
    public MObject getEditedElement() {
        return this.editedElement;
    }

    /**
     * @return the element filter.
     */
    @objid ("8dc2eb36-c068-11e1-8c0a-002564c97630")
    public IMObjectFilter getElementFilter() {
        return this.elementFilter;
    }

    /**
     * @return the filed name
     */
    @objid ("8dc2eb3b-c068-11e1-8c0a-002564c97630")
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * @return the allowed metaclasses.
     */
    @objid ("8dc2eb40-c068-11e1-8c0a-002564c97630")
    public Class<? extends MObject> getTargetClass() {
        return this.targetClass;
    }

    /**
     * Initialize the editor.
     * @param element The edited element
     * @param name The edited field name
     * @param target The allowed target metaclasses, children classes will be allowed too.
     * @param filter An element filter.
     */
    @objid ("8dc2eb47-c068-11e1-8c0a-002564c97630")
    public void init(MObject element, String name, Class<? extends MObject> target, IMObjectFilter filter) {
        this.targetClass = target;
        this.fieldName = name;
        this.editedElement = element;
        this.elementFilter = filter;
    }

    @objid ("8dc2eb52-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(KTable table, int col, int row, Rectangle rect) {
        super.open(table, col, row, rect);
        
        if (!this.active) {
            this.active = true;
        
            final List<MObject> initialContent = (List<MObject>) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        
            this.dialog = ElementEditionDialog.getInstance(table.getShell(), this.session, this.pickingService, this, initialContent, new EditionValidator(this.m_Model, this.m_Col, this.m_Row));
        
            // Open dialog in modal mode
            this.dialog.setBlockOnOpen(false);
            this.dialog.open();
        }
    }

    @objid ("8dc2eb59-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(Object content) {
        List<MObject> me = (List<MObject>) content;
        this.dialog.setContent(me);
    }

    @objid ("8dc2eb5d-c068-11e1-8c0a-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
    protected Control createControl() {
        final Text m_Text2 = new Text(this.m_Table, SWT.NONE);
        m_Text2.setData(null);
        m_Text2.setEnabled(false);
        m_Text2.setEditable(false);
        
        final List<ModelElement> mcontent = (List<ModelElement>) this.m_Model.getContentAt(this.m_Col, this.m_Row);
        final StringBuffer buf = new StringBuffer();
        boolean first = true;
        for (final ModelElement me : mcontent) {
            // Append separator
            if (first)
                first = false;
            else
                buf.append(", ");
        
            // Append 'name (from ...)'
            buf.append(me.getName());
        
            final ModelElement owner = (ModelElement) me.getCompositionOwner();
            if (owner != null) {
                buf.append("  (from ");
                buf.append(owner.getName());
                buf.append(")");
            }
        }
        m_Text2.setText(buf.toString());
        return m_Text2;
    }

    @objid ("8dc2eb62-c068-11e1-8c0a-002564c97630")
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

    @objid ("15be13b5-16da-11e2-aa0d-002564c97630")
    public MultipleElementCellEditor(IModel session, IModelioPickingService pickingService) {
        this.session = session;
        this.pickingService = pickingService;
    }

    @objid ("8dc2eb64-c068-11e1-8c0a-002564c97630")
    private class EditionValidator implements IEditionValidator {
        @objid ("8dc471c5-c068-11e1-8c0a-002564c97630")
        private final int col;

        @objid ("8dc471c7-c068-11e1-8c0a-002564c97630")
        private final int row;

        @objid ("9e5ba11c-c5d6-11e1-8f21-002564c97630")
        private final KTableModel model;

        @objid ("8dc471cb-c068-11e1-8c0a-002564c97630")
        EditionValidator(KTableModel model, int col, int row) {
            this.model = model;
            this.col = col;
            this.row = row;
        }

        @objid ("8dc471d0-c068-11e1-8c0a-002564c97630")
        @Override
        public void validate(List<MObject> newContent) {
            if (newContent != null) {
                this.model.setContentAt(this.col, this.row, newContent);
            }
            closeEditor(true);
        }

    }

}
