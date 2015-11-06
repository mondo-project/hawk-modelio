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
                                    

package org.modelio.core.ui.nattable.editors;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;

@objid ("4a66fbb2-d030-49e4-99c3-87073210b03f")
public class TextIcon {
    @objid ("7b9b3927-b8ea-4ec0-a41a-94c00566db45")
     String text;

    @objid ("d3a10fdf-4a1d-45a7-9e2e-fa554cfd20e5")
     Image icon;

    @objid ("72810016-7d1d-4a2e-ac38-6f3b513bf686")
    public TextIcon(String text, Image icon) {
        this.text = text;
        this.icon = icon;
    }

    @objid ("8a387270-8d57-4b84-a4c8-54e3d0c44a44")
    @Override
    public String toString() {
        return this.text;
    }

}
