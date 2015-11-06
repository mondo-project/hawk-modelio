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
                                    

package org.modelio.app.project.conf.dialog.modules.list;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.modelio.api.module.ILicenseInfos;
import org.modelio.api.module.IModule.ModuleRuntimeState;
import org.modelio.api.module.IModule;
import org.modelio.api.module.ModuleException;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.project.conf.dialog.ProjectModel;
import org.modelio.app.project.conf.dialog.common.ModuleHelper;
import org.modelio.app.project.conf.dialog.common.ScopeHelper;
import org.modelio.app.project.conf.dialog.modules.ModuleRemovalConfirmationDialog;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.gproject.data.project.FragmentType;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.module.GModule;
import org.modelio.gproject.module.IModuleCatalog;
import org.modelio.gproject.module.IModuleHandle;
import org.modelio.gproject.module.ModuleId;
import org.modelio.gproject.module.ModuleSorter;
import org.modelio.gproject.module.catalog.FileModuleStore;
import org.modelio.mda.infra.catalog.CompatibilityHelper;
import org.modelio.mda.infra.catalog.ModuleCatalogPanel;
import org.modelio.mda.infra.service.IModuleService;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.ui.UIImages;
import org.modelio.ui.progress.IModelioProgressService;
import org.modelio.vbasic.collections.TopologicalSorter.CyclicDependencyException;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.smkernel.AccessDeniedException;

/**
 * The modules management section.
 */
@objid ("a73f82a6-33f6-11e2-a514-002564c97630")
public class ModulesSection {
    /**
     * The project that is currently being displayed by the section.
     */
    @objid ("a73f82a8-33f6-11e2-a514-002564c97630")
    private ProjectModel projectAdapter;

    @objid ("a73f82aa-33f6-11e2-a514-002564c97630")
    protected IEclipseContext applicationContext;

    @objid ("a73f82ab-33f6-11e2-a514-002564c97630")
    private TableViewer modulesTable;

    @objid ("a73f82ad-33f6-11e2-a514-002564c97630")
    protected Button removeButton;

    @objid ("af20f1c8-3ed8-11e2-8121-002564c97630")
    protected static final Image CHECKED = AbstractUIPlugin
    .imageDescriptorFromPlugin(AppProjectConf.PLUGIN_ID, "icons/checked.gif").createImage();

    @objid ("af20f1ca-3ed8-11e2-8121-002564c97630")
    protected static final Image UNCHECKED = AbstractUIPlugin.imageDescriptorFromPlugin(AppProjectConf.PLUGIN_ID,
            "icons/unchecked.gif").createImage();

    @objid ("496695f8-4e09-4d0c-823f-13523624edbf")
    private Button addButton;

    @objid ("5724b605-254e-481b-be67-aa6f34f29710")
    private IStructuredSelection moduleSelectionsInCatalog;

    @objid ("e310a2d1-2b07-40d4-a1a1-15b35793dc6b")
    protected ModelioEnv env;

    @objid ("e3603a3d-944f-4c70-9e0a-e809159a92ca")
    private Button catalogButton;

    @objid ("7938901f-13fa-451f-9543-7699a5a0991c")
    protected CatalogController catalogController;

    @objid ("9430ad11-354d-4207-82f0-0ac92d5dd059")
     ModuleCatalogPanel moduleCatalogPanel = null;

    @objid ("45bd66ad-956a-46e3-9bf8-0be27611f68b")
    protected IModuleService moduleService;

    @objid ("bc59e504-6e74-4fe2-ba11-2da4a66b0b16")
    protected IModelioProgressService progressService;

    /**
     * @param application The Eclipse context
     * @param env Modelio environment
     */
    @objid ("a73f82ae-33f6-11e2-a514-002564c97630")
    public ModulesSection(IEclipseContext application, ModelioEnv env) {
        this.applicationContext = application;
        this.env = env;
        this.catalogController = new CatalogController();
        this.moduleService = this.applicationContext.get(IModuleService.class);
        this.progressService = this.applicationContext.get(IModelioProgressService.class);
    }

