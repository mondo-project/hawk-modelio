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
                                    

package org.modelio.diagram.elements.common.portcontainer;

import java.util.ArrayList;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutListener.Stub;
import org.eclipse.draw2d.LayoutListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint.Border;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.policies.DelegatingEditPolicy;

/**
 * Edit part for port container.
 * 
 * @author fpoyer
 */
@objid ("7eef9176-1dec-11e2-8cad-001ec947c8cc")
public class PortContainerEditPart extends GmNodeEditPart {
    @objid ("7eef917a-1dec-11e2-8cad-001ec947c8cc")
    protected boolean dirty = false;

    @objid ("7eef9178-1dec-11e2-8cad-001ec947c8cc")
     ChildNodesInitialLayouter postLayouter = null;

    @objid ("7eef9179-1dec-11e2-8cad-001ec947c8cc")
     PostLayoutAutoResizer postLayoutAutoResizer = null;

    /**
     * C'tor.
     */
    @objid ("7eef917b-1dec-11e2-8cad-001ec947c8cc")
    public PortContainerEditPart() {
        super();
        // Add a listener that will provoke self resize on child addition and
        // subtraction.
        this.addEditPartListener(new EditPartListener.Stub() {
            @Override
            public void childAdded(EditPart child, int index) {
                GmPortContainer pc = (GmPortContainer) getModel();
                if (pc.getMainNode() != child.getModel()) {
                    forcePostLayoutAutoResize();
                }
            }
        
            @Override
            public void removingChild(EditPart child, int index) {
                GmPortContainer pc = (GmPortContainer) getModel();
                if (pc.getMainNode() != child.getModel()) {
                    forcePostLayoutAutoResize();
                }
            }
        
        });
    }

    @objid ("7eef917e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public SelectionEditPolicy getPreferredDragRolePolicy(String requestType) {
        // If asked for a RESIZE policy, return one that will keep the bounds of
        // the container correct and instead resize the main node (if any).
        if (REQ_RESIZE.equals(requestType)) {
            return new AutoSizeEditPolicy();
        }
        return super.getPreferredDragRolePolicy(requestType);
    }

    /**
     * Provoke the self resize of the container by sending a RESIZE request (with resize delta being (0,0)) on the
     * container.
     */
    @objid ("7eef9186-1dec-11e2-8cad-001ec947c8cc")
    void autoResize() {
        // Request a "fake" resize of container, so that it can adapts
        // itself to its new child.
        ChangeBoundsRequest resizeContainerRequest = new ChangeBoundsRequest(REQ_RESIZE);
        resizeContainerRequest.setEditParts(PortContainerEditPart.this);
        resizeContainerRequest.setSizeDelta(new Dimension(0, 0));
        Command resizeContainerCommand = getCommand(resizeContainerRequest);
        if (resizeContainerCommand != null && resizeContainerCommand.canExecute()) {
            resizeContainerCommand.execute();
        }
    }

