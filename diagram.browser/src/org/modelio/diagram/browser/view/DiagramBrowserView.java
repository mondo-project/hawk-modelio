package org.modelio.diagram.browser.view;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;
import org.eclipse.e4.ui.model.application.ui.menu.MPopupMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.model.application.ui.menu.impl.ItemImpl;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.diagram.browser.model.bycontext.ByCtxModel;
import org.modelio.diagram.browser.model.byset.BySetModel;
import org.modelio.diagram.browser.model.bytype.ByTypeModel;
import org.modelio.diagram.browser.model.core.AbstractModel;
import org.modelio.diagram.browser.model.core.DiagramRef;
import org.modelio.diagram.browser.model.flat.FlatModel;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Main class of the diagram browser.
 */
@objid ("000c3122-0d4f-10c6-842f-001ec947cd2a")
public class DiagramBrowserView {
    @objid ("505388d6-7b87-4218-af68-48c2069d3c9e")
    private static final String POPUPID = "org.modelio.diagram.browser.popupmenu";

    @objid ("37ccb9f0-b0d5-4063-a119-c9abd0508cbf")
    @Inject
    @Optional
    public ESelectionService selectionService;

    @objid ("096a2c3a-5e4a-4f8b-9426-94f1f858f3f6")
    @Inject
    protected EMenuService menuService;

    @objid ("000c9752-0d4f-10c6-842f-001ec947cd2a")
    protected GProject project;

    @objid ("000caef4-0d4f-10c6-842f-001ec947cd2a")
    protected DiagramBrowserModelChangeListener modelChangeListener;

    @objid ("000cbe6c-0d4f-10c6-842f-001ec947cd2a")
    private final DiagramBrowserActivationStrategy actSupport = null;

    @objid ("000cc664-0d4f-10c6-842f-001ec947cd2a")
    protected DiagramBrowserPickingManager pickingManager;

    @objid ("85b99c0e-54b9-11e2-85c1-002564c97630")
    protected DiagramBrowserPanelProvider diagramBrowserPanelProvider;

    @objid ("750d7fa0-f000-4bcb-b713-64da18a0840c")
    @Inject
    @Optional
    protected IProjectService projectService;

    @objid ("89a98bfd-5c8e-45a2-924d-2dd574ae5d34")
    protected Composite parent;

    @objid ("3e3474be-6fec-4cb6-97ec-3bb21d908170")
    @Inject
    @Optional
    protected IActivationService activationService;

    @objid ("331faaf6-b80f-48c9-b190-5efcd7500c19")
    @Inject
     static EContextService contextService;

    @objid ("000ccc86-0d4f-10c6-842f-001ec947cd2a")
    @PostConstruct
    public void createPartControl(Composite aParent, @Optional IProjectService aProjectService, MPart part) {
        // Connect to session if one is open
        this.parent = aParent;
        this.projectService = aProjectService;
            if (this.projectService != null && this.projectService.getOpenedProject() != null) {
            onProjectOpened(this.projectService.getOpenedProject(), part);
        }
    }

    /**
     * Get the modeling session.
     * @return the modeling session.
     */
    @objid ("000d345a-0d4f-10c6-842f-001ec947cd2a")
    public ICoreSession getSession() {
        return this.project.getSession();
    }

    /**
     * @see INavigationListener#navigateTo(Element target)
     */
    @objid ("000d5a52-0d4f-10c6-842f-001ec947cd2a")
    public void onNavigateElement_______navigateTo(Element target) {
        selectElement(target);
    }

    /**
     * Select and reveal the given model element in the browser.
     * @param element the element to select.
     */
    @objid ("000d76e0-0d4f-10c6-842f-001ec947cd2a")
    public void selectElement(Element element) {
        this.diagramBrowserPanelProvider.getPanel().setSelection(new StructuredSelection(element), true);
    }

