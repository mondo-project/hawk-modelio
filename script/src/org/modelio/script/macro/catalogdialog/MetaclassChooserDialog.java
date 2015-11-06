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
                                    

package org.modelio.script.macro.catalogdialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.images.MetamodelImageService;
import org.modelio.script.plugin.Script;
import org.modelio.vcore.smkernel.meta.SmClass;

@objid ("00696928-c497-106a-bf4f-001ec947cd2a")
class MetaclassChooserDialog extends ModelioDialog {
    @objid ("006aa964-c497-106a-bf4f-001ec947cd2a")
    private final List<String> leftValues = new ArrayList<>();

    @objid ("006aaa68-c497-106a-bf4f-001ec947cd2a")
    private final List<String> rightValues = new ArrayList<>();

    @objid ("a3ad7f4f-882a-4fe0-aa40-2dfcb8257a4a")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_modelio_settings_macros_catalog.html";

    @objid ("00699628-c497-106a-bf4f-001ec947cd2a")
    private Button addButton;

    @objid ("00699740-c497-106a-bf4f-001ec947cd2a")
    private Button removeButton;

    @objid ("006d0038-071b-106c-bf4f-001ec947cd2a")
    private Table leftTree;

    @objid ("006d0952-071b-106c-bf4f-001ec947cd2a")
    private Table rightTree;

    @objid ("006aab6c-c497-106a-bf4f-001ec947cd2a")
    void handleAdd() {
        for (TableItem i : this.leftTree.getSelection()) {
            String val = i.getText();
            this.leftValues.remove(val);
            this.rightValues.add(val);
        
            Collections.sort(this.leftValues);
            Collections.sort(this.rightValues);
        
            TableItem rightItem = new TableItem(this.rightTree, SWT.NONE, this.rightValues.indexOf(val));
            rightItem.setText(val);
            rightItem.setImage(i.getImage());
        
            i.dispose();
        }
    }

    @objid ("006aac16-c497-106a-bf4f-001ec947cd2a")
    void handleRemove() {
        for (TableItem i : this.rightTree.getSelection()) {
            String val = i.getText();
            this.rightValues.remove(val);
            this.leftValues.add(val);
        
            Collections.sort(this.leftValues);
            Collections.sort(this.rightValues);
        
            TableItem rightItem = new TableItem(this.leftTree, SWT.NONE, this.leftValues.indexOf(val));
            rightItem.setText(val);
            rightItem.setImage(i.getImage());
        
            i.dispose();
        
        }
    }

    /**
     * Get the selected metaclasses.
     * @return the selected metaclasses.
     */
    @objid ("006aacc0-c497-106a-bf4f-001ec947cd2a")
    public Collection<String> getSelection() {
        return this.rightValues;
    }

    @objid ("00697b8e-c497-106a-bf4f-001ec947cd2a")
    public MetaclassChooserDialog(Shell parentShell, Collection<String> initValues) {
        super(parentShell);
        
        for (SmClass smClass : SmClass.getRegisteredClasses()) {
            this.leftValues.add(smClass.getName());
        }
        this.rightValues.addAll(initValues);
        
        Collections.sort(this.leftValues);
        Collections.sort(this.rightValues);
    }

    /**
     * Update the dialog box content
     */
    @objid ("006aad56-c497-106a-bf4f-001ec947cd2a")
    public void update() {
        this.leftTree.removeAll();
        this.rightTree.removeAll();
        
        for (String s : this.leftValues) {
            TableItem item = new TableItem(this.leftTree, SWT.NONE);
            item.setText(s);
            item.setImage(getMetaclassImage(s));
        }
        
        for (String s : this.rightValues) {
            TableItem item = new TableItem(this.rightTree, SWT.NONE);
            item.setText(s);
            item.setImage(getMetaclassImage(s));
        }
    }

    @objid ("0069aaf0-c497-106a-bf4f-001ec947cd2a")
    private Image getMetaclassImage(String s) {
        return MetamodelImageService.getIcon(SmClass.getClass(s));
    }

    @objid ("0069ada2-c497-106a-bf4f-001ec947cd2a")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
    }

    @objid ("0069ae38-c497-106a-bf4f-001ec947cd2a")
    @Override
    public Control createContentArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridDataFactory.defaultsFor(composite).grab(true, true).applyTo(composite);
        
        // Left list
        this.leftTree = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        this.leftTree.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                MetaclassChooserDialog.this.handleAdd();
            }
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Nothing to do.
            }
        });
        
        // Buttons
        Composite buttonsGroup = new Composite(composite, SWT.NONE);
        new Composite(buttonsGroup, SWT.NONE);
        
        this.addButton = new Button(buttonsGroup, SWT.PUSH);
        this.removeButton = new Button(buttonsGroup, SWT.PUSH);
        
        new Composite(buttonsGroup, SWT.NONE);
        GridLayoutFactory.swtDefaults().generateLayout(buttonsGroup);
        
        this.addButton.addSelectionListener(new AddButtonSelectionListener());
        this.addButton.setText(Script.I18N.getString("MetaclassChooserDialog.AddButton"));
        
        this.removeButton.addSelectionListener(new RemoveButtonSelectionListener());
        this.removeButton.setText(Script.I18N.getString("MetaclassChooserDialog.RemoveButton"));
        
        // right list
        this.rightTree = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
        this.rightTree.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                MetaclassChooserDialog.this.handleRemove();
            }
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                // Nothing to do.
            }
        });
        
        update();
        
        GridLayoutFactory.swtDefaults().numColumns(3).generateLayout(composite);
        return composite;
    }

    @objid ("006aade2-c497-106a-bf4f-001ec947cd2a")
    @Override
    public void init() {
        getShell().setText(Script.I18N.getString("MetaclassChooserDialog.Title"));
        setTitle(Script.I18N.getString("MetaclassChooserDialog.Title"));
        this.setMessage(Script.I18N.getString("MetaclassChooserDialog.Description"));
        
        getShell().setMinimumSize(600, 400);
    }

    @objid ("55171f9b-173c-437f-bd8a-ada62af38147")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("006969be-c497-106a-bf4f-001ec947cd2a")
    class AddButtonSelectionListener implements SelectionListener {
        @objid ("0069ccf6-c497-106a-bf4f-001ec947cd2a")
        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // Nothing to do.
        }

        @objid ("0069cd8c-c497-106a-bf4f-001ec947cd2a")
        @Override
        public void widgetSelected(SelectionEvent e) {
            handleAdd();
        }

    }

    @objid ("00696a4a-c497-106a-bf4f-001ec947cd2a")
    class RemoveButtonSelectionListener implements SelectionListener {
        @objid ("0069ce90-c497-106a-bf4f-001ec947cd2a")
        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // Nothing to do.
        }

        @objid ("0069cf26-c497-106a-bf4f-001ec947cd2a")
        @Override
        public void widgetSelected(SelectionEvent e) {
            handleRemove();
        }

    }

}
