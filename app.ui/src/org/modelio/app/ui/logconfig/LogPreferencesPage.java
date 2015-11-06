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
                                    

package org.modelio.app.ui.logconfig;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.modelio.app.preferences.ScopedPreferenceStore;
import org.modelio.app.ui.plugin.AppUi;
import org.modelio.log.writers.PluginLogger;
import org.osgi.service.log.LogService;

@objid ("f335cc77-eb91-4f14-b4bd-98aece15c8c3")
public class LogPreferencesPage extends FieldEditorPreferencePage {
    @objid ("23b480f8-981e-4cb1-98a2-b12e344fec78")
    public LogPreferencesPage() {
        super(GRID);
        init();
    }

    @objid ("57ea152f-935d-4329-a0d8-2b3f2334a696")
    @Override
    public void createFieldEditors() {
        //
        final String[][] logLevels = new String[][] {
                { AppUi.I18N.getString("LogLevel.ERROR"), Integer.toString(LogService.LOG_ERROR) },
                { AppUi.I18N.getString("LogLevel.WARNING"), Integer.toString(LogService.LOG_WARNING) },
                { AppUi.I18N.getString("LogLevel.INFO"), Integer.toString(LogService.LOG_INFO) },
                { AppUi.I18N.getString("LogLevel.DEBUG"), Integer.toString(LogService.LOG_DEBUG) }, };
        
        RadioGroupFieldEditor logLevelFields = new RadioGroupFieldEditor(LogPreferencesKeys.LOGLEVEL_PREFKEY,
                AppUi.I18N.getString("LogLevel.label"), 1, // nb columns
                logLevels, getFieldEditorParent(), true);
        addField(logLevelFields);
        
        BooleanFieldEditor showAdmTools = new BooleanFieldEditor(LogPreferencesKeys.SHOWADMTOOLS_PREFKEY, AppUi.I18N.getString("AdmTools.Show"), getFieldEditorParent());
        addField(showAdmTools);
    }

    @objid ("1ad16d2e-36d6-498d-bb67-e28e7d36fdbd")
    private void init() {
        final IPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, AppUi.PLUGIN_ID);
        setPreferenceStore(preferenceStore);
    }

    @objid ("7520b9b5-d675-474e-a14b-f72c46b97151")
    @Override
    public boolean isValid() {
        return super.isValid();
    }

    @objid ("e20d5546-4e94-43f6-8d45-fbfe28d3d5d0")
    @Override
    protected void performDefaults() {
        super.performDefaults();
        getPreferenceStore().setValue(LogPreferencesKeys.LOGLEVEL_PREFKEY, LogService.LOG_ERROR);
        getPreferenceStore().setValue(LogPreferencesKeys.SHOWADMTOOLS_PREFKEY, false);
        changeLogLevel(LogService.LOG_ERROR);
    }

    @objid ("9fbfb8fe-91eb-42be-a1df-1c3b3686ab75")
    @Override
    public boolean performOk() {
        final boolean ret = super.performOk();
        final int logLevel = getPreferenceStore().getInt(LogPreferencesKeys.LOGLEVEL_PREFKEY);
        changeLogLevel(logLevel);
        return ret;
    }

    @objid ("df853ed8-4e5a-41a7-b801-db637e131149")
    private void changeLogLevel(int level) {
        PluginLogger.logLevel = level;
    }

}
