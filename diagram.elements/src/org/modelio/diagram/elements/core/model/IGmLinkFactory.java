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
                                    

package org.modelio.diagram.elements.core.model;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Factory that creates graphic links representing a given link element.
 */
@objid ("80801ca4-1dec-11e2-8cad-001ec947c8cc")
public interface IGmLinkFactory {
    /**
     * Creates a graphic link representing the given link element.
     * @param diagram the diagram in which the gm is to be created
     * @param linkElement The model element to display
     * @return the created graphic link
     */
    @objid ("80801ca6-1dec-11e2-8cad-001ec947c8cc")
    GmLink create(GmAbstractDiagram diagram, MObject linkElement);

    /**
     * Register an GmLink factory extension.
     * <p>
     * Extension GmLink factories are called first when looking for an GmLink.
     * @param id id for the GmLink extension factory
     * @param factory the edit part factory.
     */
    @objid ("80801cab-1dec-11e2-8cad-001ec947c8cc")
    void registerFactory(String id, IGmLinkFactory factory);

    /**
     * Remove a registered extension factory.
     * @param id The id used to register the extension factory.
     */
    @objid ("80801caf-1dec-11e2-8cad-001ec947c8cc")
    void unregisterFactory(String id);

}
