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
                                    

package org.modelio.property.ui.data.stereotype;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.modelio.metamodel.mda.ModuleComponent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.property.ui.data.DataPanelInput;
import org.modelio.property.ui.data.IPropertyPanel;
import org.modelio.property.ui.data.stereotype.data.TableDataModel;
import org.modelio.property.ui.data.stereotype.model.PropertyTableModel;

/**
 * Implementation of the "Extensions" part of the property panel, available when a module is selected in it. Uses a KTable to
 * display all non hidden tag types provided by this module for the 'editedElement' (i.e. the selected element given by the
 * platform).
 */
@objid ("5efe1443-4f4a-412d-87fc-55e6de46f667")
public class GenericPropertyPanel implements IPropertyPanel {
    @objid ("8d9c7e47-5c17-4682-9006-e0d10fe47737")
    private ModelElement editedElement;

    @objid ("b38a9cac-90e9-46b2-b1af-d3109ad437e9")
    private Stereotype stereotype;

    @objid ("8f9840c9-227d-4120-8b1e-ab2a7d0c9829")
    private PropertyTableViewer tableViewer;

    @objid ("4b74e4a9-97ef-4ded-8a15-183b735ae65d")
    private ModuleComponent module;

    @objid ("d8d5ea8b-865c-49bb-bbf4-ecda80e7a634")
    private Composite comp;

    @objid ("9eaa7dd8-78da-4129-9aac-f1fc87bce3fd")
    public GenericPropertyPanel(final Composite parent, final ModelElement editedElement, final Stereotype stereotype) {
        this.editedElement = editedElement;
        this.stereotype = stereotype;
        createGUI(parent);
    }

    @objid ("e317f931-6215-4873-96ae-f2588049b01a")
    @Override
    public void setInput(DataPanelInput newInput) {
        if (newInput == null) {
            this.tableViewer.getControl().dispose();
            return;
        }
        
        // Data model for the editor
        TableDataModel dataModel = this.stereotype != null ? new TableDataModel(new PropertyTableModel(this.editedElement, this.stereotype, newInput.getModelService())) : new TableDataModel(new PropertyTableModel(this.editedElement, this.module, newInput.getModelService()));
        
        this.tableViewer.setProjectService(newInput.getProjectService());
        this.tableViewer.setPickingService(newInput.getPickingService());
        
        this.tableViewer.setInput(dataModel);
    }

    @objid ("98b4f8ef-a5e4-4146-9747-ed4e0dbe224c")
    @Override
    public void stop() {
        setInput(null);
        disableGUI();
    }

    @objid ("03925c0f-257a-4bdf-9526-9cb6c94f0f78")
    @Override
    public void start() {
        // Nothing to do
    }

    @objid ("91ea2a08-3986-4b85-b456-74d78154c4ae")
    @Override
    public void disableGUI() {
        // Nothing to do
    }

    @objid ("e2a7ee2a-e002-45dd-b626-1bd1058fdc23")
    @Override
    public void enableGUI() {
        // Nothing to do
    }

    @objid ("62fd3176-5a0c-4000-b723-a65047fe2528")
    @Override
    public Composite getComposite() {
        return this.comp;
    }

    @objid ("3a6543be-0e05-40d7-8120-b83cadc72886")
    @Override
    public void refresh() {
        if (!this.comp.isDisposed()) {
            this.comp.redraw();
        }
    }

    @objid ("bcde8807-cb94-476e-8157-c5c5103c46d9")
    private void createGUI(final Composite parent) {
        // Create the composite to hold the controls
        this.comp = new Composite(parent, SWT.BORDER | SWT.NO_BACKGROUND);
        final GridLayout layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        
        // Create the nattable
        this.tableViewer = new PropertyTableViewer(this.comp);
        
        final GridData gridData = new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gridData.minimumHeight = 20;
        this.tableViewer.getControl().setLayoutData(gridData);
        
        this.comp.setLayout(layout);
    }

    @objid ("945d03cc-18bc-4291-98e0-f9e9a0d78ca0")
    public GenericPropertyPanel(final Composite parent, final ModelElement editedElement, final ModuleComponent module) {
        this.editedElement = editedElement;
        this.module = module;
        createGUI(parent);
    }

}
