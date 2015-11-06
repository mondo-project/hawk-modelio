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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.NamespaceUse;
import org.modelio.vaudit.plugin.Vaudit;
import org.modelio.vbasic.progress.IModelioProgress;
import org.modelio.vbasic.progress.SubProgress;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.repository.IRepository;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.api.transactions.ITransactionClosureHandler;
import org.modelio.vcore.session.impl.transactions.Transaction;
import org.modelio.vcore.session.impl.transactions.smAction.AppendDependencyAction;
import org.modelio.vcore.session.impl.transactions.smAction.CreateElementAction;
import org.modelio.vcore.session.impl.transactions.smAction.DeleteElementAction;
import org.modelio.vcore.session.impl.transactions.smAction.EraseDependencyAction;
import org.modelio.vcore.session.impl.transactions.smAction.IAction;
import org.modelio.vcore.session.impl.transactions.smAction.MoveDependencyAction;
import org.modelio.vcore.session.impl.transactions.smAction.SetAttributeAction;
import org.modelio.vcore.session.impl.transactions.smAction.smActionInteractions.IActionVisitor;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MDependency;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.meta.SmClass;
import org.modelio.vcore.smkernel.meta.SmDependency;

/**
 * Blue links builder.
 * <p>
 * Can :
 * <li>build blue links from a transaction with {@link #commit(ITransaction)}
 * <li>rebuild all blue links with {@link #rebuildAll(IModelioProgress)}
 */
@objid ("8c7fc428-4165-4945-ada3-0c16bd1fff96")
public class NSUseUpdater implements ITransactionClosureHandler {
    @objid ("d19584e1-9e7a-4307-8081-0a4a7e837e7d")
    private long transactionId;

    @objid ("a2f8a93b-66c4-4802-ae7d-be5dd21c1069")
    private NSUseAnalyser useBuilder;

    @objid ("f7786243-8b2b-4609-8ed3-2347cb25dfce")
    private Set<NamespaceUse> usesToRebuild;

    @objid ("5a7e2b55-65cc-468f-b734-a1c064c29998")
    private ICoreSession session;

    @objid ("2b6d7c99-19fd-438c-a495-d11098237772")
    private ActionVisitor actionVisitor;

    @objid ("c18364ee-9c95-4aae-8530-3c8ab5fd90e3")
    private IRepository nsUseRepo;

    @objid ("d3d5801f-80bd-45d9-9727-e153091c6aa7")
    private GProject gproject;

    /**
     * Update the NS Use model (aka blue links) based on the passed transaction
     * contents
     */
    @objid ("9a73b617-b8c3-4a02-8fde-3f0af66ee94a")
    @Override
    public void commit(ITransaction transaction) {
        // Analyse the transaction to get the list of object to process
        //if (Vaudit.LOG.isDebugEnabled()) {
        //    Vaudit.LOG.debug("--- NSUseUpdater BEGIN ----------------------------------------------------");
        //    Vaudit.LOG.debug("1 - analysing transaction");
        //}
        
        this.actionVisitor.reset();
        ((Transaction) transaction).accept(this.actionVisitor);
        List<MObject> objsToRebuild = new ArrayList<>(this.actionVisitor.getResults());
        
        //Vaudit.LOG.debug("\tthere are %d objects to process.", objsToRebuild.size());
        
        // Build the NSuse
        //if (Vaudit.LOG.isDebugEnabled())
        //    Vaudit.LOG.debug("2 - building NamespaceUses");
        //int i = 1;
        for (MObject o : objsToRebuild) {
            //if (Vaudit.LOG.isDebugEnabled())
            //    Vaudit.LOG.debug("\t- processing object %d/%d -> '%s' %s ", i, objsToRebuild.size(), o.getName(), o.getMClass().getName());
            if (o.isValid() && o instanceof Element) {
                NSUseUtils.dereferenceNSUsesCausedBy((Element) o);
                this.useBuilder.buildFor((Element) o);
            }
            //i++;
        }
        //if (Vaudit.LOG.isDebugEnabled())
        //    Vaudit.LOG.debug("--- NSUseUpdater END ----\n");
        
        // Reset visitors (facilitate garbage)
        this.actionVisitor.reset();
    }

