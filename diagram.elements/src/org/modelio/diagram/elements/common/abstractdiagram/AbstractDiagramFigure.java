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
                                    

package org.modelio.diagram.elements.common.abstractdiagram;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * Figure to represent a diagram.
 * 
 * @author phv
 */
@objid ("7e05eb34-1dec-11e2-8cad-001ec947c8cc")
public class AbstractDiagramFigure extends FreeformLayer {
    @objid ("7e084d39-1dec-11e2-8cad-001ec947c8cc")
    private boolean showPageBoundaries = false;

    @objid ("65d83c49-1e83-11e2-8cad-001ec947c8cc")
    private Dimension pageBoundaries = new Dimension(800, 600);

    @objid ("65d83c4a-1e83-11e2-8cad-001ec947c8cc")
    private Rectangle workarea;

    /**
     * Constructor
     */
    @objid ("7e084d40-1dec-11e2-8cad-001ec947c8cc")
    public AbstractDiagramFigure() {
        super();
        
        setBounds(new Rectangle(new Point(0, 0), this.pageBoundaries));
        this.setLayoutManager(new FreeformLayout());
        
        // the abstract diagram figure must be kept transparent in order 
        // for the grid and the background to be displayed
        this.setBackgroundColor(null);
        this.setOpaque(false);
    }

    @objid ("7e084d43-1dec-11e2-8cad-001ec947c8cc")
    @Override
    public Rectangle getFreeformExtent() {
        Rectangle r = super.getFreeformExtent();
        return r.union(this.workarea);
    }

    /**
     * Set page boundary dimensions.
     * @param size the page boundary dimensions.
     */
    @objid ("7e084d4a-1dec-11e2-8cad-001ec947c8cc")
    public void setPageBoundaries(Dimension size) {
        this.pageBoundaries = size;
        repaint();
    }

    /**
     * Set the working area bounds.
     * @param r The new work area bounds.
     */
    @objid ("7e084d50-1dec-11e2-8cad-001ec947c8cc")
    public void setWorkArea(Rectangle r) {
        if (r.equals(this.workarea))
            return;
        
        this.workarea = r;
        this.fireExtentChanged();
    }

    /**
     * Toggle display of page boundaries.
     * @param onOff true to display page boundaries, false to hide them.
     */
    @objid ("7e084d56-1dec-11e2-8cad-001ec947c8cc")
    public void showPageBoundaries(boolean onOff) {
        if (this.showPageBoundaries != onOff) {
            this.showPageBoundaries = onOff;
            repaint();
        }
    }

    @objid ("7e084d5a-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        if (this.showPageBoundaries && (this.pageBoundaries != null)) {
            graphics.setBackgroundColor(ColorConstants.blue);
            graphics.setLineStyle(SWT.LINE_DASH);
            Rectangle r = getBounds();
        
            if (this.pageBoundaries.width > 0) {
                int i = r.x;
                while (i < r.width) {
                    graphics.drawLine(i, r.y, i, r.y + r.height);
                    i += this.pageBoundaries.width;
                }
            }
        
            if (this.pageBoundaries.height > 0) {
                int j = r.y;
                while (j < r.height) {
                    graphics.drawLine(r.x, j, r.x + r.width, j);
                    j += this.pageBoundaries.height;
                }
            }
        
        }
    }

}
