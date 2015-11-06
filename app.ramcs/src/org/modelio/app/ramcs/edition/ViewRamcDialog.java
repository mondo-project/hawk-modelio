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
                                    

package org.modelio.app.ramcs.edition;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.modelio.api.module.IModule;
import org.modelio.app.ramcs.plugin.AppRamcs;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.images.BasicModelElementLabelProvider;
import org.modelio.gproject.ramc.core.model.ModelComponent;
import org.modelio.gproject.ramc.core.packaging.IModelComponentContributor.ExportedFileEntry;
import org.modelio.ui.UIColor;
import org.modelio.ui.UIImages;
import org.modelio.vbasic.version.Version;

@objid ("74916f47-4dbf-4126-9b01-87906a5e5452")
public class ViewRamcDialog extends ModelioDialog {
    @objid ("2de30b8a-0fae-4efc-bc2c-fb78ee2ddda2")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Model_components_development.html";

    @objid ("3193f51f-8ae5-4ca3-ae4c-65bc5de24676")
    protected RamcModel dataModel = null;

    @objid ("f04b9a60-a377-4773-9ff4-04b4a1bf5637")
    protected Text ramcPathText = null;

    @objid ("3e06e552-7159-46da-aa0f-181ceec9bfa6")
    protected Text ramcNameText = null;

    @objid ("4b164719-c4e8-46bd-bb2d-25e5b2df8fef")
    protected Text ramcVersionText = null;

    @objid ("5f838844-20bc-4cbd-b17a-8073582447ef")
    protected Text ramcDescriptionText = null;

    @objid ("0a914c0f-aafc-4213-a457-457eaaa7d2b4")
    protected TableViewer dependenciesTable = null;

    @objid ("1f322a19-9ec6-48fb-8d77-179fac51ff10")
    protected TableViewer manifestationsTable = null;

    @objid ("d341da93-e5d1-4bce-b631-01f2262b6b86")
    protected TableViewer ramcFilesList = null;

    @objid ("a1ee571d-95ae-4408-8f15-06f13f40e5da")
    protected Button ramcPathButton;

    @objid ("3fc619d9-6f14-451a-94c8-741187b78630")
    protected Button addFilesButton;

    @objid ("cbd1de4c-2052-4753-ac4e-d6eb4d182a7d")
    protected Button removeFilesButton;

    @objid ("9d1fc12c-9df3-4f43-8f28-71069346c510")
    protected CheckboxTableViewer contributorsTable;

    @objid ("540085eb-beef-42b5-8535-e210066e2960")
    public ViewRamcDialog(Shell parentShell, RamcModel dataModel) {
        super(parentShell);
        this.dataModel = dataModel;
        this.setShellStyle(SWT.MODELESS | SWT.DIALOG_TRIM | SWT.RESIZE);
    }

