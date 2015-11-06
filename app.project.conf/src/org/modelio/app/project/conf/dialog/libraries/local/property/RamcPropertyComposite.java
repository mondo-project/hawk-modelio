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
                                    

/**
 * 
 */
package org.modelio.app.project.conf.dialog.libraries.local.property;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.project.conf.dialog.ProjectModel;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.gproject.data.ramc.IModelComponentInfos.ExportedFile;
import org.modelio.gproject.data.ramc.IModelComponentInfos;
import org.modelio.ui.UIColor;
import org.modelio.vbasic.version.Version;

/**
 * Ramc Property Composite
 * Show the properties of the select module component
 * @author xzhang
 */
@objid ("9b577266-79f6-46bf-b051-892f87db13b6")
public class RamcPropertyComposite extends Composite {
    @objid ("2ded6f35-1d02-4839-9c89-1199d16dbd32")
    protected IModelComponentInfos fragmentInfos = null;

    @objid ("e0eca356-9f5b-4e03-a2f9-00c142382be9")
    protected Button closeButton = null;

    @objid ("b3b0c86c-713b-433f-80de-1807faf7e44b")
    private Label ramcNameLabel = null;

    @objid ("46c89682-6a38-434f-9196-b89ee4dd75c1")
    private Label ramcVersionLabel = null;

    @objid ("f8f199d2-9b9e-4f04-8de3-8942d7b9d086")
    private Label ramcVersionHistoryLabel = null;

    @objid ("b971c3ef-d458-4d85-8b98-a02600463d26")
    private Label ramcDependenciesLabel = null;

    @objid ("05931749-d7e7-437c-b0ad-94aebf522634")
    protected Text ramcNameText = null;

    @objid ("642e388b-792d-4dcc-a425-8fae7887c20d")
    protected Text ramcVersionText = null;

    @objid ("dde9523d-04c7-4beb-821c-af3ee8d6ff0f")
    protected Text ramcVersionHistoryText = null;

    @objid ("f314d1c1-3921-4549-8923-dcc6b7811bc1")
    private TableViewer ramcDependenciesList = null;

    @objid ("ee3ec8ea-6f50-4251-9bbd-f8ab1dca69af")
    protected Label ramcFilesLabel = null;

    @objid ("f70838e7-d99e-4e92-a252-b53318a75f24")
    protected TableViewer ramcFilesList = null;

    @objid ("ef7c13ca-b6fa-452f-9764-a20cbbaf42e6")
    private Label ramcContributingModulesLabel = null;

    @objid ("55c30bf5-a826-45e0-8b2c-88816cd40c55")
    private TableViewer ramcContributingModulesList = null;

    @objid ("a5eb8d02-5186-4279-afd0-ad4052c8c930")
    private ProjectModel projectAdapter;

    /**
     * @param parent
     * @param style
     * @param projectAdapter
     */
    @objid ("f8934f5a-f189-499d-8cd3-5722773ba2e0")
    public RamcPropertyComposite(Composite parent, int style, IModelComponentInfos fragmentInfos, ProjectModel projectAdapter) {
        super(parent, style);
        this.fragmentInfos = fragmentInfos;
        this.projectAdapter = projectAdapter;
        GridLayout mainAreaLayout = new GridLayout(2, false);
        this.setLayout(mainAreaLayout);
        createControls();
        if (this.fragmentInfos != null) refresh();
    }

    @objid ("7710d5ba-8ca0-4b8c-85f6-56c2cc761612")
    private void createControls() {
        createRamcNameField(this);
        createRamcVersionField(this);
        createVersionHistoryField(this);
        createDependenciesField(this);
        createContributingModulesField(this);
        createFilesField(this);
    }

