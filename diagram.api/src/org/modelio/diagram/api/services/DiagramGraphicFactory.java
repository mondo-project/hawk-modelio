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
                                    

package org.modelio.diagram.api.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramGraphicFactory;
import org.modelio.api.diagram.IDiagramLink;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.api.diagram.dg.IDiagramDrawingsLayer;
import org.modelio.diagram.editor.ScalableFreeformRootEditPart2;
import org.modelio.diagram.elements.core.commands.DrawingObjectFactory;
import org.modelio.diagram.elements.core.link.CreateBendedConnectionRequest;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.drawings.core.IGmDrawing;
import org.modelio.diagram.elements.drawings.ellipse.GmEllipseDrawing;
import org.modelio.diagram.elements.drawings.line.GmLineDrawing;
import org.modelio.diagram.elements.drawings.rectangle.GmRectangleDrawing;
import org.modelio.diagram.elements.drawings.text.GmTextDrawing;
import org.modelio.diagram.styles.core.StyleKey.ConnectionRouterId;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Implementation of {@link IDiagramGraphicFactory}.
 */
@objid ("384732da-2879-4997-97c7-e2e131229a0c")
class DiagramGraphicFactory implements IDiagramGraphicFactory {
    @objid ("66535a0e-3403-40fc-8adf-f03a7df9a011")
    private DiagramHandle diagramHandle;

    @objid ("34d389c1-6d7f-4f28-9b79-cccdcae8e5e4")
    public DiagramGraphicFactory(DiagramHandle diagramHandle) {
        this.diagramHandle = diagramHandle;
    }

    @objid ("92dac9ec-75e2-459b-837b-c95b817fcb4f")
    @Override
    public IDiagramNode createDrawingRectangle(IDiagramDrawingsLayer layer, String drawingIdentifier, int x, int y, int w, int h) {
        return createDrawingNode(GmRectangleDrawing.class, layer, drawingIdentifier, x, y, w, h);
    }

    @objid ("daaf364b-bb13-4a7b-8ffc-d700a7336975")
    protected IDiagramNode createDrawingNode(Class<? extends IGmDrawing> gmClass, IDiagramDrawingsLayer layer, String drawingIdentifier, int x, int y, int w, int h) {
        final GraphicalEditPart p = this.diagramHandle.getEditPart(((DiagramGraphic) layer).getModel());
        if (p == null) {
            return null;
        }
        
        // Adapt the given coordinates if necessary
        final Point newLocation = new Point(x, y);
        final Dimension size = new Dimension(w,h);
        
        final IFigure rootFigure = ((GraphicalEditPart) p.getViewer().getRootEditPart()).getFigure();
        rootFigure.translateToParent(newLocation);
        rootFigure.translateToParent(size);
        
        DrawingObjectFactory creationFactory = new DrawingObjectFactory(gmClass, drawingIdentifier);
        
        final CreateRequest req = new CreateRequest();
        req.setType(RequestConstants.REQ_CREATE);
        req.setLocation(newLocation);
        req.setSize(size);
        req.setFactory(creationFactory);
        final Command com = p.getCommand(req);
        if (com != null && com.canExecute()) {
            p.getViewer().getEditDomain().getCommandStack().execute(com);
            p.getFigure().getUpdateManager().performValidation();
            return (IDiagramNode) this.diagramHandle.getDrawingGraphic((String)creationFactory.getNewObject());
        } else {
            return null;
        }
    }

    @objid ("90cd95da-5e01-4706-9cc3-5bb01dfaf2ae")
    @Override
    public IDiagramNode createDrawingEllipse(IDiagramDrawingsLayer layer, String drawingIdentifier, int x, int y, int w, int h) {
        return createDrawingNode(GmEllipseDrawing.class, layer, drawingIdentifier, x, y, w, h);
    }

    @objid ("55e83d2c-4f0d-4535-b65e-7ca34bcbb8ef")
    @Override
    public IDiagramNode createDrawingText(IDiagramDrawingsLayer layer, String drawingIdentifier, String label, int x, int y, int w, int h) {
        return createDrawingNode(GmTextDrawing.class, layer, drawingIdentifier, x, y, w, h);
    }

    @objid ("8535cd61-abc7-4e89-ae82-79ce5265a414")
    @Override
    public IDiagramLink createDrawingLine(IDiagramDrawingsLayer layer, String drawingIdentifier, int x, int y, int x2, int y2) {
        return createDrawingConnection(GmLineDrawing.class, layer, drawingIdentifier, x, y, x2, y2);
    }

