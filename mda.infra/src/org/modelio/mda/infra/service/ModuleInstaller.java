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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.gproject.data.project.GProperties.Entry;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.module.GModule;
import org.modelio.gproject.module.IModuleHandle;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.vbasic.version.Version;

/**
 * @author phv
 */
@objid ("eabbc60d-01a1-11e2-9fca-001ec947c8cc")
class ModuleInstaller {
    @objid ("99525452-0fac-4e5b-aa9e-0f0d2ac5b9f5")
    private ModuleService moduleService;

    @objid ("e153cfd1-9766-48a9-8940-bf89249e797b")
    private GProject gProject;

    @objid ("44326855-0324-11e2-9fca-001ec947c8cc")
    GModule moduleUpdateInstall(GModule gModule, IModuleHandle rtModuleHandle, GModule gModuleAlreadyInstalled) throws ModuleException {
        Collection<GModule> dependentGModules = stopDependentModules(rtModuleHandle);
        
        // Get the previous values of the module parameters
        Map<String, String> oldParameters = new HashMap<>();
        for (Entry entry : gModuleAlreadyInstalled.getParameters().entries()) {
            oldParameters.put(entry.getName(), entry.getValue());
        }
        
        // Call static method #install on module main class so that post-unzip/pre-loading operations can be done.
        callStaticMethodInstall(gModule, rtModuleHandle);
        
        // Load, upgrade and start the module.
        IModule updatedIModule = this.moduleService.loadModule(this.gProject, gModule);
        upgradeAndActivateModule(updatedIModule, gModuleAlreadyInstalled.getVersion(), oldParameters);
        
        
        restartDependentModules(dependentGModules);
        return null;
    }

    @objid ("4434caad-0324-11e2-9fca-001ec947c8cc")
    GModule moduleFirstInstall(GModule gModule, IModuleHandle rtModuleHandle) throws ModuleException {
        Collection<GModule> dependentGModules = stopDependentModules(rtModuleHandle);
        
        // Call static method #install on module main class so that
        // post-unzip/pre-loading operations can be
        // done.
        callStaticMethodInstall(gModule, rtModuleHandle);
        // Load, select and start the module.
        IModule iModule = this.moduleService.loadModule(this.gProject, gModule);
        selectAndActivateModule(iModule);
        
        restartDependentModules(dependentGModules);
        return gModule;
    }

    @objid ("44372cff-0324-11e2-9fca-001ec947c8cc")
    private void callStaticMethodInstall(GModule gModule, IModuleHandle rtModuleHandle) throws ModuleException {
        // We need to "preload" the module so that we can actually access its
        // main class to call the static method on.
        // Resolve all loaded dependencies
        List<IModule> loadedDependencies = ModuleResolutionHelper.getGModuleDependsOnIModules(gModule, this.gProject, this.moduleService);
        loadedDependencies.addAll(ModuleResolutionHelper.getGModuleActivatedWeakDependenciesIModules(gModule, this.gProject, this.moduleService));
        // Resolve and update it classpath
        
        List<Path> runtimeJarPaths = ModuleLoader.getModuleJarPaths(rtModuleHandle);
        // Construct a class loader on these informations.
        // NOTE: the call to #install may actually bring in something that will
        // complete the class loader created in
        // ModuleLoader#loadModule
        try (URLClassLoader classLoader = ModuleLoader.setupClassLoader(rtModuleHandle, loadedDependencies, runtimeJarPaths)) {
            Class<?> mainClass = null;
            try {
                mainClass = classLoader.loadClass(rtModuleHandle.getMainClassName());
            } catch (ClassNotFoundException e) {
                MdaInfra.LOG.error(e);
                // Could not find main class in class loader, load a placeholder
                // instead
                mainClass = IncompatibleModule.class;
                // Add some log
                ModuleException e2 = new ModuleException(String.format(
                        "The '%1$s' module class has not been found.\n (%2$s)", rtModuleHandle
                        .getMainClassName(), e.getMessage()));
                e2.initCause(e);
                MdaInfra.LOG.error(e2);
            } catch (NoClassDefFoundError e) {
                MdaInfra.LOG.error(e);
                // Could not find main class in class loader, load a placeholder
                // instead
                mainClass = IncompatibleModule.class;
            }
            try {
        
                // Declare the parameters of the install method.
                Class<?>[] classParamArray = { String.class, String.class };
                Object[] initParamArray = { this.gProject.getProjectPath(), rtModuleHandle.getResourcePath() };
        
                Method installMethod = mainClass.getMethod("install", classParamArray);
        
                // Install method invocation.
                installMethod.invoke(null, initParamArray);
        
            } catch (NullPointerException npe) {
                MdaInfra.LOG.error(npe);
                ModuleException e2 = new ModuleException(MdaInfra.I18N.getMessage("ModuleExceptionMessage.InstallIsNotStatic", rtModuleHandle.getName())); //$NON-NLS-1$
                e2.initCause(npe);
                throw e2;
            } catch (SecurityException e) {
                MdaInfra.LOG.error(e);
                ModuleException e2 = new ModuleException(String.format(
                        "Security violation while loading the '%1$s' module:\n %2$s ", rtModuleHandle.getName(),
                        e.getMessage()));
                e2.initCause(e);
                throw e2;
            } catch (NoClassDefFoundError e) {
                MdaInfra.LOG.error(e);
                // dump the classpath
                classLoader.getURLs();
                StringBuilder sUrls = new StringBuilder();
                for (URL url : classLoader.getURLs()) {
                    sUrls.append(" - ");
                    sUrls.append(url.toString());
                    sUrls.append("\n");
                }
        
                ModuleException e2 = new ModuleException(String.format(
                        "The '%1$s' module class couldn't find the '%2$s' class in the following classpath:\n (%3$s)",
                        rtModuleHandle.getMainClassName(), e.getMessage(), sUrls));
                e2.initCause(e);
                throw e2;
            } catch (NoSuchMethodException e) {
                // No method to call...
            } catch (IllegalAccessException e) {
                MdaInfra.LOG.error(e);
                ModuleException e2 = new ModuleException(String.format(
                        "Illegal access error occured while initalizing the '%1$s' module: %2$s ", rtModuleHandle.getName(),
                        e.getMessage()));
                e2.initCause(e);
                throw e2;
            } catch (InvocationTargetException e) {
                MdaInfra.LOG.error(e);
                Throwable cause = e.getCause();
                if (cause == null) {
                    cause = e;
                }
                ModuleException e2 = new ModuleException(String.format(
                        "Exception thrown while initalizing the '%1$s' module: %2$s ", rtModuleHandle.getName(),
                        cause.getMessage()));
                e2.initCause(cause);
                throw e2;
            }
        } catch (IOException e) {
            MdaInfra.LOG.error(e);
        }
    }

