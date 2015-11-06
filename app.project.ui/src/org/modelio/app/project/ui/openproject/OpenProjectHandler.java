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
                                    

package org.modelio.app.project.ui.openproject;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.module.ModuleException;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.app.project.ui.auth.AuthDataDialog;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.gproject.data.project.DefinitionScope;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.project.ModuleDescriptor;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.fragment.FragmentAuthenticationException;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.gproject.GProjectAuthenticationException;
import org.modelio.gproject.gproject.GProjectFactory;
import org.modelio.gproject.module.GModule;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vbasic.auth.IAuthData;
import org.modelio.vbasic.auth.NoneAuthData;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vbasic.progress.IModelioProgress;

/**
 * Handler for "Open project" command.
 */
@objid ("0044ac32-cc35-1ff2-a7f4-001ec947cd2a")
public class OpenProjectHandler {
    @objid ("52a322d6-6d3f-4338-b86c-1641c73deb20")
    @Inject
    @Optional
     IProjectService projectService;

    @objid ("f3f5681b-ade9-42e4-a0ce-455fde687a0e")
    @Inject
    @Optional
     IModuleService moduleService;

    @objid ("00470482-cc35-1ff2-a7f4-001ec947cd2a")
    @Execute
    void execute(@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, final IModelioProgressService progressService) {
        final ProjectDescriptor projectToOpen = getSelectedElements(selection).get(0);
        if (projectToOpen == null) {
            return;
        }
        
        // Check that authentication data is complete, if not => prompt user
        IAuthData authData = checkProjectAuth(shell, projectToOpen);
        
        if (authData == null) {
            // User cancelled => abort
            return;
        }
        
        assert (this.projectService.getOpenedProject() == null);
        AppProjectUi.LOG.info("Opening project '%s' ", projectToOpen.getName());
        
        boolean more = true;
        while (more) {
            final IAuthData effectiveAuthData = authData;
            IRunnableWithProgress runnable = new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        OpenProjectHandler.this.projectService.openProject(projectToOpen, effectiveAuthData, monitor);
        
                        // Check again that authentication data is complete, if not => prompt user
                        checkOpenProjectAuth(OpenProjectHandler.this.projectService.getOpenedProject());
        
                    } catch (IOException | GProjectAuthenticationException e) {
                        throw new InvocationTargetException(e, e.getLocalizedMessage());
                    }
        
                    // Update the descriptor lock infos
                    try {
                        projectToOpen.setLockInfo(GProjectFactory.getLockInformations(projectToOpen));
                    } catch (IOException e) {
                        AppProjectUi.LOG.warning(e);
                    }
                }
                
