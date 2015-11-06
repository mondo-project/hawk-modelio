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
                                    

package org.modelio.app.project.conf.dialog.modules;

import java.io.IOException;
import java.net.URL;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.osgi.framework.Bundle;

@objid ("4e8deca2-d493-4784-a287-44a8768b36ea")
public class ModuleRemovalConfirmationDialog extends ModelioDialog {
    @objid ("04258cfa-ebfd-48cd-b9b6-2649fbc71246")
    public ModuleRemovalConfirmationDialog(Shell parentShell) {
        super(parentShell);
    }

    @objid ("a108539a-ac33-45d4-a26d-edabe09c38d9")
    @Override
    public Control createContentArea(Composite parent) {
        Browser browser = new Browser(parent, SWT.BORDER);
        browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        browser.setMenu(new Menu(browser));
        browser.setJavascriptEnabled(false);
        IPath helpUrl = new Path( AppProjectConf.I18N.getString("RemoveMdacsDlg.Confirm.Url"));
        Bundle bundle = Platform.getBundle(AppProjectConf.PLUGIN_ID);
        try {
            URL url = FileLocator.toFileURL(FileLocator.find(bundle, helpUrl, null));
            browser.setUrl(url.toString());
        } catch (IOException e) {
            AppProjectConf.LOG.error(e);
        }
        return browser;
    }

    @objid ("0a79dd5f-4a5e-453e-8c6f-dcbac622b365")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.YES_LABEL, false);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.NO_LABEL, true);
    }

    @objid ("f648d937-eae9-49f8-bcfc-a02d3fc381ac")
    @Override
    public void init() {
        getShell().setText(AppProjectConf.I18N.getString("RemoveMdacsDlg.Confirm.Title"));
        setTitle(AppProjectConf.I18N.getString("RemoveMdacsDlg.Confirm.Title"));
        setMessage(AppProjectConf.I18N.getString("RemoveMdacsDlg.Confirm.Text"));
        getShell().setSize(600, 700);
        getShell().setMinimumSize(600, 550);
    }

}
