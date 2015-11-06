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
                                    

package org.modelio.diagram.elements.core.figures.routers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.BendpointConnectionRouter;
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

/**
 * Routes {@link Connection}s through a <code>List</code> of {@link Bendpoint Bendpoints} that make an orthogonal path.
 * <p>
 * The route constraint is modified to be made orthogonal.
 * 
 * @author fpoyer
 */
@objid ("7fb5747f-1dec-11e2-8cad-001ec947c8cc")
public class OrthogonalRouter extends BendpointConnectionRouter {
    /**
     * Temporary point used to avoid Point allocations.
     */
    @objid ("6354815d-1e83-11e2-8cad-001ec947c8cc")
    private static final PrecisionPoint A_POINT = new PrecisionPoint();

    @objid ("7fb57488-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void route(Connection conn) {
        final ConnectionAnchor sourceAnchor = conn.getSourceAnchor();
        final ConnectionAnchor targetAnchor = conn.getTargetAnchor();
        
        final List<Bendpoint> bendpoints = computeInitialBendpointsList(conn, sourceAnchor, targetAnchor);
        
        // Source and target locations are now fixed, we are not allowed to move them anymore.
        Point sourceLocation = bendpoints.get(0).getLocation();
        Point targetLocation = bendpoints.get(bendpoints.size() - 1).getLocation();
        
        // Now the tricky part: fix the first and last bend points to form an orthogonal path.
        final Rectangle sourceRelativeBounds = getAnchorOwnerAbsoluteBounds(sourceAnchor).expand(1, 1);
        conn.translateToRelative(sourceRelativeBounds);
        final Rectangle targetRelativeBounds = getAnchorOwnerAbsoluteBounds(targetAnchor).expand(1, 1);
        conn.translateToRelative(targetRelativeBounds);
        Direction sourceAnchorOrientation = GeomUtils.getDirection(sourceLocation, sourceRelativeBounds);
        Direction targetAnchorOrientation = GeomUtils.getDirection(targetLocation, targetRelativeBounds);
        if (bendpoints.size() == 2) {
            fixNoBendpointsLink(bendpoints,
                                sourceLocation,
                                targetLocation,
                                sourceAnchorOrientation,
                                targetAnchorOrientation);
        } else if (bendpoints.size() == 3) {
            fixOneBendpointLink(bendpoints,
                                sourceLocation,
                                targetLocation,
                                sourceAnchorOrientation,
                                targetAnchorOrientation);
        
        } else {
            fixSeveralBendpointsLink(bendpoints,
                                     sourceLocation,
                                     targetLocation,
                                     sourceAnchorOrientation,
                                     targetAnchorOrientation);
        }
        
        // Some cleanup of useless bendpoints.
        cleanup(bendpoints);
        
        // Clear the old points list
        final PointList points = conn.getPoints();
        points.removeAllPoints();
        for (int i = 0; i < bendpoints.size(); i++) {
            Bendpoint bp = bendpoints.get(i);
            points.addPoint(bp.getLocation());
        }
        
        conn.setPoints(points);
    }

    /**
     * convenience method to get the constraint as a list of bend points.
     * @param conn a connection figure
     * @return The list of bend points.
     */
    @objid ("7fb5748e-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    private List<Bendpoint> getBendpoints(Connection conn) {
        return (List<Bendpoint>) getConstraint(conn);
    }

    /**
     * Get the anchor owner (handle)bounds in absolute coordinates. If the anchor is not attached to a figure, returns a
     * 1x1 sized rectangle located at the anchor reference point.
     * @param anchor The anchor.
     * @return The anchor owner bounds.
     */
    @objid ("7fb5749b-1dec-11e2-8cad-001ec947c8cc")
    private Rectangle getAnchorOwnerAbsoluteBounds(ConnectionAnchor anchor) {
        final IFigure f = anchor.getOwner();
        if (f == null) {
            Point p = anchor.getReferencePoint();
            return new Rectangle(p.x, p.y, 1, 1);
        } else {
            PrecisionRectangle bounds = new PrecisionRectangle(f instanceof HandleBounds
                    ? ((HandleBounds) f).getHandleBounds() : f.getBounds());
            f.translateToAbsolute(bounds);
        
            return bounds;
        }
    }

