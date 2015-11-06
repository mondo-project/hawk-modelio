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
                                    

package org.modelio.diagram.elements.core.policies;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.diagram.elements.core.requests.ModelElementSmartDropRequest;

@objid ("80c2de59-1dec-11e2-8cad-001ec947c8cc")
public class DefaultSmartDropEditPolicy extends GraphicalEditPolicy {
    @objid ("5699963e-dcab-43db-819e-865e14dbc28a")
    private RectangleFigure highlight;

    @objid ("80c2de5f-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (request.getType().equals(ModelElementSmartDropRequest.TYPE))
            return getSmartDropTargetEditPart((ModelElementSmartDropRequest) request);
        else
            return super.getTargetEditPart(request);
    }

    /**
     * Redefined to handle {@link ModelElementSmartDropRequest} request by calling
     * {@link #getSmartDropCommand(ModelElementSmartDropRequest)}, or call super. The DefaultElementDropPolicy also manages the
     * smart interaction drops.
     */
    @objid ("80c54094-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Command getCommand(Request request) {
        if (request.getType().equals(ModelElementSmartDropRequest.TYPE)) {
            return getSmartDropCommand((ModelElementSmartDropRequest) request);
        } else {
            return super.getCommand(request);
        }
    }

    /**
     * Creates the Command to handle a ModelElementSmartDropRequest. When subclassing the policy, this method has to be redefined to
     * provide the expected behaviour
     * @param request The drop request.
     * @return the created command.
     */
    @objid ("80c5409e-1dec-11e2-8cad-001ec947c8cc")
    protected Command getSmartDropCommand(ModelElementSmartDropRequest request) {
        // return un-executable command by default
        return null; // UnexecutableCommand.INSTANCE;
    }

    /**
     * Returns the edit part the SMART DROP is to be processed on. In this default implementation simply returns the EditPart which
     * the policy has been installed on (host) When subclassing the policy, this method can be redefined.
     * @param request @return
     */
    @objid ("80c540a6-1dec-11e2-8cad-001ec947c8cc")
    protected EditPart getSmartDropTargetEditPart(ModelElementSmartDropRequest request) {
        return this.getHost();
    }

    @objid ("80c540ae-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void showTargetFeedback(Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            if (this.highlight == null) {
                this.highlight = new RectangleFigure();
                this.highlight.setBounds(getHostFigure().getBounds().getCopy().expand(1, 1));
                this.highlight.setFill(false);
                addFeedback(this.highlight);
            }
        }
        super.showTargetFeedback(request);
    }

    @objid ("80c540b4-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void eraseTargetFeedback(Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            if (this.highlight != null) {
                removeFeedback(this.highlight);
                this.highlight = null;
            }
        }
        
        super.eraseTargetFeedback(request);
    }

}
