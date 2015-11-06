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

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.swt.SWT;
import org.modelio.diagram.elements.common.abstractdiagram.GmAbstractDiagram;
import org.modelio.diagram.elements.core.commands.ModelioCreationContext;
import org.modelio.diagram.elements.core.figures.FigureUtilities2.HighlightType;
import org.modelio.diagram.elements.core.figures.FigureUtilities2;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeModel;
import org.modelio.diagram.elements.core.requests.ModelElementDropRequest;
import org.modelio.vcore.smkernel.mapi.MObject;
import org.modelio.vcore.smkernel.mapi.MRef;

/**
 * A default drop policy implementation that can process ModelElementDropRequest requests.
 */
@objid ("80bbb77e-1dec-11e2-8cad-001ec947c8cc")
public class DefaultElementDropEditPolicy extends GraphicalEditPolicy {
    @objid ("92531b5f-1e83-11e2-8cad-001ec947c8cc")
    protected HighlightType highlightType;

    @objid ("67fefb7f-1e83-11e2-8cad-001ec947c8cc")
    protected IFigure highlight;

    @objid ("80bbb786-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public EditPart getTargetEditPart(Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            return getDropTargetEditPart((ModelElementDropRequest) request);
        }
        // else
        return null;
    }

    /**
     * Redefined to handle {@link ModelElementDropRequest} request by calling
     * {@link #getDropCommand(ModelElementDropRequest)}. <br>
     * The DefaultElementDropPolicy also manages the smart interaction drops.
     */
    @objid ("80be198c-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Command getCommand(Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            // Start by looking for a "smart" behaviour.
            final Command command = getSmartDropCommand((ModelElementDropRequest) request);
            if (command == null) {
                // If none found, look for a "regular" behaviour.
                return getDropCommand((ModelElementDropRequest) request);
            }
            return command;
        }
        return null;
    }

    /**
     * <p>
     * Creates the Command to handle a ModelElementDropRequest. This default implementation will delegate a
     * CreateRequest for each dropped element.
     * </p>
     * @param request The drop request.
     * @return the created command.
     */
    @objid ("80be1997-1dec-11e2-8cad-001ec947c8cc")
    protected Command getDropCommand(ModelElementDropRequest request) {
        final CompoundCommand command = new CompoundCommand();
        
        Point dropLocation = request.getDropLocation();
        for (final MObject toUnmask : request.getDroppedElements()) {
            final GmModel hostModel = (GmModel) getHost().getModel();
            final GmAbstractDiagram gmDiagram = hostModel.getDiagram();
            final GmModel previousUnmask = getVisibleModelFor(toUnmask, gmDiagram);
            if (previousUnmask == null) {
                createDropCommandForNode(command, dropLocation, toUnmask);
                // Introduce some offset, so that all elements are not totally on
                // top of each other.
                dropLocation = dropLocation.getTranslated(20, 20);
            } else {
                // Otherwise, just select it.
                createSelectionCommand(command, previousUnmask);
            }
        }
        return command.unwrap();
    }

    /**
     * <p>
     * Returns the edit part the DROP is to be processed on. If the Gm corresponding to the host edit part knows how to
     * unmask each dropped element, the host is returned. Otherwise, <code>null</code> is returned.
     * </p>
     * <p>
     * Subclasses should redefine this method to provide "smart interactions".
     * </p>
     * @param request the drop request
     * @return the host if all dropped elements can be unmasked by the Gm, <code>null</code> otherwise.
     */
    @objid ("80be199f-1dec-11e2-8cad-001ec947c8cc")
    protected EditPart getDropTargetEditPart(ModelElementDropRequest request) {
        // If either of the dropped elements cannot be unmasked, return null.
        for (final MObject droppedElement : request.getDroppedElements()) {
            final Object omodel = getHost().getModel();
            if (!(omodel instanceof GmModel)) 
                return null;
            
            final GmModel hostModel = (GmModel) omodel;
            if (!hostModel.canUnmask(droppedElement)) {
                return null;
            }
        
        }
        // All dropped elements understood: return host!
        return getHost();
    }

    @objid ("80be19a7-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void showTargetFeedback(Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            // compute highlight type
            final Command c = getCommand(request);
            HighlightType newHighlightType = FigureUtilities2.HighlightType.INFO;
            if (c == null) {
                newHighlightType = FigureUtilities2.HighlightType.ERROR;
            } else if (c.canExecute()) {
                newHighlightType = FigureUtilities2.HighlightType.SUCCESS;
            } else {
                newHighlightType = FigureUtilities2.HighlightType.WARNING;
            }
        
            if (newHighlightType != this.highlightType) {
                // configure the highlight figure
                this.highlightType = newHighlightType;
        
                // create a highlight figure if it does not exist
                if (this.highlight == null) {
                    // create a highlight figure
                    this.highlight = FigureUtilities2.createHighlightFigure(getFeedbackLayer(),
                                                                            getHostFigure(),
                                                                            this.highlightType);
                    // add the highlight figure to the feedback layer
                    getFeedbackLayer().add(this.highlight);
                }
                FigureUtilities2.updateHighlightType(this.highlight, this.highlightType);
            }
        
        }
        super.showTargetFeedback(request);
    }

    @objid ("80be19ad-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void eraseTargetFeedback(Request request) {
        if (request.getType().equals(ModelElementDropRequest.TYPE)) {
            if (this.highlight != null) {
                removeFeedback(this.highlight);
                this.highlight = null;
                this.highlightType = null;
            }
        }
        
        super.eraseTargetFeedback(request);
    }

    /**
     * <p>
     * Creates the Command to "smartly" handle a ModelElementDropRequest. This default implementation returns
     * <code>null</code>.
     * </p>
     * <p>
     * Subclasses should redefine this method to provide "smart interactions".
     * </p>
     * @param request The drop request.
     * @return the created command.
     */
    @objid ("80be19b3-1dec-11e2-8cad-001ec947c8cc")
    protected Command getSmartDropCommand(ModelElementDropRequest request) {
        return null;
    }

    @objid ("80be19bb-1dec-11e2-8cad-001ec947c8cc")
    protected void createSelectionCommand(CompoundCommand command, GmModel toSelect) {
        final SelectionRequest request = new SelectionRequest();
        request.setType(REQ_SELECTION);
        request.setLastButtonPressed(SWT.BUTTON_MASK);
        final EditPart toSelectEditPart = (EditPart) getHost().getViewer()
                                                              .getEditPartRegistry()
                                                              .get(toSelect);
        EditPart target = null;
        if (toSelectEditPart != null) {
            target = toSelectEditPart.getTargetEditPart(request);
            if (target == null) {
                target = toSelectEditPart;
            }
            Command selectionCommand = target.getCommand(request);
            if (selectionCommand == null) {
                selectionCommand = new RevealEditPartCommand(target);
            }
            command.add(selectionCommand);
        }
    }

    /**
     * Creates a drop command for an element that will be unmasked as a node.
     * @param command the compound in which to add the created command.
     * @param dropLocation the point where the drop happened.
     * @param toUnmask the element to unmask.
     */
    @objid ("80be19c1-1dec-11e2-8cad-001ec947c8cc")
    protected void createDropCommandForNode(CompoundCommand command, Point dropLocation, MObject toUnmask) {
        // If not yet unmasked in diagram, unmask it.
        final CreateRequest req = new CreateRequest();
        req.setLocation(dropLocation);
        req.setSize(new Dimension(-1, -1));
        req.setFactory(new ModelioCreationContext(toUnmask));
        final EditPart targetPart = getHost().getTargetEditPart(req);
        if (targetPart != null) {
            command.add(targetPart.getCommand(req));
        }
    }

    @objid ("80be19cb-1dec-11e2-8cad-001ec947c8cc")
    protected GmModel getVisibleModelFor(final MObject element, final GmAbstractDiagram inDiagram) {
        final List<GmModel> allGMRepresenting = inDiagram.getAllGMRepresenting(new MRef(element));
        for (final GmModel gmRepresenting : allGMRepresenting) {
            if (!(gmRepresenting instanceof GmNodeModel) || ((GmNodeModel) gmRepresenting).isVisible()) {
                return gmRepresenting;
            }
        }
        return null;
    }

}