    /**
     * @param bendpoints bend points list to clean
     */
    @objid ("7fb574a4-1dec-11e2-8cad-001ec947c8cc")
    private void cleanup(final List<Bendpoint> bendpoints) {
        // Finish by removing unnecessary points:
        // 1: overlapping points.
        List<Integer> indexesToRemove = new ArrayList<>();
        for (int i = 1; i < bendpoints.size() - 2; ++i) {
            Point p1 = bendpoints.get(i).getLocation();
            Point p2 = bendpoints.get(i + 1).getLocation();
        
            if (p1.getDistance(p2) < 1) {
                indexesToRemove.add(i);
            }
        }
        for (int i = indexesToRemove.size() - 1; i >= 0; --i) {
            bendpoints.remove(indexesToRemove.get(i).intValue());
        }
        // 2: bendpoints not bending
        indexesToRemove.clear();
        for (int i = 1; i < bendpoints.size() - 1; ++i) {
            if (bendpoints.get(i - 1).getLocation().x == bendpoints.get(i + 1).getLocation().x ||
                bendpoints.get(i - 1).getLocation().y == bendpoints.get(i + 1).getLocation().y) {
                indexesToRemove.add(i);
            }
        }
        for (int i = indexesToRemove.size() - 1; i >= 0; --i) {
            bendpoints.remove(indexesToRemove.get(i).intValue());
        }
    }

