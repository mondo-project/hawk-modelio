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
                                    

package org.modelio.diagram.api.tools;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.modelio.api.diagram.IDiagramGraphic;
import org.modelio.api.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.diagram.tools.ILinkCommand;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.api.dg.LinkPath;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.editor.IDiagramEditor;
import org.modelio.diagram.editor.plugin.DiagramEditorsManager;
import org.modelio.diagram.elements.core.link.ConnectionRouterRegistry;
import org.modelio.diagram.elements.core.link.CreateBendedConnectionRequest;
import org.modelio.diagram.elements.core.link.path.ConnectionHelperFactory;
import org.modelio.diagram.elements.core.link.path.IConnectionHelper;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.requests.CreateLinkConstants;
import org.modelio.diagram.elements.core.tools.BendedConnectionCreationTool;
import org.modelio.metamodel.diagrams.AbstractDiagram;

/**
 * Tool used for "link like" interactions in diagrams for modules.
 */
@objid ("d1104fb9-20b0-4f54-bd96-136302efab45")
public class LinkTool extends BendedConnectionCreationTool {
    @objid ("07af8348-9737-48e2-b271-7a42c9f54122")
    protected GmModel sourceGm;

    @objid ("657d3847-fae3-4078-9143-d969af6ec9fa")
    protected ILinkCommand linkCommand;

    /**
     * PropertyDefinition name for the actual handler on the module side.
     */
    @objid ("928eaf04-8866-4277-a903-291596d9f567")
    public static final Object PROPERTY_HANDLER = "handler";

    @objid ("63278321-cb9f-4093-87ff-d8d07e8f1b99")
    private DiagramHandle diagramHandle = null;

    /**
     * C'tor, used by platform to instantiate the tool by reflexion.
     */
    @objid ("7e47695b-1980-441c-9115-9006327f1b80")
    public LinkTool() {
        this.linkCommand = null;
    }

    @objid ("f1561a77-728a-49ba-b922-cb9dcda57ca9")
    @Override
    protected org.eclipse.gef.EditPartViewer.Conditional getTargetingConditional() {
        return new EditPartViewer.Conditional() {
            @SuppressWarnings("synthetic-access")
            @Override
            public boolean evaluate(EditPart editpart) {
                if (LinkTool.super.getTargetingConditional().evaluate(editpart)) {
                    return doAccept(editpart);
                }
        return false;
                    }
                };
    }

    @objid ("25eb4915-37a4-48d2-a7c8-d79b6fd47f9a")
    @Override
    protected void executeCurrentCommand() {
        if (this.getTargetEditPart() == null) {
            return;
        }
        
        CreateBendedConnectionRequest request = getTargetRequest();
        EditPart targetEditPart = getTargetEditPart();
        GmModel targetModel = (GmModel) targetEditPart.getModel();
        initDiagramHandle(targetModel);
        
        IDiagramGraphic dg = null;
        while (dg == null && targetModel != null) {
            dg = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, targetModel);
            targetModel = targetModel.getParent();
        }
        
        NodeEditPart source = (NodeEditPart) request.getSourceEditPart();
        ConnectionAnchor sourceAnchor = source.getSourceConnectionAnchor(request);
        
        NodeEditPart target = (NodeEditPart) request.getTargetEditPart();
        ConnectionAnchor targetAnchor = target.getTargetConnectionAnchor(request);
        
        // Additional step: add the optional bend points.
        List<Point> points = new ArrayList<>();
        // Create a "dummy" connection to translate the coordinates of points in the request (absolute) to relative to the connection layer
        PolylineConnection dummyConnection = new PolylineConnection();
        IFigure connectionsLayer = LayerManager.Helper.find(targetEditPart)
                                                      .getLayer(LayerConstants.CONNECTION_LAYER);
        connectionsLayer.add(dummyConnection);
        dummyConnection.setSourceAnchor(sourceAnchor);
        dummyConnection.setTargetAnchor(targetAnchor);
        ConnectionRouter router = ((ConnectionRouterRegistry) targetEditPart.getViewer().getProperty(ConnectionRouterRegistry.ID)).get(request.getData().getRoutingMode());
        dummyConnection.setConnectionRouter(router);
        IConnectionHelper connPath = ConnectionHelperFactory.createFromRawData(request.getData(),
                                                                               dummyConnection);
        points.addAll(connPath.getBendPoints());
        
