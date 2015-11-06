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
package org.modelio.edition.html.plugin;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Shared images used by the default rich text editor.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("d6caf661-5ca5-44a3-ba04-646c9b1156fa")
@SuppressWarnings("javadoc")
public class HtmlTextImages {
    @objid ("c621c494-4560-4bc9-b9af-73e2d7962047")
    private static final String ETOOL16 = "full/etool16/"; // $NON-NLS-1$

    @objid ("8b54bf4f-26c6-469d-8273-daf7c2bc6f43")
    private static final String IMG_PATH_DELETE_COLUMN = ETOOL16 + "deleteColumn.gif";

    @objid ("96fa5bb5-2956-4f95-a4e6-1fcc03bb7ee3")
    private static final String IMG_PATH_DELETE_ROW = ETOOL16 + "deleteRow.gif";

    @objid ("b6388e89-fda8-44b4-9a1e-9cd749f65e4b")
    private static final String IMG_PATH_ADD_COLUMN = ETOOL16 + "addColumn.gif";

    @objid ("894af1e4-7220-4d10-b289-e98dd87addb6")
    private static final String IMG_PATH_ADD_ROW = ETOOL16 + "addRow.gif";

    @objid ("45c6da3c-bc2b-4fa8-b872-7573180b40df")
    private static final String DTOOL16 = "full/dtool16/"; // $NON-NLS-1$

    @objid ("9dd64949-e76e-40c5-8d68-24ee2b0e1355")
    private static final String DISABLED_IMG_PATH_DELETE_COLUMN = DTOOL16 + "deleteColumn.gif";

    @objid ("65c97702-1242-412b-b45a-936fe5498f6e")
    private static final String DISABLED_IMG_PATH_DELETE_ROW = DTOOL16 + "deleteRow.gif";

    @objid ("4020497a-4a48-483a-96b7-89c28200a60c")
    private static final String DISABLED_IMG_PATH_ADD_COLUMN = DTOOL16 + "addColumn.gif";

    @objid ("97425e9e-f0cd-4c60-88ce-efe92cc43027")
    private static final String DISABLED_IMG_PATH_ADD_ROW = DTOOL16 + "addRow.gif";

    @objid ("70626776-526f-47a7-a964-abb9f0a454a0")
    public static final String IMG_PATH_ADD_IMAGE = ETOOL16 + "AddImage.gif"; // $NON-NLS-1$

    @objid ("322ab08b-f6e7-4bf0-911e-417989c8781c")
    public static final String IMG_PATH_ADD_LINE = ETOOL16 + "AddLine.gif"; // $NON-NLS-1$

    @objid ("cbb6ffe4-7018-4348-a26f-d6d3c19bd78f")
    public static final String IMG_PATH_ADD_LINK = ETOOL16 + "AddLink.gif"; // $NON-NLS-1$

    @objid ("a5bcbdcd-7b61-4a24-987b-6d9ff0d59733")
    public static final String IMG_PATH_ADD_ORDERED_LIST = ETOOL16
			+ "AddOrderedList.gif"; // $NON-NLS-1$

    @objid ("27955065-e33f-4058-994d-f6776986bfc6")
    public static final String IMG_PATH_ADD_TABLE = ETOOL16 + "AddTable.gif"; // $NON-NLS-1$

    @objid ("2973adb0-e7dc-4b06-ac40-0ef3dd97e90e")
    public static final String IMG_PATH_ADD_UNORDERED_LIST = ETOOL16
			+ "AddUnorderedList.gif"; // $NON-NLS-1$

    @objid ("4cfd5dd2-c53c-43fb-80cf-96d46f63cea8")
    public static final String IMG_PATH_BOLD = ETOOL16 + "Bold.gif"; // $NON-NLS-1$

    @objid ("e608d30f-67fc-4815-bbe0-5b79838119e7")
    public static final String IMG_PATH_CLEAR_CONTENT = ETOOL16
			+ "ClearContent.gif"; // $NON-NLS-1$

    @objid ("aaeee223-51b5-45f5-bb1e-3e130cf5ec18")
    public static final String IMG_PATH_COPY = ETOOL16 + "Copy.gif"; // $NON-NLS-1$

    @objid ("0751488c-9f90-46a7-9457-c492f7a7e044")
    public static final String IMG_PATH_CUT = ETOOL16 + "Cut.gif"; // $NON-NLS-1$

    @objid ("a3a47641-7d94-4e72-894d-aa868e314ddf")
    public static final String IMG_PATH_FIND_REPLACE = ETOOL16
			+ "FindReplace.gif"; // $NON-NLS-1$

    @objid ("e12a52dc-fc15-474f-9316-60f1a6067c6a")
    public static final String IMG_PATH_INDENT = ETOOL16 + "Indent.gif"; // $NON-NLS-1$

    @objid ("260f7805-ed75-4463-89a8-5da887fb278d")
    public static final String IMG_PATH_ITALIC = ETOOL16 + "Italic.gif"; // $NON-NLS-1$

