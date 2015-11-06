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
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.gproject.module.ModuleId;
import org.modelio.metamodel.mda.ModuleComponent;

/**
 * This interface:
 * <ul>
 * <li>gives access to loaded and started modules.</li>
 * <li>provides the API that maps an {@link ModuleComponent} to a loaded
 * {@link IModule}.</li>
 * </ul>
 * Any module installed in a project is loaded when the project starts and is
 * added to the loaded modules list. When an module is started it is added in
 * the started modules list.
 */
@objid ("c1468600-edc2-11e1-88ee-001ec947c8cc")
public interface IModuleRegistry {
    /**
     * Get the started modules.
     * @return The started modules.
     */
    @objid ("1e6375df-edc3-11e1-88ee-001ec947c8cc")
    Collection<IModule> getStartedModules();

    /**
     * Get the started {@link IModule} corresponding to the given
     * {@link ModuleComponent}.
     * @param model the module model.
     * @return the matching started module or <i>null</i> if no started module
     * matches the <i>IModule</i>
     */
    @objid ("1e6375e4-edc3-11e1-88ee-001ec947c8cc")
    IModule getStartedModule(ModuleComponent model);

    /**
     * Adds a module to the list of started modules
     * @param module the started module.
     */
    @objid ("1e6375e8-edc3-11e1-88ee-001ec947c8cc")
    void addStartedModule(IModule module);

    /**
     * Remove a module from the list of started modules.
     * @param module the stopped module.
     */
    @objid ("1e6375eb-edc3-11e1-88ee-001ec947c8cc")
    void removeStartedModule(IModule module);

    /**
     * Adds a module to the list of loaded modules
     * @param module the loaded module.
     */
    @objid ("1e6375ee-edc3-11e1-88ee-001ec947c8cc")
    void addLoadedModule(IModule module);

    /**
     * Get the loaded {@link IModule} corresponding to the given
     * {@link ModuleComponent}.
     * @param model the module model.
     * @return the matching loaded module or <i>null</i> if no loaded module
     * matches the <i>IModule</i>
     */
    @objid ("1e6375f1-edc3-11e1-88ee-001ec947c8cc")
    IModule getLoadedModule(ModuleComponent model);

    /**
     * Remove a module from the list of loaded modules.
     * @param module the unloaded module.
     */
    @objid ("1e6375f5-edc3-11e1-88ee-001ec947c8cc")
    void removeLoadedModule(IModule module);

    /**
     * Get the loaded modules.
     * @return the loaded modules.
     */
    @objid ("1e6375f8-edc3-11e1-88ee-001ec947c8cc")
    Collection<IModule> getLoadedModules();

    /**
     * Get the loaded {@link IModule} which name correspond to the given
     * {@link ModuleId} name and which version is newer or equal to the given
     * version.
     * @param moduleId the Id of the searched module.
     * @return the matching loaded module or <code>null</code> if no loaded module
     * matches the ModuleId.
     */
    @objid ("bc4337ab-f37d-11e1-9458-001ec947c8cc")
    IModule getLoadedModule(ModuleId moduleId);

    /**
     * Get the started {@link IModule} which name correspond to the given
     * {@link ModuleId} name and which version is newer or equal to the given
     * version.
     * @param moduleId the Id of the searched module.
     * @return the matching started module or <code>null</code> if no started module
     * matches the ModuleId.
     */
    @objid ("bc4337af-f37d-11e1-9458-001ec947c8cc")
    IModule getStartedModule(ModuleId moduleId);

}