    /**
     * This method connects the view to the current modeling session ie set the tree input, branch several listeners like model
     * change or navigate events and so on. It is guaranteed when this method is called that the view GUI and the modeling session
     * are available.
     * @param openedProject
     * @param part
     */
    @objid ("0011a2ce-43b1-10c7-842f-001ec947cd2a")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject openedProject, final MPart part) {
        // @UIEventTopic doesn't seems to be working here...
        this.project = openedProject;
        
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (DiagramBrowserView.this.diagramBrowserPanelProvider==null) {
                    // diagramBrowserPanelProvider may be null if we click diagram browser view before opening a project
                    initDiagramBrowserPanelProvider();
                }
                // install the select browser content provider
                DiagramBrowserView.this.diagramBrowserPanelProvider.switchBrowserModel(getSelectedContentModel(part));
                // set the tree input
                DiagramBrowserView.this.diagramBrowserPanelProvider.setInput(openedProject);
                
                
                // branch a model change listener:
                DiagramBrowserView.this.modelChangeListener = new DiagramBrowserModelChangeListener(DiagramBrowserView.this.diagramBrowserPanelProvider);
                if (openedProject != null) {
                    openedProject.getSession().getModelChangeSupport().addModelChangeListener(DiagramBrowserView.this.modelChangeListener);
                    openedProject.getSession().getModelChangeSupport().addStatusChangeListener(DiagramBrowserView.this.modelChangeListener);
                }
                
                DiagramBrowserView.this.parent.layout();
            }           
        });
        
        // branch application event listeners:
        // navigate listener
        // INavigationService navigationService = O.getDefault().getNavigateService();
        // navigationService.addNavigationListener(this);
        
        // double-click listener , it fires a open diagram or validates a picking
        // this.activateSender = new BrowserActivateSender();
        // getCommonViewer().getTree().addMouseListener(this.activateSender);
        
        //
        // final IServiceLocator locator = this.getSite();
        //
        // final String commandId = "org.modelio.diagram.browser.SwitchBrowserTypeCommandID";
        // final ICommandService commandService = (ICommandService) locator.getService(ICommandService.class);
        // final Command command = commandService.getCommand(commandId);
        // final State state = command.getState(RadioState.STATE_ID);
        // if (state != null) {
        // Object stateValue = state.getValue();
        // if ("flat".equals(stateValue)) {
        // switchBrowserModel(new FlatModel());
        // } else if ("byType".equals(stateValue)) {
        // switchBrowserModel(new ByTypeModel());
        // } else if ("user".equals(stateValue)) {
        // switchBrowserModel(new BySetModel());
        // } else if ("context".equals(stateValue)) {
        // switchBrowserModel(new ByCtxModel());
        // } else {
        // assert (false);
        // }
        // } else {
        // // Fallback, should never happen
        // try {
        // HandlerUtil.updateRadioState(command, "user");
        // } catch (ExecutionException e) {
        // e.printStackTrace();
        // }
        // configureBrowser(new BySetModel());
        // }
    }

    /**
     * Called when the modeling session is closed. This method deactivates the DiagramBrowser ie making it completely passive (no
     * more listeners to anything) It does not 'close' the view in the GUI. This method disconnects the view from the session ie it
     * carefully undoes what the onProjectOpened() did previously. It is not guaranteed when this method is called that the view GUI
     * has not been already disposed.
     * @param session
     * the modeling session
     * @param closedProject
     */
    @objid ("0011ecf2-43b1-10c7-842f-001ec947cd2a")
    @Inject
    @Optional
    void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject closedProject) {
        // @UIEventTopic doesn't seems to be working here...
        
        // remove model change listener if session still alive
        if (closedProject != null && closedProject.isOpen()) {
            closedProject.getSession().getModelChangeSupport().removeModelChangeListener(this.modelChangeListener);
            closedProject.getSession().getModelChangeSupport().removeStatusChangeListener(this.modelChangeListener);
        }
        this.modelChangeListener = null;
        this.project = null;
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                DiagramBrowserView.this.diagramBrowserPanelProvider.setInput(null);
                DiagramBrowserView.this.diagramBrowserPanelProvider.getPanel().getTree().dispose();
                DiagramBrowserView.this.diagramBrowserPanelProvider = null;
            }           
        });
    }

    @objid ("0012503e-43b1-10c7-842f-001ec947cd2a")
    @Focus
    public void setFocus() {
        if (this.diagramBrowserPanelProvider != null) {
            this.diagramBrowserPanelProvider.getPanel().getTree().setFocus();
        }
    }

    @objid ("28686740-4ab5-11e2-a4d3-002564c97630")
    public void selectElement(List<Element> elements) {
        this.diagramBrowserPanelProvider.getPanel().setSelection(new StructuredSelection(elements), true);
    }

    @objid ("2869ede2-4ab5-11e2-a4d3-002564c97630")
    @Inject
    @Optional
    void onNavigateElement(@EventTopic(ModelioEventTopics.NAVIGATE_ELEMENT) final List<MObject> elements) {
        // @UIEventTopic doesn't seems to be working here...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                DiagramBrowserView.this.diagramBrowserPanelProvider.getPanel().setSelection(new StructuredSelection(elements));
            }           
        });
    }

    @objid ("2869edea-4ab5-11e2-a4d3-002564c97630")
    @Inject
    @Optional
    void onPickingStart(@EventTopic(ModelioEventTopics.PICKING_START) final IPickingSession session) {
        // @UIEventTopic doesn't seems to be working here...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (DiagramBrowserView.this.diagramBrowserPanelProvider != null) {
                    DiagramBrowserView.this.pickingManager = new DiagramBrowserPickingManager(DiagramBrowserView.this.diagramBrowserPanelProvider.getPanel(), session);
                    DiagramBrowserView.this.pickingManager.beginPicking();
                }
            }           
        });
    }

    @objid ("2869edf0-4ab5-11e2-a4d3-002564c97630")
    @Inject
    @Optional
    @SuppressWarnings("unused")
    void onPickingStop(@EventTopic(ModelioEventTopics.PICKING_STOP) final IPickingSession session) {
        // @UIEventTopic doesn't seems to be working here...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (DiagramBrowserView.this.pickingManager != null) {
                    DiagramBrowserView.this.pickingManager.endPicking();
                    DiagramBrowserView.this.pickingManager.dispose();
                    DiagramBrowserView.this.pickingManager = null;
                }
            }           
        });
    }

    @objid ("2869edf6-4ab5-11e2-a4d3-002564c97630")
    public DiagramBrowserPanelProvider getComposite() {
        return this.diagramBrowserPanelProvider;
    }

    /**
     * Selects and edits the given element in the tree if possible.
     * @param elementToEdit the element to select and edit.
     */
    @objid ("cd493808-54c7-11e2-ae63-002564c97630")
    public void edit(final Element elementToEdit) {
        Display display = this.diagramBrowserPanelProvider.getPanel().getControl().getDisplay();
        display.asyncExec(new Runnable() {
            
            @Override
            public void run() {
                DiagramBrowserView.this.diagramBrowserPanelProvider.getPanel().expandToLevel(elementToEdit, 0);
                DiagramBrowserView.this.diagramBrowserPanelProvider.getPanel().editElement(elementToEdit, 0);
            }
        });
    }

    @objid ("cd49380d-54c7-11e2-ae63-002564c97630")
    public void collapseAll() {
        if (this.diagramBrowserPanelProvider != null) {            
            this.diagramBrowserPanelProvider.getPanel().collapseAll();
        }
    }

    @objid ("fc05adf8-2a43-402f-8e86-0018f85d2c90")
    protected void initDiagramBrowserPanelProvider() {
        this.diagramBrowserPanelProvider = new DiagramBrowserPanelProvider(this.projectService.getOpenedProject());
        this.diagramBrowserPanelProvider.createPanel(this.parent);
        
        // Add the selection provider
        this.diagramBrowserPanelProvider.getPanel().addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (DiagramBrowserView.this.selectionService != null) {
                    DiagramBrowserView.this.selectionService.setSelection(event.getSelection());
                }
            }
        });
        
        // Add the double click listener
        this.diagramBrowserPanelProvider.getPanel().addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                final ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
                    final Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
                    if (selectedObject instanceof MObject) {
                        if (((MObject) selectedObject).isValid()) {                            
                            DiagramBrowserView.this.activationService.activateMObject((MObject) selectedObject);
                        }
                    } else if (selectedObject instanceof DiagramRef) {
                        DiagramBrowserView.this.activationService.activateMObject(((DiagramRef) selectedObject).getReferencedDiagram());
                    } else if (selectedObject instanceof IAdaptable) {
                        final MObject adapter = (MObject) ((IAdaptable) selectedObject).getAdapter(MObject.class);
                        if (adapter != null) {
                            DiagramBrowserView.this.activationService.activateMObject(adapter);
                        }
                    }
                }
        
            }
        });
        
        // Add the contextual menu
        MPopupMenu popupMenu = this.menuService.registerContextMenu(this.diagramBrowserPanelProvider.getPanel().getTree(), POPUPID);
        
        // FIXME Hack : PopupMenu are disposed when the view is closed and not loaded the second time. Force reloading of e4 popupmenu model 
        MMenu moduleMenu = MMenuFactory.INSTANCE.createMenu();
        popupMenu.getChildren().add(moduleMenu);
    }

    /**
     * Get selected content model in the toolbar
     * @param part @return
     */
    @objid ("756fe621-4b86-442a-9f1d-5530556c46a7")
    protected AbstractModel getSelectedContentModel(MPart part) {
        MToolBar toolbar = part.getToolbar();
        for (MToolBarElement element : toolbar.getChildren()) {
            if (element instanceof ItemImpl) {
                ItemImpl toolItem = (ItemImpl) element;
                if (toolItem.isSelected()) {
                    if (toolItem.getElementId().equals("org.modelio.diagram.browser.toolbar.tool.byset")) {
                        return new BySetModel();
                    } else if (toolItem.getElementId().equals("org.modelio.diagram.browser.toolbar.tool.flat")) {
                        return new FlatModel();
                    } else if (toolItem.getElementId().equals("org.modelio.diagram.browser.toolbar.tool.bytype")) {
                        return new ByTypeModel();
                    } else if (toolItem.getElementId().equals("org.modelio.diagram.browser.toolbar.tool.bycontext")) {
                        return new ByCtxModel();
                    } 
                }               
            }
        }
        return new BySetModel();
    }

}
