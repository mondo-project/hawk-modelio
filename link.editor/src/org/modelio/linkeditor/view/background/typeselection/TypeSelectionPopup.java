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
                                    

package org.modelio.linkeditor.view.background.typeselection;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.linkeditor.plugin.LinkEditor;
import org.modelio.metamodel.uml.infrastructure.Stereotype;

/**
 * Dialog used to resolve ambiguity on the type a link to create
 * 
 * @author fpoyer
 */
@objid ("1b9865cd-5e33-11e2-b81d-002564c97630")
public class TypeSelectionPopup extends ModelioDialog {
    @objid ("4ba3b8ec-6713-4393-8cf6-91091cdde99f")
    private Composite composite;

    @objid ("d25e7579-91d3-4122-8b2b-408842db0163")
    private Button okButton;

    @objid ("1b9865cf-5e33-11e2-b81d-002564c97630")
     TypeSelectionModel model;

    /**
     * C'tor.
     * @param shell The shell to use to create this dialog.
     * @param model The model to use for this popup.
     */
    @objid ("1b9865d2-5e33-11e2-b81d-002564c97630")
    public TypeSelectionPopup(final Shell shell, final TypeSelectionModel model) {
        super(shell);
        this.model = model;
        setShellStyle(SWT.CLOSE |
                      SWT.MIN |
                      SWT.MAX |
                      SWT.RESIZE |
                      SWT.TITLE |
                      SWT.BORDER |
                      SWT.APPLICATION_MODAL |
                      getDefaultOrientation());
    }

    @objid ("1b9865d9-5e33-11e2-b81d-002564c97630")
    @Override
    public Control createContentArea(final Composite parent) {
        this.composite = new Composite(parent, SWT.NONE);
        this.composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.composite.setLayout(new GridLayout(1, true));
        TreeViewer treeViewer = new TreeViewer(this.composite, SWT.BORDER);
        treeViewer.setContentProvider(new TypeSelectionContentProvider(this.model));
        treeViewer.setLabelProvider(new TypeSelectionLabelProvider());
        treeViewer.setInput(this.model);
        // TODO: decide initial state: expanded or not?
        treeViewer.expandAll();
        treeViewer.setAutoExpandLevel(2);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        treeViewer.getTree().setLayoutData(gridData);
        
        treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                ISelection selection = event.getSelection();
                if (selection instanceof StructuredSelection) {
                    Object selectedObject = ((StructuredSelection) selection).getFirstElement();
                    TypeSelectionPopup.this.model.setSelectedType(selectedObject);
                    updateButtons(selectedObject);
                }
            }
        });
        return this.composite;
    }

    @objid ("1b9865e0-5e33-11e2-b81d-002564c97630")
    @Override
    public void addButtonsInButtonBar(final Composite parent) {
        this.okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @objid ("1b9865e5-5e33-11e2-b81d-002564c97630")
    @Override
    public void init() {
        // Put the window title, dialog title and dialog message
        getShell().setText(LinkEditor.I18N.getMessage("TypeSelectionPopup.WindowTitle"));
        setTitle(LinkEditor.I18N.getMessage("TypeSelectionPopup.DialogTitle"));
        setMessage(LinkEditor.I18N.getMessage("TypeSelectionPopup.DialogMessage"));
        
        // Set minimum size
        this.getShell().setMinimumSize(400, 300);
    }

    @objid ("1b9865e8-5e33-11e2-b81d-002564c97630")
    @Override
    protected Point getInitialSize() {
        return new Point(400, 400);
    }

    @objid ("1b9865ed-5e33-11e2-b81d-002564c97630")
    @Override
    protected Point getInitialLocation(final Point initialSize) {
        // TODO fetch the LinkEditor control and center on it.
        return super.getInitialLocation(initialSize);
    }

    @objid ("1b9ac6ee-5e33-11e2-b81d-002564c97630")
    void updateButtons(final Object selection) {
        if (selection instanceof Class<?> || selection instanceof Stereotype) {
            this.okButton.setEnabled(true);
        } else {
            this.okButton.setEnabled(false);
        }
    }

}
