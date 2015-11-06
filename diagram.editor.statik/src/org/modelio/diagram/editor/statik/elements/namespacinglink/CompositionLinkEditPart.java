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
                                    

package org.modelio.diagram.editor.statik.elements.namespacinglink;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
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
import org.modelio.diagram.elements.core.figures.IBrushOptionsSupport;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.figures.LinkFigure;
import org.modelio.diagram.elements.core.figures.RoundedLinkFigure;
import org.modelio.diagram.elements.core.figures.anchors.LinkAnchor;
import org.modelio.diagram.elements.core.link.ConnectionRouterRegistry;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.GmLinkLayoutEditPolicy;
import org.modelio.diagram.elements.core.link.GmPath;
import org.modelio.diagram.elements.core.link.IAnchorModelProvider;
import org.modelio.diagram.elements.core.link.SelectConnectionEditPartTracker;
import org.modelio.diagram.elements.core.link.extensions.GmFractionalConnectionLocator;
import org.modelio.diagram.elements.core.link.extensions.IGmLocator;
import org.modelio.diagram.elements.core.link.extensions.LocatorFactory;
import org.modelio.diagram.elements.core.link.ortho.OrthoBendpointEditPolicy;
import org.modelio.diagram.elements.core.link.path.ConnectionHelperFactory;
import org.modelio.diagram.elements.core.link.path.GmPathDataExtractor;
import org.modelio.diagram.elements.core.link.path.IConnectionHelper;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmLink;
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
 * Edit part for the GmCompositionLink
 * 
 * @author fpoyer
 */
@objid ("35a670c0-55b7-11e2-877f-002564c97630")
public class CompositionLinkEditPart extends AbstractConnectionEditPart implements PropertyChangeListener, IAnchorModelProvider {
    @objid ("35a670c4-55b7-11e2-877f-002564c97630")
    private RoutingMode currentRoutingMode = new RoutingMode();

    @objid ("35a670c5-55b7-11e2-877f-002564c97630")
    protected void refreshFromStyle(final IFigure aFigure, final IStyle style) {
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
        refreshDecorationFromStyle((LinkFigure) aFigure, style);
    }

    @objid ("35a7f73c-55b7-11e2-877f-002564c97630")
    protected void refreshDecorationFromStyle(final LinkFigure connection, final IStyle style) {
        GmCompositionLink model = (GmCompositionLink) getModel();
        // Source decoration
        RotatableDecoration decoration = connection.getSourceDecoration();
        if (decoration != null) {
            final IPenOptionsSupport pennable = (IPenOptionsSupport) decoration;
            pennable.setLineWidth(style.getInteger(model.getStyleKey(MetaKey.LINEWIDTH)));
            pennable.setLineColor(style.getColor(model.getStyleKey(MetaKey.LINECOLOR)));
            final IBrushOptionsSupport brushable = (IBrushOptionsSupport) decoration;
            brushable.setFillColor(style.getColor(model.getStyleKey(MetaKey.FILLCOLOR)));
        
        }
    }

    @objid ("35a7f746-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        PolylineConnection fig = new RoundedLinkFigure();
        CircleDeco dec = new CircleDeco();
        dec.setOpaque(true);
        fig.setSourceDecoration(dec);
        refreshFromStyle(fig, getModelStyle());
        return fig;
    }

    @objid ("35a7f74b-55b7-11e2-877f-002564c97630")
    @Override
    public void activate() {
        super.activate();
        
        final GmLink gmLink = (GmLink) getModel();
        gmLink.addPropertyChangeListener(this);
    }

    @objid ("35a7f74e-55b7-11e2-877f-002564c97630")
    @Override
    public Object createAnchorModel(final ConnectionAnchor anchor) {
        return null;
    }

