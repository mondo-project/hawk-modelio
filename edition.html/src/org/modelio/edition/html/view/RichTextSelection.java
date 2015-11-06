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
 * Models a text selection in a rich text control and editor.
 * 
 * @author Jeff Hardy
 * @since 1.2
 */
@objid ("35c00ebe-2d63-43f1-a2d4-d7b1a0d8a079")
public class RichTextSelection {
// The control's text selection.
    @objid ("b2434ead-7e32-4986-953b-0601ec2ec429")
    protected String text = ""; // $NON-NLS-1$

// The control's text selection offset
    @objid ("a7e7708f-f3cf-4f24-be28-9fef8ad6927d")
    protected int offsetStart = 0;

// The control's text selection font
    @objid ("de7f24fb-7ebf-4b0e-acca-eb86fdfa5857")
    protected String fontName = ""; // $NON-NLS-1$

    /**
     * The control's text selection font size
     * <p>
     * could be in point size (12pt), or HTML size (1-7)
     */
    @objid ("23ad56d0-d96b-4956-a816-3e3fdcf298e4")
    protected String fontSize = ""; // $NON-NLS-1$

// The control's text current style
    @objid ("8b35f90f-b0a8-4e06-b9d7-770dd265cd94")
    protected String blockStyle = ""; // $NON-NLS-1$

// The control's text current flags
    @objid ("7621bb03-d92d-42d3-83b4-02efd31d757e")
    protected int flags = 0;

// Text status flags
    /**
     * Text status flag
     */
    @objid ("4addaeff-4730-4feb-917e-08aa7e803515")
    public static final int BOLD = 1;

    /**
     * Text status flag
     */
    @objid ("07f80b8c-2790-4510-bf52-0bbdf75876b7")
    public static final int ITALIC = BOLD << 1;

    /**
     * Text status flag
     */
    @objid ("e4cb4461-34de-4f0f-a079-4f72daeeae52")
    public static final int UNDERLINE = ITALIC << 1;

    /**
     * Text status flag
     */
    @objid ("747bb8ca-7e08-48e8-a95a-3ebd4fea5fdf")
    public static final int SUBSCRIPT = UNDERLINE << 1;

    /**
     * Text status flag
     */
    @objid ("7d08a014-9b9a-4795-b759-0140374b3d50")
    public static final int SUPERSCRIPT = SUBSCRIPT << 1;

    /**
     * @return The control's text current style.
     */
    @objid ("9ee192da-a310-4128-a29e-adf331be8a8b")
    public String getBlockStyle() {
        return this.blockStyle;
    }

    /**
     * @param blockStyle The control's text current style.
     */
    @objid ("b9b3a17e-d066-46d2-b4d5-cb6b85d5b114")
    public void setBlockStyle(String blockStyle) {
        this.blockStyle = blockStyle;
    }

    /**
     * @return text The control's text current flags.
     */
    @objid ("fa404e45-0054-496d-9ad4-93bfa8e6e3af")
    public int getFlags() {
        return this.flags;
    }

    /**
     * @param flags The control's text current flags.
     */
    @objid ("8e209ad3-8cea-4de3-b075-6f772da75ac0")
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * @return The control's text selection font.
     */
    @objid ("78693297-20f8-4ee8-8b9f-c32bf5b5a595")
    public String getFontName() {
        return this.fontName;
    }

    /**
     * @param fontName The control's text selection font.
     */
    @objid ("5cba5f2f-da62-4755-8f69-862ee7041943")
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * @return The control's text selection font size.
     */
    @objid ("3110d75c-b05a-4cd2-9a14-3061c3172808")
    public String getFontSize() {
        return this.fontSize;
    }

    /**
     * @param fontSize The control's text selection font size.
     */
    @objid ("ec8f1d9a-41a6-4b50-9b4d-64f44b1525e1")
    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    /**
     * @return The control's text selection offset.
     */
    @objid ("09ddebd4-a463-4343-9f9b-b738cf68f231")
    public int getOffsetStart() {
        return this.offsetStart;
    }

    /**
     * @param offsetStart The control's text selection offset.
     */
    @objid ("fb6fbc62-ad7b-477b-9c01-f019186935f7")
    public void setOffsetStart(int offsetStart) {
        this.offsetStart = offsetStart;
    }

    /**
     * @return The control's text selection.
     */
    @objid ("1105f14c-ac4c-4ab8-ae1c-c63409a6a789")
    public String getText() {
        return this.text;
    }

    /**
     * @param text The control's text selection.
     */
    @objid ("545a08b7-fb43-4db2-a194-70db15d85721")
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return <i>true</i> if the selection is bold.
     */
    @objid ("c4f70d5f-98a9-4991-9390-19088df0f024")
    public boolean isBold() {
        return (this.flags & BOLD) != 0;
    }

    /**
     * @return <i>true</i> if the selection is italic.
     */
    @objid ("37601cbb-a227-40c8-9fcd-3393f6dc1eb2")
    public boolean isItalic() {
        return (this.flags & ITALIC) != 0;
    }

    /**
     * @return <i>true</i> if the selection is underlined.
     */
    @objid ("0bfe0aa8-e22f-4ef3-bb98-beafb1f6faef")
    public boolean isUnderLine() {
        return (this.flags & UNDERLINE) != 0;
    }

    /**
     * @return <i>true</i> if the selection is subscript.
     */
    @objid ("1a4bc286-2a08-43ff-b41d-ac37429b67a6")
    public boolean isSubscript() {
        return (this.flags & SUBSCRIPT) != 0;
    }

    /**
     * @return <i>true</i> if the selection is superscript.
     */
    @objid ("8692db67-3f42-4828-9d27-ff505233ce64")
    public boolean isSuperscript() {
        return (this.flags & SUPERSCRIPT) != 0;
    }

    /**
     * Clears the selection info
     */
    @objid ("3268156e-9fd0-467e-ac4a-9ea581d40954")
    public void clear() {
        this.text = ""; //$NON-NLS-1$
        this.offsetStart = 0;
        this.fontName = ""; //$NON-NLS-1$
        this.fontSize = ""; //$NON-NLS-1$
        this.blockStyle = ""; //$NON-NLS-1$
        this.flags = 0;
    }

    @objid ("18506e57-59c5-441a-bdb7-21754e16f132")
    @Override
    public String toString() {
        String str = "Text: " + this.text + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        str += "fontName: " + this.fontName + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        str += "fontSize: " + this.fontSize + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        str += "blockStyle: " + this.blockStyle + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        str += "flags: " + this.flags + "\n"; //$NON-NLS-1$ //$NON-NLS-2$
        return str;
    }

}
