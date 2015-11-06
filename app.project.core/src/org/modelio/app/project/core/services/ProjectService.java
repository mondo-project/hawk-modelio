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
                                    

package org.modelio.app.project.core.services;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystemException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipEntry;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.events.ModelioEvent;
import org.modelio.app.preferences.GProjectPreferenceNode;
import org.modelio.app.preferences.GProjectPreferenceStore;
import org.modelio.app.project.core.creation.IProjectCreationData;
import org.modelio.app.project.core.creation.IProjectCreator;
import org.modelio.app.project.core.plugin.AppProjectCore;
import org.modelio.app.project.core.prefs.ProjectPreferencesHelper;
import org.modelio.app.project.core.prefs.ProjectPreferencesKeys;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.data.project.DescriptorWriter;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.fragment.FragmentAuthenticationException;
import org.modelio.gproject.fragment.FragmentMigrationNeededException;
import org.modelio.gproject.fragment.FragmentState;
import org.modelio.gproject.fragment.Fragments;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.FragmentConflictException;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.gproject.GProjectAuthenticationException;
import org.modelio.gproject.gproject.GProjectConfigurer.Failure;
import org.modelio.gproject.gproject.GProjectEvent;
import org.modelio.gproject.gproject.GProjectEventType;
import org.modelio.gproject.gproject.GProjectFactory;
import org.modelio.gproject.gproject.IProjectMonitor;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.MModelServices;
import org.modelio.gproject.module.IModuleCatalog;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.metamodel.factory.ModelFactory;
import org.modelio.metamodel.uml.statik.DataType;
import org.modelio.metamodel.uml.statik.VisibilityMode;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vbasic.auth.IAuthData;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vbasic.files.Unzipper;
import org.modelio.vbasic.files.Zipper;
import org.modelio.vbasic.progress.IModelioProgress;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.AccessDeniedException;
import org.modelio.vcore.smkernel.mapi.MRef;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.prefs.BackingStoreException;

/**
 * {@link IProjectService} implementation.
 */
@objid ("00964fc4-9ea6-103b-a520-001ec947cd2a")
class ProjectService implements IProjectService, EventHandler {
    @objid ("00270c0e-1e45-105b-aa42-001ec947cd2a")
    private final IEclipseContext context;

    @objid ("003dc516-7baa-10b3-9941-001ec947cd2a")
    private static final String LAST_USED_WORKSPACE_PREFERENCE_KEY = "workspace.last";

    @objid ("00801182-acc2-103b-a520-001ec947cd2a")
    private Path workspace;

    @objid ("008017fe-acc2-103b-a520-001ec947cd2a")
    private GProject project;

    @objid ("00801e98-acc2-103b-a520-001ec947cd2a")
    private ICoreSession session;

    @objid ("f354133c-7447-4867-895c-f63bf790b2f0")
    private GProjectPreferenceStore prefsStore;

    /**
     * C'tor
     * @param context the Eclipse context.
     */
    @objid ("00802442-acc2-103b-a520-001ec947cd2a")
    public ProjectService(final IEclipseContext context) {
        this.context = context;
        final IEventBroker eventBroker = context.get(IEventBroker.class);
        eventBroker.subscribe("BATCH", this);
    }

