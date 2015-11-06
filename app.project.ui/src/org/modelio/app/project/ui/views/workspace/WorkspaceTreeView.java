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
                                    

package org.modelio.app.project.ui.views.workspace;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.app.project.ui.closeproject.CloseProjectHandler;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.gproject.GProjectFactory;
import org.modelio.ui.progress.IModelioProgressService;

/**
 * Workspace tree viewer
 */
@objid ("00061b02-a110-1fbc-82db-001ec947cd2a")
public class WorkspaceTreeView {
    /**
     * The popup menu ID as specified in the .e4xmi file.
     */
    @objid ("0074b008-8b74-1fe1-bf4c-001ec947cd2a")
    private static final String POPUP_MENU_ID = "org.modelio.app.workspace.popupmenu";

    /**
     * The ID of the view as specified by the extension.
     */
    @objid ("0045c360-ef9d-1fc5-854f-001ec947cd2a")
    private static final String VIEW_ID = "org.modelio.model.workspace.views.BrowserView";

    @objid ("0071a8d6-ef91-1fc5-854f-001ec947cd2a")
    @Inject
    @Optional
    private ESelectionService selectionService;

    @objid ("00363c10-b10f-1fc8-b42e-001ec947cd2a")
    @Inject
    @Optional
    private EMenuService service;

    @objid ("0014b28e-44b4-1060-84ef-001ec947cd2a")
    @Inject
    private ECommandService commandService;

    @objid ("0014cf8a-44b4-1060-84ef-001ec947cd2a")
    @Inject
    private EHandlerService handlerService;

    @objid ("2052c570-3712-4654-ba76-6a7f60b4c2d9")
    private static final String OPENPROJECT_COMMAND_ID = "org.modelio.app.ui.command.openproject";

    @objid ("00961e64-e525-1fc8-b42e-001ec947cd2a")
    private Path workspacePath;

    @objid ("00370a32-f974-1fc8-b42e-001ec947cd2a")
    private final ProjectCache cache = new ProjectCache();

    @objid ("00318792-6a48-1fcf-b5e2-001ec947cd2a")
    @Inject
     IProjectService projectService;

    @objid ("27ea110d-2497-4128-8180-ed05a4e5406c")
    private TreeViewer viewer;

    /**
     * Constructor
     */
    @objid ("00068da8-a110-1fbc-82db-001ec947cd2a")
    public WorkspaceTreeView() {
    }

