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
                                    

package org.modelio.diagram.api.dg.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.api.diagram.dg.IDiagramDG;
import org.modelio.api.diagram.dg.IDiagramDrawingsLayer;
import org.modelio.api.diagram.dg.IDiagramElementsLayer;
import org.modelio.api.diagram.dg.IDiagramLayer;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.api.services.DiagramNode;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmLink;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.drawings.core.IGmDrawingLayer;

/**
 * This class is the top level DiagramNode of a diagram. It represents the diagram itself.
 */
@objid ("808dcc97-0d69-4b50-ac39-bb4b6966e2af")
public abstract class DiagramDG extends DiagramNode implements IDiagramDG, IDiagramElementsLayer, IDiagramDrawingsLayer {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("6c1cee84-f492-41ad-acac-2d898e724a37")
    public DiagramDG(DiagramHandle diagramHandle, GmNodeModel node) {
        super(diagramHandle, node);
    }

    @objid ("039c838c-959e-4e22-9e4d-8de0748e1f00")
    @Override
    public DiagramNode getParent() {
        return null;
    }

    @objid ("e896c5a0-85e3-482c-8aeb-8af0e0667a5e")
    @Override
    public List<IDiagramNode> getNodes() {
        return DGFactory.getInstance().getDiagramNodes(this.diagramHandle, ((GmCompositeNode) this.gmNode).getVisibleChildren());
    }

    @objid ("25e13791-2d8c-496f-8874-df1adbd2d6ee")
    @Override
    public final Rectangle getBounds() {
        GraphicalEditPart p = this.diagramHandle.getEditPart(this.gmNode);
        return computeMinimumBounds(p.getFigure());
    }

    @objid ("1028d2a7-3328-45d7-9e98-39b8353776e3")
    @Override
    public final void setBounds(Rectangle bounds) {
        // nothing to do
    }

    @objid ("4af5aaed-8dcd-4b3c-9b16-e1235e034a5a")
    @Override
    public List<IDiagramLink> getFromLinks() {
        return Collections.emptyList();
    }

    @objid ("159a8361-dace-446a-9bd6-8dc71d4b53db")
    @Override
    public List<IDiagramLink> getToLinks() {
        return Collections.emptyList();
    }

    /**
     * @return the list of all the links present in the diagram
     */
    @objid ("d15b1396-c5cf-4b1a-9d7e-b1c866f51081")
    @Override
    public List<IDiagramLink> getLinks() {
        List<IDiagramLink> links = new ArrayList<>();
        
        for (GmModel gm : this.gmNode.getDiagram().getAllModels()) {
            if (gm instanceof IGmLink) {
                IDiagramLink diagramLink = DGFactory.getInstance().getDiagramLink(this.diagramHandle, (IGmLink) gm);
                if (diagramLink != null) {
                    links.add(diagramLink);
                }
            }
        }
        return links;
    }

    /**
     * Computes the minimum bounds of a diagram figure. The returned rectangle is the smallest rectangle enclosing all
     * the diagram nodes (note: the computation does not take links into account which are laid in the Connection layer)
     * @param iFigure @return
     */
    @objid ("bab00664-04aa-45ad-8ffe-f0aaf7d43910")
    private Rectangle computeMinimumBounds(final IFigure iFigure) {
        Rectangle ret = null;
        
        for (Object fig : iFigure.getChildren()) {
            Rectangle b;
            if (fig instanceof FreeformFigure) {
                FreeformFigure f = (FreeformFigure) fig;
                b = computeMinimumBounds(f);
            } else {
                b = ((Figure) fig).getBounds();
            }
            
            if (ret == null)
                ret = b;
            else
                ret.union(b);
        }
        return ret;
    }

    @objid ("6a9fa5e2-9741-46bb-81a1-600d1da1863d")
    @Override
    public final boolean setLocation(final int x, final int y) {
        return false;
    }

    @objid ("cab9db01-147c-4a1c-b318-38ad4d5008e4")
    @Override
    public final boolean setSize(final int x, final int y) {
        return false;
    }

