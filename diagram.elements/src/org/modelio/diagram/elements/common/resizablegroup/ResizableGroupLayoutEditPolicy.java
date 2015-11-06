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
                                    

package org.modelio.diagram.elements.common.resizablegroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transposer;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.LayoutEditPolicy;
import org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.DropRequest;
import org.modelio.diagram.elements.core.commands.DefaultReparentElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * An EditPolicy for use with {@link ResizableGroupLayout}.
 * <p>
 * This EditPolicy knows how to map an <x,y> coordinate on the layout container to the appropriate index for the operation being
 * performed. It also shows target feedback consisting of an insertion line at the appropriate location.
 * 
 * 
 * @author fpoyer
 */
@objid ("7f0e9029-1dec-11e2-8cad-001ec947c8cc")
public class ResizableGroupLayoutEditPolicy extends OrderedLayoutEditPolicy {
    @objid ("00ddff29-87e8-42e4-8011-cee9483dc990")
    private Polyline insertionLine;

    @objid ("7e9ab84e-0268-477d-a01a-aa6d77e4473c")
    private static final Dimension DEFAULT_SIZE = new Dimension(-1, -1);

    @objid ("7f0e9034-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType() instanceof String) {
            final ModelioCreationContext ctx = (ModelioCreationContext) request.getNewObject();
            final EditPart insertAfter = getInsertionReference(request);
            final GmNodeModel insertAfterModel = (insertAfter == null ? null : (GmNodeModel) insertAfter.getModel());
        
            getHostFigure().revalidate();
        
            int constraint = -1;
            final Dimension containerDimension = getHostFigure().getSize();
            if (isHorizontal() && containerDimension.width != 0) {
                constraint = containerDimension.width / (getHostFigure().getChildren().size() + 1);
            } else if (!isHorizontal() && containerDimension.height != 0) {
                constraint = containerDimension.height / (getHostFigure().getChildren().size() + 1);
            } else {
                constraint = -1;
            }
            return new AddChildToGroupCommand(getHost(), ctx, insertAfterModel, constraint);
        } else {
            return null;
        }
    }

    @objid ("7f10f241-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command createMoveChildCommand(EditPart child, EditPart after) {
        // if child is a 'node' it usually can be resized and/or moved
        if (child instanceof GmNodeEditPart) {
            GmNodeModel reference = null;
            if (after != null)
                reference = (GmNodeModel) after.getModel();
            ReorderChildrenCommand command = new ReorderChildrenCommand(getHostCompositeNode(), (GmNodeModel) child.getModel(),
                    reference);
            return command;
        }
        return null;
    }

    @objid ("7f10f24e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command createAddCommand(EditPart child, EditPart after) {
        if (!getHostCompositeNode().allowsMove()) {
            return null;
        }
        // First re-parent, then put at the correct place.
        // TODO: could probably be done better with a single dedicated
        // command...
        GmNodeModel reference = null;
        if (after != null)
            reference = (GmNodeModel) after.getModel();
        
        CompoundCommand compound = new CompoundCommand();
        compound.add(new DefaultReparentElementCommand(getHostElement(), getHostCompositeNode(), (GmNodeModel) child.getModel(),
                ((GmNodeModel) child.getModel()).getLayoutData()));
        compound.add(new ReorderChildrenCommand(getHostCompositeNode(), (GmNodeModel) child.getModel(), reference));
        return compound;
    }

    /**
     * @return the {@link GmResizableGroup label} model of the host edit part.
     */
    @objid ("7f10f25b-1dec-11e2-8cad-001ec947c8cc")
    private GmResizableGroup getHostCompositeNode() {
        return (GmResizableGroup) this.getHost().getModel();
    }

    /**
     * @return the element represented.
     */
    @objid ("7f10f260-1dec-11e2-8cad-001ec947c8cc")
    protected MObject getHostElement() {
        MObject hostElement = getHostCompositeNode().getRelatedElement();
        // Watch out for container being on the diagram background: we
        // actually want the context of the diagram.
        if (hostElement instanceof AbstractDiagram) {
            hostElement = ((AbstractDiagram) hostElement).getOrigin();
        }
        return hostElement;
    }

    @objid ("7f10f265-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            CreateRequest createRequest = (CreateRequest) request;
            return getTargetEditPart(createRequest);
        }
        if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType()) || REQ_CLONE.equals(request.getType())) {
            // Special case: MOVE and ADD request can be tricky: depending on the "previous loop" of the tool sending
            // the request, an ADD request can be send for a simple graphic move, and a MOVE request can be sent for an
            // actual graphic re-parenting. Let's analyse the request a bit to determine how to handle it.
            ChangeBoundsRequest changeBoundsRequest = (ChangeBoundsRequest) request;
            if (isMove(changeBoundsRequest)) {
                // This is a simple graphic move inside this container, it should be accepted.
                return getHost();
            } else {
                // This is a clone or a graphic re-parenting: it probably involves Ob model changes and such, so
                // further analysis is needed to decide.
                return getTargetEditPart(changeBoundsRequest);
            }
        }
        return null;
    }

    /**
     * Return the host edit part if this policy can handle the metaclass involved in the request.
     * @param createRequest the request.
     * @return the host editpart if the metaclass involved in the request can be handled by this policy, <code>null</code>
     * otherwise.
     */
    @objid ("7f10f26f-1dec-11e2-8cad-001ec947c8cc")
    private EditPart getTargetEditPart(CreateRequest createRequest) {
        if (createRequest.getNewObject() instanceof ModelioCreationContext) {
            final ModelioCreationContext ctx = (ModelioCreationContext) createRequest.getNewObject();
        
            if (ctx.getElementToUnmask() != null) {
                if (getHostCompositeNode().canUnmask(ctx.getElementToUnmask())) {
                    return getHost();
                } else {
                    return null;
                }
            }
        
            if (!canHandle(Metamodel.getMClass(ctx.getMetaclass())))
                return null;
        }
        return getHost();
    }

    /**
     * Return the host edit part if this policy can handle all edit parts involved in the request.
     * @param changeBoundsRequest the request, can be CLONE or ADD.
     * @return the host editpart if all editparts involved in the request can be handled by this policy, <code>null</code>
     * otherwise.
     */
    @objid ("7f10f279-1dec-11e2-8cad-001ec947c8cc")
    private EditPart getTargetEditPart(ChangeBoundsRequest changeBoundsRequest) {
        for (Object editPartObj : changeBoundsRequest.getEditParts()) {
            // If there is at least 1 element that this policy cannot
            // handle, do not handle the request at all!
            final EditPart editPart = (EditPart) editPartObj;
            if (editPart.getModel() instanceof GmModel) {
                final GmModel gmModel = (GmModel) editPart.getModel();
                final String metaclassName = gmModel.getRepresentedRef().mc;
        
                if (!this.canHandle(Metamodel.getMClass(metaclassName)) && !(editPart instanceof ConnectionEditPart))
                    return null;
        
            }
        }
        // This policy can handle all elements of this request: handle it!
        return getHost();
    }

    /**
     * Returns whether this edit policy can handle this metaclass (either through simple or smart behavior). Default behavior is to
     * accept any metaclass that can be child (in the CreationExpert's understanding) of the host's metaclass This method should be
     * overridden by subclasses to add specific the behavior.
     * @param metaclass the metaclass to handle.
     * @return true if this policy can handle the metaclass.
     */
    @objid ("7f10f283-1dec-11e2-8cad-001ec947c8cc")
    protected boolean canHandle(MClass metaclass) {
        return MTools.getMetaTool().canCompose(getHostElement(), metaclass, null)
                && getHostCompositeNode().canCreate(Metamodel.getJavaInterface(metaclass));
    }

    @objid ("7f10f28b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void eraseLayoutTargetFeedback(Request request) {
        if (this.insertionLine != null) {
            removeFeedback(this.insertionLine);
            this.insertionLine = null;
        }
    }

    @objid ("7f135499-1dec-11e2-8cad-001ec947c8cc")
    private static Rectangle getAbsoluteBounds(GraphicalEditPart ep) {
        Rectangle bounds = ep.getFigure().getBounds().getCopy();
        ep.getFigure().translateToAbsolute(bounds);
        return bounds;
    }

    /**
     * @param request the Request
     * @return the index for the insertion reference
     */
    @objid ("7f1354a2-1dec-11e2-8cad-001ec947c8cc")
    protected int getFeedbackIndexFor(Request request) {
        List<?> children = getHost().getChildren();
        if (children.isEmpty())
            return -1;
        
        Transposer transposer = new Transposer();
        transposer.setEnabled(!isHorizontal());
        
        Point locationFromRequest = getLocationFromRequest(request);
        Point p = locationFromRequest != null ? transposer.t(locationFromRequest) : new Point(0, 0);
        
        // Current row bottom, initialize to above the top.
        int rowBottom = Integer.MIN_VALUE;
        int candidate = -1;
        for (int i = 0; i < children.size(); i++) {
            EditPart child = (EditPart) children.get(i);
            Rectangle rect = transposer.t(getAbsoluteBounds(((GraphicalEditPart) child)));
            if (rect.y > rowBottom) {
                /*
                 * We are in a new row, so if we don't have a candidate but yet are within the previous row, then the current entry
                 * becomes the candidate. This is because we know we must be to the right of center of the last Figure in the
                 * previous row, so this Figure (which is at the start of a new row) is the candidate.
                 */
                if (p.y <= rowBottom) {
                    if (candidate == -1)
                        candidate = i;
                    break;
                }
                // else
                candidate = -1; // Mouse's Y is outside the row, so reset the
                // candidate
            }
            rowBottom = Math.max(rowBottom, rect.bottom());
            if (candidate == -1) {
                /*
                 * See if we have a possible candidate. It is a candidate if the cursor is left of the center of this candidate.
                 */
                if (p.x <= rect.x + (rect.width / 2))
                    candidate = i;
            }
            if (candidate != -1) {
                // We have a candidate, see if the rowBottom has grown to
                // include the mouse Y.
                if (p.y <= rowBottom) {
                    /*
                     * Now we have determined that the cursor.Y is above the bottom of the current row of figures. Stop now, to
                     * prevent the next row from being searched
                     */
                    break;
                }
            }
        }
        return candidate;
    }

    @objid ("7f1354aa-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected EditPart getInsertionReference(Request request) {
        List<?> children = getHost().getChildren();
        
        if (request.getType().equals(RequestConstants.REQ_CREATE)) {
            int i = getFeedbackIndexFor(request);
            if (i == -1)
                return null;
            return (EditPart) children.get(i);
        }
        
        int index = getFeedbackIndexFor(request);
        if (index != -1) {
            List<?> selection = getHost().getViewer().getSelectedEditParts();
            do {
                EditPart editpart = (EditPart) children.get(index);
                if (!selection.contains(editpart))
                    return editpart;
            } while (++index < children.size());
        }
        return null; // Not found, add at the end.
    }

    /**
     * Lazily creates and returns a <code>Polyline</code> Figure for use as feedback.
     * @return a Polyline figure
     */
    @objid ("7f1354b4-1dec-11e2-8cad-001ec947c8cc")
    protected Polyline getLineFeedback() {
        if (this.insertionLine == null) {
            this.insertionLine = new Polyline();
            this.insertionLine.setForegroundColor(ColorConstants.blue);
            this.insertionLine.setLineWidth(3);
            this.insertionLine.setAlpha(128);
            this.insertionLine.addPoint(new Point(-5, 0));
            this.insertionLine.addPoint(new Point(+5, 0));
            this.insertionLine.addPoint(new Point(0, 0));
            this.insertionLine.addPoint(new Point(10, 10));
            addFeedback(this.insertionLine);
        }
        return this.insertionLine;
    }

    @objid ("7f1354bb-1dec-11e2-8cad-001ec947c8cc")
    private static Point getLocationFromRequest(Request request) {
        return ((DropRequest) request).getLocation();
    }

    /**
     * @return <code>true</code> if the host is in a horizontal orientation
     */
    @objid ("7f1354c4-1dec-11e2-8cad-001ec947c8cc")
    protected boolean isHorizontal() {
        return !getHostCompositeNode().isVertical();
    }

    /**
     * Shows an insertion line if there is one or more current children.
     * @see LayoutEditPolicy#showLayoutTargetFeedback(Request)
     */
    @objid ("7f1354c9-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void showLayoutTargetFeedback(Request request) {
        // Show nothing if we cannot issue an executable command.
        Command command = getCommand(request);
        if (!RequestConstants.REQ_MOVE.equals(request.getType()) && (command == null || !command.canExecute()))
            return;
        
        if (getHost().getChildren().isEmpty()) {
            // First child is a specific case
            showFirstChildFeedback();
        } else {
            // Otherwise, show a line where the partition would be inserted.
            showInsertionFeedback(request);
        }
    }

    @objid ("7f1354d0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        // if child is a node that can provide its own policy (maybe for keeping
        // specific geometric needs, etc) return it.
        if (child instanceof GmNodeEditPart) {
            GmNodeEditPart childNode = (GmNodeEditPart) child;
            SelectionEditPolicy childPolicy = childNode.getPreferredDragRolePolicy(REQ_RESIZE);
            if (childPolicy != null)
                return childPolicy;
        }
        // default
        return new DefaultNodeResizableEditPolicy();
    }

    /**
     * A translation is interpreted here as a change in order of the children. This method obtains the proper index, and then calls
     * {@link #createMoveChildCommand(EditPart, EditPart)}.
     * @see LayoutEditPolicy#getMoveChildrenCommand(Request)
     */
    @objid ("7f1354da-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getMoveChildrenCommand(Request request) {
        if (!getHostCompositeNode().allowsMove()) {
            return null;
        }
        
        // Translation
        List<?> editParts = ((ChangeBoundsRequest) request).getEditParts();
        CompoundCommand command = new CompoundCommand();
        
        EditPart insertionReference = getInsertionReference(request);
        for (int i = 0; i < editParts.size(); i++) {
            EditPart child = (EditPart) editParts.get(i);
            command.add(createMoveChildCommand(child, insertionReference));
        }
        return command.unwrap();
    }

    /**
     * Overridden to prevent sizes from becoming too small, and to prevent preferred sizes from getting lost. If the Request is a
     * MOVE, the existing width and height are preserved. During RESIZE, the new width and height have a lower bound determined by
     * {@link #getMinimumSizeFor(GraphicalEditPart)}.
     * @param request the ChangeBoundsRequest.
     * @param child the child EditPart for which the constraint should be generated.
     * @return the rectangle being the desired bounds of the child.
     */
    @objid ("7f1354e5-1dec-11e2-8cad-001ec947c8cc")
    protected Rectangle getConstraintFor(ChangeBoundsRequest request, GraphicalEditPart child) {
        Rectangle rect = new PrecisionRectangle(child.getFigure().getBounds());
        Rectangle original = rect.getCopy();
        child.getFigure().translateToAbsolute(rect);
        rect = request.getTransformedRectangle(rect);
        child.getFigure().translateToRelative(rect);
        rect.translate(getLayoutContainer().getClientArea().getLocation().getNegated());
        
        if (request.getSizeDelta().width == 0 && request.getSizeDelta().height == 0) {
            Rectangle cons = getCurrentConstraintFor(child);
            if (cons != null) // Bug 86473 allows for unintended use of this
                // method
                rect.setSize(cons.width, cons.height);
        } else { // resize
            Dimension minSize = getMinimumSizeFor(child);
            if (rect.width < minSize.width) {
                rect.width = minSize.width;
                if (rect.x > (original.right() - minSize.width))
                    rect.x = original.right() - minSize.width;
            }
            if (rect.height < minSize.height) {
                rect.height = minSize.height;
                if (rect.y > (original.bottom() - minSize.height))
                    rect.y = original.bottom() - minSize.height;
            }
        }
        return getConstraintFor(rect);
    }

    /**
     * Returns a Rectangle at the given Point with width and height of -1. Layout uses width or height equal to '-1' to mean use the
     * figure's preferred size.
     * @param p the input Point
     * @return a Rectangle
     */
    @objid ("7f15b6fb-1dec-11e2-8cad-001ec947c8cc")
    public static Rectangle getConstraintFor(Point p) {
        return new Rectangle(p, DEFAULT_SIZE);
    }

    /**
     * Returns a new Rectangle equivalent to the passed Rectangle.
     * @param r the input Rectangle
     * @return a copy of the input Rectangle
     */
    @objid ("7f15b705-1dec-11e2-8cad-001ec947c8cc")
    public static Rectangle getConstraintFor(Rectangle r) {
        return new Rectangle(r);
    }

    /**
     * Retrieves the child's current constraint from the <code>LayoutManager</code>.
     * @param child the child
     * @return the current constraint
     */
    @objid ("7f15b70f-1dec-11e2-8cad-001ec947c8cc")
    protected static Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
        IFigure fig = child.getFigure();
        return (Rectangle) fig.getParent().getLayoutManager().getConstraint(fig);
    }

    /**
     * Determines the <em>minimum</em> size that the specified child can be resized to. Called from
     * {@link #getConstraintFor(ChangeBoundsRequest, GraphicalEditPart)}. By default, a small <code>Dimension</code> is returned.
     * @param child the child
     * @return the minimum size
     */
    @objid ("7f15b719-1dec-11e2-8cad-001ec947c8cc")
    protected static Dimension getMinimumSizeFor(GraphicalEditPart child) {
        return child.getFigure().getMinimumSize();
    }

    @objid ("7f15b723-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE_CHILDREN.equals(request.getType()))
            return getResizeChildrenCommand((ChangeBoundsRequest) request);
        // else
        return super.getCommand(request);
    }

    /**
     * @param request the resize children request.
     * @return the command resizing the children.
     */
    @objid ("7f15b72d-1dec-11e2-8cad-001ec947c8cc")
    protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
        if (!getHostCompositeNode().allowsResize()) {
            return null;
        }
        // Resizing children: resize children as they asked (as much as
        // possible) and also resize their "neighbour" to keep the whole
        // container the same size. If there is no neighbour (resized child is
        // either the leftmost or the rightmost), in that particular case then
        // try to resize the container.
        CompoundCommand compound = new CompoundCommand();
        ResizeChildrenCommand command = new ResizeChildrenCommand(getHostCompositeNode());
        List<?> resizedEditParts = request.getEditParts();
        Map<GmNodeModel, Integer> newConstraints = new HashMap<>();
        // int newHeight = -1;
        for (int i = 0; i < resizedEditParts.size(); i++) {
            GraphicalEditPart resizedChild = (GraphicalEditPart) resizedEditParts.get(i);
            Dimension constraint = getConstraintFor(request, resizedChild).getSize();
            newConstraints.put((GmNodeModel) resizedChild.getModel(),
                    isHorizontal() ? Integer.valueOf(constraint.width) : Integer.valueOf(constraint.height));
            // Get the impacted neighbour:
            GraphicalEditPart impactedNeighbour = getImpactedNeighbour(resizedChild, request);
            if (impactedNeighbour != null) {
                // Resize neighbour to compensate for size change of
                // resizedChild.
                ChangeBoundsRequest inverseRequest = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
                inverseRequest.setEditParts(impactedNeighbour);
                inverseRequest.setLocation(request.getLocation());
                inverseRequest.setSizeDelta(request.getSizeDelta().getNegated());
                inverseRequest.setResizeDirection(request.getResizeDirection());
                Dimension neighbourConstraint = getConstraintFor(inverseRequest, impactedNeighbour).getSize();
                newConstraints.put((GmNodeModel) impactedNeighbour.getModel(),
                        isHorizontal() ? Integer.valueOf(neighbourConstraint.width) : Integer.valueOf(neighbourConstraint.height));
            } else {
                // No neighbour, this means the resizedChild is on a border:
                // request a resize of the the container and append the
                // resulting command to the returned command.
                // Ask that container parent is resized (not container itself,
                // as it is only meant to be a child)
                ChangeBoundsRequest resizeContainerRequest = new ChangeBoundsRequest(RequestConstants.REQ_RESIZE);
                resizeContainerRequest.setEditParts(getHost().getParent());
                resizeContainerRequest.setLocation(request.getLocation());
                resizeContainerRequest.setResizeDirection(request.getResizeDirection());
                Dimension sizeDelta = request.getSizeDelta().getCopy();
                // Only ask to be resized in the "major" axis.
                if (isHorizontal())
                    sizeDelta.height = 0;
                else
                    sizeDelta.width = 0;
                resizeContainerRequest.setSizeDelta(sizeDelta);
        
                Command parentCommand = getHost().getParent().getCommand(resizeContainerRequest);
                compound.add(parentCommand);
            }
        }
        command.setNewConstraints(newConstraints);
        compound.add(command);
        return compound.unwrap();
    }

    /**
     * Generates a draw2d constraint for the given <code>CreateRequest</code>. If the CreateRequest has a size,
     * {@link #getConstraintFor(Rectangle)} is called with a Rectangle of that size and the result is returned. This is used during
     * size-on-drop creation. Otherwise, {@link #getConstraintFor(Point)} is returned.
     * <P>
     * The CreateRequest's location is relative the Viewer. The location is made layout-relative before calling one of the methods
     * mentioned above.
     * @param request the CreateRequest
     * @return a draw2d constraint
     */
    @objid ("7f15b737-1dec-11e2-8cad-001ec947c8cc")
    protected Rectangle getConstraintFor(CreateRequest request) {
        IFigure figure = getLayoutContainer();
        
        Point where = request.getLocation().getCopy();
        Dimension size = request.getSize();
        
        figure.translateToRelative(where);
        figure.translateFromParent(where);
        where.translate(getLayoutContainer().getClientArea().getLocation().getNegated());
        
        if (size == null || size.isEmpty())
            return getConstraintFor(where);
        // else
        size = size.getCopy();
        figure.translateToRelative(size);
        figure.translateFromParent(size);
        return getConstraintFor(new Rectangle(where, size));
    }

    @objid ("7f18194b-1dec-11e2-8cad-001ec947c8cc")
    private GraphicalEditPart getImpactedNeighbour(GraphicalEditPart resizedChild, ChangeBoundsRequest request) {
        // Previous child is initially "null", indicating there is no neighbour
        // on the left of first child.
        GraphicalEditPart previousChild = null;
        List<GraphicalEditPart> nextChildren = new ArrayList<>(getHost().getChildren().size());
        for (Object childObj : getHost().getChildren()) {
            nextChildren.add((GraphicalEditPart) childObj);
        }
        nextChildren.removeAll(request.getEditParts());
        // Add "null" at the end, indicating there is no neighbour on the right
        // of last child.
        nextChildren.add(null);
        
        for (Object childObj : getHost().getChildren()) {
            GraphicalEditPart child = (GraphicalEditPart) childObj;
            if (child.equals(resizedChild)) {
                // Depending on the resize direction, return either previous or
                // next child, or null.
                // If movement to the right, return next child
                if (((request.getResizeDirection() & PositionConstants.EAST) != 0)
                        || ((request.getResizeDirection() & PositionConstants.SOUTH) != 0)) {
                    // Watch out: first element if the nextChildren list might
                    // be the current child, in which case just skip over it.
                    if (child.equals(nextChildren.get(0))) {
                        return nextChildren.get(1);
                    }
                    // else
                    return nextChildren.get(0);
                }
                // If movement to the left, return previous child
                if (((request.getResizeDirection() & PositionConstants.WEST) != 0)
                        || ((request.getResizeDirection() & PositionConstants.NORTH) != 0)) {
                    return previousChild;
                }
                // else
                return null;
        
            }
            // Update the nextChildren list by removing current child (note that
            // first element of nextChildren may NOT be current child, since we
            // already removed all resized children from it) and add it as the
            // previous child for next loop.
            if (child.equals(nextChildren.get(0))) {
                nextChildren.remove(0);
                previousChild = child;
            }
        
        }
        // Not found, something is wrong here
        throw new IllegalArgumentException("argument edit part is not a child of current container");
    }

    @objid ("7f181956-1dec-11e2-8cad-001ec947c8cc")
    private boolean isMove(ChangeBoundsRequest changeBoundsRequest) {
        // Start by excluding CLONE: this is never a move:
        if (REQ_CLONE.equals(changeBoundsRequest.getType())) {
            return false;
        }
        // The request is actually a move request (not taking its type into account) if the primary selection's
        // parent edit part is the host.
        for (Object o : changeBoundsRequest.getEditParts()) {
            EditPart editPart = (EditPart) o;
            if (editPart.getSelected() == EditPart.SELECTED_PRIMARY) {
                if (editPart.getParent() != null && editPart.getParent().equals(getHost())) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * @param request
     * @param childrenEditParts
     */
    @objid ("7f18195d-1dec-11e2-8cad-001ec947c8cc")
    private void showInsertionFeedback(Request request) {
        List<?> childrenEditParts = getHost().getChildren();
        Polyline fb = getLineFeedback();
        Transposer transposer = new Transposer();
        transposer.setEnabled(!isHorizontal());
        
        boolean before = true;
        int epIndex = getFeedbackIndexFor(request);
        Rectangle r = null;
        if (epIndex == -1) {
            before = false;
            epIndex = childrenEditParts.size() - 1;
            EditPart editPart = (EditPart) childrenEditParts.get(epIndex);
            r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
        } else {
            EditPart editPart = (EditPart) childrenEditParts.get(epIndex);
            r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
            Point p = transposer.t(getLocationFromRequest(request));
            if (p.x <= r.x + (r.width / 2))
                before = true;
            else {
                /*
                 * We are not to the left of this Figure, so the emphasis line needs to be to the right of the previous Figure,
                 * which must be on the previous row.
                 */
                before = false;
                epIndex--;
                editPart = (EditPart) childrenEditParts.get(epIndex);
                r = transposer.t(getAbsoluteBounds((GraphicalEditPart) editPart));
            }
        }
        int x = Integer.MIN_VALUE;
        if (before) {
            /*
             * Want the line to be halfway between the end of the previous and the beginning of this one. If at the begining of a
             * line, then start halfway between the left edge of the parent and the beginning of the box, but no more than 5 pixels
             * (it would be too far and be confusing otherwise).
             */
            if (epIndex > 0) {
                // Need to determine if a line break.
                Rectangle boxPrev = transposer.t(getAbsoluteBounds((GraphicalEditPart) childrenEditParts.get(epIndex - 1)));
                int prevRight = boxPrev.right();
                if (prevRight < (r.x - ((ResizableGroupLayout) getHostFigure().getLayoutManager()).getSpacing())) {
                    // Not a line break
                    x = prevRight + (r.x - prevRight) / 2;
                } else if (prevRight == (r.x - ((ResizableGroupLayout) getHostFigure().getLayoutManager()).getSpacing())) {
                    x = prevRight + 1;
                }
            }
            if (x == Integer.MIN_VALUE) {
                // It is a line break.
                Rectangle parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
                x = r.x - 5;
                if (x < parentBox.x)
                    x = parentBox.x + (r.x - parentBox.x) / 2;
            }
        } else {
            /*
             * We only have before==false if we are at the end of a line, so go halfway between the right edge and the right edge of
             * the parent, but no more than 5 pixels.
             */
            Rectangle parentBox = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
            int rRight = r.x + r.width;
            int pRight = parentBox.x + parentBox.width;
            x = rRight + 5;
            if (x > pRight)
                x = rRight + (pRight - rRight) / 2;
        }
        Point header1 = new Point(x - 10, r.y - 4);
        header1 = transposer.t(header1);
        fb.translateToRelative(header1);
        Point header2 = new Point(x + 10, r.y - 4);
        header2 = transposer.t(header2);
        fb.translateToRelative(header2);
        Point p1 = new Point(x, r.y - 4);
        p1 = transposer.t(p1);
        fb.translateToRelative(p1);
        Point p2 = new Point(x, r.y + r.height + 4);
        p2 = transposer.t(p2);
        fb.translateToRelative(p2);
        fb.setPoint(header1, 0);
        fb.setPoint(header2, 1);
        fb.setPoint(p1, 2);
        fb.setPoint(p2, 3);
    }

    @objid ("7f181963-1dec-11e2-8cad-001ec947c8cc")
    private void showFirstChildFeedback() {
        // if this is a request for the creation of the first INNER
        // partitions, show a line in the middle of the container.
        Polyline fb = getLineFeedback();
        Transposer transposer = new Transposer();
        transposer.setEnabled(!isHorizontal());
        Rectangle r = transposer.t(getAbsoluteBounds((GraphicalEditPart) getHost()));
        Point p1 = new Point(r.x + (r.width / 2), r.y - 4);
        p1 = transposer.t(p1);
        fb.translateToRelative(p1);
        Point p2 = new Point(r.x + (r.width / 2), r.y + r.height + 4);
        p2 = transposer.t(p2);
        fb.translateToRelative(p2);
        fb.setPoint(p1, 0);
        fb.setPoint(p1, 1);
        fb.setPoint(p2, 2);
        fb.setPoint(p2, 3);
    }

    @objid ("7f181965-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getAddCommand(final Request req) {
        ChangeBoundsRequest request = (ChangeBoundsRequest) req;
        List<?> editParts = request.getEditParts();
        CompoundCommand command = new CompoundCommand();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart child = (EditPart) editParts.get(i);
            if (child instanceof ConnectionEditPart) {
                command.add(child.getCommand(req));
            } else {
                command.add(createAddCommand(child, getInsertionReference(request)));
            }
        }
        return command.unwrap();
    }

    @objid ("7f181970-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getOrphanChildrenCommand(final Request request) {
        if (!getHostCompositeNode().allowsMove()) {
            return UnexecutableCommand.INSTANCE;
        }
        return super.getOrphanChildrenCommand(request);
    }

}
