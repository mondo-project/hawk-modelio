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
                                    

package org.modelio.app.project.conf.dialog.libraries.local.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.conf.dialog.ProjectModel;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.data.ramc.IModelComponentInfos;

@objid ("87aa46b6-92e4-4aa2-8cc7-d540d11a0faf")
public class RamcPropertyDialog extends ModelioDialog {
    @objid ("24fc47d6-0bff-49fb-8253-84d51fa553e7")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_managing_projects_configuring_project_libraries.html";

    @objid ("5bb52158-b437-4a2a-943f-3c542e6094a0")
    protected Button closeButton = null;

    @objid ("8fcb3237-194d-4f8d-a42e-3d47e063723f")
    protected IModelComponentInfos fragmentInfos = null;

    @objid ("1106a0a7-e178-4619-80b4-7505a1737402")
    private ProjectModel projectAdapter;

    @objid ("ad3c4972-106a-4134-912a-d5acbc6863d9")
    public RamcPropertyDialog(Shell parentShell, IModelComponentInfos fragmentInfos, ProjectModel projectAdapter) {
        super(parentShell);
        this.fragmentInfos = fragmentInfos;
        this.projectAdapter = projectAdapter;
        this.setShellStyle(SWT.MODELESS | SWT.DIALOG_TRIM | SWT.RESIZE);
    }

    @objid ("4d7ee709-7b44-4618-89b2-7edd1aebc843")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        this.closeButton = this.createButton(parent,
                                              IDialogConstants.CANCEL_ID,
                                              AppProjectConf.I18N.getString("RamcPropertyDialog.Close"),
                                              true);
    }

    @objid ("965579fc-5106-414f-931b-8ac45f16c2db")
    @Override
    public Control createContentArea(Composite parent) {
        Composite area = new Composite(parent, SWT.NONE);
        area.setLayoutData(new GridData(GridData.FILL_BOTH));
        area.setLayout(new FillLayout());
        
        RamcPropertyComposite mainArea = new RamcPropertyComposite(area, SWT.NONE, this.fragmentInfos, this.projectAdapter);
        return area;
    }

    @objid ("036dcbce-0118-41ac-b770-95485d11fa99")
    @Override
    public void init() {
        this.setLogoImage(null);
        
        // Put the AppProjectConf.I18N in the banner area                
        this.getShell().setText(AppProjectConf.I18N.getString("RamcPropertyDialog.ViewRamcDialogTitle"));
        this.setTitle(AppProjectConf.I18N.getString("RamcPropertyDialog.ViewRamcDialogTitle"));
        this.setMessage(AppProjectConf.I18N.getString("RamcPropertyDialog.ViewRamcMessage"));
        
        Point parentLocation = this.getShell().getParent().getLocation();
        
        this.getShell().setLocation(parentLocation.x + 300, parentLocation.y + 200);
        this.getShell().setSize(500, 580);
        this.getShell().setMinimumSize(500, 550);
    }

    @objid ("c39397bf-0851-4346-8141-741cede1c291")
    public boolean close(int code) {
        this.setReturnCode(code);
        return this.close();
    }

    @objid ("6542e648-0ab3-4cc6-85e6-7e72696004d4")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }


//    @objid ("490b3013-be6e-4089-b70e-5a2d4419480d")
//    @Override
//    protected String getHelpId() {
//        return "/com.modeliosoft.modelio.documentation.modeler/html/Model_components_development.html";
//    }
}
