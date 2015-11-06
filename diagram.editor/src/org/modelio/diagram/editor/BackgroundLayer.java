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
                                    

package org.modelio.diagram.editor;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Additionnal layer for Modelio diagram providing a background image
 */
@objid ("657b423c-33f7-11e2-95fe-001ec947c8cc")
class BackgroundLayer extends FreeformLayer {
    @objid ("657b423e-33f7-11e2-95fe-001ec947c8cc")
    private int alpha = 255;

    @objid ("658006bc-33f7-11e2-95fe-001ec947c8cc")
    private Image bgImage = null;

    @objid ("65826916-33f7-11e2-95fe-001ec947c8cc")
    private ImageDescriptor bgDescriptor = null;

    @objid ("65826917-33f7-11e2-95fe-001ec947c8cc")
    private Dimension tileSize = null;

    @objid ("65826918-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.setAlpha(this.alpha);
        super.paintFigure(graphics);
        
        if (this.bgImage != null) {
            if (this.tileSize == null || ((this.tileSize != null) && (this.tileSize.isEmpty())))
                // no significative tile size => use the full size of the layer (no tiling)
                drawBackgroundTiles(graphics, this.bgImage, this.getBounds().getSize());
            else
                //  a significative tile size is defined => use tiling for the image
                drawBackgroundTiles(graphics, this.bgImage, this.tileSize);
        }
    }

    @objid ("6582691c-33f7-11e2-95fe-001ec947c8cc")
    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    /**
     * Set the background image. Use 'null' to set no image. Allocation/disposal of the image is controlled by the
     * BackgroundLayer class. If an invalid descriptor or null is passed, or if the image cannot be created, the
     * background is set to no image.
     * @param id
     */
    @objid ("6582691f-33f7-11e2-95fe-001ec947c8cc")
    public void setImage(ImageDescriptor id) {
        // Null descriptor => clear background image
        if (id == null) {
            removeBgImage();
            return;
        }
        
        // Attempt to set the same image => return, do nothing
        if (id.equals(this.bgDescriptor)) {
            return;
        }
        
        // Setting a new image
        removeBgImage();
        Image newImage = id.createImage();
        if (newImage != null) {
            setImage(id, newImage);
        }
    }

    @objid ("65826923-33f7-11e2-95fe-001ec947c8cc")
    private void setImage(ImageDescriptor id, Image newImage) {
        this.bgDescriptor = id;
        this.bgImage = newImage;
    }

    @objid ("65826927-33f7-11e2-95fe-001ec947c8cc")
    private void removeBgImage() {
        if (this.bgImage != null) {
            this.bgImage.dispose();
            this.bgImage = null;
            this.bgDescriptor = null;
        }
    }

    @objid ("65826929-33f7-11e2-95fe-001ec947c8cc")
    @Override
    protected void finalize() throws Throwable {
        removeBgImage();
        super.finalize();
    }

    @objid ("6582692c-33f7-11e2-95fe-001ec947c8cc")
    private void drawBackgroundTiles(Graphics graphics, Image img, Dimension tileSize) {
        Rectangle r = getBounds();
        int x = r.x;
        int y = r.y;
        int xmax = r.x + r.width;
        int ymax = r.y + r.height;
        
        y = r.y;
        while (y < ymax) {
            x = r.x;
            while (x < xmax) {
                graphics.drawImage(img,
                                   0,
                                   0,
                                   img.getBounds().width,
                                   img.getBounds().height,
                                   x,
                                   y,
                                   tileSize.width,
                                   tileSize.height);
                x += tileSize.width;
            }
            y += tileSize.height;
        }
    }

    /**
     * Set the page size (width, height) in pixels. The caller is responsible for converting physical dimensions (mm or
     * ") into pixels taking care of the Display getDPI() conversion factor The Page will be used to tile the background
     * figure when there is one
     * @param size
     */
    @objid ("65826931-33f7-11e2-95fe-001ec947c8cc")
    public void setTileSize(Dimension size) {
        this.tileSize = size;
    }

}
