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
                                    

package org.modelio.mda.infra.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IPeerModule;
import org.modelio.api.module.ModuleException;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.core.events.ModelioEvent;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.gproject.data.project.GProperties.Entry;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.module.GModule;
import org.modelio.gproject.module.IModuleCache;
import org.modelio.gproject.module.IModuleHandle;
import org.modelio.gproject.module.ModuleId;
import org.modelio.gproject.module.ModuleSorter;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.vbasic.collections.TopologicalSorter.CyclicDependencyException;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vbasic.version.Version;

/**
 * Module services.
 */
@objid ("8dd0dee5-f1b7-11e1-af52-001ec947c8cc")
@Creatable
public class ModuleService implements IModuleService {
    @objid ("ca5e85e3-0c73-11e2-a703-001ec947c8cc")
    @Inject
    @Optional
    private ModelioEnv env;

    @objid ("877d9db1-0e30-11e2-baba-001ec947c8cc")
    @Inject
    @Optional
    private IMModelServices mModelServices;

    @objid ("2a611be9-f348-11e1-9458-001ec947c8cc")
    @Inject
    @Optional
    private IModelioEventService modelioEventService;

    @objid ("495b93d1-f2b5-11e1-af52-001ec947c8cc")
    private static IModuleRegistry moduleRegistry = new ModuleRegistry();

    @objid ("ff9c9181-da55-4e7a-a451-5004f40ccf05")
    @Inject
    @Optional
    private IModuleCache moduleCache;

    @objid ("2bb63eee-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public void activateModule(GModule gModule) throws ModuleException {
        gModule.setActivated(true);
        
        final ModuleComponent moduleElement = gModule.getModuleElement();
        if (moduleElement != null) {
            startModule(getIModule(moduleElement), gModule.getProject());
        }
    }

    /**
     * Activates and starts the given module.
     * <p>
     * This method does NOT activate nor start modules required by the given module.
     * @param iModule the module to start
     * @param gProject the project
     * @throws org.modelio.api.module.ModuleException in case of failure
     */
    @objid ("c85c0675-03ec-11e2-8e1f-001ec947c8cc")
    void activateModule(IModule iModule, GProject gProject) throws ModuleException {
        GModule gModule = ModuleResolutionHelper.getGModuleByName(gProject, iModule.getName());
        activateModule(gModule);
    }

    @objid ("2bb63ef2-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public void deactivateModule(GModule gModule) throws ModuleException {
        gModule.setActivated(false);
        
        final ModuleComponent moduleElement = gModule.getModuleElement();
        if (moduleElement != null) {
            stopModule(getIModule(moduleElement), gModule.getProject());
        }
    }

    /**
     * Stops and deactivates the given module.
     * <p>
     * Modules requiring the given module will be stopped first.
     * @param iModule the module to deactivate
     * @param gProject the project
     * @throws org.modelio.api.module.ModuleException on failure
     */
    @objid ("c85c0678-03ec-11e2-8e1f-001ec947c8cc")
    void deactivateModule(IModule iModule, GProject gProject) throws ModuleException {
        GModule gModule = ModuleResolutionHelper.getGModuleByName(gProject, iModule.getName());
        deactivateModule(gModule);
    }

    @objid ("61ca78df-186b-11e2-92d2-001ec947c8cc")
    @Override
    public IModule getIModule(ModuleComponent moduleComponent) {
        return getModuleRegistry().getLoadedModule(moduleComponent);
    }

    @objid ("2bb63ef6-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public IModuleRegistry getModuleRegistry() {
        return ModuleService.moduleRegistry;
    }

    @objid ("008f6d9e-96dd-103f-87fd-001ec947cd2a")
    @Override
    public String getName() {
        return "ModuleService";
    }

    @objid ("2bb63ed3-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public <T extends IPeerModule> T getPeerModule(Class<T> metaclass) throws UnknownModuleException {
        try {
            for (IModule module : ModuleService.moduleRegistry.getStartedModules()) {
                IPeerModule peerModule = module.getPeerModule();
                String metaclassInterfaceName = metaclass.getCanonicalName();
                Class<? extends IPeerModule> peerModuleClass = peerModule.getClass();
                for (Class<?> interf : peerModuleClass.getInterfaces()) {
                    String interfName = interf.getCanonicalName();
                    if (interfName.equals(metaclassInterfaceName)) {
                        return metaclass.cast(peerModule);
                    }
                }
            }
        } catch (ClassCastException e) {
            MdaInfra.LOG.error(e);
        }
        throw new UnknownModuleException(
          String.format( "The '%s' class is not kwown as a Java module." , metaclass.getName())
         );
    }

