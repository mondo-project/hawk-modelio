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
                                    

package org.modelio.app.project.conf.dialog.workmodel.local;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.images.FragmentImageService;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.gproject.gproject.FragmentConflictException;
import org.modelio.vbasic.files.FileUtils;

/**
 * Manage the local model section.
 * <p>
 * Call {@link LocalModelSection#createControls(FormToolkit, Composite)} and
 * {@link LocalModelSection#setInput(ProjectModel)} to use it.
 * </p>
 */
@objid ("7d61a03a-3adc-11e2-916e-002564c97630")
public class LocalModelSection {
    /**
     * The project that is currently being displayed by the section.
     */
    @objid ("7d61a03c-3adc-11e2-916e-002564c97630")
    private ProjectModel projectAdapter;

    @objid ("7d61a03e-3adc-11e2-916e-002564c97630")
     IEclipseContext applicationContext;

    @objid ("7d61a03f-3adc-11e2-916e-002564c97630")
    private TableViewer viewer;

    @objid ("7d61a040-3adc-11e2-916e-002564c97630")
    private Button addFragment;

    @objid ("7d61a041-3adc-11e2-916e-002564c97630")
    private Button removeFragment;

    @objid ("7d61a042-3adc-11e2-916e-002564c97630")
    public LocalModelSection(IEclipseContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Update() is called by the project infos view when the project to be
     * displayed changes or need contents refresh
     * @param selectedProject the project selected in the workspace tree view
     */
    @objid ("7d61a045-3adc-11e2-916e-002564c97630")
    public void setInput(ProjectModel selectedProject) {
        this.projectAdapter = selectedProject;
        
        if (selectedProject != null) {
            boolean isFragmentSelected = !this.viewer.getSelection().isEmpty();
        
            this.viewer.setInput(selectedProject.getLocalFragments());
            this.addFragment.setEnabled(true && this.projectAdapter.isLocalProject());
            this.removeFragment.setEnabled(isFragmentSelected && this.projectAdapter.isLocalProject());
        } else {
            this.viewer.setInput(new Object[0]);
            this.addFragment.setEnabled(false);
            this.removeFragment.setEnabled(false);
        }
        
        for (TableColumn col : this.viewer.getTable().getColumns()) {
            col.pack();
        }
    }

    @objid ("7d61a049-3adc-11e2-916e-002564c97630")
    public Section createControls(final FormToolkit toolkit, final Composite parent) {
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE
                | Section.DESCRIPTION);
        section.setText(AppProjectConf.I18N.getString("LocalModelSection.SectionText")); //$NON-NLS-1$
        section.setDescription(AppProjectConf.I18N.getString("LocalModelSection.SectionDescription")); //$NON-NLS-1$
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
        
        /*TableViewerColumn uriColumn = new TableViewerColumn(this.viewer, SWT.NONE);
        uriColumn.getColumn().setWidth(300);
        uriColumn.getColumn().setResizable(true);
        uriColumn.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                URI uri = null;
                if (element instanceof IProjectFragment) {
                    uri = ((IProjectFragment) element).getUri();
                }
                return uri != null ? uri.toString().replaceAll("%20", " ") : ""; //$NON-NLS-1$
            }
        
            @Override
            public Image getImage(Object element) {
                return null;
            }
        });*/
        
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
        this.addFragment = toolkit.createButton(panel, AppProjectConf.I18N.getString("LocalModelSection.AddLocalModel"), SWT.PUSH); //$NON-NLS-1$
        this.addFragment.addSelectionListener(new AddFragmentButtonListener());
        
        // The delete button
        this.removeFragment = toolkit.createButton(panel, AppProjectConf.I18N.getString("LocalModelSection.Delete"), SWT.PUSH); //$NON-NLS-1$
        this.removeFragment.setEnabled(false);
        this.removeFragment.addSelectionListener(new RemoveFragmentButtonListener());
        
        toolkit.paintBordersFor(composite);
        section.setClient(composite);
        return section;
    }

    @objid ("7d61a051-3adc-11e2-916e-002564c97630")
    TableViewer getViewer() {
        return this.viewer;
    }

    @objid ("7d61a055-3adc-11e2-916e-002564c97630")
    ProjectModel getProjectAdapter() {
        return LocalModelSection.this.projectAdapter;
    }

    @objid ("4541a725-3df9-11e2-b413-002564c97630")
    protected void refresh() {
        setInput(this.projectAdapter);
    }

    @objid ("7d61a059-3adc-11e2-916e-002564c97630")
    private class AddFragmentButtonListener extends SelectionAdapter {
        @objid ("7d61a05a-3adc-11e2-916e-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            AppProjectConf.LOG.debug("add exml fragment"); //$NON-NLS-1$
            
            final IProjectService projectService = LocalModelSection.this.applicationContext.get(IProjectService.class);
            
            Shell shell = getViewer().getControl().getShell();
            
            AddLocalModelDialog dlg = new AddLocalModelDialog(shell, getProjectAdapter().getFragmentIdList());
            dlg.open();
            
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
                    refresh();
                } catch (InvocationTargetException ex) {
                    AppProjectConf.LOG.error(ex);
                    MessageDialog.openError(shell, AppProjectConf.I18N.getString("Error"), ex.getCause().getLocalizedMessage()); //$NON-NLS-1$
                } catch (InterruptedException ex) {
                    // nothing
                }
            
            }
        }

        @objid ("7d61a05e-3adc-11e2-916e-002564c97630")
        AddFragmentButtonListener() {
            // empty
        }

    }

    @objid ("7d61a060-3adc-11e2-916e-002564c97630")
    private class RemoveFragmentButtonListener extends SelectionAdapter {
        @objid ("7d61a061-3adc-11e2-916e-002564c97630")
        @Override
        public void widgetSelected(SelectionEvent e) {
            AppProjectConf.LOG.debug("remove exml fragment"); //$NON-NLS-1$
            
            IProjectService projectService = LocalModelSection.this.applicationContext.get(IProjectService.class);
            
            if (getViewer().getSelection().isEmpty()) {
                return;
            }
            
            boolean confirm = MessageDialog.openQuestion(null, AppProjectConf.I18N.getString("LocalModelRemoval.Confirm.Title"),
                    AppProjectConf.I18N.getString("LocalModelRemoval.Confirm.Message"));
            
            if (confirm) {
            
                IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
                for (Object obj : selection.toList()) {
                    projectService.removeFragment(getProjectAdapter().getOpenedProject(), (IProjectFragment) obj);
                }
            }
            refresh();
        }

        @objid ("7d61a065-3adc-11e2-916e-002564c97630")
        RemoveFragmentButtonListener() {
            // empty
        }

    }

}
