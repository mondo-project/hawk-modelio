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
 * Delete the last column of the selected table.
 * 
 * @author Shi Jin
 */
@objid ("938ea500-38cb-47c8-bac8-31a7d5af7329")
public class DeleteLastColumnAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("7c3edf91-57d4-4c63-91f4-e39f99644f56")
    public DeleteLastColumnAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_DELETE_COLUMN);
        //        setDisabledImageDescriptor(RichTextImages.DISABLED_IMG_DESC_ADD_ROW);
        setToolTipText(HtmlTextResources.deleteLastColumnAction_text);
        setText(HtmlTextResources.deleteLastColumnAction_text);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("e711d7d4-7f55-42b2-a3fc-3a483f9584ec")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.DELETE_LAST_COLUMN);
        }
    }

}
