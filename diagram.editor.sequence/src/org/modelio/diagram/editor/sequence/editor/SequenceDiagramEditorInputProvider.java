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

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.DiagramEditorInputProvider.IDiagramEditorInputProvider;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.SequenceDiagram;

@objid ("ca208a23-456f-4f52-9961-77613258a92b")
public class SequenceDiagramEditorInputProvider implements IDiagramEditorInputProvider {
    @objid ("732318c6-0de1-41e8-9e18-d39b2a2b49af")
    public SequenceDiagramEditorInputProvider() {
        super();
    }

    @objid ("cee89d60-79d0-4612-9037-b3c9c7775d08")
    @Override
    public DiagramEditorInput compute(IEclipseContext context) {
        IMModelServices modelServices = context.get(IMModelServices.class);
        
        String diagramUID = context.get(MInputPart.class).getInputURI();
        SequenceDiagram diagram = (SequenceDiagram) modelServices.findById(Metamodel.getMClass(SequenceDiagram.class), UUID.fromString(diagramUID));
        return diagram != null ? new SequenceDiagramEditorInput(new ModelManager(context), diagram) : null;
    }

}
