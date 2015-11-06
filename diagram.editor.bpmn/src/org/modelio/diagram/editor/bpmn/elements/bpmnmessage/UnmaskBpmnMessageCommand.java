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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnmessage;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.modelio.diagram.elements.common.abstractdiagram.AbstractDiagramEditPart;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.link.GmLink;
import org.modelio.diagram.elements.core.link.ModelioLinkCreationContext;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.model.IGmLinkable;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.metamodel.bpmn.flows.BpmnMessage;
import org.modelio.metamodel.bpmn.flows.BpmnMessageFlow;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * Specific command that will unmask a message and the link between it and the annoted model element.
 */
@objid ("6169dc00-55b6-11e2-877f-002564c97630")
public class UnmaskBpmnMessageCommand extends Command {
    @objid ("6169dc02-55b6-11e2-877f-002564c97630")
    private final BpmnMessage theBpmnMessage;

    @objid ("6169dc06-55b6-11e2-877f-002564c97630")
    private final Object constraint;

    @objid ("71f22f9b-55c1-11e2-9337-002564c97630")
    private final GmAbstractDiagram diagram;

    @objid ("71f22f9d-55c1-11e2-9337-002564c97630")
    private final AbstractDiagramEditPart host;

    @objid ("471f1a2b-db38-4a0c-a60c-4b599487ac1f")
    private final Point dropLocation;

    /**
     * C'tor.
     * @param theBpmnMessage the note to unmask.
     * @param host the edit part of the diagram in which to unmask it.
     * @param initialLayoutData the initial layout data for the node part
     * @param dropLocation the drop location retreived from the request.
     */
    @objid ("6169dc12-55b6-11e2-877f-002564c97630")
    public UnmaskBpmnMessageCommand(final BpmnMessage theBpmnMessage, final AbstractDiagramEditPart host, final Object initialLayoutData, final Point dropLocation) {
        this.theBpmnMessage = theBpmnMessage;
        this.host = host;
        this.diagram = (GmAbstractDiagram) host.getModel();
        this.constraint = initialLayoutData;
        this.dropLocation = dropLocation;
    }

    @objid ("6169dc21-55b6-11e2-877f-002564c97630")
    @Override
    public void execute() {
        // unmask the node part
        GmNodeModel targetModel = this.diagram.unmask(this.diagram, this.theBpmnMessage, this.constraint);
        // Simulate creation of the link: 
        // 1 - start creation of a connection
        final CreateConnectionRequest req = new CreateConnectionRequest();
        req.setLocation(this.dropLocation);
        req.setSize(new Dimension(-1, -1));
        req.setFactory(new ModelioLinkCreationContext(this.theBpmnMessage));
        req.setType(RequestConstants.REQ_CONNECTION_START);
        
        // Look for edit part of subject element... If none found, unmask it.
        List<BpmnMessageFlow> subjects = this.theBpmnMessage.getMessageFlow();
        for (BpmnMessageFlow subject : subjects) {
            EditPart sourceEditPart = getEditPartFor(subject, req);
            if (sourceEditPart == null) {
                unmaskElement(subject, this.dropLocation.getTranslated(-50, 0));
                sourceEditPart = getEditPartFor(subject, req);
                if (sourceEditPart == null) {
                    // Failed to get subject element, revert what was done until now and abort.
                    targetModel.delete();
                    return;
                }
            }
            IGmLinkable sourceModel = (IGmLinkable) sourceEditPart.getModel();
            // Unmask the link
            GmLink link = this.diagram.unmaskLink(this.theBpmnMessage);
            targetModel.addEndingLink(link);
            sourceModel.addStartingLink(link);
        }
    }

    @objid ("6169dc24-55b6-11e2-877f-002564c97630")
    private EditPart getEditPartFor(final MObject element, final CreateConnectionRequest req) {
        // Search all gm related the element
        List<GmModel> models = this.diagram.getAllGMRelatedTo(new MRef(element));
        // This boolean will be used to note that the searched End was found
        // unmasked at least once.
        for (GmModel model : models) {
            // For each gm, search the corresponding edit part
            EditPart editPart = (EditPart) this.host.getViewer().getEditPartRegistry().get(model);
            if (editPart != null) {
                // See if this edit part accepts the reconnection request
                EditPart targetEditPart = editPart.getTargetEditPart(req);
                if (targetEditPart != null) {
                    return targetEditPart;
                }
            }
        }
        return null;
    }

    @objid ("6169dc2e-55b6-11e2-877f-002564c97630")
    private void unmaskElement(final MObject element, final Point location) {
        ModelElementDropRequest dropRequest = new ModelElementDropRequest();
        dropRequest.setDroppedElements(new MObject[] { element });
        dropRequest.setLocation(location);
        EditPart targetEditPart = this.host.getTargetEditPart(dropRequest);
        Command command = targetEditPart.getCommand(dropRequest);
        if (command != null && command.canExecute())
            command.execute();
    }

}
