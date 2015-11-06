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
                                    

package org.modelio.linkeditor.view.background;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.linkeditor.view.LinkEditorView;
import org.modelio.linkeditor.view.node.EdgeBus;
import org.modelio.linkeditor.view.node.GraphNode;
import org.modelio.ui.UIColor;

/**
 * Edit part of the background of the Link Editor.
 * 
 * @author fpoyer
 */
@objid ("1b855a97-5e33-11e2-b81d-002564c97630")
public class BackgroundEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {
    @objid ("1b855a9b-5e33-11e2-b81d-002564c97630")
    private static int HORIZONTAL_RANK_GAP;

    @objid ("1b855a9c-5e33-11e2-b81d-002564c97630")
    private static int VERTICAL_GAP;

    @objid ("1b87bbf5-5e33-11e2-b81d-002564c97630")
    private static int VERTICAL_RANK_GAP;

    @objid ("1b87bbf6-5e33-11e2-b81d-002564c97630")
    private static int HORIZONTAL_GAP;

    @objid ("e5f57cc6-5efd-11e2-a8be-00137282c51b")
    private static IMModelServices modelServices;

    @objid ("1b87bbf7-5e33-11e2-b81d-002564c97630")
    @Override
    public void activate() {
        super.activate();
        this.getModel().addPropertyChangeListener(this);
    }

    @objid ("1b87bbfa-5e33-11e2-b81d-002564c97630")
    @Override
    public void deactivate() {
        super.deactivate();
        this.getModel().removePropertyChangeListener(this);
    }

    @objid ("1b87bbfd-5e33-11e2-b81d-002564c97630")
    @Override
    public BackgroundModel getModel() {
        return (BackgroundModel) super.getModel();
    }

    @objid ("1b87bc02-5e33-11e2-b81d-002564c97630")
    @Override
    public void performRequest(final Request req) {
        if (RequestConstants.REQ_OPEN.equals(req.getType())) {
            LinkEditorView.getOptions().setPinned(!LinkEditorView.getOptions().isPinned());
            LinkEditorView.configureToolbar();
            getModel().contentChanged();
        } else {
            super.performRequest(req);
        }
    }

