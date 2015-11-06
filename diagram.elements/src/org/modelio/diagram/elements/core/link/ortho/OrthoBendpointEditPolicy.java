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
                                    

package org.modelio.diagram.elements.core.link.ortho;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.handles.BendpointMoveHandle;
import org.eclipse.gef.requests.BendpointRequest;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.modelio.diagram.elements.core.figures.geometry.Orientation;
import org.modelio.diagram.elements.core.model.IGmLinkObject;
import org.modelio.diagram.elements.core.model.IGmPath;

/**
 * Try 2 at an edit policy for links with and Orthogonal router.
 * 
 * @author fpoyer
 */
@objid ("803895f3-1dec-11e2-8cad-001ec947c8cc")
public class OrthoBendpointEditPolicy extends SelectionHandlesEditPolicy implements PropertyChangeListener {
    @objid ("80389602-1dec-11e2-8cad-001ec947c8cc")
    private static final int TOLERANCE = 7;

    @objid ("8038960a-1dec-11e2-8cad-001ec947c8cc")
    private static final double CONSTANT_FACTOR = 1000.0;

    @objid ("65957a71-1e83-11e2-8cad-001ec947c8cc")
    private static final List<Bendpoint> NULL_CONSTRAINT = new ArrayList<>();

    @objid ("65957a75-1e83-11e2-8cad-001ec947c8cc")
    private List<Bendpoint> originalConstraint;

    @objid ("6597dccb-1e83-11e2-8cad-001ec947c8cc")
    private ConnectionAnchor originalSourceAnchor;

    @objid ("659ca17f-1e83-11e2-8cad-001ec947c8cc")
    private ConnectionAnchor originalTargetAnchor;

    @objid ("65a16633-1e83-11e2-8cad-001ec947c8cc")
    private static final Point p1 = new PrecisionPoint();

    @objid ("65a16635-1e83-11e2-8cad-001ec947c8cc")
    private static final Point p2 = new PrecisionPoint();

    /**
     * <code>activate()</code> is extended to add a listener to the <code>Connection</code> figure.
     */
    @objid ("803af836-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void activate() {
        super.activate();
        getConnection().addPropertyChangeListener(Connection.PROPERTY_POINTS, this);
    }

    /**
     * Creates selection handles for the bendpoints. Explicit (user-defined) bendpoints will have
     * {@link BendpointMoveHandle}s on them. Segments between them will have {@link HorizontalSegmentMoveHandle} or
     * {@link VerticalSegmentMoveHandle} on them.
     */
    @objid ("803af83a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> createSelectionHandles() {
        List<Handle> list = new ArrayList<>();
        ConnectionEditPart connEP = (ConnectionEditPart) getHost();
        PointList points = getConnection().getPoints();
        
        p1.setLocation(points.getPoint(0));
        p2.setLocation(points.getPoint(1));
        Orientation orientation = getSegmentOrientation(p1, p2);
        if (orientation == Orientation.HORIZONTAL) {
            list.add(new HorizontalSegmentMoveHandle(connEP, 0));
        } else if (orientation == Orientation.VERTICAL) {
            list.add(new VerticalSegmentMoveHandle(connEP, 0));
        }
        for (int i = 1; i < points.size() - 1; i++) {
            // Add a bendpoint move handle using orientation of previous segment.
            list.add(new BendPointMoveHandle(connEP, i, orientation));
            p1.setLocation(points.getPoint(i));
            p2.setLocation(points.getPoint(i + 1));
            orientation = getSegmentOrientation(p1, p2);
            if (orientation == Orientation.HORIZONTAL) {
                list.add(new HorizontalSegmentMoveHandle(connEP, i));
            } else if (orientation == Orientation.VERTICAL) {
                list.add(new VerticalSegmentMoveHandle(connEP, i));
            }
        }
        return list;
    }

    /**
     * <code>deactivate()</code> is extended to remove the property change listener on the <code>Connection</code>
     * figure.
     */
    @objid ("803af842-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void deactivate() {
        getConnection().removePropertyChangeListener(Connection.PROPERTY_POINTS, this);
        super.deactivate();
    }

