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
                                    

package org.modelio.diagram.editor.bpmn.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.bpmn.elements.diagrams.processcollaboration.GmBpmnProcessCollaborationDiagram;
import org.modelio.diagram.editor.bpmn.elements.diagrams.subprocess.GmBpmnSubProcessDiagram;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.IGmLinkFactory;
import org.modelio.diagram.elements.core.model.IGmNodeFactory;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.vcore.smkernel.mapi.MRef;

@objid ("6072aade-55b6-11e2-877f-002564c97630")
public class BpmnDiagramEditorInput extends DiagramEditorInput {
    @objid ("6072aae1-55b6-11e2-877f-002564c97630")
    @Override
    protected GmAbstractDiagram createModel(ModelManager modelManager, AbstractDiagram diagram) {
        if (diagram instanceof BpmnProcessCollaborationDiagram) {
            return new GmBpmnProcessCollaborationDiagram(modelManager,
                                                         (BpmnProcessCollaborationDiagram) diagram,
                                                         new MRef(diagram));
        } else if (diagram instanceof BpmnSubProcessDiagram) {
            return new GmBpmnSubProcessDiagram(modelManager,
                                               (BpmnSubProcessDiagram) diagram,
                                               new MRef(diagram));
        }
        return null;
    }

    @objid ("6074315d-55b6-11e2-877f-002564c97630")
    @Override
    protected IGmLinkFactory createGmLinkFactory(GmAbstractDiagram diagramModel) {
        // currently no specific extension to register, simply return super method
        return super.createGmLinkFactory(diagramModel);
    }

    @objid ("60743167-55b6-11e2-877f-002564c97630")
    @Override
    protected IGmNodeFactory createGmNodeFactory(GmAbstractDiagram diagramModel) {
        // currently no specific extension to register, simply return super method
        return super.createGmNodeFactory(diagramModel);
    }

    @objid ("60743171-55b6-11e2-877f-002564c97630")
    public BpmnDiagramEditorInput(ModelManager modelManager, AbstractDiagram diagram) {
        super(diagram, modelManager);
    }

}
