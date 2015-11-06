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
                                    

package org.modelio.diagram.editor.activity.elements.decisionmerge;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.common.text.MultilineTextFigure;

/**
 * @author fpoyer
 */
@objid ("2a477ac8-55b6-11e2-877f-002564c97630")
public class InputBehaviourText extends MultilineTextFigure {
    @objid ("2a477ace-55b6-11e2-877f-002564c97630")
    private static final int FOLDSIZE = 12;

    @objid ("2a477acc-55b6-11e2-877f-002564c97630")
    private static final PointList foldTemplate = new PointList(new int[] { 0, 0, 0,
            InputBehaviourText.FOLDSIZE, InputBehaviourText.FOLDSIZE, InputBehaviourText.FOLDSIZE });

    /**
     * @param text the text to display.
     */
    @objid ("2a477ad0-55b6-11e2-877f-002564c97630")
    public InputBehaviourText(String text) {
        super(text);
    }

    @objid ("2a477ad4-55b6-11e2-877f-002564c97630")
    @Override
    protected void paintFigure(Graphics graphics) {
        final Rectangle aBounds = getBounds().getCopy();
        final Rectangle originalClip = new Rectangle();
        graphics.getClip(originalClip);
        
        if (this.isOpaque()) {
            final Path path = new Path(Display.getCurrent());
            final Rectangle r1 = new Rectangle(aBounds.x, aBounds.y, aBounds.width - FOLDSIZE, aBounds.height);
            final Rectangle r2 = new Rectangle(aBounds.x + aBounds.width - FOLDSIZE,
                                               aBounds.y + FOLDSIZE,
                                               FOLDSIZE,
                                               aBounds.height);
            path.addRectangle(r1.x, r1.y, r1.width, r1.height);
            path.addRectangle(r2.x, r2.y, r2.width, r2.height);
            graphics.setClip(path);
            // now let the BoxFigure draw the gradient
            super.paintFigure(graphics);
        
            path.dispose();
            graphics.setClip(originalClip);
        
            // paint the fold background
            final PointList polygon = InputBehaviourText.foldTemplate.getCopy();
            polygon.translate(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y);
            graphics.fillPolygon(polygon);
        
        } // end is opaque
        
        // Draw outline
        
        if (this.penOptions.lineWidth % 2 == 1) {
            aBounds.width--;
            aBounds.height--;
        }
        aBounds.shrink(this.penOptions.lineWidth / 2, this.penOptions.lineWidth / 2);
        graphics.setLineWidth(this.penOptions.lineWidth);
        graphics.setForegroundColor(this.penOptions.lineColor);
        
        Point p1 = new Point(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y);
        Point p2 = new Point(aBounds.x + aBounds.width - FOLDSIZE, aBounds.y + FOLDSIZE);
        Point p3 = new Point(aBounds.x + aBounds.width, aBounds.y + FOLDSIZE);
        
        graphics.drawLine(aBounds.getTopLeft(), aBounds.getBottomLeft());
        graphics.drawLine(aBounds.getBottomLeft(), aBounds.getBottomRight());
        graphics.drawLine(aBounds.getBottomRight(), p3);
        graphics.drawLine(p3, p1);
        graphics.drawLine(p1, aBounds.getTopLeft());
        graphics.drawLine(p3, p2);
        graphics.drawLine(p2, p1);
    }

}
