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

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * The NotesView manages two GUI panels:
 * <ul>
 * <li>the tree panel that displays the list of the notes, constraints and extern documents that are available on the
 * selected model element</li>
 * <li>the content panel that displays the contents of the 'noteItem' selected in the tree panel.</li>
 * </ul>
 * The NotesView aggregates the two panel and controls all their behaviors so that the two panels do not have to know
 * anything about each other, it also provides services to the view toolbar handlers so that these handlers so that they
 * do not have to deal with the two panels organisation.
 * 
 * The model element whose note items are currently listed is called the currentElement. The particular note, constraint
 * or extern document whose contents is currently shown is called the current note item.
 */
@objid ("fba32286-19e5-11e2-ad19-002564c97630")
public class NotesView {
    /**
     * The Notes view ID.
     */
    @objid ("fd9be3ee-19e5-11e2-ad19-002564c97630")
    public static final String VIEW_ID = "org.modelio.edition.notes.notesview";

    @objid ("7d267903-b1ae-40d9-aa0b-3f4ab8b28e55")
    private static final String POPUPID = "org.modelio.edition.notes.popupmenu";

    @objid ("fba4a8f5-19e5-11e2-ad19-002564c97630")
     GProject project;

    @objid ("fba4a8f6-19e5-11e2-ad19-002564c97630")
     NotesPanelProvider notesPanel;

    @objid ("fba4a8f7-19e5-11e2-ad19-002564c97630")
     SelectionChangedListener treeSelectionListener;

    @objid ("853c3e08-31db-4710-a76d-e9b7959fa878")
    protected Composite parentComposite = null;

    @objid ("da0ded00-6c89-4015-977a-5ba836c83ca2")
     EMenuService menuService;

    /**
     * Use a static context to avoid initialization issues...
     */
    @objid ("0b17cd92-e804-4bde-ba91-cc75c88a7e14")
    @Optional
    @Inject
    public static EContextService contextService;

    /**
     * Constructor.
     */
    @objid ("fba4a8fb-19e5-11e2-ad19-002564c97630")
    public NotesView() {
    }

    /**
     * Get the notes panel.
     * @return the current notes panel.
     */
    @objid ("fba4a8fe-19e5-11e2-ad19-002564c97630")
    public NotesPanelProvider getNotesPanel() {
        return this.notesPanel;
    }

    /**
     * Called by the framework to create the view and initialize it.
     * @param projectService the project service.
     * @param parent the composite the view must add its content into.
     * @param selection the application selection.
     * @param theMenuService Eclipse menu service
     * @param activationService Modelio navigation service
     */
    @objid ("fba4a903-19e5-11e2-ad19-002564c97630")
    @PostConstruct
    public void createControls(IProjectService projectService, Composite parent, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, @Optional EMenuService theMenuService, @Optional IActivationService activationService) {
        this.parentComposite = parent;
        
        // Sometimes, the view is instantiated only after the project is opened
        if (projectService != null && projectService.getOpenedProject() != null) {
            onProjectOpened(projectService.getOpenedProject(), theMenuService, activationService, selection);
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @objid ("fba4a909-19e5-11e2-ad19-002564c97630")
    @Focus
    void setFocus() {
        if (this.notesPanel != null) {
            this.notesPanel.setFocus();
        }
    }

    /**
     * This method is called after a project opening.
     * Keep a reference to the project.
     */
    @objid ("fba4a90d-19e5-11e2-ad19-002564c97630")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject openedProject, @Optional EMenuService theMenuService, @Optional final IActivationService activationService, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        if (openedProject == null) {
            return;
        }
        
        assert (activationService != null);
        
        this.project = openedProject;
        this.menuService = theMenuService;
        
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (NotesView.this.notesPanel == null) {
                    // Create the view content
                    NotesView.this.notesPanel = new NotesPanelProvider(activationService);
                    NotesView.this.notesPanel.createPanel(NotesView.this.parentComposite);
        
                    NotesView.this.treeSelectionListener = new SelectionChangedListener(NotesView.this.notesPanel);
                    NotesView.this.notesPanel.getTreeViewer().addSelectionChangedListener(NotesView.this.treeSelectionListener);
        
                    MPopupMenu popupMenu = NotesView.this.menuService.registerContextMenu(NotesView.this.getNotesPanel().getTreeViewer().getTree(), POPUPID);
                    MMenu menu = MMenuFactory.INSTANCE.createMenu();
                    popupMenu.getChildren().add(menu);
                }
                NotesView.this.notesPanel.activateEdition(NotesView.this.project.getSession());
        
                if (selection != null) 
                    update(selection);
        
                NotesView.this.notesPanel.parentComposite.layout(true,true);
            }
        });
    }

    /**
     * Called when a project is closed.
     * On session close un-reference the project.
     */
    @objid ("fba4a914-19e5-11e2-ad19-002564c97630")
    @Inject
    @SuppressWarnings("unused")
    @Optional
    void onProjectClosing(@EventTopic(ModelioEventTopics.PROJECT_CLOSING) final GProject closedProject) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (NotesView.this.notesPanel != null) {
                    NotesView.this.notesPanel.activateEdition(null);
                }
            }
        });
    }

    /**
     * @return the control that has keyboard focus.
     */
    @objid ("fba4a91b-19e5-11e2-ad19-002564c97630")
    public Control getFocusControl() {
        return this.parentComposite.getDisplay().getFocusControl();
    }

    /**
     * This listener is activated when the selection changes in the workbench.<br>
     * Its responsibility is to set the NotesView's current element.
     * @param selection the current modelio selection.
     */
    @objid ("fba4a920-19e5-11e2-ad19-002564c97630")
    @Optional
    @Inject
    public void update(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        // This method listen to the selection changes in the workbench.
        if (selection != null && this.notesPanel != null) {            
            List<MObject> selectedElements = new ArrayList<>();
            for (Object object : selection.toList()) {
                if (object instanceof MObject) {
                    selectedElements.add((MObject) object);
                } else if (object instanceof IAdaptable) {
                    final MObject adapter = (MObject) ((IAdaptable) object).getAdapter(MObject.class);
                    if (adapter != null) {
                        selectedElements.add(adapter);
                    }
                } 
            }
        
            if (selectedElements.size() > 0) {
                this.notesPanel.setInput(selectedElements.get(0));
            }
        }
    }

    /**
     * This listener is the Tree selection listener of the note view. Its responsibility is to update the data panel
     * contents when a particular note is selected in the tree.
     */
    @objid ("fba62f91-19e5-11e2-ad19-002564c97630")
    private static class SelectionChangedListener implements ISelectionChangedListener {
        @objid ("fba62f94-19e5-11e2-ad19-002564c97630")
        private NotesPanelProvider notesPanel;

        @objid ("fba62f95-19e5-11e2-ad19-002564c97630")
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            // This method listen to the selection changes in the notes tree viewer.
            ISelection currentSelection = event.getSelection();
            
            if (currentSelection instanceof IStructuredSelection) {
                IStructuredSelection structuredSelection = (IStructuredSelection) currentSelection;
                Object object = structuredSelection.getFirstElement();
                if (object != null) {
                    if (object instanceof ModelElement) {
                        this.notesPanel.setInput(object);
                    }
                } else {
                    this.notesPanel.setInput(null);
                }
            }
        }

        @objid ("fba62f9a-19e5-11e2-ad19-002564c97630")
        public SelectionChangedListener(final NotesPanelProvider notesPanel) {
            this.notesPanel = notesPanel;
        }

    }

}
