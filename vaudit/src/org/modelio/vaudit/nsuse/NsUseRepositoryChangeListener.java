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

import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystemException;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.fragment.FragmentState;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProjectEvent;
import org.modelio.gproject.gproject.IProjectMonitor;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vaudit.plugin.Vaudit;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vbasic.progress.SubProgress;
import org.modelio.vcore.model.CompositionGetter.IStopFilter;
import org.modelio.vcore.model.CompositionGetter;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.session.api.repository.IRepositoryChangeEvent;
import org.modelio.vcore.session.api.repository.IRepositoryChangeListener;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.api.transactions.ITransactionSupport;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;
import org.modelio.vcore.smkernel.meta.SmClass;

/**
 * Repository and project change listener that computes namespace use links
 * for the changed repository elements.
 */
@objid ("b126822d-0b50-465b-8e1a-65fb24707090")
class NsUseRepositoryChangeListener implements IRepositoryChangeListener, IProjectMonitor {
    @objid ("b0392c8f-2ee8-4255-b745-6253f52a2dc5")
    private final IModelioProgressService progressService;

    @objid ("f9a8a2fe-3ab7-443e-b607-c3cb235901e7")
    private final IRepository nsUseRepo;

    @objid ("1dfe0f1f-eccd-4cab-95dd-23aa5db39a23")
     final NSUseUpdater updater;

    @objid ("3aeb1069-f3bc-4248-b8c5-f5671e8263a1")
     final ITransactionSupport tm;

    @objid ("b9e2b29c-2e09-496d-b78d-459b34f3b9e0")
    private final StatusReporter statusReporter;

    @objid ("6662d1b5-d818-43ed-af3c-3641b93e7340")
    private final NsUseState state;

    @objid ("997bc160-2e7f-4e0e-b603-dce94830f7c1")
    NsUseRepositoryChangeListener(IRepository nsUseRepo, NSUseUpdater updater, ITransactionSupport tm, IModelioProgressService progressService, StatusReporter statusReporter, NsUseState state) {
        this.nsUseRepo = nsUseRepo;
        this.updater = updater;
        this.tm = tm;
        this.progressService = progressService;
        this.statusReporter = statusReporter;
        this.state = state;
    }

    @objid ("8b903647-6818-4330-8221-3fcef837c29a")
    @Override
    public void repositoryChanged(IRepositoryChangeEvent event) {
        if (event.getRepository() == this.nsUseRepo)
            return;
        
        switch (event.getGranularity()) {
        case CMSNODE:
            processCmsNodeEvent(event);
            break;
        case OBJECT:
            processObjectEvent(event);
            break;
        case UNDEFINED:
            processUndefinedEvent(event);
            break;
        default:
            break;
        
        }
    }

    @objid ("e1dee410-e9d3-4383-8de9-f439efe764fd")
    private void processUndefinedEvent(final IRepositoryChangeEvent event) {
        final String jobName = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Updating");
        final IRepository repo = event.getRepository();
        IRunnableWithProgress job = new RebuildUsesForRepository(jobName, repo);
        
        runJob(job, jobName);
    }

