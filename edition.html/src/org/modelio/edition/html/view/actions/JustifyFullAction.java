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
 * Fully justifies the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("049cc1e5-b1ba-40d1-b9a2-3025b8d8183b")
public class JustifyFullAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText a rich text control
     */
    @objid ("11fefc2e-e7f5-4e9d-ac7c-b3ecebdf8ca2")
    public JustifyFullAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_JUSTIFY_FULL);
        setToolTipText(HtmlTextResources.justifyFullAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("7a71e0d8-5e1f-470f-9c77-d3f9ec0b769e")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.JUSTIFY_FULL);
        }
    }

}
