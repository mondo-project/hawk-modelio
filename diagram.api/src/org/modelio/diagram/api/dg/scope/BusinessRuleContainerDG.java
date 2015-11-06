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
 * This class represents the DiagramGraphic of a 'BusinessRuleContainer' element.
 */
@objid ("768f0db8-1de6-457f-92e2-1f4d2cbcc608")
public class BusinessRuleContainerDG extends PortContainerDG {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("79ecd63c-47a2-452c-bcbf-4a00c67a1383")
    public BusinessRuleContainerDG(DiagramHandle diagramHandle, GmNodeModel node) {
        super(diagramHandle, node);
    }

    @objid ("c3b9a5bc-5b17-436c-90fd-9f865238a760")
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
