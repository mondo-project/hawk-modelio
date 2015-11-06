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
                                    

package org.modelio.diagram.api.dg.scope;

import java.util.Collections;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.diagram.IDiagramNode;
import org.modelio.diagram.api.dg.DGFactory;
import org.modelio.diagram.api.dg.common.PortContainerDG;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.elements.common.freezone.GmFreeZone;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;

/**
 * This class represents the DiagramGraphic of a 'GoalContainer' element.
 */
@objid ("12b4f5e8-63d5-4938-88dc-bedb463e4d0c")
public class GoalContainerDG extends PortContainerDG {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("a983b9d5-9b9a-4a50-a247-3fc5b096f7b0")
    public GoalContainerDG(DiagramHandle diagramHandle, GmNodeModel node) {
        super(diagramHandle, node);
    }

    @objid ("4261e654-0484-4f3c-ac44-8e01d96451da")
    @Override
    public List<IDiagramNode> getPrimaryChildrenNodes() {
        // Inner nodes
        GmFreeZone bodyNode = (GmFreeZone) ((GmCompositeNode) getPrimaryNode()).getFirstChild("body");
        if (bodyNode != null) {
            return DGFactory.getInstance().getDiagramNodes(this.diagramHandle, bodyNode.getVisibleChildren());
        } else {
            return Collections.emptyList();
        }
    }

}