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
                                    

package org.modelio.app.project.conf.dialog.libraries.local;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.modelio.app.project.conf.dialog.ProjectModel;
import org.modelio.app.project.conf.dialog.common.ColumnHelper;
import org.modelio.app.project.conf.dialog.common.ScopeHelper;
import org.modelio.app.project.conf.dialog.libraries.local.property.RamcPropertyDialog;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.images.FragmentImageService;
import org.modelio.gproject.data.project.DefinitionScope;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.ramc.IModelComponentInfos;
import org.modelio.gproject.data.ramc.ModelComponentArchive;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.fragment.ramcfile.RamcFileFragment;
import org.modelio.gproject.gproject.FragmentConflictException;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vbasic.net.UriPathAccess;

/**
 * Manage the local libraries section.
 * <p>
 * Call {@link LocalLibrariesSection#createControls(FormToolkit, Composite)} and {@link LocalLibrariesSection#setInput(ProjectModel)} to use it.
 * </p>
 */
@objid ("7d53580e-3adc-11e2-916e-002564c97630")
public class LocalLibrariesSection {
    /**
     * The project that is currently being displayed by the section.
     */
    @objid ("7d535810-3adc-11e2-916e-002564c97630")
     ProjectModel projectAdapter;

    @objid ("acef8a6c-eb10-4fc1-87d1-4076fd99b03a")
     IEclipseContext applicationContext;

    @objid ("07f8aed4-e4fe-40bc-90f9-780d55328570")
    private TableViewer viewer;

    @objid ("b0dea301-52fb-45ce-84cb-da490f703d56")
    private Button addBtn;

    @objid ("6949b41c-15d4-4b80-9d9a-4c4e53cefc73")
    private Button removeBtn;

    @objid ("4fe4256f-364e-4595-9dff-eda64001242b")
    private Button showFragmentBtn;

    @objid ("600bcfa9-0a29-4c68-8c03-bd373fad32b2")
    private Button upgradeFragmentBtn;

