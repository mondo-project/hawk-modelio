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

import java.io.File;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.html.Image;

/**
 * Prompts the user to specify the image that will be used to create a HTML
 * &lt;image&gt; tag in the rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("24e868da-6737-429d-9444-23cc4751fd17")
public class AddImageDialog extends BaseDialog {
    @objid ("eaa2ef0c-ccc4-4113-a8bd-423d88c94701")
    protected Image image = new Image();

    @objid ("fd9261b0-6f07-4587-9fd5-b86d4e24994f")
    protected Text urlText;

    @objid ("c11edaf5-4cc2-4017-8ada-b329a9a8aa7d")
    protected Button browseButton;

    @objid ("49dca79e-a680-4267-ad69-7ddf7602524d")
    protected SelectionAdapter browseSelectionAdapter = new SelectionAdapter() {
		@Override
        public void widgetSelected(SelectionEvent event) {
			FileDialog dialog = new FileDialog(Display.getCurrent()
					.getActiveShell(), SWT.OPEN);
			dialog
					.setFilterExtensions(new String[] {
							"*.gif", "*.jpg", "*.bmp" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String imageFile = dialog.open();
			if (imageFile != null && imageFile.length() > 0) {
				File file = new File(imageFile);
				try {
					String url = file.toURL().toExternalForm();
					AddImageDialog.this.urlText.setText(url);
				} catch (Exception e) {
					AddImageDialog.this.logger.error(e);
				}
			}
		}
	};

    @objid ("25fe2fa4-f0c0-44e0-8073-ef8d8d8dc94d")
    protected ModifyListener urlTextListener = new ModifyListener() {
		@Override
        public void modifyText(ModifyEvent e) {
			if (AddImageDialog.this.okButton != null) {
				AddImageDialog.this.okButton.setEnabled(AddImageDialog.this.urlText.getText().trim().length() > 0);
			}
		}
	};

    /**
     * Creates a new instance.
     * @param parent the parent shell
     */
    @objid ("a6413112-3dae-4b09-b2ef-302e65e8b36d")
    public AddImageDialog(Shell parent) {
        super(parent);
    }

    @objid ("136ddc40-2c74-48b2-b3c6-a5b6fd0cb727")
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.numColumns = 3;
        
        Label urlLabel = new Label(composite, SWT.NONE);
        urlLabel.setText(HtmlTextResources.urlLabel_text);
        this.urlText = new Text(composite, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.widthHint = 300;
        this.urlText.setLayoutData(gridData);
        this.urlText.addModifyListener(this.urlTextListener);
        
        this.browseButton = new Button(composite, SWT.NONE);
        this.browseButton.setText(HtmlTextResources.browseButton_text);
        this.browseButton.addSelectionListener(this.browseSelectionAdapter);
        
        super.getShell().setText(HtmlTextResources.addImageDialog_title);
        return composite;
    }

    @objid ("8ce5f2c4-5f53-4206-9e6b-e9cd2a08e69f")
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        this.okButton.setEnabled(false);
    }

    @objid ("50662edf-7a9a-4a09-8c0b-2788ea2d16d0")
    @Override
    protected void okPressed() {
        String url = this.urlText.getText();
        if (url != null && url.length() > 0) {
            this.image.setURL(url);
        }
        super.okPressed();
    }

    /**
     * Gets the user specified image.
     * @return an <code>Image</code> object
     */
    @objid ("182c5407-f34d-441f-a3e5-6a26f9fcff9d")
    public Image getImage() {
        return this.image;
    }

}
