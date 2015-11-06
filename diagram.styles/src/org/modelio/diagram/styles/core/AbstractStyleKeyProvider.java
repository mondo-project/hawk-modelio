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
                                    

package org.modelio.diagram.styles.core;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.styles.plugin.DiagramStyles;

/**
 * This class is actually a helper for Gm's to implement their {@link IStyleProvider} behavior.
 * To properly use this helper a Gm must:<ul>
 * <li> create a class GmXXXStyleKeyProvider derived from AbstractStyleKeyProvider
 * <li>create the supported {@link StyleKey} instances in GmXXXStyleKeyProvider
 * (as public {@link StyleKey} instances)
 * <li>create and initialize a static instance of GmXXXStyleKeyProvider in the Gm class
 * <li>implement the {@link IStyleProvider#getStyleKeys() getStyleKeys()}
 * and {@link IStyleProvider#getStyleKey(MetaKey)} methods in the
 * GmXXX by delegating the call to the GmXXXStyleKeyProvider instance</li>
 * </ul>
 * 
 * @author pvlaemyn
 */
@objid ("85479ad6-1926-11e2-92d2-001ec947c8cc")
public abstract class AbstractStyleKeyProvider {
    @objid ("85479ad8-1926-11e2-92d2-001ec947c8cc")
    private ArrayList<StyleKey> allKeys = null;

    /**
     * Get all declared style keys.
     * @return all declared style keys.
     */
    @objid ("85479adb-1926-11e2-92d2-001ec947c8cc")
    public final List<StyleKey> getStyleKeys() {
        if (this.allKeys == null) {
            this.allKeys = new ArrayList<>();
        
            this.scanForStyleKeys(this);
        }
        return this.allKeys;
    }

    /**
     * Get the {@link StyleKey} corresponding to the given {@link MetaKey}.
     * @param metakey The meta key, must not be null.
     * @return the found style key, or null if none found.
     */
    @objid ("85479ae2-1926-11e2-92d2-001ec947c8cc")
    public final StyleKey getStyleKey(MetaKey metakey) {
        for (final StyleKey s : this.getStyleKeys()) {
            if (metakey.equals(s.getMetakey()))
                return s;
        }
        return null;
    }

    /**
     * Find the declared style keys in the class and its inner classes, and add them to the {@link #allKeys} list.
     * @param obj The AbstractStyleKeyProvider object or class to scan
     */
    @objid ("85479ae8-1926-11e2-92d2-001ec947c8cc")
    private void scanForStyleKeys(Object obj) {
        Class<?> aClass = obj instanceof Class ? (Class<?>)obj : obj.getClass();
        final Field[] declaredFields = aClass.getDeclaredFields();
        
        // Allow access to private fields.
        AccessibleObject.setAccessible(declaredFields, true);
        
        // Scan all fields for style keys
        for (final Field field : declaredFields) {
            if (field.getType() == StyleKey.class) {
                StyleKey styleKey;
                try {
                    if (Modifier.isStatic(field.getModifiers())) {
                        styleKey = (StyleKey) field.get(null);
                        this.allKeys.add(styleKey);
                    } else if (obj != aClass) {
                        // obj is an instance, get the instance field value
                        styleKey = (StyleKey) field.get(obj);
                        this.allKeys.add(styleKey);
                    }
                } catch (IllegalArgumentException e) {
                    DiagramStyles.LOG.error(e);
                } catch (IllegalAccessException e) {
                    DiagramStyles.LOG.error(e);
                }
            }
            else if ( AbstractStyleKeyProvider.class.isAssignableFrom(field.getType()) 
                    && !Modifier.isStatic(field.getModifiers())) {
                // instance field to a style key container, scan it
                try {
                    this.scanForStyleKeys(field.get(obj));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    DiagramStyles.LOG.error(e);
                }
            }
        }
        
        for (final Class<?> innerClass : aClass.getClasses()) {
            this.scanForStyleKeys(innerClass);
        }
    }

}
