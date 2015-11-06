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
                                    

package org.modelio.app.project.conf.dialog;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.modelio.app.core.ModelioEnv;
import org.modelio.app.project.conf.dialog.audit.AuditPage;
import org.modelio.app.project.conf.dialog.libraries.LibrariesPage;
import org.modelio.app.project.conf.dialog.modules.ModulesPage;
import org.modelio.app.project.conf.dialog.projectinfo.ProjectInfosPage;
import org.modelio.app.project.conf.dialog.urls.ReferencedUrlsPage;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.audit.service.IAuditService;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.gproject.GProject;

/**
 * Project configuration dialog box.
 * <p>It mainly consists of a {@link TabFolder} displaying all the different parts of a project configuration.<br/>
 * The default configuration configuration contains:
 * <ul>
 * <li>{@link ProjectInfosPage}.</li>
 * <li>{@link ModulesPage}</li>
 * <li>{@link WorkModelPage}</li>
 * <li>{@link LibrariesPage}</li>
 * <li>{@link AuditPage}</li>
 * </ul>
 * </p>
 * <p>This class is meant to be extended, the page list being customized via the {@link ProjectConfigurationDialog#addPages(TabFolder)} method.</p>
 */
@objid ("a7410963-33f6-11e2-a514-002564c97630")
public class ProjectConfigurationDialog extends ModelioDialog {
    @objid ("ae6fecce-4381-11e2-b513-002564c97630")
    public static final String PROJECT_INFOS = "PROJECT_INFOS";

    @objid ("ae6fecd2-4381-11e2-b513-002564c97630")
    public static final String MODULES = "MODULES";

    @objid ("ae6fecda-4381-11e2-b513-002564c97630")
    public static final String LIBRARIES = "LIBRARIES";

    @objid ("ae6fecde-4381-11e2-b513-002564c97630")
    public static final String AUDIT = "AUDIT";

    @objid ("ae6fece5-4381-11e2-b513-002564c97630")
    private String pageToSelect;

    @objid ("05cffd92-aa80-46c2-8475-59a091b50e72")
    public static final String REFERENCED_URLS = "REFERENCED_URLS";

    @objid ("050e8817-e7f6-4819-891c-6f2f06657a23")
    private static final String CONFPAGE_EXTENSION_ID = "org.modelio.app.project.conf.page";

    @objid ("ca31165d-c15f-4e1b-ba35-983dc68aafc9")
    private static final String PAGE_DATAKEY = "page";

    @objid ("3349d049-0176-4aed-a249-838ef9ba5ae9")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_managing_projects_configuring_project_informations.html";

    @objid ("a7410967-33f6-11e2-a514-002564c97630")
    protected ProjectModel model;

    @objid ("a46659b7-3f86-4c7a-be34-ca59aa7ec36b")
    private IAuditService auditService;

    @objid ("1e00c95b-7dd0-4065-b987-723a4c3b03ca")
    private MApplication application;

    @objid ("5d149ea0-0088-4b53-bf4c-ba4e318d499c")
    private FormToolkit toolkit;

    @objid ("5ddb8fdf-c047-4e47-8ba6-c897e561addd")
    protected TabFolder tabFolder;

    @objid ("8ab5cdbd-b041-4c4b-82d9-47512dd66be4")
    private ModelioEnv env;

    @objid ("a7410968-33f6-11e2-a514-002564c97630")
    public ProjectConfigurationDialog(MApplication application, GProject openedProject, Shell parentShell, IAuditService auditService, ModelioEnv env) {
        super(parentShell);
        this.application = application;
        this.auditService = auditService;
        this.model = new ProjectModel(openedProject);
        this.toolkit = new FormToolkit(parentShell.getDisplay());
        this.env = env;
    }

    @objid ("a741096d-33f6-11e2-a514-002564c97630")
    @Override
    public final Control createContentArea(final Composite parent) {
        this.tabFolder = new TabFolder(parent, SWT.NONE);
        this.tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        // no layout to add on the tab folder.
        
        addPages();
        return null;
    }

    @objid ("a7410974-33f6-11e2-a514-002564c97630")
    @Override
    public void init() {
        setLogoImage(null);
        // Put the messages in the banner area
        getShell().setText(AppProjectConf.I18N.getString("ProjectConfiguration.BoxTitle")); //$NON-NLS-1$
        setTitle(AppProjectConf.I18N.getString("ProjectConfiguration.Title")); //$NON-NLS-1$
        setMessage(AppProjectConf.I18N.getString("ProjectConfiguration.Subtitle")); //$NON-NLS-1$
    }

