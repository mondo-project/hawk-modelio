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
                                    

package org.modelio.model.browser.views;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.services.EContextService;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.picking.IPickingSession;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.api.MTools;
import org.modelio.gproject.module.GModule;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.AttributeLink;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Parameter;
import org.modelio.model.browser.context.ElementCreationDynamicMenuManager;
import org.modelio.model.browser.plugin.ModelBrowser;
import org.modelio.model.browser.views.treeview.ModelBrowserPanelProvider;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Model browser view
 */
@objid ("0014eeca-dd16-1fab-b27f-001ec947cd2a")
public class BrowserView {
    /**
     * The ID of the view as specified by the extension.
     */
    @objid ("001602d8-dd16-1fab-b27f-001ec947cd2a")
    public static final String ID = "org.modelio.browser.part";

    @objid ("ec8bad11-3245-11e2-ad6b-002564c97630")
    public static final String POPUPID = "model.browser.popupmenu.0";

    @objid ("837dd517-1d01-429a-9847-052278adca28")
    private static final String MENUITEM_SHOW_ANALYST = "org.modelio.model.browser.directmenuitem.analyst";

    @objid ("80a333cb-356e-4394-b6bc-56370d3e6c15")
    private static final String MENUITEM_SHOW_FRAGMENTS = "org.modelio.model.browser.directmenuitem.projects";

    @objid ("7dd10462-2c26-4474-9426-5a14d8d7204b")
    private static final String MENUITEM_SHOW_MDA = "org.modelio.model.browser.directmenuitem.mda";

    @objid ("d308a23b-85ae-439a-8036-0dc7ecadcebd")
    private static final String MENUITEM_SHOW_VISIBILITY = "org.modelio.model.browser.directmenuitem.visibility";

    @objid ("196d00c4-cdcc-465d-b748-0e9f0578db49")
    private static final String VIEWMENU = "org.modelio.model.browser.viewmenu";

    @objid ("000a3912-493b-105c-aa42-001ec947cd2a")
    @Inject
     MPart myPart;

    @objid ("f0f31289-ae17-4470-8220-a73e9c142821")
    @Inject
    @Optional
     ESelectionService selectionService;

    @objid ("03dab967-b100-44ba-b2f0-a9ed19cf170b")
     Composite parentComposite;

    @objid ("001318a2-7ab3-1006-9c1d-001ec947cd2a")
     ModelBrowserPanelProvider browserTreePanel;

    @objid ("7f0e6454-16a3-11e2-aa0d-002564c97630")
     BrowserPickingManager pickingManager;

    @objid ("b17646ff-396b-11e2-a430-001ec947c8cc")
    @Inject
    @Optional
     IActivationService activationService;

    @objid ("06e86809-ab94-4f19-b916-81b3105af41b")
    @Inject
    @Optional
     EModelService modelService;

    @objid ("cbbb8469-3ab0-48ff-bcd6-8520c805e184")
     BrowserConfigurator configurator;

    @objid ("87d61f2c-6505-4b48-bbf0-d2b5710c8998")
    @Inject
     static EContextService contextService;

    @objid ("0b9d550a-fffd-47ad-89bb-f622b99f2308")
    private IPropertyChangeListener projPreferenceListener;

    @objid ("00177910-dd16-1fab-b27f-001ec947cd2a")
    @PostConstruct
    void createControls(final Composite parent, EMenuService menuService, @Optional IProjectService projectService) {
        this.parentComposite = parent;
        
        // Sometimes, the view is instantiated only after the project is opened
        if (projectService != null) {
            onProjectOpened(projectService.getOpenedProject(), projectService, menuService);
        }
    }

