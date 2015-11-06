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
import java.util.SortedSet;
import java.util.TreeSet;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;

/**
 * Sets the font name for the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @author Jeff Hardy
 * @since 1.0
 */
@objid ("4f997344-1676-4ef9-8c85-bd4138953714")
public class FontNameAction extends RichTextComboAction {
    /**
     * Creates a new instance.
     * @param richText a rich text control
     */
    @objid ("b99da8d1-61c2-4b13-96cd-3863237c2dd2")
    public FontNameAction(final IRichText richText) {
        super(richText);
        setToolTipText(HtmlTextResources.fontNameAction_toolTipText);
        
        // get system fonts
        SortedSet<String> fontSet = new TreeSet<>();
        
        for (FontData font : Display.getCurrent().getFontList(null, true)) {
            fontSet.add(font.getName());
        }
        //for (FontData font : Display.getCurrent().getFontList(null, false)) {
        //    fontSet.add(font.getName());
        // }
        
        
        this.input = new ArrayList<>(fontSet.size()+1);
        this.input.add(HtmlTextResources.fontNameAction_DefaultFontName);
        
        this.input.addAll(fontSet);
        
        // add listener
        richText.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                String fontName = richText.getSelected().getFontName();
                if (fontName.equals(HtmlTextResources.fontNameAction_CSS_Default) 
                        || fontName.equals(HtmlTextResources.fontNameAction_CSS_Default_Mozilla)
                        || fontName.equals("default")) { //$NON-NLS-1$
                    fontName = HtmlTextResources.fontNameAction_DefaultFontName;
                }
                
                int index = findFontNameInItems(fontName);
                setNotifyListeners(false);
                getCCombo().select(index);
                setNotifyListeners(true);
            }
        });
    }

    @objid ("dbef195e-4c22-4cfd-a05f-58e8f2a72397")
    int findFontNameInItems(String fontName) {
        int index = -1;
        for (String font : this.input) {
            index++;
            if (font.equalsIgnoreCase(fontName)) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Executes the action.
     * @param aRichText a rich text control
     */
    @objid ("9e5ef907-08de-4b97-8689-722b38852345")
    @Override
    public void execute(IRichText aRichText) {
        if (aRichText != null) {
            String selected = getCComboSelection();
            if (selected.equals(HtmlTextResources.fontNameAction_DefaultFontName)) {
                aRichText.executeCommand(IRichTextCommands.SET_FONT_NAME, ""); //$NON-NLS-1$
            } else {
                aRichText.executeCommand(IRichTextCommands.SET_FONT_NAME, selected);
            }
        }
    }

    @objid ("39ff5c3f-b6a4-48dd-9da4-3a0cbf951cab")
    @Override
    public Collection<String> getInput() {
        return this.input;
    }

}
