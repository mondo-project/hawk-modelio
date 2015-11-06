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
                                    

package org.modelio.diagram.editor.sequence.elements.message.label;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.behavior.interactionModel.Message;

/**
 * Utility class that computes Message symbol.
 */
@objid ("d95a82c9-55b6-11e2-877f-002564c97630")
public class MessageLabelProvider {
    /**
     * This class is not instantiable.
     */
    @objid ("d95a82cb-55b6-11e2-877f-002564c97630")
    private MessageLabelProvider() {
    }

    /**
     * Get the Message label at the following format: "name : representedType [min..max]"
     * @param c the Message
     * @return the computed label
     */
    @objid ("d95a82ce-55b6-11e2-877f-002564c97630")
    public static String computeSimpleLabel(final Message c) {
        final StringBuilder s = new StringBuilder(60);
        
        if (c.getInvoked() != null) {
            // Compute the label from the operation's name
            s.append(c.getInvoked().getName());
            s.append(" (");
            s.append(c.getArgument());
            s.append(")");
        } else if (c.getSignalSignature() != null) {
            // Compute the label from the signal's name
            s.append(c.getSignalSignature().getName());
        } else {
            // Compute the label from the message's name
            s.append(c.getName());
        }
        return s.toString();
    }

}
