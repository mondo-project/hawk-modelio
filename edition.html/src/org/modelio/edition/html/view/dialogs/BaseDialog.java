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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.modelio.edition.html.plugin.HtmlTextPlugin;
import org.modelio.log.writers.PluginLogger;

/**
 * The base class for all dialogs used by the rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("174bc9c7-0720-44d7-8738-5c1546b19c2a")
public class BaseDialog extends Dialog {
// A logger for logging runtime warnings and errors.
    @objid ("c807f1bf-1805-4c9c-a0ec-39b9e7672662")
    protected PluginLogger logger;

// The OK button.
    @objid ("3a16adc6-40f7-47d0-9407-da3dff41f466")
    protected Button okButton;

// The Cancel button.
    @objid ("9d6e099a-1172-4be5-b613-61e3044b1c7c")
    protected Button cancelButton;

    /**
     * Creates a new instance.
     * @param parent the parent shell
     */
    @objid ("5e03c0b9-fa99-497b-a9f3-6d4a0134e740")
    public BaseDialog(Shell parent) {
        super(parent);
        this.logger = HtmlTextPlugin.getDefault().getLogger();
    }

    @objid ("dd8c951b-d60e-4e63-ad63-3ddaba06028b")
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.marginWidth = 10;
        layout.marginHeight = 10;
        layout.numColumns = 2;
        GridData gridData = (GridData) composite.getLayoutData();
        gridData.verticalIndent = 10;
        return composite;
    }

    @objid ("afd41b03-9fe6-4241-9591-f3c2e7fb02e4")
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Create the OK button.
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        
        // Create the Cancel button.
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
        
        // Set help context for the OK button.
        this.okButton = super.getButton(IDialogConstants.OK_ID);
        
        // Set help context for the Cancel button.
        this.cancelButton = super.getButton(IDialogConstants.CANCEL_ID);
    }

}
