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
                                    

package org.modelio.diagram.editor;

import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.properties.UndoablePropertySheetPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.modelio.api.app.picking.IPickingProvider;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.editor.FlyoutPaletteComposite2.FlyoutPreferences;
import org.modelio.diagram.editor.context.ModuleMenuCreator;
import org.modelio.diagram.editor.plugin.DiagramEditor;
import org.modelio.diagram.editor.plugin.DiagramEditorsManager;
import org.modelio.diagram.editor.plugin.IDiagramConfigurer;
import org.modelio.diagram.editor.plugin.IDiagramConfigurerRegistry;
import org.modelio.diagram.editor.plugin.ToolRegistry;
import org.modelio.diagram.editor.tools.PanSelectionTool;
import org.modelio.diagram.editor.tools.PickingSelectionTool;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.figures.routers.OrthogonalRouter;
import org.modelio.diagram.elements.core.link.ConnectionRouterRegistry;
import org.modelio.diagram.styles.core.StyleKey;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.model.change.IModelChangeEvent;
import org.modelio.vcore.session.api.model.change.IModelChangeListener;
import org.modelio.vcore.session.api.model.change.IStatusChangeEvent;
import org.modelio.vcore.session.api.model.change.IStatusChangeListener;
import org.osgi.framework.Bundle;

/**
 * Graphical Editor for Diagrams.
 */
@objid ("656f5647-33f7-11e2-95fe-001ec947c8cc")
public abstract class AbstractDiagramEditor implements IModelChangeListener, IEditPartAbstractFactory, IDiagramEditor, IStatusChangeListener, CommandStackListener {
    @objid ("65741afb-33f7-11e2-95fe-001ec947c8cc")
    private static final double[] zoomLevels = { 0.25, 0.50, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.25, 3.5,
			3.75, 4.0 };

    @objid ("7a3b1c18-5e25-11e2-a8be-00137282c51b")
    private GraphicalViewer graphicalViewer;

    @objid ("7a3fe0c9-5e25-11e2-a8be-00137282c51b")
    @Inject
    private IProjectService projectService;

    @objid ("7a3fe0d0-5e25-11e2-a8be-00137282c51b")
    private final RootEditPart rootEditPart = new ScalableFreeformRootEditPart2();

    @objid ("12fb3f83-6576-41f1-9f19-1e70969fb576")
    @Inject
    public EPartService partService;

    @objid ("e177640d-fe3e-4fab-bc6d-fbe64265a6f4")
    private MPart part;

    @objid ("521aa63a-5325-4313-a1d5-0685718bb23d")
    @Inject
    @Optional
     ESelectionService selectionService;

    @objid ("9fdc7927-ee66-4e77-8b75-6cafe41ee6d4")
    @Inject
    protected EMenuService menuService;

    @objid ("a828789f-b927-4038-a41e-505e84ee0427")
    @Inject
    protected EContextService contextService;

    @objid ("65741b06-33f7-11e2-95fe-001ec947c8cc")
    private DiagramEditorInput input;

    @objid ("65741b08-33f7-11e2-95fe-001ec947c8cc")
    private IsModifiableIndicator modifIndicator = new IsModifiableIndicator();

    @objid ("65741b0d-33f7-11e2-95fe-001ec947c8cc")
    private List<Object> propertyActions = new ActionIDList();

    @objid ("65741b13-33f7-11e2-95fe-001ec947c8cc")
    private List<Object> selectionActions = new ActionIDList();

    @objid ("65741b18-33f7-11e2-95fe-001ec947c8cc")
     FlyoutPaletteComposite2 splitter;

    @objid ("65741b19-33f7-11e2-95fe-001ec947c8cc")
    private List<Object> stackActions = new ActionIDList();

    @objid ("94d1670d-cb26-43fc-bff8-fbaceafc8f91")
    private ActionRegistry actionRegistry;

    @objid ("2137022e-8f3a-4906-bfcd-486bbc862c4d")
    private EditDomain editDomain;

    @objid ("d79e7123-8254-4a76-8eec-924f6281a33a")
    private PaletteRoot paletteRoot;

    @objid ("1bb81ee1-6eb8-4c2f-a52c-fd999d657ab5")
    private PaletteViewerProvider provider;

    @objid ("06524245-6200-47a8-8e19-4d7e6dfd598e")
    private SelectionSynchronizer synchronizer;

    @objid ("cb5186f7-a046-4c59-bef5-59f27c813a65")
     static Image modifiableImage;

    @objid ("7464af77-90a4-4ae4-a6e2-dda1289e824b")
     static Image notModifiableImage;

