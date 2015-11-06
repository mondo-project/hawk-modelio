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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.edition.html.plugin.HtmlTextResources;

/**
 * Models a HTML font style.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("32caa8b3-782e-407d-b608-084c5c5ad259")
public class FontStyle {
// The user friendly names.
    @objid ("861e531d-131f-489b-9a09-2cdcc58dee6a")
    private static final String NAME_NORMAL = HtmlTextResources.fontStyle_normal;

    @objid ("0ccec9b3-63ad-49fc-a64d-e7c33e2b4087")
    private static final String NAME_SECTION_HEADING = HtmlTextResources.fontStyle_sectionHeading;

    @objid ("59f7832c-ac63-4b06-97df-1fc756bf6a44")
    private static final String NAME_SUBSECTION_HEADING = HtmlTextResources.fontStyle_subsectionHeading;

    @objid ("6d4aa087-28d0-4ad7-a54a-df1b2cd88a36")
    private static final String NAME_SUB_SUBSECTION_HEADING = HtmlTextResources.fontStyle_subSubsectionHeading;

    @objid ("b72dd96c-7e6d-4e54-8e94-2eeda6537e58")
    private static final String NAME_QUOTE = HtmlTextResources.fontStyle_quote;

    @objid ("3d22c4ec-f5fc-4561-8ada-528a16059beb")
    private static final String NAME_CODE_SAMPLE = HtmlTextResources.fontStyle_codeSample;

// The internal values.
    @objid ("01f8115a-97e0-4d67-9e5d-5008fd5d16a3")
    private static final String VALUE_NORMAL = "<p>"; // $NON-NLS-1$

    @objid ("b7f631fa-4430-45ae-8129-8f2fa72087f7")
    private static final String VALUE_SECTION_HEADING = "<h3>"; // $NON-NLS-1$

    @objid ("b037c79b-4432-4c28-9e9d-f178df9689a7")
    private static final String VALUE_SUBSECTION_HEADING = "<h4>"; // $NON-NLS-1$

    @objid ("03467da5-a3fa-49fe-8524-f3ae50be9702")
    private static final String VALUE_SUB_SUBSECTION_HEADING = "<h5>"; // $NON-NLS-1$

    @objid ("ced9b71c-b408-481b-9c0f-e75feeb59cfb")
    private static final String VALUE_QUOTE = "<quote>"; // $NON-NLS-1$

    @objid ("f568916e-411e-43fa-8929-b811d1fe4881")
    private static final String VALUE_CODE_SAMPLE = "<code>"; // $NON-NLS-1$

// The font style name.
    @objid ("f8ce4656-23f1-43fc-8803-4123e7e110c6")
    private String name;

// The font style value.
    @objid ("49772679-244d-496d-a232-94ebb84ed62c")
    private String value;

    /**
     * Font style for normal text.
     */
    @objid ("f047a451-5f72-4a1b-a038-14ea818651d1")
    public static final FontStyle NORMAL = new FontStyle(NAME_NORMAL, VALUE_NORMAL);

    /**
     * Font style for section heading.
     */
    @objid ("6e3c8cb7-c38b-4cf7-bb67-58fa0435de72")
    public static final FontStyle SECTION_HEADING = new FontStyle(
			NAME_SECTION_HEADING, VALUE_SECTION_HEADING);

    /**
     * Font style for sub section heading.
     */
    @objid ("aceb99c5-0dd5-48b3-ace2-302d284bdbe5")
    public static final FontStyle SUBSECTION_HEADING = new FontStyle(
			NAME_SUBSECTION_HEADING, VALUE_SUBSECTION_HEADING);

    /**
     * Font style for sub sub section heading.
     */
    @objid ("3abbee14-3f5b-4cb5-8405-ad0d1e068dfa")
    public static final FontStyle SUB_SUBSECTION_HEADING = new FontStyle(
			NAME_SUB_SUBSECTION_HEADING, VALUE_SUB_SUBSECTION_HEADING);

    /**
     * Font style for quotations.
     */
    @objid ("10092891-afe2-4154-8e4b-43f4ac2a2081")
    public static final FontStyle QUOTE = new FontStyle(NAME_QUOTE, VALUE_QUOTE);

    /**
     * Font style for displaying program codes.
     */
    @objid ("3a363ced-ae05-4c5a-8471-bf407cdb5825")
    public static final FontStyle CODE_SAMPLE = new FontStyle(NAME_CODE_SAMPLE,
			VALUE_CODE_SAMPLE);

    /**
     * A list of <code>FontStyle</code> objects.
     */
    @objid ("f4566b4e-0f13-458f-80ea-65ecfeeffb35")
    private static final List<FontStyle> FONT_STYLES = new ArrayList<>();

    /**
     * Creates a new instance.
     * @param name the font style name
     * @param value the font style value
     */
    @objid ("0fcc1706-8e0c-4a8d-9640-b19ac3a9a603")
    public FontStyle(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the font style name.
     * @return the font style name
     */
    @objid ("286efc06-81fe-4a6b-9a3e-f83a540b6c26")
    public String getName() {
        return this.name;
    }

    /**
     * Gets the font style value.
     * @return the font style value
     */
    @objid ("36d87570-4410-408a-8c5c-fcd00f3b42a2")
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the <code>FontStyle</code> object that is mapped to the given
     * index.
     * @param index an index into the <code>FontStyle</code> list
     * @return a <code>FontStyle</code> object
     */
    @objid ("9d9e82ce-0669-4d0e-b281-e432428bfa0c")
    public static FontStyle getFontStyle(int index) {
        FontStyle result = FONT_STYLES.get(index);
        if (result != null) {
            return result;
        }
        return NORMAL;
    }

    /**
     * Gets the display name of the <code>FontStyle</code> object with the
     * given value.
     * @param value one of the FontStyles
     * @return a display name of a FontStyle, or null if none found
     */
    @objid ("cf4bf735-4541-4604-ba78-d95c5d5f9557")
    public static String getFontStyleName(String value) {
        for (FontStyle style : FONT_STYLES) {
            if (style.getValue().equals(value)) {
                return style.getName();
            }
        }
        return null;
    }

    /**
     * Gets the value of the <code>FontStyle</code> object with the
     * given display name.
     * @param name one of the FontStyles
     * @return a value of a FontStyle, or null if none found
     */
    @objid ("14b959af-ef7d-4962-8187-42901dfc45de")
    public static String getFontStyleValue(String name) {
        for (FontStyle style : FONT_STYLES) {
            if (style.getName().equals(name)) {
                return style.getValue();
            }
        }
        return null;
    }


static {
        FONT_STYLES.add(NORMAL);
        FONT_STYLES.add(SECTION_HEADING);
        FONT_STYLES.add(SUBSECTION_HEADING);
        FONT_STYLES.add(SUB_SUBSECTION_HEADING);
        FONT_STYLES.add(QUOTE);
        FONT_STYLES.add(CODE_SAMPLE);
    }
}
