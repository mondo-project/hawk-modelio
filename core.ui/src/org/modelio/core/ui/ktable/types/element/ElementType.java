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
                                    

package org.modelio.core.ui.ktable.types.element;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.core.ui.ktable.types.PropertyType;
import org.modelio.vcore.session.api.model.IMObjectFilter;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8de910eb-c068-11e1-8c0a-002564c97630")
abstract class ElementType extends PropertyType {
    @objid ("8de910ec-c068-11e1-8c0a-002564c97630")
    protected IMObjectFilter elementFilter = null;

    @objid ("8de910ed-c068-11e1-8c0a-002564c97630")
    protected List<Class<? extends MObject>> allowedClasses = null;

    @objid ("8de910f2-c068-11e1-8c0a-002564c97630")
    public ElementType(boolean acceptNullValue, Class<? extends MObject> allowedClass) {
        super(acceptNullValue);
        this.allowedClasses = new ArrayList<>();
        this.allowedClasses.add(allowedClass);
    }

    @objid ("8de910f8-c068-11e1-8c0a-002564c97630")
    public List<Class<? extends MObject>> getAllowedClasses() {
        return this.allowedClasses;
    }

    @objid ("8de91100-c068-11e1-8c0a-002564c97630")
    public ElementType(boolean acceptNullValue, List<Class<? extends MObject>> allowedClasses) {
        super(acceptNullValue);
        this.allowedClasses = allowedClasses;
    }

    @objid ("8dea9768-c068-11e1-8c0a-002564c97630")
    public IMObjectFilter getElementFilter() {
        return this.elementFilter;
    }

    @objid ("8dea976c-c068-11e1-8c0a-002564c97630")
    public void setElementFilter(IMObjectFilter elementFilter) {
        this.elementFilter = elementFilter;
    }

}