    @objid ("f6102df8-1d70-4649-b42c-bd9a80217961")
    public static final String IMG_PATH_JUSTIFY_CENTER = ETOOL16
			+ "JustifyCenter.gif"; // $NON-NLS-1$

    @objid ("e081b066-d992-481e-b77c-e7188c007a65")
    public static final String IMG_PATH_JUSTIFY_FULL = ETOOL16
			+ "JustifyFull.gif"; // $NON-NLS-1$

    @objid ("835ebab1-a97c-4ccf-ba78-99faa70730a8")
    public static final String IMG_PATH_JUSTIFY_LEFT = ETOOL16
			+ "JustifyLeft.gif"; // $NON-NLS-1$

    @objid ("0a42f9e2-ba68-4a0f-a6ff-4a169b391ef9")
    public static final String IMG_PATH_JUSTIFY_RIGHT = ETOOL16
			+ "JustifyRight.gif"; // $NON-NLS-1$

    @objid ("b5b81132-6e29-43a0-82cd-246a0592b18d")
    public static final String IMG_PATH_OUTDENT = ETOOL16 + "Outdent.gif"; // $NON-NLS-1$

    @objid ("c1acac41-8c0f-4f59-9055-766ceed70ef9")
    public static final String IMG_PATH_PASTE = ETOOL16 + "Paste.gif"; // $NON-NLS-1$

    @objid ("11fbd9b4-dbeb-4560-a32f-344df1c73727")
    public static final String IMG_PATH_PASTE_PLAIN_TEXT = ETOOL16 + "PastePlainText.gif"; // $NON-NLS-1$

    @objid ("4e937609-7c7b-4376-864b-5de6c4870a10")
    public static final String IMG_PATH_STRIKE_THROUGH = ETOOL16
			+ "StrikeThrough.gif"; // $NON-NLS-1$

    @objid ("9f0c796a-f597-44d5-87ee-b0ae1216c806")
    public static final String IMG_PATH_SUBSCRIPT = ETOOL16 + "Subscript.gif"; // $NON-NLS-1$

    @objid ("b54c15c6-e88d-4f64-a2e1-f7bb0dc26fec")
    public static final String IMG_PATH_SUPERSCRIPT = ETOOL16
			+ "Superscript.gif"; // $NON-NLS-1$

    @objid ("e24c94b5-c96e-479f-a87e-97c47eeb3dae")
    public static final String IMG_PATH_TEXTCOLOR = ETOOL16 + "TextColor.gif"; // $NON-NLS-1$

    @objid ("44ae042f-592b-45e0-a061-fb0823fbf069")
    public static final String IMG_PATH_TEXTHIGHLIGHT = ETOOL16 + "TextHighlight.gif"; // $NON-NLS-1$

    @objid ("4ef90416-b259-494d-a64c-19a9d9f8e174")
    public static final String IMG_PATH_UNDERLINE = ETOOL16 + "Underline.gif"; // $NON-NLS-1$

    @objid ("59966975-2c02-40f9-ac65-898f62ee53b4")
    public static final String IMG_PATH_TIDY = ETOOL16 + "TidyHTML.gif"; // $NON-NLS-1$

    @objid ("7ad97fe4-4493-42fe-8edb-883d52930410")
    public static final String IMG_PATH_ADD_CODE = ETOOL16 + "AddCode.gif"; // $NON-NLS-1$

    @objid ("d7a2d599-3d33-4385-9040-bfbfe9591d08")
    public static final String DISABLED_IMG_PATH_ADD_IMAGE = DTOOL16
			+ "AddImage.gif"; // $NON-NLS-1$

//    public static final String DISABLED_IMG_PATH_ADD_LINE = DTOOL16
//            + "AddLine.gif"; //$NON-NLS-1$
    @objid ("81943fb6-48d6-4531-940b-181c17f020e7")
    public static final String DISABLED_IMG_PATH_ADD_LINK = DTOOL16
			+ "AddLink.gif"; // $NON-NLS-1$

    @objid ("9e84d377-6ef1-46ad-a8ae-205f13cda00d")
    public static final String DISABLED_IMG_PATH_ADD_ORDERED_LIST = DTOOL16
			+ "AddOrderedList.gif"; // $NON-NLS-1$

    @objid ("04d20441-4d08-4a18-aa30-b68c8630fb06")
    public static final String DISABLED_IMG_PATH_ADD_TABLE = DTOOL16
			+ "AddTable.gif"; // $NON-NLS-1$

    @objid ("19d25f02-f3bf-4ee3-9df5-aa9f2dd2b2e8")
    public static final String DISABLED_IMG_PATH_ADD_UNORDERED_LIST = DTOOL16
			+ "AddUnorderedList.gif"; // $NON-NLS-1$

