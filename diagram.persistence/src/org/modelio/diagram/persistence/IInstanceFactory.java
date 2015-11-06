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
                                    

package org.modelio.diagram.persistence;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * Instance factory used by {@link IDiagramReader} implementations in order to create instances of read persistent
 * elements.
 * 
 * @author cmarin
 */
@objid ("cb6de7ab-186f-11e2-92d2-001ec947c8cc")
public interface IInstanceFactory {
    /**
     * Return an instance of the given class for the given diagram reader in its current state. {@link IDiagramReader
     * readXxxxAtt(String)} methods can be called to get more informations.
     * <p>
     * This method may create a new instance or return an existing one.
     * @param nodeType The element type. This class must have an accessible default (no parameter) constructor.
     * @param in The diagram reader.
     * @return The created instance.
     * 
     * @exception PersistenceException
     * in case of failure to create an instance.
     */
    @objid ("cb7049a6-186f-11e2-92d2-001ec947c8cc")
    Object createInstance(String nodeType, IDiagramReader in) throws PersistenceException;

    /**
     * Get the enumeration class of the given java name.
     * @param enumType The java name of an enumeration class
     * @return The enumeration class
     */
    @objid ("cb7049ab-186f-11e2-92d2-001ec947c8cc")
    <T extends  Enum<T>> Class<T> getEnumClass(String enumType);

}