    @objid ("826d3e82-a0cd-439e-8659-3e6cca6a5718")
    @Override
    public IDiagramDrawingsLayer getDrawingsLayer(String layerIdentifier) {
        GmAbstractDiagram dg = getDiagramModel();
        
        switch (layerIdentifier) {
        case IDiagramDrawingsLayer.BACKGROUND:
            return (IDiagramDrawingsLayer) DGFactory.getInstance().getDiagramLayer(this.diagramHandle, dg.getBackgroundDrawingLayer());
        case IDiagramDrawingsLayer.TOP:
            return (IDiagramDrawingsLayer) DGFactory.getInstance().getDiagramLayer(this.diagramHandle, dg.getDrawingLayers().get(0));
        default:
            return null;
        }
    }

    @objid ("ba0c65bf-730e-4c13-b355-47309f27946d")
    @Override
    public IDiagramElementsLayer getElementsLayer(String layerIdentifier) {
        if (IDiagramElementsLayer.MAIN.equals(layerIdentifier))
            return this;
        return null;
    }

    @objid ("f87ec796-2850-4f89-abc3-f399be06e2de")
    @Override
    public List<IDiagramLayer> getLayers() {
        GmAbstractDiagram dg = getDiagramModel();
        final List<IDiagramLayer> diagramLayers = DGFactory.getInstance().getDiagramLayers(this.diagramHandle, dg.getDrawingLayers());
        diagramLayers.add(new DiagramElementLayerDG(this.diagramHandle, getDiagramModel()));
        return diagramLayers;
    }

    @objid ("67319aac-2db5-4c23-be09-a06669546729")
    @Override
    public IDiagramLayer getLayer(String layerIdentifier) {
        Objects.requireNonNull(layerIdentifier, "layer identifier is null.");
        
        switch (layerIdentifier) {
        case IDiagramElementsLayer.MAIN:
            return new DiagramElementLayerDG(this.diagramHandle, getDiagramModel());
        case IDiagramDrawingsLayer.BACKGROUND:
            return new DiagramDrawingLayerDG(this.diagramHandle, getDiagramModel().getBackgroundDrawingLayer());
        case IDiagramDrawingsLayer.TOP:
            final List<IGmDrawingLayer> drawingLayers = getDiagramModel().getDrawingLayers();
            return new DiagramDrawingLayerDG(this.diagramHandle, drawingLayers.get(drawingLayers.size() - 1));
        default:
            for (IDiagramLayer l : getLayers())
                if (layerIdentifier.equals(l.getName()))
                    return l;
            
        }
        return null;
    }

    @objid ("173a0865-2881-4b2e-bad5-b61b966a162d")
    protected GmAbstractDiagram getDiagramModel() {
        return (GmAbstractDiagram) this.gmNode;
    }

    /**
     * @return the element nodes.
     */
    @objid ("97fd67e9-d334-4bc7-a60d-4cdecf6f4b59")
    @Override
    public List<IDiagramNode> getElementNodes() {
        return getNodes();
    }

    /**
     * @return the element links.
     */
    @objid ("84f7047c-dd60-4b6a-9d00-6fd5ee7e80c1")
    @Override
    public List<IDiagramLink> getElementLinks() {
        return getLinks();
    }

    @objid ("1f9718d0-7d66-4b5c-8193-1e37d8f14a1d")
    @Override
    public List<IDiagramNode> getDrawingNodes() {
        List<IDiagramNode> ret = new ArrayList<>();
        
        for (IDiagramLayer l : getLayers()) {
            if (l instanceof IDiagramDrawingsLayer) {
                IDiagramDrawingsLayer dl = (IDiagramDrawingsLayer) l;
                ret.addAll(dl.getDrawingNodes());
            }
        }
        return ret;
    }

    @objid ("947717e9-39e5-4477-b42f-a793db243b8e")
    @Override
    public List<IDiagramLink> getDrawingLinks() {
        List<IDiagramLink> ret = new ArrayList<>();
        
        for (IDiagramLayer l : getLayers()) {
            if (l instanceof IDiagramDrawingsLayer) {
                IDiagramDrawingsLayer dl = (IDiagramDrawingsLayer) l;
                ret.addAll(dl.getDrawingLinks());
            }
        }
        return ret;
    }

}
