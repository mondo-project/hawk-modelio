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
                                    

package org.modelio.property.ui;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.core.picking.IModelioPickingService;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.model.browser.views.treeview.ModelBrowserPanelProvider;

/**
 * Dialog box displaying the properties of a model element, just like the Element view. Gathers the UML properties and tagged values
 * tables.
 */
@objid ("8fa7c852-c068-11e1-8c0a-002564c97630")
public class EditPropertiesDialog extends ModelioDialog {
    @objid ("d364f8f7-276d-41c9-a9df-6915151d5ada")
    private Element editedElement;

    @objid ("f0c962ee-e2fc-4d07-8932-b76199d2c421")
    private ISelectionChangedListener browserSelectionListener;

    @objid ("b0a647b4-1790-4d97-8e5c-5b5080ac916e")
    public ModelPropertyPanelProvider propertyPanel;

    @objid ("c19c0510-8165-4036-b0f7-13fb445e3283")
    private final IProjectService projectService;

    @objid ("16850b4c-2f32-455e-8369-d70e7d6b4c7b")
    private final IMModelServices modelService;

    @objid ("42abb1cf-3ab4-4851-a4c8-39f36cf683ad")
    private ModelBrowserPanelProvider browserPanel;

    @objid ("8f561054-fb47-4d82-b188-595ab2546561")
    private final IModelioPickingService pickingService;

    @objid ("db1313c9-9783-4900-8b1a-bc61f4bc50a9")
    private final IActivationService activationService;

    @objid ("8fa7c85a-c068-11e1-8c0a-002564c97630")
    @Override
    public Control createContentArea(final Composite parent) {
        SashForm shform = new SashForm(parent, SWT.HORIZONTAL);
        shform.setLayoutData(new GridData(GridData.FILL_BOTH));
        shform.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        // Create the browser panel
        this.browserPanel = new ModelBrowserPanelProvider();
        this.browserPanel.createPanel(shform);
        this.browserSelectionListener = new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                if (selection != null && selection.size() == 1) {
                    for (Object selectedElement : selection.toList()) {
                        if (selectedElement instanceof Element) {
                            EditPropertiesDialog.this.propertyPanel.setInput(selectedElement);
                            return;
                        }
                    }
                }
            }
        };
        this.browserPanel.getPanel().addSelectionChangedListener(this.browserSelectionListener);
        
        // Create the property panel
        this.propertyPanel = new ModelPropertyPanelProvider();
        this.propertyPanel.activateEdition(this.projectService, this.projectService.getSession(), this.modelService, this.pickingService, this.activationService);
        this.propertyPanel.createPanel(shform);
        this.propertyPanel.disableAutoLayout();
        this.propertyPanel.setHorizontalLayout();
        
        // Init the view
        shform.setWeights(new int[] { 40, 60 });
        this.browserPanel.setInput(this.projectService.getOpenedProject());
        
        List<Object> roots = new ArrayList<>();
        roots.add(this.editedElement);
        this.browserPanel.setLocalRoots(roots);
        
        // Select the root
        this.browserPanel.getPanel().setSelection(new StructuredSelection(this.editedElement));
        return shform;
    }

    @objid ("bb8a8c1c-4ad4-4d4d-a559-42821fc7c82f")
    public EditPropertiesDialog(IProjectService projectService, IMModelServices modelService, IModelioPickingService pickingService, IActivationService activationService, Shell parentShell) {
        super(parentShell);
        this.projectService = projectService;
        this.modelService = modelService;
        this.pickingService = pickingService;
        this.activationService = activationService;
    }

    @objid ("28287d4a-6941-4896-8ddf-0c8415c7dcb9")
    public Element getEditedElement() {
        return editedElement;
    }

    @objid ("d244f2f1-e5fb-4437-845e-7d405b05b4c5")
    public void setEditedElement(Element editedElement) {
        this.editedElement = editedElement;
    }

    @objid ("150dc9ef-37fa-43dc-a8ee-61b1de036c00")
    @Override
    public void addButtonsInButtonBar(Composite parent) {
        // TODO Auto-generated method stub
    }

    @objid ("71bfee22-df63-4672-a7cc-4be8a23a019c")
    @Override
    public void init() {
        // TODO Auto-generated method stub
    }

}
