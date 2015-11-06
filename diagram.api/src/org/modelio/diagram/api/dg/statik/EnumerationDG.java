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
import org.modelio.diagram.editor.statik.elements.enumeration.GmEnumPrimaryNode;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.EnumerationLiteral;

/**
 * This class represents the DiagramGraphic of a 'Enumeration' element.
 */
@objid ("08561978-2cbc-424e-99f6-55a369f0e491")
public class EnumerationDG extends PortContainerDG {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("a0f86604-9017-44aa-b836-c9ad276f55b5")
    public EnumerationDG(DiagramHandle diagramHandle, GmNodeModel node) {
        super(diagramHandle, node);
    }

    @objid ("60279775-b534-4d52-baf3-22e24c0f83fa")
    @Override
    protected List<IDiagramNode> getPrimaryChildrenNodes() {
        List<IDiagramNode> nodes = new ArrayList<>();
        
        // Inner nodes
        GmEnumPrimaryNode mainNode = (GmEnumPrimaryNode) getPrimaryNode();
        
        // Attributes
        GmCompositeNode attributeZone = mainNode.getCompositeFor(Attribute.class);
        if (attributeZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, attributeZone.getVisibleChildren()));
        }
        
        // EnumerationLiteral
        GmCompositeNode enumLiteralZone = mainNode.getCompositeFor(EnumerationLiteral.class);
        if (enumLiteralZone != null) {
            nodes.addAll(DGFactory.getInstance().getDiagramNodes(this.diagramHandle, enumLiteralZone.getVisibleChildren()));
        }
        return nodes;
    }

}
