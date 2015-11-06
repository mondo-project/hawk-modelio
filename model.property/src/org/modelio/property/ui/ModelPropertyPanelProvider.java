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
                                    

package org.modelio.property.ui;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.property.ui.data.ContentPanel;
import org.modelio.property.ui.data.DataPanelInput;
import org.modelio.property.ui.tree.TreePanel;
import org.modelio.property.ui.tree.TreePanelInput;
import org.modelio.ui.panel.IPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IModelChangeSupport;
import org.modelio.vcore.session.api.model.change.IStatusChangeEvent;
import org.modelio.vcore.session.api.model.change.IStatusChangeListener;
import org.modelio.vcore.smkernel.SmObjectImpl;

/**
 * The ModelPropertyView manages two GUI panels:
 * <ul>
 * <li>the tree panel that displays the selected model element along with list of the stereotypes that are available on it</li>
 * <li>the content panel that displays the contents of the 'typeItem' selected in the tree panel.</li>
 * </ul>
 * The ModelPropertyView aggregates the two panel and controls their behavior so that the two panels do not have to know anything
 * about each other, it also provides services to the view toolbar handlers so that these handlers so that they do not have to deal
 * with the two panels organization.
 * 
 * The model element whose type items are currently listed is called the currentElement. The particular stereotype whose contents is
 * currently shown is called the current type item.
 * 
 * @author phv
 */
@objid ("8fa7c870-c068-11e1-8c0a-002564c97630")
public class ModelPropertyPanelProvider implements IPanelProvider {
    @objid ("8fa7c874-c068-11e1-8c0a-002564c97630")
    private Element currentElement;

    /**
     * Tells whether the view is "pinned".
     * <p>
     * A pinned view doesn't update on selection changes or navigation events.
     */
    @objid ("8fa7c87a-c068-11e1-8c0a-002564c97630")
    protected boolean pinned;

    @objid ("e570a847-40af-42a1-9341-faef4dbc629f")
    private boolean showHiddenAnnotations;

    @objid ("8fa7c872-c068-11e1-8c0a-002564c97630")
    private LayoutChangeListener layoutChangeListener = null;

    @objid ("8fa7c873-c068-11e1-8c0a-002564c97630")
    private ContentPanel contentPanel = null;

    @objid ("8fa7c878-c068-11e1-8c0a-002564c97630")
    private SelectionChangedListener treeSelectionListener;

    @objid ("8fa7c879-c068-11e1-8c0a-002564c97630")
    private TreePanel treePanel = null;

    @objid ("869933f6-cf24-11e1-80a9-002564c97630")
    private ICoreSession modelingSession;

    @objid ("1fa136e0-d005-11e1-9020-002564c97630")
    private IMModelServices modelService;

    @objid ("06b4c8f6-16d1-11e2-aa0d-002564c97630")
    private IModelioPickingService pickingService;

    @objid ("c0268e42-e2bc-49b0-b8b0-2182e7c53316")
    private ModelChangeListener modelChangeListener;

    @objid ("ca00fb95-e089-48ad-bb59-107eb316d59b")
    private IActivationService activationService;

    @objid ("8c65dd74-940f-4834-83bc-f7973d4d5d5e")
    private IProjectService projectService;

    @objid ("cd5a4d17-51b1-4495-b0fa-6bfc9c530aa6")
    private SashForm shform = null;

    @objid ("82d0ce97-d239-4a5b-a7ff-56a1616f28f4")
     Composite theViewParent = null;

    /**
     * Instantiate a new Model Property panel. The property view is read only.
     * @See activateEdition
     */
    @objid ("869933f7-cf24-11e1-80a9-002564c97630")
    public ModelPropertyPanelProvider() {
        // Empty
    }