    @objid ("a09920c6-a2d7-48d6-94b5-b65d90b62928")
    private void processObjectEvent(final IRepositoryChangeEvent event) {
        final String jobName = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Updating");
        IRunnableWithProgress job = new IRunnableWithProgress() {
        
            @Override
            public void run(IProgressMonitor monitor) {
                final SubProgress amonitor = ModelioProgressAdapter.convert(monitor, jobName, 6);
                
                Collection<Element> toRebuild = new HashSet<>();
        
                IRepository repo = event.getRepository();
                for (MRef ref : event.getCreatedElements()) {
                    MObject obj = findByRef(repo, ref);
                    if (obj instanceof Element)
                        toRebuild.add((Element) obj);
                }
                for (MRef ref : event.getModifiedElements()) {
                    MObject obj = findByRef(repo, ref);
                    if (obj instanceof Element)
                        toRebuild.add((Element) obj);
                }
        
                amonitor.worked(1);
                if (toRebuild.isEmpty())
                    return;
        
                final String label = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.UpdatingN", String.valueOf(toRebuild.size()));
                monitor.setTaskName(label);
        
                try (ITransaction t = NsUseRepositoryChangeListener.this.tm.createTransaction(label, 20, TimeUnit.SECONDS)){
                    NsUseRepositoryChangeListener.this.updater.rebuild(toRebuild, amonitor.newChild(3));
        
                    amonitor.setTaskName(Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Cleaning"));
                    NsUseRepositoryChangeListener.this.updater.cleanNamespaceUses(amonitor.newChild(2));
                    
                    t.commit();
                }
            }
        };
        
        runJob(job, jobName);
    }

    @objid ("b2d143c8-c06b-4a0c-822c-cc838522ccae")
    static MObject findByRef(IRepository repo, MRef ref) {
        final SmClass cls = SmClass.getClass(ref.mc);
        if (cls == null)
            return null;
        return repo.findById(cls, ref.uuid);
    }

    @objid ("c9c8b246-2967-4be1-8c98-16825d236251")
    private void processCmsNodeEvent(final IRepositoryChangeEvent event) {
        final String jobName = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Updating");
        final IRunnableWithProgress job = new IRunnableWithProgress() {
            
            @Override
            public void run(IProgressMonitor monitor) {
                final SubProgress amonitor = ModelioProgressAdapter.convert(monitor, jobName, 6);
        
                Collection<Element> toRebuild = new HashSet<>();
                Collection<SmObjectImpl> smToRebuild = new HashSet<>();
        
                IRepository repo = event.getRepository();
                for (MRef ref : event.getCreatedElements()) {
                    MObject obj = findByRef(repo, ref);
                    if (obj instanceof Element) {
                        smToRebuild.add((SmObjectImpl) obj);
                    }
                }
                for (MRef ref : event.getModifiedElements()) {
                    MObject obj = findByRef(repo, ref);
                    if (obj instanceof Element)
                        smToRebuild.add((SmObjectImpl) obj);
                }
        
                for (SmObjectImpl obj : CompositionGetter.getAllChildren(smToRebuild, NonCmsNodeFilter.instance)) {
                    if (obj instanceof Element)
                        toRebuild.add((Element) obj);
                }
                
                for (SmObjectImpl cmsNode : smToRebuild) {
                    if (cmsNode instanceof Element)
                        toRebuild.add((Element) cmsNode);
                }
                
                amonitor.worked(1);
                if (toRebuild.isEmpty())
                    return;
                
                
                final String label = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.UpdatingN", String.valueOf(toRebuild.size()));
                amonitor.setTaskName(label);
        
                try (ITransaction t = NsUseRepositoryChangeListener.this.tm.createTransaction(label, 20, TimeUnit.SECONDS)){
                    NsUseRepositoryChangeListener.this.updater.rebuild(toRebuild, amonitor.newChild(3));
        
                    amonitor.subTask(Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Cleaning"));
                    NsUseRepositoryChangeListener.this.updater.cleanNamespaceUses(amonitor.newChild(2));
                    
                    t.commit();
                }
                
            }
        };
        
        runJob(job, jobName);
    }

    @objid ("7e144f5a-b5c7-422a-a4c4-de9c5817c44c")
    private void runJob(final IRunnableWithProgress job, final String title) {
        final IModelioProgressService progressSvc = this.progressService;
        final StatusReporter reporter = this.statusReporter;
        Display.getDefault().syncExec(new Runnable() {
            
            @Override
            public void run() {
                try {
                    progressSvc.run(title, true, false, job);
                } catch (InvocationTargetException e) {
                    Vaudit.LOG.error(e);
                    String message = e.getCause().getLocalizedMessage();
                    if (e.getCause() instanceof FileSystemException)
                        message = FileUtils.getLocalizedMessage((FileSystemException) e.getCause());
        
                    message = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.UpdateFailed", message);
                    reporter.show(StatusReporter.ERROR, message, e.getCause());
                } catch (InterruptedException e) {
                    Vaudit.LOG.error(e);
                } catch (RuntimeException e) {
                    Vaudit.LOG.error(e);
                    
                    
                    String message = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.UpdateFailed", e.toString());
                    reporter.show(StatusReporter.ERROR, message, e.getCause());
                }
            }
        });
    }

    @objid ("9939ac59-c482-4cd4-8c0b-08bb65aee2ef")
    @Override
    public void handleProjectEvent(GProjectEvent ev) {
        switch (ev.type) {
        case FRAGMENT_STATE_CHANGED:
            if (ev.fragment.getState() == FragmentState.UP_FULL)
                onFragmentAdded(ev.fragment);
            break;
        case FRAGMENT_ADDED:
            onFragmentAdded(ev.fragment);
            break;
            
        case FRAGMENT_REMOVED:
            onFragmentRemoved(ev.fragment);
            break;
        default:
        }
    }

    @objid ("62e74bca-32c9-48f5-a9fa-430f07c67c0c")
    private void onFragmentAdded(IProjectFragment newFragment) {
        if (newFragment.getState() != FragmentState.UP_FULL)
            return;
        if (! NSUseUtils.isEditableFragment(newFragment))
            return;
        
        final Collection<String> handledFragments = this.state.getHandledFragments();
        if (handledFragments.contains(newFragment.getId()))
            return;
        
        final String jobName = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Updating");
        final IRepository repo = newFragment.getRepository();
        IRunnableWithProgress job = new RebuildUsesForRepository(jobName, repo);
        
        runJob(job, jobName);
        
        handledFragments.add(newFragment.getId());
        this.state.setHandledFragments(handledFragments);
    }

    /**
     * Called when a fragment is removed.
     * @param removedFragment the removed fragment
     */
    @objid ("0672e0e6-3b5a-422b-b15d-5793ad871835")
    private void onFragmentRemoved(IProjectFragment removedFragment) {
        if (! NSUseUtils.isEditableFragment(removedFragment))
            return;
        
        final Collection<String> handledFragments = this.state.getHandledFragments();
        if (! handledFragments.contains(removedFragment.getId()))
            return;
        
        
        final String jobName = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Cleaning");
        final IRunnableWithProgress job = new IRunnableWithProgress() {
            
            @Override
            public void run(IProgressMonitor monitor) {
                final SubProgress amonitor = ModelioProgressAdapter.convert(monitor, jobName, 6);
        
                try (ITransaction t = NsUseRepositoryChangeListener.this.tm.createTransaction(jobName, 20, TimeUnit.SECONDS)){
                    NsUseRepositoryChangeListener.this.updater.cleanNamespaceUses(amonitor);
                    
                    t.commit();
                }
            }
        };
        
        runJob(job, jobName);  
        
        handledFragments.remove(removedFragment.getId());
        this.state.setHandledFragments(handledFragments);
    }

    @objid ("5d036d6e-46fe-491f-a061-d0ea279d9f51")
    private static class NonCmsNodeFilter implements IStopFilter {
        @objid ("f04083c3-1484-427e-a97b-53f01aa165b2")
        public static final NonCmsNodeFilter instance = new NonCmsNodeFilter();

        @objid ("5edc4754-b3b9-47f0-a6c1-ea4275950ca1")
        @Override
        public boolean accept(SmObjectImpl child) {
            return ! child.getClassOf().isCmsNode();
        }

    }

    @objid ("ab08209b-c074-463c-aa1b-16c2b801ad64")
    private final class RebuildUsesForRepository implements IRunnableWithProgress {
        @objid ("79e45187-1553-4784-89e9-ab570f31c3c9")
        private final String jobName;

        @objid ("d587e922-770d-433b-8e6d-b03dda6e8c60")
        private final IRepository repo;

        @objid ("6601f654-0f64-455d-a954-a4623bd46782")
        public RebuildUsesForRepository(String jobName, IRepository repo) {
            this.jobName = jobName;
            this.repo = repo;
        }

        @objid ("970b66d2-c345-47c6-954c-2af6b7c7dfeb")
        @Override
        public void run(IProgressMonitor monitor) {
            final SubProgress amonitor = ModelioProgressAdapter.convert(monitor, this.jobName, 6);
            
            final Collection<MObject> all = this.repo.findByClass(SmClass.getClass(Element.class));
            Collection<Element> toRebuild  = new CastedCollection<>(all);
            
            amonitor.worked(1);
            if (toRebuild.isEmpty())
                return;
                  
            final String label = Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.UpdatingN", String.valueOf(toRebuild.size()));
            monitor.setTaskName(label);
                  
            try (ITransaction t = NsUseRepositoryChangeListener.this.tm.createTransaction(label, 20, TimeUnit.SECONDS)){
                NsUseRepositoryChangeListener.this.updater.rebuild(toRebuild, amonitor.newChild(3));
                  
                amonitor.setTaskName(Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.Cleaning"));
                NsUseRepositoryChangeListener.this.updater.cleanNamespaceUses(amonitor.newChild(2));
                
                t.commit();
            }
        }

    }

    @objid ("efe2626e-7efb-4446-9482-c343f3f6caaf")
    private static final class CastedCollection<T> extends AbstractCollection<T> {
        @objid ("9a3eae6c-75af-4f6c-a8ca-d1e97394c520")
         final Collection<MObject> all;

        @objid ("31d09a7c-b5c0-44c1-9822-6e994523a642")
        public CastedCollection(Collection<MObject> all) {
            this.all = all;
        }

        @objid ("5d81f709-9fc1-4765-9b37-8241bf6c54c4")
        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private Iterator<MObject> it = CastedCollection.this.all.iterator();
                
                @Override
                public void remove() {
                    this.it.remove();
                }
                
                @SuppressWarnings("unchecked")
                @Override
                public T next() {
                    return (T) this.it.next();
                }
                
                @Override
                public boolean hasNext() {
            return this.it.hasNext();
                            }
                        };
        }

        @objid ("46a1e755-f0f9-4fd4-8877-922e0c435e6f")
        @Override
        public int size() {
            return this.all.size();
        }

    }

}
