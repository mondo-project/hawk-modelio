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
                                    

package org.modelio.mda.infra.service.dynamic;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("74fe2831-8d67-47e2-a7b5-436c33f90523")
public class GenericScope {
    @objid ("e585d2a1-17f6-459e-9338-1b10409f062a")
    protected String metaclass;

    @objid ("ac63437c-96fe-400b-9c3b-984e54b11918")
    protected String stereotype;

    @objid ("6b3e5d91-2bcc-45af-9699-25222eeff729")
    public GenericScope(String metaclass, String stereotype) {
        this.metaclass = metaclass;
        this.stereotype = stereotype;
    }

    @objid ("fb59f651-e67e-4aa3-b323-56bdce4096f0")
    public String getMetaclass() {
        return this.metaclass;
    }

    @objid ("868387f8-496a-41a1-80b4-f7de79b1bd4f")
    public String getStereotype() {
        return this.stereotype;
    }

}
