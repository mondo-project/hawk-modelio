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

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Models a simplified HTML &lt;link&gt; tag.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("f8e24367-8a19-4b9c-934a-d0a19c8a9a13")
public class Link {
    /**
     * The link name.
     */
    @objid ("32dfcc4d-bd3f-4d2d-88f0-292ebe14a32c")
    private String name = ""; // $NON-NLS-1$

    /**
     * The link URL.
     */
    @objid ("67528b6c-700c-46b5-9264-05efacf620fb")
    private String url = ""; // $NON-NLS-1$

    /**
     * Creates a new instance.
     */
    @objid ("27858483-c32f-4023-84ee-93cd3af4690e")
    public Link() {
    }

    /**
     * Gets the link name.
     * @return the link name.
     */
    @objid ("ea4798c1-79ce-4e9c-9bce-9cf6040e9a2e")
    public String getName() {
        return this.name;
    }

    /**
     * Sets the link name.
     * @param name the link name
     */
    @objid ("9f3be3ea-3484-4d9c-86ca-9deda1efad82")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the link URL.
     * @return the link URL
     */
    @objid ("e2f216d1-005a-49cc-a2e0-0cefdf57f736")
    public String getURL() {
        return this.url;
    }

    /**
     * Sets the link URL.
     * @param url the link URL
     */
    @objid ("ca7c25e0-a283-4bed-9d44-678fae94e17f")
    public void setURL(String url) {
        this.url = url;
    }

    /**
     * @Gets the HTML representation of this link.
     * @return the HTML representation of this link
     */
    @objid ("09567e1c-e574-44e0-9065-03bcc8e71119")
    public String toHTML() {
        return null;
    }

}
