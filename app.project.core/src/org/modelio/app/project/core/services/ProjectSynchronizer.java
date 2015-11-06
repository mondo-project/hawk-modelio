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
                                    

package org.modelio.app.project.core.services;

import java.io.IOException;
import java.nio.file.Path;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.modelio.api.module.ModuleException;
import org.modelio.core.ui.progress.ModelioProgressAdapter;
import org.modelio.gproject.data.project.AuthDescriptor;
import org.modelio.gproject.data.project.ModuleDescriptor;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.gproject.GProjectAuthenticationException;
import org.modelio.gproject.gproject.GProjectConfigurer;
import org.modelio.gproject.module.GModule;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.vbasic.auth.IAuthData;
import org.modelio.vbasic.net.UriPathAccess;
import org.modelio.vbasic.progress.IModelioProgress;

/**
 * Service class that synchronize a {@link GProject} against its remote configuration.
 * <p>
 * Installs, upgrade or remove modules and fragments when needed.
 * <p>
 * Usage:<ul>
 * <li> allocate with {@link #ProjectSynchronizer(GProject, IModuleService)}
 * <li> run with {@link #synchronize(IProgressMonitor)}
 * <li> if no exception, call {@link #getFailures()} to display potential problems (if any) to the user.
 * </ul>
 */
@objid ("7b94b9c5-be61-4e61-90e9-16be4c6e853d")
public class ProjectSynchronizer extends GProjectConfigurer {
    @objid ("ef392239-de66-4338-9a57-a8669c2bf71c")
    private IModuleService moduleService;

    /**
     * Initialize the project configurer.
     * @param project the project to synchronize
     * @param moduleService the module service used to install and remove modules.
     */
    @objid ("7bb1e59a-4a10-4959-b242-8c1566bf63e5")
    public ProjectSynchronizer(GProject project, IModuleService moduleService) {
        super(project);
        this.moduleService = moduleService;
    }

    /**
     * Synchronize the project against its configuration.
     * @param monitor the progress monitor to use for reporting progress to the user. It is the caller's responsibility to call
     * <code>done()</code> on the given monitor. Accepts <code>null</code>, indicating that no progress should be
     * reported and that the operation cannot be cancelled.
     * @return <code>true</code> if the configuration was changed, <code>false</code> if no change was made.
     * @throws java.io.IOException if the project descriptor couldn't be read.
     * @throws org.modelio.gproject.gproject.GProjectAuthenticationException in case of authentication failure
     */
    @objid ("ebef6208-8ac1-4509-ae96-78b8755d4e6f")
    public boolean synchronize(IProgressMonitor monitor) throws GProjectAuthenticationException, IOException {
        ModelioProgressAdapter mon = new ModelioProgressAdapter(monitor);
        return synchronize(mon);
    }

    @objid ("f1648aa4-b7c0-4707-b43e-22d73e12f03c")
    @Override
    protected void installModule(ModuleDescriptor fd, IModelioProgress mon) throws IOException {
        AuthDescriptor authDesc = fd.getAuthDescriptor();
        IAuthData authData = (authDesc != null)? authDesc.getData() : null;
        try (UriPathAccess access = new UriPathAccess(fd.getArchiveLocation(),authData)){
            // Install the module as if the user asked for it
            Path archivePath = access.getPath();
            this.moduleService.installModule(getProject(), archivePath);
            
            // Overwrite default module parameters with the server parameters
            reconfigureModule(findModule(fd.getName()), fd, mon);
            
        } catch (ModuleException e) {
            throw new IOException(e);
        }
    }

    @objid ("6949d2fb-0fac-4d25-bee5-2ddc49c59c7a")
    @Override
    protected void removeModule(GModule m, IModelioProgress mon) throws IOException {
        mon.subTask("Removing "+m.getName()+" "+m.getVersion()+"...");
        try {
            this.moduleService.removeModule(m);
        } catch (ModuleException e) {
            throw new IOException(e);
        }
    }

    @objid ("4fbda55c-3723-4084-9ae2-b2ab8d1f6463")
    @Override
    protected void upgradeModule(GModule m, ModuleDescriptor fd, IModelioProgress mon) throws IOException {
        AuthDescriptor authDesc = fd.getAuthDescriptor();
        IAuthData authData = (authDesc != null)? authDesc.getData() : null;
        try (UriPathAccess access = new UriPathAccess(fd.getArchiveLocation(),authData)){
            // Install the module as if the user asked for it
            // The module may change some parameters on upgrade, they won't be lost.
            Path archivePath = access.getPath();
            this.moduleService.installModule(getProject(), archivePath);
            
            // Overwrite default module parameters with the server parameters
            // Note: 'm' is invalid after installModule(...)
            reconfigureModule(findModule(fd.getName()), fd, mon);
            
        } catch (ModuleException e) {
            throw new IOException(e);
        }
    }

    @objid ("006ee00d-d704-4630-9af6-3c0a68e54a0c")
    protected GModule findModule(String name) {
        for (GModule g : getProject().getModules())
            if (g.getName().equals(name))
                return g;
        return null;
    }

}