    /**
     * Erases all bendpoint feedback. Since the original <code>Connection</code> figure is used for feedback, we just
     * restore the original constraint that was saved before feedback started to show.
     * @param request the BendpointRequest
     */
    @objid ("803af846-1dec-11e2-8cad-001ec947c8cc")
    protected void eraseConnectionFeedback(final BendpointRequest request) {
        restoreOriginalConstraint();
        this.originalConstraint = null;
        this.originalSourceAnchor = null;
        this.originalTargetAnchor = null;
    }

    @objid ("803af84d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void eraseSourceFeedback(final Request request) {
        if (ConnectionSegmentTracker.REQ_MOVE_SEGMENT.equals(request.getType()) ||
            REQ_MOVE_BENDPOINT.equals(request.getType())) {
            eraseConnectionFeedback((BendpointRequest) request);
        } else if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType())) {
            eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
        }
    }

    /**
     * Factors the Request into either a MOVE, a DELETE, or a CREATE of a bendpoint or a segment.
     */
    @objid ("803af854-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Command getCommand(final Request request) {
        if (ConnectionSegmentTracker.REQ_MOVE_SEGMENT.equals(request.getType())) {
            return getMoveSegmentCommand((BendpointRequest) request);
        }
        if (REQ_MOVE_BENDPOINT.equals(request.getType())) {
        
            return getMoveBendpointCommand((BendpointRequest) request);
        }
        if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType())) {
            return getMoveCommand((ChangeBoundsRequest) request);
        }
        return null;
    }

    /**
     * Convenience method for obtaining the host's <code>Connection</code> figure.
     * @return the Connection figure
     */
    @objid ("803d5a89-1dec-11e2-8cad-001ec947c8cc")
    protected Connection getConnection() {
        return (Connection) ((ConnectionEditPart) getHost()).getFigure();
    }

    /**
     * @param request the request to use to build the command.
     */
    @objid ("803d5a90-1dec-11e2-8cad-001ec947c8cc")
    protected Command getMoveBendpointCommand(final BendpointRequest request) {
        ConnectionEditPart hostEP = (ConnectionEditPart) getHost();
        IGmPath path = ((IGmLinkObject) hostEP.getModel()).getPath();
        return new ChangeLinkRoutingConstraintCommand(path, hostEP);
    }

    /**
     * If the number of bendpoints changes, handles are updated.
     * @see java.beans.PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    @objid ("803d5a9b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // TODO optimize so that handles aren't added constantly.
        if (getHost().getSelected() != EditPart.SELECTED_NONE)
            addSelectionHandles();
    }

    /**
     * Restores the original constraint that was saved before feedback began to show.
     */
    @objid ("803d5aa1-1dec-11e2-8cad-001ec947c8cc")
    protected void restoreOriginalConstraint() {
        if (this.originalConstraint != null) {
            if (this.originalConstraint == NULL_CONSTRAINT)
                getConnection().setRoutingConstraint(null);
            else
                getConnection().setRoutingConstraint(this.originalConstraint);
        }
        getConnection().setSourceAnchor(this.originalSourceAnchor);
        getConnection().setTargetAnchor(this.originalTargetAnchor);
    }

    /**
     * Since the original figure is used for feedback, this method saves the original constraint, so that is can be
     * restored when the feedback is erased. It also builds an exhaustive routing constraint (== 1 bend point for each
     * visual point of the connection, inclusive of the first and last).
     */
    @objid ("803d5aa4-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    protected void saveOriginalConstraint() {
        this.originalConstraint = (List<Bendpoint>) getConnection().getRoutingConstraint();
        if (this.originalConstraint == null)
            this.originalConstraint = NULL_CONSTRAINT;
        this.originalSourceAnchor = getConnection().getSourceAnchor();
        this.originalTargetAnchor = getConnection().getTargetAnchor();
        getConnection().setRoutingConstraint(rebuildRoutingConstraint(getConnection()));
    }

    @objid ("803d5aa8-1dec-11e2-8cad-001ec947c8cc")
    protected void showMoveBendpointFeedback(final BendpointRequest request) {
        // Before modifying the connection, save its original constraint, so as to be able to cancel if needed. 
        if (this.originalConstraint == null) {
            saveOriginalConstraint();
        }
        if (request.getExtendedData().get(Orientation.class) == Orientation.HORIZONTAL) {
            showMoveHorizontalBendpointFeedback(request);
        } else if (request.getExtendedData().get(Orientation.class) == Orientation.VERTICAL) {
            showMoveVerticalBendpointFeedback(request);
        }
    }

    @objid ("803d5aae-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void showSourceFeedback(final Request request) {
        if (REQ_MOVE_BENDPOINT.equals(request.getType()))
            showMoveBendpointFeedback((BendpointRequest) request);
        else if (ConnectionSegmentTracker.REQ_MOVE_SEGMENT.equals(request.getType()))
            showMoveSegmentFeedback((BendpointRequest) request);
        else if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType())) {
            showChangeBoundsFeedback((ChangeBoundsRequest) request);
        }
    }

    /**
     * @param request the request to use to build the command.
     */
    @objid ("803d5ab5-1dec-11e2-8cad-001ec947c8cc")
    private Command getMoveSegmentCommand(final BendpointRequest request) {
        ConnectionEditPart hostEP = (ConnectionEditPart) getHost();
        IGmPath path = ((IGmLinkObject) hostEP.getModel()).getPath();
        return new ChangeLinkRoutingConstraintCommand(path, hostEP);
    }

    @objid ("803d5ac0-1dec-11e2-8cad-001ec947c8cc")
    protected void showMoveSegmentFeedback(final BendpointRequest request) {
        // Before modifying the connection, save its original constraint and anchors so as to be able to cancel if needed.
        if (this.originalConstraint == null) {
            saveOriginalConstraint();
        }
        if (request.getExtendedData().get(Orientation.class) == Orientation.HORIZONTAL) {
            showMoveHorizontalSegmentFeedback(request);
        } else {
            assert (request.getExtendedData().get(Orientation.class) == Orientation.VERTICAL) : "no orientation in data!";
            showMoveVerticalSegmentFeedback(request);
        }
    }

    /**
     * Rebuilds a complete routing constraint for the given connection and returns it.
     * @param connection the connection to rebuild a constraint for.
     */
    @objid ("803d5ac6-1dec-11e2-8cad-001ec947c8cc")
    private List<Bendpoint> rebuildRoutingConstraint(final Connection connection) {
        PointList points = connection.getPoints();
        List<Bendpoint> newConstraint = new ArrayList<>(points.size());
        for (int i = 0; i < points.size(); ++i) {
            Point point = points.getPoint(i);
            newConstraint.add(new AbsoluteBendpoint(point));
        }
        return newConstraint;
    }

    /**
     * Returns the main {@link Orientation} (VERTICAL, HORIZONTAL or NONE) of the segment expressed by the two given
     * points in absolute coordinates. A segment Orientation is:
     * <ul>
     * <li>HORIZONTAL if <code>p1.y == p2.y || abs(p1.x - p2.x)/abs(p1.y - p2.y) > 1</code>,</li>
     * <li>VERTICAL if <code>p1.x == p2.x || abs(p1.y - p2.y)/abs(p1.x - p2.x) > 1</code></li>
     * <li>NONE otherwise (ie: either point is <code>null</code> or
     * <code>p1.x != p2.x && p1.y != p2.y && abs(p1.x - p2.x)/abs(p1.y - p2.y) == 1</code></li>
     * </ul>
     * @param point1 the first point
     * @param point2 the second point
     * @return the orientation of the segment.
     */
    @objid ("803d5ad3-1dec-11e2-8cad-001ec947c8cc")
    protected Orientation getSegmentOrientation(final Point point1, final Point point2) {
        if (point1 == null || point2 == null)
            return Orientation.NONE;
        if (point1.x() == point2.x()) {
            if (point1.y() == point2.y()) {
                return Orientation.NONE;
            } else {
                return Orientation.VERTICAL;
            }
        } else {
            if (point1.y() == point2.y()) {
                return Orientation.HORIZONTAL;
            } else {
                // Using a big constant factor to avoid rounding problems
                double ratio = (OrthoBendpointEditPolicy.CONSTANT_FACTOR * Math.abs(point1.x() - point2.x())) /
                               (OrthoBendpointEditPolicy.CONSTANT_FACTOR * Math.abs(point1.y() - point2.y()));
                if (ratio < 1) {
                    return Orientation.VERTICAL;
                } else if (ratio > 1) {
                    return Orientation.HORIZONTAL;
                } else {
                    return Orientation.NONE;
                }
            }
        }
    }

    @objid ("803d5adf-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    private void showMoveVerticalSegmentFeedback(final BendpointRequest request) {
        Connection connection = getConnection();
        List<Bendpoint> routingConstraint = (List<Bendpoint>) connection.getRoutingConstraint();
        Bendpoint startPoint = routingConstraint.get(request.getIndex());
        Bendpoint endPoint = routingConstraint.get(request.getIndex() + 1);
        boolean snapPrevious = false;
        boolean snapNext = false;
        
        // Moving a vertical segment, only take the x axis information.
        // Start point
        p1.setLocation(startPoint.getLocation());
        connection.translateToAbsolute(p1);
        p1.setX(request.getLocation().x());
        if (request.getIndex() > 0) {
            Point previous = routingConstraint.get(request.getIndex() - 1).getLocation().getCopy();
            connection.translateToAbsolute(previous);
            if (Math.abs(previous.x() - p1.x()) < TOLERANCE) {
                snapPrevious = true;
                p1.setX(previous.x());
            }
        }
        
        // End point
        p2.setLocation(endPoint.getLocation());
        connection.translateToAbsolute(p2);
        p2.setX(request.getLocation().x());
        if (request.getIndex() + 2 < routingConstraint.size()) {
            Point next = routingConstraint.get(request.getIndex() + 2).getLocation().getCopy();
            connection.translateToAbsolute(next);
            if (Math.abs(next.x() - p2.x()) < TOLERANCE) {
                snapNext = true;
                p2.setX(next.x());
            }
        }
        
        if (snapPrevious) {
            p2.setX(p1.x());
        } else if (snapNext) {
            p1.setX(p2.x());
        }
        connection.translateToRelative(p1);
        routingConstraint.set(request.getIndex(), new AbsoluteBendpoint(p1));
        connection.translateToRelative(p2);
        routingConstraint.set(request.getIndex() + 1, new AbsoluteBendpoint(p2));
        // If necessary ask for new anchors
        if (request.getIndex() == 0) {
            // Update source anchor.
            ReconnectRequest reconnectRequest = new ReconnectRequest(RequestConstants.REQ_RECONNECT_SOURCE);
            ConnectionEditPart connectionEditPart = (ConnectionEditPart) getHost();
            reconnectRequest.setConnectionEditPart(connectionEditPart);
            NodeEditPart sourceEditPart = (NodeEditPart) connectionEditPart.getSource();
            reconnectRequest.setTargetEditPart(sourceEditPart);
            connection.translateToAbsolute(p1);
            reconnectRequest.setLocation(p1);
            connection.setSourceAnchor(sourceEditPart.getSourceConnectionAnchor(reconnectRequest));
        }
        if (request.getIndex() == routingConstraint.size() - 2) {
            // Update target anchor.
            ReconnectRequest reconnectRequest = new ReconnectRequest(RequestConstants.REQ_RECONNECT_TARGET);
            ConnectionEditPart connectionEditPart = (ConnectionEditPart) getHost();
            reconnectRequest.setConnectionEditPart(connectionEditPart);
            NodeEditPart targetEditPart = (NodeEditPart) connectionEditPart.getTarget();
            reconnectRequest.setTargetEditPart(targetEditPart);
            connection.translateToAbsolute(p2);
            reconnectRequest.setLocation(p2);
            connection.setTargetAnchor(targetEditPart.getTargetConnectionAnchor(reconnectRequest));
        }
        connection.setRoutingConstraint(routingConstraint);
    }

    @objid ("803fbce9-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    private void showMoveHorizontalSegmentFeedback(final BendpointRequest request) {
        Connection connection = getConnection();
        List<Bendpoint> routingConstraint = (List<Bendpoint>) connection.getRoutingConstraint();
        Bendpoint startPoint = routingConstraint.get(request.getIndex());
        Bendpoint endPoint = routingConstraint.get(request.getIndex() + 1);
        boolean snapPrevious = false;
        boolean snapNext = false;
        
        // Moving an horizontal segment, only take the y axis information.
        // Start point
        p1.setLocation(startPoint.getLocation());
        connection.translateToAbsolute(p1);
        p1.setY(request.getLocation().y());
        if (request.getIndex() > 0) {
            Point previous = routingConstraint.get(request.getIndex() - 1).getLocation().getCopy();
            connection.translateToAbsolute(previous);
            if (Math.abs(previous.y() - p1.y()) < TOLERANCE) {
                snapPrevious = true;
                p1.setY(previous.y());
            }
        }
        
        // End point
        p2.setLocation(endPoint.getLocation());
        connection.translateToAbsolute(p2);
        p2.setY(request.getLocation().y());
        if (request.getIndex() + 2 < routingConstraint.size()) {
            Point next = routingConstraint.get(request.getIndex() + 2).getLocation().getCopy();
            connection.translateToAbsolute(next);
            if (Math.abs(next.y() - p2.y()) < TOLERANCE) {
                snapNext = true;
                p2.setY(next.y());
            }
        }
        
        if (snapPrevious) {
            p2.setY(p1.y());
        } else if (snapNext) {
            p1.setY(p2.y());
        }
        connection.translateToRelative(p1);
        connection.translateToRelative(p2);
        routingConstraint.set(request.getIndex(), new AbsoluteBendpoint(p1));
        routingConstraint.set(request.getIndex() + 1, new AbsoluteBendpoint(p2));
        
        // If necessary ask for new anchors
        if (request.getIndex() == 0) {
            // Update source anchor.
            ReconnectRequest reconnectRequest = new ReconnectRequest(RequestConstants.REQ_RECONNECT_SOURCE);
            ConnectionEditPart connectionEditPart = (ConnectionEditPart) getHost();
            reconnectRequest.setConnectionEditPart(connectionEditPart);
            NodeEditPart sourceEditPart = (NodeEditPart) connectionEditPart.getSource();
            reconnectRequest.setTargetEditPart(sourceEditPart);
            connection.translateToAbsolute(p1);
            reconnectRequest.setLocation(p1);
            connection.setSourceAnchor(sourceEditPart.getSourceConnectionAnchor(reconnectRequest));
        }
        if (request.getIndex() == routingConstraint.size() - 2) {
            // Update target anchor.
            ReconnectRequest reconnectRequest = new ReconnectRequest(RequestConstants.REQ_RECONNECT_TARGET);
            ConnectionEditPart connectionEditPart = (ConnectionEditPart) getHost();
            reconnectRequest.setConnectionEditPart(connectionEditPart);
            NodeEditPart targetEditPart = (NodeEditPart) connectionEditPart.getTarget();
            reconnectRequest.setTargetEditPart(targetEditPart);
            connection.translateToAbsolute(p2);
            reconnectRequest.setLocation(p2);
            connection.setTargetAnchor(targetEditPart.getTargetConnectionAnchor(reconnectRequest));
        }
        connection.setRoutingConstraint(routingConstraint);
    }

    @objid ("803fbcf0-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    private void showMoveHorizontalBendpointFeedback(final BendpointRequest request) {
        List<Bendpoint> routingConstraint = (List<Bendpoint>) getConnection().getRoutingConstraint();
        Bendpoint previousPoint = routingConstraint.get(request.getIndex() - 1);
        Bendpoint nextPoint = routingConstraint.get(request.getIndex() + 1);
        
        // Previous point
        p1.setLocation(previousPoint.getLocation());
        getConnection().translateToAbsolute(p1);
        p1.setY(request.getLocation().y());
        getConnection().translateToRelative(p1);
        routingConstraint.set(request.getIndex() - 1, new AbsoluteBendpoint(p1));
        
        // Next point
        p1.setLocation(nextPoint.getLocation());
        getConnection().translateToAbsolute(p1);
        p1.setX(request.getLocation().x());
        getConnection().translateToRelative(p1);
        routingConstraint.set(request.getIndex() + 1, new AbsoluteBendpoint(p1));
        
        // Moved point.
        p1.setLocation(request.getLocation());
        getConnection().translateToRelative(p1);
        routingConstraint.set(request.getIndex(), new AbsoluteBendpoint(p1));
        
        getConnection().setRoutingConstraint(routingConstraint);
    }

    @objid ("803fbcf7-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    private void showMoveVerticalBendpointFeedback(final BendpointRequest request) {
        List<Bendpoint> routingConstraint = (List<Bendpoint>) getConnection().getRoutingConstraint();
        Bendpoint previousPoint = routingConstraint.get(request.getIndex() - 1);
        Bendpoint nextPoint = routingConstraint.get(request.getIndex() + 1);
        
        // Previous point
        p1.setLocation(previousPoint.getLocation());
        getConnection().translateToAbsolute(p1);
        p1.setX(request.getLocation().x());
        getConnection().translateToRelative(p1);
        routingConstraint.set(request.getIndex() - 1, new AbsoluteBendpoint(p1));
        
        // Next point
        p1.setLocation(nextPoint.getLocation());
        getConnection().translateToAbsolute(p1);
        p1.setY(request.getLocation().y());
        getConnection().translateToRelative(p1);
        routingConstraint.set(request.getIndex() + 1, new AbsoluteBendpoint(p1));
        
        // Moved point.
        p1.setLocation(request.getLocation());
        getConnection().translateToRelative(p1);
        routingConstraint.set(request.getIndex(), new AbsoluteBendpoint(p1));
        
        getConnection().setRoutingConstraint(routingConstraint);
    }

    @objid ("803fbcfe-1dec-11e2-8cad-001ec947c8cc")
    protected Command getMoveCommand(final ChangeBoundsRequest request) {
        // The request is completely ignored in this method, it might be a design problem:
        // when handling a request without displaying feedback first, we have to simulate it first
        // in order to compute the move coordinates...
        boolean simulateFeedback = false;
        if (this.originalSourceAnchor == null && this.originalTargetAnchor == null) {
            simulateFeedback = true;
            showSourceFeedback(request);
            showTargetFeedback(request);
        }
        
        ConnectionAnchor currentSourceAnchor = getConnection().getSourceAnchor();
        ConnectionAnchor currentTargetAnchor = getConnection().getTargetAnchor();
        getConnection().setSourceAnchor(this.originalSourceAnchor);
        getConnection().setTargetAnchor(this.originalTargetAnchor);
        ConnectionEditPart hostEP = (ConnectionEditPart) getHost();
        IGmPath path = ((IGmLinkObject) hostEP.getModel()).getPath();
        Command command = new TranslateBendpointsCommand(path, hostEP);
        getConnection().setSourceAnchor(currentSourceAnchor);
        getConnection().setTargetAnchor(currentTargetAnchor);
        
        if (simulateFeedback) {
            eraseSourceFeedback(request);
            eraseTargetFeedback(request);
        }
        return command;
    }

    @objid ("803fbd08-1dec-11e2-8cad-001ec947c8cc")
    protected void showChangeBoundsFeedback(final ChangeBoundsRequest request) {
        // Before modifying the connection, save its original constraint and anchors so as to be able to cancel if needed.
        if (this.originalConstraint == null) {
            saveOriginalConstraint();
        }
        Connection connection = getConnection();
        List<Bendpoint> newConstraint = new ArrayList<>();
        for (Bendpoint bendpoint : this.originalConstraint) {
            Point location = Point.SINGLETON;
            location.setLocation(bendpoint.getLocation());
            connection.translateToAbsolute(location);
            location.translate(request.getMoveDelta());
            connection.translateToRelative(location);
            newConstraint.add(new AbsoluteBendpoint(location));
        }
        getConnection().setSourceAnchor(new XYAnchor(this.originalSourceAnchor.getReferencePoint()
                                                                              .getTranslated(request.getMoveDelta())));
        getConnection().setTargetAnchor(new XYAnchor(this.originalTargetAnchor.getReferencePoint()
                                                                              .getTranslated(request.getMoveDelta())));
        
        getConnection().setRoutingConstraint(newConstraint);
    }

    @objid ("803fbd0e-1dec-11e2-8cad-001ec947c8cc")
    protected void eraseChangeBoundsFeedback(final ChangeBoundsRequest request) {
        restoreOriginalConstraint();
        this.originalConstraint = null;
        this.originalSourceAnchor = null;
        this.originalTargetAnchor = null;
    }

    @objid ("803fbd14-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean understandsRequest(final Request req) {
        if (REQ_MOVE.equals(req.getType())) {
            return true;
        } else {
            return super.understandsRequest(req);
        }
    }

}
