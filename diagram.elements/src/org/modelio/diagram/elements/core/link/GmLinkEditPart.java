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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.LocationRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.swt.graphics.Color;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.common.portcontainer.SatelliteDragTrackerProvider;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.figures.LinkFigure;
import org.modelio.diagram.elements.core.figures.RoundedLinkFigure;
import org.modelio.diagram.elements.core.figures.anchors.LinkAnchor;
import org.modelio.diagram.elements.core.figures.routers.RakeRouter;
import org.modelio.diagram.elements.core.link.extensions.GmFractionalConnectionLocator;
import org.modelio.diagram.elements.core.link.extensions.IGmLocator;
import org.modelio.diagram.elements.core.link.extensions.LocatorFactory;
import org.modelio.diagram.elements.core.link.ortho.OrthoBendpointEditPolicy;
import org.modelio.diagram.elements.core.link.path.ConnectionHelperFactory;
import org.modelio.diagram.elements.core.link.path.GmPathDataExtractor;
import org.modelio.diagram.elements.core.link.path.IConnectionHelper;
import org.modelio.diagram.elements.core.link.path.OrthoConnectionHelper;
import org.modelio.diagram.elements.core.link.rake.CreateRakeLinkEditPolicy;
import org.modelio.diagram.elements.core.link.rake.RakeLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.model.IGmLinkRake;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.model.IGmPath;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultBendpointEditPolicy;
import org.modelio.diagram.elements.core.policies.DefaultDeleteLinkEditPolicy;
import org.modelio.diagram.elements.core.policies.DelegatingDirectEditionEditPolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Basic edit part for GmLinks. A GmLinkEditPart: <br/>
 * <li>extends AbsbractConnectionEditPart => to have a link behavior</li><br/>
 * <li>implements NodeEditPart => to be usable as a source or a target of another link</li><br/>
 * <li>implements PropertyChangeListener => for update and refreshing from model or style change</li><br/>
 */
@objid ("80199771-1dec-11e2-8cad-001ec947c8cc")
public class GmLinkEditPart extends AbstractConnectionEditPart implements PropertyChangeListener, IAnchorModelProvider {
    @objid ("80199775-1dec-11e2-8cad-001ec947c8cc")
    private RoutingMode currentRoutingMode = new RoutingMode();

    /**
     * Rake router for all links.
     */
    @objid ("80199776-1dec-11e2-8cad-001ec947c8cc")
    protected static RakeRouter rakeRouter = new RakeRouter();

    /**
     * Creates a GmLinkEditPart.
     */
    @objid ("80199778-1dec-11e2-8cad-001ec947c8cc")
    public GmLinkEditPart() {
        super();
    }

    @objid ("8019977b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void activate() {
        super.activate();
        
        final GmLink gmLink = (GmLink) getModel();
        gmLink.addPropertyChangeListener(this);
    }

    @objid ("8019977e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object createAnchorModel(final ConnectionAnchor anchor) {
        //TODO Temporary fake implementation: try to return the anchor of 'this' origin anchoring point
        return null;
    }

    @objid ("801bf99d-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void deactivate() {
        super.deactivate();
        ((GmLink) getModel()).removePropertyChangeListener(this);
    }

    /**
     * Returns an object which is an instance of the given class associated with this object. Returns <code>null</code>
     * if no such object can be found.
     * <p>
     * Extends {@link AbstractConnectionEditPart#getAdapter(Class)} to support {@link MObject}, {@link IGmObject},
     * {@link GmModel} and their subclasses.
     * @see IAdaptable#getAdapter(Class)
     * @param adapter the adapter class to look up
     * @return a object castable to the given class, or <code>null</code> if this object does not have an adapter for
     * the given class
     */
    @objid ("801bf9a0-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        final Object model = getModel();
        
        // Support IGmObject, GmModel and its subclasses
        if (adapter.isInstance(model))
            return model;
        
        // Support MObject & subclasses
        if (model instanceof GmModel) {
            final GmModel gmModel = (GmModel) model;
            final MObject obElement = gmModel.getRelatedElement();
            if (adapter.isInstance(obElement))
                return obElement;
        }
        return super.getAdapter(adapter);
    }

    @objid ("801bf9a7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public DragTracker getDragTracker(Request req) {
        return new SelectConnectionEditPartTracker(this);
    }

