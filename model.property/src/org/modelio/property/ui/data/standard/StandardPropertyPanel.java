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
                                    

package org.modelio.property.ui.data.standard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableModel;
import de.kupzog.ktable.SWTX;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.ktable.IPropertyModel;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.property.ui.data.DataPanelInput;
import org.modelio.property.ui.data.IPropertyPanel;
import org.modelio.property.ui.data.ModelioKTable;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * Standard UML properties panel.
 */
@objid ("8fa5670f-c068-11e1-8c0a-002564c97630")
public class StandardPropertyPanel implements IPropertyPanel {
    @objid ("8fa56710-c068-11e1-8c0a-002564c97630")
    private final PropertyModelFactory propertyModelFactory;

    @objid ("8fa56712-c068-11e1-8c0a-002564c97630")
    private Composite comp = null;

    @objid ("f9d188b0-c5d4-11e1-8f21-002564c97630")
    private KTable table = null;

    @objid ("86a0580f-cf24-11e1-80a9-002564c97630")
    private Element typedElement;

    @objid ("8fa7c825-c068-11e1-8c0a-002564c97630")
    public StandardPropertyPanel(Composite parent, Element typedElement) {
        this.typedElement = typedElement;
        this.propertyModelFactory = new PropertyModelFactory();
        createGUI(parent);
    }

    @objid ("8fa7c82b-c068-11e1-8c0a-002564c97630")
    @Override
    public void setInput(DataPanelInput newInput) {
        if (newInput == null) {
            this.typedElement = null;
            final KTableModel model = this.propertyModelFactory.getIPropertyModel(null, null, this.table, null);
            this.table.setModel(model);
        } else {
            this.typedElement = newInput.getTypedElement();
            IProjectService projectService = newInput.getProjectService();
            ICoreSession session = projectService != null ? projectService.getSession() : null;
            final DataModelFactory f = new DataModelFactory(newInput.getModelService(), projectService, newInput.getActivationService(), session != null ? session.getModel() : null);
            final IPropertyModel data = f.getPropertyModel(this.typedElement);
            final KTableModel model = this.propertyModelFactory.getIPropertyModel(session, newInput.getPickingService(), this.table, data);
            this.table.setModel(model);
        }
    }

    @objid ("8fa7c830-c068-11e1-8c0a-002564c97630")
    @Override
    public void stop() {
        setInput(null);
        disableGUI();
    }

    @objid ("8fa7c832-c068-11e1-8c0a-002564c97630")
    @Override
    public void start() {
        // Nothing to do
    }

    @objid ("8fa7c834-c068-11e1-8c0a-002564c97630")
    @Override
    public void disableGUI() {
        // Nothing to do
    }

    @objid ("8fa7c836-c068-11e1-8c0a-002564c97630")
    @Override
    public void enableGUI() {
        // Nothing to do
    }

    @objid ("8fa7c838-c068-11e1-8c0a-002564c97630")
    private void createGUI(Composite parent) {
        // Create the composite to hold the controls
        this.comp = new Composite(parent, SWT.BORDER | SWT.NO_BACKGROUND);
        final GridLayout layout = new GridLayout(1, false);
        layout.horizontalSpacing = 0;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        
        this.comp.setLayout(layout);
        // comp.setBackground(comp.getDisplay().getSystemColor(SWT.COLOR_BLUE));
        
        final int tableStyle = SWTX.AUTO_SCROLL | SWTX.FILL_WITH_LASTCOL;
        this.table = new ModelioKTable(this.comp, tableStyle);
        
        final GridData gridData = new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gridData.minimumHeight = 20;
        this.table.setLayoutData(gridData);
        this.table.setBackground(this.table.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    }

    @objid ("8fa7c83b-c068-11e1-8c0a-002564c97630")
    @Override
    public Composite getComposite() {
        return this.comp;
    }

    /**
     * Refresh the panel from its current input.
     */
    @objid ("8fa7c840-c068-11e1-8c0a-002564c97630")
    @Override
    public void refresh() {
        if (!this.table.isDisposed()) {
            this.table.redraw();
        }
    }

}