    /**
     * @param bendpoints
     * @param sourceLocation
     * @param targetLocation
     * @param sourceAnchorOrientation
     * @param targetAnchorOrientation
     */
    @objid ("7fb574ad-1dec-11e2-8cad-001ec947c8cc")
    private void fixSeveralBendpointsLink(final List<Bendpoint> bendpoints, final Point sourceLocation, final Point targetLocation, final Direction sourceAnchorOrientation, final Direction targetAnchorOrientation) {
        // If there are at least 2 intermediary bend points, fix them to get orthogonal segments
        Point fixedPoint = bendpoints.get(1).getLocation();
        Point nextPoint = bendpoints.get(2).getLocation();
        Orientation nextSegmentOrientation = Orientation.NONE;
        if (fixedPoint.x == nextPoint.x) {
            nextSegmentOrientation = Orientation.VERTICAL;
        } else if (fixedPoint.y == nextPoint.y) {
            nextSegmentOrientation = Orientation.HORIZONTAL;
        } else {
            assert (false) : "impossible to determine orientation of start segment, something is wrong with the provided list of bendpoints!";
        }
        if (sourceAnchorOrientation == Direction.NONE) {
            if (nextSegmentOrientation == Orientation.VERTICAL) {
                // next segment is vertical, so first was horizontal
                fixedPoint.y = sourceLocation.y;
            } else if (nextSegmentOrientation == Orientation.HORIZONTAL) {
                // next segment is horizontal so first is vertical
                fixedPoint.x = sourceLocation.x;
            }
        } else if (sourceAnchorOrientation == Direction.NORTH || sourceAnchorOrientation == Direction.SOUTH) {
            // First segment is vertical: align the X coordinates
            // check that we don't need an additional bend point (next segment must be horizontal) first
            if (nextSegmentOrientation != Orientation.HORIZONTAL) {
                // Add an additional bendpoint (null is allright, it will be replaced later anyway).
                bendpoints.add(1, null);
                fixedPoint = new Point(fixedPoint);
            }
            fixedPoint.x = sourceLocation.x;
        } else {
            // First segment is horizontal: align the Y coordinates
            // check that we don't need an additional bend point (next segment must be vertical) first
            if (nextSegmentOrientation != Orientation.VERTICAL) {
                // Add an additional bendpoint (null is allright, it will be replaced later anyway).
                bendpoints.add(1, null);
                fixedPoint = new Point(fixedPoint);
            }
            fixedPoint.y = sourceLocation.y;
        }
        AbsoluteBendpoint fixedBendpoint = new AbsoluteBendpoint(fixedPoint);
        bendpoints.set(1, fixedBendpoint);
        
        int lastBendpointIndex = bendpoints.size() - 2;
        fixedPoint = bendpoints.get(lastBendpointIndex).getLocation();
        nextPoint = bendpoints.get(lastBendpointIndex - 1).getLocation();
        Orientation previousSegmentOrientation = Orientation.NONE;
        if (fixedPoint.x == nextPoint.x) {
            previousSegmentOrientation = Orientation.VERTICAL;
        } else if (fixedPoint.y == nextPoint.y) {
            previousSegmentOrientation = Orientation.HORIZONTAL;
        } else {
            assert (false) : "impossible to determine orientation of last segment, something is wrong with the provided list of bendpoints!";
        }
        if (targetAnchorOrientation == Direction.NONE) {
            // Target anchor is not oriented, deduct orientation from previous segment if possible.
            if (previousSegmentOrientation == Orientation.VERTICAL) {
                // previous segment is vertical, so first was horizontal
                fixedPoint.y = targetLocation.y;
            } else if (previousSegmentOrientation == Orientation.HORIZONTAL) {
                // previous segment is horizontal so first is vertical
                fixedPoint.x = targetLocation.x;
            }
        } else if (targetAnchorOrientation == Direction.NORTH || targetAnchorOrientation == Direction.SOUTH) {
            // Last segment is vertical: align the X coordinates
            // Check that we don't need an additional bend point (previous segment must be horizontal) first
            if (previousSegmentOrientation != Orientation.HORIZONTAL) {
                // Add an additional bendpoint (null is allright, it will be replaced later anyway).
                ++lastBendpointIndex;
                bendpoints.add(lastBendpointIndex, null);
                fixedPoint = new Point(fixedPoint);
            }
            fixedPoint.x = targetLocation.x;
        } else {
            // Last segment is horizontal: align the Y coordinates
            // Check that we don't need an additional bend point (previous segment must be vertical) first
            if (previousSegmentOrientation != Orientation.VERTICAL) {
                // Add an additional bendpoint (null is allright, it will be replaced later anyway).
                ++lastBendpointIndex;
                bendpoints.add(lastBendpointIndex, null);
                fixedPoint = new Point(fixedPoint);
            }
            fixedPoint.y = targetLocation.y;
        }
        fixedBendpoint = new AbsoluteBendpoint(fixedPoint);
        bendpoints.set(lastBendpointIndex, fixedBendpoint);
    }

