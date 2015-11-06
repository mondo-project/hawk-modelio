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
 * Adds a column after the last column of the selected table.
 * 
 * @author Shi Jin
 */
@objid ("e9537ede-8f0a-44ca-87a9-fc83fa27e6db")
public class AddColumnAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("1970632e-c8b0-4a30-827e-7058a994ae9c")
    public AddColumnAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_ADD_COLUMN);
        //setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_ADD_COLUMN);
        setToolTipText(HtmlTextResources.addColumnAction_text);
        setText(HtmlTextResources.addColumnAction_text);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("9e933f6d-4b27-4621-a482-09353e8db87e")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.ADD_COLUMN);
        }
    }

}