    /**
     * @param projectAdapter the project model
     */
    @objid ("a73f82b1-33f6-11e2-a514-002564c97630")
    public void setInput(ProjectModel projectAdapter) {
        this.projectAdapter = projectAdapter;
        
        if (projectAdapter != null) {
            this.modulesTable.setInput(projectAdapter.getModules());
            Boolean enableRemove = (!this.modulesTable.getSelection().isEmpty()) && getProjectAdapter().isLocalProject();
            this.removeButton.setEnabled(enableRemove);
            if (enableRemove)
                this.addButton.setEnabled(false);
        } else {
            this.modulesTable.setInput(new Object[0]);
            this.removeButton.setEnabled(false);
        }
        
        for (TableColumn col : this.modulesTable.getTable().getColumns()) {
            col.pack();
        }
    }

    /**
     * Build the Eclipse form Section.
     * @param toolkit the form toolkit
     * @param parent the parent composite
     * @return the built section.
     */
    @objid ("a73f82b4-33f6-11e2-a514-002564c97630")
    public Section createControls(final FormToolkit toolkit, final Composite parent) {
        final Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                | Section.DESCRIPTION);
        section.setText(AppProjectConf.I18N.getString("ModulesSection.SectionText")); //$NON-NLS-1$
        section.setDescription(AppProjectConf.I18N.getString("ModulesSection.SectionDescription")); //$NON-NLS-1$
        section.setExpanded(true);
        
        final Composite composite = toolkit.createComposite(section, SWT.WRAP);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        
        final Table table = toolkit.createTable(composite, SWT.BORDER | SWT.FULL_SELECTION);
        this.modulesTable = new TableViewer(table);
        table.setHeaderVisible(true);
        
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 180;
        gd.minimumWidth = 300;
        table.setLayoutData(gd);
        
        this.modulesTable.setContentProvider(new ArrayContentProvider());
        
