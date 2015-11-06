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
import org.modelio.edition.html.view.dialogs.AddLinkDialog;

/**
 * Adds a link to a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("033db5aa-4cd1-4459-aff5-1fee0d3543ad")
public class AddLinkAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("a3c132d0-efd4-4cf1-b40b-602ae004ff16")
    public AddLinkAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_ADD_LINK);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_ADD_LINK);
        setToolTipText(HtmlTextResources.addLinkAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("a879ff35-d279-4aa5-8b50-926e915dae36")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            AddLinkDialog dialog = new AddLinkDialog(Display.getCurrent()
                    .getActiveShell(), richText.getBasePath());
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                String linkURL = dialog.getLink().getURL();
                if (linkURL.length() > 0) {
                    richText.executeCommand(IRichTextCommands.ADD_LINK, linkURL);
                }
            }
        }
    }

    @objid ("394e96ee-9cdf-40ee-8880-9281a634e330")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

}
