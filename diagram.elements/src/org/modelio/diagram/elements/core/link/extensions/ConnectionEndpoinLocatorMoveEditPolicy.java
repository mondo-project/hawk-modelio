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
                                    

package org.modelio.diagram.elements.core.link.extensions;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.modelio.diagram.elements.core.node.GmNodeModel;

/**
 * Edit policy that allows connection extension labels to be moved.
 * <p>
 * Can handle any figure that use a ConnectionEndpointLocator as layout constraint.
 */
@objid ("7ff5d416-1dec-11e2-8cad-001ec947c8cc")
public class ConnectionEndpoinLocatorMoveEditPolicy extends NonResizableEditPolicy {
    @objid ("7ff5d41a-1dec-11e2-8cad-001ec947c8cc")
    private static LocatorFactory f = LocatorFactory.getInstance();

    @objid ("663ec167-1e83-11e2-8cad-001ec947c8cc")
    private PolylineConnection focuslink = null;

    @objid ("7ff5d41e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public boolean understandsRequest(Request request) {
        if (REQ_MOVE.equals(request.getType()))
            return isDragAllowed();
        return false;
    }

    @objid ("7ff5d426-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getMoveCommand(ChangeBoundsRequest request) {
        final IFigure extension = ((GraphicalEditPart) getHost()).getFigure();
        final Connection connection = (Connection) extension.getParent();
        final Point moveDelta0 = request.getMoveDelta();
        final Dimension moveDelta = new Dimension(moveDelta0.x, moveDelta0.y);
        
        final SidedConnectionEndpointLocator newLoc = f.getLocator(connection,
                                                                   extension,
                                                                   moveDelta,
                                                                   request.getLocation());
        
        final ChangeExtensionLocationCommand cmd = new ChangeExtensionLocationCommand();
        
        GmConnectionEndpoinLocator newconstraint = new GmConnectionEndpoinLocator();
        newconstraint.setEnd(newLoc.isEnd());
        newconstraint.setUDistance(newLoc.getUDistance());
        newconstraint.setVDistance(newLoc.getVDistance());
        
        cmd.setConstraint(newconstraint);
        cmd.setModel((GmNodeModel) getHost().getModel());
        return cmd;
    }

    @objid ("7ff8365e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void showSelection() {
        super.showSelection();
        
        final IFigure extension = ((GraphicalEditPart) getHost()).getFigure();
        final Connection connection = (Connection) extension.getParent();
        final SidedConnectionEndpointLocator loc = (SidedConnectionEndpointLocator) connection.getLayoutManager()
                                                                                              .getConstraint(extension);
        
        this.focuslink = new PolylineConnection();
        ConnectionAnchor srcAnchor = new ChopboxAnchor(extension);
        ConnectionAnchor targetAnchor = new LocatorAnchor(connection, loc);
        this.focuslink.setSourceAnchor(srcAnchor);
        this.focuslink.setTargetAnchor(targetAnchor);
        this.focuslink.setLineStyle(org.eclipse.swt.SWT.LINE_DOT);
        addFeedback(this.focuslink);
    }

    @objid ("7ff83661-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void hideSelection() {
        super.hideSelection();
        if (this.focuslink != null) {
            removeFeedback(this.focuslink);
            this.focuslink = null;
        }
    }

    /**
     * Anchor whose location is given by a provided Locator.
     */
    @objid ("7ff83664-1dec-11e2-8cad-001ec947c8cc")
    private static class LocatorAnchor extends AbstractConnectionAnchor {
        @objid ("7ff83669-1dec-11e2-8cad-001ec947c8cc")
        private FractionalConnectionLocator loc;

        @objid ("7ff8366a-1dec-11e2-8cad-001ec947c8cc")
        public LocatorAnchor(final IFigure owner, final SidedConnectionEndpointLocator loc) {
            super(owner);
            if (loc.isEnd())
                this.loc = new FractionalConnectionLocator((Connection) owner, 0.9);
            else
                this.loc = new FractionalConnectionLocator((Connection) owner, 0.1);
        }

        @objid ("7ff83672-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Point getLocation(final Point reference) {
            return this.loc.getReferencePoint();
        }

        @objid ("7ff8367d-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public Point getReferencePoint() {
            return this.loc.getReferencePoint();
        }

    }

}
