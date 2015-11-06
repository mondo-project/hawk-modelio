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
                                    

package org.modelio.diagram.editor.object.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.object.elements.objectdiagram.GmObjectDiagram;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Use Case Diagram editor input.
 * 
 * @author chm
 */
@objid ("9d5eaced-55b6-11e2-877f-002564c97630")
public class ObjectDiagramEditorInput extends DiagramEditorInput {
    @objid ("9d5eacf1-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram createModel(ModelManager modelManager, AbstractDiagram diagram) {
        return new GmObjectDiagram(modelManager, (StaticDiagram) diagram, new MRef(diagram));
    }

    /**
     * Initialize the editor input.
     * <p>
     * Creates the diagram graphic model and load it from the diagram model element.
     * @param contextService
     * @param session a modeling session.
     * @param diagram the diagram to edit.
     */
    @objid ("9d5eacfe-55b6-11e2-877f-002564c97630")
    public ObjectDiagramEditorInput(ModelManager modelManager, AbstractDiagram diagram) {
        super(diagram, modelManager);
    }

}