    @objid ("68ace8e5-4f7b-4aa1-b05b-0f11a8cd290b")
    public static final String DISABLED_IMG_PATH_BOLD = DTOOL16 + "Bold.gif"; // $NON-NLS-1$

    @objid ("96346c71-bc0f-4fad-ab39-9c5a77c0818b")
    public static final String DISABLED_IMG_PATH_CLEAR_CONTENT = DTOOL16
			+ "ClearContent.gif"; // $NON-NLS-1$

    @objid ("d94a4f40-5311-493b-9126-2a67366a05a5")
    public static final String DISABLED_IMG_PATH_COPY = DTOOL16 + "Copy.gif"; // $NON-NLS-1$

    @objid ("64b40af1-ed86-4243-803a-52abcf0d2e8a")
    public static final String DISABLED_IMG_PATH_CUT = DTOOL16 + "Cut.gif"; // $NON-NLS-1$

    @objid ("8c98d647-6064-4ab4-b1ed-f9b77e234aa3")
    public static final String DISABLED_IMG_PATH_FIND_REPLACE = DTOOL16
			+ "FindReplace.gif"; // $NON-NLS-1$

    @objid ("484591f4-02b9-41b3-89ff-34f2fbef6d91")
    public static final String DISABLED_IMG_PATH_INDENT = DTOOL16
			+ "Indent.gif"; // $NON-NLS-1$

    @objid ("e5eab9b9-3f44-45e8-8a4d-0619e06dfaad")
    public static final String DISABLED_IMG_PATH_ITALIC = DTOOL16
			+ "Italic.gif"; // $NON-NLS-1$

//    public static final String DISABLED_IMG_PATH_JUSTIFY_CENTER = DTOOL16
//            + "JustifyCenter.gif"; //$NON-NLS-1$
//
//    public static final String DISABLED_IMG_PATH_JUSTIFY_FULL = DTOOL16
//            + "JustifyFull.gif"; //$NON-NLS-1$
//
//    public static final String DISABLED_IMG_PATH_JUSTIFY_LEFT = DTOOL16
//            + "JustifyLeft.gif"; //$NON-NLS-1$
//
//    public static final String DISABLED_IMG_PATH_JUSTIFY_RIGHT = DTOOL16
//            + "JustifyRight.gif"; //$NON-NLS-1$
    @objid ("d1a9a811-6e59-4b9f-8677-9e39981a06fd")
    public static final String DISABLED_IMG_PATH_OUTDENT = DTOOL16
			+ "Outdent.gif"; // $NON-NLS-1$

    @objid ("e587797b-2bf7-4a1f-aa3d-e97638499484")
    public static final String DISABLED_IMG_PATH_PASTE = DTOOL16 + "Paste.gif"; // $NON-NLS-1$

    @objid ("2ec6823c-fe04-4005-8926-5dbab2cf8f71")
    public static final String DISABLED_IMG_PATH_PASTE_PLAIN_TEXT = DTOOL16 + "PastePlainText.gif"; // $NON-NLS-1$

//    public static final String DISABLED_IMG_PATH_STRIKE_THROUGH = DTOOL16
//            + "StrikeThrough.gif"; //$NON-NLS-1$
    @objid ("32ce79f1-7d47-4e9a-b377-31a5f7c5a1d0")
    public static final String DISABLED_IMG_PATH_SUBSCRIPT = DTOOL16
			+ "Subscript.gif"; // $NON-NLS-1$

    @objid ("2869e47e-e8ae-4253-a911-ff3265d9bee0")
    public static final String DISABLED_IMG_PATH_SUPERSCRIPT = DTOOL16
			+ "Superscript.gif"; // $NON-NLS-1$

    @objid ("8772f753-2d35-409a-bf2c-c10969451ea8")
    private static String DISABLED_IMG_PATH_TIDY = DTOOL16 + "TidyHTML.gif"; // $NON-NLS-1$

    @objid ("674bf0f2-87a1-4918-a0ce-8fc92b0f40d7")
    public static final String DISABLED_IMG_PATH_UNDERLINE = DTOOL16
			+ "Underline.gif"; // $NON-NLS-1$

    @objid ("09a1ba4a-8679-4359-b294-44a72cd1ebf0")
    public static final String DISABLED_IMG_PATH_ADD_CODE = DTOOL16
			+ "AddCode.gif"; // $NON-NLS-1$

    @objid ("dada5853-a814-4d21-a984-3e1ca59822f0")
    private static final HtmlTextPlugin richTextPlugin = HtmlTextPlugin
			.getDefault();

    @objid ("fb09b103-18a0-4a71-a3c6-332588abea30")
    public static final Image IMG_ADD_IMAGE = richTextPlugin
			.getSharedImage(IMG_PATH_ADD_IMAGE);

    @objid ("d3900135-f212-400f-bb13-5adb2b9f7bf4")
    public static final Image IMG_ADD_LINE = richTextPlugin
			.getSharedImage(IMG_PATH_ADD_LINE);

