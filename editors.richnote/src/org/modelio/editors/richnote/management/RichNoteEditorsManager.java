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
                                    

package org.modelio.editors.richnote.management;

import java.util.ArrayList;
import java.util.Collection;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.statushandlers.StatusManager;
import org.modelio.app.core.events.ModelioEventTopics;
import org.modelio.app.core.inputpart.IInputPartService;
import org.modelio.editors.richnote.api.IRichNoteEditorProvider;
import org.modelio.editors.richnote.api.RichNoteFormat;
import org.modelio.editors.richnote.api.RichNoteFormatRegistry;
import org.modelio.editors.richnote.editor.IRichNoteEditor;
import org.modelio.editors.richnote.management.EditorsRegistry.RichNoteToken;
import org.modelio.editors.richnote.plugin.EditorsRichNote;
import org.modelio.gproject.gproject.GProject;
import org.modelio.metamodel.uml.infrastructure.ExternDocument;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * A manager for the diagram editors.
 * <p>
 * This class is responsible for handling the activation request on a rich note, by either opening a
 * new rich note editor for said rich note if none already exists,
 * or if on is found by bringing it to top so that it is visible.<br>
 */
@objid ("a229c715-ca90-4891-9cb8-310160df2f2d")
public class RichNoteEditorsManager {
    /**
     * Used to keep the singleton in memory.
     */
    @objid ("8e6cc9ec-d0d4-4fcd-980a-424a3b802f6c")
    private static RichNoteEditorsManager instance;

    @objid ("6ea7da69-37d3-437c-add9-c79ee434bf2f")
    @Inject
    @Optional
    boolean onEditElement(@EventTopic(ModelioEventTopics.EDIT_ELEMENT) final MObject target, final IInputPartService inputPartService, final EPartService partService) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        RichNoteFormat format = null;
        
        if (target == null) {
            return false;
        }
        
        // If there is already an opened editor for the container, bring it to top
        final GProject project = GProject.getProject(target);
        final RichNotesSession session = RichNotesSession.get(project);
        final IRichNoteEditor editor = session.getEditorRegistry().getEditor(target);
        if (editor != null) {
            // bring the editor to top
            Display.getDefault().asyncExec(new Runnable() {
                @Override
                public void run() {
                    partService.activate(editor.getMPart());
                }});
            return true;
        }
        
        if ( target instanceof ExternDocument) {
            format = RichNoteFormatRegistry.getInstance().getFormat((ExternDocument) target);
        }
        
        if (format != null) {
            if (format.isUsable()) {
                final IRichNoteEditorProvider editorProvider = format.getEditorProvider();
        
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        final String inputUri = new MRef(target).toString();
                        
                        inputPartService.showInputPart(editorProvider.getEditorId(target), inputUri, PartState.ACTIVATE);
        
                        //RichNoteEditorsManager.this.put(target, openedPart);
                    }
                });        
            } else { 
                reportFormatNotUsable(format);
            }
        }
        return false;
    }

    @objid ("9f0f33d4-5603-41b5-b4a4-3047b673823a")
    private void reportFormatNotUsable(final RichNoteFormat format) {
        String message = EditorsRichNote.I18N.getMessage("DocumentFormatNotEditable", 
                format.getLabel(), 
                format.getMimeType());
        Status status = new Status(IStatus.INFO, EditorsRichNote.PLUGIN_ID, message);
        
        StatusManager.getManager().handle(status,StatusManager.SHOW );
    }

    @objid ("4eb06743-fa89-49f9-bc68-d645e3ba8dd9")
    @Execute
    void execute(IEclipseContext context) {
        ContextInjectionFactory.inject(this, context);
    }

    /**
     * Close all editors
     * @param project the closed project
     * @param partService Eclipse parts service
     */
    @objid ("9f6cbf3f-1a38-4ddd-addd-6866888f2b3f")
    void closeAll(GProject project, EPartService partService) {
        RichNotesSession richNotesSession = RichNotesSession.get(project);
        
        if (richNotesSession == null)
            return;
        
        // Close all opened diagram editors.
        Collection<RichNoteToken> allEditors = new ArrayList<>(richNotesSession.getEditorRegistry().getAllEditors());
        for (RichNoteToken token : allEditors) {            
            // close the editor.
            MPart mpart = token.editor.getMPart();
            if (mpart != null)
                partService.hidePart(mpart, true);
            
            token.editor.disposeResources();
        }
        
        richNotesSession.closeSession();
    }

    @objid ("b00da0fc-bce8-4481-abaf-822606c57aef")
    @Inject
    @Optional
    void onProjectClosed(@EventTopic(ModelioEventTopics.PROJECT_CLOSING) final GProject project, final EPartService partService) {
        // FIXME this should be an @UIEventTopic, but they are not triggered with eclipse 4.3 M5...
        Display.getDefault().syncExec(new Runnable() {
        
            @Override
            public void run() {
                // close all diagram editors when closing the project
                closeAll(project, partService);
                
            }
        });
    }

    @objid ("4408ac9a-bd17-4985-b950-42c08044997b")
    @Inject
    @Optional
    void onProjectOpen(@EventTopic(ModelioEventTopics.PROJECT_OPENING) final GProject project) {
        // Instantiate a new rich note modeling session
        @SuppressWarnings("unused")
        RichNotesSession richNotesSession = new RichNotesSession(project);
    }

    @objid ("67cff99c-e23b-439a-a5a6-fd5ceab5ca66")
    public RichNoteEditorsManager() {
        instance = this;
    }

}
