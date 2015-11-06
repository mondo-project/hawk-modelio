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
                                    

package org.modelio.edition.notes.view.data;

import java.util.regex.Pattern;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.modelio.app.core.activation.IActivationService;
import org.modelio.edition.notes.view.data.constraints.ConstraintContentComposite;
import org.modelio.edition.notes.view.data.externs.ExternNoteContentComposite;
import org.modelio.edition.notes.view.data.notes.NoteContentComposite;
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.NoteType;
import org.modelio.vcore.session.api.ICoreSession;

/**
 * This class provide the panel that display the note content, constraint content and the tagged values.
 */
@objid ("26f1cda5-186f-11e2-bc4e-002564c97630")
public class NoteViewContentPanel extends Composite {
    @objid ("26f1cda7-186f-11e2-bc4e-002564c97630")
    private INoteContent annotationContent = null;

    @objid ("26f1cda8-186f-11e2-bc4e-002564c97630")
    private ModelElement element = null;

    @objid ("26f1cda9-186f-11e2-bc4e-002564c97630")
    private ICoreSession modelingSession = null;

    @objid ("26f1cdaa-186f-11e2-bc4e-002564c97630")
    private StackLayout stackLayout = null;

    @objid ("26f1cdab-186f-11e2-bc4e-002564c97630")
    private NoteContentComposite noteContentComposite = null;

    @objid ("26f1cdac-186f-11e2-bc4e-002564c97630")
    private Composite contentArea = null;

    @objid ("26f1cdad-186f-11e2-bc4e-002564c97630")
    private ConstraintContentComposite constraintContentComposite = null;

    @objid ("26f1cdae-186f-11e2-bc4e-002564c97630")
    private ExternNoteContentComposite externNoteContentComposite;

    @objid ("f2843e78-8524-4a28-8fdd-053cf6c88986")
    private static Pattern REGEX_IS_HTML = Pattern.compile(".*\\<[^>]+>.*", Pattern.DOTALL);

    @objid ("26f1cdaf-186f-11e2-bc4e-002564c97630")
    public NoteViewContentPanel(SashForm sash, int style, IActivationService activationService) {
        super(sash, style);
        createGUI(activationService);
    }

    @objid ("26f1cdb3-186f-11e2-bc4e-002564c97630")
    private void createGUI(IActivationService activationService) {
        this.contentArea = new Composite(this, SWT.NONE);
        this.stackLayout = new StackLayout();
        this.contentArea.setLayout(this.stackLayout);
        
        this.noteContentComposite = new NoteContentComposite(this.contentArea, SWT.NONE);
        
        this.constraintContentComposite = new ConstraintContentComposite(this.contentArea, SWT.NONE);
        
        this.externNoteContentComposite = new ExternNoteContentComposite(this.contentArea, SWT.NONE, activationService);
        
        //--- Disabled HTML notes ---
        //this.htmlNoteContentComposite = new HtmlNoteContentComposite(this.contentArea, SWT.NONE);
    }

    @objid ("26f1cdb5-186f-11e2-bc4e-002564c97630")
    public void setInput(final ModelElement element) {
        cleanContent();
        
        if (element == null || (element.isDeleted())) {        
            this.annotationContent = null;
            this.contentArea.layout();
            return;
        }
        
        if (element instanceof Note) {
            this.element = element;
            this.annotationContent = this.noteContentComposite;
        
            /* --- Disabled HTML notes ---
              
            Note note = (Note) element;
            this.element = element;
            if (isHtmlNote(note))
                this.annotationContent = this.htmlNoteContentComposite;
            else
                this.annotationContent = this.noteContentComposite;*/
        } else if (element instanceof Constraint) {
            this.element = element;
            this.annotationContent = this.constraintContentComposite;
        } else if (element instanceof ExternDocument) {
            this.element = element;
            this.annotationContent = this.externNoteContentComposite;
        }
        
        if (this.annotationContent != null && !this.element.isDeleted()) {
            this.annotationContent.start(this.modelingSession);
            this.annotationContent.setInput(this.element, (ModelElement) this.element.getCompositionOwner());
            this.stackLayout.topControl = this.annotationContent.getControl();
        }
        
        this.contentArea.layout();
    }

    @objid ("26f1cdb9-186f-11e2-bc4e-002564c97630")
    public void start(ICoreSession session) {
        this.modelingSession = session;
    }

    @objid ("26f1cdbc-186f-11e2-bc4e-002564c97630")
    public void stop() {
        this.modelingSession = null;
    }

    @objid ("26f1cdbe-186f-11e2-bc4e-002564c97630")
    public ModelElement getCurrentInput() {
        if (this.annotationContent != null) {
            return this.annotationContent.getNoteElement();
        } else {
            return null;
        }
    }

    /**
     * Clean up existing note content
     */
    @objid ("01749769-7ef6-4c56-906e-dbc4e4d32d4b")
    public void cleanContent() {
        if (this.annotationContent != null) {
            this.annotationContent.setInput(null, null);
            this.annotationContent.stop();
            this.annotationContent = null;
            this.stackLayout.topControl = null;
        }
    }

    @objid ("4e6e8617-612f-4cb0-bc0f-a7dd043dd38a")
    protected boolean isHtmlNote(Note note) {
        final String content = note.getContent();
        
        if (content.trim().startsWith("<html"))
            return true;
        
        final NoteType noteType = note.getModel();
        if (noteType == null)
            return false;
        
        if (noteType.getMimeType().toLowerCase().contains("html"))
            return true;
        
        if (noteType.getName().equals("description")) {
            // return true unless the content is plain text.
            return REGEX_IS_HTML.matcher(content).matches();
        }
        return false;
    }

}