    @objid ("7eef9189-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void addChildVisual(EditPart childEditPart, final int index) {
        final PortContainerFigure portContainerFigure = (PortContainerFigure) getFigure();
        final IFigure childFigure = ((GraphicalEditPart) childEditPart).getFigure();
        
        final GmPortContainer pc = (GmPortContainer) getModel();
        final GmNodeModel childModel = (GmNodeModel) childEditPart.getModel();
        boolean isMainNode = false;
        
        if (childModel == pc.getMainNode()) {
            // Indicate main node figure to the container's figure and layout so
            // it can use it for handle bounds and for laying out the ports.
            isMainNode = true;
            portContainerFigure.setMainNodeFigure(childFigure);
            ((PortContainerLayout) portContainerFigure.getLayoutManager()).setMainNodeFigure(childFigure);
        } else if (pc.isSatellite(childModel)) {
            // Override the IDragTrackerProvider of the child EditPart to use a
            // custom one that will handle the fact that a satellite should not
            // be reparented.
            ((GmNodeEditPart) childEditPart).setDragTrackerProvider(new SatelliteDragTrackerProvider(childEditPart));
        } else {
            assert (pc.isPort(childModel)) : "unsupported type of child: " +
                                                                                           childModel.getRoleInComposition();
        }
        
        if (childModel.getLayoutData() != null) {
            if (childModel.getLayoutData() instanceof Integer) {
                // Specific case of a satellite placed "ex nihilo", wait for the layout to have been applied at least once before placing the satellite.
                getPostLayouter().addLayouter(new ChildSatelliteInitialLayouter(childFigure, childModel));
        
            } else if (pc.isPort(childModel)) {
        
                if (childModel.getLayoutData() instanceof Rectangle) {
                    final Rectangle oldConstraint = (Rectangle) childModel.getLayoutData();
        
                    final PortConstraint newConstraint = new PortConstraint();
                    newConstraint.setRequestedBounds(oldConstraint);
        
                    Border referenceBorder = ((PortContainerLayout) portContainerFigure.getLayoutManager()).determineReferenceBorder(portContainerFigure,
                                                                                                                                     oldConstraint);
                    newConstraint.setReferenceBorder(referenceBorder);
        
                    childModel.setLayoutData(newConstraint);
                } else if (childModel.getLayoutData() instanceof Border) {
                    getPostLayouter().addLayouter(new ChildPortInitialLayouter(childFigure, childModel));
                }
            }
        }
        
        // Actually add the child.
        // Same as calling :
        // super.addChildVisual(childEditPart, index);
        // Except we do get the constraint right now instead of later.
        if (isMainNode) {
            getContentPane().add(childFigure, childModel.getLayoutData(), 0);
        } else {
            getContentPane().add(childFigure, childModel.getLayoutData(), index);
        }
    }

    @objid ("7eef9191-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new PortContainerEditPolicy());
        installEditPolicy("Delegation", new DelegatingEditPolicy());
        installEditPolicy("satellite selection", new SatelliteChildrenSelectionPolicy());
    }

    @objid ("7eef9194-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected IFigure createFigure() {
        IFigure fig = new PortContainerFigure();
        
        // Style independent properties.
        fig.setLayoutManager(new PortContainerLayout());
        
        // DEBUG
        // fig.setOpaque(true);
        // fig.setBackgroundColor(ColorConstants.red);
        
        // Style dependent properties.
        refreshFromStyle(fig, getModelStyle());
        
        fig.addLayoutListener(new LayoutListener.Stub() {
            @Override
            public void postLayout(IFigure container) {
                PortContainerEditPart.this.dirty = false;
            }
        });
        return fig;
    }

    @objid ("7eef919b-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void refreshVisuals() {
        IFigure portContainerFigure = getFigure();
        GmPortContainer portContainerModel = (GmPortContainer) getModel();
        
        portContainerFigure.getParent()
                           .setConstraint(portContainerFigure, portContainerModel.getLayoutData());
        
        super.refreshVisuals();
        
        // Additionally force refresh of main node edit part
        GmNodeModel mainNode = portContainerModel.getMainNode();
        if (mainNode != null) {
            AbstractGraphicalEditPart mainNodeEditPart = (AbstractGraphicalEditPart) getViewer().getEditPartRegistry()
                                                                                                .get(mainNode);
            if (mainNodeEditPart != null) {
                mainNodeEditPart.refresh();
            }
        }
        this.dirty = true;
    }

    @objid ("7ef1f3ab-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        // Just watch out for the main node:
        GmPortContainer pc = (GmPortContainer) getModel();
        if (pc.getMainNode() == childEditPart.getModel()) {
            ((PortContainerFigure) getFigure()).setMainNodeFigure(null);
            ((PortContainerLayout) getFigure().getLayoutManager()).setMainNodeFigure(null);
        }
        
        // Actually remove the child.
        super.removeChildVisual(childEditPart);
    }

    /**
     * Get the post layout listener used to initialize some Ports and satellite node constraint.
     * @return the post layouter.
     */
    @objid ("7ef1f3b1-1dec-11e2-8cad-001ec947c8cc")
    private ChildNodesInitialLayouter getPostLayouter() {
        if (this.postLayouter == null) {
            this.postLayouter = new ChildNodesInitialLayouter();
            this.getFigure().addLayoutListener(this.postLayouter);
        }
        return this.postLayouter;
    }

