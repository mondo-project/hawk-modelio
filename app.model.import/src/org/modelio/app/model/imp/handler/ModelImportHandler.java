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
                                    

package org.modelio.app.model.imp.handler;

import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.progress.IProgressService;
import org.modelio.app.model.imp.impl.ModelImportDataModel;
import org.modelio.app.model.imp.impl.ModelImporter;
import org.modelio.app.model.imp.impl.ui.ImportModelDialog;
import org.modelio.app.model.imp.plugin.AppModelImport;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.module.IModuleCatalog;
import org.modelio.metamodel.analyst.AnalystProject;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.mda.Project;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Handler for the Import command.
 * Only valid on a fragment having a modifiable Project root.
 */
@objid ("a46138fa-c556-4235-82e6-b406b92fc359")
public class ModelImportHandler {
    @objid ("8269f820-4e0c-4406-9a27-dd7757ce9197")
    @Execute
    public void execute(IProjectService projectService, IModuleCatalog moduleCatalog, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, @Named(IServiceConstants.ACTIVE_SHELL) Shell activeShell, IProgressService progressService) {
        IProjectFragment fragment = getSelectedFragment(selection, projectService.getOpenedProject());
        
        Project project = null;
        AnalystProject analystProject = null;
        ModuleComponent localModule = null;
        for (MObject root : fragment.getRoots()) {
            if (root instanceof Project) {
                project = (Project) root;
            } else if (root instanceof AnalystProject) {
                analystProject = (AnalystProject) root;
            } else if (root instanceof ModuleComponent) {
                localModule = (ModuleComponent) root;
            }
        }
        
        ICoreSession session = projectService.getSession();
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Import Model");
                ModelImportDataModel dataModel = new ModelImportDataModel()) {
            if (promptUser(activeShell, moduleCatalog, dataModel) && dataModel.getElementsToImport().size() > 0) {
                // Import the model...
                progressService.run(true, false, new ModelImporter(session, project, analystProject, localModule, dataModel));
        
                transaction.commit();
            } else {
                transaction.rollback();
            }
        } catch (InterruptedException e) {
            // Nothing specific to do: transaction will be rolled back in the finally block.
        } catch (Exception e) {
            AppModelImport.LOG.error(e);
            // Nothing specific to do: transaction will be rolled back in the finally block.
        }
    }

    @objid ("1fc787a6-0741-4477-ba75-b336379b5e8a")
    @CanExecute
    public boolean isEnabled(IProjectService projectService, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        final GProject openedProject = projectService.getOpenedProject();
        if (openedProject == null) {
            return false;
        }
        return getSelectedFragment(selection, openedProject) != null;
    }

    @objid ("025d93f7-99d1-48a9-95d9-5e72ada76c3d")
    private IProjectFragment getSelectedFragment(IStructuredSelection selection, GProject project) {
        if (selection.size() == 1) {
            Object first = selection.getFirstElement();
            if (first instanceof IProjectFragment) {
                return ((IProjectFragment) first);
            } else if (first instanceof Project || first instanceof AnalystProject || first instanceof ModuleComponent) {
                final MObject elt = (MObject) first;
                if ((elt.getStatus().isModifiable() || elt.getStatus().isCmsManaged())) {
                    return project.getFragment(elt);
                }
            }
        }
        return null;
    }

    @objid ("eda08f72-e5c8-475a-9c13-003ec39fc7e0")
    private boolean promptUser(Shell parentShell, IModuleCatalog moduleCatalog, ModelImportDataModel dataModel) {
        ImportModelDialog dialog = new ImportModelDialog(parentShell, dataModel, moduleCatalog);
        
        int code = dialog.open();
        
        if (code == Window.OK) {
            return true;
        } else {
            return false;
        }
    }

}
