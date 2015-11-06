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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.project.conf.dialog.ProjectModel;
import org.modelio.app.project.conf.dialog.libraries.local.property.RamcPropertyComposite;
import org.modelio.app.project.conf.plugin.AppProjectConf;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.data.project.FragmentDescriptor;
import org.modelio.gproject.data.ramc.IModelComponentInfos;
import org.modelio.gproject.data.ramc.ModelComponentArchive;
import org.modelio.gproject.fragment.IProjectFragment;
import org.modelio.ui.UIImages;

/**
 * Dialog box used to instantiate a new RAMC fragment descriptor to mount in a
 * project.
 * <p>
 * A ramc fragment has only one configuration point:
 * <ul>
 * <li>A .ramc file path</li>
 * </ul>
 * </p>
 * <p>
 * Use {@link AddDistantLibraryDialog#getFragmentDescriptor()} to get the new
 * fragment.
 * </p>
 */
@objid ("7d5357e9-3adc-11e2-916e-002564c97630")
public final class AddLocalLibraryDialog extends ModelioDialog {
    @objid ("d9f30cf3-ff30-488b-b707-a751cbfa785c")
     List<String> invalidIds;

    @objid ("5801228b-6e20-457d-9864-c99df6f99b45")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_managing_projects_configuring_project_libraries.html";

    @objid ("7d5357f0-3adc-11e2-916e-002564c97630")
    private RamcFragmentPanel panel;

    @objid ("fbfeefb4-eee4-4b88-a376-412c5f748d4b")
    private ModelComponentArchive modelComponentArchive;

    @objid ("b4ec2dca-3184-4fe1-8a86-5c59eaaa7239")
    private FragmentDescriptor fragmentDescriptor;

    @objid ("184b9a28-6eda-40e8-96e1-f51c29757b90")
    private RamcPropertyComposite propertyComposite;

    @objid ("2bf2034e-4e7f-4585-97d4-eead1a40920d")
    private IModelComponentInfos fragmentInfos;

    @objid ("6c5d9c12-91fc-4f02-9c11-1eae3e85fe23")
     Composite area;

    @objid ("f3c1958d-d5e8-4bd6-a747-469b071b9139")
    private Button addBtn;

    @objid ("b5816707-4ca0-4bf4-82f1-8ffd9e12a0c1")
    private ProjectModel projectAdapter;

    @objid ("7d5357f1-3adc-11e2-916e-002564c97630")
    public AddLocalLibraryDialog(Shell parentShell, ProjectModel projectAdapter) {
        super(parentShell);
        this.projectAdapter = projectAdapter;
        this.invalidIds = projectAdapter.getFragmentIdList();
    }

    @objid ("7d5357f4-3adc-11e2-916e-002564c97630")
    @Override
    public Control createContentArea(Composite parent) {
        // fragment data area
        this.area = new Composite(parent, SWT.NONE);
        this.area.setLayout(new GridLayout(1, false));
        this.area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        
        // fragment type-specific panel
        this.panel = new RamcFragmentPanel(this.area, SWT.NONE);
        GridData gd1 = new GridData(SWT.FILL, SWT.FILL, true, false);
        gd1.horizontalSpan = 2;
        this.panel.setLayoutData(gd1);
        
        // fragment properties composite
        Group parametersGroup = new Group(this.area, SWT.NONE);
        parametersGroup.setText(AppProjectConf.I18N.getString("AddLocalLibraryDialog.Panel.title")); //$NON-NLS-1$
        parametersGroup.setLayout(new GridLayout(1, true));
        parametersGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.propertyComposite = new RamcPropertyComposite(parametersGroup, SWT.NONE, this.fragmentInfos, this.projectAdapter);
        this.propertyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        return this.area;
    }