    @objid ("983f47f7-430c-44e5-9b08-31168ed99b83")
    protected Text text;

    /**
     * Initialize all connection routers and add them to the given connection
     * router registry.
     * @param routersRegistry the connection router registry.
     */
    @objid ("65741aff-33f7-11e2-95fe-001ec947c8cc")
    protected static void initializeConnectionRouters(ConnectionRouterRegistry routersRegistry) {
        routersRegistry.put(StyleKey.ConnectionRouterId.DIRECT, ConnectionRouter.NULL);
        routersRegistry.put(StyleKey.ConnectionRouterId.BENDPOINT, new BendpointConnectionRouter());
        routersRegistry.put(StyleKey.ConnectionRouterId.ORTHOGONAL, new OrthogonalRouter());
    }

    /**
     * Constructor. Registers the editor as {@link IModelChangeListener} and
     * {@link IPickingProvider}.
     */
    @objid ("65741b1f-33f7-11e2-95fe-001ec947c8cc")
    public AbstractDiagramEditor() {
        super();
        
        this.setEditDomain(new EditDomain());
    }

    /**
     * When the command stack changes, the actions interested in the command
     * stack are updated.
     * @param event the change event
     */
    @objid ("65741b22-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void commandStackChanged(EventObject event) {
        updateActions(this.stackActions);
    }

    @objid ("65741b27-33f7-11e2-95fe-001ec947c8cc")
    protected void configureGraphicalViewer() {
        getGraphicalViewer().getControl().addFocusListener(new FocusListener() {
            private ArrayList<String> activeContexts = new ArrayList<>();
        
            @Override
            public void focusGained(FocusEvent e) {
                // Restore previously deactivated contexts
                for (String contextId : this.activeContexts) {
                    AbstractDiagramEditor.this.contextService.activateContext(contextId);
                }
                this.activeContexts = new ArrayList<>();
            }
        
            @Override
            public void focusLost(FocusEvent e) {
                // We must deactivate the active contexts when losing focus, to
                // avoid the editor's shortcuts to be triggered when editing
                // elsewhere...
        
                // Store those contexts for further reactivation
                this.activeContexts = new ArrayList<>(AbstractDiagramEditor.this.contextService.getActiveContextIds());
                for (String contextId : this.activeContexts) {
                    AbstractDiagramEditor.this.contextService.deactivateContext(contextId);
                }
            }
        });
        
        getGraphicalViewer().getControl().setBackground(ColorConstants.listBackground);
        
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        // Set the root edit part
        viewer.setRootEditPart(this.rootEditPart);
        viewer.setEditPartFactory(this.getEditPartFactory());
        
        // Configure the edit domain
        // Set the active and default tool
        SelectionTool selectionTool = new PanSelectionTool();
        getEditDomain().setActiveTool(selectionTool);
        getEditDomain().setDefaultTool(selectionTool);
        
        viewer.setEditDomain(getEditDomain());
        
        // Initialize the graphical model
        GmAbstractDiagram gmDiagram = getEditorInput() != null ? getEditorInput().getGmDiagram() : null;
        
        // Plug our own command stack that is bound to the Modelio transaction
        // manager
        this.getEditDomain().setCommandStack(new DiagramCommandStack(getModelingSession(), gmDiagram));
        
        // D&D support
        viewer.addDropTargetListener(new ModelElementDropTargetListener(viewer, this.projectService.getSession().getModel()));
        
        // Configure zoom levels
        ZoomManager zoomManager = ((ScalableFreeformRootEditPart2) this.getGraphicalViewer().getRootEditPart()).getZoomManager();
        zoomManager.setZoomLevels(zoomLevels);
        
        // Scroll-wheel Zoom
        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
        
        String popupId = getPopupId();
        if (popupId != null) {
            this.menuService.registerContextMenu(viewer.getControl(), popupId);
        }
    }

    /**
     * Creates the GraphicalViewer on the specified <code>Composite</code>.
     * @param parent the parent composite
     */
    @objid ("65741b2c-33f7-11e2-95fe-001ec947c8cc")
    protected void createGraphicalViewer(Composite parent) {
        GraphicalViewer viewer = new ScrollingGraphicalViewer();
        viewer.createControl(parent);
        setGraphicalViewer(viewer);
        configureGraphicalViewer();
        hookGraphicalViewer();
        initializeGraphicalViewer();
    }

    /**
     * Creates a PaletteViewerProvider that will be used to create palettes for
     * the view and the flyout.
     * @return the palette provider
     */
    @objid ("65741b30-33f7-11e2-95fe-001ec947c8cc")
    protected PaletteViewerProvider createPaletteViewerProvider() {
        return new PaletteViewerProvider(getEditDomain());
    }