    @objid ("a7410977-33f6-11e2-a514-002564c97630")
    @Override
    public final void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
    }

    @objid ("a1616d40-3d2f-11e2-97a2-002564c97630")
    protected void addPages() {
        // Project infos page
        addProjectInfoPage();
        
        // Work model page
        addExtensionPage();
        
        // Libraries page
        addLibrariesPage();
        
        // Modules page
        addModulesPage();
        
        // Audit configuration page
        addAuditConfigurationPage();
        
        // Referenced URL page
        addReferencedUrlPage();
    }

    @objid ("a1616d43-3d2f-11e2-97a2-002564c97630")
    protected void addAuditConfigurationPage() {
        AuditPage auditPage = new AuditPage();
        Control auditControl = auditPage.createControls(this.toolkit, this.application,this.auditService,this.tabFolder);
        auditPage.setInput(this.model);
        
        TabItem auditTab = new TabItem(this.tabFolder, SWT.NONE);
        auditTab.setText(AppProjectConf.I18N.getString("ProjectConfiguration.AuditPage")); //$NON-NLS-1$
        auditTab.setControl(auditControl);
        
        if (AUDIT.equals(this.pageToSelect)) {
            this.tabFolder.setSelection(auditTab);
        }
    }

    @objid ("a1619452-3d2f-11e2-97a2-002564c97630")
    protected final void addModulesPage() {
        ModulesPage modulesPage = new ModulesPage();
        Control modulesControl = modulesPage.createControls(this.toolkit, this.application, this.tabFolder, this.env);
        modulesPage.setInput(this.model);
        
        TabItem moduleTab = new TabItem(this.tabFolder, SWT.NONE);
        moduleTab.setText(AppProjectConf.I18N.getString("ProjectConfiguration.ModulesPage")); //$NON-NLS-1$
        moduleTab.setControl(modulesControl);
        
        if (MODULES.equals(this.pageToSelect)) {
            this.tabFolder.setSelection(moduleTab);
        }
    }

    @objid ("a1619455-3d2f-11e2-97a2-002564c97630")
    protected final void addLibrariesPage() {
        LibrariesPage librariesPage = new LibrariesPage();
        Control librariesControl = librariesPage.createControls(this.toolkit, this.application, this.tabFolder);
        librariesPage.setInput(this.model);
        
        TabItem librariesTab = new TabItem(this.tabFolder, SWT.NONE);
        librariesTab.setText(AppProjectConf.I18N.getString("ProjectConfiguration.LibrariesPage")); //$NON-NLS-1$
        librariesTab.setControl(librariesControl);
        
        if (LIBRARIES.equals(this.pageToSelect)) {
            this.tabFolder.setSelection(librariesTab);
        }
    }

    @objid ("a161bb64-3d2f-11e2-97a2-002564c97630")
    protected final void addProjectInfoPage() {
        final ProjectInfosPage infosPage = new ProjectInfosPage();
        Control infosControl = infosPage.createControls(this.toolkit, this.application, this.tabFolder);
        infosPage.setInput(this.model);
        
        final TabItem infoTab = new TabItem(this.tabFolder, SWT.NONE);
        infoTab.setText(AppProjectConf.I18N.getString("ProjectConfiguration.InfosPage")); //$NON-NLS-1$
        infoTab.setControl(infosControl);
        
        this.tabFolder.addSelectionListener(new SelectionListener() {
            
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (ProjectConfigurationDialog.this.tabFolder.getSelection()[0]==infoTab) {
                    infosPage.setInput(ProjectConfigurationDialog.this.model);  // refresh info page when selecting info tab
                }
            }
            
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                //               
            }
        });
        if (PROJECT_INFOS.equals(this.pageToSelect)) {
            this.tabFolder.setSelection(infoTab);
        }
    }

    /**
     * Select a specific page in the project configurator.
     * See constants for available page names.
     * @param page the page to select.
     */
    @objid ("ae61a48e-4381-11e2-b513-002564c97630")
    public void setSelectedPage(String page) {
        this.pageToSelect = page;
    }

    @objid ("51a62d00-4510-11e2-a5d7-bc305ba4815c")
    @Override
    protected Point getInitialSize() {
        return new Point (1024, 768);
    }

    @objid ("1782d137-4270-479b-bb7d-a944f51edf19")
    protected void addReferencedUrlPage() {
        ReferencedUrlsPage referencedUrlsPage = new ReferencedUrlsPage();
        Control referencedUrlControl = referencedUrlsPage.createControls(this.toolkit, this.tabFolder);
        referencedUrlsPage.setInput(this.model);
        
        TabItem referencedUrlsTab = new TabItem(this.tabFolder, SWT.NONE);
        referencedUrlsTab.setText(AppProjectConf.I18N.getString("ProjectConfiguration.ReferencedUrlsPage")); //$NON-NLS-1$
        referencedUrlsTab.setControl(referencedUrlControl);
        
        if (REFERENCED_URLS.equals(this.pageToSelect)) {
            this.tabFolder.setSelection(referencedUrlsTab);
        }
    }

    @objid ("b138f564-25a3-4e30-a4a1-13227e137031")
    protected final void addExtensionPage() {
        IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(CONFPAGE_EXTENSION_ID);
        for (IConfigurationElement e : config) {
            if (e.getName().equals("confpage")) {
                parseConfPage(this, e);
            }        
        }
    }

    @objid ("6bea975f-1139-4ecf-8f3b-91b543f30410")
    protected void parseConfPage(ProjectConfigurationDialog dlg, IConfigurationElement elt) {
        String label = elt.getAttribute("label");
        String name = elt.getAttribute("name");
        Object page;
        try {
            page = elt.createExecutableExtension("page");
        
            if (page instanceof IProjectConfPage) {
                dlg.createConfPage(label, name, (IProjectConfPage)page);
            }
        
        } catch (CoreException e) {
            AppProjectConf.LOG.error(e);
        }
    }

    @objid ("d838070f-b8f3-4e34-8176-7446b4ae5d91")
    public void createConfPage(String label, String name, IProjectConfPage page) {
        page.createControls(this.toolkit, this.application, this.tabFolder);
        page.setInput(this.model);
        final TabItem tabItem = new TabItem(this.tabFolder, SWT.NULL);
        tabItem.setText(label);
        tabItem.setControl(page.getControl());
        tabItem.setData(PAGE_DATAKEY, page);
        if (name.equals(this.pageToSelect)) {
            this.tabFolder.setSelection(tabItem);
        }
    }

    @objid ("5aa0afb4-6d15-4926-847c-62d2aba7e950")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

}
