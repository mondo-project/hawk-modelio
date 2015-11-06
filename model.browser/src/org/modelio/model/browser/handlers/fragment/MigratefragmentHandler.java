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
                                    

package org.modelio.model.browser.handlers.fragment;

import java.lang.reflect.InvocationTargetException;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.core.services.FragmentsMigrator;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.fragment.FragmentMigrationNeededException;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.model.browser.plugin.ModelBrowser;
import org.modelio.ui.progress.IModelioProgressService;

/**
 * Handler that migrates selected fragments.
 */
@objid ("a9e8696c-5c1e-4411-9e71-d9865eb9fe41")
public class MigratefragmentHandler {
    @objid ("685c26f0-4347-4477-b706-caf446a85819")
    @Execute
    void execute(IModelioProgressService progressService, @Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection, StatusReporter statusReporter, final IProjectService projServ, @Named(IServiceConstants.ACTIVE_SHELL) final Shell parentShell) {
        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                String taskName = ModelBrowser.I18N.getMessage("MigrateFragmentHandler.monitor.title", selection.size());
                SubMonitor parentMon = SubMonitor.convert(monitor, taskName, selection.size() * 10);
                for (Object o : selection.toArray()) {
                    if (o instanceof IProjectFragment) {
                        IProjectFragment f = (IProjectFragment) o;
                        new FragmentsMigrator(projServ.getOpenedProject()).migrateFragment(f, parentShell, parentMon, 10);
                    }
                }
            }
        };
        
        
        try {
            progressService.run(true, false, runnable);
        } catch (InvocationTargetException e) {
            ModelBrowser.LOG.error(e);
            String msg = ModelBrowser.I18N.getMessage("MigrateFragmentHandler.error.msg", e.getCause().toString());
            statusReporter.show(StatusReporter.ERROR, msg, e.getCause());
        } catch (InterruptedException e) {
            ModelBrowser.LOG.info("Migration cancelled");
            ModelBrowser.LOG.debug(e);
        }
    }

    @objid ("1c0662e6-22a2-4505-a64a-86c446eacd2e")
    @CanExecute
    boolean isVisible(@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        for (Object o : selection.toArray()) {
            if (o instanceof IProjectFragment) {
                IProjectFragment f = (IProjectFragment) o;
                if( !(f.getDownError() instanceof FragmentMigrationNeededException))
                    return false;
            } else
                return false;
        }
        return true;
    }

}
