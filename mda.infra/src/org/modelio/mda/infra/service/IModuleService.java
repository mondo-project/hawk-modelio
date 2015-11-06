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

import java.nio.file.Path;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IPeerModule;
import org.modelio.api.module.ModuleException;
import org.modelio.app.core.IModelioService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.module.GModule;
import org.modelio.metamodel.mda.ModuleComponent;

/**
 * Modelio modules service interface.
 */
@objid ("895d3ad9-f1b7-11e1-af52-001ec947c8cc")
public interface IModuleService extends IModelioService {
    /**
     * Get the public services of a specific module.
     * <p>
     * This method needs the concrete interface of a module, to return the
     * loaded instance of this peer module.
     * <p>
     * For example, the following example shows a call of this method to return
     * a specific module named MyModule:
     * <p>
     * <code>
     * CoreSession session = Modelio.getInstance().getModelingSession();<br>
     * IMyPeerModule peerModule = session.getPeerModule(IMyPeerModule.class);
     * </code>
     * <p>
     * The returned peer module can be casted without risk to the right desired
     * module.
     * @param <T> the interface of the searched peer module.
     * @param metaclass the interface of the searched peer module.
     * @return the peer module regarding the given metaclass
     * @throws org.modelio.mda.infra.service.UnknownModuleException when the required module is not found.
     */
    @objid ("2bb63efb-f1ed-11e1-af52-001ec947c8cc")
    <T extends IPeerModule> T getPeerModule(Class<T> metaclass) throws UnknownModuleException;

    /**
     * Load and Start all activated non started modules of the given project.
     * @param project the project to start all activated modules of.
     * @param aMonitor optional progress monitor, may be <code>null</code>
     */
    @objid ("2bb63f02-f1ed-11e1-af52-001ec947c8cc")
    void startAllModules(GProject project, final IProgressMonitor aMonitor);

    /**
     * Stop all started modules and unloads all loaded modules of the given
     * project.
     * @param project the project to stop all modules of.
     */
    @objid ("2bb63f07-f1ed-11e1-af52-001ec947c8cc")
    void stopAllModules(GProject project);

    /**
     * Installs, load and start the module contained in the given file in the
     * given project. This method adds (or update) a module in
     * the given GProject, then load and start the corresponding {@link IModule}.
     * @param gProject the project to install the module into.
     * @param moduleFilePath the path to the file of the module.
     * @throws org.modelio.api.module.ModuleException if an error occurred while trying to install the module.
     */
    @objid ("2bb63f0d-f1ed-11e1-af52-001ec947c8cc")
    void installModule(GProject gProject, Path moduleFilePath) throws ModuleException;

    /**
     * Activates and starts the given module. This method does NOT activate nor
     * start modules required by the given module.
     * @param module the module to activate.
     * @throws org.modelio.api.module.ModuleException if an error occurred while trying to activate the module.
     */
    @objid ("2bb63f11-f1ed-11e1-af52-001ec947c8cc")
    void activateModule(GModule module) throws ModuleException;

    /**
     * Stops and deactivates the given module. Modules requiring the given
     * module will be stopped first.
     * @param module the module to deactivate.
     * @throws org.modelio.api.module.ModuleException if an error occurred while trying to deactivate the module.
     */
    @objid ("2bb8a12c-f1ed-11e1-af52-001ec947c8cc")
    void deactivateModule(GModule module) throws ModuleException;

    /**
     * Returns the ModuleRegistry which contains the list of all loaded IModule
     * and the list of all started IModule.
     * @return the {@link IModuleRegistry}
     */
    @objid ("2bb8a12f-f1ed-11e1-af52-001ec947c8cc")
    IModuleRegistry getModuleRegistry();

    /**
     * Stops, unload and removes a module.
     * @param module the module to remove.
     * @throws org.modelio.api.module.ModuleException if an error occurred while trying to remove the module.
     */
    @objid ("b61165c6-0d64-11e2-ae8f-002564c97630")
    void removeModule(GModule module) throws ModuleException;

    /**
     * Returns the started IModule corresponding to the passed ModuleComponent or <code>null</code> if none is found.
     * @param moduleComponent the ModuleComponent to search a started IModule for.
     * @return the started IModule corresponding to the passed ModuleComponent or <code>null</code> if none is found.
     */
    @objid ("61ca78e5-186b-11e2-92d2-001ec947c8cc")
    IModule getIModule(ModuleComponent moduleComponent);

}
