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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.modelio.api.model.IModelingSession;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.IModule.ModuleRuntimeState;
import org.modelio.api.module.IModule;
import org.modelio.api.module.IModuleAPIConfiguration;
import org.modelio.api.module.IModuleUserConfiguration;
import org.modelio.api.module.ModuleException;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.module.GModule;
import org.modelio.gproject.module.IModuleHandle;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.metamodel.mda.ModuleComponent;
import org.osgi.framework.Bundle;

@objid ("8a81c9bc-f34b-11e1-9458-001ec947c8cc")
class ModuleLoader {
    @objid ("7f2a6960-0263-11e2-9fca-001ec947c8cc")
    private static final String FAKE_MODULE_CLASS_NAME = "<FAKE>";

    @objid ("8d38afe4-a9a0-40c6-9a15-e8be1e7de551")
    private IMModelServices mModelServices;

    @objid ("b91d1222-f3cf-44b4-99a9-138b18328063")
    private GProject gProject;

    @objid ("1e2e9bae-ee74-4601-85cb-69acde94b67a")
    private Path projectMdaRuntimePath;

    @objid ("8a81c9ac-f34b-11e1-9458-001ec947c8cc")
    @SuppressWarnings("resource")
    public IModule loadModule(GModule gModule, final IModuleHandle rtModuleHandle, List<IModule> loadedDependencies) throws ModuleException {
        // No valid RT module means the module is broken
        if (rtModuleHandle == null) {
            return new BrokenModule(Modelio.getInstance().getModelingSession(),
                    gModule.getName(), gModule.getVersion(), null, null);
        }
        
        // 1) Setup the module class loader:
        // - collect the module resources directory and add it in a list with
        // all files in the 'classpath'
        // administrative infos
        // - collect all required and available weak dependency modules class
        // loaders
        // - collect all required plugins class loaders
        // - build a class loader from both the list of path and the list of
        // class loaders.
        List<Path> runtimeJarPaths = rtModuleHandle.getJarPaths();
        URLClassLoader classLoader = setupClassLoader(rtModuleHandle,
                loadedDependencies, runtimeJarPaths);
        
        // 2) Get and load the module main class
        String mainClassName = rtModuleHandle.getMainClassName();
        if (mainClassName.equals(FAKE_MODULE_CLASS_NAME)) {
            // Fake module is added to the registry to get access to stereotype
            // icons
            mainClassName = FakeModule.class.getName();
        }
        IModule loadedIModule = instantiateModuleMainClass(gModule,
                rtModuleHandle, classLoader, mainClassName,
                this.gProject.getProjectPath());
        
        // 3) Load the dynamic part of the module (contextual menu
        // contributions, diagram tools, etc)
        if (!loadedIModule.getName().equals("LocalModule")
                && loadedIModule.getState() != ModuleRuntimeState.Incompatible) {
            try {
                Path dynamicModelPath = rtModuleHandle.getDynamicModelPath();
                if (Files.exists(dynamicModelPath)
                        && Files.isRegularFile(dynamicModelPath)) {
                    ModuleImporter.loadDynamicModel(dynamicModelPath,
                            loadedIModule, this.mModelServices);
                }
            } catch (IOException e) {
                ModuleException e2 = new ModuleException(
                        MdaInfra.I18N.getMessage("L1", loadedIModule.getName(),
                                e.getMessage()));
                e2.initCause(e);
                throw e2;
            }
        }
        return loadedIModule;
    }

