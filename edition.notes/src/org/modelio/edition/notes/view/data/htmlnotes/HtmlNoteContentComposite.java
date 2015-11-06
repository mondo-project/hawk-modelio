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
                                    

package org.modelio.edition.notes.view.data.htmlnotes;

import java.util.ArrayList;
import java.util.Collection;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.modelio.edition.html.view.RichTextEditor;
import org.modelio.edition.notes.plugin.EditionNotes;
import org.modelio.edition.notes.view.NotesView;
import org.modelio.edition.notes.view.data.INoteContent;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

/**
 * HTML Note content editor.
 */
@objid ("26ef6c47-186f-11e2-bc4e-002564c97630")
public class HtmlNoteContentComposite extends Composite implements INoteContent {
    @objid ("73d45c60-e52f-4d13-90b2-ff075486a681")
    private static final String DATA_KEY = "MObject_key";

    @objid ("26ef6c48-186f-11e2-bc4e-002564c97630")
    private Note note = null;

    @objid ("26ef6c49-186f-11e2-bc4e-002564c97630")
    private ModelElement annotedElement = null;

    @objid ("26ef6c4a-186f-11e2-bc4e-002564c97630")
    private RichTextEditor noteText = null;

    @objid ("26ef6c4c-186f-11e2-bc4e-002564c97630")
    private NoteContentModifier noteModifier = null;

    /**
     * @param parentComposite the parent SWT composite
     * @param style style bits, see {@link Composite} flags.
     */
    @objid ("26ef6c4d-186f-11e2-bc4e-002564c97630")
    public HtmlNoteContentComposite(Composite parentComposite, int style) {
        super(parentComposite, style);
        setLayout(new FillLayout());
        
        this.noteText = new RichTextEditor(this, SWT.BORDER);
        addDisposeListener(new DisposeListener() {
            
            @SuppressWarnings("synthetic-access")
            @Override
            public void widgetDisposed(DisposeEvent e) {
                HtmlNoteContentComposite.this.noteText.dispose();
            }
        });
    }

    @objid ("26ef6c51-186f-11e2-bc4e-002564c97630")
    @Override
    public void setInput(final ModelElement aNote, final ModelElement annotedElement) {
        this.note = ((Note) aNote);
        this.annotedElement = annotedElement;
        
        if (this.note != null) {
            this.noteText.setText(this.note.getContent());
            this.noteText.setData(DATA_KEY, this.note);
            
            if (this.note.isModifiable()) {
                this.noteText.getControl().setBackground(getBackground());
                this.noteText.setEditable(true);
            } else {
                this.noteText.getControl().setBackground(UIColor.TEXT_READONLY_BG);
                this.noteText.setEditable(false);
            }
        } else {
            this.noteText.setText("");
            this.noteText.setData(DATA_KEY, null);
            this.noteText.getControl().setBackground(UIColor.TEXT_READONLY_BG);
        }
    }

    @objid ("26ef6c58-186f-11e2-bc4e-002564c97630")
    @Override
    public void start(ICoreSession newSession) {
        if (newSession != null) {
            this.noteModifier = new NoteContentModifier(newSession, this.noteText);
        
            this.noteText.addFocusListener(this.noteModifier);
            this.noteText.addKeyListener(this.noteModifier);
        }
    }

    @objid ("26ef6c5c-186f-11e2-bc4e-002564c97630")
    @Override
    public void stop() {
        if (this.noteModifier != null) {
            this.noteText.removeFocusListener(this.noteModifier);
            this.noteText.removeKeyListener(this.noteModifier);
            this.noteModifier = null;
        }
    }

    @objid ("26ef6c5f-186f-11e2-bc4e-002564c97630")
    @Override
    public Control getControl() {
        return this;
    }

    @objid ("26f1cd82-186f-11e2-bc4e-002564c97630")
    @Override
    public ModelElement getNoteElement() {
        return this.note;
    }

    @objid ("26f1cd87-186f-11e2-bc4e-002564c97630")
    @Override
    public ModelElement getAnnotedElement() {
        return this.annotedElement;
    }

