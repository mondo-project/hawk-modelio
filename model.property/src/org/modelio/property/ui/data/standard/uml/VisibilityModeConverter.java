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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.statik.VisibilityMode;

@objid ("8fa566ea-c068-11e1-8c0a-002564c97630")
public class VisibilityModeConverter {
    @objid ("8fa566eb-c068-11e1-8c0a-002564c97630")
    public static String getLabel(VisibilityMode visibility) {
        if(visibility == VisibilityMode.PUBLIC) {
            return "Public";
        } else if(visibility == VisibilityMode.PACKAGEVISIBILITY) {
            return "Package";
        } else if(visibility == VisibilityMode.PROTECTED) {
            return "Protected";
        } else if(visibility == VisibilityMode.PRIVATE) {
            return "Private";
        } else {
            return "<Undefined>";
        }
    }

    @objid ("8fa566f1-c068-11e1-8c0a-002564c97630")
    public static VisibilityMode getValue(String value) {
        if(value.equals("Public")) {
            return VisibilityMode.PUBLIC;
        } else if(value.equals("Package")) {
            return VisibilityMode.PACKAGEVISIBILITY;
        } else if(value.equals("Protected")) {
            return VisibilityMode.PROTECTED;
        } else if(value.equals("Private")) {
            return VisibilityMode.PRIVATE;
        } else {
            return VisibilityMode.VISIBILITYUNDEFINED;
        }
    }

}