    @objid ("7ef1f3b6-1dec-11e2-8cad-001ec947c8cc")
    void forcePostLayoutAutoResize() {
        if (this.postLayoutAutoResizer == null) {
            this.postLayoutAutoResizer = new PostLayoutAutoResizer();
            this.getFigure().addLayoutListener(this.postLayoutAutoResizer);
        }
        return;
    }

    /**
     * Sets the satellite initial bounds.
     * <p>
     * Computes the initial location of a satellite based on the bounds of the main node, a placement constraint
     * expressed as a value from {@link PositionConstants} and the preferred size of the satellite.
     * 
     * @author cmarin
     */
    @objid ("7ef1f3b9-1dec-11e2-8cad-001ec947c8cc")
    private final class ChildSatelliteInitialLayouter extends Stub {
        @objid ("0ee09691-c24e-4a0e-b25a-56ecd4e8ada7")
        private final IFigure childFigure;

        @objid ("7ef1f3c1-1dec-11e2-8cad-001ec947c8cc")
        private final GmNodeModel childModel;

        /**
         * Converts an Integer (interpreted as a value from {@link PositionConstants} to a Rectangle.
         * @param childFigure the child figure for which to convert the constraint.
         * @param childModel the child model for which to convert the constraint.
         */
        @objid ("7ef1f3c3-1dec-11e2-8cad-001ec947c8cc")
        public ChildSatelliteInitialLayouter(final IFigure childFigure, final GmNodeModel childModel) {
            this.childFigure = childFigure;
            this.childModel = childModel;
        }

        @objid ("7ef1f3cc-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void postLayout(final IFigure container) {
            // Avoid setting a constraint when the figure isn't attached to 'content pane'
            if (this.childFigure.getParent() == getContentPane()) {
                Rectangle newConstraint = convertIntConstraintToRectangleConstraint();
            
                this.childModel.setLayoutData(newConstraint);
                getContentPane().setConstraint(this.childFigure, this.childModel.getLayoutData());
            }
        }

        /**
         * Converts an Integer (interpreted as a value from {@link PositionConstants} to a Rectangle.
         * @return the new rectangle constraint.
         */
        @objid ("7ef1f3d3-1dec-11e2-8cad-001ec947c8cc")
        private Rectangle convertIntConstraintToRectangleConstraint() {
            // Let's suppose that it is a placement constraint from
            // PositionConstants, and we'll place the child around the main
            // node's figure.
            Rectangle newConstraint = new Rectangle(0, 0, -1, -1);
            // 1 - Get the bounds of the main node (default to (0, 0, 0, 0) if
            // not found).
            Rectangle mainNodeBounds = ((PortContainerLayout) getFigure().getLayoutManager()).getMainNodeConstraint();
            if (mainNodeBounds == null) {
                mainNodeBounds = new Rectangle(0, 0, 0, 0);
            }
            
            // 2 - define a constraint around the main node bounds (default to
            // EAST).
            int placement = ((Integer) this.childModel.getLayoutData()).intValue();
            Dimension childPreferredSize = this.childFigure.getPreferredSize();
            this.childFigure.translateToAbsolute(childPreferredSize);
            newConstraint.setLocation(computeSatelliteInitialLocation(mainNodeBounds,
                                                                      placement,
                                                                      childPreferredSize));
            return newConstraint;
        }

