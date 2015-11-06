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
//         Anyware Technologies - implementation
//------------------------------------------------------------------------------
package org.modelio.edition.html.view.actions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Set the color attribute of the selected text in a rich text control.
 * 
 * @author <a href="mailto:alfredo@anyware-tech.com">Jose Alfredo Serrano</a>
 */
@objid ("4649f1b7-b042-4743-b911-856019b4a2af")
public class TextColorAction extends RichTextAction {
    @objid ("2cc7fd20-c669-46ef-899b-184dfec63fb3")
    private String command = IRichTextCommands.FORGROUND_COLOR;

    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("ba1814ac-5bc8-44e0-810f-99b3024fbc2c")
    public TextColorAction(final IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_TEXTCOLOR);
        setToolTipText(HtmlTextResources.textColorAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("c0db29c4-6a26-4025-a9dd-cf00eebf07fa")
    @Override
    public void execute(IRichText richText) {
        if (richText != null)
        {
            ColorDialog dialog = new ColorDialog(Display.getCurrent().getActiveShell());
            RGB color = dialog.open();
            if (color != null)
            {
                String rgb = "RGB(" + color.red + "," + color.green + "," + color.blue + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                richText.executeCommand(this.command, rgb);
            }
        }
    }

}
