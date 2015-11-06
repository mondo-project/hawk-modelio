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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnstartevent;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.modelio.diagram.editor.bpmn.plugin.DiagramEditorBpmn;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeStartCreationEditPolicy;
import org.modelio.diagram.elements.core.figures.ColorizableImageFigure;
import org.modelio.diagram.elements.core.link.DefaultCreateLinkEditPolicy;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.elements.core.node.GmNodeEditPart;
import org.modelio.diagram.elements.core.policies.DefaultNodeResizableEditPolicy;
import org.modelio.diagram.elements.core.tools.multipoint.CreateMultiPointRequest;
import org.modelio.diagram.elements.umlcommon.constraint.ConstraintLinkEditPolicy;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;
import org.modelio.metamodel.Metamodel;
import org.modelio.metamodel.bpmn.events.BpmnStartEvent;

/**
 * EditPart for an {@link GmBpmnStartEventPrimaryNode}.
 */
@objid ("61b6273a-55b6-11e2-877f-002564c97630")
public final class BpmnStartEventEditPart extends GmNodeEditPart {
    @objid ("61b7adda-55b6-11e2-877f-002564c97630")
    @Override
    public boolean isSelectable() {
        return false;
    }

    @objid ("61b7addf-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.NODE_ROLE, new DefaultCreateLinkEditPolicy());
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                new LinkedNodeStartCreationEditPolicy());
        installEditPolicy(CreateMultiPointRequest.REQ_MULTIPOINT_FIRST, new ConstraintLinkEditPolicy(false));
    }

    @objid ("61b7ade2-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        Image image = DiagramEditorBpmn
                .getImageRegistry()
                .getImage(BpmnStartEventImageService.getImageRef((GmModel) this.getModel()));
        
        if (image == null) {
            image = DiagramEditorBpmn.getImageRegistry().getImage(Metamodel.getMClass(BpmnStartEvent.class).getName());
        }
        ColorizableImageFigure fig = new ColorizableImageFigure(image);
        fig.setPreferredSize(33, 33);
        fig.setMinimumSize(new Dimension(33, 33));
        
        // set style dependent properties
        refreshFromStyle(fig, getModelStyle());
        
        // return the figure
        return fig;
    }

    @objid ("61b7ade7-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        if (!switchRepresentationMode()) {
            super.refreshFromStyle(aFigure, style);
        
            if (aFigure instanceof ColorizableImageFigure) {
                ColorizableImageFigure cFigure = (ColorizableImageFigure) aFigure;
                final GmModel gmModel = (GmModel) getModel();
                Color color = style.getColor(gmModel.getStyleKey(MetaKey.FILLCOLOR));
                cFigure.setColor(color);
            }
        }
    }

    @objid ("61b7adee-55b6-11e2-877f-002564c97630")
    @Override
    protected void refreshVisuals() {
        GmBpmnStartEventPrimaryNode initialNodeModel = (GmBpmnStartEventPrimaryNode) this.getModel();
        getFigure().getParent().setConstraint(getFigure(), initialNodeModel.getLayoutData());
    }

    @objid ("61b7adf1-55b6-11e2-877f-002564c97630")
    @Override
    public SelectionEditPolicy getPreferredDragRolePolicy(final String requestType) {
        return new DefaultNodeResizableEditPolicy() {
            @Override
            protected Command getResizeCommand(ChangeBoundsRequest request) {
                ChangeBoundsRequest req = new ChangeBoundsRequest(REQ_RESIZE_CHILDREN);
                req.setEditParts(getHost());
        
                req.setMoveDelta(request.getMoveDelta());
        
                int dimension = 0;
                int x = request.getSizeDelta().height;
                int y = request.getSizeDelta().width;
        
                if (x >= 0 && y >= 0) {
                    if (x > y) {
                        dimension = x;
                    } else {
                        dimension = y;
                    }
                } else {
                    if (x < y) {
                        dimension = x;
                    } else {
                        dimension = y;
                    }
                }
        
                req.setSizeDelta(new Dimension(dimension, dimension));
                req.setLocation(request.getLocation());
                req.setExtendedData(request.getExtendedData());
                req.setResizeDirection(request.getResizeDirection());
        return getHost().getParent().getCommand(req);
                    }
                };
    }

}
