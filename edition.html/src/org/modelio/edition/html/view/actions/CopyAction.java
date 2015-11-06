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
import org.eclipse.swt.custom.StyledText;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.RichTextEditor;

/**
 * Copies the selected text in a rich text control to the clipboard.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("240fea10-0bc9-4eda-8712-a7f865cdf655")
public class CopyAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("3efe613d-8e8e-47cb-b483-9cadb0506a12")
    public CopyAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_COPY);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_COPY);
        setToolTipText(HtmlTextResources.copyAction_toolTipText);
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in readonly mode.
     */
    @objid ("cb21940c-f580-4c7d-8fc3-47fc7fcb8d48")
    @Override
    public boolean disableInReadOnlyMode() {
        return false;
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("ffb7ee17-31da-44a2-aff8-8f807d41a21c")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("9d8a58a4-e5df-4636-b7a3-9273077ce48c")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            if (richText instanceof RichTextEditor
                    && ((RichTextEditor) richText).isHTMLTabSelected()) {
                StyledText styledText = ((RichTextEditor) richText).getSourceEdit();
                styledText.copy();
            } else {
                richText.setCopyURL();
                richText.executeCommand(IRichTextCommands.COPY);
            }
        }
    }

}
