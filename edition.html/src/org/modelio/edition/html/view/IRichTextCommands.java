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
package org.modelio.edition.html.view;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Defines the editing commands supported by the default rich text control and
 * editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("068f95e4-0a19-4234-a1df-98a5ad2ef9ef")
public interface IRichTextCommands {
    /**
     * Adds a HTML fragment.
     */
    @objid ("bb970e11-890e-4ffe-8083-dffc28a894e0")
    public static final String ADD_HTML = "addHTML"; // $NON-NLS-1$

    /**
     * Adds an image.
     */
    @objid ("3654c143-2587-44a3-a06d-afeab0793cd9")
    public static final String ADD_IMAGE = "addImage"; // $NON-NLS-1$

    /**
     * Adds a horizontal line.
     */
    @objid ("7b725a1e-f067-4622-b64e-bc31fac00faf")
    public static final String ADD_LINE = "addLine"; // $NON-NLS-1$

    /**
     * Adds a link.
     */
    @objid ("f1d507a1-4532-4320-99c9-406b601e25cc")
    public static final String ADD_LINK = "addLink"; // $NON-NLS-1$

    /**
     * Inserts text over the current selection.
     */
    @objid ("04612dba-f394-474b-a8f2-0aff4f2cdf7b")
    public static final String INSERT_TEXT = "insertText"; // $NON-NLS-1$

    /**
     * Adds an ordered list.
     */
    @objid ("452287c3-6e9e-43ac-9c5f-0c86e996762c")
    public static final String ADD_ORDERED_LIST = "addOrderedList"; // $NON-NLS-1$

    /**
     * Adds a table.
     */
    @objid ("8abd1c81-bc9c-413f-bd58-a5cf6e105e4f")
    public static final String ADD_TABLE = "addTable"; // $NON-NLS-1$

    /**
     * Adds an unordered list.
     */
    @objid ("8e24cbb4-7a25-46d8-aeab-875ba5b9f113")
    public static final String ADD_UNORDERED_LIST = "addUnorderedList"; // $NON-NLS-1$

    /**
     * Sets the background color of the selected text.
     */
    @objid ("be4dc8ea-3f78-4d7e-a3e9-7effb4e22511")
    public static final String BACKGROUND_COLOR = "backColor"; // $NON-NLS-1$

    /**
     * Toggles the 'bold' attribute of the selected text.
     */
    @objid ("168fd022-b0f8-436c-89b0-9f08f2b67ce6")
    public static final String BOLD = "bold"; // $NON-NLS-1$

    /**
     * Clears the rich text content.
     */
    @objid ("94fd35e2-c031-4aa4-a04b-aac2845fdb34")
    public static final String CLEAR_CONTENT = "clearContent"; // $NON-NLS-1$

    /**
     * Copies the selected text to the clipboard.
     */
    @objid ("71691db9-7d4b-434e-b6c0-7126c36efe86")
    public static final String COPY = "copy"; // $NON-NLS-1$

    /**
     * Cuts the selected text to the clipboard.
     */
    @objid ("cefb8c36-8a2c-40da-8e42-1327ab8be40f")
    public static final String CUT = "cut"; // $NON-NLS-1$

    /**
     * Deletes the selected text.
     */
    @objid ("69c77cda-57a3-4e0f-bd4b-d589f6bd7893")
    public static final String DELETE = "deleteText"; // $NON-NLS-1$

    /**
     * Finds text.
     */
    @objid ("5e650712-91e8-4879-b87b-7a08e9a027c2")
    public static final String FIND_TEXT = "findText"; // $NON-NLS-1$

    /**
     * Gets the HTML source.
     */
    @objid ("bc1727f6-6122-4c6d-86b3-87c8f10e790e")
    public static final String GET_TEXT = "getText"; // $NON-NLS-1$

