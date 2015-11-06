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
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.swt.graphics.Color;
import org.modelio.diagram.elements.core.figures.ShapedFigure;
import org.modelio.diagram.elements.core.figures.borders.ShapedBorder;

@objid ("2a6f272f-55b6-11e2-877f-002564c97630")
public class SendArrowFigure extends ShapedFigure {
    @objid ("d1faf3ea-55c0-11e2-9337-002564c97630")
    private ShapedBorder shapedBorder;

    @objid ("2a6f2735-55b6-11e2-877f-002564c97630")
    public SendArrowFigure() {
        super();
        
        setShaper(new SendArrowShaper());
        this.shapedBorder = new ShapedBorder(this.penOptions.lineColor,
                                             this.penOptions.lineWidth,
                                             this.shaper);
        
        this.setBorder(new CompoundBorder(this.shapedBorder, new MarginBorder(2)));
        this.setOpaque(true);
    }

    @objid ("2a70ad9a-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineColor(Color lineColor) {
        if (lineColor != this.penOptions.lineColor) {
            super.setLineColor(lineColor);
            this.shapedBorder.setColor(lineColor);
        }
    }

    @objid ("2a70ad9e-55b6-11e2-877f-002564c97630")
    @Override
    public void setLineWidth(int lineWidth) {
        if (lineWidth != this.penOptions.lineWidth) {
            super.setLineWidth(lineWidth);
            this.shapedBorder.setWidth(lineWidth);
        }
    }

}
