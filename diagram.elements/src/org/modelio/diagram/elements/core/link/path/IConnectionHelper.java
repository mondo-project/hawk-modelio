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
                                    

package org.modelio.diagram.elements.core.link.path;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Point;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;

/**
 * Interface for a connection path.
 * 
 * @author cmarin
 */
@objid ("80494660-1dec-11e2-8cad-001ec947c8cc")
public interface IConnectionHelper {
    /**
     * Updates this path from the given connection creation raw data.
     * @param req The connection creation raw data (in absolute coordinates).
     */
    @objid ("80494662-1dec-11e2-8cad-001ec947c8cc")
    void updateFrom(final RawPathData req);

    /**
     * Get all bend points without extremity points.
     * @return the bend points in coordinates relative to the connection..
     */
    @objid ("80494666-1dec-11e2-8cad-001ec947c8cc")
    List<Point> getBendPoints();

    /**
     * Get the path routing mode.
     * @return the path routing mode.
     */
    @objid ("8049466d-1dec-11e2-8cad-001ec947c8cc")
    ConnectionRouterId getRoutingMode();

    /**
     * Get the draw2d routing constraint to apply to the connection figure.
     * @return the draw2d routing constraint.
     */
    @objid ("80494670-1dec-11e2-8cad-001ec947c8cc")
    Object getRoutingConstraint();

}
