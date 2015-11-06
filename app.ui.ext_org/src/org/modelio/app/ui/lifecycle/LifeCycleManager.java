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
                                    

package org.modelio.app.ui.lifecycle;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.progress.IProgressService;
import org.modelio.app.core.IModelioEventService;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.core.activation.ActivationService;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.events.ModelioEventService;
import org.modelio.app.core.inputpart.IInputPartService;
import org.modelio.app.core.inputpart.InputPartService;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.app.core.navigate.ModelioNavigationService;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.core.picking.ModelioPickingService;
import org.modelio.app.project.core.services.CommandLineData;
import org.modelio.app.ui.ApplicationTitleUpdater;
import org.modelio.app.ui.SwapLogMonitor;
import org.modelio.app.ui.login.Splash;
import org.modelio.app.ui.persp.IModelioUiService;
import org.modelio.app.ui.plugin.AppUi;
import org.modelio.app.ui.progress.ModelioProgressService;
import org.modelio.app.ui.welcome.WelcomeView;
import org.modelio.core.help.system.ModelioHelpSystem;
import org.modelio.gproject.module.catalog.FileModuleStore;
import org.modelio.log.writers.PluginLogger;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.data.MetamodelLoader;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vbasic.net.SslManager;
import org.modelio.vcore.session.impl.cache.MemoryManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.osgi.service.log.LogService;
import org.osgi.service.prefs.BackingStoreException;

/**
 * E4 life cycle manager.
 * <p>
 * Drives Modelio start sequence.
 */
@objid ("474f0435-3e51-43c4-b021-4a3fae2bfab1")
public class LifeCycleManager {
    @objid ("c767d44e-2514-4993-947d-a221ee6abb67")
    private static final String WELCOME_HREF = "org.modelio.documentation.welcome/html/Index.html";

    @objid ("e19a9138-eec0-43c8-8e4e-b5773ffb280f")
    private ApplicationTitleUpdater titleUpdater;

    @objid ("6a09416d-f314-490d-9f10-fa8564b3e3f6")
    private Splash splash;

    @objid ("58c41593-e154-46bc-9dd4-e02ded767fef")
    private CommandLineData cmdLineData;

    @objid ("b46266aa-9b70-4eca-bc39-03ed33589e0f")
    @PostContextCreate
    void postContextCreate(final IEclipseContext context) {
        AppUi.LOG.info("Modelio by Modeliosoft");
        // Get the command line arguments
        final IApplicationContext appContext = context.get(IApplicationContext.class);
        final String args[] = (String[]) appContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);
        StringBuilder cmdline = new StringBuilder();
        for (final String arg : args) {
            cmdline.append(arg);
            cmdline.append(' ');
        }
        AppUi.LOG.info("Command line arguments = '%s'", cmdline);
        this.cmdLineData = new CommandLineData(args);
        
        // Depending on command line options, pop up a splash
        if (!this.cmdLineData.isBatch()) {
            // No batch mode, show the splash
            this.splash = new Splash();
            this.splash.open();
        }
        
        // -mdebug on command line forces log level to help development and
        // debugging
        if (this.cmdLineData.isDebug())
            PluginLogger.logLevel = LogService.LOG_DEBUG;
        
        // Set up the ModelioEnv instance, this also create the modelio runtime
        // directories if needed
        final ModelioEnv modelioEnv = ContextInjectionFactory.make(ModelioEnv.class, context);
        context.set(ModelioEnv.class, modelioEnv);
        AppUi.LOG.info("Modelio version            : '%s'", modelioEnv.getVersion());
        AppUi.LOG.info("Modelio runtime data path  : '%s'", modelioEnv.getRuntimeDataPath());
        AppUi.LOG.info("Modelio module catalog path: '%s'", modelioEnv.getModuleCatalogPath());
        AppUi.LOG.info("Modelio macro  catalog path: '%s'", modelioEnv.getMacroCatalogPath());
        
        // initialize SSL certificates manager
        final Path serverCertsDb = modelioEnv.getRuntimeDataPath().resolve("servercerts.db");
        try {
            SslManager.getInstance().setTrustStoreFile(modelioEnv.getRuntimeDataPath().resolve("servercerts.db"));
        } catch (IOException e1) {
            AppUi.LOG.warning(e1);
            String message = "Cannot read trusted server certificates from '"+serverCertsDb+"':\n"+e1.getLocalizedMessage();
            context.get(StatusReporter.class).show(StatusReporter.ERROR, message, e1);
        }
        
        // EXPERIMENTAL STUFF ABOUT UPDATE
        // Check for updates
        // If an update is performed, restart. Otherwise log the status.
        // Get the update site location from the preferences
        // if (this.splash != null)
        // this.splash.showMessage(AppUi.I18N.getString("Splash.updates"));
        
        // final IEclipsePreferences prefs =
        // InstanceScope.INSTANCE.getNode(AppUpdate.PLUGIN_ID);
        // final IWorkbench workbench = context.get(IWorkbench.class);
        // final ModelioOnStartUpdater updater = new ModelioOnStartUpdater();
        // updater.execute(workbench, prefs, modelioEnv.getVersion());
        
