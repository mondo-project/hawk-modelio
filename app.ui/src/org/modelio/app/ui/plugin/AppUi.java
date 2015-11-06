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
                                    

package org.modelio.app.ui.plugin;

import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.log.ExtendedLogService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.modelio.app.preferences.ScopedPreferenceStore;
import org.modelio.app.ui.logconfig.LogPreferencesKeys;
import org.modelio.log.writers.PluginLogger;
import org.modelio.ui.i18n.BundledMessages;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * Bundle activator.
 * <p>
 * Also initializes the log level from preferences.
 */
@objid ("002e1cc4-d6b6-1ff2-a7f4-001ec947cd2a")
public class AppUi implements BundleActivator {
    /**
     * The 'app.ui' plugin identifier.
     */
    @objid ("002e2426-d6b6-1ff2-a7f4-001ec947cd2a")
    public static final String PLUGIN_ID = "org.modelio.app.ui";

    @objid ("002e255c-d6b6-1ff2-a7f4-001ec947cd2a")
    private static BundleContext context;

    /**
     * Translated messages service.
     */
    @objid ("002e2b1a-d6b6-1ff2-a7f4-001ec947cd2a")
    public static BundledMessages I18N;

    /**
     * The plugin logger
     */
    @objid ("002e2c46-d6b6-1ff2-a7f4-001ec947cd2a")
    public static PluginLogger LOG;

    @objid ("002e225a-d6b6-1ff2-a7f4-001ec947cd2a")
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        context = bundleContext;
        ServiceReference<ExtendedLogService> ref = bundleContext.getServiceReference(ExtendedLogService.class);
        ExtendedLogService service = bundleContext.getService(ref);
        LOG = new PluginLogger(service.getLogger(null));
        I18N = new BundledMessages(LOG, ResourceBundle.getBundle("appui"));
        
        initLogLevel();
    }

    @objid ("002e26e2-d6b6-1ff2-a7f4-001ec947cd2a")
    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        context = null;
    }

    /**
     * @return the bundle context.
     */
    @objid ("002e276e-d6b6-1ff2-a7f4-001ec947cd2a")
    public static BundleContext getContext() {
        return context;
    }

    @objid ("6484e866-f678-4f39-b036-bdfa0163c593")
    private void initLogLevel() {
        // Initialize log level from preferences
        final IPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, AppUi.PLUGIN_ID);
        final int level = preferenceStore.getInt(LogPreferencesKeys.LOGLEVEL_PREFKEY);
        
        if (level != 0)
            PluginLogger.logLevel = level;
    }

}
