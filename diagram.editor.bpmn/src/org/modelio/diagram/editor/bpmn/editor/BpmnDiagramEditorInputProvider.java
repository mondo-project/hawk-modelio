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

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.DiagramEditorInputProvider.IDiagramEditorInputProvider;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnProcessCollaborationDiagram;
import org.modelio.metamodel.bpmn.bpmnDiagrams.BpmnSubProcessDiagram;
import org.modelio.metamodel.diagrams.AbstractDiagram;

@objid ("3209cddb-5973-11e2-ae45-002564c97630")
public class BpmnDiagramEditorInputProvider implements IDiagramEditorInputProvider {
    @objid ("c4a3440f-59a6-11e2-ae45-002564c97630")
    public BpmnDiagramEditorInputProvider() {
        super();
    }

    @objid ("c4a34411-59a6-11e2-ae45-002564c97630")
    @Override
    public DiagramEditorInput compute(IEclipseContext context) {
        IMModelServices modelServices = context.get(IMModelServices.class);
        
        String diagramUID = context.get(MInputPart.class).getInputURI();
        AbstractDiagram diagram = (BpmnProcessCollaborationDiagram) modelServices.findById(Metamodel.getMClass(BpmnProcessCollaborationDiagram.class), UUID.fromString(diagramUID));
        if (diagram == null) {
            diagram = (BpmnSubProcessDiagram) modelServices.findById(Metamodel.getMClass(BpmnSubProcessDiagram.class), UUID.fromString(diagramUID));
        }
        return diagram != null ? new BpmnDiagramEditorInput(new ModelManager(context), diagram) : null;
    }

}