    @objid ("35a7f755-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(final Class adapter) {
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

    @objid ("35a7f75c-55b7-11e2-877f-002564c97630")
    @Override
    public DragTracker getDragTracker(final Request req) {
        return new SelectConnectionEditPartTracker(this);
    }

    @objid ("35a7f763-55b7-11e2-877f-002564c97630")
    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getModelChildren() {
        final ArrayList<Object> ret = new ArrayList<>(8);
        final GmLink link = (GmLink) getModel();
        
        ret.addAll(link.getVisibleExtensions());
        ret.addAll(super.getModelChildren());
        return ret;
    }

    @objid ("35a7f76a-55b7-11e2-877f-002564c97630")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(final ConnectionEditPart connection) {
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("35a7f771-55b7-11e2-877f-002564c97630")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(final Request request) {
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("35a97dda-55b7-11e2-877f-002564c97630")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final ConnectionEditPart connection) {
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("35a97de1-55b7-11e2-877f-002564c97630")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final Request request) {
        return new LinkAnchor(getConnectionFigure(), new GmFractionalConnectionLocator(0.5, 0, 0));
    }

    @objid ("35a97de8-55b7-11e2-877f-002564c97630")
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

    @objid ("35a97ded-55b7-11e2-877f-002564c97630")
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final String propName = evt.getPropertyName();
        
        if (propName.equals(IGmObject.PROPERTY_LAYOUTDATA)) {
            // Link layout (bendpoints) update
        
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
        } else if (propName.equals(IGmObject.PROPERTY_LINK_SOURCE)) {
            // Links were added/removed from the link
            refreshSourceConnections();
        } else if (propName.equals(IGmObject.PROPERTY_LINK_TARGET)) {
            // Links were added/removed from the link
            refreshTargetConnections();
        }
    }

    @objid ("35a97df2-55b7-11e2-877f-002564c97630")
    @Override
    protected void addChildVisual(final EditPart childEditPart, final int index) {
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

    @objid ("35a97df9-55b7-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy(true));
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new DefaultDeleteLinkEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new GmLinkLayoutEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DelegatingDirectEditionEditPolicy());
        
        if (getRoutingMode().routingStyle != null)
            updateBendPointEditPolicies(getRoutingMode());
        
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("35a97dfc-55b7-11e2-877f-002564c97630")
    protected ConnectionRouterRegistry getConnectionRouterRegistry() {
        return (ConnectionRouterRegistry) this.getViewer().getProperty(ConnectionRouterRegistry.ID);
    }

    @objid ("35a97e02-55b7-11e2-877f-002564c97630")
    protected final GmLink getLinkModel() {
        return (GmLink) getModel();
    }

    @objid ("35a97e08-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getModelSourceConnections() {
        if (getModel() instanceof IGmLinkable)
            // ugly cast...
            return (List<Object>) (Object) (((IGmLinkable) getModel()).getStartingLinks());
        else
            return Collections.emptyList();
    }

    @objid ("35a97e0e-55b7-11e2-877f-002564c97630")
    protected IStyle getModelStyle() {
        return ((GmLink) getModel()).getStyle();
    }

    @objid ("35ab047d-55b7-11e2-877f-002564c97630")
    @SuppressWarnings("unchecked")
    @Override
    protected List<Object> getModelTargetConnections() {
        if (getModel() instanceof IGmLinkable)
            // ugly cast...
            return (List<Object>) (Object) (((IGmLinkable) getModel()).getEndingLinks());
        else
            return Collections.emptyList();
    }

    @objid ("35ab0483-55b7-11e2-877f-002564c97630")
    protected RoutingMode getRoutingMode() {
        return this.currentRoutingMode;
    }

    @objid ("35ab0487-55b7-11e2-877f-002564c97630")
    protected void refreshDecorationsPenOptionsFromStyle(final LinkFigure connection, final IStyle style) {
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

    @objid ("35ab0491-55b7-11e2-877f-002564c97630")
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

    @objid ("35ab0494-55b7-11e2-877f-002564c97630")
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

    @objid ("35ab049b-55b7-11e2-877f-002564c97630")
    protected final void swapEnd(final MObject newEndElement, final ReconnectRequest request) {
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

    @objid ("35ab04a3-55b7-11e2-877f-002564c97630")
    private boolean containsAbsolutePoint(final GraphicalEditPart editPart, final Point aPoint) {
        final IFigure fig = editPart.getFigure();
        final Point p = aPoint.getCopy();
        fig.translateToRelative(p);
        return fig.containsPoint(p);
    }

    @objid ("35ab04ab-55b7-11e2-877f-002564c97630")
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
        
                gmLink.setLayoutData(newPath);
            }
        }
    }

    @objid ("35ac8b1b-55b7-11e2-877f-002564c97630")
    private void updateBendPointEditPolicies(final RoutingMode mode) {
        removeEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE);
        
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

    @objid ("35ac8b1f-55b7-11e2-877f-002564c97630")
    private void updateConnectionRoute(final Connection cnx) {
        // Refresh anchors
        refreshSourceAnchor();
        refreshTargetAnchor();
        
        final IGmLink gmLink = this.getLinkModel();
        final RoutingMode newRoutingMode = new RoutingMode(gmLink.getPath());
        final RoutingMode oldRoutingMode = getRoutingMode();
        // Change connection router if the rake mode changes or there is no rake and the style changes
        if (oldRoutingMode.routingStyle != newRoutingMode.routingStyle) {
            // Set the connection router
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

    @objid ("35ac8b23-55b7-11e2-877f-002564c97630")
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

    @objid ("35ac8b2b-55b7-11e2-877f-002564c97630")
    private static class RoutingMode {
        @objid ("35ac8b2d-55b7-11e2-877f-002564c97630")
        public ConnectionRouterId routingStyle = null;

        @objid ("35ac8b30-55b7-11e2-877f-002564c97630")
        public RoutingMode() {
        }

        @objid ("35ac8b32-55b7-11e2-877f-002564c97630")
        public RoutingMode(final IGmPath path) {
            this.routingStyle = path.getRouterKind();
        }

        @objid ("35ac8b38-55b7-11e2-877f-002564c97630")
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((this.routingStyle == null) ? 0 : this.routingStyle.hashCode());
            return result;
        }

        @objid ("35ac8b3d-55b7-11e2-877f-002564c97630")
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
