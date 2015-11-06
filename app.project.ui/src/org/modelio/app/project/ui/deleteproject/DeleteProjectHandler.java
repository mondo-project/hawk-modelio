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
                                    

package org.modelio.app.project.ui.deleteproject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.gproject.data.project.ProjectDescriptor;

/**
 * Delete a project.
 */
@objid ("0044a250-cc35-1ff2-a7f4-001ec947cd2a")
public class DeleteProjectHandler {
    @objid ("0046fe60-cc35-1ff2-a7f4-001ec947cd2a")
    @Execute
    public void execute(final IProjectService projectService, @Named(IServiceConstants.ACTIVE_SHELL) final Shell shell, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        List<ProjectDescriptor> projectDescriptors = getSelectedElements(selection);
        StringBuilder nameList = new StringBuilder();
        for (ProjectDescriptor projectDescriptor : projectDescriptors) {
            nameList.append(" - ");
            nameList.append(projectDescriptor.getName());
            nameList.append("\n");
        }
        if (MessageDialog.openConfirm(shell, AppProjectUi.I18N.getString("ConfirmProjectDeletion"),
                                        AppProjectUi.I18N.getMessage("ConfirmProjectDeletionMessage", nameList.toString()))) {
            for (ProjectDescriptor projectDescriptor : projectDescriptors) {
                AppProjectUi.LOG.info("Deleting project '%s' ", projectDescriptor.getName());
                
                try {
                    projectService.deleteProject(projectDescriptor);
                } catch (IOException e) {
                    AppProjectUi.LOG.equals(e);
                }     
            }
        }
    }

    @objid ("0046fef6-cc35-1ff2-a7f4-001ec947cd2a")
    @CanExecute
    public boolean canExecute(final IProjectService projectService, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        if (selection == null) {
            return false;
        }
        
        List<ProjectDescriptor> projectDescriptors = getSelectedElements(selection);
        for (ProjectDescriptor descriptor : projectDescriptors) {
            // cannot delete currently opened project
            if (descriptor.getLockInfo() != null)
                return false;
        }
        return true;
    }

    @objid ("cb8a364c-7aae-4f90-bee1-c69cb9e8ab53")
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
