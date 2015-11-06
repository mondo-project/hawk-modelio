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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.dialogs.AddImageDialog;

/**
 * Adds an image to a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("e63c2a25-f5e0-440a-b03a-db221f01d22b")
public class AddImageAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("9de5a408-1c9b-4c9d-b09c-bf5bdd435914")
    public AddImageAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_ADD_IMAGE);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_ADD_IMAGE);
        setToolTipText(HtmlTextResources.addImageAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("e8ec0ce0-0177-406b-aa09-27c246110d9c")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            AddImageDialog dialog = new AddImageDialog(Display.getCurrent()
                    .getActiveShell());
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                String imageURL = dialog.getImage().getURL();
                if (imageURL.length() > 0) {
                    richText
                            .executeCommand(IRichTextCommands.ADD_IMAGE, imageURL);
                }
            }
        }
    }

    @objid ("1e4792da-c560-4638-9cab-4fbf57f6c722")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

}
