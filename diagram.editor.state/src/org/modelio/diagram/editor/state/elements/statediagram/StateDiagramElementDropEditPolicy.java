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
                                    

package org.modelio.diagram.editor.state.elements.statediagram;

import java.util.Deque;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.modelio.diagram.elements.common.abstractdiagram.DiagramElementDropEditPolicy;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.metamodel.diagrams.StateMachineDiagram;
import org.modelio.metamodel.uml.behavior.stateMachineModel.Region;
import org.modelio.metamodel.uml.behavior.stateMachineModel.StateMachine;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Overrides the default drop handling policy to add a smart interaction:<br>
 * - dropping a state machine creates a submachine State referencing the drag and dropped State Machine.
 */
@objid ("f591b75f-55b6-11e2-877f-002564c97630")
public class StateDiagramElementDropEditPolicy extends DiagramElementDropEditPolicy {
    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("f591b763-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getDropCommand(final ModelElementDropRequest request) {
        CompoundCommand command = new CompoundCommand();
        
        Point dropLocation = request.getDropLocation();
        
        for (MObject toUnmask : request.getDroppedElements()) {
            if (toUnmask instanceof StateMachine) {
                command.add(getBehaviorDropCommand(dropLocation, (StateMachine) toUnmask));
        
                // Introduce some offset, so that all elements are not totally
                // on top of each other.
                dropLocation = dropLocation.getTranslated(20, 20);
            } else {
                command.add(super.getDropCommand(request));
            }
        }
        return command;
    }

    @objid ("f591b76d-55b6-11e2-877f-002564c97630")
    private Command getBehaviorDropCommand(final Point dropLocation, final StateMachine toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final GmAbstractDiagram gmDiagram = gmModel.getDiagram();
        final StateMachineDiagram diag = (StateMachineDiagram) gmDiagram.getRelatedElement();
        final StateMachine owner = (StateMachine) diag.getOrigin();
        return new CreateCallBehaviorCommand(dropLocation, toUnmask, getHost(), owner);
    }

    /**
     * Overridden to allow independent unmasking of the content of a Composite State (i.e. a State with at least 1 region).
     */
    @objid ("f591b777-55b6-11e2-877f-002564c97630")
    @Override
    protected boolean shouldUnmask(MObject element, Deque<MObject> hierarchy) {
        if (!hierarchy.isEmpty() && element instanceof Region && ((Region) element).getParent() != null) {
            return false;
        } else {
            return super.shouldUnmask(element, hierarchy);
        }
    }

}
