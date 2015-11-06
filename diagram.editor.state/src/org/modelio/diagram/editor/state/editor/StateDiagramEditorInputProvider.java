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
                                    

package org.modelio.diagram.editor.state.editor;

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.DiagramEditorInputProvider.IDiagramEditorInputProvider;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.StateMachineDiagram;

@objid ("d544ff51-694f-4fd2-9def-4fb0a9654fce")
public class StateDiagramEditorInputProvider implements IDiagramEditorInputProvider {
    @objid ("7dc20ea7-ff78-40c8-af95-ed59840d2a69")
    public StateDiagramEditorInputProvider() {
        super();
    }

    @objid ("57008509-898c-46d1-97ca-8dc21122c85e")
    @Override
    public DiagramEditorInput compute(IEclipseContext context) {
        IMModelServices modelServices = context.get(IMModelServices.class);
        
        String diagramUID = context.get(MInputPart.class).getInputURI();
        StateMachineDiagram diagram = (StateMachineDiagram) modelServices.findById(Metamodel.getMClass(StateMachineDiagram.class), UUID.fromString(diagramUID));
        return diagram != null ? new StateDiagramEditorInput(new ModelManager(context), diagram) : null;
    }

}
