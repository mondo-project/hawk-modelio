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
                                    

package org.modelio.edition.notes.view;

import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.ui.dnd.ModelElementTransfer;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.core.ui.dnd.MObjectViewerDragProvider;
import org.modelio.edition.notes.view.data.NoteViewContentPanel;
import org.modelio.edition.notes.view.tree.NoteViewTreePanel;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.ui.panel.IPanelProvider;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IStatusChangeEvent;
import org.modelio.vcore.session.api.model.change.IStatusChangeListener;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * The NotesView manages two GUI panels:
 * <ul>
 * <li>the tree panel that displays the list of the notes, constraints and extern documents that are available on the selected model
 * element</li>
 * <li>the content panel that displays the contents of the 'noteItem' selected in the tree panel.</li>
 * </ul>
 * The NotesView aggregates the two panel and controls all their behaviors so that the two panels do not have to know anything about
 * each other, it also provides services to the view toolbar handlers so that these handlers so that they do not have to deal with
 * the two panels organisation.
 * 
 * The model element whose note items are currently listed is called the currentElement. The particular note, constraint or extern
 * document whose contents is currently shown is called the current note item.
 */
@objid ("fb9e8e75-19e5-11e2-ad19-002564c97630")
public class NotesPanelProvider implements IPanelProvider {
    /**
     * The Notes view ID.
     */
    @objid ("fd60139f-19e5-11e2-ad19-002564c97630")
    public static final String VIEW_ID = "org.modelio.edition.notes.NotesViewID";

    /**
     * Minimum and maxim H/W ratio
     */
    @objid ("9d53e993-50fa-4228-908b-48fe2bd0a90a")
    private static final float HWMIN = (float) 0.6;

    @objid ("db85af4f-c0d5-47ea-8a50-a020d2afaab0")
    private static final float HWMAX = (float) 0.65;

    @objid ("fb9e8e77-19e5-11e2-ad19-002564c97630")
    private NoteViewTreePanel treePanel = null;

    @objid ("fb9e8e78-19e5-11e2-ad19-002564c97630")
    private NoteViewContentPanel contentPanel = null;

    @objid ("fb9e8e79-19e5-11e2-ad19-002564c97630")
    private LayoutChangeListener layoutChangeListener = null;

    @objid ("fb9e8e7a-19e5-11e2-ad19-002564c97630")
    protected ModelElement currentElement;

    @objid ("fb9e8e7b-19e5-11e2-ad19-002564c97630")
    private ModelChangeListener modelChangeListener;

    @objid ("fb9e8e7c-19e5-11e2-ad19-002564c97630")
    private SelectionChangedListener treeSelectionListener;

    @objid ("fb9e8e7d-19e5-11e2-ad19-002564c97630")
    private SashForm shform = null;

    @objid ("fb9e8e7e-19e5-11e2-ad19-002564c97630")
    protected Composite parentComposite = null;

    @objid ("fba01514-19e5-11e2-ad19-002564c97630")
    private ICoreSession session;

    @objid ("74b1af15-7c9a-4662-a17b-6dd9c7ca4a58")
    private IActivationService activationService;

    @objid ("0bb58649-7d38-4e6d-93e7-450e68c08440")
    private DragSourceListener dragListener;

    /**
     * Constructor.
     */
    @objid ("fba01518-19e5-11e2-ad19-002564c97630")
    public NotesPanelProvider(IActivationService activationService) {
        this.activationService = activationService;
    }

    @objid ("fba0151b-19e5-11e2-ad19-002564c97630")
    @Override
    public SashForm getPanel() {
        return this.shform;
    }

    /**
     * Set the layout to horizontal.
     */
    @objid ("fba01520-19e5-11e2-ad19-002564c97630")
    public void setHorizontalLayout() {
        this.shform.setOrientation(SWT.HORIZONTAL);
    }

    /**
     * Set the layout to vertical.
     */
    @objid ("fba01523-19e5-11e2-ad19-002564c97630")
    public void setVerticalLayout() {
        this.shform.setOrientation(SWT.VERTICAL);
    }

    /**
     * Enable automatic horizontal/vertical layout change when resizing the view.
     */
    @objid ("fba01526-19e5-11e2-ad19-002564c97630")
    public void enableAutoLayout() {
        this.parentComposite.addControlListener(this.layoutChangeListener);
        layoutView(this.parentComposite, this);
    }

