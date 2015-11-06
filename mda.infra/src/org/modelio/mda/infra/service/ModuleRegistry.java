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
import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.gproject.module.ModuleId;
import org.modelio.metamodel.mda.ModuleComponent;

/**
 * Modules registry.
 * <p>
 * Knows which modules are loaded and which of them are started.
 */
@objid ("07dbe9c3-edc0-11e1-88ee-001ec947c8cc")
class ModuleRegistry implements IModuleRegistry {
    @objid ("1e65d837-edc3-11e1-88ee-001ec947c8cc")
    private Map<ModuleComponent, IModule> loadedModules = new HashMap<>();

    @objid ("1e683a92-edc3-11e1-88ee-001ec947c8cc")
    private Map<ModuleComponent, IModule> startedModules = new HashMap<>();

    /**
     * Get the started modules.
     * @return The started modules.
     */
    @objid ("1e683a96-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public Collection<IModule> getStartedModules() {
        return this.startedModules.values();
    }

    /**
     * Get the started {@link IModule} corresponding to the given
     * {@link ModuleComponent}.
     * @param model the module model.
     * @return the matching started module or <i>null</i> if no started module
     * matches the <i>IModule</i>
     */
    @objid ("1e683a9e-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public IModule getStartedModule(ModuleComponent model) {
        return this.startedModules.get(model);
    }

    /**
     * Adds a module to the list of started modules
     * @param module the started module.
     */
    @objid ("1e683aa5-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public void addStartedModule(IModule module) {
        final ModuleComponent model = module.getModel();
        if (model != null) {
            this.startedModules.put(model, module);
        }
    }

    /**
     * Remove a module from the list of started modules.
     * @param module the stopped module.
     */
    @objid ("1e6a9ceb-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public void removeStartedModule(IModule module) {
        this.startedModules.remove(module.getModel());
    }

    /**
     * Adds a module to the list of loaded modules
     * @param module the loaded module.
     */
    @objid ("1e6a9cf0-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public void addLoadedModule(IModule module) {
        final ModuleComponent model = module.getModel();
        if (model != null) {
            this.loadedModules.put(model, module);
        }
    }

    /**
     * Get the loaded {@link IModule} corresponding to the given
     * {@link ModuleComponent}.
     * @param model the module model.
     * @return the matching loaded module or <i>null</i> if no loaded module
     * matches the <i>IModule</i>
     */
    @objid ("1e6a9cf5-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public IModule getLoadedModule(ModuleComponent model) {
        return this.loadedModules.get(model);
    }

    /**
     * Remove a module from the list of loaded modules.
     * @param module the unloaded module.
     */
    @objid ("1e6a9cfc-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public void removeLoadedModule(IModule module) {
        this.loadedModules.remove(module.getModel());
    }

    /**
     * Get the loaded modules.
     * @return the loaded modules.
     */
    @objid ("1e6a9d01-edc3-11e1-88ee-001ec947c8cc")
    @Override
    public Collection<IModule> getLoadedModules() {
        return this.loadedModules.values();
    }

    /**
     * Get the loaded {@link IModule} which name correspond to the given
     * {@link ModuleId} name and which version is newer or equal to the given
     * version.
     * @param moduleId the Id of the searched module.
     * @return the matching loaded module or <code>null</code> if no loaded module
     * matches the ModuleId.
     */
    @objid ("bc47fc61-f37d-11e1-9458-001ec947c8cc")
    @Override
    public IModule getLoadedModule(ModuleId moduleId) {
        for (IModule module : this.loadedModules.values()) {
            if (module.getName().equals(moduleId.getName())) {
                if (module.getVersion().equals(moduleId.getVersion())
                        || module.getVersion().isNewerThan(moduleId.getVersion())) {
                    return module;
                }
            }
        }
        return null;
    }

    /**
     * Get the started {@link IModule} which name correspond to the given
     * {@link ModuleId} name and which version is newer or equal to the given
     * version.
     * @param moduleId the Id of the searched module.
     * @return the matching started module or <code>null</code> if no started module
     * matches the ModuleId.
     */
    @objid ("bc47fc67-f37d-11e1-9458-001ec947c8cc")
    @Override
    public IModule getStartedModule(ModuleId moduleId) {
        for (IModule module : this.startedModules.values()) {
            if (module.getName().equals(moduleId.getName())) {
                if (module.getVersion().equals(moduleId.getVersion())
                        || module.getVersion().isNewerThan(moduleId.getVersion())) {
                    return module;
                }
            }
        }
        return null;
    }

}
