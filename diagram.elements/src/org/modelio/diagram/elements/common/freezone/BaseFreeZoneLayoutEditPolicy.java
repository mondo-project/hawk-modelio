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
                                    

package org.modelio.diagram.elements.common.freezone;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.diagram.elements.core.commands.DefaultCloneElementCommand;
import org.modelio.diagram.elements.core.commands.DefaultCreateElementCommand;
import org.modelio.diagram.elements.core.commands.DefaultReparentElementCommand;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.commands.NodeChangeLayoutCommand;
import org.modelio.diagram.elements.core.figures.FigureUtilities2;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.diagram.elements.drawings.core.GmDrawing;
import org.modelio.gproject.model.api.MTools;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.processCollaboration.BpmnLane;
import org.modelio.metamodel.bpmn.rootElements.BpmnFlowElement;
import org.modelio.metamodel.experts.meta.IMetaTool;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Specialization of the XYLayoutEditPolicy providing actual commands for usual requests and handling DROP requests. Also provides a
 * smart (colored) feedback.
 */
@objid ("7e30d540-1dec-11e2-8cad-001ec947c8cc")
public class BaseFreeZoneLayoutEditPolicy extends XYLayoutEditPolicy {
    @objid ("e252b106-9395-4fb4-8791-79781bea7fb0")
    protected IFigure highlight = null;

    @objid ("7e333790-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void eraseTargetFeedback(Request request) {
        if (REQ_ADD.equals(request.getType()) || REQ_CREATE.equals(request.getType())) {
        
            if (this.highlight != null) {
                removeFeedback(this.highlight);
                this.highlight = null;
            }
        }
        
        super.eraseTargetFeedback(request);
    }

    @objid ("7e333796-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (REQ_CREATE.equals(request.getType())) {
            final CreateRequest createRequest = (CreateRequest) request;
            return getTargetEditPart(createRequest);
        }
        if (REQ_MOVE.equals(request.getType()) || REQ_ADD.equals(request.getType()) || REQ_CLONE.equals(request.getType())) {
            // Special case: MOVE and ADD request can be tricky: depending on the "previous loop" of the tool sending
            // the request, an ADD request can be send for a simple graphic move, and a MOVE request can be sent for an
            // actual graphic re-parenting. Let's analyse the request a bit to determine how to handle it.
            final ChangeBoundsRequest changeBoundsRequest = (ChangeBoundsRequest) request;
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

    @objid ("7e3337a0-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void showTargetFeedback(Request request) {
        if (REQ_ADD.equals(request.getType()) || REQ_CREATE.equals(request.getType())) {
        
            // compute highlight type
            final Command c = getHost().getCommand(request);
            FigureUtilities2.HighlightType hightlightType = null;
            if (c == null) {
                hightlightType = FigureUtilities2.HighlightType.ERROR;
            } else if (c.canExecute()) {
                hightlightType = FigureUtilities2.HighlightType.SUCCESS;
            } else {
                hightlightType = FigureUtilities2.HighlightType.WARNING;
            }
        
            // create the highlight figure if it does not exists
            if (this.highlight == null) {
                // create a highlight figure
                this.highlight = FigureUtilities2.createHighlightFigure(getFeedbackLayer(), getHostFigure(), hightlightType);
                // add the highlight figure to the feedback layer
                getFeedbackLayer().add(this.highlight);
            }
            // configure the highlight figure
            FigureUtilities2.updateHighlightType(this.highlight, hightlightType);
        }
        super.showTargetFeedback(request);
    }

    /**
     * Returns whether this edit policy can handle this metaclass (either through simple or smart behavior). Default behavior is to
     * accept any metaclass that can be child (in the CreationExpert's understanding) of the host's metaclass This method should be
     * overridden by subclasses to add specific the behavior.
     * @param metaclass the metaclass to handle.
     * @return true if this policy can handle the metaclass.
     */
    @objid ("7e3337a6-1dec-11e2-8cad-001ec947c8cc")
    protected boolean canHandle(Class<? extends MObject> metaclass) {
        final MObject hostElement = getHostElement();
        if (hostElement == null) {
            return false;
        }
        return MTools.getMetaTool().canCompose(hostElement.getMClass(), Metamodel.getMClass(metaclass), null)
                && getHostCompositeNode().canCreate(metaclass);
    }

    @objid ("7e3337ae-1dec-11e2-8cad-001ec947c8cc")
    @Deprecated
    @Override
    protected Command createAddCommand(EditPart child, Object constraint) {
        if (child.getModel() instanceof GmNodeModel)
            return new DefaultReparentElementCommand(getHostElement(), getHostCompositeNode(), (GmNodeModel) child.getModel(), constraint);
        else
            return null;
    }

    @objid ("7e3337b9-1dec-11e2-8cad-001ec947c8cc")
    @Deprecated
    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        // if child is a 'node' it usually can be resized and/or moved
        if (child instanceof GmNodeEditPart || child.getModel() instanceof GmDrawing) {
            final NodeChangeLayoutCommand command = new NodeChangeLayoutCommand();
            command.setModel(child.getModel());
            command.setConstraint(constraint);
            return command;
        }
        return null;
    }