    @objid ("7770dc89-5b15-4a29-b8df-ced143abc47b")
    private void createRamcNameField(Composite area) {
        // model component name field:
        this.ramcNameLabel = new Label(area, SWT.NONE);
        this.ramcNameLabel.setText(AppProjectConf.I18N.getString("RamcPropertyDialog.RamcName"));
        
        this.ramcNameText = new Text(area, SWT.BORDER);
        this.ramcNameText.setEditable(false);
        
        this.ramcNameText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcNameText.setBackground(UIColor.TEXT_READONLY_BG);
        
        final GridData gd_ramcNameLabel = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        this.ramcNameLabel.setLayoutData(gd_ramcNameLabel);
        
        final GridData gd_ramcNameText = new GridData(SWT.FILL, SWT.UP, true, false, 1, 1);
        this.ramcNameText.setLayoutData(gd_ramcNameText);
    }

    @objid ("d41bade6-2694-4025-a4fb-7de0df02f6dc")
    private void createRamcVersionField(Composite area) {
        // model component name field:
        this.ramcVersionLabel = new Label(area, SWT.NONE);
        this.ramcVersionLabel.setText(AppProjectConf.I18N.getString("RamcPropertyDialog.RamcVersion"));
        
        this.ramcVersionText = new Text(area, SWT.BORDER);
        this.ramcVersionText.setEditable(false);
        
        this.ramcVersionText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcVersionText.setBackground(UIColor.TEXT_READONLY_BG);
        
        final GridData gd_ramcVersionLabel = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        this.ramcVersionLabel.setLayoutData(gd_ramcVersionLabel);
        
        final GridData gd_ramcVersionText = new GridData(SWT.FILL, SWT.UP, true, false, 1, 1);
        this.ramcVersionText.setLayoutData(gd_ramcVersionText);
    }

