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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.module.GModule;
import org.modelio.gproject.module.IModuleHandle;
import org.modelio.gproject.module.ModuleId;
import org.modelio.gproject.module.ModuleSorter;
import org.modelio.vbasic.collections.TopologicalSorter.CyclicDependencyException;
import org.modelio.vbasic.version.Version;

/**
 * Helper class to convert {@link GModule} into {@link IModule} or a name into a {@link GModule}, get the list of weak dependencies, etc.
 */
@objid ("2f013d2a-0233-11e2-9fca-001ec947c8cc")
class ModuleResolutionHelper {
    /**
     * Returns the list of <strong>activated</strong> IModule that the passed module has weak dependencies on.
     * @param gModule the module for which to get weak dependencies.
     * @param gProject the project to search into.
     * @param moduleService the module service
     * @return the list of IModule that the passed module has weak dependencies on.
     */
    @objid ("c413849f-4503-4f56-aa96-714c01c47e45")
    static List<IModule> getGModuleActivatedWeakDependenciesIModules(GModule gModule, GProject gProject, ModuleService moduleService) {
        return getModuleHandleActivatedWeakDependenciesIModules(gModule.getModuleHandle(), gProject, moduleService);
    }

    /**
     * Returns the list of <strong>activated</strong> IModule that the passed module handle has weak dependencies on.
     * @param iModuleHandle the module handle for which to get weak dependencies.
     * @param gProject the project to search into.
     * @param moduleService the module service
     * @return the list of IModule that the passed module has weak dependencies on.
     */
    @objid ("b8a9cc6c-9543-4876-a936-144fef5d8adc")
    static List<IModule> getModuleHandleActivatedWeakDependenciesIModules(IModuleHandle iModuleHandle, GProject gProject, ModuleService moduleService) {
        List<IModule> activatedWeakDependencies = new ArrayList<>();
        // Read the list from administrative informations
        for (ModuleId moduleId : iModuleHandle.getWeakDependencies()) {
            // Search the corresponding GModule
            GModule module = getGModuleByName(gProject, moduleId.getName());
            if (module != null) {
                // If Version compatible and activated, look for the loaded
                // IModule and add it to the list.
                if (isVersionCompatible(module.getVersion(), moduleId.getVersion()) && module.isActivated()) {
                    final IModule weakRequiredModule = moduleService.getModuleRegistry().getLoadedModule(module.getModuleElement());
                    if (weakRequiredModule != null) {
                        activatedWeakDependencies.add(weakRequiredModule);
                    }
                }
            }
        }
        return activatedWeakDependencies;
    }

    /**
     * Returns whether the passed module is Version compatible with the passed reference Version.
     * <p>
     * Current strategy is to return <i>true</i>
     * if the module Version is equal or newer than the reference Version.
     * @param moduleVersion the Version to test for compatibility.
     * @param referenceVersion the reference version.
     * @return <i>true</i> if the passed module is version compatible with the reference.
     */
    @objid ("0fe5a024-67b6-4172-a32a-1cb93f3e3d9c")
    static boolean isVersionCompatible(Version moduleVersion, Version referenceVersion) {
        return moduleVersion.isNewerThan(referenceVersion) || moduleVersion.equals(referenceVersion);
    }

    /**
     * Returns the list of mandatory IModule required by the passed module.
     * @param iModule the module for which to get mandatory dependencies.
     * @param moduleService the module service.
     * @return the list of mandatory IModule required by the passed module.
     */
    @objid ("a3030846-6bdf-4892-ad0f-d7f71370d2b6")
    static List<IModule> getIModuleDependsOnIModules(IModule iModule, GProject gProject, ModuleService moduleService) {
        final GModule gModule = getGModuleByName(gProject, iModule.getName());
        final IModuleHandle moduleHandle = gModule != null ? gModule.getModuleHandle() : null;
        return getModuleComponentDependsOnIModules(moduleHandle, gProject, moduleService);
    }

    /**
     * Returns the list of mandatory IModule required by the passed module.
     * @param gModule the module for which to get mandatory dependencies.
     * @param moduleService the module service.
     * @return the list of mandatory IModule required by the passed module.
     */
    @objid ("1584670b-1b9a-474c-bdb0-eddd9676123b")
    static List<IModule> getGModuleDependsOnIModules(GModule gModule, GProject gProject, ModuleService moduleService) {
        return getModuleComponentDependsOnIModules(gModule.getModuleHandle(), gProject, moduleService);
    }

