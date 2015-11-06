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
                                    

package org.modelio.editors.service;

import java.io.File;
import javax.inject.Inject;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.modelio.api.editor.EditorType;
import org.modelio.api.editor.IMDATextEditor;
import org.modelio.app.core.inputpart.IInputPartService;
import org.modelio.editors.texteditors.IDocumentEditor;
import org.modelio.editors.texteditors.input.IDocumentInput;
import org.modelio.editors.texteditors.mdd.MDDEditor;
import org.modelio.editors.texteditors.rt.RTEditor;
import org.modelio.editors.texteditors.txt.TXTEditor;
import org.modelio.metamodel.uml.infrastructure.ModelElement;

@objid ("7b46fecb-2a77-11e2-9fb9-bc305ba4815c")
public class EditionManager {
    @objid ("ab475603-2a77-11e2-9fb9-bc305ba4815c")
    private static final String PARENT_PART_ID = "org.modelio.app.center.parts";

    @objid ("ab475608-2a77-11e2-9fb9-bc305ba4815c")
    private static final String BUNDLE_PREFIX = "bundleclass://";

    @objid ("ab47560d-2a77-11e2-9fb9-bc305ba4815c")
    private static final String URI_SEPARATOR = "/";

    @objid ("7b46fed5-2a77-11e2-9fb9-bc305ba4815c")
    private static EditionManager instance;

// Injected Attributes
    @objid ("7b46fedb-2a77-11e2-9fb9-bc305ba4815c")
    @Inject
    @Optional
    private MApplication application;

    @objid ("7b46fede-2a77-11e2-9fb9-bc305ba4815c")
    @Inject
    @Optional
    private EModelService modelService;

    @objid ("7b46fee0-2a77-11e2-9fb9-bc305ba4815c")
    @Inject
    @Optional
    private EPartService partService;

    @objid ("e2d85694-2fd8-11e2-a79f-bc305ba4815c")
    @Inject
    @Optional
    private IInputPartService inputservice;

// Singleton
    @objid ("7b46fed2-2a77-11e2-9fb9-bc305ba4815c")
    private EditionManager() {
    }

    @objid ("7b46fed6-2a77-11e2-9fb9-bc305ba4815c")
    public static synchronized EditionManager services() {
        if (instance == null) {
            instance = new EditionManager();
        }
        return instance;
    }

    @objid ("7b48856b-2a77-11e2-9fb9-bc305ba4815c")
    public void closeEditor(final IMDATextEditor editor) {
        if (editor instanceof MDATextEditor) {
            MDATextEditor mdaEditor = (MDATextEditor) editor;
            mdaEditor.getEditor().setParent(null);
        }
    }

    @objid ("7b48856f-2a77-11e2-9fb9-bc305ba4815c")
    public IMDATextEditor openEditor(final ModelElement modelElement, final File file, final EditorType editorTypeID, final boolean readonly) {
        MInputPart inputPart = null;
        
        String partid = null;
        
        switch (editorTypeID) {
        case MDDEditor:
            partid = MDDEditor.EDITOR_ID;
            break;
        case RTEditor:
            partid = RTEditor.EDITOR_ID;
            break;
        case TXTEditor:
            partid = TXTEditor.EDITOR_ID;
            break;
        default:
            break;
        }
        
        MPart shownPart = this.inputservice.showInputPart(partid, file.getAbsolutePath(), PartState.ACTIVATE);
        
        if (modelElement != null) {            
            shownPart.setLabel(modelElement.getName());
        }
        shownPart.setTooltip(file.getAbsolutePath());
        
        if (shownPart instanceof MInputPart) {
            inputPart = (MInputPart)shownPart;
        } else if (shownPart instanceof MPlaceholder) {
            inputPart = (MInputPart) ((MPlaceholder)shownPart).getRef();
        } 
        
        if (inputPart != null && inputPart.getObject() != null) {
            IDocumentEditor editor = (IDocumentEditor) inputPart.getObject();
            IDocumentInput input = editor.getDocumentInput();
            editor.setReadonlyMode(readonly);
            
            MDATextEditor proxy = new MDATextEditor(input, inputPart);
            proxy.setElement(modelElement);
            return proxy;
        }
        return null;
    }

    @objid ("7b48857b-2a77-11e2-9fb9-bc305ba4815c")
    public void activateEditor(final IMDATextEditor editor) {
        if (editor instanceof MDATextEditor) {
            MDATextEditor mdaEditor = (MDATextEditor) editor;
            mdaEditor.getEditor().setOnTop(true);
        }
    }

}
