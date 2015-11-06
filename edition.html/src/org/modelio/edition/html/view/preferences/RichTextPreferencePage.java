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
package org.modelio.edition.html.view.preferences;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.edition.html.plugin.HtmlTextResources;

/**
 * The Preference page for the rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("3497c538-947d-4f6b-a38b-5921c20f6f7b")
public class RichTextPreferencePage extends PreferencePage implements SelectionListener, ModifyListener {
    @objid ("55ec5a9a-b21c-4652-ab7a-f15113c5c64f")
    private IPreferenceStore store;

    @objid ("4d5f5faa-be9d-4b8a-9fee-a6a2208cbd53")
    private Text lineWidthText;

    @objid ("ae1d26b3-7b42-4b08-84e8-b361d677a5be")
    private Button indentCheckbox;

    @objid ("c2267097-63ee-4963-91bc-2945c26b7b3a")
    private Text indentSizeText;

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    @objid ("b7870985-73da-4643-b3d3-c52ce4469d47")
    public void init() {
        final IPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE, HtmlTextPlugin.PLUGIN_ID);
        setPreferenceStore(preferenceStore);
        
        this.store = preferenceStore;
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @objid ("f1b28b48-4fc0-46a6-be1f-0dacbc4d6e06")
    @Override
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(1, false));
        composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Composite widthComposite = new Composite(composite, SWT.NONE);
        widthComposite.setLayout(new GridLayout(2, false));
        widthComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        Label lineWidthLabel = new Label(widthComposite, SWT.NONE);
        lineWidthLabel.setText(HtmlTextResources.maxCharsPerLineLabel_text); 
        
        this.lineWidthText = new Text(widthComposite, SWT.BORDER);
        this.lineWidthText.setText(this.store.getString(IRichTextPreferencesConstants.LINE_WIDTH));
        this.lineWidthText.setTextLimit(3);
        GridData gridData = new GridData();
        gridData.widthHint = 25;
        this.lineWidthText.setLayoutData(gridData);
        this.lineWidthText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                // ignore
            }
        });
        
        Composite indentComposite = new Composite(composite, SWT.NONE);
        indentComposite.setLayout(new GridLayout(2, false));
        indentComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        this.indentCheckbox = new Button(indentComposite, SWT.CHECK);
        this.indentCheckbox.setText(HtmlTextResources.indentHTMLCheckbox_text); 
        this.indentCheckbox.setSelection(this.store
                .getBoolean(IRichTextPreferencesConstants.INDENT));
        
        @SuppressWarnings("unused")
        Label label = new Label(indentComposite, SWT.NONE);
        
        Label indentSizeLabel = new Label(indentComposite, SWT.NONE);
        gridData = new GridData();
        gridData.horizontalIndent = 20;
        indentSizeLabel.setLayoutData(gridData);
        indentSizeLabel.setText(HtmlTextResources.indentSizeLabel_text); 
        
        this.indentSizeText = new Text(indentComposite, SWT.BORDER);
        this.indentSizeText
                .setText(this.store.getString(IRichTextPreferencesConstants.INDENT_SIZE));
        this.indentSizeText.setTextLimit(1);
        gridData = new GridData();
        gridData.widthHint = 10;
        this.indentSizeText.setLayoutData(gridData);
        this.indentSizeText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                // ignore
            }
        });
        return composite;
    }

    @objid ("589b4c6d-3837-4140-812e-cd21767eb4c1")
    @Override
    protected void performDefaults() {
        super.performDefaults();
        this.lineWidthText.setText(this.store
                .getDefaultString(IRichTextPreferencesConstants.LINE_WIDTH));
        this.indentCheckbox.setSelection(this.store
                .getDefaultBoolean(IRichTextPreferencesConstants.INDENT));
        this.indentSizeText.setText(this.store
                .getDefaultString(IRichTextPreferencesConstants.INDENT_SIZE));
    }

    @objid ("becfb342-8c28-44c1-af55-370341764df2")
    @Override
    public boolean performOk() {
        String lineWidthValue = this.lineWidthText.getText();
        if (lineWidthValue != null && lineWidthValue.length() > 0) {
            try {
                int lineWidth = Integer.parseInt(lineWidthValue);
                this.store.setValue(IRichTextPreferencesConstants.LINE_WIDTH, lineWidth);
            } catch (Exception e) {
                HtmlTextPlugin.LOG.warning(e);
            }
        }
        
        boolean indentValue = this.indentCheckbox.getSelection();
        this.store.setValue(IRichTextPreferencesConstants.INDENT, indentValue);
        
        String indentSizeValue = this.indentSizeText.getText();
        if (indentSizeValue != null && indentSizeValue.length() > 0) {
            try {
                int indentSize = Integer.parseInt(indentSizeValue);
                this.store.setValue(IRichTextPreferencesConstants.INDENT_SIZE, indentSize);
            } catch (Exception e) {
                HtmlTextPlugin.LOG.warning(e);
            }
        }
        return true;
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
     */
    @objid ("808e75cb-ef08-4918-b338-1f697068e5db")
    @Override
    public void widgetSelected(SelectionEvent e) {
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
     */
    @objid ("eea9f59c-fbe7-4527-8d4c-d596658d44cd")
    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    /**
     * @see org.eclipse.swt.events.ModifyListener#modifyText(ModifyEvent)
     */
    @objid ("e62e2cb9-5833-4d20-97f9-ed3ef0de5689")
    @Override
    public void modifyText(ModifyEvent e) {
    }

}
