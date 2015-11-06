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

import java.util.Date;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.smkernel.mapi.MRef;
import org.modelio.vcore.smkernel.meta.SmAttribute;
import org.modelio.vcore.smkernel.meta.SmDependency;

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
@objid ("2357d4ba-db54-44f4-a011-64d43e43ca63")
public class TextIconDisplayAdapter {
    @objid ("cb3dd7cc-f47e-44da-8e0b-e2603bdfb0cb")
    public static Object convertToObject(Class<?> type, String value) {
        Class<?> realType;
        // If there is no base type, can only return the string
        if (type == null) {
            return value;
        } else if (Element.class.isAssignableFrom(type)) {
            realType = Element.class;
        } else if (SmAttribute.class.isAssignableFrom(type)) {
            realType = SmAttribute.class;
        } else if (SmDependency.class.isAssignableFrom(type)) {
            realType = SmAttribute.class;
        } else {
            realType = type;
        }
        
        // Conversion based on the base type of the property definition
        switch (realType.getSimpleName()) {
        case "Boolean":
            return new Boolean(value);
        case "Enum":
            for (Object l : realType.getEnumConstants()) {
                if (l.toString().equals(value)) {
                    return l;
                }
            }
            return realType.getEnumConstants()[0];
        case "Float":
            if (value == null || value.isEmpty()) {
                return new Float("0.0");
            } else {
                return new Float(value);
            }
        case "Integer":
            if (value == null || value.isEmpty()) {
                return new Integer(0);
            }
            try {
                return new Integer(new Float(value).intValue());
            } catch (final NumberFormatException e) {
                return new Integer(Integer.MIN_VALUE);
            }
        case "Element":
            if (value != null) {
                try {
                    return new MRef(value);
                } catch (final IllegalArgumentException e) {
                    return null;
                }
            }
            return null;
        case "Time":
            if (value == null || value.isEmpty()) {
                return new Date();
            }
            try {
                return new Date(Long.parseLong(value));
            } catch (final NumberFormatException e) {
                return new Date();
            }
        case "Date":
            if (value == null || value.isEmpty()) {
                return new Date();
            }
            try {
                return new Date(Long.parseLong(value));
            } catch (final NumberFormatException e) {
                return new Date();
            }
        case "String":
        default:
            return value;
        
        }
    }

    @objid ("378b1344-f7a3-44a4-a054-7eb79444b021")
    public static String convertToString(Class<?> type, Object value) {
        Class<?> realType;
        if (value == null) {
            return "";
        } else if (Element.class.isAssignableFrom(value.getClass())) {
            realType = Element.class;
        } else if (type == null) {
            realType = value.getClass();
        } else if (Element.class.isAssignableFrom(type)) {
            realType = Element.class;
        } else if (SmAttribute.class.isAssignableFrom(type)) {
            realType = SmAttribute.class;
        } else if (SmDependency.class.isAssignableFrom(type)) {
            realType = SmAttribute.class;
        } else {
            realType = type;
        }
        
        switch (realType.getSimpleName()) {
        case "Boolean":
            return Boolean.toString((Boolean) value);
        case "Enum":
            return ((Enum<?>) value).name();
        case "Integer":
            return value.toString();
        case "String":
            return value.toString();
        case "Date":
            return String.valueOf(((Date) value).getTime());
        case "Element":
            return ((Element)value).getName();
        case "Float":
            return value.toString();
        case "SmAttribute":
            return ((SmAttribute) value).getName();
        case "SmDependency":
            return ((SmDependency) value).getName();
        case "Time":
            return String.valueOf(((Date) value).getTime());
        default:
            return "?" + value.getClass().getSimpleName() + "?";
        }
    }

}
