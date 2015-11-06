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
                                    

package org.modelio.diagram.styles.editingsupport.element;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.core.ui.textelement.ITextElementSelectionListener;
import org.modelio.core.ui.textelement.TextElement;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.IModel;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * Cell editor for a {@link MRef}
 */
@objid ("642dc660-7e85-4a89-a9b5-6a724ee76057")
public class ElementCellEditor extends CellEditor {
    @objid ("b8fa1e54-fc1b-4890-972f-051e7b3e3dc7")
    private final ICoreSession session;

    @objid ("d3baf059-a429-4d2e-8a02-cf1bdc3b87dc")
    private TextElement textElement;

    @objid ("2b13e533-631d-42f5-bc61-6a8794be1fe7")
    private final IModelioPickingService pickingService;

    /**
     * Constructor.
     * @param parent the parent composite
     * @param session the modeling session
     * @param pickingService the Modelio picking service
     */
    @objid ("5b648224-278d-4931-afd3-309ec265bb70")
    public ElementCellEditor(Composite parent, ICoreSession session, IModelioPickingService pickingService) {
        super();
        
        this.session = session;
        this.pickingService = pickingService;
        
        this.setStyle(SWT.NONE);
        create(parent);
    }

    @objid ("7c3c984b-ac9a-4bbf-adce-446ae2ec6a96")
    @Override
    protected Object doGetValue() {
        final MObject mObject = this.textElement.getValue();
        if (mObject != null)
            return new MRef(mObject);
        else
            return null;
    }

    /**
     * Values are MRef, the wrapped TextElement control works with Element instances.<br>
     * Therefore, the MRef value is resolved into an
     * MObject instance that can be used to initialize the TextElement editor.
     */
    @objid ("1e4d037d-1b90-4ded-b244-2f5e7a535b04")
    @Override
    protected void doSetValue(Object value) {
        if (value != null) {
            final MObject obj;
            if (value instanceof MObject) {
                obj = (MObject) value;
            } else if (value instanceof MRef) {
                final MRef mRef = (MRef) value;
                obj = this.session.getModel().findByRef(mRef, IModel.NODELETED);
            } else {
                throw new IllegalArgumentException(value.toString());
            }
            this.textElement.setValue(obj);
        } else {
            this.textElement.setValue(null);
        }
    }

    @objid ("2792442a-1421-4e88-8aac-4c1b3482d786")
    @Override
    public Text createControl(Composite parent) {
        this.textElement = new TextElement(parent, getStyle());
        if (this.session != null) {
            this.textElement.activateCompletion(this.session);
        }
        if (this.pickingService != null) {
            this.textElement.activatePicking(this.pickingService);
        }
        
        this.textElement.setAcceptNullValue(true);
        this.textElement.getAcceptedMetaclasses().add(SmClass.getClass(MObject.class));
        
        final Text textControl = this.textElement.getTextControl();
        
        textControl.setEnabled(this.session!=null && this.pickingService!=null);
        
        textControl.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_ESCAPE
                        || e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                }
            }
        });        
        
        this.textElement.addListener(new ITextElementSelectionListener() {
            
            @Override
            public void selectedElementChanged(MObject oldElement, MObject newElement) {
                //dbgPrint("ITextElementSelectionListener.selectedElementChanged("+oldElement+","+newElement+")");
                applyEditorValueAndDeactivate();
            }
        });
        
        /*this.textElement.getTextControl().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                dbgPrint("FocusAdapter.focusLost");
                applyEditorValueAndDeactivate();
            }
        });*/
        
        textControl.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetDefaultSelected(SelectionEvent event) {
                //dbgPrint("SelectionAdapter.widgetDefaultSelected");
                applyEditorValueAndDeactivate();
            }
        
            @Override
            public void widgetSelected(SelectionEvent event) {
                //dbgPrint("SelectionAdapter.widgetSelected");
                applyEditorValueAndDeactivate();
            }
        });
        return textControl;
    }

    @objid ("9a6e39ce-4990-4e7b-b5d0-15425d905f5e")
    @Override
    protected void doSetFocus() {
        this.textElement.getTextControl().setFocus();
    }

    @objid ("947d23df-57ce-4e1b-9e64-c2883df09a2a")
    @Override
    public boolean isDeleteEnabled() {
        if (this.textElement == null)
            return false;
        
        final Text control = this.textElement.getTextControl();
        return control != null && !control.isDisposed() && control.isEnabled();
    }

    @objid ("63d3cf7f-d56a-4a7c-af07-cbf8ec19467e")
    @Override
    public void performDelete() {
        doSetValue(null);
    }

    @objid ("6de19881-ca98-4f69-9dde-14df3cbf22bc")
    protected void applyEditorValueAndDeactivate() {
        fireApplyEditorValue();
        deactivate();
    }

    @objid ("3c8a042c-695c-4102-afc6-b55f4ef3de29")
    @Deprecated
    void dbgPrint(String msg) {
        final Text textControl = this.textElement.getTextControl();
        final boolean disposed = textControl.isDisposed();
        final String message = "ECE: "+msg+" on "+this.textElement+" disposed="+disposed+" visible="+(!disposed && textControl.isVisible());
        //System.out.println(message);
        new Throwable(message).printStackTrace();
    }

    @objid ("e52ac278-ba92-45e5-b36e-fd54a7876a42")
    @Override
    protected boolean dependsOnExternalFocusListener() {
        return false;
    }

}
