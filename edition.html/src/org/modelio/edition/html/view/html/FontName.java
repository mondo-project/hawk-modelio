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
package org.modelio.edition.html.view.html;

import java.util.Arrays;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.edition.html.plugin.HtmlTextResources;

/**
 * Models a HTML font name.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("566f0ddc-84ed-4b6a-9cae-f4a82d366649")
public class FontName {
// The user friendly names.
    @objid ("fbc8a4b4-0332-4cbf-9779-f66b65854460")
    private static final String NAME_DEFAULT = HtmlTextResources.fontName_default;

    @objid ("22cd6dd2-1b4e-459d-8970-2c860d12702c")
    private static final String NAME_ARIAL = HtmlTextResources.fontName_arial;

    @objid ("42e41867-9f57-4394-945c-149c46e77564")
    private static final String NAME_COURIER_NEW = HtmlTextResources.fontName_courierNew;

    @objid ("8d61d817-6efc-43f9-a6ff-1c48afe13535")
    private static final String NAME_TIMES_NEW_ROMAN = HtmlTextResources.fontName_timesNewRoman;

    @objid ("6224aa1a-6f02-4da9-86db-0bd9fe5f4c85")
    private static final String NAME_VERDANA = HtmlTextResources.fontName_verdana;

// The internal values.
    @objid ("4264ad2c-5aaa-4627-9b2a-073921dabd1c")
    private static final String VALUE_DEFAULT = ""; // $NON-NLS-1$

    @objid ("4a74d7d0-be12-4269-b54a-333859b19107")
    private static final String VALUE_ARIAL = "Arial, Helvetica, sans-serif"; // $NON-NLS-1$

    @objid ("31537d92-ed10-4381-9c98-bbfc432c5889")
    private static final String VALUE_COURIER_NEW = "Courier New, Courier, mono"; // $NON-NLS-1$

    @objid ("ffa7790b-bf19-466d-8d55-a7fae71b43dc")
    private static final String VALUE_TIMES_NEW_ROMAN = "Times New Roman, Times, serif"; // $NON-NLS-1$

    @objid ("180b6422-adcf-4dc8-82ba-a6a35a65ecd7")
    private static final String VALUE_VERDANA = "Verdana, Arial, Helvetica, sans-serif"; // $NON-NLS-1$

    /**
     * The font user friendly name.
     */
    @objid ("a945aeac-bbc8-4cc4-b676-9728f735233f")
    private final String name;

    /**
     * The font HTML value.
     */
    @objid ("edc7d61a-3845-44f1-b6da-c584c7328592")
    private final String value;

    /**
     * The lower case font HTML value.
     */
    @objid ("5380a9eb-7b76-4469-b987-12685f8995ab")
    private final String lowCaseValue;

    /**
     * Default font.
     */
    @objid ("13999830-cb30-438c-ba90-789805a1612c")
    public static final FontName DEFAULT = new FontName(NAME_DEFAULT, VALUE_DEFAULT);

    /**
     * Arial font.
     */
    @objid ("bebc4fd7-7934-4b22-929b-f2675a0d98c6")
    public static final FontName ARIAL = new FontName(NAME_ARIAL, VALUE_ARIAL);

    /**
     * Courier New font.
     */
    @objid ("c3ff200f-6f14-4b74-b534-8eed99432ff7")
    public static final FontName COURIER_NEW = new FontName(NAME_COURIER_NEW, VALUE_COURIER_NEW);

    /**
     * Times New Roman font.
     */
    @objid ("ce314d74-8f90-4ed7-8f5e-fa5526a8a73b")
    public static final FontName TIMES_NEW_ROMAN = new FontName(
			NAME_TIMES_NEW_ROMAN, VALUE_TIMES_NEW_ROMAN);

    /**
     * Verdana font.
     */
    @objid ("cf8e5dbb-828f-49f6-9c47-bf4e022dc5d9")
    public static final FontName VERDANA = new FontName(NAME_VERDANA, VALUE_VERDANA);

    /**
     * List of all <code>FontName</code> objects.
     */
    @objid ("b0ddb7e9-c35a-4090-b085-10ec534e1040")
    private static final List<FontName> FONT_NAMES = Arrays.asList(DEFAULT,ARIAL,COURIER_NEW,TIMES_NEW_ROMAN,VERDANA);

    /**
     * Creates a new instance.
     * @param name the font name
     * @param value the font value
     */
    @objid ("d2528b5f-d36d-4584-8fa8-cb16daacb90d")
    public FontName(String name, String value) {
        this.name = name;
        this.value = value;
        this.lowCaseValue = value.toLowerCase();
    }

    /**
     * Gets the user friendly font name.
     * @return the user friendly font name
     */
    @objid ("720ab8aa-5934-46be-a4ce-f3206d4489ff")
    public String getName() {
        return this.name;
    }

    /**
     * Gets the font HTML value.
     * @return the font HTML value
     */
    @objid ("60107137-0c09-4db2-a41c-ed6c7cadcc16")
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the <code>FontName</code> object that is mapped to the given
     * index.
     * @param index an index into the <code>FontName</code> list
     * @return a <code>FontName</code> object
     */
    @objid ("242d1543-b982-420e-922a-99d9b7a2e8ce")
    public static FontName getFontName(int index) {
        FontName result = FONT_NAMES.get(index);
        if (result != null) {
            return result;
        }
        return DEFAULT;
    }

    /**
     * Find a <code>FontName</code> from a HTML font value.
     * @param htmlName a HTML font value.
     * @return the found font or <i>null</i>.
     */
    @objid ("adcef9d7-2d21-469b-8e7b-3007439f043a")
    public static FontName fromHtml(String htmlName) {
        final String lowerCase = htmlName.toLowerCase();
        
        for (FontName f : FONT_NAMES) {
            if (f.getValue().equals(lowerCase))
                return f;
        }
        return null;
    }

}
