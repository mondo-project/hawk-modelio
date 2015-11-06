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
                                    

package org.modelio.diagram.editor.activity.elements.figures;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.modelio.diagram.elements.common.portcontainer.PortConstraint.Border;
import org.modelio.diagram.elements.core.figures.GradientFigure;
import org.modelio.diagram.elements.core.figures.IOrientableShaper.Orientation;

@objid ("2a6da078-55b6-11e2-877f-002564c97630")
public class InputPinFigure extends GradientFigure {
    @objid ("2a6da07c-55b6-11e2-877f-002564c97630")
    protected Orientation orientation = Orientation.Undefined;

    @objid ("2a6da07b-55b6-11e2-877f-002564c97630")
    private LineBorder lineBorder;

    @objid ("2a6da07f-55b6-11e2-877f-002564c97630")
    public InputPinFigure() {
        super();
        this.setOpaque(true);
        this.lineBorder = new LineBorder(1);
        setBorder(this.lineBorder);
    }

    @objid ("2a6f26fa-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineColor(Color lineColor) {
        if (lineColor != this.penOptions.lineColor) {
            super.setLineColor(lineColor);
            this.lineBorder.setColor(lineColor);
        }
    }

    @objid ("2a6f26fe-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineWidth(int lineWidth) {
        if (lineWidth != this.penOptions.lineWidth) {
            super.setLineWidth(lineWidth);
            this.lineBorder.setWidth(lineWidth);
        }
    }

    @objid ("2a6f2702-55b6-11e2-877f-002564c97630")
    public void setOrientation(Border b) {
        switch (b) {
            case North:
                this.orientation = Orientation.NorthSouth;
                break;
            case South:
                this.orientation = Orientation.SouthNorth;
                break;
            case East:
                this.orientation = Orientation.EastWest;
                break;
            case West:
                this.orientation = Orientation.WestEast;
                break;
            default:
                this.orientation = Orientation.Undefined;
                break;
        }
    }

    @objid ("2a6f2707-55b6-11e2-877f-002564c97630")
    @Override
    protected void paintFigure(Graphics g) {
        super.paintFigure(g);
        
        Rectangle rect = this.getBounds();
        int x = rect.x;
        int y = rect.y;
        int w = rect.width;
        int h = rect.height;
        
        switch (this.orientation) {
            case NorthSouth: {
                Point p1 = new Point(x + w / 2, y + 2);
                Point p2 = new Point(x + w / 2, y + h - 4);
                g.drawLine(p1, p2);
                g.drawLine(p2, p2.getCopy().translate(w / 3, -h / 3));
                g.drawLine(p2, p2.getCopy().translate(-w / 3, -h / 3));
            }
                break;
            case SouthNorth: {
                Point p1 = new Point(x + w / 2, y + 2);
                Point p2 = new Point(x + w / 2, y + h - 4);
                g.drawLine(p1, p2);
                g.drawLine(p1, p1.getCopy().translate(w / 3, h / 3));
                g.drawLine(p1, p1.getCopy().translate(-w / 3, h / 3));
        
            }
                break;
            case EastWest: {
                Point p1 = new Point(x + 2, y + h / 2);
                Point p2 = new Point(x + w - 4, y + h / 2);
                g.drawLine(p1, p2);
                g.drawLine(p1, p1.getCopy().translate(w / 3, -h / 3));
                g.drawLine(p1, p1.getCopy().translate(w / 3, h / 3));
        
            }
                break;
            case WestEast: {
                Point p1 = new Point(x + 2, y + h / 2);
                Point p2 = new Point(x + w - 4, y + h / 2);
                g.drawLine(p1, p2);
                g.drawLine(p2, p2.getCopy().translate(-w / 3, -h / 3));
                g.drawLine(p2, p2.getCopy().translate(-w / 3, h / 3));
        
            }
                break;
            default:
                break;
        
        }
    }

}