    @objid ("36d2b80b-cff7-49d1-9dfe-39ffb514cc02")
    public static final Image IMG_ADD_LINK = richTextPlugin
			.getSharedImage(IMG_PATH_ADD_LINK);

    @objid ("d6e1f4bb-e6d8-42f8-9f52-67c4af6d6aa9")
    public static final Image IMG_ADD_ORDERED_LIST = richTextPlugin
			.getSharedImage(IMG_PATH_ADD_ORDERED_LIST);

    @objid ("5c6c91da-a621-4e42-af03-746d0f4d55cc")
    public static final Image IMG_ADD_TABLE = richTextPlugin
			.getSharedImage(IMG_PATH_ADD_TABLE);

    @objid ("c4701e0c-b2a7-46ba-a572-515ca37db791")
    public static final Image IMG_ADD_UNORDERED_LIST = richTextPlugin
			.getSharedImage(IMG_PATH_ADD_UNORDERED_LIST);

    @objid ("1a3c7eb9-344b-4825-8d4a-b25562a1c604")
    public static final Image IMG_BOLD = richTextPlugin
			.getSharedImage(IMG_PATH_BOLD);

    @objid ("7a154f87-c764-4bef-9638-0ca7334ec904")
    public static final Image IMG_CLEAR_CONTENT = richTextPlugin
			.getSharedImage(IMG_PATH_CLEAR_CONTENT);

    @objid ("4dff7d43-d3f5-4d84-903b-4f9091fcd470")
    public static final Image IMG_COPY = richTextPlugin
			.getSharedImage(IMG_PATH_COPY);

    @objid ("f7d40d45-95cb-4f57-8805-5e8fa7ff04ba")
    public static final Image IMG_CUT = richTextPlugin
			.getSharedImage(IMG_PATH_CUT);

    @objid ("a7a4eef1-b35a-455b-b575-11abd678efc5")
    public static final Image IMG_FIND_REPLACE = richTextPlugin
			.getSharedImage(IMG_PATH_FIND_REPLACE);

    @objid ("d04fa944-f413-43c8-8802-81cbd7e3a555")
    public static final Image IMG_INDENT = richTextPlugin
			.getSharedImage(IMG_PATH_INDENT);

    @objid ("9456d8a7-768a-42b8-b00e-2fdf7b5cd119")
    public static final Image IMG_ITALIC = richTextPlugin
			.getSharedImage(IMG_PATH_ITALIC);

    @objid ("79af15bd-7654-4942-9d6d-50dfc372bf4b")
    public static final Image IMG_JUSTIFY_CENTER = richTextPlugin
			.getSharedImage(IMG_PATH_JUSTIFY_CENTER);

    @objid ("69ad5046-6bbd-47fd-91f5-7c92ee13e87b")
    public static final Image IMG_JUSTIFY_FULL = richTextPlugin
			.getSharedImage(IMG_PATH_JUSTIFY_FULL);

    @objid ("c5f6a0e5-7499-482b-8f64-07908a60644e")
    public static final Image IMG_JUSTIFY_LEFT = richTextPlugin
			.getSharedImage(IMG_PATH_JUSTIFY_LEFT);

    @objid ("cef9486f-eba4-4726-9536-b79a62f77976")
    public static final Image IMG_JUSTIFY_RIGHT = richTextPlugin
			.getSharedImage(IMG_PATH_JUSTIFY_RIGHT);

    @objid ("b1e62cc2-8606-4c0e-aba8-b336a9b2226a")
    public static final Image IMG_OUTDENT = richTextPlugin
			.getSharedImage(IMG_PATH_OUTDENT);

    @objid ("5e620920-d539-4b26-9411-337adfaea035")
    public static final Image IMG_PASTE = richTextPlugin
			.getSharedImage(IMG_PATH_PASTE);

    @objid ("353f7345-6a0b-4701-93e6-dda2476e20de")
    public static final Image IMG_PASTE_PLAIN_TEXT = richTextPlugin
			.getSharedImage(IMG_PATH_PASTE_PLAIN_TEXT);

    @objid ("ffe47419-2aa2-4f51-87e0-c0a9f0dbe3de")
    public static final Image IMG_STRIKE_THROUGH = richTextPlugin
			.getSharedImage(IMG_PATH_STRIKE_THROUGH);

    @objid ("c306e7ec-2f7d-46cd-937c-b2d0aa226381")
    public static final Image IMG_SUBSCRIPT = richTextPlugin
			.getSharedImage(IMG_PATH_SUBSCRIPT);

    @objid ("d4c40e68-6d06-42e1-b991-a3dfc9308860")
    public static final Image IMG_SUPERSCRIPT = richTextPlugin
			.getSharedImage(IMG_PATH_SUPERSCRIPT);

    @objid ("28066ad8-1b6e-4c76-992e-785a5cee6a4e")
    public static final Image IMG_TEXTHIGHLIGHT = richTextPlugin
            .getSharedImage(IMG_PATH_TEXTHIGHLIGHT);

