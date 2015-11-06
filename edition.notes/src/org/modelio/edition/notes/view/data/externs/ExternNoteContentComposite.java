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
                                    

package org.modelio.edition.notes.view.data.externs;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.data.INoteContent;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * Right panel for rich notes.
 */
@objid ("26ed0ad3-186f-11e2-bc4e-002564c97630")
public class ExternNoteContentComposite extends Composite implements INoteContent {
    @objid ("26ed0ad4-186f-11e2-bc4e-002564c97630")
    protected ExternDocument xdoc = null;

    @objid ("26ed0ad5-186f-11e2-bc4e-002564c97630")
    private ModelElement annotedElement = null;

    @objid ("26ed0ad6-186f-11e2-bc4e-002564c97630")
    protected ICoreSession modelingSession = null;

    @objid ("26ed0ad7-186f-11e2-bc4e-002564c97630")
    private ExternNoteContentModifier noteModifier = null;

    @objid ("26ed0ad8-186f-11e2-bc4e-002564c97630")
    private Text text = null;

    @objid ("26ed0ad9-186f-11e2-bc4e-002564c97630")
    private final Button editButton;

    @objid ("72577cde-8d85-44b4-a949-d5f7ebe365de")
    private IActivationService activationService;

    /**
     * Constructor
     * @param parentComposite the parent composite
     * @param style style bits
     * @param activationService Modelio activation service
     */
    @objid ("26ed0ada-186f-11e2-bc4e-002564c97630")
    public ExternNoteContentComposite(Composite parentComposite, int style, IActivationService activationService) {
        super(parentComposite, style);
        
        this.activationService  =activationService;
        
        GridLayout layout = new GridLayout(1, true);
        setLayout(layout);
        
        this.text = new Text(this, SWT.BORDER | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        this.noteModifier = new ExternNoteContentModifier();
        GridData gdText = new GridData();
        gdText.grabExcessHorizontalSpace = true;
        gdText.horizontalAlignment = SWT.FILL;
        gdText.grabExcessVerticalSpace = true;
        gdText.verticalAlignment = SWT.FILL;
        this.text.setLayoutData(gdText);
        // this.text.setToolTipText(EditionNotes.I18N.getString("DocumentAbstract.tooltip"));
        
        this.editButton = new Button(this, SWT.PUSH);
        
        GridData gdButton = new GridData();
        gdButton.grabExcessHorizontalSpace = true;
        gdButton.horizontalAlignment = SWT.FILL;
        this.editButton.setLayoutData(gdButton);
        this.editButton.setText(EditionNotes.I18N.getString("EditDocumentButton.label"));
        this.editButton.setToolTipText(EditionNotes.I18N.getString("EditDocumentButton.tooltip"));
        
        this.editButton.addSelectionListener(new SelectionListener() {
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (getActivationService() != null)
                    getActivationService().activateMObject(ExternNoteContentComposite.this.xdoc);
            }
        
            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                // nothing to do
            }
        });
    }

    @objid ("26ed0ade-186f-11e2-bc4e-002564c97630")
    @Override
    public void setInput(final ModelElement aNote, final ModelElement annotedElement) {
        if (aNote != null) {
            this.xdoc = ((ExternDocument) aNote);
            this.text.setText(this.xdoc.getAbstract());
            this.text.setData(this.xdoc);
            if (aNote.isModifiable()) {
                this.text.setBackground(UIColor.POST_IT_BG);
            } else {
                this.text.setBackground(UIColor.TEXT_READONLY_BG);
            }
        } else {
            this.xdoc = null;
            this.text.setText("");
            this.text.setData(null);
            this.text.setBackground(UIColor.TEXT_READONLY_BG);
        }
        
        this.annotedElement = annotedElement;
    }

    @objid ("26ed0ae5-186f-11e2-bc4e-002564c97630")
    @Override
    public void start(ICoreSession session) {
        this.modelingSession = session;
        this.text.addFocusListener(this.noteModifier);
        this.text.addKeyListener(this.noteModifier);
    }

    @objid ("26ed0ae9-186f-11e2-bc4e-002564c97630")
    @Override
    public void stop() {
        this.text.removeFocusListener(this.noteModifier);
        this.text.removeKeyListener(this.noteModifier);
        this.modelingSession = null;
    }

    @objid ("26ed0aec-186f-11e2-bc4e-002564c97630")
    @Override
    public Control getControl() {
        return this;
    }

    @objid ("26ed0af1-186f-11e2-bc4e-002564c97630")
    @Override
    public ModelElement getNoteElement() {
        return this.xdoc;
    }

    @objid ("26ed0af6-186f-11e2-bc4e-002564c97630")
    @Override
    public ModelElement getAnnotedElement() {
        return this.annotedElement;
    }

    @objid ("f5104b83-468e-4b3f-b27f-fad5b43181bf")
    IActivationService getActivationService() {
        return this.activationService;
    }

    @objid ("26ed0afb-186f-11e2-bc4e-002564c97630")
    private class ExternNoteContentModifier implements FocusListener, KeyListener {
        @objid ("26ed0afc-186f-11e2-bc4e-002564c97630")
        public ExternNoteContentModifier() {
        }

        @objid ("26ed0afe-186f-11e2-bc4e-002564c97630")
        @Override
        public void focusGained(final FocusEvent event) {
            Text externDocText = (Text) event.getSource();
            ExternDocument externNote = (ExternDocument) externDocText.getData();
            
            if (externNote != null) {
                if (externNote.getStatus().isModifiable()) {
                    externDocText.setBackground(UIColor.TEXT_WRITABLE_BG);
                } else {
                    externDocText.getShell().setFocus();
                }
            }
        }

        @objid ("26ed0b03-186f-11e2-bc4e-002564c97630")
        @Override
        public void focusLost(final FocusEvent event) {
            validate(event);
        }

        @objid ("26ef6c29-186f-11e2-bc4e-002564c97630")
        @Override
        public void keyPressed(final KeyEvent event) {
            if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                event.doit = false;
            }
        }

        @objid ("26ef6c2e-186f-11e2-bc4e-002564c97630")
        @Override
        public void keyReleased(final KeyEvent event) {
            Text externDocText = (Text) event.getSource();
            ExternDocument note = (ExternDocument) externDocText.getData();
            
            if (event.keyCode == SWT.ESC) {
                // restore content from note
                externDocText.setText(note.getAbstract());
                // this.notesList.getControl().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                validate(event);
                externDocText.getShell().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == 'a') {
                externDocText.selectAll();
            }
        }

        @objid ("26ef6c33-186f-11e2-bc4e-002564c97630")
        private void validate(final TypedEvent event) {
            Text externDocText = (Text) event.getSource();
            ExternDocument note = (ExternDocument) externDocText.getData();
            
            if (note != null && note.getStatus().isModifiable()) {
                String s = externDocText.getText();
            
                if (!s.equals(note.getAbstract())) {
                    try (ITransaction transaction = ExternNoteContentComposite.this.modelingSession.getTransactionSupport()
                            .createTransaction(EditionNotes.I18N.getString("UpdateDescriptionNote"))) {
                        s = s.replaceAll("\r\n", "\n");
                        note.setAbstract(s);
                        transaction.commit();
                    }
                }
                
                if (note.isModifiable()) {
                    externDocText.setBackground(UIColor.POST_IT_BG);
                } else {
                    externDocText.setBackground(UIColor.TEXT_READONLY_BG);
                }
            }
        }

    }

}
