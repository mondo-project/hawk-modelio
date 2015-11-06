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
 * Toggles the underline attribute of the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("53529650-2f72-4317-a256-14b64c87a98e")
public class UnderlineAction extends RichTextAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("a959356a-72f5-47d5-9af4-645e0dac4c18")
    public UnderlineAction(final IRichText richText) {
        super(richText, IAction.AS_CHECK_BOX);
        setImageDescriptor(HtmlTextImages.IMG_DESC_UNDERLINE);
        setDisabledImageDescriptor(HtmlTextImages.DISABLED_IMG_DESC_UNDERLINE);
        setToolTipText(HtmlTextResources.underlineAction_toolTipText);
        // add listener
        richText.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                setChecked(richText.getSelected().isUnderLine());
            }
        });
    }

    /**
     * Executes the action.
     * @param richText a rich text control
     */
    @objid ("5e2c8675-9df9-4c77-8695-2494d295b1ab")
    @Override
    public void execute(IRichText richText) {
        if (richText != null) {
            richText.executeCommand(IRichTextCommands.UNDERLINE);
        }
    }

}
