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
                                    

package org.modelio.mda.infra.catalog.update;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.preferences.ScopedPreferenceStore;
import org.modelio.gproject.module.IModuleHandle;
import org.modelio.gproject.module.catalog.FileModuleStore;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.ui.i18n.BundledMessages;
import org.modelio.vbasic.net.UriPathAccess;
import org.modelio.vbasic.version.Version;

/**
 * This thread checks an update file for the current version of Modelio.
 * If a more recent version of Modelio exists, an information box is opened for the user.
 * Relies on the platform preferences store for the ignore version process.
 * @see ModuleUpdateChecker#checkUpdate() for more infos.
 */
@objid ("3e15f42c-baa4-4599-8eee-470437f7b482")
public class ModuleUpdateChecker extends Thread {
    @objid ("eb14e81e-ca89-4478-a98c-e45b552ff7f5")
    private boolean validUpdateSite = true;

    @objid ("3c60c64e-cf8e-488b-a386-5eb3421f605f")
    private Collection<IModuleHandle> catalogModules = new ArrayList<>();

    @objid ("8b916c1a-9147-4a15-8ff5-3d56c6d2ff5a")
    protected FileModuleStore catalog;

    @objid ("fa8d7ab2-498f-4eb4-82b0-1ac84bd248ab")
    private List<ModuleUpdateDescriptor> modulesToUpdate;

    /**
     * Default constructor, initializing a bunch of modules to check updates for.
     * @param catalog
     * @param startedMdacs the modules to check update.
     */
    @objid ("d5844af0-25a7-4eea-acba-4bddef83fbcf")
    public ModuleUpdateChecker(Collection<IModuleHandle> startedMdacs, FileModuleStore catalog) {
        this.catalogModules.addAll(startedMdacs);
        this.catalog = catalog;
    }

    @objid ("6d21557e-7d7b-452e-9f98-b00472ba923a")
    @Override
    public void run() {
        checkUpdate();
    }

    /**
     * Checks the update file for the current version of Modelio.
     * If a more recent version of Modelio exists, an information box is opened for the user, unless this version is currently ignored.
     */
    @objid ("7b4c728f-7e69-43f6-bcc5-a886f2125564")
    private void checkUpdate() {
        //this.monitor.beginTask("update", IProgressMonitor.UNKNOWN);
        Properties updateProperties = initUpdateProperties();
        // No property file means no update available
        if (updateProperties == null) {
            return;
        }
        
        this.modulesToUpdate = new ArrayList<>();
        
        // Get already installed modules
        Map<String, IModuleHandle> latestInstalledModules = new HashMap<>();
        for (IModuleHandle installedModule : this.catalogModules) {
            String key = installedModule.getName();
            
            if (!latestInstalledModules.containsKey(key) || (latestInstalledModules.containsKey(key) && latestInstalledModules.get(key).getVersion().isOlderThan(installedModule.getVersion()))) {                     
                latestInstalledModules.put(installedModule.getName(), installedModule);
            }
        }
        
        // Get all new modules from the catalog update site
        int cpt = 0;
        String keyPrefix;
        do {
            cpt++;
            keyPrefix = updateProperties.getProperty("last" + cpt);
            if (keyPrefix != null) {
                final String label = updateProperties.getProperty(keyPrefix + ".label");
                final String nextVersion = updateProperties.getProperty(keyPrefix + ".next");
                final String url = updateProperties.getProperty(keyPrefix + ".url");
                final String downloadLink = updateProperties.getProperty(keyPrefix + ".file");
        
                // Have we found a newer version?
                if (nextVersion != null && !nextVersion.equals("")) {
                    String name = keyPrefix.substring(0, keyPrefix.indexOf("."));
                    String currentVersion = "";
                    final IModuleHandle latestInstalledModule = latestInstalledModules.get(name);
                    if (latestInstalledModule != null) {
                        currentVersion = latestInstalledModule.getVersion().toString();
                        if (latestInstalledModule.getVersion().isOlderThan(new Version(nextVersion))) {
                            this.modulesToUpdate.add(new ModuleUpdateDescriptor(label, currentVersion, nextVersion, url, downloadLink));
                        }
                    } else {
                        this.modulesToUpdate.add(new ModuleUpdateDescriptor(label, currentVersion, nextVersion, url, downloadLink));
                    }
                }
            }
        } while (keyPrefix != null);
    }

    @objid ("c4de2b63-cadc-4450-af4c-3673fa369971")
    private Properties initUpdateProperties() {
        BundledMessages i18n = new BundledMessages(MdaInfra.LOG, ResourceBundle.getBundle("catalogupdate"));
        IPreferenceStore prefs = new ScopedPreferenceStore(InstanceScope.INSTANCE, MdaInfra.PLUGIN_ID);
        prefs.setDefault(CatalogUpdatePreferencesPage.CATALOG_UPDATE_SITE, i18n.getString("ModuleCatalog.Preference.DefaultUpdateSite"));
        
        // Read properties file.
        Properties updateProperties = new Properties();
        
        final String serverUpdateSite = prefs.getString(CatalogUpdatePreferencesPage.CATALOG_UPDATE_SITE);
        try (UriPathAccess pathAccess = new UriPathAccess(URIUtil.fromString(serverUpdateSite), null)) {
            final Path path = pathAccess.getPath();
            try (BufferedReader in = new BufferedReader(new FileReader(path.toFile()))) {
                updateProperties.load(in);
                in.close();
            }
        } catch (IOException| URISyntaxException e) {
            this.validUpdateSite = false;
            Display.getDefault().asyncExec(new Runnable() {
                
                @Override
                public void run() {                    
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), MdaInfra.I18N.getString("ModuleUpdateCheckerError.Title"), MdaInfra.I18N.getMessage("ModuleUpdateCheckerError.Message", serverUpdateSite));
                }
            });
            MdaInfra.LOG.error(e.getMessage());
            return null;
        }
        return updateProperties;
    }

    @objid ("9529c49f-ec1e-4cc7-b890-dde760007e4e")
    public List<ModuleUpdateDescriptor> getModulesToUpdate() {
        return this.modulesToUpdate;
    }

    @objid ("f1a9c230-d048-41b5-ba47-8dec5c5b00df")
    public boolean isValidUpdateSite() {
        return this.validUpdateSite;
    }

}
