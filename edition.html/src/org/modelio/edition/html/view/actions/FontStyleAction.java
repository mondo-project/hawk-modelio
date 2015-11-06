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
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.html.FontStyle;

/**
 * Sets the font style for the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("d7362a8f-c921-4b6e-b682-7b01f3cc77b8")
public class FontStyleAction extends RichTextComboAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("c24755c5-e4d6-46ec-9ae1-1202d4db5834")
    public FontStyleAction(final IRichText richText) {
        super(richText);
        setToolTipText(HtmlTextResources.fontStyleAction_toolTipText);
        
        this.input = new ArrayList<>();
        this.input.add(FontStyle.NORMAL.getName());
        this.input.add(FontStyle.SECTION_HEADING.getName()); // H3
        this.input.add(FontStyle.SUBSECTION_HEADING.getName()); // H4
        this.input.add(FontStyle.SUB_SUBSECTION_HEADING.getName()); // H5
        this.input.add(FontStyle.QUOTE.getName());
        this.input.add(FontStyle.CODE_SAMPLE.getName());
        
        // add listener
        richText.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String blockStyle = richText.getSelected().getBlockStyle();
                String name = FontStyle.getFontStyleName(blockStyle);
                
                int index = ((List<String>)getInput()).indexOf(name);
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
    @objid ("00ab5f77-909e-4d13-a670-b346b235cdf3")
    @Override
    public void execute(IRichText aRichText) {
        if (aRichText != null) {
            String selected = getCComboSelection();
            String value = FontStyle.getFontStyleValue(selected);
            aRichText.executeCommand(IRichTextCommands.SET_FONT_STYLE, value);
        }
    }

    @objid ("1c9fbba4-5b1f-4b27-8a11-9c917846c5bd")
    @Override
    public Collection<String> getInput() {
        return this.input;
    }

}