    @objid ("1b87bc09-5e33-11e2-b81d-002564c97630")
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        // handle model property change (most probably: central node changed or
        // updated: re-apply layout).
        if (evt.getPropertyName().equals(BackgroundModel.CONTENT)) {
            // Update the layout offsets based on the nodes size.
            BackgroundEditPart.HORIZONTAL_RANK_GAP = (int) (0.75 * GraphNode.WIDTH);
            BackgroundEditPart.VERTICAL_GAP = BackgroundEditPart.HORIZONTAL_RANK_GAP / 2;
            BackgroundEditPart.VERTICAL_RANK_GAP = -BackgroundEditPart.VERTICAL_GAP / 4 * 3;
            BackgroundEditPart.HORIZONTAL_GAP = (int) (BackgroundEditPart.HORIZONTAL_RANK_GAP * 1.5);
        
            this.getFigure().setBackgroundColor((LinkEditorView.getOptions().isPinned() ? UIColor.TEXT_WRITABLE_BG : UIColor.POST_IT_BG));
            this.getFigure().getUpdateManager().addInvalidFigure(this.getFigure());
        
            this.refreshChildren();
            this.refreshVisuals();
        }
    }

    @objid ("1b87bc0e-5e33-11e2-b81d-002564c97630")
    @Override
    protected void addChildVisual(final EditPart childEditPart, final int index) {
        IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
        Node model = (Node) childEditPart.getModel();
        this.getContentPane().add(child, new Rectangle(model.x, model.y, model.width, model.height), index);
    }

    @objid ("1b87bc17-5e33-11e2-b81d-002564c97630")
    @Override
    protected void createEditPolicies() {
        // TODO add edit policies for the background (if any?)
        this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new DropEditPolicy());
    }

    @objid ("1b87bc1a-5e33-11e2-b81d-002564c97630")
    @Override
    protected IFigure createFigure() {
        // TODO Create and return a background figure with the correct layout
        // manager.
        FreeformLayer freeformLayer = new FreeformLayer();
        freeformLayer.setLayoutManager(new FreeformLayout());
        freeformLayer.setOpaque(true);
        Color swtColor = LinkEditorView.getOptions().isPinned() ? UIColor.TEXT_WRITABLE_BG : UIColor.POST_IT_BG;
        freeformLayer.setBackgroundColor(swtColor);
        return freeformLayer;
    }

    @objid ("1b87bc21-5e33-11e2-b81d-002564c97630")
    @Override
    protected List<?> getModelChildren() {
        BackgroundModel directedGraph = this.getModel();
        boolean vertical = LinkEditorView.getOptions().isLayoutOrientationVertical();
        // TODO treelayout
        Node center = directedGraph.getCenter();
        if (center != null) {
            // "blank" layout
            int offsetLeft = this.layout(center, 0, 0, vertical, true);
            int offsetRight = this.layout(center, 0, 0, vertical, false);
            // Now use the computed offsets to align both sides of the tree.
            int offset = Math.max(offsetLeft, offsetRight);
            this.layout(center, 0, (offset - offsetLeft) / 2, vertical, true);
            this.layout(center, 0, (offset - offsetRight) / 2, vertical, false);
            // Make sure all buses span as much as needed
            this.fixBusesSize(directedGraph, vertical);
            // And finally center the whole tree in the Control.
            this.shiftAll(directedGraph);
        }
        return directedGraph.nodes;
    }

    @objid ("1b87bc28-5e33-11e2-b81d-002564c97630")
    private void fixBusesSize(final BackgroundModel directedGraph, final boolean vertical) {
        for (Object node : directedGraph.nodes) {
            if (node instanceof EdgeBus) {
                int min = Integer.MAX_VALUE;
                int max = Integer.MIN_VALUE;
                if (((EdgeBus) node).incoming.size() > 1) {
                    for (Object incomingEdge : ((EdgeBus) node).incoming) {
                        int tmp = vertical ? ((Edge) incomingEdge).source.x : ((Edge) incomingEdge).source.y;
                        int tmp2 = vertical ? tmp + ((Edge) incomingEdge).source.width : tmp + ((Edge) incomingEdge).source.height;
                        if (tmp < min) {
                            min = tmp;
                        }
                        if (max < tmp2) {
                            max = tmp2;
                        }
                    }
                }
                if (((EdgeBus) node).outgoing.size() > 1) {
                    for (Object outgoingEdge : ((EdgeBus) node).outgoing) {
                        int tmp = vertical ? ((Edge) outgoingEdge).target.x : ((Edge) outgoingEdge).target.y;
                        int tmp2 = vertical ? tmp + ((Edge) outgoingEdge).target.width : tmp + ((Edge) outgoingEdge).target.height;
                        if (tmp < min) {
                            min = tmp;
                        }
                        if (max < tmp2) {
                            max = tmp2;
                        }
                    }
                }
                if (vertical) {
                    ((EdgeBus) node).x = min;
                    ((EdgeBus) node).width = max - min;
                } else {
                    ((EdgeBus) node).y = min;
                    ((EdgeBus) node).height = max - min;
                }
            }
        }
    }

    /**
     * Get the size of the control showing the viewer .
     * <p>
     * Scroll bars are deduced from the view size.
     * @return the visible view size.
     */
    @objid ("1b87bc2e-5e33-11e2-b81d-002564c97630")
    private Point getViewAreaSize() {
        final Point controlSize = new Point(0, 0);
        // this has to be done in the display thread to avoid
        // InvalidThreadAccessException.
        this.getViewer().getControl().getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                final Control control = BackgroundEditPart.this.getViewer().getControl();
                Point p = control.getSize();
                controlSize.x = p.x;
                controlSize.y = p.y;
        
                if (control instanceof Scrollable) {
                    Scrollable c = (Scrollable) control;
                    ScrollBar b = c.getHorizontalBar();
                    if (b != null /* && b.isVisible() */)
                        controlSize.y -= b.getSize().y;
                    b = c.getVerticalBar();
                    if (b != null /* && b.isVisible() */)
                        controlSize.x -= b.getSize().x;
                }
            }
        });
        return controlSize;
    }

    @objid ("1b87bc33-5e33-11e2-b81d-002564c97630")
    private int layout(final Node node, final int rank, final int initialOffset, final boolean vertical, final boolean goLeft) {
        int offset = initialOffset;
        EdgeList edges = goLeft ? node.incoming : node.outgoing;
        // Start by layouting children
        for (Object edgeObj : edges) {
            Edge edge = (Edge) edgeObj;
            Node nextNode = goLeft ? edge.source : edge.target;
            int rankIncrement;
            int rankGap = vertical ? BackgroundEditPart.VERTICAL_RANK_GAP : BackgroundEditPart.HORIZONTAL_RANK_GAP;
            if (node instanceof EdgeBus) {
                rankIncrement = 2 * rankGap;
            } else if (nextNode instanceof EdgeBus) {
                rankIncrement = 1 * rankGap;
            } else {
                rankIncrement = (int) (2.5 * rankGap);
            }
            int nextRank;
            if (goLeft) {
                nextRank = rank - rankIncrement;
            } else {
                nextRank = rank + rankIncrement;
            }
            offset = (vertical ? BackgroundEditPart.HORIZONTAL_GAP : BackgroundEditPart.VERTICAL_GAP)
                    + this.layout(nextNode, nextRank, offset, vertical, goLeft);
        }
        if (edges.size() > 0) {
            offset -= (vertical ? BackgroundEditPart.HORIZONTAL_GAP : BackgroundEditPart.VERTICAL_GAP);
        }
        
        // Now layout node
        if (vertical) {
            node.x = initialOffset + ((offset - initialOffset) / 2);
            node.y = (rank /* * RANK_GAP */) - (node.height / 2);
        } else {
            node.x = (rank /* * RANK_GAP */) - (node.width / 2);
            node.y = initialOffset + ((offset - initialOffset) / 2);
        }
        return offset;
    }

    /**
     * Align the center node on the center of the view or one of its border
     * depending on the edges existence.
     * @param directedGraph the model graph
     */
    @objid ("1b87bc43-5e33-11e2-b81d-002564c97630")
    private void shiftAll(final BackgroundModel directedGraph) {
        // Center the center node in the view.
        // Get the size of the control showing the viewer (this has to be done
        // in the display thread to avoid InvalidThreadAccessException).
        final Point controlSize = this.getViewAreaSize();
        
        final GraphNode centerNode = directedGraph.getCenter();
        final boolean vertical = LinkEditorView.getOptions().isLayoutOrientationVertical();
        
        // Compute the vector to align the nodes.
        int xDelta;
        int yDelta;
        if (centerNode.incoming.isEmpty() && !centerNode.outgoing.isEmpty()) {
            // No incoming : center on the left/bottom
            if (vertical) {
                // Center on bottom
                xDelta = (controlSize.x / 2) - (centerNode.x + (centerNode.width / 2));
                yDelta = (controlSize.y) - (centerNode.y + centerNode.height + 2);
            } else {
                // Center on left
                xDelta = -centerNode.x + 2;
                yDelta = (controlSize.y / 2) - (centerNode.y + (centerNode.height / 2));
            }
        } else if (centerNode.outgoing.isEmpty() && !centerNode.incoming.isEmpty()) {
            // No outgoing : center on the right/top
            if (vertical) {
                // Center on top
                xDelta = (controlSize.x / 2) - (centerNode.x + (centerNode.width / 2));
                yDelta = -centerNode.y + 2;
            } else {
                // Center on right
                xDelta = (controlSize.x) - (centerNode.x + centerNode.width + 2);
                yDelta = (controlSize.y / 2) - (centerNode.y + (centerNode.height / 2));
            }
        } else {
            // Center
            xDelta = (controlSize.x / 2) - (centerNode.x + (centerNode.width / 2));
            yDelta = (controlSize.y / 2) - (centerNode.y + (centerNode.height / 2));
        }
        
        // Move all nodes
        for (Object nodeObj : directedGraph.nodes) {
            Node node = (Node) nodeObj;
            node.x += xDelta;
            node.y += yDelta;
        }
    }

    @objid ("e5f57cc7-5efd-11e2-a8be-00137282c51b")
    public BackgroundEditPart(IMModelServices modelServices) {
        BackgroundEditPart.modelServices = modelServices;
    }

}