    @objid ("2bb63ee9-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public void installModule(GProject gProject, Path moduleFilePath) throws ModuleException {
        try {
            IModuleHandle rtModuleHandle = this.moduleCache.getModuleHandle(moduleFilePath, null);
            // Is module in the cache?
            if (rtModuleHandle == null) {
                throw new ModuleException(MdaInfra.I18N.getMessage("ModuleExceptionMessage.InvalidArchivePath", moduleFilePath.toString()));
            }
        
            // Is metamodel compatible?
            if (rtModuleHandle.getVersion().getMetamodelVersion() < 8008) {
                throw new ModuleException(MdaInfra.I18N.getMessage("ModuleExceptionMessage.ModuleMetamodelTooOld", rtModuleHandle.getName(), rtModuleHandle.getVersion().toString()));
            }
        
            // Are dependencies missing?
            if (ModuleResolutionHelper.checkCanInstall(rtModuleHandle, gProject) == false) {
                // Gather only latest version for all dependencies
                Map<String, Version> dependencies = new HashMap<>();
                
                for (ModuleId requiredModuleId : rtModuleHandle.getDependencies()) {
                    dependencies.put(requiredModuleId.getName(), requiredModuleId.getVersion());
                }
                for (GModule gModuleInProject : gProject.getModules()) {
                    for (ModuleId requiredModuleId : gModuleInProject.getModuleHandle().getDependencies()) {
                        final Version foundVersion = dependencies.get(requiredModuleId.getName());
                        if (foundVersion == null || foundVersion.isOlderThan(requiredModuleId.getVersion())) {
                            dependencies.put(requiredModuleId.getName(), requiredModuleId.getVersion());
                        }
                    }
                }
                
                // Build the message
                StringBuilder dependencyIds = new StringBuilder();
                for (java.util.Map.Entry<String, Version> dependency : dependencies.entrySet()) {
                    dependencyIds.append("\n - " + dependency.getKey() + " " + dependency.getValue());
                }
                
                throw new ModuleException(MdaInfra.I18N.getMessage("ModuleExceptionMessage.CannotInstallModuleDetail", rtModuleHandle.getName(), dependencyIds.toString()+"\n"));
            }
        
            ModuleInstaller installer = new ModuleInstaller(gProject, this);
        
            // Determine whether it is a first deployment or it is an upgrade.
            // For this, we have to look for the old module.
            GModule previouslyInstalledGModule = ModuleResolutionHelper.getGModuleByName(gProject, rtModuleHandle.getName());
            GModule newModule;
            if (previouslyInstalledGModule == null) {
                // actually install the module in the gproject
                GModule gModule = gProject.installModule(rtModuleHandle, moduleFilePath);
        
                newModule = installer.moduleFirstInstall(gModule, rtModuleHandle);
            } else {
                // Update model
                IModule startedOldModule = this.getModuleRegistry().getStartedModule(previouslyInstalledGModule.getModuleElement());
                this.stopModule(startedOldModule, gProject);
        
                this.unloadModule(startedOldModule, gProject);
        
        
                gProject.removeModule(previouslyInstalledGModule);
        
                GModule updatedGModule = gProject.installModule(rtModuleHandle, moduleFilePath);
                newModule = installer.moduleUpdateInstall(updatedGModule, rtModuleHandle, previouslyInstalledGModule);
            }
            if (newModule != null) {
                this.modelioEventService.postSyncEvent(this, ModelioEvent.MODULE_DEPLOYED, newModule);
            }
        } catch (IOException e) {
            throw new ModuleException(MdaInfra.I18N.getMessage(
                    "ModuleExceptionMessage.CannotInstallModule",
                    FileUtils.getLocalizedMessage(e)), e);
        }
    }

    @objid ("b60fdf1c-0d64-11e2-ae8f-002564c97630")
    @Override
    public void removeModule(GModule gModule) throws ModuleException {
        IModule iModule = getIModule(gModule.getModuleElement());
        
        GProject gProject = gModule.getProject();
        
        // IModule might be null for broken modules
        if (iModule != null) {
            ModuleComponent comp = iModule.getModel();
        
            // If there remains some strong deps, cannot remove the module
            Collection<IModule> deps = ModuleResolutionHelper.getIModuleDependentIModules(iModule, gProject, this);
            if (!deps.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append(MdaInfra.I18N.getString("ModuleRemove.error.dependencies"));
                sb.append("\n");
                for (IModule dep : deps) {
                    sb.append(" - ");
                    sb.append(dep.getLabel());
                    sb.append("\n");
                }
                throw new ModuleException(sb.toString());
            }
        
        
            // Stop
            stopModule(iModule, gProject);
        
            // Remove the module MDA annotations and the ModuleComponent itself
            ModuleRemover.remove(comp);
        
            // Unload
            unloadModule(iModule, gProject);
        
        }
        
        // Remove
        gProject.removeModule(gModule);
    }