    @objid ("65741b35-33f7-11e2-95fe-001ec947c8cc")
    public void createPartControl(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout(1, true);
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        panel.setLayout(gl);
        
        GridData splitterLayoutData = new GridData();
        splitterLayoutData.grabExcessHorizontalSpace = true;
        splitterLayoutData.grabExcessVerticalSpace = true;
        splitterLayoutData.horizontalAlignment = SWT.FILL;
        splitterLayoutData.verticalAlignment = SWT.FILL;
        
        this.splitter = new FlyoutPaletteComposite2(panel, SWT.NONE, getPaletteViewerProvider(), getPalettePreferences());
        
        this.splitter.setLayoutData(splitterLayoutData);
        
        createGraphicalViewer(this.splitter);
        this.splitter.setGraphicalControl(getGraphicalControl());
    }

    @objid ("65741b38-33f7-11e2-95fe-001ec947c8cc")
    @PreDestroy
    public void dispose(DiagramEditorsManager manager) {
        // Unregister closed diagram from the diagram manager
        final DiagramEditorInput editorInput = getEditorInput();
        
        // Unregister as model change listener
        if (getModelingSession() != null && getModelingSession().getModelChangeSupport() != null) {
            getModelingSession().getModelChangeSupport().removeStatusChangeListener(this);
            getModelingSession().getModelChangeSupport().removeModelChangeListener(this);
        }
        
        // Dispose the editor input
        if (editorInput != null) {
            manager.remove(editorInput.getDiagram());
            editorInput.dispose();
        }
        
        // empty the palette
        final PaletteViewer paletteViewer = getEditDomain().getPaletteViewer();
        if (paletteViewer != null) {
            paletteViewer.setContents(null);
        }
        
        this.paletteRoot = null;
        getCommandStack().removeCommandStackListener(this);
        getEditDomain().setActiveTool(null);
        getActionRegistry().dispose();
        if (this.modifIndicator != null) {
            this.modifIndicator.setDiagram(null);
            this.modifIndicator.setControl(null);
        }
    }

    /**
     * Notifies the editor that the handle opened on it has been closed.
     * Depending on its nature, the editor might decide to delete itself,
     * release some resources, or ignore this event altogether.
     */
    @objid ("65741b3b-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void disposeHandle() {
        // Nothing to do: this editor doesn't care about handles on it.
    }

    @objid ("65767d56-33f7-11e2-95fe-001ec947c8cc")
    protected void firePropertyChange() {
        updateActions(this.propertyActions);
    }

    /**
     * Lazily creates and returns the action registry.
     * @return the action registry
     */
    @objid ("65767d58-33f7-11e2-95fe-001ec947c8cc")
    protected ActionRegistry getActionRegistry() {
        if (this.actionRegistry == null)
            this.actionRegistry = new ActionRegistry();
        return this.actionRegistry;
    }

