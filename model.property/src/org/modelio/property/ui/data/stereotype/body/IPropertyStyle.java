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
                                    

package org.modelio.property.ui.data.stereotype.body;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;

@objid ("c39ff2b6-e066-4c24-a5af-4713662be381")
public interface IPropertyStyle {
    @objid ("a98d99b0-a116-45e8-b0c4-3e7664e97bd0")
    Color getBackgroundColor();

    @objid ("63306e22-b2b9-4024-a251-40966a4dfdaa")
    void setBackgroundColor(Color color);

    @objid ("34cf3bba-4d66-4264-83da-fb31bdb6d563")
    Color getForegroundColor();

    @objid ("45b81a4c-116b-4980-97d5-5fcf494512a6")
    void setForegroundColor(Color color);

}