    @objid ("d7b6c18a-196f-48bf-96cf-e27e18acdb1b")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        this.createButton(parent, IDialogConstants.CANCEL_ID, AppRamcs.I18N.getString("EditRamcDialog.Close"), false);
    }

    @objid ("b4b7f6f5-d624-4bf8-a2d3-36f503e5d08f")
    @Override
    public Control createContentArea(Composite parent) {
        Composite area = new Composite(parent, SWT.NONE);
        area.setLayoutData(new GridData(GridData.FILL_BOTH));
        area.setLayout(new GridLayout());
        
        TabFolder tabFolder = new TabFolder(area, SWT.NONE);
        tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        TabItem mainTab = new TabItem(tabFolder, SWT.NONE);
        mainTab.setText(AppRamcs.I18N.getString("EditRamcDialog.MainTab"));
        TabItem filesTab = new TabItem(tabFolder, SWT.NONE);
        filesTab.setText(AppRamcs.I18N.getString("EditRamcDialog.FilesTab"));
        
        Composite mainArea = new Composite(tabFolder, SWT.NONE);
        mainTab.setControl(mainArea);
        mainArea.setLayout(new GridLayout(3, false));
        this.createRamcNameField(mainArea);
        this.createRamcVersionField(mainArea);
        this.createDescriptionField(mainArea);
        this.createManifestationsField(mainArea);
        this.createDependenciesField(mainArea);
        this.createContributorsField(mainArea);
        
        Composite filesArea = new Composite(tabFolder, SWT.NONE);
        filesTab.setControl(filesArea);
        filesArea.setLayout(new GridLayout(2, false));
        this.createFilesField(filesArea);
        this.refresh();
        return area;
    }

    @objid ("8d2475d1-bfa3-4049-8498-345b7b9ef33c")
    @Override
    public void init() {
        this.setLogoImage(null);
        
        // Put the messages in the banner area
        this.getShell().setText(AppRamcs.I18N.getString("EditRamcDialog.ViewRamcDialogTitle"));
        this.setTitle(AppRamcs.I18N.getString("EditRamcDialog.ViewRamcDialogTitle"));
        this.setMessage(AppRamcs.I18N.getString("EditRamcDialog.ViewRamcMessage"));
        
        Point parentLocation = this.getShell().getParent().getLocation();
        
        this.getShell().setLocation(parentLocation.x + 300, parentLocation.y + 200);
        this.getShell().setSize(500, 600);
        this.getShell().setMinimumSize(500, 550);
    }

    @objid ("e4886bcd-fa2d-47f8-a0d4-86061afe19b8")
    public boolean close(int code) {
        this.setReturnCode(code);
        return this.close();
    }

    @objid ("1b042a95-fc1a-4397-b485-6e345b9ce553")
    private void createDependenciesField(Composite area) {
        // Label
        Label ramcDependenciesLabel = new Label(area, SWT.NONE);
        ramcDependenciesLabel.setText(AppRamcs.I18N.getString("EditRamcDialog.RamcDependencies.label"));
        ramcDependenciesLabel.setLayoutData(new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1));
        
        // Table viewer
        this.dependenciesTable = new TableViewer(area, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        final GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdTable.heightHint = 50;
        this.dependenciesTable.getTable().setLayoutData(gdTable);
        this.dependenciesTable.getTable().setToolTipText(AppRamcs.I18N.getString("EditRamcDialog.RamcDependencies.tooltip"));
        this.dependenciesTable.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.dependenciesTable.getTable().setBackground(UIColor.TEXT_READONLY_BG);
        
        // Droparea indicator
        Label targetLabel = new Label(area, SWT.NONE);
        targetLabel.setImage(UIImages.DROPAREA);
        final GridData gdTarget = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        gdTarget.heightHint = 21;
        gdTarget.widthHint = 21;
        targetLabel.setLayoutData(gdTarget);
        
        // Branch providers
        this.dependenciesTable.setContentProvider(new ArrayContentProvider());
        this.dependenciesTable.setLabelProvider(new BasicModelElementLabelProvider() {
            @Override
            public Image getImage(Object obj) {
                final ModelComponent model = (ModelComponent) obj;
                return super.getImage(model.getArtifact());
            }
        
            @Override
            public String getText(Object obj) {
                final ModelComponent model = (ModelComponent) obj;
                return model.getName() +  model.getVersion().toString(" V.R.C");
            }
        });
    }

    @objid ("0eeafa47-4cec-4c48-ab86-d2f2b5bd0d7e")
    private void createDescriptionField(Composite area) {
        // Label
        Label ramcDescriptionLabel = new Label(area, SWT.NONE);
        ramcDescriptionLabel.setText(AppRamcs.I18N.getString("EditRamcDialog.RamcDescription.label"));
        final GridData descriptionGD = new GridData(SWT.LEFT, SWT.UP, false, false);
        descriptionGD.heightHint = 100;
        ramcDescriptionLabel.setLayoutData(descriptionGD);
        
        // Text
        this.ramcDescriptionText = new Text(area, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.ramcDescriptionText.setEditable(false);
        this.ramcDescriptionText.setToolTipText(AppRamcs.I18N.getString("EditRamcDialog.RamcDescription.tooltip"));
        
        this.ramcDescriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.ramcDescriptionText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcDescriptionText.setBackground(UIColor.TEXT_READONLY_BG);
        
        Composite emptyComposite = new Composite(area, SWT.NONE);
        final GridData gd = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        gd.heightHint = 21;
        gd.widthHint = 21;
        emptyComposite.setLayoutData(gd);
    }

    @objid ("68c34ec8-b347-4330-a9e2-1535ae4cbdec")
    private void createRamcVersionField(Composite area) {
        // Label
        Label ramcVersionLabel = new Label(area, SWT.NONE);
        ramcVersionLabel.setText(AppRamcs.I18N.getString("EditRamcDialog.RamcVersion.label"));
        ramcVersionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        
        // Text
        this.ramcVersionText = new Text(area, SWT.BORDER);
        this.ramcVersionText.setEditable(false);
        this.ramcVersionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.ramcVersionText.setToolTipText(AppRamcs.I18N.getString("EditRamcDialog.RamcVersion.tooltip"));
        
        this.ramcVersionText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcVersionText.setBackground(UIColor.TEXT_READONLY_BG);
        
        Composite emptyComposite = new Composite(area, SWT.NONE);
        final GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd.heightHint = 21;
        gd.widthHint = 21;
        emptyComposite.setLayoutData(gd);
    }

    @objid ("76f0a7dd-8197-44a3-814c-d6e6964dbe8c")
    private void createRamcNameField(Composite area) {
        // Label
        Label ramcNameLabel = new Label(area, SWT.NONE);
        ramcNameLabel.setText(AppRamcs.I18N.getString("EditRamcDialog.RamcName.label"));
        ramcNameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        
        // Text
        this.ramcNameText = new Text(area, SWT.BORDER);
        this.ramcNameText.setEditable(false);
        this.ramcNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        this.ramcNameText.setToolTipText(AppRamcs.I18N.getString("EditRamcDialog.RamcName.tooltip"));
        this.ramcNameText.setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcNameText.setBackground(UIColor.TEXT_READONLY_BG);
        
        Composite emptyComposite = new Composite(area, SWT.NONE);
        final GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd.heightHint = 21;
        gd.widthHint = 21;
        emptyComposite.setLayoutData(gd);
    }

    @objid ("08755c9a-273f-4884-b4a8-384cf0a92838")
    private void removeListener(@SuppressWarnings("unused") Controller listener) {
        // FIXME
        // this.ramcNameText.removeModifyListener(listener);
        // this.ramcPathText.removeModifyListener(listener);
        // this.ramcVersionText.removeModifyListener(listener);
        // this.ramcDescriptionText.removeModifyListener(listener);
        //
        // IModelingSession modelingSession =
        // Ramcs.getInstance().getModelingSession();
        // modelingSession.getModel().removeModelListener(listener);
    }

    @objid ("d498e17f-0729-4124-b739-b94eac03beca")
    public void refresh() {
        this.ramcNameText.setText(this.dataModel.getName());
        Version ramcVersion = this.dataModel.getVersion();
        if (ramcVersion == null) {
            this.ramcVersionText.setText("");
        } else {
            String buildVersion = String.format("%02d", ramcVersion.getBuildVersion());
            this.ramcVersionText.setText(ramcVersion.getMajorVersion() + "." + ramcVersion.getMinorVersion() + "." + buildVersion);
        }
        
        this.ramcDescriptionText.setText(this.dataModel.getDescription());
        this.dependenciesTable.setInput(this.dataModel.getRequiredModelComponents());
        this.manifestationsTable.setInput(this.dataModel.getExportedElements());
        this.ramcFilesList.setInput(this.dataModel.getExportedFiles());
        
        this.contributorsTable.setInput(this.dataModel.getContributorCandidates());
    }

    @objid ("9e5730f4-2768-473d-8a15-a0737a481d78")
    public Version getVersion() {
        return new Version(this.ramcVersionText.getText());
    }

    @objid ("9deb24f5-c423-4df8-b275-0db953eaf6c6")
    public String getDescription() {
        return this.ramcDescriptionText.getText();
    }

    @objid ("f5ccd16e-a5aa-4efd-a8af-22bb18d5ede3")
    public String getRamcPath() {
        return this.ramcPathText.getText();
    }

    @objid ("b90e13f3-7153-4aab-bc61-b6b69ce11df0")
    public void invalidateRamcVersion(@SuppressWarnings("unused") boolean invalid) {
        this.ramcVersionText.setForeground(UIColor.EDITOR_ROTEXT_FG);
    }

    @objid ("54dcea16-2e2d-4ab6-8503-c411bec88234")
    public void invalidateRamcPath(@SuppressWarnings("unused") boolean invalid) {
        this.ramcPathText.setForeground(UIColor.EDITOR_ROTEXT_FG);
    }

    @objid ("e73a05e7-4ad2-4197-a6cf-7258439d4757")
    public String getRamcName() {
        return this.ramcNameText.getText();
    }

    @objid ("64e6fe04-2b34-44b9-86a9-e2fc9d1357a4")
    public void invalidateRamcName(@SuppressWarnings("unused") boolean invalid) {
        this.ramcNameText.setForeground(UIColor.EDITOR_ROTEXT_FG);
    }

    @objid ("84dbf008-aaa3-4ef5-88c4-dc70bbf3075e")
    private void createManifestationsField(Composite parent) {
        // Label
        Label label = new Label(parent, SWT.NONE);
        label.setText(AppRamcs.I18N.getString("EditRamcDialog.RamcManifestationLabel.label"));
        label.setLayoutData(new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1));
        
        // List
        this.manifestationsTable = new TableViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        final GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdTable.heightHint = 100;
        this.manifestationsTable.getTable().setLayoutData(gdTable);
        this.manifestationsTable.getTable()
                .setToolTipText(AppRamcs.I18N.getString("EditRamcDialog.RamcManifestationLabel.tooltip"));
        
        // Drop indicator
        Label target = new Label(parent, SWT.NONE);
        target.setImage(UIImages.DROPAREA);
        final GridData gdTarget = new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1);
        gdTarget.heightHint = 21;
        gdTarget.widthHint = 21;
        target.setLayoutData(gdTarget);
        
        // Branch list providers
        this.manifestationsTable.setContentProvider(new ArrayContentProvider());
        this.manifestationsTable.setLabelProvider(new BasicModelElementLabelProvider());
        
        this.manifestationsTable.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.manifestationsTable.getTable().setBackground(UIColor.TEXT_READONLY_BG);
    }

    @objid ("d51f4ebc-3f62-422a-a203-668b96fc15b5")
    private void createFilesField(Composite area) {
        // Files list
        this.ramcFilesList = new TableViewer(area, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL| SWT.MULTI);
        final Table table = this.ramcFilesList.getTable();
        
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        
        
        this.ramcFilesList.setContentProvider(new ArrayContentProvider());
        
        // First column is for the local file path
        TableViewerColumn col1 = new TableViewerColumn(this.ramcFilesList, SWT.NONE);
        col1.getColumn().setWidth(200);
        col1.getColumn().setText(AppRamcs.I18N.getString("EditRamcDialog.LocalFile"));
        col1.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((ExportedFileEntry) element).getExportedFile();
            }
        });
        
        // Second column is for the deployment relative path
        TableViewerColumn col2 = new TableViewerColumn(this.ramcFilesList, SWT.NONE);
        col2.getColumn().setWidth(200);
        col2.getColumn().setText(AppRamcs.I18N.getString("EditRamcDialog.DeploymentPath"));
        col2.setLabelProvider(new ColumnLabelProvider() {
            @Override
            public String getText(Object element) {
                return ((ExportedFileEntry) element).getExportPath();
            }
        });
        col2.setEditingSupport(new EditingSupport(this.ramcFilesList) {
        
            @Override
            protected CellEditor getCellEditor(Object element) {
                return new TextCellEditor(table);
            }
        
            @Override
            protected boolean canEdit(Object element) {
                return ViewRamcDialog.this.dataModel.isEditable();
            }
        
            @Override
            protected Object getValue(Object element) {
                return ((ExportedFileEntry) element).getExportPath();
            }
        
            @Override
            protected void setValue(Object element, Object value) {
                ((ExportedFileEntry) element).setExportPath((String)value);
                this.getViewer().refresh();
            }
            
        });
        
        this.ramcFilesList.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        this.ramcFilesList.getTable().setBackground(UIColor.TEXT_READONLY_BG);
        
        Composite buttonComposite = new Composite(area, SWT.NONE);
        GridLayout l_buttonComposite = new GridLayout(1, false);
        l_buttonComposite.horizontalSpacing = 0;
        l_buttonComposite.marginWidth = 0;
        buttonComposite.setLayout(l_buttonComposite);
        
        final GridData gd_buttonComposite = new GridData(SWT.LEFT, SWT.FILL, false, true);
        // gd_buttonComposite.widthHint = 21;
        buttonComposite.setLayoutData(gd_buttonComposite);
        
        this.addFilesButton = new Button(buttonComposite, SWT.NONE);
        this.addFilesButton.setImage(UIImages.FILECHOOSE);
        this.addFilesButton.setEnabled(false);
        
        this.removeFilesButton = new Button(buttonComposite, SWT.NONE);
        this.removeFilesButton.setImage(UIImages.DELETE);
        this.removeFilesButton.setEnabled(false);
        
        final FileDialog filesChooser = new FileDialog(this.getShell(), SWT.OPEN | SWT.MULTI);
        final String projectPath = ""; // FIXME
        // modelingSession.getProjectSpacePath().getPath();
        filesChooser.setFilterPath(projectPath);
        
        
        Label exportedFileDescription = new Label(area, SWT.WRAP);
        exportedFileDescription.setText(AppRamcs.I18N.getString("EditRamcDialog.ExportedFileDescription"));
        exportedFileDescription.setForeground(org.modelio.ui.UIColor.LABEL_TIP_FG);
        final GridData gd_exportedFileDescription = new GridData(SWT.FILL, SWT.UP, true, false, 2, 1);
        exportedFileDescription.setLayoutData(gd_exportedFileDescription);
    }

    @objid ("0a238302-c9b4-4e06-bea1-1e4d29c41788")
    private void createContributorsField(Composite area) {
        // Label
        Label label = new Label(area, SWT.NONE);
        label.setText(AppRamcs.I18N.getString("EditRamcDialog.Contributors.label"));
        label.setLayoutData(new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1));
        
        // Table viewer
        this.contributorsTable = CheckboxTableViewer.newCheckList(area, SWT.CHECK | SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
        
        final GridData gdTable = new GridData(SWT.FILL, SWT.FILL, true, true);
        gdTable.heightHint = 50;
        this.contributorsTable.getTable().setLayoutData(gdTable);
        // this.contributorsTable.getTable().setForeground(UIColor.EDITOR_ROTEXT_FG);
        // this.contributorsTable.getTable().setBackground(UIColor.TEXT_READONLY_BG);
        this.contributorsTable.getTable().setEnabled(false);
        this.contributorsTable.getTable().setToolTipText(AppRamcs.I18N.getString("EditRamcDialog.Contributors.tooltip"));
        
        Composite emptyComposite = new Composite(area, SWT.NONE);
        final GridData gd = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd.heightHint = 21;
        gd.widthHint = 21;
        emptyComposite.setLayoutData(gd);
        
        // Branch providers
        this.contributorsTable.setContentProvider(new ArrayContentProvider());
        this.contributorsTable.setLabelProvider(new LabelProvider() {
            @Override
            public Image getImage(Object obj) {
                final IModule module = (IModule) obj;
                return module.getModuleImage();
            }
        
            @Override
            public String getText(Object obj) {
                final IModule module = (IModule) obj;
                return module.getName() + " " + module.getVersion();
            }
        });
        
        this.contributorsTable.setCheckStateProvider(new ICheckStateProvider() {
        
            @Override
            public boolean isGrayed(Object obj) {
                return false;
            }
        
            @Override
            public boolean isChecked(Object obj) {
                String n = ((IModule) obj).getName();
                return ViewRamcDialog.this.dataModel.getContributingModules().containsKey(n);
            }
        });
    }

    @objid ("2a6b9a8a-22f0-4fb2-bb70-67c62cbb04e8")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

}