    /**
     * Called by the framework to create the view and initialize it.
     * @return the SashForm containing the property panel.
     */
    @objid ("8fa7c87e-c068-11e1-8c0a-002564c97630")
    @Override
    public SashForm createPanel(Composite parent) {
        this.theViewParent = parent;
        
        this.shform = new SashForm(parent, SWT.HORIZONTAL);
        this.shform.setLayout(new FillLayout());
        
        this.treePanel = new TreePanel(this.shform, SWT.NONE);
        this.contentPanel = new ContentPanel(this.shform, SWT.NONE);
        
        this.treePanel.setLayout(new FillLayout());
        this.contentPanel.setLayout(new FillLayout());
        
        this.shform.setWeights(new int[] { 30, 70 });
        
        this.layoutChangeListener = new LayoutChangeListener();
        enableAutoLayout();
        
        // TODO CHM Activate the context for NoteView :
        // IContextService contextService = (IContextService)
        // getViewSite().getService(IContextService.class);
        // contextService.activateContext("com.modeliosoft.modelio.bindings.context.AnnotationViewID");
        
        // TODO CHM set tooltip
        // setTitleToolTip(ModelProperty.I18N.getString("NotesTabTooltip"));
        
        this.treeSelectionListener = new SelectionChangedListener(this);
        this.treePanel.getTreeViewer().addSelectionChangedListener(this.treeSelectionListener);
        parent.layout();
        return this.shform;
    }

    @objid ("76530813-c677-11e1-8f21-002564c97630")
    @Override
    public Composite getPanel() {
        return this.shform;
    }

    /**
     * Get the current element displayed by the view.
     * @return the model element whose content is listed in the property panel. May be null.
     */
    @objid ("8faa2997-c068-11e1-8c0a-002564c97630")
    @Override
    public Element getInput() {
        return this.currentElement;
    }

    /**
     * Set the current element displayed by the view.
     * @param selectedElement the model element whose content is listed in the property panel. May be null.
     */
    @objid ("8faa299e-c068-11e1-8c0a-002564c97630")
    @Override
    public void setInput(Object selectedElement) {
        if (selectedElement == null || !(selectedElement instanceof Element)) {
            this.currentElement = null;
            this.treePanel.setInput(createTreeInput(true));
            return;
        }
        
        this.currentElement = (Element) selectedElement;
        
        TreePanelInput newInput = createTreeInput(false);
        newInput.setPreselectedTypingElement(null);
        this.treePanel.setInput(newInput);
    }

    /**
     * Tells whether the view is "pinned".
     * <p>
     * A pinned view doesn't update on selection changes or navigation events.
     * @return <code>true</code> if the view is pinned, else <code>false</code>.
     */
    @objid ("8faa29c2-c068-11e1-8c0a-002564c97630")
    public boolean isPinned() {
        return this.pinned;
    }

    /**
     * Set the layout to horizontal.
     */
    @objid ("8fa7c882-c068-11e1-8c0a-002564c97630")
    public void setHorizontalLayout() {
        this.shform.setOrientation(SWT.HORIZONTAL);
    }

    /**
     * Set the layout to vertical.
     */
    @objid ("8fa7c885-c068-11e1-8c0a-002564c97630")
    public void setVerticalLayout() {
        this.shform.setOrientation(SWT.VERTICAL);
    }

    /**
     * Enable automatic horizontal/vertical layout change when resizing the view.
     */
    @objid ("8fa7c888-c068-11e1-8c0a-002564c97630")
    public void enableAutoLayout() {
        this.theViewParent.addControlListener(this.layoutChangeListener);
    }

    /**
     * Disable automatic horizontal/vertical layout change when resizing the view.
     */
    @objid ("8fa7c88b-c068-11e1-8c0a-002564c97630")
    public void disableAutoLayout() {
        this.theViewParent.removeControlListener(this.layoutChangeListener);
    }

    /**
     * Get the currently displayed element.
     * @return the currently displayed element.
     */
    @objid ("8faa29a9-c068-11e1-8c0a-002564c97630")
    protected Element getCurrentTypeItem() {
        return this.contentPanel.getCurrentInput();
    }

    /**
     * Set the given tagged values set (from Stereotype or Module) in the data panel.
     * @param typeItem : the type item whose contents is to be displayed in the content panel. May be null
     */
    @objid ("8faa29b0-c068-11e1-8c0a-002564c97630")
    protected void setCurrentTypeItem(final Object typeItem) {
        DataPanelInput newInput = createDataInput();
        newInput.setTypingElement(typeItem);
        this.contentPanel.setInput(newInput);
        
        if (typeItem != null) {
            this.treePanel.setLastType(typeItem);
        }
    }

    /**
     * Get the selected elements in the left tree.
     * @return the selected elements.
     */
    @objid ("8faa29b8-c068-11e1-8c0a-002564c97630")
    @SuppressWarnings("unchecked")
    public List<ModelElement> getSelectedTypeItems() {
        ISelection selection = this.treePanel.getTreeViewer().getSelection();
        if (selection instanceof IStructuredSelection) {
            return ((IStructuredSelection) selection).toList();
        }
        return Collections.emptyList();
    }

