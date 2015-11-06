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
                                    

package org.modelio.diagram.editor.sequence.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.sequence.elements.sequencediagram.GmSequenceDiagram;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.SequenceDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("d8bedef0-55b6-11e2-877f-002564c97630")
public class SequenceDiagramEditorInput extends DiagramEditorInput {
    @objid ("d8bedef3-55b6-11e2-877f-002564c97630")
    public SequenceDiagramEditorInput(ModelManager modelManager, AbstractDiagram diagram) {
        super(diagram, modelManager);
    }

    @objid ("d8bedefb-55b6-11e2-877f-002564c97630")
    @Override
    protected IGmLinkFactory createGmLinkFactory(GmAbstractDiagram diagramModel) {
        // currently no specific extension to register, simply return super method
        return super.createGmLinkFactory(diagramModel);
    }

    @objid ("d8bedf05-55b6-11e2-877f-002564c97630")
    @Override
    protected IGmNodeFactory createGmNodeFactory(GmAbstractDiagram diagramModel) {
        // currently no specific extension to register, simply return super method
        return super.createGmNodeFactory(diagramModel);
    }

    @objid ("d8bedf0f-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram createModel(ModelManager modelManager, AbstractDiagram diagram) {
        return new GmSequenceDiagram(modelManager, (SequenceDiagram) diagram, new MRef(diagram));
    }

}
