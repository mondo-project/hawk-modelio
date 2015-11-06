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
                                    

package org.modelio.vaudit.nsuse;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;

/**
 * Handler called by {@link NSUseAnalyser} when a namespace use should be created.
 */
@objid ("6ab5ef7c-8c43-424f-b4b8-3e074b94b9c5")
interface INsUseHandler {
    /**
     * Called when a namespace use should exist .
     * @param aSource the source namespace.
     * @param aDest the used destination namespace.
     * @param aCause the cause of the link.
     */
    @objid ("0e89db3f-d61c-4e3a-a985-2e6979716b2e")
    void addNSUses(NameSpace aSource, NameSpace aDest, Element aCause);

}