    /**
     * Disable automatic horizontal/vertical layout change when resizing the view.
     */
    @objid ("fba01529-19e5-11e2-ad19-002564c97630")
    public void disableAutoLayout() {
        this.parentComposite.removeControlListener(this.layoutChangeListener);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @objid ("fba0152c-19e5-11e2-ad19-002564c97630")
    @Focus
    void setFocus() {
        if (this.treePanel != null) {
            this.treePanel.setFocus();
        }
    }

    @objid ("fba01530-19e5-11e2-ad19-002564c97630")
    @Override
    public SashForm createPanel(Composite parent) {
        this.parentComposite = parent;
        
        this.shform = new SashForm(parent, SWT.HORIZONTAL);
        this.shform.setLayout(new FillLayout());
        
        this.treePanel = new NoteViewTreePanel(this.shform, SWT.NONE, this, this.session);
        this.contentPanel = new NoteViewContentPanel(this.shform, SWT.NONE, this.activationService);
        
        this.treePanel.setLayout(new FillLayout());
        this.contentPanel.setLayout(new FillLayout());
        
        this.layoutChangeListener = new LayoutChangeListener(this);
        enableAutoLayout();
        
        this.shform.setWeights(new int[] { 30, 70 });
        
        layoutView(this.parentComposite, this);
        
        this.treeSelectionListener = new SelectionChangedListener(this);
        getTreeViewer().addSelectionChangedListener(this.treeSelectionListener);
        
        getTreeViewer().addDoubleClickListener(new DoubleClickListener());
        
        
        if (this.dragListener == null) {
            this.dragListener = new MObjectViewerDragProvider(getTreeViewer());
            getTreeViewer().addDragSupport(DND.DROP_MOVE | DND.DROP_COPY, new Transfer[] { ModelElementTransfer.getInstance() }, this.dragListener);
        }
        return this.shform;
    }

    /**
     * Makes this view editable. <code>modelingSession</code> and <code>modelService</code> are mandatory otherwise edition cannot
     * be supported.
     * 
     * To deactivate edition, call <code>activateEdition(null, null, null)</code>
     * @param newModelingSession the current edited modeling session.
     */
    @objid ("fba01536-19e5-11e2-ad19-002564c97630")
    public void activateEdition(ICoreSession newModelingSession) {
        if (newModelingSession != null) {
            this.session = newModelingSession;
        
            this.modelChangeListener = new ModelChangeListener(this);
            this.session.getModelChangeSupport().addModelChangeListener(this.modelChangeListener);
            this.session.getModelChangeSupport().addStatusChangeListener(this.modelChangeListener);
        
            this.contentPanel.start(this.session);
        } else {
            if (this.session != null) {
                // remove listeners
                if (this.session.getModelChangeSupport() != null) {                    
                    this.session.getModelChangeSupport().removeModelChangeListener(this.modelChangeListener);
                    this.session.getModelChangeSupport().removeStatusChangeListener(this.modelChangeListener);
                }
                this.modelChangeListener = null;
        
                this.session = null;
            }
        
            if (this.shform != null) {
                setInput(null);
        
                this.contentPanel.stop();
            }
        }
    }

    /**
     * Get the annotation view tree viewer.
     * @return the tree viewer.
     */
    @objid ("fba0153a-19e5-11e2-ad19-002564c97630")
    public TreeViewer getTreeViewer() {
        return this.treePanel.getTreeViewer();
    }

    /**
     * @return the control that has keyboard focus.
     */
    @objid ("fba0153f-19e5-11e2-ad19-002564c97630")
    public Control getFocusControl() {
        return this.parentComposite.getDisplay().getFocusControl();
    }

    /**
     * Get the current element displayed by the view.
     * @return the model element whose notes are currently listed in the tree panel. May be null.
     */
    @objid ("fba01544-19e5-11e2-ad19-002564c97630")
    @Override
    public ModelElement getInput() {
        return this.currentElement;
    }

    /**
     * Set the current element displayed by the view.
     * @param input the model element whose note are to be listed in the tree panel. May be null.
     */
    @objid ("fba0154a-19e5-11e2-ad19-002564c97630")
    @Override
    public void setInput(final Object input) {
        if (!(input instanceof Element)) {
            if (this.currentElement != null && this.currentElement.isValid()) {
                this.treePanel.setInput(this.currentElement);
            } else {
                this.currentElement = null;
                this.treePanel.setInput(null);
            }
            return;
        }
        
        Element selectedElement = (Element) input;
        if (selectedElement instanceof Note || selectedElement instanceof Constraint || selectedElement instanceof ExternDocument) {
            this.currentElement = (ModelElement) selectedElement.getCompositionOwner();
            this.treePanel.setInput(this.currentElement, (ModelElement) selectedElement);
        } else if (selectedElement instanceof ModelElement) {
            this.currentElement = (ModelElement) selectedElement;
            this.treePanel.setInput(this.currentElement);
        } else {
            this.currentElement = null;
            this.treePanel.setInput(null);
        }
    }

    /**
     * Set the given object (Note, Constraint or ExternDocument) in the data panel.
     * @param noteItem : the note item whose contents is to be displayed in the content panel. May be null
     */
    @objid ("fba19bb5-19e5-11e2-ad19-002564c97630")
    public void setCurrentNoteItem(final ModelElement noteItem) {
        this.treePanel.setLastType(noteItem);
        this.contentPanel.setInput(noteItem);
    }

    /**
     * Refresh the whole notes view.
     */
    @objid ("fba19bba-19e5-11e2-ad19-002564c97630")
    public void refreshView() {
        if (this.currentElement != null && !this.currentElement.isDeleted()) {
            ModelElement currentNoteItem = getCurrentNoteItem();
            if (currentNoteItem != null && !currentNoteItem.isDeleted()) {
                this.treePanel.setInput(this.currentElement, currentNoteItem);
            } else {
                this.treePanel.setInput(this.currentElement, null);
            }
        } else {
            this.currentElement = null;
            this.treePanel.setInput(null);
        }
    }

    /**
     * Clean up the content of the currently selected note
     */
    @objid ("fba19bbd-19e5-11e2-ad19-002564c97630")
    public void cleanCurrentNoteItemContent() {
        ModelElement noteItem = getCurrentNoteItem();
        
        if (noteItem == null) {
            return;
        }
        
        try (ITransaction transaction = this.session.getTransactionSupport().createTransaction("Clear note content")) {
            if (noteItem instanceof Note) {
                ((Note) noteItem).setContent("");
            } else if (noteItem instanceof Constraint) {
                ((Constraint) noteItem).setBody("");
            } else if (noteItem instanceof ExternDocument) {
                ((ExternDocument) noteItem).setAbstract("");
            }
            transaction.commit();
            this.contentPanel.cleanContent();
        }
    }

    /**
     * Set the note item object (Note, Constraint or ExternDocument) currently displayed in the content panel.
     * @return the note item whose contents is to be displayed in the content panel. May be null.
     */
    @objid ("fba19bc0-19e5-11e2-ad19-002564c97630")
    public ModelElement getCurrentNoteItem() {
        return this.contentPanel.getCurrentInput();
    }

    /**
     * Get the elements selected in the tree viewer.
     * @return the selected model elements.
     */
    @objid ("fba19bc5-19e5-11e2-ad19-002564c97630")
    @SuppressWarnings("unchecked")
    public List<ModelElement> getSelectedNoteItems() {
        ISelection selection = getTreeViewer().getSelection();
        if (selection instanceof IStructuredSelection) {
            return ((IStructuredSelection) selection).toList();
        }
        return Collections.emptyList();
    }

    /**
     * This listener is activated when the selection changes in the workbench.<br>
     * Its responsibility is to set the NotesView's current element.
     * @param selection the current modelio selection.
     */
    @objid ("fba19bcd-19e5-11e2-ad19-002564c97630")
    @Optional
    @Inject
    public void update(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        // This method listen to the selection changes in the workbench.
        if (selection != null && this.shform != null) {
            final List<?> selectedElements = selection.toList();
            if (selectedElements.size() == 1 && selectedElements.get(0) instanceof Element) {
                final Element selectedElement = (Element) selectedElements.get(0);
        
                if (selectedElement != null && !selectedElement.isDeleted() && selectedElement instanceof ModelElement) {
                    setInput(selectedElement);
                } else {
                    setInput(null);
                }
            }
        }
    }

    /**
     * This listener is activated when an application navigation event is fired.<br>
     * Its responsibility is to set the NotesView's current element.
     * @param target the element to select.
     */
    @objid ("fba19bd4-19e5-11e2-ad19-002564c97630")
    @Optional
    @Inject
    public void navigateTo(@EventTopic(ModelioEventTopics.NAVIGATE_ELEMENT) final Element target) {
        if (this.contentPanel.isDisposed()) {
            return;
        }
        if (target instanceof Note || target instanceof Constraint || target instanceof ExternDocument) {
            setInput(target);
        }
    }

    @objid ("f4f01196-d2b7-42cd-bead-8fc4720406b8")
    protected static void layoutView(Composite comp, NotesPanelProvider view) {
        float ratio = (float) comp.getSize().y / (float) comp.getSize().x;
        
        if (ratio < HWMIN) {
            view.setHorizontalLayout();
        } else if (ratio > HWMAX) {
            view.setVerticalLayout();
        }
    }

    @objid ("0f13108b-97c0-4e3d-a011-69c39368c8df")
    @Override
    public boolean isRelevantFor(Object obj) {
        return true;
    }

    @objid ("0f1e96be-32fd-4cf5-94d6-8cfc5d7b1a77")
    @Override
    public String getHelpTopic() {
        return null;
    }

    @objid ("fba19bd9-19e5-11e2-ad19-002564c97630")
    private static class LayoutChangeListener implements ControlListener {
        @objid ("fba19be0-19e5-11e2-ad19-002564c97630")
        private final NotesPanelProvider view;

        @objid ("fba19be1-19e5-11e2-ad19-002564c97630")
        @Override
        public void controlResized(ControlEvent theEvent) {
            changeLayout(theEvent);
        }

        @objid ("fba32252-19e5-11e2-ad19-002564c97630")
        @Override
        public void controlMoved(ControlEvent theEvent) {
            // Nothing to do
        }

        @objid ("fba32256-19e5-11e2-ad19-002564c97630")
        public LayoutChangeListener(final NotesPanelProvider view) {
            super();
            this.view = view;
        }

        @objid ("fba3225a-19e5-11e2-ad19-002564c97630")
        private void changeLayout(ControlEvent theEvent) {
            Composite comp = (Composite) theEvent.widget;
            comp.layout();
            
            layoutView(comp, this.view);
        }

    }

    /**
     * This listener is the Tree selection listener of the note view. Its responsibility is to update the data panel contents when a
     * particular note is selected in the tree.
     */
    @objid ("fba3225d-19e5-11e2-ad19-002564c97630")
    private static class SelectionChangedListener implements ISelectionChangedListener {
        @objid ("fba32260-19e5-11e2-ad19-002564c97630")
        private final NotesPanelProvider notesView;

        @objid ("fba32261-19e5-11e2-ad19-002564c97630")
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            // This method listen to the selection changes in the notes tree viewer.
            ISelection currentSelection = event.getSelection();
            
            if (currentSelection instanceof IStructuredSelection) {
                IStructuredSelection structuredSelection = (IStructuredSelection) currentSelection;
                Object object = structuredSelection.getFirstElement();
                if (object != null) {
                    if (object instanceof ModelElement) {
                        this.notesView.setCurrentNoteItem((ModelElement) object);
                    }
                } else {
                    this.notesView.setCurrentNoteItem(null);
                }
            }
        }

        @objid ("fba32266-19e5-11e2-ad19-002564c97630")
        public SelectionChangedListener(final NotesPanelProvider notesView) {
            this.notesView = notesView;
        }

    }