    @objid ("b0d74e1e-f88b-4d63-a078-bb40bf700e40")
    public static final Image IMG_UNDERLINE = richTextPlugin
			.getSharedImage(IMG_PATH_UNDERLINE);

    @objid ("0361c2fe-423a-4758-8c78-43ec2168ff1b")
    public static final Image IMG_TIDY = richTextPlugin
			.getSharedImage(IMG_PATH_TIDY);

    @objid ("7b63f215-750f-4c96-8ed5-2e03f37f7056")
    public static final ImageDescriptor IMG_DESC_ADD_IMAGE = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_IMAGE);

    @objid ("769252f2-2f08-452f-9cb1-57752bef8d10")
    public static final ImageDescriptor IMG_DESC_ADD_LINE = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_LINE);

    @objid ("62b800a2-657d-45e2-b04e-b9cc359f5a4b")
    public static final ImageDescriptor IMG_DESC_ADD_ROW = richTextPlugin
            .getImageDescriptor(IMG_PATH_ADD_ROW);

    @objid ("4a67fbd6-5b13-4628-9b88-55713ede37e2")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_ROW = richTextPlugin
    .getImageDescriptor(DISABLED_IMG_PATH_ADD_ROW);

    @objid ("d407b7c7-2691-41fc-92a9-f4844dab363c")
    public static final ImageDescriptor IMG_DESC_ADD_COLUMN = richTextPlugin
            .getImageDescriptor(IMG_PATH_ADD_COLUMN);

    @objid ("5d8b486e-6fdc-4193-aab1-984f0f1e742d")
    public static final ImageDescriptor IMG_DESC_ADD_LINK = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_LINK);

    @objid ("90ee09b0-cec4-4bbf-a0af-34bfc5375c4d")
    public static final ImageDescriptor IMG_DESC_ADD_ORDERED_LIST = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_ORDERED_LIST);

    @objid ("c4950a14-c21b-4ac4-9381-c267b4835b97")
    public static final ImageDescriptor IMG_DESC_ADD_TABLE = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_TABLE);

    @objid ("910cf65f-2d5c-4a22-a2c3-a7d76f2d10c6")
    public static final ImageDescriptor IMG_DESC_ADD_UNORDERED_LIST = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_UNORDERED_LIST);

    @objid ("1fa9c567-83bc-48fc-b2c6-aa20a151f36f")
    public static final ImageDescriptor IMG_DESC_BOLD = richTextPlugin
			.getImageDescriptor(IMG_PATH_BOLD);

    @objid ("aaf1e3fb-738d-4d73-ba04-c36468763814")
    public static final ImageDescriptor IMG_DESC_CLEAR_CONTENT = richTextPlugin
			.getImageDescriptor(IMG_PATH_CLEAR_CONTENT);

    @objid ("4852d005-bf75-4416-bbd4-b22614097251")
    public static final ImageDescriptor IMG_DESC_COPY = richTextPlugin
			.getImageDescriptor(IMG_PATH_COPY);

    @objid ("abf62c15-c93e-4ea7-b860-223b96d8d113")
    public static final ImageDescriptor IMG_DESC_CUT = richTextPlugin
			.getImageDescriptor(IMG_PATH_CUT);

    @objid ("ba955aa8-b1d9-4ab7-98cb-900c8df97fcd")
    public static final ImageDescriptor IMG_DESC_DELETE_ROW = richTextPlugin
            .getImageDescriptor(IMG_PATH_DELETE_ROW);

    @objid ("a339ba6e-d5fd-49ad-95da-4d8e006ea536")
    public static final ImageDescriptor IMG_DESC_DELETE_COLUMN = richTextPlugin
            .getImageDescriptor(IMG_PATH_DELETE_COLUMN);

    @objid ("6b5b736d-dd1b-45e7-a115-1e193d330666")
    public static final ImageDescriptor IMG_DESC_FIND_REPLACE = richTextPlugin
			.getImageDescriptor(IMG_PATH_FIND_REPLACE);

    @objid ("0c3f4622-899c-4eb3-903c-81e13212eae0")
    public static final ImageDescriptor IMG_DESC_INDENT = richTextPlugin
			.getImageDescriptor(IMG_PATH_INDENT);

    @objid ("995ddd85-3224-4b9e-a12c-9eebadb104e6")
    public static final ImageDescriptor IMG_DESC_ITALIC = richTextPlugin
			.getImageDescriptor(IMG_PATH_ITALIC);

    @objid ("f5e1de3c-50b4-4e4d-8b0d-79935ee70fa9")
    public static final ImageDescriptor IMG_DESC_JUSTIFY_CENTER = richTextPlugin
			.getImageDescriptor(IMG_PATH_JUSTIFY_CENTER);

    @objid ("0f2cfe1a-2d09-47e6-b3dd-65a3730b385d")
    public static final ImageDescriptor IMG_DESC_JUSTIFY_FULL = richTextPlugin
			.getImageDescriptor(IMG_PATH_JUSTIFY_FULL);

    @objid ("662757a3-8679-4f63-9f74-ee068ab5dbb3")
    public static final ImageDescriptor IMG_DESC_JUSTIFY_LEFT = richTextPlugin
			.getImageDescriptor(IMG_PATH_JUSTIFY_LEFT);

    @objid ("93fad00c-2450-429d-9833-8d6f02202689")
    public static final ImageDescriptor IMG_DESC_JUSTIFY_RIGHT = richTextPlugin
			.getImageDescriptor(IMG_PATH_JUSTIFY_RIGHT);

    @objid ("6ad4e5d0-54cb-4073-8619-26c4d87591b1")
    public static final ImageDescriptor IMG_DESC_OUTDENT = richTextPlugin
			.getImageDescriptor(IMG_PATH_OUTDENT);

    @objid ("b1d32da7-0aaa-4b6c-a38b-1c8c74fe66a4")
    public static final ImageDescriptor IMG_DESC_PASTE = richTextPlugin
			.getImageDescriptor(IMG_PATH_PASTE);

    @objid ("2a8546d8-401b-4c4a-8426-3b6cf3b6390f")
    public static final ImageDescriptor IMG_DESC_PASTE_PLAIN_TEXT = richTextPlugin
			.getImageDescriptor(IMG_PATH_PASTE_PLAIN_TEXT);

    @objid ("39f4da26-1935-4988-9c58-f6b3c2a821ea")
    public static final ImageDescriptor IMG_DESC_STRIKE_THROUGH = richTextPlugin
			.getImageDescriptor(IMG_PATH_STRIKE_THROUGH);

    @objid ("bb47bd98-e714-4425-8cf9-7798aa9c395b")
    public static final ImageDescriptor IMG_DESC_SUBSCRIPT = richTextPlugin
			.getImageDescriptor(IMG_PATH_SUBSCRIPT);

    @objid ("24d8cb71-d81d-4bd8-a262-79e106469c7e")
    public static final ImageDescriptor IMG_DESC_SUPERSCRIPT = richTextPlugin
			.getImageDescriptor(IMG_PATH_SUPERSCRIPT);

    @objid ("0c8866da-a121-4198-9036-1b90df625e4b")
    public static final ImageDescriptor IMG_DESC_TEXTCOLOR = richTextPlugin
            .getImageDescriptor(IMG_PATH_TEXTCOLOR);

    @objid ("ba308287-822f-4249-9f79-6ce891396115")
    public static final ImageDescriptor IMG_DESC_TEXTHIGHLIGHT = richTextPlugin
            .getImageDescriptor(IMG_PATH_TEXTHIGHLIGHT);

    @objid ("7627878f-1820-4328-b1b3-96b925cb107d")
    public static final ImageDescriptor IMG_DESC_UNDERLINE = richTextPlugin
			.getImageDescriptor(IMG_PATH_UNDERLINE);

    @objid ("6157c787-724b-4726-b8c5-7c643cd9a251")
    public static final ImageDescriptor IMG_DESC_TIDY = richTextPlugin
			.getImageDescriptor(IMG_PATH_TIDY);

    @objid ("17192c2d-b036-44f6-aec2-60e95afc6866")
    public static final ImageDescriptor IMG_DESC_ADD_CODE = richTextPlugin
			.getImageDescriptor(IMG_PATH_ADD_CODE);

    @objid ("21c8e660-4e97-450e-a920-f237c13122fc")
    public static final Image DISABLED_IMG_ADD_IMAGE = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_ADD_IMAGE);

