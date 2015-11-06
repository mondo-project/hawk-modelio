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
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;
import org.modelio.diagram.elements.core.figures.geometry.Direction;
import org.modelio.diagram.elements.core.figures.geometry.GeomUtils;
import org.modelio.diagram.elements.core.figures.geometry.Orientation;
import org.modelio.diagram.elements.core.figures.routers.OrthogonalRouter;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;

/**
 * Helper class for Orthogonal routing mode.
 * 
 * @author cmarin
 */
@objid ("804ba8d7-1dec-11e2-8cad-001ec947c8cc")
public class OrthoConnectionHelper implements IConnectionHelper {
    @objid ("804ba8e1-1dec-11e2-8cad-001ec947c8cc")
    private static final int REFLEXIVE_OFFSET = 20;

    @objid ("64ec337b-1e83-11e2-8cad-001ec947c8cc")
    private List<Point> bendPoints = new ArrayList<>();

    @objid ("64ec337e-1e83-11e2-8cad-001ec947c8cc")
    private Connection connection;

    /**
     * Factory method to build an instance from a routing constraint.
     * @param routingConstraint the routing constraint.
     * @param connection the connection to build a helper for.
     * @return a new connection helper.
     */
    @objid ("804ba8e3-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    public static OrthoConnectionHelper createFromRoutingConstraint(final Object routingConstraint, final Connection connection) {
        // For an Orthogonal router, the routing constraint is a list of Bendpoint.
        List<Bendpoint> bendpoints = (List<Bendpoint>) routingConstraint;
        // Get each bend point location and build a list of Point from it. 
        List<Point> points = new ArrayList<>(bendpoints.size());
        for (Bendpoint bendpoint : bendpoints) {
            points.add(new Point(bendpoint.getLocation()));
        }
        // Create the instance.
        return new OrthoConnectionHelper(points, connection);
    }

    /**
     * Constructor a connection and its existing points.
     * @param connection the connection for which this helper is created.
     */
    @objid ("804e0aff-1dec-11e2-8cad-001ec947c8cc")
    public OrthoConnectionHelper(final Connection connection) {
        this.connection = connection;
        
        PointList l = connection.getPoints();
        this.bendPoints = new ArrayList<>(l.size());
        for (int i = 0; i < l.size(); i++) {
            this.bendPoints.add(l.getPoint(i));
        }
    }

    /**
     * Builds a set of routing constraints for an {@link OrthogonalRouter} from the RawData given by a link creation
     * tool.
     * @param rawData the RawData to build from.
     * @param connection the connection for which this helper is created.
     */
    @objid ("804e0b06-1dec-11e2-8cad-001ec947c8cc")
    public OrthoConnectionHelper(final RawPathData rawData, final Connection connection) {
        this.connection = connection;
        ConnectionAnchor sourceAnchor = connection.getSourceAnchor();
        IFigure sourceFigure = sourceAnchor.getOwner();
        ConnectionAnchor targetAnchor = connection.getTargetAnchor();
        IFigure targetFigure = targetAnchor.getOwner();
        if (sourceFigure != null && sourceFigure.equals(targetFigure) && rawData.getPath().isEmpty()) {
            // Reflexive case, add whatever points are necessary 
            RawPathData reflexivePath = new RawPathData();
            reflexivePath.setSrcPoint(rawData.getSrcPoint().getCopy());
            reflexivePath.setLastPoint(rawData.getLastPoint());
            reflexivePath.setRoutingMode(rawData.getRoutingMode());
            addReflexivePoints(reflexivePath, sourceAnchor, sourceFigure, targetAnchor);
            readRawPoints(reflexivePath.getPath(), reflexivePath.getLastPoint());
        
        } else {
            readRawPoints(rawData.getPath(), rawData.getLastPoint());
        }
    }

    /**
     * constructor from a list of points (in coordinates relative to the connection).
     * @param points the list of point.
     * @param connection the connection for which this helper is created.
     */
    @objid ("804e0b0f-1dec-11e2-8cad-001ec947c8cc")
    public OrthoConnectionHelper(final List<Point> points, final Connection connection) {
        this.connection = connection;
        this.bendPoints = points;
    }

