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
                                    

package org.modelio.core.ui.nattable.editors;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.BackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.style.CellStyleUtil;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Paints an image. If no image is provided, it will attempt to look up an image from the cell style.
 */
@objid ("b553a702-bd5b-4f35-8713-0cba55eeb38b")
public class ImagePainter extends BackgroundPainter {
    @objid ("132c70cc-a5f7-42ab-bc0c-46f296bc0f54")
    private final boolean paintBg;

    @objid ("2101c6f3-a718-4196-b081-1d0bfe30ccc4")
    public ImagePainter() {
        this.paintBg = true;
    }

    @objid ("e1b56993-40fd-4ff0-bc2b-c5ffafbae245")
    @Override
    public int getPreferredWidth(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        Image anImage = getImage(cell);
        if (anImage != null) {
            return anImage.getBounds().width;
        } else {
            return 0;
        }
    }

    @objid ("3b12558b-8531-4316-b395-6d93f22991b9")
    @Override
    public int getPreferredHeight(ILayerCell cell, GC gc, IConfigRegistry configRegistry) {
        Image anImage = getImage(cell);
        if (anImage != null) {
            return anImage.getBounds().height;
        } else {
            return 0;
        }
    }

    @objid ("1ec4c05e-2abc-4206-96de-ef2320adbffb")
    @Override
    public ICellPainter getCellPainterAt(int x, int y, ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        final Image image = getImage(cell);
        if (image == null) {
            return null;
        }
        Rectangle imageBounds = image.getBounds();
        IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
        int x0 = bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, imageBounds.width);
        int y0 = bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, imageBounds.height);
        if (    x >= x0 &&
                x < x0 + imageBounds.width &&
                y >= y0 &&
                y < y0 + imageBounds.height) {
            return super.getCellPainterAt(x, y, cell, gc, bounds, configRegistry);
        } else {
            return null;
        }
    }

    @objid ("80f5a94e-97c8-420d-9f7a-ac75208ab68e")
    @Override
    public void paintCell(ILayerCell cell, GC gc, Rectangle bounds, IConfigRegistry configRegistry) {
        if (this.paintBg) {
            super.paintCell(cell, gc, bounds, configRegistry);
        }
        
        Image anImage = getImage(cell);
        if (anImage != null) {
            Rectangle imageBounds = anImage.getBounds();
            IStyle cellStyle = CellStyleUtil.getCellStyle(cell, configRegistry);
            gc.drawImage(
                    anImage,
                    bounds.x + CellStyleUtil.getHorizontalAlignmentPadding(cellStyle, bounds, imageBounds.width),
                    bounds.y + CellStyleUtil.getVerticalAlignmentPadding(cellStyle, bounds, imageBounds.height));
        }
    }

    @objid ("5da9bc43-fd49-4054-9fd3-ef5f4137ef01")
    protected Image getImage(ILayerCell cell) {
        return (Image) cell.getDataValue();
    }

}