    /**
     * Initialize the blue links updater.
     * @param gproject the project
     * @param nsUseRepo the repository where blue links are stored.
     */
    @objid ("b7491a5f-a84c-4e34-a04f-5f3b69a6bb33")
    public NSUseUpdater(GProject gproject, IRepository nsUseRepo) {
        this.gproject = gproject;
        this.session = gproject.getSession();
        this.actionVisitor = new ActionVisitor(this);
        this.nsUseRepo = nsUseRepo;
        NsUseCreateHandler handler = new NsUseCreateHandler(this.session.getModel().getGenericFactory(), nsUseRepo);
        this.useBuilder = new NSUseAnalyser(handler);
    }

    /**
     * Rebuild namespace use links caused by the given model objects.
     * @param toRebuild the given model objects whose namespace uses must be rebuilt.
     * @param amonitor a progress monitor
     */
    @objid ("957943cd-259e-40f0-8acb-86883f7e9d93")
    public void rebuild(Collection<Element> toRebuild, IModelioProgress amonitor) {
        SubProgress m = SubProgress.convert(amonitor, toRebuild.size());
        for (Element el : toRebuild) {
            NSUseUtils.dereferenceNSUsesCausedBy(el);
            this.useBuilder.buildFor(el);
            m.worked(1);
        }
    }

    @objid ("6ab676b3-c9e0-4487-aca1-f2303dcfb30f")
    private boolean isOwnedBy(MObject obj, MObject owner) {
        MObject parent = obj;
        while (parent != null && parent != owner)
            parent = parent.getCompositionOwner();
        return (parent == owner);
    }

    /**
     * Computes objects whose blue links must be refreshed after a move.
     * <p>
     * Theory: Let's call "the NSUs of object AAA" (or AAA's NSUs)
     * the NSU objects where AAA is either the source of the target.
     * When 'movedObj' is moved:
     * <ol>
     * 
     * <li> the NSU of its composed objects remain valid unless the next conditions occur.
     * 
     * <li> the NSUs between the 'oldOwner' and the common owner of both
     * the old and new owner have to be rebuilt if their causes
     * contain any of the moved object composed objects.
     * 
     * <li> if 'movedObj' is a NameSpace, the NSUs caused by any of
     * the movedObj's NSUs are invalid and have to be rebuild
     * @param movedObj the moved obj
     * @param oldOwner the old parent
     * @return the list of objects whose blue links must be recomputed
     */
    @objid ("01fa8b9d-923b-472e-864d-50e19d41fd8b")
    List<MObject> onObjectMovedFrom(MObject movedObj, MObject oldOwner) {
        // Protection
        if (movedObj == null || oldOwner == null)
            return Collections.emptyList();
        
        // Theory:
        // Let's call "the NSUs of object AAA" (or AAA's NSUs) the NSU objects
        // where AAA is either the source of the target.
        // When 'movedObj' is moved:
        //
        // 1- the NSU of its composed objects remain valid unless the next
        // conditions occur.
        //
        // 2- the NSUs between the 'oldOwner' and the common owner of both the
        // old and new owner have to be rebuilt if their causes contain any of
        // the moved object composed objects.
        //
        // 3- if 'movedObj' is a NameSpace,
        // the NSUs caused by any of the movedObj's NSUs are invalid and have to
        // be rebuild
        
        List<MObject> objsToRebuild = new ArrayList<>();
        
        NameSpace oldNameSpaceOwner = null;
        NameSpace newNameSpaceOwner = NSUseUtils.getNameSpaceOwner(movedObj);
        
        if (oldOwner instanceof NameSpace)
            oldNameSpaceOwner = (NameSpace) oldOwner;
        else
            oldNameSpaceOwner = NSUseUtils.getNameSpaceOwner(oldOwner);
        
        if (oldNameSpaceOwner != null) {
            // Get all namespaces between the old owner and the common root
            // of the old and new owner.
            Deque<NameSpace> nameSpacesToCheck = new ArrayDeque<>();
            Deque<NameSpace> newPath = new ArrayDeque<>(); // parameter not used
            NSUseUtils.getRelativePathsFromCommonRoot(oldNameSpaceOwner, newNameSpaceOwner, nameSpacesToCheck, newPath);
        
            // add the new owner
            if (newNameSpaceOwner != null)
                nameSpacesToCheck.add(newNameSpaceOwner);
        
            // getAllNSUses ( oldOwners, uses);
            for (NameSpace ns : nameSpacesToCheck) {
                // Get all starting and coming NSUses
                List<NamespaceUse> uses = new ArrayList<>();
                uses.addAll(ns.getUserNsu());
                uses.addAll(ns.getUsedNsu());
        
                // Get all NSUses causes
                List<Element> causes = new ArrayList<>(uses.size() * 8);
                for (NamespaceUse nsu : uses) {
                    causes.addAll(nsu.getCause());
                }
        
                // Get causes who:
                // - are owned by the 'movedObj',
                // - or have a dependency to at least an element owned by
                // 'movedObj'
                // These causes are to be rebuilt.
                for (Element cause : causes) {
                    if (isOwnedBy(cause, movedObj)) {
                        // the cause is owned by the moved object
                        objsToRebuild.add(cause);
                    } else {
                        // Get all reference dependencies in order to get their
                        // values:
                        // if one dependency of the cause is owned by the moved
                        // object,
                        // some of its caused NSUses may be invalid so they must
                        // all be rebuilt.
                        List<SmDependency> deps = ((SmClass) cause.getMClass()).getAllReferenceDepDef();
                        // int nbdeps = deps.size();
        
                        boolean found = false;
        
                        for (SmDependency dep : deps) {
                            // ignore dynamic dependencies
                            if (!dep.isDynamic()) {
                                // iterate the dependency values
                                List<MObject> refs = cause.mGet(dep);
                                for (MObject ref : refs) {
                                    if (isOwnedBy(ref, movedObj)) {
                                        found = true;
                                        break;
                                    }
                                }
                            }
                            if (found)
                                break;
                        }
                        if (found) {
                            // at least one dependency of the cause is owned by
                            // the moved object
                            objsToRebuild.add(cause);
                        }
                    }
                } // for (icause ...)
            } // for (iowner ...)
        }
        return objsToRebuild;
    }