        /**
         * Computes the initial location of a satellite based on the bounds of the main node, a placement constraint
         * expressed as a value from {@link PositionConstants} and the preferred size of the satellite.
         * @param mainNodeBounds the bounds of the main node.
         * @param placement a placement constraint expressed as a value from {@link PositionConstants}. Can be either
         * {@link PositionConstants#SOUTH_EAST}, {@link PositionConstants#SOUTH},
         * {@link PositionConstants#SOUTH_WEST}, {@link PositionConstants#WEST},
         * {@link PositionConstants#NORTH_WEST}, {@link PositionConstants#NORTH},
         * {@link PositionConstants#NORTH_EAST} or {@link PositionConstants#EAST} which is the default.
         * @param childPreferredSize the preferred size of the satellite
         * @return the initial location of the satellite.
         */
        @objid ("7ef1f3da-1dec-11e2-8cad-001ec947c8cc")
        private Point computeSatelliteInitialLocation(Rectangle mainNodeBounds, int placement, Dimension childPreferredSize) {
            switch (placement) {
                case PositionConstants.SOUTH_EAST:
                    return mainNodeBounds.getBottomRight();
            
                case PositionConstants.SOUTH:
                    return mainNodeBounds.getBottom().translate(-childPreferredSize.width / 2, 0);
            
                case PositionConstants.SOUTH_WEST:
                    return mainNodeBounds.getBottomLeft().translate(-childPreferredSize.width, 0);
            
                case PositionConstants.WEST:
                    return mainNodeBounds.getLeft().translate(-childPreferredSize.width,
                                                              -childPreferredSize.height / 2);
                case PositionConstants.NORTH_WEST:
                    return mainNodeBounds.getTopLeft().translate(-childPreferredSize.width,
                                                                 -childPreferredSize.height);
            
                case PositionConstants.NORTH:
                    return mainNodeBounds.getTop().translate(-childPreferredSize.width / 2,
                                                             -childPreferredSize.height);
            
                case PositionConstants.NORTH_EAST:
                    return mainNodeBounds.getTopRight().translate(0, -childPreferredSize.height);
            
                case PositionConstants.EAST:
                default:
                    return mainNodeBounds.getRight().translate(0, -childPreferredSize.height / 2);
            }
        }

    }

    /**
     * Sets the port initial bounds if not defined.
     */
    @objid ("7ef1f3e7-1dec-11e2-8cad-001ec947c8cc")
    private final class ChildPortInitialLayouter extends Stub {
        @objid ("81502cbe-8c0f-4eb1-a905-08ffa9460ca0")
        private final IFigure childFigure;

        @objid ("7ef1f3ef-1dec-11e2-8cad-001ec947c8cc")
        private final GmNodeModel childModel;

        /**
         * Converts an Integer (interpreted as a value from {@link PositionConstants} to a Rectangle.
         * @param childFigure the child figure for which to convert the constraint.
         * @param childModel the child model for which to convert the constraint.
         */
        @objid ("7ef1f3f1-1dec-11e2-8cad-001ec947c8cc")
        public ChildPortInitialLayouter(final IFigure childFigure, final GmNodeModel childModel) {
            this.childFigure = childFigure;
            this.childModel = childModel;
        }

        @objid ("7ef1f3fa-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void postLayout(final IFigure container) {
            // Avoid setting a constraint when the figure isn't attached to 'container'
            if (this.childFigure.getParent() == container) {
                if (this.childModel.getLayoutData() instanceof Border) {
                    final Border portConstraint = (Border) this.childModel.getLayoutData();
                    final PortConstraint newConstraint = computeRectangleFromBorder(portConstraint);
            
                    this.childModel.setLayoutData(newConstraint);
                    container.setConstraint(this.childFigure, newConstraint);
                }
            }
        }

        /**
         * Converts an Integer (interpreted as a value from {@link PositionConstants} to a Rectangle.
         * @return the new rectangle constraint.
         */
        @objid ("7ef45608-1dec-11e2-8cad-001ec947c8cc")
        private PortConstraint computeRectangleFromBorder(final Border placement) {
            // Let's suppose that it is a placement constraint from
            // PositionConstants, and we'll place the child around the main
            // node's figure.
            Rectangle newRect = new Rectangle(0, 0, -1, -1);
            // 1 - Get the bounds of the main node (default to (0, 0, 0, 0) if
            // not found).
            Rectangle mainNodeBounds = ((PortContainerLayout) getFigure().getLayoutManager()).getMainNodeConstraint();
            if (mainNodeBounds == null) {
                mainNodeBounds = new Rectangle(0, 0, 0, 0);
            }
            
            // 2 - define a constraint around the main node bounds (default to
            // EAST).
            Dimension childPreferredSize = this.childFigure.getPreferredSize();
            this.childFigure.translateToAbsolute(childPreferredSize);
            newRect.setLocation(computePortInitialLocation(mainNodeBounds, placement, childPreferredSize));
            
            PortConstraint newConstraint = new PortConstraint();
            newConstraint.setReferenceBorder(placement);
            newConstraint.setRequestedBounds(newRect);
            return newConstraint;
        }