    @objid ("a0dbce84-a12c-4e5a-944d-420fa5c5c7b0")
    protected IDiagramLink createDrawingConnection(Class<? extends IGmDrawing> gmClass, IDiagramDrawingsLayer layer, String drawingIdentifier, int x, int y, int x2, int y2) {
        final GraphicalEditPart layerPart = this.diagramHandle.getEditPart(((DiagramGraphic) layer).getModel());
        if (layerPart == null) {
            return null;
        }
        
        // Adapt the given coordinates if necessary
        final Point p1 = new Point(x, y);
        final Point p2 = new Point(x2, y2);
        
        final GraphicalViewer viewer = (GraphicalViewer) layerPart.getViewer();
        final IFigure rootFigure = ((GraphicalEditPart) viewer.getRootEditPart()).getFigure();
        rootFigure.translateToParent(p1);
        rootFigure.translateToParent(p2);
        
        
        DrawingObjectFactory creationFactory = new DrawingObjectFactory(gmClass, drawingIdentifier);
        
        // First point
        // -----------
        final CreateConnectionRequest req = new CreateConnectionRequest();
        req.setType(RequestConstants.REQ_CONNECTION_START);
        req.setLocation(p1);
        req.setFactory(creationFactory);
        
        @SuppressWarnings("unchecked")
        List<?> toExclude = new ArrayList<>(layerPart.getParent().getChildren());
        toExclude.remove(layerPart);
        
        EditPart targetEditPart = viewer.findObjectAtExcluding(p1, toExclude, new Conditional(req));
        
        if (targetEditPart == null)
            return null;
        
        req.setTargetEditPart(targetEditPart);
        Command com = targetEditPart.getCommand(req);
        
        if (com == null || !com.canExecute())
            return null;
        
        req.setStartCommand(com);
        req.setSourceEditPart(targetEditPart);
        
        // Second point
        // -----------
        req.setType(RequestConstants.REQ_CONNECTION_END);
        req.setLocation(p2);
        //req.getData().setRoutingMode(ConnectionRouterId.BENDPOINT);
        
        targetEditPart = viewer.findObjectAtExcluding(p2, toExclude, new Conditional(req));
        
        if (targetEditPart == null)
            return null;
        
        req.setTargetEditPart(targetEditPart);
        com = targetEditPart.getCommand(req);
        
        if (com == null || !com.canExecute())
            return null;
        
        
        viewer.getEditDomain().getCommandStack().execute(com);
        layerPart.getFigure().getUpdateManager().performValidation();
        return (IDiagramLink) this.diagramHandle.getDrawingGraphic((String)creationFactory.getNewObject());
    }

    @objid ("f47d13f7-ac88-4f80-af2e-4081c1303964")
    @Override
    public List<IDiagramGraphic> unmask(MObject element, int x, int y) {
        if (element == null) {
            return Collections.emptyList();
        }
        List<IDiagramGraphic> existingGraphics = this.diagramHandle.getDiagramGraphics(element);
        List<GmModel> existingGMs = this.diagramHandle.getDiagramGraphicModels(element);
        
        GraphicalEditPart diagramEditPart = this.diagramHandle.getEditPart(this.diagramHandle.getDiagramEditorInput().getGmDiagram());
        GraphicalViewer viewer = (GraphicalViewer) diagramEditPart.getViewer();
        Point dropLocation = new Point(x, y);
        
        // Adapt the given coordinates if necessary
        ((ScalableFreeformRootEditPart2) diagramEditPart.getViewer().getRootEditPart()).getFigure().translateToParent(dropLocation);
        
        final ModelElementDropRequest req = new ModelElementDropRequest();
        req.setDroppedElements(new MObject[] { element });
        req.setLocation(dropLocation);
        req.isSmart(false);
        
        EditPart targetEditPart = viewer.findObjectAtExcluding(dropLocation, Collections.EMPTY_LIST, new Conditional(req));
        
        targetEditPart = targetEditPart.getTargetEditPart(req);
        if (targetEditPart != null) {
        
            Command com = targetEditPart.getCommand(req);
            if (com != null && com.canExecute()) {
                targetEditPart.getViewer().getEditDomain().getCommandStack().execute(com);
        
                List<IDiagramGraphic> allGraphics = this.diagramHandle.getDiagramGraphics(element);
                List<IDiagramGraphic> results = new ArrayList<>();
                for (IDiagramGraphic dg : allGraphics) {
                    if (!existingGraphics.contains(dg)) {
                        results.add(dg);
                    }
                }
        
                List<GmModel> allGMs = this.diagramHandle.getDiagramGraphicModels(element);
                for (GmModel dg : allGMs) {
                    if (!existingGMs.contains(dg)) {
                        EditPart newEP = this.diagramHandle.getEditPart(dg);
                        if (newEP != null) {
                            ((GraphicalEditPart) newEP).getFigure().getUpdateManager().performValidation();
                        }
                    }
                }
        
                return results;
            }
        }
        return Collections.emptyList();
    }

    @objid ("747c03ab-c2ff-4297-b4dc-30a7d391f3b1")
    private static class Conditional implements org.eclipse.gef.EditPartViewer.Conditional {
        @objid ("f16d4ae3-5d65-4b42-8797-637b7977bcdb")
        private final Request req;

        @objid ("9a7b81dd-4cfc-4524-8bc8-6ca003a55c60")
        public Conditional(final Request req) {
            this.req = req;
        }

        @objid ("ea8c0c67-5793-4505-b2ec-b1f562944e51")
        @Override
        public boolean evaluate(final EditPart editpart) {
            return editpart.getTargetEditPart(this.req) != null;
        }

    }

}
