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
                                    

package org.modelio.core.ui.ktable.types.text;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.core.ui.plugin.CoreUi;
import org.modelio.metamodel.uml.infrastructure.Element;

@objid ("8db83cc6-c068-11e1-8c0a-002564c97630")
public class EditionDialog extends ModelioDialog {
    @objid ("a48d73c8-c068-11e1-8c0a-002564c97630")
    private String content;

    @objid ("8db83cc9-c068-11e1-8c0a-002564c97630")
     boolean dialogActive = false;

    @objid ("8db83cca-c068-11e1-8c0a-002564c97630")
    private Element editedElement;

    @objid ("a48fd526-c068-11e1-8c0a-002564c97630")
    private String fieldName;

    @objid ("6f9640ad-de8d-4a76-aed5-b0b815bdbbcc")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Index.html";

    @objid ("8db83cc8-c068-11e1-8c0a-002564c97630")
    private MultilineTextCellEditor editor = null;

    @objid ("8db83cce-c068-11e1-8c0a-002564c97630")
    private Text textfield = null;

    @objid ("ed203f4c-08e4-42bf-a0d1-c059edee0cb4")
    protected static EditionDialog instance = null;

    @objid ("8db83ccf-c068-11e1-8c0a-002564c97630")
    private EditionDialog(final Shell parentShell, final MultilineTextCellEditor editor, final String initialContent) {
        super(parentShell);
        this.content = initialContent;
        this.editor = editor;
        this.editedElement = editor.getEditedElement();
        this.fieldName = editor.getFieldName();
        
        // Set the shell style so that the dialog is not modal.
        int style = getShellStyle();
        style &= ~(SWT.PRIMARY_MODAL | SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL);
        style |= SWT.MODELESS;
        setShellStyle(style);
    }

    @objid ("8db83cd7-c068-11e1-8c0a-002564c97630")
    @Override
    public void addButtonsInButtonBar(final Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, false);
    }

    @objid ("8db83cdc-c068-11e1-8c0a-002564c97630")
    @Override
    public Control createContentArea(final Composite parent) {
        final Composite composite = new Composite(parent, 0);
        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setFont(parent.getFont());
        
        // Text field right to the Name: label
        this.textfield = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP);
        this.textfield.setText(this.content);
        this.textfield.selectAll();
        this.textfield.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        // Prevent CR from going to the default button
        this.textfield.addTraverseListener(new TraverseListener() {
            @Override
            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_RETURN) {
                    e.doit = false;
                    e.detail = SWT.TRAVERSE_NONE;
                }
            }
        });
        return composite;
    }

    @objid ("8db83ce3-c068-11e1-8c0a-002564c97630")
    public String getContent() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.content;
    }

    @objid ("8db83ce7-c068-11e1-8c0a-002564c97630")
    public MultilineTextCellEditor getEditor() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.editor;
    }

    @objid ("8db83ceb-c068-11e1-8c0a-002564c97630")
    @Override
    public void init() {
        setLogoImage(null);
        
        // TODO CHM metaclass name
        final String title = this.editedElement.getMClass().getName() + " - " + this.fieldName;
        
        getShell().setText(title);
        setTitle(title);
        setMessage(CoreUi.I18N.getString("KTable.MultipleTextInputHelp"));
        
        this.dialogActive = true;
    }

    @objid ("8db83cee-c068-11e1-8c0a-002564c97630")
    @Override
    public int open() {
        return super.open();
    }

    @objid ("8db9c368-c068-11e1-8c0a-002564c97630")
    public void setContent(final String newContent) {
        this.content = newContent;
    }

    @objid ("8db9c36c-c068-11e1-8c0a-002564c97630")
    @Override
    protected void cancelPressed() {
        this.editor.closeEditor(false);
        this.dialogActive = false;
        super.cancelPressed();
    }

    @objid ("8db9c375-c068-11e1-8c0a-002564c97630")
    @Override
    protected void okPressed() {
        this.dialogActive = false;
        this.editor.setContent(this.textfield.getText());
        super.okPressed();
    }

    @objid ("8db9c378-c068-11e1-8c0a-002564c97630")
    @Override
    protected Point getInitialSize() {
        return new Point(800, 600);
    }

    @objid ("476b2eee-40a2-4d22-bf7b-9b07ba2490a2")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

    @objid ("b34e06db-2c20-4dda-82ff-2f032e134251")
    @Override
    public boolean close() {
        if (this.equals(instance)) { // should always be the case, could be an assert!
            instance = null;
        }
        return super.close();
    }

    @objid ("0d195bc6-46b8-4f6d-9cdb-236e048e88fb")
    public static EditionDialog getInstance(final Shell parentShell, final MultilineTextCellEditor editor, final String initialContent) {
        if (parentShell == null)
            return null;
        
        if (instance != null) {
            return instance;
        }
        
        instance = new EditionDialog(parentShell, editor, initialContent);
        return instance;
    }

    @objid ("347520b4-8d01-44cd-85d6-f85295ad805c")
    public static void closeInstance() {
        if (instance != null) {
            Display.getDefault().asyncExec(new Runnable() {        
                @Override
                public void run() {
                    instance.close();
                }
            });
        }
    }

}
