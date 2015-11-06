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
                                    

package org.modelio.edition.html.view.actions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.RichText;
import org.modelio.edition.html.view.dialogs.AddCodeDialog;

/**
 * Add code action
 */
@objid ("ddd2ded5-7a01-4e08-97db-da12b5205a3e")
public class AddCodeAction extends RichTextAction {
    /**
     * @param richText the rich text
     */
    @objid ("3e73dd10-920e-41f6-87f4-0e7f78ebcb09")
    public AddCodeAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_ADD_CODE);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_ADD_CODE);
        setToolTipText(HtmlTextResources.addCodeAction_toolTipText);
    }

    @objid ("c4ea2ed3-df7b-4b4a-8599-86843fa1a125")
    @Override
    public void execute(IRichText richText) {
        if (richText != null ) {
            AddCodeDialog dialog = new AddCodeDialog(Display.getCurrent().getActiveShell());
            dialog.open();
            if (dialog.getReturnCode() == Window.OK) {
                String html = RichText.workaroundForObjectParamNode(dialog.getCode());
                richText.executeCommand(IRichTextCommands.ADD_HTML, html);
            }            
        }
    }

}
