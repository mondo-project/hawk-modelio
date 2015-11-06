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
                                    

package org.modelio.editors.richnote.gui.handler;

import java.io.IOException;
import javax.inject.Named;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.editors.richnote.api.RichNoteCreator;
import org.modelio.editors.richnote.gui.typechooser.PromptRichNoteDescriptorDlg;
import org.modelio.editors.richnote.gui.typechooser.RichNoteDescriptor;
import org.modelio.editors.richnote.plugin.EditorsRichNote;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;
import org.modelio.vcore.session.api.transactions.ITransactionSupport;
import org.modelio.vcore.session.impl.CoreSession;
import org.modelio.vcore.smkernel.IllegalModelManipulationException;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Handler for 'Add rich note' button.
 */
@objid ("9cfe3a9b-afdb-4151-81e7-63c1c66305a9")
public class AddRichNoteHandler {
    @objid ("216ef0d3-738b-4c29-929e-60f694cb1d25")
    @CanExecute
    final boolean canExecute(@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection) {
        // Must have and only one selected element
        MObject element = getSelectedElement(selection);
        
        if (element == null || !element.getStatus().isModifiable()) {
            return false;
        }
        return true;
    }

    @objid ("dedd985f-efef-4bb0-a219-9b14d3a682b1")
    private ModelElement getSelectedElement(IStructuredSelection selection) {
        if (selection.size() == 1) {
            Object first = selection.getFirstElement();
            if (first instanceof ModelElement) {
                return (ModelElement) first;
            } else if (first instanceof IAdaptable) {
                final ModelElement adapter = (ModelElement) ((IAdaptable) first).getAdapter(ModelElement.class);
                if (adapter != null) {
                    return adapter;
                }
            }
        }
        return null;
    }

    @objid ("90c10778-0498-49c4-8fa1-5077685e59c9")
    @Execute
    void execute(@Optional
@Named(IServiceConstants.ACTIVE_SHELL) Shell parentShell, @Optional
@Named(IServiceConstants.ACTIVE_SELECTION) IStructuredSelection selection, @Optional IActivationService activationService, @Optional IMModelServices modelServices) {
        ModelElement currentElement = getSelectedElement(selection);
        
        PromptRichNoteDescriptorDlg dialog = new PromptRichNoteDescriptorDlg(parentShell, new RichNoteDescriptor(currentElement),
                modelServices);
        
        // Don't return from open() until window closes
        dialog.setBlockOnOpen(true);
        
        // Open the dialog window
        int ret = dialog.open();
        
        if (ret == IDialogConstants.OK_ID) {
            RichNoteDescriptor richNoteDescriptor = dialog.getRichNoteDescriptor();
        
            ExternDocument richNote = createRichNote(currentElement, richNoteDescriptor, modelServices.getModelFactory(),
                    parentShell);
        
            if (richNote != null) {
                // This does select the note DEFINITIVELY !!?!
                // selectionService.setSelection(new StructuredSelection(richNote));
                activationService.activateMObject(richNote);
            }
        }
    }

    @objid ("6daf8da9-dd1b-42d4-9461-0597f24abc39")
    private ExternDocument createRichNote(final ModelElement modelElement, final RichNoteDescriptor model, final IModelFactory factory, final Shell parentShell) {
        ICoreSession session = CoreSession.getSession(modelElement);
        ITransactionSupport manager = session.getTransactionSupport();
        
        try (ITransaction transaction = manager.createTransaction(EditorsRichNote.I18N.getString("AddDocument"))) {
            ExternDocument doc = factory.createExternDocument();
            doc.setSubject(modelElement);
            doc.setName(model.getName());
            doc.setType(model.getDocumentType());
            doc.setMimeType(model.getChosenMimeType());
            doc.setAbstract(model.getAbstract());
            // doc.setPath(model.getPath());
        
            try {
                RichNoteCreator.createRichNote(doc);
            } catch (IOException e) {
                reportException(e, parentShell);
            }
        
            transaction.commit();
            return doc;
        }
    }

    /**
     * Show a warning dialog box.
     */
    @objid ("12d55105-8a58-4328-83c8-791d69caf19f")
    void reportException(final IOException e, Shell parentShell) {
        //if (!(e instanceof IllegalModelManipulationException)) {
            EditorsRichNote.LOG.error(e);
        //}
            
        String title = EditorsRichNote.I18N.getMessage("CannotInitializeRichNoteContent");
        
        MessageDialog.openWarning(parentShell, title, FileUtils.getLocalizedMessage(e));
    }

}