    /**
     * Returns the adapter for the specified key.
     * 
     * <P>
     * <EM>IMPORTANT</EM> certain requests, such as the property sheet, may be
     * made before or after {@link #createPartControl(Composite)} is called. The
     * order is unspecified by the Workbench.
     * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
     */
    @objid ("65767d5d-33f7-11e2-95fe-001ec947c8cc")
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart2) this.getGraphicalViewer().getRootEditPart()).getZoomManager();
        }
        
        if (type == IContentOutlinePage.class) {
            return new OutlinePage();
        }
        if (type == org.eclipse.ui.views.properties.IPropertySheetPage.class) {
            return new UndoablePropertySheetPage(getCommandStack(), getActionRegistry().getAction(ActionFactory.UNDO.getId()),
                    getActionRegistry().getAction(ActionFactory.REDO.getId()));
        }
        if (type == GraphicalViewer.class)
            return getGraphicalViewer();
        if (type == CommandStack.class)
            return getCommandStack();
        if (type == ActionRegistry.class)
            return getActionRegistry();
        if (type == EditPart.class && getGraphicalViewer() != null)
            return getGraphicalViewer().getRootEditPart();
        if (type == IFigure.class && getGraphicalViewer() != null)
            return ((GraphicalEditPart) getGraphicalViewer().getRootEditPart()).getFigure();
        return null;
    }

    /**
     * Returns the command stack.
     * @return the command stack
     */
    @objid ("65767d64-33f7-11e2-95fe-001ec947c8cc")
    protected CommandStack getCommandStack() {
        return getEditDomain().getCommandStack();
    }

    /**
     * Returns the edit domain.
     * @return the edit domain
     */
    @objid ("65767d6c-33f7-11e2-95fe-001ec947c8cc")
    protected EditDomain getEditDomain() {
        return this.editDomain;
    }

    @objid ("65767d71-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public DiagramEditorInput getEditorInput() {
        return this.input;
    }

    /**
     * @return the graphical viewer's control
     */
    @objid ("65767d76-33f7-11e2-95fe-001ec947c8cc")
    protected Control getGraphicalControl() {
        return getGraphicalViewer().getControl();
    }

    /**
     * Returns the graphical viewer.
     * @return the graphical viewer
     */
    @objid ("65767d7b-33f7-11e2-95fe-001ec947c8cc")
    public GraphicalViewer getGraphicalViewer() {
        return this.graphicalViewer;
    }

    /**
     * Get the modeling session.
     * @return The modeling session.
     */
    @objid ("65767d80-33f7-11e2-95fe-001ec947c8cc")
    protected final ICoreSession getModelingSession() {
        return this.projectService.getSession();
    }

    @objid ("65767d85-33f7-11e2-95fe-001ec947c8cc")
    protected FlyoutPreferences getPalettePreferences() {
        return new PalettePreferences();
    }

    @objid ("65767d89-33f7-11e2-95fe-001ec947c8cc")
    public PaletteRoot getPaletteRoot() {
        return this.paletteRoot;
    }

    /**
     * Returns the palette viewer provider that is used to create palettes for
     * the view and the flyout. Creates one if it doesn't already exist.
     * @see #createPaletteViewerProvider()
     * @return the PaletteViewerProvider that can be used to create
     * PaletteViewers for this editor
     */
    @objid ("65767d8d-33f7-11e2-95fe-001ec947c8cc")
    protected final PaletteViewerProvider getPaletteViewerProvider() {
        if (this.provider == null)
            this.provider = createPaletteViewerProvider();
        return this.provider;
    }

    @objid ("65767d92-33f7-11e2-95fe-001ec947c8cc")
    public MPart getPart() {
        return this.part;
    }

    /**
     * Returns the list of {@link IAction IActions} dependant on property
     * changes in the Editor. These actions should implement the
     * {@link UpdateAction} interface so that they can be updated in response to
     * property changes. An example is the "Save" action.
     * @return the list of property-dependant actions
     */
    @objid ("65767d96-33f7-11e2-95fe-001ec947c8cc")
    protected List<Object> getPropertyActions() {
        return this.propertyActions;
    }

    /**
     * Return the root edit part of this editor.
     * @return the root edit part of this editor.
     */
    @objid ("65767d9d-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public RootEditPart getRootEditPart() {
        // Automatically generated method. Please delete this comment before
        // entering specific code.
        return this.rootEditPart;
    }

    /**
     * Returns the list of <em>IDs</em> of Actions that are dependant on changes
     * in the workbench's {@link ISelectionService}. The associated Actions can
     * be found in the action registry. Such actions should implement the
     * {@link UpdateAction} interface so that they can be updated in response to
     * selection changes.
     * @see #updateActions(List)
     * @return the list of selection-dependant action IDs
     */
    @objid ("65767da3-33f7-11e2-95fe-001ec947c8cc")
    protected List<Object> getSelectionActions() {
        return this.selectionActions;
    }

    /**
     * Returns the selection synchronizer object. The synchronizer can be used
     * to sync the selection of 2 or more EditPartViewers.
     * @return the synchronizer
     */
    @objid ("6578dfb1-33f7-11e2-95fe-001ec947c8cc")
    protected SelectionSynchronizer getSelectionSynchronizer() {
        if (this.synchronizer == null)
            this.synchronizer = new SelectionSynchronizer();
        return this.synchronizer;
    }

    /**
     * Returns the list of <em>IDs</em> of Actions that are dependant on the
     * CommmandStack's state. The associated Actions can be found in the action
     * registry. These actions should implement the {@link UpdateAction}
     * interface so that they can be updated in response to command stack
     * changes. An example is the "undo" action.
     * @return the list of stack-dependant action IDs
     */
    @objid ("6578dfb6-33f7-11e2-95fe-001ec947c8cc")
    protected List<Object> getStackActions() {
        return this.stackActions;
    }

    /**
     * Hooks the GraphicalViewer to the rest of the Editor. By default, the
     * viewer is added to the SelectionSynchronizer, which can be used to keep 2
     * or more EditPartViewers in sync. The viewer is also registered as the
     * ISelectionProvider for the Editor's PartSite.
     */
    @objid ("6578dfbd-33f7-11e2-95fe-001ec947c8cc")
    protected void hookGraphicalViewer() {
        getSelectionSynchronizer().addViewer(getGraphicalViewer());
        
        getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                AbstractDiagramEditor.this.selectionService.setSelection(event.getSelection());
            }
        });
    }

    /**
     * Sets the site and input for this editor then creates and initializes the
     * actions. Subclasses may extend this method, but should always call
     * <code>super.init(site, input)
     * </code>.
     */
    @objid ("6578dfc0-33f7-11e2-95fe-001ec947c8cc")
    public void init(DiagramEditorInput newInput) {
        this.input = newInput;
        getCommandStack().addCommandStackListener(this);
        initializeActionRegistry();
        
        // Register as a model change listener to:
        // - update tab's name if the diagram is renamed
        // - close editor if the diagram (or an ancestor) is deleted
        if (this.projectService != null) {
            getModelingSession().getModelChangeSupport().addModelChangeListener(this);
            getModelingSession().getModelChangeSupport().addStatusChangeListener(this);
        }
    }

    /**
     * Initializes the ActionRegistry. This registry may be used by
     * {@link ActionBarContributor ActionBarContributors} and/or
     * {@link ContextMenuProvider ContextMenuProviders}.
     * <P>
     * This method may be called on Editor creation, or lazily the first time
     * {@link #getActionRegistry()} is called.
     */
    @objid ("6578dfc4-33f7-11e2-95fe-001ec947c8cc")
    protected void initializeActionRegistry() {
        updateActions(this.propertyActions);
        updateActions(this.stackActions);
    }

    @objid ("6578dfc7-33f7-11e2-95fe-001ec947c8cc")
    protected void initializeGraphicalViewer() {
        this.splitter.hookDropTargetListener(getGraphicalViewer());
        
        final GraphicalViewer viewer = this.getGraphicalViewer();
        
        // Initialize connection routers
        final ConnectionRouterRegistry routersRegistry = new ConnectionRouterRegistry();
        viewer.setProperty(ConnectionRouterRegistry.ID, routersRegistry);
        AbstractDiagramEditor.initializeConnectionRouters(routersRegistry);
        
        // Set the viewer content
        if (getEditorInput() != null) {
            GmAbstractDiagram gmDiagram = getEditorInput().getGmDiagram();
            viewer.setContents(gmDiagram);
            // Force a complete refresh now that edit parts are finally listening to
            // events that might
            // be sent by the model (e.g.: links that have changed source and/or
            // target while diagram
            // was closed).
            gmDiagram.refreshAllFromObModel();
        }
    }

    @objid ("6578dfc9-33f7-11e2-95fe-001ec947c8cc")
    public boolean isDirty() {
        return false;
    }

    /**
     * Returns <code>false</code> by default. Subclasses must return
     * <code>true</code> to allow {@link #doSaveAs()} to be called.
     * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
     */
    @objid ("6578dfcd-33f7-11e2-95fe-001ec947c8cc")
    public boolean isSaveAsAllowed() {
        return false;
    }

    /**
     * Invoked when the model has changed.
     * <p>
     * <li>Refresh the diagram title
     * <li>Reload the diagram if the saved string has changed (to be done)
     * <li>Set the view read only if the model becomes read only (to be done)
     * <li>Close the diagram if deleted from the model.
     * <p>
     */
    @objid ("6578dfd2-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void modelChanged(final IModelChangeEvent event) {
        // Re enter the UI thread
        Display display = Display.getDefault();
        if (display != null) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    final AbstractDiagram diagram = getEditorInput().getDiagram();
                    if (event.getDeleteEvents().size() != 0) {
                        // Some elements were deleted: check for validity of the
                        // diagram.
                        if (diagram != null && !diagram.isValid()) {
                            // The diagram in no longer valid, close the editor
                            // use the PartService to close this editor
                            AbstractDiagramEditor.this.partService.hidePart(getPart(), true);
                            return;
                        }
                    }
                    // At this point, we know that diagram is still valid,
                    // update the editor's title.
                    if (diagram != null) {
                        getPart().setLabel(diagram.getName());
                    }
                }
            });
        }
    }

    @objid ("6578dfeb-33f7-11e2-95fe-001ec947c8cc")
    @PostConstruct
    public void postConstruct(Composite composite, DiagramEditorInput diagramEditorInput, MPart mPart, IDiagramConfigurerRegistry configurerRegistry, ToolRegistry toolRegistry) {
        this.part = mPart;
        
        if (diagramEditorInput != null) {
            // init
            init(diagramEditorInput);
            // createPartControl
            createPartControl(composite);
        
            // Palette
            AbstractDiagram editedDiagram = diagramEditorInput.getDiagram();
            List<String> stereotypes = new ArrayList<>();
            for (Stereotype stereotype : editedDiagram.getExtension()) {
                stereotypes.add(stereotype.getName());
            }
            List<IDiagramConfigurer> configurers = configurerRegistry.getConfigurers(editedDiagram.getMClass().getName(), stereotypes);
            for (IDiagramConfigurer stereotypeConfigurer : configurers) {
                setPaletteRoot(stereotypeConfigurer.initPalette(this, toolRegistry));
            }
        } else {
            MessageDialog.openError(composite.getShell(), DiagramEditor.I18N.getMessage("InvalidDiagram.title"), DiagramEditor.I18N.getMessage("InvalidDiagram.message"));
        }
    }

    @objid ("6578dfd8-33f7-11e2-95fe-001ec947c8cc")
    @Inject
    @Optional
    public void onPickingStart(@EventTopic(ModelioEventTopics.PICKING_START) final IPickingSession session) {
        // FIXME this should be an @UIEventTopic, but they are not triggered
        // with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                getEditDomain().setActiveTool(new PickingSelectionTool(session));
            }
        });
    }

    @objid ("76c9ecb8-dc7f-4493-8fe4-a68f66bc8c40")
    @Inject
    @Optional
    @SuppressWarnings("unused")
    public void onPickingStop(@EventTopic(ModelioEventTopics.PICKING_STOP) final IPickingSession session) {
        // FIXME this should be an @UIEventTopic, but they are not triggered
        // with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                getEditDomain().setActiveTool(new PanSelectionTool());
            }
        });
    }

    /**
     * Sets the ActionRegistry for this EditorPart.
     * @param registry the registry
     */
    @objid ("6578dff3-33f7-11e2-95fe-001ec947c8cc")
    protected void setActionRegistry(ActionRegistry registry) {
        this.actionRegistry = registry;
    }

    @objid ("657b4209-33f7-11e2-95fe-001ec947c8cc")
    protected void setEditDomain(EditDomain ed) {
        this.editDomain = ed;
        getEditDomain().setPaletteRoot(getPaletteRoot());
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPart#setFocus()
     */
    @objid ("657b420c-33f7-11e2-95fe-001ec947c8cc")
    @Focus
    public void setFocus() {
        final GraphicalViewer viewer = getGraphicalViewer();
        if (viewer != null) {
            viewer.getControl().setFocus();
        
            // Initialize the module contextual menu creator with the part having
            // the focus...
            ModuleMenuCreator.setPart(this.part);
        }
    }

    /**
     * Sets the graphicalViewer for this EditorPart.
     * @param viewer the graphical viewer
     */
    @objid ("657b4210-33f7-11e2-95fe-001ec947c8cc")
    protected void setGraphicalViewer(GraphicalViewer viewer) {
        getEditDomain().addViewer(viewer);
        this.graphicalViewer = viewer;
    }

    @objid ("657b4214-33f7-11e2-95fe-001ec947c8cc")
    protected void setInput(final DiagramEditorInput input) {
        Assert.isLegal(input != null);
        this.input = input;
        AbstractDiagram adapter = (AbstractDiagram) input.getAdapter(AbstractDiagram.class);
        if (adapter != null) {
            this.modifIndicator.setDiagram(adapter);
        } else {
            this.modifIndicator.setDiagram(null);
        }
    }

    /**
     * Set the palette root of this editor.
     * @param value the palette root for this diagram.
     */
    @objid ("657b4218-33f7-11e2-95fe-001ec947c8cc")
    public void setPaletteRoot(final PaletteRoot value) {
        this.paletteRoot = value;
        this.getEditDomain().setPaletteRoot(this.getPaletteRoot());
    }

    @objid ("657b421d-33f7-11e2-95fe-001ec947c8cc")
    @Override
    public void statusChanged(IStatusChangeEvent event) {
        final AbstractDiagram diagram = getEditorInput().getDiagram();
        // Some elements were deleted: check for validity of the diagram.
        if (diagram != null && !diagram.isValid()) {
            // The diagram in no longer valid, close the editor
            // use the PartService to close this editor
            // Re enter the UI thread
            Display display = this.graphicalViewer.getControl().getDisplay();
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    AbstractDiagramEditor.this.partService.hidePart(getPart(), true);
                }
            });
        }
        this.modifIndicator.statusChanged();
    }

    /**
     * A convenience method for updating a set of actions defined by the given
     * List of action IDs. The actions are found by looking up the ID in the
     * {@link #getActionRegistry() action registry}. If the corresponding action
     * is an {@link UpdateAction}, it will have its <code>update()</code> method
     * called.
     * @param actionIds the list of IDs to update
     */
    @objid ("657b4221-33f7-11e2-95fe-001ec947c8cc")
    protected void updateActions(List<?> actionIds) {
        ActionRegistry registry = getActionRegistry();
        for (Object id : actionIds) {
            IAction action = registry.getAction(id);
            if (action instanceof UpdateAction) {
                ((UpdateAction) action).update();
            }
        }
    }

    @objid ("6b1dd411-59bd-4cde-871a-ae072268c61c")
    protected abstract String getPopupId();


