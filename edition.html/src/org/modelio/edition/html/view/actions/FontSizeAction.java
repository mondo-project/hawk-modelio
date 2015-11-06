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

import java.util.ArrayList;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Sets the font size for the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("5d19e4b8-0d59-46bc-8ef1-84cc07c62f75")
public class FontSizeAction extends RichTextComboAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("b2b75875-4ebe-454a-a4c6-5dcd1685f3c0")
    public FontSizeAction(final IRichText richText) {
        super(richText);
        setToolTipText(HtmlTextResources.fontSizeAction_toolTipText);
        this.input = new ArrayList<>();
        
        this.input.add("Default"); //$NON-NLS-1$
        this.input.add("1"); //$NON-NLS-1$
        this.input.add("2"); //$NON-NLS-1$
        this.input.add("3"); //$NON-NLS-1$
        this.input.add("4"); //$NON-NLS-1$
        this.input.add("5"); //$NON-NLS-1$
        this.input.add("6"); //$NON-NLS-1$
        this.input.add("7"); //$NON-NLS-1$        
        
        // add listener
        richText.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                // mozilla returns "default" if no size is applied
                // IE returns 2 if no size is applied
                String fontSize = richText.getSelected().getFontSize();
                int index = -1;
                if (fontSize.equals("default")) { //$NON-NLS-1$
                    index = 0;
                } else {
                    try {
                        index = Integer.parseInt(fontSize);
                    } catch (NumberFormatException e) {
                        // leave index at -1 so nothing is selected
                    }
                }
                setNotifyListeners(false);
                getCCombo().select(index);
                setNotifyListeners(true);
            }
        });
    }

    /**
     * Executes the action.
     * @param aRichText a rich text control
     */
    @objid ("19401c22-60e6-4639-9407-4d2ea2120549")
    @Override
    public void execute(IRichText aRichText) {
        if (aRichText != null) {
            String selected = getCComboSelection();
            if ("Default".equals(selected)) { //$NON-NLS-1$
                aRichText.executeCommand(IRichTextCommands.SET_FONT_SIZE, ""); //$NON-NLS-1$
            } else {
                aRichText.executeCommand(IRichTextCommands.SET_FONT_SIZE,
                        selected);
            }
        }
    }

    @objid ("9dc07939-071d-445e-b3ef-05bdbf6af209")
    @Override
    public Collection<String> getInput() {
        return this.input;
    }

}
