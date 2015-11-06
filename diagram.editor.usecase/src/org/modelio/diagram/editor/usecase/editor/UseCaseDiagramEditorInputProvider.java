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
                                    

package org.modelio.diagram.editor.usecase.editor;

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.DiagramEditorInputProvider.IDiagramEditorInputProvider;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.UseCaseDiagram;

@objid ("7b9bb527-5eff-11e2-b9cc-001ec947c8cc")
public class UseCaseDiagramEditorInputProvider implements IDiagramEditorInputProvider {
    @objid ("7bea42e8-5eff-11e2-b9cc-001ec947c8cc")
    public UseCaseDiagramEditorInputProvider() {
        super();
    }

    @objid ("7bea42ea-5eff-11e2-b9cc-001ec947c8cc")
    @Override
    public DiagramEditorInput compute(IEclipseContext context) {
        IMModelServices modelServices = context.get(IMModelServices.class);
        
        String diagramUID = context.get(MInputPart.class).getInputURI();
        UseCaseDiagram diagram = (UseCaseDiagram) modelServices.findById(Metamodel.getMClass(UseCaseDiagram.class), UUID.fromString(diagramUID));
        return diagram != null ? new UseCaseDiagramEditorInput(new ModelManager(context), diagram) : null;
    }

}
