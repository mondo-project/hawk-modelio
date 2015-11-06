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
                                    

package org.modelio.edition.notes.view.data.constraints;

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
import org.modelio.metamodel.uml.infrastructure.Constraint;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.ui.UIColor;
import org.modelio.vcore.session.api.ICoreSession;
import org.modelio.vcore.session.api.transactions.ITransaction;

@objid ("26eaa97a-186f-11e2-bc4e-002564c97630")
public class ConstraintContentComposite extends Composite implements INoteContent {
    @objid ("26eaa97b-186f-11e2-bc4e-002564c97630")
    private ICoreSession modelingSession = null;

    @objid ("26eaa97c-186f-11e2-bc4e-002564c97630")
    private ModelElement annotedElement = null;

    @objid ("26eaa97d-186f-11e2-bc4e-002564c97630")
    private Constraint constraint = null;

    @objid ("26eaa97e-186f-11e2-bc4e-002564c97630")
    private Text constraintText = null;

    @objid ("26eaa97f-186f-11e2-bc4e-002564c97630")
    private ConstraintContentModifier constraintModifier = null;

    @objid ("26eaa980-186f-11e2-bc4e-002564c97630")
    public ConstraintContentComposite(Composite parentComposite, int style) {
        super(parentComposite, style);
        setLayout(new FillLayout());
        
        this.constraintText = new Text(this, SWT.BORDER | SWT.BORDER | SWT.V_SCROLL | SWT.WRAP);
        
        this.constraintModifier = new ConstraintContentModifier();
    }

    @objid ("26eaa984-186f-11e2-bc4e-002564c97630")
    @Override
    public ModelElement getNoteElement() {
        return this.constraint;
    }

    @objid ("26eaa989-186f-11e2-bc4e-002564c97630")
    @Override
    public Control getControl() {
        return this;
    }

    @objid ("26eaa98e-186f-11e2-bc4e-002564c97630")
    @Override
    public void setInput(final ModelElement note, final ModelElement annotedElement) {
        this.constraint = (Constraint) note;
        
        this.annotedElement = annotedElement;
        
        if (this.constraint != null) {
            this.constraintText.setText(this.constraint.getBody());
            this.constraintText.setData(this.constraint);
            if (this.constraint.isModifiable()) {
                this.constraintText.setBackground(UIColor.POST_IT_BG);
            } else {
                this.constraintText.setBackground(UIColor.TEXT_READONLY_BG);
            }
        } else {
            this.constraintText.setText("");
            this.constraintText.setData(null);
            this.constraintText.setBackground(UIColor.TEXT_READONLY_BG);
        }
    }

    @objid ("26eaa995-186f-11e2-bc4e-002564c97630")
    @Override
    public void start(final ICoreSession newModelingSession) {
        this.modelingSession = newModelingSession;
        this.constraintText.addFocusListener(this.constraintModifier);
        this.constraintText.addKeyListener(this.constraintModifier);
    }

    @objid ("26eaa99a-186f-11e2-bc4e-002564c97630")
    @Override
    public void stop() {
        this.constraintText.removeFocusListener(this.constraintModifier);
        this.constraintText.removeKeyListener(this.constraintModifier);
        this.modelingSession = null;
    }

    @objid ("26eaa99d-186f-11e2-bc4e-002564c97630")
    @Override
    public ModelElement getAnnotedElement() {
        return this.annotedElement;
    }

    @objid ("26eaa9a2-186f-11e2-bc4e-002564c97630")
    private class ConstraintContentModifier implements FocusListener, KeyListener {
        @objid ("db9b3196-c9fb-4a03-a9de-7709d75a8647")
        private ArrayList<String> activeContexts;

        @objid ("26eaa9a3-186f-11e2-bc4e-002564c97630")
        @Override
        public void focusGained(final FocusEvent event) {
            // We must deactivate the active contexts during the edition, to avoid the editor's shortcuts to be triggered when entering an element's name... 
            
            // Store those contexts for further reactivation
            this.activeContexts = new ArrayList<>(NotesView.contextService.getActiveContextIds());
            for (String contextId : this.activeContexts) {
                NotesView.contextService.deactivateContext(contextId);
            }
            
            Text text = (Text) event.getSource();
            
            Constraint editedConstraint = (Constraint) text.getData();
            if (editedConstraint != null) {
                if (editedConstraint.getStatus().isModifiable()) {
                    text.setBackground(UIColor.TEXT_WRITABLE_BG);
                } else {
                    text.getShell().setFocus();
                }
            }
        }

        @objid ("26eaa9a8-186f-11e2-bc4e-002564c97630")
        @Override
        public void focusLost(final FocusEvent event) {
            validate(event);
            
            // Restore previously deactivated contexts
            for (String contextId : this.activeContexts) {
                NotesView.contextService.activateContext(contextId);
            }
            this.activeContexts = null;
        }

        @objid ("26eaa9ad-186f-11e2-bc4e-002564c97630")
        @Override
        public void keyPressed(final KeyEvent event) {
            if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                event.doit = false;
            }
        }

        @objid ("26eaa9b2-186f-11e2-bc4e-002564c97630")
        @Override
        public void keyReleased(final KeyEvent event) {
            Text text = (Text) event.getSource();
            
            if (event.keyCode == SWT.ESC) {
                // restore content from note
                Constraint editedConstraint = (Constraint) text.getData();
                text.setText(editedConstraint.getBody());
                text.getShell().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == SWT.CR) {
                validate(event);
                text.getShell().setFocus();
            } else if ((event.stateMask &= SWT.MOD1) != 0 && event.keyCode == 'a') {
                text.selectAll();
            }
        }

        @objid ("26eaa9b7-186f-11e2-bc4e-002564c97630")
        private void validate(final TypedEvent event) {
            Text text = (Text) event.getSource();
            Constraint constraintToEdit = (Constraint) text.getData();
            
            if (constraintToEdit != null && constraintToEdit.getStatus().isModifiable()) {
                String s = text.getText();
            
                if (!s.equals(constraintToEdit.getBody())) {
                    try (ITransaction transaction = ConstraintContentComposite.this.modelingSession.getTransactionSupport()
                            .createTransaction(EditionNotes.I18N.getString("UpdateDescriptionNote"))) {
                        s = s.replaceAll("\r\n", "\n");
                        constraintToEdit.setBody(s);
                        transaction.commit();
                    }
                }
                
                if (constraintToEdit.isModifiable()) {
                    text.setBackground(UIColor.POST_IT_BG);
                } else {
                    text.setBackground(UIColor.TEXT_READONLY_BG);
                }
            }
        }

        @objid ("26ed0ad0-186f-11e2-bc4e-002564c97630")
        public ConstraintContentModifier() {
            // TODO Auto-generated constructor stub
        }

    }

}