    @objid ("7d535818-3adc-11e2-916e-002564c97630")
    public LocalLibrariesSection(IEclipseContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Update() is called by the project infos view when the project to be
     * displayed changes or need contents refresh
     * @param selectedProject the project selected in the workspace tree view
     */
    @objid ("7d53581b-3adc-11e2-916e-002564c97630")
    public void setInput(ProjectModel selectedProject) {
        this.projectAdapter = selectedProject;
        
        if (selectedProject != null) {
            List<IProjectFragment> sel = getSelectedFragments();
            boolean isFragmentSelected = !sel.isEmpty();
            boolean areLocal = isFragmentSelected && areAllLocalFragments(sel);
        
            this.viewer.setInput(selectedProject.getLocalLibraryFragments());
            this.addBtn.setEnabled(true && this.projectAdapter.isLocalProject());
            this.upgradeFragmentBtn.setEnabled(areLocal && this.projectAdapter.isLocalProject());
            this.removeBtn.setEnabled(areLocal && this.projectAdapter.isLocalProject());
            this.showFragmentBtn.setEnabled(isFragmentSelected);
        } else {
            this.viewer.setInput(new Object[0]);
            this.addBtn.setEnabled(false);
            this.upgradeFragmentBtn.setEnabled(false);
            this.removeBtn.setEnabled(false);
            this.showFragmentBtn.setEnabled(false);
        }
        
        for (TableColumn col : this.viewer.getTable().getColumns()) {
            col.pack();
        }
    }

    @objid ("7d53581f-3adc-11e2-916e-002564c97630")
    public Section createControls(final FormToolkit toolkit, final Composite parent) {
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | Section.DESCRIPTION);
        section.setText(AppProjectConf.I18N.getString("LocalLibrariesSection.SectionText"));  //$NON-NLS-1$
        section.setDescription(AppProjectConf.I18N.getString("LocalLibrariesSection.SectionDescription")); //$NON-NLS-1$
        section.setExpanded(true);
        
        Composite composite = toolkit.createComposite(section, SWT.WRAP);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        
        Table table = toolkit.createTable(composite, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        this.viewer = new TableViewer(table);
        
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(gd);
        
        this.viewer.setContentProvider(new ArrayContentProvider());
        ColumnViewerToolTipSupport.enableFor(this.viewer);
        
        // Name column
        @SuppressWarnings("unused")
        TableViewerColumn nameColumn = ColumnHelper.createFragmentNameColumn(this.viewer);
        
        // Scope column
        @SuppressWarnings("unused")
        TableViewerColumn scopeColumn = ColumnHelper.createFragmentScopeColumn(this.viewer);
        
        // Metamodel version column
        @SuppressWarnings("unused")
        TableViewerColumn mmVer = ColumnHelper.createFragmentMmVersionColumn(this.viewer);
        
        
        this.viewer.setInput(null);
        
        this.viewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // launch a full refresh of the section
                setInput(getProjectAdapter());
            }
        });
        
        // The buttons composite
        Composite panel = toolkit.createComposite(composite, SWT.NONE);
        GridData gd2 = new GridData(SWT.FILL, SWT.FILL, false, false);
        panel.setLayoutData(gd2);
        
        RowLayout rowLayout = new RowLayout();
        rowLayout.wrap = false;
        rowLayout.pack = false;
        rowLayout.justify = false;
        rowLayout.type = SWT.VERTICAL;
        rowLayout.marginLeft = 2;
        rowLayout.marginTop = 2;
        rowLayout.marginRight = 2;
        rowLayout.marginBottom = 2;
        rowLayout.spacing = 1;
        
        panel.setLayout(rowLayout);
        
        // The add button
        this.addBtn = toolkit.createButton(panel, AppProjectConf.I18N.getString("LocalLibrariesSection.AddLibrary"), SWT.PUSH); //$NON-NLS-1$
        this.addBtn.addSelectionListener(new AddFragmentButtonListener());
        
        // The upgrade button
        this.upgradeFragmentBtn = toolkit.createButton(panel, AppProjectConf.I18N.getString("LocalLibrariesSection.Upgrade"), SWT.PUSH); //$NON-NLS-1$
        this.upgradeFragmentBtn.setEnabled(false);
        this.upgradeFragmentBtn.addSelectionListener(new UpgradeFragmentButtonListener());
        
        // The show fragment (properties) button
        this.showFragmentBtn = toolkit.createButton(panel, AppProjectConf.I18N.getString("LocalLibrariesSection.Edit"), SWT.PUSH); //$NON-NLS-1$
        this.showFragmentBtn.setEnabled(false);
        this.showFragmentBtn.addSelectionListener(new ShowFragmentPropertiesButtonListener());
        
        // The delete button
        this.removeBtn = toolkit.createButton(panel, AppProjectConf.I18N.getString("LocalLibrariesSection.Delete"), SWT.PUSH); //$NON-NLS-1$
        this.removeBtn.setEnabled(false);
        this.removeBtn.addSelectionListener(new RemoveFragmentButtonListener());
        
        toolkit.paintBordersFor(composite);
        section.setClient(composite);
        return section;
    }

    @objid ("7d535827-3adc-11e2-916e-002564c97630")
    TableViewer getViewer() {
        return this.viewer;
    }

    @objid ("7d53582b-3adc-11e2-916e-002564c97630")
    ProjectModel getProjectAdapter() {
        return LocalLibrariesSection.this.projectAdapter;
    }

    @objid ("454669e7-3df9-11e2-b413-002564c97630")
    protected void refresh() {
        setInput(this.projectAdapter);
    }

    @objid ("c1941d10-88f4-41a4-98a2-ae2d4e665f1c")
    @SuppressWarnings("unchecked")
    public List<IProjectFragment> getSelectedFragments() {
        IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
        return selection.toList();
    }

    @objid ("4744039c-1850-4f27-af83-34728d315819")
    private boolean areAllLocalFragments(List<IProjectFragment> sel) {
        for (IProjectFragment f : sel) {
            if (f.getScope()==DefinitionScope.SHARED)
                return false;
        }
        return true;
    }

    @objid ("0a798463-1b10-42e3-ac32-4760e82643e8")
    void installExportedFilesOfFragment(final ModelComponentArchive modelComponentArchive) {
        Shell shell = getViewer().getControl().getShell();
        IRunnableWithProgress runnable1 = new IRunnableWithProgress() {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {                        
                try {
                    Path deploymentPath = LocalLibrariesSection.this.projectAdapter.getPath();
                    modelComponentArchive.installExportedFiles(deploymentPath, null);
                } catch (IOException er) {
                    AppProjectConf.LOG.error(er);
                }
            }
        };
        try {
            new ProgressMonitorDialog(shell).run(true, false, runnable1);
            refresh();
        } catch (InvocationTargetException ex) {
            AppProjectConf.LOG.error(ex);
            MessageDialog.openError(shell, AppProjectConf.I18N.getString("Error"), ex.getCause().getLocalizedMessage()); //$NON-NLS-1$
        } catch (InterruptedException ex) {
            // nothing
        }
    }

    @objid ("6072cf04-eb91-4635-8b00-3580e9cd9edd")
    void removeExportedFilesOfFragment(IProjectFragment fragment) throws IOException {
        try(UriPathAccess acc = new UriPathAccess(fragment.getUri(), fragment.getAuthConfiguration().getAuthData())) {
            Path archivePath = acc.getPath();
        
            ModelComponentArchive modelComponentArchive = new ModelComponentArchive(archivePath, true);
            Path deploymentPath = LocalLibrariesSection.this.projectAdapter.getPath();
            modelComponentArchive.removeExportedFiles(deploymentPath, null);
        }
    }

    @objid ("7d53582f-3adc-11e2-916e-002564c97630")
    private class AddFragmentButtonListener extends SelectionAdapter {
        @objid ("7d535830-3adc-11e2-916e-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            AppProjectConf.LOG.debug("add ramc fragment"); //$NON-NLS-1$
            
            final IProjectService projectService = LocalLibrariesSection.this.applicationContext.get(IProjectService.class);
            
            Shell shell = getViewer().getControl().getShell();
            
            AddLocalLibraryDialog dlg = new AddLocalLibraryDialog(shell, getProjectAdapter());
            dlg.open();
            
            final ModelComponentArchive modelComponentArchive = dlg.getModelComponentArchive();
            final FragmentDescriptor fragmentDescriptor = dlg.getFragmentDescriptor();
            if (fragmentDescriptor != null) {
            
                IRunnableWithProgress runnable = new IRunnableWithProgress() {
            
                    @Override
                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        try {
                            projectService.addFragment(getProjectAdapter().getOpenedProject(), fragmentDescriptor, monitor);
                        } catch (FragmentConflictException ex) {
                            throw new InvocationTargetException(ex, ex.getLocalizedMessage());
                        }
                    }
                };
            
                try {
                    new ProgressMonitorDialog(shell).run(true, false, runnable);
            
                } catch (InvocationTargetException ex) {
                    AppProjectConf.LOG.error(ex);
                    MessageDialog.openError(shell, AppProjectConf.I18N.getString("Error"), ex.getCause().getLocalizedMessage()); //$NON-NLS-1$
                } catch (InterruptedException ex) {
                    // nothing
                }
                installExportedFilesOfFragment(modelComponentArchive);
                refresh();
            }
        }

        @objid ("7d55b94b-3adc-11e2-916e-002564c97630")
        AddFragmentButtonListener() {
            // empty
        }

    }

    @objid ("7d55b94d-3adc-11e2-916e-002564c97630")
    private class RemoveFragmentButtonListener extends SelectionAdapter {
        @objid ("7d55b94e-3adc-11e2-916e-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            AppProjectConf.LOG.debug("remove ramc fragment"); //$NON-NLS-1$
            
            IProjectService projectService = LocalLibrariesSection.this.applicationContext.get(IProjectService.class);
            
            if (getViewer().getSelection().isEmpty()) {
                return;
            }
            
            IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
            for (Object obj : selection.toList()) {
                IProjectFragment fragment = (IProjectFragment) obj;
                try {
                    removeExportedFilesOfFragment(fragment);
                } catch (IOException error) {
                    AppProjectConf.LOG.error(error);
                }
                projectService.removeFragment(getProjectAdapter().getOpenedProject(), fragment);
                refresh();
            }
        }

        @objid ("7d55b952-3adc-11e2-916e-002564c97630")
        RemoveFragmentButtonListener() {
            // empty
        }

    }

    @objid ("7d55b954-3adc-11e2-916e-002564c97630")
    private class UpgradeFragmentButtonListener extends SelectionAdapter {
        @objid ("7d55b955-3adc-11e2-916e-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            AppProjectConf.LOG.debug("Upgrade ramc fragment"); //$NON-NLS-1$
            
            final IProjectService projectService = LocalLibrariesSection.this.applicationContext.get(IProjectService.class);
            
            Shell shell = getViewer().getControl().getShell();
            
            AddLocalLibraryDialog dlg = new AddLocalLibraryDialog(shell, getProjectAdapter());
            dlg.open();
            
            final ModelComponentArchive newArchive = dlg.getModelComponentArchive();
            
            final FragmentDescriptor fragmentDescriptor = dlg.getFragmentDescriptor();
            if (fragmentDescriptor != null) {
                // UPDATE: remove the old one, then add the new one
                // 1. remove the fragment selected to update
                IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
                for (Object obj : selection.toList()) {
                    try {                        
                        IProjectFragment fragment = (IProjectFragment) obj;
                        removeExportedFilesOfFragment(fragment);
                        projectService.removeFragment(getProjectAdapter().getOpenedProject(), fragment);
                        refresh();
                    } catch (IOException error) {
                        AppProjectConf.LOG.error(error);
                    }                     
                    // 2. add the new fragment 
                    IRunnableWithProgress runnable = new IRunnableWithProgress() {
                        
                        @Override
                        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                            try {
                                projectService.addFragment(getProjectAdapter().getOpenedProject(), fragmentDescriptor, monitor);
                            } catch (FragmentConflictException ex) {
                                throw new InvocationTargetException(ex, ex.getLocalizedMessage());
                            }
                        }
                    };
                    
                    try {
                        new ProgressMonitorDialog(shell).run(true, false, runnable); 
                        refresh();
                    } catch (InvocationTargetException ex) {
                        AppProjectConf.LOG.error(ex);
                        MessageDialog.openError(shell, AppProjectConf.I18N.getString("Error"), ex.getCause().getLocalizedMessage()); //$NON-NLS-1$
                    } catch (InterruptedException ex) {
                        // nothing
                    }
                    
                    installExportedFilesOfFragment(newArchive);
                }
                
            }
        }

        @objid ("7d55b959-3adc-11e2-916e-002564c97630")
        UpgradeFragmentButtonListener() {
            // empty
        }

    }

    @objid ("7d55b95b-3adc-11e2-916e-002564c97630")
    private class ShowFragmentPropertiesButtonListener extends SelectionAdapter {
        @objid ("7d55b95c-3adc-11e2-916e-002564c97630")
        ShowFragmentPropertiesButtonListener() {
            // empty
        }

        @objid ("7d55b95e-3adc-11e2-916e-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            AppProjectConf.LOG.debug("show ramc fragment properties"); //$NON-NLS-1$
            
            if (getViewer().getSelection().isEmpty()) {
                return;
            }
            
            IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
            
            RamcFileFragment fragment = (RamcFileFragment) selection.getFirstElement();
            
            try {
                IModelComponentInfos fragmentInfos = fragment.getInformations();
                Shell shell = getViewer().getControl().getShell();
            
                if (fragmentInfos != null) {                    
                    RamcPropertyDialog propertyDialog = new RamcPropertyDialog(shell, fragmentInfos, getProjectAdapter());          
                    propertyDialog.open();
                }
            } catch (IOException error) {
                AppProjectConf.LOG.error(error);
            }
        }

    }

}
