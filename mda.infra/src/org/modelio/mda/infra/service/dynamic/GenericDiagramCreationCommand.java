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
                                    

package org.modelio.mda.infra.service.dynamic;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramHandle;
import org.modelio.api.diagram.IDiagramService;
import org.modelio.api.diagram.style.IStyleHandle;
import org.modelio.api.modelio.Modelio;
import org.modelio.api.module.IModule;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Element;

@objid ("e4f33d6b-b3fb-4e6b-af5b-f177635fef05")
public class GenericDiagramCreationCommand extends GenericElementCreationCommand {
    @objid ("5e9147b9-8cc6-4c14-9871-25dd01784948")
    private String style;

    @objid ("3f0f561f-5570-452c-acd0-7f9545c477a4")
    private boolean open;

    @objid ("2371011f-6d06-43e8-9e9f-bb79693d176f")
    public GenericDiagramCreationCommand(String name, String metaclass, String stereotype, String relation, String style, boolean open) {
        super(name, metaclass, stereotype, relation);
        
        this.style = style;
        this.open = open;
    }

    @objid ("e49b47e4-9019-4b82-859c-bc3e448f7c6b")
    @Override
    protected void postConfigureElement(Element newElement, IModule mdac) {
        super.postConfigureElement(newElement, mdac);
        
        if (newElement instanceof AbstractDiagram) {
            final AbstractDiagram diagram = (AbstractDiagram) newElement;
        
            // Set new diagram style
            final IDiagramService diagramService = Modelio.getInstance().getDiagramService();
            final IStyleHandle styleHandle = diagramService.getStyle(this.style);
            if (styleHandle != null) {
                try (IDiagramHandle handle = diagramService.getDiagramHandle(diagram)) {
                    handle.getDiagramNode().setStyle(styleHandle);
                    handle.save();
                }
            }
        
            // Open diagram
            if (this.open) {
                Modelio.getInstance().getEditionService().openEditor(diagram);
            }
        }
    }

}