        // END EXPERIMENTAL STUFF
        
         // Loading metamodel
        AppUi.LOG.info("Loading metamodel...");
        if (this.splash != null)
            this.splash.showMessage(AppUi.I18N.getString("Splash.metamodel"));
        MetamodelLoader.Load();
        AppUi.LOG.info("Metamodel loaded, version  : '%s'", Metamodel.VERSION);
        
        // Initialize catalog from mdastore if the local catalog directory does not contain any entry
        final FileModuleStore catalog = new FileModuleStore(modelioEnv.getModuleCatalogPath());
        boolean emptyCatalog;
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(modelioEnv.getModuleCatalogPath(), new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path entry) throws IOException {
                return !entry.endsWith("version.dat");
            }
        })) {
            emptyCatalog = !ds.iterator().hasNext();
        } catch (IOException e) {
            AppUi.LOG.warning(e);
            emptyCatalog = false;
        }
        
        if (emptyCatalog) {
            if (this.splash != null) {
                this.splash.showMessage(AppUi.I18N.getString("Splash.modules"));
            }
            deliverMdaStoreModules(catalog);
        }
        
        if (this.splash != null)
            this.splash.showMessage(AppUi.I18N.getString("Splash.services"));
        
        // Create and Register the ModelioEventService instance
        final ModelioEventService modelioEventService = new ModelioEventService(context);
        context.set(IModelioEventService.class, modelioEventService);
        
        // Create and Register the ModelioProgressService instance
        // The service is registered both as a standard IProgressService and as
        // an extended IModelioProgressService
        final ModelioProgressService modelioProgressService = new ModelioProgressService();
        context.set(IModelioProgressService.class, modelioProgressService);
        context.set(IProgressService.class, modelioProgressService);
    }

    @objid ("ab606429-1892-408d-9739-071039297c1c")
    @ProcessAdditions
    void onProcessAdditions(final MApplication application) {
        final IEclipseContext context = application.getContext();
        
        this.titleUpdater = ContextInjectionFactory.make(ApplicationTitleUpdater.class, context);
        context.set(ApplicationTitleUpdater.class, this.titleUpdater);
        
        PerspectiveManager pm = ContextInjectionFactory.make(PerspectiveManager.class, context);
        context.set(IModelioUiService.class, pm);
        
        // Perspectives initialization, force the Welcome part if first launch
        WelcomeView.setWelcomeHref(WELCOME_HREF);
        final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(AppUi.PLUGIN_ID);
        boolean firstLaunch = prefs.getBoolean("FirstLaunch", true);
        if (firstLaunch) {
            pm.showWelcome(true);
            prefs.putBoolean("FirstLaunch", false);
            try {
                prefs.sync();
            } catch (BackingStoreException e) {
                AppUi.LOG.warning(e);
            }
        } else {
            pm.switchToPerspective(null); // 'null' sets a default perspective
        }
        
        // Create and Register the picking service instance
        final IModelioPickingService pickingService = ContextInjectionFactory.make(ModelioPickingService.class, context);
        context.set(IModelioPickingService.class, pickingService);
        
        // Create and Register the navigate service instance
        final IModelioNavigationService navigateService = ContextInjectionFactory.make(ModelioNavigationService.class, context);
        context.set(IModelioNavigationService.class, navigateService);
        
        // Create and Register the activation service instance
        final IActivationService activationService = ContextInjectionFactory.make(ActivationService.class, context);
        context.set(IActivationService.class, activationService);
        
        // Create and Register the InputPart Service instance
        final IInputPartService inputPartService = ContextInjectionFactory.make(InputPartService.class, context);
        context.set(IInputPartService.class, inputPartService);
        
        // Swap monitoring:
        // - the title updater for a simplified swap activity report to the end
        // user
        // - a LOG report in debug mode
        AppUi.LOG.info("SWAP is %s", MemoryManager.get().isSwapEnabled() ? "ENABLED" : "DISABLED");
        if (MemoryManager.get().isSwapEnabled()) {
            MemoryManager.get().addMemoryListener(this.titleUpdater);
            if (AppUi.LOG.isDebugEnabled()) {
                MemoryManager.get().addMemoryListener(new SwapLogMonitor());
            }
        }
        
        // Configuring HELP
        /*if (AppUi.LOG.isDebugEnabled()) {
            IToc[] tocs = org.eclipse.help.HelpSystem.getTocs();
            for (IToc toc : tocs) {
                AppUi.LOG.debug("TOC %s", toc.getLabel());
                for (ITopic topic : toc.getTopics()) {
                    AppUi.LOG.debug("  - %s (href=%s)", topic.getLabel(), topic.getHref());
                }
            }
        }*/
        context.set(IWorkbenchHelpSystem.class, ModelioHelpSystem.getInstance());
        
        // Switch off the splash screen if needed.
        if (this.splash != null) {
            this.splash.close();
            this.splash = null;
        }
        
        // Batch mode
        // MANTAYORY: keep this code sequence the last one of the method
        final IEventBroker eventBroker = context.get(IEventBroker.class);
        if (eventBroker != null) {
            EventHandler eventHandler = new EventHandler() {
                
                @SuppressWarnings("synthetic-access")
                @Override
                public void handleEvent(Event arg0) {
                    // Unlock the workspace
                    Location instanceLocation = (Location) context.get(E4Workbench.INSTANCE_LOCATION);
                    if (instanceLocation != null) {
                        instanceLocation.release();
                    }
        
                    // Run Batch
                    // MANTAYORY: keep this code sequence the last one of the method
                    eventBroker.post("BATCH", LifeCycleManager.this.cmdLineData);
                }
            };
            
            eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, eventHandler);
        }
    }

    @objid ("68684452-09b9-43cc-8187-12587debedd4")
    @ProcessRemovals
    void onProcessRemovals() {
        // Called after @ProcessAdditions but for removals.
        // final MApplication application
    }

    @objid ("4021a0d4-a0af-4d87-b82d-eac07a7f0ef6")
    @PreSave
    void onPreSave() {
        // Is called before the application model is saved. You can modify the
        // model before it is persisted.
    }

    @objid ("61c57b6e-4aac-477f-b313-244e7115779a")
    private void deliverMdaStoreModules(FileModuleStore catalog) {
        Location location = Platform.getInstallLocation();
        Path mdaStore = new File(location.getURL().getFile()).toPath().resolve("mdastore");
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(mdaStore, "*.jmdac")) {
            for (Path jmdacPath : ds) {
                // Iterate over the paths in the directory and print
                // filenames
                AppUi.LOG.debug("installing module %s in catalog", jmdacPath.getFileName());
                catalog.getModuleHandle(jmdacPath, null);
            }
        } catch (IOException e) {
            AppUi.LOG.debug(e);
        }
    }


