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
import org.modelio.edition.html.plugin.HtmlTextResources;
import org.modelio.edition.html.view.IRichText;
import org.modelio.edition.html.view.IRichTextCommands;
import org.modelio.edition.html.view.html.BlockTag;
import org.modelio.edition.html.view.html.FontStyle;

/**
 * Sets the font name for the selected text in a rich text control.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("a89be873-ea24-4fb9-8eb4-367e79057c73")
public class BlockTagAction extends RichTextComboAction {
    /**
     * Creates a new instance.
     * @param richText the rich text
     */
    @objid ("be1b4b87-5b58-4f96-9a49-16cd65ea6214")
    public BlockTagAction(final IRichText richText) {
        super(richText);
        setToolTipText(HtmlTextResources.blockTagAction_toolTipText);
    }

    @objid ("7f514976-14fe-4b8f-a25c-d87993c631cd")
    @Override
    public Collection<String> getInput() {
        Collection<String> returnList = new ArrayList<>(9);
        
        returnList.add(BlockTag.PARAGRAPH.getName());
        returnList.add(BlockTag.HEADING_1.getName());
        returnList.add(BlockTag.HEADING_2.getName());
        returnList.add(BlockTag.HEADING_3.getName());
        returnList.add(BlockTag.HEADING_4.getName());
        returnList.add(BlockTag.HEADING_5.getName());
        returnList.add(BlockTag.HEADING_6.getName());
        returnList.add(BlockTag.ADDRESS.getName());
        returnList.add(BlockTag.PREFORMATTED_TEXT.getName());
        return returnList;
    }

    /**
     * Executes the action.
     * @param aRichText a rich text control
     */
    @objid ("33aa043b-417c-4f53-8580-01b4453e4b8f")
    @Override
    public void execute(IRichText aRichText) {
        if (aRichText != null) {
            String selected = getCComboSelection();
            String value = FontStyle.getFontStyleValue(selected);
            aRichText.executeCommand(IRichTextCommands.FORMAT_BLOCK, value);
        }
    }

}
