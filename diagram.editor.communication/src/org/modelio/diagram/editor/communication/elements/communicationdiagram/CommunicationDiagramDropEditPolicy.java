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
                                    

package org.modelio.diagram.editor.communication.elements.communicationdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.modelio.diagram.elements.common.abstractdiagram.DiagramElementDropEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmCompositeNode;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.metamodel.diagrams.CommunicationDiagram;
import org.modelio.metamodel.uml.behavior.communicationModel.CommunicationInteraction;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.AssociationEnd;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.metamodel.uml.statik.Instance;
import org.modelio.metamodel.uml.statik.Port;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
 * (eg: for pins).
 * 
 * @author fpoyer
 */
@objid ("7a268fdd-55b6-11e2-877f-002564c97630")
public class CommunicationDiagramDropEditPolicy extends DiagramElementDropEditPolicy {
    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("7a268fe1-55b6-11e2-877f-002564c97630")
    @Override
    protected EditPart getDropTargetEditPart(final ModelElementDropRequest request) {
        final GmCompositeNode gmModel = (GmCompositeNode) this.getHost().getModel();
        
        // If either of the dropped elements cannot be unmasked, return null.
        for (MObject droppedElement : request.getDroppedElements()) {
            if (!gmModel.canUnmask(droppedElement)) {
                // Gm doesn't know how to handle this element directly, look if
                // it is an element for which we can do something "smarter".
                if (!isSmartDropTarget(droppedElement, request)) {
                    // It is not a smart interaction
                    //
                    // The Gm cannot unmask this element directly, and we don't know
                    // what to do with it... return null
                    return null;
                }
            }
        }
        
        // All dropped elements understood: return host!
        return this.getHost();
    }

    /**
     * <p>
     * Overrides the default drop handling policy so that in some case it searches for the parent of the dropped element
     * (eg: for pins).
     * </p>
     */
    @objid ("7a268feb-55b6-11e2-877f-002564c97630")
    @Override
    protected Command getDropCommand(final ModelElementDropRequest request) {
        CompoundCommand command = new CompoundCommand();
        
        Point dropLocation = request.getDropLocation();
        
        for (MObject toUnmask : request.getDroppedElements()) {
        
            if (isSmartDropTarget(toUnmask, request)) {
                command.add(getSmartDropCommand(dropLocation, toUnmask));
            } else if (toUnmask != null) {
                createSubRequest(request, command, dropLocation, toUnmask);
            }
        
            // Introduce some offset, so that all elements are not totally
            // on top of each other.
            dropLocation = dropLocation.getTranslated(20, 20);
        }
        return command.unwrap();
    }

    @objid ("7a281679-55b6-11e2-877f-002564c97630")
    private Command getSmartDropCommand(final Point dropLocation, final MObject toUnmask) {
        final GmModel gmModel = (GmModel) getHost().getModel();
        final CommunicationDiagram owner = (CommunicationDiagram) gmModel.getRelatedElement();
        ModelElement origin = owner.getOrigin();
        if (origin instanceof CommunicationInteraction) {
            return new SmartCreateCommunicationNodeCommand(dropLocation, toUnmask, getHost(), origin);
        } else {
            // No interaction, return null
            return null;
        }
    }

    @objid ("7a281682-55b6-11e2-877f-002564c97630")
    private boolean isSmartDropTarget(final MObject element, final ModelElementDropRequest request) {
        if (!request.isSmart())
            return false;
        return (element instanceof AssociationEnd ||
                element instanceof Attribute ||
                (element instanceof Instance && !(element instanceof Port)) || element instanceof GeneralClass);
    }

    @objid ("7a28168e-55b6-11e2-877f-002564c97630")
    private void createSubRequest(final ModelElementDropRequest request, final CompoundCommand command, final Point dropLocation, final MObject toUnmask) {
        ModelElementDropRequest subReq = new ModelElementDropRequest();
        subReq.setDroppedElements(new MObject[] { toUnmask });
        subReq.setExtendedData(request.getExtendedData());
        subReq.setLocation(dropLocation);
        subReq.isSmart(request.isSmart());
        command.add(super.getDropCommand(subReq));
    }

}
