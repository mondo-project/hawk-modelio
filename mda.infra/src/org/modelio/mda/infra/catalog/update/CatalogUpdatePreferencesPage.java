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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.preferences.ScopedPreferenceStore;
import org.modelio.mda.infra.plugin.MdaInfra;
import org.modelio.ui.i18n.BundledMessages;
import org.modelio.vbasic.files.FileUtils;

@objid ("e46387a6-a669-48b9-8131-ca484a12b266")
public class CatalogUpdatePreferencesPage extends FieldEditorPreferencePage {
    @objid ("da0dbc1b-1cbe-44bb-9fcc-1da8ed096064")
    public static final String CATALOG_UPDATE_SITE = "ModuleCatalog.UpdateSite";

    @objid ("4f2fbba1-9b34-45c5-baa9-60e144b94cff")
    public static final String CATALOG_SHOW_LATEST = "ModuleCatalog.ShowLatest";

    @objid ("f7972004-9d30-41f0-ace7-5daf81fe3edf")
    public static final String CATALOG_SHOW_COMPATIBLE = "ModuleCatalog.ShowCompatible";

    @objid ("b7f2ccd6-6ce6-4e71-8734-5043a2c9d9be")
    private StringFieldEditor updateSiteField;

    @objid ("bb75c97e-b708-4687-88f2-78844766c165")
    private BooleanFieldEditor showLatestField;

    @objid ("127ccbbb-5685-489c-8056-054996e37c6a")
    private BooleanFieldEditor showCompatibleField;

    @objid ("be2a7eaa-b747-440c-9ff3-cf8506ce19c2")
    private DirectoryFieldEditor localCatalogPathFieldEditor;

    @objid ("2031fdc1-fe68-455a-99c5-b8dc40143e45")
    protected Path currentModulePath;

    @objid ("1be2933f-72b6-4b49-bdba-c23599147a59")
    @Inject
    public CatalogUpdatePreferencesPage() {
        init();
    }

    @objid ("15d552ed-ef13-4b70-976f-38cb7885a75d")
    private void init() {
        IPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, MdaInfra.PLUGIN_ID);
        setPreferenceStore(preferenceStore);
        
        BundledMessages i18n = new BundledMessages(MdaInfra.LOG, ResourceBundle.getBundle("catalogupdate"));
        preferenceStore.setDefault(CatalogUpdatePreferencesPage.CATALOG_UPDATE_SITE, i18n.getString("ModuleCatalog.Preference.DefaultUpdateSite"));
        preferenceStore.setDefault(CatalogUpdatePreferencesPage.CATALOG_SHOW_COMPATIBLE, true);
        preferenceStore.setDefault(CatalogUpdatePreferencesPage.CATALOG_SHOW_LATEST, true);
        preferenceStore.setDefault(ModelioEnv.MODULE_PATH_PREFERENCE, preferenceStore.getString(ModelioEnv.MODULE_PATH_PREFERENCE));
        
        this.currentModulePath = Paths.get(getPreferenceStore().getString(ModelioEnv.MODULE_PATH_PREFERENCE));
    }

    @objid ("d86fcd83-8195-4b5d-bb51-eaf5a3b1d8c6")
    @Override
    public void createFieldEditors() {
        this.updateSiteField = new StringFieldEditor(CATALOG_UPDATE_SITE, MdaInfra.I18N.getString("ModuleCatalog.Preference.UpdateSite"), getFieldEditorParent());
        addField(this.updateSiteField);
        
        this.showLatestField = new BooleanFieldEditor(CATALOG_SHOW_LATEST, MdaInfra.I18N.getString("ModuleCatalog.Preference.ShowOnlyLatest"), getFieldEditorParent());
        addField(this.showLatestField);
        
        this.showCompatibleField = new BooleanFieldEditor(CATALOG_SHOW_COMPATIBLE, MdaInfra.I18N.getString("ModuleCatalog.Preference.ShowOnlyCompatible"), getFieldEditorParent());
        addField(this.showCompatibleField);
        
        final Composite composite = getFieldEditorParent();
        
        this.localCatalogPathFieldEditor = new DirectoryFieldEditor(ModelioEnv.MODULE_PATH_PREFERENCE, MdaInfra.I18N.getString("ModuleCatalog.Preference.MoveCatalog.label"), composite);
        this.localCatalogPathFieldEditor.setChangeButtonText(MdaInfra.I18N.getString("ModuleCatalog.Preference.MoveCatalog.button"));
        final Text localCatalogPathText = this.localCatalogPathFieldEditor.getTextControl(composite);
        localCatalogPathText.setEditable(false);
        
        Label label = new Label(composite, SWT.NONE);
        label.setText(MdaInfra.I18N.getString("ModuleCatalog.Preference.MoveCatalog.description"));
        final GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        label.setLayoutData(gd);
        
        // Add a modify listener to move the old catalog's contents to the new one
        localCatalogPathText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                Path newPath = Paths.get(localCatalogPathText.getText());
                if (Files.exists(CatalogUpdatePreferencesPage.this.currentModulePath) && !CatalogUpdatePreferencesPage.this.currentModulePath.equals(newPath)) {
                    try {
                        FileUtils.copyDirectoryTo(CatalogUpdatePreferencesPage.this.currentModulePath, newPath);
                        
                        // Delete the old catalog
                        try {
                            FileUtils.delete(CatalogUpdatePreferencesPage.this.currentModulePath);
                        } catch (IOException e1) {
                            MdaInfra.LOG.error(e1.getMessage());
                        }
                        
                        MessageDialog.openInformation(localCatalogPathText.getShell(), MdaInfra.I18N.getString("ModuleCatalog.Preference.MoveCatalog.SuccessTitle"), MdaInfra.I18N.getString("ModuleCatalog.Preference.MoveCatalog.SuccessMessage"));
                    } catch (IOException e1) {
                        // Error occured during copy, reset the Text's value
                        localCatalogPathText.setText(CatalogUpdatePreferencesPage.this.currentModulePath.toString());
                        MdaInfra.LOG.error(e1.getMessage());
                        MessageDialog.openError(localCatalogPathText.getShell(), MdaInfra.I18N.getString("ModuleCatalog.Preference.MoveCatalog.FailTitle"), MdaInfra.I18N.getMessage("ModuleCatalog.Preference.MoveCatalog.FailMessage", e1.getMessage()));
                        return;
                    }
                    
                    CatalogUpdatePreferencesPage.this.currentModulePath = newPath;
                }
            }
        });
        addField(this.localCatalogPathFieldEditor);
    }

    @objid ("19fa08d8-2016-4506-ae31-3b968694612b")
    @Override
    public boolean performCancel() {
        // Restore original catalog
        Path originalPath = Paths.get(getPreferenceStore().getString(ModelioEnv.MODULE_PATH_PREFERENCE));
        
        if (Files.exists(this.currentModulePath) && !this.currentModulePath.equals(originalPath)) {
            try {
                FileUtils.copyDirectoryTo(this.currentModulePath, originalPath);
                FileUtils.delete(this.currentModulePath);
            } catch (IOException e1) {
                // Error occured during copy, reset the Text's value
                MdaInfra.LOG.error(e1.getMessage());
            }
            
            this.currentModulePath = originalPath;
        }
        return super.performCancel();
    }

}