    @objid ("1d60147b-3c53-431e-8003-719ab7dccd49")
    static void __setToolbarVisible(RichTextEditor htmlEditor, boolean visible) {
        final Composite toolbar = htmlEditor.getToolBar().getToolbarMgr().getControl().getParent();
        toolbar.setVisible(visible);
        toolbar.getParent().layout(new Control[]{toolbar, toolbar.getParent()});
    }

    @objid ("26f1cd8c-186f-11e2-bc4e-002564c97630")
    private static class NoteContentModifier implements FocusListener, KeyListener {
        @objid ("0158f9f6-0bfa-43fd-b299-c28c4deb4bc0")
        private ArrayList<String> activeContexts;

        @objid ("26ef6c4b-186f-11e2-bc4e-002564c97630")
        private ICoreSession modelingSession = null;

        @objid ("f31ecfe7-c724-456f-a67f-26bc7dee67d5")
        private RichTextEditor htmlEditor;

        @objid ("26f1cd8d-186f-11e2-bc4e-002564c97630")
        @Override
        public void focusGained(final FocusEvent event) {
            // We must deactivate the active contexts during the edition, to avoid the editor's shortcuts to be triggered when entering an element's name... 
            
            // Store those contexts for further reactivation
            final Collection<String> activeContextIds = NotesView.contextService.getActiveContextIds();
            if (activeContextIds != null) {
                this.activeContexts = new ArrayList<>(activeContextIds);
                for (String contextId : this.activeContexts) {
                    NotesView.contextService.deactivateContext(contextId);
                }
            }
            
            RichTextEditor text = this.htmlEditor; //(RichTextEditor) event.getSource();
            Note note = (Note) text.getData(DATA_KEY);
            
            if (note != null) {
                if (note.getStatus().isModifiable()) {
                    //text.getControl().setBackground(UIColor.TEXT_WRITABLE_BG);
                } else {
                    text.setFocus();
                    //text.getControl().getShell().setFocus();
                }
            }
        }

        @objid ("26f1cd92-186f-11e2-bc4e-002564c97630")
        @Override
        public void focusLost(final FocusEvent event) {
            validate();
            
            // Restore previously deactivated contexts
            if (this.activeContexts != null) {
                for (String contextId : this.activeContexts) {
                    NotesView.contextService.activateContext(contextId);
                }
                this.activeContexts = null;
            }
        }

        @objid ("26f1cd97-186f-11e2-bc4e-002564c97630")
        @Override
        public void keyPressed(final KeyEvent event) {
            if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                event.doit = false;
            }
        }

        @objid ("26f1cd9c-186f-11e2-bc4e-002564c97630")
        @Override
        public void keyReleased(final KeyEvent event) {
            RichTextEditor text = this.htmlEditor; //(RichTextEditor) event.getSource();
            Note note = (Note) text.getData(DATA_KEY);
            
            if (event.keyCode == SWT.ESC) {
                // restore content from note
                text.setText(note.getContent());
                // this.notesList.getControl().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                validate();
                text.setFocus();
                //text.getControl().getShell().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == 'a') {
                text.selectAll();
                
            }
        }

        @objid ("26f1cda1-186f-11e2-bc4e-002564c97630")
        private void validate() {
            RichTextEditor text = this.htmlEditor; //(RichTextEditor) event.getSource();
            Note note = (Note) text.getData(DATA_KEY);
            
            if (note != null && note.getStatus().isModifiable()) {
                String editorContent = text.getTidyText();
            
                if (!editorContent.equals(note.getContent())) {
                    try (ITransaction transaction = this.modelingSession.getTransactionSupport().createTransaction(
                            EditionNotes.I18N.getString("UpdateDescriptionNote"))) {
                        editorContent = editorContent.replaceAll("\r\n", "\n");
                        note.setContent(editorContent);
                        transaction.commit();
                    }
                }
                
                if (note.isModifiable()) {
                    //text.getControl().setBackground(UIColor.POST_IT_BG);
                } else {
                    text.getControl().setBackground(UIColor.TEXT_READONLY_BG);
                }
            }
        }

        @objid ("fb9d07dc-19e5-11e2-ad19-002564c97630")
        public NoteContentModifier(ICoreSession modelingSession, RichTextEditor noteText) {
            this.modelingSession = modelingSession;
            this.htmlEditor = noteText;
        }

    }

}
