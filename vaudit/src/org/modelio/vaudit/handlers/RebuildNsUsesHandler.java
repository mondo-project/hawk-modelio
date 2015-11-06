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
                                    

package org.modelio.vaudit.handlers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vaudit.nsuse.NSUseUpdater;
import org.modelio.vaudit.plugin.Vaudit;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.session.api.repository.IRepositorySupport;
import org.modelio.vcore.session.api.transactions.ITransaction;

@objid ("1aed6f05-8ac3-4496-a0e4-c5820098c56e")
public class RebuildNsUsesHandler implements IRunnableWithProgress {
    @objid ("d6993b79-1ec9-47eb-8b57-e8b113781ef8")
    private GProject project;

    @objid ("c8b1afb8-b8b8-49b2-915b-5b8696bf4c59")
    @Execute
    void execute(@Optional
@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection sel, IModelioProgressService progressSvc, StatusReporter reporter) {
        IProjectFragment frag = (IProjectFragment) sel.iterator().next();
        this.project = GProject.getProject(frag.getRoots().iterator().next());
        
        try {
            String title = Vaudit.I18N.getString("NSUseUpdater.RebuildAll");
            progressSvc.run(title, true, true, this);
        } catch (InvocationTargetException e) {
            Vaudit.LOG.error(e);
            reporter.show(StatusReporter.ERROR, e.getLocalizedMessage(), e.getCause());
        } catch (InterruptedException e) {
            Vaudit.LOG.error(e);
        }
    }

    @objid ("0eae160b-f990-4229-a312-ec664c2e76bf")
    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        IRepository nsUseRepo = this.project.getSession().getRepositorySupport().getRepository(IRepositorySupport.REPOSITORY_KEY_LOCAL);
        String title = Vaudit.I18N.getString("NSUseUpdater.RebuildAll");
        try (ITransaction t= this.project.getSession().getTransactionSupport().createTransaction(title)){
            new NSUseUpdater(this.project, nsUseRepo).rebuildAll(new ModelioProgressAdapter(monitor));
            t.commit();
        } catch (IOException e) {
            throw new InvocationTargetException(e, e.getLocalizedMessage());
        }
    }

    @objid ("b0439cd3-934a-4c44-bd25-adb96b559a82")
    @CanExecute
    boolean canExecute() {
        return true;
    }

}