    @objid ("44372d04-0324-11e2-9fca-001ec947c8cc")
    private void selectAndActivateModule(IModule iModule) throws ModuleException {
        try {
            boolean selectSuccessful = iModule.getSession().select();
        
            if (selectSuccessful) {
                // Note the module as activated
                this.moduleService.activateModule(iModule, this.gProject);
            } else {
                // Note as deactivated.
                this.moduleService.deactivateModule(iModule, this.gProject);
            }
        } catch (ModuleException e) {
            MdaInfra.LOG.error(e);
            // Note as deactivated.
            this.moduleService.deactivateModule(iModule, this.gProject);
            throw e;
        } catch (RuntimeException | Error e) {
            MdaInfra.LOG.error(e);
            // Note as deactivated.
            this.moduleService.deactivateModule(iModule, this.gProject);
            ModuleException e2 = new ModuleException(
                    String.format(
                            "Exception thrown while selecting the '%1$s' module: %2$s . \n\nThe stack trace is available in the Modelio log file.\nReport it with the error to the module developer.",
                            iModule.getName(), e.getLocalizedMessage()));
            e2.initCause(e);
            throw e2;
        
        }
    }

    @objid ("2d48b334-0c90-11e2-a703-001ec947c8cc")
    private void upgradeAndActivateModule(IModule iModule, Version oldVersion, Map<String, String> oldParameters) throws ModuleException {
        try {
            iModule.getSession().upgrade(oldVersion, oldParameters);
            // Note the module as activated
            this.moduleService.activateModule(iModule, this.gProject);
        } catch (ModuleException e) {
            MdaInfra.LOG.error(e);
            // Note as deactivated.
            this.moduleService.deactivateModule(iModule, this.gProject);
            throw e;
        } catch (RuntimeException | Error e) {
            MdaInfra.LOG.error(e);
            // Note as deactivated.
            this.moduleService.deactivateModule(iModule, this.gProject);
            ModuleException e2 = new ModuleException(
                    String.format(
                            "Exception thrown while selecting the '%1$s' module: %2$s . \n\nThe stack trace is available in the Modelio log file.\nReport it with the error to the module developer.",
                            iModule.getName(), e.getLocalizedMessage()));
            e2.initCause(e);
            throw e2;
        
        }
    }

    @objid ("a9653cb9-9432-43db-b7b3-5c63afda0241")
    public ModuleInstaller(GProject gProject, ModuleService moduleService) {
        this.gProject = gProject;
        this.moduleService = moduleService;
    }

    /**
     * Stop and unload all dependent modules so they can be restarted
     * with the dependency filled once the
     * module is installed
     * @param rtModuleHandle the handle of the module whose dependencies must be stopped.
     * @return all stopped dependent modules
     * @throws org.modelio.api.module.ModuleException in case of failure
     */
    @objid ("acef8e0f-535e-47f4-9e3c-08e25b7a07fc")
    private Collection<GModule> stopDependentModules(IModuleHandle rtModuleHandle) throws ModuleException {
        List<GModule> dependentGModules = ModuleResolutionHelper.getModuleHandleDependentGModules(rtModuleHandle, this.gProject);
        
        for (GModule dependentGModule : dependentGModules) {
            IModule started = this.moduleService.getModuleRegistry().getStartedModule(dependentGModule.getModuleElement());
            if (started != null) {
                this.moduleService.stopModule(started, this.gProject);
            }
        
            IModule loaded = this.moduleService.getModuleRegistry().getLoadedModule(dependentGModule.getModuleElement());
            if (loaded != null) {
                this.moduleService.unloadModule(loaded, this.gProject);
            }
        }
        return dependentGModules;
    }

    /**
     * Reload and restart all previously stopped modules
     */
    @objid ("b668ef79-d796-4fcf-841f-64b8ad0fbf8d")
    private void restartDependentModules(Collection<GModule> dependentGModules) throws ModuleException {
        for (GModule dependentGModule : dependentGModules) {
            this.moduleService.loadModule(this.gProject, dependentGModule);
        }
        for (GModule dependentGModule : dependentGModules) {
            IModule loadedModule = this.moduleService.getModuleRegistry().getLoadedModule(dependentGModule.getModuleElement());
            if (loadedModule != null) {
                this.moduleService.startModule(loadedModule, this.gProject);
            }
        }
    }

}
