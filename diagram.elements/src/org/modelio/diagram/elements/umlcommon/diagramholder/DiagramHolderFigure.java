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
                                    

package org.modelio.diagram.elements.umlcommon.diagramholder;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.ToolbarLayoutWithGrab;

/**
 * A diagram holder figure looks like a photo:
 * 
 * <pre>
 * p0---------p1
 * |          |
 * |          |
 * |          |
 * |       p3-p2
 * |       | /
 * p5------p4
 * </pre>
 */
@objid ("81354f6b-1dec-11e2-8cad-001ec947c8cc")
public class DiagramHolderFigure extends GradientFigure {
    /**
     * Size of the fold.
     */
    @objid ("81354f72-1dec-11e2-8cad-001ec947c8cc")
     static final int FOLDSIZE = 12;

    /**
     * Template for the corner fold
     */
    @objid ("6645e877-1e83-11e2-8cad-001ec947c8cc")
    private static final PointList foldTemplate = new PointList(new int[] { -DiagramHolderFigure.FOLDSIZE,
            -DiagramHolderFigure.FOLDSIZE, 0, -DiagramHolderFigure.FOLDSIZE, -DiagramHolderFigure.FOLDSIZE, 0 });

    /**
     * Creates a note figure.
     */
    @objid ("8137b175-1dec-11e2-8cad-001ec947c8cc")
    public DiagramHolderFigure() {
        // The note figure is a container layouted as a vertical toolbar
        // Children are transparent without borders
        ToolbarLayout layout = new ToolbarLayoutWithGrab(false);
        layout.setStretchMinorAxis(true);
        setLayoutManager(layout);
    }

    @objid ("8137b178-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void paintFigure(Graphics graphics) {
        /*
         * 
         *  Figure shape:
         *  
         *  p0------p1
         *  |       | \
         *  |       p2-p3
         *  |          |
         *  |          |
         *  p5---------p4
         * 
        final Point p0 = figBounds.getTopLeft();
        final Point p1 = new Point(figBounds.x + figBounds.width - FOLDSIZE, figBounds.y);
        final Point p2 = new Point(figBounds.x + figBounds.width - FOLDSIZE, figBounds.y + FOLDSIZE);
        final Point p3 = new Point(figBounds.x + figBounds.width, figBounds.y + FOLDSIZE);
        final Point p4 = figBounds.getBottomRight();
        final Point p5 = figBounds.getBottomLeft();
         *  
         *  p0---------p1
         *  |    /     |
         *  |----      |
         *  |          |
         *  |       p3-p2
         *  |       | / 
         *  p5------p4
         * 
         */
        final Rectangle figBounds = getBounds().getResized(-1, -1);
        
        final Point p0 = figBounds.getTopLeft();
        final Point p1 = figBounds.getTopRight();
        final Point p2 = new Point(figBounds.right(), figBounds.bottom() - FOLDSIZE);
        final Point p3 = new Point(figBounds.right() - FOLDSIZE, figBounds.bottom() - FOLDSIZE);
        final Point p4 = new Point(figBounds.right() - FOLDSIZE, figBounds.bottom());
        final Point p5 = figBounds.getBottomLeft();
        
        if (this.isOpaque()) {
            final Rectangle originalClip = new Rectangle();
            graphics.getClip(originalClip);
        
            // Clip to exclude the fold
            final Path path = new Path(Display.getCurrent());
            path.moveTo(p0.x, p0.y);
            path.lineTo(p1.x, p1.y);
            path.lineTo(p2.x, p2.y);
            path.lineTo(p3.x, p3.y);
            path.lineTo(p4.x, p4.y);
            path.lineTo(p5.x, p5.y);
            path.lineTo(p0.x, p0.y);
        
            graphics.setClip(path);
        
            // Now let the BoxFigure draw the gradient
            super.paintFigure(graphics);
        
            path.dispose();
            graphics.setClip(originalClip);
        
            // paint the fold background
            final PointList polygon = DiagramHolderFigure.foldTemplate.getCopy();
            polygon.translate(p1.x, p1.y);
            graphics.fillPolygon(polygon);
        
        } // end is opaque
        
        // Draw outline
        if (this.penOptions.lineWidth % 2 == 1) {
            figBounds.width--;
            figBounds.height--;
        }
        figBounds.shrink(this.penOptions.lineWidth / 2, this.penOptions.lineWidth / 2);
        graphics.setLineWidth(this.penOptions.lineWidth);
        graphics.setForegroundColor(this.penOptions.lineColor);
        
        /*
        *  p0---------p1
        *  |    /     |
        *  |----      |
        *  |          |
        *  |       p3-p2
        *  |       | / 
        *  p5------p4
        * 
        */
        graphics.drawLine(p0, p5);
        graphics.drawLine(p5, p4);
        graphics.drawLine(p4, p3);
        graphics.drawLine(p4, p2);
        graphics.drawLine(p3, p2);
        graphics.drawLine(p2, p1);
        graphics.drawLine(p1, p0);
        
        // Draw separation line
        /*graphics.setLineStyle(SWT.LINE_DOT);
        final Rectangle r = this.scrollPane.getBounds();
        graphics.drawLine(r.x - 8, r.y, r.x + r.width - 8, r.y);*/
    }

    @objid ("8137b17e-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void paintBorder(final Graphics graphics) {
        super.paintBorder(graphics);
        
        final Rectangle figBounds = getBounds().getResized(-1, -1);
        
        // paint the fold background
        final PointList polygon = DiagramHolderFigure.foldTemplate.getCopy();
        polygon.translate(figBounds.getBottomRight());
        graphics.fillPolygon(polygon);
        graphics.drawPolygon(polygon);
    }

}
