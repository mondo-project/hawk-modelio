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
                                    

package org.modelio.diagram.editor.sequence.elements.modelmanipulation;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.behavior.interactionModel.Interaction;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Helper class for manipulating the model of Sequence Diagrams.
 * 
 * @author fpoyer
 */
@objid ("feaa0c2c-6179-4501-91d3-241eec658a2f")
public class SequenceModelManipulationServices {
    /**
     * Returns the {@link Interaction} enclosing the passed element if any, <code>null</code> otherwise.
     * @param el the element which enclosing Interaction is searched.
     * @return the {@link Interaction} enclosing the passed element if any, <code>null</code> otherwise.
     */
    @objid ("df957203-2194-43f2-a158-8f076295b5af")
    public static Interaction getInteraction(MObject el) {
        MObject composed = el;
        while (composed != null && !(composed instanceof Interaction)) {
            composed = composed.getCompositionOwner();
        }
        return (Interaction) composed;
    }

}
