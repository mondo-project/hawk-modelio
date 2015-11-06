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
                                    

package org.modelio.diagram.elements.core.link;

import java.util.HashMap;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gef.requests.CreationFactory;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Creation factory for link creations in the diagram.
 * <p>
 * The context contains:
 * <ul>
 * <li>the metaclass of the link to create,
 * <li>an optional existing link to unmask instead of creating it.
 * <li>an optional stereotype to apply to the new link element.
 * <li>the new link connection routing mode.
 * </ul>
 * <p>
 * The factory returns itself in {@link #getNewObject()}. Used by DefaultCreateLinkElementCommand.
 * 
 * @see DefaultCreateLinkCommand
 */
@objid ("802a47f0-1dec-11e2-8cad-001ec947c8cc")
public class ModelioLinkCreationContext implements CreationFactory {
    @objid ("91d4bebd-1e83-11e2-8cad-001ec947c8cc")
    private String metaclass;

    /**
     * An optional stereotype to apply to the new element.
     */
    @objid ("802a47f5-1dec-11e2-8cad-001ec947c8cc")
    private Stereotype stereotype;

    /**
     * A custom properties map.
     */
    @objid ("802a47f7-1dec-11e2-8cad-001ec947c8cc")
    private Map<String, Object> properties = new HashMap<>();

    @objid ("802a47fc-1dec-11e2-8cad-001ec947c8cc")
    private MObject elementToUnmask;

    @objid ("802a47fd-1dec-11e2-8cad-001ec947c8cc")
    private StyleKey defaultRoutingMode;

    /**
     * Create a creation context
     * @param metaclass Metaclass of the element to create
     * @param obstereotype an optional stereotype
     */
    @objid ("802caa13-1dec-11e2-8cad-001ec947c8cc")
    public ModelioLinkCreationContext(String metaclass, Stereotype obstereotype) {
        this.metaclass = metaclass;
        this.stereotype = obstereotype;
    }

    /**
     * Creates a {@link ModelioLinkCreationContext} that unmask an already existing MObject in the diagram.
     * @param elementToUnmask The element to unmask
     */
    @objid ("802caa18-1dec-11e2-8cad-001ec947c8cc")
    public ModelioLinkCreationContext(MObject elementToUnmask) {
        this.elementToUnmask = elementToUnmask;
        this.metaclass = elementToUnmask.getMClass().getName();
        if (elementToUnmask instanceof ModelElement) {
            final EList<Stereotype> extension = ((ModelElement) elementToUnmask).getExtension();
            if (!extension.isEmpty())
                this.stereotype = extension.get(0);
        }
    }

    /**
     * Get the element to unmask.
     * <p>
     * If <code>null</code>, the element has to be created.
     * @return The element to unmask.
     */
    @objid ("802caa1c-1dec-11e2-8cad-001ec947c8cc")
    public MObject getElementToUnmask() {
        return this.elementToUnmask;
    }

    /**
     * @return the metaclass of the element to create.
     */
    @objid ("802caa21-1dec-11e2-8cad-001ec947c8cc")
    public String getMetaclass() {
        return this.metaclass;
    }

    @objid ("802caa26-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object getNewObject() {
        return this;
    }

    @objid ("802caa2b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object getObjectType() {
        return this.metaclass;
    }

    /**
     * Get the creation custom properties.
     * @return the creation properties or <tt>null</tt> if no property was defined.
     */
    @objid ("802caa30-1dec-11e2-8cad-001ec947c8cc")
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    /**
     * Get the stereotype to apply, may be <tt>null</tt>.
     * @return the stereotype to apply, may be <tt>null</tt>.
     */
    @objid ("802caa38-1dec-11e2-8cad-001ec947c8cc")
    public Stereotype getStereotype() {
        return this.stereotype;
    }

    /**
     * Set the creation properties.
     * @param properties the creation properties.
     */
    @objid ("802caa3d-1dec-11e2-8cad-001ec947c8cc")
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Add a custom property value.
     * @param key The property key
     * @param value The property value.
     */
    @objid ("802caa44-1dec-11e2-8cad-001ec947c8cc")
    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    /**
     * Set the style key used to get the default connection routing mode for this link.
     * @param defaultRoutingModeKey the connection routing mode style key.
     */
    @objid ("802caa49-1dec-11e2-8cad-001ec947c8cc")
    public void setRoutingModeKey(final StyleKey defaultRoutingModeKey) {
        this.defaultRoutingMode = defaultRoutingModeKey;
    }

    /**
     * Get the style key used to get the default connection routing mode for this link.
     * @return the connection routing mode style key.
     */
    @objid ("802caa4e-1dec-11e2-8cad-001ec947c8cc")
    public StyleKey getDefaultRoutingModeKey() {
        return this.defaultRoutingMode;
    }

}
