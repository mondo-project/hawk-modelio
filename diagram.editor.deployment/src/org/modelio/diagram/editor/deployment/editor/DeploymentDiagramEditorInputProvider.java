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
                                    

package org.modelio.diagram.editor.deployment.editor;

import java.util.UUID;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MInputPart;
import org.modelio.diagram.editor.DiagramEditorInput;
import org.modelio.diagram.editor.DiagramEditorInputProvider.IDiagramEditorInputProvider;
import org.modelio.diagram.elements.core.model.ModelManager;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.DeploymentDiagram;

@objid ("ea20fbce-5a6e-11e2-9e33-00137282c51b")
public class DeploymentDiagramEditorInputProvider implements IDiagramEditorInputProvider {
    @objid ("436dd5d4-5beb-11e2-9e33-00137282c51b")
    public DeploymentDiagramEditorInputProvider() {
        super();
    }

    @objid ("436dd5d6-5beb-11e2-9e33-00137282c51b")
    @Override
    public DiagramEditorInput compute(IEclipseContext context) {
        IMModelServices modelServices = context.get(IMModelServices.class);
        
        String diagramUID = context.get(MInputPart.class).getInputURI();
        AbstractDiagram diagram = (DeploymentDiagram) modelServices.findById(Metamodel.getMClass(DeploymentDiagram.class), UUID.fromString(diagramUID));
        return diagram != null ? new DeploymentDiagramEditorInput(new ModelManager(context), diagram) : null;
    }

}
