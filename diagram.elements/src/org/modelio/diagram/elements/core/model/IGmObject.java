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
                                    

package org.modelio.diagram.elements.core.model;

import java.beans.PropertyChangeListener;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.persistence.IPersistent;
import org.modelio.diagram.styles.core.IStyleProvider;

/**
 * Interface that all graphic model displayed in a diagram must implement.
 * <p>
 * All graphic elements
 * 
 * @author cmarin
 */
@objid ("80827eed-1dec-11e2-8cad-001ec947c8cc")
public interface IGmObject extends IStyleProvider, IPersistent {
    /**
     * Property change event name, indicates the layout data changed.
     */
    @objid ("92093273-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_LAYOUTDATA = "LayoutData";

    /**
     * Property change event name, indicates the displayed label changed.
     */
    @objid ("92093279-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_LABEL = "Name";

    /**
     * Property change event name, indicates that links were added or removed to a node, or that source or destination
     * were modified on a link.
     */
    @objid ("920b94d0-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_LINK_SOURCE = "Link source";

    /**
     * Property change event name, indicates that links were added or removed to a node, or that source or destination
     * were modified on a link.
     */
    @objid ("920df72a-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_LINK_TARGET = "Link target";

    /**
     * Property change event name, indicates that children nodes have been added or removed.
     */
    @objid ("920df730-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_CHILDREN = "Children";

    /**
     * Property change event name, indicates that the element style changed.
     */
    @objid ("920df736-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_STYLE = "Style";

    /**
     * Property change event name, indicates that the gm has been deleted.
     */
    @objid ("92105984-1e83-11e2-8cad-001ec947c8cc")
    public static final String PROPERTY_DELETE = "Delete";

    /**
     * Add a listener that is fired when a graphic model property change.
     * @param listener a property change listener.
     */
    @objid ("8084e11a-1dec-11e2-8cad-001ec947c8cc")
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Get the diagram containing this element.
     * @return the diagram.
     */
    @objid ("8084e11d-1dec-11e2-8cad-001ec947c8cc")
    GmAbstractDiagram getDiagram();

    /**
     * Get the data used by the parent node layout manager to set this element position and size.
     * <p>
     * Usually only the parent node layout manager has an idea of the expected type of the layout data.
     * @return The element layout data. May be <tt>null</tt>
     */
    @objid ("8084e120-1dec-11e2-8cad-001ec947c8cc")
    Object getLayoutData();

    /**
     * Remove a model change listener.
     * @param listener a property change listener.
     */
    @objid ("8084e123-1dec-11e2-8cad-001ec947c8cc")
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Set the data used by the parent node layout manager to set this element position and size.
     * @param layoutData The element layout data. May be <tt>null</tt>
     */
    @objid ("8084e126-1dec-11e2-8cad-001ec947c8cc")
    void setLayoutData(Object layoutData);

    /**
     * Delete this Gm.
     */
    @objid ("8084e129-1dec-11e2-8cad-001ec947c8cc")
    void delete();

    /**
     * Return true if the Gm can be edited.
     * Usually, the result is equivalent to the editable status of the owner diagram.
     * @return true if the Gm can be edited.
     */
    @objid ("8084e12b-1dec-11e2-8cad-001ec947c8cc")
    boolean isEditable();

}
