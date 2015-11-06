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
import org.modelio.metamodel.uml.statik.AggregationKind;

@objid ("8ecc6bf9-c068-11e1-8c0a-002564c97630")
public class AggregationKindConverter {
    @objid ("8ecc6bfa-c068-11e1-8c0a-002564c97630")
    public static String getLabel(AggregationKind visibility) {
        if(visibility == AggregationKind.KINDISASSOCIATION) {
            return "Association";
        }
                
        if(visibility == AggregationKind.KINDISAGGREGATION) {
            return "Aggregation";
        }
        
        if(visibility == AggregationKind.KINDISCOMPOSITION) {
            return "Composition";
        }
        return "<Undefined>";
    }

    @objid ("8ecc6c01-c068-11e1-8c0a-002564c97630")
    public static AggregationKind getValue(String value) {
        if(value.equals("Association")) {
            return AggregationKind.KINDISASSOCIATION;
        }
        
        if(value.equals("Aggregation")) {
            return AggregationKind.KINDISAGGREGATION;
        }
        
        if(value.equals("Composition")) {
            return AggregationKind.KINDISCOMPOSITION;
        }
        return null;
    }

}
