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
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Delete the last row of the selected table.
 * 
 * @author Shi Jin
 */
@objid ("e53f1866-7dc3-44d5-810f-391c3120e730")
public class DeleteLastRowAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("794c3c93-52ae-4464-a8de-adc86eabacc9")
    public DeleteLastRowAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_DELETE_ROW);
        //setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_DELETE_ROW);
        setToolTipText(HtmlTextResources.deleteLastRowAction_text);
        setText(HtmlTextResources.deleteLastRowAction_text);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("0d6e98b0-3a0f-4dce-ad07-b78397be5243")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.DELETE_LAST_ROW);
        }
    }

}
