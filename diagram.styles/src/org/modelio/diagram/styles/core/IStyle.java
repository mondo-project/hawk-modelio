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

import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * The style contains many properties such has the foreground and background color, the font and some display options.
 * <p>
 * A property value can be read with the {@link #getProperty(StyleKey)} method or one of the convenience accessors.<br>
 * A property value may be set with the {@link #setProperty(StyleKey, Object)} method.
 * <p>
 * These properties are intended to be displayed and edited in a properties tab.
 * 
 * @author pvlaemyn
 */
@objid ("8551246b-1926-11e2-92d2-001ec947c8cc")
public interface IStyle {
    /**
     * Get a style property
     * @param <T>
     * The wanted property value type .
     * @param propertyKey The property key
     * @return The property value
     */
    @objid ("8551246d-1926-11e2-92d2-001ec947c8cc")
    <T> T getProperty(StyleKey propertyKey);

    /**
     * Change a style property and fires the style listeners.
     * @param key The property key.
     * @param value The new value.
     */
    @objid ("85512472-1926-11e2-92d2-001ec947c8cc")
    void setProperty(StyleKey key, Object value);

    /**
     * Remove a property value and fires style changes listeners.
     * @param key The property to remove
     */
    @objid ("85538692-1926-11e2-92d2-001ec947c8cc")
    void removeProperty(StyleKey key);

    /**
     * Convenience method to get a Color property.
     * @param propertyKey The property key
     * @return The Color value.
     */
    @objid ("85538695-1926-11e2-92d2-001ec947c8cc")
    Color getColor(StyleKey propertyKey);

    /**
     * Convenience method to get a boolean property.
     * @param propertyKey The property key
     * @return The boolean value.
     */
    @objid ("85538699-1926-11e2-92d2-001ec947c8cc")
    boolean getBoolean(StyleKey propertyKey);

    /**
     * Convenience method to get a Font property.
     * @param propertyKey The property key
     * @return The Font value.
     */
    @objid ("8553869d-1926-11e2-92d2-001ec947c8cc")
    Font getFont(StyleKey propertyKey);

    /**
     * Convenience method to get an integer property.
     * @param propertyKey The property key
     * @return The integer value.
     */
    @objid ("855386a1-1926-11e2-92d2-001ec947c8cc")
    int getInteger(StyleKey propertyKey);

    /**
     * Set the parent style used to get a property value when it is not defined on this style.
     * @param style The new parent style.
     */
    @objid ("855386a5-1926-11e2-92d2-001ec947c8cc")
    void setCascadedStyle(IStyle style);

    /**
     * Register a style change listener.
     * <p>
     * The listener will be fired each time a property is changed or removed.<br>
     * Registering 2 times a listener will make it fired 2 times.
     * @param l The style change listener.
     */
    @objid ("855386a8-1926-11e2-92d2-001ec947c8cc")
    void addListener(IStyleChangeListener l);

    /**
     * Remove a style change listener.
     * @param l a style change listener to remove.
     */
    @objid ("855386ab-1926-11e2-92d2-001ec947c8cc")
    void removeListener(IStyleChangeListener l);

    /**
     * @param propertyKey
     * @return true if a local value is defined for the key
     */
    @objid ("855386ae-1926-11e2-92d2-001ec947c8cc")
    boolean isLocal(StyleKey propertyKey);

    /**
     * Clean all the local values (removing them from the properties map).<br>
     * Does not modify the cascaded style
     */
    @objid ("855386b2-1926-11e2-92d2-001ec947c8cc")
    void reset();

    /**
     * Get the style where property values are looked for when not defined on this style.
     * @return The cascaded style.
     */
    @objid ("855386b4-1926-11e2-92d2-001ec947c8cc")
    IStyle getCascadedStyle();

    /**
     * Get all style keys for which a value has been locally set.
     * @return locally defined style keys.
     */
    @objid ("855386b7-1926-11e2-92d2-001ec947c8cc")
    Set<StyleKey> getLocalKeys();

    /**
     * Normalizing a style consists in removing from its local definitions the values that are currently the same as the
     * value in cascaded style.
     */
    @objid ("8555e8eb-1926-11e2-92d2-001ec947c8cc")
    void normalize();

}
