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
                                    

package org.modelio.diagram.api.dg.bpmn;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.api.services.DiagramLink;
import org.modelio.diagram.elements.core.model.IGmLink;

/**
 * This class represents the DiagramGraphic of a 'BpmnDataAssociation' element.
 */
@objid ("8193fbf2-f434-4056-8011-fb61c5afeb7a")
public class BpmnDataAssociationDG extends DiagramLink {
    /**
     * @param node The gm node represented by this class.
     * @param diagramHandle The diagram manipulation class.
     */
    @objid ("5d7d4aae-ecef-40db-ad40-4b7d2f3643eb")
    public BpmnDataAssociationDG(DiagramHandle diagramHandle, IGmLink link) {
        super(diagramHandle, link);
    }

}