    /**
     * Returns the list of mandatory IModule required by the passed module component.
     * @param moduleHandle the module handle for which to get mandatory dependencies.
     * @param moduleService the module service.
     * @return the list of mandatory IModule required by the passed module.
     */
    @objid ("d9197889-0783-4321-bd54-444b76893e42")
    static List<IModule> getModuleComponentDependsOnIModules(IModuleHandle moduleHandle, GProject gProject, ModuleService moduleService) {
        List<IModule> dependsOn = new ArrayList<>();
        // Read the list directly from the ModelComponent.
        if (moduleHandle != null) {
         // Read the list from administrative informations
            for (ModuleId moduleId : moduleHandle.getDependencies()) {
                // Search the corresponding GModule
                GModule module = getGModuleByName(gProject, moduleId.getName());
                if (module != null) {
                    // If Version compatible and activated, add it to the list.
                    if (isVersionCompatible(module.getVersion(), moduleId.getVersion())) {
                        dependsOn.add(moduleService.getIModule(module.getModuleElement()));
                    }
                }
            }
        }
        return dependsOn;
    }

    /**
     * Returns the first found GModule in the project which name match the passed name.
     * @param gProject the project to search into.
     * @param moduleName the name of the GModule to find.
     * @return the first found GModule in the project which name match the passed name or <code>null</code> if none found.
     */
    @objid ("fd69ce1d-5d5a-46c8-990a-f54f45d0dad7")
    static GModule getGModuleByName(GProject gProject, String moduleName) {
        for (GModule module : gProject.getModules()) {
            if (module.getName().equals(moduleName)) {
                return module;
            }
        }
        return null;
    }

    /**
     * Returns the list of GModule of the passed project that the passed GModule depends on
     * (i.e. the returned GModules are required as mandatory by the passed GModule).
     * @param gModule the GModule to look dependencies for
     * @param gProject the project to look into.
     * @return the list of GModule of the passed project that the passed GModule depends on.
     */
    @objid ("fd87f63b-bb23-480b-aadf-420aa68f28a4")
    static List<GModule> getGModuleDependsOnGModules(GModule gModule, GProject gProject) {
        return getModuleComponentDependsOnGModules(gModule.getModuleHandle(), gProject);
    }

    /**
     * Returns the list of GModule of the passed project that the passed ModuleComponent depends on
     * (i.e. the returned GModules are required as mandatory by the passed ModuleComponent).
     * @param moduleHandle the module handle to look dependencies for
     * @param gProject the project to look into.
     * @return the list of GModule of the passed project that the passed ModuleComponent depends on.
     */
    @objid ("2c1e14ee-3d75-4ad4-8bd8-31adc2a3103e")
    static List<GModule> getModuleComponentDependsOnGModules(IModuleHandle moduleHandle, GProject gProject) {
        // Read info in the model and find the corresponding GModules in the project.
        List<GModule> dependsOn = new ArrayList<>();
        // Read the list from administrative informations
        for (ModuleId moduleId : moduleHandle.getDependencies()) {
            // Search the corresponding GModule
            GModule module = getGModuleByName(gProject, moduleId.getName());
            if (module != null) {
                // If Version compatible and activated, add it to the list.
                if (isVersionCompatible(module.getVersion(), moduleId.getVersion())) {
                    dependsOn.add(module);
                }
            }
        }
        return dependsOn;
    }

    /**
     * Returns the list of <strong>activated</strong> GModule that the passed module has weak dependencies on.
     * @param gModule the GModule to look weak dependencies for
     * @param gProject the project to look into.
     * @return the list of GModule of the passed project that the passed GModule have weak dependencies on.
     */
    @objid ("9fdad231-2763-4a01-b254-e598c065f305")
    static List<GModule> getGModuleActivatedWeakDependenciesGModules(GModule gModule, GProject gProject) {
        return getModuleHandleActivatedWeakDependenciesGModules(gModule.getModuleHandle(), gProject);
    }

    /**
     * Returns the list of <strong>activated</strong> GModule that the passed module has weak dependencies on.
     * @param moduleHandle the module to look weak dependencies for.
     * @param gProject the project to look into.
     * @return the list of GModule of the passed project that the passed GModule have weak dependencies on.
     */
    @objid ("cb822355-ec0a-4192-9dde-6e68b15467cc")
    static List<GModule> getModuleHandleActivatedWeakDependenciesGModules(IModuleHandle moduleHandle, GProject gProject) {
        // Read info in the model and find the corresponding GModules in the project.
        List<GModule> weakDependencies = new ArrayList<>();
        // Read the list from administrative informations
        for (ModuleId moduleId : moduleHandle.getWeakDependencies()) {
            // Search the corresponding GModule
            GModule module = getGModuleByName(gProject, moduleId.getName());
            if (module != null) {
                // If Version compatible and activated, add it to the list.
                if (isVersionCompatible(module.getVersion(), moduleId.getVersion()) && module.isActivated()) {
                    weakDependencies.add(module);
                }
            }
        }
        return weakDependencies;
    }

