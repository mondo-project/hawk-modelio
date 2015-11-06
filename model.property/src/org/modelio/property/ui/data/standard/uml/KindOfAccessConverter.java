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
import org.modelio.metamodel.uml.statik.KindOfAccess;

@objid ("8f3f0bb6-c068-11e1-8c0a-002564c97630")
public class KindOfAccessConverter {
    @objid ("8f3f0bb7-c068-11e1-8c0a-002564c97630")
    public static String getLabel(KindOfAccess kindOfAccess) {
        if(kindOfAccess == KindOfAccess.READ) {
            return "Read";
        } 
        
        if(kindOfAccess == KindOfAccess.WRITE) {
            return "Write";
        }
        
        if(kindOfAccess == KindOfAccess.READWRITE) {
            return "Read write";
        }
        
        if (kindOfAccess == KindOfAccess.ACCESNONE) {
            return "None";
        }
        return "<Undefined>";
    }

    @objid ("8f3f0bbe-c068-11e1-8c0a-002564c97630")
    public static KindOfAccess getValue(String value) {
        if(value.equals("Read")) {
            return KindOfAccess.READ;
        }
        
        if(value.equals("Write")) {
            return KindOfAccess.WRITE;
        }
        
        if(value.equals("Read write")) {
            return KindOfAccess.READWRITE;
        }
        
        if(value.equals("None")) {
            return KindOfAccess.ACCESNONE;
        }
        return null;
    }

}