    @objid ("804e0b1c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<Point> getBendPoints() {
        return this.bendPoints;
    }

    @objid ("804e0b25-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public List<Bendpoint> getRoutingConstraint() {
        List<Bendpoint> ret = new ArrayList<>(this.bendPoints.size());
        
        for (Point p : this.bendPoints)
            ret.add(new AbsoluteBendpoint(p.x, p.y));
        return ret;
    }

    @objid ("804e0b2e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionRouterId getRoutingMode() {
        return ConnectionRouterId.ORTHOGONAL;
    }

    @objid ("804e0b33-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void updateFrom(final RawPathData req) {
        // TODO: update rather than read all!
        this.bendPoints.clear();
        ConnectionAnchor sourceAnchor = this.connection.getSourceAnchor();
        IFigure sourceFigure = sourceAnchor.getOwner();
        ConnectionAnchor targetAnchor = this.connection.getTargetAnchor();
        IFigure targetFigure = targetAnchor.getOwner();
        if (sourceFigure != null && sourceFigure.equals(targetFigure) && req.getPath().isEmpty()) {
            // Reflexive case, add whatever points are necessary 
            RawPathData reflexivePath = new RawPathData();
            reflexivePath.setSrcPoint(req.getSrcPoint().getCopy());
            reflexivePath.setLastPoint(req.getLastPoint());
            reflexivePath.setRoutingMode(req.getRoutingMode());
            addReflexivePoints(reflexivePath, sourceAnchor, sourceFigure, targetAnchor);
            readRawPoints(reflexivePath.getPath(), reflexivePath.getLastPoint());
        
        } else {
            readRawPoints(req.getPath(), req.getLastPoint());
        }
    }

    /**
     * Get the anchor owner (handle)bounds in absolute coordinates. If the anchor is not attached to a figure, returns a
     * 1x1 sized rectangle located at the anchor reference point.
     * @param anchor The anchor.
     * @return The anchor owner bounds.
     */
    @objid ("804e0b38-1dec-11e2-8cad-001ec947c8cc")
    private Rectangle getAnchorOwnerAbsoluteBounds(final ConnectionAnchor anchor) {
        final IFigure f = anchor.getOwner();
        if (f == null) {
            Point p = anchor.getReferencePoint();
            return new Rectangle(p.x, p.y, 1, 1);
        } else {
            final Rectangle bounds = f instanceof HandleBounds ? ((HandleBounds) f).getHandleBounds()
                                                                                   .getCopy() : f.getBounds()
                                                                                                 .getCopy();
            f.translateToAbsolute(bounds);
        
            return bounds;
        }
    }

