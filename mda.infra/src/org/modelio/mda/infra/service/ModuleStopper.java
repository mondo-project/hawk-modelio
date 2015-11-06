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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.AbstractJavaModule;
import org.modelio.api.module.IModule.ModuleRuntimeState;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.gproject.GProject;

/**
 * Small helper class in charge of stopping a module
 */
@objid ("be77cd8c-0ca7-11e2-8ebb-002564c97630")
class ModuleStopper {
    /**
     * Stops the passed module after making sure all the modules that depends on it are stopped first.
     * @param iModuleToStop the module to stop.
     * @param dependentIModules the modules to be stopped first, in order please.
     * @param moduleService the module service.
     * @param gProject the project.
     * @return a set of all stopped modules.
     * @throws org.modelio.api.module.ModuleException when the stop fails.
     */
    @objid ("1843a05c-0ca8-11e2-8ebb-002564c97630")
    static Set<IModule> stopModule(final IModule iModuleToStop, List<IModule> dependentIModules, final ModuleService moduleService, GProject gProject) throws ModuleException {
        Set<IModule> ret = new HashSet<>();
        
        // Stop dependent modules first
        for (IModule dependentIModule : dependentIModules) {
            ret.addAll(moduleService.stopModule(dependentIModule, gProject));
        }
        
        // Unregister the module from the image loader
        ModuleI18NService.unregisterModule(iModuleToStop.getModel());
        
        // Stop the module itself
        doStopModule(iModuleToStop, moduleService);
        return ret;
    }

    @objid ("1844158c-0ca8-11e2-8ebb-002564c97630")
    private static void doStopModule(final IModule module, final ModuleService moduleService) throws ModuleException {
        // Add to registry and set state
        moduleService.getModuleRegistry().removeStartedModule(module);
        setState(module, ModuleRuntimeState.Loaded);
        
        // Call IModuleSession#stop() method.
        try {
            module.getSession().stop();
        } catch (Error e) {
            throw new ModuleException("Could not stop" + module.getName() + ".", e);
        }
    }

    @objid ("18443c9c-0ca8-11e2-8ebb-002564c97630")
    private static void setState(final IModule module, final ModuleRuntimeState newState) {
        if (module instanceof AbstractJavaModule) {
            ((AbstractJavaModule) module).setState(newState);
        } else if (module instanceof FakeModule) {
            ((FakeModule) module).setState(newState);
        }
    }

}