    /**
     * Creates a ClassLoader for the given module and local class path.
     * @param rtModuleHandle The module
     * @param loadedDependencies the list of all loaded IModules the current module depends on
     * (either strongly or weakly)
     * @param declaredClasspath The local class path for the module
     * @return a class loader
     * @throws org.modelio.api.module.ModuleException in case of error
     */
    @objid ("8a81c9af-f34b-11e1-9458-001ec947c8cc")
    static URLClassLoader setupClassLoader(IModuleHandle rtModuleHandle, List<IModule> loadedDependencies, List<Path> declaredClasspath) throws ModuleException {
        List<URL> completeClassPath = new ArrayList<>();
        List<ClassLoader> parentLoaders = new ArrayList<>();
        
        Path fClassPath = rtModuleHandle.getResourcePath();
        if (fClassPath == null) {
            return new ModuleClassLoader(
                    rtModuleHandle.getName(),
                    completeClassPath.toArray(new URL[completeClassPath.size()]),
                    parentLoaders);
        }
        
        try {
            // Add the module path to the loader classpath (for resources
            // loading)
            completeClassPath.add(fClassPath.toUri().toURL());
        
            // Add the module declared libraries to the classpath
            if (declaredClasspath == null || declaredClasspath.isEmpty()) {
                String msg = String.format(
                        "The '%1$s' module classpath is empty!",
                        rtModuleHandle.getName());
                ModuleException e2 = new ModuleException(msg);
                throw e2;
            } else {
                for (Path path : declaredClasspath) {
                    try {
                        completeClassPath.add(fClassPath.resolve(path).toUri().toURL());
                    } catch (MalformedURLException e) {
                        String msg = String
                                .format("The '%1$s' module '%2$s' classpath entry is invalid:\n %3$s",
                                        rtModuleHandle.getName(),
                                        path.toString(),
                                        e.getLocalizedMessage());
                        ModuleException e2 = new ModuleException(msg);
                        e2.initCause(e);
                        throw e2;
                    }
                }
            }
        
            // Collect class loaders of some plugins modules should have access
            // to.
            addPluginsClassLoaders(parentLoaders);
        
            // Get required modules class loaders
            for (IModule im : loadedDependencies) {
                parentLoaders.add(im.getClass().getClassLoader());
        
            }
        
            return new ModuleClassLoader(
                    rtModuleHandle.getName(),
                    completeClassPath.toArray(new URL[completeClassPath.size()]),
                    parentLoaders);
        
        } catch (MalformedURLException e) {
            String msg = String.format(
                    "The '%1$s' module '%2$s' classpath is invalid:\n %3$s",
                    rtModuleHandle.getName(), fClassPath.toString(),
                    e.getLocalizedMessage());
            ModuleException e2 = new ModuleException(msg);
            e2.initCause(e);
            throw e2;
        }
    }

    @objid ("7f103033-0263-11e2-9fca-001ec947c8cc")
    static List<Path> getModuleJarPaths(IModuleHandle rtModuleHandle) {
        if (rtModuleHandle == null) {
            return Collections.emptyList();
        }
        
        List<Path> absolutJarPaths = new ArrayList<>();
        for (Path relativeJarPath :  rtModuleHandle.getJarPaths()) {
            absolutJarPaths.add(rtModuleHandle.getResourcePath().resolve(relativeJarPath));
        }
        return absolutJarPaths;
    }

    @objid ("8a81c9a7-f34b-11e1-9458-001ec947c8cc")
    private static void addPluginClassLoader(List<ClassLoader> parentLoaders, String pluginId, String className) {
        Bundle bundle = Platform.getBundle(pluginId);
        // Depending on Modelio edition/packaging, all bundles might not be
        // present.
        if (bundle != null) {
            try {
                Class<?> clazz = bundle.loadClass(className);
                ClassLoader loader = clazz.getClassLoader();
                parentLoaders.add(loader);
            } catch (ClassNotFoundException e) {
                // Should not happen: if the bundle was found, the class should
                // be found too.
                MdaInfra.LOG.error(e);
            }
        }
    }

    @objid ("8a81c9b3-f34b-11e1-9458-001ec947c8cc")
    private static void addPluginsClassLoaders(List<ClassLoader> parentLoaders) {
        addPluginClassLoader(parentLoaders, "org.modelio.api", "org.modelio.api.module.IModule");
        addPluginClassLoader(parentLoaders, "org.modelio.core.utils", "org.modelio.vbasic.version.Version");
        
        
        addPluginClassLoader(parentLoaders, "com.modeliosoft.modelio.app.svn", "com.modeliosoft.modelio.cms.api.ICmsServices");
        addPluginClassLoader(parentLoaders, "com.modeliosoft.modelio.matrix", "com.modeliosoft.modelio.matrix.api.IMatrixService");
    }