    /**
     * @Inheritdoc
     */
    @objid ("008063d0-acc2-103b-a520-001ec947cd2a")
    @Override
    public void changeWorkspace(Path workspacePath) {
        if (this.project != null) {
            throw new IllegalStateException("A project is already opened.");
        }
        
        if (Files.exists(workspacePath) && Files.isDirectory(workspacePath)) {
            this.workspace = workspacePath;
            writePreferedWorkspace(workspacePath);
            this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.WORKSPACE_SWITCH, workspacePath);
        } else {
            throw new IllegalArgumentException("Invalid workspace path: " + workspacePath);
        }
    }

    @objid ("e92ae6b5-2c62-44c7-8665-3a6bd7f76ee9")
    @Override
    public void createProject(final IProjectCreator projectCreator, final IProjectCreationData data, IProgressMonitor monitor) throws IOException {
        Objects.requireNonNull(projectCreator);
        Objects.requireNonNull(data);
        
        // Get the module catalog used by the application.
        final IModuleCatalog moduleCatalog = getModuleCatalog();
        
        IModelioProgress progress = monitor==null ? null : new ModelioProgressAdapter(monitor);
        
        projectCreator.createProject(data, moduleCatalog, progress);
        
        this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.WORKSPACE_CONTENTS, getWorkspace());
    }

    @objid ("971ad6d9-026d-11e2-8189-001ec947ccaf")
    @Override
    public void openProject(ProjectDescriptor projectToOpen, IAuthData authData, IProgressMonitor monitor) throws GProjectAuthenticationException, IOException {
        if (this.project != null) {
            throw new IllegalStateException("A project is already opened.");
        }
        
        if (projectToOpen == null) {
            throw new IllegalArgumentException("Cannot open 'null' project.");
        }
        
        final String taskName = AppProjectCore.I18N.getMessage("ProjectService.open.task", projectToOpen.getName());
        final SubMonitor mon = SubMonitor.convert(monitor, taskName, 200);
        mon.subTask(taskName);
        
        // Get the module catalog used by the application.
        final IModuleCatalog moduleCatalog = getModuleCatalog();
        
        final ProjectMonitor projectMonitor = new ProjectMonitor();
        this.project = GProjectFactory.openProject(projectToOpen, authData, moduleCatalog,
                projectMonitor, new ModelioProgressAdapter(mon.newChild(50)));
        
        try (ProjectCloser closer = new ProjectCloser()) {
        
            this.session = this.project.getSession();
        
            // Project preferences
            this.prefsStore = new GProjectPreferenceStore(this.project);
        
            // Add the services that depends on project to the context
            // model service
            final MModelServices modelServices = new MModelServices(this.project);
            this.context.set(IMModelServices.class, modelServices);
            
            // Check for migration
            migrateFragments(mon, 50);
            mon.setWorkRemaining(100);
        
            // Fire PROJECT_OPENING
            this.context.get(IModelioEventService.class).postSyncEvent(this, ModelioEvent.PROJECT_OPENING, this.project);
        
            // Start all modules
            IModuleService moduleService = this.context.get(IModuleService.class);
            moduleService.startAllModules(this.project, mon.newChild(50));
        
            // Synchronize the project against server
            try {
                ProjectSynchronizer projectSynchronizer = new ProjectSynchronizer(this.project, moduleService);
                projectSynchronizer.synchronize(mon.newChild(50));
                if (! projectSynchronizer.getFailures().isEmpty())
                    reportSynchronizationFailures(projectSynchronizer);
            } catch (IOException e) {
                reportSynchronizationFail(e);
            }
        
            // Configure project preferences
            installProjectPreferencesDefaults();
            installProjectPreferences();
        
            // Fire PROJECT_OPENED
            this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.PROJECT_OPENED, this.project);
        
            // Validate project opening
            closer.abort();
            projectMonitor.openInProgress = false;
        }
    }

    @objid ("004dc0f6-8d1e-10b4-9941-001ec947cd2a")
    @Override
    public void openProject(String projectName, IAuthData authData, IProgressMonitor monitor) throws GProjectAuthenticationException {
        final Path projectPath = getWorkspace().resolve(projectName);
        final Path confFile = projectPath.resolve("project.conf");
        
        if (Files.isRegularFile(confFile)) {
            try {
                ProjectDescriptor pDesc = GProjectFactory.readProjectDirectory(projectPath);
                openProject(pDesc, authData, monitor);
            } catch (final IOException e) {
                AppProjectCore.LOG.error(e);
            }
        }
    }

    @objid ("00809bf2-acc2-103b-a520-001ec947cd2a")
    @Override
    public void closeProject(GProject projectToClose) throws IllegalStateException {
        checkProjectOpened();
        
        if (projectToClose == null || projectToClose != this.project) {
            throw new IllegalArgumentException("Closing invalid "+projectToClose+" project.");
        }
        
        this.context.get(IModelioEventService.class).postSyncEvent(this, ModelioEvent.PROJECT_CLOSING, projectToClose);
        
        // FIXME use the current monitor...
        IModuleService moduleService = this.context.get(IModuleService.class);
        if (moduleService != null)
            moduleService.stopAllModules(this.project);
        
        this.project.close();
        this.project = null;
        this.session = null;
        
        // preferences store
        this.prefsStore = null;
        
        // Invalidate the current model services instance before removing it
        // so that if some reference have been kept on it by @&#!%*!&
        // programmers, the error will be detected.
        final IMModelServices s = this.context.get(IMModelServices.class);
        ((MModelServices) s).invalidateProject(null);
        this.context.remove(IMModelServices.class);
        
        // remove IModuleService from context
        this.context.remove(IModuleService.class);
        
        this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.PROJECT_CLOSED, null);
    }

    @objid ("0080b83a-acc2-103b-a520-001ec947cd2a")
    @Override
    public void saveProject(IProgressMonitor monitor) throws IOException {
        checkProjectOpened();
        
        try {
            final String taskName = AppProjectCore.I18N.getMessage("ProjectService.save.task", this.project.getName());
            SubMonitor m = SubMonitor.convert(monitor, taskName,1000);
            
            this.context.set(IProgressMonitor.class, m.newChild(50));
            this.context.get(IModelioEventService.class).postSyncEvent(this, ModelioEvent.PROJECT_SAVING, this.project);
            this.context.remove(IProgressMonitor.class);
            
            this.project.save(new ModelioProgressAdapter(m.newChild(900)));
            
            this.prefsStore.save();
            
            this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.PROJECT_SAVED, this.project);
        } catch (final IOException e) {
            throw e;
        }
    }

    @objid ("00804076-acc2-103b-a520-001ec947cd2a")
    @Override
    public String getName() {
        return "ProjectService";
    }

    @objid ("0080cffa-acc2-103b-a520-001ec947cd2a")
    @Override
    public GProject getOpenedProject() {
        return this.project;
    }

    @objid ("14aa57e7-a7cb-4ee1-82ed-973bb22d6870")
    @Override
    public IPreferenceStore getProjectPreferences(String nodeId) {
        return new GProjectPreferenceNode(this.prefsStore, nodeId);
    }

    @objid ("0053aa84-bb2f-103c-a520-001ec947cd2a")
    @Override
    public ICoreSession getSession() {
        return (this.project != null) ? this.project.getSession() : null;
    }

    @objid ("0080f426-acc2-103b-a520-001ec947cd2a")
    @Override
    public Path getWorkspace() {
        if (this.workspace == null) {
            this.workspace = readPreferedWorkspace();
        }
        return this.workspace;
    }

    @objid ("ca2f60e4-1f24-4956-965e-92f53059258c")
    @Override
    public boolean isDirty() {
        boolean isSessionDirty = this.session != null && this.session.isDirty();
        boolean isPrefStoreDirty = this.prefsStore != null && this.prefsStore.needsSaving();
        return isSessionDirty || isPrefStoreDirty;
    }

    @objid ("0089dd7a-8c65-103c-a520-001ec947cd2a")
    @Override
    public void deleteProject(ProjectDescriptor projectToDelete) throws IOException, FileSystemException {
        // TODO this is a quite naive implementation
        // should deal with project path for delegating project
        
        FileUtils.delete(projectToDelete.getPath());
        refreshWorkspace();
    }

    @objid ("0089fb8e-8c65-103c-a520-001ec947cd2a")
    @Override
    public void exportProject(ProjectDescriptor projectToExport, Path archivePath, IModelioProgress monitor) {
        final Zipper zipper = new Zipper(archivePath);
        try {
            // not export .runtime directory
            PathMatcher directoryMatcher = FileSystems.getDefault().getPathMatcher(
                    ("glob:**" + projectToExport.getPath().resolve(".runtime").resolve("modules")).replace("\\", "\\\\"));
            List<PathMatcher> skipDirectoryMatchers = new ArrayList<>();
            skipDirectoryMatchers.add(directoryMatcher);
            zipper.compress(projectToExport.getPath(), skipDirectoryMatchers, null, monitor, null);
            AppProjectCore.LOG.info("Exported archive '%s' %d bytes.", archivePath, Files.size(archivePath));
        
        } catch (final IOException e) {
            AppProjectCore.LOG.error(e);
        }
    }

    @objid ("008a1ee8-8c65-103c-a520-001ec947cd2a")
    @Override
    public void importProject(Path archiveFile) {
        final Unzipper unzipper = new Unzipper();
        final IModelioProgress monitor = null;
        try {
            ZipEntry[] projectConf = unzipper.findEntry(archiveFile.toFile(), "^[^/]+/project\\.conf$");
            String projectName = "";
            if (projectConf.length == 1) {
                projectName = projectConf[0].getName().substring(0, projectConf[0].getName().indexOf("/"));
                if (getWorkspace().resolve(projectName).toFile().exists()) { // Checks for an already existing project
                    if (!MessageDialog.openQuestion(null, AppProjectCore.I18N.getString("CannotImportExistingProjectTitle"), AppProjectCore.I18N.getMessage("CannotImportExistingProjectMsg", projectName))) {                    
                        return;
                    }
                }
                unzipper.unzip(archiveFile, getWorkspace(), monitor);
                AppProjectCore.LOG.info("Imported archive '%s' %d bytes.", archiveFile, Files.size(archiveFile));
                refreshWorkspace();
            } else {
                MessageDialog.openInformation(null, AppProjectCore.I18N.getString("InvalidProjectArchiveTitle"), AppProjectCore.I18N.getMessage("InvalidProjectArchiveMsg", archiveFile.toString()));
            }
        } catch (final IOException e) {
            AppProjectCore.LOG.error(e);
        }
    }

    @objid ("008a3cfc-8c65-103c-a520-001ec947cd2a")
    @Override
    public void createProject(final IProjectCreator projectCreator, final IProjectCreationData data) {
        Objects.requireNonNull(projectCreator);
        Objects.requireNonNull(data);
        
        // Get the module catalog used by the application.
        final IModuleCatalog moduleCatalog = getModuleCatalog();
        
        // Provide a progress monitor as parameter if possible
        IModelioProgressService progressService = this.context.get(IModelioProgressService.class);
        if (progressService != null) {
            try {
                final String title = AppProjectCore.I18N.getString("CreateProject.ProgressDialog.title");
                
                progressService.run(title, false, false, new IRunnableWithProgress() {
                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        try {
                            IModelioProgress progress = new ModelioProgressAdapter(monitor);
                            progress.beginTask(title, 100);
                            projectCreator.createProject(data, moduleCatalog, progress);
                        } catch (IOException e) {
                            AppProjectCore.LOG.error(e);
                        }
                    }
        
                });
            } catch (InvocationTargetException | InterruptedException e) {
                AppProjectCore.LOG.error(e);
            }
        } else {
            // No progress service available
            try {
                projectCreator.createProject(data, moduleCatalog, null);
            } catch (IOException e) {
                AppProjectCore.LOG.error(e);
            }
        }
        
        this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.WORKSPACE_CONTENTS, getWorkspace());
    }

    @objid ("005385e0-bb2f-103c-a520-001ec947cd2a")
    @Override
    public void addFragment(GProject openedProject, FragmentDescriptor fragmentDescriptor, IProgressMonitor monitor) throws FragmentConflictException {
        final IProjectFragment newFragment = Fragments.getFactory(fragmentDescriptor).instantiate(fragmentDescriptor);
        openedProject.registerFragment(newFragment, new ModelioProgressAdapter(monitor));
        
        this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.FRAGMENT_ADDED, newFragment);
    }

    @objid ("002e01f8-a4c3-1044-a30e-001ec947cd2a")
    @Override
    public void removeFragment(GProject openedProject, IProjectFragment fragmentToRemove) {
        if (fragmentToRemove == null) {
            throw new IllegalArgumentException("Fragment must not be null.");
        }
        
        openedProject.unregisterFragment(fragmentToRemove);
        
        // Delete the fragment files.
        try {
            fragmentToRemove.delete();
        } catch (final IOException e) {
            AppProjectCore.LOG.warning(e);
        }
        
        this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.FRAGMENT_REMOVED, fragmentToRemove);
    }

    @objid ("008118fc-acc2-103b-a520-001ec947cd2a")
    @Override
    public void refreshWorkspace() {
        this.context.get(IModelioEventService.class).postAsyncEvent(this, ModelioEvent.WORKSPACE_CONTENTS, this.workspace);
    }

    @objid ("6bcf2ddb-37b3-11e2-82ed-001ec947ccaf")
    IEclipseContext getContext() {
        return this.context;
    }

    @objid ("004d1728-8d1e-10b4-9941-001ec947cd2a")
    @Inject
    @Optional
    public void onBATCH(@EventTopic("BATCH") final CommandLineData data) {
        final IEventBroker eventBroker = this.context.get(IEventBroker.class);
        eventBroker.unsubscribe(this);
        
        AppProjectCore.LOG.info("Running batch: %s", data.toString());
        if (!data.isEmpty()) {
            Display.getCurrent().asyncExec(new BatchRunner(this, data));
        }
    }

    @objid ("004d9dc4-8d1e-10b4-9941-001ec947cd2a")
    @Override
    public void handleEvent(Event event) {
        switch (event.getTopic()) {
        case "BATCH":
            onBATCH((CommandLineData) event.getProperty("org.eclipse.e4.data"));
            return;
        default:
            return;
        }
    }

    /**
     * Get the workspace to use:
     * <ol>
     * <li>use the last used workspace as saved in the preferences</li>
     * <li>default to user's home directory otherwise</li>
     * </ol>
     * @return the workspace path
     */
    @objid ("0035c4b0-7baa-10b3-9941-001ec947cd2a")
    private static Path readPreferedWorkspace() {
        final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(AppProjectCore.PLUGIN_ID);
        
        final String lastUsed = prefs.get(LAST_USED_WORKSPACE_PREFERENCE_KEY, null);
        if (lastUsed != null) {
            final Path lastPath = Paths.get(lastUsed);
            if (lastPath != null && Files.isDirectory(lastPath)) {
                return lastPath; // we are done
            }
        }
        // Preferences could not provide a valid workspace, default to user's
        // home
        Path defaultPath = Paths.get(System.getProperty("user.home"), "modelio", "workspace");
        if (!Files.exists(defaultPath, LinkOption.NOFOLLOW_LINKS)) {
            (new File(defaultPath.toString())).mkdirs(); // create if the
                                                            // default workspace
                                                            // doesn't exist.
        }
        return defaultPath;
    }

    /**
     * Write the workspace preferences
     * @param workspace the workspace path
     */
    @objid ("0035eddc-7baa-10b3-9941-001ec947cd2a")
    private static void writePreferedWorkspace(Path workspace) {
        if (workspace != null) {
            final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(AppProjectCore.PLUGIN_ID);
        
            prefs.put(LAST_USED_WORKSPACE_PREFERENCE_KEY, workspace.toString());
            try {
                prefs.flush();
            } catch (final BackingStoreException e) {
                AppProjectCore.LOG.error(e);
            }
        }
    }

    @objid ("9de1f14d-8669-48e5-b0c0-11c43702c837")
    private void checkProjectOpened() throws IllegalStateException {
        if (this.project == null) {
            throw new IllegalStateException("No current project.");
        }
    }

    /**
     * Get a module catalog.
     * <p>
     * Returns the module catalog cache. Instantiate the module catalog and the
     * cache on first call.
     * @return the module catalog cache.
     */
    @objid ("e84ef926-a1af-4f78-9342-507d1c7efcb4")
    private IModuleCatalog getModuleCatalog() {
        // look for the module catalog
        return this.context.get(IModuleCatalog.class);
    }

    @objid ("cb61fb6e-713f-480a-8fe4-42c7e06a13ec")
    private void installProjectPreferences() {
        // Setup the model factory initializer
        ModelFactory.INITIALIZER.setDefaultAttributeType(ProjectPreferencesHelper.getAttributeDefaultType(this));
        ModelFactory.INITIALIZER.setDefaultAttributeVisibility(ProjectPreferencesHelper.getAttributeDefaultVisibility(this));
        ModelFactory.INITIALIZER.setDefaultParameterType(ProjectPreferencesHelper.getParameterDefaultType(this));
        ModelFactory.INITIALIZER.setDefaultReturnType(ProjectPreferencesHelper.getReturnDefaultType(this));
        
        // Setup a listener for changes that will reconfigure the INITIALIZER
        // when project preferences change
        this.prefsStore.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                String name = event.getProperty();
        
                if (name.endsWith(ProjectPreferencesKeys.ATT_DEFAULT_TYPE_PREFKEY))
                    ModelFactory.INITIALIZER.setDefaultAttributeType(ProjectPreferencesHelper
                            .getAttributeDefaultType(ProjectService.this));
                else if (name.endsWith(ProjectPreferencesKeys.ATT_DEFAULT_VIS_PREFKEY))
                    ModelFactory.INITIALIZER.setDefaultAttributeVisibility(ProjectPreferencesHelper
                            .getAttributeDefaultVisibility(ProjectService.this));
                else if (name.endsWith(ProjectPreferencesKeys.PARAM_DEFAULT_TYPE_PREFKEY))
                    ModelFactory.INITIALIZER.setDefaultParameterType(ProjectPreferencesHelper
                            .getParameterDefaultType(ProjectService.this));
                else if (name.endsWith(ProjectPreferencesKeys.RETURN_DEFAULT_TYPE_PREFKEY))
                    ModelFactory.INITIALIZER.setDefaultReturnType(ProjectPreferencesHelper
                            .getReturnDefaultType(ProjectService.this));
            }
        });
    }

    @objid ("7b203cde-7d61-4515-88a8-cb2cc182b426")
    private void installProjectPreferencesDefaults() {
        DataType stringDataType = this.project.getSession().getModel()
                .findById(DataType.class, UUID.fromString("00000004-0000-000d-0000-000000000000"));
        DataType integerDataType = this.project.getSession().getModel()
                .findById(DataType.class, UUID.fromString("00000004-0000-0009-0000-000000000000"));
        
        // During project creation the project might be opened once without
        // modeler module because of some configuration defect. In this case it is not possible to set the default
        // values, just do nothing to avoid stopping here where things would be difficult to understand by the user. 
        if (stringDataType != null && integerDataType != null) {
            // Define default values if not already defined
            IPreferenceStore store = this.getProjectPreferences(ProjectPreferencesKeys.NODE_ID);
            if (store.getDefaultString(ProjectPreferencesKeys.ATT_DEFAULT_TYPE_PREFKEY).isEmpty()) {
                store.setDefault(ProjectPreferencesKeys.ATT_DEFAULT_TYPE_PREFKEY, new MRef(stringDataType).toString());
                store.setToDefault(ProjectPreferencesKeys.ATT_DEFAULT_TYPE_PREFKEY);
            }
            if (store.getDefaultString(ProjectPreferencesKeys.ATT_DEFAULT_VIS_PREFKEY).isEmpty()) {
                store.setDefault(ProjectPreferencesKeys.ATT_DEFAULT_VIS_PREFKEY, VisibilityMode.PUBLIC.getName());
                store.setToDefault(ProjectPreferencesKeys.ATT_DEFAULT_VIS_PREFKEY);
            }
            if (store.getDefaultString(ProjectPreferencesKeys.PARAM_DEFAULT_TYPE_PREFKEY).isEmpty()) {
                store.setDefault(ProjectPreferencesKeys.PARAM_DEFAULT_TYPE_PREFKEY, new MRef(stringDataType).toString());
                store.setToDefault(ProjectPreferencesKeys.PARAM_DEFAULT_TYPE_PREFKEY);
            }
            if (store.getDefaultString(ProjectPreferencesKeys.RETURN_DEFAULT_TYPE_PREFKEY).isEmpty()) {
                store.setDefault(ProjectPreferencesKeys.RETURN_DEFAULT_TYPE_PREFKEY, new MRef(integerDataType).toString());
                store.setToDefault(ProjectPreferencesKeys.RETURN_DEFAULT_TYPE_PREFKEY);
            }
            if (store.getDefaultString(ProjectPreferencesKeys.RICHNOTE_DEFAULT_TYPE_PREFKEY).isEmpty()) {
                // Use a default value
                String defaultValue;
                if (System.getProperty("os.name").equals("Linux")) {
                    defaultValue = "application/vnd.oasis.opendocument.text";
                } else {
                    defaultValue ="application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                }
                
                store.setDefault(ProjectPreferencesKeys.RICHNOTE_DEFAULT_TYPE_PREFKEY, defaultValue);
                store.setToDefault(ProjectPreferencesKeys.RICHNOTE_DEFAULT_TYPE_PREFKEY);
            }
        }
    }

    @objid ("ddfed9a9-bcb8-4683-9326-13ed63566396")
    @Override
    public void renameProject(ProjectDescriptor projectDescriptor, String name) throws IOException {
        final Path oldPath = projectDescriptor.getPath();
        
        // Set the project name
        projectDescriptor.setName(name);
        
        // Save the new project conf
        Path confFilePath = oldPath.resolve("project.conf");
        new DescriptorWriter().write(projectDescriptor, confFilePath);
        
        // Move the project directory itself
        final Path targetPath = oldPath.resolveSibling(name);
        
        Files.move(oldPath, targetPath, StandardCopyOption.ATOMIC_MOVE);
        
        refreshWorkspace();
    }

    @objid ("9cb90edd-3cb3-4b86-a51b-f0a3992de641")
    private void reportSynchronizationFail(final IOException e) {
        final String err = FileUtils.getLocalizedMessage(e);
        final String message = AppProjectCore.I18N.getMessage(
                "ProjectService.ProjectSynchroFailed.message",
                this.project.getName(),
                err);
        
        AppProjectCore.LOG.warning(message);
        AppProjectCore.LOG.debug(e);
        
        // TODO detect batch mode and not display dialog box
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                String title = AppProjectCore.I18N.getMessage("ProjectService.ProjectSynchroFailed.title", getOpenedProject().getName());
                MessageDialog.openWarning(Display.getCurrent().getActiveShell(), title, message);
            }
        });
    }

    @objid ("bc6e9b7c-48b4-4f36-ab76-51e1b856a5f2")
    private void migrateFragments(SubMonitor mon, int allowedMonWork) {
        new FragmentsMigrator(this.project).migrateFragments(mon, allowedMonWork );
    }

    @objid ("41d09262-e599-4095-b93d-6e639bd3bc9a")
    private void reportSynchronizationFailures(ProjectSynchronizer projectSynchronizer) {
        // TODO detect batch mode and not display dialog box
        // TODO make a better dialog
        final StringBuilder sb = new StringBuilder();
        
        sb.append(AppProjectCore.I18N.getMessage(
                "ProjectService.ProjectSynchroProblems.message",
                this.project.getName()));
        
        for (Failure f : projectSynchronizer.getFailures()) {
            sb
            .append(" - ")
            .append(f.getSourceIdentifier())
            .append(": ");
            
            Throwable cause = f.getCause();
            if (cause instanceof IOException)
                sb.append(FileUtils.getLocalizedMessage((IOException) cause));
            else if (cause instanceof AccessDeniedException)
                sb.append(cause.getLocalizedMessage());
            else if (cause instanceof RuntimeException)
                sb.append(cause.toString());
            else 
                sb.append(cause.getLocalizedMessage());
        }
        
        // Get a shell
        Shell shell = (Shell) this.context.getActive(IServiceConstants.ACTIVE_SHELL);
        if (shell == null) {
            IShellProvider sp = this.context.getActive(IShellProvider.class);
            if (sp != null)
                shell = sp.getShell();
        }
        
        final Shell fshell = shell;
        final String title = AppProjectCore.I18N.getMessage(
                "ProjectService.ProjectSynchroProblems.title",
                this.project.getName());
        
        Display d = shell != null ? shell.getDisplay() : Display.getDefault();
        d.syncExec(new Runnable() {
            
            @Override
            public void run() {
                MessageDialog.openWarning(fshell, title, sb.toString());
            }
        });
    }

    /**
     * ProjectService project monitor
     * <p>
     * TODO make a real implementation. This one only sends events to log.
     */
    @objid ("6bc806dc-37b3-11e2-82ed-001ec947ccaf")
    private final class ProjectMonitor implements IProjectMonitor {
        @objid ("2f56600c-3346-4f15-920c-0ba9ca220429")
         boolean openInProgress = true;

        @objid ("6bca6931-37b3-11e2-82ed-001ec947ccaf")
        ProjectMonitor() {
            // nothing
        }

        @objid ("6bca6933-37b3-11e2-82ed-001ec947ccaf")
        @Override
        public void handleProjectEvent(GProjectEvent ev) {
            switch (ev.type) {
            case FRAGMENT_DOWN:
                
                if (this.openInProgress && ev.throwable instanceof FragmentMigrationNeededException) {
                    // don't report migration needs because dialog asks for migration
                    // on project opening.
                    AppProjectCore.LOG.info("%s fragment requires migration: %s", ev.fragment.getId(), ev.message);
                } else if (this.openInProgress && ev.throwable instanceof FragmentAuthenticationException) {
                    // don't report migration needs because caller asks for auth
                    // on project opening.
                    AppProjectCore.LOG.info("%s fragment requires authentication: %s", ev.fragment.getId(), ev.message);
                } else {
                    AppProjectCore.LOG.error("'%s' fragment falled DOWN: %s", ev.fragment.getId(), ev.message);
                    AppProjectCore.LOG.error(ev.throwable);
            
                    // display problem to user
                    reportAsStatus(ev);
                }
                getContext().get(IModelioEventService.class).postAsyncEvent(ProjectService.this, ModelioEvent.FRAGMENT_DOWN, ev.fragment);
                
                break;
            case WARNING:
                if (ev.throwable != null) {
                    AppProjectCore.LOG.warning(ev.throwable);
                } else if (ev.message != null) {
                    if (ev.fragment != null)
                        AppProjectCore.LOG.warning("%s : %s", ev.fragment.getId(), ev.message);
                    else
                        AppProjectCore.LOG.warning(ev.message);
                }
                break;
            case FRAGMENT_STATE_CHANGED:
                AppProjectCore.LOG.debug("'%s' fragment state changed to '%s'.",ev.fragment.getId(), ev.fragment.getState().toString());
                if (ev.fragment.getState()==FragmentState.UP_FULL || ev.fragment.getState()==FragmentState.UP_LIGHT)
                    getContext().get(IModelioEventService.class).postAsyncEvent(ProjectService.this, ModelioEvent.FRAGMENT_UP, ev.fragment);
                break;
            default:
                if (ev.message != null) {
                    AppProjectCore.LOG.info(ev.message);
                }
                if (ev.throwable != null) {
                    AppProjectCore.LOG.info(ev.throwable);
                }
                break;
            
            }
        }

        @objid ("ff9cf99a-5c5e-47ba-8ea7-57daa521a97b")
        String getMessage(GProjectEvent ev) {
            if (ev.message != null && !ev.message.isEmpty())
                return ev.message;
            else if (ev.throwable == null)
                return AppProjectCore.I18N.getMessage("ProjectService.noEventMessage");
            else if (ev.throwable instanceof FileSystemException)
                return FileUtils.getLocalizedMessage((FileSystemException) ev.throwable);
            else if (ev.throwable instanceof RuntimeException)
                return ev.throwable.toString(); // some runtime exceptions have no message at all
            else 
                return ev.throwable.getLocalizedMessage();
        }

        @objid ("357af83e-da98-41b2-8095-e74ddcdffa42")
        private void reportAsStatus(final GProjectEvent ev) {
            final StatusReporter statusReporter = getContext().get(StatusReporter.class);
            
            if (statusReporter != null) {
                Display.getDefault().asyncExec(new Runnable() {
            
                    @Override
                    public void run() {
                        IStatus s1 ;
                        if (ev.type == GProjectEventType.FRAGMENT_DOWN) {
                            String message = AppProjectCore.I18N.getMessage("ProjectService.fragmentDown", ev.fragment.getId());
                            s1 = statusReporter.newStatus(StatusReporter.WARNING, message, ev.throwable);
                        } else {
                            s1 = statusReporter.newStatus(StatusReporter.WARNING, getMessage(ev), ev.throwable);
                        }
                        statusReporter.report(s1, StatusReporter.SHOW, ev);
                    }
                });
            }
        }

    }

    /**
     * To be used in try-with-resource to automatically close the project unless
     * abort is called.
     * 
     * @author cmarin
     */
    @objid ("db5f4a1c-12ac-4142-a3c3-ea0d159d9669")
    private class ProjectCloser implements AutoCloseable {
        @objid ("8f4b6e4e-040b-41c6-bc9c-03e32fa18ad9")
        private boolean abort = false;

        @objid ("0d7b407c-27ba-46bf-9bf2-06fff2bc4286")
        public ProjectCloser() {
            // nothing
        }

        @objid ("be54458f-6225-4ced-aba2-963ce44de6f0")
        @Override
        public void close() {
            if (!this.abort)
                closeProject(getOpenedProject());
        }

        @objid ("d8c4e9a6-9abb-44b7-a308-7c0d155518b6")
        public void abort() {
            this.abort = true;
        }

    }

}
