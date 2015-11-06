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
                                    

package org.modelio.edition.notes.view.data.notes;

import java.util.ArrayList;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesView;
import org.modelio.edition.notes.view.data.INoteContent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

@objid ("ba7fa118-53f5-4d07-bfd4-fae111eea2c2")
public class NoteContentComposite extends Composite implements INoteContent {
    @objid ("dab25eb2-851d-4524-92ec-e8ece1338ee3")
    private Note note = null;

    @objid ("e204bd24-cd6c-4fe3-8f1f-63b832fadb7f")
    private ModelElement annotedElement = null;

    @objid ("9afaf362-3e9a-4b33-9eba-d71ff0cbcc3c")
    private Text noteText = null;

    @objid ("1a26ae5c-bc70-43e7-b601-ad1858f08067")
    private NoteContentModifier noteModifier = null;

    @objid ("00115b46-b29e-42fa-a3b8-72c96f800a73")
    public NoteContentComposite(Composite parentComposite, int style) {
        super(parentComposite, style);
        setLayout(new FillLayout());
        
        this.noteText = new Text(this, SWT.BORDER | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
    }

    @objid ("49207963-91d8-4e02-9365-23e4558bb286")
    @Override
    public void setInput(final ModelElement aNote, final ModelElement annotedElement) {
        this.note = ((Note) aNote);
        this.annotedElement = annotedElement;
        
        if (this.note != null) {
            this.noteText.setText(this.note.getContent());
            this.noteText.setData(this.note);
            
            if (this.note.isModifiable()) {
                this.noteText.setBackground(UIColor.POST_IT_BG);
            } else {
                this.noteText.setBackground(UIColor.TEXT_READONLY_BG);
            }
        } else {
            this.noteText.setText("");
            this.noteText.setData(null);
            this.noteText.setBackground(UIColor.TEXT_READONLY_BG);
        }
    }

    @objid ("a1337c3c-a82d-4ca9-986a-23265934cae7")
    @Override
    public void start(ICoreSession newSession) {
        if (newSession != null) {
            this.noteModifier = new NoteContentModifier(newSession);
        
            this.noteText.addFocusListener(this.noteModifier);
            this.noteText.addKeyListener(this.noteModifier);
        }
    }

    @objid ("9598164f-db33-49f9-a061-464bf18d45fc")
    @Override
    public void stop() {
        if (this.noteModifier != null) {
            this.noteText.removeFocusListener(this.noteModifier);
            this.noteText.removeKeyListener(this.noteModifier);
        }
    }

    @objid ("0e17cec6-beac-4ed5-aa7a-98fa48aa30ef")
    @Override
    public Control getControl() {
        return this;
    }

    @objid ("42b7691a-97be-4f93-b9fc-300074cf5e7e")
    @Override
    public ModelElement getNoteElement() {
        return this.note;
    }

    @objid ("f0b7697e-d163-47ef-9dcf-cdd0abd4017d")
    @Override
    public ModelElement getAnnotedElement() {
        return this.annotedElement;
    }

    @objid ("df776278-c31b-420f-9462-8dd19cdb7825")
    private static class NoteContentModifier implements FocusListener, KeyListener {
        @objid ("4496dcd4-c810-40e5-b34c-30541c38661d")
        private ArrayList<String> activeContexts;

        @objid ("8fe02762-ae81-4936-8ea2-e98fcebfab33")
        private ICoreSession modelingSession = null;

        @objid ("a79898c0-57bc-4eee-b4fd-4ece73cb5fe3")
        @Override
        public void focusGained(final FocusEvent event) {
            // We must deactivate the active contexts during the edition, to avoid the editor's shortcuts to be triggered when entering an element's name... 
            
            // Store those contexts for further reactivation
            this.activeContexts = new ArrayList<>(NotesView.contextService.getActiveContextIds());
            for (String contextId : this.activeContexts) {
                NotesView.contextService.deactivateContext(contextId);
            }
            
            Text text = (Text) event.getSource();
            Note note = (Note) text.getData();
            
            if (note != null) {
                if (note.getStatus().isModifiable()) {
                    text.setBackground(UIColor.TEXT_WRITABLE_BG);
                } else {
                    text.getShell().setFocus();
                }
            }
        }

        @objid ("9494c3a0-c76a-48b7-9068-f4287565761f")
        @Override
        public void focusLost(final FocusEvent event) {
            validate(event);
            
            // Restore previously deactivated contexts
            for (String contextId : this.activeContexts) {
                NotesView.contextService.activateContext(contextId);
            }
            this.activeContexts = null;
        }

        @objid ("681623dd-b3f6-44f3-b003-84aa8e5ee5ca")
        @Override
        public void keyPressed(final KeyEvent event) {
            if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                event.doit = false;
            }
        }

        @objid ("a380cbab-013d-4f62-827f-cdb22d7395e6")
        @Override
        public void keyReleased(final KeyEvent event) {
            Text text = (Text) event.getSource();
            Note note = (Note) text.getData();
            
            if (event.keyCode == SWT.ESC) {
                // restore content from note
                text.setText(note.getContent());
                // this.notesList.getControl().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                validate(event);
                text.getShell().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == 'a') {
                text.selectAll();
            }
        }

        @objid ("8a5c80e7-57c4-4442-82c4-1b792953f2ae")
        private void validate(final TypedEvent event) {
            Text text = (Text) event.getSource();
            Note note = (Note) text.getData();
            
            if (note != null && note.getStatus().isModifiable()) {
                String s = text.getText();
            
                if (!s.equals(note.getContent())) {
                    try (ITransaction transaction = this.modelingSession.getTransactionSupport().createTransaction(
                            EditionNotes.I18N.getString("UpdateDescriptionNote"))) {
                        s = s.replaceAll("\r\n", "\n");
                        note.setContent(s);
                        transaction.commit();
                    }
                }
                
                if (note.isModifiable()) {
                    text.setBackground(UIColor.POST_IT_BG);
                } else {
                    text.setBackground(UIColor.TEXT_READONLY_BG);
                }
            }
        }

        @objid ("e8bbee95-106b-4aed-95a3-fe41c6830f7b")
        public NoteContentModifier(ICoreSession modelingSession) {
            this.modelingSession = modelingSession;
        }

    }

}
