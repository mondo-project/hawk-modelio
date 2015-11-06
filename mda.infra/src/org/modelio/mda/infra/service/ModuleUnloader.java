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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.gproject.gproject.GProject;

/**
 * Small helper class in charge of unloading a module
 */
@objid ("e5f7005d-0d4f-11e2-b01f-002564c97630")
class ModuleUnloader {
    /**
     * Unloads the passed module after making sure all the modules that depends on it are unloaded first.
     * @param iModuleToStop the module to unload.
     * @param dependentIModules the modules to be unloaded first.
     * @param moduleService the module service.
     * @param gProject the project.
     * @return a set of all unloaded modules.
     * @throws org.modelio.api.module.ModuleException in case of failure
     */
    @objid ("f48f2f31-0d4f-11e2-b01f-002564c97630")
    static Set<IModule> unloadModule(final IModule iModuleToStop, Collection<IModule> dependentIModules, final ModuleService moduleService, GProject gProject) throws ModuleException {
        Set<IModule> ret = new HashSet<>();
        
        // Stop dependent modules first
        for (IModule dependentIModule : dependentIModules) {
            ret.addAll(moduleService.unloadModule(dependentIModule, gProject));
        }
        
        // Stop the module itself
        doUnloadModule(iModuleToStop, moduleService);
        return ret;
    }

    @objid ("f48fcb75-0d4f-11e2-b01f-002564c97630")
    private static void doUnloadModule(final IModule module, final ModuleService moduleService) throws ModuleException {
        // Remove from registry and set state
        moduleService.getModuleRegistry().removeLoadedModule(module);
        
        // Call IModuleSession#uninit() method.
        try {
            module.uninit();
        } catch (RuntimeException | Error e) {
            throw new ModuleException("uninit() failed on '" + module.getName() + "' module: "+e.toString(), e);
        }
    }

}