                private void checkOpenProjectAuth(final GProject openedProject) {
                    shell.getDisplay().asyncExec(new Runnable() {
                        @SuppressWarnings("synthetic-access")
                        @Override
                        public void run() {
                            doCheckOpenProjectAuth(shell, openedProject);
                        }
                    });
                    
                }
                
            };
        
            try {
                progressService.run(true, false, runnable);
                more = false;
            } catch (InvocationTargetException e) {
                try {
                    throw e.getCause();
                } catch (GProjectAuthenticationException cause) {
                    AppProjectUi.LOG.info(cause.getLocalizedMessage());
                    String label = AppProjectUi.I18N.getMessage("OpenProjectHandler.Auth.ProjectLabel", projectToOpen.getName());
                    authData = promptAuthentication(shell, effectiveAuthData, label);
                    more = true;
                } catch (IOException cause) {
                    AppProjectUi.LOG.debug(e);
                    MessageDialog.openError(shell, AppProjectUi.I18N.getString("Error"), FileUtils.getLocalizedMessage(cause));
                    more = false;
                } catch (Throwable cause) {
                    AppProjectUi.LOG.error(e);
                    MessageDialog.openError(shell, AppProjectUi.I18N.getString("Error"), cause.toString());
                    more = false;
                }
            } catch (InterruptedException e) {
                // nothing
                more = false;
            }
        }
    }

    @objid ("00470518-cc35-1ff2-a7f4-001ec947cd2a")
    @CanExecute
    boolean canExecute(final IProjectService projectService, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        if (selection == null || projectService.getOpenedProject() != null) {
            return false;
        }
        List<ProjectDescriptor> projects = getSelectedElements(selection);
        if (projects.size() != 1) {
            return false;
        }
        
        if (projects.get(0).getLockInfo() != null)
            return false;
        return true;
    }

    /**
     * @param parent a SWT shell
     * @param authData the authentication to complete
     * @param name the project or fragment name to authenticate.
     * @return the authentication data or null to abort.
     */
    @objid ("7161b2f6-c45d-4826-9441-df4c7fc50d80")
    private IAuthData promptAuthentication(Shell parent, IAuthData authData, String name) {
        AuthDataDialog dlg = new AuthDataDialog(parent, authData, name);
        dlg.setBlockOnOpen(true);
        int ret = dlg.open();
        switch (ret) {
        case 0:
            return dlg.getAuthData();
        default:
            return null;
        }
    }

    @objid ("384b0ca0-9855-4902-bc4a-44b058677c75")
    private IAuthData checkPartAuth(final Shell shell, IAuthData authToCheck, String name) {
        IAuthData authData = authToCheck;
        
        if (authData == null || !authData.isComplete()) {
            if (authData == null)
                authData = new NoneAuthData();
        
            do {
                authData = promptAuthentication(shell, authData, name);
            } while (authData != null && !authData.isComplete());
        }
        return authData;
    }

    @objid ("ad0a56c0-7178-456b-8463-3f8f3e50a6ab")
    private IAuthData checkProjectAuth(final Shell shell, final ProjectDescriptor projectToOpen) {
        String label = AppProjectUi.I18N.getMessage("OpenProjectHandler.Auth.ProjectLabel", projectToOpen.getName());
        IAuthData projAuthData = checkPartAuth(shell, projectToOpen.getAuthDescriptor().getData(), projectToOpen.getName()
                + " project");
        
        for (FragmentDescriptor f : projectToOpen.getFragments()) {
            IAuthData authData = f.getAuthDescriptor().getData();
            if (authData != null) {
                label = AppProjectUi.I18N.getMessage("OpenProjectHandler.Auth.FragmentLabel", f.getId());
                IAuthData newAuthData = checkPartAuth(shell, authData, label);
                if (newAuthData == null)
                    return null;
                if (authData != newAuthData)
                    f.getAuthDescriptor().setData(newAuthData);
            }
        }
        
        for (ModuleDescriptor f : projectToOpen.getModules()) {
            IAuthData authData = f.getAuthDescriptor().getData();
            if (authData != null) {
                label = AppProjectUi.I18N.getMessage("OpenProjectHandler.Auth.ModuleLabel", f.getName(), f.getVersion().toString());
                IAuthData newAuthData = checkPartAuth(shell, authData, label);
                if (newAuthData == null)
                    return null;
                if (authData != newAuthData)
                    f.getAuthDescriptor().setData(newAuthData);
            }
        }
        return projAuthData;
    }

    @objid ("0e09003a-88a6-403e-88c8-92584d18c49b")
    private List<ProjectDescriptor> getSelectedElements(final IStructuredSelection selection) {
        List<ProjectDescriptor> selectedElements = new ArrayList<>();
        if (selection.size() > 0) {
            Object[] elements = selection.toArray();
            for (Object element : elements) {
                if (element instanceof ProjectDescriptor) {
                    selectedElements.add((ProjectDescriptor) element);
                }
            }
        }
        return selectedElements;
    }

    @objid ("5ef276f9-7d08-4baf-9c93-89a096fac7b3")
    private void doCheckOpenProjectAuth(Shell shell, GProject openedProject) {
        String label;
        
        for (IProjectFragment f : openedProject.getFragments()) {
            while (needsAuth(f)) {
                IAuthData authData = f.getAuthConfiguration().getAuthData();
                if (authData == null)
                    authData = new NoneAuthData();
        
                label = AppProjectUi.I18N.getMessage("OpenProjectHandler.Auth.FragmentLabel", f.getId());
        
                IAuthData newAuthData = promptAuthentication(shell, authData, label);
                
                if (newAuthData == null)
                    break;
                if (authData != newAuthData)
                    f.getAuthConfiguration().setAuthData(newAuthData);
                
                IModelioProgress aMonitor = null; //TODO
                f.mount(openedProject, aMonitor);
            }
        }
        
        for (GModule f : openedProject.getModules()) {
            IAuthData authData = f.getAuthData().getAuthData();
            while (needsAuth(f)) {
                label = AppProjectUi.I18N.getMessage("OpenProjectHandler.Auth.ModuleLabel", f.getName(), f.getVersion().toString());
                IAuthData newAuthData = promptAuthentication(shell, authData, label);
                if (newAuthData == null)
                    break;
                if (authData != newAuthData)
                    f.getAuthData().setAuthData(newAuthData);
             
                try {
                    this.moduleService.activateModule(f);
                } catch (ModuleException e) {
                    // ignore
                }
            }
        }
    }

    @objid ("41451476-d25e-478b-abff-d6c9699fe875")
    private boolean needsAuth(GModule f) {
        if (f.getAuthData().getScope() == DefinitionScope.SHARED) 
            return false;
        
        IProjectFragment moduleFrag = f.getModelFragment();
        Throwable downError = moduleFrag!= null ? moduleFrag.getDownError() : null;
        return downError instanceof FragmentAuthenticationException || downError instanceof AccessDeniedException;
    }

    @objid ("4588b5fc-47d4-41d9-b185-892929816229")
    private boolean needsAuth(IProjectFragment f) {
        Throwable downError = f.getDownError();
        if (downError instanceof FragmentAuthenticationException || downError instanceof AccessDeniedException)
            return (f.getAuthConfiguration().getScope() != DefinitionScope.SHARED);
        else
            return false;
    }

}