    @objid ("7e3337c4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected EditPolicy createChildEditPolicy(EditPart child) {
        // if child is a node that can provide its own policy (maybe for keeping
        // specific geometric needs, etc) return it.
        if (child instanceof GmNodeEditPart) {
            final GmNodeEditPart childNode = (GmNodeEditPart) child;
            final SelectionEditPolicy childPolicy = childNode.getPreferredDragRolePolicy(REQ_RESIZE);
            if (childPolicy != null) {
                return childPolicy;
            }
        }
        // default
        return new DefaultNodeResizableEditPolicy();
    }

    @objid ("7e3337ce-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getCloneCommand(ChangeBoundsRequest request) {
        if (getHost().getModel() instanceof GmCompositeNode) {
            IMetaTool metaUtils = MTools.getMetaTool();
            
            final GmCompositeNode hostModel = getHostCompositeNode();
            final CompoundCommand command = new CompoundCommand();
            for (final Object editPartObj : request.getEditParts()) {
                final EditPart editPart = (EditPart) editPartObj;
                if (editPart.getModel() instanceof GmModel) {
                    final GmModel gmModel = (GmModel) editPart.getModel();
                    if(getHostElement() instanceof BpmnLane && gmModel.getRelatedElement() instanceof BpmnFlowElement){
                         final Object requestConstraint = getConstraintForClone((GraphicalEditPart) editPart, request);
                         command.add(new BpmnCloneFlowElementCommand(hostModel, (BpmnLane) getHostElement(), (BpmnFlowElement)gmModel.getRelatedElement(),
                                 requestConstraint));
                    } else if (metaUtils.canCompose(getHostElement(), gmModel.getRelatedElement(), null)) {
                        final Object requestConstraint = getConstraintForClone((GraphicalEditPart) editPart, request);
                        command.add(new DefaultCloneElementCommand(hostModel, getHostElement(), gmModel.getRelatedElement(),
                                requestConstraint));
                    } 
                }
            }
            return command.unwrap();
        }
        return null;
    }

