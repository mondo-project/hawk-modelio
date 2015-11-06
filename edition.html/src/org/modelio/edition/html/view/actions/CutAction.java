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
 * Cuts the selected text in the rich text control to the clipboard.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("7dd8fe5c-2a24-4ad9-9983-1ecc9cb19698")
public class CutAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText a rich text control
     */
    @objid ("0b9f4d3e-5c2e-451b-ba0f-d9499b79c605")
    public CutAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_CUT);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_CUT);
        setToolTipText(HtmlTextResources.cutAction_toolTipText);
    }

    /**
     * Returns <code>true</code> if this action should be disabled when the
     * rich text editor is in source edit mode.
     */
    @objid ("92aff731-a147-4b53-91c9-506d0814ff81")
    @Override
    public boolean disableInSourceMode() {
        return false;
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("b0ba41a1-03cc-4c0c-ac08-f1cf005e8276")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            if (richText instanceof RichTextEditor
                    && ((RichTextEditor) richText).isHTMLTabSelected()) {
                StyledText styledText = ((RichTextEditor) richText)
                        .getSourceEdit();
                styledText.cut();
            } else {
                richText.setCopyURL();
                richText.executeCommand(IRichTextCommands.CUT);
            }
        }
    }

}