    @objid ("0b868718-f62a-4d5a-b499-93e34d2c9c0e")
    private void rebuildNSUse(NamespaceUse use) {
        // 1) dereference caused uses
        NSUseUtils.dereferenceNSUsesCausedBy(use);
        
        // 2) rebuild caused uses
        this.useBuilder.visitNamespaceUse(use);
    }

    /**
     * Delete and Rebuild all "blue links"
     * @param aMonitor the progress monitor to use for reporting progress to the
     * user. It is the caller's responsibility to call done() on the
     * given monitor. Accepts null, indicating that no progress
     * should be reported and that the operation cannot be cancelled.
     * @throws java.lang.InterruptedException if the user cancelled the operation
     * @throws java.io.IOException if the save fails.
     */
    @objid ("acb2b2d2-143c-4173-b236-74d6b70adb60")
    public void rebuildAll(IModelioProgress aMonitor) throws IOException, InterruptedException {
        SubProgress mainMon = SubProgress.convert(aMonitor, Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll"), 10);
        SubProgress mon;
        
        // Get all existing NSUses
        mainMon.subTask(Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll.GetBLToDelete"));
        List<NamespaceUse> uses = new ArrayList<>(this.session.getModel().findByClass(NamespaceUse.class, null));
        mainMon.worked(1);
        if (mainMon.isCanceled())
            throw new InterruptedException();
        
        // Delete all existing NSUses
        mon = SubProgress.convert(mainMon.newChild(1), uses.size());
        mon.subTask(Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll.DeleteBL", String.valueOf(uses.size())));
        for (MObject o : uses) {
            o.delete();
        
            mon.worked(1);
            if (mainMon.isCanceled())
                throw new InterruptedException();
        }
        
        // Get all objects
        mainMon.subTask(Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll.GettingAllObjects"));
        List<MObject> allObjs = new ArrayList<>(findAllEditableObjects());
        assert (dump(allObjs));
        mainMon.worked(1);
        
        // Build blue links from all objects
        mon = SubProgress.convert(mainMon.newChild(6), allObjs.size());
        int i = 0;
        String allObjscount = String.valueOf(allObjs.size());
        for (MObject obj : allObjs) {
            if (obj.isValid() && !obj.isShell() && !obj.getStatus().isRamc())
                obj.accept(this.useBuilder);
        
            mon.worked(1);
            i++;
            if (mainMon.isCanceled())
                throw new InterruptedException();
            
            if (i % 5 == 0)
                mon.subTask(Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll.ComputeBL", allObjscount, String.valueOf(i)));
        }
        
        mon.setTaskName(Vaudit.I18N.getMessage("NSUseUpdater.RebuildAll.Saving", allObjscount));
        this.session.save(mainMon.newChild(1));
    }

    @objid ("e3b8f3db-1cd6-47cd-abbe-48b7ae5e9e02")
    private boolean dump(List<MObject> allObjs) {
        List<SmClass> classes = SmClass.getRegisteredClasses();
        int nbClasses = classes.size();
        long[] count = new long[nbClasses];
        
        for (MObject obj : allObjs) {
            short clid = ((SmObjectImpl) obj).getClassOf().getId();
            count[clid]++;
        }
        
        StringBuilder s = new StringBuilder();
        s.append("Project content:\n");
        for (short i = 0; i < nbClasses; i++) {
            SmClass cls = SmClass.getClass(i);
            if (!cls.isAbstract()) {
                s.append(cls.getName());
                s.append(":");
                s.append(count[i]);
                s.append("\n");
            }
        }
        //if (Vaudit.LOG.isDebugEnabled())
        //    Vaudit.LOG.debug(s.toString());
        return true;
    }

    /**
     * Check all namespace use links and deletes all that have no valid source, destination or cause.
     * <p>
     * A model transaction must be open.
     * @param amonitor a progress monitor
     */
    @objid ("e2ddf265-99ae-4ec0-aac5-4763334e0b23")
    public void cleanNamespaceUses(IModelioProgress amonitor) {
        final Collection<MObject> nsuses = this.nsUseRepo.findByClass(SmClass.getClass(NamespaceUse.class));
        final int nbuses = nsuses.size();
        final SubProgress m = SubProgress.convert(amonitor, nbuses);
        int i=0;
        
        for (MObject obj : nsuses) {
            NamespaceUse u = (NamespaceUse)obj;
            if (u.isDeleted()) {
                // ignore
            } else if (isToDelete(u)) {
                NSUseUtils.dereferenceNSUsesCausedBy(u);
                u.delete();
            } else for (Iterator<Element> it= u.getCause().iterator(); it.hasNext();) {
                Element c = it.next();
                if (!c.isValid())
                    it.remove();
            }
            
            if (++i % 11 == 1) {
                m.worked(11);
                m.subTask(Vaudit.I18N.getMessage("NsUseRepositoryChangeListener.CleaningN", String.valueOf(i), String.valueOf(nbuses)));
            }
        }
    }

    @objid ("bdf4fad6-5544-482d-a5b6-cf66af9bf56b")
    private static boolean isToDelete(NamespaceUse u) {
        // Test the source and destination are valid
        final NameSpace user = u.getUser();
        final NameSpace used = u.getUsed();
        if (user == null || !user.isValid() )
            return true;
        
        if (used == null || !used.isValid())
            return true;
        
        // The use is valid if any cause is valid
        for (Element c : u.getCause())
            if (c.isValid())
                return false;
        
        // No valid cause found
        return true;
    }

    /**
     * Get all model objects in the project excepted RAMC elements.
     * @return all non RAMC objects.
     */
    @objid ("19ab1276-0b88-4cb8-89ab-1db7dc96af52")
    private Collection<MObject> findAllEditableObjects() {
        final SmClass MOBJECT_CLASS = SmClass.getClass(MObject.class);
        Collection<MObject> ret = new ArrayList<>();
        
        for (IProjectFragment f : this.gproject.getFragments()) {
            boolean isRamc = true;
            for (MObject obj : f.getRoots()) {
                isRamc = obj.getStatus().isRamc();
                if (! isRamc) 
                    break;
            }
            
            if (! isRamc) 
                ret.addAll(f.getRepository().findByClass(MOBJECT_CLASS));
            
        }
        return ret;
    }

    @objid ("e986ecdc-174d-467b-919e-700848409f13")
    static class ActionVisitor implements IActionVisitor {
        @objid ("1169c2a0-9067-4183-95de-83c13c944e89")
        private final MClass Element_MClass = Metamodel.getMClass(Element.class);

        @objid ("8b3d271d-421e-422d-8598-cbc4656103e4")
        private final MClass NamespaceUse_MClass = Metamodel.getMClass(NamespaceUse.class);

        @objid ("e1190042-c3aa-4aae-8144-40d0aa952868")
        private final MClass NameSpace_MClass = Metamodel.getMClass(NameSpace.class);

        @objid ("70bbd527-ad2d-4fa4-a45b-e21638ef96b8")
        private final MDependency NameSpace_user_NamespaceUse = this.NameSpace_MClass.getDependency("UserNsu");

        @objid ("6dd5f822-8a10-451d-9c7c-d4217dbf1715")
        private final MDependency NameSpace_used_NamespaceUse = this.NameSpace_MClass.getDependency("UsedNsu");

        @objid ("9bab6d0c-50f8-4f3f-a384-7fdab6e0cce9")
        private final MDependency NamespaceUse_cause_Element = this.NamespaceUse_MClass.getDependency("Cause");

        @objid ("f57158ce-f8e1-45a8-9596-314894d8f75e")
        private final MDependency NamespaceUse_user_NameSpace = this.NamespaceUse_MClass.getDependency("User");

        @objid ("73569436-1acb-4e55-833a-c5d563ea65ea")
        private final MDependency NamespaceUse_used_NameSpace = this.NamespaceUse_MClass.getDependency("Used");

        @objid ("dfaeeeff-68d4-481f-80bd-9f467e743861")
        private final MDependency Element_causing_NamespaceUse = this.Element_MClass.getDependency("Causing");

        @objid ("d87deada-b134-41a9-a9bd-28c9780c9641")
        private Set<MObject> objsToRebuild = new HashSet<>();

        @objid ("feb77af6-af47-482e-97a2-d662365b22ed")
         NSUseUpdater updater;

        @objid ("cc00abe8-586b-429b-8c51-4d5554ca0007")
        public ActionVisitor(NSUseUpdater updater) {
            this.updater = updater;
        }

        @objid ("fbba389e-9ebe-402d-9e28-4de9029dca02")
        public void reset() {
            this.objsToRebuild.clear();
        }

        @objid ("566f2fc2-e777-4b55-89b8-7c8873b5c8f2")
        public Set<MObject> getResults() {
            return this.objsToRebuild;
        }

        @objid ("c772d152-ef97-4d31-aadb-8aa417ca3e0b")
        @Override
        public void visitTransaction(Transaction theTransaction) {
            ArrayList<IAction> actions = new ArrayList<>();
            for (IAction a : theTransaction.getActions())
                actions.add(a);
            
            for (IAction a : actions) {
                a.accept(this);
            }
        }

        @objid ("79e44b61-3cf3-42ae-8376-cea776051ed2")
        @Override
        public void visitCreateElementAction(CreateElementAction theCreateElementAction) {
            // nothing
        }

        @objid ("acbdb1ce-0aaf-4dbc-b019-f9da3384a3b3")
        @Override
        public void visitDeleteElementAction(DeleteElementAction theDeleteElementAction) {
            // nothing
        }

        @objid ("80fcd411-dc45-4732-ae73-e2863700b847")
        @Override
        public void visitSetAttributeAction(SetAttributeAction theSetAttributeAction) {
            // nothing
        }

        @objid ("6e5d0d67-506a-4700-b156-eba35fa406c8")
        @Override
        public void visitEraseDependencyAction(EraseDependencyAction theEraseDependencyAction) {
            SmObjectImpl value = theEraseDependencyAction.getRef();
            MObject obj = theEraseDependencyAction.getRefered();
            SmDependency dep = theEraseDependencyAction.getDep();
            
            //if (Vaudit.LOG.isDebugEnabled())
            //    Vaudit.LOG.debug("\t\tvisited action: erase '%s' (%s) from dep '%s' of '%s' (%s)",
            //            (value != null) ? value.getName() : "null", (value != null) ? value.getMClass().getName() : null,
            //            dep.getName(), obj.getName(), obj.getMClass().getName());
            
            // Abort cases: do nothing
            if (dep == this.NameSpace_user_NamespaceUse ||
                dep == this.NameSpace_used_NamespaceUse || 
                dep == this.Element_causing_NamespaceUse) {
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>ignore, dep is user/use/cause");
                return;
            }
            
            if (obj == null || !obj.isValid()) {
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>ignore, obj is not valid");
                return;
            }
            
            // Valid cases
            if (dep.isComponent()) {
                // The erased dependency is a composition
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>add object '%s' (%s)", obj.getName(), obj.getMClass().getName());
                this.objsToRebuild.add(obj);
                if (value != null && value.isValid()) {
                    // it is a move
                    
                    //if (Vaudit.LOG.isDebugEnabled())
                    //    Vaudit.LOG.debug("\t\t\t\t=>add object '%s' (%s)", o.getName(), o.getMClass().getName());
                    this.objsToRebuild.addAll(this.updater.onObjectMovedFrom(value, obj));
                }
            
            } else if (obj.getMClass() == this.NamespaceUse_MClass) {
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>ignore, the modified object is a nsu");
                NamespaceUse nsu = (NamespaceUse) obj;
                if (dep == this.NamespaceUse_cause_Element) {
                    if (nsu.getCause().isEmpty()) {
                        NSUseUtils.dereferenceNSUsesCausedBy(nsu);
                        nsu.delete(); // delete the useless use
                    }
                } else if (dep == this.NamespaceUse_user_NameSpace) {
                    if (nsu.getUser() == null) {
                        NSUseUtils.dereferenceNSUsesCausedBy(nsu);
                        nsu.delete();
                    }
                } else if (dep == this.NamespaceUse_used_NameSpace) {
                    if (nsu.getUsed() == null) {
                        NSUseUtils.dereferenceNSUsesCausedBy(nsu);
                        nsu.delete();
                    }
                }
            } else if (dep.isPartOf() || dep.isSharedComposition()) {
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>add object '%s' (%s)", obj.getName(), obj.getMClass().getName());
                this.objsToRebuild.add(obj);
            }
        }

        @objid ("e0e66e43-6e93-46b3-9e56-0dc7f2605925")
        @Override
        public void visitAppendDependencyAction(AppendDependencyAction theAppendDependencyAction) {
            MObject obj = theAppendDependencyAction.getRefered();
            SmDependency dep = theAppendDependencyAction.getDep();
            MObject value = theAppendDependencyAction.getRef();
            
            //if (Vaudit.LOG.isDebugEnabled())
            //    Vaudit.LOG.debug("\t\tvisited action: append '%s' (%s) to dep '%s' of '%s' (%s)", (value != null) ? value.getName()
            //            : "null", (value != null) ? value.getMClass().getName() : null, dep.getName(), obj.getName(), obj
            //            .getMClass().getName());
            
            if ((obj.isDeleted())
                    || (value != null && value.getMClass() == this.NamespaceUse_MClass || (obj.getMClass() == this.NamespaceUse_MClass))) {
                // do nothing
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>ignore");
            } else if (dep.isPartOf() || dep.isSharedComposition()) {
                //if (Vaudit.LOG.isDebugEnabled())
                //    Vaudit.LOG.debug("\t\t\t=>add object '%s' (%s)", obj.getName(), obj.getMClass().getName());
                this.objsToRebuild.add(obj);
            }
        }

        @objid ("e1d8a4b9-32a3-4923-ac6c-a6c1dc7b7ef4")
        @Override
        public void visitMoveDependencyAction(MoveDependencyAction theMoveDependencyAction) {
            // do nothing
        }

    }

}