    @objid ("2bb63edc-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public void startAllModules(GProject gProject, final IProgressMonitor aMonitor) {
        SubMonitor progress = SubMonitor.convert(aMonitor);
        List<GModule> gModules = gProject.getModules();
        progress.setWorkRemaining(gModules.size());
        
        ModuleInstaller installer = new ModuleInstaller(gProject, this);
        
        try {
            for (GModule gModule : ModuleSorter.sortModules(gModules)) {
                // All modules are loaded, but only activated modules are started.
                try {
                    // If the module has the property "SELECT_ON_OPEN", consider it's a first install
                    final Entry toSelect = gModule.getParameters().getProperty(GModule.SELECT_ON_OPEN);
                    if (toSelect != null && toSelect.getValue().equals("true")) {
                        installer.moduleFirstInstall(gModule, gModule.getModuleHandle());
                        
                        // Remove the property, next time it will be a simple start
                        gModule.getParameters().remove(GModule.SELECT_ON_OPEN);
                    } else {
                        // Load the module
                        IModule iModule = loadModule(gProject, gModule);
                        if (gModule.isActivated()) {
                            // Start the module
                            progress.subTask(MdaInfra.I18N.getMessage("ModuleStartProgress.Starting", iModule.getLabel(),
                                    iModule.getVersion().toString()));
                            startModule(iModule, gProject);
                        }
                    }
                } catch (ModuleException | RuntimeException | LinkageError e) {
                    MdaInfra.LOG.error(e);
                }
                progress.worked(1);
            
            }
        } catch (CyclicDependencyException e) {
            MdaInfra.LOG.error(e);
        }
        progress.done();
    }

    @objid ("2bb63ee1-f1ed-11e1-af52-001ec947c8cc")
    @Override
    public void stopAllModules(GProject project) {
        // Stops modules in any order : stopModule(...) will stop itself dependent modules first.
        for (IModule module : new ArrayList<>(getModuleRegistry().getStartedModules())) {
            try {
                stopModule(module, project);
            } catch (ModuleException | RuntimeException | LinkageError e) {
                MdaInfra.LOG.warning(e);
            }
        }
        
        for (IModule module : new ArrayList<>(getModuleRegistry().getLoadedModules())) {
            try {
                unloadModule(module, project);
            } catch (ModuleException | RuntimeException | LinkageError e) {
                MdaInfra.LOG.warning(e);
            }
        }
    }

    /**
     * Stop a module.
     * <p>
     * Modules that depend on the given module will be stopped first.
     * @param iModule the module to stop
     * @param gProject the project
     * @return all stopped modules
     * @throws org.modelio.api.module.ModuleException in case of failure.
     */
    @objid ("4440b654-0324-11e2-9fca-001ec947c8cc")
    public Set<IModule> stopModule(IModule iModule, GProject gProject) throws ModuleException {
        // Check that module is not already stopped
        if (iModule==null ||  getModuleRegistry().getStartedModule(iModule.getModel()) == null) {
            return Collections.emptySet();
        }
        
        // Get all required modules
        List<IModule> dependentIModules = ModuleResolutionHelper.getIModuleDependentIModules(iModule, gProject,
                this);
        
        // Stop the module (dependencies will be started if not already)
        Set<IModule> stoppedModules = ModuleStopper.stopModule(iModule, dependentIModules, this, gProject);
        this.modelioEventService.postSyncEvent(this, ModelioEvent.MODULE_STOPPED, iModule);
        return stoppedModules;
    }

