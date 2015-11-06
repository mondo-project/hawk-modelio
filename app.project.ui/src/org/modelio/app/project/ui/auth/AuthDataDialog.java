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
                                    

package org.modelio.app.project.ui.auth;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.project.ui.plugin.AppProjectUi;
import org.modelio.core.ui.auth.AuthDataPanel;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.vbasic.auth.IAuthData;

/**
 * Dialog asking the user to authenticate.
 */
@objid ("b60a680d-1239-4fd0-858d-1df58ab81c62")
public class AuthDataDialog extends ModelioDialog {
    @objid ("936b4a65-5020-46d2-bf6e-e9aea2a17d52")
    private String toAuthenticate;

    @objid ("dca78735-c3ad-43a7-ba9d-f0b9c22d23bc")
    private static final String HELP_TOPIC = AppProjectUi.I18N.getString("AuthDataDialog.HelpTopic");

    @objid ("b6e2c006-ce32-44a4-833f-d15c72702902")
    private AuthDataPanel authDataPanel;

    @objid ("339844ab-0e8a-489d-bf1e-f95fcdcbf10c")
    private IAuthData initialData;

    @objid ("dec207b1-baa6-4f0e-a84a-e35e49d48935")
    private IAuthData authData;

    /**
     * Initialize
     * @param parentShell a SWT Shell
     * @param initialData the initial values
     * @param toAuthenticate the part asking for authentication. Will be displayed to the user.
     */
    @objid ("b7cbbd1b-cde2-4deb-bccc-3f82df04aef9")
    public AuthDataDialog(Shell parentShell, IAuthData initialData, String toAuthenticate) {
        super(parentShell);
        this.toAuthenticate = toAuthenticate;
        this.authDataPanel = new AuthDataPanel();
        this.initialData = initialData;
        this.authData = initialData;
    }

    @objid ("c3643fc6-ae90-4ca7-bd62-e7cd3b83a72a")
    @Override
    public Control createContentArea(Composite parent) {
        // Top level container
        Composite top = new Composite(parent, SWT.NULL);
        top.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        top.setLayout(new FormLayout());
        
        FormData fd = null;
        
        // Authentication data: use an AuthDataPanel
        this.authDataPanel.createPanel(top);
        Composite panelComposite = this.authDataPanel.getPanel();
        fd = new FormData();
        fd.top = new FormAttachment(0, 0);
        fd.left = new FormAttachment(0, 0);
        fd.bottom = new FormAttachment(100, 0);
        fd.right = new FormAttachment(100, 0);
        panelComposite.setLayoutData(fd);
        
        
        this.authDataPanel.setInput(this.initialData);
        return top;
    }

    @objid ("cd4883da-ed07-4d55-b7f3-19261bf3c7f4")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, OK, AppProjectUi.I18N.getString("AuthDataDialog.ok"), true);
        createButton(parent, CANCEL, AppProjectUi.I18N.getString("AuthDataDialog.cancel"), false);
    }

    @objid ("eeeee811-68a4-491b-8b04-441a9b80f961")
    @Override
    protected void okPressed() {
        this.authData = this.authDataPanel.getInput();
        super.okPressed();
    }

    /**
     * @return the result off the dialog.
     */
    @objid ("af9e0fbf-23c0-4386-af0d-ef5d5c903e5a")
    public IAuthData getAuthData() {
        return this.authData;
    }

    @objid ("c997d502-e65c-44ed-8176-29e3cad1a49f")
    @Override
    protected void cancelPressed() {
        this.authData = this.initialData;
        super.cancelPressed();
    }

    @objid ("0733bff8-6b6c-4737-8685-63b3ee15d91b")
    @Override
    public void init() {
        setTitle(AppProjectUi.I18N.getMessage("AuthDataDialog.Title", this.toAuthenticate));
        setMessage(AppProjectUi.I18N.getMessage("AuthDataDialog.Message", this.toAuthenticate));
        
        // Position and resize dialog shell
        int width = 600;
        int height = 300;
        
        Rectangle refBounds = this.getShell().getParent().getBounds();
        this.getShell().setMinimumSize(width, height);
        this.getShell().layout(true);
        
        this.getShell().setBounds(refBounds.x + (refBounds.width - width) / 2, refBounds.y + (refBounds.height - height) / 2,
                width, height);
    }

    @objid ("0c146f51-2d48-45a7-9fe1-8e492a8d3918")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

}
