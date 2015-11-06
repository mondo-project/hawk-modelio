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
                                    

package org.modelio.diagram.api.dg.usecase;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.api.dg.common.PortContainerDG;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.editor.usecase.elements.usecase.GmUseCase;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.uml.behavior.usecaseModel.ExtensionPoint;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Operation;

/**
 * This class represents the DiagramGraphic of a 'UseCase' element.
 */
@objid ("93bedfd9-6a0a-4042-ad62-17745cd2f22c")
public class UseCaseDG extends PortContainerDG {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("b01acb46-ecaf-4b1e-94ed-beacecf0a3b9")
    public UseCaseDG(DiagramHandle diagramHandle, GmNodeModel node) {
        super(diagramHandle, node);
    }

    @objid ("8bd3a03a-220d-4c8e-86fd-8cbc4ff306a6")
    @Override
    public List<IDiagramNode> getPrimaryChildrenNodes() {
        List<IDiagramNode> nodes = new ArrayList<>();
        
        // Inner nodes
        GmUseCase mainNode = (GmUseCase) this.gmNode;
        
        // Attributes
        GmCompositeNode attributeZone = mainNode.getCompositeFor(Attribute.class);
        if (attributeZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle,
                                                                 attributeZone.getVisibleChildren()));
        }
        
        // CollaborationUse
        GmCompositeNode collabuseZone = mainNode.getCompositeFor(CollaborationUse.class);
        if (collabuseZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle,
                                                                 collabuseZone.getVisibleChildren()));
        }
        
        // ExtensionPoint
        GmCompositeNode extensionPointZone = mainNode.getCompositeFor(ExtensionPoint.class);
        if (extensionPointZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle,
                                                                 extensionPointZone.getVisibleChildren()));
        }
        
        // Instances
        GmCompositeNode instanceZone = mainNode.getCompositeFor(Instance.class);
        if (instanceZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle,
                                                                 instanceZone.getVisibleChildren()));
        }
        
        // Operations
        GmCompositeNode operationZone = mainNode.getCompositeFor(Operation.class);
        if (operationZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle,
                                                                 operationZone.getVisibleChildren()));
        }
        return nodes;
    }

}
