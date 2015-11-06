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
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.modelio.diagram.elements.core.figures.routers.RakeConstraint;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;

/**
 * Factory to create connection helper.
 * 
 * @author cmarin
 */
@objid ("80448198-1dec-11e2-8cad-001ec947c8cc")
public final class ConnectionHelperFactory {
    /**
     * Create a new connection helper from the given raw path data.
     * @param rawData the data given by the creation tool in absolute coordinates.
     * @param connection the connection for which to create a helper.
     * @return the new connection helper.
     */
    @objid ("8044819a-1dec-11e2-8cad-001ec947c8cc")
    public static IConnectionHelper createFromRawData(final RawPathData rawData, final Connection connection) {
        switch (rawData.getRoutingMode()) {
            case DIRECT:
                return new DirectConnectionHelper(connection);
            case BENDPOINT:
                return new ObliqueConnectionHelper(rawData, connection);
            case ORTHOGONAL: {
                return new OrthoConnectionHelper(rawData, connection);
            }
            //case RAKE:
            //    return new RakeConnectionHelper(rawData, connection);
        
            default:
                throw new IllegalArgumentException(rawData.getRoutingMode() + " is unknown");
        }
    }

    /**
     * Convert a path to another routing mode.
     * @param toConvert the path to convert.
     * @param mode the new mode
     * @param connection the connection for which to create a helper.
     * @return a converted path.
     */
    @objid ("8046e3f1-1dec-11e2-8cad-001ec947c8cc")
    public static IConnectionHelper convert(final IConnectionHelper toConvert, final ConnectionRouterId mode, final Connection connection) {
        // TODO: write more useful conversion algorithms.
        switch (mode) {
            case DIRECT:
                return new DirectConnectionHelper(connection);
            case BENDPOINT:
                return new ObliqueConnectionHelper(new ArrayList<Point>(0), connection);
            case ORTHOGONAL:
                return new OrthoConnectionHelper(new ArrayList<Point>(0), connection);
        
            default:
                throw new IllegalArgumentException(mode + " is unknown");
        }
    }

    /**
     * Create a new connection helper from the given serialized data.
     * @param router The connection router to use
     * @param serializedData the serialized data.
     * @param connection the connection for which to create a helper.
     * @return a connection helper.
     */
    @objid ("8046e3fd-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    public static IConnectionHelper createFromSerializedData(final ConnectionRouterId router, final Object serializedData, final Connection connection) {
        if (serializedData instanceof RakeConstraint)
            return new RakeConnectionHelper(serializedData, connection);
        
        switch (router) {
            case DIRECT:
                return new DirectConnectionHelper(connection);
            case BENDPOINT:
                return new ObliqueConnectionHelper((List<Point>) serializedData, connection);
            case ORTHOGONAL:
                return new OrthoConnectionHelper((List<Point>) serializedData, connection);
        
            default:
                throw new IllegalArgumentException(router + " is unknown");
        }
    }

    /**
     * Create a new connection helper from the given routing constraint.
     * @param router The connection router to use.
     * @param routingConstraint the routing constraint.
     * @param connection the connection for which to create a helper.
     * @return a connection helper.
     */
    @objid ("8046e40a-1dec-11e2-8cad-001ec947c8cc")
    public static IConnectionHelper createFromRoutingConstraint(final ConnectionRouterId router, final Object routingConstraint, final Connection connection) {
        if (routingConstraint instanceof RakeConstraint)
            return new RakeConnectionHelper(routingConstraint, connection);
        
        switch (router) {
            case DIRECT:
                return new DirectConnectionHelper(connection);
            case BENDPOINT:
                return ObliqueConnectionHelper.createFromRoutingConstraint(routingConstraint, connection);
            case ORTHOGONAL:
                return OrthoConnectionHelper.createFromRoutingConstraint(routingConstraint, connection);
        
            default:
                throw new IllegalArgumentException(router + " is unknown");
        }
    }

}
