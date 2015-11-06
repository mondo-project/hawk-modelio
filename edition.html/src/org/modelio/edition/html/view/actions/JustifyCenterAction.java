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
 * Center justifies the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("efc3dc85-87c9-4ba0-8b4b-87e389509610")
public class JustifyCenterAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("07b27e54-f59a-456a-adc0-05c6f0051934")
    public JustifyCenterAction(IRichText richText) {
        super(richText, IAction.AS_PUSH_BUTTON);
        setImageDescriptor(HtmlTextImages.IMG_DESC_JUSTIFY_CENTER);
        setToolTipText(HtmlTextResources.justifyCenterAction_toolTipText);
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("882dec23-deac-466d-8d40-c4fffa4a702f")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.JUSTIFY_CENTER);
        }
    }

}
