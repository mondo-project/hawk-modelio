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
                                    

package org.modelio.diagram.editor.transmuter;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.gproject.model.IMModelServices;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Interface of transmutations services of model elements
 */
@objid ("83d32644-5c05-11e2-a156-00137282c51b")
public interface ITransmuter {
    /**
     * Test if a transmutation between two elements can be done
     * @return True If transmutation can be done, False otherwise
     */
    @objid ("847ecf96-5c05-11e2-a156-00137282c51b")
    boolean canTransmute();

    /**
     * Transmutation of an element
     */
    @objid ("21c1d7fa-5e26-11e2-a8be-00137282c51b")
    MObject transmute(IMModelServices modelServices);

}