    /**
     * Refresh the view from the model.
     */
    @objid ("8faa29b5-c068-11e1-8c0a-002564c97630")
    void refreshView() {
        final Tree tree = this.treePanel.getTreeViewer().getTree();
        if (tree == null || tree.isDisposed()) {
            // No graphic control, nothing to update, return.
            return;
        }
        
        if (this.currentElement != null && this.currentElement.isValid()) {
            TreePanelInput newInput = createTreeInput(false);
            DataPanelInput dataInput = createDataInput();
        
            final Element currTypeItem = getCurrentTypeItem();
            if (currTypeItem != null && 
                    ((SmObjectImpl) currTypeItem).isValid() && 
                    currTypeItem!=this.currentElement) {
                newInput.setPreselectedTypingElement(currTypeItem);
            } else {
                newInput.setPreselectedTypingElement(null);
            }
        
            this.treePanel.setInput(newInput);
        
            // Update content panel
            Object curSel = ((IStructuredSelection)this.treePanel.getTreeViewer().getSelection()).getFirstElement();
            setCurrentTypeItem(curSel);
        } else {
            this.currentElement = null;
            TreePanelInput newInput = createTreeInput(true);
            
            this.treePanel.setInput(newInput);
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @objid ("8faa2986-c068-11e1-8c0a-002564c97630")
    private void setFocus() {
        this.treePanel.setFocus();
    }

    /**
     * Pin or unpin the view.
     * <p>
     * A pinned view doesn't update on selection changes or navigation events.
     * @param pinned <code>true</code> if the view must be pinned, else <code>false</code>.
     */
    @objid ("06b5b35c-16d1-11e2-aa0d-002564c97630")
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    /**
     * Makes this view editable. <code>modelingSession</code> and <code>modelService</code> are mandatory otherwise edition cannot
     * be supported. <code>pickingService</code> is optional.
     * 
     * To deactivate edition, call <code>activateEdition(null, null, null)</code>
     * @param newProjectService
     * @param newModelingSession the current edited modeling session.
     * @param newModelService the model service.
     * @param newPickingService the picking service.
     * @param newActivationService the activation service, to open rich notes and diagram editors.
     */
    @objid ("008fed1e-1e1f-107d-a016-001ec947cd2a")
    public void activateEdition(IProjectService newProjectService, ICoreSession newModelingSession, IMModelServices newModelService, IModelioPickingService newPickingService, IActivationService newActivationService) {
        assert newActivationService != null;
        assert newModelService != null;
        
        this.projectService = newProjectService;
        this.modelService = newModelService;
        this.pickingService = newPickingService;
        this.activationService = newActivationService;
        
        if (newModelingSession != null) {            
            if (this.modelingSession != null && this.modelChangeListener != null) {
                final IModelChangeSupport modelChangeSupport = this.modelingSession.getModelChangeSupport();
                if (modelChangeSupport != null) {
                    modelChangeSupport.removeModelChangeListener(this.modelChangeListener);
                }
            }
        
            this.modelingSession = newModelingSession;
            this.modelChangeListener = new ModelChangeListener(this);
            this.modelingSession.getModelChangeSupport().addModelChangeListener(this.modelChangeListener);
        } else {
            if (this.modelingSession != null) {
                if (this.modelChangeListener != null)
                    this.modelingSession.getModelChangeSupport().removeModelChangeListener(this.modelChangeListener);
                this.modelChangeListener = null;
                this.modelingSession = null;
            }
        }
    }

    @objid ("ee546419-85a4-418b-b413-d9e547a98fc3")
    public TreeViewer getTreeViewer() {
        return this.treePanel.getTreeViewer();
    }

    @objid ("9ef67ffd-5bcc-40cb-8660-05b906aa38c0")
    private DataPanelInput createDataInput() {
        Object typeItem = null;
        DataPanelInput ret = new DataPanelInput(this.projectService, this.modelService, this.pickingService, this.activationService, this.currentElement, typeItem, this.showHiddenAnnotations);
        return ret;
    }

    @objid ("2939c8ff-ca73-4f92-b190-2148018f477f")
    private TreePanelInput createTreeInput(boolean empty) {
        TreePanelInput ret;
        if (empty) {
            ret = new TreePanelInput(this.modelService, null, null, this.showHiddenAnnotations);
        } else {
            ret = new TreePanelInput(this.modelService, this.currentElement, getCurrentTypeItem(), this.showHiddenAnnotations);
        }
        return ret;
    }

    /**
     * Set whether hidden annotations are show or not.
     * @param show <i>true</i> to show, <i>false</i> to hide hidden annotations.
     */
    @objid ("05a9c9f5-2053-4e1f-8584-f4cee26ee2aa")
    public void setShowHiddenMdaElements(boolean show) {
        this.showHiddenAnnotations = show;
        
        refreshView();
    }

    @objid ("52fca2df-4746-4e85-81b9-f7f07ce6c4ab")
    @Override
    public boolean isRelevantFor(Object obj) {
        return true;
    }

    @objid ("b2bf327c-5bad-449b-a88c-9b97912a9000")
    @Override
    public String getHelpTopic() {
        return null;
    }

    @objid ("8faa29c7-c068-11e1-8c0a-002564c97630")
    private class LayoutChangeListener implements ControlListener {
        @objid ("8faa29d0-c068-11e1-8c0a-002564c97630")
        protected LayoutChangeListener() {
            super();
        }

        @objid ("8faa29c8-c068-11e1-8c0a-002564c97630")
        @Override
        public void controlResized(ControlEvent theEvent) {
            changeLayout(theEvent);
        }

        @objid ("8faa29cc-c068-11e1-8c0a-002564c97630")
        @Override
        public void controlMoved(ControlEvent theEvent) {
            // Nothing to do
        }

        @objid ("8faa29d2-c068-11e1-8c0a-002564c97630")
        private void changeLayout(ControlEvent theEvent) {
            // Minimum and maxim H/W ratio
            float hwmin = (float) 0.6;
            float hwmax = (float) 0.65;
            
            Composite comp = (Composite) theEvent.widget;
            comp.layout();
            
            float ratio = (float) comp.getSize().y / (float) comp.getSize().x;
            
            if (ratio < hwmin) {
                setHorizontalLayout();
            } else if (ratio > hwmax) {
                setVerticalLayout();
            }
        }

    }

    /**
     * This listener is the Tree selection listener of the note view.
     * Its responsibility is to update the data panel contents when a
     * particular note is selected in the tree.
     * 
     * @author phv
     */
    @objid ("8faa29d5-c068-11e1-8c0a-002564c97630")
    private static class SelectionChangedListener implements ISelectionChangedListener {
        @objid ("8fac8ae7-c068-11e1-8c0a-002564c97630")
        private final ModelPropertyPanelProvider view;

        @objid ("8fac8aed-c068-11e1-8c0a-002564c97630")
        protected SelectionChangedListener(final ModelPropertyPanelProvider view) {
            this.view = view;
        }

        @objid ("8fac8ae8-c068-11e1-8c0a-002564c97630")
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            if (this.view.isPinned()) {
                return;
            }
            
            // This method listen to the selection changes in the notes tree
            // viewer.
            ISelection currentSelection = event.getSelection();
            
            if (currentSelection instanceof IStructuredSelection) {
                IStructuredSelection structuredSelection = (IStructuredSelection) currentSelection;
                Object object = structuredSelection.getFirstElement();
                this.view.setCurrentTypeItem(object);
            }
        }

    }

    /**
     * This listener is called when the model is modified.<br>
     * Its responsibility is to refresh the PropertyView for the current element.
     */
    @objid ("031c28ce-4e43-4c50-ae05-1a9c6449d3fd")
    private static class ModelChangeListener implements IModelChangeListener, IStatusChangeListener {
        @objid ("40a40e48-5737-414b-aef8-f7d297d4b41b")
        protected ModelPropertyPanelProvider propertyView;

        @objid ("2f218374-4aa8-4748-b76f-0e9f13e897ee")
        public ModelChangeListener(final ModelPropertyPanelProvider propertyView) {
            this.propertyView = propertyView;
        }

        @objid ("b5fd3159-1c42-4c8a-a02e-55378deead34")
        @Override
        public void modelChanged(final IModelChangeEvent event) {
            // Re enter the UI thread
            Display display = Display.getDefault();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ModelChangeListener.this.propertyView.refreshView();
                    }
                });
            }
        }

        @objid ("ddc4f6a7-f459-4b25-ae91-fd35a00eed3b")
        @Override
        public void statusChanged(final IStatusChangeEvent event) {
            // Re enter the UI thread
            Display display = Display.getDefault();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ModelChangeListener.this.propertyView.refreshView();
                    }
                });
            }
        }

    }

}