//    public static final Image DISABLED_IMG_ADD_LINE = richTextPlugin
//            .getSharedImage(DISABLED_IMG_PATH_ADD_LINE);
    @objid ("9de2e354-480e-4d56-bd59-0b4751972200")
    public static final Image DISABLED_IMG_ADD_LINK = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_ADD_LINK);

    @objid ("ce41091a-f33b-4709-b304-9e2baded884c")
    public static final Image DISABLED_IMG_ADD_ORDERED_LIST = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_ADD_ORDERED_LIST);

    @objid ("45ca50bf-65b7-4125-bfd1-0f37ee2d7d1c")
    public static final Image DISABLED_IMG_ADD_TABLE = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_ADD_TABLE);

    @objid ("3babbc1a-2bed-4383-a8ba-788c36e097e7")
    public static final Image DISABLED_IMG_ADD_UNORDERED_LIST = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_ADD_UNORDERED_LIST);

    @objid ("c1cf8c42-4223-41c5-8608-b191843b5159")
    public static final Image DISABLED_IMG_BOLD = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_BOLD);

    @objid ("2954c698-372e-472a-bcbb-f427f922686e")
    public static final Image DISABLED_IMG_CLEAR_CONTENT = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_CLEAR_CONTENT);

    @objid ("5fc8fc61-b0e7-4cea-bfb5-1ae1d3e7a59e")
    public static final Image DISABLED_IMG_COPY = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_COPY);

    @objid ("fccabd3f-c26a-4502-95d7-4d4eda9966d7")
    public static final Image DISABLED_IMG_CUT = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_CUT);

    @objid ("b1fbfe94-771f-4b2a-8195-7da3edd966ad")
    public static final Image DISABLED_IMG_FIND_REPLACE = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_FIND_REPLACE);

    @objid ("6d40ca62-ca89-4648-a9ce-9eade61e2799")
    public static final Image DISABLED_IMG_INDENT = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_INDENT);

    @objid ("35b93510-dbd3-4082-bcbf-010d0c182f46")
    public static final Image DISABLED_IMG_ITALIC = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_ITALIC);

