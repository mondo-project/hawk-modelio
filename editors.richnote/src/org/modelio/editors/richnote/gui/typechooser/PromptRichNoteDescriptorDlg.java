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
                                    

package org.modelio.editors.richnote.gui.typechooser;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.nebula.jface.tablecomboviewer.TableComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.core.ui.dialog.ModelioDialog;
import org.modelio.editors.richnote.api.RichNoteFormat;
import org.modelio.editors.richnote.api.RichNoteFormatRegistry;
import org.modelio.editors.richnote.api.SupportLevel;
import org.modelio.editors.richnote.plugin.EditorsRichNote;
import org.modelio.gproject.model.IMModelServices;

/**
 * External document creation dialog.
 * <p>
 * Asks for: <ul>
 * <li> a name
 * <li> a document type
 * <li> a document MIME type
 * <li> an abstract
 * </ul>
 * @author cmarin
 */
@objid ("aa8ab13d-00a2-4ee7-8a5c-f68ec47be66b")
public class PromptRichNoteDescriptorDlg extends ModelioDialog {
    @objid ("0f423b7d-6161-4020-99c6-94382f18b571")
    private static final String HELP_TOPIC = "/org.modelio.documentation.modeler/html/Modeler-_modeler_building_models_add_richnotes.html";

    @objid ("6eeb1c0e-4fe1-4db1-acbc-95b5100e5b8f")
    private DocTypeChooserDriver docTypeDriver;

    @objid ("3faa4057-583a-4462-a3eb-c54f406f9202")
    private RichNoteDescriptor richNoteDescriptor;

    @objid ("793a6d77-3823-4891-8955-c07d5922eb2a")
    private StructuredViewer docTypeViewer;

    @objid ("27f411b6-fd89-4271-a0cd-0a7dc366eca5")
    private Text nameText;

    @objid ("395699cb-e481-4faa-8947-98901683b44e")
    private StructuredViewer mimeTypeViewer;

    @objid ("64690dfb-39cc-4aee-bf0a-a311123f240f")
    private Text abstractText;

    /**
     * Initialize the dialog.
     * @param parentShell the parent shell
     * @param documentDescriptor initial values for the descriptor, the descriptor will be updated with user chosen values on dialog return
     * @param modelService model search service
     */
    @objid ("b13a028c-0f14-4e80-b184-5c2f469810a9")
    public PromptRichNoteDescriptorDlg(final Shell parentShell, final RichNoteDescriptor documentDescriptor, final IMModelServices modelService) {
        super(parentShell);
        this.richNoteDescriptor = documentDescriptor;
        this.docTypeDriver = new DocTypeChooserDriver(modelService);
    }