    @objid ("7f10303c-0263-11e2-9fca-001ec947c8cc")
    private static Path copyToRuntimePath(Path relativeFilePath, Path moduleResourceBasePath, Path moduleRuntimeBasePath) {
        Path sourcePath = moduleResourceBasePath.resolve(relativeFilePath);
        Path destinationPath = getRuntimeJarFile(moduleRuntimeBasePath,
                relativeFilePath);
        try {
            Files.createDirectories(destinationPath.getParent());
            Files.copy(sourcePath, destinationPath,
                    StandardCopyOption.COPY_ATTRIBUTES);
            return destinationPath;
        } catch (IOException e) {
            MdaInfra.LOG.error(e);
        }
        return null;
    }

    /**
     * Returns the first "available" path (i.e. there is not an already existing
     * file with this path) in moduleRuntimeBasePath.
     * @param moduleRuntimeBasePath the module runtime path
     * @param relativFilePath a file path relative to the runtime path
     * @return the first "available" path .
     */
    @objid ("7f103043-0263-11e2-9fca-001ec947c8cc")
    private static Path getRuntimeJarFile(Path moduleRuntimeBasePath, Path relativFilePath) {
        final String jarName = relativFilePath.toString().substring(0,
                relativFilePath.toString().lastIndexOf(".jar"));
        
        Path runTimeJarFile = moduleRuntimeBasePath.resolve(relativFilePath);
        
        int cpt = 0;
        while (Files.exists(runTimeJarFile)) {
            runTimeJarFile = moduleRuntimeBasePath.resolve(jarName + "_" + cpt
                    + ".jar");
            ++cpt;
        }
        return runTimeJarFile;
    }

