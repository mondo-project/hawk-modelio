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
                                    

package org.modelio.diagram.editor.bpmn.elements.bpmnmessageflow;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;
import org.modelio.diagram.elements.common.linkednode.LinkedNodeRequestConstants;
import org.modelio.diagram.elements.core.figures.decorations.DefaultPolygonDecoration;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;

/**
 * Edit part for {@link GmBpmnMessageFlow}.
 */
@objid ("616b629a-55b6-11e2-877f-002564c97630")
public class BpmnMessageFlowEditPart extends GmLinkEditPart {
    @objid ("616b629e-55b6-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        PolylineConnection connection = (PolylineConnection) super.createFigure();
        connection.setTargetDecoration(getArrowDecoration());
        connection.setSourceDecoration(getCircleDecoration());
        return connection;
    }

    @objid ("616ce93d-55b6-11e2-877f-002564c97630")
    private RotatableDecoration getArrowDecoration() {
        DefaultPolygonDecoration decoration = new DefaultPolygonDecoration();
        decoration.setTemplate(PolygonDecoration.TRIANGLE_TIP);
        decoration.setScale(8, 5);
        decoration.setOpaque(true);
        decoration.setFill(true);
        decoration.setBackgroundColor(ColorConstants.white);
        return decoration;
    }

    @objid ("616ce941-55b6-11e2-877f-002564c97630")
    private RotatableDecoration getCircleDecoration() {
        DefaultPolygonDecoration decoration = new DefaultPolygonDecoration();
        
        PointList points = new PointList(new int[] { 0, -2, 0, 2, 0, 0, -1, 0, -1, 3, -1, -3, -1, 0, -2, 0,
                -2, 3, -2, -3, -2, 0, -3, 0, -3, 3, -3, -3, -3, 0, -4, 0, -4, 3, -4, -3, -4, 0, -5, 0, -5,
                -2, -5, 2 });
        decoration.setTemplate(points);
        decoration.setScale(1, 1);
        decoration.setOpaque(true);
        decoration.setBackgroundColor(ColorConstants.white);
        return decoration;
    }

    @objid ("616ce945-55b6-11e2-877f-002564c97630")
    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(LinkedNodeRequestConstants.REQ_LINKEDNODE_START,
                          new LinkedMessageFlowStartEditPolicy());
    }

}
