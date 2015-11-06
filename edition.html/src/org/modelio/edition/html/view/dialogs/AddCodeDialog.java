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

@objid ("f8bdfea3-0bc9-4d8b-b7b9-a7e5478bfb6b")
public class AddCodeDialog extends BaseDialog {
    @objid ("71ba567f-8d1e-4182-a209-cfdc30a69700")
    private String htmlStr;

    @objid ("a5940473-b24f-4e11-a221-0e4d44e39c43")
    private Text html;

    @objid ("ff3c8afe-4a12-4741-afb7-46c947395192")
    public AddCodeDialog(Shell parent) {
        super(parent);
    }

    @objid ("81399b2d-44ef-4edf-89f0-1e04b274eb6e")
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        Label label = new Label(composite, SWT.NONE);
        label.setText(HtmlTextResources.addCodeDialog_Msg);
        {
            GridData gd = new GridData(GridData.FILL_HORIZONTAL);
            gd.horizontalSpan = 2;
            label.setLayoutData(gd);
        }
        
        this.html = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        {
            GridData gd = new GridData(GridData.FILL_BOTH);
            gd.horizontalSpan = 2;
            gd.widthHint = 300;
            gd.heightHint = 200;
            this.html.setLayoutData(gd);
        }
        this.html.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {                
                AddCodeDialog.this.okButton.setEnabled(AddCodeDialog.this.html.getText().trim().length() > 0);        
            }            
        });
        
        super.getShell().setText(HtmlTextResources.addCodeDialog_title);
        return composite;
    }

    @objid ("dad76708-abca-4548-9283-ae9f58ea6281")
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
        this.okButton.setEnabled(false);
    }

    @objid ("30f1d067-01a7-4382-b86b-1fe9899a2ced")
    @Override
    protected void okPressed() {
        //add the extra "<br/>" to avoid Jtidy problem
        String br = "<br/>"; //$NON-NLS-1$
        String customHtml = this.html.getText().trim();        
        this.htmlStr = br + customHtml;
        
        super.okPressed();
    }

    @objid ("ce49b79c-800c-4750-84b2-0381a92058d0")
    public String getCode() {
        return this.htmlStr;
    }

}
