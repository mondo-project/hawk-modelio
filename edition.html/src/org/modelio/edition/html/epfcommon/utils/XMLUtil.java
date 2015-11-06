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
package org.modelio.edition.html.epfcommon.utils;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Utility class for processing XML documents.
 * 
 * @author Kelvin Low
 * @author Jinhua Xi
 * @author Phong Nguyen Le
 * @since 1.0
 */
@objid ("64d94a5e-81b9-4080-8b33-0f2b96060464")
@SuppressWarnings("javadoc")
public class XMLUtil {
    /**
     * XML declaration.
     */
    @objid ("533fe70b-11b9-48a6-936d-859e1b5b18b7")
    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"; // $NON-NLS-1$

    /**
     * XML Escape characters.
     */
    @objid ("7bbc2ff5-2779-4d5c-abcf-34bf7068f9a3")
    public static final String XML_AMP = "&amp;"; // $NON-NLS-1$

    @objid ("ce798fca-e1f9-41d7-b387-3c1838b97d4e")
    public static final String XML_BACKSLASH = "&#92;"; // $NON-NLS-1$

    @objid ("ed24eef7-29c5-450e-b484-74e9c52f612b")
    public static final String XML_APOS = "&apos;"; // $NON-NLS-1$

    @objid ("bd23d914-89b8-4cc4-aa83-170d0e484d28")
    public static final String XML_CR = "&#13;"; // $NON-NLS-1$

    @objid ("322918a6-2dea-4d78-97cb-b97bf4777937")
    public static final String XML_GT = "&gt;"; // $NON-NLS-1$

    @objid ("52f985f7-a7c7-45f5-bbb0-ab96c8615899")
    public static final String XML_LT = "&lt;"; // $NON-NLS-1$

    @objid ("d7f45699-0482-4551-90af-3b272e504367")
    public static final String XML_LF = "&#10;"; // $NON-NLS-1$

    @objid ("eb8df4e4-f1b6-4058-beca-3f98aa5a69cd")
    public static final String XML_QUOT = "&quot;"; // $NON-NLS-1$

    @objid ("6a42fe5e-266f-4525-a2b4-dfb15c83f708")
    public static final String XML_TAB = "&#9;"; // $NON-NLS-1$

    /**
     * Private constructor to prevent this class from being instantiated. All
     * methods in this class should be static.
     */
    @objid ("049aae74-0b1a-43cb-9e20-a86b0a66d4b9")
    private XMLUtil() {
    }

    /**
     * Escapes the given string to make it XML parser friendly.
     * @param str The source string.
     * @return The escaped string.
     */
    @objid ("25c69bf5-70b2-443d-a4bd-b3d5f19969e3")
    public static String escape(String str) {
        if (str == null || str.length() == 0)
            return ""; //$NON-NLS-1$
        StringBuffer sb = new StringBuffer();
        int len = str.length();
        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);
            switch (ch) {
            case '<':
                sb.append(XML_LT);
                break;
            case '>':
                sb.append(XML_GT);
                break;
            case '&':
                sb.append(XML_AMP);
                break;
            case '"':
                sb.append(XML_QUOT);
                break;
            case '\'':
                sb.append(XML_APOS);
                break;
            case '\r':
                sb.append(XML_CR);
                break;
            case '\n':
                sb.append(XML_LF);
                break;
            case '\\':
                sb.append(XML_BACKSLASH);
                break;
            default:
                sb.append(ch);
                break;
            }
        }
        return sb.toString();
    }

}
