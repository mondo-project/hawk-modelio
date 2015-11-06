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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.html.Table;

/**
 * Prompts the user to specify the attributes that will be used to create a HTML
 * &lt;table&gt; tag in the rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("ed5b489d-67f2-4628-8a43-f778e1bb545b")
public class AddTableDialog extends BaseDialog {
    @objid ("f0b31e8c-24b0-4051-bf4d-04e7b4e25acc")
    private static final int DEFAULT_ROWS = 2;

    @objid ("7ed8f29a-3615-4811-a2f2-388f524a28f8")
    private static final int DEFAULT_COLUMNS = 2;

    @objid ("1bf948d6-6472-4b90-a4f1-1ee0b782fa3b")
    private static final String DEFAULT_WIDTH = "85%"; // $NON-NLS-1$

    @objid ("19a278b1-3816-49db-a6a4-cee6a7dd85f4")
    private static final String[] TABLE_STYLE_LABELS = {
			HtmlTextResources.tableHeaderNone_text,
			HtmlTextResources.tableHeaderCols_text,
			HtmlTextResources.tableHeaderRows_text,
			HtmlTextResources.tableHeaderBoth_text, };

    @objid ("14de135e-b672-492d-9fbe-12bd079cf59b")
    private Table table = new Table();

    @objid ("d1a76439-eee8-4d59-8fe3-2758e6ce3df2")
    private Text rowsText;

    @objid ("1fc55cd3-d44c-4bc2-b630-fc279ecd32e1")
    private Text colsText;

    @objid ("f48c4b33-f868-42c2-8b7d-4313e5c5da3d")
    private Text widthText;

    @objid ("92d44105-e375-4861-8e3e-34521d6ea082")
    private Combo tableTypeCombo;

    @objid ("44925c97-da5a-4091-b6a3-ff0136a4a912")
    private Text summaryText;

    @objid ("c805eff8-1cce-4381-8d70-0b51ab958988")
    private Text captionText;

    @objid ("fdf78d89-8798-4e18-997b-2085147e03af")
    private ModifyListener modifyListener = new ModifyListener() {
		@Override
        public void modifyText(ModifyEvent event) {
			if (AddTableDialog.this.okButton != null) {
				try {
					int rows = Integer.parseInt(AddTableDialog.this.rowsText.getText().trim());
					int cols = Integer.parseInt(AddTableDialog.this.colsText.getText().trim());
					String width = AddTableDialog.this.widthText.getText().trim();
					AddTableDialog.this.okButton.setEnabled(rows > 0 && cols > 0
							&& width.length() > 0);
				} catch (Exception e) {
					AddTableDialog.this.okButton.setEnabled(false);
				}
			}
		}
	};

    /**
     * Creates a new instance.
     * @param parent the parent shell
     */
    @objid ("27adf4cf-8658-434e-b5ec-5fa819ea6885")
    public AddTableDialog(Shell parent) {
        super(parent);
    }

    @objid ("abb7eb62-9f29-451d-8c67-1068745d0acf")
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        
        Label rowsLabel = new Label(composite, SWT.NONE);
        rowsLabel.setText(HtmlTextResources.rowsLabel_text);
        
        this.rowsText = new Text(composite, SWT.BORDER);
        this.rowsText.setTextLimit(2);
        this.rowsText.setText("" + DEFAULT_ROWS); //$NON-NLS-1$
        {
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL
                    | GridData.GRAB_HORIZONTAL);
            this.rowsText.setLayoutData(gridData);
        }
        this.rowsText.addModifyListener(this.modifyListener);
        
        Label colsLabel = new Label(composite, SWT.NONE);
        colsLabel.setText(HtmlTextResources.columnsLabel_text);
        
        this.colsText = new Text(composite, SWT.BORDER);
        this.colsText.setTextLimit(2);
        this.colsText.setText("" + DEFAULT_COLUMNS); //$NON-NLS-1$
        {
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL
                    | GridData.GRAB_HORIZONTAL);
            this.colsText.setLayoutData(gridData);
        }
        this.colsText.addModifyListener(this.modifyListener);
        
        Label widthLabel = new Label(composite, SWT.NONE);
        widthLabel.setText(HtmlTextResources.widthLabel_text);
        
        this.widthText = new Text(composite, SWT.BORDER);
        this.widthText.setText("" + DEFAULT_WIDTH); //$NON-NLS-1$
        {
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL
                    | GridData.GRAB_HORIZONTAL);
            this.widthText.setLayoutData(gridData);
        }
        this.widthText.addModifyListener(this.modifyListener);
        
        Label headerTypeLabel = new Label(composite, SWT.NONE);
        headerTypeLabel.setText(HtmlTextResources.tableStyleLabel_text);
        
        this.tableTypeCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
        this.tableTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.tableTypeCombo.setItems(TABLE_STYLE_LABELS);
        this.tableTypeCombo.setText(TABLE_STYLE_LABELS[0]);
        
        Label summaryLabel = new Label(composite, SWT.NONE);
        summaryLabel.setText(HtmlTextResources.summaryLabel_text);
        this.summaryText = new Text(composite, SWT.BORDER);
        {
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL
                    | GridData.GRAB_HORIZONTAL);
            this.summaryText.setLayoutData(gridData);
        }
        
        Label captionLabel = new Label(composite, SWT.NONE);
        captionLabel.setText(HtmlTextResources.captionLabel_text);
        this.captionText = new Text(composite, SWT.BORDER);
        {
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL
                    | GridData.GRAB_HORIZONTAL);
            this.captionText.setLayoutData(gridData);
        }
        
        super.getShell().setText(HtmlTextResources.addTableDialog_title);
        return composite;
    }

    @objid ("7271923f-ce3e-4b8c-8bf4-5e7b1644fcc6")
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        super.createButtonsForButtonBar(parent);
    }

    @objid ("f0992386-68e0-4bed-8b53-48328ad8ef5f")
    @Override
    protected void okPressed() {
        String rowsValue = this.rowsText.getText();
        if (rowsValue != null && rowsValue.length() > 0) {
            try {
                int rows = Integer.parseInt(rowsValue);
                this.table.setRows(rows);
            } catch (Exception e) {
                this.table.setRows(DEFAULT_ROWS);
            }
        }
        
        String colsValue = this.colsText.getText();
        if (colsValue != null && colsValue.length() > 0) {
            try {
                int cols = Integer.parseInt(colsValue);
                this.table.setColumns(cols);
            } catch (Exception e) {
                this.table.setColumns(DEFAULT_COLUMNS);
            }
        }
        
        String widthValue = this.widthText.getText();
        if (widthValue != null && widthValue.length() > 0) {
            this.table.setWidth(widthValue);
        } else {
            this.table.setWidth(DEFAULT_WIDTH);
        }
        
        this.table.setSummary(this.summaryText.getText().trim());
        this.table.setCaption(this.captionText.getText().trim());
        this.table.setTableHeaders(this.tableTypeCombo.getSelectionIndex());
        
        super.okPressed();
    }

    /**
     * Gets the user specified table.
     * @return an <code>Table</code> object
     */
    @objid ("a985b1f2-0cc1-425f-ae77-673fbe957a99")
    public Table getTable() {
        return this.table;
    }

}
