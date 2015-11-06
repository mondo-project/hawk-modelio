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
                                    

package org.modelio.property.ui.data.standard.uml;

import java.util.Arrays;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("8f62c08d-c068-11e1-8c0a-002564c97630")
public class ModelPropertyDefinition {
    @objid ("a8046a48-c068-11e1-8c0a-002564c97630")
    public String name;

    @objid ("a8046a4d-c068-11e1-8c0a-002564c97630")
    public String[] items = null;

    @objid ("a8046a50-c068-11e1-8c0a-002564c97630")
    public ValueTypeEnum type;

    @objid ("8f62c093-c068-11e1-8c0a-002564c97630")
    public ModelPropertyDefinition(String name, ValueTypeEnum type) {
        super();
        this.name = name;
        this.type = type;
    }

    @objid ("8f62c097-c068-11e1-8c0a-002564c97630")
    public ModelPropertyDefinition(String name, ValueTypeEnum type, String[] items) {
        super();
        this.name = name;
        this.type = type;
        this.items = Arrays.copyOf(items, items.length);
    }

    @objid ("8fa566f9-c068-11e1-8c0a-002564c97630")
    public enum ValueTypeEnum {
        BOOLEAN,
        STRING,
        ENUMERATION,
        ELEMENT,
        ELEMENTS_LIST;
    }

}