    /**
     * Sets the foreground color of the selected text.
     */
    @objid ("8242d6fe-ea86-4108-b6a0-1c6c76e6522c")
    public static final String FORGROUND_COLOR = "foreColor"; // $NON-NLS-1$

    /**
     * Formats the selected text.
     */
    @objid ("39681900-a8d8-4fbd-8e92-aff2fc5f04d8")
    public static final String FORMAT_BLOCK = "formatBlock"; // $NON-NLS-1$

    /**
     * Returns the selected text.
     */
    @objid ("815fce98-1729-46f9-a1ad-2d08f19f2beb")
    public static final String GET_SELECTED_TEXT = "getSelectedText"; // $NON-NLS-1$

    /**
     * Indents the selected text.
     */
    @objid ("2e8c99f0-82d0-4308-a8fd-81e5b5d50740")
    public static final String INDENT = "indent"; // $NON-NLS-1$

    /**
     * Toggles the 'italic' attribute of the selected text.
     */
    @objid ("1b92a003-7c45-4147-9651-19cdd618c355")
    public static final String ITALIC = "italic"; // $NON-NLS-1$

    /**
     * Center justifies the selected text.
     */
    @objid ("25b5d632-2d6c-4141-a30f-14daaa526f46")
    public static final String JUSTIFY_CENTER = "justifyCenter"; // $NON-NLS-1$

    /**
     * Fully justifies the selected text.
     */
    @objid ("08c7c002-5d38-40ec-994a-95b629e6c548")
    public static final String JUSTIFY_FULL = "justifyFull"; // $NON-NLS-1$

    /**
     * Left justifies the selected text.
     */
    @objid ("c94c8d6e-ce7f-427b-9aa8-96bccbe76d4c")
    public static final String JUSTIFY_LEFT = "justifyLeft"; // $NON-NLS-1$

    /**
     * Right justifies the selected text.
     */
    @objid ("ad790b4d-bde9-4b54-bc9f-651f6ae80dba")
    public static final String JUSTIFY_RIGHT = "justifyRight"; // $NON-NLS-1$

    /**
     * Outdents the selected text.
     */
    @objid ("527517fb-ec9a-4715-ba97-635b4dcc51b5")
    public static final String OUTDENT = "outdent"; // $NON-NLS-1$

    /**
     * Pastes text from the clipboard.
     */
    @objid ("86a3b206-9723-4a79-adb0-ff6bd49ffe6d")
    public static final String PASTE = "paste"; // $NON-NLS-1$

    /**
     * Replaces all text.
     */
    @objid ("69fd1736-1002-4faa-a467-2b7ab2e7ba14")
    public static final String REPLACE_ALL_TEXT = "replaceAllText"; // $NON-NLS-1$

    /**
     * Replaces the selected text.
     */
    @objid ("1d8fc2db-8bcd-40ec-8203-81477af94651")
    public static final String REPLACE_TEXT = "replaceText"; // $NON-NLS-1$

    /**
     * Redoes the previous command.
     */
    @objid ("822215ae-8217-46f1-8cfc-709c7256b265")
    public static final String REDO = "redo"; // $NON-NLS-1$

    /**
     * Removes the current formatting of the selected text.
     */
    @objid ("36ae6fdd-9fe5-4798-aa32-e38e62ba5495")
    public static final String REMOVE_FORMAT = "removeFormat"; // $NON-NLS-1$

    /**
     * Saves the editor
     */
    @objid ("ff7c7536-0bf6-4a3f-9a3e-8be47ffda73b")
    public static final String SAVE = "save"; // $NON-NLS-1$

    /**
     * Saves all editors
     */
    @objid ("0b548e28-110d-45a8-b30f-f0bca00d86ef")
    public static final String SAVE_ALL = "saveAll"; // $NON-NLS-1$

    /**
     * Selects all text.
     */
    @objid ("267d75b8-884c-4736-8b6f-fc987ad3452e")
    public static final String SELECT_ALL = "selectAll"; // $NON-NLS-1$