//    public static final Image DISABLED_IMG_JUSTIFY_CENTER = richTextPlugin
//            .getSharedImage(DISABLED_IMG_PATH_JUSTIFY_CENTER);
//
//    public static final Image DISABLED_IMG_JUSTIFY_FULL = richTextPlugin
//            .getSharedImage(DISABLED_IMG_PATH_JUSTIFY_FULL);
//
//    public static final Image DISABLED_IMG_JUSTIFY_LEFT = richTextPlugin
//            .getSharedImage(DISABLED_IMG_PATH_JUSTIFY_LEFT);
//
//    public static final Image DISABLED_IMG_JUSTIFY_RIGHT = richTextPlugin
//            .getSharedImage(DISABLED_IMG_PATH_JUSTIFY_RIGHT);
    @objid ("ede0d49e-a83e-4a38-b272-2eb3f92b2f93")
    public static final Image DISABLED_IMG_OUTDENT = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_OUTDENT);

    @objid ("f68da464-b271-4a29-962f-f0f0a912fd18")
    public static final Image DISABLED_IMG_PASTE = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_PASTE);

//    public static final Image DISABLED_IMG_STRIKE_THROUGH = richTextPlugin
//            .getSharedImage(DISABLED_IMG_PATH_STRIKE_THROUGH);
    @objid ("1e3e498e-231a-488d-937f-1c7a2c55bb4d")
    public static final Image DISABLED_IMG_SUBSCRIPT = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_SUBSCRIPT);

    @objid ("df15eaea-05da-4120-9e0b-d87d12ffa1e4")
    public static final Image DISABLED_IMG_SUPERSCRIPT = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_SUPERSCRIPT);

    @objid ("f9cbc023-80c4-4235-a3e5-6f09ffff0bf2")
    public static final Image DISABLED_IMG_UNDERLINE = richTextPlugin
			.getSharedImage(DISABLED_IMG_PATH_UNDERLINE);

    @objid ("38aca95f-899a-4ccf-92e1-b08c42071553")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_IMAGE = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ADD_IMAGE);

//    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_LINE = richTextPlugin
//            .getImageDescriptor(DISABLED_IMG_PATH_ADD_LINE);
    @objid ("535ff465-6392-40e5-bc0c-659efa7cfa1c")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_LINK = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ADD_LINK);

    @objid ("dadf9ab1-d384-4d97-b72c-3fbdd80afc53")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_ORDERED_LIST = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ADD_ORDERED_LIST);

    @objid ("f90a9e61-bd2f-40aa-a585-f73aeb698b27")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_TABLE = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ADD_TABLE);

    @objid ("3de50114-d7a8-4902-b1d2-62f6e770a4c8")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_UNORDERED_LIST = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ADD_UNORDERED_LIST);

    @objid ("9d7b8ccd-1540-4173-87a8-787d3197d559")
    public static final ImageDescriptor DISABLED_IMG_DESC_BOLD = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_BOLD);

    @objid ("c66a2e71-716d-40ba-979e-bd0492904d67")
    public static final ImageDescriptor DISABLED_IMG_DESC_CLEAR_CONTENT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_CLEAR_CONTENT);

    @objid ("94e10aae-1f99-4d56-b068-213135c1c586")
    public static final ImageDescriptor DISABLED_IMG_DESC_COPY = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_COPY);

    @objid ("40cc3acc-7aeb-4515-aec9-ba3cbf9e7480")
    public static final ImageDescriptor DISABLED_IMG_DESC_CUT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_CUT);

    @objid ("85d94d12-0554-4278-aebc-39587d2b109d")
    public static final ImageDescriptor DISABLED_IMG_DESC_FIND_REPLACE = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_FIND_REPLACE);

    @objid ("d0c2ecf0-c9a6-46f3-a5eb-2525cff32c0f")
    public static final ImageDescriptor DISABLED_IMG_DESC_INDENT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_INDENT);

    @objid ("1df71239-1aee-4d77-aade-cfc8b64232dc")
    public static final ImageDescriptor DISABLED_IMG_DESC_ITALIC = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ITALIC);

