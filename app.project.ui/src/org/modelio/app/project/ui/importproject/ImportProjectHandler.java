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
                                    

package org.modelio.app.project.ui.importproject;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.app.project.ui.plugin.AppProjectUi;

@objid ("0044947c-cc35-1ff2-a7f4-001ec947cd2a")
public class ImportProjectHandler {
    @objid ("004702ac-cc35-1ff2-a7f4-001ec947cd2a")
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, @Optional final IProjectService projectService) {
        AppProjectUi.LOG.info("Importing a project");
        // Check that the project is not opened.
        // Opened project cannot be exported as the exported project file would
        // be locked
        if (projectService.getOpenedProject() != null) {
            MessageDialog.openError(shell, AppProjectUi.I18N.getMessage("CannotImportOpenedProjectTitle"),
                    AppProjectUi.I18N.getMessage("CannotImportOpenedProjectMsg"));
            return;
        }
        
        // Importing a project consists in un-zipping its archive contents into
        // a
        // directory
        // Prompt the user for the archive file path and name
        Path archiveFile = promptUserForFile(shell, projectService.getWorkspace());
        if (archiveFile != null) {
            AppProjectUi.LOG
                    .info("Importing project '%s' in workspace '%s'", archiveFile.toString(), projectService.getWorkspace());
            projectService.importProject(archiveFile);
        
        } else {
            AppProjectUi.LOG.info("Import aborted by user.");
        }
        return;
    }

    @objid ("00470342-cc35-1ff2-a7f4-001ec947cd2a")
    @CanExecute
    public boolean canExecute(final IProjectService projectService) {
        return projectService.getOpenedProject() == null;
    }

    @objid ("004703d8-cc35-1ff2-a7f4-001ec947cd2a")
    protected Path promptUserForFile(Shell parentShell, Path workspace) {
        FileDialog dialog = new FileDialog(parentShell, SWT.OPEN);
        
        dialog.setFilterNames(new String[] { AppProjectUi.I18N.getString("ProjectArchive") });
        dialog.setFilterExtensions(new String[] { "*.zip;" });
        
        dialog.setFileName("");
        
        dialog.setFilterPath(workspace.toString());
        
        String sfilePath = dialog.open();
        if (sfilePath != null) {
        
            return Paths.get(sfilePath);
        
        }
        return null;
    }

}
