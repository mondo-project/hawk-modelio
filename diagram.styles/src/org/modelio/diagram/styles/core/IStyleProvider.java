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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Interface for all graphic elements that support styles.
 * 
 * @author cmarin
 */
@objid ("8555e8f6-1926-11e2-92d2-001ec947c8cc")
public interface IStyleProvider {
    /**
     * Get the element style.
     * @return the element style.
     */
    @objid ("8555e8f8-1926-11e2-92d2-001ec947c8cc")
    IStyle getStyle();

    /**
     * Get all style keys the element supports.
     * @return style keys supported by the element.
     */
    @objid ("8555e8fb-1926-11e2-92d2-001ec947c8cc")
    List<StyleKey> getStyleKeys();

    /**
     * Get the style key corresponding to the given meta key.
     * @param metakey a meta key
     * @return the corresponding style key or null if none maps.
     */
    @objid ("8555e900-1926-11e2-92d2-001ec947c8cc")
    StyleKey getStyleKey(MetaKey metakey);

}
