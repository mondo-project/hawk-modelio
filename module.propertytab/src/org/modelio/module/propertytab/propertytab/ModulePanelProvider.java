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
                                    

package org.modelio.module.propertytab.propertytab;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.propertiesPage.IModulePropertyPage;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.module.propertytab.model.ModulePropertyModel;
import org.modelio.module.propertytab.ui.panel.IModulePropertyPanel;
import org.modelio.module.propertytab.ui.panel.PropertyPanelFactory;
import org.modelio.ui.panel.IPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IStatusChangeEvent;
import org.modelio.vcore.session.api.model.change.IStatusChangeListener;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("c882a7c0-1eba-11e2-9382-bc305ba4815c")
public class ModulePanelProvider implements IPanelProvider {
    /**
     * The Notes view ID.
     */
    @objid ("044badb3-1ebb-11e2-9382-bc305ba4815c")
    public static final String VIEW_ID = "org.modelio.module.propertytab.ModulePropertyViewID";

    @objid ("c882a7c1-1eba-11e2-9382-bc305ba4815c")
    private ModulePropertyModel currentElement;

    @objid ("c882a7c2-1eba-11e2-9382-bc305ba4815c")
    private ModelChangeListener modelChangeListener;

    @objid ("c882ced0-1eba-11e2-9382-bc305ba4815c")
    private SashForm shform = null;

    @objid ("c882ced1-1eba-11e2-9382-bc305ba4815c")
    protected Composite parentComposite = null;

    @objid ("c882ced2-1eba-11e2-9382-bc305ba4815c")
    private ICoreSession modelingSession;

    @objid ("c882ced3-1eba-11e2-9382-bc305ba4815c")
    private IModelioPickingService pickingService;

    @objid ("c882ced4-1eba-11e2-9382-bc305ba4815c")
    private IModulePropertyPanel propertyPanel = null;

    @objid ("c882f5e0-1eba-11e2-9382-bc305ba4815c")
    private IModulePropertyPage propertyPage;

    /**
     * Constructor.
     */
    @objid ("c8831cf0-1eba-11e2-9382-bc305ba4815c")
    public ModulePanelProvider() {
    }

    @objid ("c8831cf3-1eba-11e2-9382-bc305ba4815c")
    @Override
    public SashForm getPanel() {
        return this.shform;
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @objid ("c8834402-1eba-11e2-9382-bc305ba4815c")
    @Focus
    void setFocus() {
        if (this.propertyPanel != null) {
            this.propertyPanel.setFocus();
        }
    }

    @objid ("c8836b11-1eba-11e2-9382-bc305ba4815c")
    @Override
    public SashForm createPanel(Composite parent) {
        this.parentComposite = parent;
        
        this.shform = new SashForm(parent, SWT.HORIZONTAL);
        this.shform.setLayout(new FillLayout());
        this.propertyPanel = PropertyPanelFactory.createStandardPanel(this.shform, null);
        return this.shform;
    }

    /**
     * Makes this view editable. <code>modelingSession</code> and <code>modelService</code> are mandatory otherwise edition cannot
     * be supported.
     * 
     * To deactivate edition, call <code>activateEdition(null, null, null)</code>
     * @param newModelingSession the current edited modeling session.
     */
    @objid ("c8839221-1eba-11e2-9382-bc305ba4815c")
    public void activateEdition(ICoreSession newModelingSession, IModelioPickingService aPickingService) {
        if (newModelingSession != null) {
            this.modelingSession = newModelingSession;
            this.pickingService = aPickingService;
            this.modelChangeListener = new ModelChangeListener(this);
            this.modelingSession.getModelChangeSupport().addModelChangeListener(this.modelChangeListener);
            this.propertyPanel.start(this.modelingSession);
        } else {
            if (this.modelingSession != null) {
                // remove listeners
                this.modelingSession.getModelChangeSupport().removeModelChangeListener(this.modelChangeListener);
                this.modelChangeListener = null;
                this.modelingSession = null;
            }
        
            if (this.shform != null) {
                setInput(null);
                this.propertyPanel.stop();
            }
        }
    }

    /**
     * Get the current element displayed by the view.
     * @return the model element whose notes are currently listed in the tree panel. May be null.
     */
    @objid ("c883b933-1eba-11e2-9382-bc305ba4815c")
    @Override
    public Object getInput() {
        return this.currentElement;
    }

    /**
     * Set the current element displayed by the view.
     * @param input the model element whose note are to be listed in the tree panel. May be null.
     */
    @objid ("c8840750-1eba-11e2-9382-bc305ba4815c")
    @Override
    public void setInput(final Object input) {
        boolean valid = false;
        if (this.shform != null && input instanceof List<?>) {
            List<?> selectedElements = (List<?>)input;
            for (Object selected : selectedElements) {
                if (selected instanceof MObject) {
                    valid = true;
                } else {
                    valid = false;
                    break;
                }
            }
        }
        
        if (valid == false) {
            // Consider this is an empty selection
            this.currentElement = null;
            this.propertyPanel.setInput(this.pickingService, null);
            return;
        }
        
        @SuppressWarnings("unchecked") 
        List<MObject> elList = (List<MObject>) input;
        this.currentElement = new ModulePropertyModel(this.modelingSession,this.propertyPage, elList);
        this.propertyPanel.setInput(this.pickingService, this.currentElement);
        return;
    }

    /**
     * Refresh the module view.
     */
    @objid ("c8842e62-1eba-11e2-9382-bc305ba4815c")
    public void refreshView() {
        Composite control = this.propertyPanel.getComposite();
        if (control==null || control.isDisposed())
            return;
        
        if (this.currentElement != null) {
            this.currentElement.clearTable();
            this.propertyPanel.setInput( this.pickingService, this.currentElement);
        } else {
            this.currentElement = null;
            this.propertyPanel.setInput(this.pickingService, null);
        }
    }

    @objid ("c8845570-1eba-11e2-9382-bc305ba4815c")
    public ModulePanelProvider(IModulePropertyPage propertyPage) {
        this.propertyPage = propertyPage;
    }

    @objid ("75dab102-8ea1-4368-be4d-0a338e50198b")
    @Override
    public boolean isRelevantFor(Object obj) {
        return true;
    }

    @objid ("c2a2f432-55cb-4a54-99e3-07f080bd7a5a")
    @Override
    public String getHelpTopic() {
        return null;
    }

    @objid ("c8845573-1eba-11e2-9382-bc305ba4815c")
    private class ModelChangeListener implements IModelChangeListener, IStatusChangeListener {
        @objid ("c8845574-1eba-11e2-9382-bc305ba4815c")
        protected ModulePanelProvider moduleView;

        @objid ("c8847c80-1eba-11e2-9382-bc305ba4815c")
        public ModelChangeListener(final ModulePanelProvider moduleView) {
            this.moduleView = moduleView;
        }

        @objid ("c8847c84-1eba-11e2-9382-bc305ba4815c")
        @Override
        public void modelChanged(final IModelChangeEvent event) {
            // Re enter the UI thread
            Display display = Display.getDefault();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ModelChangeListener.this.moduleView.refreshView();
                    }
                });
            }
        }

        @objid ("c884a393-1eba-11e2-9382-bc305ba4815c")
        @Override
        public void statusChanged(final IStatusChangeEvent event) {
            // Re enter the UI thread
            Display display = Display.getDefault();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ModelChangeListener.this.moduleView.refreshView();
                    }
                });
            }
        }

    }

}