    /**
     * Fetch by reflexion the main class of this module and instantiate it.
     * @param rtModuleHandle the module to load
     * @param classLoader the module class loader
     * @param mainClassName the module main class name
     * @return the loaded module
     * @throws org.modelio.api.module.ModuleException in case of error
     */
    @objid ("7f175707-0263-11e2-9fca-001ec947c8cc")
    private static IModule instantiateModuleMainClass(final GModule gModule, final IModuleHandle rtModuleHandle, URLClassLoader classLoader, String mainClassName, Path projectspacePath) throws ModuleException {
        // Clear the ResourceBundles cache for the module class loader,
        // otherwise the module resources bundle are not
        // found when upgrading a module and non understandable
        // MissingResourceException are thrown.
        ResourceBundle.clearCache(classLoader);
        
        // Get the shared modeling session for the module
        IModelingSession apiSession = Modelio.getInstance()
                .getModelingSession();
        
        // Prepare the ModuleConfiguration objects for the module.
        ModuleConfiguration moduleUserConfiguration = new ModuleConfiguration(
                gModule, projectspacePath, rtModuleHandle.getResourcePath(),
                rtModuleHandle.getDocPaths(), rtModuleHandle.getStylePaths());
        
        PeerModuleConfiguration moduleApiConfiguration = new PeerModuleConfiguration(
                gModule, projectspacePath, rtModuleHandle.getResourcePath(),
                rtModuleHandle.getDocPaths());
        
        Class<?> mainClass = null;
        if (isIncompatible(rtModuleHandle)) {
            mainClass = IncompatibleModule.class;
        } else {
            try {
                mainClass = classLoader.loadClass(mainClassName);
            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                MdaInfra.LOG.warning(e);
                // Could not find main class in class loader, load a placeholder
                // instead
                return new BrokenModule(apiSession, rtModuleHandle.getName(),
                        rtModuleHandle.getVersion(), moduleUserConfiguration,
                        moduleApiConfiguration);
            }
        }
        
        try {
            // Instantiate the module:
            // Constructor parameters types
            Class<?>[] classParamArray = { IModelingSession.class,
                    ModuleComponent.class, IModuleUserConfiguration.class,
                    IModuleAPIConfiguration.class };
        
            // Get the constructor
            Constructor<?> constructor = mainClass
                    .getConstructor(classParamArray);
        
            // Create the new instance by calling the constructor.
            Object[] initParamArray = { apiSession, gModule.getModuleElement(),
                    moduleUserConfiguration, moduleApiConfiguration };
            IModule module = (IModule) constructor.newInstance(initParamArray);
        
            // Call the init() method
            module.init();
        
            return module;
        
        } catch (SecurityException e) {
            final ModuleException e2 = new ModuleException(
                    String.format(
                            "Security violation while loading the '%1$s' module: %2$s ",
                            rtModuleHandle.getName(), e.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (NoSuchMethodException e) {
            final ModuleException e2 = new ModuleException(
                    String.format(
                            "The '%1$s' module class constructor has not been found: %2$s",
                            rtModuleHandle.getName(), e.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (IllegalArgumentException e) {
            final ModuleException e2 = new ModuleException(
                    String.format(
                            "Illegal argument error while initalizing the '%1$s' module: %2$s ",
                            rtModuleHandle.getName(), e.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (InstantiationException e) {
            final ModuleException e2 = new ModuleException(String.format(
                    "Cannot instantiate the '%1$s' module class: %2$s ",
                    rtModuleHandle.getName(), e.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (IllegalAccessException e) {
            final ModuleException e2 = new ModuleException(
                    String.format(
                            "Illegal access error occured while initalizing the '%1$s' module: %2$s ",
                            rtModuleHandle.getName(), e.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (InvocationTargetException e) {
            Throwable cause = e;
            while (cause.getCause() != null)
                cause = cause.getCause();
            final ModuleException e2 = new ModuleException(
                    String.format(
                            "Exception thrown while initalizing the '%1$s' module: %2$s\n",
                            rtModuleHandle.getName(), cause.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (java.lang.ExceptionInInitializerError e) {
            Throwable cause = e.getCause();
            if (cause == null)
                cause = e;
            final ModuleException e2 = new ModuleException(String.format(
                    "Error thrown while initalizing the '%1$s' module: %2$s ",
                    rtModuleHandle.getName(), cause.getMessage()));
            e2.initCause(e);
            throw e2;
        } catch (LinkageError e) {
            Throwable cause = e.getCause();
            if (cause == null)
                cause = e;
            final ModuleException e2 = new ModuleException(
                    String.format(
                            "Linkage error thrown while initalizing the '%1$s' module: %2$s ",
                            rtModuleHandle.getName(), cause.getMessage()));
            e2.initCause(cause);
            throw e2;
        }
    }

    /**
     * Check the module version is compatible with the current Modelio
     * @param rtModuleHandle the module to check.
     * @return <code>true</code> if this module is not compatible with the
     * current Modelio.
     */
    @objid ("6fd59629-2f29-11e2-9ab7-002564c97630")
    private static boolean isIncompatible(IModuleHandle rtModuleHandle) {
        return false;
    }

    @objid ("36d66263-ae93-4f00-8eb3-993dcb071285")
    public ModuleLoader(final GProject gProject, IMModelServices mModelServices) {
        this.gProject = gProject;
        this.mModelServices = mModelServices;
        
        this.projectMdaRuntimePath = gProject.getProjectRuntimePath().resolve(
                "modules");
    }

    /**
     * This is the ClassLoader specially designed for modules.
     * <p>
     * Classes are looked up in the required module class loaders, before the
     * module own class path. That's why this class loader may have many parent
     * class loaders.
     */
    @objid ("8a81c9bd-f34b-11e1-9458-001ec947c8cc")
    private static class ModuleClassLoader extends URLClassLoader {
        /**
         * Jxbv2Module name of the class loader.<br>
         * Just for debugging purpose.
         */
        @objid ("7f508e16-0263-11e2-9fca-001ec947c8cc")
        private String moduleName;

        @objid ("8a842bfe-f34b-11e1-9458-001ec947c8cc")
        private List<ClassLoader> parents = new ArrayList<>();

        @objid ("8a842ca6-f34b-11e1-9458-001ec947c8cc")
        public ModuleClassLoader(String name, URL[] classpath, List<ClassLoader> pParents) {
            super(classpath);
            this.moduleName = name;
            this.parents = pParents;
        }

        @objid ("8a842c56-f34b-11e1-9458-001ec947c8cc")
        @Override
        public URL findResource(String name) {
            for (ClassLoader parent : this.parents) {
                URL ret = parent.getResource(name);
                if (ret != null)
                    return ret;
            }
            return super.findResource(name);
        }

        @objid ("8a842c58-f34b-11e1-9458-001ec947c8cc")
        @Override
        @SuppressWarnings("unchecked")
        public Enumeration<URL> findResources(String name) throws IOException {
            Enumeration<URL>[] tmp = new Enumeration[this.parents.size() + 1];
            int i = 0;
            for (ClassLoader parent : this.parents) {
                tmp[i] = parent.getResources(name);
                i++;
            }
            
            tmp[i] = super.findResources(name);
            return new CompoundEnumeration<>(tmp);
        }

        /**
         * Returns the search path of URLs for loading classes and resources.
         * This includes: - the parent classloaders URLs - the original list of
         * URLs specified to the constructor, - along with any URLs subsequently
         * appended by the addURL() method.
         * @return the search path of URLs for loading classes and resources.
         */
        @objid ("8a842c6e-f34b-11e1-9458-001ec947c8cc")
        @Override
        public URL[] getURLs() {
            List<URL> urls = new ArrayList<>();
            for (ClassLoader l : this.parents) {
                if (l instanceof URLClassLoader) {
                    for (URL u : ((URLClassLoader) l).getURLs()) {
                        urls.add(u);
                    }
                }
            }
            for (URL u : super.getURLs())
                urls.add(u);
            return urls.toArray(new URL[urls.size()]);
        }

        /**
         * Looks for classes first in parents classloaders
         */
        @objid ("8a842c65-f34b-11e1-9458-001ec947c8cc")
        @Override
        protected Class<? extends Object> findClass(String name) throws ClassNotFoundException {
            try {
                for (ClassLoader parent : this.parents) {
                    try {
                        return parent.loadClass(name);
                    } catch (ClassNotFoundException e) {
                        // nothing: search next
                    }
                }
                return super.findClass(name);
            } catch (Error e) {
                MdaInfra.LOG.error(MdaInfra.PLUGIN_ID, e.getClass()
                        .getSimpleName()
                        + " while looking for a class. classpath=");
                for (URL url : this.getURLs()) {
                    MdaInfra.LOG.error(MdaInfra.PLUGIN_ID,
                            " - " + url.toString());
                }
                throw e;
            }
        }

        /**
         * A useful utility class that will enumerate over an array of
         * enumerations.
         * 
         * @param <E>
         * The type of enumerated elements
         */
        @objid ("8a842bfd-f34b-11e1-9458-001ec947c8cc")
        public class CompoundEnumeration<E> implements Enumeration<E> {
            @objid ("8a842c4f-f34b-11e1-9458-001ec947c8cc")
            private int index = 0;

            @objid ("8a842c4c-f34b-11e1-9458-001ec947c8cc")
            private Enumeration<E>[] enums;

            @objid ("8a842ca7-f34b-11e1-9458-001ec947c8cc")
            public CompoundEnumeration(Enumeration<E>[] enums) {
                this.enums = enums;
            }

            @objid ("8a842c50-f34b-11e1-9458-001ec947c8cc")
            @Override
            public boolean hasMoreElements() {
                return this.next();
            }

            @objid ("8a842c51-f34b-11e1-9458-001ec947c8cc")
            @Override
            public E nextElement() {
                if (!this.next()) {
                    throw new NoSuchElementException();
                }
                return this.enums[this.index].nextElement();
            }

            @objid ("8a842c52-f34b-11e1-9458-001ec947c8cc")
            private boolean next() {
                while (this.index < this.enums.length) {
                    if (this.enums[this.index] != null
                            && this.enums[this.index].hasMoreElements()) {
                        return true;
                    }
                    this.index++;
                }
                return false;
            }

        }

    }

}