    @objid ("7e3337d8-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        final MObject hostElement = getHostElement();
        final Object newObject = request.getNewObject();
        if (newObject instanceof ModelioCreationContext) {
            final ModelioCreationContext ctx = (ModelioCreationContext) newObject;
        
            final MObject elementToUnmask = ctx.getElementToUnmask();
            final GmCompositeNode gmParentNode = getHostCompositeNode();
            if (elementToUnmask != null) {
                if (gmParentNode.canUnmask(elementToUnmask)) {
                    final Object requestConstraint = getConstraintFor(request);
                    return new DefaultCreateElementCommand(hostElement, gmParentNode, ctx, requestConstraint);
                } else {
                    return null;
                }
            } else if (hostElement != null) {
                MClass metaclassToCreate = Metamodel.getMClass(ctx.getMetaclass());
        
                if (gmParentNode.canCreate(Metamodel.getJavaInterface(metaclassToCreate))) {
                    if (MTools.getMetaTool().canCompose(hostElement.getMClass(), metaclassToCreate, null)) {
                        final Object requestConstraint = getConstraintFor(request);
                        return new DefaultCreateElementCommand(hostElement, gmParentNode, ctx, requestConstraint);
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return the {@link GmCompositeNode} model of the host edit part.
     */
    @objid ("7e3599ec-1dec-11e2-8cad-001ec947c8cc")
    protected GmCompositeNode getHostCompositeNode() {
        return (GmCompositeNode) getHost().getModel();
    }

    /**
     * @return the element represented.
     */
    @objid ("7e3599f1-1dec-11e2-8cad-001ec947c8cc")
    protected MObject getHostElement() {
        return getHostCompositeNode().getRelatedElement();
    }

    /**
     * There is currently nothing to do to orphan a group of children since the orphan is done when the children are added to their
     * new parent (this is needed to cover cases where no orphan is actually performed, like in smart interactions), so
     * <code>null</code> is returned.
     */
    @objid ("7e3599f6-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getOrphanChildrenCommand(Request request) {
        return super.getOrphanChildrenCommand(request);
    }

    /**
     * Return the host edit part if this policy can handle the metaclass involved in the request.
     * @param createRequest the request.
     * @return the host editpart if the metaclass involved in the request can be handled by this policy, <code>null</code>
     * otherwise.
     */
    @objid ("7e359a01-1dec-11e2-8cad-001ec947c8cc")
    private EditPart getTargetEditPart(CreateRequest createRequest) {
        final Object newObject = createRequest.getNewObject();
        if (newObject instanceof ModelioCreationContext) {
            final ModelioCreationContext ctx = (ModelioCreationContext) newObject;
        
            if (ctx.getElementToUnmask() != null) {
                if (getHostCompositeNode().canUnmask(ctx.getElementToUnmask())) {
                    return getHost();
                } else {
                    return null;
                }
            }
        
            if (canHandle(Metamodel.getJavaInterface(Metamodel.getMClass(ctx.getMetaclass())))) {
                return getHost();
            }
        }
        return null;
    }

    /**
     * Return the host edit part if this policy can handle all edit parts involved in the request.
     * @param changeBoundsRequest the request, can be CLONE or ADD.
     * @return the host edit part if all edit parts involved in the request can be handled by this policy, <code>null</code>
     * otherwise.
     */
    @objid ("7e359a0b-1dec-11e2-8cad-001ec947c8cc")
    private EditPart getTargetEditPart(ChangeBoundsRequest changeBoundsRequest) {
        for (final Object editPartObj : changeBoundsRequest.getEditParts()) {
        
            final EditPart editPart = (EditPart) editPartObj;
            if (editPart.getModel() instanceof GmModel) {
                final GmModel gmModel = (GmModel) editPart.getModel();
                final String metaclassName = gmModel.getRepresentedRef().mc;
                // If there is at least 1 element that this policy cannot
                // handle, do not handle the request at all!
                if (!canHandle(Metamodel.getJavaInterface(Metamodel.getMClass(metaclassName)))
                        && !(editPart instanceof ConnectionEditPart)) {
                    return null;
                }
        
            }
        }
        // This policy can handle all elements of this request: handle it!
        return getHost();
    }

    @objid ("7e359a15-1dec-11e2-8cad-001ec947c8cc")
    private boolean isMove(ChangeBoundsRequest changeBoundsRequest) {
        // Start by excluding CLONE: this is never a move:
        if (REQ_CLONE.equals(changeBoundsRequest.getType())) {
            return false;
        }
        // The request is actually a move request (not taking its type into account) if the primary selection's
        // parent edit part is the host.
        for (final Object o : changeBoundsRequest.getEditParts()) {
            final EditPart editPart = (EditPart) o;
            if (editPart.getSelected() == EditPart.SELECTED_PRIMARY) {
                if (editPart.getParent() != null && editPart.getParent().equals(getHost())) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    @objid ("7e359a1c-1dec-11e2-8cad-001ec947c8cc")
    protected Rectangle getEffectiveBounds(final IFigure figure) {
        return (figure instanceof HandleBounds) ? ((HandleBounds) figure).getHandleBounds().getCopy() : figure.getBounds()
                .getCopy();
    }

    /**
     * Returns the correct rectangle bounds for the new clone's location. Handles the HandleBounds to avoid errors with
     * PortContainers.
     * @param part the graphical edit part representing the object to be cloned.
     * @param request the changeboundsrequest that knows where to place the new object.
     * @return the bounds that will be used for the new object.
     */
    @objid ("7e359a26-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Object getConstraintForClone(final GraphicalEditPart part, final ChangeBoundsRequest request) {
        IFigure figure = part.getFigure();
        Rectangle bounds = new PrecisionRectangle(getEffectiveBounds(figure));
        
        figure.translateToAbsolute(bounds);
        bounds = request.getTransformedRectangle(bounds);
        
        ((GraphicalEditPart) getHost()).getContentPane().translateToRelative(bounds);
        bounds.translate(getLayoutOrigin().getNegated());
        return getConstraintFor(bounds);
    }

    @objid ("7e359a34-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Object getConstraintFor(final CreateRequest request) {
        // Just making sure that the constraint doesn't violate the "min size".
        Object constraint = super.getConstraintFor(request);
        if (constraint instanceof Rectangle) {
            Rectangle rectConstraint = (Rectangle) constraint;
            if (rectConstraint.width != -1 && rectConstraint.width < 8) {
                rectConstraint.width = 8;
            }
            if (rectConstraint.height != -1 && rectConstraint.height < 8) {
                rectConstraint.height = 8;
            }
        }
        return constraint;
    }

    /**
     * Same as super implementation, except we filter the connection editparts.
     */
    @objid ("7e37fc45-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Command getAddCommand(final Request generic) {
        ChangeBoundsRequest request = (ChangeBoundsRequest) generic;
        List<?> editParts = request.getEditParts();
        CompoundCommand command = new CompoundCommand();
        command.setDebugLabel("Add in ConstrainedLayoutEditPolicy");//$NON-NLS-1$
        GraphicalEditPart child;
        
        for (int i = 0; i < editParts.size(); i++) {
            child = (GraphicalEditPart) editParts.get(i);
            if (child instanceof ConnectionEditPart) {
                command.add(child.getCommand(generic));
            } else {
                command.add(createAddCommand(request, child, translateToModelConstraint(getConstraintFor(request, child))));
            }
        }
        return command.unwrap();
    }

    /**
     * Retrieves the child's current constraint from the <code>LayoutManager</code>.
     * @param child the child
     * @return the current constraint
     */
    @objid ("7e37fc51-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected Rectangle getCurrentConstraintFor(GraphicalEditPart child) {
        IFigure fig = child.getFigure();
        Object constraint = fig.getParent().getLayoutManager().getConstraint(fig);
        if (constraint instanceof Rectangle) {
            return (Rectangle) constraint;
        }
        return null;
    }

}
