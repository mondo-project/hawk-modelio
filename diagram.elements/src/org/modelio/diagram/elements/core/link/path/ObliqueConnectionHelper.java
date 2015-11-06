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

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;

/**
 * Path for connections in oblique/bendpoint mode.
 * 
 * @author cmarin
 */
@objid ("80494675-1dec-11e2-8cad-001ec947c8cc")
public class ObliqueConnectionHelper implements IConnectionHelper {
    @objid ("48640c96-d892-420a-b162-5d0457ed8510")
    private Connection connection;

    @objid ("67bd0702-1f19-4ccd-80ec-f2764126084a")
    private List<Point> bendPoints = new ArrayList<>();

    /**
     * Get all bend points without extremity points.
     * @return the bend points.
     */
    @objid ("8049467f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<Point> getBendPoints() {
        return this.bendPoints;
    }

    @objid ("80494689-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void updateFrom(final RawPathData rawData) {
        this.bendPoints.clear();
        readRawPoints(rawData);
    }

    /**
     * Get the path routing mode.
     * @return the path routing mode.
     */
    @objid ("8049468e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionRouterId getRoutingMode() {
        return ConnectionRouterId.BENDPOINT;
    }

    /**
     * @param rawData the raw data provided by the tool (expressed in absolute coordinates)
     * @param connection the connection for which this helper is created.
     */
    @objid ("804ba8a6-1dec-11e2-8cad-001ec947c8cc")
    public ObliqueConnectionHelper(final RawPathData rawData, final Connection connection) {
        this.connection = connection;
        readRawPoints(rawData);
    }

    /**
     * constructor from a list of points (in coordinates relative to the connection).
     * @param points the list of point.
     * @param connection the connection for which this helper is created.
     */
    @objid ("804ba8af-1dec-11e2-8cad-001ec947c8cc")
    public ObliqueConnectionHelper(final List<Point> points, final Connection connection) {
        this.connection = connection;
        this.bendPoints = points;
    }

    /**
     * Get the draw2d routing constraint to apply to the connection figure.
     * @return the draw2d routing constraint.
     */
    @objid ("804ba8bc-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object getRoutingConstraint() {
        List<Bendpoint> ret = new ArrayList<>(this.bendPoints.size());
        
        for (Point p : this.bendPoints)
            ret.add(new AbsoluteBendpoint(p));
        return ret;
    }

    @objid ("804ba8c2-1dec-11e2-8cad-001ec947c8cc")
    private void readRawPoints(final RawPathData rawData) {
        // Raw data is expressed in absolute coordinates
        // Translate to coordinates to the connection.
        final Point tmp = Point.SINGLETON;
        for (Point absolutePoint : rawData.getPath()) {
            tmp.setLocation(absolutePoint);
            this.connection.translateToRelative(tmp);
            this.bendPoints.add(tmp.getCopy());
        }
    }

    /**
     * Factory method to build an instance from a routing constraint.
     * @param routingConstraint the routing constraint.
     * @param connection the connection to build a helper for.
     * @return a new connection helper.
     */
    @objid ("804ba8c6-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    public static ObliqueConnectionHelper createFromRoutingConstraint(final Object routingConstraint, final Connection connection) {
        // For an Oblique router, the routing constraint is a list of Bendpoint.
        List<Bendpoint> bendpoints = (List<Bendpoint>) routingConstraint;
        // Get each bend point location and build a list of Point from it. 
        List<Point> points = new ArrayList<>(bendpoints.size());
        for (Bendpoint bendpoint : bendpoints) {
            points.add(new Point(bendpoint.getLocation()));
        }
        // Create the instance.
        return new ObliqueConnectionHelper(points, connection);
    }

}
