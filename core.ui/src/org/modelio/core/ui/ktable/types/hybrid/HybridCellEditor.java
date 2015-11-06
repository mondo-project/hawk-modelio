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
                                    

package org.modelio.core.ui.ktable.types.hybrid;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.hybridtext.HybridTextElement;
import org.modelio.core.ui.hybridtext.IHybridTextElementSelectionListener;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * This Editor is used to edit a ModelElement name in a textfield. The editor is opened on a SINGLECLICK. When the user validates by
 * enter or tab performs a search of the ModelElement using the value entered in the textfield. If a unique ModelElement is found,
 * the input is validated and the editor is closed. If no or several model elements are found, the ElementFinderDialog box is
 * popped, its name filter being set to the value entered by the user and the search being launched with this value. When closing
 * the finder dialog without a selected element or by cancel, the edition is not acknowledged otherwise it is.
 */
@objid ("8d614358-c068-11e1-8c0a-002564c97630")
public class HybridCellEditor extends KTableCellEditor {
    @objid ("8d62c9c6-c068-11e1-8c0a-002564c97630")
    protected boolean acceptNullValue = false;

    @objid ("c3f0ed5c-1b48-497b-acd6-a60086d6b25a")
    private boolean acceptStringType;

    @objid ("8d61435a-c068-11e1-8c0a-002564c97630")
    private List<Class<? extends MObject>> targetClasses = null;

    @objid ("8d62c9c5-c068-11e1-8c0a-002564c97630")
    private IMObjectFilter elementFilter = null;

    @objid ("16222cb4-16da-11e2-aa0d-002564c97630")
    private IModelioPickingService pickingService;

    @objid ("16222cb6-16da-11e2-aa0d-002564c97630")
    private ICoreSession session;

    @objid ("eb265db4-b642-4d60-8777-4353884df402")
    protected KeyAdapter keyListener = new KeyAdapter() {
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

    @objid ("cfed78df-ca98-4cc9-abbf-e77a789db5fd")
    private HybridTextElement textElement;

    /**
     * Creates an hybrid cell editor.
     * @param acceptNullValue true to accept <i>null</i> values, else false.
     * @param pickingService the modelio picking service.
     */
    @objid ("8d62c9cb-c068-11e1-8c0a-002564c97630")
    public HybridCellEditor(boolean acceptNullValue, IModelioPickingService pickingService, ICoreSession session, boolean acceptStringType) {
        this.acceptNullValue = acceptNullValue;
        this.pickingService = pickingService;
        this.session = session;
        this.acceptStringType = acceptStringType;
    }

    /**
     * Close forcibly the editor.
     */
    @objid ("8d62c9d7-c068-11e1-8c0a-002564c97630")
    public void forceClose() {
        if (this.m_Table != null && this.textElement != null) {
            super.close(true);
            this.textElement.activatePicking(null);
            this.textElement.activateCompletion(null);
            this.textElement = null;
        }
    }

    @objid ("8d62c9da-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY;
    }

    @objid ("8d62c9df-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(KTable table, int col, int row, Rectangle rect) {
        super.open(table, col, row, rect);
        
        final Object content = this.m_Model.getContentAt(this.m_Col, this.m_Row);
        ModelElement me = null;
        if (content instanceof ModelElement) {
            me = (ModelElement) content;
        }
        
        this.textElement.setValue(me);
        this.textElement.getTextControl().selectAll();
        this.textElement.setAcceptNullValue(this.acceptNullValue);
    }

    @objid ("8d62c9e6-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(Object content) {
        if (this.textElement != null) {
            final ModelElement me = (ModelElement) content;
            this.textElement.setValue(me);
            this.textElement.activateCompletion(CoreSession.getSession(me));
        }
    }

    /**
     * Set the filter.
     * @param elementFilter the filter.
     */
    @objid ("8d62c9eb-c068-11e1-8c0a-002564c97630")
    public void setElementFilter(IMObjectFilter elementFilter) {
        this.elementFilter = elementFilter;
    }

    /**
     * Set the allowed classes as values.
     * <p>
     * The given metaclasses as well as their children classes will be allowed.
     * @param targetClass the allowed metaclasses.
     */
    @objid ("8d62c9f0-c068-11e1-8c0a-002564c97630")
    public void setTargetClasses(final List<Class<? extends MObject>> targetClass) {
        this.targetClasses = targetClass;
    }

    @objid ("8d62c9f7-c068-11e1-8c0a-002564c97630")
    @Override
    protected Control createControl() {
        this.textElement = new HybridTextElement(this.m_Table, SWT.NONE);
        this.textElement.setAcceptNullValue(this.acceptNullValue);
        this.textElement.setAcceptStringValue(this.acceptStringType);
        for(Class<? extends MObject> c : this.targetClasses) {
            this.textElement.getAcceptedMetaclasses().add(Metamodel.getMClass(c));
        }
        this.textElement.setFilter(this.elementFilter);
        this.textElement.activatePicking(this.pickingService);
        this.textElement.activateCompletion(this.session);
        this.textElement.addListener(new IHybridTextElementSelectionListener() {
            
            @Override
            public void selectedElementChanged(Object oldElement, Object newElement) {
                validate(true);
            }
        });
        this.textElement.getTextControl().addKeyListener(this.keyListener);
        return this.textElement.getTextControl();
    }

    @objid ("8d62c9fc-c068-11e1-8c0a-002564c97630")
    @Override
    protected void onKeyPressed(KeyEvent e) {
        if (e.character == '\r') {
            super.close(true);
        } else if (e.character == SWT.ESC) {
            super.close(false);
            if (this.textElement != null) {                
                this.textElement.activatePicking(null);
                this.textElement.activateCompletion(null);
                this.textElement = null;
            }
        } else {
            super.onKeyPressed(e);
        }
    }

    @objid ("8d64506a-c068-11e1-8c0a-002564c97630")
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

    @objid ("8d64506d-c068-11e1-8c0a-002564c97630")
    protected void validate(boolean save) {
        if (this.textElement != null) {
            // Update the data model
            if (save) {
                this.m_Model.setContentAt(this.m_Col, this.m_Row, this.textElement.getValue());
            }
            
            this.textElement.activatePicking(null);
            this.textElement.activateCompletion(null);
        
        }
    }

}
