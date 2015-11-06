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
                                    

package org.modelio.app.model.imp.impl.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.modelio.app.model.imp.impl.ModelImportDataModel;
import org.modelio.app.model.imp.plugin.AppModelImport;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.data.project.ProjectDescriptor;
import org.modelio.gproject.gproject.GProjectFactory;
import org.modelio.gproject.module.IModuleCatalog;
import org.modelio.metamodel.analyst.AnalystPropertyTable;
import org.modelio.metamodel.analyst.PropertyContainer;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.model.browser.views.treeview.BrowserLabelProvider;
import org.modelio.ui.UIImages;
import org.modelio.vcore.smkernel.SmObjectImpl;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Import model dialog box.
 */
@objid ("d0188834-ab6f-4e4c-8b2e-c0757b7b6f82")
public class ImportModelDialog extends ModelioDialog {
    @objid ("501228c7-22de-4333-a3ff-7b81ddbbd380")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_managing_projects_importing_elements.html";

    @objid ("2d049afb-ddcb-41dd-972f-13668f10467c")
    protected ModelImportDataModel dataModel;

    @objid ("e58e18b5-c207-4883-9e8e-3830c6983ce5")
    private Button createButton;

    @objid ("d1728ff2-685c-46a9-b69b-e905bf052063")
    protected Text importedProjectText;

    @objid ("a476fa84-c34a-4ebb-b8d9-3d60cb064ea5")
    private Button projectChooserButton;

    @objid ("e2924d61-2924-49b5-9158-89f891c851d7")
    private CheckboxTreeViewer viewer;

    @objid ("5af16816-4af4-4d3d-beac-544f147f2667")
    private Text importDescriptionText;

    @objid ("91008a6f-4a30-4f67-b82f-7a34a3a4da29")
    protected IModuleCatalog moduleCatalog;

    /**
     * Initialize the dialog.
     * @param destSession
     * the destination modeling session
     * @param parentShell The parent SWT shell
     * @param dataModel the data model of elements to import
     */
    @objid ("50c121f9-c137-4c59-aa86-5a3838a1e211")
    public ImportModelDialog(Shell parentShell, ModelImportDataModel dataModel, IModuleCatalog moduleCatalog) {
        super(parentShell);
        this.dataModel = dataModel;
        this.moduleCatalog = moduleCatalog;
        
        // since Feb 2013 import is always 'by copy' in Modelio 3 - force it here
        this.dataModel.setReidentify(true);
    }

