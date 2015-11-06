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
                                    

package org.modelio.editors.richnote.gui.ktable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownServiceException;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import de.kupzog.ktable.KTable;
import de.kupzog.ktable.KTableCellEditor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.app.project.core.prefs.ProjectPreferencesKeys;
import org.modelio.app.project.core.services.IProjectService;
import org.modelio.editors.richnote.api.RichNoteCreator;
import org.modelio.editors.richnote.api.RichNoteFormat;
import org.modelio.editors.richnote.api.RichNoteFormatRegistry;
import org.modelio.editors.richnote.api.SupportLevel;
import org.modelio.editors.richnote.plugin.EditorsRichNote;
import org.modelio.gproject.gproject.GProject;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.gproject.model.MModelServices;
import org.modelio.metamodel.analyst.BusinessRule;
import org.modelio.metamodel.analyst.GenericAnalystElement;
import org.modelio.metamodel.analyst.Goal;
import org.modelio.metamodel.analyst.Requirement;
import org.modelio.metamodel.analyst.Term;
import org.modelio.metamodel.factory.ElementNotUniqueException;
import org.modelio.metamodel.factory.IModelFactory;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ExternDocumentType;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.vbasic.files.FileUtils;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * This Editor is used to edit a Requirement or Term,  using an external editor.
 * When opening the editor, an ExternDocument is created on the edited element, if it doesn't exists.
 * Therefore, the ExternDocument is edited  on a separate extern editor, in the same way as using a double click on it.
 */
@objid ("8dc5f866-c068-11e1-8c0a-002564c97630")
public class ScopeRichTextCellEditor extends KTableCellEditor {
    @objid ("8dc5f868-c068-11e1-8c0a-002564c97630")
    private boolean active = false;

    @objid ("8dc5f869-c068-11e1-8c0a-002564c97630")
    private ModelElement editedElement;

    /**
     * Modeler module name.
     */
    @objid ("c618d9d4-5e68-4b74-a0e4-ab251d39a4c6")
    private static final String MODELER_MODULE = "ModelerModule";

    @objid ("8dc5f86c-c068-11e1-8c0a-002564c97630")
    protected KeyAdapter keyListener = new KeyAdapter() {
        @SuppressWarnings("synthetic-access")
        @Override
        public void keyPressed(KeyEvent e) {
            try {
                onKeyPressed(e);
            } catch (final Exception ex) {
                EditorsRichNote.LOG.warning(ex);
                // Do nothing
            }
        }
    };

    /**
     * The Modelio activation service to use to open the rich note.
     */
    @objid ("6aa14d45-f4f5-4aa6-a743-02304b35585f")
    private IActivationService activationService;

    @objid ("852c1a6f-3545-4a22-9111-d4bf5f10058c")
    private IProjectService projectService;

    /**
     * Default constructor.
     * @param editedElement the element to edit. Should be a Term or a Requirement.
     * @param activationService The Modelio activation service to use to open the new rich note.
     */
    @objid ("8dc5f86e-c068-11e1-8c0a-002564c97630")
    public ScopeRichTextCellEditor(final ModelElement editedElement, IActivationService activationService, IProjectService projectService) {
        this.editedElement = editedElement;
        this.activationService = activationService;
        this.projectService = projectService;
    }

    /**
     * Close the editor.
     * @param save true to save the content in the data model.
     */
    @objid ("8dc5f875-c068-11e1-8c0a-002564c97630")
    public void closeEditor(final boolean save) {
        this.active = false;
        super.close(save);
        selectNextField();
    }

    @objid ("8dc5f87a-c068-11e1-8c0a-002564c97630")
    @Override
    public void close(final boolean save) {
        // Nothing to do.
    }

    @objid ("8dc5f87f-c068-11e1-8c0a-002564c97630")
    @Override
    public int getActivationSignals() {
        return SINGLECLICK | KEY_ANY | KEY_RETURN_AND_SPACE;
    }

