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
 * Models a HTML block tag.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("d1cf826c-b2e0-4918-92cc-e28855304930")
public class BlockTag {
// The user friendly names.
    @objid ("35f8377c-e89e-4c4e-92a8-4e918f3aae50")
    private static final String NAME_PARAGRAPH = HtmlTextResources.blockTag_paragraph;

    @objid ("9dfdef0d-2c62-4637-a34f-d5ee8e5d269b")
    private static final String NAME_HEADING_1 = HtmlTextResources.blockTag_heading1;

    @objid ("09e2d7a9-ef6a-429d-ae7c-5d1aa0c1d3c0")
    private static final String NAME_HEADING_2 = HtmlTextResources.blockTag_heading2;

    @objid ("b82852ba-66b1-47ec-8616-be9dce761740")
    private static final String NAME_HEADING_3 = HtmlTextResources.blockTag_heading3;

    @objid ("31b838fd-f5c9-43ea-ab90-d3eb599cde25")
    private static final String NAME_HEADING_4 = HtmlTextResources.blockTag_heading4;

    @objid ("c47b5a0b-fcd5-4f67-99fe-ed34a2ff29ae")
    private static final String NAME_HEADING_5 = HtmlTextResources.blockTag_heading5;

    @objid ("f6fe7f38-9d2b-49a5-a132-aeb57a2e0765")
    private static final String NAME_HEADING_6 = HtmlTextResources.blockTag_heading6;

    @objid ("b7a9cdbb-6241-40ed-805c-8fa391f6cde4")
    private static final String NAME_ADDRESS = HtmlTextResources.blockTag_address;

    @objid ("91fc4f98-3a0c-4cc3-97a2-b888e69a8d96")
    private static final String NAME_PREFORMATTED_TEXT = HtmlTextResources.blockTag_preformattedText;

// The internal values.
    @objid ("4314562a-c7a3-443b-9ad6-48174cb45a6c")
    private static final String VALUE_PARAGRAPH = "<p>"; // $NON-NLS-1$

    @objid ("62aa118f-9ffe-47a7-a367-a78a486849bb")
    private static final String VALUE_HEADING_1 = "<h1>"; // $NON-NLS-1$

    @objid ("3e8fc2d6-36bf-4cd1-bd35-fcaa35e9b718")
    private static final String VALUE_HEADING_2 = "<h2>"; // $NON-NLS-1$

    @objid ("78161aab-7150-4503-a3a4-18f25e3a6900")
    private static final String VALUE_HEADING_3 = "<h3>"; // $NON-NLS-1$

    @objid ("857af871-5210-4e82-a087-cf0e94c06915")
    private static final String VALUE_HEADING_4 = "<h4>"; // $NON-NLS-1$

    @objid ("76763a5c-2700-49b7-a16a-26bc0417b218")
    private static final String VALUE_HEADING_5 = "<h5>"; // $NON-NLS-1$

    @objid ("a89f8f81-b032-4509-a3b3-717f5e229fe3")
    private static final String VALUE_HEADING_6 = "<h6>"; // $NON-NLS-1$

    @objid ("2cbb7753-f530-4b6f-9c5c-b240a0a0a1be")
    private static final String VALUE_ADDRESS = "<address>"; // $NON-NLS-1$

    @objid ("f2f6da85-3b62-449e-a574-1305b1ab52cf")
    private static final String VALUE_PREFORMATTED_TEXT = "<pre>"; // $NON-NLS-1$

// The block tag name.
    @objid ("caef9af7-9e25-4d9d-a24a-d9e1b394c40c")
    private String name;

// The block tag value.
    @objid ("6118eff3-685d-4ebb-ba92-1d55e8c6371c")
    private String value;

    /**
     * The &lt;p&gt; tag.
     */
    @objid ("158a507c-f115-4559-b164-fdd811048154")
    public static final BlockTag PARAGRAPH = new BlockTag(NAME_PARAGRAPH,
			VALUE_PARAGRAPH);

