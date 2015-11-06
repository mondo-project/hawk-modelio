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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Highlight selected text in a rich text control.
 * 
 * @author <a href="mailto:alfredo@anyware-tech.com">Jose Alfredo Serrano</a>
 */
@objid ("244cb75b-1bd0-45ec-83f2-266b542b76da")
public class TextHighlightAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("8144acdd-9f31-4ab4-a88b-e1f10aa85230")
    public TextHighlightAction(final IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_TEXTHIGHLIGHT);
        setToolTipText(HtmlTextResources.textHighlightColor_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("fc891e51-e3be-4c4b-a6bf-cfcb567e71b2")
    @Override
    public void execute(IRichText richText) {
        if (richText != null)
        {
            ColorDialog dialog = new ColorDialog(Display.getCurrent().getActiveShell());
            RGB color = dialog.open();
            if (color != null)
            {
                String rgb = "RGB(" + color.red + "," + color.green + "," + color.blue + ")";  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                String command = IRichTextCommands.BACKGROUND_COLOR;
                String platform = SWT.getPlatform ();
                if ("motif".equals (platform) || "gtk".equals(platform)) { //$NON-NLS-1$
                    // for this kind of graphical libs a Mozilla browser is created.
                    // a different command should be created
                    command = "mozillaBackColor";
                } 
                richText.executeCommand(command, rgb);
            }
        }
    }

}