    @objid ("8dc5f884-c068-11e1-8c0a-002564c97630")
    @Override
    public void open(final KTable table, final int col, final int row, final Rectangle rect) {
        super.open(table, col, row, rect);
        
        if (!this.active) {
            this.active = true;
        
            String docTypeName;
            if (this.editedElement instanceof Goal) {
                docTypeName = "goal";
            } else if (this.editedElement instanceof BusinessRule) {
                docTypeName = "business_rule";
            } else if (this.editedElement instanceof Requirement) {
                docTypeName = "requirement";
            } else if (this.editedElement instanceof Term) {
                docTypeName = "term";
            } else if (this.editedElement instanceof GenericAnalystElement) {
                docTypeName = "generic_analyst";
            } else {
                // Not an analyst element, should never happen
                assert false : this.editedElement ;
                return;
            }
        
            ExternDocument richNote = (ExternDocument) this.m_Model.getContentAt(this.m_Col, this.m_Row);
            if (richNote == null) {
                String mimeType = getSelectedMimeType();
                
                RichNoteFormat format = RichNoteFormatRegistry.getInstance().getDocumentFormatForMime(mimeType);
                if (format == null) {
                    MessageDialog.openError(Display.getDefault().getActiveShell(), 
                                            EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.InvalidMimeType"),
                                            EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.InvalidMimeTypeDetails", mimeType));
                } else if ( !format.isUsable() || format.getSupportLevel()!=SupportLevel.Primary) {
                    MessageDialog.openError(Display.getDefault().getActiveShell(), 
                            EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.InvalidMimeType"),
                            EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.UnsupportedFormatDetails", format.getLabel()));
                } else {
                    richNote = createExternDoc(this.editedElement, docTypeName, mimeType);
                } 
                 
            }
        
            if (richNote != null) {
                // Store the note in the model
                setContent(richNote.toString());
                
                // Must call the fire after the closeEditor for focus issues...
                this.activationService.activateMObject(richNote);
            } else {
                closeEditor(false);
            }
        } else {
            closeEditor(true);
        }
    }

    @objid ("8dc5f88f-c068-11e1-8c0a-002564c97630")
    protected ExternDocument createExternDoc(final ModelElement element, final String docTypeName, final String mimeType) {
        ExternDocument externDoc = null;
        
        GProject gproject = GProject.getProject(element);
        ICoreSession session = gproject.getSession();
        IMModelServices modelSvc = new MModelServices(gproject);
        
        try (ITransaction transaction = session.getTransactionSupport().createTransaction("Create '"+mimeType+"' "+docTypeName+"rich note")) {
            IModelFactory f = modelSvc.getModelFactory();
        
            ExternDocumentType docType = modelSvc.getExternDocumentType(MODELER_MODULE, docTypeName, element.getMClass());
            if (docType == null)
                throw new FileNotFoundException("'"+docTypeName+"' rich note type not found in '"+MODELER_MODULE+"' module.");
            
            externDoc = f.createExternDocument(docType, element, mimeType);
            externDoc.setName(element.getName());
            
            RichNoteCreator.createRichNote(externDoc);
        
            transaction.commit();
        } catch (ElementNotUniqueException e) {
            // many rich note types with same name
            EditorsRichNote.LOG.error(e);
            MessageDialog.openError(Display.getDefault().getActiveShell(), 
                    EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.CannotCreateRichNote"), 
                                    e.getLocalizedMessage());
        } catch (UnknownServiceException e) {
            // no default content could be found.
            EditorsRichNote.LOG.error(e);
            MessageDialog.openError(Display.getDefault().getActiveShell(), 
                    EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.CannotCreateRichNote"), 
                                    e.getLocalizedMessage());
        } catch (IOException e) {
            // error trying to create the file.
            EditorsRichNote.LOG.error(e);
            MessageDialog.openError(Display.getDefault().getActiveShell(), 
                    EditorsRichNote.I18N.getMessage("ScopeRichTextCellEditor.CannotCreateRichNote"), 
                    FileUtils.getLocalizedMessage(e));
        }
        return externDoc;
    }

    @objid ("8dc77f0a-c068-11e1-8c0a-002564c97630")
    @Override
    public void setContent(final Object content) {
        this.m_Model.setContentAt(this.m_Col, this.m_Row, content);
        closeEditor(true);
    }

    @objid ("8dc77f0f-c068-11e1-8c0a-002564c97630")
    @Override
    protected Control createControl() {
        final Text m_Text2 = new Text(this.m_Table, SWT.NONE);
        m_Text2.setData(null);
        m_Text2.setEnabled(false);
        m_Text2.setEditable(false);
        return m_Text2;
    }

    @objid ("8dc77f14-c068-11e1-8c0a-002564c97630")
    private void selectNextField() {
        int nextCol = this.m_Col + 1;
        int nextRow = this.m_Row;
        
        if (nextCol > this.m_Model.getColumnCount() - 1) {
            nextCol = 1;
            nextRow++;
        }
        
        if (nextRow > this.m_Model.getRowCount() - 1) {
            nextRow = 1;
        }
        
        this.m_Table.setSelection(nextCol, nextRow, true);
    }

    @objid ("8dc77f16-c068-11e1-8c0a-002564c97630")
    private String getSelectedMimeType() {
        IPreferenceStore preferenceStore = this.projectService.getProjectPreferences(ProjectPreferencesKeys.NODE_ID);
        final String mimeType = preferenceStore.getString(ProjectPreferencesKeys.RICHNOTE_DEFAULT_TYPE_PREFKEY);
        if (mimeType != null && !mimeType.isEmpty()) {
            return mimeType;
        }
        
        // Use a default value
        if (System.getProperty("os.name").equals("Linux")) {
            return "application/vnd.oasis.opendocument.text";
        } else {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
    }

}
