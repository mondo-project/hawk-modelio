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
                                    

package org.modelio.diagram.api.dg.statik;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.api.dg.common.PortContainerDG;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.CollaborationUse;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Operation;

/**
 * This class represents the DiagramGraphic of a 'Module' element.
 */
@objid ("bd45ae07-4f25-47f9-845c-07a49400c338")
public class ModuleDG extends PortContainerDG {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("d5bafce6-7759-4d02-a012-4580b4e60789")
    public ModuleDG(DiagramHandle diagramHandle, GmNodeModel node) {
        super(diagramHandle, node);
    }

    @objid ("c0b77998-3906-4e8d-b3cf-30ce2dd37e0d")
    @Override
    protected List<IDiagramNode> getPrimaryChildrenNodes() {
        List<IDiagramNode> nodes = new ArrayList<>();
        
        // Inner nodes
        GmCompositeNode mainNode = (GmCompositeNode) getPrimaryNode();
        
        // Attributes
        GmCompositeNode attributeZone = mainNode.getCompositeFor(Attribute.class);
        if (attributeZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, attributeZone.getVisibleChildren()));
        }
        
        // CollaborationUse
        GmCompositeNode collabuseZone = mainNode.getCompositeFor(CollaborationUse.class);
        if (collabuseZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, collabuseZone.getVisibleChildren()));
        }
        
        // Instances
        GmCompositeNode instanceZone = mainNode.getCompositeFor(Instance.class);
        if (instanceZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, instanceZone.getVisibleChildren()));
        }
        
        // NameSpace
        GmCompositeNode namespaceZone = mainNode.getCompositeFor(NameSpace.class);
        if (namespaceZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, namespaceZone.getVisibleChildren()));
        }
        
        // Operations
        GmCompositeNode operationZone = mainNode.getCompositeFor(Operation.class);
        if (operationZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, operationZone.getVisibleChildren()));
        }
        return nodes;
    }

}