    /**
     * Sets the font name for the selected text.
     */
    @objid ("618246a4-c365-4d7f-8bd5-e40c6cb260fb")
    public static final String SET_FONT_NAME = "setFontName"; // $NON-NLS-1$

    /**
     * Sets the font size for the selected text.
     */
    @objid ("f79d86f3-804f-4d00-8ae1-cce6b28c6fde")
    public static final String SET_FONT_SIZE = "setFontSize"; // $NON-NLS-1$

    /**
     * Sets the font style for the selected text.
     */
    @objid ("2871ce26-242d-45fe-a11e-ea42e2a3d235")
    public static final String SET_FONT_STYLE = "setFontStyle"; // $NON-NLS-1$

    /**
     * Sets whether the content can be edited.
     */
    @objid ("d570ca74-c2e3-4d08-ab99-83e08dc3e90d")
    public static final String SET_EDITABLE = "setEditable"; // $NON-NLS-1$

    /**
     * Sets focus to this control.
     */
    @objid ("c14eaf68-4a6f-4718-a56a-11635788ab8d")
    public static final String SET_FOCUS = "setFocus"; // $NON-NLS-1$

    /**
     * Sets the height of this control.
     */
    @objid ("7b3e1a32-fd24-45d7-bc44-149834116652")
    public static final String SET_HEIGHT = "setHeight"; // $NON-NLS-1$

    /**
     * Sets the HTML source.
     */
    @objid ("753b0674-7c26-46a8-9e00-96f7ae8951e5")
    public static final String SET_TEXT = "setText"; // $NON-NLS-1$

    /**
     * Toggles the 'strike-through' attribute of the selected text.
     */
    @objid ("35fcaf23-dc3c-43f1-9add-ed45977a6637")
    public static final String STRIKE_THROUGH = "strikeThrough"; // $NON-NLS-1$

    /**
     * Toggles the subscript attribute of the selected text.
     */
    @objid ("8903d2bd-642f-47ed-bea8-ef84d6e9071c")
    public static final String SUBSCRIPT = "subscript"; // $NON-NLS-1$

    /**
     * Toggles the superscript attribute of the selected text.
     */
    @objid ("ca7b7687-e528-45da-a086-103217f83427")
    public static final String SUPERSCRIPT = "superscript"; // $NON-NLS-1$

    /**
     * Toggles the underline attribute of the selected text.
     */
    @objid ("29a8a57b-ca00-4e4e-a912-22939b014da3")
    public static final String UNDERLINE = "underline"; // $NON-NLS-1$

    /**
     * Undoes the previous command.
     */
    @objid ("72b836db-1b1e-4b1d-9568-df4686da152a")
    public static final String UNDO = "undo"; // $NON-NLS-1$

    /**
     * Converts a link to normal text.
     */
    @objid ("27f609f1-2593-443f-9af3-26e4c6eeaf18")
    public static final String UNLINK = "unlink"; // $NON-NLS-1$

    /**
     * Add a row to the selected table.
     */
    @objid ("8adaf5dd-b5bb-4404-8ef7-36ebcf9faf5d")
    public static final String ADD_ROW = "addRow"; // $NON-NLS-1$

    /**
     * Add a column to the selected table.
     */
    @objid ("8639420c-f1d3-4bfe-89fa-b4357544c73a")
    public static final String ADD_COLUMN = "addColumn"; // $NON-NLS-1$

    /**
     * Delete the last row of the selected table.
     */
    @objid ("9464e6db-3f3e-4f37-a4fb-e35e08231495")
    public static final String DELETE_LAST_ROW = "deleteLastRow"; // $NON-NLS-1$

    /**
     * Delete the last column of the selected table.
     */
    @objid ("d19e2c5d-4969-4ada-a112-177032f7472f")
    public static final String DELETE_LAST_COLUMN = "deleteLastColumn"; // $NON-NLS-1$

}