    /**
     * When double-clicking on a link, trigger a selection of it's target.
     * @param editedElement the edited element.
     */
    @objid ("814fb0af-604c-4b07-8f99-aa795428e28b")
    @Optional
    @Inject
    void onEditElement(@EventTopic(ModelioEventTopics.EDIT_ELEMENT) final MObject editedElement) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (editedElement != null) {
                    if ( MTools.getLinkTool().isLink(editedElement.getMClass())) {
                        selectElement(MTools.getModelTool().getTarget(editedElement));
                    } else {
                        MObject target = null;
                        if (editedElement instanceof Attribute) {
                            target = (((Attribute) editedElement).getType());
                        } else if (editedElement instanceof Parameter) {
                            target = (((Parameter) editedElement).getType());
                        } else if (editedElement instanceof Instance) {
                            target = (((Instance) editedElement).getBase());
                        } else if (editedElement instanceof AttributeLink) {
                            final AttributeLink attributeLink = (AttributeLink) editedElement;
                            target = (attributeLink.getBase());
                        }
                        if (target != null)
                            selectElement(target);
                    }
                }
            }
        });
    }

    @objid ("2e5964c6-d7d2-11e1-a4a6-002564c97630")
    @Inject
    @Optional
    @SuppressWarnings("unused")
    void onFragmentEvents(@EventTopic(ModelioEventTopics.FRAGMENT_EVENTS) final IProjectFragment fragment) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (BrowserView.this.browserTreePanel != null && !BrowserView.this.browserTreePanel.getPanel().getTree().isDisposed()) {
                    BrowserView.this.browserTreePanel.setInput(BrowserView.this.browserTreePanel.getInput());
                }
            }
        });
    }

    @objid ("a2f0a44f-0405-11e2-8e1f-001ec947c8cc")
    @Inject
    @SuppressWarnings("unused")
    @Optional
    void onModuleEvents(@EventTopic(ModelioEventTopics.MODULE_EVENTS) final GModule module) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (BrowserView.this.browserTreePanel != null && !BrowserView.this.browserTreePanel.getPanel().getTree().isDisposed()) {
                    BrowserView.this.browserTreePanel.setInput(BrowserView.this.browserTreePanel.getInput());
                }
            }
        });
    }

    @objid ("b21075b6-404b-11e2-8458-002564c97630")
    @Inject
    @Optional
    void onNavigateElement(@EventTopic(ModelioEventTopics.NAVIGATE_ELEMENT) final List<MObject> elements) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                BrowserView.this.browserTreePanel.getPanel().setSelection(new StructuredSelection(elements));
            }
        });
    }

    @objid ("b21075be-404b-11e2-8458-002564c97630")
    @Inject
    @Optional
    void onNavigateElement(@EventTopic(ModelioEventTopics.NAVIGATE_ELEMENT) final MObject element) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                BrowserView.this.browserTreePanel.getPanel().setSelection(new StructuredSelection(element));
            }
        });
    }

    @objid ("7f0e6455-16a3-11e2-aa0d-002564c97630")
    @Inject
    @Optional
    void onPickingStart(@EventTopic(ModelioEventTopics.PICKING_START) final IPickingSession session) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().syncExec(new Runnable() {
        
            @Override
            public void run() {
                if (BrowserView.this.browserTreePanel != null) {
                    BrowserView.this.pickingManager = new BrowserPickingManager(BrowserView.this.browserTreePanel, session);
                    BrowserView.this.pickingManager.beginPicking();
                }
            }
        });
    }

    @objid ("7f0e645b-16a3-11e2-aa0d-002564c97630")
    @SuppressWarnings("unused")
    @Inject
    @Optional
    void onPickingStop(@EventTopic(ModelioEventTopics.PICKING_STOP) final IPickingSession session) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().syncExec(new Runnable() {
        
            @Override
            public void run() {
                if (BrowserView.this.pickingManager != null) {
                    BrowserView.this.pickingManager.endPicking();
                    BrowserView.this.pickingManager.dispose();
                    BrowserView.this.pickingManager = null;
                }
            }
        });
    }

    @objid ("004a9840-181d-1fd2-a931-001ec947cd2a")
    @Inject
    @Optional
    void onProjectOpened(@EventTopic(ModelioEventTopics.PROJECT_OPENED) final GProject openedProject, final IProjectService projectService, final EMenuService menuService) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (BrowserView.this.browserTreePanel == null) {
                    if (openedProject != null) {
                        // Create the view content
                        BrowserView.this.browserTreePanel = new ModelBrowserPanelProvider();
                        BrowserView.this.browserTreePanel.createPanel(BrowserView.this.parentComposite);
        
                        // Add the contextual menu
                        menuService.registerContextMenu(BrowserView.this.browserTreePanel.getPanel().getTree(), POPUPID);
        
                        // Add the selection provider
                        BrowserView.this.browserTreePanel.getPanel().addSelectionChangedListener(new ISelectionChangedListener() {
                            @Override
                            public void selectionChanged(SelectionChangedEvent event) {
                                if (BrowserView.this.selectionService != null) {
                                    BrowserView.this.selectionService.setSelection(event.getSelection());
                                }
                            }
                        });
                        BrowserView.this.browserTreePanel.getPanel().addDoubleClickListener(new IDoubleClickListener() {
                            @Override
                            public void doubleClick(DoubleClickEvent event) {
                                final ISelection selection = event.getSelection();
                                if (selection instanceof IStructuredSelection && ((IStructuredSelection) selection).size() == 1) {
                                    final Object selectedObject = ((IStructuredSelection) selection).getFirstElement();
                                    if (selectedObject instanceof MObject) {
                                        BrowserView.this.activationService.activateMObject((MObject) selectedObject);
                                        TreeViewer tv = BrowserView.this.browserTreePanel.getPanel();
                                        if (tv.getExpandedState(selectedObject))
                                            tv.collapseToLevel(selectedObject, AbstractTreeViewer.ALL_LEVELS);
                                        else
                                            tv.expandToLevel(selectedObject, 1);
                                    }
                                }
        
                            }
                        });
        
                        // Branching the dynamic element creation menu stuff
                        {
                            // Each time the popupmenu is about to be displayed,
                            // re-configure it
                            BrowserView.this.browserTreePanel.getPanel().getTree().addMenuDetectListener(new MenuDetectListener() {
                                @Override
                                public void menuDetected(MenuDetectEvent e) {
                                    final ISelection selection = BrowserView.this.browserTreePanel.getPanel().getSelection();
                                    if (selection.isEmpty()) {
                                        return;
                                    }
        
                                    final IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        
                                    final Object obj = structuredSelection.getFirstElement();
                                    if (obj instanceof MObject) {
                                        // Get the dynamic creation menu from the current part, it can't be injected right here...
                                        for (MMenu menu : BrowserView.this.myPart.getMenus()) {
                                            for (MMenuElement subMenu : menu.getChildren()) {
                                                if (subMenu.getElementId().equals(ElementCreationDynamicMenuManager.MENUID)) {
                                                    ElementCreationDynamicMenuManager.configure((MMenu) subMenu, (MObject) obj);
                                                }
                                            }
                                        }
                                    }  
                                }
                            });
                        }
                    } // else openedProject != null
                }// this.browserTreePanel == null
                if (openedProject != null) {
                    // Manually update the preferences...
                    configurePanelByPreferences(projectService);
                    initBrowserTreePanelForOpenedProject(openedProject);
                }
                BrowserView.this.parentComposite.layout(true,true);     // Lay out the children of the parent composite
            }
        });
    }

    @objid ("004ad396-181d-1fd2-a931-001ec947cd2a")
    @Inject
    @SuppressWarnings("unused")
    @Optional
    void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSED) final GProject project) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                if (BrowserView.this.browserTreePanel != null && !BrowserView.this.browserTreePanel.getPanel().getTree().isDisposed()) {
                    // avoid java.lang.IllegalStateException: KernelRegistry - Provider
                    // service id 1 no longer active.
                    // because of
                    // StructuredViewer.preservingSelection(StructuredViewer.java:1404)
                    BrowserView.this.browserTreePanel.getPanel().setSelection(null);
                    BrowserView.this.browserTreePanel.activateEdition(null);
                    BrowserView.this.browserTreePanel.setInput(null);
        
                    // Reset configuration
                    BrowserView.this.configurator = null;
                }
            }
        });
    }

    /**
     * Selects and edits the given element in the tree if possible.
     * @param elementToEdit the element to select and edit.
     */
    @objid ("4e50bb86-ccde-11e1-97e5-001ec947c8cc")
    public void edit(final Element elementToEdit) {
        Display display = this.browserTreePanel.getPanel().getControl().getDisplay();
        display.asyncExec(new Runnable() {
        
            @Override
            public void run() {
                BrowserView.this.browserTreePanel.getPanel().expandToLevel(elementToEdit, 0);
                BrowserView.this.browserTreePanel.getPanel().editElement(elementToEdit, 0);
            }
        });
    }

    /**
     * Sets new roots for the browser view.
     * @param roots the new browser roots.
     */
    @objid ("b8409468-f1d7-11e1-af04-002564c97630")
    public void setRoots(final List<Object> roots) {
        // FIXME with the replacement of UIEvents, the browser tree panel instantiation is now asynchronous... We have to make the set roots asynchronous too.
        Display.getDefault().asyncExec(new Runnable() {
        
            @Override
            public void run() {
                BrowserView.this.browserTreePanel.setLocalRoots(roots);
            }
        });
    }

    @objid ("35e43636-43a7-11e2-b513-002564c97630")
    public void collapseAll() {
        this.browserTreePanel.getPanel().collapseAll();
    }

    @objid ("f34dc898-88a8-4bc9-a3e8-c23e0bb783cd")
    public boolean isShowAnalystModel() {
        return this.browserTreePanel.isShowAnalystModel();
    }

    @objid ("406e594d-3f78-4614-b7ec-2f6513a3cfcb")
    public void setShowAnalystModel(boolean showAnalystModel) {
        if (this.configurator != null) {
            this.configurator.setShowAnalystModel(showAnalystModel);
        }
    }

    @objid ("724330e9-4540-11e2-aeb7-002564c97630")
    public boolean isShowVisibility() {
        return this.browserTreePanel.isShowVisibility();
    }

    @objid ("724330ed-4540-11e2-aeb7-002564c97630")
    public void setShowVisibility(boolean showVisibility) {
        if (this.configurator != null) {
            this.configurator.setShowVisibility(showVisibility);
        }
    }

    @objid ("72459249-4540-11e2-aeb7-002564c97630")
    public boolean isShowMdaModel() {
        return this.browserTreePanel.isShowMdaModel();
    }

    @objid ("7245924d-4540-11e2-aeb7-002564c97630")
    public void setShowMdaModel(boolean showMdaModel) {
        if (this.configurator != null) {
            this.configurator.setShowMdaModel(showMdaModel);
        }
    }

    @objid ("72459250-4540-11e2-aeb7-002564c97630")
    public boolean isShowProjects() {
        return this.browserTreePanel.isShowProjects();
    }

    @objid ("72459254-4540-11e2-aeb7-002564c97630")
    public void setShowProjects(boolean showProjects) {
        if (this.configurator != null) {
            this.configurator.setShowProjects(showProjects);
        }
    }

    @objid ("031063bd-4607-11e2-960d-002564c97630")
    public void selectElement(MObject element) {
        this.browserTreePanel.getPanel().setSelection(new StructuredSelection(element));
    }

    @objid ("031063c0-4607-11e2-960d-002564c97630")
    public void selectElement(List<MObject> elements) {
        this.browserTreePanel.getPanel().setSelection(new StructuredSelection(elements));
    }

    /**
     * Configure panel from project preferences
     * @param projectService
     */
    @objid ("2c273c76-b3a3-4492-b0cf-fbaf7a1a0562")
    public void configurePanelByPreferences(final IProjectService projectService) {
        if (this.configurator == null) {
            this.configurator = new BrowserConfigurator(projectService, this.myPart);
        
            // Add a property change listener for future updates
            this.projPreferenceListener = new IPropertyChangeListener() {
        
                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    Display.getDefault().asyncExec(new Runnable() {
                        
                        @Override
                        public void run() {
                            configurePanelByPreferences(projectService);
                        }
                    });
                }
            };
            projectService.getProjectPreferences(this.myPart.getElementId()).addPropertyChangeListener(this.projPreferenceListener);
        }
        if (BrowserView.this.browserTreePanel != null) {
            this.configurator.loadConfiguration(BrowserView.this.browserTreePanel);
            configureMenuItem();
        }
    }

    @objid ("15cf03a4-16da-11e2-aa0d-002564c97630")
    @Focus
    void setFocus() {
        if (this.browserTreePanel != null) {
            this.browserTreePanel.getPanel().getTree().setFocus();
        }
    }

    /**
     * Initialize the view content
     * @param openedProject
     */
    @objid ("b803a8da-948c-4fc7-baa5-5acb6b0e2c02")
    void initBrowserTreePanelForOpenedProject(final GProject openedProject) {
        this.browserTreePanel.setInput(openedProject);
        this.browserTreePanel.activateEdition(openedProject.getSession());
    }

    /**
     * Configure the view menu items
     */
    @objid ("5eabd6a3-63ff-4645-9579-34405f3faa0c")
    void configureMenuItem() {
        MMenu hideMenu = null;
        for (MMenu mmenu : this.myPart.getMenus()) {
            if (VIEWMENU.equals(mmenu.getElementId())) {
                hideMenu = mmenu;
                break;
            }
        }
        if (hideMenu != null) {
            // show/hide analyst
            MDirectMenuItem analystMenuItem = (MDirectMenuItem) this.modelService.find(MENUITEM_SHOW_ANALYST, hideMenu);
            analystMenuItem.setSelected(isShowAnalystModel());
            // show/hide fragments
            MDirectMenuItem fragmentsMenuItem = (MDirectMenuItem) this.modelService.find(MENUITEM_SHOW_FRAGMENTS, hideMenu);
            fragmentsMenuItem.setSelected(isShowProjects());
            // show/hide mda
            MDirectMenuItem mdaMenuItem = (MDirectMenuItem) this.modelService.find(MENUITEM_SHOW_MDA, hideMenu);
            mdaMenuItem.setSelected(isShowMdaModel());
            // show/hide visibility
            MDirectMenuItem visibilityMenuItem = (MDirectMenuItem) this.modelService.find(MENUITEM_SHOW_VISIBILITY, hideMenu);
            visibilityMenuItem.setSelected(isShowVisibility());
        }
    }

    @objid ("6ee19fc9-6353-4500-b1d9-45be4afd8f96")
    public List<Object> getRoots() {
        return this.browserTreePanel.getLocalRoots();
    }

    @objid ("c64ea87f-f2fd-454f-9e41-e8e0337b8122")
    @PreDestroy
    void onDispose(@Optional IProjectService projectService) {
        if (projectService != null && this.myPart!=null) {
            // Remove the project preferences change listener 
            projectService.getProjectPreferences(this.myPart.getElementId()).removePropertyChangeListener(this.projPreferenceListener);
            this.projPreferenceListener = null;
        } else if (this.projPreferenceListener != null) {
            ModelBrowser.LOG.warning(new Throwable("Cannot unregister project preferences listener, projectService="+projectService+", myPart="+this.myPart));
        }
    }

    /**
     * @return the Eclipse context service.
     */
    @objid ("6574205c-b6f6-4937-9aaa-74986b9044d6")
    public static EContextService getContextService() {
        return contextService;
    }

    @objid ("2efe5dfc-acc9-4d28-8b45-bece3ca07c53")
    private static class BrowserConfigurator {
        @objid ("453a5270-aab8-424d-8614-aec9e65b00a0")
        private static final String SHOW_ANALYST_MODEL = "ShowAnalystModel";

        @objid ("81eed10c-c520-43d6-b1a9-899aef50fab8")
        private static final String SHOW_FRAGMENTS = "ShowFragments";

        @objid ("a4702ba1-5d26-4ac8-b33e-e9322cf8fb95")
        private static final String SHOW_MDA_MODEL = "ShowMdaModel";

        @objid ("8f89b379-ad7f-4584-b9ab-d694dfd1bfb3")
        private static final String SHOW_VISIBILITY = "ShowVisibility";

        @objid ("a87b089a-d517-4bac-8714-457d9a964794")
        private static final boolean SHOW_ANALYST_MODEL_DEFAULT = false;

        @objid ("4cc182e0-588c-4788-ab3c-9a7b7cb507e6")
        private static final boolean SHOW_FRAGMENTS_DEFAULT = false;

        @objid ("ae2e1b50-8782-4c04-8733-9faa6ef08f73")
        private static final boolean SHOW_MDA_MODEL_DEFAULT = false;

        @objid ("a46962e7-2f4a-48e9-aade-7cc5d3e750e0")
        private static final boolean SHOW_VISIBILITY_DEFAULT = true;

        @objid ("59b32b4d-7586-4f2e-8d00-16261cc4f597")
        private IProjectService projectService;

        @objid ("b1d28743-c80c-45eb-9bf8-f2151e6e4ce5")
        private MPart part;

        @objid ("61af700c-eb93-4d98-ac74-5978ea753f76")
        public void setShowAnalystModel(boolean isShowAnalystModel) {
            final IPreferenceStore prefs = this.projectService.getProjectPreferences(this.part.getElementId());
            if (prefs != null) {
                prefs.setValue(SHOW_ANALYST_MODEL, isShowAnalystModel);
            }
        }

        @objid ("4a0d1950-03a2-4328-abbe-2bcdbe3a8c50")
        public void loadConfiguration(ModelBrowserPanelProvider browserTreePanel) {
            final IPreferenceStore prefs = this.projectService.getProjectPreferences(this.part.getElementId());
            if (prefs != null) {
                browserTreePanel.setShowAnalystModel(prefs.getBoolean(SHOW_ANALYST_MODEL));
                browserTreePanel.setShowProjects(prefs.getBoolean(SHOW_FRAGMENTS));
                browserTreePanel.setShowMdaModel(prefs.getBoolean(SHOW_MDA_MODEL));
                browserTreePanel.setShowVisibility(prefs.getBoolean(SHOW_VISIBILITY));
            }
        }

        @objid ("27bbbeb8-af96-4787-888e-b39b9551907f")
        public BrowserConfigurator(IProjectService projectService, MPart part) {
            this.projectService = projectService;
            this.part = part;
            
            final IPreferenceStore prefs = projectService.getProjectPreferences(part.getElementId());
            if (prefs != null) {
                prefs.setDefault(SHOW_ANALYST_MODEL, SHOW_ANALYST_MODEL_DEFAULT);
                prefs.setDefault(SHOW_FRAGMENTS, SHOW_FRAGMENTS_DEFAULT);
                prefs.setDefault(SHOW_MDA_MODEL, SHOW_MDA_MODEL_DEFAULT);
                prefs.setDefault(SHOW_VISIBILITY, SHOW_VISIBILITY_DEFAULT);
            }
        }

        @objid ("4aca6d31-b75c-4661-b5f2-dab2c7540ce9")
        public void setShowProjects(boolean isShowProjects) {
            final IPreferenceStore prefs = this.projectService.getProjectPreferences(this.part.getElementId());
            if (prefs != null) {
                prefs.setValue(SHOW_FRAGMENTS, isShowProjects);
            }
        }

        @objid ("454683c6-a027-473f-8b6b-1521733067b3")
        public void setShowMdaModel(boolean isShowMdaModel) {
            final IPreferenceStore prefs = this.projectService.getProjectPreferences(this.part.getElementId());
            if (prefs != null) {
                prefs.setValue(SHOW_MDA_MODEL, isShowMdaModel);
            }
        }

        @objid ("596e31d0-8e60-4be2-9a5c-65bcd25cb6c4")
        public void setShowVisibility(boolean isShowVisibility) {
            final IPreferenceStore prefs = this.projectService.getProjectPreferences(this.part.getElementId());
            if (prefs != null) {
                prefs.setValue(SHOW_VISIBILITY, isShowVisibility);
            }
        }

    }

}
