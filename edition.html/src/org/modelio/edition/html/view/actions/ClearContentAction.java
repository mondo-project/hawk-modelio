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
package org.modelio.edition.html.view.actions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Clears the content of a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("8a200245-1c15-4ae8-86ca-dd599c4d3074")
public class ClearContentAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("0e4a80b5-0f01-4c60-99ac-6dce3d7224bf")
    public ClearContentAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_CLEAR_CONTENT);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_CLEAR_CONTENT);
        setToolTipText(HtmlTextResources.clearContentAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("41573189-86c0-460f-9892-2a28e24df3f7")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            boolean ok = MessageDialog.openQuestion(richText.getControl().getShell(), 
                    HtmlTextResources.clearContentDialog_title,
                    HtmlTextResources.clearContentDialog_text);
            if (ok) {
                richText.executeCommand(IRichTextCommands.CLEAR_CONTENT);
            }
        }
    }

}
