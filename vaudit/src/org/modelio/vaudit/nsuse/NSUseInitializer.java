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
                                    

package org.modelio.vaudit.nsuse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vaudit.plugin.Vaudit;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.session.api.repository.IRepositorySupport;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * Initialize the namespace uses (blue links) on a project.
 * <p>
 * <li> Creates the namespace uses repository if needed
 * <li> connect the repository to the project
 * <li> Rebuild all namespace uses if needed
 * <li> Register the namespace uses  builder
 */
@objid ("af00fa47-7f7d-4fa8-bd77-007df07af7fc")
public class NSUseInitializer {
    @objid ("213e9173-75eb-47ef-9dc3-134d587bd130")
     IModelioProgressService progressService;

    @objid ("e07d35ed-59d3-417d-b22a-0a8bd2a0bc38")
    private GProject openedProject;

    @objid ("490572b5-1d8f-4083-bbeb-81dd903cefc6")
     StatusReporter statusReporter;

    @objid ("d1a599ee-a189-459b-8a0a-c6fe456e224b")
     ICoreSession coreSession;

    /**
     * Initialize the namespace uses (blue links) on a project.
     * @param openedProject the project
     * @param statusReporter to report errors and warnings
     * @param progressService a progress monitor to report initial blue links building if needed.
     */
    @objid ("aba84c24-c24e-4cd4-b2cd-ede160ad96df")
    public NSUseInitializer(final GProject openedProject, StatusReporter statusReporter, IModelioProgressService progressService) {
        this.openedProject = openedProject;
        this.statusReporter = statusReporter;
        this.progressService = progressService;
        this.coreSession = this.openedProject.getSession();
    }

    /**
     * Initialize the namespace uses (blue links) on a project.
     */
    @objid ("c76ebf7e-d4a4-4edf-89a7-381342b37830")
    public void init() {
        IRepository nsUseRepo = this.coreSession.getRepositorySupport().getRepository(IRepositorySupport.REPOSITORY_KEY_LOCAL);
        
        if (nsUseRepo != null) {
            // Test whether namespace uses have been computed
            NsUseState nsState = new NsUseState(nsUseRepo, this.openedProject.getProperties());
            boolean needRebuild = !nsState.isInitialized(); 
        
            if (needRebuild && !nsUseRepo.findByClass(SmClass.getClass(NamespaceUse.class)).isEmpty()) {
                //TODO to be removed: Ascendent compat: The above test took too much time time
                nsState.setInitialized();
                needRebuild = false;
            }
            
            needRebuild |= projectContentChanged(this.openedProject, nsState);
            
            NSUseUpdater nsUseUpdater = new NSUseUpdater(this.openedProject, nsUseRepo);
            
            // Rebuild blue links if needed
            if (needRebuild) {
                if (!buildNsUses(nsUseUpdater, nsState))
                    nsUseUpdater = null;
                else
                    nsState.setInitialized();
            }
            
            if (nsUseUpdater != null) {
                // Register the namespace uses (blue links) builder on transaction commit
                this.coreSession.getTransactionSupport().setClosureHandler(nsUseUpdater);
                
                // Register repository change listener that updates namespace uses
                NsUseRepositoryChangeListener changesHandler = new NsUseRepositoryChangeListener(nsUseRepo, nsUseUpdater, this.coreSession.getTransactionSupport(), this.progressService, this.statusReporter, nsState);
                this.coreSession.getRepositorySupport().addRepositoryChangeListener(changesHandler);
                this.openedProject.getMonitorSupport().addMonitor(changesHandler);
        
            } 
        }
    }

    @objid ("d7a49f1b-d2fa-4d58-8557-98b8d3165062")
    static String getMsg(IOException e) {
        if (e instanceof FileSystemException)
            return FileUtils.getLocalizedMessage((FileSystemException) e);
        else
            return e.getLocalizedMessage();
    }

    @objid ("ba3442b1-60ca-4267-8c86-07e120ab84d2")
    private boolean buildNsUses(final NSUseUpdater nsUseUpdater, NsUseState nsState) {
        final String title = Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll");
        
        final IRunnableWithProgress runnable = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                try (ITransaction t = NSUseInitializer.this.coreSession.getTransactionSupport().createTransaction(title)){
        
                    nsUseUpdater.rebuildAll(new ModelioProgressAdapter(monitor));
                    t.commit();
                } catch (IOException e) {
                    throw new InvocationTargetException(e, getMsg(e));
                } 
            }
        };
        
        
        final boolean[] ret = new boolean[]{false};
        
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                try {
        
                    NSUseInitializer.this.progressService.run(title , true, true, runnable);
                    ret [0] = true;
                } catch (InterruptedException e) {
                    String msg = Vaudit.I18N.getMessage("ModelShield.NSUseBuildCanceled"); 
                    String msg2 = Vaudit.I18N.getMessage("ModelShield.NSUseBuildCanceled.2");
                    NSUseInitializer.this.statusReporter.show(StatusReporter.WARNING, msg, null, msg2);
                } catch (InvocationTargetException e) {
                    String msg = Vaudit.I18N.getMessage("ModelShield.NSUseBuildFailed"); 
                    String msg2 = Vaudit.I18N.getMessage("ModelShield.NSUseBuildCanceled.2");
                    NSUseInitializer.this.statusReporter.show(StatusReporter.WARNING, msg, e.getCause(), msg2);
                }
            }});
        
        if (ret[0]) {
            // Update the namespace use repository state & record all handled fragments
            nsState.setInitialized();
            nsState.setHandledFragments(getHandledFragmentsIds(this.openedProject));
            // save project descriptor
            try {
                this.openedProject.save(null);
            } catch (IOException e) {
                String msg = Vaudit.I18N.getMessage("ModelShield.NSUseBuildCanceled"); 
                String msg2 = Vaudit.I18N.getMessage("ModelShield.NSUseBuildCanceled.2");
                this.statusReporter.show(StatusReporter.WARNING, msg, e, msg2);
                return false;
            }
        }
        return ret[0];
    }

    /**
     * Test whether the project content is consistent with actually handled fragments.
     * @param proj the project to test
     * @param nsState the namespace use storage state
     * @return true if the namespace uses need to be rebuilt.
     */
    @objid ("dd96cc6f-490d-4f8e-a2fb-3907f7304e02")
    private boolean projectContentChanged(GProject proj, NsUseState nsState) {
        Collection<String> handled = nsState.getHandledFragments();
        Collection<String> projFragmentIds = getHandledFragmentsIds(proj);
        
        if (handled.isEmpty()) {
            //TODO compat : fill the list and tells everything's ok
            nsState.setHandledFragments(projFragmentIds);
            return false;
        } else {
            // Test all fragments are handled
            for (String f : projFragmentIds) {
                if (! handled.contains(f))
                    return true;
            }
            
            // test all handled fragments are still here
            for (String fid : handled) {
                if (!projFragmentIds.contains(fid))
                    return true;
            }
        }
        return false;
    }

    @objid ("811d47a3-e3e1-443b-a97b-5c46f24843f7")
    private List<String> getHandledFragmentsIds(GProject proj) {
        final List<IProjectFragment> ownFragments = proj.getOwnFragments();
        List<String> projFragmentIds = new ArrayList<>(ownFragments.size());
        
        for (IProjectFragment f : ownFragments)
            if (NSUseUtils.isEditableFragment(f))
                projFragmentIds.add(f.getId());
        return projFragmentIds;
    }

}