    /**
     * Returns <i>true</i> if the module defined by the passed ModuleHandle can safely be installed in the project.
     * <p>
     * Current strategy is to test if all modules required by the passed module can be found in
     * the passed project in a version compatible
     * with the requirement and that no module in the project requires a newer version of this model.
     * @param moduleHandle the module to test.
     * @param gProject the project in which the passed module would be installed
     * @return <i>true</i> if the passed ModuleHandle can safely be installed in the project
     */
    @objid ("f51019ab-0358-4a08-ad63-d38979b5fc87")
    static boolean checkCanInstall(IModuleHandle moduleHandle, GProject gProject) {
        for (ModuleId moduleId : moduleHandle.getDependencies()) {
            boolean moduleFoundInProject = false;
            for (GModule gModuleInProject : gProject.getModules()) {
                if (gModuleInProject.getName().equals(moduleId.getName())
                        && isVersionCompatible(gModuleInProject.getVersion(), moduleId.getVersion())) {
                    moduleFoundInProject = true;
                    break;
                }
            }
            if (!moduleFoundInProject) {
                return false;
            }
        }
        for (GModule gModuleInProject : gProject.getModules()) {
            for (ModuleId requiredModuleId : gModuleInProject.getModuleHandle().getDependencies()) {
                if (requiredModuleId.getName().equals(moduleHandle.getName())
                        && !isVersionCompatible(moduleHandle.getVersion(), requiredModuleId.getVersion())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns the topologically sorted list of all {@link GModule GModules} in the project
     * that have a dependency (direct or not, strong or weak) on the passed module.
     * <p>
     * The returned list is topologically sorted by uninstallation/stopping order.
     * @param moduleHandle the module for which dependents are searched.
     * @param gProject the project to search into.
     * @return the list of GModules in the project that have a dependency (direct or not, strong or weak) on the passed module.
     * @throws org.modelio.api.module.ModuleException in case of cyclic module dependency.
     */
    @objid ("0b2b4156-be19-4e2b-862a-5e5044934ee3")
    static List<GModule> getModuleHandleDependentGModules(IModuleHandle moduleHandle, GProject gProject) throws ModuleException {
        Set<GModule> deps = new HashSet<>();
        getModuleHandleDependentGModules(moduleHandle, gProject, deps);
        try {
            final List<GModule> sortedModules = ModuleSorter.sortModules(deps);
            // the sorter returns modules in installation order, reverse it.
            Collections.reverse(sortedModules);
            return sortedModules;
        } catch (CyclicDependencyException e) {
            throw new ModuleException(e.getLocalizedMessage(), e);
        }
    }

    /**
     * Returns the list of all loaded {@link IModule} in the project that have a dependency
     * (direct or not, strong or weak) on the passed module.
     * <p>
     * The returned list is topologically sorted by installation order.
     * @param iModule the module for which dependents are searched.
     * @param gProject the project to search into.
     * @param moduleService the modules service.
     * @return the list of {@link IModule} in the project that have a dependency (direct or not, strong or weak) on the passed module.
     * @throws org.modelio.api.module.ModuleException in case of cyclic module dependency.
     */
    @objid ("b96097d6-0aa5-4dfb-9259-5cf5a83768dc")
    public static List<IModule> getIModuleDependentIModules(IModule iModule, GProject gProject, ModuleService moduleService) throws ModuleException {
        List<IModule> ret = new ArrayList<>();
        
        GModule gModule = ModuleResolutionHelper.getGModuleByName(gProject, iModule.getName());
        
        List<GModule> dependentGModules = getModuleHandleDependentGModules(gModule.getModuleHandle(), gProject);
        for (GModule dependentGModule : dependentGModules) {
            IModule dependentLoadedModule = moduleService.getModuleRegistry().getLoadedModule(dependentGModule.getModuleElement());
            if (dependentLoadedModule != null) {
                ret.add(dependentLoadedModule);
            }
        }
        return ret;
    }

    @objid ("0f380173-9925-4d1a-8e58-2fc0f6dc1fe9")
    private static void getModuleHandleDependentGModules(IModuleHandle moduleHandle, GProject gProject, Set<GModule> dependents) {
        // Go through each module in the project and look at its dependencies in case it contains the passed
        // module...
        for (GModule gModuleInProject : gProject.getModules()) {
            // Weak dependencies
            for (ModuleId moduleId : gModuleInProject.getModuleHandle().getWeakDependencies()) {
                if (moduleHandle.getName().equals(moduleId.getName())
                        && isVersionCompatible(moduleHandle.getVersion(), moduleId.getVersion())) {
                    dependents.add(gModuleInProject);
                    // ... and recursively
                    getModuleHandleDependentGModules(gModuleInProject.getModuleHandle(), gProject, dependents);
                }
            }
            // Strong dependencies
            for (ModuleId moduleId : gModuleInProject.getModuleHandle().getDependencies()) {
                if (moduleHandle.getName().equals(moduleId.getName())) {
                    dependents.add(gModuleInProject);
                    // ... and recursively
                    getModuleHandleDependentGModules(gModuleInProject.getModuleHandle(), gProject, dependents);
                }
            }
        }
    }

}
