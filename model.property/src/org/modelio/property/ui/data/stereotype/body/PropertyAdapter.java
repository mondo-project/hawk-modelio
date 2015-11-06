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

import java.util.Date;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.properties.EnumeratedPropertyType;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyDefinition;
import org.modelio.metamodel.uml.infrastructure.properties.PropertyEnumerationLitteral;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Provides conversion facilities for property values.
 * 
 * <table border='1'>
 * <th>Enum Type</th>
 * <th>Java Object type</th>
 * <th>Storage string</th>
 * <tr align='left'>
 * <td>BOOLEAN</td>
 * <td>java.lang.Boolean</td>
 * <td>Boolean.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>ENUMERATE</td>
 * <td>org.modelio.metamodel.uml.infrastructure.properties.
 * PropertyEnumerationLitteral</td>
 * <td>The property literal name</td>
 * </tr>
 * <tr align='left'>
 * <td>FLOAT</td>
 * <td>java.lang.Float</td>
 * <td>Float.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>UNSIGNED</td>
 * <td>java.lang.Integer</td>
 * <td>Integer.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>INTEGER</td>
 * <td>java.lang.Integer</td>
 * <td>Integer.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>ELEMENT</td>
 * <td>MRef</td>
 * <td>MRef.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>RICHTEXT</td>
 * <td>?</td>
 * <td>?</td>
 * </tr>
 * <tr align='left'>
 * <td>TIME</td>
 * <td>java.sql.Time</td>
 * <td>Time as long, long.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>DATE</td>
 * <td>java.util.Date</td>
 * <td>Date as long, long.toString()</td>
 * </tr>
 * <tr align='left'>
 * <td>STRING</td>
 * <td>java.lang.String</td>
 * <td>the string itself</td>
 * </tr>
 * <tr align='left'>
 * <td>TEXT</td>
 * <td>java.lang.String</td>
 * <td>the string itself</td>
 * </tr>
 * </table>
 * 
 * @author phv
 */
@objid ("72832eec-5fdc-4fa8-b362-93b67b4b090d")
public class PropertyAdapter {
    @objid ("be2b5bc6-7eb6-44af-9ec7-a4e5cca3b5a5")
    public static Object convertToObject(PropertyDefinition pdef, String value) {
        // If there is no base type, can only return the string
        if (pdef.getType().getBaseType() == null) {
            return value;
        }
        
        // Conversion based on the base type of the property definition
        switch (pdef.getType().getBaseType()) {
        case BOOLEAN:
            return new Boolean(value);
        case ENUMERATE:
            final EnumeratedPropertyType type = (EnumeratedPropertyType) pdef.getType();
            for (final PropertyEnumerationLitteral v : type.getLitteral()) {
                if (v.getName().equals(value)) {
                    return v;
                }
            }
            return null;
        case FLOAT:
            if (value == null || value.isEmpty()) {
                return new Float("0.0");
            } else {
                return new Float(value);
            }
        case UNSIGNED:
        case INTEGER:
            if (value == null || value.isEmpty()) {
                return new Integer(0);
            }
            try {
                return new Integer(new Float(value).intValue());
            } catch (final NumberFormatException e) {
                return new Integer(Integer.MIN_VALUE);
            }
        case ELEMENT:
            if (value != null) {
                try {
                    return new MRef(value);
                } catch (final IllegalArgumentException e) {
                    return null;
                }
            }
            return null;
        case RICHTEXT:
            if (value != null) {
                try {
                    return new MRef(value);
                } catch (final IllegalArgumentException e) {
                    return null;
                }
            }
            return null;
        case TIME:
            if (value == null || value.isEmpty()) {
                return new Date();
            }
            try {
                return new Date(Long.parseLong(value));
            } catch (final NumberFormatException e) {
                return new Date();
            }
        case DATE:
            if (value == null || value.isEmpty()) {
                return new Date();
            }
            try {
                return new Date(Long.parseLong(value));
            } catch (final NumberFormatException e) {
                return new Date();
            }
        case STRING:
        case TEXT:
        default:
            return value;
        
        }
    }

    @objid ("92050935-8245-4501-9593-97ad9c462ffc")
    public static Class<?> getType(PropertyDefinition pdef) {
        // If there is no base type, can only return the string
        if (pdef.getType().getBaseType() == null) {
            return String.class;
        }
        
        // Conversion based on the base type of the property definition
        switch (pdef.getType().getBaseType()) {
        case BOOLEAN:
            return Boolean.class;
        case ENUMERATE:
            return Enum.class;
        case FLOAT:
            return Float.class;
        case UNSIGNED:
        case INTEGER:
            return Integer.class;
        case ELEMENT:
            return MObject.class;
        case TIME:
        case DATE:
            return Date.class;
        case STRING:
        case RICHTEXT:
        case TEXT:
        default:
            return String.class;
        }
    }

    @objid ("4de3822d-8c34-4047-91d6-e3bd2366fa95")
    public static String convertToString(PropertyDefinition pdef, Object value) {
        if (value == null) {
            return "";
        }
        
        switch (pdef.getType().getBaseType()) {
        case BOOLEAN:
            return Boolean.toString((Boolean) value);
        case ENUMERATE:
            return ((PropertyEnumerationLitteral) value).getName();
        case INTEGER:
            return value.toString();
        case STRING:
            return value.toString();
        case DATE:
            return String.valueOf(((Date) value).getTime());
        case ELEMENT:
            return value.toString();
        case FLOAT:
            return value.toString();
        case RICHTEXT:
            return value.toString();
        case TEXT:
            return value.toString();
        case TIME:
            return String.valueOf(((Date) value).getTime());
        case UNSIGNED:
            return value.toString();
        default:
            return "?" + value.getClass().getSimpleName() + "?";
        }
    }

}