    /**
     * &lt;h1&gt; tag.
     */
    @objid ("6a16f6f0-7e27-46fd-939b-2eb615c974e4")
    public static final BlockTag HEADING_1 = new BlockTag(NAME_HEADING_1,
			VALUE_HEADING_1);

    /**
     * &lt;h2&gt; tag.
     */
    @objid ("742db280-4d49-4152-b75e-1d8a5bf7f526")
    public static final BlockTag HEADING_2 = new BlockTag(NAME_HEADING_2,
			VALUE_HEADING_2);

    /**
     * &lt;h3&gt; tag.
     */
    @objid ("53a90c41-60f6-4b3f-8b27-e715c2622761")
    public static final BlockTag HEADING_3 = new BlockTag(NAME_HEADING_3,
			VALUE_HEADING_3);

    /**
     * &lt;h4&gt; tag.
     */
    @objid ("7393ab3e-7f28-48db-8490-dcf0409ac589")
    public static final BlockTag HEADING_4 = new BlockTag(NAME_HEADING_4,
			VALUE_HEADING_4);

    /**
     * &lt;h5&gt; tag.
     */
    @objid ("72d33eab-e468-4fac-b113-e8bf567b3fd5")
    public static final BlockTag HEADING_5 = new BlockTag(NAME_HEADING_5,
			VALUE_HEADING_5);

    /**
     * &lt;h6&gt; tag.
     */
    @objid ("1b88cf2c-774d-4670-af5e-aacfe352b9dd")
    public static final BlockTag HEADING_6 = new BlockTag(NAME_HEADING_6,
			VALUE_HEADING_6);

    /**
     * &lt;address&gt; tag.
     */
    @objid ("29ea201b-53d9-4fea-a38b-c438bcbafa4a")
    public static final BlockTag ADDRESS = new BlockTag(NAME_ADDRESS,
			VALUE_ADDRESS);

    /**
     * &lt;pre&gt; tag.
     */
    @objid ("864f5fe1-6b1e-41f7-a24f-eddcdc5d8fce")
    public static final BlockTag PREFORMATTED_TEXT = new BlockTag(
			NAME_PREFORMATTED_TEXT, VALUE_PREFORMATTED_TEXT);

// A list of <code>BlockTag</code> objects.
    @objid ("2c902b0f-6e65-4b10-bd19-f357a3289ad9")
    private static final List<BlockTag> BLOCK_TAGS = new ArrayList<>();

    /**
     * Creates a new instance.
     * @param name the block tag name
     * @param value the block tag value
     */
    @objid ("4f5d7ce3-5eff-4967-af5d-55b802780a22")
    public BlockTag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the block tag name.
     * @return the block tag name
     */
    @objid ("7dd8a060-b613-4ff5-8acb-ca221bf3d783")
    public String getName() {
        return this.name;
    }

    /**
     * Gets the block tag value.
     * @return the block tag value
     */
    @objid ("8222f9eb-961d-4211-9b77-93d5135f0d26")
    public String getValue() {
        return this.value;
    }

    /**
     * Gets the <code>BlockTag</code> object that is mapped to the given
     * index.
     * @param index an index into the <code>BlockTag</code> list
     * @return a <code>BlockTag</code> object
     */
    @objid ("93693bcc-1272-418f-a44b-fc57c4553fa6")
    public static BlockTag getBlockTag(int index) {
        BlockTag result = BLOCK_TAGS.get(index);
        if (result != null) {
            return result;
        }
        return PARAGRAPH;
    }


static {
        BLOCK_TAGS.add(PARAGRAPH);
        BLOCK_TAGS.add(HEADING_1);
        BLOCK_TAGS.add(HEADING_2);
        BLOCK_TAGS.add(HEADING_3);
        BLOCK_TAGS.add(HEADING_4);
        BLOCK_TAGS.add(HEADING_5);
        BLOCK_TAGS.add(HEADING_6);
        BLOCK_TAGS.add(ADDRESS);
        BLOCK_TAGS.add(PREFORMATTED_TEXT);
    }
}