    @objid ("7d5357fa-3adc-11e2-916e-002564c97630")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, CANCEL, IDialogConstants.CANCEL_LABEL, true);
        this.addBtn = createButton(parent, OK, AppProjectConf.I18N.getString("AddLocalLibraryDialog.AddFragment"), true); //$NON-NLS-1$
        this.addBtn.setEnabled(false);
    }

    @objid ("7d5357fe-3adc-11e2-916e-002564c97630")
    @Override
    public void init() {
        this.getShell().setText(AppProjectConf.I18N.getString("AddLocalLibraryDialog.ShellTitle")); //$NON-NLS-1$
        setTitle(AppProjectConf.I18N.getString("AddLocalLibraryDialog.Title")); //$NON-NLS-1$
        setMessage(AppProjectConf.I18N.getString("AddLocalLibraryDialog.Message")); //$NON-NLS-1$
        Point parentLocation = this.getShell().getParent().getLocation();
        
        this.getShell().setLocation(parentLocation.x + 300, parentLocation.y + 200);
        this.getShell().setSize(550, 650);
        this.getShell().setMinimumSize(520, 600);
    }

    @objid ("7d535807-3adc-11e2-916e-002564c97630")
    @Override
    protected void okPressed() {
        // FIXME Replace with the following code to allow HTTP ramc fragments 
        //        try(UriPathAccess acc = new UriPathAccess(URI, IAuthData)) {
        //            Path p = acc.getPath();
        Path archivePath = Paths.get(this.panel.text.getText());
        this.modelComponentArchive = new ModelComponentArchive(archivePath, true);
        try {
            this.fragmentDescriptor = this.modelComponentArchive.getFragmentDescriptor();
        } catch (Exception e) {
            // Display and log the message, and prevent dialog from closing.
            AppProjectConf.LOG.error(e);
            setErrorMessage(e.getLocalizedMessage());
            return;
        }
        
        super.okPressed();
    }

    @objid ("7d53580a-3adc-11e2-916e-002564c97630")
    public FragmentDescriptor getFragmentDescriptor() {
        return this.fragmentDescriptor;
    }

    @objid ("4d93dac4-342c-422b-8be0-d2287e3339af")
    public ModelComponentArchive getModelComponentArchive() {
        return this.modelComponentArchive;
    }

    @objid ("13e84e83-b486-452e-95ec-7aefd59f1357")
    void showFragmentInfos() {
        this.fragmentInfos = getFragmentInfos();
        if (this.fragmentInfos != null) {
            this.propertyComposite.setFragmentInfos(this.fragmentInfos);
            this.propertyComposite.refresh();
        }
        this.area.layout();
    }

    @objid ("6fc7d5f2-6ed1-4536-8ec3-a49322fe7587")
    IModelComponentInfos getFragmentInfos() {
        IModelComponentInfos infos = null;
        Path archivePath = Paths.get(this.panel.text.getText());
        ModelComponentArchive archive = new ModelComponentArchive(archivePath, true);
        try {
            infos = archive.getInfos();
        } catch (IOException error) {
            AppProjectConf.LOG.error(error);
        }
        return infos;
    }

    @objid ("9647a0db-a39b-4887-b87a-acc0fdb22d5e")
    void isFragmentValid() {
        boolean valid = true;
        String fragmentId = this.fragmentInfos.getName() + " " + this.fragmentInfos.getVersion();
        if (this.fragmentInfos != null) {
            if (this.invalidIds.contains(fragmentId)) {
                valid = false; // already exist
            }
        }
        if (valid) {
            this.addBtn.setEnabled(true);
            this.panel.text.setForeground(this.panel.text.getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN));
            setErrorMessage(null);
        } else {
            this.addBtn.setEnabled(false);
            this.panel.text.setForeground(this.panel.text.getDisplay().getSystemColor(SWT.COLOR_RED));
            setErrorMessage(AppProjectConf.I18N.getMessage("AddLocalLibraryDialog.ErrorMessage.AlreadyExist", fragmentId));
        }
    }

    @objid ("12aade3b-8453-4487-b8b1-d59d97e7b334")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    /**
     * Model component fragment panel.
     */
    @objid ("7d55b962-3adc-11e2-916e-002564c97630")
    private class RamcFragmentPanel extends Composite {
        @objid ("ac32e19a-4d60-407d-8444-ccdb4a5251fc")
        protected Text text;

        /**
         * Initialize the panel.
         * @param parent the parent composite.
         * @param style the style of widget to construct
         */
        @objid ("7d55b965-3adc-11e2-916e-002564c97630")
        public RamcFragmentPanel(final Composite parent, int style) {
            super(parent, style);
            setLayout(new GridLayout(1, false));
            createContents(this);
        }

        @objid ("7d55b96e-3adc-11e2-916e-002564c97630")
        private void createContents(Composite parent) {
            final Composite data = new Composite(parent, SWT.NONE);
            data.setLayout(new GridLayout(3, false));
            
            Label label = new Label(data, SWT.NONE);
            label.setText(AppProjectConf.I18N.getString("AddLocalLibraryDialog.Panel.label")); //$NON-NLS-1$
            
            this.text = new Text(data, SWT.BORDER);
            this.text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            
            Button button = new Button(data, SWT.PUSH);
            button.setImage(UIImages.FILECHOOSE);
            
            button.addSelectionListener(new SelectionListener() {
            
                @Override
                public void widgetSelected(SelectionEvent e) {
                    FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
                    fd.setText(AppProjectConf.I18N.getString("AddLocalLibraryDialog.Panel.select")); //$NON-NLS-1$
                    String[] filterExt = { "*.ramc" }; //$NON-NLS-1$
                    fd.setFilterExtensions(filterExt);
                    String selected = fd.open();
                    if (selected != null) {
                        RamcFragmentPanel.this.text.setText(selected);
                        AddLocalLibraryDialog.this.showFragmentInfos();
                        // test exist
                        isFragmentValid();
                    }
                }
            
                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    // nothing to do
                }
            
            });
            data.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
            return;
        }

        @objid ("7d55b972-3adc-11e2-916e-002564c97630")
        public void setEdited(IProjectFragment fragment) {
            this.text.setText(fragment.getUri().toString());
        }

    }

}
