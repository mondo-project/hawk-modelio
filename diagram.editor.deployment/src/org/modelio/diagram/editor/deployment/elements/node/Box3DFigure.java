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
                                    

package org.modelio.diagram.editor.deployment.elements.node;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.swt.graphics.Color;
import org.modelio.diagram.elements.core.figures.ShapedFigure;
import org.modelio.diagram.elements.core.figures.borders.ShapedBorder;

/**
 * Draws a figure using a 3D box shape.
 * 
 * @author chm
 */
@objid ("97344e5a-55b6-11e2-877f-002564c97630")
public class Box3DFigure extends ShapedFigure {
    @objid ("1ccff243-55c2-11e2-9337-002564c97630")
    private ShapedBorder shapedBorder;

    /**
     * Initializes the figure with a BoxShaper, and adds the default border.
     */
    @objid ("9735d4fb-55b6-11e2-877f-002564c97630")
    public Box3DFigure() {
        super();
        
        setShaper(new Box3DShaper());
        this.setOpaque(true);
        this.shapedBorder = new ShapedBorder(this.penOptions.lineColor,
                                             this.penOptions.lineWidth,
                                             this.shaper);
        
        setBorder(new CompoundBorder(this.shapedBorder, new MarginBorder(2)));
    }

    @objid ("9735d4fe-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineColor(Color lineColor) {
        if (lineColor != this.penOptions.lineColor) {
            super.setLineColor(lineColor);
            this.shapedBorder.setColor(lineColor);
        }
    }

    @objid ("9735d502-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineWidth(int lineWidth) {
        if (lineWidth != this.penOptions.lineWidth) {
            super.setLineWidth(lineWidth);
            this.shapedBorder.setWidth(lineWidth);
        }
    }

    /**
     * Setter for the depth of the box.
     * @param value the new depth for the box.
     */
    @objid ("9735d506-55b6-11e2-877f-002564c97630")
    public void setDepth(int value) {
        ((Box3DShaper) this.shaper).setDepth(value);
    }

    /**
     * Getter for the depth of the box.
     * @return the current depth of the box.
     */
    @objid ("9735d50a-55b6-11e2-877f-002564c97630")
    public int getDepth() {
        return ((Box3DShaper) this.shaper).getDepth();
    }

}
