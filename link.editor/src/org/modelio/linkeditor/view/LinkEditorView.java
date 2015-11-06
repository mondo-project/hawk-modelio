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
                                    

package org.modelio.linkeditor.view;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GraphicsSource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.navigate.IModelioNavigationService;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.linkeditor.leftdepth.LeftDepthSpinner;
import org.modelio.linkeditor.options.LinkEditorOptions;
import org.modelio.linkeditor.plugin.LinkEditor;
import org.modelio.linkeditor.rightdepth.RightDepthSpinner;
import org.modelio.linkeditor.view.background.BackgroundEditPart;
import org.modelio.linkeditor.view.background.BackgroundModel;
import org.modelio.linkeditor.view.background.LinkEditorDropTargetListener;
import org.modelio.linkeditor.view.node.GraphNode;
import org.modelio.linkeditor.view.node.NodeEditPart;
import org.modelio.linkeditor.view.tools.PanSelectionTool;
import org.modelio.linkeditor.view.tools.PickingSelectionTool;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Main view class for the LinkViewer.
 * 
 * @author fpoyer
 */
@objid ("1ba44c6c-5e33-11e2-b81d-002564c97630")
public class LinkEditorView implements CommandStackListener {
    /**
     * ID of this view, as defined in the plugin.xml file.
     */
    @objid ("1ba44c72-5e33-11e2-b81d-002564c97630")
    public static final String VIEW_ID = "org.modelio.linkeditor.view.LinkEditorViewID";

    @objid ("1ba44c78-5e33-11e2-b81d-002564c97630")
    private GraphicalViewer graphicalViewer;

    @objid ("1ba44c81-5e33-11e2-b81d-002564c97630")
    private final RootEditPart rootEditPart = new ScalableFreeformRootEditPart();

    @objid ("d4aed2a5-5efd-11e2-a8be-00137282c51b")
    @Inject
    private IProjectService projectService;

    @objid ("9af87109-4ba1-43e2-93fa-d36ca8d50865")
    private FocusListener focusListener;

    @objid ("6edc9745-6ac1-450e-bce0-708c3ba0d188")
    @Inject
    @Optional
     ESelectionService selectionService;

    @objid ("8574796d-78f7-4205-91a3-c08d0602bd89")
    private static final String POPUPID = "org.modelio.linkeditor.popupmenu";

    @objid ("1ba44c85-5e33-11e2-b81d-002564c97630")
    private MObject currentSelection;

    @objid ("1ba44c88-5e33-11e2-b81d-002564c97630")
    private ModelChangeListener modelChangeListener;

    /**
     * FIXME CHM this shouldn't be static...
     * @see org.modelio.linkeditor.view.background.CreateLinkCommand#execute()
     */
    @objid ("d4aed2a9-5efd-11e2-a8be-00137282c51b")
    @Inject
    @Optional
    public static IMModelServices modelServices;

    @objid ("81054bf8-e7db-4ad5-b9f8-49cbe55d38d4")
    private EditDomain editDomain;

    @objid ("e3571523-4096-4585-9997-d2b08d571337")
    private SelectionSynchronizer synchronizer;

// FIXME CHM this shouldn't be static...
    @objid ("8fe48f6f-e2f9-45a2-a58f-354a734b3d12")
    private static LinkEditorOptions options = new LinkEditorOptions(null,null);

    @objid ("43f311f1-376c-4f92-b7d4-6b1f98ced3a9")
    @Inject
    private EMenuService menuService;

    @objid ("9b195c92-ac77-40b0-8fcb-3bd008c58df3")
    @Inject
    private Shell activeShell;

    @objid ("1eada249-9f7a-45db-b3f4-d7b716f25be1")
    @Inject
    private EModelService eModelService;

    @objid ("2ef4ad13-1583-46df-bec9-5a4246524b75")
    @Inject
    @Optional
    private MApplication application;

    @objid ("12712677-9bf0-4b6a-ba0d-807546c4668e")
    private static MToolBar toolbar;

    @objid ("a8992ae6-4faf-415f-9f4c-6915389d9ae8")
    private MMenu menu;

    @objid ("6ef267ce-960b-4a58-ad8a-71e047c73176")
    @Inject
     IModelioNavigationService navigationService;