    @objid ("804e0b42-1dec-11e2-8cad-001ec947c8cc")
    private void readRawPoints(final List<Point> path, final Point lastPoint) {
        // Go through the list of point and try to make an orthogonal path from it.
        ConnectionAnchor sourceAnchor = this.connection.getSourceAnchor();
        ConnectionAnchor targetAnchor = this.connection.getTargetAnchor();
        
        // Start by determining the exact position of the source anchor (in absolute coordinates).
        Point lastReferencePoint = targetAnchor != null ? targetAnchor.getReferencePoint() : lastPoint;
        Point tmpPoint = Point.SINGLETON;
        if (path.size() > 0) {
            tmpPoint.setLocation(path.get(0));
            this.connection.translateToAbsolute(tmpPoint);
        } else {
            tmpPoint.setLocation(lastReferencePoint);
        }
        Point sourceLocation = sourceAnchor.getLocation(tmpPoint);
        // Now get the bounds of its owning figure (in absolute coordinates too)
        final Rectangle sourceBounds = getAnchorOwnerAbsoluteBounds(sourceAnchor);
        // Given the two datas, compute the orientation of the initial segment.
        Direction sourceAnchorOrientation = GeomUtils.getDirection(sourceLocation, sourceBounds);
        Orientation currentOrientation = GeomUtils.getOrientation(sourceAnchorOrientation);
        
        if (path.size() > 0) {
            Point previousPoint = sourceLocation;
        
            for (int i = 0; i < path.size(); ++i) {
                Point currentPoint = path.get(i);
                if (currentOrientation == Orientation.HORIZONTAL) {
                    tmpPoint.setLocation(currentPoint.x, previousPoint.y);
                    currentOrientation = Orientation.VERTICAL;
                } else {
                    tmpPoint.setLocation(previousPoint.x, currentPoint.y);
                    currentOrientation = Orientation.HORIZONTAL;
                }
                this.connection.translateToRelative(tmpPoint);
                this.bendPoints.add(tmpPoint.getCopy());
                this.connection.translateToAbsolute(tmpPoint);
                previousPoint.setLocation(tmpPoint);
            }
        //            // Take last point/target position into account
        //            Point currentPoint = targetAnchor != null ? targetAnchor.getReferencePoint() : lastPoint;
        //            if (currentOrientation == Orientation.HORIZONTAL) {
        //                tmpPoint.setLocation(currentPoint.x, previousPoint.y);
        //            } else {
        //                tmpPoint.setLocation(previousPoint.x, currentPoint.y);
        //            }
        //            this.connection.translateToRelative(tmpPoint);
        //            this.bendPoints.add(tmpPoint.getCopy());
        
        }
    }

