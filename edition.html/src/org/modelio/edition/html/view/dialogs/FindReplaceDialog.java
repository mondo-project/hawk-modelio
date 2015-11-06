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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.actions.FindReplaceAction;

/**
 * Prompts the user to specify the search and replace strings and options.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("8ba2ea74-6078-4513-95c6-f7fcce6b2027")
public class FindReplaceDialog extends BaseDialog {
    @objid ("9038c9e4-0267-42d4-b430-ae8b1d8b1838")
    private boolean findOnly;

    @objid ("3e70d41a-98d6-466e-8679-5cf885984b2e")
    private FindReplaceAction findReplaceAction;

    @objid ("c3dcf806-26f8-4df9-a5d3-38409df5ebb5")
    private Text findText;

    @objid ("09fcd111-8b8a-42bd-80bc-eea1fc5c1eff")
    private Text replaceText;

    @objid ("865a2b2a-dd76-43b8-a964-dc1efa58007c")
    private Button searchForwardRadioButton;

    @objid ("758b8fad-4dab-42f4-82d3-8517cea96e5f")
    private Button searchBackwardRadioButton;

    @objid ("831ed5cf-1db8-4bfc-aac6-b4548d77265c")
    private Button caseSensitiveCheckbox;

    @objid ("1188765d-1b1d-48da-8772-7e427442ad1f")
    private Button wholeWordCheckbox;

    @objid ("4d5611cc-f804-493f-8b2a-621e79a0614f")
    private Button findButton;

    @objid ("a54f8297-fefa-4926-b938-de1cde0f7a11")
    private Button replaceButton;

    @objid ("b2bf7fad-e7ca-47fb-83bc-1fd2811387fe")
    private Button replaceFindButton;

    @objid ("bd900fd2-9596-460e-bc1d-5628d4061ff7")
    private Button replaceAllButton;

    @objid ("613b5233-37f0-4347-8121-6423f1fb6d2f")
    private Label statusLabel;

    /**
     * Creates a new instance.
     * @param parent the parent shell
     * @param findReplaceAction the Find and Replace action
     * @param findOnly if <code>true</code>, disable the replace and replace all
     * functionalities
     */
    @objid ("7c038097-c286-40f9-8517-7c9e441c4276")
    public FindReplaceDialog(Shell parent, FindReplaceAction findReplaceAction, boolean findOnly) {
        super(parent);
        setShellStyle(SWT.DIALOG_TRIM | SWT.MODELESS | getDefaultOrientation());
        setBlockOnOpen(false);
        this.findReplaceAction = findReplaceAction;
        this.findOnly = findOnly;
    }

    @objid ("5692052e-1809-4388-a8d1-56a4ea280772")
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.numColumns = 1;
        
        Composite textComposite = new Composite(composite, SWT.NONE);
        textComposite.setLayout(new GridLayout(2, false));
        textComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label findLabel = new Label(textComposite, SWT.NONE);
        findLabel.setText(HtmlTextResources.findLabel_text);
        this.findText = new Text(textComposite, SWT.BORDER);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.widthHint = 200;
        this.findText.setLayoutData(gridData);
        this.findText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (FindReplaceDialog.this.findButton != null) {
                    FindReplaceDialog.this.findButton
                            .setEnabled(FindReplaceDialog.this.findText.getText().trim().length() > 0);
                }
                if (FindReplaceDialog.this.replaceAllButton != null && !FindReplaceDialog.this.findOnly) {
                    FindReplaceDialog.this.replaceAllButton.setEnabled(FindReplaceDialog.this.findText.getText().trim()
                            .length() > 0);
                }
            }
        });
        
        Label replaceLabel = new Label(textComposite, SWT.NONE);
        replaceLabel.setText(HtmlTextResources.replaceLabel_text);
        this.replaceText = new Text(textComposite, SWT.BORDER);
        this.replaceText.setLayoutData(gridData);
        if (this.findOnly) {
            this.replaceText.setEnabled(false);
        } else {
            this.replaceText.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    if (FindReplaceDialog.this.replaceButton != null) {
                        FindReplaceDialog.this.replaceButton.setEnabled(FindReplaceDialog.this.findReplaceAction
                                .getFoundMatch());
                    }
                    if (FindReplaceDialog.this.replaceFindButton != null) {
                        FindReplaceDialog.this.replaceFindButton.setEnabled(FindReplaceDialog.this.findReplaceAction
                                .getFoundMatch());
                    }
                }
            });
        }
        
        Composite optionsComposite = new Composite(composite, SWT.NONE);
        optionsComposite.setLayout(new GridLayout(2, true));
        optionsComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Group directionGroup = new Group(optionsComposite, SWT.NONE);
        directionGroup.setText(HtmlTextResources.directionGroup_text);
        directionGroup.setLayout(new GridLayout(1, false));
        directionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.searchForwardRadioButton = new Button(directionGroup, SWT.RADIO);
        this.searchForwardRadioButton
                .setText(HtmlTextResources.forwardRadioButton_text);
        this.searchForwardRadioButton.setSelection(true);
        this.searchBackwardRadioButton = new Button(directionGroup, SWT.RADIO);
        this.searchBackwardRadioButton
                .setText(HtmlTextResources.backwardRadioButton_text);
        
        Group optionsGroup = new Group(optionsComposite, SWT.NONE);
        optionsGroup.setText(HtmlTextResources.optionsGroup_text);
        optionsGroup.setLayout(new GridLayout(1, false));
        optionsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.caseSensitiveCheckbox = new Button(optionsGroup, SWT.CHECK);
        this.caseSensitiveCheckbox
                .setText(HtmlTextResources.caseSensitiveCheckbox_text);
        this.wholeWordCheckbox = new Button(optionsGroup, SWT.CHECK);
        this.wholeWordCheckbox.setText(HtmlTextResources.wholeWordCheckbox_text);
        
        this.statusLabel = new Label(composite, SWT.NONE);
        this.statusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        super.getShell().setText(HtmlTextResources.findReplaceDialog_title);
        return composite;
    }

    @objid ("301e271e-b294-4136-b4b4-ba7410b4f9cd")
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CLIENT_ID + 1,
                HtmlTextResources.findButton_text, true);
        this.findButton = super.getButton(IDialogConstants.CLIENT_ID + 1);
        this.findButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent event) {
                FindReplaceDialog.this.findReplaceAction.run(FindReplaceAction.FIND_TEXT,
                        getFindText(), getReplaceText(), getMatchDirection(),
                        getMatchOptions());
                if (!FindReplaceDialog.this.findOnly) {
                    FindReplaceDialog.this.replaceButton.setEnabled(FindReplaceDialog.this.findReplaceAction.getFoundMatch());
                    FindReplaceDialog.this.replaceFindButton.setEnabled(FindReplaceDialog.this.findReplaceAction
                            .getFoundMatch());
                }
                if (FindReplaceDialog.this.findReplaceAction.getFoundMatch()) {
                    FindReplaceDialog.this.statusLabel.setText(""); //$NON-NLS-1$
                } else {
                    FindReplaceDialog.this.statusLabel
                            .setText(HtmlTextResources.FindReplace_Status_noMatch_label);
                }
                FindReplaceDialog.this.findButton.setFocus();
            }
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
        
        createButton(parent, IDialogConstants.CLIENT_ID + 2,
                HtmlTextResources.replaceButton_text, false);
        this.replaceButton = super.getButton(IDialogConstants.CLIENT_ID + 2);
        if (!this.findOnly) {
            this.replaceButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    FindReplaceDialog.this.findReplaceAction.run(FindReplaceAction.REPLACE_TEXT,
                            getFindText(), getReplaceText(),
                            getMatchDirection(), getMatchOptions());
                    FindReplaceDialog.this.replaceButton.setFocus();
                }
        
                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });
        }
        
        createButton(parent, IDialogConstants.CLIENT_ID + 3,
                HtmlTextResources.replaceFindButton_text, false);
        this.replaceFindButton = super.getButton(IDialogConstants.CLIENT_ID + 3);
        if (!this.findOnly) {
            this.replaceFindButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    FindReplaceDialog.this.findReplaceAction.run(FindReplaceAction.REPLACE_FIND_TEXT,
                            getFindText(), getReplaceText(),
                            getMatchDirection(), getMatchOptions());
                    FindReplaceDialog.this.replaceButton.setEnabled(FindReplaceDialog.this.findReplaceAction.getFoundMatch());
                    FindReplaceDialog.this.replaceFindButton.setEnabled(FindReplaceDialog.this.findReplaceAction
                            .getFoundMatch());
                    FindReplaceDialog.this.replaceFindButton.setFocus();
                }
        
                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });
        }
        
        createButton(parent, IDialogConstants.CLIENT_ID + 4,
                HtmlTextResources.replaceallButton_text, false);
        this.replaceAllButton = super.getButton(IDialogConstants.CLIENT_ID + 4);
        if (!this.findOnly) {
            this.replaceAllButton.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    FindReplaceDialog.this.findReplaceAction.run(FindReplaceAction.REPLACE_ALL_TEXT,
                            getFindText(), getReplaceText(),
                            getMatchDirection(), getMatchOptions());
                    FindReplaceDialog.this.replaceButton.setEnabled(false);
                    FindReplaceDialog.this.replaceFindButton.setEnabled(false);
                    FindReplaceDialog.this.replaceAllButton.setFocus();
                }
        
                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }
            });
        }
        
        // Create the Cancel button.
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
        this.cancelButton = super.getButton(IDialogConstants.CANCEL_ID);
        
        this.findButton.setEnabled(false);
        this.replaceButton.setEnabled(false);
        this.replaceFindButton.setEnabled(false);
        this.replaceAllButton.setEnabled(false);
    }

    /**
     * Gets the user specified find text.
     * @return the find text
     */
    @objid ("b1af2070-27f6-423c-bcdf-880e95a60e47")
    public String getFindText() {
        return this.findText.getText();
    }

    /**
     * Gets the user specified replace text.
     * @return the replace text
     */
    @objid ("87925753-ccf1-4edb-b76d-8155e1040b40")
    public String getReplaceText() {
        return this.replaceText.getText();
    }

    /**
     * Gets the text match direction.
     * @return <code>FIND_FORWARD</code> or <code>FIND_BACKWARD</code>
     */
    @objid ("6e69b41d-3097-4bfb-9f16-9f36787e12c0")
    public int getMatchDirection() {
        return this.searchForwardRadioButton.getSelection() ? FindReplaceAction.FORWARD_MATCH
                : FindReplaceAction.BACKWARD_MATCH;
    }

    /**
     * Gets the text match options.
     * @return the text match options
     */
    @objid ("4dbb7d45-0c7c-4f76-ad5b-a01de6e1e865")
    public int getMatchOptions() {
        int options = 0;
        if (this.wholeWordCheckbox.getSelection() == true) {
            options |= FindReplaceAction.WHOLE_WORD_MATCH;
        }
        if (this.caseSensitiveCheckbox.getSelection() == true) {
            options |= FindReplaceAction.CASE_SENSITIVE_MATCH;
        }
        return options;
    }

    /**
     * Checks the find only option.
     * @return <code>true</code> if find only option is enabled
     */
    @objid ("dc05c3b4-bc26-4fc5-9607-004c5e56a2bf")
    public boolean isFindOnly() {
        return this.findOnly;
    }

    /**
     * Sets the find only option.
     * @param findOnly,
     * if <code>true</code>, enable the find only option
     */
    @objid ("4542a44c-76eb-4cc1-b580-2a84a94b2b10")
    public void setFindOnly(boolean findOnly) {
        this.findOnly = findOnly;
    }

}
