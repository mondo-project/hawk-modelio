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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.modelio.edition.html.plugin.HtmlTextImages;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Toggles the superscript attribute of the selected text in a rich text
 * control.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("142953b8-d84c-4eba-8114-534b2158ca23")
public class SuperscriptAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("c8537030-0b40-4320-9391-42d51f086836")
    public SuperscriptAction(final IRichText richText) {
        super(richText, IAction.AS_CHECK_BOX);
        setImageDescriptor(HtmlTextImages.IMG_DESC_SUPERSCRIPT);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_SUPERSCRIPT);
        setToolTipText(HtmlTextResources.superscriptAction_toolTipText);
        // add listener
        richText.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                setChecked(richText.getSelected().isSuperscript());
            }
        });
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("4c60bf6b-b771-40cb-993c-8dfe056cb2fa")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.SUPERSCRIPT);
        }
    }

}