    @objid ("00068e3e-a110-1fbc-82db-001ec947cd2a")
    @PostConstruct
    void createControls(final Composite parent) {
        this.viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.viewer.setContentProvider(new WksContentProvider());
        this.viewer.setLabelProvider(new WksLabelProvider(this.projectService, this.viewer.getTree().getFont()));
        this.viewer.setSorter(new WksNameSorter());
        this.viewer.setUseHashlookup(true);
        
        ColumnViewerToolTipSupport.enableFor(this.viewer);
        
        this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (WorkspaceTreeView.this.selectionService != null) {
                    WorkspaceTreeView.this.selectionService.setSelection((event.getSelection()));
                }
            }
        });
        
        this.viewer.addDoubleClickListener(new IDoubleClickListener() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void doubleClick(DoubleClickEvent event) {
                ParameterizedCommand openCommand = WorkspaceTreeView.this.commandService.createCommand(OPENPROJECT_COMMAND_ID,
                        new HashMap<String, Object>());
                WorkspaceTreeView.this.handlerService.executeHandler(openCommand);
            }
        
        });
        
        Path path = this.projectService.getWorkspace();
        setWorkspacePath(path);
        
        this.service.registerContextMenu(this.viewer.getTree(), POPUP_MENU_ID);
    }

    /**
     * Modify the workspace path.
     * @param path the workspace path.
     */
    @objid ("0036a074-b10f-1fc8-b42e-001ec947cd2a")
    public void setWorkspacePath(final Path path) {
        AppProjectUi.LOG.info("Changing workspace to: %s", path.toString());
        this.workspacePath = path;
        File file = path.toFile();
        if (file != null && file.exists() && file.isDirectory()) {
            this.cache.load(path);
        
            final ProjectCache aCache = this.cache;
            this.viewer.getTree().getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    getViewer().setInput(aCache);
                }
            });
        
        } else {
            AppProjectUi.LOG.error("Invalid workspace path: %s", path.toString());
        }
    }

    /**
     * @return the workspace path
     */
    @objid ("0096bfc2-e525-1fc8-b42e-001ec947cd2a")
    public Path getWorkspacePath() {
        return this.workspacePath;
    }

    /**
     * Refresh the workspace view.
     */
    @objid ("0037b4e6-f974-1fc8-b42e-001ec947cd2a")
    private void refreshContents() {
        this.cache.refresh();
        refreshLabels();
    }

    @objid ("003d2ffc-83c8-1fe1-bf4c-001ec947cd2a")
    private void refreshLabels() {
        this.viewer.getTree().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (!getViewer().getTree().isDisposed())
                    getViewer().refresh(true);
            }
        });
    }

    /**
     * Select the project with the given name.
     * @param projectName the project to select
     */
    @objid ("0037c404-f974-1fc8-b42e-001ec947cd2a")
    private void selectProject(final String projectName) {
        final ProjectDescriptor project = this.cache.findProjectByName(projectName);
        if (project != null) {
            this.viewer.getTree().getDisplay().asyncExec(new Runnable() {
                @Override
                public void run() {
                    getViewer().setSelection(new StructuredSelection(project));
                }
            });
        }
    }

    @objid ("0033239a-6a48-1fcf-b5e2-001ec947cd2a")
    @Focus
    void setFocus() {
        this.viewer.getTree().setFocus();
    }

    /**
     * Called when the contents of the workspace are known to have changed (add/removing project)
     * @param wkspace the changed workspace
     */
    @objid ("00341ae8-6a48-1fcf-b5e2-001ec947cd2a")
    @Inject
    @Optional
    void onWorkspaceContents(@EventTopic(ModelioEventTopics.WORKSPACE_CONTENTS) final Path wkspace) {
        AppProjectUi.LOG.debug("onWorkspaceContents() ", wkspace.toString());
        if (isValid())
            refreshContents();
    }

    @objid ("003dc944-83c8-1fe1-bf4c-001ec947cd2a")
    @Inject
    @Optional
    void onProjectOpened(@UIEventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject project, @Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, MWindow window, final IModelioProgressService progressService, final StatusReporter statusReporter) {
        AppProjectUi.LOG.debug("onProjectOpened() %s", project.getName());
        refreshLabels();
        
        // FIXME Misplaced
        IWindowCloseHandler handler = new IWindowCloseHandler() {
            @Override
            public boolean close(MWindow windoww) {
                return CloseProjectHandler.saveBeforeClose(shell, WorkspaceTreeView.this.projectService, progressService, statusReporter);
            }
        };
        
        window.getContext().set(IWindowCloseHandler.class, handler);
    }

    /**
     * Called when the current opened project has been closed
     * @param project the closed project
     */
    @objid ("003e126e-83c8-1fe1-bf4c-001ec947cd2a")
    @Inject
    @Optional
    void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject project) {
        AppProjectUi.LOG.debug("onProjectClosed() %s", project);
        // fire a whole workspace contents refresh (reloading the cache) to
        // ensure that project descriptors are "re-read" from disc
        if (isValid())
            refreshContents();
    }

    /**
     * Called when the currently opened project has been saved.
     * @param project the saved project
     */
    @objid ("0090b816-9548-106d-bbdd-001ec947cd2a")
    @Inject
    @Optional
    void onProjectSaved(@UIEventTopic(ModelioEventTopics.PROJECT_SAVED) final GProject project) {
        if (isValid()) {
            refreshContents();
            selectProject(project.getName());
        }
    }

    /**
     * Called when current workspace has been changed (another workspace was chosen)
     * @param wkspace the new workspace
     */
    @objid ("005c029c-7b47-10b3-9941-001ec947cd2a")
    @Inject
    @Optional
    void onWorkspaceSwitch(@EventTopic(ModelioEventTopics.WORKSPACE_SWITCH) final Path wkspace) {
        AppProjectUi.LOG.debug("onWorkspaceSwitch() ", wkspace.toString());
        setWorkspacePath(wkspace);
    }

    @objid ("35be9f1f-e5f0-4fb3-81ee-a189745dc63c")
    TreeViewer getViewer() {
        return WorkspaceTreeView.this.viewer;
    }

    /**
     * Tells whether the tree view is initialized and not disposed.
     * @return <code>true</code> if the viewer is usable else <code>false</code>.
     */
    @objid ("a8d55f96-363b-4568-9f42-6848eb42fbc8")
    boolean isValid() {
        return this.viewer != null && this.viewer.getTree() != null && !this.viewer.getTree().isDisposed();
    }

    /**
     * Cache of all project descriptors found in the workspace.
     */
    @objid ("00397e3e-f974-1fc8-b42e-001ec947cd2a")
    static class ProjectCache {
        @objid ("00572dda-f974-1fc8-b42e-001ec947cd2a")
        public Path cachePath;

        @objid ("005a32c8-663f-1fec-a7f4-001ec947cd2a")
         ArrayList<ProjectDescriptor> cachedProjects = new ArrayList<>();

        @objid ("00576aca-f974-1fc8-b42e-001ec947cd2a")
        public ProjectCache() {
        }

        @objid ("005779e8-f974-1fc8-b42e-001ec947cd2a")
        public ProjectDescriptor[] getProjects() {
            return this.cachedProjects.toArray(new ProjectDescriptor[this.cachedProjects.size()]);
        }

        @objid ("0057ad3c-f974-1fc8-b42e-001ec947cd2a")
        public void load(final Path aWorkspacePath) {
            this.cachePath = aWorkspacePath;
            rescan(this.cachePath);
        }

        @objid ("0057cdc6-f974-1fc8-b42e-001ec947cd2a")
        public void refresh() {
            rescan(this.cachePath);
        }

        @objid ("0057dca8-f974-1fc8-b42e-001ec947cd2a")
        public ProjectDescriptor findProjectByName(final String projectName) {
            for (ProjectDescriptor projectDescriptor : this.cachedProjects) {
                if (projectDescriptor.getName().equals(projectName)) {
                    return projectDescriptor;
                }
            }
            return null;
        }

        @objid ("00103f6a-9366-1061-84ef-001ec947cd2a")
        private void rescan(final Path aWorkspacePath) {
            this.cachedProjects.clear();
            
            // scan workspace
            if (Files.isDirectory(aWorkspacePath)) {
                try (DirectoryStream<Path> dirList = Files.newDirectoryStream(aWorkspacePath);) {
                    for (Path entry : dirList) {
                        if (GProjectFactory.isProjectSpace(entry)) {
                            ProjectDescriptor projectDescriptor = GProjectFactory.readProjectDirectory(entry);
                            this.cachedProjects.add(projectDescriptor);
                        }
                    }
                } catch (Exception e) {
                    // Should catch IOException of 'Files.newDirectoryStream'
                    // but JDK compiler don't allow it.
                    AppProjectUi.LOG.error(e);
                }
            }
        }

    }

}
