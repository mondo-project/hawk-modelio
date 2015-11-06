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
                                    

package org.modelio.diagram.editor.silent;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.SelectionTool;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.editor.AbstractDiagramEditor;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.IDiagramEditor;
import org.modelio.diagram.editor.ScalableFreeformRootEditPart2;
import org.modelio.diagram.editor.tools.PanSelectionTool;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.figures.routers.OrthogonalRouter;
import org.modelio.diagram.elements.core.link.ConnectionRouterRegistry;
import org.modelio.diagram.elements.editpartFactory.ModelioEditPartFactory;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Quite the same as an {@link AbstractDiagramEditor}, only with nothing visible.
 * 
 * @author fpoyer
 */
@objid ("66995c53-33f7-11e2-95fe-001ec947c8cc")
public class SilentDiagramEditor implements IDiagramEditor {
    @objid ("66995c55-33f7-11e2-95fe-001ec947c8cc")
    private final RootEditPart rootEditPart = new ScalableFreeformRootEditPart2();

    @objid ("66995c57-33f7-11e2-95fe-001ec947c8cc")
    private EditDomain editDomain;

    @objid ("66995c58-33f7-11e2-95fe-001ec947c8cc")
    private GraphicalViewer graphicalViewer;

    @objid ("66995c59-33f7-11e2-95fe-001ec947c8cc")
    private DiagramEditorInput editorInput = null;

    @objid ("2c847454-3a3d-11e2-a430-001ec947c8cc")
    private IProjectService projectService;

    /**
     * C'tor.
     * @param input the editor input to use as data source.
     * @param projectService a project service
     */
    @objid ("66995c5a-33f7-11e2-95fe-001ec947c8cc")
    public SilentDiagramEditor(final DiagramEditorInput input, IProjectService projectService) {
        this.projectService = projectService;
        this.setEditDomain(new EditDomain());
        this.editorInput = input;
        
        createGraphicalViewer();
    }

    @objid ("66995c5f-33f7-11e2-95fe-001ec947c8cc")
    private void configureGraphicalViewer() {
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        // Set the root edit part
        viewer.setRootEditPart(this.rootEditPart);
        viewer.setEditPartFactory(SilentDiagramEditor.getEditPartFactory());
        
        // Configure the edit domain
        // Set the active and default tool
        final SelectionTool selectionTool = new PanSelectionTool();
        this.getEditDomain().setActiveTool(selectionTool);
        this.getEditDomain().setDefaultTool(selectionTool);
        
        viewer.setEditDomain(this.getEditDomain());
        
        // Plug our own command stack that is bound to the Modelio transaction
        // manager
        this.getEditDomain().setCommandStack(new DiagramCommandStack(getModelingSession().getTransactionSupport()));
    }

    @objid ("66995c61-33f7-11e2-95fe-001ec947c8cc")
    private static void initializeConnectionRouters(final ConnectionRouterRegistry routersRegistry) {
        routersRegistry.put(StyleKey.ConnectionRouterId.DIRECT, ConnectionRouter.NULL);
        routersRegistry.put(StyleKey.ConnectionRouterId.BENDPOINT, new BendpointConnectionRouter());
        routersRegistry.put(StyleKey.ConnectionRouterId.ORTHOGONAL, new OrthogonalRouter());
    }

    @objid ("66995c65-33f7-11e2-95fe-001ec947c8cc")
    private void createGraphicalViewer() {
        final GraphicalViewer viewer = new NoControlGraphicalViewer();
        viewer.createControl(null);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        initializeGraphicalViewer();
    }

    /**
     * Notifies the editor that the handle opened on it has been closed. Depending on its nature, the editor might decide to delete
     * itself, release some resources, or ignore this event altogether.
     */
    @objid ("66995c67-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void disposeHandle() {
        // Dispose the editor input
        DiagramEditorInput input = (this.getEditorInput());
        if (input != null) {
            input.dispose();
        }
        
        getEditDomain().setActiveTool(null);
    }

    @objid ("66995c6b-33f7-11e2-95fe-001ec947c8cc")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(final Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) this.getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        if (type == GraphicalViewer.class) {
            return getGraphicalViewer();
        }
        if (type == CommandStack.class) {
            return getCommandStack();
        }
        if (type == EditPart.class && getGraphicalViewer() != null) {
            return getGraphicalViewer().getRootEditPart();
        }
        if (type == IFigure.class && getGraphicalViewer() != null) {
            return ((GraphicalEditPart) getGraphicalViewer().getRootEditPart()).getFigure();
        }
        /**
         * This implementation of the method declared by <code>IAdaptable</code> passes the request along to the platform's adapter
         * manager; roughly <code>Platform.getAdapterManager().getAdapter(this, adapter)</code>.
         */
        return Platform.getAdapterManager().getAdapter(this, type);
    }

    @objid ("66995c72-33f7-11e2-95fe-001ec947c8cc")
    private CommandStack getCommandStack() {
        return getEditDomain().getCommandStack();
    }

    @objid ("66995c76-33f7-11e2-95fe-001ec947c8cc")
    private EditDomain getEditDomain() {
        return this.editDomain;
    }

    @objid ("66995c7a-33f7-11e2-95fe-001ec947c8cc")
    private GraphicalViewer getGraphicalViewer() {
        return this.graphicalViewer;
    }

    @objid ("66995c7e-33f7-11e2-95fe-001ec947c8cc")
    private void initializeGraphicalViewer() {
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        // Initialize connection routers
        final ConnectionRouterRegistry routersRegistry = new ConnectionRouterRegistry();
        viewer.setProperty(ConnectionRouterRegistry.ID, routersRegistry);
        SilentDiagramEditor.initializeConnectionRouters(routersRegistry);
        
        // Set the viewer content
        GmAbstractDiagram gmDiagram = this.getEditorInput().getGmDiagram();
        viewer.setContents(gmDiagram);
        // Force a complete refresh now that edit parts are finally listening to events that might
        // be sent by the model (e.g.: links that have changed source and/or target while diagram
        // was closed).
        gmDiagram.refreshAllFromObModel();
    }

    /**
     * Return the root edit part of this editor.
     * @return the root edit part of this editor.
     */
    @objid ("66995c80-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public RootEditPart getRootEditPart() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.rootEditPart;
    }

    @objid ("669bbe93-33f7-11e2-95fe-001ec947c8cc")
    private static EditPartFactory getEditPartFactory() {
        // return an EditPart factory, here we can return the Modelio UML
        // standard EditPartFactory
        return ModelioEditPartFactory.getInstance();
    }

    @objid ("669bbe97-33f7-11e2-95fe-001ec947c8cc")
    private void setEditDomain(final EditDomain ed) {
        this.editDomain = ed;
    }

    @objid ("669bbe9b-33f7-11e2-95fe-001ec947c8cc")
    private void setGraphicalViewer(final GraphicalViewer viewer) {
        getEditDomain().addViewer(viewer);
        this.graphicalViewer = viewer;
    }

    /**
     * Returns the input for this editor.
     * @return the editor input
     */
    @objid ("669bbe9f-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public DiagramEditorInput getEditorInput() {
        return this.editorInput;
    }

    @objid ("2c847450-3a3d-11e2-a430-001ec947c8cc")
    private final ICoreSession getModelingSession() {
        return this.projectService.getSession();
    }

}