        /**
         * Computes the initial location of a satellite based on the bounds of the main node, a placement constraint
         * expressed as a value from {@link PositionConstants} and the preferred size of the satellite.
         * @param mainNodeBounds the bounds of the main node.
         * @param placement the border on which the port should be.
         * @param childSize the preferred size of the Port
         * @return the initial location of center of the Port.
         */
        @objid ("7ef4560f-1dec-11e2-8cad-001ec947c8cc")
        private Point computePortInitialLocation(final Rectangle mainNodeBounds, final Border placement, final Dimension childSize) {
            switch (placement) {
                case SouthEast:
                    return mainNodeBounds.getBottomRight();
            
                case South:
                    return mainNodeBounds.getBottom().translate(-childSize.width / 2, 0);
            
                case SouthWest:
                    return mainNodeBounds.getBottomLeft().translate(-childSize.width, 0);
            
                case West:
                    return mainNodeBounds.getLeft().translate(-childSize.width, -childSize.height / 2);
                case NorthWest:
                    return mainNodeBounds.getTopLeft().translate(-childSize.width, -childSize.height);
            
                case North:
                    return mainNodeBounds.getTop().translate(-childSize.width / 2, -childSize.height);
            
                case NorthEast:
                    return mainNodeBounds.getTopRight().translate(0, -childSize.height);
            
                case East:
                default:
                    return mainNodeBounds.getRight().translate(0, -childSize.height / 2);
            }
        }

    }

    /**
     * Set some child nodes initial constraint.
     */
    @objid ("7ef4561f-1dec-11e2-8cad-001ec947c8cc")
    private class ChildNodesInitialLayouter extends Stub {
        @objid ("63e12c1b-1e83-11e2-8cad-001ec947c8cc")
        private Collection<LayoutListener> initializers = new ArrayList<>();

        @objid ("7ef45628-1dec-11e2-8cad-001ec947c8cc")
        public ChildNodesInitialLayouter() {
        }

        @objid ("7ef4562a-1dec-11e2-8cad-001ec947c8cc")
        public void addLayouter(final LayoutListener l) {
            this.initializers.add(l);
        }

        @objid ("7ef45630-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void postLayout(final IFigure container) {
            // Postpone until the main node is initialized.
            Rectangle mainNodeBounds = ((PortContainerLayout) container.getLayoutManager()).getMainNodeConstraint();
            if (mainNodeBounds == null)
                return;
            
            // In all case, remove this listener
            container.removeLayoutListener(this);
            PortContainerEditPart.this.postLayouter = null;
            
            // Run all registered initializers
            for (LayoutListener l : this.initializers) {
                l.postLayout(container);
            }
            this.initializers.clear();
            
            // Auto resize the port container
            forcePostLayoutAutoResize();
        }

    }

    /**
     * Helper class that will force a call to {@link PortContainerEditPolicy#autoResize} after the port container has
     * been layouted at least once.
     * 
     * @author fpoyer
     */
    @objid ("7ef45637-1dec-11e2-8cad-001ec947c8cc")
    private class PostLayoutAutoResizer extends Stub {
        @objid ("7ef4563b-1dec-11e2-8cad-001ec947c8cc")
        public PostLayoutAutoResizer() {
            super();
        }

        @objid ("7ef4563d-1dec-11e2-8cad-001ec947c8cc")
        @Override
        public void postLayout(final IFigure container) {
            // Postpone until the main node is initialized.
            Rectangle mainNodeBounds = ((PortContainerLayout) container.getLayoutManager()).getMainNodeConstraint();
            if (mainNodeBounds == null)
                return;
            
            // In all case, remove this listener
            container.removeLayoutListener(this);
            PortContainerEditPart.this.postLayoutAutoResizer = null;
            
            // Auto resize the port container
            autoResize();
        }

    }

}