    @objid ("801bf9b1-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    @Override
    public List<Object> getModelChildren() {
        final ArrayList<Object> ret = new ArrayList<>(8);
        final GmLink link = (GmLink) getModel();
        
        ret.addAll(link.getVisibleExtensions());
        ret.addAll(super.getModelChildren());
        return ret;
    }

    @objid ("801bf9b8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(final ConnectionEditPart connection) {
        //TODO Temporary fake implementation: try to return the anchor of 'this' origin anchoring point
        //final GmLink gmlink = (GmLink) getModel();
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("801bf9c3-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(final Request request) {
        //TODO Temporary fake implementation: try to return the anchor of 'this' origin anchoring point 
        //final GmLink gmlink = (GmLink) getModel();
        //ConnectionAnchor anchor = this.getConnectionFigure().getSourceAnchor();
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("801bf9ce-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final ConnectionEditPart connection) {
        //TODO Temporary fake implementation: try to return the anchor of 'this' origin anchoring point
        //final GmLink gmlink = (GmLink) getModel();
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("801bf9d9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final Request request) {
        //TODO Temporary fake implementation: try to return the anchor of 'this' origin anchoring point 
        //final GmLink gmlink = (GmLink) getModel();
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    /**
     * Overridden to add a specific behavior on {@link RequestConstants#REQ_DIRECT_EDIT DIRECT_EDIT} request: the
     * request if forwarded to all children edit parts until one understand it, and then said child edit part is asked
     * to perform the request.
     */
    @objid ("801bf9e4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(final Request req) {
        if (RequestConstants.REQ_DIRECT_EDIT.equals(req.getType())) {
            if (req instanceof LocationRequest) {
                // Give the request to the child where the request is located
                final Point reqLocation = ((LocationRequest) req).getLocation();
        
                for (Object childEditPartObj : getChildren()) {
        
                    final GraphicalEditPart childEditPart = (GraphicalEditPart) childEditPartObj;
                    if (childEditPart.understandsRequest(req) &&
                        containsAbsolutePoint(childEditPart, reqLocation)) {
                        childEditPart.performRequest(req);
                    }
        
                }
            } else {
                // Give the request to the first child that understand it
                for (Object childEditPartObj : getChildren()) {
                    final EditPart childEditPart = (EditPart) childEditPartObj;
                    if (childEditPart.understandsRequest(req)) {
                        childEditPart.performRequest(req);
                        return;
                    }
        
                }
            }
        }
        super.performRequest(req);
    }

    @objid ("801e5bfa-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final String propName = evt.getPropertyName();
        
        if (propName.equals(IGmObject.PROPERTY_LAYOUTDATA)) {
            // Link layout (bendpoints) update
            refreshRakeModeOnSource();
            refreshRakeModeOnTarget();
        
            refreshSourceAnchor();
            refreshTargetAnchor();
            refreshVisuals();
        
        } else if (propName.equals(IGmObject.PROPERTY_CHILDREN)) {
            refreshChildren();
        } else if (propName.equals(IGmObject.PROPERTY_STYLE)) {
            refreshFromStyle(getFigure(), (IStyle) evt.getNewValue());
            // Since many extensions' visibility is driven by a style key value, force refresh of children
            refreshChildren();
        } else if (propName.equals(GmLink.PROP_SOURCE_EL)) {
            // If notified that the source changed.
        
            if (evt.getNewValue() instanceof MObject) {
                // Build a reconnection request, that will be passed to edit
                // parts that might accept it.
                ReconnectRequest request = new ReconnectRequest(RequestConstants.REQ_RECONNECT_SOURCE);
                request.setConnectionEditPart(this);
                request.setLocation(new Point(0, 0));
        
                swapEnd((MObject) evt.getNewValue(), request);
            }
        } else if (propName.equals(GmLink.PROP_TARGET_EL)) {
            // If notified that the target changed.
        
            if (evt.getNewValue() instanceof MObject) {
                // Build a reconnection request, that will be passed to edit
                // parts that might accept it.
                ReconnectRequest request = new ReconnectRequest(RequestConstants.REQ_RECONNECT_TARGET);
                request.setConnectionEditPart(this);
                request.setLocation(new Point(0, 0));
        
                swapEnd((MObject) evt.getNewValue(), request);
            }
        } else if (propName.equals(GmLink.PROP_SOURCE_GM)) {
            refreshRakeModeOnSource();
        } else if (propName.equals(GmLink.PROP_TARGET_GM)) {
            refreshRakeModeOnTarget();
        } else if (propName.equals(IGmObject.PROPERTY_LINK_SOURCE)) {
            // Links were added/removed from the link
            refreshSourceConnections();
        } else if (propName.equals(IGmObject.PROPERTY_LINK_TARGET)) {
            // Links were added/removed from the link
            refreshTargetConnections();
        }
    }

    @objid ("801e5bfe-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        final IFigure childFigure = ((GraphicalEditPart) childEditPart).getFigure();
        
        final PolylineConnection connection = (PolylineConnection) getFigure();
        
        ((GmNodeEditPart) childEditPart).setDragTrackerProvider(new SatelliteDragTrackerProvider(childEditPart));
        
        connection.add(childFigure, index);
        
        final GmLink gmlink = (GmLink) getModel();
        final IGmObject childModel = (IGmObject) childEditPart.getModel();
        final Locator constraint = LocatorFactory.getInstance()
                                                 .getLocator(connection,
                                                             gmlink.getLayoutContraint(childModel));
        
        this.figure.setConstraint(childFigure, constraint);
    }

    @objid ("801e5c05-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy(true));
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new DefaultDeleteLinkEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new GmLinkLayoutEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DelegatingDirectEditionEditPolicy());
        installEditPolicy("rake", new CreateRakeLinkEditPolicy());
        
        if (getRoutingMode().routingStyle != null)
            updateBendPointEditPolicies(getRoutingMode());
        
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("801e5c08-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        final IFigure connection = new RoundedLinkFigure();
        
        refreshFromStyle(connection, getModelStyle());
        return connection;
    }

    /**
     * Get the connection router registry.
     * @return the connection router registry.
     */
    @objid ("801e5c0f-1dec-11e2-8cad-001ec947c8cc")
    protected ConnectionRouterRegistry getConnectionRouterRegistry() {
        return (ConnectionRouterRegistry) this.getViewer().getProperty(ConnectionRouterRegistry.ID);
    }

    /**
     * Convenience method to get the {@link GmLink} model.
     * @return the link model.
     */
    @objid ("801e5c14-1dec-11e2-8cad-001ec947c8cc")
    protected final GmLink getLinkModel() {
        return (GmLink) getModel();
    }

    @objid ("801e5c19-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getModelSourceConnections() {
        if (getModel() instanceof IGmLinkable)
            // ugly cast...
            return (List<Object>) (Object) (((IGmLinkable) getModel()).getStartingLinks());
        else
            return Collections.emptyList();
    }

    /**
     * @return the model style.
     */
    @objid ("801e5c1f-1dec-11e2-8cad-001ec947c8cc")
    protected IStyle getModelStyle() {
        return ((GmLink) getModel()).getStyle();
    }

    @objid ("801e5c24-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getModelTargetConnections() {
        if (getModel() instanceof IGmLinkable)
            // ugly cast...
            return (List<Object>) (Object) (((IGmLinkable) getModel()).getEndingLinks());
        else
            return Collections.emptyList();
    }

    /**
     * Get the current connection routing mode.
     * @return the connection routing mode.
     */
    @objid ("801e5c2a-1dec-11e2-8cad-001ec947c8cc")
    protected RoutingMode getRoutingMode() {
        return this.currentRoutingMode;
    }

    /**
     * Refresh source and target decoration line color, width and pattern from the style
     * @param connection The figure to update, should be {@link #getFigure()}.
     * @param style The style to update from, usually {@link #getModelStyle()}
     */
    @objid ("801e5c2f-1dec-11e2-8cad-001ec947c8cc")
    protected void refreshDecorationsPenOptionsFromStyle(LinkFigure connection, IStyle style) {
        GmLink model = (GmLink) getModel();
        
        // Get style values
        int lineWidth = 1;
        LinePattern lineStyle = LinePattern.LINE_SOLID;
        Color lineColor = null;
        
        if (model.getStyleKey(MetaKey.LINECOLOR) != null)
            lineColor = style.getColor(model.getStyleKey(MetaKey.LINECOLOR));
        if (model.getStyleKey(MetaKey.LINEWIDTH) != null)
            lineWidth = style.getInteger(model.getStyleKey(MetaKey.LINEWIDTH));
        if (model.getStyleKey(MetaKey.LINEPATTERN) != null)
            lineStyle = style.getProperty(model.getStyleKey(MetaKey.LINEPATTERN));
        
        // Source decoration
        RotatableDecoration decoration = connection.getSourceDecoration();
        if (decoration instanceof IPenOptionsSupport) {
            final IPenOptionsSupport pennable = (IPenOptionsSupport) decoration;
            pennable.setLineColor(lineColor);
            pennable.setLinePattern(lineStyle);
            pennable.setLineWidth(lineWidth);
        }
        
        // Target decoration
        decoration = connection.getTargetDecoration();
        if (decoration instanceof IPenOptionsSupport) {
            final IPenOptionsSupport pennable = (IPenOptionsSupport) decoration;
            pennable.setLineColor(lineColor);
            pennable.setLinePattern(lineStyle);
            pennable.setLineWidth(lineWidth);
        }
    }

    /**
     * Refresh the figure from the given style. This implementation updates pen and brush properties if applicable.
     * StyleKey are looked up by MetaKey.
     * <p>
     * Often called in {@link #createFigure()} and after a style change.
     * @param aFigure The figure to update, should be {@link #getFigure()}.
     * @param style The style to update from, usually {@link #getModelStyle()}
     */
    @objid ("801e5c34-1dec-11e2-8cad-001ec947c8cc")
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        final GmLink gmModel = (GmLink) getModel();
        
        // Set pen properties where applicable
        if (aFigure instanceof IPenOptionsSupport) {
            IPenOptionsSupport pen = (IPenOptionsSupport) aFigure;
            if (gmModel.getStyleKey(MetaKey.FONT) != null)
                pen.setTextFont(style.getFont(gmModel.getStyleKey(MetaKey.FONT)));
            if (gmModel.getStyleKey(MetaKey.TEXTCOLOR) != null)
                pen.setTextColor(style.getColor(gmModel.getStyleKey(MetaKey.TEXTCOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINECOLOR) != null)
                pen.setLineColor(style.getColor(gmModel.getStyleKey(MetaKey.LINECOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINEWIDTH) != null)
                pen.setLineWidth(style.getInteger(gmModel.getStyleKey(MetaKey.LINEWIDTH)));
            if (gmModel.getStyleKey(MetaKey.LINEPATTERN) != null) {
                LinePattern pattern = style.getProperty(gmModel.getStyleKey(MetaKey.LINEPATTERN));
                pen.setLinePattern(pattern);
            }
        }
        
        if (aFigure instanceof LinkFigure) {
            // Refresh decorations
            refreshDecorationsPenOptionsFromStyle((LinkFigure) aFigure, style);
        
            // Refresh rounded line radius.
            if (aFigure instanceof RoundedLinkFigure) {
                final RoundedLinkFigure roundedLinkFigure = (RoundedLinkFigure) aFigure;
        
                // Line corner radius
                final StyleKey radiusStyleKey = gmModel.getStyleKey(MetaKey.LINERADIUS);
                if (radiusStyleKey != null)
                    roundedLinkFigure.setRadius(style.getInteger(radiusStyleKey));
        
                // Enable bridges on segment crossings
                final StyleKey bridgeStyleKey = gmModel.getStyleKey(MetaKey.DRAWLINEBRIDGES);
                if (bridgeStyleKey != null)
                    roundedLinkFigure.setBridgesEnabled(style.getBoolean(bridgeStyleKey));
            }
        }
        
        refreshRouterFromStyle((Connection) aFigure, style, gmModel);
    }

    @objid ("801e5c3b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        
        final GmLink gmLink = getLinkModel();
        final PolylineConnection conn = (PolylineConnection) this.getFigure();
        
        // Update the connection router & Refresh route
        updateConnectionRoute(conn);
        
        // Refresh children constraint
        for (Object c : getChildren()) {
            final GraphicalEditPart childPart = (GraphicalEditPart) c;
            final IGmObject childNode = (IGmObject) childPart.getModel();
            final IGmLocator gmLoc = gmLink.getLayoutContraint(childNode);
            final Locator loc = LocatorFactory.getInstance().getLocator(conn, gmLoc);
            if (loc != null)
                conn.setConstraint(childPart.getFigure(), loc);
        }
    }

    /**
     * This method is redefined to fix the constraint saving in the case where the child figure does not directly belong
     * to the edit part figure.
     * @see org.eclipse.gef.editparts.AbstractEditPart#reorderChild(EditPart, int)
     */
    @objid ("801e5c3e-1dec-11e2-8cad-001ec947c8cc")
    @SuppressWarnings("unchecked")
    @Override
    protected void reorderChild(final EditPart child, final int index) {
        // Save the constraint of the child so that it does not
        // get lost during the remove and re-add.
        IFigure childFigure = ((GraphicalEditPart) child).getFigure();
        LayoutManager layout = childFigure.getParent().getLayoutManager();
        Object constraint = null;
        if (layout != null)
            constraint = layout.getConstraint(childFigure);
        
        //super.reorderChild(child, index);
        // Copy of AbstractEditPart#reorderChild(EditPart, int)
        removeChildVisual(child);
        List<Object> lchildren = getChildren();
        lchildren.remove(child);
        lchildren.add(index, child);
        addChildVisual(child, index);
        
        if (constraint != null)
            setLayoutConstraint(child, childFigure, constraint);
    }

    /**
     * Change the source or destination of the link given by the request.
     * <p>
     * Unmask the element if it is not displayed in the diagram. Updates the model, the graphic model and the view.
     * @param newEndElement the new source/destination
     * @param request The reconnect request.
     */
    @objid ("801e5c48-1dec-11e2-8cad-001ec947c8cc")
    protected final void swapEnd(MObject newEndElement, ReconnectRequest request) {
        // Search all gm representing the new target
        List<GmModel> models = ((GmLink) this.getModel()).getDiagram()
                                                         .getAllGMRelatedTo(new MRef(newEndElement));
        // This boolean will be used to note that the searched End was found
        // unmasked at least once.
        boolean foundUnmaskedEnd = false;
        for (GmModel model : models) {
            // For each gm, search the corresponding edit part
            EditPart editPart = (EditPart) this.getViewer().getEditPartRegistry().get(model);
            if (editPart != null) {
                foundUnmaskedEnd = true;
                // See if this edit part accepts the reconnection request
                EditPart targetEditPart = editPart.getTargetEditPart(request);
                if (targetEditPart != null) {
                    request.setTargetEditPart(targetEditPart);
                    Command command = targetEditPart.getCommand(request);
                    if (command != null && command.canExecute()) {
                        // Found a potential new target!
                        command.execute();
                        return;
                    }
                }
            }
        }
        if (!foundUnmaskedEnd) {
            unmaskEndElement(newEndElement, request);
        }
        // Now that (hopefully) we managed to unmasked the new end, let's try to
        // reroute ourselves to it!
        models = ((GmLink) this.getModel()).getDiagram().getAllGMRelatedTo(new MRef(newEndElement));
        for (GmModel model : models) {
            // For each gm, search the corresponding edit part
            EditPart editPart = (EditPart) this.getViewer().getEditPartRegistry().get(model);
            if (editPart != null) {
                // See if this edit part accepts the reconnection request
                EditPart targetEditPart = editPart.getTargetEditPart(request);
                if (targetEditPart != null) {
                    request.setTargetEditPart(targetEditPart);
                    Command command = targetEditPart.getCommand(request);
                    if (command != null && command.canExecute()) {
                        // Found a potential new target!
                        command.execute();
                        return;
                    }
                }
            }
        }
    }

    @objid ("8020be57-1dec-11e2-8cad-001ec947c8cc")
    private boolean containsAbsolutePoint(final GraphicalEditPart editPart, final Point aPoint) {
        final IFigure fig = editPart.getFigure();
        final Point p = aPoint.getCopy();
        fig.translateToRelative(p);
        return fig.containsPoint(p);
    }

    @objid ("8020be63-1dec-11e2-8cad-001ec947c8cc")
    private void refreshRakeModeOnSource() {
        final GmLink gm = getLinkModel();
        final IGmPath oldPath = gm.getPath();
        final IGmLinkRake sourceRake = oldPath.getSourceRake();
        if (sourceRake != null && sourceRake.getSharedAnchor() != oldPath.getSourceAnchor()) {
        
            OrthoConnectionHelper connectionPath = new OrthoConnectionHelper(getConnectionFigure());
            Object pathData = GmPathDataExtractor.extractDataModel(connectionPath);
        
            GmPath path = new GmPath(oldPath);
            path.setRouterKind(ConnectionRouterId.ORTHOGONAL);
            path.setSourceRake(null);
            path.setTargetRake(null);
            path.setPathData(pathData);
            gm.setLayoutData(path);
        }
    }

    @objid ("8020be65-1dec-11e2-8cad-001ec947c8cc")
    private void refreshRakeModeOnTarget() {
        final GmLink gm = getLinkModel();
        final IGmPath oldPath = gm.getPath();
        final IGmLinkRake targetRake = oldPath.getTargetRake();
        if (targetRake != null && targetRake.getSharedAnchor() != oldPath.getTargetAnchor()) {
        
            OrthoConnectionHelper connectionPath = new OrthoConnectionHelper(getConnectionFigure());
            Object pathData = GmPathDataExtractor.extractDataModel(connectionPath);
        
            GmPath path = new GmPath(oldPath);
            path.setRouterKind(ConnectionRouterId.ORTHOGONAL);
            path.setSourceRake(null);
            path.setTargetRake(null);
            path.setPathData(pathData);
            gm.setLayoutData(path);
        }
    }

    @objid ("8020be67-1dec-11e2-8cad-001ec947c8cc")
    private void refreshRouterFromStyle(final Connection connectionFigure, final IStyle style, final GmLink gmLink) {
        // Update the connection router if changed from the style.
        final StyleKey routerStyleKey = gmLink.getStyleKey(MetaKey.CONNECTIONROUTER);
        if (routerStyleKey != null) {
            final ConnectionRouterId styleRouter = style.getProperty(routerStyleKey);
            final ConnectionRouterId oldRouter = gmLink.getPath().getRouterKind();
            if (styleRouter != oldRouter) {
                final GmPath newPath = new GmPath(gmLink.getPath());
                newPath.setRouterKind(styleRouter);
        
                IConnectionHelper oldHelper = ConnectionHelperFactory.createFromSerializedData(oldRouter,
                                                                                               gmLink.getPath()
                                                                                                     .getPathData(),
                                                                                               connectionFigure);
                IConnectionHelper newHelper = ConnectionHelperFactory.convert(oldHelper,
                                                                              styleRouter,
                                                                              connectionFigure);
        
                newPath.setPathData(GmPathDataExtractor.extractDataModel(newHelper));
        
                if (newHelper.getRoutingMode() != ConnectionRouterId.ORTHOGONAL) {
                    newPath.setSourceRake(null);
                    newPath.setTargetRake(null);
                }
                
                gmLink.setLayoutData(newPath);
            }
        }
    }

    @objid ("8020be71-1dec-11e2-8cad-001ec947c8cc")
    private void unmaskEndElement(final MObject newEndElement, final ReconnectRequest request) {
        // The searched end was not found in the diagram (either there is no
        // Gm for it, or the Gm is "not visible" in that it doesn"t have an
        // EditPart).
        // => We will try to unmasked the searched end at roughly the same
        // place as the previous end.
        ModelElementDropRequest dropRequest = new ModelElementDropRequest();
        dropRequest.setDroppedElements(new MObject[] { newEndElement });
        Point dropPoint;
        if (request.getType().equals(REQ_RECONNECT_SOURCE)) {
            ((GraphicalEditPart) getRoot().getContents()).getFigure().invalidateTree();
            ((GraphicalEditPart) getRoot().getContents()).getFigure().validate();
            IFigure sourceFigure = ((GraphicalEditPart) this.getSource()).getFigure();
            dropPoint = sourceFigure.getBounds().getLocation().getTranslated(20, 20);
            sourceFigure.translateToAbsolute(dropPoint);
        } else {
            ((GraphicalEditPart) getRoot().getContents()).getFigure().invalidateTree();
            ((GraphicalEditPart) getRoot().getContents()).getFigure().validate();
            IFigure targetFigure = ((GraphicalEditPart) this.getTarget()).getFigure();
            dropPoint = targetFigure.getBounds().getLocation().getTranslated(20, 20);
            targetFigure.translateToAbsolute(dropPoint);
        }
        dropRequest.setLocation(dropPoint);
        GmCompositeNode gmCompositeForUnmasking = ((GmLink) this.getModel()).getDiagram()
                                                                            .getCompositeFor(newEndElement.getClass());
        EditPart compositeEditPartForUnmasking = (EditPart) getViewer().getEditPartRegistry()
                                                                       .get(gmCompositeForUnmasking);
        if (compositeEditPartForUnmasking == null)
            return;
        compositeEditPartForUnmasking = compositeEditPartForUnmasking.getTargetEditPart(dropRequest);
        Command command = compositeEditPartForUnmasking.getCommand(dropRequest);
        if (command == null || !command.canExecute())
            return;
        command.execute();
    }

    /**
     * Add an edit policy to edit bend points if the router handles bend point editing.
     */
    @objid ("8020be79-1dec-11e2-8cad-001ec947c8cc")
    private void updateBendPointEditPolicies(final RoutingMode mode) {
        removeEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE);
        
        if (mode.rake) {
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new RakeLinkEditPolicy());
        } else
            switch (mode.routingStyle) {
                case DIRECT:
                    break;
                case BENDPOINT:
                    installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new DefaultBendpointEditPolicy());
                    break;
                case ORTHOGONAL:
                    installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new OrthoBendpointEditPolicy());
                    break;
                default:
                    throw new IllegalStateException(getRoutingMode() + " routing mode not supported");
            }
    }

    /**
     * Update the connection router, the edit policies and the drag tracker from the model routing style.
     * @param cnx The connection figure
     */
    @objid ("8020be7e-1dec-11e2-8cad-001ec947c8cc")
    private void updateConnectionRoute(final Connection cnx) {
        // Refresh anchors
        refreshSourceAnchor();
        refreshTargetAnchor();
        
        final IGmLink gmLink = this.getLinkModel();
        final RoutingMode newRoutingMode = new RoutingMode(gmLink.getPath());
        final RoutingMode oldRoutingMode = getRoutingMode();
        // Change connection router if the rake mode changes or there is no rake and the style changes
        if (oldRoutingMode.rake != newRoutingMode.rake ||
            (!newRoutingMode.rake && oldRoutingMode.routingStyle != newRoutingMode.routingStyle)) {
            // Set the connection router
            if (newRoutingMode.rake)
                cnx.setConnectionRouter(rakeRouter);
            else
                cnx.setConnectionRouter(getConnectionRouterRegistry().get(newRoutingMode.routingStyle));
        
            // Set the new constraint
            IConnectionHelper helper = ConnectionHelperFactory.createFromSerializedData(newRoutingMode.routingStyle,
                                                                                        this.getLinkModel()
                                                                                            .getPath()
                                                                                            .getPathData(),
                                                                                        cnx);
            cnx.setRoutingConstraint(helper.getRoutingConstraint());
        
            // Update edit policy
            updateBendPointEditPolicies(newRoutingMode);
        
            this.currentRoutingMode = newRoutingMode;
        
        } else {
            IConnectionHelper helper = ConnectionHelperFactory.createFromSerializedData(newRoutingMode.routingStyle,
                                                                                        this.getLinkModel()
                                                                                            .getPath()
                                                                                            .getPathData(),
                                                                                        cnx);
            cnx.setRoutingConstraint(helper.getRoutingConstraint());
        }
    }

    /**
     * Represents the routing mode of the link.
     * 
     * @author cmarin
     */
    @objid ("8020be85-1dec-11e2-8cad-001ec947c8cc")
    private static class RoutingMode {
        @objid ("8020be88-1dec-11e2-8cad-001ec947c8cc")
        public boolean rake = false;

        @objid ("8e6298a4-1e83-11e2-8cad-001ec947c8cc")
        public ConnectionRouterId routingStyle = null;

        @objid ("802320ac-1dec-11e2-8cad-001ec947c8cc")
        public RoutingMode() {
        }

        @objid ("802320ae-1dec-11e2-8cad-001ec947c8cc")
        public RoutingMode(final IGmPath path) {
            this.routingStyle = path.getRouterKind();
            this.rake = path.getSourceRake() != null || path.getTargetRake() != null;
        }

        @objid ("802320b2-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (this.rake ? 1231 : 1237);
            result = prime * result + ((this.routingStyle == null) ? 0 : this.routingStyle.hashCode());
            return result;
        }

        @objid ("802320b7-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            RoutingMode other = (RoutingMode) obj;
            if (this.rake != other.rake) {
                return false;
            }
            if (this.routingStyle == null) {
                if (other.routingStyle != null) {
                    return false;
                }
            } else if (!this.routingStyle.equals(other.routingStyle)) {
                return false;
            }
            return true;
        }

    }

}