    /**
     * @param bendpoints
     * @param sourceLocation
     * @param targetLocation
     * @param sourceAnchorOrientation
     * @param targetAnchorOrientation
     */
    @objid ("7fb574c2-1dec-11e2-8cad-001ec947c8cc")
    private void fixOneBendpointLink(final List<Bendpoint> bendpoints, final Point sourceLocation, final Point targetLocation, final Direction sourceAnchorOrientation, final Direction targetAnchorOrientation) {
        // If there is only 1 intermediary bend point, try to fix it or add another bend point if needed
        Point fixedPoint = bendpoints.get(1).getLocation();
        if (sourceAnchorOrientation == Direction.NORTH || sourceAnchorOrientation == Direction.SOUTH) {
            fixedPoint.x = sourceLocation.x;
            if (targetAnchorOrientation == Direction.NORTH || targetAnchorOrientation == Direction.SOUTH) {
                // Unless the 3 points are aligned on the X axis, we are gonna need an additional bend point.
                if (targetLocation.x != fixedPoint.x) {
                    A_POINT.setLocation(targetLocation.x, fixedPoint.y);
                    bendpoints.add(2, new AbsoluteBendpoint(A_POINT));
                }
                // else: do nothing, the 3 points are aligned, the intermediary bendpoint will be removed during the cleanup phase.
            } else {
                fixedPoint.y = targetLocation.y;
            }
        } else {
            fixedPoint.y = sourceLocation.y;
            if (targetAnchorOrientation == Direction.NORTH ||
                targetAnchorOrientation == Direction.SOUTH ||
                targetAnchorOrientation == Direction.NONE) {
                fixedPoint.x = targetLocation.x;
            } else {
                // Unless the 3 points are aligned on the Y axis, we are gonna need an additional bend point.
                if (targetLocation.y != fixedPoint.y) {
                    A_POINT.setLocation(fixedPoint.x, targetLocation.y);
                    bendpoints.add(2, new AbsoluteBendpoint(A_POINT));
                }
                // else: do nothing, the 3 points are aligned, the intermediary bendpoint will be removed during the cleanup phase.
            }
        }
        AbsoluteBendpoint fixedBendpoint = new AbsoluteBendpoint(fixedPoint);
        bendpoints.add(1, fixedBendpoint);
        bendpoints.remove(2);
    }

    /**
     * @param bendpoints
     * @param sourceLocation
     * @param targetLocation
     * @param sourceAnchorOrientation
     * @param targetAnchorOrientation
     */
    @objid ("7fb574d7-1dec-11e2-8cad-001ec947c8cc")
    private void fixNoBendpointsLink(final List<Bendpoint> bendpoints, final Point sourceLocation, final Point targetLocation, final Direction sourceAnchorOrientation, final Direction targetAnchorOrientation) {
        // If there is no intermediary bend point, check whether the anchors location are aligned, and add bend point(s) if not.
        if (sourceAnchorOrientation == Direction.NORTH || sourceAnchorOrientation == Direction.SOUTH) {
            if (targetAnchorOrientation == Direction.NORTH || targetAnchorOrientation == Direction.SOUTH) {
                if (sourceLocation.x != targetLocation.x) {
                    // No luck: not aligned, we need 2 additional bend points.
                    A_POINT.setLocation(sourceLocation.x, (sourceLocation.y + targetLocation.y) / 2);
                    bendpoints.add(1, new AbsoluteBendpoint(A_POINT));
                    A_POINT.setLocation(targetLocation.x, (sourceLocation.y + targetLocation.y) / 2);
                    bendpoints.add(2, new AbsoluteBendpoint(A_POINT));
                }
                // else: good luck: both anchors are aligned, nothing to do!
            } else {
                // We need an additional bend point.
                A_POINT.setLocation(sourceLocation.x, targetLocation.y);
                bendpoints.add(1, new AbsoluteBendpoint(A_POINT));
            }
        } else {
            if (targetAnchorOrientation == Direction.NONE) {
                // Not oriented target anchor: we might need an additional bend point.
                if (sourceLocation.y != targetLocation.y) {
                    // No luck, anchors are not aligned, we need a bend point.
                    A_POINT.setLocation(targetLocation.x, sourceLocation.y);
                    bendpoints.add(1, new AbsoluteBendpoint(A_POINT));
                }
                // else: good luck, both anchors are aligned, nothing to do!
            } else if (targetAnchorOrientation == Direction.SOUTH ||
                       targetAnchorOrientation == Direction.NORTH) {
                // We need an additional bend point
                A_POINT.setLocation(targetLocation.x, sourceLocation.y);
                bendpoints.add(1, new AbsoluteBendpoint(A_POINT));
            } else {
                if (sourceLocation.y != targetLocation.y) {
                    // No luck: not aligned, we need 2 additional bend points.
                    A_POINT.setLocation((sourceLocation.x + targetLocation.x) / 2, sourceLocation.y);
                    bendpoints.add(1, new AbsoluteBendpoint(A_POINT));
                    A_POINT.setLocation((sourceLocation.x + targetLocation.x) / 2, targetLocation.y);
                    bendpoints.add(2, new AbsoluteBendpoint(A_POINT));
                }
                // else: good luck: both anchors are aligned, nothing to do!
            }
        }
    }

