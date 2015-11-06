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
import java.beans.PropertyChangeSupport;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.graph.CompoundDirectedGraph;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;
import org.modelio.linkeditor.view.node.GraphNode;

/**
 * Model of the background of the Link Editor.
 * 
 * @author fpoyer
 */
@objid ("1b8a1da3-5e33-11e2-b81d-002564c97630")
public class BackgroundModel extends CompoundDirectedGraph {
    @objid ("1b8a1da8-5e33-11e2-b81d-002564c97630")
     static final String CONTENT = "Content";

    @objid ("1b8a1da7-5e33-11e2-b81d-002564c97630")
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    @objid ("1b8ee013-5e33-11e2-b81d-002564c97630")
    private GraphNode center;

    /**
     * C'tor.
     */
    @objid ("1b8ee014-5e33-11e2-b81d-002564c97630")
    public BackgroundModel() {
    }

    @objid ("1b8ee017-5e33-11e2-b81d-002564c97630")
    public void contentChanged() {
        this.listeners.firePropertyChange(new PropertyChangeEvent(this, BackgroundModel.CONTENT, null, this.nodes));
    }

    @objid ("1b8ee019-5e33-11e2-b81d-002564c97630")
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        // Making sure a listener is only added once.
        this.listeners.removePropertyChangeListener(listener);
        this.listeners.addPropertyChangeListener(listener);
    }

    @objid ("1b8ee01d-5e33-11e2-b81d-002564c97630")
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        this.listeners.removePropertyChangeListener(listener);
    }

    /**
     * Adds the given node to the graph. Does not add its incoming and outgoing edges.
     * @param node the node to add to the graph.
     */
    @objid ("1b8ee021-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("unchecked")
    public void addNode(final Node node) {
        // Make sure a node is not twice in the list.
        this.nodes.remove(node);
        this.nodes.add(node);
        //contentChanged();
    }

    /**
     * Removes the given node from the graph. Does not remove its incoming and outgoing edges.
     * @param node the node to remove from the graph
     */
    @objid ("1b8ee029-5e33-11e2-b81d-002564c97630")
    @Override
    public void removeNode(final Node node) {
        super.removeNode(node);
        //contentChanged();
    }

    /**
     * Adds the given edge to the graph. Does not add its source and target nodes NOR its virtual nodes (if any).
     * @param edge the edge to add to the graph.
     */
    @objid ("1b8ee031-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("unchecked")
    public void addEdge(final Edge edge) {
        // Make sure the edge is not twice in the list.
        this.edges.remove(edge);
        this.edges.add(edge);
        //contentChanged();
    }

    /**
     * Removes the given edge from the graph and from its source and target nodes. Also remove its virtual nodes if any.
     * @param edge the edge to be removed
     */
    @objid ("1b8ee039-5e33-11e2-b81d-002564c97630")
    @Override
    public void removeEdge(final Edge edge) {
        super.removeEdge(edge);
        //contentChanged();
    }

    @objid ("1b8ee041-5e33-11e2-b81d-002564c97630")
    public GraphNode getCenter() {
        return this.center;
    }

    @objid ("1b8ee045-5e33-11e2-b81d-002564c97630")
    public void setCenter(final GraphNode center) {
        this.center = center;
    }

}