    @objid ("73d27cbd-a530-4359-aa3d-ba1c7d1f8e4a")
    @Override
    public void addButtonsInButtonBar(final Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @objid ("15ad8ee6-1567-4f29-b333-cd5113067509")
    @Override
    public Control createContentArea(final Composite parent) {
        Composite acomposite = new Composite(parent, 0);
        GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        
        acomposite.setLayoutData(data);
        acomposite.setFont(parent.getFont());
        
        GridLayout compositeLayout = new GridLayout(1, false);
        compositeLayout.marginWidth = 3;
        compositeLayout.marginHeight = 3;
        compositeLayout.verticalSpacing = 0;
        compositeLayout.horizontalSpacing = 0;
        acomposite.setLayout(compositeLayout);
        
        
        // Create left viewer:
        Composite leftViewerComposite = new Composite(acomposite, SWT.NONE);
        GridData leftViewerCompositeData = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        leftViewerComposite.setLayoutData(leftViewerCompositeData);
        FormLayout leftViewerLayout = new FormLayout();
        leftViewerComposite.setLayout(leftViewerLayout);
        FormData formData ;
        
        // Name field
        Label nameLabel = new Label(leftViewerComposite, SWT.NONE);
        nameLabel.setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.Name.Label"));
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        //formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(0, 3);
        //formData.bottom = new FormAttachment(100, 0);
        nameLabel.setLayoutData(formData);
        
        
        this.nameText = new Text(leftViewerComposite, SWT.SINGLE | SWT.BORDER);
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(nameLabel, 3);
        //formData.bottom = new FormAttachment(100, 0);
        this.nameText.setLayoutData(formData);
        
        // Document type
        Label label = new Label(leftViewerComposite, SWT.NONE);
        label.setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.DocType.Label"));
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(this.nameText, 3);
        label.setLayoutData(formData);
        
        
        this.docTypeViewer = this.docTypeDriver.createViewer(leftViewerComposite);
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(label, 3);
        this.docTypeViewer.getControl().setLayoutData(formData);
        
        // Document MIME type
        Label mimeTypeLabel = new Label(leftViewerComposite, SWT.NONE);
        mimeTypeLabel.setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.MimeType.Label"));
        
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(this.docTypeViewer.getControl(), 3);
        mimeTypeLabel.setLayoutData(formData);
        
        this.mimeTypeViewer = new TableComboViewer(leftViewerComposite, SWT.BORDER | SWT.READ_ONLY);
        this.mimeTypeViewer.setContentProvider(new MimeTypeContentProvider());
        this.mimeTypeViewer.setLabelProvider(new MimeTypeLabelProvider());
        this.mimeTypeViewer.setSorter(new ViewerSorter());
        this.mimeTypeViewer.setInput(this.richNoteDescriptor.getTargetElement());
        
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(mimeTypeLabel, 3);
        this.mimeTypeViewer.getControl().setLayoutData(formData);
        
        // Abstract field
        Label abstractLabel = new Label(leftViewerComposite, 0);
        abstractLabel.setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.Abstract.Label"));
        
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.top = new FormAttachment(this.mimeTypeViewer.getControl(), 3);
        abstractLabel.setLayoutData(formData);
        
        this.abstractText = new Text(leftViewerComposite, SWT.MULTI | SWT.BORDER);
        this.abstractText.setToolTipText(EditorsRichNote.I18N.getString("CreateDocumentDlg.Abstract.Tooltip"));
        
        formData = new FormData();
        formData.left = new FormAttachment(0, 0);
        formData.right = new FormAttachment(100, 0);
        formData.top = new FormAttachment(abstractLabel, 3);
        formData.bottom = new FormAttachment(100, 0);
        this.abstractText.setLayoutData(formData);
        return acomposite;
    }

    /**
     * @return the dialog data model.
     */
    @objid ("981b1ba9-5382-4731-8aef-307d21b99dcf")
    public RichNoteDescriptor getRichNoteDescriptor() {
        return this.richNoteDescriptor;
    }

    @objid ("cad84cdd-e04b-40da-b4bc-bd9f0ec536bd")
    @Override
    public void init() {
        getShell().setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.dlg.Title"));
        setTitle(EditorsRichNote.I18N.getString("CreateDocumentDlg.dlg.Title"));
        setMessage(EditorsRichNote.I18N.getString("CreateDocumentDlg.dlg.Message"));
        
        this.docTypeDriver.init(this.richNoteDescriptor.getTargetElement());
        
        // Set a default mime type
        RichNoteFormat defaultFormat = RichNoteFormatRegistry.getInstance().getDocumentFormatForMime("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        if (defaultFormat== null || 
                !defaultFormat.isUsable() || 
                defaultFormat.getSupportLevel() != SupportLevel.Primary) {
            defaultFormat = RichNoteFormatRegistry.getInstance().getDocumentFormatForMime("application/vnd.oasis.opendocument.text");
        }
        this.mimeTypeViewer.setSelection(new StructuredSelection(defaultFormat));
        
        // Set a default name
        this.nameText.setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.dlg.DefaultName"));
        
        // Set a default abstract
        this.abstractText.setText(EditorsRichNote.I18N.getString("CreateDocumentDlg.EnterRichNoteAbstract"));
        
        Shell parentShell = getShell().getParent().getShell();
        Point shellLocation = parentShell.getLocation();
        getShell().setSize(500, 500);
        getShell().setLocation(shellLocation.x + 300, shellLocation.y + 300);
        setLogoImage(null);
    }

    @objid ("573c9bf3-e08b-4f88-8d18-49f3c440afb5")
    @Override
    protected void okPressed() {
        if (this.docTypeDriver.getSelection() == null) {
            setErrorMessage(EditorsRichNote.I18N.getString("CreateDocumentDlg.DocType.Missing"));
            return;
        }
        
        IStructuredSelection mimeSel = (IStructuredSelection) this.mimeTypeViewer.getSelection();
        if (mimeSel==null || mimeSel.isEmpty()) {
            setErrorMessage(EditorsRichNote.I18N.getString("CreateDocumentDlg.MimeType.Missing"));
            return;
        }
        
        setErrorMessage(null);
        
        final RichNoteFormat firstFormat = (RichNoteFormat) mimeSel.getFirstElement();
        
        
        this.richNoteDescriptor.setAbstract(this.abstractText.getText());
        this.richNoteDescriptor.setMimeType(firstFormat.getMimeType());
        this.richNoteDescriptor.setPath("");
        this.richNoteDescriptor.setName(this.nameText.getText());
        this.richNoteDescriptor.setDocumentType(this.docTypeDriver.getSelection());
        
        super.okPressed();
    }

    @objid ("5cbef1ee-83e8-4144-b764-1b5221a9e28d")
    @Override
    protected String getHelpId() {
        return HELP_TOPIC;
    }

}