    @objid ("7fb7d6e7-1dec-11e2-8cad-001ec947c8cc")
    private List<Bendpoint> computeInitialBendpointsList(final Connection conn, final ConnectionAnchor sourceAnchor, final ConnectionAnchor targetAnchor) {
        List<Bendpoint> origBendpoints = getBendpoints(conn);
        if (origBendpoints == null) {
            origBendpoints = Collections.emptyList();
        }
        final List<Bendpoint> bendpoints = new ArrayList<>();
        
        // Let's assume the first point is the source anchor reference point (This may be modified later).
        A_POINT.setLocation(sourceAnchor.getReferencePoint());
        conn.translateToRelative(A_POINT);
        bendpoints.add(new AbsoluteBendpoint(A_POINT));
        // Now assume the given bendpoints are good (we'll fix them later if needed)
        for (Bendpoint bendpoint : origBendpoints) {
            bendpoints.add(new AbsoluteBendpoint(bendpoint.getLocation()));
        }
        // End with the target anchor reference point
        A_POINT.setLocation(targetAnchor.getReferencePoint());
        conn.translateToRelative(A_POINT);
        bendpoints.add(new AbsoluteBendpoint(A_POINT));
        
        final Rectangle srcBounds = getAnchorOwnerAbsoluteBounds(sourceAnchor).expand(1, 1);
        conn.translateToRelative(srcBounds);
        final Rectangle targetBounds = getAnchorOwnerAbsoluteBounds(targetAnchor).expand(1, 1);
        conn.translateToRelative(targetBounds);
        
        // Cleanup some useless points if needed at the beginning
        boolean sourceContainsTarget = srcBounds.contains(targetBounds);
        if (!sourceContainsTarget) {
            // Remove from the beginning of the list all bendpoints until the first outside the source bounds.
            // We want to keep at least 2 points (source and target anchor reference point)
            while (bendpoints.size() > 2 && srcBounds.contains(bendpoints.get(1).getLocation())) {
                bendpoints.remove(1);
            }
        }
        
        // Cleanup some useless points if needed at the end
        boolean targetContainsSource = targetBounds.contains(srcBounds);
        if (!targetContainsSource) {
            // Remove from the end of the list all bendpoints until the first outside the target bounds.
            // We want to keep at least 2 points (source and target anchor reference point)
            while (bendpoints.size() > 2 &&
                   targetBounds.contains(bendpoints.get(bendpoints.size() - 2).getLocation())) {
                bendpoints.remove(bendpoints.size() - 2);
            }
        }
        
        // Now compute the actual location of the source anchor, based on the next bendpoint (might be the target anchor reference point).
        A_POINT.setLocation(bendpoints.get(1).getLocation());
        conn.translateToAbsolute(A_POINT);
        A_POINT.setLocation(sourceAnchor.getLocation(A_POINT));
        conn.translateToRelative(A_POINT);
        // Use that value in the list, instead of the reference point.
        bendpoints.set(0, new AbsoluteBendpoint(A_POINT));
        
        // Now compute the actual location of the target anchor, based on the previous bendpoint (might be the source anchor location point).
        int index = bendpoints.size() - 1;
        A_POINT.setLocation(bendpoints.get(index - 1).getLocation());
        conn.translateToAbsolute(A_POINT);
        A_POINT.setLocation(targetAnchor.getLocation(A_POINT));
        conn.translateToRelative(A_POINT);
        // Use that value in the list, instead of the reference point.
        bendpoints.set(index, new AbsoluteBendpoint(A_POINT));
        return bendpoints;
    }

}
