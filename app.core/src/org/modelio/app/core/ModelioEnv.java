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
                                    

package org.modelio.app.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.modelio.app.core.plugin.AppCore;
import org.modelio.metamodel.Metamodel;
import org.modelio.vbasic.version.Version;
import org.osgi.service.prefs.BackingStoreException;

/**
 * The ModelioEnv singleton holds several configuration values for Modelio.
 * ModelioEnv values deal only on the current user environment and on the
 * Modelio version.
 * For example, no ModelioEnv value depends on the currently opened project or
 * the current workspace.
 * 
 * 
 * @author phv
 */
@objid ("002890a6-8562-103f-87fd-001ec947cd2a")
@Creatable
@Singleton
public class ModelioEnv {
    @objid ("003e9a0e-9277-103f-87fd-001ec947cd2a")
    public static final String MODELIO_VERSION = "3.2.1";

    @objid ("cc93fe4d-eaad-43f3-b511-732fe3711631")
    public static final String MODULE_PATH_PREFERENCE = "ModuleCatalog.LocalPath";

    @objid ("0065c1a6-856d-103f-87fd-001ec947cd2a")
    private Version version;

    @objid ("0064d462-856d-103f-87fd-001ec947cd2a")
    private Path runtimeDataPath;

    @objid ("0069c58a-7778-1061-84ef-001ec947cd2a")
    protected Path moduleCatalogPath;

    @objid ("008572d0-cdbe-106a-bf4f-001ec947cd2a")
    protected Path macroCatalogPath;

    @objid ("0001d380-de89-1040-a120-001ec947cd2a")
    @PostConstruct
    private void init() {
        this.version = new Version(MODELIO_VERSION + "." + Metamodel.VERSION);
        final String versionSubpath = this.version.getMajorVersion() + "." + this.version.getMinorVersion();
        
        // Modelio runtime data path
        this.runtimeDataPath = Paths.get(System.getProperty("user.home"), ".modelio", versionSubpath);
        
        // Get the mda.infra preference node, as the module catalog is managed by this plugin
        IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("org.modelio.mda.infra");
        
        // Modelio modules catalog
        Path defaultModuleCatalogPath = Paths.get(this.runtimeDataPath.toString(), "modules");
        String value = prefs.get(MODULE_PATH_PREFERENCE, null);
        if (value == null) {
            // No value in the preference store, set the default one 
            this.moduleCatalogPath = defaultModuleCatalogPath;
            prefs.put(MODULE_PATH_PREFERENCE, this.moduleCatalogPath.toString());
        
            // Save preferences
            try {
                prefs.flush();
            } catch (BackingStoreException e) {
                AppCore.LOG.error(e.getMessage());
            }
        } else {
            this.moduleCatalogPath = Paths.get(value);
        }
        
        // Add a preference change listener to update module catalog path.
        prefs.addPreferenceChangeListener(new IPreferenceChangeListener() {
            @Override
            public void preferenceChange(PreferenceChangeEvent event) {
                if (MODULE_PATH_PREFERENCE.equals(event.getKey())) {
                    ModelioEnv.this.moduleCatalogPath = Paths.get((String) event.getNewValue());
                }
            }
        });
        
        // Modelio macro catalog
        this.macroCatalogPath = Paths.get(this.runtimeDataPath.toString(), "macros");
        
        // Create dirs
        try {
            Files.createDirectories(this.runtimeDataPath);
            Files.createDirectories(this.moduleCatalogPath);
            Files.createDirectories(this.macroCatalogPath);
        } catch (final IOException e) {
            AppCore.LOG.error("Failed to create Modelio runtime directories.");
            AppCore.LOG.error(e);
        }
    }

    @objid ("004d2236-de99-1040-a120-001ec947cd2a")
    public Version getVersion() {
        // Automatically generated method. Please delete this comment before
        // entering specific code.
        return this.version;
    }

    @objid ("00017610-dde4-1040-a120-001ec947cd2a")
    public Path getRuntimeDataPath() {
        // Automatically generated method. Please delete this comment before
        // entering specific code.
        return this.runtimeDataPath;
    }

    @objid ("0061210a-77d6-1061-84ef-001ec947cd2a")
    public Path getModuleCatalogPath() {
        // Automatically generated method. Please delete this comment before
        // entering specific code.
        return this.moduleCatalogPath;
    }

    @objid ("fdef1a4e-9cff-4bd0-8892-af0e0033c7fb")
    public Path getMacroCatalogPath() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.macroCatalogPath;
    }

}
