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
                                    

package org.modelio.diagram.elements.core.figures.borders;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Color;
import org.modelio.diagram.elements.core.figures.ZoomDrawer;

/**
 * Border that draws a shadow on the bottom right of the figure.
 * <p>
 * To be used on rectangular shaped figures.
 */
@objid ("7f5f9fdf-1dec-11e2-8cad-001ec947c8cc")
public class ShadowBorder extends AbstractBorder {
    @objid ("7f5f9fe7-1dec-11e2-8cad-001ec947c8cc")
    private int shadowWidth = 1;

    @objid ("f8b4ef9a-b86c-4598-9a91-0cc0c5d26460")
    private Color shadowColor = null;

    @objid ("a0d77c3f-390a-42dc-88e2-6ed950d457ee")
    private Insets myInsets = new Insets(0, 0, 1, 1);

    /**
     * Create a shadow border.
     * @param shadowColor Shadow color
     * @param shadowWidth Shadow width
     */
    @objid ("7f620231-1dec-11e2-8cad-001ec947c8cc")
    public ShadowBorder(Color shadowColor, int shadowWidth) {
        this.shadowColor = shadowColor;
        this.shadowWidth = shadowWidth;
        this.myInsets = new Insets(0, 0, shadowWidth, shadowWidth);
    }

    @objid ("7f620236-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public void paint(IFigure figure, Graphics graphics, Insets insets) {
        // Get border dimensions
        tempRect = getPaintRectangle(figure, insets);
        
        //tempRect.height -= shadowWidth;
        //tempRect.width -= shadowWidth;
        
        if (this.shadowWidth % 2 == 1) {
            tempRect.width--;
            tempRect.height--;
        }
        tempRect.shrink(this.shadowWidth / 2, this.shadowWidth / 2);
        
        // Set shadow color and alpha
        graphics.setAlpha(100);
        ZoomDrawer.setLineWidth(graphics, this.shadowWidth);
        //graphics.setLineWidth(shadowWidth);
        
        if (this.shadowColor != null)
            graphics.setForegroundColor(this.shadowColor);
        
        // Draw the shadow
        PointList points = new PointList();
        points.addPoint(tempRect.getTopRight().translate(0, 3));
        points.addPoint(tempRect.getBottomRight());
        points.addPoint(tempRect.getBottomLeft().translate(3, 0));
        graphics.drawPolyline(points);
        
        //graphics.drawLine(tempRect.getTopRight().translate(0, 3), tempRect.getBottomRight());
        //graphics.drawLine(tempRect.getBottomLeft().translate(3, 0), tempRect.getBottomRight());
    }

    @objid ("7f620242-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Insets getInsets(IFigure figure) {
        return this.myInsets;
    }

}
