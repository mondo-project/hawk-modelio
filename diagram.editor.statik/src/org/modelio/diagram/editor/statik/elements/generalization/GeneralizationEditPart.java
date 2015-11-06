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
                                    

package org.modelio.diagram.editor.statik.elements.generalization;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.modelio.diagram.elements.core.figures.LinkFigure;
import org.modelio.diagram.elements.core.figures.decorations.DefaultPolygonDecoration;
import org.modelio.diagram.elements.core.link.GmLinkEditPart;
import org.modelio.diagram.elements.core.model.GmModel;
import org.modelio.diagram.styles.core.IStyle;
import org.modelio.diagram.styles.core.MetaKey;

/**
 * Inheritance link edit part.
 */
@objid ("34eac1ba-55b7-11e2-877f-002564c97630")
public class GeneralizationEditPart extends GmLinkEditPart {
    @objid ("34eac1be-55b7-11e2-877f-002564c97630")
    @Override
    protected IFigure createFigure() {
        PolylineConnection connection = (PolylineConnection) super.createFigure();
        
        // Arrow toward target
        DefaultPolygonDecoration arrow = new DefaultPolygonDecoration();
        arrow.setTemplate(PolygonDecoration.TRIANGLE_TIP);
        arrow.setScale(8, 5);
        arrow.setOpaque(true);
        arrow.setFill(true);
        connection.setTargetDecoration(arrow);
        
        refreshFromStyle(connection, getModelStyle());
        return connection;
    }

    @objid ("34eac1c3-55b7-11e2-877f-002564c97630")
    @Override
    protected void refreshFromStyle(IFigure aFigure, IStyle style) {
        final LinkFigure genFigure = (LinkFigure) aFigure;
        
        super.refreshFromStyle(aFigure, style);
        
        final GmModel gmModel = (GmModel) getModel();
        
        if (genFigure.getTargetDecoration() != null && gmModel.getStyleKey(MetaKey.FILLCOLOR) != null)
            genFigure.getTargetDecoration()
                     .setBackgroundColor(style.getColor(gmModel.getStyleKey(MetaKey.FILLCOLOR)));
    }

}
