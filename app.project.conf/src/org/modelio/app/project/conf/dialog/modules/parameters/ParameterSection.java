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
                                    

package org.modelio.app.project.conf.dialog.modules.parameters;

import java.util.Arrays;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.modelio.api.module.IModule.ModuleRuntimeState;
import org.modelio.api.module.IModule;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.gproject.module.GModule;
import org.modelio.mda.infra.service.IModuleService;

/**
 * Manage the modules parameters.
 */
@objid ("a741094c-33f6-11e2-a514-002564c97630")
public class ParameterSection {
    @objid ("a741094e-33f6-11e2-a514-002564c97630")
    protected IEclipseContext applicationContext;

    @objid ("a741094f-33f6-11e2-a514-002564c97630")
    protected TreeViewer parameterViewer;

    @objid ("a7410950-33f6-11e2-a514-002564c97630")
    public ParameterSection(IEclipseContext application) {
        this.applicationContext = application;
    }

    @objid ("a7410953-33f6-11e2-a514-002564c97630")
    public void setInput(GModule module) {
        IModuleService moduleService = this.applicationContext.get(IModuleService.class);
        
        // Get the runtime module from the static one
        if (module != null && module.getModuleElement() != null) {
            IModule iModule = moduleService.getIModule(module.getModuleElement());
            if (iModule != null && iModule.getState() == ModuleRuntimeState.Started) {
                if (iModule.getParametersEditionModel() != null) {
                    this.parameterViewer.setInput(Arrays.asList(iModule));
                    return;
                }                    
            }
        }
        this.parameterViewer.setInput(null);
    }

    @objid ("a7410956-33f6-11e2-a514-002564c97630")
    public Section createControls(final FormToolkit toolkit, final Composite parent) {
        final Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE | Section.DESCRIPTION);
        section.setText(AppProjectConf.I18N.getString("ParameterSection.SectionText")); //$NON-NLS-1$
        section.setDescription(AppProjectConf.I18N.getString("ParameterSection.SectionDescription")); //$NON-NLS-1$
        section.setExpanded(true);
        
        final Composite composite = toolkit.createComposite(section, SWT.WRAP);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        
        this.parameterViewer = new TreeViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
        this.parameterViewer.getTree().setHeaderVisible(true);
        this.parameterViewer.getTree().setLinesVisible(true);
        
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.minimumHeight = 200;
        this.parameterViewer.getTree().setLayoutData(gd);
        
        // Create columns
        String[] columnTitles = { AppProjectConf.I18N.getString("ParameterSection.NameColumn"),  AppProjectConf.I18N.getString("ParameterSection.ValueColumn"), AppProjectConf.I18N.getString("ParameterSection.ScopeColumn")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        int[] columnInitialWidths = { 300, 500,  100};
        TreeViewerColumn col1 = createTreeViewerColumn(this.parameterViewer, columnTitles[0], columnInitialWidths[0]);
        col1.setLabelProvider(new NameLabelProvider());
        
        TreeViewerColumn col2 = createTreeViewerColumn(this.parameterViewer, columnTitles[1], columnInitialWidths[1]);
        col2.setLabelProvider(new ValueLabelProvider());
        col2.setEditingSupport(new ParametersEditingSupport(this.parameterViewer));
        
        TreeViewerColumn col3 = createTreeViewerColumn(this.parameterViewer, columnTitles[2], columnInitialWidths[2]);
        col3.setLabelProvider(new ScopeLabelProvider());
        
        this.parameterViewer.setContentProvider(new ParametersContentProvider());
        this.parameterViewer.setAutoExpandLevel(2);
        this.parameterViewer.setInput(null);
        
        toolkit.paintBordersFor(composite);
        section.setClient(composite);
        return section;
    }

    @objid ("f717767c-3a39-11e2-90eb-002564c97630")
    private static TreeViewerColumn createTreeViewerColumn(TreeViewer viewer, String title, int bound) {
        final TreeViewerColumn column = new TreeViewerColumn(viewer, SWT.CENTER);
        column.getColumn().setText(title);
        column.getColumn().setWidth(bound);
        column.getColumn().setResizable(true);
        column.getColumn().setMoveable(true);
        return column;
    }

    @objid ("46ed76dc-5790-4d6b-8c87-5a29794499a5")
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        this.parameterViewer.addSelectionChangedListener(listener);
    }

}
