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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.AbstractJavaModule;
import org.modelio.api.module.IModule.ModuleRuntimeState;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.core.ui.images.ModuleI18NService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.mda.infra.plugin.MdaInfra;

/**
 * Small helper class in charge of starting a module
 */
@objid ("3e63ab20-0228-11e2-9fca-001ec947c8cc")
class ModuleStarter {
    /**
     * Starts the passed module after making sure all the modules it depends on are started first.
     * @param iModuleToStart the module to start.
     * @param dependsOnIModules the modules to be started first (failure to start those prevents to start the module).
     * @param weakDependencies the modules to be started first if activated (failure to start those do NOT prevent to start the
     * module).
     * @param moduleService the module service.
     * @param gProject the project.
     * @return <code>true</code> if the module was correctly started, <code>false</code> otherwise.
     * @throws org.modelio.api.module.ModuleException when the start fails.
     */
    @objid ("5a85c179-023e-11e2-9fca-001ec947c8cc")
    static boolean startModule(final IModule iModuleToStart, List<IModule> dependsOnIModules, List<IModule> weakDependencies, final ModuleService moduleService, GProject gProject) throws ModuleException {
        // Start required modules first (if one fails, consider this start to be failed
        for (IModule requiredModule : dependsOnIModules) {
            if (!moduleService.startModule(requiredModule, gProject)) {
                return false;
            }
        }
        // Try to start weak dependencies but catch and silently ignore failures
        // (if any) and continue.
        for (IModule weakDependency : weakDependencies) {
            try {
                moduleService.startModule(weakDependency, gProject);
            } catch (ModuleException e) {
                // Log the error as warning but continue
                MdaInfra.LOG.warning("Starting module %s as weak dependency of module %s failed.",
                        weakDependency.getName(), iModuleToStart.getName());
                MdaInfra.LOG.warning(e);
            }
        }
        
        // Start the module itself
        if (doStartModule(iModuleToStart, moduleService)) {
        
            // Register the module to allow stereotype image loading
            ModuleI18NService.registerModule(iModuleToStart.getModel(), iModuleToStart);
            return true;
        }
        return false;
    }

    @objid ("5a85c189-023e-11e2-9fca-001ec947c8cc")
    private static boolean doStartModule(final IModule module, final ModuleService moduleService) throws ModuleException {
        boolean startSuccessful = false;
        try {
            // Add to registry and set state
            moduleService.getModuleRegistry().addStartedModule(module);
            setState(module, ModuleRuntimeState.Started);
        
            // Call IModuleSession#start() method.
            try {
                startSuccessful = module.getSession().start();
            } catch (Error e) {
                throw new ModuleException("Could not start" + module.getName() + ".", e);
            }
        } finally {
            if (startSuccessful) {
                MdaInfra.LOG.debug("Jxbv2Module %s v%s started successfully.", module.getName(), module.getVersion());
            } else {
                // Start could not complete, remove form registry and reset state.
                MdaInfra.LOG.debug("Jxbv2Module %s v%s failed to start.", module.getName(), module.getVersion());
                setState(module, ModuleRuntimeState.Loaded);
                moduleService.getModuleRegistry().removeStartedModule(module);
            }
        }
        return startSuccessful;
    }

    @objid ("5a8823c8-023e-11e2-9fca-001ec947c8cc")
    private static void setState(final IModule module, final ModuleRuntimeState newState) {
        if (module instanceof AbstractJavaModule) {
            ((AbstractJavaModule) module).setState(newState);
        } else if (module instanceof FakeModule) {
            ((FakeModule) module).setState(newState);
        }
    }

}
