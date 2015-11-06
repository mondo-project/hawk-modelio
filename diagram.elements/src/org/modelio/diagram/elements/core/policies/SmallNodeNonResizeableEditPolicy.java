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
                                    

package org.modelio.diagram.elements.core.policies;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.SWT;
import org.modelio.diagram.elements.plugin.DiagramElements;

/**
 * A specialization of {@link DefaultNodeResizableEditPolicy} that forbids resize and show only one handle.
 * <p>
 * Moving the node will show a ghost for the node and all links connected to the node.
 * <p>
 * To be used one small figures that are not resizeable.
 */
@objid ("80d85371-1dec-11e2-8cad-001ec947c8cc")
public class SmallNodeNonResizeableEditPolicy extends DefaultNodeResizableEditPolicy {
    @objid ("cbc20e86-0992-43d0-ab82-f3f653cfb190")
    protected List<IFigure> feedbackFigures = new ArrayList<>();

    /**
     * Initialize the policy.
     */
    @objid ("80d85378-1dec-11e2-8cad-001ec947c8cc")
    public SmallNodeNonResizeableEditPolicy() {
        super();
    }

    /**
     * Redefined to make a copy of the dragged node and all connections going from or to him.
     */
    @objid ("80d8537b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createDragSourceFeedbackFigure() {
        IFigure fig = getHostFigure();
        try {
            final IFigure feed = fig.getClass().newInstance();
            feed.setBounds(fig.getBounds());
        
            if (feed instanceof Shape)
                ((Shape) feed).setLineStyle(SWT.LINE_DASH);
        
            getFeedbackLayer().add(feed);
        
            final GraphicalEditPart host = (GraphicalEditPart) getHost();
            for (Object c : host.getSourceConnections()) {
                final GraphicalEditPart connPart = (GraphicalEditPart) c;
                final Connection origConn = (Connection) connPart.getFigure();
                final Connection connFig = origConn.getClass().newInstance();
                connFig.setSourceAnchor(new ChopboxAnchor(feed));
                connFig.setTargetAnchor(origConn.getTargetAnchor());
        
                connFig.setConnectionRouter(origConn.getConnectionRouter());
                connFig.setRoutingConstraint(origConn.getRoutingConstraint());
        
                if (connFig instanceof Shape)
                    ((Shape) connFig).setLineStyle(SWT.LINE_DASH);
        
                this.feedbackFigures.add(connFig);
                getFeedbackLayer().add(connFig);
        
            }
        
            for (Object c : host.getTargetConnections()) {
                final GraphicalEditPart connPart = (GraphicalEditPart) c;
                final Connection origConn = (Connection) connPart.getFigure();
                final Connection connFig = origConn.getClass().newInstance();
        
                connFig.setSourceAnchor(origConn.getSourceAnchor());
                connFig.setTargetAnchor(new ChopboxAnchor(feed));
        
                connFig.setConnectionRouter(origConn.getConnectionRouter());
                connFig.setRoutingConstraint(origConn.getRoutingConstraint());
        
                if (connFig instanceof Shape)
                    ((Shape) connFig).setLineStyle(SWT.LINE_DASH);
        
                this.feedbackFigures.add(connFig);
                getFeedbackLayer().add(connFig);
            }
        
            return feed;
        
        } catch (InstantiationException | IllegalAccessException e) {
            DiagramElements.LOG.error(e);
            return super.createDragSourceFeedbackFigure();
        }
    }

    /**
     * Create a single rectangular handle that fits the entire figure.
     */
    @objid ("80d85382-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> createSelectionHandles() {
        final List<Object> ret = new ArrayList<>();
        ret.add(new MoveHandle((GraphicalEditPart) getHost()));
        return ret;
    }

    @objid ("80d8538a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void eraseChangeBoundsFeedback(final ChangeBoundsRequest request) {
        //getHostFigure().setBounds(this.oldBounds);
        //System.out.println("erase feed move, old bounds= :"+this.oldBounds+", current="+getHostFigure().getBounds());
        super.eraseChangeBoundsFeedback(request);
        
        for (IFigure f : this.feedbackFigures) {
            f.getParent().remove(f);
        }
        this.feedbackFigures.clear();
    }

    @objid ("80d85391-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getResizeCommand(final ChangeBoundsRequest request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
        req.setEditParts(getHost());
        
        req.setMoveDelta(request.getMoveDelta());
        // Ignore actual resize request: node is not really resizeable.
        req.setSizeDelta(computeSizeDelta());
        req.setLocation(request.getLocation());
        req.setExtendedData(request.getExtendedData());
        req.setResizeDirection(request.getResizeDirection());
        return getHost().getParent().getCommand(req);
    }

    @objid ("80d8539c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void showChangeBoundsFeedback(final ChangeBoundsRequest request) {
        super.showChangeBoundsFeedback(request);
    }

    /**
     * Compute the delta from the current size to the size fixed by the figure preferred size.
     * @return the delta to the preferred size.
     */
    @objid ("80d853a3-1dec-11e2-8cad-001ec947c8cc")
    private Dimension computeSizeDelta() {
        final IFigure fig = getHostFigure();
        return fig.getPreferredSize().getShrinked(fig.getBounds().getSize());
    }

}
