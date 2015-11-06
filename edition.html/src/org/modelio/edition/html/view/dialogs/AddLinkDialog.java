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
                                    

//------------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/epl-v10.html
//
// Contributors:
// IBM Corporation - initial implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.view.dialogs;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.html.Link;

/**
 * Prompts the user to specify the file that will be used to create a HTML
 * &lt;a&gt; tag in the rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("9d5a4738-e753-4829-85a3-926170248276")
public class AddLinkDialog extends BaseDialog {
    @objid ("097859dd-ac3f-4555-a051-16802d8e0dd8")
    protected String basePath;

    @objid ("296e3b96-51b9-47e7-b400-16b3936f76fd")
    protected Link link = new Link();

    @objid ("38d8ac9d-1540-4794-b87d-317da7ea21fc")
    protected Text urlText;

    @objid ("28bc798d-dd0e-4f01-bead-793e690f7242")
    protected Composite composite;

    @objid ("4fc32d08-7c52-4145-8733-a86131916737")
    protected Label urlLabel;

    @objid ("3161c8d9-bbc3-4806-8b6b-5b04392ebc62")
    protected Text urlDisplayNameText;

    @objid ("9e78fc9a-454b-4467-9e40-ee970da65421")
    protected Label urlDisplayNameLabel;

    @objid ("ac1bbf52-790d-44c9-919a-fc6d626b9b7a")
    protected ModifyListener urlTextModifyListener = new ModifyListener() {
		@Override
        public void modifyText(ModifyEvent e) {
			if (AddLinkDialog.this.okButton != null) {
				AddLinkDialog.this.okButton.setEnabled(AddLinkDialog.this.urlText.getText().trim().length() > 0);
			}
		}
	};

    /**
     * Creates a new instance.
     * @param parent the parent shell
     */
    @objid ("830373c4-fbdb-4303-a6e7-5fe17db66e23")
    public AddLinkDialog(Shell parent, String basePath) {
        super(parent);
        this.basePath = basePath;
    }

    @objid ("de30c86a-6fef-4bbf-ba52-b47927ba0f56")
    @Override
    protected Control createDialogArea(Composite parent) {
        this.composite = (Composite) super.createDialogArea(parent);
        
        this.urlLabel = new Label(this.composite, SWT.NONE);
        this.urlLabel.setText(HtmlTextResources.urlLabel_text);
        this.urlText = new Text(this.composite, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.widthHint = 300;
        this.urlText.setLayoutData(gridData);
        this.urlText.addModifyListener(this.urlTextModifyListener);
        
        this.urlDisplayNameLabel = new Label(this.composite, SWT.NONE);
        this.urlDisplayNameLabel.setText(HtmlTextResources.urlDisplayNameLabel_text);
        this.urlDisplayNameText = new Text(this.composite, SWT.BORDER);
        GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
        gridData2.widthHint = 300;
        this.urlDisplayNameText.setLayoutData(gridData2);
        
        super.getShell().setText(HtmlTextResources.addLinkDialog_title);
        return this.composite;
    }

    @objid ("c3c5a8a3-4d3d-426c-bfb7-aef463219b29")
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        this.okButton.setEnabled(false);
    }

    @objid ("d06ff078-3ec3-4d73-83d8-796f34de35ad")
    @Override
    protected void okPressed() {
        String url = this.urlText.getText();
        if (url != null && url.length() > 0) {
            this.link.setURL(url);
            this.link.setName(this.urlDisplayNameText.getText());
        }
        super.okPressed();
    }

    /**
     * Gets the user specified link.
     * @return an <code>Link</code> object
     */
    @objid ("67395d33-627e-456f-bf39-282628cb78da")
    public Link getLink() {
        return this.link;
    }

}