// private static class ModelioOnStartUpdater {
//
// private static final String JUSTUPDATED = "Update.justUpdated";
//
//
// private static final String LASTUPDATE = "Update.LastUpdate";
//
//
// public void execute(IWorkbench workbench, IEclipsePreferences
// updatePrefs, Version modelioCurrentVersion) {
// // If we are restarting after update we no not want to check again
// // for an update
// final Boolean restartingAfterUpdate = updatePrefs.getBoolean(JUSTUPDATED,
// false);
// if (restartingAfterUpdate) {
// updatePrefs.putBoolean(JUSTUPDATED, false);
// return;
// }
//
// // If the 'update on start' option is NOT set, there is nothing to
// // do here
// final Boolean updateOnStart =
// updatePrefs.getBoolean(UpdatePreferencesKeys.UPDATEONSTART_PREFKEY,
// true);
// if (!updateOnStart) {
// return;
// }
//
// // Get the update repository URL. If none (or a bad one) just leave
// String updateSiteUrl =
// updatePrefs.get(UpdatePreferencesKeys.UPDATESITE_PREFKEY, null);
// if (updateSiteUrl == null) {
// return;
// }
// URI repoLocation = null;
// try {
// repoLocation = new URI(updateSiteUrl);
// } catch (final URISyntaxException e1) {
// AppUpdate.LOG.error(e1);
// return;
// }
//
// // Check that the minimum time interval between two 'update on
// // start' is exceeded
// // The check period is chosen by the end user.
// final Integer minIntervalBetweenUpdates =
// updatePrefs.getInt(UpdatePreferencesKeys.UPDATEONSTARTFREQUENCY_PREFKEY,
// 0);
// final long lastUpdate = updatePrefs.getLong(LASTUPDATE, 0);
// final long now = System.currentTimeMillis();
// final Boolean minUpdateIntervalExceeded = ((now - lastUpdate) >
// minIntervalBetweenUpdates * 1000 * 3600 * 24);
//
// // Carry out the update
// if (minUpdateIntervalExceeded) {
// boolean wasUpdated = false;
// final boolean notifyNoUpdate = false;
// wasUpdated = P2Util.updateApplication(modelioCurrentVersion,
// repoLocation, notifyNoUpdate, null);
//
// // Note when we ran this update
// updatePrefs.putLong(LASTUPDATE, now);
//
// if (wasUpdated) {
// // set the 'just updated' flag to ensure no checking at
// // restart.
// updatePrefs.putBoolean(JUSTUPDATED, true);
// try {
// updatePrefs.sync();
// } catch (final BackingStoreException e) {
// AppUi.LOG.error(e);
// }
//
// // Hacked code to force a restart as E4 RCP does not
// // currently provide any official API for that
// FrameworkProperties.setProperty(EclipseStarter.PROP_EXITCODE, "23");
// FrameworkProperties.getProperty(EclipseStarter.PROP_NOSHUTDOWN);
// workbench.close();
// }
//
// }
// }
//
// }
}