    /**
     * Unload a module.
     * <p>
     * Modules that depend on the given module will be unloaded first.
     * <p>
     * FIXME: dependent modules are directly unloaded. They are not stopped if already started.
     * @param iModule the module to stop
     * @param gProject the project
     * @return all stopped modules
     * @throws org.modelio.api.module.ModuleException in case of failure.
     */
    @objid ("444318ad-0324-11e2-9fca-001ec947c8cc")
    public Set<IModule> unloadModule(IModule iModule, GProject gProject) throws ModuleException {
        // Check that module is loaded
        if (iModule == null || getModuleRegistry().getLoadedModule(iModule.getModel()) == null) {
            // This module was already unloaded as a 'dependent' module.
            return Collections.emptySet();
        }
        
        // Check that module is not started
        if (getModuleRegistry().getStartedModule(iModule.getModel()) != null) {
            throw new ModuleException("Module '" + iModule.getName()
                    + "' is still in started state. Stop it before unloading it.");
        }
        
        // Get all required modules topologically sorted by stopping order
        Collection<IModule> dependentIModules = ModuleResolutionHelper.getIModuleDependentIModules(iModule, gProject, this);
        
        // Unload the module (dependencies will be unloaded if not already)
        return ModuleUnloader.unloadModule(iModule, dependentIModules, this, gProject);
    }

    @objid ("495b93d2-f2b5-11e1-af52-001ec947c8cc")
    IModule loadModule(final GProject gProject, final GModule gModule) throws ModuleException {
        IModule iModule = getModuleRegistry().getLoadedModule(gModule.getModuleElement());
        // Return the module if already loaded
        if (iModule == null) {
            List<IModule> loadedDependencies = new ArrayList<>();
        
            for (GModule strongDependency : ModuleResolutionHelper.getGModuleDependsOnGModules(gModule, gProject)) {
                IModule loadedStrongDependency = loadModule(gProject, strongDependency);
                if (loadedStrongDependency != null) {
                    loadedDependencies.add(loadedStrongDependency);
                } else {
                    MdaInfra.LOG.warning("Loading module %s as strong dependency of module %s failed.", strongDependency.getName(), gModule.getName());
                }
            }
        
            for (GModule weakDependency : ModuleResolutionHelper.getGModuleActivatedWeakDependenciesGModules(gModule, gProject)) {
                try {
                    IModule loadedWeakDependency = loadModule(gProject, weakDependency);
                    if (loadedWeakDependency != null) {
                        loadedDependencies.add(loadedWeakDependency);
                    }
                } catch (ModuleException e) {
                    // Log the error as warning but continue
                    MdaInfra.LOG.warning("Loading module %s as weak dependency of module %s failed: %s", weakDependency.getName(), gModule.getName(), e.getMessage());
                    MdaInfra.LOG.warning(e);
                }
            }
        
            ModuleLoader loader = new ModuleLoader(gProject, this.mModelServices);
            iModule = loader.loadModule(gModule, gModule.getModuleHandle(), loadedDependencies);
        
            // Add loaded module to the registry
            getModuleRegistry().addLoadedModule(iModule);
        
        }
        return iModule;
    }

    @objid ("b5a61523-1207-11e2-8ab5-001ec947c8cc")
    @Inject
    @Optional
    @SuppressWarnings("unused")
    static void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) GProject gProject, IEclipseContext context) {
        // The current project has been closed, remove this instance from the context until another project is opened.
        context.set(IModuleService.class, null);
    }

    @objid ("b5a6151d-1207-11e2-8ab5-001ec947c8cc")
    @Inject
    @Optional
    @SuppressWarnings("unused")
    void onProjectOpening(@EventTopic(ModelioEventTopics.PROJECT_OPENING) GProject gProject, IEclipseContext context) {
        // A project is being opened, put this instance in the context so it can be used to manipulate modules on the
        // current project.
        context.set(IModuleService.class, this);
    }

    @objid ("b7e56813-01a1-11e2-9fca-001ec947c8cc")
    boolean startModule(final IModule iModule, GProject gProject) throws ModuleException {
        // Check that module is not already started
        if (getModuleRegistry().getStartedModule(iModule.getModel()) != null) {
            return true;
        }
        
        // Get all required modules
        List<IModule> dependsOnIModules = ModuleResolutionHelper.getIModuleDependsOnIModules(iModule, gProject, this);
        
        // Get all activated weak dependencies
        GModule gModule = ModuleResolutionHelper.getGModuleByName(gProject, iModule.getName());
        List<IModule> weakDependencies = ModuleResolutionHelper.getGModuleActivatedWeakDependenciesIModules(gModule,
                gProject, this);
        
        // Start the module (dependencies will be started if not already)
        boolean success = ModuleStarter.startModule(iModule, dependsOnIModules, weakDependencies, this, gProject);
        if (success) {
            this.modelioEventService.postSyncEvent(this, ModelioEvent.MODULE_STARTED, iModule);
        }
        return success;
    }

}