    /**
     * This method changes the contents of the view. It is usually called when the selected element changes in the application.
     * However, calling it directly passing a ModelElement is perfectly valid. Passing a <code>null</code> element 'disables' the
     * view that becomes inactive.
     * <p>
     * @param element the new input element for the view.
     */
    @objid ("1ba6add5-5e33-11e2-b81d-002564c97630")
    public void setInput(final MObject element) {
        final GraphicalViewer viewer = this.getGraphicalViewer();
        Display display = viewer.getControl().getDisplay();
        final BackgroundModel backgroundModel = (BackgroundModel) viewer.getContents().getModel();
        backgroundModel.nodes.clear();
        backgroundModel.edges.clear();
        // Force the view to empty itself (so that it gets correctly centred
        // later on).
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                // notify content change to edit part.
                backgroundModel.contentChanged();
            }
        });
        if (element != null && element.isValid()) {
            // Update the width and height of nodes based on current size of
            // system font.
            Font systemFont = display.getSystemFont();
            GraphNode.WIDTH = GraphNode.MARGIN_WIDTH + GraphNode.ICON_SIZE + GraphNode.VERTICAL_SPACING
                    + FigureUtilities.getStringExtents("abcdefghijklmn...", systemFont).width() + GraphNode.MARGIN_WIDTH;
            GraphNode.HEIGHT = GraphNode.MARGIN_HEIGHT + Math.max(GraphNode.ICON_SIZE, systemFont.getFontData()[0].getHeight())
                    + GraphNode.MARGIN_HEIGHT + 2;
            // build new tree from element and options.
            // Create node for central element
            // TODO: try to update rather than start over if central element
            // didn't change
            GraphNode centralNode = new GraphNode(element);
            centralNode.setCentral(true);
            backgroundModel.addNode(centralNode);
            backgroundModel.setCenter(centralNode);
            // Build the "left" part of the tree
            if (options.getLeftDepth() > 0) {
                TreeBuilder.buildLeftTree(backgroundModel, centralNode, options.getLeftDepth(), options);
            }
            // Build the "right" part of the tree
            if (options.getRightDepth() > 0) {
                TreeBuilder.buildRightTree(backgroundModel, centralNode, options.getRightDepth(), options);
            }
        }
        // Notifies the view to display new content (and this we made sure it
        // has been emptied before, new content will be centred correctly).
        display.syncExec(new Runnable() {
            @Override
            public void run() {
                // notify content change to edit part.
                backgroundModel.contentChanged();
            }
        });
    }

    @objid ("1ba6adda-5e33-11e2-b81d-002564c97630")
    private void createControls(final Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        GridLayout gl = new GridLayout(1, true);
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        panel.setLayout(gl);
        
        // Add the GEF viewer on top
        this.createGraphicalViewer(panel);
        GridData viewerLayoutData = new GridData();
        viewerLayoutData.grabExcessHorizontalSpace = true;
        viewerLayoutData.grabExcessVerticalSpace = true;
        viewerLayoutData.horizontalAlignment = SWT.FILL;
        viewerLayoutData.verticalAlignment = SWT.FILL;
        this.getGraphicalViewer().getControl().setLayoutData(viewerLayoutData);
    }

    @objid ("1ba6addf-5e33-11e2-b81d-002564c97630")
    @Focus
    void setFocus() {
        if (this.getGraphicalViewer() != null && this.getGraphicalViewer().getControl() != null) {
            this.getGraphicalViewer().getControl().setFocus();
        }
    }

    @objid ("1ba6ade2-5e33-11e2-b81d-002564c97630")
    @Override
    public void commandStackChanged(EventObject event) {
        // Nothing to do
    }

    /**
     * Called to configure the graphical viewer before it receives its contents. This is where the root editpart should be
     * configured. Subclasses should extend or override this method as needed.
     */
    @objid ("1ba6ade8-5e33-11e2-b81d-002564c97630")
    protected void configureGraphicalViewer() {
        final GraphicalViewer viewer = this.getGraphicalViewer();
        viewer.getControl().setBackground(ColorConstants.listBackground);
        
        // Set the root edit part
        viewer.setRootEditPart(this.rootEditPart);
        viewer.setEditPartFactory(new LinkEditorEditPartFactory(LinkEditorView.modelServices));
        
        // Configure the edit domain
        // Set the active and default tool
        final SelectionTool selectionTool = new PanSelectionTool(this.navigationService);
        this.getEditDomain().setActiveTool(selectionTool);
        this.getEditDomain().setDefaultTool(selectionTool);
        
        viewer.setEditDomain(this.getEditDomain());
        
        // Plug our own command stack that is bound to the Modelio transaction
        // manager
        ICoreSession modelingSession = this.projectService.getSession();
        if (modelingSession != null) {
            this.getEditDomain().setCommandStack(new LinkEditorCommandStack(modelingSession));
        }
        
        // Configure zoom levels
        int nZoomLevels = 32;
        double minZoom = 0.25;
        double maxZoom = 4.0;
        double zoomLevels[] = new double[nZoomLevels];
        double zoomIncrement = (maxZoom - minZoom) / nZoomLevels;
        for (int i = 0; i < nZoomLevels; i++) {
            zoomLevels[i] = minZoom + zoomIncrement * i;
        }
        
        ZoomManager zoomManager = ((ScalableFreeformRootEditPart) viewer.getRootEditPart()).getZoomManager();
        zoomManager.setZoomLevels(zoomLevels);
        
        // Scroll-wheel Zoom
        viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
        
        // Add the contextual menu
        this.menuService.registerContextMenu(viewer.getControl(), POPUPID);
        buildMenuElementsMap();
    }

    /**
     * Creates the GraphicalViewer on the specified <code>Composite</code>.
     * @param parent the parent composite
     */
    @objid ("1ba6adee-5e33-11e2-b81d-002564c97630")
    protected void createGraphicalViewer(final Composite parent) {
        // XXX Hack: we need to specialize the GraphicsSource to avoid calling
        // the
        // Canvas#update() method in the GraphicsSource#getGraphics(Rectangle)
        // method.
        //
        // This hack is needed to avoid SWT/GEF bug 137786
        // (see https://bugs.eclipse.org/bugs/show_bug.cgi?id=137786 ) where
        // drag over
        // feedback (ie the drag feedback provided by the drag source) and drag
        // under
        // feedback (ie the feedback provided by the drop target) interfere with
        // each
        // other, causing flickering and more importantly ugly graphical
        // artifacts.
        //
        // In order to be able to specialize the GraphicsSource, we need to
        // specialize
        // the LightWeightSystem, and to do that we need in turn to specialize
        // the GraphicalViewer.
        
        final GraphicalViewer viewer = new ScrollingGraphicalViewer() {
            @Override
            protected LightweightSystem createLightweightSystem() {
                return new LightweightSystem() {
                    @Override
                    public void setControl(final Canvas c) {
                        super.setControl(c);
                        this.getUpdateManager().setGraphicsSource(new GraphicsSource() {
                            @Override
                            public Graphics getGraphics(Rectangle r) {
                                c.redraw(r.x, r.y, r.width, r.height, false);
                                // The actual hack is the following code
                                // line: in original GEF code a call is
                                // made to the #update() method of the c
                                // Canvas.
                                // But calling #update() at this point
                                // causes SWT to redraw the drag over
                                // feedback which in turn causes GEF to
                                // redraw the drag under feedback etc.
                                // The final result is flickering
                                // (because of constant erase and
                                // redraw) and
                                // graphical artifacts.
                                // Commenting this line however seems to
                                // have no side effect (so far).
                                // c.update();
                                return null;
                            }
        
                            @Override
                            public void flushGraphics(Rectangle region) {
                                // Nothing to do.
                            }
                        });
                    }
                };
            }
        };
        // XXX end of Hack
        viewer.createControl(parent);
        this.setGraphicalViewer(viewer);
        this.configureGraphicalViewer();
        this.hookGraphicalViewer();
        this.initializeGraphicalViewer();
        viewer.addDropTargetListener(new LinkEditorDropTargetListener(viewer, this.projectService));
        this.focusListener = new FocusListener() {
        
            @Override
            public void focusLost(FocusEvent e) {
                NodeEditPart.hasFocus = false;
                LinkEditorView.this.forceRefreshCenterNode();
            }
        
            @Override
            public void focusGained(FocusEvent e) {
                NodeEditPart.hasFocus = true;
                LinkEditorView.this.forceRefreshCenterNode();
            }
        };
        viewer.getControl().addFocusListener(this.focusListener);
    }

    @objid ("1ba6adf3-5e33-11e2-b81d-002564c97630")
    @PreDestroy
    void dispose() {
        if (this.getGraphicalViewer().getControl() != null && !this.getGraphicalViewer().getControl().isDisposed()) {
            this.getGraphicalViewer().getControl().removeFocusListener(this.focusListener);
        }
        // // Unregister as property change listener on options
        // LinkEditorView.options.removePropertyChangeListener(this.optionsChangeListener);
        // this.optionsChangeListener = null;
        
        // Unregister as a model change listener.
        if (this.modelChangeListener != null) {
            ICoreSession session = this.projectService.getSession();
            if (session != null)
                session.getModelChangeSupport().removeModelChangeListener(this.modelChangeListener);
            this.modelChangeListener = null;
        }
        
        this.getCommandStack().removeCommandStackListener(this);
        
        this.getEditDomain().setActiveTool(null);
        
        // Free resources
        LinkEditorView.toolbar = null;
        this.application = null;
        this.projectService = null;
        LinkEditorView.modelServices = null;
        this.eModelService = null;
    }

    /**
     * Adapt the instance to another supported type.
     * @param <T> the type to adapt to
     * @param type the type to adapt to
     * @return the instance of the asked type or <code>null</code>.
     */
    @objid ("1ba6ae02-5e33-11e2-b81d-002564c97630")
    @SuppressWarnings("unchecked")
    public <T> T getAdapter(final Class<T> type) {
        if (type == ZoomManager.class) {
            ScalableFreeformRootEditPart scalableFreeformRootEditPart = (ScalableFreeformRootEditPart) this.getGraphicalViewer().getRootEditPart();
            return (T) scalableFreeformRootEditPart.getZoomManager();
        }
        if (type == GraphicalViewer.class) {
            return (T) this.getGraphicalViewer();
        }
        if (type == CommandStack.class) {
            return (T) this.getCommandStack();
        }
        if (type == EditPart.class && this.getGraphicalViewer() != null) {
            return (T) this.getGraphicalViewer().getRootEditPart();
        }
        if (type == IFigure.class && this.getGraphicalViewer() != null) {
            return (T) ((GraphicalEditPart) this.getGraphicalViewer().getRootEditPart()).getFigure();
        }
        return null;
    }

    /**
     * Returns the command stack.
     * @return the command stack
     */
    @objid ("1ba6ae09-5e33-11e2-b81d-002564c97630")
    protected CommandStack getCommandStack() {
        return this.getEditDomain().getCommandStack();
    }

    /**
     * Returns the edit domain.
     * @return the edit domain
     */
    @objid ("1ba6ae10-5e33-11e2-b81d-002564c97630")
    protected EditDomain getEditDomain() {
        return this.editDomain;
    }

    /**
     * Returns the graphical viewer.
     * @return the graphical viewer
     */
    @objid ("1ba6ae17-5e33-11e2-b81d-002564c97630")
    public GraphicalViewer getGraphicalViewer() {
        return this.graphicalViewer;
    }

    /**
     * Returns the selection synchronizer object. The synchronizer can be used to sync the selection of 2 or more EditPartViewers.
     * @return the synchronizer
     */
    @objid ("1ba90f2d-5e33-11e2-b81d-002564c97630")
    protected SelectionSynchronizer getSelectionSynchronizer() {
        if (this.synchronizer == null)
            this.synchronizer = new SelectionSynchronizer();
        return this.synchronizer;
    }

    /**
     * Hooks the GraphicalViewer to the rest of the Editor. By default, the viewer is added to the SelectionSynchronizer, which can
     * be used to keep 2 or more EditPartViewers in sync.
     */
    @objid ("1ba90f3b-5e33-11e2-b81d-002564c97630")
    protected void hookGraphicalViewer() {
        this.getSelectionSynchronizer().addViewer(this.getGraphicalViewer());
        
        this.getGraphicalViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                LinkEditorView.this.selectionService.setSelection(event.getSelection());
            }
        });
        
        this.getSelectionSynchronizer().addViewer(this.getGraphicalViewer());
    }

    /**
     * Sets the site for this view then creates and initializes the actions. Subclasses may extend this method, but should always
     * call <code>super.init(site, input)
     * </code>.
     * @param part the Eclipse 4 part
     */
    @objid ("1ba90f3e-5e33-11e2-b81d-002564c97630")
    private void createPart(MPart part) {
        // Toolbar
        LinkEditorView.toolbar = part.getToolbar();
        
        // View menu
        for (MMenu mmenu : part.getMenus()) {
            if ("org.modelio.linkeditor.viewmenu".equals(mmenu.getElementId())) {
                this.menu = mmenu;
            }
        }
        this.setEditDomain(new EditDomain());
        this.getCommandStack().addCommandStackListener(this);
    }

    /**
     * Override to set the contents of the GraphicalViewer after it has been created.
     * @see #createGraphicalViewer(Composite)
     */
    @objid ("1ba90f47-5e33-11e2-b81d-002564c97630")
    protected void initializeGraphicalViewer() {
        // Set the viewer content
        BackgroundModel backgroundModel = new BackgroundModel();
        this.getGraphicalViewer().setContents(backgroundModel);
    }

    /**
     * Sets the EditDomain for this EditorPart.
     * @param ed the domain
     */
    @objid ("1ba90f51-5e33-11e2-b81d-002564c97630")
    protected void setEditDomain(final EditDomain ed) {
        this.editDomain = ed;
    }

    /**
     * Sets the graphicalViewer for this EditorPart.
     * @param viewer the graphical viewer
     */
    @objid ("1ba90f58-5e33-11e2-b81d-002564c97630")
    protected void setGraphicalViewer(final GraphicalViewer viewer) {
        this.getEditDomain().addViewer(viewer);
        this.graphicalViewer = viewer;
    }

    /**
     * Forces the view to refresh completely from last received selection.
     */
    @objid ("1ba90f6c-5e33-11e2-b81d-002564c97630")
    public void refreshFromCurrentSelection() {
        // Just avoid refreshing from an invalid model element or when there is
        // not valid control.
        if (this.getGraphicalViewer() != null && this.getGraphicalViewer().getControl() != null
                && !this.getGraphicalViewer().getControl().isDisposed()
                && (this.currentSelection == null || this.currentSelection.isValid())) {
            this.setInput(this.currentSelection);
        }
        
        configureToolbar();
    }

    @objid ("1ba90f79-5e33-11e2-b81d-002564c97630")
    private void setCurrentSelection(final MObject currentSelection) {
        this.currentSelection = currentSelection;
    }

    @objid ("1bab7086-5e33-11e2-b81d-002564c97630")
    void forceRefreshCenterNode() {
        if (this.currentSelection != null && this.currentSelection.isValid()) {
            GraphNode centerNode = ((BackgroundModel) this.graphicalViewer.getContents().getModel()).getCenter();
            if (centerNode != null) {
                GraphicalEditPart centerPart = (GraphicalEditPart) this.graphicalViewer.getEditPartRegistry().get(centerNode);
                if (centerPart != null) {
                    centerPart.refresh();
                }
            }
        }
    }

    /**
     * Eclipse 4 constructor.
     * @param composite the parent SWT composite
     * @param part the Eclipse 4 part
     * @param theProjectService the Modelio project services
     * @param selection the current active selection if any
     */
    @objid ("d4aed2ab-5efd-11e2-a8be-00137282c51b")
    @PostConstruct
    void postConstruct(Composite composite, MPart part, IProjectService theProjectService, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) final IStructuredSelection selection) {
        this.projectService = theProjectService;
        
        // Create the MPart
        this.createPart(part);
        // Create the GUI controls
        this.createControls(composite);
        
        // Sometimes, the view is instantiated only after the project is opened
        // project preferences maybe null if there is no opened project
        GProject project = theProjectService.getOpenedProject();
        if (project != null) {
            onProjectOpened(project);
        
            if (selection != null)
                onSelectionChange(selection);
        } else {
            // No project has been opened yet, initialize preferences with default values
            configureOptions(getProjectPreferences());
        }
    }

    /**
     * @return the active shell
     */
    @objid ("d424256d-fa95-4b38-873d-148d21a13c34")
    public Shell getActiveShell() {
        return this.activeShell;
    }

    /**
     * Get the link editor options.
     * <p>
     * TODO : this method and the {@link #options} attribute should not be static,
     * but EdgeEditPart and others don't have reference to the LinkEditorView instance.
     * @see org.modelio.linkeditor.view.edge.EdgeEditPart
     * @return the link editor options.
     * 
     * 
     * @see org.modelio.linkeditor.view.edge.EdgeEditPart 
     */
    @objid ("6efd7677-751a-49e1-9cf2-688720a56fdb")
    public static LinkEditorOptions getOptions() {
        return options;
    }

    @objid ("d4b5f9ae-5efd-11e2-a8be-00137282c51b")
    @Inject
    @Optional
    void onPickingStart(@EventTopic(ModelioEventTopics.PICKING_START) final IPickingSession session) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                LinkEditorView.this.getEditDomain().setActiveTool(new PickingSelectionTool(session));
            }
        });
    }

    @objid ("6578dfde-33f7-11e2-95fe-001ec947c8cc")
    @Inject
    @Optional
    void onPickingStop(@SuppressWarnings("unused")
@EventTopic(ModelioEventTopics.PICKING_STOP) final IPickingSession session) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                LinkEditorView.this.getEditDomain().setActiveTool(new PanSelectionTool(LinkEditorView.this.navigationService));
            }
        });
    }

    @objid ("6578dfe4-33f7-11e2-95fe-001ec947c8cc")
    @Inject
    @Optional
    void onSelectionChange(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection) {
        // Force a refresh of the content if not pinned.
        if (!getOptions().isPinned()) {
            if (selection != null && selection.size() == 1) {
                Object selectedElement = selection.getFirstElement();
                if (selectedElement instanceof BackgroundEditPart) {
                    return;
                } else if (selectedElement instanceof MObject) {
                    setCurrentSelection((MObject) selectedElement);
                } else if (selectedElement instanceof IAdaptable) {
                    MObject element = (MObject) ((IAdaptable) selectedElement)
                            .getAdapter(MObject.class);
                    setCurrentSelection(element);
                }
            }
            refreshFromCurrentSelection();
        }
    }

    @objid ("1249df2f-9ab2-4dfd-baa3-0b4bc8152bff")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject project) {
        configureOptions(getProjectPreferences());
        
        // Register as a model change listener to update content on model change.
        this.modelChangeListener = new ModelChangeListener(this);
        project.getSession().getModelChangeSupport().addModelChangeListener(this.modelChangeListener);
        // Plug our own command stack that is bound to the Modelio transaction manager
        GraphicalViewer viewer = this.getGraphicalViewer();
        if (viewer != null) {
            this.getEditDomain().setCommandStack(new LinkEditorCommandStack(project.getSession()));
        }
    }

    @objid ("1ba90f74-5e33-11e2-b81d-002564c97630")
    @Inject
    @Optional
    void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject project) {
        configureOptions(null); //set options back to default
        this.setCurrentSelection(null);
        this.refreshFromCurrentSelection();
        // Unregister as a model change listener.
        if (project != null) {
            project.getSession().getModelChangeSupport().removeModelChangeListener(this.modelChangeListener);
        }
        this.modelChangeListener = null;
    }

    @objid ("b1012d11-631f-4af7-aecd-a34331da41b0")
    private void configureOptions(IPreferenceStore projectPreferences) {
        // Link options with the current preference store.
        LinkEditorView.options = new LinkEditorOptions(projectPreferences, LinkEditorView.modelServices);
        
        // Configure toolbar from the options
        configureToolbar();
        
        // Configure view menu from the options
        configureViewMenu(LinkEditorView.options);
    }

    /**
     * Configure toolbar by loading a local copy of some options from the platform preference store.
     * @param theModelServices the model services
     * @param theOptions the editor options
     */
    @objid ("c7203f3d-9ad0-4b2f-8b3f-0833ee992d7f")
    public static void configureToolbar() {
        // The view might not be opened yet
        if (toolbar == null) {
            return;
        }
        
        final LinkEditorOptions theOptions = LinkEditorView.options;
        
        Map<String, MToolBarElement> toolbarElements = buildToolbarElementsMap();  
        
        //Association
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ShowAssociation")).setSelected(theOptions.isAssociationShown()); 
        
        //Dependency filter
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ApplyDependencyFilter")).setSelected(theOptions.isDependencyFiltered());
        
        //Dependency
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ShowDependency")).setSelected(theOptions.isDependencyShown());
        
        //Import
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ShowImportElement")).setSelected(theOptions.isImportShown());
        
        //Inheritance
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ShowInheritance")).setSelected(theOptions.isInheritanceShown());
        
        //NamespaceUse
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ShowNamespaceUse")).setSelected(theOptions.isNamespaceUseShown());
        
        //Pin
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.PinEditor")).setSelected(theOptions.isPinned());
        
        //Trace
        ((MHandledToolItem)toolbarElements.get("org.modelio.linkeditor.handledtoolitem.ShowTrace")).setSelected(theOptions.isTraceShown());
        
        LeftDepthSpinner leftSpinner = ((LeftDepthSpinner)((MToolControl)toolbarElements.get("org.modelio.linkeditor.toolcontrol.LeftDepthSpinner")).getObject());
        if (leftSpinner != null) leftSpinner.setSpinnerValue(theOptions.getLeftDepth());
        
        RightDepthSpinner rightSpinner = ((RightDepthSpinner)((MToolControl)toolbarElements.get("org.modelio.linkeditor.toolcontrol.RightDepthSpinner")).getObject());
        if (rightSpinner != null) rightSpinner.setSpinnerValue(theOptions.getRightDepth());
    }

    /**
     * Configure view menu by loading a local copy of some options from the platform preference store.
     * @param theOptions the editor options
     */
    @objid ("93b353d2-4342-498b-ad0f-eaf237504fba")
    private void configureViewMenu(LinkEditorOptions theOptions) {
        Map<String, MMenuElement> menuElements = buildMenuElementsMap();
        
        MHandledMenuItem horizontalMenuItem = (MHandledMenuItem)menuElements.get("org.modelio.linkeditor.viewmenu.SetHorizontalOrientationLayout");
        MHandledMenuItem verticalMenuItem = (MHandledMenuItem)menuElements.get("org.modelio.linkeditor.viewmenu.SetVerticalOrientationLayout");
        MHandledMenuItem autoMenuItem = (MHandledMenuItem)menuElements.get("org.modelio.linkeditor.viewmenu.SetAutoOrientationLayout");
        horizontalMenuItem.setEnabled(false);
        verticalMenuItem.setEnabled(false);
        autoMenuItem.setEnabled(false);
        
        //Orientation        
        String orientation = theOptions.getOrientation();
        switch(orientation){
        case "Horizontal":
            horizontalMenuItem.setSelected(true);
            break;
        case "Vertical":
            verticalMenuItem.setSelected(true);
            break;
        default:
            autoMenuItem.setSelected(true);
        }
    }

    @objid ("0a29c2ff-5cbd-436f-8ac0-e1c2c9ca0b4b")
    private static Map<String, MToolBarElement> buildToolbarElementsMap() {
        Map<String, MToolBarElement> toolbarElements = new HashMap<>();
        for (MToolBarElement element : toolbar.getChildren()) {           
            toolbarElements.put(element.getElementId(), element);
        }
        return toolbarElements;
    }

    @objid ("6f27d845-9edb-49ea-b73b-e82a255f0a58")
    private Map<String, MMenuElement> buildMenuElementsMap() {
        Map<String, MMenuElement> menuElements = new HashMap<>();
        for (MMenuElement element : this.menu.getChildren()) {           
            menuElements.put(element.getElementId(), element);
        }
        return menuElements;
    }

    @objid ("684ec700-5ba3-47f9-9a37-024fa6cf118f")
    protected IPreferenceStore getProjectPreferences() {
        if (this.projectService != null && this.projectService.getOpenedProject() != null) {            
            return this.projectService.getProjectPreferences(LinkEditor.PLUGIN_ID); 
        }
        return null;
    }

    @objid ("13fb35ea-2f7a-4b1b-bf0b-49b56552398b")
    public void refreshPinnedBackground() {
        BackgroundModel model = (BackgroundModel)(this.getGraphicalViewer().getContents().getModel());
        model.contentChanged();
    }

}
