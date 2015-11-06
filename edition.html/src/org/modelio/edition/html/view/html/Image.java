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
 * Models a simplified HTML &lt;image&gt; tag.
 * 
 * @author Kelvin Low
 * @since 1.0
 */
@objid ("8bf0dd9e-b463-43f7-a4cb-e30d5ebb7cd1")
public class Image {
// The image URL.
    @objid ("98c98354-41ca-4e31-8041-e70d591e7594")
    private String url = ""; // $NON-NLS-1$

    /**
     * Creates a new instance.
     */
    @objid ("16894a4d-3f75-4064-85b4-988ece6a9789")
    public Image() {
    }

    /**
     * Gets the image URL.
     * @return the image URL
     */
    @objid ("77935fb8-0d30-4c34-ac69-b32fc10b76f4")
    public String getURL() {
        return this.url;
    }

    /**
     * Sets the image URL.
     * @param url the image URL
     */
    @objid ("0e97208c-b1c9-4082-8f15-89818364730a")
    public void setURL(String url) {
        this.url = url;
    }

}
