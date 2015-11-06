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
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.widgets.Display;
import org.modelio.diagram.elements.core.figures.IShaper;

/**
 * Draws a 3D box shape.
 * 
 * @author chm
 */
@objid ("97375b99-55b6-11e2-877f-002564c97630")
public class Box3DShaper implements IShaper {
    @objid ("97375b9d-55b6-11e2-877f-002564c97630")
    private int depth;

    /**
     * Initializes a BoxShaper with a default depth of 12.
     */
    @objid ("97375b9e-55b6-11e2-877f-002564c97630")
    public Box3DShaper() {
        this.depth = 12;
    }

    @objid ("97375ba1-55b6-11e2-877f-002564c97630")
    @Override
    public Insets getShapeInsets(Rectangle rect) {
        return new Insets(this.depth, 0, 0, this.depth);
    }

    @objid ("97375ba7-55b6-11e2-877f-002564c97630")
    @Override
    public Path getShapePath(Rectangle rect) {
        float x = rect.x;
        float y = rect.y;
        float w = rect.width - 1;
        float h = rect.height - 1;
        
        Path path = new Path(Display.getCurrent());
        
        // Add the main rectangle
        path.addRectangle(x, y + this.depth, w - this.depth, h - this.depth);
        
        // Add top parallelogram
        path.moveTo(x + w - this.depth, y + this.depth);
        path.lineTo(x, y + this.depth);
        path.lineTo(x + this.depth, y);
        path.lineTo(x + w, y);
        path.lineTo(x + w - this.depth, y + this.depth);
        
        // Add right parallelogram
        path.lineTo(x + w - this.depth, y + h);
        path.lineTo(x + w, y + h - this.depth);
        path.lineTo(x + w, y);
        return path;
    }

    /**
     * Setter for the depth of the box.
     * @param value the new depth for the box.
     */
    @objid ("97375bad-55b6-11e2-877f-002564c97630")
    public void setDepth(int value) {
        this.depth = value;
    }

    /**
     * Getter for the depth of the box.
     * @return the current depth of the box.
     */
    @objid ("97375bb1-55b6-11e2-877f-002564c97630")
    public int getDepth() {
        return this.depth;
    }

}