    @objid ("d4ec6f14-896e-44a3-8729-4de3127b0fcf")
    private void createVersionHistoryField(Composite area) {
        // model component name field:
        this.ramcVersionHistoryLabel = new Label(area, SWT.NONE);
        this.ramcVersionHistoryLabel.setText(AppProjectConf.I18N.getString("RamcPropertyDialog.RamcVersionHistory"));
        
        this.ramcVersionHistoryText = new Text(area, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.ramcVersionHistoryText.setEditable(false);
        
        this.ramcVersionHistoryText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcVersionHistoryText.setBackground(UIColor.TEXT_READONLY_BG);
        
        final GridData gd_ramcDescriptionLabel = new GridData(SWT.LEFT, SWT.UP, false, false);
        this.ramcVersionHistoryLabel.setLayoutData(gd_ramcDescriptionLabel);
        
        final GridData gd_ramcDescriptionText = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.ramcVersionHistoryText.setLayoutData(gd_ramcDescriptionText);
        gd_ramcDescriptionText.heightHint = 80;
    }

    @objid ("f54f4f90-35ee-46fe-b644-efb1f6bccc8a")
    private void createDependenciesField(Composite area) {
        // Ramc dependencies field:
        this.ramcDependenciesLabel = new Label(area, SWT.NONE);
        this.ramcDependenciesLabel.setText(AppProjectConf.I18N.getString("RamcPropertyDialog.RamcDependencies"));
        
        this.initDependenciesListViewer(area);
                       
        final GridData gd_ramcDependenciesLabel = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        this.ramcDependenciesLabel.setLayoutData(gd_ramcDependenciesLabel);
    }

    @objid ("73b873d8-79fd-4435-a5e5-b53d85b90891")
    private void createFilesField(Composite area) {
        // Ramc file field:
        this.ramcFilesLabel = new Label(area, SWT.NONE);
        this.ramcFilesLabel.setText(AppProjectConf.I18N.getString("RamcPropertyDialog.RamcFilesLabel"));
        
        this.initFilesListViewer(area);
        final GridData gd_ramcFilesLabel = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        this.ramcFilesLabel.setLayoutData(gd_ramcFilesLabel);
    }

    @objid ("2602088e-7cf4-44dd-be05-77cf60faa853")
    private void initDependenciesListViewer(Composite area) {
        this.ramcDependenciesList = new TableViewer(area, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        
        this.ramcDependenciesList.setContentProvider(new DependenciesContentProvider());
        this.ramcDependenciesList.setLabelProvider(new DependenciesLabelProvider(this.projectAdapter.getLocalLibraryFragments()));
        
        this.ramcDependenciesList.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcDependenciesList.getTable().setBackground(UIColor.TEXT_READONLY_BG);
        
        final GridData gd_ramcDependenciesList = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_ramcDependenciesList.heightHint = 60;
        this.ramcDependenciesList.getTable().setLayoutData(gd_ramcDependenciesList);
    }

    @objid ("e610daa5-9072-4796-ad7f-c909173e7e1f")
    private void initFilesListViewer(Composite area) {
        this.ramcFilesList = new TableViewer(area, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        
        this.ramcFilesList.setContentProvider(new FilesContentProvider());
        this.ramcFilesList.setLabelProvider(new FilesLabelProvider());
        
        this.ramcFilesList.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcFilesList.getTable().setBackground(UIColor.TEXT_READONLY_BG);
        
        final GridData gd_ramcFilesList = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd_ramcFilesList.heightHint = 60;
        this.ramcFilesList.getTable().setLayoutData(gd_ramcFilesList);
    }

    @objid ("1f7a0917-e653-4e24-85f9-a027c54e259d")
    public void refresh() {
        this.ramcNameText.setText(this.fragmentInfos.getName());
        
        Version ramcVersion = this.fragmentInfos.getVersion();
        if (ramcVersion == null) {
            this.ramcVersionText.setText("");
        } else {
            String buildVersion = String.format("%02d", ramcVersion.getBuildVersion());
            this.ramcVersionText.setText(ramcVersion.getMajorVersion() +
                                         "." +
                                         ramcVersion.getMinorVersion() +
                                         "." +
                                         buildVersion);
        }
        
        this.ramcVersionHistoryText.setText(this.fragmentInfos.getDescription());
        this.ramcDependenciesList.setInput(this.fragmentInfos.getRequiredModelComponents());
        this.ramcContributingModulesList.setInput(this.fragmentInfos.getContributingModules());
        this.ramcFilesList.setInput(getRamcExportedFiles());
    }

    @objid ("91918d06-28cb-4f2d-9896-cf0a13567bd8")
    private Set<File> getRamcExportedFiles() {
        Set<File> exportedFiles = new HashSet<>();
        for (ExportedFile ef : this.fragmentInfos.getExportedFiles()) {
            exportedFiles.add(ef.getPath().toFile());
        }
        return exportedFiles;
    }

    @objid ("496542ce-f54e-4720-969c-2eb6388213a3")
    public void setFragmentInfos(IModelComponentInfos fragmentInfos) {
        this.fragmentInfos = fragmentInfos;
    }

    @objid ("6d8a3002-5ac5-4db5-a871-57019b5db2c4")
    private void createContributingModulesField(Composite area) {
        // Ramc contributing modules field
        this.ramcContributingModulesLabel = new Label(area, SWT.NONE);
        this.ramcContributingModulesLabel.setText(AppProjectConf.I18N.getString("RamcPropertyDialog.RamcContributingModules"));
        
        this.initContributingModulesListViewer(area);
                       
        final GridData gd_ramcContributingModulesLabel = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        this.ramcContributingModulesLabel.setLayoutData(gd_ramcContributingModulesLabel);
    }

    @objid ("8bd431ac-9e32-42d4-b216-3364e7d59b2a")
    private void initContributingModulesListViewer(Composite area) {
        this.ramcContributingModulesList = new TableViewer(area, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        
        this.ramcContributingModulesList.setContentProvider(new ContributingModulesContentProvider());
        this.ramcContributingModulesList.setLabelProvider(new ContributingModulesLabelProvider(this.projectAdapter.getModules()));
        
        this.ramcContributingModulesList.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcContributingModulesList.getTable().setBackground(UIColor.TEXT_READONLY_BG);
        
        final GridData gd_ramcContributingModulesList = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd_ramcContributingModulesList.heightHint = 60;
        this.ramcContributingModulesList.getTable().setLayoutData(gd_ramcContributingModulesList);
    }

}
