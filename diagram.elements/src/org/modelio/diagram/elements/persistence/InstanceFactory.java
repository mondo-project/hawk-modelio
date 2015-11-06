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
                                    

package org.modelio.diagram.elements.persistence;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.gmfactory.GmNodeFactory;
import org.modelio.diagram.persistence.IDiagramReader;
import org.modelio.diagram.persistence.IInstanceFactory;
import org.modelio.diagram.persistence.PersistenceException;
import org.modelio.diagram.styles.core.FactoryStyle;
import org.modelio.diagram.styles.core.NamedStyle;
import org.modelio.diagram.styles.core.Style;

/**
 * Implementation of {@link IInstanceFactory} to read diagrams.
 * 
 * @author cmarin
 */
@objid ("810a64ce-1dec-11e2-8cad-001ec947c8cc")
public class InstanceFactory implements IInstanceFactory {
    @objid ("810a64d0-1dec-11e2-8cad-001ec947c8cc")
    private static Class<?> getClass(String elementType) throws PersistenceException {
        // Get the java class
        Class<?> clazz = null;
        try {
            clazz = Class.forName(elementType);
        } catch (ClassNotFoundException e) {
            // Call registered node factories.
            clazz = GmNodeFactory.getInstance().getClass(elementType);
            if (clazz != null)
                return clazz;
        
            throw new PersistenceException(elementType + " class cannot be found.", e);
        }
        return clazz;
    }

    @objid ("810a64d7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object createInstance(String elementType, IDiagramReader in) throws PersistenceException {
        Class<?> clazz = getClass(elementType);
        
        if (clazz == FactoryStyle.class) {
            return FactoryStyle.getInstance();
        } else if (clazz == NamedStyle.class) {
            throw new IllegalArgumentException("NamedStyle not supported: should be an external reference.");
        } else if (clazz == Style.class) {
            return new Style(FactoryStyle.getInstance());
        } else {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                throw new PersistenceException(e);
            } catch (IllegalAccessException e) {
                throw new PersistenceException(e);
            }
        }
    }

    @objid ("810a64dd-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    @Override
    public <T extends  Enum<T>> Class<T> getEnumClass(String enumType) {
        // TODO It's bad to ask the GmNodeFactory for that
        return (Class<T>) getClass(enumType);
    }

}
