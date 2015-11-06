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
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Right justifies the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("f57ef6fa-b9db-47ce-871d-16531cb4cd32")
public class JustifyRightAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("4638028e-5e48-487a-b597-af5a0c33b8e4")
    public JustifyRightAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_JUSTIFY_RIGHT);
        setToolTipText(HtmlTextResources.justifyRightAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("9f851f54-74e6-49e7-a50d-4b7ebc1bdac8")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.JUSTIFY_RIGHT);
        }
    }

}
