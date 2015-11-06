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
                                    

package org.modelio.diagram.elements.core.node;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.LocationRequest;
import org.modelio.diagram.elements.core.commands.FitToMinSizeCommand;
import org.modelio.diagram.elements.core.figures.IBrushOptionsSupport;
import org.modelio.diagram.elements.core.figures.IPenOptionsSupport;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.IAnchorModelProvider;
import org.modelio.diagram.elements.core.link.extensions.IGmLocator;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmObject;
import org.modelio.diagram.elements.core.policies.DefaultDeleteNodeEditPolicy;
import org.modelio.diagram.elements.core.policies.DefaultElementDropEditPolicy;
import org.modelio.diagram.elements.core.policies.DelegatingDirectEditionEditPolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.diagram.styles.core.StyleKey.FillMode;
import org.modelio.diagram.styles.core.StyleKey.LinePattern;
import org.modelio.diagram.styles.core.StyleKey.RepresentationMode;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Edit parts for {@link GmNodeModel nodes} representing {@link MObject Elements}.
 */
@objid ("8090cd05-1dec-11e2-8cad-001ec947c8cc")
public abstract class GmNodeEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener, IAnchorModelProvider {
    @objid ("9219e2f1-1e83-11e2-8cad-001ec947c8cc")
    private RepresentationMode initialRepMode = null;

    @objid ("8090cd09-1dec-11e2-8cad-001ec947c8cc")
    private IDragTrackerProvider dragTrackerProvider;

    /**
     * Constructor.
     */
    @objid ("8090cd0b-1dec-11e2-8cad-001ec947c8cc")
    public GmNodeEditPart() {
        super();
        this.dragTrackerProvider = new DefaultDragTrackerProvider(this);
    }

    @objid ("8090cd0e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void activate() {
        super.activate();
        ((GmNodeModel) getModel()).addPropertyChangeListener(this);
    }

    /**
     * Create a serializable anchor model from the given anchor.
     * @param anchor a figure anchor
     * @return an anchor model.
     */
    @objid ("8090cd11-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object createAnchorModel(final ConnectionAnchor anchor) {
        return RectangleNodeAnchorProvider.get().createAnchorModel(anchor);
    }

    @objid ("8090cd1b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void deactivate() {
        super.deactivate();
        ((GmNodeModel) getModel()).removePropertyChangeListener(this);
    }

    /**
     * Returns an object which is an instance of the given class associated with this object. Returns <code>null</code>
     * if no such object can be found.
     * <p>
     * Extends {@link AbstractGraphicalEditPart#getAdapter(Class)} to support {@link Element}, {@link IGmObject},
     * {@link GmModel} and their subclasses.
     * @see IAdaptable#getAdapter(Class)
     * @param adapter the adapter class to look up
     * @return a object castable to the given class, or <code>null</code> if this object does not have an adapter for
     * the given class
     */
    @objid ("8090cd1e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        final Object model = getModel();
        
        // Support IGmObject, GmModel and its subclasses
        if (adapter.isInstance(model))
            return model;
        
        // Support ObElement & subclasses
        if (model instanceof GmModel) {
            final GmModel gmModel = (GmModel) model;
            final MObject obElement = gmModel.getRelatedElement();
            if (adapter.isInstance(obElement))
                return obElement;
        }
        return super.getAdapter(adapter);
    }

    @objid ("8090cd26-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public DragTracker getDragTracker(Request request) {
        return this.dragTrackerProvider.getDragTracker(request);
    }

    /**
     * <p>
     * This method must return a policy handling the kind of request passed as parameter <em>IF (and only if)</em> it
     * has specific geometric needs. This policy will be installed by the EditPart containing this node with the
     * DRAG_ROLE role.
     * </p>
     * <p>
     * If this node has no specific needs, it can return <code>null</code> which indicates to the parent that the
     * default behavior should be used.
     * </p>
     * @param requestType the type of request the returned policy must handle.
     * @return a policy able to handle the passed type of request or <code>null</code>.
     */
    @objid ("8090cd30-1dec-11e2-8cad-001ec947c8cc")
    public SelectionEditPolicy getPreferredDragRolePolicy(String requestType) {
        assert (RequestConstants.REQ_SELECTION.equals(requestType) ||
                RequestConstants.REQ_MOVE.equals(requestType) || RequestConstants.REQ_RESIZE.equals(requestType)) : "GmNodeEditPart#getPreferredDragRolePolicy: Unhandled request type";
        return null;
    }

    @objid ("80932f32-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return RectangleNodeAnchorProvider.get().getSourceConnectionAnchor(this, connection);
    }

    @objid ("80932f3c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return RectangleNodeAnchorProvider.get().getSourceConnectionAnchor(this, request);
    }

    @objid ("80932f46-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(final ConnectionEditPart connectionPart) {
        return RectangleNodeAnchorProvider.get().getTargetConnectionAnchor(this, connectionPart);
    }

    @objid ("80932f51-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return RectangleNodeAnchorProvider.get().getTargetConnectionAnchor(this, request);
    }