//    public static final ImageDescriptor DISABLED_IMG_DESC_JUSTIFY_CENTER = richTextPlugin
//            .getImageDescriptor(DISABLED_IMG_PATH_JUSTIFY_CENTER);
//
//    public static final ImageDescriptor DISABLED_IMG_DESC_JUSTIFY_FULL = richTextPlugin
//            .getImageDescriptor(DISABLED_IMG_PATH_JUSTIFY_FULL);
//
//    public static final ImageDescriptor DISABLED_IMG_DESC_JUSTIFY_LEFT = richTextPlugin
//            .getImageDescriptor(DISABLED_IMG_PATH_JUSTIFY_LEFT);
//
//    public static final ImageDescriptor DISABLED_IMG_DESC_JUSTIFY_RIGHT = richTextPlugin
//            .getImageDescriptor(DISABLED_IMG_PATH_JUSTIFY_RIGHT);
    @objid ("c24c7d1f-c6b2-4a21-b825-c6e75ebe43eb")
    public static final ImageDescriptor DISABLED_IMG_DESC_OUTDENT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_OUTDENT);

    @objid ("427c1e38-1d36-46ed-95ac-4154a17e9e60")
    public static final ImageDescriptor DISABLED_IMG_DESC_PASTE = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_PASTE);

    @objid ("1218e9d2-96cf-4a0f-b4e6-099ee334a107")
    public static final ImageDescriptor DISABLED_IMG_DESC_PASTE_PLAIN_TEXT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_PASTE_PLAIN_TEXT);

//    public static final ImageDescriptor DISABLED_IMG_DESC_STRIKE_THROUGH = richTextPlugin
//            .getImageDescriptor(DISABLED_IMG_PATH_STRIKE_THROUGH);
    @objid ("9373ae0c-222c-42f1-96bb-3854f85cb6e5")
    public static final ImageDescriptor DISABLED_IMG_DESC_SUBSCRIPT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_SUBSCRIPT);

    @objid ("f816032a-22e2-4579-9977-6e8fc78333fa")
    public static final ImageDescriptor DISABLED_IMG_DESC_SUPERSCRIPT = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_SUPERSCRIPT);

    @objid ("4fa5330a-bd03-438b-96bb-9a450aad23d6")
    public static final ImageDescriptor DISABLED_IMG_DESC_UNDERLINE = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_UNDERLINE);

    @objid ("b6efc427-9467-4e36-9000-f36462f2452b")
    public static final ImageDescriptor DISABLED_IMG_DESC_TIDY = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_TIDY);

    @objid ("4cdde49b-66bc-4031-aa18-031e122e2795")
    public static final ImageDescriptor DISABLED_IMG_DESC_ADD_CODE = richTextPlugin
			.getImageDescriptor(DISABLED_IMG_PATH_ADD_CODE);

    @objid ("f3b04a8a-5f23-49f1-811e-bb5d06b61b63")
    public static final Image IMG_ADD_ROW = richTextPlugin.getSharedImage(IMG_PATH_ADD_ROW); // $NON-NLS-1$

    @objid ("aa03c755-d5e0-4747-bea0-e51fe4df80a2")
    public static final Image DISABLE_IMG_ADD_ROW = richTextPlugin.getSharedImage(DISABLED_IMG_PATH_ADD_ROW); // $NON-NLS-1$

    @objid ("0221c575-14d3-4f8b-915d-8d752a88342b")
    public static final Image IMG_ADD_COLUMN = richTextPlugin.getSharedImage(IMG_PATH_ADD_COLUMN); // $NON-NLS-1$

    @objid ("f11c9e94-af91-486a-b55c-4bf05cda2565")
    public static final Image DISABLE_IMG_ADD_COLUMN = richTextPlugin.getSharedImage(DISABLED_IMG_PATH_ADD_COLUMN); // $NON-NLS-1$

    @objid ("13ecf156-5b2d-4756-a10a-80ad361a15c5")
    public static final Image IMG_DELETE_ROW = richTextPlugin.getSharedImage(IMG_PATH_DELETE_ROW); // $NON-NLS-1$

    @objid ("fc31b50b-f5da-4d79-be27-9e71cd3b8285")
    public static final Image DISABLE_IMG_DELETE_ROW = richTextPlugin.getSharedImage(DISABLED_IMG_PATH_DELETE_ROW); // $NON-NLS-1$

    @objid ("1b565e76-d111-4b5d-aaf2-cfe455a9edd6")
    public static final Image IMG_DELETE_COLUMN = richTextPlugin.getSharedImage(IMG_PATH_DELETE_COLUMN); // $NON-NLS-1$

    @objid ("d9c5cde1-9765-4f75-8aa9-0da01c25bd58")
    public static final Image DISABLE_IMG_DELETE_COLUMN = richTextPlugin.getSharedImage(DISABLED_IMG_PATH_DELETE_COLUMN); // $NON-NLS-1$

}