        // Add source anchor position
        if (points.isEmpty()) {
            Point.SINGLETON.setLocation(sourceAnchor.getLocation(targetAnchor.getReferencePoint()));
            dummyConnection.translateToRelative(Point.SINGLETON);
            points.add(0, Point.SINGLETON.getCopy());
        } else {
            Point.SINGLETON.setLocation(points.get(0));
            dummyConnection.translateToAbsolute(Point.SINGLETON);
            Point.SINGLETON.setLocation(sourceAnchor.getLocation(Point.SINGLETON));
            dummyConnection.translateToRelative(Point.SINGLETON);
            points.add(0, Point.SINGLETON.getCopy());
        }
        
        // Add target anchor position
        Point.SINGLETON.setLocation(points.get(points.size() - 1));
        dummyConnection.translateToAbsolute(Point.SINGLETON);
        Point.SINGLETON.setLocation(targetAnchor.getLocation(Point.SINGLETON));
        dummyConnection.translateToRelative(Point.SINGLETON);
        points.add(Point.SINGLETON.getCopy());
        
        connectionsLayer.remove(dummyConnection);
        
        LinkPath path = new LinkPath(points);
        
        // Delegate the execution to the linkCommand handler
        LinkRouterKind routerKind;
        switch (request.getData().getRoutingMode()) {
            case DIRECT: {
                routerKind = LinkRouterKind.DIRECT;
                break;
            }
            case ORTHOGONAL: {
                routerKind = LinkRouterKind.ORTHOGONAL;
                break;
            }
            default:
            case BENDPOINT: {
                routerKind = LinkRouterKind.BENDPOINT;
                break;
            }
        }
        IDiagramGraphic sourceDg = null;
        GmModel sourceModel = this.sourceGm;
        while (sourceDg == null && sourceModel != null) {
            sourceDg = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, sourceModel);
            sourceModel = sourceModel.getParent();
        }
        this.linkCommand.actionPerformed(this.diagramHandle, sourceDg, dg, routerKind, path);
        
        this.setCurrentCommand(null);
    }

    @objid ("5d8a333a-5aa5-4a95-ac2a-289bf3a89c4e")
    @Override
    protected void applyProperty(final Object key, final Object value) {
        if (PROPERTY_HANDLER.equals(key)) {
            if (value instanceof ILinkCommand) {
                this.linkCommand = (ILinkCommand) value;
            }
            return;
        }
        super.applyProperty(key, value);
    }

    /**
     * Sets the tools state.
     * @param state the new state
     */
    @objid ("f5a26eb0-323d-45f7-9f17-fef60bf0eea0")
    @Override
    protected void setState(final int state) {
        super.setState(state);
        
        if (state == STATE_CONNECTION_STARTED) {
            this.sourceGm = (GmModel) getTargetEditPart().getModel();
        }
    }

    /**
     * Updates the target editpart and returns <code>true</code> if the target changes. The target is updated by using
     * the target conditional and the target request. If the target has been locked, this method does nothing and
     * returns <code>false</code>.
     * @return <code>true</code> if the target was changed
     */
    @objid ("edb15184-9ccb-4ff8-8d15-bbbf55aed856")
    @Override
    protected boolean updateTargetUnderMouse() {
        if (!isTargetLocked()) {
            getTargetRequest().setType(getCommandName());
            EditPart editPart = getTargetUnderMouse();
        
            if (editPart == null && isInState(STATE_CONNECTION_STARTED)) {
                // If the target cannot end the link, ask him to add a bendpoint
                getTargetRequest().setType(CreateLinkConstants.REQ_CONNECTION_ADD_BENDPOINT);
                editPart = getTargetUnderMouse();
            }
        
            return updateTargetEditPart(editPart);
        
        } else {
            return false;
        }
    }

    /**
     * Same as {@link org.eclipse.gef.tools.TargetingTool#setTargetEditPart(EditPart) setTargetEditPart(EditPart)} but
     * returns whether a change was done or not.
     * @param editPart The new edit part, may be null
     * @return true if the edit part was changed, false if it is still the same.
     */
    @objid ("3205d8be-74d2-4231-8044-3d3403a4d527")
    private boolean updateTargetEditPart(final EditPart editPart) {
        boolean changed = getTargetEditPart() != editPart;
        setTargetEditPart(editPart);
        return changed;
    }

    /**
     * Find the target editpart and returns it. The target is searched by using the target conditional and the target
     * request.
     * @return the edit part that can handle the request under the mouse.
     */
    @objid ("7bdc4ca2-520d-4e1a-812c-a5e8c2bc1003")
    private EditPart getTargetUnderMouse() {
        EditPart editPart = getCurrentViewer().findObjectAtExcluding(getLocation(),
                                                                     getExclusionSet(),
                                                                     getTargetingConditional());
        if (editPart != null) {
            // First, check if we are creating intermediary points
            if (getTargetRequest().getType().equals(CreateLinkConstants.REQ_CONNECTION_ADD_BENDPOINT)) {
                editPart = editPart.getTargetEditPart(getTargetRequest());
            } else if (doAccept(editPart)) { // This is a start or end request, check accept
                editPart = editPart.getTargetEditPart(getTargetRequest());
            } else { // Element not accepted, refuse it
                editPart = null;
            }
        }
        return editPart;
    }

    @objid ("e38f67e9-9c66-4d72-9609-5d3f470c2f80")
    protected boolean doAccept(final EditPart editpart) {
        GmModel targetModel = (GmModel) editpart.getModel();
        initDiagramHandle(targetModel);
        
        IDiagramGraphic dg = null;
        while (dg == null && targetModel != null) {
            dg = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, targetModel);
            targetModel = targetModel.getParent();
        }
        
        if (dg == null)
            return false;
        
        if (isInState(STATE_CONNECTION_STARTED | STATE_ACCESSIBLE_DRAG_IN_PROGRESS)) {
            IDiagramGraphic sourceDg = null;
            GmModel sourceModel = this.sourceGm;
            while (sourceDg == null && sourceModel != null) {
                sourceDg = DGFactory.getInstance().getDiagramGraphic(this.diagramHandle, sourceModel);
                sourceModel = sourceModel.getParent();
            }
            return this.linkCommand.acceptSecondElement(this.diagramHandle, sourceDg, dg);
        } else {
            return this.linkCommand.acceptFirstElement(this.diagramHandle, dg);
        }
    }

    @objid ("17e0ba7d-610a-49bb-8f60-4db2a0bef762")
    private void initDiagramHandle(final GmModel targetModel) {
        if (this.diagramHandle == null) {
            // Create a diagram handle on the opened editor (there must be one: we are in one of its tools!).
            AbstractDiagram diagram = (AbstractDiagram) targetModel.getDiagram().getRelatedElement();
            IDiagramEditor editor = (IDiagramEditor)  DiagramEditorsManager.getInstance().get(diagram).getObject(); 
        
            this.diagramHandle = DiagramHandle.create(editor);
        }
    }

    @objid ("8d4de317-635e-40f4-a73e-c1ab53da69b6")
    @Override
    public void deactivate() {
        super.deactivate();
        if (this.diagramHandle != null) {
            this.diagramHandle.close();
            this.diagramHandle = null;
        }
    }

}