    /**
     * Overridden to add a specific behavior on {@link RequestConstants#REQ_DIRECT_EDIT DIRECT_EDIT} request: the
     * request if forwarded to all children edit parts until one understand it, and then said child edit part is asked
     * to perform the request.
     */
    @objid ("80932f5b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void performRequest(Request req) {
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

    @objid ("80932f62-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LINK_SOURCE)) {
            refreshSourceConnections();
        }
        
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LINK_TARGET)) {
            refreshTargetConnections();
        }
        
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_CHILDREN)) {
            refreshChildren();
        }
        
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_LAYOUTDATA)) {
            refreshVisuals();
        }
        
        if (evt.getPropertyName().equals(IGmObject.PROPERTY_STYLE)) {
            refreshFromStyle(getFigure(), (IStyle) evt.getNewValue());
        }
    }

    /**
     * Changes the current {@link IDragTrackerProvider} used by this edit part.
     * @param value the new {@link IDragTrackerProvider} to be used by this edit part.
     */
    @objid ("80932f66-1dec-11e2-8cad-001ec947c8cc")
    public void setDragTrackerProvider(IDragTrackerProvider value) {
        // Automatically generated method. Please delete this comment before
        // entering specific code.
        this.dragTrackerProvider = value;
    }

    @objid ("80932f6a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void setModel(final Object model) {
        super.setModel(model);
        
        // Set the initial representation mode
        GmNodeModel gmAbstractObject = (GmNodeModel) model;
        this.initialRepMode = gmAbstractObject.getRepresentationMode();
        if (this.initialRepMode == null)
            throw new IllegalStateException("No initial representation mode on" + gmAbstractObject);
    }

    @objid ("80932f6f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        // installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GraphicBoxSelectionPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new DelegatingDirectEditionEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultDeleteNodeEditPolicy());
        installEditPolicy(ModelElementDropRequest.TYPE, new DefaultElementDropEditPolicy());
    }

    @objid ("80932f72-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        return null;
    }

    @objid ("8095918f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> getModelChildren() {
        final GmNodeModel gmNodeModel = (GmNodeModel) getModel();
        
        // Only visible composite nodes have children
        if (gmNodeModel.isVisible() && gmNodeModel instanceof GmCompositeNode) {
            // Filter visible children
            return ((GmCompositeNode) gmNodeModel).getVisibleChildren();
        } else {
            return Collections.emptyList();
        }
    }

    @objid ("80959195-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> getModelSourceConnections() {
        if (getModel() instanceof GmNodeModel) {
            return ((GmNodeModel) getModel()).getStartingLinks();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Convenience method to retrieve the model style.
     * @return the model style.
     */
    @objid ("8095919b-1dec-11e2-8cad-001ec947c8cc")
    protected IStyle getModelStyle() {
        return ((IGmObject) getModel()).getStyle();
    }

    @objid ("809591a0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected List<?> getModelTargetConnections() {
        if (getModel() instanceof GmNodeModel)
            return ((GmNodeModel) getModel()).getEndingLinks();
        else
            return Collections.emptyList();
    }

    /**
     * Refresh the figure from the given style. This implementation updates pen and brush properties if applicable.
     * StyleKey are looked up by MetaKey.
     * <p>
     * Often called in {@link #createFigure()} and after a style change.
     * @param aFigure The figure to update, should be {@link #getFigure()}.
     * @param style The style to update from, usually {@link #getModelStyle()}
     */
    @objid ("809591a6-1dec-11e2-8cad-001ec947c8cc")
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        final GmModel gmModel = (GmModel) getModel();
        
        // Set pen properties where applicable
        if (aFigure instanceof IPenOptionsSupport) {
            final IPenOptionsSupport pen = (IPenOptionsSupport) aFigure;
            if (gmModel.getStyleKey(MetaKey.FONT) != null)
                pen.setTextFont(style.getFont(gmModel.getStyleKey(MetaKey.FONT)));
            if (gmModel.getStyleKey(MetaKey.TEXTCOLOR) != null)
                pen.setTextColor(style.getColor(gmModel.getStyleKey(MetaKey.TEXTCOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINECOLOR) != null)
                pen.setLineColor(style.getColor(gmModel.getStyleKey(MetaKey.LINECOLOR)));
            if (gmModel.getStyleKey(MetaKey.LINEWIDTH) != null)
                pen.setLineWidth(style.getInteger(gmModel.getStyleKey(MetaKey.LINEWIDTH)));
            if (gmModel.getStyleKey(MetaKey.LINEPATTERN) != null) {
                LinePattern linePattern = style.getProperty(gmModel.getStyleKey(MetaKey.LINEPATTERN));
                pen.setLinePattern(linePattern);
            }
        }
        
        // Set brush properties where applicable
        if (aFigure instanceof IBrushOptionsSupport) {
            final IBrushOptionsSupport brush = (IBrushOptionsSupport) aFigure;
        
            if (gmModel.getStyleKey(MetaKey.FILLCOLOR) != null)
                brush.setFillColor(style.getColor(gmModel.getStyleKey(MetaKey.FILLCOLOR)));
        
            if (gmModel.getStyleKey(MetaKey.FILLMODE) != null) {
                switch ((FillMode) style.getProperty(gmModel.getStyleKey(MetaKey.FILLMODE))) {
                    case GRADIENT:
                        brush.setUseGradient(true);
                        break;
                    case SOLID:
                        brush.setUseGradient(false);
                        break;
                    case TRANSPARENT:
                        brush.setFillColor(null);
                        break;
                }
            }
        }
    }

    /**
     * This convenience method tests if the representation must be switched from/to image stereotype mode and does the
     * switch if needed.
     * <p>
     * It switches the representation by removing and adding again the gm element from its parent.<br>
     * Doing this makes the EditPart killing itself and call the ModelioEditPartFactory.
     * @return true if the representation was swapped, false if it didn't change.
     */
    @objid ("809591ad-1dec-11e2-8cad-001ec947c8cc")
    protected boolean switchRepresentationMode() {
        final EditPart parentEditPart = getParent();
        if (parentEditPart == null) {
            return false;
        }
        final GmNodeModel gmModel = (GmNodeModel) getModel();
        
        final RepresentationMode askedMode = gmModel.getRepresentationMode();
        
        if (askedMode != this.initialRepMode) {
            final GmCompositeNode parentNode = gmModel.getParentNode();
            final GmLink parentLink = gmModel.getParentLink();
            if (parentNode != null) {
                if (parentEditPart instanceof GmNodeEditPart) {
                    // This will "delete" the current edit part.
                    ((GmNodeEditPart) parentEditPart).removeChild(this);
                    // This will invoke the ModelioEditPartFactory that will create another edit part.
                    parentEditPart.refresh();
                } else {
                    // Keep the index of the child avoids problems with positional read
                    int index = parentNode.getChildIndex(gmModel);
                    // This will "delete" the current edit part (AND modify the GM model).
                    parentNode.removeChild(gmModel);
                    // This will restore the GM model to its original state and invoke the ModelioEditPartFactory that will create another edit part.
                    parentNode.addChild(gmModel, index);
                }
        
                final EditPart newEditPart = (EditPart) parentEditPart.getViewer()
                                                                      .getEditPartRegistry()
                                                                      .get(gmModel);
                autoSizeNode(newEditPart);
            } else if (parentLink != null) {
                final IGmLocator constraint = parentLink.getLayoutContraint(gmModel);
        
                // This will "delete" the current edit part.
                parentLink.removeExtension(gmModel);
        
                // This will invoke the ModelioEditPartFactory that will
                // create another edit part.
                parentLink.addExtension(gmModel, constraint);
        
            }
        
            return true;
        }
        return false;
    }

    @objid ("809591ba-1dec-11e2-8cad-001ec947c8cc")
    private void autoSizeNode(final EditPart newEditPart) {
        // Look for an edit part in the parent hierarchy that understands resize requests.
        final ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE);
        req.setEditParts(newEditPart);
        req.setSizeDelta(new Dimension(-1, -1));
        
        EditPart editPart = newEditPart;
        while (editPart != null && !editPart.understandsRequest(req)) {
            editPart = editPart.getParent();
            req.setEditParts(newEditPart);
        }
        
        if (editPart != null) {
            final GraphicalEditPart graphicEditPart = (GraphicalEditPart) editPart;
        
            // Force layout so that child figures on Port container have valid bounds needed by 
            // XYLayoutEditPolicy.getConstraintFor(ChangeBoundsRequest , GraphicalEditPart ) .
            graphicEditPart.refresh();
            graphicEditPart.getFigure().getUpdateManager().performValidation();
        
            // Run fit to content to the found edit part.
            new FitToMinSizeCommand(graphicEditPart).execute();
        }
    }

    /**
     * Tells whether the figure of the given edit part contains the given point.
     * @param editPart A graphic edit part
     * @param aPoint a point in absolute coordinates
     * @return <i>true</i> if the edit part figure contains the point, else <i>false</i>
     */
    @objid ("809591c0-1dec-11e2-8cad-001ec947c8cc")
    private boolean containsAbsolutePoint(GraphicalEditPart editPart, Point aPoint) {
        final IFigure fig = editPart.getFigure();
        final Point p = aPoint.getCopy();
        fig.translateToRelative(p);
        return fig.containsPoint(p);
    }

    @objid ("809591cb-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void removeNotify() {
        // If this node is becoming invisible, delete all links to and from it.
        if (!((GmNodeModel) getModel()).isVisible()) {
            for (Object obj : getSourceConnections()) {
                ConnectionEditPart connection = (ConnectionEditPart) obj;
                ((IGmObject) connection.getModel()).delete();
            }
            for (Object obj : getTargetConnections()) {
                ConnectionEditPart connection = (ConnectionEditPart) obj;
                ((IGmObject) connection.getModel()).delete();
            }
        }
        super.removeNotify();
    }

}
