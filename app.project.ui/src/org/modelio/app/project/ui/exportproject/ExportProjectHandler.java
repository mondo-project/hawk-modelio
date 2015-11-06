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
                                    

package org.modelio.app.project.ui.exportproject;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.gproject.GProject;
import org.modelio.ui.progress.IModelioProgressService;

@objid ("00449daa-cc35-1ff2-a7f4-001ec947cd2a")
public class ExportProjectHandler {
    @objid ("0049997c-cc35-1ff2-a7f4-001ec947cd2a")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, final IProjectService projectService, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, IModelioProgressService progressService) {
        final ProjectDescriptor projectToExport = getSelectedElements(selection).get(0);
        if (projectToExport == null) {
            return;
        }
        
        AppProjectUi.LOG.info("Exporting project " + projectToExport.getName());
        
        final GProject openedProject = projectService.getOpenedProject();
        
        // check that the project to export is not the currently opened one.
        // Opened project cannot be exported as the exported project file would
        // be locked
        if (openedProject != null && openedProject.getName().equals(projectToExport.getName())) {
            MessageDialog.openError(shell,
                    AppProjectUi.I18N.getMessage("CannotExportOpenedProjectTitle", projectToExport.getName()),
                    AppProjectUi.I18N.getMessage("CannotExportOpenedProjectMsg", projectToExport.getName()));
            return;
        }
        
        // Exporting a project consists in zipping its directory contents into a
        // zip archive
        // Prompt the user for the archive file path and name
        final Path archiveFile = promptUserForFile(shell, projectToExport.getPath());
        if (archiveFile != null) {
            IRunnableWithProgress runnable = new IRunnableWithProgress() {
                
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    projectService.exportProject(projectToExport, archiveFile, new ModelioProgressAdapter(monitor));
                }
            };
            
            try {
                progressService.busyCursorWhile(runnable);
                //service.run(false, false, runnable);
            } catch (InvocationTargetException e) {
                AppProjectUi.LOG.error(e.getCause());
                MessageDialog.openError(shell, AppProjectUi.I18N.getString("ExportError"), e.getCause().toString());
            } catch (InterruptedException e) {
                AppProjectUi.LOG.info("Export aborted by user.");
            }
        } else {
            AppProjectUi.LOG.info("Export aborted by user.");
        }
        return;
    }

    @objid ("00499a12-cc35-1ff2-a7f4-001ec947cd2a")
    @CanExecute
    public boolean canExecute(final IProjectService projectService, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        if (selection == null) {
            return false;
        }
        List<ProjectDescriptor> projectDescriptors = getSelectedElements(selection);
        if (projectDescriptors.size() != 1) {
            return false;
        }
        
        if (projectDescriptors.get(0).getLockInfo() != null)
            return false;
        
        GProject openedProject = projectService.getOpenedProject();
        if (openedProject != null) {
            // cannot export currently opened project
            if (openedProject.getName().equals(projectDescriptors.get(0).getName())) {
                return false;
            }
        }
        return true;
    }

    @objid ("00499ab2-cc35-1ff2-a7f4-001ec947cd2a")
    protected Path promptUserForFile(Shell parentShell, Path projectSpace) {
        FileDialog dialog = new FileDialog(parentShell, SWT.SAVE);
        
        dialog.setFilterNames(new String[] { AppProjectUi.I18N.getString("ProjectArchive") });
        dialog.setFilterExtensions(new String[] { "*.zip;" });
        
        dialog.setFileName(Paths.get(projectSpace.getParent().toString(), projectSpace.getFileName().toString() + ".zip")
                .toString());
        
        dialog.setFilterPath(projectSpace.getParent().toString());
        
        String sfilePath = dialog.open();
        if (sfilePath != null) {
        
            if (Files.exists(Paths.get(sfilePath))) {
                boolean override = MessageDialog.openConfirm(parentShell, "Override ?", "The chosen file exists. Override it ?");
        
                if (override == false) {
                    return promptUserForFile(parentShell, projectSpace);
                }
            }
        
            try {
                if (!sfilePath.endsWith(".zip")) {
                    sfilePath += ".zip";
                }
        
                return Paths.get(sfilePath);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @objid ("7b047e2a-ea67-48f1-a0b9-d79227df86e3")
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

}