    @objid ("e0a03f89-eff5-4b8c-97f7-5b09bebef11a")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        this.createButton = createButton(parent, IDialogConstants.OK_ID, AppModelImport.I18N.getString("ImportModelDialog.Import"),
                false);
        createButton(parent, IDialogConstants.CANCEL_ID, AppModelImport.I18N.getString("ImportModelDialog.Cancel"), true);
    }

    @objid ("9569fbe7-824d-4136-ac80-265718f1320c")
    @Override
    public Control createContentArea(Composite parent) {
        Composite area = new Composite(parent, SWT.NONE);
        area.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        GridLayout areaLayout = new GridLayout(3, false);
        area.setLayout(areaLayout);
        
        createProjectPathChooser(area);
        createTreeViewer(area);
        return area;
    }

    @objid ("a7d918a7-92b6-4733-8209-273e1f422779")
    @Override
    public void init() {
        setLogoImage(null);
        // Put the messages in the banner area
        getShell().setText(AppModelImport.I18N.getString("ImportModelDialog.DialogTitle"));
        setTitle(AppModelImport.I18N.getString("ImportModelDialog.MessageTitle"));
        setMessage(AppModelImport.I18N.getString("ImportModelDialog.DialogMessage"));
        this.getShell().setSize(450, 500);
        this.getShell().setMinimumSize(450, 450);
    }

    /**
     * Set the project path as invalid or not.
     * @param invalid
     * true to set the path as invalid
     */
    @objid ("a2a5075f-d7ac-4a32-ab98-9fd183f2a437")
    public void updateProjectPathColor(boolean isValid) {
        if (isValid) {
            this.importedProjectText.setForeground(this.importedProjectText.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
        } else {
            this.importedProjectText.setForeground(this.importedProjectText.getDisplay().getSystemColor(SWT.COLOR_RED));
        }
    }

    /**
     * Refresh the content.
     */
    @objid ("17861ca3-ad8c-4cc1-a40e-36430b1106cf")
    public void refresh() {
        // this.contentProvider.setSrcSession(this.dataModel.getSrcSession());
        this.viewer.setInput(this.dataModel.getImportedProject());
    }

    /**
     * Enable or disable the import button.
     * @param canImport true to enable the import button, false to disable it.
     */
    @objid ("b5e9fa4d-d6ad-49d3-8af6-8621d821cb0d")
    public void updateButtons(boolean canImport) {
        if (this.createButton != null) {
            this.createButton.setEnabled(canImport);
        }
    }

    @objid ("fd7ae863-3bf2-4235-80f6-edc23f89a593")
    String getImportedProjectPath() {
        return this.importedProjectText.getText();
    }

    @objid ("882054d5-beff-4102-bcfd-e75a95433f24")
    protected TreeItem[] getChildren(Widget o) {
        if (o instanceof TreeItem) {
            return ((TreeItem) o).getItems();
        }
        if (o instanceof Tree) {
            return ((Tree) o).getItems();
        }
        return null;
    }

    @objid ("6b1c6028-48fc-4684-b07e-da78c8b40cfe")
    @Override
    protected void okPressed() {
        List<SmObjectImpl> elementsToImport = getElementsToImport();
        
        // Fill the elements to import in the data model
        this.dataModel.getElementsToImport().clear();
        this.dataModel.getElementsToImport().addAll(elementsToImport);
        
        super.okPressed();
    }

    @objid ("8718f20a-c50d-4c8c-89b2-84af0fd8a85c")
    private void collectElementsToImport(List<SmObjectImpl> result, Widget widget) {
        for (TreeItem treeItem : getChildren(widget)) {
            if (treeItem.getChecked()) {
                // If checked the element must be imported no need to search for
                // children they will be imported as well
                Object data = treeItem.getData();
                if (data instanceof ModelElement) {
                    final ModelElement elt = (ModelElement) data;
                    if (elt.getCompositionOwner() == null || elt instanceof PropertyContainer) {
                        collectHiddenChildren(result, elt);
                    } else {
                        result.add((SmObjectImpl) elt);
                    }
                } else {
                    collectElementsToImport(result, treeItem);
                }
            } else if (!treeItem.getChecked()) {
                // If grayed the element must not be imported but it has
                // children that must be imported
                // So we collect them.
                collectElementsToImport(result, treeItem);
            }
        }
    }

    @objid ("bf09b70f-d930-4016-afa8-d73eb775314b")
    private void collectHiddenChildren(List<SmObjectImpl> result, MObject element) {
        for (MObject c : element.getCompositionChildren()) {
            if (!(c instanceof TaggedValue) && !(c instanceof AnalystPropertyTable) && !c.getStatus().isRamc()) {
                if (c instanceof PropertyContainer) {
                    collectHiddenChildren(result, c);
                } else {
                    result.add((SmObjectImpl) c);
                }
            }
        }
    }

    @objid ("fce62fff-dd60-4b0a-acb8-20b0a2366dff")
    private void createProjectPathChooser(Composite area) {
        // Import project title
        Label importedProjectLabel = new Label(area, SWT.NONE);
        importedProjectLabel.setText(AppModelImport.I18N.getString("ImportModelDialog.SourceProjectPath"));
        importedProjectLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        
        // Current project path
        this.importedProjectText = new Text(area, SWT.BORDER);
        this.importedProjectText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        this.importedProjectText.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent event) {
                final String projectPath = getImportedProjectPath();
        
                boolean isValidPath = true;
        
                try {
                    // Close the previous project if any
                    if (ImportModelDialog.this.dataModel.getImportedProject() != null) {
                        ImportModelDialog.this.dataModel.getImportedProject().close();
                        ImportModelDialog.this.dataModel.setImportedProject(null);
                    }
        
                    // Open the new project
                    Path projectConfPath = new File(projectPath).getParentFile().toPath();
        
                    ProjectDescriptor compareProjectDescriptor = GProjectFactory.readProjectDirectory(projectConfPath);
                    ImportModelDialog.this.dataModel.setImportedProject(GProjectFactory.openProject(compareProjectDescriptor, null,
                            ImportModelDialog.this.moduleCatalog, null));
                } catch (IOException e) {
                    updateProjectPathColor(!isValidPath);
                }
        
                refresh();
        
                updateButtons(isValidPath);
            }
        });
        
        // Button to choose a project
        this.projectChooserButton = new Button(area, SWT.PUSH);
        this.projectChooserButton.setImage(UIImages.FILECHOOSE);
        this.projectChooserButton.setLayoutData(new GridData(SWT.LEFT, SWT.UP, false, false, 1, 1));
        this.projectChooserButton.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // Nothing to do
            }
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Open a file chooser to select the project.conf file
                final FileDialog projectPathChooser = new FileDialog(getShell(), SWT.OPEN);
                projectPathChooser.setFilterExtensions(new String[] { "project.conf" });
                projectPathChooser.setFilterNames(new String[] { AppModelImport.I18N.getString("ImportModelDialog.OfpxFiles") });
        
                String fileName = projectPathChooser.open();
                if (fileName != null) {
                    ImportModelDialog.this.importedProjectText.setText(fileName);
                }
            }
        
        });
    }

    @objid ("16552813-ce70-4345-99f3-bc6695b8d5fc")
    private void createTreeViewer(Composite area) {
        Label viewerLabel = new Label(area, SWT.NONE);
        viewerLabel.setText(AppModelImport.I18N.getString("ImportModelDialog.ViewerLabel"));
        
        this.viewer = new CheckboxTreeViewer(area, SWT.BORDER | SWT.CHECK | SWT.V_SCROLL | SWT.H_SCROLL);
        this.viewer.setAutoExpandLevel(2);
        this.viewer.setContentProvider(new ImportModelContentProvider());
        this.viewer.setLabelProvider(new BrowserLabelProvider());
        
        this.viewer.setUseHashlookup(true);
        
        TreeviewerListener hook = new TreeviewerListener(this.viewer);
        this.viewer.addTreeListener(hook);
        this.viewer.addCheckStateListener(hook);
        
        final GridData gd_viewerLabel = new GridData(SWT.LEFT, SWT.TOP, true, false, 3, 1);
        viewerLabel.setLayoutData(gd_viewerLabel);
        
        final GridData gd_viewer = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
        this.viewer.getTree().setLayoutData(gd_viewer);
    }

    @objid ("04bc9a1c-e7d8-458d-9a89-0447fdd24904")
    private List<SmObjectImpl> getElementsToImport() {
        List<SmObjectImpl> result = new ArrayList<>();
        collectElementsToImport(result, this.viewer.getControl());
        return result;
    }

    @objid ("19e4e572-1276-4233-8a9d-91690c94dfa3")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("db40f186-fddb-4c7d-a5cd-a4529da7e835")
    private static class TreeviewerListener implements ICheckStateListener, ITreeViewerListener {
        @objid ("a4b01075-85ca-43d4-b5b3-afc5c3a1515c")
        private final CheckboxTreeViewer tree;

        @objid ("c85e7e39-cf55-4b51-8960-9c9ea3478ddb")
        TreeviewerListener(CheckboxTreeViewer tree) {
            this.tree = tree;
        }

        @objid ("bc2be895-f6fb-4b1a-b3e8-166f260630a8")
        @Override
        public void checkStateChanged(CheckStateChangedEvent event) {
            doCheckStateChanged(event.getElement());
        }

        @objid ("fd396a6f-a8ce-4628-9321-a96ebed2f19c")
        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            // Nothing to do
        }

        @objid ("ac0de913-6831-4b6d-a944-72a01ffc7951")
        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            final Object element = (event.getElement());
            final boolean checked = this.tree.getChecked(element);
            // final boolean grayed = tree.getGrayed(element);
            
            final ITreeContentProvider provider = (ITreeContentProvider) this.tree.getContentProvider();
            final Object[] children = provider.getChildren(element);
            
            if (checked) {
                for (Object child : children) {
                    if (!this.tree.getGrayed(child)) {
                        this.tree.setGrayed(child, true);
                        this.tree.setChecked(child, checked);
                        updateChildrenElements(child, true, checked);
                    }
                }
            } else {
                for (Object child : children) {
                    if (this.tree.getGrayed(child)) {
                        this.tree.setGrayed(child, false);
                        this.tree.setChecked(child, checked);
                        updateChildrenElements(child, false, checked);
                    }
                }
            
            }
        }

        @objid ("0ff9b7e0-2b92-4348-90b3-fe71642bee76")
        protected void doCheckStateChanged(Object element) {
            final boolean newChecked = this.tree.getChecked(element);
            final boolean grayed = this.tree.getGrayed(element);
            
            if (grayed) {
                // Forbid changing state
                this.tree.setChecked(element, !newChecked);
            } else {
                updateChildrenElements(element, newChecked, newChecked);
            }
        }

        @objid ("56d079b7-8827-466f-b839-e5bb87f2a6a9")
        private void updateChildrenElements(Object element, boolean grayed, boolean checked) {
            boolean expanded = this.tree.getExpandedState(element);
            
            if (expanded) {
                ITreeContentProvider provider = (ITreeContentProvider) this.tree.getContentProvider();
                Object[] children = provider.getChildren(element);
            
                for (Object child : children) {
                    this.tree.setGrayed(child, grayed);
                    this.tree.setChecked(child, checked);
                    updateChildrenElements(child, grayed, checked);
                }
            }
        }

    }

}