{
        Bundle imageBundle = Platform.getBundle(DiagramEditor.PLUGIN_ID);
        IPath bitmapPath = new Path("icons/modifiable.png");
        URL bitmapUrl = FileLocator.find(imageBundle, bitmapPath, null);
        ImageDescriptor desc = ImageDescriptor.createFromURL(bitmapUrl);
        modifiableImage = desc.createImage();

        bitmapPath = new Path("icons/notModifiable.png");
        bitmapUrl = FileLocator.find(imageBundle, bitmapPath, null);
        desc = ImageDescriptor.createFromURL(bitmapUrl);
        notModifiableImage = desc.createImage();
    }
    @objid ("656f5649-33f7-11e2-95fe-001ec947c8cc")
    private static class ActionIDList extends ArrayList<Object> {
        @objid ("656f564b-33f7-11e2-95fe-001ec947c8cc")
        private static final long serialVersionUID = 2638440179718621761L;

        @objid ("656f564d-33f7-11e2-95fe-001ec947c8cc")
        public ActionIDList() {
            super();
        }

        @objid ("656f564f-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public boolean add(Object o) {
            if (o instanceof IAction) {
                try {
                    IAction action = (IAction) o;
                    throw new IllegalArgumentException("Action IDs should be added to lists, not the action: " + action); //$NON-NLS-1$
                } catch (IllegalArgumentException exc) {
                    exc.printStackTrace();
                }
            }
            return super.add(o);
        }

    }

    @objid ("656f5655-33f7-11e2-95fe-001ec947c8cc")
    private static final class IsModifiableIndicator {
        @objid ("656f5657-33f7-11e2-95fe-001ec947c8cc")
         AbstractDiagram diagram;

        @objid ("9abc1e97-b29f-4baa-83cd-b64e1f7d3cd4")
         Label label;

        @objid ("656f5659-33f7-11e2-95fe-001ec947c8cc")
        public IsModifiableIndicator() {
            // Nothing specific to do here.
        }

        @objid ("6571b8a0-33f7-11e2-95fe-001ec947c8cc")
        public void setControl(Label label) {
            this.label = label;
            statusChanged();
        }

        @objid ("6571b8a3-33f7-11e2-95fe-001ec947c8cc")
        public void setDiagram(AbstractDiagram diagram) {
            this.diagram = diagram;
            statusChanged();
        }

        @objid ("6571b8a6-33f7-11e2-95fe-001ec947c8cc")
        protected void statusChanged() {
            if (this.diagram != null && this.label != null) {
                final boolean isModifiable = IsModifiableIndicator.this.diagram.isModifiable();
                
                this.label.getDisplay().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        IsModifiableIndicator.this.label.setImage(isModifiable ? modifiableImage : notModifiableImage);
                    }
                });
            }
        }

    }

    /**
     * Implementation of the Outline view.
     * 
     * @author phv
     */
    @objid ("6571b8a8-33f7-11e2-95fe-001ec947c8cc")
    protected class OutlinePage extends ContentOutlinePage {
        @objid ("bd2e38e2-45c1-4b7f-a2d4-c180bcbed5a9")
        private DisposeListener disposeListener;

        /**
         * The actual thumbnail of the outlined diagram.
         */
        @objid ("0fa23b93-9f99-4bce-b259-b1977216e838")
         ScrollableThumbnail thumbnail;

        @objid ("8cd8aaa5-c493-40e7-829c-e71463d11d94")
        private Composite panel;

        /**
         * Constructor.
         */
        @objid ("6571b8ae-33f7-11e2-95fe-001ec947c8cc")
        public OutlinePage() {
            super(new ScrollingGraphicalViewer());
        }

        @objid ("6571b8b1-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public void createControl(Composite parent) {
            this.panel = new Composite(parent, SWT.NONE);
            GridLayout gl = new GridLayout(1, true);
            gl.horizontalSpacing = 0;
            gl.verticalSpacing = 0;
            gl.marginHeight = 0;
            gl.marginWidth = 0;
            this.panel.setLayout(gl);
            
            AbstractDiagramEditor.this.getSelectionSynchronizer().addViewer(this.getViewer());
            
            // Create the thumbnail view
            Canvas canvas = new Canvas(this.panel, SWT.BORDER);
            GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
            canvas.setLayoutData(gd);
            LightweightSystem lws = new LightweightSystem(canvas);
            final GraphicalViewer viewer = AbstractDiagramEditor.this.getGraphicalViewer();
            if (viewer != null) {
                this.thumbnail = new ScrollableThumbnail((Viewport) ((ScalableFreeformRootEditPart) viewer.getRootEditPart()).getFigure());
            
                this.thumbnail.setSource(((ScalableFreeformRootEditPart) viewer.getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS));
                lws.setContents(this.thumbnail);
            
                // add a dispose listener for cleaning
                this.disposeListener = new DisposeListener() {
                    @Override
                    public void widgetDisposed(DisposeEvent e) {
                        if (OutlinePage.this.thumbnail != null) {
                            OutlinePage.this.thumbnail.deactivate();
                            OutlinePage.this.thumbnail = null;
                        }
                        OutlinePage.this.dispose(); // dispose the outline page to
                        // avoid a graphical refresh
                        // problem
                    }
                };
                viewer.getControl().addDisposeListener(this.disposeListener);
            }
        }

        @objid ("6571b8b5-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public void dispose() {
            AbstractDiagramEditor.this.getSelectionSynchronizer().removeViewer(this.getViewer());
            if (AbstractDiagramEditor.this.getGraphicalViewer() != null && AbstractDiagramEditor.this.getGraphicalViewer().getControl() != null
                    && !AbstractDiagramEditor.this.getGraphicalViewer().getControl().isDisposed()) {
                AbstractDiagramEditor.this.getGraphicalViewer().getControl().removeDisposeListener(this.disposeListener);
            }
            super.dispose();
        }

        @objid ("6571b8b8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public Control getControl() {
            return this.panel;
        }

    }

    /**
     * Palette preferences manager
     * 
     * @author phv
     */
    @objid ("6571b8bd-33f7-11e2-95fe-001ec947c8cc")
    protected class PalettePreferences implements FlyoutPreferences {
        @objid ("2717440b-3897-11e2-95fe-001ec947c8cc")
        private static final String PALETTE_LOCATION = "com.modeliosoft.modelio.diagram.editor.palettedock"; // $NON-NLS-1$

        @objid ("271e6b18-3897-11e2-95fe-001ec947c8cc")
        private static final String PALETTE_SIZE = "com.modeliosoft.modelio.diagram.editor.palettesize"; // $NON-NLS-1$

        @objid ("2720cd73-3897-11e2-95fe-001ec947c8cc")
        private static final String PALETTE_STATE = "com.modeliosoft.modelio.diagram.editor.palettestate"; // $NON-NLS-1$

        @objid ("6571b8c8-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public int getDockLocation() {
            return InstanceScope.INSTANCE.getNode(DiagramEditor.PLUGIN_ID).getInt(PALETTE_LOCATION, PositionConstants.WEST);
        }

        @objid ("6571b8cd-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public int getPaletteState() {
            return InstanceScope.INSTANCE.getNode(DiagramEditor.PLUGIN_ID).getInt(PALETTE_STATE,
                    FlyoutPaletteComposite.STATE_PINNED_OPEN);
        }

        @objid ("6571b8d2-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public int getPaletteWidth() {
            return InstanceScope.INSTANCE.getNode(DiagramEditor.PLUGIN_ID).getInt(PALETTE_SIZE,
                    FlyoutPaletteComposite2.DEFAULT_PALETTE_SIZE);
        }

        @objid ("6571b8d7-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public void setDockLocation(int location) {
            if (location == PositionConstants.WEST || location == PositionConstants.EAST) {
                InstanceScope.INSTANCE.getNode(DiagramEditor.PLUGIN_ID).putInt(PALETTE_LOCATION, location);
            }
        }

        @objid ("6571b8db-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public void setPaletteState(int state) {
            InstanceScope.INSTANCE.getNode(DiagramEditor.PLUGIN_ID).putInt(PALETTE_STATE, state);
        }

        @objid ("6571b8df-33f7-11e2-95fe-001ec947c8cc")
        @Override
        public void setPaletteWidth(int width) {
            InstanceScope.INSTANCE.getNode(DiagramEditor.PLUGIN_ID).putInt(PALETTE_SIZE, width);
        }

    }

}