    /**
     * This listener is called when the model is modified.<br>
     * Its responsibility is to refresh the NotesView for the current element.
     */
    @objid ("fba3226a-19e5-11e2-ad19-002564c97630")
    private static class ModelChangeListener implements IModelChangeListener, IStatusChangeListener {
        @objid ("fba3226d-19e5-11e2-ad19-002564c97630")
        protected NotesPanelProvider notesView;

        @objid ("fba3226e-19e5-11e2-ad19-002564c97630")
        public ModelChangeListener(final NotesPanelProvider notesView) {
            this.notesView = notesView;
        }

        @objid ("fba32272-19e5-11e2-ad19-002564c97630")
        @Override
        public void modelChanged(final IModelChangeEvent event) {
            // Re enter the UI thread
            Display display = Display.getDefault();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ModelChangeListener.this.notesView.refreshView();
                    }
                });
            }
        }

        @objid ("fba32277-19e5-11e2-ad19-002564c97630")
        @Override
        public void statusChanged(final IStatusChangeEvent event) {
            // Re enter the UI thread
            Display display = Display.getDefault();
            if (display != null) {
                display.asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ModelChangeListener.this.notesView.refreshView();
                    }
                });
            }
        }

    }

    /**
     * Listen for double clicks on the tree viewer and "activates" the double clicked element.
     */
    @objid ("fba3227c-19e5-11e2-ad19-002564c97630")
    private class DoubleClickListener implements IDoubleClickListener {
        @objid ("fba3227f-19e5-11e2-ad19-002564c97630")
        public DoubleClickListener() {
        }

        @objid ("fba32281-19e5-11e2-ad19-002564c97630")
        @Override
        public void doubleClick(final DoubleClickEvent event) {
            IStructuredSelection sel = (IStructuredSelection) event.getSelection();
            Element el = (Element) sel.getFirstElement();
            // fire activation
            NotesPanelProvider.this.activationService.activateMObject(el);
        }

    }

}