        TableViewerColumn enableColumn = new TableViewerColumn(this.modulesTable, SWT.LEFT);
        enableColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.EnableColumn")); //$NON-NLS-1$
        enableColumn.getColumn().setWidth(30);
        enableColumn.setEditingSupport(new ModuleStateEditingSupport(this.modulesTable, this.applicationContext));
        enableColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ""; //$NON-NLS-1$
            }
        
            @Override
            public Image getImage(Object element) {
                if (element instanceof GModule) {
                    ModuleComponent moduleElement = ((GModule) element).getModuleElement();
                    if (moduleElement != null) {
                        IModule iModule = ModulesSection.this.moduleService.getIModule(moduleElement);
                        if (iModule != null && iModule.getState() == ModuleRuntimeState.Started) {
                            return CHECKED;
                        }
                    }
                }
                return UNCHECKED;
            }
        });
        
        TableViewerColumn scopeColumn = new TableViewerColumn(this.modulesTable, SWT.NONE);
        scopeColumn.getColumn().setWidth(120);
        scopeColumn.getColumn().setResizable(true);
        scopeColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.ScopeColumn")); //$NON-NLS-1$
        scopeColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof GModule) {
                    return ScopeHelper.getText(((GModule) element).getScope());
                }
                return ""; //$NON-NLS-1$
            }
        });
        
        TableViewerColumn labelColumn = new TableViewerColumn(this.modulesTable, SWT.LEFT);
        labelColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.NameColumn")); //$NON-NLS-1$
        labelColumn.getColumn().setWidth(100);
        labelColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ModuleHelper.getLabel(element, ModulesSection.this.moduleService);
            }
        
            @Override
            public Image getImage(Object element) {
                return ModuleHelper.getIcon(element);
            }
        });
        
        TableViewerColumn versionColumn = new TableViewerColumn(this.modulesTable, SWT.RIGHT);
        versionColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.VersionColumn")); //$NON-NLS-1$
        versionColumn.getColumn().setWidth(20);
        versionColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ModuleHelper.getVersion(element);
            }
        
            @Override
            public Image getImage(Object element) {
                return null;
            }
        });
        
        TableViewerColumn statusColumn = new TableViewerColumn(this.modulesTable, SWT.LEFT);
        statusColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.StatusColumn")); //$NON-NLS-1$
        statusColumn.getColumn().setWidth(100);
        statusColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof GModule) {
                    ModuleComponent moduleElement = ((GModule) element).getModuleElement();
                    if (moduleElement != null) {
                        IModule iModule = ModulesSection.this.moduleService.getIModule(moduleElement);
                        if (iModule != null) {
                            return AppProjectConf.I18N.getString("ModulesSection." + iModule.getState().name()); //$NON-NLS-1$
                        }
                    }
                }
                return AppProjectConf.I18N.getString("ModulesSection.Broken"); //$NON-NLS-1$
            }
        
            @Override
            public Image getImage(Object element) {
                return null;
            }
        });
        
        TableViewerColumn licenseColumn = new TableViewerColumn(this.modulesTable, SWT.LEFT);
        licenseColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.LicenseColumn")); //$NON-NLS-1$
        licenseColumn.getColumn().setWidth(200);
        licenseColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof GModule) {
                    ModuleComponent moduleElement = ((GModule) element).getModuleElement();
                    if (moduleElement != null) {
                        IModule iModule = ModulesSection.this.moduleService.getIModule(moduleElement);
                        if (iModule != null) {
                            final ILicenseInfos licenseInfos = iModule.getLicenseInfos();
                            if (licenseInfos != null) {
                                Date date = licenseInfos.getDate();
                                if (date != null) {
                                    SimpleDateFormat sdf = new SimpleDateFormat(AppProjectConf.I18N.getString("ModulesSection.License.DateFormat"));
                                    return AppProjectConf.I18N.getMessage("ModulesSection.License." + licenseInfos.getStatus().name() + ".limited", sdf.format(date));
                                } else {
                                    return AppProjectConf.I18N.getMessage("ModulesSection.License." + licenseInfos.getStatus().name() + ".unlimited"); 
                                }
                            } else {
                                AppProjectConf.LOG.warning("'"+iModule.getName()+"' ("+iModule.getClass().getName()+") module has no license info (state="+iModule.getState()+")");
                            }
                        }
                    }
                }
                return AppProjectConf.I18N.getString("ModulesSection.License.UNDEFINED.unlimited"); //$NON-NLS-1$
            }
        
            @Override
            public Image getImage(Object element) {
                return null;
            }
        });
        
        TableViewerColumn compatibilityColumn = new TableViewerColumn(this.modulesTable, SWT.LEFT);
        compatibilityColumn.getColumn().setText(AppProjectConf.I18N.getString("ModulesSection.CompatibilityColumn")); //$NON-NLS-1$
        compatibilityColumn.getColumn().setWidth(200);
        compatibilityColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                if (element instanceof GModule) {
                    IModuleHandle mh = ((GModule) element).getModuleHandle();
                    switch (CompatibilityHelper.getCompatibilityLevel(ModulesSection.this.env, mh)) {
                    case COMPATIBLE:
                        return AppProjectConf.I18N.getString("ModulesSection.Compatible");
                    case FULLYCOMPATIBLE:
                        return AppProjectConf.I18N.getString("ModulesSection.FullyCompatible");
                    case MODELIO_TOO_OLD:
                        return AppProjectConf.I18N.getString("ModulesSection.ModelioTooOld");
                    case MODULE_TOO_OLD:
                        return AppProjectConf.I18N.getString("ModulesSection.ModuleTooOld");
                    default:
                        break;
                    }
                }
                return "";
            }
        
            @Override
            public Color getForeground(Object element) {
                if (element instanceof GModule) {
                    IModuleHandle mh = ((GModule) element).getModuleHandle();
                    switch (CompatibilityHelper.getCompatibilityLevel(ModulesSection.this.env, mh)) {
                    case COMPATIBLE:
                        return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
                    case FULLYCOMPATIBLE:
                        return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
                    case MODELIO_TOO_OLD:
                    case MODULE_TOO_OLD:
                        return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
                    default:
                        break;
                    }
                }
                return super.getForeground(element);
            }
        });
        this.modulesTable.setInput(null);
        
        this.modulesTable.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                setInput(getProjectAdapter());
            }
        });
        
        // The buttons composite
        Composite btnPanel = toolkit.createComposite(composite, SWT.NONE);
        GridData gd2 = new GridData(SWT.FILL, SWT.FILL, false, false);
        btnPanel.setLayoutData(gd2);
        btnPanel.setLayout(new GridLayout(1, false));
        
        // The catalog composite
        final Composite catalogComposite = toolkit.createComposite(composite, SWT.WRAP);
        catalogComposite.setLayout(new GridLayout());
        GridData gd3 = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd3.heightHint = 180;
        gd3.minimumWidth = 430;
        gd3.exclude = true;
        catalogComposite.setLayoutData(gd3);
        this.moduleCatalogPanel = createModuleCatalogPanel(catalogComposite);
        catalogComposite.setVisible(false);
        
        // The add button
        this.addButton = toolkit.createButton(btnPanel, "", SWT.PUSH);
        this.addButton.setImage(UIImages.ADD);
        this.addButton.setToolTipText(AppProjectConf.I18N.getString("ModulesSection.AddModuleButtonToolTip"));
        this.addButton.setEnabled(false);
        this.addButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                addSelectedModules();
            }
        });
        
        // The delete button
        this.removeButton = toolkit.createButton(btnPanel, "", SWT.PUSH); //$NON-NLS-1$
        this.removeButton.setImage(UIImages.DELETE);
        this.removeButton.setToolTipText(AppProjectConf.I18N.getString("ModulesSection.RemoveModuleButtonToolTip"));
        this.removeButton.setEnabled(false);
        this.removeButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));
        this.removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent evt) {
                AppProjectConf.LOG.debug("Remove a module"); //$NON-NLS-1$
        
                ModuleRemovalConfirmationDialog confirmDlg = new ModuleRemovalConfirmationDialog(ModulesSection.this.removeButton.getShell());
                confirmDlg.setBlockOnOpen(true);
                if (confirmDlg.open() == 0) {
                    for (TableItem item : table.getSelection()) {
                        final GModule module = (GModule) item.getData();
                        try {
                            ModulesSection.this.moduleService.removeModule(module);
                        } catch (ModuleException e) {
                            MessageDialog.openError(null, AppProjectConf.I18N.getMessage("ModulesSection.UnableToRemoveModule", module.getName()), e.getLocalizedMessage() + "\n" + (e.getCause() != null ? e.getCause().getLocalizedMessage() : ""));
                            AppProjectConf.LOG.debug(e);
                        } catch (AccessDeniedException e) {
                            MessageDialog.openError(null, AppProjectConf.I18N.getMessage("ModulesSection.UnableToRemoveModule", module.getName()), e.getLocalizedMessage());
                            AppProjectConf.LOG.debug(e);
                        }
                    }
                }
                setInput(getProjectAdapter());
            }
        });
        
        // The open/close catalog button
        this.catalogButton = toolkit.createButton(btnPanel, "", SWT.PUSH);
        this.catalogButton.setImage(UIImages.MAXIMIZE);
        this.catalogButton.setToolTipText(AppProjectConf.I18N.getString("ModulesSection.OpenCatalogButtonToolTip"));
        this.catalogButton.setEnabled(true);
        this.catalogButton.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));
        this.catalogButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Button button = (Button) e.widget;
                GridData catalogLayoutData = (GridData) catalogComposite.getLayoutData();
                if (catalogComposite.getVisible()) {
                    catalogComposite.setVisible(false);
                    catalogLayoutData.exclude = true;
                    button.setImage(UIImages.MAXIMIZE);
                    button.setToolTipText(AppProjectConf.I18N.getString("ModulesSection.OpenCatalogButtonToolTip"));
                } else {
                    catalogComposite.setVisible(true);
                    catalogLayoutData.exclude = false;
                    button.setImage(UIImages.MINIMIZE);
                    button.setToolTipText(AppProjectConf.I18N.getString("ModulesSection.CloseCatalogButtonToolTip"));
                }
                composite.layout();
                section.layout();
            }
        });
        
        toolkit.paintBordersFor(composite);
        section.setClient(composite);
        return section;
    }

    /**
     * Add a change listener to the modules table
     * @param listener the listener.
     */
    @objid ("a73f82bc-33f6-11e2-a514-002564c97630")
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        this.modulesTable.addSelectionChangedListener(listener);
    }

    @objid ("e4304706-ec48-46c1-a1f2-11ad6bb24d90")
    ProjectModel getProjectAdapter() {
        return ModulesSection.this.projectAdapter;
    }

    @objid ("73ae9e26-5177-4dd7-ae30-f900337b3c94")
    protected void setModuleSelectionsInCatalog(IStructuredSelection structuredSelection) {
        this.moduleSelectionsInCatalog = structuredSelection;
        if (getProjectAdapter().isLocalProject()) {
            Object[] objs = structuredSelection.toArray();
            for (Object obj : objs) {
                if (obj instanceof IModuleHandle) {
                    this.addButton.setEnabled(true);
                    this.removeButton.setEnabled(false);
                    break;
                }
                this.addButton.setEnabled(false);
            }
        }
    }

    @objid ("c173a2ce-321c-4b2b-bfca-d1c894e64f35")
    Collection<IModuleHandle> getModulesToAdd() {
        Map<String, IModuleHandle> modulesToAdd = new HashMap<>();
        for (Object obj : this.moduleSelectionsInCatalog.toList()) {
            if (obj instanceof IModuleHandle) {
                IModuleHandle module = (IModuleHandle) obj;
                IModuleHandle moduleToCompare = modulesToAdd.get(module.getName());
                if (moduleToCompare == null || module.getVersion().isNewerThan(moduleToCompare.getVersion())) {
                    modulesToAdd.put(module.getName(), module);
                }
            }
        }
        
        addMissingDependencies(modulesToAdd);
        
        return modulesToAdd.values();
        //return ModuleSorter.sortHandles(modulesToAdd.values());
    }

    @objid ("a195e2c5-5dfa-4cca-9cc3-1a2e9ca023e5")
    ModuleCatalogPanel createModuleCatalogPanel(Composite parent) {
        ModuleCatalogPanel panel = new ModuleCatalogPanel(this.env);
        panel.createPanel(parent);
        
        panel.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                ModulesSection.this.catalogController.onModuleSelection(selection);
            }
        });
        
        panel.getViewer().addSelectionChangedListener(new ISelectionChangedListener() {
        
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Select module(s) to add
                ISelection selection = event.getSelection();
                if (selection instanceof IStructuredSelection) {
                    IStructuredSelection structuredSelection = (IStructuredSelection) selection;
                    ModulesSection.this.setModuleSelectionsInCatalog(structuredSelection);
                }
            }
        });
        
        panel.addDoubleClickListener(new IDoubleClickListener() {
            
            @Override
            public void doubleClick(DoubleClickEvent event) {
                addSelectedModules();
            }
        });
        this.catalogController.init();
        return panel;
    }

    @objid ("1388620e-79d6-41bc-b84c-5052d29a0e82")
    protected void addSelectedModules() {
        IRunnableWithProgress runnable = new IRunnableWithProgress() {
            
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                final Collection<IModuleHandle> modulesToAdd = getModulesToAdd();
                
                if (AppProjectConf.LOG.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Add module(s):\n");//$NON-NLS-1$
                    for (IModuleHandle h : modulesToAdd) {
                        sb.append(" - ");
                        sb.append(h.getName());
                        sb.append("  v");
                        sb.append(h.getVersion());
                        sb.append("  (");
                        sb.append(h.getDependencies().size() + h.getWeakDependencies().size());
                        sb.append("  deps)\n");
                    }
                    AppProjectConf.LOG.debug(sb.toString()); 
                }
                
                IProjectService projectService = ModulesSection.this.applicationContext.get(IProjectService.class);
                GProject openedProject = projectService.getOpenedProject();
                AddModuleHelper.run(openedProject, ModulesSection.this.moduleService, modulesToAdd, monitor, getProjectAdapter());
            }
        };
        try {
            this.progressService.run(false, false, runnable);
            setInput(getProjectAdapter());
        } catch (InvocationTargetException | InterruptedException e) {
            AppProjectConf.LOG.error(e);
        }
    }

    /**
     * Analyze the module list and adds all required modules to it by taking the latest version in the catalog.
     */
    @objid ("b4e655c4-7490-4506-a966-e32523783704")
    private void addMissingDependencies(Map<String, IModuleHandle> modulesToAdd) {
        // Compute all missing dependencies
        List<ModuleId> dependencies = new ArrayList<>();
        for (IModuleHandle module : modulesToAdd.values()) {
            for (ModuleId dep : module.getDependencies()) {
                if (!modulesToAdd.containsKey(dep.getName())) {
                    dependencies.add(dep);
                }
            }
        }
        
        if (!dependencies.isEmpty()) {
            IModuleCatalog catalog = new FileModuleStore(this.env.getModuleCatalogPath());
        
            try {
                for (ModuleId moduleId : dependencies) {
                    final IModuleHandle latestModule = catalog.findModule(moduleId.getName(), null, null);
                    if (latestModule != null) {
                        modulesToAdd.put(moduleId.getName(), latestModule);
                    } else {
                        // At least one module is missing, let the deployment phase pop an error.
                        return;
                    }
                }
        
                // Make sure the module list is complete
                addMissingDependencies(modulesToAdd);
            } catch (IOException e) {
                // log & let the deployment phase pop an error.
                AppProjectConf.LOG.warning(e);
            }
        }
    }

    @objid ("234a564c-79b4-4bfa-a39c-3b0664ac44b3")
    private static class AddModuleHelper {
        @objid ("cdbd5b64-6a82-42f2-8adb-f5bc22324328")
        public static void run(final GProject project, final IModuleService moduleService, Collection<IModuleHandle> modules, IProgressMonitor monitor, ProjectModel projectAdapter) {
            // Sort the module according to their dependencies
            List<IModuleHandle> sortedModules;
            try {
                sortedModules = ModuleSorter.sortHandles(modules);
                
                if (AppProjectConf.LOG.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("AddModuleHelper: Installing module(s):\n");//$NON-NLS-1$
                    for (IModuleHandle h : sortedModules) {
                        sb.append(" - ");
                        sb.append(h.getName());
                        sb.append("  v");
                        sb.append(h.getVersion());
                        sb.append("  (");
                        sb.append(h.getDependencies().size() + h.getWeakDependencies().size());
                        sb.append("  deps)\n");
                    }
                    AppProjectConf.LOG.debug(sb.toString()); 
                }
                
            } catch (CyclicDependencyException e) {
                // Error dialog
                AppProjectConf.LOG.debug(e);
                MessageDialog.openError(null, 
                        AppProjectConf.I18N.getString("ModulesSection.ModuleInstallationErrorTitle"), 
                        e.getLocalizedMessage());
                return;
            }
            
            int sum = sortedModules.size();
            monitor.beginTask(AppProjectConf.I18N.getString("ModulesSection.AddModulesProgressTitle"), sum);
            List<String> invalidIds = getExistFragmentIdList(projectAdapter);
            for (int i = 0; i < sum; i++) {
                final IModuleHandle module = sortedModules.get(i);
                if (invalidIds.contains(module.getName())) {
                    MessageDialog.openError(null, AppProjectConf.I18N.getString("ModulesSection.ModuleInstallationErrorTitle"), 
                            AppProjectConf.I18N.getMessage("ModulesSection.ModuleInstallationErrorMessage.NameExistAlready", module.getName()));
                    return;
                }
                
                monitor.subTask(AppProjectConf.I18N.getMessage("ModulesSection.AddModulesProgressSubTask", String.valueOf(i+1), String.valueOf(sum), module.getName()));
                monitor.worked(1);
                
                try (ITransaction t = project.getSession().getTransactionSupport().createTransaction("install a module")) { //$NON-NLS-1$
                    moduleService.installModule(project, module.getArchive());
                    t.commit();
                } catch (ModuleException e) {
                    // Error dialog
                    AppProjectConf.LOG.debug(e);
                    MessageDialog.openError(null, AppProjectConf.I18N.getString("ModulesSection.ModuleInstallationErrorTitle"), e.getMessage());
                }   
            }
            monitor.done();
        }

        /**
         * get existing model fragment identifiers, excepted MDA ones.
         * @param projectAdapter a project
         * @return existing model fragments identifiers
         */
        @objid ("9ce64677-493c-4635-b691-3df4248a08ce")
        private static List<String> getExistFragmentIdList(ProjectModel projectAdapter) {
            List<String> fragmentIds = new ArrayList<>();
            List<IProjectFragment> fragments = projectAdapter.getAllFragments();
            for (IProjectFragment fragment : fragments) {
                if (fragment.getType() != FragmentType.MDA) {                    
                    fragmentIds.add(fragment.getId());
                }
            }
            return fragmentIds;
        }

    }

    @objid ("ea13c05a-4bf4-44c8-8f4b-5e2fafe38f0a")
    private class CatalogController {
        @objid ("43d7b185-b38b-4d21-89ba-d98d0cc628d3")
        public CatalogController() {
            //
        }

        @objid ("99ea9b22-1f21-4459-b70a-a73fb35a98fc")
        public void init() {
            //
        }

        @objid ("f2f3936b-7e40-4d97-aab6-f424f4cae408")
        public void onModuleSelection(IStructuredSelection selection) {
            List<IModuleHandle> selectedModules = new ArrayList<>();
            for (Object obj : selection.toList()) {
                if (obj instanceof IModuleHandle) {
                    selectedModules.add((IModuleHandle) obj);
                }
            }
        }

    }

}