    @objid ("804e0b4e-1dec-11e2-8cad-001ec947c8cc")
    private void addReflexivePoints(final RawPathData rawData, final ConnectionAnchor sourceAnchor, final IFigure sourceFigure, final ConnectionAnchor targetAnchor) {
        Rectangle sourceAbsoluteBounds = new PrecisionRectangle(sourceFigure.getBounds());
        sourceFigure.translateToAbsolute(sourceAbsoluteBounds);
        int right = sourceAbsoluteBounds.right() + OrthoConnectionHelper.REFLEXIVE_OFFSET;
        int bottom = sourceAbsoluteBounds.bottom() + OrthoConnectionHelper.REFLEXIVE_OFFSET;
        int left = sourceAbsoluteBounds.x() - OrthoConnectionHelper.REFLEXIVE_OFFSET;
        int top = sourceAbsoluteBounds.y() - OrthoConnectionHelper.REFLEXIVE_OFFSET;
        Point center = sourceAbsoluteBounds.getCenter();
        
        Point sourceRef = sourceAnchor.getReferencePoint();
        Point targetRef = targetAnchor.getReferencePoint();
        
        Direction sourceDirection = GeomUtils.getDirection(sourceRef, sourceAbsoluteBounds);
        Direction targetDirection = GeomUtils.getDirection(targetRef, sourceAbsoluteBounds);
        
        List<Point> path = rawData.getPath();
        switch (sourceDirection) {
            case EAST: {
                switch (targetDirection) {
                    case EAST: {
                        path.add(new PrecisionPoint(right, sourceRef.y()));
                        path.add(new PrecisionPoint(right, targetRef.y()));
                        break;
                    }
                    case SOUTH: {
                        path.add(new PrecisionPoint(right, sourceRef.y()));
                        path.add(new PrecisionPoint(right, bottom));
                        path.add(new PrecisionPoint(targetRef.x(), bottom));
                        break;
                    }
                    case WEST: {
                        path.add(new PrecisionPoint(right, sourceRef.y()));
                        if (sourceRef.y() <= center.y()) {
                            path.add(new PrecisionPoint(right, top));
                            path.add(new PrecisionPoint(left, top));
        
                        } else {
                            path.add(new PrecisionPoint(right, bottom));
                            path.add(new PrecisionPoint(left, bottom));
                        }
                        path.add(new PrecisionPoint(left, targetRef.y()));
                        break;
                    }
                    case NORTH:
                    case NONE: {
                        path.add(new PrecisionPoint(right, sourceRef.y()));
                        path.add(new PrecisionPoint(right, top));
                        path.add(new PrecisionPoint(targetRef.x(), top));
                        break;
                    }
                }
                break;
            }
            case SOUTH: {
                switch (targetDirection) {
                    case EAST: {
                        path.add(new PrecisionPoint(sourceRef.x(), bottom));
                        path.add(new PrecisionPoint(right, bottom));
                        path.add(new PrecisionPoint(right, targetRef.y()));
                        break;
                    }
                    case SOUTH: {
                        path.add(new PrecisionPoint(sourceRef.x(), bottom));
                        path.add(new PrecisionPoint(targetRef.x(), bottom));
                        break;
                    }
                    case WEST: {
                        path.add(new PrecisionPoint(sourceRef.x(), bottom));
                        path.add(new PrecisionPoint(left, bottom));
                        path.add(new PrecisionPoint(left, targetRef.y()));
                        break;
                    }
                    case NORTH:
                    case NONE: {
                        path.add(new PrecisionPoint(sourceRef.x(), bottom));
                        if (sourceRef.x() < center.x()) {
                            path.add(new PrecisionPoint(left, bottom));
                            path.add(new PrecisionPoint(left, top));
                        } else {
                            path.add(new PrecisionPoint(right, bottom));
                            path.add(new PrecisionPoint(right, top));
                        }
                        path.add(new PrecisionPoint(targetRef.x(), top));
                        break;
                    }
                }
                break;
            }
            case WEST: {
                switch (targetDirection) {
                    case EAST: {
                        path.add(new PrecisionPoint(left, sourceRef.y()));
                        if (sourceRef.y() < center.y()) {
                            path.add(new PrecisionPoint(left, top));
                            path.add(new PrecisionPoint(right, top));
                        } else {
                            path.add(new PrecisionPoint(left, bottom));
                            path.add(new PrecisionPoint(right, bottom));
                        }
                        path.add(new PrecisionPoint(right, targetRef.y()));
                        break;
                    }
                    case SOUTH: {
                        path.add(new PrecisionPoint(left, sourceRef.y()));
                        path.add(new PrecisionPoint(left, bottom));
                        path.add(new PrecisionPoint(targetRef.x(), bottom));
                        break;
                    }
                    case WEST: {
                        path.add(new PrecisionPoint(left, sourceRef.y()));
                        path.add(new PrecisionPoint(left, targetRef.y()));
                        break;
                    }
                    case NORTH:
                    case NONE: {
                        path.add(new PrecisionPoint(left, sourceRef.y()));
                        path.add(new PrecisionPoint(left, top));
                        path.add(new PrecisionPoint(targetRef.x(), top));
                        break;
                    }
                }
                break;
            }
            case NORTH:
            case NONE: {
                switch (targetDirection) {
                    case EAST:
                    case NONE: {
                        path.add(new PrecisionPoint(sourceRef.x(), top));
                        path.add(new PrecisionPoint(right, top));
                        path.add(new PrecisionPoint(right, targetRef.y()));
                        break;
                    }
                    case SOUTH: {
                        path.add(new PrecisionPoint(sourceRef.x(), top));
                        if (sourceRef.x() < center.x()) {
                            path.add(new PrecisionPoint(left, top));
                            path.add(new PrecisionPoint(left, bottom));
                        } else {
                            path.add(new PrecisionPoint(right, top));
                            path.add(new PrecisionPoint(right, bottom));
                        }
                        path.add(new PrecisionPoint(targetRef.x(), bottom));
                        break;
                    }
                    case WEST: {
                        path.add(new PrecisionPoint(sourceRef.x(), top));
                        path.add(new PrecisionPoint(left, top));
                        path.add(new PrecisionPoint(left, targetRef.y()));
                        break;
                    }
                    case NORTH: {
                        path.add(new PrecisionPoint(sourceRef.x(), top));
                        path.add(new PrecisionPoint(targetRef.x(), top));
                        break;
                    }
                }
                break;
            }
        }
    }

}
