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

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.api.services.DiagramHandle;
import org.modelio.diagram.api.services.DiagramLink;
import org.modelio.diagram.elements.core.model.IGmLink;

/**
 * This class represents the DiagramGraphic of a 'ProvidedInterface' element.
 */
@objid ("899033f5-f081-4555-879b-5f5c0fc73ad7")
public class ProvidedInterfaceDG extends DiagramLink {
    /**
     * @param diagramHandle The diagram manipulation class.
     * @param node The gm node represented by this class.
     */
    @objid ("a4702247-46bc-4276-9e87-03dc585aeb32")
    public ProvidedInterfaceDG(DiagramHandle diagramHandle, final IGmLink node) {
        super(diagramHandle, node);
    }

}
