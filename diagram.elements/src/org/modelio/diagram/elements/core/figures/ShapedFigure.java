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
                                    

package org.modelio.diagram.elements.core.figures;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.Graphics;

/**
 * A shaped figure filled with a gradient.
 * 
 * @author phv
 */
@objid ("7fcd4c10-1dec-11e2-8cad-001ec947c8cc")
public class ShapedFigure extends GradientFigure {
    @objid ("7fcd4c12-1dec-11e2-8cad-001ec947c8cc")
    protected IShaper shaper = null;

    /**
     * Default constructor.
     */
    @objid ("7fcd4c13-1dec-11e2-8cad-001ec947c8cc")
    public ShapedFigure() {
        super();
    }

    /**
     * Builds a ShapedFigure with a specific Shaper.
     * @param shaper A shaper.
     */
    @objid ("7fcd4c16-1dec-11e2-8cad-001ec947c8cc")
    public ShapedFigure(IShaper shaper) {
        super();
        this.shaper = shaper;
    }

    /**
     * Get the current shaper.
     * @return A shaper.
     */
    @objid ("7fcd4c1a-1dec-11e2-8cad-001ec947c8cc")
    public IShaper getShaper() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return this.shaper;
    }

    /**
     * Set the current shaper.
     * @param value The new shaper.
     */
    @objid ("7fcd4c1f-1dec-11e2-8cad-001ec947c8cc")
    public void setShaper(IShaper value) {
        // Automatically generated method. Please delete this comment before entering specific code.
        this.shaper = value;
    }

    @objid ("7fcd4c23-1dec-11e2-8cad-001ec947c8cc")
    @Override
    protected void paintFigure(Graphics graphics) {
        if (this.shaper != null) {
            graphics.clipPath(this.shaper.getShapePath(getBounds()));
        }
        super.paintFigure(graphics);
    }

}
